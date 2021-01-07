/**
 * CountValueTableModel.java
 * edu.harvard.mcz.imagecapture.struct
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

import edu.harvard.mcz.imagecapture.entity.fixed.CountValue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CountValueTableModel extends AbstractTableModel {

  private static final Logger log =
      LoggerFactory.getLogger(CountValueTableModel.class);
  private static final long serialVersionUID = -1744182887180402474L;

  private List<CountValue> items = null;
  private String itemType = null;

  public CountValueTableModel() {
    items = new ArrayList<CountValue>();
    itemType = "Value";
  }

  public CountValueTableModel(List<CountValue> countValueItemList) {
    items = countValueItemList;
    itemType = "Value";
  }

  public CountValueTableModel(List<CountValue> countValueItemList,
                              String valueColumnHeader) {
    items = countValueItemList;
    itemType = valueColumnHeader;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return items.size();
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
    return 2;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Object result = null;
    switch (columnIndex) {
    case 0:
      result = items.get(rowIndex).getCount();
      break;
    case 1:
      result = items.get(rowIndex).getValue();
      break;
    }
    return result;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex) {
    String result = null;
    switch (columnIndex) {
    case 0:
      result = "Count";
      break;
    case 1:
      result = itemType;
      break;
    }
    return result;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return Integer.class;
    case 1:
      return String.class;
    }
    return Object.class;
  }
}
