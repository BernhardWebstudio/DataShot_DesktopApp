/**
 * TestImageDocumentAndMetadata.java
 * edu.harvard.mcz.imagecapture.tests
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
 */
package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.ImageMetadata;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for {@link ImageDocument} and {@link ImageMetadata}.
 */
public class TestImageDocumentAndMetadata {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private File getResourceFile(String resourcePath) {
		URL url = getClass().getResource(resourcePath);
		assertNotNull("Missing test resource: " + resourcePath, url);
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			return new File(url.getPath());
		}
	}

	@Test
	public void testMissingPathZeroIoInConstructorAndEnsureReadableThrows() {
		Path missingPath = tempFolder.getRoot().toPath().resolve("does_not_exist.jpg");
		ImageDocument doc = ImageDocument.of(missingPath);
		assertNotNull(doc);
		assertEquals(missingPath, doc.getPath());
		assertFalse(doc.isInMemory());

		try {
			doc.ensureReadable();
			fail("Expected UnreadableFileException for missing file");
		} catch (UnreadableFileException expected) {
			assertTrue(expected.getMessage().contains("does_not_exist.jpg"));
		}

		ImageDocument nullDoc = ImageDocument.of((File) null);
		try {
			nullDoc.ensureReadable();
			fail("Expected UnreadableFileException for null file");
		} catch (UnreadableFileException expected) {
			// expected
		}
	}

	@Test
	public void testSinglePassExifAndXmpParsingOnFixturesAndEdgeCases() {
		ImageMetadata img60 = ImageMetadata
				.readFrom(getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG"));
		assertEquals("MCZ-ENT00050008", img60.effectiveUserComment());
		assertEquals("2009-04-22", img60.formatDateCreated("yyyy-MM-dd"));
		assertEquals(2848, img60.getWidth());
		assertEquals(4272, img60.getHeight());

		ImageMetadata img69 = ImageMetadata.readFrom(getResourceFile("/IMG_000069.JPG"));
		assertEquals("", img69.effectiveUserComment());
		assertEquals("2017-01-30", img69.formatDateCreated("yyyy-MM-dd"));
		assertTrue(img69.getWidth() > 0);
		assertTrue(img69.getHeight() > 0);

		ImageMetadata img7027 = ImageMetadata.readFrom(getResourceFile("/IMG_007027.JPG"));
		assertNotNull(img7027.effectiveUserComment());
		assertTrue(img7027.getWidth() > 0);
		assertTrue(img7027.getHeight() > 0);

		ImageMetadata empty = ImageMetadata.readFrom(getResourceFile(AllTests.FILE_EMPTY));
		assertEquals("", empty.effectiveUserComment());
		assertNull(empty.formatDateCreated("yyyy-MM-dd"));
		assertEquals(0, empty.getWidth());

		ImageMetadata missing = ImageMetadata.readFrom(Paths.get("/nonexistent/missing_fixture.jpg"));
		assertEquals("", missing.effectiveUserComment());
		assertNull(missing.getDateCreated());
		assertNull(ImageMetadata.readFrom((Path) null).getDateCreated());

		Date d = new Date(1_700_000_000_000L);
		ImageMetadata xmpFallback = new ImageMetadata("   ", "XMP-DC-DESC", d, 100, 200, 1);
		assertEquals("XMP-DC-DESC", xmpFallback.effectiveUserComment());
		d.setTime(0L);
		assertEquals(1_700_000_000_000L, xmpFallback.getDateCreated().getTime());
	}

	@Test
	public void testLazyCachedImageAndMetadataWithoutHittingDiskAfterDelete() throws Exception {
		Path source = getResourceFile(AllTests.FILE_VALID_BARCODE_PATH + "IMG_000060.JPG").toPath();
		Path tempCopy = tempFolder.getRoot().toPath().resolve("cached_copy.jpg");
		Files.copy(source, tempCopy, StandardCopyOption.REPLACE_EXISTING);

		ImageDocument doc = ImageDocument.of(tempCopy);
		doc.ensureReadable();

		BufferedImage firstImage = doc.getImage();
		ImageMetadata firstMetadata = doc.getMetadata();
		assertNotNull(firstImage);
		assertNotNull(firstMetadata);
		assertEquals("MCZ-ENT00050008", firstMetadata.effectiveUserComment());

		// Delete backing file; subsequent calls must return cached instances without
		// I/O
		Files.delete(tempCopy);
		assertFalse(Files.exists(tempCopy));

		BufferedImage secondImage = doc.getImage();
		ImageMetadata secondMetadata = doc.getMetadata();
		assertSame("Expected cached BufferedImage instance", firstImage, secondImage);
		assertSame("Expected cached ImageMetadata instance", firstMetadata, secondMetadata);
	}
}
