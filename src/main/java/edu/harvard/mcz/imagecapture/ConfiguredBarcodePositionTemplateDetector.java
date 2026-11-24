/**
 * ConfiguredBarcodePositionTemplateDetector.java
 * <p>
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector;
import edu.harvard.mcz.imagecapture.scanning.ImageDocument;
import edu.harvard.mcz.imagecapture.scanning.TemplateResolver;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfiguredBarcodePositionTemplateDetector finds a template by the position of
 * a catalog number barcode in an image file (without requiring a
 * {@link CandidateImageFile} instance), checking for a barcode that matches the
 * configured catalog pattern.
 *
 * @see edu.harvard.mcz.imagecapture.PositionTemplate
 */
public class ConfiguredBarcodePositionTemplateDetector implements PositionTemplateDetector {

	private static final Logger log = LoggerFactory.getLogger(ConfiguredBarcodePositionTemplateDetector.class);

	@Override
	public String detectTemplateForImage(File anImageFile) throws UnreadableFileException {
		return detectTemplateForImage(anImageFile, null, false);
	}

	@Override
	public String detectTemplateForImage(CandidateImageFile scannableFile) throws UnreadableFileException {
		if (scannableFile == null) {
			throw new UnreadableFileException("Unable to detect template for null CandidateImageFile.");
		}
		return detectTemplateForImage(scannableFile.getFile(), scannableFile, false);
	}

	protected String detectTemplateForImage(File anImageFile, CandidateImageFile scannableFile, boolean quickCheck)
			throws UnreadableFileException {
		ImageDocument document = (scannableFile != null && scannableFile.getImageDocument() != null)
				? scannableFile.getImageDocument()
				: ImageDocument.of(anImageFile);
		document.ensureReadable();

		String defaultTemplate = Singleton.getSingletonInstance().getProperties().getProperties()
				.getProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES);
		if (defaultTemplate != null && !defaultTemplate.isBlank()
				&& !PositionTemplate.TEMPLATE_DEFAULT.equals(defaultTemplate.trim())
				&& Singleton.getSingletonInstance().getProperties().testDefaultTemplate()) {
			return defaultTemplate.trim();
		}

		log.debug("Detecting template for file: {} (quickCheck={})",
				anImageFile != null ? anImageFile.getAbsolutePath() : document.getPath(), quickCheck);

		PositionTemplate resolved = TemplateResolver.fromSingleton().resolveTemplate(document, quickCheck);
		return resolved.getTemplateId();
	}
}
