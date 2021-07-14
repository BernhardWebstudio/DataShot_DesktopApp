/**
 * TemplateTableModel.java
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

import edu.harvard.mcz.imagecapture.PositionTemplate;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TemplateTableModel
 */
public class PositionTemplateTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -6485594139102438531L;

    private List<PositionTemplate> templates = null;

    /**
     * Default constructor with an initially empty set of templates.
     */
    public PositionTemplateTableModel() {
        templates = new ArrayList<PositionTemplate>();
    }

    /**
     * Constructor with an initial set of templates.
     */
    public PositionTemplateTableModel(List<PositionTemplate> aTemplateSet) {
        templates = aTemplateSet;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return templates.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = ((PositionTemplate) templates.toArray()[rowIndex]).getTemplateId();
                break;
            case 1:
                returnvalue = ((PositionTemplate) templates.toArray()[rowIndex]).getTemplateId();
                break;
            case 2:
                returnvalue = ((PositionTemplate) templates.toArray()[rowIndex]).getName();
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
                returnvalue = "";
                break;
            case 1:
                returnvalue = "TemplateId";
                break;
            case 2:
                returnvalue = "Name";
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = columnIndex == 0;
        return result;
    }


}
