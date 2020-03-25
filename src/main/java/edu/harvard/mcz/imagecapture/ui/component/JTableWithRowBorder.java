package edu.harvard.mcz.imagecapture.ui.component;

import edu.harvard.mcz.imagecapture.ui.DataShotColors;
import edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Default JTable used for tables in SpecimenDetailsViewPane
 */
public class JTableWithRowBorder extends JTable {
    private static final Log log = LogFactory.getLog(JTableWithRowBorder.class);

    protected boolean deletableRows = false;
    protected int clickedRow;
    protected String objectName;
    protected JPanel parentPane;

    protected ArrayList<ActionListener> listeners = new ArrayList<>();

    public JTableWithRowBorder(AbstractDeleteableTableModel tableModel) {
        super(tableModel);
        this.setShowGrid(true);
    }

    /**
     * Add listener to listen for any events changing the content of this table
     *
     * @param listener
     */
    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Set the name of the entity in the table
     *
     * @param name the entity name
     */
    public void setObjectName(String name) {
        this.objectName = name;
    }

    /**
     * Set the pane in which to display notification etc.
     *
     * @param parentPane
     */
    public void setParentPane(JPanel parentPane) {
        this.parentPane = parentPane;
    }

    /**
     * Enable the option to delete rows.
     * Note that only the events which lead to data change are emitted.
     */
    public void enableDeleteability() {
        if (deletableRows == false) {
            deletableRows = true;

            JPopupMenu jPopupDeletor = new JPopupMenu();
            JMenuItem menuItemDeleteRow = new JMenuItem("Delete Row");
            JTableWithRowBorder tableWithRowBorder = this;
            menuItemDeleteRow.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (clickedRow >= 0) {
                            int ok = JOptionPane.showConfirmDialog(parentPane, "Delete the selected " + objectName + "?", "Delete " + objectName, JOptionPane.OK_CANCEL_OPTION);
                            if (ok == JOptionPane.OK_OPTION) {
                                log.debug("deleting " + objectName + "s row " + clickedRow);
                                ((AbstractDeleteableTableModel) tableWithRowBorder.getModel()).deleteRow(clickedRow);
                                tableWithRowBorder.emitEvent(e);
                            } else {
                                log.debug(objectName + " row delete canceled by user.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(parentPane, "Unable to select row to delete.  Try empting the value and pressing Save.");
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        JOptionPane.showMessageDialog(parentPane, "Failed to delete a " + objectName + " row. " + ex.getMessage());
                    }
                }
            });
            jPopupDeletor.add(menuItemDeleteRow);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        clickedRow = ((JTable) e.getComponent()).getSelectedRow();
                        jPopupDeletor.show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        clickedRow = ((JTable) e.getComponent()).getSelectedRow();
                        jPopupDeletor.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

            this.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    tableWithRowBorder.emitEvent(new ActionEvent(e, e.getID(), e.toString()));
                }
            });
        }
    }

    @Override
    public Component prepareRenderer(
            TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent) c;

        //  Color row based on a cell value
        //  Alternate row color
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0 ? getBackground() : DataShotColors.VERY_LIGHT_GRAY);
        }

        jc.setBorder(new MatteBorder(0, 0, 1, 0, DataShotColors.VERY_DARK_GRAY));


        //  Use bold font on selected row

        return c;
    }

    /**
     * @param event
     */
    protected void emitEvent(ActionEvent event) {
        for (ActionListener listener :
                listeners) {
            listener.actionPerformed(event);
        }
    }
}
