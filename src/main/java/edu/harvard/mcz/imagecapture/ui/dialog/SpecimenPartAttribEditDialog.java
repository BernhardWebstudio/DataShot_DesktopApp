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

import com.privatejgoodies.forms.layout.ColumnSpec;
import com.privatejgoodies.forms.layout.FormLayout;
import com.privatejgoodies.forms.layout.FormSpecs;
import com.privatejgoodies.forms.layout.RowSpec;
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute;
import edu.harvard.mcz.imagecapture.entity.fixed.Caste;
import edu.harvard.mcz.imagecapture.entity.fixed.LifeStage;
import edu.harvard.mcz.imagecapture.entity.fixed.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SpecimenPartAttribEditDialog extends JDialog {
    private static final long serialVersionUID = -549010965241755136L;

    private static final Logger log =
            LoggerFactory.getLogger(SpecimenPartAttribEditDialog.class);

    private final SpecimenPartAttribEditDialog thisDialog;
    private final SpecimenPartAttribute targetAttribute;
    private final JPanel contentPanel = new JPanel();
    private JButton okButton;
    private JComboBox<String> comboBoxType;
    private JComboBox<String> comboBoxValue;
    private JTextField textFieldUnits;
    private JTextField textFieldRemarks;

    /**
     * Create the dialog.
     */
    public SpecimenPartAttribEditDialog() {
        thisDialog = this;
        targetAttribute = new SpecimenPartAttribute();
        init();
    }

    public SpecimenPartAttribEditDialog(SpecimenPartAttribute attribute) {
        thisDialog = this;
        targetAttribute = attribute;
        init();
        comboBoxType.setSelectedItem(targetAttribute.getAttributeType());
        configureComboBoxValue(targetAttribute.getAttributeType());
        comboBoxValue.setSelectedItem(targetAttribute.getAttributeValue());
        textFieldUnits.setText(targetAttribute.getAttributeUnits());
        textFieldRemarks.setText(targetAttribute.getAttributeRemark());
    }

    private void init() {
        setTitle("Edit Part Attribute");
        setBounds(100, 100, 420, 200);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButton.grabFocus();
                        if (comboBoxType.getSelectedIndex() > -1) {
                            targetAttribute.setAttributeType(
                                    comboBoxType.getSelectedItem().toString());
                        }
                        targetAttribute.setAttributeValue(
                                comboBoxValue.getSelectedItem().toString());

                        targetAttribute.setAttributeUnits(textFieldUnits.getText());
                        targetAttribute.setAttributeRemark(textFieldRemarks.getText());

                        thisDialog.setVisible(false);
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        thisDialog.setVisible(false);
                    }
                });
                buttonPane.add(cancelButton);
            }
        }
        {
            JPanel panel = new JPanel();
            getContentPane().add(panel, BorderLayout.CENTER);
            panel.setLayout(new FormLayout(
                    new ColumnSpec[]{
                            FormSpecs.RELATED_GAP_COLSPEC,
                            FormSpecs.DEFAULT_COLSPEC,
                            FormSpecs.RELATED_GAP_COLSPEC,
                            ColumnSpec.decode("default:grow"),
                    },
                    new RowSpec[]{
                            FormSpecs.RELATED_GAP_ROWSPEC,
                            FormSpecs.DEFAULT_ROWSPEC,
                            FormSpecs.RELATED_GAP_ROWSPEC,
                            FormSpecs.DEFAULT_ROWSPEC,
                            FormSpecs.RELATED_GAP_ROWSPEC,
                            FormSpecs.DEFAULT_ROWSPEC,
                            FormSpecs.RELATED_GAP_ROWSPEC,
                            FormSpecs.DEFAULT_ROWSPEC,
                            FormSpecs.RELATED_GAP_ROWSPEC,
                            FormSpecs.DEFAULT_ROWSPEC,
                    }));
            {
                JLabel lblAttributeType = new JLabel("Attribute Type");
                panel.add(lblAttributeType, "2, 2, right, default");
            }
            {
                comboBoxType = new JComboBox();
                comboBoxType.setModel(new DefaultComboBoxModel(
                        new String[]{"caste", "scientific name", "sex", "life stage"}));
                comboBoxType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String item = comboBoxType.getSelectedItem().toString();
                        if (item != null) {
                            configureComboBoxValue(item);
                        }
                    }
                });
                panel.add(comboBoxType, "4, 2, fill, default");
            }
            {
                JLabel lblValue = new JLabel("Value");
                panel.add(lblValue, "2, 4, right, default");
            }
            {
                comboBoxValue = new JComboBox();
                comboBoxValue.setModel(
                        new DefaultComboBoxModel(Caste.getCasteValues()));

                panel.add(comboBoxValue, "4, 4, fill, default");
            }
            {
                JLabel lblUnits = new JLabel("Units");
                panel.add(lblUnits, "2, 6, right, default");
            }
            {
                textFieldUnits = new JTextField();
                panel.add(textFieldUnits, "4, 6, fill, default");
                textFieldUnits.setColumns(10);
            }
            {
                JLabel lblRemarks = new JLabel("Remarks");
                panel.add(lblRemarks, "2, 8, right, default");
            }
            {
                textFieldRemarks = new JTextField();
                panel.add(textFieldRemarks, "4, 8, fill, default");
                textFieldRemarks.setColumns(10);
            }
        }
    }

    private void configureComboBoxValue(String item) {
        comboBoxValue.setEditable(false);
        if (item.equals("scientific name")) {
            comboBoxValue.setEditable(true);
        }
        if (item.equals("sex")) {
            comboBoxValue.setModel(new DefaultComboBoxModel(Sex.getSexValues()));
        }
        if (item.equals("life stage")) {
            comboBoxValue.setModel(
                    new DefaultComboBoxModel(LifeStage.getLifeStageValues()));
        }
        if (item.equals("caste")) {
            comboBoxValue.setModel(new DefaultComboBoxModel(Caste.getCasteValues()));
        }
    }
}
