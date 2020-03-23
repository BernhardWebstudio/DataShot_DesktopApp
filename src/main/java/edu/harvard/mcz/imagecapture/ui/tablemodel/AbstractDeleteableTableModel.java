package edu.harvard.mcz.imagecapture.ui.tablemodel;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractDeleteableTableModel extends AbstractTableModel {
    public abstract void deleteRow(int rowIndex);
}
