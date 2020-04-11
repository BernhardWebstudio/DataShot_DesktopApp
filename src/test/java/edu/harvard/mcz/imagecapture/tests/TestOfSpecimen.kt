/**
 * TestOfSpecimen.java
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

import edu.harvard.mcz.imagecapture.entity.Specimen
import junit.framework.TestCase
import java.util.*

/**
 * TestOfSpecimen Test sanity checks in Specimen class.
 */
class TestOfSpecimen
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [Specimen.setFamily].
     */
    fun testSetFamily() { // Testing issues around char/byte length.
        val s = Specimen()
        // "Familyname" contains 10 characters and 10 bytes
        s.setFamily("Familyname")
        assertEquals(10, s.getFamily().length())
        assertEquals(10, s.getFamily().codePointCount(0, s.getFamily().length()))
        assertEquals(10, s.getFamily().getBytes().length)
        // "FamilynameѦ" contains 11 characters and 12 bytes
        s.setFamily("FamilynameѦ")
        assertEquals(11, s.getFamily().length())
        assertEquals(12, s.getFamily().getBytes().length)
        assertEquals(11, s.getFamily().codePointCount(0, s.getFamily().length()))
        // test truncation with multi-byte characters
        var st39 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(39, st39.length)
        s.setFamily(st39)
        assertEquals(st39, s.getFamily())
        var st40 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(40, st40.length)
        s.setFamily(st40)
        assertEquals(st40, s.getFamily())
        // should truncate at 40 char.
        var st41 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(41, st41.length)
        s.setFamily(st41) // store 41 char
        assertEquals(40, s.getFamily().length()) // returns just 40
        assertEquals(st40, s.getFamily())
        // test truncation with single byte characters
        st39 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(39, st39.length)
        s.setFamily(st39)
        assertEquals(st39, s.getFamily())
        st40 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(40, st40.length)
        s.setFamily(st40)
        assertEquals(st40, s.getFamily())
        // should truncate at 40 char.
        st41 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(41, st41.length)
        s.setFamily(st41) // store 41 char
        assertEquals(40, s.getFamily().length()) // returns just 40
        assertEquals(st40, s.getFamily())
    }

    fun testSetDateCreated() {
        val s = Specimen()
        val startSeconds = 50000
        val date = Date(startSeconds)
        s.setDateCreated(date)
        // date and s private date should now be different values
        assertNotSame(date, s.getDateCreated())
        assertEquals(date, s.getDateCreated())
        assertTrue(s.getDateCreated().getYear() === Date(startSeconds).getYear())
        assertTrue(s.getDateCreated().getDate() === Date(startSeconds).getDate())
        assertEquals(s.getDateCreated().getYear(), Date(startSeconds).getYear())
        assertEquals(s.getDateCreated().getDate(), Date(startSeconds).getDate())
        val seconds = 900000
        // modify the date object, make sure change doesn't change private value inside specimen
// both should be different objects with different values.
        date.date = seconds
        assertFalse(date == s.getDateCreated())
        assertNotSame(date, s.getDateCreated())
        // try to modify the values of the private value inside specimen through the get
        s.getDateCreated().setYear(1800)
        s.getDateCreated().setDate(15)
        assertFalse(date === s.getDateCreated())
        assertNotSame(date, s.getDateCreated())
        //assertFalse(s.getDateCreated().getYear()==1800);
//assertFalse(s.getDateCreated().getDate()==15);
        assertEquals(s.getDateCreated().getYear(), 1800)
        assertEquals(s.getDateCreated().getDate(), 15)
    }

    fun testSetDateDateLastUpdated() {
        val s = Specimen()
        val startSeconds = 50000
        val date = Date(startSeconds)
        s.setDateLastUpdated(date)
        // date and s private date should now be different values
        assertNotSame(date, s.getDateLastUpdated())
        assertEquals(date, s.getDateLastUpdated())
        assertTrue(s.getDateLastUpdated().getYear() === Date(startSeconds).getYear())
        assertTrue(s.getDateLastUpdated().getDate() === Date(startSeconds).getDate())
        assertEquals(s.getDateLastUpdated().getYear(), Date(startSeconds).getYear())
        assertEquals(s.getDateLastUpdated().getDate(), Date(startSeconds).getDate())
        val seconds = 900000
        // modify the date object, make sure change doesn't change private value inside specimen
// both should be different objects with different values.
        date.date = seconds
        assertFalse(date == s.getDateLastUpdated())
        assertNotSame(date, s.getDateLastUpdated())
        // try to modify the values of the private value inside specimen through the get
        s.getDateLastUpdated().setYear(1800)
        s.getDateLastUpdated().setDate(15)
        assertFalse(date === s.getDateLastUpdated())
        assertNotSame(date, s.getDateLastUpdated())
        //assertFalse(s.getDateLastUpdated().getYear()==1800);
//assertFalse(s.getDateLastUpdated().getDate()==15);
        assertEquals(s.getDateLastUpdated().getYear(), 1800)
        assertEquals(s.getDateLastUpdated().getDate(), 15)
    }
}