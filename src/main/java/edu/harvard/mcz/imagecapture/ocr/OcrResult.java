/**
 * OcrResult.java
 * edu.harvard.mcz.imagecapture.ocr
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
package edu.harvard.mcz.imagecapture.ocr;

import java.util.Objects;

/**
 * Immutable result of an OCR recognition attempt.
 */
public final class OcrResult {

	private final String text;
	private final boolean success;
	private final String errorMessage;

	/**
	 * Creates an {@link OcrResult}.
	 *
	 * @param text
	 *            recognized text (normalized to {@code ""} if null)
	 * @param success
	 *            whether OCR completed cleanly
	 * @param errorMessage
	 *            optional error description (normalized to {@code ""} if null)
	 */
	public OcrResult(String text, boolean success, String errorMessage) {
		this.text = text == null ? "" : text;
		this.success = success;
		this.errorMessage = errorMessage == null ? "" : errorMessage;
	}

	/**
	 * Creates a successful OCR result.
	 *
	 * @param text
	 *            recognized text
	 * @return successful result
	 */
	public static OcrResult success(String text) {
		return new OcrResult(text, true, "");
	}

	/**
	 * Creates a failed OCR result.
	 *
	 * @param errorMessage
	 *            failure description
	 * @return failed result
	 */
	public static OcrResult failure(String errorMessage) {
		return new OcrResult("", false, errorMessage);
	}

	/**
	 * @return recognized text (never {@code null})
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return {@code true} if OCR succeeded
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return error message if failed, otherwise {@code ""}
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OcrResult other)) {
			return false;
		}
		return success == other.success && Objects.equals(text, other.text)
				&& Objects.equals(errorMessage, other.errorMessage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, success, errorMessage);
	}

	@Override
	public String toString() {
		return "OcrResult{success=" + success + ", textLength=" + text.length() + ", errorMessage='" + errorMessage
				+ "'}";
	}
}
