/**
 * GenusSpeciesCountTableModel.java
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

import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GenusSpeciesCountTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1907248383164693796L;

    private static final Logger log =
            LoggerFactory.getLogger(GenusSpeciesCountTableModel.class);

    private List<GenusSpeciesCount> genusSpeciesCounts = null;

    public GenusSpeciesCountTableModel() {
        genusSpeciesCounts = new ArrayList<GenusSpeciesCount>();
    }

    public GenusSpeciesCountTableModel(
            List<GenusSpeciesCount> genusSpeciesCounts) {
        this.genusSpeciesCounts = genusSpeciesCounts;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return genusSpeciesCounts.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = columnIndex == 0;
        return result;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = genusSpeciesCounts.get(rowIndex);
                break;
            case 1:
                returnvalue = genusSpeciesCounts.get(rowIndex).getCount().toString();
                break;
            case 2:
                returnvalue = genusSpeciesCounts.get(rowIndex).getGenus() + " " +
                        genusSpeciesCounts.get(rowIndex).getSpecificEpithet();
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return GenusSpeciesCount.class;
        } else {
            return String.class;
        }
    }
}
