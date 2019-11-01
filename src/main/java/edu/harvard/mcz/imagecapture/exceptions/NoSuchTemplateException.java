/**
 *
 */
package edu.harvard.mcz.imagecapture.exceptions;

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
public class NoSuchTemplateException extends Exception {

    private static final long serialVersionUID = 8722765693873368902L;

    public NoSuchTemplateException() {
        super("NoSuchTemplateException");
    }

    public NoSuchTemplateException(String message) {
        super(message);
    }

    public NoSuchTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchTemplateException(Throwable cause) {
        super(cause);
    }
}
