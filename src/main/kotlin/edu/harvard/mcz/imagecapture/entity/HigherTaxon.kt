package edu.harvard.mcz.imagecapture.entity


import java.io.Serializable

/**
 * HigherTaxon, authority file of Family, Subfamily, and Tribe names.
 */
class HigherTaxon : Serializable {
    var id: Int? = null
    var family: String? = null
    var subfamily: String? = null
    var tribe: String? = null
    var hasCastes = 0

    constructor() {}
    constructor(family: String?, subfamily: String?, tribe: String?) {
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
    }

    companion object {
        private const val serialVersionUID = -5729385642306510832L
    }
}
