/**
 * ZxingBarcodeScanner.java
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

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BarcodeScanner} backed by ZXing with non-mutating
 * {@link ImagePreprocessor} retry stages, strict region bounds validation, and
 * no side-effect working-directory debug image writes.
 */
public class ZxingBarcodeScanner implements BarcodeScanner {

	public static final double INITIAL_SCALING_WIDTH = 800d;
	private static final Logger log = LoggerFactory.getLogger(ZxingBarcodeScanner.class);
	private static final float[] BRIGHTNESS_SCALE_FACTORS = {1.2f, 0.8f, 0.6f, 1.4f, 1.6f};
	private static final float[] BRIGHTNESS_OFFSETS = {15f, -15f, -30f, 30f, 45f};

	@Override
	public String id() {
		return "zxing";
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public List<BarcodeDetection> detect(BufferedImage image, ScanOptions options) {
		if (image == null) {
			return List.of();
		}
		ScanOptions effective = options == null ? ScanOptions.defaults() : options;
		Optional<Rectangle> preferred = effective.preferredRegion();
		if (preferred.isPresent()) {
			return detectInRegion(image, preferred.get(), effective);
		}
		return detectFullImage(image, effective.zxingTryHarder());
	}

	private List<BarcodeDetection> detectFullImage(BufferedImage image, boolean tryHarder) {
		BinaryBitmap bitmap = toBitmap(image);
		if (bitmap == null) {
			return List.of();
		}

		List<BarcodeDetection> multiResults = decodeMultiple(bitmap, false);
		if (!multiResults.isEmpty()) {
			return multiResults;
		}
		if (tryHarder) {
			multiResults = decodeMultiple(bitmap, true);
			if (!multiResults.isEmpty()) {
				return multiResults;
			}
		}

		Optional<BarcodeDetection> single = decodeSingle(image, null, tryHarder);
		return single.map(List::of).orElseGet(List::of);
	}

	private List<BarcodeDetection> detectInRegion(BufferedImage image, Rectangle region, ScanOptions options) {
		Optional<BufferedImage> directCropOpt = ImagePreprocessor.copySubimage(image, region);
		if (directCropOpt.isEmpty()) {
			log.debug("Requested barcode region {} lies outside image bounds {}x{}", region, image.getWidth(),
					image.getHeight());
			return List.of();
		}
		BufferedImage crop = directCropOpt.get();

		// Stage 1: Direct crop
		Optional<BarcodeDetection> direct = decodeSingle(crop, region, options.zxingTryHarder());
		if (direct.isPresent()) {
			return List.of(direct.get());
		}

		if (options.quickCheck()) {
			// Fixed quickCheck displacement fallback (was unreachable when comparing text
			// == null)
			return tryDisplacedRegions(image, region, options.zxingTryHarder());
		}

		// Stage 2: Scale crop to 800px width
		if (crop.getWidth() != Math.round(INITIAL_SCALING_WIDTH)) {
			BufferedImage scaled = ImagePreprocessor.scaleToWidth(crop, INITIAL_SCALING_WIDTH);
			Optional<BarcodeDetection> scaledResult = decodeSingle(scaled, region, options.zxingTryHarder());
			if (scaledResult.isPresent()) {
				return List.of(scaledResult.get());
			}
		}

		// Stage 3: Configured rescale/filter specs
		for (String spec : options.rescaleTransforms()) {
			if (isRedundantTransformSpec(spec, region.width)) {
				continue;
			}
			Optional<BufferedImage> transformed = ImagePreprocessor.applyTransformSpec(crop, spec,
					INITIAL_SCALING_WIDTH);
			if (transformed.isPresent()) {
				Optional<BarcodeDetection> result = decodeSingle(transformed.get(), region, options.zxingTryHarder());
				if (result.isPresent()) {
					return List.of(result.get());
				}
			}
		}

		// Stage 4: Sharpened crop
		try {
			BufferedImage sharpened = ImagePreprocessor.sharpen(crop);
			Optional<BarcodeDetection> sharpenResult = decodeSingle(sharpened, region, options.zxingTryHarder());
			if (sharpenResult.isPresent()) {
				return List.of(sharpenResult.get());
			}
		} catch (IllegalArgumentException e) {
			log.debug("Sharpen transform failed: {}", e.getMessage());
		}

		// Stage 5: Non-mutating brightness/contrast grid
		for (float scaleFactor : BRIGHTNESS_SCALE_FACTORS) {
			for (float offset : BRIGHTNESS_OFFSETS) {
				try {
					BufferedImage adjusted = ImagePreprocessor.adjustBrightnessContrast(crop, scaleFactor, offset);
					Optional<BarcodeDetection> adjustedResult = decodeSingle(adjusted, region,
							options.zxingTryHarder());
					if (adjustedResult.isPresent()) {
						return List.of(adjustedResult.get());
					}
				} catch (IllegalArgumentException e) {
					log.debug("Brightness/contrast adjustment failed: {}", e.getMessage());
				}
			}
		}

		// Stage 6: Small bounded region displacements
		return tryDisplacedRegions(image, region, options.zxingTryHarder());
	}

	private List<BarcodeDetection> tryDisplacedRegions(BufferedImage image, Rectangle region, boolean tryHarder) {
		for (Rectangle displaced : ImagePreprocessor.displacedRegions(region, image.getWidth(), image.getHeight())) {
			Optional<BufferedImage> displacedCrop = ImagePreprocessor.copySubimage(image, displaced);
			if (displacedCrop.isPresent()) {
				Optional<BarcodeDetection> match = decodeSingle(displacedCrop.get(), displaced, tryHarder);
				if (match.isPresent()) {
					return List.of(match.get());
				}
			}
		}
		return List.of();
	}

	private boolean isRedundantTransformSpec(String spec, int originalWidth) {
		if (spec == null || spec.isBlank()) {
			return true;
		}
		String trimmed = spec.trim();
		if (!trimmed.matches("[0-9.]+")) {
			return false;
		}
		try {
			double width = Double.parseDouble(trimmed);
			return Math.round(width) == Math.round(INITIAL_SCALING_WIDTH) || Math.round(width) == originalWidth;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private List<BarcodeDetection> decodeMultiple(BinaryBitmap bitmap, boolean tryHarder) {
		QRCodeMultiReader multiReader = new QRCodeMultiReader();
		try {
			Result[] results = multiReader.decodeMultiple(bitmap, buildHints(tryHarder));
			if (results == null || results.length == 0) {
				return List.of();
			}
			List<BarcodeDetection> detections = new ArrayList<>(results.length);
			for (Result result : results) {
				if (result != null && result.getText() != null && !result.getText().isEmpty()) {
					detections.add(
							new BarcodeDetection(result.getText(), boundsFromPoints(result.getResultPoints()), id()));
				}
			}
			return detections;
		} catch (ReaderException | ArrayIndexOutOfBoundsException e) {
			return List.of();
		}
	}

	private Optional<BarcodeDetection> decodeSingle(BufferedImage image, Rectangle regionBounds,
			boolean alsoTryHarder) {
		BinaryBitmap bitmap = toBitmap(image);
		if (bitmap == null) {
			return Optional.empty();
		}
		QRCodeReader reader = new QRCodeReader();
		Optional<Result> firstTry = decodeWithHints(reader, bitmap, false);
		if (firstTry.isPresent()) {
			Result res = firstTry.get();
			Rectangle bounds = regionBounds != null ? regionBounds : boundsFromPoints(res.getResultPoints());
			return Optional.of(new BarcodeDetection(res.getText(), bounds, id()));
		}
		if (alsoTryHarder) {
			Optional<Result> secondTry = decodeWithHints(reader, bitmap, true);
			if (secondTry.isPresent()) {
				Result res = secondTry.get();
				Rectangle bounds = regionBounds != null ? regionBounds : boundsFromPoints(res.getResultPoints());
				return Optional.of(new BarcodeDetection(res.getText(), bounds, id()));
			}
		}
		return Optional.empty();
	}

	private Optional<Result> decodeWithHints(QRCodeReader reader, BinaryBitmap bitmap, boolean tryHarder) {
		try {
			Result result = reader.decode(bitmap, buildHints(tryHarder));
			if (result != null && result.getText() != null && !result.getText().isEmpty()) {
				return Optional.of(result);
			}
		} catch (ReaderException | ArrayIndexOutOfBoundsException e) {
			log.trace("ZXing decode (tryHarder={}) did not match: {}", tryHarder, e.getMessage());
		}
		return Optional.empty();
	}

	private BinaryBitmap toBitmap(BufferedImage image) {
		try {
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			return new BinaryBitmap(new HybridBinarizer(source));
		} catch (RuntimeException e) {
			log.debug("Failed to create ZXing bitmap: {}", e.getMessage());
			return null;
		}
	}

	private Map<DecodeHintType, Object> buildHints(boolean tryHarder) {
		Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
		hints.put(DecodeHintType.TRY_HARDER, tryHarder);
		return hints;
	}

	private Rectangle boundsFromPoints(ResultPoint[] points) {
		if (points == null || points.length < 2) {
			return null;
		}
		float minX = Float.POSITIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		for (ResultPoint point : points) {
			if (point != null) {
				minX = Math.min(minX, point.getX());
				minY = Math.min(minY, point.getY());
				maxX = Math.max(maxX, point.getX());
				maxY = Math.max(maxY, point.getY());
			}
		}
		if (!Float.isFinite(minX) || !Float.isFinite(minY)) {
			return null;
		}
		return new Rectangle(Math.max(0, Math.round(minX)), Math.max(0, Math.round(minY)),
				Math.max(1, Math.round(maxX - minX)), Math.max(1, Math.round(maxY - minY)));
	}
}
