/**
 * SpecimenImageAnalyzer.java
 * edu.harvard.mcz.imagecapture.scanning
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.scanning;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ocr.OcrEngine;
import edu.harvard.mcz.imagecapture.ocr.OcrResult;
import edu.harvard.mcz.imagecapture.ocr.TesseractCliOcrEngine;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates single-pass metadata extraction, single image decode, scan-once
 * template resolution, catalog and unit-tray QR extraction, and gated OCR
 * fallback for a specimen image.
 */
public class SpecimenImageAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(SpecimenImageAnalyzer.class);

	private final BarcodeScannerChain scannerChain;
	private final TemplateResolver templateResolver;
	private final BarcodeMatcher barcodeMatcher;
	private final OcrEngine ocrEngine;
	private final boolean ocrEnabled;
	private final ScanOptions baseScanOptions;

	/**
	 * Creates an analyzer with explicit dependencies.
	 *
	 * @param scannerChain
	 *            barcode scanner chain
	 * @param templateResolver
	 *            template resolver
	 * @param barcodeMatcher
	 *            catalog barcode matcher (nullable)
	 * @param ocrEngine
	 *            OCR engine used when {@code ocrEnabled} is {@code true} (nullable)
	 * @param ocrEnabled
	 *            whether OCR fallback is enabled
	 */
	public SpecimenImageAnalyzer(BarcodeScannerChain scannerChain, TemplateResolver templateResolver,
			BarcodeMatcher barcodeMatcher, OcrEngine ocrEngine, boolean ocrEnabled) {
		this(scannerChain, templateResolver, barcodeMatcher, ocrEngine, ocrEnabled, ScanOptions.defaults());
	}

	/**
	 * Creates an analyzer with explicit dependencies and base scan options.
	 *
	 * @param scannerChain
	 *            barcode scanner chain
	 * @param templateResolver
	 *            template resolver
	 * @param barcodeMatcher
	 *            catalog barcode matcher (nullable)
	 * @param ocrEngine
	 *            OCR engine (nullable)
	 * @param ocrEnabled
	 *            whether OCR fallback is enabled
	 * @param baseScanOptions
	 *            base scan options
	 */
	public SpecimenImageAnalyzer(BarcodeScannerChain scannerChain, TemplateResolver templateResolver,
			BarcodeMatcher barcodeMatcher, OcrEngine ocrEngine, boolean ocrEnabled, ScanOptions baseScanOptions) {
		this.scannerChain = Objects.requireNonNull(scannerChain, "scannerChain must not be null");
		this.templateResolver = Objects.requireNonNull(templateResolver, "templateResolver must not be null");
		this.barcodeMatcher = barcodeMatcher;
		this.ocrEngine = ocrEngine;
		this.ocrEnabled = ocrEnabled;
		this.baseScanOptions = baseScanOptions == null ? ScanOptions.defaults() : baseScanOptions;
	}

	/**
	 * Builds a {@link SpecimenImageAnalyzer} wired from {@link Singleton}
	 * configuration.
	 *
	 * @return configured analyzer
	 */
	public static SpecimenImageAnalyzer fromSingleton() {
		Properties properties;
		BarcodeMatcher matcher = null;
		try {
			properties = Singleton.getSingletonInstance().getProperties().getProperties();
			matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
		} catch (Exception e) {
			properties = new Properties();
		}
		boolean ocrEnabled = Boolean
				.parseBoolean(properties.getProperty(ImageCaptureProperties.KEY_OCR_ENABLED, "false").trim());
		BarcodeScannerChain chain = BarcodeScannerChain.defaultChain(properties);
		String defaultTemplate = properties.getProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES);
		TemplateResolver resolver = new TemplateResolver(new DefaultPositionTemplateRepository(), matcher, chain,
				defaultTemplate);
		return new SpecimenImageAnalyzer(chain, resolver, matcher, new TesseractCliOcrEngine(), ocrEnabled,
				ScanOptions.fromProperties(properties));
	}

	/**
	 * Analyzes an image at the given {@link Path}.
	 *
	 * @param path
	 *            image path
	 * @return immutable {@link ImageAnalysisResult}
	 * @throws UnreadableFileException
	 *             if the file does not exist or cannot be read
	 */
	public ImageAnalysisResult analyze(Path path) throws UnreadableFileException {
		return analyze(ImageDocument.of(path));
	}

	/**
	 * Analyzes an {@link ImageDocument} using single-pass metadata and single image
	 * decode.
	 *
	 * @param document
	 *            image document handle
	 * @return immutable {@link ImageAnalysisResult}
	 * @throws UnreadableFileException
	 *             if the document cannot be read
	 */
	public ImageAnalysisResult analyze(ImageDocument document) throws UnreadableFileException {
		Objects.requireNonNull(document, "document must not be null");
		document.ensureReadable();

		ImageMetadata metadata = document.getMetadata();
		List<String> diagnostics = new ArrayList<>();

		BufferedImage image;
		try {
			image = document.getImage();
		} catch (IOException e) {
			diagnostics.add("Failed to decode image: " + e.getMessage());
			return ImageAnalysisResult.builder().template(noComponentPartsTemplate())
					.catalogBarcodeStatus(ImageAnalysisResult.STATUS_ERROR)
					.unitTrayLabelStatus(ImageAnalysisResult.STATUS_ERROR).metadata(metadata).diagnostics(diagnostics)
					.build();
		}

		if (image == null) {
			diagnostics.add("Could not decode image (null BufferedImage).");
			return ImageAnalysisResult.builder().template(noComponentPartsTemplate())
					.catalogBarcodeStatus(ImageAnalysisResult.STATUS_ERROR)
					.unitTrayLabelStatus(ImageAnalysisResult.STATUS_ERROR).metadata(metadata).diagnostics(diagnostics)
					.build();
		}

		// 1. Scan-once and template resolution
		List<BarcodeDetection> pooledDetections = new ArrayList<>();
		PositionTemplate template;
		if (templateResolver.hasExplicitDefaultTemplate()) {
			template = templateResolver.resolveTemplate(image, List.of(), true);
		} else {
			pooledDetections.addAll(scannerChain.scanAll(image, baseScanOptions));
			template = templateResolver.resolveTemplate(image, pooledDetections, true);
			if (PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(template.getTemplateId())) {
				template = templateResolver.resolveTemplate(image, pooledDetections, false);
			}
		}

		boolean wholeImageOnly = PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(template.getTemplateId());
		if (wholeImageOnly && pooledDetections.isEmpty()) {
			pooledDetections.addAll(scannerChain.scanAll(image, baseScanOptions));
		}

		// 2. Catalog barcode extraction
		String catalogBarcode = null;
		String catalogEngine = "";
		int catalogStatus = ImageAnalysisResult.STATUS_ERROR;

		if (wholeImageOnly) {
			Optional<BarcodeDetection> catalogMatch = selectCatalogDetection(pooledDetections, null);
			if (catalogMatch.isPresent()) {
				catalogBarcode = catalogMatch.get().text();
				catalogEngine = catalogMatch.get().engine();
				catalogStatus = ImageAnalysisResult.STATUS_BARCODE_SCANNED;
			}
		} else if (matchesTemplateWidth(template, image) && template.getBarcodeULPosition() != null
				&& template.getBarcodeSize() != null && image.getWidth() > template.getBarcodeULPosition().width) {
			Rectangle catalogRegion = new Rectangle(template.getBarcodeULPosition().width,
					template.getBarcodeULPosition().height, template.getBarcodeSize().width,
					template.getBarcodeSize().height);
			Optional<BarcodeDetection> catalogMatch = selectCatalogDetection(pooledDetections, catalogRegion);
			if (catalogMatch.isEmpty()) {
				List<BarcodeDetection> regionDetections = scannerChain.scanRegion(image, catalogRegion,
						baseScanOptions);
				pooledDetections.addAll(regionDetections);
				catalogMatch = selectCatalogDetection(regionDetections, catalogRegion);
			}
			if (catalogMatch.isPresent()) {
				catalogBarcode = catalogMatch.get().text();
				catalogEngine = catalogMatch.get().engine();
				catalogStatus = ImageAnalysisResult.STATUS_BARCODE_SCANNED;
			}
		} else {
			diagnostics.add("Image width " + image.getWidth() + " does not match template catalog region.");
		}

		// 3. UnitTrayLabel QR extraction
		UnitTrayLabel unitTrayLabel = null;
		String rawLabelText = null;
		String unitTrayEngine = "";
		int unitTrayStatus = ImageAnalysisResult.STATUS_ERROR;

		if (wholeImageOnly) {
			Optional<UnitTrayMatch> utMatch = selectUnitTrayDetection(pooledDetections, null);
			if (utMatch.isPresent()) {
				unitTrayLabel = utMatch.get().label();
				rawLabelText = utMatch.get().detection().text();
				unitTrayEngine = utMatch.get().detection().engine();
				unitTrayStatus = ImageAnalysisResult.STATUS_BARCODE_SCANNED;
			}
		} else if (matchesTemplateWidth(template, image) && template.getUtBarcodePosition() != null
				&& template.getUtBarcodeSize() != null && image.getWidth() >= template.getUtBarcodePosition().width) {
			Rectangle utRegion = new Rectangle(template.getUtBarcodePosition().width,
					template.getUtBarcodePosition().height, template.getUtBarcodeSize().width,
					template.getUtBarcodeSize().height);
			Optional<UnitTrayMatch> utMatch = selectUnitTrayDetection(pooledDetections, utRegion);
			if (utMatch.isEmpty()) {
				List<BarcodeDetection> utDetections = scannerChain.scanRegion(image, utRegion, baseScanOptions);
				pooledDetections.addAll(utDetections);
				utMatch = selectUnitTrayDetection(utDetections, utRegion);
			}
			if (utMatch.isPresent()) {
				unitTrayLabel = utMatch.get().label();
				rawLabelText = utMatch.get().detection().text();
				unitTrayEngine = utMatch.get().detection().engine();
				unitTrayStatus = ImageAnalysisResult.STATUS_BARCODE_SCANNED;
			}
		}

		// 4. Gated OCR fallback when UnitTrayLabel QR is missing
		String ocrText = null;
		if (ocrEnabled && unitTrayLabel == null && !wholeImageOnly && template.getTextPosition() != null
				&& template.getTextSize() != null) {
			ocrText = tryOcrFallback(image, template, diagnostics);
		}

		return ImageAnalysisResult.builder().template(template).catalogBarcode(catalogBarcode)
				.catalogBarcodeStatus(catalogStatus).catalogBarcodeEngine(catalogEngine).unitTrayLabel(unitTrayLabel)
				.rawLabelText(rawLabelText).unitTrayLabelStatus(unitTrayStatus).unitTrayLabelEngine(unitTrayEngine)
				.metadata(metadata).ocrText(ocrText).diagnostics(diagnostics).build();
	}

	private String tryOcrFallback(BufferedImage image, PositionTemplate template, List<String> diagnostics) {
		if (ocrEngine == null) {
			diagnostics.add("OCR enabled but no OcrEngine configured.");
			return null;
		}
		if (!ocrEngine.isAvailable()) {
			diagnostics.add("OCR enabled but OCR engine is not available.");
			return null;
		}

		int x = template.getTextPosition().width;
		int y = template.getTextPosition().height;
		int w = template.getTextSize().width;
		int h = template.getTextSize().height;

		Optional<BufferedImage> cropOpt = ImagePreprocessor.copySubimage(image, new Rectangle(x, y, w, h));
		if (cropOpt.isEmpty()) {
			diagnostics.add("OCR text region (" + x + "," + y + "," + w + "x" + h + ") lies outside image bounds "
					+ image.getWidth() + "x" + image.getHeight() + ".");
			return null;
		}

		OcrResult first = ocrEngine.recognize(cropOpt.get(), TesseractCliOcrEngine.DEFAULT_LANGUAGE);
		if (first.isSuccess() && !first.getText().isBlank()) {
			return first.getText();
		}

		Optional<BufferedImage> shiftedOpt = ImagePreprocessor.copySubimage(image,
				new Rectangle(x + 1, y + 1, w + 1, h + 1));
		if (shiftedOpt.isPresent()) {
			OcrResult second = ocrEngine.recognize(shiftedOpt.get(), TesseractCliOcrEngine.DEFAULT_LANGUAGE);
			if (second.isSuccess() && !second.getText().isBlank()) {
				return second.getText();
			}
			if (!second.isSuccess()) {
				diagnostics.add("OCR fallback failed: " + second.getErrorMessage());
			}
		} else if (!first.isSuccess()) {
			diagnostics.add("OCR fallback failed: " + first.getErrorMessage());
		}

		return first.isSuccess() ? first.getText() : null;
	}

	private Optional<BarcodeDetection> selectCatalogDetection(List<BarcodeDetection> detections,
			Rectangle preferredRegion) {
		if (detections == null || detections.isEmpty()) {
			return Optional.empty();
		}
		List<BarcodeDetection> ordered = sortByRegionOverlap(detections, preferredRegion);
		for (BarcodeDetection detection : ordered) {
			if (matchesCatalogPattern(detection.text())
					&& (preferredRegion == null || overlapsOrNoBounds(detection, preferredRegion))) {
				return Optional.of(detection);
			}
		}
		for (BarcodeDetection detection : ordered) {
			if (!looksLikeJsonObject(detection.text())
					&& (preferredRegion == null || overlapsOrNoBounds(detection, preferredRegion))) {
				return Optional.of(detection);
			}
		}
		return Optional.empty();
	}

	private Optional<UnitTrayMatch> selectUnitTrayDetection(List<BarcodeDetection> detections,
			Rectangle preferredRegion) {
		if (detections == null || detections.isEmpty()) {
			return Optional.empty();
		}
		List<BarcodeDetection> ordered = sortByRegionOverlap(detections, preferredRegion);
		for (BarcodeDetection detection : ordered) {
			if (!looksLikeJsonObject(detection.text())) {
				continue;
			}
			if (preferredRegion != null && !overlapsOrNoBounds(detection, preferredRegion)) {
				continue;
			}
			try {
				UnitTrayLabel parsed = UnitTrayLabel.createFromJSONString(detection.text());
				if (parsed != null) {
					return Optional.of(new UnitTrayMatch(detection, parsed));
				}
			} catch (Exception e) {
				log.debug("Failed parsing UnitTrayLabel JSON: {}", e.getMessage());
			}
		}
		return Optional.empty();
	}

	private List<BarcodeDetection> sortByRegionOverlap(List<BarcodeDetection> detections, Rectangle preferredRegion) {
		if (preferredRegion == null) {
			return detections;
		}
		List<BarcodeDetection> sorted = new ArrayList<>(detections);
		sorted.sort(Comparator
				.<BarcodeDetection>comparingDouble(d -> overlapScore(preferredRegion, d.bounds().orElse(null)))
				.reversed());
		return sorted;
	}

	private boolean overlapsOrNoBounds(BarcodeDetection detection, Rectangle preferredRegion) {
		return detection.bounds().map(b -> b.intersects(preferredRegion)).orElse(true);
	}

	private double overlapScore(Rectangle preferredRegion, Rectangle candidate) {
		if (preferredRegion == null || candidate == null || candidate.width <= 0 || candidate.height <= 0) {
			return -1d;
		}
		Rectangle overlap = preferredRegion.intersection(candidate);
		if (overlap.width <= 0 || overlap.height <= 0) {
			return 0d;
		}
		double overlapArea = (double) overlap.width * (double) overlap.height;
		double candidateArea = (double) candidate.width * (double) candidate.height;
		return overlapArea / candidateArea;
	}

	private boolean matchesCatalogPattern(String value) {
		if (value == null || value.isBlank() || barcodeMatcher == null) {
			return false;
		}
		try {
			return barcodeMatcher.matchesPattern(value);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean matchesTemplateWidth(PositionTemplate template, BufferedImage image) {
		return template != null && template.getImageSize() != null
				&& image.getWidth() == Math.round(template.getImageSize().getWidth());
	}

	private boolean looksLikeJsonObject(String value) {
		if (value == null) {
			return false;
		}
		String trimmed = value.trim();
		return trimmed.startsWith("{") && trimmed.endsWith("}");
	}

	private PositionTemplate noComponentPartsTemplate() {
		try {
			return new PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS);
		} catch (NoSuchTemplateException e) {
			throw new IllegalStateException(
					"Missing built-in template: " + PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, e);
		}
	}

	private record UnitTrayMatch(BarcodeDetection detection, UnitTrayLabel label) {
	}
}
