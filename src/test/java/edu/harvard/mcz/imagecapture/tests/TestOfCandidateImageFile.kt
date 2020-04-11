/**
 * TestOfCandidateImageFile.java
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
import org.junit.experimental.categories.Category
import java.io.File

/**
 * TestOfCandidateImageFile tests construction and some capabilities of CandidateImageFile.
 *
 * @see edu.harvard.mcz.imagecapture.CandidateImageFile
 */
@Category(IntegrationTest::class)
class TestOfCandidateImageFile
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.CandidateImageFile.CandidateImageFile].
     */
    fun testCandidateImageFile() {
        var file: CandidateImageFile
        try {
            file = CandidateImageFile(File(this.javaClass.getResource(AllTests.FILE_EMPTY).file), PositionTemplate())
            file.getClass() // added to suppress FindBugs DLS_DEAD_LOCAL_STORE
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage())
        }
        try {
            file = CandidateImageFile(File(AllTests.FILE_INVALID_NAME), PositionTemplate())
            file.getClass() // added to suppress FindBugs DLS_DEAD_LOCAL_STORE
            fail("Failed to throw UnreadableFileException for nonexistent file.")
        } catch (e: UnreadableFileException) { // pass
        }
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.CandidateImageFile.isFileReadable].
     */
    fun testIsFileReadable() {
        var file: CandidateImageFile? = null
        try {
            file = CandidateImageFile(File(this.javaClass.getResource(AllTests.FILE_EMPTY).file), PositionTemplate())
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage())
        }
        var ok = false
        try {
            ok = file.isFileReadable()
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage())
        }
        assertTrue(ok)
        try {
            file = CandidateImageFile(File(AllTests.FILE_INVALID_NAME), PositionTemplate())
            file.getClass() // added to suppress FindBugs DLS_DEAD_LOCAL_STORE
            fail("Failed to throw UnreadableFileException for invalid filename")
            try {
                ok = file.isFileReadable()
                fail("Failed to throw UnreadableFileException for invalid filename")
            } catch (e: UnreadableFileException) { // pass - sort of, but we shouldn't have been able to get here.
            }
        } catch (e: UnreadableFileException) { // pass
        }
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.CandidateImageFile.setFile].
     */
    fun testSetFile() {
        val file: CandidateImageFile
        try {
            file = CandidateImageFile(File(this.javaClass.getResource(AllTests.FILE_EMPTY).file), PositionTemplate())
            try {
                file.setFile(File(AllTests.FILE_INVALID_NAME), PositionTemplate())
                fail("Failed to throw UnreadableFileException for invalid filename.")
            } catch (e: UnreadableFileException) { // pass
            }
        } catch (e: UnreadableFileException) {
            fail("Threw unexpected UnreadableFileException. " + e.getMessage())
        }
    }
}