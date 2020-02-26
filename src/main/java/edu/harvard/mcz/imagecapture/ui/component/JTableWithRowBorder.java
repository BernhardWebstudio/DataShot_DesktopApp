package edu.harvard.mcz.imagecapture.ui.component;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableWithRowBorder extends JTable {

    public TableWithRowBorder(AbstractTableModel tableModel) {
        super(tableModel);
    }

    @Override
    public Component prepareRenderer(
            TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent) c;

        //  Color row based on a cell value
        //  Alternate row color
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);
        }

        jc.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));


        //  Use bold font on selected row

        return c;
    }
}
