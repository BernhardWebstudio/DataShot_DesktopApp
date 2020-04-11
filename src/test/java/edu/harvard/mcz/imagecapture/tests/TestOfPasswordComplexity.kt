/**
 * TestOfPasswordComplexity.java
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

import edu.harvard.mcz.imagecapture.entity.Users
import junit.framework.TestCase

class TestOfPasswordComplexity : TestCase() {
    fun testTestProposedPassword() {
        var proposal = "A"
        val user = Users()
        user.setUsername("username")
        assertFalse(Users.testProposedPassword(proposal, user.username))
        proposal = "A9a"
        assertFalse(Users.testProposedPassword(proposal, user.username))
        proposal = user.username
        assertFalse(Users.testProposedPassword(proposal, user.username))
        proposal = "123456789Aa"
        assertTrue(Users.testProposedPassword(proposal, user.username))
    }
}
