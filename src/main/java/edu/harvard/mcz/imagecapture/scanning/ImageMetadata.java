/**
 * ImageMetadata.java
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

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.properties.XMPProperty;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.xmp.XmpDirectory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Immutable snapshot of EXIF, XMP, and JPEG dimension metadata read in a single
 * pass from an image file.
 */
public final class ImageMetadata {

	private static final Logger log = LoggerFactory.getLogger(ImageMetadata.class);
	private static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/";
	private static final String DC_DESCRIPTION = "dc:description";

	private final String userComment;
	private final String xmpDescription;
	private final Date dateCreated;
	private final int width;
	private final int height;
	private final int orientation;

	/**
	 * Creates an immutable {@link ImageMetadata} instance.
	 *
	 * @param userComment
	 *            EXIF UserComment text (normalized to {@code ""} if null)
	 * @param xmpDescription
	 *            XMP {@code dc:description} text (normalized to {@code ""} if null)
	 * @param dateCreated
	 *            EXIF creation timestamp, or {@code null} if unavailable
	 * @param width
	 *            image width in pixels from metadata, or {@code 0} if unknown
	 * @param height
	 *            image height in pixels from metadata, or {@code 0} if unknown
	 * @param orientation
	 *            EXIF orientation tag value, or {@code 0} if unknown
	 */
	public ImageMetadata(String userComment, String xmpDescription, Date dateCreated, int width, int height,
			int orientation) {
		this.userComment = userComment == null ? "" : userComment;
		this.xmpDescription = xmpDescription == null ? "" : xmpDescription;
		this.dateCreated = dateCreated == null ? null : new Date(dateCreated.getTime());
		this.width = width;
		this.height = height;
		this.orientation = orientation;
	}

	/**
	 * Returns an empty metadata instance with no comment, no date, and zero
	 * dimensions/orientation.
	 *
	 * @return empty metadata
	 */
	public static ImageMetadata empty() {
		return new ImageMetadata("", "", null, 0, 0, 0);
	}

	/**
	 * Reads EXIF, XMP, and dimension metadata from a path in a single pass. Missing
	 * directories, empty files, or unreadable/corrupt files return an
	 * {@link ImageMetadata} with available fields populated and never throw
	 * {@link NullPointerException}.
	 *
	 * @param path
	 *            image file path
	 * @return extracted metadata (or {@link #empty()} on error/missing file)
	 */
	public static ImageMetadata readFrom(Path path) {
		if (path == null) {
			return empty();
		}
		return readFrom(path.toFile());
	}

