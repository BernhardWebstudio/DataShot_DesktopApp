/**
 * SpecimenListTableModel.java
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

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * SpecimenListTableModel
 */
public class SpecimenListTableModel extends AbstractTableModel {

    public static final int COL_ID = 0;
    public static final int COL_COPY = 1;
    public static final int COL_BARCODE = 2;
    public static final int COL_WORKFLOW = 3;
    public static final int COL_FAMILY = 4;
    public static final int COL_SUBFAMILY = 5;
    public static final int COL_TRIBE = 6;
    public static final int COL_GENUS = 7;
    public static final int COL_SPECIFIC = 8;
    public static final int COL_SUBSPECIFIC = 9;
    public static final int COL_COUNTRY = 10;
    public static final int COL_DIVISION = 11;
    public static final int COL_VERBLOCALITY = 12;
    public static final int COL_COLLECTION = 13;
    public static final int COL_COLLECTION_NR = 14;
    public static final int COLUMCOUNT = 15;
    private static final Logger log =
            LoggerFactory.getLogger(SpecimenListTableModel.class);
    private static final long serialVersionUID = -8394267503927374758L;
    private List<Specimen> specimens = null;

    public SpecimenListTableModel(List<Specimen> specimenList) {
        specimens = specimenList;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return SpecimenDetailsViewPane.copyPasteActivated ? COLUMCOUNT : COLUMCOUNT - 1;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        if (specimens == null) {
            return 0;
        }
        return specimens.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (specimens == null || specimens.size() <= rowIndex) {
            return null;
        }
        Specimen s = specimens.get(rowIndex);
        if (s == null) {
            return null;
        }
        Object result = null;
        if (columnIndex == COL_ID) {
            result = s;
//            result = s.getSpecimenId();
        } else {
            if (!SpecimenDetailsViewPane.copyPasteActivated) {
                columnIndex += 1;
            }
            switch (columnIndex) {
                case COL_ID:
                    result = s;
//                     result = s.getSpecimenId();
                    break;
                case COL_COPY:
                    result = s;
                    break;
                case COL_BARCODE:
                    result = s.getBarcode();
                    break;
                case COL_WORKFLOW:
                    result = s.getWorkFlowStatus();
                    break;
                case COL_FAMILY:
                    result = s.getFamily();
                    break;
                case COL_SUBFAMILY:
                    result = s.getSubfamily();
                    break;
                case COL_TRIBE:
                    result = s.getTribe();
                    break;
                case COL_GENUS:
                    result = s.getGenus();
                    break;
                case COL_SPECIFIC:
                    result = s.getSpecificEpithet();
                    break;
                case COL_SUBSPECIFIC:
                    result = s.getSubspecificEpithet();
                    break;
                case COL_VERBLOCALITY:
                    result = s.getVerbatimLocality();
                    break;
                case COL_COUNTRY:
                    result = s.getCountry();
                    break;
                case COL_DIVISION:
                    result = s.getPrimaryDivison();
                    break;
                case COL_COLLECTION:
                    result = s.getCollection();
                    break;
                case COL_COLLECTION_NR:
                    String collectionNr = s.getFirstNumberWithType("Collection Number");
                    if (collectionNr != null) {
                        try {
                            result = Double.parseDouble(collectionNr);
                        } catch (NumberFormatException e) {
                            result = collectionNr;
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * Must be implemented for ButtonEditor to work.  Needs to return Long for
     * ID column that is to contain button to work with ButtonEditor.
     */

    public Class getColumnClass(int c) {
        // Given current implementation of button in SpecimenBrowser,
        // needs to return Long for ID column that is to contain button
        // and ** Must Not ** return Long for any other column).
        Class result = String.class; // Default value to return when table is empty.
        if (c == COL_COLLECTION_NR) {
            return Double.class;
        }
        try {
            result = getValueAt(0, c).getClass();
        } catch (NullPointerException e) {
            // continue
        }
        return result;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String result = null;
        if (columnIndex == COL_ID) {
            result = "Edit";
        } else {
            if (!SpecimenDetailsViewPane.copyPasteActivated) {
                columnIndex += 1;
            }
            switch (columnIndex) {
                case COL_COPY:
                    result = "Copy";
                    break;
                case COL_BARCODE:
                    result = "Barcode";
                    break;
                case COL_WORKFLOW:
                    result = "Workflow";
                    break;
                case COL_FAMILY:
                    result = "Family";
                    break;
                case COL_SUBFAMILY:
                    result = "Subfamily";
                    break;
                case COL_TRIBE:
                    result = "Tribe";
                    break;
                case COL_GENUS:
                    result = "Genus";
                    break;
                case COL_SPECIFIC:
                    result = "Species";
                    break;
                case COL_SUBSPECIFIC:
                    result = "Subspecies";
                    break;
                case COL_VERBLOCALITY:
                    result = "Verbatim Locality";
                    break;
                case COL_COUNTRY:
                    result = "Country";
                    break;
                case COL_DIVISION:
                    result = "State/Province";
                    break;
                case COL_COLLECTION:
                    result = "Collection";
                    break;
                case COL_COLLECTION_NR:
                    result = "Collection Nr.";
                    break;
            }
        }
        return result;
    }

    /**
     * Must be implemented for ButtonEditor to work.
     *
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = columnIndex == COL_ID || (SpecimenDetailsViewPane.copyPasteActivated && columnIndex == COL_COPY);
        return result;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
     *     int)
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        super.setValueAt(value, rowIndex, columnIndex);
        // don't do anything particular, edit is only to enable a buttonEditor as a
        // renderer that can hear mouse clicks.
    }
}
