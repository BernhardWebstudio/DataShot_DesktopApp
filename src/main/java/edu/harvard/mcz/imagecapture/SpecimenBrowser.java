/**
 * SpecimenBrowser.java
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
import edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditor;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.TableColumnManager;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.SessionException;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * SpecimenBrowser is a Searchable, Sortable, tabular view of multiple
 * specimens.
 */
public class SpecimenBrowser extends JPanel implements DataChangeListener {

    private static final long serialVersionUID = 1336228109288304785L;

    private static final Logger log =
            LoggerFactory.getLogger(SpecimenBrowser.class);

    private JScrollPane jScrollPane = null;
    private JTable jTable = null;
    private JPanel jPanel = null;
    private JTextField jTextField = null;
    private JComboBox jComboBox = null;
    private TableRowSorter<SpecimenListTableModel> sorter;
    private JTextField jTextFieldFamily = null;
    private JTextField jTextFieldDrawerNumber = null;
    private Map<String, Object> searchCriteria2 = null;
    private boolean useLike = false;
    private int maxResults = 0;
    private int offset = 0;

    /**
     * This method initializes an instance of SpecimenBrowser
     */
    public SpecimenBrowser() {
        super();
        useLike = false;
        initialize();
    }

    public SpecimenBrowser(Map<String, Object> criteria, boolean like, int limit, int offset) {
        super();
        this.useLike = like;
        this.searchCriteria2 = criteria;
        this.maxResults = limit;
        this.offset = offset;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(444, 290));
        this.add(getJScrollPane(), BorderLayout.CENTER);
        this.add(getJPanel(), BorderLayout.NORTH);
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            try {
                jScrollPane.setViewportView(getJTable());
            } catch (SessionException | TransactionException e) {
                log.debug(e.getMessage(), e);
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                        "Database Connection Error.");
                HibernateUtil.terminateSessionFactory();
                this.setVisible(false);
            }
            jScrollPane.setPreferredSize(new Dimension(444, 290));
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable();
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            // jTable.setAutoCreateRowSorter(true);
            SpecimenLifeCycle s = new SpecimenLifeCycle();
            SpecimenListTableModel model = null;
            if (searchCriteria2 != null) {
                model = new SpecimenListTableModel(
                        s.findBy(this.searchCriteria2, maxResults, offset, useLike)
                );
            } else {
                model = new SpecimenListTableModel(s.findAll());
            }
            jTable.setModel(model);
            new TableColumnManager(jTable);
            sorter = new TableRowSorter<>(model);
            int copyPasteOffset = BooleanUtils.toInteger(!SpecimenDetailsViewPane.copyPasteActivated);
            sorter.toggleSortOrder(SpecimenListTableModel.COL_BARCODE - copyPasteOffset);
            sorter.setComparator(SpecimenListTableModel.COL_COLLECTION_NR - copyPasteOffset, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof String && ((String) o1).trim().equals("")) {
                        o1 = Double.valueOf(0);
                    }
                    if (o2 instanceof String && ((String) o2).trim().equals("")) {
                        o2 = Double.valueOf(0);
                    }
                    if (o1 instanceof String && o2 instanceof String) {
                        return ((String) o1).compareToIgnoreCase((String) o2);
                    }
                    if (o1 instanceof Double && o2 instanceof Double) {
                        return ((Double) o1).compareTo((Double) o2);
                    }
                    if (o1 instanceof Double && o2 instanceof String) {
                        return -1;
                    }
                    if (o2 instanceof Double && o1 instanceof String) {
                        return 1;
                    }
                    log.error("Unexpected type in compare: {} - {}", o1, o2);
                    return 0;
                }
            });
            jTable.setRowSorter(sorter);
            jTable.setDefaultRenderer(Specimen.class, new ButtonRenderer());
            jTable.setDefaultEditor(Specimen.class, new ButtonEditor());
            if (SpecimenDetailsViewPane.copyPasteActivated) {
                jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.COL_COPY))
                        .setCellRenderer(new ButtonRenderer("Copy"));
                jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.COL_COPY))
                        .setCellEditor(new CopyRowButtonEditor(new JCheckBox()));
            }
            // set some column widths
            int characterWidth = Singleton.getSingletonInstance().getCharacterWidth();
            jTable.getColumnModel().getColumn(0).setPreferredWidth(characterWidth *
                    3);
            jTable.getColumnModel().getColumn(1).setPreferredWidth(characterWidth *
                    3);
            jTable.getColumnModel().getColumn(2).setPreferredWidth(characterWidth *
                    14);
        }
        return jTable;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.BOTH;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.gridx = 7;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 6;
            gridBagConstraints3.gridy = 0;
            JLabel jLabel3 = new JLabel();
            jLabel3.setText("Drawer:");
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = GridBagConstraints.BOTH;
            gridBagConstraints21.gridy = 0;
            gridBagConstraints21.weightx = 1.0;
            gridBagConstraints21.gridx = 5;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 4;
            gridBagConstraints11.gridy = 0;
            JLabel jLabel2 = new JLabel();
            jLabel2.setText("Family:");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints2.gridy = 0;
            JLabel jLabel1 = new JLabel();
            jLabel1.setText("Workflow:");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridx = 3;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            JLabel jLabel = new JLabel();
            jLabel.setText("Find Barcode:");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(jLabel, new GridBagConstraints());
            jPanel.add(getJTextField(), gridBagConstraints);
            jPanel.add(getJComboBox(), gridBagConstraints1);
            jPanel.add(jLabel1, gridBagConstraints2);
            jPanel.add(jLabel2, gridBagConstraints11);
            jPanel.add(getJTextFieldFamily(), gridBagConstraints21);
            jPanel.add(jLabel3, gridBagConstraints3);
            jPanel.add(getJTextFieldDrawerNumber(), gridBagConstraints4);
        }
        return jPanel;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    newFilter();
                }
            });
        }
        return jTextField;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBox() {
        if (jComboBox == null) {
            jComboBox = new JComboBox(WorkFlowStatus.getWorkFlowStatusValues());
            jComboBox.addItem("");
            jComboBox.setSelectedItem("");
            jComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jTextField.setText("");
                    newFilter();
                }
            });
        }
        return jComboBox;
    }

    private void newFilter() {
        RowFilter<SpecimenListTableModel, Object> rf = null;
        // If current expression doesn't parse, don't update.
        try {
            RowFilter<SpecimenListTableModel, Object> rf_family = null;
            RowFilter<SpecimenListTableModel, Object> rf_barcode = null;
            RowFilter<SpecimenListTableModel, Object> rf_drawer = null;
            RowFilter<SpecimenListTableModel, Object> rf_workflow = null;
            rf_family = RowFilter.regexFilter(jTextFieldFamily.getText(),
                    SpecimenListTableModel.COL_FAMILY);
            rf_barcode = RowFilter.regexFilter(jTextField.getText(),
                    SpecimenListTableModel.COL_BARCODE);
            // rf_drawer = RowFilter.regexFilter(jTextFieldDrawerNumber.getText(),
            // SpecimenListTableModel.COL_DRAWER);
            rf_workflow =
                    RowFilter.regexFilter(jComboBox.getSelectedItem().toString(),
                            SpecimenListTableModel.COL_WORKFLOW);
            ArrayList<RowFilter<SpecimenListTableModel, Object>> i =
                    new ArrayList<>();
            i.add(rf_family);
            i.add(rf_barcode);
            // i.add(rf_drawer);
            i.add(rf_workflow);
            rf = RowFilter.andFilter(i);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    /* (non-Javadoc)
     * @see
     *     edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#notifyDataHasChanged()
     */
    @Override
    public void notifyDataHasChanged() {
        ((SpecimenListTableModel) jTable.getModel()).fireTableDataChanged();
        log.debug("Data change notified.");
    }

    /**
     * This method initializes jTextFieldFamily
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldFamily() {
        if (jTextFieldFamily == null) {
            jTextFieldFamily = new JTextField();
            jTextFieldFamily.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    newFilter();
                }
            });
        }
        return jTextFieldFamily;
    }

    /**
     * This method initializes jTextFieldDrawerNumber
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDrawerNumber() {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = new JTextField();
            jTextFieldDrawerNumber.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    newFilter();
                }
            });
        }
        return jTextFieldDrawerNumber;
    }

    public int getRowCount() {
        int result = 0;
        if (jTable != null) {
            result = jTable.getRowCount();
        }
        return result;
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
