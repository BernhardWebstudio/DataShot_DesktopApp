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

import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
public class SpecimenPartsAttrTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -4139892019645663114L;
    private static final Logger log =
            LoggerFactory.getLogger(SpecimenPartsAttrTableModel.class);
    private final Collection<SpecimenPartAttribute> specimenPartAttributes;

    public SpecimenPartsAttrTableModel() {
        this(null);
    }

    /**
     * @param specimenPartAttributes
     */
    public SpecimenPartsAttrTableModel(
            Collection<SpecimenPartAttribute> specimenPartAttributes) {
        super();
        if (specimenPartAttributes != null) {
            this.specimenPartAttributes = specimenPartAttributes;
        } else {
            this.specimenPartAttributes = new HashSet<SpecimenPartAttribute>();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return specimenPartAttributes.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = "Type";
                break;
            case 1:
                returnvalue = "Value";
                break;
            case 2:
                returnvalue = "Units";
                break;
            case 3:
                returnvalue = "Remarks";
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        switch (columnIndex) {
            case 0:
                result =
                        ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                                .getAttributeType();
                break;
            case 1:
                result =
                        ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                                .getAttributeValue();
                break;
            case 2:
                result =
                        ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                                .getAttributeUnits();
                break;
            case 3:
                result =
                        ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                                .getAttributeRemark();
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
        switch (columnIndex) {
            case 0:
                ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                        .setAttributeType((String) aValue);
                break;
            case 1:
                ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                        .setAttributeValue((String) aValue);
                break;
            case 2:
                ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                        .setAttributeUnits((String) aValue);
                break;
            case 3:
                ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex])
                        .setAttributeRemark((String) aValue);
                break;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = false;
        return result;
    }

    /**
     * @param rowIndex row to be deleted
     */
    public void deleteRow(int rowIndex) {
        SpecimenPartAttribute toRemove =
                ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex]);
//        SpecimenPartAttributeLifeCycle spals = new SpecimenPartAttributeLifeCycle();
        try {
//            spals.remove(toRemove);
            specimenPartAttributes.remove(toRemove);
            fireTableDataChanged();
        } catch (Exception e) {
            log.error("Failed to remove specimen part attr", e);
        }
    }

    public SpecimenPartAttribute getRowObject(int rowIndex) {
        return ((SpecimenPartAttribute) specimenPartAttributes.toArray()[rowIndex]);
    }
}
