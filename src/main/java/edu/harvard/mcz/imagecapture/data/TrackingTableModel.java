/**
 *
 */
package edu.harvard.mcz.imagecapture.data;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/** Table model for displaying Tracking records in a JTable.
 *
 *
 *
 */
public class TrackingTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -391531601684450925L;

    private List<Tracking> events = null;

    public TrackingTableModel() {
        events = new ArrayList<Tracking>();
    }

    public TrackingTableModel(List<Tracking> aTrackingSet) {
        events = aTrackingSet;
    }

    public void setTrackings(List<Tracking> aTrackingSet) {
        events = aTrackingSet;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return events.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = "Specimen";
                break;
            case 1:
                returnvalue = "User";
                break;
            case 2:
                returnvalue = "Event";
                break;
            case 3:
                returnvalue = "Timestamp";
                break;
        }
        return returnvalue;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String returnvalue = null;
        switch (columnIndex) {
            case 0:
                returnvalue = events.get(rowIndex).getSpecimen().getBarcode();
                break;
            case 1:
                returnvalue = events.get(rowIndex).getUser();
                break;
            case 2:
                returnvalue = events.get(rowIndex).getEventType();
                break;
            case 3:
                returnvalue = events.get(rowIndex).getEventDateTime().toString();
                break;
        }
        return returnvalue;
    }

}
