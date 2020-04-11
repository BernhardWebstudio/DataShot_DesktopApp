/**
 * EditUserPanel.java
 * edu.harvard.mcz.imagecapture
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
 *
 *
 */
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog
import edu.harvard.mcz.imagecapture.utility.HashUtility
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

/**
 * EditUserPanel
 */
class EditUserPanel : JPanel() {
    private var user: Users? = null //  @jve:decl-index=0:
    private var jLabel: JLabel? = null
    private var jTextFieldUsername: JTextField? = null
    private var jLabel1: JLabel? = null
    private var jLabel2: JLabel? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jPasswordField: JPasswordField? = null
    private var jPasswordField1: JPasswordField? = null
    private var jTextFieldFullName: JTextField? = null
    private var jTextFieldAbout: JTextField? = null
    private var jLabel5: JLabel? = null
    private var jComboBox: JComboBox<*>? = null
    private var jButton: JButton? = null
    private var jButtonSetPassword: JButton? = null
    private fun setEditingState(enable: Boolean) {
        jTextFieldUsername.setEnabled(enable)
        jTextFieldFullName.setEnabled(enable)
        jTextFieldAbout.setEnabled(enable)
        jComboBox.setEnabled(enable)
        jComboBox.setEditable(false)
        jPasswordField.setEnabled(enable)
        jPasswordField1.setEnabled(enable)
        jButton.setEnabled(enable)
    }

    fun setUser(aUser: Users?) {
        setEditingState(true)
        if (aUser != null) {
            user = aUser
            jTextFieldUsername.setText(user.username)
            jTextFieldFullName.setText(user.Fullname)
            jTextFieldAbout.setText(user.Description)
            jComboBox.setSelectedItem(user.Role)
        }
        if (user.Userid == null) {
            jPasswordField.setVisible(true)
            jPasswordField1.setVisible(true)
            jPasswordField.setEnabled(true)
            jPasswordField1.setEnabled(true)
            jButtonSetPassword.setEnabled(false)
            jButtonSetPassword.setVisible(false)
            jLabel2.setVisible(true)
        } else {
            jPasswordField.setEnabled(false)
            jPasswordField1.setEnabled(false)
            jPasswordField.setVisible(false)
            jPasswordField1.setVisible(false)
            jButtonSetPassword.setEnabled(true)
            jButtonSetPassword.setVisible(true)
            jLabel2.setVisible(false)
        }
    }

