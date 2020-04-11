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
        assertEquals(10, s.Family.length())
        assertEquals(10, s.Family.codePointCount(0, s.Family.length()))
        assertEquals(10, s.Family.Bytes.length)
        // "FamilynameѦ" contains 11 characters and 12 bytes
        s.setFamily("FamilynameѦ")
        assertEquals(11, s.Family.length())
        assertEquals(12, s.Family.Bytes.length)
        assertEquals(11, s.Family.codePointCount(0, s.Family.length()))
        // test truncation with multi-byte characters
        var st39 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(39, st39.length)
        s.setFamily(st39)
        assertEquals(st39, s.Family)
        var st40 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(40, st40.length)
        s.setFamily(st40)
        assertEquals(st40, s.Family)
        // should truncate at 40 char.
        var st41 = "ѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦѦ"
        assertEquals(41, st41.length)
        s.setFamily(st41) // store 41 char
        assertEquals(40, s.Family.length()) // returns just 40
        assertEquals(st40, s.Family)
        // test truncation with single byte characters
        st39 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(39, st39.length)
        s.setFamily(st39)
        assertEquals(st39, s.Family)
        st40 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(40, st40.length)
        s.setFamily(st40)
        assertEquals(st40, s.Family)
        // should truncate at 40 char.
        st41 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        assertEquals(41, st41.length)
        s.setFamily(st41) // store 41 char
        assertEquals(40, s.Family.length()) // returns just 40
        assertEquals(st40, s.Family)
    }

    fun testSetDateCreated() {
        val s = Specimen()
        val startSeconds = 50000
        val date = Date(startSeconds)
        s.setDateCreated(date)
        // date and s private date should now be different values
        assertNotSame(date, s.DateCreated)
        assertEquals(date, s.DateCreated)
        assertTrue(s.DateCreated.Year === Date(startSeconds).Year)
        assertTrue(s.DateCreated.Date === Date(startSeconds).Date)
        assertEquals(s.DateCreated.Year, Date(startSeconds).Year)
        assertEquals(s.DateCreated.Date, Date(startSeconds).Date)
        val seconds = 900000
        // modify the date object, make sure change doesn't change private value inside specimen
// both should be different objects with different values.
        date.date = seconds
        assertFalse(date == s.DateCreated)
        assertNotSame(date, s.DateCreated)
        // try to modify the values of the private value inside specimen through the get
        s.DateCreated.setYear(1800)
        s.DateCreated.setDate(15)
        assertFalse(date === s.DateCreated)
        assertNotSame(date, s.DateCreated)
        //assertFalse(s.DateCreated.Year==1800);
//assertFalse(s.DateCreated.Date==15);
        assertEquals(s.DateCreated.Year, 1800)
        assertEquals(s.DateCreated.Date, 15)
    }

    fun testSetDateDateLastUpdated() {
        val s = Specimen()
        val startSeconds = 50000
        val date = Date(startSeconds)
        s.setDateLastUpdated(date)
        // date and s private date should now be different values
        assertNotSame(date, s.DateLastUpdated)
        assertEquals(date, s.DateLastUpdated)
        assertTrue(s.DateLastUpdated.Year === Date(startSeconds).Year)
        assertTrue(s.DateLastUpdated.Date === Date(startSeconds).Date)
        assertEquals(s.DateLastUpdated.Year, Date(startSeconds).Year)
        assertEquals(s.DateLastUpdated.Date, Date(startSeconds).Date)
        val seconds = 900000
        // modify the date object, make sure change doesn't change private value inside specimen
// both should be different objects with different values.
        date.date = seconds
        assertFalse(date == s.DateLastUpdated)
        assertNotSame(date, s.DateLastUpdated)
        // try to modify the values of the private value inside specimen through the get
        s.DateLastUpdated.setYear(1800)
        s.DateLastUpdated.setDate(15)
        assertFalse(date === s.DateLastUpdated)
        assertNotSame(date, s.DateLastUpdated)
        //assertFalse(s.DateLastUpdated.Year==1800);
//assertFalse(s.DateLastUpdated.Date==15);
        assertEquals(s.DateLastUpdated.Year, 1800)
        assertEquals(s.DateLastUpdated.Date, 15)
    }
}
