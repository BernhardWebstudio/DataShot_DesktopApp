package edu.harvard.mcz.imagecapture.entity.fixed


import edu.harvard.mcz.imagecapture.interfaces.ValueLister

/**
 * LifeStage authority list of values of LifeStage to populate picklists.
 */
class LifeStage : ValueLister {
    val values: Array<String?>?
        get() = lifeStageValues

    companion object {
        val lifeStageValues: Array<String?>?
            get() = arrayOf(
                    "adult", "callow", "egg", "juvenile",
                    "larva", "naiad", "non-adult", "nymph",
                    "pharate", "pupa", "teneral adult", "teneral nymph",
                    "1st instar", "2nd instar", "3rd instar", "4th instar",
                    "5th instar", "6th instar", "7th instar")
    }
}
