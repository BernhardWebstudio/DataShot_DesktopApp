/**
 * MathUtility.java
 * Created Oct 15, 2008 7:01:40 AM
 * <p>
 * © Copyright 2008 Paul J. Morris
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.harvard.mcz.imagecapture.utility;

/**
 *
 */
public class MathUtility {
    /**
     * Test for equality of double precision numbers with values near 1
     *
     * @param a one double to test
     * @param b the other double to test.
     * @return True if a and b are equal when rounded out to ten decimal places.
     */
    public static boolean equalTenPlaces(double a, double b) {
        boolean returnvalue = Math.round(a * 10000000000d) == Math.round(b * 10000000000d);
        return returnvalue;
    }

}
