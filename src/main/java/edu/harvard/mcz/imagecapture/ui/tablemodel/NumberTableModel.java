package edu.harvard.mcz.imagecapture.ui.tablemodel;

import edu.harvard.mcz.imagecapture.entity.Number;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NumberTableModel Table model for displaying and editing Number records in a
 * JTable.
 */
public class NumberTableModel extends AbstractDeleteableTableModel {
  public static final int COLUMN_NUMBER = 0;
  public static final int COLUMN_TYPE = 1;
  private static final long serialVersionUID = 2244991738218368487L;
  private static final Logger log =
      LoggerFactory.getLogger(NumberTableModel.class);
  private Set<Number> numbers = null;

  public NumberTableModel() { numbers = new HashSet<Number>(); }

  public NumberTableModel(Set<Number> aNumberList) { numbers = aNumberList; }

  public void addNumber(Number aNumber) {
    numbers.add(aNumber);
    this.fireTableDataChanged();
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
    return 2;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex) {
    String returnvalue = null;
    switch (columnIndex) {
    case COLUMN_NUMBER:
      returnvalue = "Number";
      break;
    case COLUMN_TYPE:
      returnvalue = "Type";
      break;
    }
    return returnvalue;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return numbers.size();
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (rowIndex >= numbers.size()) {
      log.warn("Tried to get value in NumberTable at rowIndex higher than " +
               "row count; rowIndex: " +
               rowIndex + "; columnIndex: " + columnIndex);
      return null;
    }
    Object returnvalue = null;
    switch (columnIndex) {
    case 0:
      returnvalue = ((Number)numbers.toArray()[rowIndex]).getNumber();
      break;
    case COLUMN_TYPE:
      returnvalue = ((Number)numbers.toArray()[rowIndex]).getNumberType();
      break;
    }
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
    if (rowIndex >= numbers.size()) {
      log.warn("Tried to set value in number table at rowIndex higher than " +
               "row count; rowIndex: " +
               rowIndex + "; columnIndex: " + columnIndex +
               "; value: " + value);
      return;
    }

    String stringValue = value != null ? value.toString().trim() : "";

    switch (columnIndex) {
    case 0:
      ((Number)numbers.toArray()[rowIndex]).setNumber(stringValue);
      break;
    case COLUMN_TYPE:
      ((Number)numbers.toArray()[rowIndex]).setNumberType(stringValue);
      break;
    }

    // Fire table cell updated event
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  /**
   * Remove a row from this table
   *
   * @param rowIndex row to be deleted
   */
  public void deleteRow(int rowIndex) {
    Number toRemove = ((Number)numbers.toArray()[rowIndex]);
    //        NumberLifeCycle spals = new NumberLifeCycle();
    try {
      //            spals.delete(toRemove);
      numbers.remove(toRemove);
      fireTableDataChanged();
    } catch (Exception e) {
      log.error("Failed to remove number", e);
    }
  }
}
