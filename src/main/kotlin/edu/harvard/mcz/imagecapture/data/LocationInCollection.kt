/**
 * LocationInCollection.java
 *
 *
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 */
package edu.harvard.mcz.imagecapture.data


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton

/**
 * Controled vocabulary for Specimen.locationInCollection, storage and workflow
 * indicator for types, general collection, slides, etc.
 */
object LocationInCollection {
    val GENERALANT: String? = "General Ant Collection"
    internal val GENERAL: String? = "General Lepidoptera Collection"
    internal val PALEARCTIC: String? = "Palaearctic Lepidoptera Collection"// TODO: make list available as configuration (or from a database table)
    /**
     * Obtain a list of default location in collection values to use for picklists
     * of collections.
     *
     * @return a string array of values for the location in collection suitable
     * for populating a picklist.
     */
    val locationInCollectionValues: Array<String?>?
        get() { // TODO: make list available as configuration (or from a database table)
            val configuredcollection: String = Singleton
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.Companion.KEY_SPECIFIC_COLLECTION)
            val coll: String = Singleton
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
            return if (configuredcollection == null ||
                    configuredcollection.trim { it <= ' ' }.length == 0) {
                if (coll == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) {
                    arrayOf("ETH Entomological Collection", "Type Collection")
                } else {
                    arrayOf(GENERAL, GENERALANT, "Type Collection",
                            "Nabokov Collection", "Slide Collection")
                }
            } else {
                if (coll == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) {
                    arrayOf(configuredcollection,
                            "Palaearctic Lepidoptera Collection")
                } else {
                    arrayOf(configuredcollection, GENERAL,
                            GENERALANT, "Type Collection",
                            "Nabokov Collection", "Slide Collection")
                }
            }
        }

    /**
     * Obtain the configured value for the default collection, either the default
     * value implied by ImageCaptureProperties.KEY_COLLECTION or, if provided, the
     * specific value obtained from
     * ImageCaptureProperties.KEY_SPECIFIC_COLLECTION.
     *
     * @return string value for the default location in collection.
     */
    val defaultLocation: String?
        get() {
            val value: String = Singleton
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.Companion.KEY_SPECIFIC_COLLECTION)
            if (value != null && value.trim { it <= ' ' }.length > 0) {
                return value
            }
            val coll: String = Singleton
                    .getProperties()
                    .getProperties()
                    .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
            return if (coll == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) {
                PALEARCTIC
            } else {
                GENERAL
            }
        }
}