	/**
	 * Reads EXIF, XMP, and dimension metadata from a file in a single pass.
	 *
	 * @param file
	 *            image file
	 * @return extracted metadata (or {@link #empty()} on error/missing file)
	 */
	public static ImageMetadata readFrom(File file) {
		if (file == null || !file.isFile() || !file.canRead()) {
			return empty();
		}
		try {
			if (Files.size(file.toPath()) == 0L) {
				return empty();
			}
		} catch (IOException e) {
			log.debug("Unable to stat metadata file {}: {}", file.getName(), e.getMessage());
			return empty();
		}

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			if (metadata == null) {
				return empty();
			}

			String userComment = "";
			Date dateCreated = null;
			ExifSubIFDDirectory exifSub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if (exifSub != null) {
				ExifSubIFDDescriptor descriptor = new ExifSubIFDDescriptor(exifSub);
				String uc = descriptor.getUserCommentDescription();
				if (uc != null) {
					userComment = uc;
				}
				dateCreated = exifSub.getDate(ExifSubIFDDirectory.TAG_DATETIME);
				if (dateCreated == null) {
					dateCreated = exifSub.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				}
				if (dateCreated == null) {
					dateCreated = exifSub.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
				}
			}

			String xmpDescription = "";
			XmpDirectory xmpDirectory = metadata.getFirstDirectoryOfType(XmpDirectory.class);
			if (xmpDirectory != null && xmpDirectory.getXMPMeta() != null) {
				XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
				try {
					if (xmpMeta.doesPropertyExist(DC_NAMESPACE, DC_DESCRIPTION)
							&& xmpMeta.countArrayItems(DC_NAMESPACE, DC_DESCRIPTION) >= 1) {
						XMPProperty descriptionProp = xmpMeta.getArrayItem(DC_NAMESPACE, DC_DESCRIPTION, 1);
						if (descriptionProp != null && descriptionProp.getValue() != null) {
							xmpDescription = descriptionProp.getValue();
						}
					}
				} catch (XMPException | RuntimeException e) {
					log.debug("Unable to read XMP dc:description from {}: {}", file.getName(), e.getMessage());
				}
			}

			int orientation = 0;
			ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (ifd0 != null && ifd0.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				try {
					orientation = ifd0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
				} catch (MetadataException e) {
					log.debug("Unable to read EXIF orientation from {}: {}", file.getName(), e.getMessage());
				}
			}

			int width = 0;
			int height = 0;
			JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
			if (jpegDirectory != null) {
				try {
					if (jpegDirectory.containsTag(JpegDirectory.TAG_IMAGE_WIDTH)) {
						width = jpegDirectory.getImageWidth();
					}
					if (jpegDirectory.containsTag(JpegDirectory.TAG_IMAGE_HEIGHT)) {
						height = jpegDirectory.getImageHeight();
					}
				} catch (MetadataException e) {
					log.debug("Unable to read JPEG dimensions from {}: {}", file.getName(), e.getMessage());
				}
			}

			return new ImageMetadata(userComment, xmpDescription, dateCreated, width, height, orientation);
		} catch (ImageProcessingException | IOException | RuntimeException e) {
			log.debug("Error reading metadata from {}: {}", file.getName(), e.getMessage());
			return empty();
		}
	}

	/**
	 * @return EXIF UserComment text (never {@code null})
	 */
	public String getUserComment() {
		return userComment;
	}

	/**
	 * @return XMP {@code dc:description} text (never {@code null})
	 */
	public String getXmpDescription() {
		return xmpDescription;
	}

	/**
	 * @return defensive copy of the creation date, or {@code null} if unavailable
	 */
	public Date getDateCreated() {
		return dateCreated == null ? null : new Date(dateCreated.getTime());
	}

	/**
	 * @return image width in pixels from metadata, or {@code 0} if unknown
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return image height in pixels from metadata, or {@code 0} if unknown
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return EXIF orientation tag value, or {@code 0} if unknown
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Returns {@link #getUserComment()} if non-blank, otherwise
	 * {@link #getXmpDescription()} if non-blank, otherwise {@code ""}.
	 *
	 * @return effective comment text (never {@code null})
	 */
	public String effectiveUserComment() {
		if (!userComment.isBlank()) {
			return userComment;
		}
		if (!xmpDescription.isBlank()) {
			return xmpDescription;
		}
		return "";
	}

	/**
	 * Formats {@link #getDateCreated()} using the supplied {@link SimpleDateFormat}
	 * pattern.
	 *
	 * @param pattern
	 *            date format pattern
	 * @return formatted date string, or {@code null} if no date is available or
	 *         pattern is blank
	 */
	public String formatDateCreated(String pattern) {
		if (dateCreated == null || pattern == null || pattern.isBlank()) {
			return null;
		}
		return new SimpleDateFormat(pattern).format(dateCreated);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ImageMetadata other)) {
			return false;
		}
		return width == other.width && height == other.height && orientation == other.orientation
				&& Objects.equals(userComment, other.userComment)
				&& Objects.equals(xmpDescription, other.xmpDescription)
				&& Objects.equals(dateCreated, other.dateCreated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userComment, xmpDescription, dateCreated, width, height, orientation);
	}

	@Override
	public String toString() {
		return "ImageMetadata{userComment='" + userComment + "', xmpDescription='" + xmpDescription + "', dateCreated="
				+ dateCreated + ", width=" + width + ", height=" + height + ", orientation=" + orientation + '}';
	}
}
