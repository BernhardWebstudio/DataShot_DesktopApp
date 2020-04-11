/**
 *
 */
package edu.harvard.mcz.imagecapture.entity.fixed


/**
 * Sex provides an authority file of values for the field Sex that can be used
 * to populate picklists.
 *
 *
 */
object Sex {
    val sexValues: Array<String?>?
        get() = arrayOf(
                "", "Male", "Female", "NotApplicable", "Gynandromorph",
                "Intersex", "unknown")
}
