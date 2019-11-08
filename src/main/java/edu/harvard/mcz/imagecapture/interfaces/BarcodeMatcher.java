/**
 * BarcodeMatcher.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture.interfaces;

/**
 * BarcodeMatcher, interface for retrieving the numeric part of the text string found in a
 * barcode that identifies a specimen, and for checking if a text string matches the pattern
 * for the text that is expected to go in a barcode. Only concerns the text that is stored
 * in a barcode, not the actual encoding of the barcode.
 */
public interface BarcodeMatcher {

    /**
     * Extracts the numeric part of a string with a pattern matching the format of a barcode.
     *
     * @param aBarcode
     * @return
     */
    Integer extractNumber(String aBarcode);

    /**
     * Given a string, does it exactly match the pattern of the text to put in a barcode
     *
     * @param aBarcode
     * @return
     */
    boolean matchesPattern(String aBarcode);

    /**
     * Given a string, is a pattern matching the format of the text to put in a barcode found in
     * that string.
     *
     * @param aBarcode
     * @return
     */
    boolean matchFoundIn(String aBarcode);

}
