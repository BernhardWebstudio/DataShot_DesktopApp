/**
 * ImagePreprocessor.java
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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pure, non-mutating image transformations for barcode and OCR preprocessing.
 * Every operation returns a fresh {@link BufferedImage} and never alters its
 * input or shares the caller's backing raster.
 */
public final class ImagePreprocessor {

	private static final Logger log = LoggerFactory.getLogger(ImagePreprocessor.class);
	private static final float[] SHARPEN_KERNEL = {0f, -0.5f, 0f, -0.5f, 2.8f, -0.5f, 0f, -0.5f, 0f};
	private static final int DISPLACEMENT_STEP = 3;

	private ImagePreprocessor() {
	}

	/**
	 * Copies the requested sub-region into a new, independent {@link BufferedImage}
	 * (never sharing the parent backing raster).
	 *
	 * @param source
	 *            source image
	 * @param region
	 *            crop rectangle
	 * @return independent copy, or {@link Optional#empty()} if arguments are null,
	 *         non-positive, or out of bounds
	 */
	public static Optional<BufferedImage> copySubimage(BufferedImage source, Rectangle region) {
		if (source == null || region == null || region.width <= 0 || region.height <= 0) {
			return Optional.empty();
		}
		if (region.x < 0 || region.y < 0 || (long) region.x + region.width > source.getWidth()
				|| (long) region.y + region.height > source.getHeight()) {
			return Optional.empty();
		}
		int targetType = compatibleBufferedImageType(source);
		BufferedImage copy = new BufferedImage(region.width, region.height, targetType);
		Graphics2D g = copy.createGraphics();
		try {
			g.drawImage(source, 0, 0, region.width, region.height, region.x, region.y, region.x + region.width,
					region.y + region.height, null);
		} finally {
			g.dispose();
		}
		return Optional.of(copy);
	}

