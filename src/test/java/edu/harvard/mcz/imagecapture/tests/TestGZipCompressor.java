package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.harvard.mcz.imagecapture.utility.GZipCompressor;
import java.io.IOException;
import org.junit.Test;

/**
 * Test the GZipCompressor
 */
public class TestGZipCompressor {

	@Test
	public void testGZipCompressor() {
		String testString = "alsd290a .!as321'";
		String result = "";
		try {
			byte[] compressed = GZipCompressor.compress(testString);
			result = GZipCompressor.decompress(compressed);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		assertTrue(result.equals(testString));
	}
}
