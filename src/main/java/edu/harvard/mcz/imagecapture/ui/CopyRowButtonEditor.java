package edu.harvard.mcz.imagecapture.ui;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

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
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
				Specimen toCopy = target;
				if (target.getSpecimenId() != null && !target.isFullyLoaded()) {
					Specimen full = SpecimenCache.get(target.getSpecimenId());
					if (full == null || !full.isFullyLoaded()) {
						SpecimenLifeCycle sls = new SpecimenLifeCycle();
						try {
							full = sls.findById(target.getSpecimenId());
							if (full != null && full.isFullyLoaded()) {
								SpecimenCache.put(full);
							}
						} catch (Exception ex) {
						}
					}
					if (full != null && full.isFullyLoaded()) {
						toCopy = full;
					}
				}
				ImageCaptureApp.lastEditedSpecimenCache = toCopy;
				if (Singleton.getSingletonInstance().getMainFrame() != null) {
					Singleton.getSingletonInstance().getMainFrame()
							.setStatusMessage("Copied specimen with id " + toCopy.getSpecimenId() + ".");
				}
			} else {
				if (Singleton.getSingletonInstance().getMainFrame() != null) {
					Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Failed copying specimen.");
				}
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
