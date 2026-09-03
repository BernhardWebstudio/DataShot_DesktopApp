package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.data.LookupCache;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;

public class TestLookupAndSpecimenCache {

	@Before
	public void setUp() {
		LookupCache.clear();
		SpecimenCache.clear();
	}

	@Test
	public void testLookupCacheCachingAndInvalidation() {
		AtomicInteger loadCount = new AtomicInteger(0);

		String[] first = LookupCache.getOrLoad("test.key", () -> {
			loadCount.incrementAndGet();
			return new String[]{"Alpha", "Beta"};
		});
		assertEquals(1, loadCount.get());
		assertEquals(2, first.length);
		assertEquals("Alpha", first[0]);

		// Second call should return cached value without executing loader
		String[] second = LookupCache.getOrLoad("test.key", () -> {
			loadCount.incrementAndGet();
			return new String[]{"Gamma"};
		});
		assertEquals(1, loadCount.get());
		assertSame(first, second);

		// Invalidate specific key
		LookupCache.invalidate("test.key");
		String[] third = LookupCache.getOrLoad("test.key", () -> {
			loadCount.incrementAndGet();
			return new String[]{"Delta"};
		});
		assertEquals(2, loadCount.get());
		assertEquals(1, third.length);
		assertEquals("Delta", third[0]);
	}

	@Test
	public void testSpecimenCachePutGetInvalidate() {
		Specimen s = new Specimen();
		s.setSpecimenId(12345L);
		s.setBarcode("TEST_BARCODE_001");
		s.setFamily("Papilionidae");

		assertNull(SpecimenCache.get(12345L));

		SpecimenCache.put(s);
		Specimen retrieved = SpecimenCache.get(12345L);
		assertNotNull(retrieved);
		assertEquals(12345L, (long) retrieved.getSpecimenId());
		assertEquals("TEST_BARCODE_001", retrieved.getBarcode());

		SpecimenCache.invalidate(12345L);
		assertNull(SpecimenCache.get(12345L));
	}
}
