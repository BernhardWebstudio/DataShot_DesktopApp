/**
 * BarcodeReadTest.java
 * edu.harvard.mcz.imagecapture
 * <p>
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
package edu.harvard.mcz.imagecapture;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Allows testing of barcode detection by ZXing in an image file.
 */
public class BarcodeReadTest {
    private static final Logger log =
            LoggerFactory.getLogger(BarcodeReadTest.class);

    /**
     * Launches the application, checking one image file for a barcode.
     *
     * @param args takes filename to read as the first argument.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("java BarcodeReadTest filename");
        } else {
            String filename = args[0];
            File file = new File(filename);
            try {
                log.debug("Debug {}", file.getCanonicalPath());
                BufferedImage image = ImageIO.read(file);
                System.out.println(checkForBarcode(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String checkForBarcode(BufferedImage image) {
        // Check the entire image for a barcode and return.
        String returnValue = "";
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Result result;
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            QRCodeReader reader = new QRCodeReader();
            Hashtable<DecodeHintType, Object> hints = null;
            hints = new Hashtable<DecodeHintType, Object>(3);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            result = reader.decode(bitmap, hints);
            returnValue = result.getText();
        } catch (ReaderException e) {
            e.printStackTrace();
            returnValue = "";
        }
        return returnValue;
    }
}
