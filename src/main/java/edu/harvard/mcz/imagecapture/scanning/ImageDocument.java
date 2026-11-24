/**
 * ImageDocument.java
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

import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Lazy, thread-safe handle to an image on disk or in memory. Construction
 * performs zero disk I/O and zero scanning; decoded pixels and single-pass
 * metadata are loaded on demand and cached for reuse.
 */
public final class ImageDocument {

	private final Path path;
	private final Object imageLock = new Object();
	private final Object metadataLock = new Object();

	private volatile BufferedImage cachedImage;
	private volatile boolean imageLoaded;
	private volatile ImageMetadata cachedMetadata;

	/**
	 * Creates a file-backed {@link ImageDocument} without performing any I/O.
	 *
	 * @param path
	 *            image file path
	 */
	public ImageDocument(Path path) {
		this(path, null, false, null);
	}

	private ImageDocument(Path path, BufferedImage inMemoryImage, boolean imageLoaded, ImageMetadata metadata) {
		this.path = path;
		this.cachedImage = inMemoryImage;
		this.imageLoaded = imageLoaded;
		this.cachedMetadata = metadata;
	}

	/**
	 * Creates a file-backed {@link ImageDocument} for the given path.
	 *
	 * @param path
	 *            image path
	 * @return document handle
	 */
	public static ImageDocument of(Path path) {
		return new ImageDocument(path);
	}

	/**
	 * Creates a file-backed {@link ImageDocument} for the given {@link File}.
	 *
	 * @param file
	 *            image file
	 * @return document handle
	 */
	public static ImageDocument of(File file) {
		return new ImageDocument(file == null ? null : file.toPath());
	}

	/**
	 * Creates an in-memory {@link ImageDocument} backed directly by the given
	 * {@link BufferedImage}.
	 *
	 * @param image
	 *            in-memory image
	 * @return in-memory document handle
	 */
	public static ImageDocument inMemory(BufferedImage image) {
		Objects.requireNonNull(image, "image must not be null");
		ImageMetadata metadata = new ImageMetadata("", "", null, image.getWidth(), image.getHeight(), 0);
		return new ImageDocument(null, image, true, metadata);
	}

	/**
	 * @return backing path, or {@code null} for in-memory documents
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @return backing {@link File}, or {@code null} for in-memory documents
	 */
	public File getFile() {
		return path == null ? null : path.toFile();
	}

	/**
	 * @return {@code true} if this document is backed by an in-memory image rather
	 *         than a path
	 */
	public boolean isInMemory() {
		return path == null;
	}

	/**
	 * Verifies that the backing file exists and is readable (or that this is a
	 * valid in-memory image).
	 *
	 * @throws UnreadableFileException
	 *             if the file is missing, null, or unreadable
	 */
	public void ensureReadable() throws UnreadableFileException {
		if (path == null) {
			if (imageLoaded && cachedImage != null) {
				return;
			}
			throw new UnreadableFileException("No file path provided for ImageDocument.");
		}
		try {
			if (!Files.exists(path)) {
				throw new UnreadableFileException("No such file as: " + path.toAbsolutePath());
			}
			if (!Files.isReadable(path)) {
				throw new UnreadableFileException("Can't read file: " + path.toAbsolutePath());
			}
		} catch (SecurityException e) {
			throw new UnreadableFileException(
					"Can't read file: " + path.toAbsolutePath() + " Security problem. " + e.getMessage());
		}
	}

	/**
	 * Lazily decodes and caches the image via {@link ImageIO#read(File)} at most
	 * once in a thread-safe manner.
	 *
	 * @return decoded {@link BufferedImage}, or {@code null} if {@code ImageIO}
	 *         cannot decode the file
	 * @throws IOException
	 *             if reading from disk fails
	 */
	public BufferedImage getImage() throws IOException {
		if (!imageLoaded) {
			synchronized (imageLock) {
				if (!imageLoaded) {
					if (path != null) {
						cachedImage = ImageIO.read(path.toFile());
					}
					imageLoaded = true;
				}
			}
		}
		return cachedImage;
	}

	/**
	 * Lazily reads and caches {@link ImageMetadata} in a single pass.
	 *
	 * @return immutable {@link ImageMetadata}
	 */
	public ImageMetadata getMetadata() {
		ImageMetadata result = cachedMetadata;
		if (result == null) {
			synchronized (metadataLock) {
				result = cachedMetadata;
				if (result == null) {
					if (path != null) {
						result = ImageMetadata.readFrom(path);
					} else if (cachedImage != null) {
						result = new ImageMetadata("", "", null, cachedImage.getWidth(), cachedImage.getHeight(), 0);
					} else {
						result = ImageMetadata.empty();
					}
					cachedMetadata = result;
				}
			}
		}
		return result;
	}
}
