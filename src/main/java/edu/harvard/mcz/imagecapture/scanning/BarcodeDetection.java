/**
 * BarcodeDetection.java
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

import java.awt.Rectangle;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable result of a single barcode/QR detection, including its decoded
 * payload, optional bounding box in image pixel coordinates, and the engine id
 * that produced it.
 */
public final class BarcodeDetection {

	private final String text;
	private final Rectangle bounds;
	private final String engine;

	/**
	 * Creates a detection without bounding-box coordinates.
	 *
	 * @param text
	 *            decoded barcode text
	 * @param engine
	 *            identifier of the scanner engine
	 */
	public BarcodeDetection(String text, String engine) {
		this(text, null, engine);
	}

	/**
	 * Creates a detection with optional bounding-box coordinates.
	 *
	 * @param text
	 *            decoded barcode text
	 * @param bounds
	 *            bounding box in image coordinates, or {@code null}
	 * @param engine
	 *            identifier of the scanner engine
	 */
	public BarcodeDetection(String text, Rectangle bounds, String engine) {
		this.text = Objects.requireNonNull(text, "text must not be null");
		this.bounds = bounds == null ? null : new Rectangle(bounds);
		this.engine = engine == null ? "" : engine;
	}

	/**
	 * @return decoded barcode text
	 */
	public String text() {
		return text;
	}

	/**
	 * @return defensive copy of the bounding box if known
	 */
	public Optional<Rectangle> bounds() {
		return bounds == null ? Optional.empty() : Optional.of(new Rectangle(bounds));
	}

	/**
	 * @return scanner engine identifier (e.g. {@code "zxing"}, {@code "boofcv"},
	 *         {@code "native"})
	 */
	public String engine() {
		return engine;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BarcodeDetection other)) {
			return false;
		}
		return Objects.equals(text, other.text) && Objects.equals(bounds, other.bounds)
				&& Objects.equals(engine, other.engine);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, bounds, engine);
	}

	@Override
	public String toString() {
		return "BarcodeDetection{text='" + text + "', bounds=" + bounds + ", engine='" + engine + "'}";
	}
}
