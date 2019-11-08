/**
 * FilteringGeogJComboBox
 * edu.harvard.mcz.imagecapture.ui
 * <p>
 * Modified from:
 * FilteringJComboBox.java
 * edu.harvard.mcz.precapture.ui
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
 * Author: mole
 */
package edu.harvard.mcz.imagecapture.ui;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.HigherGeographyComboBoxModel;
import edu.harvard.mcz.imagecapture.data.MCZbaseGeogAuthRec;
import edu.harvard.mcz.imagecapture.data.MCZbaseGeogAuthRecLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

/**
 *
 */
public class FilteringGeogJComboBox extends JComboBox<MCZbaseGeogAuthRec> implements FocusListener {
    private static final long serialVersionUID = -7988464282872345110L;
    private static final Log log = LogFactory.getLog(FilteringGeogJComboBox.class);

    private String countryLimit;
    private String stateprovLimit;

    private HigherGeographyComboBoxModel cachedModel;
    private int lastTextLength;

    /**
     * Default no argument constructor, constructs a new FilteringJComboBox instance.
     */
    public FilteringGeogJComboBox() {
        super.setModel(new HigherGeographyComboBoxModel());
        init();
    }

    /**
     * @param valueList
     */
    public FilteringGeogJComboBox(HigherGeographyComboBoxModel model) {
        super(model);
        init();
    }

    public void setHGModel(HigherGeographyComboBoxModel model) {
        super.setModel(model);
    }

    private void init() {
        countryLimit = "";
        stateprovLimit = "";
        cachedModel = null;
        lastTextLength = 0;
        // listen for loss of focus on the text field
        this.getEditor().getEditorComponent().addFocusListener(this);
        this.setEditable(true);
        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent keyEvent) {
                log.debug(keyEvent);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        filter(textfield.getText(), true);
                    }
                });
            }
        });

    }

    public void setHigherGeographyModel(HigherGeographyComboBoxModel model) {
        super.setModel(model);
    }

    public void resetFilter(boolean changePopupState) {
        filter(null, changePopupState);
    }

    protected void filter(String enteredText, boolean changePopupState) {
        if (countryLimit != null && countryLimit.length() == 0) {
            countryLimit = null;
        }
        if (stateprovLimit != null && stateprovLimit.length() == 0) {
            stateprovLimit = null;
        }
        if (enteredText == null || enteredText.length() == 0 || lastTextLength < enteredText.length()) {
            // If entry is blank, show full list.
            MCZbaseGeogAuthRecLifeCycle uls = new MCZbaseGeogAuthRecLifeCycle();
            if (countryLimit == null && stateprovLimit == null) {
                if (cachedModel == null || (enteredText == null || enteredText.length() < lastTextLength)) {
                    cachedModel = new HigherGeographyComboBoxModel(uls.findAll());
                }
                super.setModel(cachedModel);
            } else {
                MCZbaseGeogAuthRec pattern = new MCZbaseGeogAuthRec();
                if (countryLimit != null && countryLimit.length() > 0) {
                    pattern.setCountry(countryLimit);
                } else {
                    if (stateprovLimit != null && stateprovLimit.length() > 0) {
                        pattern.setState_prov(stateprovLimit);
                    }
                }
                if (cachedModel == null || (enteredText == null || enteredText.length() < lastTextLength)) {
                    cachedModel = new HigherGeographyComboBoxModel(uls.findByExample(pattern));
                }
                super.setModel(cachedModel);
            }
        }
        if (!changePopupState) {
            this.firePopupMenuCanceled();
        }
        if (changePopupState && !this.isPopupVisible()) {
            this.showPopup();
        }

        int lengthThreshold = Integer.valueOf(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_FILTER_LENGTH_THRESHOLD));
        if (enteredText != null && enteredText.length() >= lengthThreshold) {

            log.debug("Filtering on " + enteredText);

            boolean isExactMatch = false;
            HigherGeographyComboBoxModel filterArray = new HigherGeographyComboBoxModel();
            filterArray.removeAllElements();
            log.debug("Model size: " + super.getModel().getSize());
            for (int i = 0; i < super.getModel().getSize(); i++) {
                if (((HigherGeographyComboBoxModel) super.getModel())
                        .getDataElementAt(i).toString().toLowerCase()
                        .contains(enteredText.toLowerCase())) {
                    filterArray.addElement((MCZbaseGeogAuthRec) ((HigherGeographyComboBoxModel) super
                            .getModel()).getDataElementAt(i));
                }
                if (((HigherGeographyComboBoxModel) super.getModel())
                        .getDataElementAt(i).toString()
                        .equalsIgnoreCase(enteredText)) {
                    isExactMatch = true;
                    super.getModel().setSelectedItem(
                            ((HigherGeographyComboBoxModel) super.getModel())
                                    .getDataElementAt(i));
                }
            }
            if (filterArray.getSize() > 0) {
                HigherGeographyComboBoxModel model = (HigherGeographyComboBoxModel) this
                        .getModel();
                model.removeAllElements();
                Iterator<MCZbaseGeogAuthRec> i = filterArray.getModel().iterator();
                while (i.hasNext()) {
                    model.addElement(i.next());
                }
                JTextField textfield = (JTextField) this.getEditor()
                        .getEditorComponent();
                textfield.setText(enteredText);
                super.setModel(model);
            }
            log.debug("Filtered Model size: " + super.getModel().getSize());
            if (changePopupState) {
                this.hidePopup();
                if (isExactMatch) {
                    super.firePopupMenuCanceled();
                } else {
                    this.showPopup();
                }
            }
        }
        if (enteredText == null) {
            lastTextLength = 0;
        } else {
            lastTextLength = enteredText.length();
        }
    }

    public void focusGained(FocusEvent e) {
        super.getModel().setSelectedItem("");
        JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.setText("");
    }

    public void focusLost(FocusEvent e) {
        // when focus is lost on the text field (editor box part of the combo box),
        // set the value of the text field to the selected item on the list, if any.
        log.debug(e.toString());
        if (super.getModel().getSelectedItem() != null) {
            log.debug(super.getModel().getSelectedItem().toString());
            //JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
            //textfield.setText(super.getModel().getSelectedItem().toString());
            this.getEditor().setItem(super.getModel().getSelectedItem().toString());
        }
    }

    /**
     * Sets the country filter limit criterion on the picklist.
     *
     * @param selectedItem the family to limit the picklist to.
     */
    public void setCountryLimit(Object selectedCountry) {
        if (selectedCountry != null && selectedCountry.toString().length() > 0) {
            this.countryLimit = selectedCountry.toString();
            this.stateprovLimit = null;
        } else {
            selectedCountry = null;
        }
        lastTextLength = 5000;
        resetFilter(false);
    }

    /**
     * Sets the primary division filter limit criterion on the picklist.
     *
     * @param selectedStateProv the genus to limit the picklist to.
     */
    public void setStateProvLimit(Object selectedStateProv) {
        if (selectedStateProv != null && selectedStateProv.toString().length() > 0) {
            this.stateprovLimit = selectedStateProv.toString();
            this.countryLimit = null;
        } else {
            stateprovLimit = null;
        }
        lastTextLength = 5000;
        resetFilter(false);
    }

}