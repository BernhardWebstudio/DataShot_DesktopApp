/**
 * UnitTrayLabelBrowser.java
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
package edu.harvard.mcz.imagecapture.encoder


import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.encoder.LabelEncoder
import edu.harvard.mcz.imagecapture.exceptions.PrintFailedException
import edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModel
import edu.harvard.mcz.imagecapture.utility.DragDropJTable
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.event.UndoableEditEvent
import javax.swing.event.UndoableEditListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableRowSorter
import javax.swing.text.DefaultEditorKit.CopyAction
import javax.swing.text.DefaultEditorKit.PasteAction
import javax.swing.undo.CannotRedoException
import javax.swing.undo.CannotUndoException
import javax.swing.undo.UndoManager

/**
 * UnitTrayLabelBrowser
 */
class UnitTrayLabelBrowser : JFrame() {
    protected var undo: UndoManager = UndoManager()
    protected var undoAction: UndoAction? = null
    protected var redoAction: RedoAction? = null
    private var jContentPane: JPanel? = null
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    private var sorter: TableRowSorter<UnitTrayLabelTableModel?>? = null
    private var jPanel: JPanel? = null
    private var jButton: JButton? = null
    private var jButton1: JButton? = null
    private var jButton2: JButton? = null
    private var thisFrame: UnitTrayLabelBrowser? = null
    private var jJMenuBar: JMenuBar? = null
    private var jMenu: JMenu? = null
    private var jMenuItemCopy: JMenuItem? = null
    private var jMenuItemPaste: JMenuItem? = null
    private var jMenuItemUndo: JMenuItem? = null
    private var jMenuItemRedo: JMenuItem? = null
    private var jMenu1: JMenu? = null
    private var jMenuItem3: JMenuItem? = null
    private var jMenuItem: JMenuItem? = null
    private var jMenuItemAddRow: JMenuItem? = null
    private var tableModel: UnitTrayLabelTableModel? = null
    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() {
        this.setSize(1100, 900)
        this.setJMenuBar(getJJMenuBar())
        this.setPreferredSize(Dimension(1400, 900))
        this.setContentPane(getJContentPane())
        this.setTitle("Create Unit Tray Labels")
    }

