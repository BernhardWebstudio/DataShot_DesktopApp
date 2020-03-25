/**
 * CollectorTableModel.java
 * edu.harvard.mcz.imagecapture.data;
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

import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Table Model for Collector objects showing just the name of the Collector.
 */
public class CollectorTableModel extends AbstractDeleteableTableModel {

    private static final long serialVersionUID = -3022078380872976717L;

    private static final Log log = LogFactory.getLog(CollectorTableModel.class);

    private Set<Collector> collectors;

    public CollectorTableModel() {
        collectors = new HashSet<>();
    }

    public CollectorTableModel(Set<Collector> aCollectorList) {
        collectors = aCollectorList;
    }


    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String returnvalue = null;
        returnvalue = "Name";
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        //allie fix
        if (collectors == null)
            return 0;
        else
            return collectors.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= collectors.size()) {
            log.warn("Cannot get value at row, col: " + rowIndex + ", " + columnIndex + " due to size " + collectors.size() + " in CollectorTable");
            return null;
        }

        Object returnvalue = null;
        returnvalue = ((Collector) collectors.toArray()[rowIndex]).getCollectorName();
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex >= collectors.size()) {
            log.warn("Cannot set value " + value.toString() + " due to size " + collectors.size() + " in CollectorTable at row, col: " + rowIndex + ", " + columnIndex);
            return;
        }

        try {
            ((Collector) collectors.toArray()[rowIndex]).setCollectorName(value.toString());

        } catch (Exception e) {
            log.debug("Exception found in CollectorTableModel.setValueAt: unable to get or convert value");
            //Original code from Paul - caused issues
            // Object is an agentname not a string.
            //((Collector)collectors.toArray()[rowIndex]).setCollectorName(((MCZbaseAuthAgentName)value).getAgent_name());
        }
    }


    /**
     * addCollector adds a Collector to this model as a new row
     *
     * @param collector the collector to add to the table model.
     */
    public void addCollector(Collector collector) {
        collectors.add(collector);
        this.fireTableDataChanged();
    }

    /**
     * @param rowIndex row to be deleted
     */
    public void deleteRow(int rowIndex) {
        Collector toRemove = ((Collector) collectors.toArray()[rowIndex]);
        CollectorLifeCycle spals = new CollectorLifeCycle();
        try {
            spals.delete(toRemove);
            collectors.remove(toRemove);
            fireTableDataChanged();
        } catch (SaveFailedException e) {
            log.error(e.getMessage());
        }
    }

}
