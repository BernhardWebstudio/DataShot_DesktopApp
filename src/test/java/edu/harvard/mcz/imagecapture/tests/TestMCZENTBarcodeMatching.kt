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

import edu.harvard.mcz.imagecapture.MCZENTBarcode
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import junit.framework.TestCase

/**
 * TestBarcodeMatching
 */
class TestMCZENTBarcodeMatching
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.extractNumber].
     */
    fun testExtractNumber() {
        val matcher: BarcodeMatcher = MCZENTBarcode()
        assertEquals(Integer.valueOf(1), matcher.extractNumber("MCZ-ENT00000001"))
        assertNull(matcher.extractNumber("MCZ-ENTaaaaaaaaaa"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.matchesPattern].
     */
    fun testMatchesPattern() {
        val matcher: BarcodeMatcher = MCZENTBarcode()
        assertTrue(matcher.matchesPattern("MCZ-ENT01234567"))
        assertTrue(matcher.matchesPattern("MCZ-ENT00000000"))
        assertTrue(matcher.matchesPattern("MCZ-ENT00000001"))
        assertTrue(matcher.matchesPattern("MCZ-ENT99999999"))
        assertTrue(matcher.matchesPattern("MCZ-ENT11111111"))
        assertTrue(matcher.matchesPattern("MCZ-ENT22222222"))
        assertTrue(matcher.matchesPattern("MCZ-ENT33333333"))
        assertTrue(matcher.matchesPattern("MCZ-ENT44444444"))
        assertTrue(matcher.matchesPattern("MCZ-ENT55555555"))
        assertTrue(matcher.matchesPattern("MCZ-ENT66666666"))
        assertTrue(matcher.matchesPattern("MCZ-ENT77777777"))
        assertTrue(matcher.matchesPattern("MCZ-ENT88888888"))
        assertFalse(matcher.matchesPattern("not a barcode"))
        assertFalse(matcher.matchesPattern("MCZ-ENT123"))
        assertFalse(matcher.matchesPattern("MCZENT01234567"))
        assertFalse(matcher.matchesPattern("MCZ-TST01234567"))
        assertFalse(matcher.matchesPattern("MCZ-ENT01234567\n"))
        assertFalse(matcher.matchesPattern("M"))
        assertFalse(matcher.matchesPattern("MC"))
        assertFalse(matcher.matchesPattern("MCZ"))
        assertFalse(matcher.matchesPattern("MCZ-"))
        assertFalse(matcher.matchesPattern("MCZ-E"))
        assertFalse(matcher.matchesPattern("MCZ-EN"))
        assertFalse(matcher.matchesPattern("MCZ-ENT"))
        assertFalse(matcher.matchesPattern("ETHZ-ENT0123456"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.matchFoundIn].
     */
    fun testMatchFoundIn() {
        val matcher: BarcodeMatcher = MCZENTBarcode()
        assertTrue(matcher.matchFoundIn("MCZ-ENT01234567"))
        assertFalse(matcher.matchFoundIn("not a barcode"))
        assertFalse(matcher.matchFoundIn(""))
        assertFalse(matcher.matchFoundIn(null))
        assertFalse(matcher.matchFoundIn("MCZ-ENT123"))
        assertFalse(matcher.matchFoundIn("MCZENT01234567"))
        assertFalse(matcher.matchFoundIn("MCZ-TST01234567"))
        assertTrue(matcher.matchFoundIn("MCZ-ENT01234567\n"))
        assertTrue(matcher.matchFoundIn("\nMCZ-ENT01234567\n"))
        assertTrue(matcher.matchFoundIn(":MCZ-ENT01234567a"))
        assertFalse(matcher.matchFoundIn("MCZENT01234567\n"))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.MCZENTBarcode.makeFromNumber].
     */
    fun testMakeFromNumber() {
        val builder: BarcodeBuilder = MCZENTBarcode()
        assertEquals("MCZ-ENT01234567", builder.makeFromNumber(Integer.valueOf(1234567)))
        assertEquals("MCZ-ENT00000001", builder.makeFromNumber(Integer.valueOf(1)))
        assertEquals("MCZ-ENT00000000", builder.makeFromNumber(Integer.valueOf(0)))
        assertNull(builder.makeFromNumber(null))
        assertNull(builder.makeFromNumber(Int.MAX_VALUE))
        assertNull(builder.makeFromNumber(Int.MIN_VALUE))
        assertNull(builder.makeFromNumber(Integer.valueOf(-1)))
    }

    fun testMakeAndMatch() {
        val builder: BarcodeBuilder = MCZENTBarcode()
        val matcher: BarcodeMatcher = MCZENTBarcode()
        run {
            var i = 1
            while (i < 99999999) {
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i - 1)))
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i)))
                if (i + 1 <= 99999999) {
                    assertTrue(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
                } else {
                    assertFalse(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
                }
                i = i * 2
            }
        }
        var i = 99999900
        while (i <= 99999999) {
            assertTrue(matcher.matchesPattern(builder.makeFromNumber(i - 1)))
            assertTrue(matcher.matchesPattern(builder.makeFromNumber(i)))
            if (i + 1 <= 99999999) {
                assertTrue(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
            } else {
                assertFalse(matcher.matchesPattern(builder.makeFromNumber(i + 1)))
            }
            i = i + 1
        }
    }
}