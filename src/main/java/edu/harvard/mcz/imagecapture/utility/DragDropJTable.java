/**
 * DragDropJTable.java
 * edu.harvard.mcz.imagecapture.utility
 * <p>
 * Modified from myTable class at:
 * http://forums.sun.com/thread.jspa?forumID=57&threadID=497102
 * <p>
 */
package edu.harvard.mcz.imagecapture.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * DragDropJTable: a JTable that supports drag and drop
 */
public class DragDropJTable extends JTable {

    private static final long serialVersionUID = -8855350581096969272L;

    private static final Logger log =
            LoggerFactory.getLogger(DragDropJTable.class);

    DragDropJTable thisTable = null;

    public DragDropJTable(TableModel model) {
        super(model);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable(int width, int height) {
        super(width, height);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable() {
        super();
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable(TableModel dm, TableColumnModel cm,
                          ListSelectionModel sm) {
        super(dm, cm, sm);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    public DragDropJTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        setTransferHandler(new DdTransferHandler());
        setDragEnabled(true);
        thisTable = this;
    }

    private String[] getStringArray(String inStr, char ctkn) {
        String[] x;
        if (inStr.length() == 0) {
            x = new String[1];
            x[0] = "";
            return x;
        }
        int i = 0;
        String tmp = "";
        ArrayList<String> AL = new ArrayList<String>(20);
        while (i < inStr.length()) {
            if (inStr.charAt(i) == ctkn) {
                AL.add(tmp);
                tmp = "";
            } else
                tmp += inStr.charAt(i);
            i++;
        }
        AL.add(tmp);
        x = new String[AL.size()];
        for (i = 0; i < AL.size(); i++)
            x[i] = AL.get(i);
        return x;
    }

    /**
     * Convert the selection to tab delimited cells and
     * newline delimited rows.
     *
     * @return
     */
    public StringSelection getTransferContents() {
        int br = getSelectedRow();
        int bc = getSelectedColumn();
        int er = br + getSelectedRowCount();
        int ec = bc + getSelectedColumnCount();
        String out = "";
        for (int row = br; row < er; row++) {
            for (int col = bc; col < ec; col++) {
                out += getValueAt(row, col);
                if (col + 1 < ec)
                    out += "\t";
            }
            if (er - br > 1)
                out += "\n";
        }
        return new StringSelection(out);
    }

    /**
     * DdTransferHandler Custom Drag and drop transfer handler.
     */
    class DdTransferHandler extends TransferHandler {

        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        public boolean canImport(TransferSupport support) {
            // Only import Strings
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        public Transferable createTransferable(JComponent comp) {
            return getTransferContents();
        }

        public void exportDone(JComponent c, Transferable contents, int action) {
        }

        public boolean importData(TransferSupport support) {
            try {
                log.debug("Getting Data Transfer for DragDropJTable",
                        support.getTransferable().getTransferData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException e1) {
                log.error("Error", e1);
            } catch (IOException e1) {
                log.error("Error", e1);
            }
            if (canImport(support)) {
                int targetRow = getSelectedRow();
                int targetCol = getSelectedColumn();
                if (support.isDrop()) {
                    // If this is a drop action (rather than a paste), then the
                    // selected row/column will be the source, not the destination.
                    JTable.DropLocation dl =
                            (JTable.DropLocation) support.getDropLocation();
                    targetRow = dl.getRow();
                    targetCol = dl.getColumn();
                }
                try {
                    String line = (String) support.getTransferable().getTransferData(
                            DataFlavor.stringFlavor);
                    int start = 0;
                    int end = line.indexOf("\n");
                    if (end < 0) {
                        if (line.indexOf('\t') < 0) {
                            // Single cell
                            setValueAt(line, targetRow, targetCol);
                            return true;
                        } else {
                            end = line.length();
                        }
                    }
                    String[] dropCells;
                    while (end <= line.length()) {
                        // More than one cell
                        dropCells = getStringArray(line.substring(start, end), '\t');
                        for (int j = 0; j < dropCells.length; j++) {
                            setValueAt(dropCells[j], targetRow, targetCol + j);
                        }
                        targetRow++;
                        start = end + 1;
                        if (start >= line.length()) {
                            break;
                        }
                        end = line.substring(start).indexOf("\n");
                        if (end >= 0) {
                            end += start;
                        } else {
                            end = line.length();
                        }
                    }
                    if (isEditing()) {
                        getCellEditor().stopCellEditing();
                    }
                    ((AbstractTableModel) getModel()).fireTableDataChanged();
                    repaint();
                } catch (Throwable e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                // Only import strings
                return false;
            }
            return true;
        }
    }
}
