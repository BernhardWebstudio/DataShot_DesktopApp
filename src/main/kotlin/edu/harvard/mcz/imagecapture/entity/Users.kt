package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import java.io.Serializable

// Generated Feb 5, 2009 5:22:31 PM by Hibernate Tools 3.2.2.GA
/**
 * Users, modified from generated class.
 */
class Users : Serializable {
    var userid: Int? = null
    var username: String? = null
    var fullname: String? = null
    var description: String? = null
    var role: String? = null
    var hash: String? = null

    constructor() {}
    constructor(username: String?, fullname: String?, role: String?) {
        this.username = username
        this.fullname = fullname
        this.role = role
    }

    constructor(username: String?, fullname: String?, description: String?,
                role: String?) {
        this.username = username
        this.fullname = fullname
        this.description = description
        this.role = role
    }

    /**
     * Test whether a user has rights under a role.  Use UsersLifeCycle.isUserAdministrator() or
     * UsersLifeCycle.isUserChiefEditor() to test if a particular user
     * has administrative rights instead of this method, as it validates the rights against
     * an underlying Users database record, while this method only validates against an instance of
     * the Users class.  This isUserRole() test returns true if the user is a member of a more
     * privileged class of users, e.g. isUserRole(Users.ROLE_DATAENTRY) returns true for a user who
     * is in role ROLE_FULL.
     *
     * @param aUserRole the role to test, one of ROLE_DATAENTRY, ROLE_FULL, or ROLE_ADMINISTRATOR.
     * @return true if this User has rights under aUserRole.
     * @see UsersLifeCycle.isUserAdministrator
     */
    fun isUserRole(aUserRole: String): Boolean {
        var result = false
        if (role == aUserRole) { // If equals, then user has this role.
            result = true
        } else { // Check more inclusive roles.
            if (role == ROLE_ADMINISTRATOR) { // Administrator can do anything.
                result = true
            }
            if (role == ROLE_CHIEF_EDITOR && (aUserRole == ROLE_DATAENTRY || aUserRole == ROLE_FULL || aUserRole == ROLE_CHIEF_EDITOR || aUserRole == ROLE_EDITOR)) { // Role chief editor includes roles full access, editor and data entry.
                result = true
            }
            if (role == ROLE_EDITOR && (aUserRole == ROLE_DATAENTRY || aUserRole == ROLE_FULL || aUserRole == ROLE_EDITOR)) { // Role editor  includes roles full and data entry.
                result = true
            }
            if (role == ROLE_FULL && aUserRole == ROLE_DATAENTRY) { // Role full includes role data entry.
                result = true
            }
        }
        return result
    }

    companion object {
        /**
         * All rights, including editing/creating users (equivalent to DBA)
         */
        val ROLE_ADMINISTRATOR: String? = "Administrator"
        /*
     * Specialist able to mark records as specialist reviewed, plus able to
     * create/edit/promote/demote users.
     *
     */
        val ROLE_CHIEF_EDITOR: String? = "Chief Editor"
        /**
         * Specialist able to mark records as specialist reviewed.
         */
        val ROLE_EDITOR: String? = "Editor"
        /**
         * All of data entry rights, plus quality control and preprocessing.
         */
        val ROLE_FULL: String? = "Full access"
        /**
         * Search/Browse/Edit specimen records only.
         */
        val ROLE_DATAENTRY: String? = "Data entry"
        val ROLES: Array<String?>? = arrayOf(ROLE_ADMINISTRATOR, ROLE_CHIEF_EDITOR, ROLE_EDITOR, ROLE_FULL, ROLE_DATAENTRY)
        private const val serialVersionUID = -5258394845638721810L
        /**
         * A text description suitable for error messages that describes
         * the rules implemented in testProposedPassword()
         *
         * @see Users.testProposedPassword
         */
        var PASSWORD_RULES_MESSAGE = "A password must be at least 11 characters long and contain at least one number, at least one lowercase letter, and at least one upper case letter"

        /**
         * Test a password against password complexity rules for the user password.  The
         * rules implemented here should match the rules described in PASSWORD_RULES_MESSAGE
         *
         * @param proposal a string that has been proposed as a new password for user.
         * @return true if password matches rules, false if password does not.
         * @see Users.PASSWORD_RULES_MESSAGE;
         */
        fun testProposedPassword(proposal: String, username: String?): Boolean {
            var result = false
            if (proposal.length > 10) {
                if (proposal.matches(".*[0-9].*")) {
                    if (proposal.matches(".*[A-Z].*")) {
                        if (proposal.matches(".*[a-z].*")) {
                            if (proposal != username) {
                                result = true
                            }
                        }
                    }
                }
            }
            return result
        }
    }
}
