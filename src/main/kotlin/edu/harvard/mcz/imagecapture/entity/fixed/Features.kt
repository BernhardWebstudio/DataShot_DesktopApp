/**
 *
 */
package edu.harvard.mcz.imagecapture.entity.fixed


/**
 * Features, values to populate picklist of possible features of specimens.
 */
object Features {
    val featuresValues: Array<String?>?
        get() = arrayOf(
                "", "abberation", "melanic", "eclosion defect",
                "runt", "deformed", "faded colours", "scales stripped for venation",
                "greasy", "stained", "psocid damaged")
}
