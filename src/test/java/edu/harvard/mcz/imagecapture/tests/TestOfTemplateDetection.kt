/**
 * TestOfTemplateDetection.java
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

import edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetector
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import junit.framework.TestCase
import org.junit.experimental.categories.Category
import java.io.File

/**
 * TestOfTemplateDetection tests the ability of the DefaultPositionTemplateDetector to correctly
 * detect templates for images with barcodes in standard positions.
 */
@Category(IntegrationTest::class)
class TestOfTemplateDetection
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetector.detectTemplateForImage].
     */
    fun testDetectTemplateForImage() {
        val detector = DefaultPositionTemplateDetector()
        try {
            detector.detectTemplateForImage(File(AllTests.FILE_INVALID_NAME))
            fail("Failed to throw UnreadableFileException for invalid file")
        } catch (e: UnreadableFileException) { // pass
        }
        try {
            val template: String = detector.detectTemplateForImage(File(this.javaClass.getResource(AllTests.FILE_EMPTY).file))
            assertEquals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, template)
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException.  " + e.getMessage())
        }
        // TODO: Test templates with example images here.
    }
}