/**
 *
 */
package edu.harvard.mcz.imagecapture.exceptions


/**
 * OCRReadException can be thrown when an implementation of the OCR interface
 * fails to OCR text from an image.
 *
 * @see edu.harvard.mcz.imagecapture.interfaces.OCR
 */
class OCRReadException : Exception {
    /**
     * default constructor
     */
    constructor() : super("OCRReadException") {}

    /**
     * @param message
     */
    constructor(message: String?) : super(message) {}

    /**
     * @param cause
     */
    constructor(cause: Throwable?) : super(cause) {}

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        private const val serialVersionUID = -2129432968655902781L
    }
}
