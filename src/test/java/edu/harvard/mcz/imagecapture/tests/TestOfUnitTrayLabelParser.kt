/**
 * TestOfUnitTrayLabelParser.java
 * edu.harvard.mcz.imagecapture.tests
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
 */
package edu.harvard.mcz.imagecapture.tests

import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.UnitTrayLabelParser
import junit.framework.TestCase

/**
 * TestOfUnitTrayLabelParser
 */
class TestOfUnitTrayLabelParser
/**
 * @param name
 */(name: String?) : TestCase(name) {
    private var originalRegexDrawerNumber: String? = null
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        // Store the currently locally configured regex for the drawer number,
        originalRegexDrawerNumber = Singleton.SingletonInstance.Properties.Properties.getProperty(ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER)
        // then set to the default value (which the tests expect) while running the tests.
        Singleton.SingletonInstance.Properties.Properties.setProperty(ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER, ImageCaptureApp.REGEX_DRAWERNUMBER)
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
        // Reset the regex for the drawer number to not lose local properties settings from testing.
        Singleton.SingletonInstance.Properties.Properties.setProperty(ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER, originalRegexDrawerNumber)
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.UnitTrayLabelParser.UnitTrayLabelParser].
     */
    fun testUnitTrayLabelParser_Species() {
        var label = "    Lycaenidae: Theclinae \n    Ministrymon una\n    (Hewitson, 1873)\n  115.13\n"
        var parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae: Tribe \n    Ministrymon una\n    (Hewitson, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("Tribe", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // tribe on second line
        label = "    Lycaenidae: Theclinae: \n Tribe \n    Ministrymon una\n    (Hewitson, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("Tribe", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Tribe on first line with questionable genus
        label = "    Lycaenidae: Theclinae: Tribe \n    Ministrymon? una\n    Hewitson, 1873\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("Tribe", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("Hewitson, 1873", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // indeterminate with drawer x
        label = "    Lycaenidae : Theclinae  \n    Ministrymon sp. 2\n  \n  x\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("sp.", parser.SpecificEpithet)
        assertEquals("2", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("", parser.Authorship)
        assertEquals("x", parser.DrawerNumber)
        // Pathological case of a trailing colon in the higher taxonomy.
        label = "    Lycaenidae: Theclinae:  \n    Ministrymon? una\n    Hewitson, 1873\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("Hewitson, 1873", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Pathological case of a trailing colon in the higher taxonomy.
        label = "    Lycaenidae : Theclinae :  \n    Ministrymon? una\n    Hewitson, 1873\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("Hewitson, 1873", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
    }

    /**
     * Unit tests for parsing some variations of a trinomial subspecies epithet.
     */
    fun testUnitTrayLabelParser_Subpecies() {
        var label = "    Lycaenidae: Theclinae \n    Ministrymon una una\n    (Hewitson, 1873)\n  115.13\n"
        var parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("una", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae : Tribe\n    Ministrymon una  subsp.\n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("Tribe", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "Lycaenidae: Theclinae: \n    Ministrymon?  una  subsp. \n(Smith and Jones, 1873)  \n  115.13 \n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae : Tribe\n    Ministrymon longspecific \n subspecific \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("Tribe", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("longspecific", parser.SpecificEpithet)
        assertEquals("subspecific", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae \n    Ministrymon longspecific \n subspecific \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("longspecific", parser.SpecificEpithet)
        assertEquals("subspecific", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae \n    Ministrymon longspecific var. \n variety \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("longspecific", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("var.", parser.InfraspecificRank)
        assertEquals("variety", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "    Lycaenidae: Theclinae \n    Ministrymon longspecific \n var. variety \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("longspecific", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("var.", parser.InfraspecificRank)
        assertEquals("variety", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
    }

    fun testUnitTrayLabelParser_trinomial() {
        var label = "Lycaenidae: Theclinae: \n    Ministrymon una var. varietal \n(Smith and Jones, 1873)  \n  115.13 \n"
        var parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("var.", parser.InfraspecificRank)
        assertEquals("varietal", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "Lycaenidae: Theclinae: \n    Ministrymon una forma formname \n(Smith and Jones, 1873)  \n  115.13 \n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("forma", parser.InfraspecificRank)
        assertEquals("formname", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Test to with a subspecies trinomial.
        label = "Lycaenidae: Theclinae: \n    Ministrymon una subspecies \n(Smith and Jones, 1873)  \n  115.13 \n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subspecies", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
    }

    /**
     * Unit tests for parsing some pathologies in the formation of labels.
     */
    fun testUnitTrayLabelParser_Pathologies() {
        var label = "Lycaen idae: Theclinae: \n    Ministrymon?  una  subsp. \n(Smith and Jones, 1873)  \n  115.13 \n"
        var parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "Lycaenidae: Thecl inae \n    Ministrymon?  una  subsp. \n   (Smith and Jones, 1873)  \n115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Space after first letter of generic name
        label = "Lycaenidae: Theclinae \n    M inistrymon  una  \n   (Smith and Jones, 1873)  \n115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Space after first letter of generic name
        label = "Lycaenidae: Theclinae \n    M inistrymon  una subspecies \n   (Smith and Jones, 1873)  \n115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subspecies", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // Space after first letter of generic name
        label = "Lycaenidae: Theclinae \n    M inistrymon  una var. variety \n   (Smith and Jones, 1873)  \n115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("var.", parser.InfraspecificRank)
        assertEquals("variety", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // no drawer number
        label = "Lycaenidae: Theclinae \n    Ministrymon?  una  subsp. \n   (Smith and Jones, 1873)  \n\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("", parser.DrawerNumber)
        label = "Lycaenidae: Theclinae \n    Ministrymon?  una  subsp. \n   (Smith and Jones, 1873)  \n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("", parser.DrawerNumber)
        label = "Lycaenidae: Theclinae \n    Ministrymon?  una  subsp. \n   (Smith and Jones, 1873)"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon?", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("subsp.", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("", parser.DrawerNumber)
        // other missing elements
        label = "Lycaenidae: Theclinae \n    Ministrymon  una "
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("", parser.Authorship)
        assertEquals("", parser.DrawerNumber)
        label = "Lycaenidae: Theclinae"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("", parser.Genus)
        assertEquals("", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("", parser.Authorship)
        assertEquals("", parser.DrawerNumber)
        // bad OCR of first line
        label = "    Lycae22222 Thecl2222 \n    Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycae22222", parser.Family)
        assertEquals("Thecl2222", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "  A \n  Lycae22222 Thecl2222 \n    Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("A", parser.Family)
        assertEquals("", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        label = "  A  \n    Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("A", parser.Family)
        assertEquals("", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // leading first line before higher taxon names line
        label = "  A \n  Lycaenidae: Theclinae \n    Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // blank line before species group name line
        label = " \n Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("", parser.Family)
        assertEquals("", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // no line before species group name line
        label = "Ministrymon una  \n    (Smith and Jones, 1873)\n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Ministrymonuna", parser.Family)
        assertEquals("", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymon", parser.Genus)
        assertEquals("una", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // no line before species group name line and no author line
        label = "Ministrymon sp. \n  115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Ministrymonsp.", parser.Family)
        assertEquals("", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("", parser.Genus)
        assertEquals("", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("Ministrymon sp.", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // no space in species group name
        label = "Ministrymon sp. \n  115.13\n"
        parser = UnitTrayLabelParser(label)
        label = "Lycaenidae: Theclinae \n    Ministrymonuna \n   (Smith and Jones, 1873)  \n115.13\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Ministrymonuna", parser.Genus)
        assertEquals("", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Smith and Jones, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
        // tribe on second line, colon missing
        label = "    Lycaenidaxx Theclinae: \n Tribe \n    M inistrymon una\n    (Hewitson, 1873)\n  115.15\n"
        parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidaxx", parser.Family)
        //assertEquals("Theclinae",parser.Subfamily);
        assertEquals("", parser.Tribe)
        //assertEquals("Ministrymon",parser.Genus);
//assertEquals("una",parser.SpecificEpithet);
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.15", parser.DrawerNumber)
    }

    fun testGenusLeadingBracket() {
        val label = "    Lycaenidae: Theclinae \n    [Aricia agestis\n    (Hewitson, 1873)\n  115.13\n"
        val parser = UnitTrayLabelParser(label)
        assertEquals("Lycaenidae", parser.Family)
        assertEquals("Theclinae", parser.Subfamily)
        assertEquals("", parser.Tribe)
        assertEquals("Aricia", parser.Genus)
        assertEquals("agestis", parser.SpecificEpithet)
        assertEquals("", parser.SubspecificEpithet)
        assertEquals("", parser.InfraspecificRank)
        assertEquals("", parser.InfraspecificEpithet)
        assertEquals("(Hewitson, 1873)", parser.Authorship)
        assertEquals("115.13", parser.DrawerNumber)
    }
}
