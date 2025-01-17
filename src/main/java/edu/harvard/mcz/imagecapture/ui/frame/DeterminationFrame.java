/**
 * DeterminationFrame.java
 * edu.harvard.mcz.imagecapture
 *
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
 * <p>
 */
package edu.harvard.mcz.imagecapture.ui.frame;

import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.entity.Determination;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.NatureOfId;
import edu.harvard.mcz.imagecapture.entity.fixed.TypeStatus;
import edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditor;
import edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * DeterminationFrame for editing identification history
 */
public class DeterminationFrame extends JFrame {

    private static final Logger log =
            LoggerFactory.getLogger(DeterminationFrame.class);

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JScrollPane jScrollPane = null;
    private JTable jTableDeterminations = null;

    private int clickedOnDetsRow;
    private JPopupMenu jPopupDets;

    private DeterminationTableModel determinationsModel;
    private Specimen specimen = null;
    private JPanel jPanel = null;
    private JButton jButtonAdd = null;
    private JButton jButtonDone = null;
    private JPanel panel;
    private JLabel lblFillInVerbatim;

    private DeterminationFrame thisFrame;

    /**
     * This is the default constructor
     */
    public DeterminationFrame() {
        super();
        thisFrame = this;
        this.determinationsModel = new DeterminationTableModel();
        initialize();
        jButtonAdd.setEnabled(false);
    }

    /**
     * Constructor to show an arbitrary list of determinations.
     *
     * @param determinationsModel
     */
    public DeterminationFrame(DeterminationTableModel determinationsModel) {
        super();
        this.determinationsModel = determinationsModel;
        initialize();
        jButtonAdd.setEnabled(false);
    }

    /**
     * Constructor to show a list of determinations for a particular specimen.
     *
     * @param aSpecimen
     */
    public DeterminationFrame(Specimen aSpecimen) {
        super();
        determinationsModel =
                new DeterminationTableModel(aSpecimen.getDeterminations());
        specimen = aSpecimen;
        initialize();
        jButtonAdd.setEnabled(true);
    }

    public void setSpecimen(Specimen aSpecimen) {
        determinationsModel =
                new DeterminationTableModel(aSpecimen.getDeterminations());
        jTableDeterminations.setModel(determinationsModel);
        setTableColumnEditors();
        specimen = aSpecimen;
        jButtonAdd.setEnabled(true);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(940, 351);
        this.setPreferredSize(new Dimension(1350, 350));
        this.setContentPane(getJContentPane());
        String forSpecimen = "";
        if (specimen != null) {
            forSpecimen = " for " + specimen.getBarcode();
        }
        this.setTitle("Enter and edit Determination history" + forSpecimen);
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
            jContentPane.add(getJPanel(), BorderLayout.SOUTH);
            jContentPane.add(getPanel(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private JTable getJTable() {
        if (jTableDeterminations == null) {
            jTableDeterminations = new JTable();
            jTableDeterminations.setModel(determinationsModel);

            // allie change
            // here is where the select field is set for determiner!!!
            // FilteringAgentJComboBox field = new FilteringAgentJComboBox();
            // jTableDeterminations.getColumnModel().getColumn(DeterminationTableModel.ROW_IDENTIFIEDBY).setCellEditor(new
            // ComboBoxCellEditor(field));

            setTableColumnEditors();

            jTableDeterminations.setRowHeight(jTableDeterminations.getRowHeight() +
                    4);

            jTableDeterminations.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        clickedOnDetsRow = ((JTable) e.getComponent()).getSelectedRow();
                        jPopupDets.show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        clickedOnDetsRow = ((JTable) e.getComponent()).getSelectedRow();
                        jPopupDets.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

            jPopupDets = new JPopupMenu();
            JMenuItem mntmDeleteRow = new JMenuItem("Delete Row");
            mntmDeleteRow.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        log.debug("Debug {}", clickedOnDetsRow);
                        if (clickedOnDetsRow >= 0) {
                            int ok = JOptionPane.showConfirmDialog(
                                    thisFrame, "Delete the selected determination?",
                                    "Delete Determination", JOptionPane.OK_CANCEL_OPTION);
                            if (ok == JOptionPane.OK_OPTION) {
                                log.debug("deleting determination row " + clickedOnDetsRow);
                                ((DeterminationTableModel) jTableDeterminations.getModel())
                                        .deleteRow(clickedOnDetsRow);
                                // force removal as apparently the jTableDeterminationsModel is
                                // not reliable enough
                            } else {
                                log.debug("determination row delete canceled by user.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Unable to select row to delete.");
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        JOptionPane.showMessageDialog(
                                thisFrame,
                                "Failed to delete a determination row. " + ex.getMessage());
                    }
                }
            });
            jPopupDets.add(mntmDeleteRow);
        }
        return jTableDeterminations;
    }

    private void setTableColumnEditors() {
        JComboBox<String> comboBoxNatureOfId =
                new JComboBox<String>(NatureOfId.getNatureOfIdValues());
        jTableDeterminations.getColumnModel()
                .getColumn(DeterminationTableModel.ROW_NATUREOFID)
                .setCellEditor(new DefaultCellEditor(comboBoxNatureOfId));

        JComboBox<String> comboBoxTypeStatus =
                new JComboBox<String>(TypeStatus.getTypeStatusValues());
        jTableDeterminations.getColumnModel()
                .getColumn(DeterminationTableModel.ROW_TYPESTATUS)
                .setCellEditor(new DefaultCellEditor(comboBoxTypeStatus));

        JTextField jTextFieldDateIdentified = new JTextField();
        jTextFieldDateIdentified.setInputVerifier(
                MetadataRetriever.getInputVerifier(
                        Determination.class, "DateIdentified", jTextFieldDateIdentified));
        jTableDeterminations.getColumnModel()
                .getColumn(DeterminationTableModel.ROW_DATEIDENTIFIED)
                .setCellEditor(new ValidatingTableCellEditor(jTextFieldDateIdentified));
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getJButtonAdd(), new GridBagConstraints());
            jPanel.add(getJButtonDone(), gridBagConstraints);
        }
        return jPanel;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonAdd() {
        if (jButtonAdd == null) {
            jButtonAdd = new JButton();
            jButtonAdd.setText("Add");
            jButtonAdd.setMnemonic(KeyEvent.VK_A);
            jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    log.debug("getJButtonAdd: adding row.....");
                    if (specimen != null) {
                        if (jTableDeterminations.isEditing()) {
                            jTableDeterminations.getCellEditor().stopCellEditing();
                        }
                        Determination d = new Determination();
                        d.setSpecimen(specimen);
                        determinationsModel.addDetermination(d);
                    }
                }
            });
        }
        return jButtonAdd;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonDone() {
        if (jButtonDone == null) {
            jButtonDone = new JButton();
            jButtonDone.setText("Done");
            jButtonDone.setMnemonic(KeyEvent.VK_D);
            jButtonDone.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // Make sure changes in field that was modal before button
                    // click are saved to the model.
                    if (jTableDeterminations.isEditing()) {
                        jTableDeterminations.getCellEditor().stopCellEditing();
                    }
                    //
                    setVisible(false);
                }
            });
        }
        return jButtonDone;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getLblFillInVerbatim());
        }
        return panel;
    }

    private JLabel getLblFillInVerbatim() {
        if (lblFillInVerbatim == null) {
            lblFillInVerbatim = new JLabel(
                    "Fill in either Verbatim Text or (Genus/Species/Subspecies/Infraspecific/Rank/Authorship), but  not both.");
        }
        return lblFillInVerbatim;
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
