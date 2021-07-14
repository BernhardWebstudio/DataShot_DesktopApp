/**
 * SpecimenPartsTableModel
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
package edu.harvard.mcz.imagecapture.ui.tablemodel;

import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SpecimenPartsTableModel extends AbstractDeleteableTableModel {

    private static final long serialVersionUID = -4139892019645663114L;
    private static final Logger log =
            LoggerFactory.getLogger(SpecimenPartsTableModel.class);
    private final Set<SpecimenPart> specimenParts;

    public SpecimenPartsTableModel() {
        super();
        this.specimenParts = new HashSet<SpecimenPart>();
    }

    /**
     * @param specimenParts
     */
    public SpecimenPartsTableModel(Set<SpecimenPart> specimenParts) {
        super();
        this.specimenParts = specimenParts;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return specimenParts.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = "Part";
                break;
            case 1:
                returnvalue = "Prep.";
                break;
            case 2:
                returnvalue = "Count";
                break;
            case 3:
                returnvalue = "Attributes";
                break;
            case 4:
                returnvalue = "";
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= specimenParts.size()) {
            log.warn("Cannot get value at row, col: " + rowIndex + ", " +
                    columnIndex + " due to size " + specimenParts.size() +
                    " in SpecimenPartsTable");
            return null;
        }
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = ((SpecimenPart) specimenParts.toArray()[rowIndex]).getPartName();
                break;
            case 1:
                result =
                        ((SpecimenPart) specimenParts.toArray()[rowIndex]).getPreserveMethod();
                break;
            case 2:
                result = ((SpecimenPart) specimenParts.toArray()[rowIndex]).getLotCount();
                break;
            case 3:
                result = ((SpecimenPart) specimenParts.toArray()[rowIndex])
                        .getPartAttributeValuesConcat();
                break;
            case 4:
                result = specimenParts.toArray()[rowIndex];
                break;
            default:
                log.warn("Undefined column with index " + columnIndex);
                break;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
     *     int)
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= specimenParts.size()) {
            log.warn("Cannot set value " + aValue.toString() + " due to size " +
                    specimenParts.size() + " in SpecimenPartsTable at row, col: " +
                    rowIndex + ", " + columnIndex);
            return;
        }
        switch (columnIndex) {
            case 0:
                ((SpecimenPart) specimenParts.toArray()[rowIndex])
                        .setPartName((String) aValue);
                break;
            case 1:
                ((SpecimenPart) specimenParts.toArray()[rowIndex])
                        .setPreserveMethod((String) aValue);
                break;
            case 2:
                ((SpecimenPart) specimenParts.toArray()[rowIndex])
                        .setLotCount(Integer.parseInt((String) aValue));
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                log.warn("Undefined column with index " + columnIndex);
                break;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = columnIndex != 3;
        return result;
    }

    /**
     * @param rowIndex row to be deleted
     */
    public void deleteRow(int rowIndex) {
        SpecimenPart toRemove = ((SpecimenPart) specimenParts.toArray()[rowIndex]);
//        SpecimenPartLifeCycle spals = new SpecimenPartLifeCycle();
        try {
//            spals.remove(toRemove);
            specimenParts.remove(toRemove);
            fireTableDataChanged();
        } catch (Exception e) {
            log.error("Failed to remove specimen part", e);
        }
    }
}
