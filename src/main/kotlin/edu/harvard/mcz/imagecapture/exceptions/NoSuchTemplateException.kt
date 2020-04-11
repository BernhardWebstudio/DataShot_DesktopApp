/**
 *
 */
package edu.harvard.mcz.imagecapture.exceptions


/**
 * NoSuchTemplateException to be thrown when trying to construct a
 * PositionTemplate using a templateId that does not exist.  This exception
 * should be thrown from problems in constructing a PositionTemplate, as opposed
 * to BadTemplateException which should be thrown from problems in using a
 * template.
 *
 * see: edu.harvard.mcz.imagecapture.exceptions.BadTemplateException.
 *
 *
 *
 */
class NoSuchTemplateException : Exception {
    constructor() : super("NoSuchTemplateException") {}
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        private const val serialVersionUID = 8722765693873368902L
    }
}
