/**
 * testBarcodeScanning.java
 * edu.harvard.mcz.imagecapture.tests
 * <p>
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
 */
package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.CandidateImageFile;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import junit.framework.TestCase;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Test cases for reading barcodes from image files.
 */
public class TestBarcodeScanning extends TestCase {

    /**
     * @param name
     */
    public TestBarcodeScanning(String name) {
        super(name);
    }

    /**
     * Test method for {@link edu.harvard.mcz.imagecapture.CandidateImageFile#getBarcodeText()}.
     */
    @Test
    public void testGetBarcodeText() {
        CandidateImageFile file;
        try {
            File testFile = new File(this.getClass().getResource(AllTests.FILE_VALID_BARCODE).getFile());
            System.out.println(testFile.getPath());
            file = new CandidateImageFile(testFile, new PositionTemplate());
            assertEquals(AllTests.BARCODE_IN_FILE_VALID_BARCODE, file.getBarcodeText());
            assertEquals(CandidateImageFile.RESULT_BARCODE_SCANNED, file.getBarcodeStatus());
            assertEquals(CandidateImageFile.RESULT_ERROR, file.getUnitTrayTaxonLabelTextStatus());
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.getCatalogNumberBarcodeStatus());
            file.getBarcodeText();
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.getCatalogNumberBarcodeStatus());
        } catch (UnreadableFileException e) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage());
        }
    }

    /**
     * Test method for {@link edu.harvard.mcz.imagecapture.CandidateImageFile#getBarcodeStatus()}.
     */
    @Test
    public void testGetBarcodeStatus() {
        CandidateImageFile file;
        try {
            file = new CandidateImageFile(new File(this.getClass().getResource(AllTests.FILE_EMPTY).getFile()), new PositionTemplate());
            assertEquals(CandidateImageFile.RESULT_ERROR, file.getBarcodeStatus());
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.getCatalogNumberBarcodeStatus());
            assertEquals(CandidateImageFile.RESULT_NOT_CHECKED, file.getCatalogNumberBarcodeStatus());
        } catch (UnreadableFileException e) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage());
        }
    }

    @Test
    public void testreadBarcodeFromLocation() {
        CandidateImageFile candidateImageFile = new CandidateImageFile();
        BufferedImage image = null;

        File testFile = new File(this.getClass().getResource("/IMG_007027.JPG").getFile());
        //  BarcodePositionX: 3035
        //  BarcodePositionY: 135
        //      BarcodeSizeX: 303
        //      BarcodeSizeY: 303
        int left = 3035;
        int top = 135;
        int width = 303;
        int height = 303;
        try {
            image = ImageIO.read(testFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals("MCZ-ENT00634766", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false));

        testFile = new File(this.getClass().getResource("/IMG_000069.JPG").getFile());
        //  BarcodePositionX: 3380
        //  BarcodePositionY: 90
        //      BarcodeSizeX: 480
        //      BarcodeSizeY: 480
        left = 3380;
        top = 90;
        width = 480;
        height = 480;
        image = null;
        try {
            image = ImageIO.read(testFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals("ETHZ-ENT0003497", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false));

        // test some problem inputs
        assertEquals("", candidateImageFile.readBarcodeFromLocation(image, left, top, 99999, 99999, false));
        assertEquals("", candidateImageFile.readBarcodeFromLocation(null, left, top, width, height, false));

        testFile = new File(this.getClass().getResource(AllTests.FILE_EMPTY).getFile());
        try {
            image = ImageIO.read(testFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals("", candidateImageFile.readBarcodeFromLocation(image, left, top, width, height, false));
    }

}
