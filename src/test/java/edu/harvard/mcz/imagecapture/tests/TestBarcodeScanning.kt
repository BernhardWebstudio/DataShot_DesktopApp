/**
 * testBarcodeScanning.java
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

import edu.harvard.mcz.imagecapture.CandidateImageFile
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import junit.framework.TestCase
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Test cases for reading barcodes from image files.
 */
class TestBarcodeScanning
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.CandidateImageFile.getBarcodeText].
     */
    @Test
    fun testGetBarcodeText() {
        val file: CandidateImageFile
        try {
            val testFile = File(this.javaClass.getResource(AllTests.FILE_VALID_BARCODE).file)
            println(testFile.path)
            file = CandidateImageFile(testFile, PositionTemplate())
            assertEquals(AllTests.BARCODE_IN_FILE_VALID_BARCODE, file.BarcodeText)
            assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, file.BarcodeStatus)
            assertEquals(CandidateImageFile.RESULT_ERROR, file.UnitTrayTaxonLabelTextStatus)
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.CatalogNumberBarcodeStatus)
            file.BarcodeText
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.CatalogNumberBarcodeStatus)
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.Message)
        }
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.CandidateImageFile.getBarcodeStatus].
     */
    @Test
    fun testGetBarcodeStatus() {
        val file: CandidateImageFile
        try {
            file = CandidateImageFile(File(this.javaClass.getResource(AllTests.FILE_EMPTY).file), PositionTemplate())
            assertEquals(CandidateImageFile.RESULT_ERROR, file.BarcodeStatus)
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.CatalogNumberBarcodeStatus)
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.CatalogNumberBarcodeStatus)
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.Message)
        }
    }

    @Test
    fun testreadBarcodeFromLocation() {
        val candidateImageFile = CandidateImageFile()
        var image: BufferedImage? = null
        var testFile = File(this.javaClass.getResource("/IMG_007027.JPG").file)
        //  BarcodePositionX: 3035
//  BarcodePositionY: 135
//      BarcodeSizeX: 303
//      BarcodeSizeY: 303
        var left = 3035
        var top = 135
        var width = 303
        var height = 303
        try {
            image = ImageIO.read(testFile)
        } catch (e: IOException) {
            fail(e.message)
        }
        assertEquals("MCZ-ENT00634766", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false))
        testFile = File(this.javaClass.getResource("/IMG_000069.JPG").file)
        //  BarcodePositionX: 3380
//  BarcodePositionY: 90
//      BarcodeSizeX: 480
//      BarcodeSizeY: 480
        left = 3380
        top = 90
        width = 480
        height = 480
        image = null
        try {
            image = ImageIO.read(testFile)
        } catch (e: IOException) {
            fail(e.message)
        }
        assertEquals("ETHZ-ENT0003497", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false))
        // test some problem inputs
        assertEquals("", candidateImageFile.readBarcodeFromLocation(image, left, top, 99999, 99999, false))
        assertEquals("", candidateImageFile.readBarcodeFromLocation(null, left, top, width, height, false))
        testFile = File(this.javaClass.getResource(AllTests.FILE_EMPTY).file)
        try {
            image = ImageIO.read(testFile)
        } catch (e: IOException) {
            fail(e.message)
        }
        assertEquals("", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false))
    }
}
