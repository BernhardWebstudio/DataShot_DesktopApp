package edu.harvard.mcz.imagecapture.ui.component;

import edu.harvard.mcz.imagecapture.ui.DataShotColors;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTableWithRowBorder extends JTable {

    public JTableWithRowBorder(AbstractTableModel tableModel) {
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
            c.setBackground(row % 2 == 0 ? getBackground() : DataShotColors.VERY_LIGHT_GRAY);
        }

        jc.setBorder(new MatteBorder(0, 0, 1, 0, DataShotColors.VERY_DARK_GRAY));


        //  Use bold font on selected row

        return c;
    }
}
