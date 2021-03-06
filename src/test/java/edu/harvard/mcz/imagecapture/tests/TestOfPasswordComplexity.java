/**
 * TestOfPasswordComplexity.java
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

import edu.harvard.mcz.imagecapture.entity.Users;
import junit.framework.TestCase;

public class TestOfPasswordComplexity extends TestCase {

    public void testTestProposedPassword() {
        String proposal = "A";
        Users user = new Users();
        user.setUsername("username");
        assertFalse(Users.testProposedPassword(proposal, user.getUsername()));

        proposal = "A9a";
        assertFalse(Users.testProposedPassword(proposal, user.getUsername()));

        proposal = user.getUsername();
        assertFalse(Users.testProposedPassword(proposal, user.getUsername()));

        proposal = "123456789Aa";
        assertTrue(Users.testProposedPassword(proposal, user.getUsername()));
    }

}
