/**
 * ExternalScannerSupport.java
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Package-private utilities shared across external-process barcode scanners
 * (JSON parsing, resource staging, and OS identification).
 */
final class ExternalScannerSupport {

	private static final Logger log = LoggerFactory.getLogger(ExternalScannerSupport.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final ConcurrentHashMap<String, Optional<String>> STAGED_RESOURCES = new ConcurrentHashMap<>();

	private ExternalScannerSupport() {
	}

	static String getOsKey() {
		String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
		if (osName.contains("mac")) {
			return "macos";
		}
		if (osName.contains("win")) {
			return "windows";
		}
		if (osName.contains("nux") || osName.contains("nix")) {
			return "linux";
		}
		return "unknown";
	}

	static Optional<String> resolveSupportFile(String[] localCandidates, String classpathResource, String tempPrefix,
			String tempSuffix) {
		return STAGED_RESOURCES.computeIfAbsent(classpathResource, key -> {
			for (String candidate : localCandidates) {
				if (candidate == null || candidate.isBlank()) {
					continue;
				}
				Path path = Paths.get(candidate);
				if (Files.exists(path)) {
					return Optional.of(path.toString());
				}
			}
			return copyClasspathResourceToTempFile(classpathResource, tempPrefix, tempSuffix);
		});
	}

	static void clearStagedResourceCache() {
		STAGED_RESOURCES.clear();
	}

	private static Optional<String> copyClasspathResourceToTempFile(String resourceName, String tempPrefix,
			String tempSuffix) {
		try (InputStream stream = ExternalScannerSupport.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (stream == null) {
				return Optional.empty();
			}
			Path tempFile = Files.createTempFile(tempPrefix, tempSuffix);
			Files.copy(stream, tempFile, StandardCopyOption.REPLACE_EXISTING);
			tempFile.toFile().deleteOnExit();
			return Optional.of(tempFile.toString());
		} catch (IOException e) {
			log.debug("Unable to stage classpath resource {}: {}", resourceName, e.getMessage());
			return Optional.empty();
		}
	}

	static String extractJsonPayload(String output) {
		if (output == null) {
			return null;
		}
		String candidate = output.trim();
		if (candidate.startsWith("ERROR:")) {
			log.debug("External scanner reported error: {}", candidate);
			return null;
		}
		if (candidate.startsWith("JSON:")) {
			candidate = candidate.substring("JSON:".length()).trim();
		}
		if (candidate.startsWith("[") || candidate.startsWith("{")) {
			return candidate;
		}

		int objectStart = candidate.indexOf('{');
		int arrayStart = candidate.indexOf('[');
		int start = -1;
		if (objectStart >= 0 && arrayStart >= 0) {
			start = Math.min(objectStart, arrayStart);
		} else if (objectStart >= 0) {
			start = objectStart;
		} else if (arrayStart >= 0) {
			start = arrayStart;
		}
		int endObject = candidate.lastIndexOf('}');
		int endArray = candidate.lastIndexOf(']');
		int end = Math.max(endObject, endArray);
		if (start >= 0 && end > start) {
			return candidate.substring(start, end + 1).trim();
		}
		return null;
	}

	static List<BarcodeDetection> parseQrDetectionsFromJson(String json, String engine) {
		List<BarcodeDetection> detections = new ArrayList<>();
		if (json == null || json.isBlank()) {
			return detections;
		}
		try {
			JsonNode root = OBJECT_MAPPER.readTree(json);
			if (root.isArray()) {
				appendDetections(detections, root, engine);
			} else if (root.isObject() && root.has("detections") && root.get("detections").isArray()) {
				appendDetections(detections, root.get("detections"), engine);
			} else if (root.isObject() && root.has("barcodes") && root.get("barcodes").isArray()) {
				appendDetections(detections, root.get("barcodes"), engine);
			} else {
				BarcodeDetection detection = mapDetectionNode(root, engine);
				if (detection != null) {
					detections.add(detection);
				}
			}
		} catch (Exception e) {
			log.debug("Unable to parse {} scanner JSON: {}", engine, e.getMessage());
		}
		return detections;
	}

	private static void appendDetections(List<BarcodeDetection> target, JsonNode arrayNode, String engine) {
		for (JsonNode item : arrayNode) {
			BarcodeDetection detection = mapDetectionNode(item, engine);
			if (detection != null) {
				target.add(detection);
			}
		}
	}

	private static BarcodeDetection mapDetectionNode(JsonNode node, String engine) {
		if (node == null || node.isNull()) {
			return null;
		}
		if (node.isTextual()) {
			String text = node.asText();
			return text == null || text.isBlank() ? null : new BarcodeDetection(text, engine);
		}

		String text = null;
		if (node.has("text") && node.get("text").isTextual()) {
			text = node.get("text").asText();
		} else if (node.has("rawValue") && node.get("rawValue").isTextual()) {
			text = node.get("rawValue").asText();
		}
		if (text == null || text.isBlank()) {
			return null;
		}

		Rectangle bounds = null;
		JsonNode bbox = node.has("bbox") ? node.get("bbox") : node.get("boundingBox");
		if (bbox != null && bbox.isObject() && bbox.has("x") && bbox.has("y") && bbox.has("width")
				&& bbox.has("height")) {
			int x = (int) Math.round(bbox.get("x").asDouble());
			int y = (int) Math.round(bbox.get("y").asDouble());
			int w = Math.max(0, (int) Math.round(bbox.get("width").asDouble()));
			int h = Math.max(0, (int) Math.round(bbox.get("height").asDouble()));
			bounds = new Rectangle(x, y, w, h);
		}
		return new BarcodeDetection(text, bounds, engine);
	}
}
