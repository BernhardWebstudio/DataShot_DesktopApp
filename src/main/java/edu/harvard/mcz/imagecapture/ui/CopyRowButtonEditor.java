package edu.harvard.mcz.imagecapture.ui;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.Specimen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CopyRowButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private Specimen target;

    public CopyRowButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public CopyRowButtonEditor(JTextField textField) {
        super(textField);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        try {
            target = (Specimen) value;
        } catch (Exception e) {
            // TODO: handle?
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            if (target != null) {
                ImageCaptureApp.lastEditedSpecimenCache = target;
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Copied specimen with id " + target.getSpecimenId() + ".");
            } else {
                Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Failed copying specimen.");
            }
        }
        isPushed = false;
        return label;
    }


    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}