/**
 * UnitTrayLabelTableModel.java
 * edu.harvard.mcz.imagecapture.data
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
package edu.harvard.mcz.imagecapture.ui.tablemodel;

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * UnitTrayLabelTableModel
 */
public class UnitTrayLabelTableModel extends AbstractTableModel {

    // TODO: Add a cloning function
    // TODO: Better support for copy and paste of multiple cells.

    /**
     * When a new row is added, the value of ordinal will be set to zero.
     */
    public static final int NEXT_ORDINAL_ZERO = 0;
    /**
     * When a new row is added the value of ordinal will be set to the
     * value of the largest ordinal currently in the database plus 1.
     */
    public static final int NEXT_ORDINAL_MAXPLUSONE = 1;
    private static final Logger log =
            LoggerFactory.getLogger(UnitTrayLabelTableModel.class);
    private static final int COLUMN_TO_PRINT = 13;
    private static final int COLUMN_PRINTED = 14;
    private static final long serialVersionUID = -8022147291895055945L;

    private List<UnitTrayLabel> labels = null;

    public UnitTrayLabelTableModel(List<UnitTrayLabel> labels) {
        this.labels = labels;
    }

    /**
     *
     */
    public UnitTrayLabelTableModel() {
        UnitTrayLabelLifeCycle uls = new UnitTrayLabelLifeCycle();
        this.labels = uls.findAll();
    }

    public void setLabels(List<UnitTrayLabel> labels) {
        this.labels = labels;
    }

    public void addRow() {
        UnitTrayLabel newLabel = new UnitTrayLabel();
        UnitTrayLabelLifeCycle uls = new UnitTrayLabelLifeCycle();
        try {
            // TODO: Finish making this a configuration option.
            int nextOrdinalMethod = NEXT_ORDINAL_ZERO;
            int nextOrdinal = 0;
            if (nextOrdinalMethod == NEXT_ORDINAL_MAXPLUSONE) {
                nextOrdinal = uls.findMaxOrdinal() + 1;
            }
            if (nextOrdinalMethod == NEXT_ORDINAL_ZERO) {
                nextOrdinal = 0;
            }
            newLabel.setOrdinal(nextOrdinal);
            uls.persist(newLabel);
            this.labels.add(newLabel);
            this.fireTableDataChanged();
        } catch (SaveFailedException e1) {
            JOptionPane.showMessageDialog(
                    Singleton.getSingletonInstance().getMainFrame(),
                    "Save of new record failed.\n" + e1.getMessage(), "Save Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 15;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return labels.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = labels.get(rowIndex).getFamily();
                break;
            case 1:
                returnvalue = labels.get(rowIndex).getSubfamily();
                break;
            case 2:
                returnvalue = labels.get(rowIndex).getTribe();
                break;
            case 3:
                returnvalue = labels.get(rowIndex).getGenus();
                break;
            case 4:
                returnvalue = labels.get(rowIndex).getSpecificEpithet();
                break;
            case 5:
                returnvalue = labels.get(rowIndex).getSubspecificEpithet();
                break;
            case 6:
                returnvalue = labels.get(rowIndex).getInfraspecificRank();
                break;
            case 7:
                returnvalue = labels.get(rowIndex).getInfraspecificEpithet();
                break;
            case 8:
                returnvalue = labels.get(rowIndex).getAuthorship();
                break;
            case 9:
                returnvalue = labels.get(rowIndex).getUnNamedForm();
                break;
            case 10:
                returnvalue = labels.get(rowIndex).getDrawerNumber();
                break;
            case 11:
                returnvalue = labels.get(rowIndex).getCollection();
                break;
            case 12:
                returnvalue = Integer.toString(labels.get(rowIndex).getOrdinal());
                break;
            case COLUMN_TO_PRINT:
                returnvalue = Integer.toString(labels.get(rowIndex).getNumberToPrint());
                break;
            case COLUMN_PRINTED:
                returnvalue = Integer.toString(labels.get(rowIndex).getPrinted());
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = "Family";
                break;
            case 1:
                returnvalue = "SubFamily";
                break;
            case 2:
                returnvalue = "Tribe";
                break;
            case 3:
                returnvalue = "Genus";
                break;
            case 4:
                returnvalue = "Species";
                break;
            case 5:
                returnvalue = "Subspecies";
                break;
            case 6:
                returnvalue = "Infra Rank";
                break;
            case 7:
                returnvalue = "Infraspecific";
                break;
            case 8:
                returnvalue = "Authorship";
                break;
            case 9:
                returnvalue = "UnNamed Form";
                break;
            case 10:
                returnvalue = "Drawer";
                break;
            case 11:
                returnvalue = "Collection";
                break;
            case 12:
                returnvalue = "Sort";
                break;
            case COLUMN_TO_PRINT:
                returnvalue = "To Print";
                break;
            case COLUMN_PRINTED:
                returnvalue = "Printed";
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != COLUMN_PRINTED;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
     *     int)
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        log.debug("Object to save at " + rowIndex + "," + columnIndex + " is " +
                value.toString());
        switch (columnIndex) {
            case 0:
                labels.get(rowIndex).setFamily(value.toString());
                break;
            case 1:
                labels.get(rowIndex).setSubfamily(value.toString());
                break;
            case 2:
                labels.get(rowIndex).setTribe(value.toString());
                break;
            case 3:
                labels.get(rowIndex).setGenus(value.toString());
                break;
            case 4:
                labels.get(rowIndex).setSpecificEpithet(value.toString());
                break;
            case 5:
                labels.get(rowIndex).setSubspecificEpithet(value.toString());
                break;
            case 6:
                labels.get(rowIndex).setInfraspecificRank(value.toString());
                break;
            case 7:
                labels.get(rowIndex).setInfraspecificEpithet(value.toString());
                break;
            case 8:
                labels.get(rowIndex).setAuthorship(value.toString());
                break;
            case 9:
                labels.get(rowIndex).setUnNamedForm(value.toString());
                break;
            case 10:
                labels.get(rowIndex).setDrawerNumber(value.toString());
                break;
            case 11:
                labels.get(rowIndex).setCollection(value.toString());
                break;
            case 12:
                labels.get(rowIndex).setOrdinal(Integer.parseInt(value.toString()));
                break;
            case COLUMN_TO_PRINT:
                labels.get(rowIndex).setNumberToPrint(
                        (Integer.parseInt(value.toString())));
                break;
            case COLUMN_PRINTED:
                labels.get(rowIndex).setPrinted((Integer.parseInt(value.toString())));
                break;
        }
        saveRow(rowIndex);
    }

    /**
     * @return
     */
    public List<UnitTrayLabel> getList() {
        return labels;
    }

    private void saveRow(int rowIndex) {
        UnitTrayLabelLifeCycle uls = new UnitTrayLabelLifeCycle();
        try {
            uls.attachDirty(labels.get(rowIndex));
        } catch (SaveFailedException e) {
            log.error("Error", e);
            JOptionPane.showMessageDialog(
                    Singleton.getSingletonInstance().getMainFrame(),
                    "Save failed for a unit tray label."
                            + "\n" + e.getMessage(),
                    "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
