/**
 * TestImagePreprocessor.java
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

import edu.harvard.mcz.imagecapture.scanning.ImagePreprocessor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link ImagePreprocessor} verifying non-mutating transforms,
 * raster independence, and bounds safety.
 */
public class TestImagePreprocessor {

	private static int[] snapshotPixels(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		return img.getRGB(0, 0, w, h, null, 0, w);
	}

	private static BufferedImage createTestPatternImage() {
		BufferedImage img = new BufferedImage(80, 60, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(220, 210, 200));
		g.fillRect(0, 0, 80, 60);
		g.setColor(new Color(30, 60, 120));
		g.fillRect(10, 10, 30, 30);
		g.setColor(new Color(180, 40, 40));
		g.fillOval(40, 20, 25, 25);
		g.dispose();
		return img;
	}

	@Test
	public void testAllTransformsPreserveSourcePixels() {
		BufferedImage source = createTestPatternImage();
		int[] before = snapshotPixels(source);

		BufferedImage sub = ImagePreprocessor.copySubimage(source, new Rectangle(10, 10, 30, 30)).orElseThrow();
		assertArrayEquals("copySubimage must not mutate source", before, snapshotPixels(source));
		assertEquals(30, sub.getWidth());
		assertEquals(30, sub.getHeight());

		BufferedImage scaled = ImagePreprocessor.scale(source, 0.5);
		assertArrayEquals("scale must not mutate source", before, snapshotPixels(source));
		assertEquals(40, scaled.getWidth());
		assertEquals(30, scaled.getHeight());

		BufferedImage scaledToWidth = ImagePreprocessor.scaleToWidth(source, 40.0);
		assertArrayEquals("scaleToWidth must not mutate source", before, snapshotPixels(source));
		assertEquals(40, scaledToWidth.getWidth());

		BufferedImage sharpened = ImagePreprocessor.sharpen(source);
		assertArrayEquals("sharpen must not mutate source", before, snapshotPixels(source));
		assertEquals(80, sharpened.getWidth());

		BufferedImage adjusted = ImagePreprocessor.adjustBrightnessContrast(source, 1.3f, 20.0f);
		assertArrayEquals("adjustBrightnessContrast must not mutate source", before, snapshotPixels(source));
		assertEquals(80, adjusted.getWidth());

		BufferedImage transformed = ImagePreprocessor.applyTransformSpec(source, "120sharpenbrighter", 80.0)
				.orElseThrow();
		assertArrayEquals("applyTransformSpec must not mutate source", before, snapshotPixels(source));
		assertEquals(120, transformed.getWidth());
	}

	@Test
	public void testCopySubimageRasterIndependenceAndBoundsHandling() {
		BufferedImage parent = createTestPatternImage();
		int originalRgb = parent.getRGB(15, 15);

		BufferedImage crop = ImagePreprocessor.copySubimage(parent, new Rectangle(10, 10, 20, 20)).orElseThrow();
		crop.setRGB(5, 5, Color.GREEN.getRGB());
		assertEquals("Mutating crop must not affect parent raster", originalRgb, parent.getRGB(15, 15));

		assertTrue(ImagePreprocessor.copySubimage(null, new Rectangle(0, 0, 10, 10)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, null).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(-1, 0, 10, 10)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(0, -1, 10, 10)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(0, 0, 0, 10)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(0, 0, 10, 0)).isEmpty());
		assertTrue(ImagePreprocessor.copySubimage(parent, new Rectangle(70, 50, 20, 20)).isEmpty());
	}

	@Test
	public void testDisplacedRegionsClampedInsideImageBounds() {
		List<Rectangle> candidates = ImagePreprocessor.displacedRegions(new Rectangle(0, 0, 20, 20), 40, 40);
		assertFalse(candidates.isEmpty());
		for (Rectangle r : candidates) {
			assertTrue(r.x >= 0);
			assertTrue(r.y >= 0);
			assertTrue(r.width > 0 && r.height > 0);
			assertTrue(r.x + r.width <= 40);
			assertTrue(r.y + r.height <= 40);
		}

		List<Rectangle> farEdge = ImagePreprocessor.displacedRegions(new Rectangle(30, 30, 10, 10), 40, 40);
		assertFalse(farEdge.isEmpty());
		for (Rectangle r : farEdge) {
			assertTrue(r.x >= 0 && r.y >= 0);
			assertTrue(r.x + r.width <= 40 && r.y + r.height <= 40);
		}
	}
}