    private fun save() {
        var okToSave = true
        var message = ""
        // Check required values
        if (jPasswordField.isEnabled()) { // Passwords must match
            if (LoginDialog.Companion.hashPassword(jPasswordField) == LoginDialog.Companion.hashPassword(jPasswordField1)) {
                user.setHash(LoginDialog.Companion.hashPassword(jPasswordField))
            } else {
                okToSave = false
                message = message + "Password and Password Again don't match.\n"
            }
            // Check for sufficiently complex password for new users.
            val pw = String(jPasswordField.Password)
            if (Users.Companion.testProposedPassword(pw, user.username)) {
                user.setHash(HashUtility.getSHA1Hash(pw))
            } else {
                okToSave = false
                message = message + "Password is not sufficiently complex.  " + Users.Companion.PASSWORD_RULES_MESSAGE + " \n"
            }
        }
        // Don't check here yet for sufficiently complex password for
// existing users, as that would force a password change by the
// admin when changing other aspects of a user.
// This will become desirable, but not yet.
// Are required fields populated?
        if (jTextFieldUsername.Text == "") {
            okToSave = false
            message = message + "An email is required.\n"
        }
        if (jTextFieldFullName.Text == "") {
            okToSave = false
            message = message + "A full name is required.\n"
        }
        if (!okToSave) { // warn
            message = "Error. Can't Save.\n$message"
            JOptionPane.showMessageDialog(Singleton.MainFrame, message, "Error. Can't save.", JOptionPane.OK_OPTION)
        } else { // Save
            val uls = UsersLifeCycle()
            user.setUsername(jTextFieldUsername.Text)
            user.setFullname(jTextFieldFullName.Text)
            user.setDescription(jTextFieldAbout.Text)
            if (jComboBox.SelectedIndex == -1 && jComboBox.SelectedItem == null) {
                user.setRole(Users.Companion.ROLE_DATAENTRY)
            } else {
                user.setRole(jComboBox.SelectedItem.toString())
            }
            try {
                if (user.Userid == null) {
                    uls.persist(user)
                } else {
                    uls.attachDirty(user)
                }
                message = "Saved " + user.Fullname
                Singleton.MainFrame.setStatusMessage(message)
            } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() {
        val gridBagConstraints13 = GridBagConstraints()
        gridBagConstraints13.gridx = 1
        gridBagConstraints13.gridy = 3
        val gridBagConstraints12 = GridBagConstraints()
        gridBagConstraints12.gridx = 0
        gridBagConstraints12.gridwidth = 2
        gridBagConstraints12.weightx = 0.0
        gridBagConstraints12.anchor = GridBagConstraints.NORTH
        gridBagConstraints12.weighty = 1.0
        gridBagConstraints12.gridy = 7
        val gridBagConstraints11 = GridBagConstraints()
        gridBagConstraints11.fill = GridBagConstraints.BOTH
        gridBagConstraints11.gridy = 6
        gridBagConstraints11.weightx = 1.0
        gridBagConstraints11.gridx = 1
        val gridBagConstraints10 = GridBagConstraints()
        gridBagConstraints10.gridx = 0
        gridBagConstraints10.anchor = GridBagConstraints.EAST
        gridBagConstraints10.gridy = 6
        jLabel5 = JLabel()
        jLabel5.setText("Role")
        val gridBagConstraints9 = GridBagConstraints()
        gridBagConstraints9.fill = GridBagConstraints.BOTH
        gridBagConstraints9.gridy = 5
        gridBagConstraints9.weightx = 1.0
        gridBagConstraints9.gridx = 1
        val gridBagConstraints8 = GridBagConstraints()
        gridBagConstraints8.fill = GridBagConstraints.BOTH
        gridBagConstraints8.gridy = 4
        gridBagConstraints8.weightx = 1.0
        gridBagConstraints8.gridx = 1
        val gridBagConstraints7 = GridBagConstraints()
        gridBagConstraints7.fill = GridBagConstraints.BOTH
        gridBagConstraints7.gridy = 2
        gridBagConstraints7.weightx = 1.0
        gridBagConstraints7.gridx = 1
        val gridBagConstraints6 = GridBagConstraints()
        gridBagConstraints6.fill = GridBagConstraints.BOTH
        gridBagConstraints6.gridy = 1
        gridBagConstraints6.weightx = 1.0
        gridBagConstraints6.gridx = 1
        val gridBagConstraints5 = GridBagConstraints()
        gridBagConstraints5.gridx = 0
        gridBagConstraints5.anchor = GridBagConstraints.EAST
        gridBagConstraints5.gridy = 5
        jLabel4 = JLabel()
        jLabel4.setText("About")
        val gridBagConstraints4 = GridBagConstraints()
        gridBagConstraints4.gridx = 0
        gridBagConstraints4.anchor = GridBagConstraints.EAST
        gridBagConstraints4.gridy = 4
        jLabel3 = JLabel()
        jLabel3.setText("Full Name")
        val gridBagConstraints3 = GridBagConstraints()
        gridBagConstraints3.gridx = 0
        gridBagConstraints3.anchor = GridBagConstraints.EAST
        gridBagConstraints3.gridy = 2
        jLabel2 = JLabel()
        jLabel2.setText("Password Again")
        val gridBagConstraints2 = GridBagConstraints()
        gridBagConstraints2.gridx = 0
        gridBagConstraints2.anchor = GridBagConstraints.EAST
        gridBagConstraints2.gridy = 1
        jLabel1 = JLabel()
        jLabel1.setText("Password")
        val gridBagConstraints1 = GridBagConstraints()
        gridBagConstraints1.fill = GridBagConstraints.BOTH
        gridBagConstraints1.gridy = 0
        gridBagConstraints1.weightx = 1.0
        gridBagConstraints1.gridx = 1
        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridx = 0
        gridBagConstraints.anchor = GridBagConstraints.EAST
        gridBagConstraints.gridy = 0
        jLabel = JLabel()
        jLabel.setText("email")
        this.setSize(300, 200)
        this.setLayout(GridBagLayout())
        this.add(jLabel, gridBagConstraints)
        this.add(getJTextFieldUsername(), gridBagConstraints1)
        this.add(jLabel1, gridBagConstraints2)
        this.add(jLabel2, gridBagConstraints3)
        this.add(jLabel3, gridBagConstraints4)
        this.add(jLabel4, gridBagConstraints5)
        this.add(getJPasswordField(), gridBagConstraints6)
        this.add(getJPasswordField1(), gridBagConstraints7)
        this.add(getJTextFieldFullName(), gridBagConstraints8)
        this.add(getJTextFieldAbout(), gridBagConstraints9)
        this.add(jLabel5, gridBagConstraints10)
        this.add(roleJComboBox, gridBagConstraints11)
        this.add(saveJButton, gridBagConstraints12)
        this.add(passwordChangeJButton, gridBagConstraints13)
    }

    /**
     * This method initializes jTextFieldUsername
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldUsername(): JTextField? {
        if (jTextFieldUsername == null) {
            jTextFieldUsername = JTextField()
        }
        return jTextFieldUsername
    }

    /**
     * This method initializes jPasswordField
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField(): JPasswordField? {
        if (jPasswordField == null) {
            jPasswordField = JPasswordField()
        }
        return jPasswordField
    }

    /**
     * This method initializes jPasswordField1
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField1(): JPasswordField? {
        if (jPasswordField1 == null) {
            jPasswordField1 = JPasswordField()
        }
        return jPasswordField1
    }

    /**
     * This method initializes jTextFieldFullName
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldFullName(): JTextField? {
        if (jTextFieldFullName == null) {
            jTextFieldFullName = JTextField()
        }
        return jTextFieldFullName
    }

    /**
     * This method initializes jTextFieldAbout
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldAbout(): JTextField? {
        if (jTextFieldAbout == null) {
            jTextFieldAbout = JTextField()
        }
        return jTextFieldAbout
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val roleJComboBox: JComboBox<*>?
        private get() {
            if (jComboBox == null) {
                jComboBox = JComboBox<Any?>(Users.Companion.ROLES)
            }
            return jComboBox
        }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val saveJButton: JButton?
        private get() {
            if (jButton == null) {
                jButton = JButton()
                jButton.setEnabled(false)
                jButton.setText("Save")
                jButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        save()
                    }
                })
            }
            return jButton
        }// user provided some input.

    /**
     * This method initializes jButtonSetPassword
     *
     * @return javax.swing.JButton
     */
    private val passwordChangeJButton: JButton?
        private get() {
            if (jButtonSetPassword == null) {
                jButtonSetPassword = JButton()
                jButtonSetPassword.setEnabled(false)
                jButtonSetPassword.setText("Set New Password")
                jButtonSetPassword.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        var suggestion: String = user.username + user.Fullname + Math.random()
                        suggestion = HashUtility.getSHA1Hash(suggestion)
                        suggestion.substring(1, 15)
                        val pw = (JOptionPane.showInputDialog(Singleton.MainFrame,
                                "Set a new password for " + user.Fullname + " (at least 10 characters)",
                                "Change password for " + user.username,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null,
                                "") as String)
                        if (pw != null && !pw.isEmpty()) { // user provided some input.
                            if (Users.Companion.testProposedPassword(pw, user.username)) {
                                user.setHash(HashUtility.getSHA1Hash(pw))
                            } else {
                                JOptionPane.showMessageDialog(Singleton.MainFrame,
                                        "Password is not complex enough" + Users.Companion.PASSWORD_RULES_MESSAGE, "Password Not Changed.",
                                        JOptionPane.ERROR_MESSAGE)
                            }
                        }
                    }
                })
            }
            return jButtonSetPassword
        }

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * This is the default constructor
     */
    init {
        initialize()
        setEditingState(false)
    }
}
