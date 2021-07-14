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

import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException;
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

/**
 * ConfiguredBarcodePositionTemplateDetector find a template by the position of
 * a barcode in an image file without recourse to construction of a a
 * CandidateImageFile instance, and checks for a catalog number barcode that
 * follows the configured pattern in the templated position, not just any
 * readable barcode. <p> This class makes the assumption that a template can be
 * uniquely identified by the location of the barcode in the image.  Each
 * template must have the barcode in a uniquely different place.
 *
 * @see edu.harvard.mcz.imagecapture.PositionTemplate
 */
public class ConfiguredBarcodePositionTemplateDetector
        implements PositionTemplateDetector {

    private static final Logger log =
            LoggerFactory.getLogger(ConfiguredBarcodePositionTemplateDetector.class);

    @Override
    public String detectTemplateForImage(File anImageFile)
            throws UnreadableFileException {
        return detectTemplateForImage(anImageFile, null, false);
    }

    @Override
    public String detectTemplateForImage(CandidateImageFile scannableFile)
            throws UnreadableFileException {
        return detectTemplateForImage(scannableFile.getFile(), scannableFile,
                false);
    }

    protected String detectTemplateForImage(File anImageFile,
                                            CandidateImageFile scannableFile,
                                            boolean quickCheck)
            throws UnreadableFileException {
        // Set default response if no template is found.
        String result = PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS;

        // Read the image file, if possible, otherwise throw exception.
        if (!anImageFile.canRead()) {
            throw new UnreadableFileException("Unable to read " +
                    anImageFile.getName());
        }

        // skip all following intensive, unnecessary computation if possible
        String defaultTemplate =
                Singleton.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES);
        if (Singleton.getSingletonInstance()
                .getProperties()
                .testDefaultTemplate() &&
                defaultTemplate != PositionTemplate.TEMPLATE_DEFAULT) {
            return defaultTemplate;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(anImageFile);
        } catch (IOException e) {
            throw new UnreadableFileException("IOException trying to read " +
                    anImageFile.getName());
        }

        // iterate through templates and check until the first template where a
        // barcode is found
        List<String> templates = PositionTemplate.getTemplateIds();

        log.debug("Detecting template for file: " + anImageFile.getAbsolutePath());
        log.debug("list of templates: " + templates);

        ListIterator<String> i = templates.listIterator();
        boolean found = false;
        while (i.hasNext() && !found) {
            try {
                // get the next template from the list
                PositionTemplate template = new PositionTemplate(i.next());
                log.debug("Testing template: " + template.getTemplateId());
                if (template.getTemplateId().equals(
                        PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
                    // skip, this is the default result if no other is found.
                } else {
                    if (image.getWidth() == template.getImageSize().getWidth()) {
                        // Check to see if the barcode is in the part of the template
                        // defined by getBarcodeULPosition and getBarcodeSize.
                        String text;
                        if (scannableFile == null) {
                            text = scannableFile.getBarcodeTextFromImage(image, template,
                                    quickCheck);
                        } else {
                            text = scannableFile.getBarcodeText(template);
                        }
                        log.debug("Found:[" + text + "] ");
                        if (text.length() > 0) {
                            // a barcode was scanned
                            // Check to see if it matches the expected pattern.
                            // Use the configured barcode matcher.
                            if (Singleton.getSingletonInstance()
                                    .getBarcodeMatcher()
                                    .matchesPattern(text)) {
                                found = true;
                                log.debug("Match to:" + template.getTemplateId());
                                result = template.getTemplateId();
                            }
                        }
                    } else {
                        log.debug("Skipping as template " + template.getTemplateId() +
                                " is not same size as image. ");
                    }
                }
            } catch (NoSuchTemplateException e) {
                // Ending up here means a serious error in PositionTemplate
                // as the list of position templates returned by getTemplates() includes
                // an entry that isn't recognized as a valid template.
                log.error(
                        "Fatal error.  PositionTemplate.getTemplates() includes an item that isn't a valid template.");
                log.trace("Trace", e);
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR);
            }
        }
        return result;
    }
}
