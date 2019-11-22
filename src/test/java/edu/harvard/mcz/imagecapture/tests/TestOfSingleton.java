/**
 * TestOfSingleton.java
 * edu.harvard.mcz.imagecapture.tests
 * <p>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.Singleton;
import junit.framework.TestCase;

/**
 * TestOfSingleton tests that the Singleton class is indeed a singleton.
 */
public class TestOfSingleton extends TestCase {

    /**
     * @param name
     */
    public TestOfSingleton(String name) {
        super(name);
    }

    /**
     * Test method for {@link edu.harvard.mcz.imagecapture.Singleton#getSingletonInstance()}.
     */
    public void testGetSingletonInstance() {
        Singleton instance = Singleton.getSingletonInstance();
        assertEquals(instance, Singleton.getSingletonInstance());
    }

    public void testGetSingletonInstanceThreadSafety() {
        // JUnit alone isn't able to handle multiple threads.
        // Need to explore extensions to JUnit for thread tests.
    }


}
