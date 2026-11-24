/**
 * TemplateResolver.java
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
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the {@link PositionTemplate} for a specimen image using configured
 * defaults, scan-once bounding-box matching, and per-template region fallback.
 */
public class TemplateResolver {

	private static final Logger log = LoggerFactory.getLogger(TemplateResolver.class);

	private final PositionTemplateRepository templateRepository;
	private final BarcodeMatcher barcodeMatcher;
	private final BarcodeScannerChain scannerChain;
	private final String configuredDefaultTemplate;

	/**
	 * Creates a {@link TemplateResolver} with explicit collaborators.
	 *
	 * @param templateRepository
	 *            repository providing candidate templates
	 * @param barcodeMatcher
	 *            matcher validating catalog barcode patterns (nullable)
	 * @param scannerChain
	 *            scanner chain used for full-image and region scans
	 * @param configuredDefaultTemplate
	 *            optional configured default template id
	 */
	public TemplateResolver(PositionTemplateRepository templateRepository, BarcodeMatcher barcodeMatcher,
			BarcodeScannerChain scannerChain, String configuredDefaultTemplate) {
		this.templateRepository = Objects.requireNonNull(templateRepository, "templateRepository must not be null");
		this.barcodeMatcher = barcodeMatcher;
		this.scannerChain = Objects.requireNonNull(scannerChain, "scannerChain must not be null");
		this.configuredDefaultTemplate = configuredDefaultTemplate == null ? null : configuredDefaultTemplate.trim();
	}

