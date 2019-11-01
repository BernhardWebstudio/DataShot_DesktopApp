/**
 * DrawerNameReturner.java
 * edu.harvard.mcz.imagecapture.interfaces
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
 * DrawerNameReturner
 *
 * Interface for returning the DrawerNumber from a source such as a class
 * that parses data off of an OCR of a label.
 *
 *
 *
 */
public interface DrawerNameReturner {

    String getDrawerNumber();

}
