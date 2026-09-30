package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.jobs.AtomicCounter;
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import org.junit.Test;

public class TestAtomicCounter extends TestCase {

	@Test
	public void testConcurrentBarcodeLogging() throws InterruptedException {
		AtomicCounter counter = new AtomicCounter();
		int threadCount = 10;
		int barcodesPerThread = 100;
		int totalBarcodes = threadCount * barcodesPerThread;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);

		for (int t = 0; t < threadCount; t++) {
			final int threadIdx = t;
			executor.submit(() -> {
				try {
					startLatch.await();
					for (int i = 0; i < barcodesPerThread; i++) {
						counter.logBarcode(String.format("ETHZ-ENT%07d", threadIdx * barcodesPerThread + i));
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		startLatch.countDown();
		boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue("Threads timed out", completed);

		List<String> recordedBarcodes = counter.getBarcodes();
		assertEquals("All barcodes should be recorded without loss", totalBarcodes, recordedBarcodes.size());

		// Verify all expected barcodes exist
		Collections.sort(recordedBarcodes);
		for (int i = 0; i < totalBarcodes; i++) {
			assertEquals(String.format("ETHZ-ENT%07d", i), recordedBarcodes.get(i));
		}
	}

	@Test
	public void testConcurrentErrorLogging() throws InterruptedException {
		AtomicCounter counter = new AtomicCounter();
		int threadCount = 8;
		int errorsPerThread = 50;
		int totalErrors = threadCount * errorsPerThread;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);

		for (int t = 0; t < threadCount; t++) {
			final int threadIdx = t;
			executor.submit(() -> {
				try {
					startLatch.await();
					for (int i = 0; i < errorsPerThread; i++) {
						counter.appendError(new RunnableJobError("File" + threadIdx + "_" + i, null, "1",
								"Error message " + i, null, RunnableJobError.TYPE_LOAD_FAILED));
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		startLatch.countDown();
		boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue("Threads timed out", completed);

		List<RunnableJobError> errors = counter.getErrors();
		assertEquals("All errors should be recorded without loss", totalErrors, errors.size());
	}
}