	/**
	 * Creates a {@link TemplateResolver} backed by {@link Singleton} configuration.
	 *
	 * @return configured template resolver
	 */
	public static TemplateResolver fromSingleton() {
		String configuredDefault = null;
		BarcodeMatcher matcher = null;
		try {
			configuredDefault = Singleton.getSingletonInstance().getProperties().getProperties()
					.getProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES);
			matcher = Singleton.getSingletonInstance().getBarcodeMatcher();
		} catch (Exception e) {
			log.debug("Unable to read Singleton template configuration: {}", e.getMessage());
		}
		return new TemplateResolver(new DefaultPositionTemplateRepository(), matcher,
				BarcodeScannerChain.fromSingleton(), configuredDefault);
	}

	/**
	 * @return {@code true} if an explicit, non-default template is configured and
	 *         resolves in the repository
	 */
	public boolean hasExplicitDefaultTemplate() {
		return resolveConfiguredDefault().isPresent();
	}

	/**
	 * Resolves the template for {@code document}, verifying readability first.
	 *
	 * @param document
	 *            image document
	 * @param quickCheck
	 *            whether region fallback should use quick-check mode
	 * @return resolved {@link PositionTemplate} (defaults to
	 *         {@link PositionTemplate#TEMPLATE_NO_COMPONENT_PARTS})
	 * @throws UnreadableFileException
	 *             if the document cannot be read or decoded
	 */
	public PositionTemplate resolveTemplate(ImageDocument document, boolean quickCheck) throws UnreadableFileException {
		Objects.requireNonNull(document, "document must not be null");
		document.ensureReadable();

		Optional<PositionTemplate> explicitDefault = resolveConfiguredDefault();
		if (explicitDefault.isPresent()) {
			return explicitDefault.get();
		}

		BufferedImage image;
		try {
			image = document.getImage();
		} catch (IOException e) {
			throw new UnreadableFileException("IOException trying to read "
					+ (document.getPath() != null ? document.getPath().getFileName() : "image") + ": "
					+ e.getMessage());
		}
		return resolveTemplate(image, null, quickCheck);
	}

	/**
	 * Resolves the template for an already decoded {@code image}, optionally
	 * reusing pre-scanned full-image detections to avoid redundant passes.
	 *
	 * @param image
	 *            decoded image (nullable)
	 * @param preScannedDetections
	 *            optional detections from a prior
	 *            {@link BarcodeScannerChain#scanAll} call, or {@code null} to run a
	 *            scan-once pass when needed
	 * @param quickCheck
	 *            whether region fallback should use quick-check mode
	 * @return resolved {@link PositionTemplate}
	 */
	public PositionTemplate resolveTemplate(BufferedImage image, List<BarcodeDetection> preScannedDetections,
			boolean quickCheck) {
		Optional<PositionTemplate> explicitDefault = resolveConfiguredDefault();
		if (explicitDefault.isPresent()) {
			return explicitDefault.get();
		}
		if (image == null) {
			return noComponentPartsTemplate();
		}

		List<PositionTemplate> candidates = templateRepository.findAll();
		if (candidates.isEmpty()) {
			return noComponentPartsTemplate();
		}

		List<BarcodeDetection> fullDetections = preScannedDetections != null
				? preScannedDetections
				: scannerChain.scanAll(image, ScanOptions.builder().quickCheck(quickCheck).build());

		// 1. Scan-once-then-match using bounding boxes when available
		for (PositionTemplate template : candidates) {
			if (matchesTemplateFromDetections(template, image, fullDetections)) {
				log.debug("Matched template '{}' via full-image detection bounds", template.getTemplateId());
				return template;
			}
		}

		// 2. Fallback to per-template region check (respecting quickCheck)
		ScanOptions regionOptions = ScanOptions.defaults().toBuilder().quickCheck(quickCheck).build();
		for (PositionTemplate template : candidates) {
			if (!isComponentTemplateMatchingWidth(template, image)) {
				continue;
			}
			Rectangle barcodeBox = catalogBarcodeRegion(template);
			if (barcodeBox == null) {
				continue;
			}
			List<BarcodeDetection> regionDetections = scannerChain.scanRegion(image, barcodeBox, regionOptions);
			for (BarcodeDetection detection : regionDetections) {
				if (matchesCatalogPattern(detection.text())) {
					log.debug("Matched template '{}' via region scan", template.getTemplateId());
					return template;
				}
			}
		}

		return noComponentPartsTemplate();
	}

	private Optional<PositionTemplate> resolveConfiguredDefault() {
		if (configuredDefaultTemplate == null || configuredDefaultTemplate.isBlank()
				|| PositionTemplate.TEMPLATE_DEFAULT.equals(configuredDefaultTemplate)
				|| PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(configuredDefaultTemplate)) {
			return Optional.empty();
		}
		return templateRepository.findById(configuredDefaultTemplate);
	}

	private boolean matchesTemplateFromDetections(PositionTemplate template, BufferedImage image,
			List<BarcodeDetection> detections) {
		if (detections == null || detections.isEmpty() || !isComponentTemplateMatchingWidth(template, image)) {
			return false;
		}
		Rectangle barcodeBox = catalogBarcodeRegion(template);
		if (barcodeBox == null) {
			return false;
		}
		for (BarcodeDetection detection : detections) {
			if (detection.bounds().isEmpty() || !matchesCatalogPattern(detection.text())) {
				continue;
			}
			if (detection.bounds().get().intersects(barcodeBox)) {
				return true;
			}
		}
		return false;
	}

	private boolean isComponentTemplateMatchingWidth(PositionTemplate template, BufferedImage image) {
		if (template == null || PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(template.getTemplateId())) {
			return false;
		}
		if (template.getImageSize() == null || template.getBarcodeULPosition() == null
				|| template.getBarcodeSize() == null) {
			return false;
		}
		return image.getWidth() == Math.round(template.getImageSize().getWidth());
	}

	private Rectangle catalogBarcodeRegion(PositionTemplate template) {
		if (template.getBarcodeULPosition() == null || template.getBarcodeSize() == null) {
			return null;
		}
		return new Rectangle(template.getBarcodeULPosition().width, template.getBarcodeULPosition().height,
				template.getBarcodeSize().width, template.getBarcodeSize().height);
	}

	private boolean matchesCatalogPattern(String text) {
		if (text == null || text.isBlank()) {
			return false;
		}
		if (barcodeMatcher == null) {
			return true;
		}
		try {
			return barcodeMatcher.matchesPattern(text);
		} catch (Exception e) {
			return false;
		}
	}

	private PositionTemplate noComponentPartsTemplate() {
		Optional<PositionTemplate> fromRepo = templateRepository.findById(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS);
		if (fromRepo.isPresent()) {
			return fromRepo.get();
		}
		try {
			return new PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS);
		} catch (NoSuchTemplateException e) {
			throw new IllegalStateException(
					"Missing built-in template: " + PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, e);
		}
	}
}
