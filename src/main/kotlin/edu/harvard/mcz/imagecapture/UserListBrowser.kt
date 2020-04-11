/**
 * UserListPanel.java
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


import edu.harvard.mcz.imagecapture.UserListBrowser
import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.ui.ButtonEditor
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer
import edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModel
import org.apache.commons.logging.LogFactory
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

/**
 * UserListPanel
 */
class UserListBrowser : JPanel() {
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    private var jPanel1: JPanel? = null
    private var jJToolBarBar: JToolBar? = null
    private var jButtonAddUser: JButton? = null
    private var editUserPanel: EditUserPanel? = null
    private var thisPanel: UserListBrowser? = null
    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() {
        this.setSize(566, 308)
        this.setLayout(BorderLayout())
        this.add(getJPanel1(), BorderLayout.SOUTH)
        this.add(getJScrollPane(), BorderLayout.CENTER)
        this.add(getJJToolBarBar(), BorderLayout.NORTH)
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            jScrollPane = JScrollPane()
            jScrollPane.setViewportView(getJTable())
        }
        return jScrollPane
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private fun getJTable(): JTable? {
        if (jTable == null) {
            jTable = JTable()
            val model = UsersTableModel(UsersLifeCycle.Companion.findAll())
            jTable.setModel(model)
            jTable.setDefaultRenderer(Users::class.java, ButtonRenderer())
            jTable.setDefaultEditor(
                    Users::class.java, ButtonEditor(ButtonEditor.Companion.OPEN_USER, thisPanel))
        }
        return jTable
    }

    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            jPanel1 = JPanel()
            jPanel1.setLayout(BorderLayout())
            jPanel1.add(getEditUserPanel(), BorderLayout.CENTER)
        }
        return jPanel1
    }

    /**
     * This method initializes jJToolBarBar
     *
     * @return javax.swing.JToolBar
     */
    private fun getJJToolBarBar(): JToolBar? {
        if (jJToolBarBar == null) {
            jJToolBarBar = JToolBar()
            jJToolBarBar.setFloatable(false)
            jJToolBarBar.add(getJButtonAddUser())
        }
        return jJToolBarBar
    }

    /**
     * This method initializes jButtonAddUser
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonAddUser(): JButton? {
        if (jButtonAddUser == null) {
            jButtonAddUser = JButton()
            jButtonAddUser.setText("Add New User")
            try {
                jButtonAddUser.setIcon(ImageIcon(this.javaClass.getResource(
                        "/edu/harvard/mcz/imagecapture/resources/images/add_person_30px.png")))
            } catch (e: Exception) {
                log!!.error("Can't open icon file for jButtonAddUser.")
                log.error(e)
            }
            jButtonAddUser.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val newUser = Users()
                    thisPanel!!.editUserPanel!!.setUser(newUser)
                }
            })
        }
        return jButtonAddUser
    }

    /**
     * This method initializes editUserPanel
     *
     * @return edu.harvard.mcz.imagecapture.EditUserPanel
     */
    fun getEditUserPanel(): EditUserPanel? {
        if (editUserPanel == null) {
            editUserPanel = EditUserPanel()
        }
        return editUserPanel
    }

    companion object {
        private const val serialVersionUID = 1L
        private val log = LogFactory.getLog(UserListBrowser::class.java)
    }

    /**
     * This is the default constructor
     */
    init {
        thisPanel = this
        initialize()
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