	/**
	 * Scales {@code source} by {@code factor} into a fresh {@link BufferedImage}.
	 *
	 * @param source
	 *            input image
	 * @param factor
	 *            positive scale factor
	 * @return newly allocated scaled image
	 */
	public static BufferedImage scale(BufferedImage source, double factor) {
		if (source == null) {
			throw new IllegalArgumentException("source must not be null");
		}
		if (!Double.isFinite(factor) || factor <= 0d) {
			throw new IllegalArgumentException("factor must be positive: " + factor);
		}
		int targetWidth = Math.max(1, (int) Math.round(source.getWidth() * factor));
		int targetHeight = Math.max(1, (int) Math.round(source.getHeight() * factor));
		int targetType = compatibleBufferedImageType(source);
		BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, targetType);
		Graphics2D g = scaled.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(source, 0, 0, targetWidth, targetHeight, null);
		} finally {
			g.dispose();
		}
		return scaled;
	}

	/**
	 * Scales {@code source} proportionally to {@code targetWidth} pixels into a
	 * fresh {@link BufferedImage}.
	 *
	 * @param source
	 *            input image
	 * @param targetWidth
	 *            desired width in pixels
	 * @return newly allocated scaled image
	 */
	public static BufferedImage scaleToWidth(BufferedImage source, double targetWidth) {
		if (source == null) {
			throw new IllegalArgumentException("source must not be null");
		}
		if (!Double.isFinite(targetWidth) || targetWidth <= 0d) {
			throw new IllegalArgumentException("targetWidth must be positive: " + targetWidth);
		}
		double factor = targetWidth / source.getWidth();
		return scale(source, factor);
	}

	/**
	 * Applies the 3x3 barcode sharpening kernel into a fresh {@link BufferedImage}
	 * without mutating {@code source}.
	 *
	 * @param source
	 *            input image
	 * @return sharpened image copy
	 */
	public static BufferedImage sharpen(BufferedImage source) {
		if (source == null) {
			throw new IllegalArgumentException("source must not be null");
		}
		BufferedImage compatible = ensureOpCompatibleCopy(source);
		Kernel kernel = new Kernel(3, 3, SHARPEN_KERNEL);
		ConvolveOp convolver = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		BufferedImage dest = new BufferedImage(compatible.getWidth(), compatible.getHeight(), compatible.getType());
		return convolver.filter(compatible, dest);
	}

	/**
	 * Applies brightness/contrast scaling into a newly allocated
	 * {@link BufferedImage} destination (never filtering in-place) after ensuring
	 * color-model compatibility so {@link RescaleOp} succeeds on unusual image
	 * types.
	 *
	 * @param source
	 *            input image
	 * @param scaleFactor
	 *            contrast multiplier
	 * @param offset
	 *            brightness offset
	 * @return newly allocated adjusted image
	 */
	public static BufferedImage adjustBrightnessContrast(BufferedImage source, float scaleFactor, float offset) {
		if (source == null) {
			throw new IllegalArgumentException("source must not be null");
		}
		BufferedImage compatible = ensureOpCompatibleCopy(source);
		BufferedImage dest = new BufferedImage(compatible.getWidth(), compatible.getHeight(), compatible.getType());
		RescaleOp rescaleOp = new RescaleOp(scaleFactor, offset, null);
		return rescaleOp.filter(compatible, dest);
	}

	/**
	 * Produces bounded small-displacement candidate rectangles ({@code +-3px})
	 * around {@code region}, all clamped and filtered to lie strictly within
	 * {@code [0, imageWidth] x [0, imageHeight]}.
	 *
	 * @param region
	 *            anchor region
	 * @param imageWidth
	 *            image width
	 * @param imageHeight
	 *            image height
	 * @return ordered unique list of non-empty candidate rectangles inside bounds
	 */
	public static List<Rectangle> displacedRegions(Rectangle region, int imageWidth, int imageHeight) {
		if (region == null || imageWidth <= 0 || imageHeight <= 0 || region.width <= 0 || region.height <= 0) {
			return List.of();
		}
		Rectangle imageBounds = new Rectangle(0, 0, imageWidth, imageHeight);
		Set<Rectangle> candidates = new LinkedHashSet<>();
		int[] deltas = {-DISPLACEMENT_STEP, DISPLACEMENT_STEP};
		for (int dx : deltas) {
			for (int dy : deltas) {
				Rectangle shifted = new Rectangle(region.x + dx, region.y + dy, region.width, region.height);
				Rectangle clamped = shifted.intersection(imageBounds);
				if (!clamped.isEmpty() && !clamped.equals(region)) {
					candidates.add(clamped);
				}
			}
		}
		return new ArrayList<>(candidates);
	}

	/**
	 * Parses transform specifications such as {@code "400"}, {@code "600sharpen"},
	 * {@code "600brighter"}, {@code "600dimmer"}, and {@code "400sharpenbrighter"}
	 * and applies them without mutating {@code crop}.
	 *
	 * @param crop
	 *            cropped input image
	 * @param spec
	 *            transform specification string
	 * @param initialScalingWidth
	 *            baseline scaling width used when parsed width is below 1
	 * @return transformed image, or {@link Optional#empty()} if the spec is blank
	 *         or produces no actionable transform
	 */
	public static Optional<BufferedImage> applyTransformSpec(BufferedImage crop, String spec,
			double initialScalingWidth) {
		if (crop == null || spec == null || spec.isBlank()) {
			return Optional.empty();
		}
		String normalized = spec.trim().toLowerCase(Locale.ROOT);
		boolean sharpen = normalized.contains("sharpen");
		boolean brighter = normalized.contains("brighter");
		boolean dimmer = normalized.contains("dimmer");

		String numeric = normalized.replace("sharpen", "").replace("brighter", "").replace("dimmer", "")
				.replaceAll("[^0-9.]", "");

		double targetWidth = crop.getWidth();
		boolean hasWidth = !numeric.isEmpty();
		if (hasWidth) {
			try {
				double parsed = Double.parseDouble(numeric);
				targetWidth = parsed < 1d ? Math.max(1d, initialScalingWidth / 2d) : parsed;
			} catch (NumberFormatException e) {
				log.debug("Invalid numeric width in transform spec '{}': {}", spec, e.getMessage());
				if (!sharpen && !brighter && !dimmer) {
					return Optional.empty();
				}
			}
		} else if (!sharpen && !brighter && !dimmer) {
			return Optional.empty();
		}

		BufferedImage current = (hasWidth && Math.round(targetWidth) != crop.getWidth())
				? scaleToWidth(crop, targetWidth)
				: copySubimage(crop, new Rectangle(0, 0, crop.getWidth(), crop.getHeight())).orElse(crop);

		if (sharpen) {
			current = sharpen(current);
		}
		if (brighter) {
			current = adjustBrightnessContrast(current, 1.2f, 15f);
		}
		if (dimmer) {
			current = adjustBrightnessContrast(current, 0.80f, -15f);
		}
		return Optional.of(current);
	}

	private static int compatibleBufferedImageType(BufferedImage source) {
		int type = source.getType();
		if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_3BYTE_BGR
				|| type == BufferedImage.TYPE_BYTE_GRAY) {
			return type;
		}
		return BufferedImage.TYPE_INT_RGB;
	}

	private static BufferedImage ensureOpCompatibleCopy(BufferedImage source) {
		int targetType = compatibleBufferedImageType(source);
		BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), targetType);
		Graphics2D g = copy.createGraphics();
		try {
			g.drawImage(source, 0, 0, null);
		} finally {
			g.dispose();
		}
		return copy;
	}
}
