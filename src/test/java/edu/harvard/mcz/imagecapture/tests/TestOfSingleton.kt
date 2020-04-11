/**
 * TestOfSingleton.java
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

import edu.harvard.mcz.imagecapture.Singleton
import junit.framework.TestCase

/**
 * TestOfSingleton tests that the Singleton class is indeed a singleton.
 */
class TestOfSingleton
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.Singleton.getSingletonInstance].
     */
    fun testGetSingletonInstance() {
        val instance: Singleton = Singleton.getSingletonInstance()
        assertEquals(instance, Singleton.getSingletonInstance())
    }

    fun testGetSingletonInstanceThreadSafety() { // JUnit alone isn't able to handle multiple threads.
// Need to explore extensions to JUnit for thread tests.
    }
}