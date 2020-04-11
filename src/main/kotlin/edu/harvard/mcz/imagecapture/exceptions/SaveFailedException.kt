/**
 * SaveFailedException
 * edu.harvard.mcz.imagecapture.exceptions
 */
package edu.harvard.mcz.imagecapture.exceptions


/**
 * SaveFailedException can be thrown when an attempt to persist an instance of a
 * class as a database record fails.
 */
class SaveFailedException : Exception {
    /**
     *
     */
    constructor() : super() { // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) { // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    constructor(message: String?) : super(message) { // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    constructor(cause: Throwable?) : super(cause) { // TODO Auto-generated constructor stub
    }

    companion object {
        private const val serialVersionUID = -4963776101118122744L
    }
}
