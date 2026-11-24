/**
 * ImageAnalysisResult.java
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

import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable result of analyzing a specimen image file, containing the resolved
 * template, catalog barcode, parsed unit-tray label, single-pass metadata,
 * optional OCR text, and diagnostics.
 */
public final class ImageAnalysisResult {

	public static final int STATUS_NOT_CHECKED = 0;
	public static final int STATUS_BARCODE_SCANNED = 1;
	public static final int STATUS_ERROR = 2;

	private final PositionTemplate template;
	private final String catalogBarcode;
	private final int catalogBarcodeStatus;
	private final String catalogBarcodeEngine;
	private final UnitTrayLabel unitTrayLabel;
	private final String rawLabelText;
	private final int unitTrayLabelStatus;
	private final String unitTrayLabelEngine;
	private final ImageMetadata metadata;
	private final String ocrText;
	private final List<String> diagnostics;

	private ImageAnalysisResult(Builder builder) {
		this.template = Objects.requireNonNull(builder.template, "template must not be null");
		this.catalogBarcode = builder.catalogBarcode;
		this.catalogBarcodeStatus = builder.catalogBarcodeStatus;
		this.catalogBarcodeEngine = builder.catalogBarcodeEngine == null ? "" : builder.catalogBarcodeEngine;
		this.unitTrayLabel = builder.unitTrayLabel;
		this.rawLabelText = builder.rawLabelText;
		this.unitTrayLabelStatus = builder.unitTrayLabelStatus;
		this.unitTrayLabelEngine = builder.unitTrayLabelEngine == null ? "" : builder.unitTrayLabelEngine;
		this.metadata = builder.metadata == null ? ImageMetadata.empty() : builder.metadata;
		this.ocrText = builder.ocrText;
		this.diagnostics = builder.diagnostics == null ? List.of() : List.copyOf(builder.diagnostics);
	}

	/**
	 * @return new builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	public PositionTemplate getTemplate() {
		return template;
	}

	public String getCatalogBarcode() {
		return catalogBarcode;
	}

	public int getCatalogBarcodeStatus() {
		return catalogBarcodeStatus;
	}

	public String getCatalogBarcodeEngine() {
		return catalogBarcodeEngine;
	}

	public UnitTrayLabel getUnitTrayLabel() {
		return unitTrayLabel;
	}

	public String getRawLabelText() {
		return rawLabelText;
	}

	public int getUnitTrayLabelStatus() {
		return unitTrayLabelStatus;
	}

	public String getUnitTrayLabelEngine() {
		return unitTrayLabelEngine;
	}

	public ImageMetadata getMetadata() {
		return metadata;
	}

	public Optional<String> getOcrText() {
		return Optional.ofNullable(ocrText);
	}

	public List<String> getDiagnostics() {
		return diagnostics;
	}

	/**
	 * Builder for {@link ImageAnalysisResult}.
	 */
	public static final class Builder {
		private PositionTemplate template;
		private String catalogBarcode;
		private int catalogBarcodeStatus = STATUS_NOT_CHECKED;
		private String catalogBarcodeEngine = "";
		private UnitTrayLabel unitTrayLabel;
		private String rawLabelText;
		private int unitTrayLabelStatus = STATUS_NOT_CHECKED;
		private String unitTrayLabelEngine = "";
		private ImageMetadata metadata = ImageMetadata.empty();
		private String ocrText;
		private List<String> diagnostics = List.of();

		private Builder() {
		}

		public Builder template(PositionTemplate template) {
			this.template = template;
			return this;
		}

		public Builder catalogBarcode(String catalogBarcode) {
			this.catalogBarcode = catalogBarcode;
			return this;
		}

		public Builder catalogBarcodeStatus(int catalogBarcodeStatus) {
			this.catalogBarcodeStatus = catalogBarcodeStatus;
			return this;
		}

		public Builder catalogBarcodeEngine(String catalogBarcodeEngine) {
			this.catalogBarcodeEngine = catalogBarcodeEngine;
			return this;
		}

		public Builder unitTrayLabel(UnitTrayLabel unitTrayLabel) {
			this.unitTrayLabel = unitTrayLabel;
			return this;
		}

		public Builder rawLabelText(String rawLabelText) {
			this.rawLabelText = rawLabelText;
			return this;
		}

		public Builder unitTrayLabelStatus(int unitTrayLabelStatus) {
			this.unitTrayLabelStatus = unitTrayLabelStatus;
			return this;
		}

		public Builder unitTrayLabelEngine(String unitTrayLabelEngine) {
			this.unitTrayLabelEngine = unitTrayLabelEngine;
			return this;
		}

		public Builder metadata(ImageMetadata metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder ocrText(String ocrText) {
			this.ocrText = ocrText;
			return this;
		}

		public Builder diagnostics(List<String> diagnostics) {
			this.diagnostics = diagnostics;
			return this;
		}

		public ImageAnalysisResult build() {
			return new ImageAnalysisResult(this);
		}
	}
}
