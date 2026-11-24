/**
 * ScanOptions.java
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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Immutable configuration options passed to {@link BarcodeScanner} and
 * {@link BarcodeScannerChain}.
 */
public final class ScanOptions {

	public static final List<String> DEFAULT_RESCALE_TRANSFORMS = List.of("400", "600sharpen", "600brighter",
			"600dimmer", "400sharpenbrighter");

	private final Rectangle preferredRegion;
	private final boolean quickCheck;
	private final List<String> rescaleTransforms;
	private final boolean zxingTryHarder;

	private ScanOptions(Builder builder) {
		this.preferredRegion = builder.preferredRegion == null ? null : new Rectangle(builder.preferredRegion);
		this.quickCheck = builder.quickCheck;
		this.rescaleTransforms = builder.rescaleTransforms == null ? List.of() : List.copyOf(builder.rescaleTransforms);
		this.zxingTryHarder = builder.zxingTryHarder;
	}

	/**
	 * @return default scan options (full-image, exhaustive retries enabled)
	 */
	public static ScanOptions defaults() {
		return builder().build();
	}

	/**
	 * Builds {@link ScanOptions} from {@link ImageCaptureProperties} property keys.
	 *
	 * @param properties
	 *            application properties (nullable)
	 * @return configured {@link ScanOptions}
	 */
	public static ScanOptions fromProperties(Properties properties) {
		if (properties == null) {
			return defaults();
		}
		Builder builder = builder();
		String rescaleRaw = properties.getProperty(ImageCaptureProperties.KEY_IMAGERESCALE);
		if (rescaleRaw != null && !rescaleRaw.isBlank()) {
			List<String> transforms = new ArrayList<>();
			for (String part : rescaleRaw.split(",")) {
				String trimmed = part.trim();
				if (!trimmed.isEmpty()) {
					transforms.add(trimmed);
				}
			}
			builder.rescaleTransforms(transforms);
		}
		String tryHarder = properties.getProperty(ImageCaptureProperties.KEY_IMAGEZXINGALSOTRYHARDER, "true");
		builder.zxingTryHarder(Boolean.parseBoolean(tryHarder.trim()));
		return builder.build();
	}

	/**
	 * @return new {@link Builder} initialized with default values
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * @return builder initialized with this instance's values
	 */
	public Builder toBuilder() {
		return new Builder().preferredRegion(preferredRegion).quickCheck(quickCheck)
				.rescaleTransforms(rescaleTransforms).zxingTryHarder(zxingTryHarder);
	}

	/**
	 * @return defensive copy of the preferred scan region if set
	 */
	public Optional<Rectangle> preferredRegion() {
		return preferredRegion == null ? Optional.empty() : Optional.of(new Rectangle(preferredRegion));
	}

	/**
	 * @return {@code true} if scanners should perform fast checks only
	 */
	public boolean quickCheck() {
		return quickCheck;
	}

	/**
	 * @return immutable list of transform specs for retry stages
	 */
	public List<String> rescaleTransforms() {
		return rescaleTransforms;
	}

	/**
	 * @return {@code true} if ZXing should retry with {@code TRY_HARDER}
	 */
	public boolean zxingTryHarder() {
		return zxingTryHarder;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ScanOptions other)) {
			return false;
		}
		return quickCheck == other.quickCheck && zxingTryHarder == other.zxingTryHarder
				&& Objects.equals(preferredRegion, other.preferredRegion)
				&& Objects.equals(rescaleTransforms, other.rescaleTransforms);
	}

	@Override
	public int hashCode() {
		return Objects.hash(preferredRegion, quickCheck, rescaleTransforms, zxingTryHarder);
	}

	/**
	 * Fluent builder for {@link ScanOptions}.
	 */
	public static final class Builder {
		private Rectangle preferredRegion;
		private boolean quickCheck = false;
		private List<String> rescaleTransforms = DEFAULT_RESCALE_TRANSFORMS;
		private boolean zxingTryHarder = true;

		private Builder() {
		}

		public Builder preferredRegion(Rectangle preferredRegion) {
			this.preferredRegion = preferredRegion == null ? null : new Rectangle(preferredRegion);
			return this;
		}

		public Builder quickCheck(boolean quickCheck) {
			this.quickCheck = quickCheck;
			return this;
		}

		public Builder rescaleTransforms(List<String> rescaleTransforms) {
			this.rescaleTransforms = rescaleTransforms == null ? List.of() : new ArrayList<>(rescaleTransforms);
			return this;
		}

		public Builder zxingTryHarder(boolean zxingTryHarder) {
			this.zxingTryHarder = zxingTryHarder;
			return this;
		}

		public ScanOptions build() {
			return new ScanOptions(this);
		}
	}
}
