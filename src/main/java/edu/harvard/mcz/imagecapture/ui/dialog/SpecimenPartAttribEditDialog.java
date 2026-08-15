/**
 * SpecimenPartAttribEditDialog.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute;
import edu.harvard.mcz.imagecapture.entity.fixed.Caste;
import edu.harvard.mcz.imagecapture.entity.fixed.LifeStage;
import edu.harvard.mcz.imagecapture.entity.fixed.Sex;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

/**
 * Dialog for editing a single SpecimenPartAttribute using declarative form
 * bindings.
 */
public class SpecimenPartAttribEditDialog extends JDialog {

	private static final long serialVersionUID = -549010965241755136L;

	private final SpecimenPartAttribEditDialog thisDialog;
	private final SpecimenPartAttribute targetAttribute;
	private final FormBindingContext<SpecimenPartAttribute> bindingContext;

	private JComboBox<String> comboBoxType;
	private JComboBox<String> comboBoxValue;

	public SpecimenPartAttribEditDialog() {
		this(new SpecimenPartAttribute());
	}

	public SpecimenPartAttribEditDialog(SpecimenPartAttribute attribute) {
		this.thisDialog = this;
		this.targetAttribute = attribute != null ? attribute : new SpecimenPartAttribute();
		this.bindingContext = new FormBindingContext<>(SpecimenPartAttribute.class, true);
		init();
		bindingContext.readFrom(targetAttribute);
		if (targetAttribute.getAttributeType() != null) {
			configureComboBoxValue(targetAttribute.getAttributeType());
			comboBoxValue.setSelectedItem(targetAttribute.getAttributeValue());
		}
	}

	private void init() {
		setTitle("Edit Part Attribute");
		setBounds(100, 100, 440, 230);
		getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 10"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPanel.add(new JLabel("Attribute Type:"), "tag label, right");
		comboBoxType = bindingContext.bindComboBox("attributeType",
				new String[]{"caste", "scientific name", "sex", "life stage"}, SpecimenPartAttribute::getAttributeType,
				SpecimenPartAttribute::setAttributeType, cb -> cb.addActionListener(e -> {
					if (cb.getSelectedItem() != null) {
						configureComboBoxValue(cb.getSelectedItem().toString());
					}
				}));
		contentPanel.add(comboBoxType, "grow");

		contentPanel.add(new JLabel("Value:"), "tag label, right");
		comboBoxValue = bindingContext.bindComboBox("attributeValue", Caste.getCasteValues(),
				SpecimenPartAttribute::getAttributeValue, SpecimenPartAttribute::setAttributeValue);
		contentPanel.add(comboBoxValue, "grow");

		contentPanel.add(new JLabel("Units:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField("attributeUnits", SpecimenPartAttribute::getAttributeUnits,
				SpecimenPartAttribute::setAttributeUnits), "grow");

		contentPanel.add(new JLabel("Remarks:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField("attributeRemark", SpecimenPartAttribute::getAttributeRemark,
				SpecimenPartAttribute::setAttributeRemark), "grow");

		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> thisDialog.setVisible(false));
		buttonPane.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(e -> {
			bindingContext.writeTo(targetAttribute);
			thisDialog.setVisible(false);
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	private void configureComboBoxValue(String item) {
		comboBoxValue.setEditable(false);
		if ("scientific name".equalsIgnoreCase(item)) {
			comboBoxValue.setEditable(true);
		} else if ("sex".equalsIgnoreCase(item)) {
			comboBoxValue.setModel(new DefaultComboBoxModel<>(Sex.getSexValues()));
		} else if ("life stage".equalsIgnoreCase(item)) {
			comboBoxValue.setModel(new DefaultComboBoxModel<>(LifeStage.getLifeStageValues()));
		} else if ("caste".equalsIgnoreCase(item)) {
			comboBoxValue.setModel(new DefaultComboBoxModel<>(Caste.getCasteValues()));
		}
	}
}
