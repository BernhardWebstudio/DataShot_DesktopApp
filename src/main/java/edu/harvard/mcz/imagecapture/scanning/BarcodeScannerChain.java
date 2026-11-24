/**
 * BarcodeScannerChain.java
 * edu.harvard.mcz.imagecapture.scanning
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
 * <p>
 */
package edu.harvard.mcz.imagecapture.scanning;

import edu.harvard.mcz.imagecapture.Singleton;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ordered chain of {@link BarcodeScanner} strategies supporting single-pass
 * full-image detection ({@link #scanAll(BufferedImage, ScanOptions)}) and
 * targeted region scanning
 * ({@link #scanRegion(BufferedImage, Rectangle, ScanOptions)}).
 */
public class BarcodeScannerChain {

	private static final Logger log = LoggerFactory.getLogger(BarcodeScannerChain.class);

	private final List<BarcodeScanner> scanners;

	/**
	 * Creates a chain from an ordered list of scanners.
	 *
	 * @param scanners
	 *            ordered scanners to try
	 */
	public BarcodeScannerChain(List<BarcodeScanner> scanners) {
		Objects.requireNonNull(scanners, "scanners must not be null");
		this.scanners = List.copyOf(scanners);
	}

	/**
	 * Creates the default scanner chain from {@link Singleton} properties.
	 *
	 * @return configured scanner chain
	 */
	public static BarcodeScannerChain fromSingleton() {
		Properties properties;
		try {
			properties = Singleton.getSingletonInstance().getProperties().getProperties();
		} catch (Exception e) {
			properties = new Properties();
		}
		return defaultChain(properties);
	}

	/**
	 * Creates the default ordered chain for full-image and region scans: optional
	 * native helper, optional Chromium helper, in-process ZXing, in-process BoofCV,
	 * and optional Python fallback.
	 *
	 * @param properties
	 *            application properties
	 * @return default scanner chain
	 */
	public static BarcodeScannerChain defaultChain(Properties properties) {
		ExternalProcessRunner runner = ExternalProcessRunner.shared();
		return new BarcodeScannerChain(
				List.of(new NativeBarcodeScanner(runner, properties), new ChromiumBarcodeScanner(runner, properties),
						new ZxingBarcodeScanner(), new BoofCvBarcodeScanner(), new PythonBarcodeScanner()));
	}

	/**
	 * Scans the entire image using available scanners in order until detections are
	 * found.
	 *
	 * @param image
	 *            full image
	 * @param options
	 *            scan options (preferredRegion is cleared for full-image scan)
	 * @return detections found by the first succeeding scanner, or empty list
	 */
	public List<BarcodeDetection> scanAll(BufferedImage image, ScanOptions options) {
		if (image == null) {
			return List.of();
		}
		ScanOptions fullImageOptions = (options == null ? ScanOptions.defaults() : options).toBuilder()
				.preferredRegion(null).build();
		for (BarcodeScanner scanner : scanners) {
			if (!scanner.isAvailable()) {
				continue;
			}
			try {
				List<BarcodeDetection> detections = scanner.detect(image, fullImageOptions);
				if (detections != null && !detections.isEmpty()) {
					log.debug("Scanner '{}' found {} full-image detection(s)", scanner.id(), detections.size());
					return detections;
				}
			} catch (Exception e) {
				log.debug("Scanner '{}' failed during full-image scan: {}", scanner.id(), e.getMessage());
			}
		}
		return List.of();
	}

	/**
	 * Scans a targeted {@code region} of {@code image}, using preprocessing retries
	 * on in-process scanners and falling back to remaining available scanners.
	 *
	 * @param image
	 *            full image
	 * @param region
	 *            target region in image coordinates
	 * @param options
	 *            base scan options
	 * @return detections found for the region, or empty list
	 */
	public List<BarcodeDetection> scanRegion(BufferedImage image, Rectangle region, ScanOptions options) {
		if (image == null || region == null) {
			return List.of();
		}
		ScanOptions effectiveBase = options == null ? ScanOptions.defaults() : options;
		ScanOptions regionOptions = effectiveBase.toBuilder().preferredRegion(region).build();

		for (BarcodeScanner scanner : scanners) {
			if (!scanner.isAvailable()) {
				continue;
			}
			// In quickCheck mode, skip heavy external process fallbacks
			if (effectiveBase.quickCheck() && isExternalScanner(scanner)) {
				continue;
			}
			try {
				List<BarcodeDetection> detections = scanner.detect(image, regionOptions);
				if (detections != null && !detections.isEmpty()) {
					log.debug("Scanner '{}' found {} region detection(s) in {}", scanner.id(), detections.size(),
							region);
					return detections;
				}
			} catch (Exception e) {
				log.debug("Scanner '{}' failed during region scan: {}", scanner.id(), e.getMessage());
			}
		}
		return List.of();
	}

	/**
	 * @return unmodifiable view of configured scanners
	 */
	public List<BarcodeScanner> getScanners() {
		return new ArrayList<>(scanners);
	}

	private boolean isExternalScanner(BarcodeScanner scanner) {
		String id = scanner.id();
		return "native".equals(id) || "chromium".equals(id) || "python".equals(id);
	}
}
