package edu.harvard.mcz.imagecapture.tests

import junit.framework.Test
import junit.framework.TestSuite
import java.io.File

/**
 * AllTests.java
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
/**
 * AllTests invokes the suite of unit tests for edu.harvard.mcz.imagecapture classes.
 *
 *
 * AllTests includes class constants for resources within edu.harvard.mcz.imagecapture.tests.resources
 * that are used repeatedly in the unit tests themselves.
 */
object AllTests {
    // Filename constants for tests
    const val FILE_VALID_BARCODE_FILENAME = "141515321.jpg"
    val FILE_VALID_BARCODE_PATH = "resources" + File.separator
    val FILE_VALID_BARCODE = FILE_VALID_BARCODE_PATH + FILE_VALID_BARCODE_FILENAME
    const val BARCODE_IN_FILE_VALID_BARCODE = "141515321"
    val FILE_INVALID_NAME = FILE_VALID_BARCODE_PATH + "nosuchfile.bad"
    val FILE_EMPTY = FILE_VALID_BARCODE_PATH + "emptyfile.jpg"
    /**
     * Run the JUnit tests (but not integration tests).
     *
     * @return Test
     */
    fun suite(): Test {
        val suite = TestSuite("Tests in edu.harvard.mcz.imagecapture.tests")
        //$JUnit-BEGIN$
        suite.addTestSuite(TestBarcodeScanning::class.java)
        suite.addTestSuite(TestOfSingleton::class.java)
        // suite.addTestSuite(TestOfPositionTemplate.class);
// suite.addTestSuite(TestOfCandidateImageFile.class);
// suite.addTestSuite(TestOfTemplateDetection.class);
        suite.addTestSuite(TestOfUnitTrayLabel::class.java)
        suite.addTestSuite(TestOfUnitTrayLabelParser::class.java)
        suite.addTestSuite(TestOfSpecimen::class.java)
        suite.addTestSuite(TestOfImageCaptureProperties::class.java)
        suite.addTestSuite(TestMCZENTBarcodeMatching::class.java)
        suite.addTestSuite(TestETHZBarcodeMatching::class.java)
        suite.addTestSuite(TestofHashUtility::class.java)
        suite.addTestSuite(TestOfUserRoles::class.java)
        suite.addTestSuite(TestOfPasswordComplexity::class.java)
        //$JUnit-END$
        return suite
    }
}