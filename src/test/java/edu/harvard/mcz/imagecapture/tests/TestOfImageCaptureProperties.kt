/**
 * testOfImageCaptureProperties.java
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

import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import junit.framework.TestCase
import java.io.File

/**
 * testOfImageCaptureProperties
 */
class TestOfImageCaptureProperties
/**
 * @param name
 */(name: String?) : TestCase(name) {
    var imageBase: String? = null
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        // store the pre-existing value of KEY_IMAGEBASE
        imageBase = Singleton.SingletonInstance.Properties.Properties.getProperty(ImageCaptureProperties.KEY_IMAGEBASE)
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
        // restore the pre-existing value of KEY_IMAGEBASE
        Singleton.SingletonInstance.Properties.Properties.setProperty(ImageCaptureProperties.KEY_IMAGEBASE, imageBase)
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.ImageCaptureProperties.getPathBelowBase].
     * Tests generation of path from base given a path including base and a filename.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_IMAGEBASE
     */
    fun testGetPathBelowBaseForFile() { // Test removing filename by testing with an existing file.
// Won't work if full doesn't exist as a file on the filesystem.
// Tests only the case of the current filesystem - run test on
// both a unix machine and a windows machine to validate this behavior
// on both filesystems.
        val full = File(this.javaClass.getResource(AllTests.FILE_VALID_BARCODE).file)
        val fullstring = full.path
        // Create a base path from the actual path to this file, leaving out one directory
        val base = File(full.parent).parent
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(AllTests.FILE_VALID_BARCODE_PATH, ImageCaptureProperties.getPathBelowBase(full, "/"))
        // test round trip re-assembly
        assertEquals(fullstring,
                ImageCaptureProperties.assemblePathWithBase(
                        ImageCaptureProperties.getPathBelowBase(full, "/"),
                        AllTests.FILE_VALID_BARCODE_FILENAME))
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.ImageCaptureProperties.getPathBelowBase].
     * Tests generation of path from base given a path from root.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_IMAGEBASE
     */
    fun testGetPathBelowBase() { // Uses ImageCaptureProperties.getPathBelowBase(file, separator) which is intended only
// for use here in unit testing.
// Goal is to retrieve:
// File imageFile = new File(IMAGEBASE + image.Path + image.Filename);
// So, given absolute path to a file, need to remove the filename and IMAGEBASE, leaving just
// a path to store in ICImage.Path.
// IMAGEBASE should end in a separator, image.getPath should not start with a separator, but end with one,
// and image.getFilename should not start with a separator.
// image.getPath will need conversion to/from local separator.
// Test unix path  - will work with non-existant directory names.
        val result = "test_files/test/"
        var base = "/mount/images/"
        var fullstring = "/mount/images/test_files/test/"
        var full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        // Test for variants of presence/absence of path terminator in base and file
        base = "/mount/images"
        fullstring = "/mount/images/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        base = "/mount/images/"
        fullstring = "/mount/images/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        base = "/mount/images"
        fullstring = "/mount/images/test_files/test"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        // test for a space in path name
        base = "/mount/images/dir name/"
        fullstring = "/mount/images/dir name/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        // test for a space in path name
        base = "/mount/images/dir name/"
        fullstring = "/mount/images/dir name/test files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals("test files/test/", ImageCaptureProperties.getPathBelowBase(full, "/"))
        // test for non-canonical base path
        base = "/mount/images/../images/"
        fullstring = "/mount/images/../images/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(result, ImageCaptureProperties.getPathBelowBase(full, "/"))
        // test for image in base directory
        base = "/home/mole/stuff/MCZ/mcz/insects/testImages/base/"
        fullstring = "/home/mole/stuff/MCZ/mcz/insects/testImages/base/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals("", ImageCaptureProperties.getPathBelowBase(full, "/"))
        // Test windows path
        base = "Z:\\images\\"
        fullstring = "Z:\\images\\test_files\\test\\"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals("test_files\\test\\", ImageCaptureProperties.getPathBelowBase(full, "\\"))
        base = "Z:\\images\\test_files\\test\\"
        fullstring = "Z:\\images\\test_files\\test\\"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals("", ImageCaptureProperties.getPathBelowBase(full, "\\"))
        /*     Note: This test won't run, as there isn't a way to tell a non-existent file from
 *     a non-existent directory.  To do this, run the test testGetPathBelowBaseForFile()
 *     on both Windows and unix machines.
 *
 		base = "Z:\\images\\";
		fullstring = "Z:\\images\\test_files\\test\\image.img";
		full = new File(fullstring);
		Singleton.SingletonInstance.Properties.Properties.setProperty(
				ImageCaptureProperties.KEY_IMAGEBASE,
				base);
		assertEquals("test_files\\test\\", ImageCaptureProperties.getPathBelowBase(full, "\\"));
*/
// Test Windows path on Unix
// Behave like a windows system and create a path from base
        base = "Z:\\images\\"
        fullstring = "Z:\\images\\test_files\\test\\"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        // first, generate the path below base as windows path
        assertEquals("test_files\\test\\", ImageCaptureProperties.getPathBelowBase(full, "\\"))
        val winPath: String = ImageCaptureProperties.getPathBelowBase(full, "\\")
        // second, test round trip re-assembly on unix
        base = "/media/images"
        fullstring = "/media/images/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(fullstring + AllTests.FILE_VALID_BARCODE_FILENAME,
                ImageCaptureProperties.assemblePathWithBase(winPath, AllTests.FILE_VALID_BARCODE_FILENAME, "/"))
        // Test Unix path on Windows
// Behave like a Unix System and create a path from base
        base = "/media/images/"
        fullstring = "/media/images/test_files/test/"
        full = File(fullstring)
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        // first, generate the path below base as unix path
        assertEquals("test_files/test/", ImageCaptureProperties.getPathBelowBase(full, "/"))
        val unixPath: String = ImageCaptureProperties.getPathBelowBase(full, "/")
        // second, test round trip re-assembly on Windows
        base = "Z:\\images\\"
        fullstring = "Z:\\images\\test_files\\test\\"
        //full = new File(fullstring);
        Singleton.SingletonInstance.Properties.Properties.setProperty(
                ImageCaptureProperties.KEY_IMAGEBASE,
                base)
        assertEquals(fullstring + AllTests.FILE_VALID_BARCODE_FILENAME,
                ImageCaptureProperties.assemblePathWithBase(unixPath, AllTests.FILE_VALID_BARCODE_FILENAME, "\\"))
    }
}
