/**
 * TestOfUnitTrayLabel.java
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

import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import junit.framework.TestCase

/**
 * TestOfUnitTrayLabel
 */
class TestOfUnitTrayLabel
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [UnitTrayLabel.toJSONString].
     */
    fun testToJSONString() {
        val u = UnitTrayLabel()
        u.setFamily("Family")
        var json: String = u.toJSONString()
        assertEquals("{ \"f\":\"Family\", \"b\":\"\", \"t\":\"\", \"g\":\"\", \"s\":\"\", \"u\":\"\", \"i\":\"\", \"r\":\"\", \"a\":\"\", \"d\":\"\" }", json)
        u.setSubfamily("Subfamily")
        u.setTribe("Tribe")
        u.setGenus("Genus")
        u.setSpecificEpithet("specific")
        u.setAuthorship("(Author, 1800)")
        u.setDrawerNumber("111.1")
        u.setCollection("Rod Eastwood Collection")
        json = u.toJSONString()
        assertEquals("{ \"f\":\"Family\", \"b\":\"Subfamily\", \"t\":\"Tribe\", \"g\":\"Genus\", \"s\":\"specific\", \"u\":\"\", \"i\":\"\", \"r\":\"\", \"a\":\"(Author, 1800)\", \"d\":\"111.1\", \"c\":\"Rod Eastwood Collection\" }", json)
    }

    /**
     * Test method for [UnitTrayLabel.createFromJSONString].
     */
    fun testCreateFromJSONString() {
        val u = UnitTrayLabel()
        u.setFamily("Family")
        var json: String = u.toJSONString()
        var u1: UnitTrayLabel = UnitTrayLabel.createFromJSONString(json)
        assertEquals(u.Family, u1.Family)
        assertEquals(u.Tribe, u1.Tribe)
        u.setSubfamily("Subfamily")
        u.setTribe("Tribe")
        u.setGenus("Genus")
        u.setSpecificEpithet("specific")
        u.setAuthorship("(Author, 1800")
        u.setDrawerNumber("111.1")
        json = u.toJSONString()
        u1 = UnitTrayLabel.createFromJSONString(json)
        assertEquals(u.Family, u1.Family)
        assertEquals(u.Tribe, u1.Tribe)
        assertEquals(u.Subfamily, u1.Subfamily)
        assertEquals(u.Genus, u1.Genus)
        assertEquals(u.SpecificEpithet, u1.SpecificEpithet)
        assertEquals(u.InfraspecificEpithet, u1.InfraspecificEpithet)
        assertEquals(u.InfraspecificRank, u1.InfraspecificRank)
        assertEquals(u.SubspecificEpithet, u1.SubspecificEpithet)
        assertEquals(u.Authorship, u1.Authorship)
        assertEquals(u.DrawerNumber, u1.DrawerNumber)
        // Check for case with some quotation marks in fields
        u.setFamily("Family")
        u.setSubfamily("Subfamily")
        u.setTribe("Tribe")
        u.setGenus("Genus")
        u.setSpecificEpithet("sp.'1' ")
        u.setAuthorship("(Author & \"author\", [1800]")
        u.setDrawerNumber("111.1")
        json = u.toJSONString()
        u1 = UnitTrayLabel.createFromJSONString(json)
        assertEquals(u.Family, u1.Family)
        assertEquals(u.Tribe, u1.Tribe)
        assertEquals(u.Subfamily, u1.Subfamily)
        assertEquals(u.Genus, u1.Genus)
        assertEquals(u.SpecificEpithet, u1.SpecificEpithet)
        assertEquals(u.InfraspecificEpithet, u1.InfraspecificEpithet)
        assertEquals(u.InfraspecificRank, u1.InfraspecificRank)
        assertEquals(u.SubspecificEpithet, u1.SubspecificEpithet)
        assertEquals(u.Authorship, u1.Authorship)
        assertEquals(u.DrawerNumber, u1.DrawerNumber)
        u.setFamily("Family")
        u.setSubfamily("Subfamily")
        u.setTribe("Tribe")
        u.setGenus("Genus")
        u.setSpecificEpithet("species")
        u.setInfraspecificEpithet("infraspecificEpithet")
        u.setInfraspecificRank("infraspecifcRank")
        u.setSubspecificEpithet("subspecificEpithet")
        u.setAuthorship("(Author, 1800)")
        u.setDrawerNumber("111.1")
        json = u.toJSONString()
        u1 = UnitTrayLabel.createFromJSONString(json)
        assertEquals(u.Family, u1.Family)
        assertEquals(u.Tribe, u1.Tribe)
        assertEquals(u.Subfamily, u1.Subfamily)
        assertEquals(u.Genus, u1.Genus)
        assertEquals(u.SpecificEpithet, u1.SpecificEpithet)
        assertEquals(u.InfraspecificEpithet, u1.InfraspecificEpithet)
        assertEquals(u.InfraspecificRank, u1.InfraspecificRank)
        assertEquals(u.SubspecificEpithet, u1.SubspecificEpithet)
        assertEquals(u.Authorship, u1.Authorship)
        assertEquals(u.DrawerNumber, u1.DrawerNumber)
        u.setFamily("Family")
        u.setSubfamily("Subfamily")
        u.setTribe("Tribe")
        u.setGenus("Genus")
        u.setSpecificEpithet("species")
        u.setInfraspecificEpithet("infraspecificEpithet")
        u.setInfraspecificRank("infraspecifcRank")
        u.setSubspecificEpithet("subspecificEpithet")
        //TODO: Support embedded curly brackets
//u.setAuthorship("(Author, {1800})");
        u.setAuthorship("(Author, [1800])")
        u.setDrawerNumber("111.1")
        u.setCollection("Rod Eastwood Collection")
        json = u.toJSONString()
        u1 = UnitTrayLabel.createFromJSONString(json)
        assertEquals(u.Family, u1.Family)
        assertEquals(u.Tribe, u1.Tribe)
        assertEquals(u.Subfamily, u1.Subfamily)
        assertEquals(u.Genus, u1.Genus)
        assertEquals(u.SpecificEpithet, u1.SpecificEpithet)
        assertEquals(u.InfraspecificEpithet, u1.InfraspecificEpithet)
        assertEquals(u.InfraspecificRank, u1.InfraspecificRank)
        assertEquals(u.SubspecificEpithet, u1.SubspecificEpithet)
        assertEquals(u.Authorship, u1.Authorship)
        assertEquals(u.DrawerNumber, u1.DrawerNumber)
        assertEquals(u.Collection, u1.Collection)
    }
}
