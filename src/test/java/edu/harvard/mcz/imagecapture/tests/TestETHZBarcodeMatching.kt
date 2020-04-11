/**
 * TestBarcodeMatching.java
 * edu.harvard.mcz.imagecapture.tests
 *
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package edu.harvard.mcz.imagecapture.tests

import edu.harvard.mcz.imagecapture.ETHZBarcode
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import junit.framework.TestCase

/**
 * TestBarcodeMatching
 */
class TestETHZBarcodeMatching
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.extractNumber].
     */
    fun testExtractNumber() {
        val matcher: BarcodeMatcher = ETHZBarcode()
        assertEquals(Integer.valueOf(1), matcher.extractNumber("ETHZ-ENT0000001"))
        assertNull(matcher.extractNumber("ETHZ-ENTaaaaaaaaa"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.matchesPattern].
     */
    fun testMatchesPattern() {
        val matcher: BarcodeMatcher = ETHZBarcode()
        assertTrue(matcher.matchesPattern("ETHZ-ENT0123456"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT0000000"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT0000001"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT9999999"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT1111111"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT2222222"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT3333333"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT4444444"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT5555555"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT6666666"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT7777777"))
        assertTrue(matcher.matchesPattern("ETHZ-ENT8888888"))
        assertFalse(matcher.matchesPattern("not a barcode"))
        assertFalse(matcher.matchesPattern("ETHZ-ENT123"))
        assertFalse(matcher.matchesPattern("ETZHENT0123456"))
        assertFalse(matcher.matchesPattern("ETHZ-TST0123456"))
        assertFalse(matcher.matchesPattern("ETHZ-ENT0123456\n"))
        assertFalse(matcher.matchesPattern("MCZ-ENT0123456"))
        assertFalse(matcher.matchesPattern("E"))
        assertFalse(matcher.matchesPattern("ET"))
        assertFalse(matcher.matchesPattern("ETZ"))
        assertFalse(matcher.matchesPattern("ETZH"))
        assertFalse(matcher.matchesPattern("ETHZ-"))
        assertFalse(matcher.matchesPattern("ETHZ-E"))
        assertFalse(matcher.matchesPattern("ETHZ-EN"))
        assertFalse(matcher.matchesPattern("ETHZ-ENT"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.matchFoundIn].
     */
    fun testMatchFoundIn() {
        val matcher: BarcodeMatcher = ETHZBarcode()
        assertTrue(matcher.matchFoundIn("ETHZ-ENT0123456"))
        assertFalse(matcher.matchFoundIn("not a barcode"))
        assertFalse(matcher.matchFoundIn(""))
        assertFalse(matcher.matchFoundIn(null))
        assertFalse(matcher.matchFoundIn("ETHZ-ENT123"))
        assertFalse(matcher.matchFoundIn("MCZENT01234567"))
        assertFalse(matcher.matchFoundIn("ETHZENT0123456"))
        assertFalse(matcher.matchFoundIn("ETHZ-TST0123456"))
        assertTrue(matcher.matchFoundIn("ETHZ-ENT0123456\n"))
        assertTrue(matcher.matchFoundIn("\nETHZ-ENT0123456\n"))
        assertTrue(matcher.matchFoundIn(":ETHZ-ENT0123456a"))
        assertFalse(matcher.matchFoundIn("MCZENT0123456\n"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.makeFromNumber].
     */
    fun testMakeFromNumber() {
        val builder: BarcodeBuilder = ETHZBarcode()
        assertEquals("ETHZ-ENT0123456", builder.makeFromNumber(Integer.valueOf(123456)))
        assertEquals("ETHZ-ENT0000001", builder.makeFromNumber(Integer.valueOf(1)))
        assertEquals("ETHZ-ENT0000000", builder.makeFromNumber(Integer.valueOf(0)))
        assertNull(builder.makeFromNumber(null))
        assertNull(builder.makeFromNumber(Int.MAX_VALUE))
        assertNull(builder.makeFromNumber(Int.MIN_VALUE))
        assertNull(builder.makeFromNumber(Integer.valueOf(-1)))
    }

    fun testMakeAndMatch() {
        val builder: BarcodeBuilder = ETHZBarcode()
        val matcher: BarcodeMatcher = ETHZBarcode()
        run {
            var i = 1
            while (i < 9999999) {
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i - 1)))
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i)))
                if (i + 1 <= 9999999) {
                    assertTrue(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
                } else {
                    assertFalse(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
                }
                i = i * 2
            }
        }
        var i = 9999000
        while (i <= 9999999) {
            assertTrue(matcher.matchesPattern(builder.makeFromNumber(i - 1)))
            assertTrue(matcher.matchesPattern(builder.makeFromNumber(i)))
            if (i + 1 <= 9999999) {
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
            } else {
                assertFalse(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
            }
            i = i + 1
        }
    }
}