    fun center() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        this.setLocation((screenSize!!.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel? {
        if (jContentPane == null) {
            jContentPane = JPanel()
            jContentPane.setLayout(BorderLayout())
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER)
            jContentPane.add(getJPanel(), BorderLayout.SOUTH)
        }
        return jContentPane
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
            tableModel = UnitTrayLabelTableModel()
            jTable = DragDropJTable(tableModel)
            jTable.setDragEnabled(true)
            jTable.setDropMode(DropMode.ON)
            //tableModel.addUndoableEditListener(new MyUndoableEditListener());
            sorter = TableRowSorter<UnitTrayLabelTableModel?>(tableModel)
            jTable.setRowSorter(sorter)
        }
        return jTable
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridx = 2
            gridBagConstraints1.gridy = 0
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 1
            gridBagConstraints.gridy = 0
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(getJButton(), GridBagConstraints())
            jPanel.add(getJButton1(), gridBagConstraints)
            jPanel.add(getJButton2(), gridBagConstraints1)
        }
        return jPanel
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Add")
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    addRow()
                }
            })
        }
        return jButton
    }

    private fun addRow() {
        tableModel.addRow()
        // scroll to last row in table.  Make first cell modal in latest row editable.
        jTable.scrollRectToVisible(jTable.getCellRect(jTable.getRowCount(), 1, false))
        val lastRow: Int = jTable.getRowCount() - 1
        jTable.getSelectionModel().setSelectionInterval(lastRow, lastRow)
        val editColumn = 0
        jTable.editCellAt(lastRow, editColumn)
        // scroll to the editing field
        jTable.scrollRectToVisible(
                jTable.getCellRect(lastRow, editColumn, true))
        log!!.debug(jTable.getVisibleRect())
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButton1(): JButton? {
        if (jButton1 == null) {
            jButton1 = JButton()
            jButton1.setText("Make PDF")
            jButton1.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    makePDF()
                }
            })
        }
        return jButton1
    }

    fun makePDF() {
        var ok = false
        var message = ""
        try {
            ok = LabelEncoder.Companion.printList(tableModel.getList())
        } catch (e1: PrintFailedException) {
            message = "PDF generation failed: " + e1.message
        }
        if (ok) {
            message = "File labels.pdf was generated."
            (jTable.getModel() as AbstractTableModel).fireTableDataChanged()
        }
        JOptionPane.showMessageDialog(thisFrame, message, "PDF Generation", JOptionPane.OK_OPTION)
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private fun getJButton2(): JButton? {
        if (jButton2 == null) {
            jButton2 = JButton()
            jButton2.setText("Close")
            jButton2.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    setVisible(false)
                }
            })
        }
        return jButton2
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private fun getJJMenuBar(): JMenuBar? {
        if (jJMenuBar == null) {
            jJMenuBar = JMenuBar()
            jJMenuBar.add(getJMenu1())
            jJMenuBar.add(getJMenu())
        }
        return jJMenuBar
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenu(): JMenu? {
        if (jMenu == null) {
            jMenu = JMenu()
            jMenu.setText("Edit")
            jMenu.setMnemonic(KeyEvent.VK_E)
            jMenu.add(getJMenuItem())
            jMenu.add(jMenuItem1)
            jMenu.add(getJMenuItemUndo())
            jMenu.add(getJMenuItemRedo())
            jMenu.add(getJMenuItemAddRow())
        }
        return jMenu
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItem(): JMenuItem? {
        if (jMenuItemCopy == null) {
            jMenuItemCopy = JMenuItem(CopyAction())
            jMenuItemCopy.setText("Copy")
            jMenuItemCopy.setMnemonic(KeyEvent.VK_C)
            jMenuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_C, ActionEvent.CTRL_MASK))
            jMenuItemCopy.setEnabled(true)
        }
        return jMenuItemCopy
    }

    /**
     * This method initializes jMenuItem1
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItem1: JMenuItem?
        private get() {
            if (jMenuItemPaste == null) {
                jMenuItemPaste = JMenuItem(PasteAction())
                jMenuItemPaste.setText("Paste")
                jMenuItemPaste.setMnemonic(KeyEvent.VK_P)
                jMenuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_V, ActionEvent.CTRL_MASK))
                jMenuItemPaste.setEnabled(true)
            }
            return jMenuItemPaste
        }

    private fun getJMenuItemUndo(): JMenuItem? {
        if (jMenuItemUndo == null) {
            undoAction = UndoAction()
            jMenuItemUndo = JMenuItem(undoAction)
            jMenuItemUndo.setText("Undo")
            jMenuItemUndo.setMnemonic(KeyEvent.VK_U)
            jMenuItemUndo.setEnabled(false)
        }
        return jMenuItemUndo
    }

    private fun getJMenuItemRedo(): JMenuItem? {
        if (jMenuItemRedo == null) {
            redoAction = RedoAction()
            jMenuItemRedo = JMenuItem(redoAction)
            jMenuItemRedo.setText("Redo")
            jMenuItemRedo.setMnemonic(KeyEvent.VK_R)
            jMenuItemRedo.setEnabled(false)
        }
        return jMenuItemRedo
    }

    /**
     * This method initializes jMenu1
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenu1(): JMenu? {
        if (jMenu1 == null) {
            jMenu1 = JMenu()
            jMenu1.setText("File")
            jMenu1.setMnemonic(KeyEvent.VK_F)
            jMenu1.add(getJMenuItem3())
            jMenu1.add(jMenuItem2)
        }
        return jMenu1
    }

    /**
     * This method initializes jMenuItem3
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItem3(): JMenuItem? {
        if (jMenuItem3 == null) {
            jMenuItem3 = JMenuItem()
            jMenuItem3.setText("Create PDF")
            jMenuItem3.setMnemonic(KeyEvent.VK_D)
            jMenuItem3.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    makePDF()
                }
            })
        }
        return jMenuItem3
    }

    private fun getJMenuItemAddRow(): JMenuItem? {
        if (jMenuItemAddRow == null) {
            jMenuItemAddRow = JMenuItem()
            jMenuItemAddRow.setText("Add Blank Row")
            jMenuItemAddRow.setMnemonic(KeyEvent.VK_D)
            jMenuItemAddRow.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    addRow()
                    //jTable.scrollRectToVisible(jTable.getCellRect(jTable.getRowCount(), 1, false));
//jTable.editCellAt(jTable.getRowCount(), 1);
//jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
//jScrollPane.getViewport().invalidate();
//jScrollPane.getViewport().validate();
//jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                    val newCell = Rectangle(
                            1,
                            jTable.getRowHeight() * jTable.getRowCount(),
                            10,
                            jTable.getRowHeight()
                    )
                    //jTable.scrollRectToVisible(newCell);
                }
            })
        }
        return jMenuItemAddRow
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItem2: JMenuItem?
        private get() {
            if (jMenuItem == null) {
                jMenuItem = JMenuItem()
                jMenuItem.setText("Close Window")
                jMenuItem.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
            }
            return jMenuItem
        }

    protected inner class MyUndoableEditListener : UndoableEditListener {
        override fun undoableEditHappened(e: UndoableEditEvent) { //Remember the edit and update the menus
            undo.addEdit(e.getEdit())
            //undoAction.updateUndoState();
//redoAction.updateRedoState();
        }
    }

    protected inner class UndoAction : AbstractAction() {
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        override fun actionPerformed(e: ActionEvent?) {
            try {
                undo.undo()
            } catch (ex: CannotUndoException) {
                println("Unable to undo: $ex")
                ex.printStackTrace()
            }
            //updateUndoState();
//redoAction.updateRedoState();
        }
    }

    protected inner class RedoAction : AbstractAction() {
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        override fun actionPerformed(e: ActionEvent?) {
            try {
                undo.redo()
            } catch (ex: CannotRedoException) {
                println("Unable to redo: $ex")
                ex.printStackTrace()
            }
            //updateRedoState();
//undoAction.updateUndoState();
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private val log: Log = LogFactory.getLog(ImageCaptureApp::class.java)
    }

    /**
     * This is the default constructor
     */
    init {
        initialize()
        pack()
        center()
        thisFrame = this
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
