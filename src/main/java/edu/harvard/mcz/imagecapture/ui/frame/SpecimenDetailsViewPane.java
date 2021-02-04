/**
 * SpecimenDetailsViewPane.java
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
package edu.harvard.mcz.imagecapture.ui.frame;

import edu.harvard.mcz.imagecapture.*;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.data.LocationInCollection;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.*;
import edu.harvard.mcz.imagecapture.entity.fixed.*;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.*;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
import edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListener;
import edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditor;
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder;
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog;
import edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBox;
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel;
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility;
import net.miginfocom.swing.MigLayout;
import org.hibernate.SessionException;
import org.hibernate.TransactionException;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.OptimisticLockException;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPanel for editing a record of a Specimen in a details view for that
 * specimen. <p>
 * TODO: BugID: 10 add length limits (remaining to do for Number/Collector
 * tables, and for JComboBox fields).
 */
public class SpecimenDetailsViewPane extends JPanel {

    private static final Logger log =
            LoggerFactory.getLogger(SpecimenDetailsViewPane.class);

    private static final long serialVersionUID = 3716072190995030749L;

    private static final int STATE_CLEAN = 0;
    private static final int STATE_DIRTY = 1;
    private final Specimen specimen;     //  @jve:decl-index=0:
    private final JPanel jPanel1 = null; // panel for navigation buttons
    private final StringBuffer higherGeogNotFoundWarning = new StringBuffer();
    KeyboardShortcutManager manager = KeyboardShortcutManager.getInstance();
    private Specimen previousSpecimen = null;
    private SpecimenController specimenController = null;
    private int state; // dirty if data in controls has been changed and not saved
    // to specimen.
    // private JTextField jTextFieldPreparationType = null;
    // allie change
    // private JTextField jTextFieldCountry = null;
    // private JTextField jTextFieldPrimaryDivision = null;
    private FilteringGeogJComboBox comboBoxHigherGeog;
    private JButton dateEmergedButton = null;
    private JButton dateCollectedButton = null;
    private JButton jButtonAddPreparationType;
    private JButton jButtonCollectorAdd = null;
    private JButton jButtonCopy = null;
    private JButton jButtonDeterminations = null;
    private JButton jButtonGeoReference = null;
    private JButton jButtonGetHistory = null;
    private JButton jButtonNext = null;
    private JButton jButtonNumbersAdd = null;
    private JButton jButtonPaste = null;
    private JButton jButtonPrevious = null;
    private JButton jButtonSave = null;
    private JButton jButtonSpecificLocality = null;
    private JCheckBox jCheckBoxValidDistributionFlag = null;
    private JComboBox<String> cbTypeStatus;
    private JComboBox<String> comboBoxElevUnits = null;
    private JComboBox<String> jCBDeterminer = null;
    private JComboBox<String> jComboBoxCollection = null;
    private JComboBox<String> jComboBoxCountry = null;
    private JComboBox<String> jComboBoxFamily = null;
    private JComboBox<String> jComboBoxFeatures = null;
    private JComboBox<String> jComboBoxLifeStage = null;
    private JComboBox<String> jComboBoxLocationInCollection = null;
    private JComboBox<String> jComboBoxNatureOfId;
    private JComboBox<String> jComboBoxPrimaryDivision = null;
    private JComboBox<String> jComboBoxSex = null;
    private JComboBox<String> jComboBoxSubfamily = null;
    private JComboBox<String> jComboBoxWorkflowStatus = null;
    private JLabel jLabelDBId = null;
    private JPanel jPanel = null;
    private JPopupMenu jPopupCollectors;
    private JPopupMenu jPopupNumbers;
    private JPopupMenu jPopupSpecimenParts;
    private JScrollPane jScrollPaneCollectors = null;
    private JScrollPane jScrollPaneNotes = null;
    private JScrollPane jScrollPaneNumbers = null;
    private JScrollPane jScrollPaneSpecimenParts = null;
    private JTableWithRowBorder jTableCollectors = null;
    private JTableWithRowBorder jTableNumbers = null;
    private JTableWithRowBorder jTableSpecimenParts = null;
    private JTextArea jTextAreaSpecimenNotes = null;
    private JTextField jTextFieldAssociatedTaxon = null;
    private JTextField jTextFieldAuthorship = null;
    private JTextField jTextFieldBarcode = null;
    private JTextField jTextFieldCitedInPub = null;
    private JTextField jTextFieldCollectingMethod = null;
    private JTextField jTextFieldCreator = null;
    private JTextField jTextFieldDateCollected = null;
    private JTextField jTextFieldDateCollectedIndicator = null;
    private JTextField jTextFieldDateCreated = null;
    private JTextField jTextFieldDateDetermined;
    private JTextField jTextFieldDateEmerged = null;
    private JTextField jTextFieldDateEmergedIndicator = null;
    private JTextField jTextFieldDateLastUpdated = null;
    private JTextField jTextFieldDateNos = null;
    private JTextField jTextFieldDrawerNumber = null;
    private JTextField jTextFieldGenus = null;
    private JTextField jTextFieldHabitat = null;
    private JTextField jTextFieldISODate = null;
    private JTextField jTextFieldIdRemarks;
    private JTextField jTextFieldImageCount = null;
    private JTextField jTextFieldInferences = null;
    private JTextField jTextFieldInfraspecificEpithet = null;
    private JTextField jTextFieldInfraspecificRank = null;
    private JTextField jTextFieldLastUpdatedBy = null;
    private JTextField jTextFieldLocality = null;
    private JTextField jTextFieldMigrationStatus = null;
    private JTextField jTextFieldMinElevation = null;
    private JTextField jTextFieldQuestions = null;
    private JTextField jTextFieldSpecies = null;
    private JTextField jTextFieldStatus = null;
    private JTextField jTextFieldSubspecies = null;
    private JTextField jTextFieldTribe = null;
    private JTextField jTextFieldUnnamedForm = null;
    private JTextField jTextFieldVerbatimLocality = null;
    private JTextField textFieldMaxElev = null;
    private JTextField textFieldMicrohabitat = null;
    private SpecimenDetailsViewPane thisPane = null;
    private int clickedOnCollsRow;
    private int clickedOnNumsRow;
    private int clickedOnPartsRow;

    /**
     * Construct an instance of a SpecimenDetailsViewPane showing the data present
     * in aSpecimenInstance.
     *
     * @param aSpecimenInstance the Specimen instance to display for editing.
     */
    public SpecimenDetailsViewPane(Specimen aSpecimenInstance,
                                   SpecimenController aController) {
        specimen = aSpecimenInstance;
        SpecimenLifeCycle s = new SpecimenLifeCycle();
        setStateToClean();
        //		SpecimenPartAttributeLifeCycle spals = new
        // SpecimenPartAttributeLifeCycle(); 		Iterator<SpecimenPart> i
        // = specimen.getSpecimenParts().iterator(); 		while
        // (i.hasNext()) { 			Iterator<SpecimenPartAttribute> ia =
        // i.next().getAttributeCollection().iterator(); 			while
        // (ia.hasNext())
        // { try { 					SpecimenPartAttribute spa =
        // ia.next();
        //					log.debug("Debug",
        // spa.getSpecimenPartAttributeId());
        // spals.attachDirty(spa); 					log.debug("Debug",
        // spa.getSpecimenPartAttributeId()); 				} catch (SaveFailedException e)
        // {
        //					// TODO Auto-generated catch block
        //					e.printStackTrace();
        //				}
        //			}
        //		}
        try {
            s.attachClean(specimen);
            specimenController = aController;
            initialize();
            setValues();
        } catch (Exception e) {
            String status =
                    "Undefined error initializing SpecimenDetails. Restarting Database connection...";
            if (e instanceof SessionException || e instanceof TransactionException) {
                status =
                        "Database Connection Error. Resetting connection... Try again.";
            } else if (e instanceof IllegalStateException) {
                status = "Illegal state exception. Last edit possibly lost. Try again.";
            } else if (e instanceof OptimisticLockException) {
                status =
                        "Error: last edited entry has been modified externally. Try again.";
            }
            Singleton.getSingletonInstance().getMainFrame().setStatusMessage(status);
            log.debug(e.getMessage(), e);
            HibernateUtil.restartSessionFactory();
            this.setVisible(false);
        }
    }

    /**
     * Initializes the specimen details view pane.
     * Note, contains comments indicating how to enable visual designer with this
     * class.
     */
    private void initialize() {
        thisPane = this;
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(0);
        borderLayout.setVgap(0);
        this.setLayout(borderLayout);
        this.add(getJTextFieldStatus(), BorderLayout.SOUTH);

        JScrollPane scrollPane =
                new JScrollPane(getJPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(100, 100));

        // add keyboard listeners
        // with all the shortcuts
        registerShortcut("specimen.save", "ctrl S", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                save();
            }
        });
        registerShortcut("specimen.next", "ctrl RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gotoNextSpecimen();
            }
        });
        registerShortcut("specimen.previous", "ctrl LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gotoPreviousSpecimen();
            }
        });
        registerShortcut("specimen.copyThis", "ctrl alt C", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ImageCaptureApp.lastEditedSpecimenCache = thisPane.specimen;
                thisPane.setStatus("Copied specimen with id " +
                        thisPane.specimen.getSpecimenId());
            }
        });
        registerShortcut("specimen.paste", "ctrl alt V", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache;
                pastePreviousRecord();
            }
        });
        if (specimen.isExported() == null || specimen.isExported()) {
            JOptionPane.showMessageDialog(
                    thisPane, "This Specimen is already exported. No edit will be saved.",
                    "Warning: not saveable", JOptionPane.WARNING_MESSAGE);
        }
    }

    void registerShortcut(String name, String defaultStroke, Action action) {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();
        inputMap.put(manager.getShortcut(name, defaultStroke), name);
        actionMap.put(name, action);
        log.debug("Registered shortcut: " + name + " as " +
                manager.getShortcut(name, defaultStroke));
    }

    public void setWarning(String warning) {
        jTextFieldStatus.setText(warning);
        jTextFieldStatus.setForeground(Color.RED);
    }

    private void setWarnings() {
        log.debug("In set warnings");
        if (specimen.getICImages() != null) {
            log.debug("specimen.getICImages is not null");
            java.util.Iterator<ICImage> i = specimen.getICImages().iterator();
            log.debug("Debug {}", i.hasNext());
            while (i.hasNext()) {
                log.debug("Checking image " + i);
                ICImage im = i.next();
                String rbc = "";
                if (im.getRawBarcode() != null) {
                    rbc = im.getRawBarcode();
                }
                String ebc = "";
                if (im.getRawExifBarcode() != null) {
                    ebc = im.getRawExifBarcode();
                }
                if (!rbc.equals(ebc)) {
                    // warn of mismatch, but only if configured to expect both to be
                    // present.
                    if (Singleton.getSingletonInstance()
                            .getProperties()
                            .getProperties()
                            .getProperty(
                                    ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE)
                            .equals("true")) {
                        this.setWarning(
                                "Warning: An image has mismatch between Comment and Barcode.");
                        log.debug(
                                "Setting: Warning: Image has mismatch between Comment and Barcode.");
                    }
                }
            }
        }
        if (higherGeogNotFoundWarning != null &&
                higherGeogNotFoundWarning.length() > 0) {
            this.setWarning(higherGeogNotFoundWarning.toString());
        }
    }

    public void setStatus(String status) {
        log.info("Setting status to: ".concat(status));
        jTextFieldStatus.setText(status);
        jTextFieldStatus.setForeground(Color.BLACK);
    }

    private boolean save() {
        if (specimen.isExported()) {
            JOptionPane.showMessageDialog(
                    thisPane, "This Specimen is already exported. No edit will be saved.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            thisPane.getParent().setCursor(
                    Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } catch (Exception ex) {
            log.error("Error", ex);
        }
        try {
            this.setStatus("Saving");
            if (jTableCollectors.isEditing()) {
                jTableCollectors.getCellEditor().stopCellEditing();
            }
            if (jTableSpecimenParts.isEditing()) {
                jTableSpecimenParts.getCellEditor().stopCellEditing();
            }
            if (jTableNumbers.isEditing()) {
                jTableNumbers.getCellEditor().stopCellEditing();
            }

            if (cbTypeStatus.getSelectedIndex() == -1 &&
                    cbTypeStatus.getSelectedItem() == null) {
                specimen.setTypeStatus(Specimen.STATUS_NOT_A_TYPE);
            } else {
                specimen.setTypeStatus((String) cbTypeStatus.getSelectedItem());
            }
            specimen.setMicrohabitat(textFieldMicrohabitat.getText());

            if (jComboBoxLocationInCollection.getSelectedItem() != null) {
                specimen.setLocationInCollection(
                        jComboBoxLocationInCollection.getSelectedItem().toString());
            }

            specimen.setDrawerNumber(jTextFieldDrawerNumber.getText());
            if (jComboBoxFamily.getSelectedIndex() == -1 &&
                    jComboBoxFamily.getSelectedItem() == null) {
                specimen.setFamily("");
            } else {
                specimen.setFamily(jComboBoxFamily.getSelectedItem().toString());
            }
            if (jComboBoxSubfamily.getSelectedIndex() == -1 &&
                    jComboBoxSubfamily.getSelectedItem() == null) {
                specimen.setSubfamily("");
            } else {
                specimen.setSubfamily(jComboBoxSubfamily.getSelectedItem().toString());
            }
            specimen.setTribe(jTextFieldTribe.getText());
            specimen.setGenus(jTextFieldGenus.getText());
            specimen.setSpecificEpithet(jTextFieldSpecies.getText());
            specimen.setSubspecificEpithet(jTextFieldSubspecies.getText());
            specimen.setInfraspecificEpithet(
                    jTextFieldInfraspecificEpithet.getText());
            specimen.setInfraspecificRank(jTextFieldInfraspecificRank.getText());
            specimen.setAuthorship(jTextFieldAuthorship.getText());
            // TODO: handle the collectors set!

            // this returns TRUE for the copied item!!
            log.debug("in save(). specimen numbers size: " +
                    specimen.getNumbers().size());
            log.debug("okok in save(), specimenid is " + specimen.getSpecimenId());

            if (previousSpecimen != null && previousSpecimen.getNumbers() != null) {
                log.debug("in save(). prev specimen numbers size: " +
                        previousSpecimen.getNumbers().size());
                // specimen.setNumbers(previousSpecimen.getNumbers()); - this gives
                // hibernate exceptions here too!
                log.debug("okok in save(), prev specimenid is " +
                        previousSpecimen.getSpecimenId());
            }

            specimen.setIdentifiedBy((String) jCBDeterminer.getSelectedItem());

            specimen.setDateIdentified(jTextFieldDateDetermined.getText());
            specimen.setIdentificationRemarks(jTextFieldIdRemarks.getText());
            if (jComboBoxNatureOfId.getSelectedIndex() == -1 &&
                    jComboBoxNatureOfId.getSelectedItem() == null) {
                // specimen.setNatureOfId(NatureOfId.LEGACY);
                specimen.setNatureOfId(NatureOfId.EXPERT_ID);
            } else {
                specimen.setNatureOfId((String) jComboBoxNatureOfId.getSelectedItem());
            }

            specimen.setUnNamedForm(jTextFieldUnnamedForm.getText());
            specimen.setVerbatimLocality(jTextFieldVerbatimLocality.getText());
            specimen.setCountry((String) jComboBoxCountry.getSelectedItem());
            specimen.setValidDistributionFlag(
                    jCheckBoxValidDistributionFlag.isSelected());
            specimen.setPrimaryDivison(
                    (String) jComboBoxPrimaryDivision.getSelectedItem());
            specimen.setSpecificLocality(jTextFieldLocality.getText());

            // Elevations
            Long min_elev;
            if (jTextFieldMinElevation.getText().trim().length() == 0) {
                min_elev = null;
            } else {
                try {
                    min_elev = Long.parseLong(jTextFieldMinElevation.getText());
                } catch (NumberFormatException e) {
                    min_elev = null;
                }
            }
            specimen.setMinimum_elevation(min_elev);
            Long max_elev;
            if (textFieldMaxElev.getText().trim().length() == 0) {
                max_elev = null;
            } else {
                try {
                    max_elev = Long.parseLong(textFieldMaxElev.getText());
                } catch (NumberFormatException e) {
                    max_elev = null;
                }
            }
            specimen.setMaximum_elevation(max_elev);
            if (this.comboBoxElevUnits.getSelectedIndex() == -1 &&
                    comboBoxElevUnits.getSelectedItem() == null) {
                specimen.setElev_units("");
            } else {
                specimen.setElev_units(comboBoxElevUnits.getSelectedItem().toString());
            }

            specimen.setCollectingMethod(jTextFieldCollectingMethod.getText());
            specimen.setIsoDate(jTextFieldISODate.getText());
            specimen.setDateNos(jTextFieldDateNos.getText());
            specimen.setDateCollected(jTextFieldDateCollected.getText());
            specimen.setDateEmerged(jTextFieldDateEmerged.getText());
            specimen.setDateCollectedIndicator(
                    jTextFieldDateCollectedIndicator.getText());
            specimen.setDateEmergedIndicator(
                    jTextFieldDateEmergedIndicator.getText());
            if (jComboBoxCollection.getSelectedIndex() == -1 &&
                    jComboBoxCollection.getSelectedItem() == null) {
                specimen.setCollection("");
            } else {
                specimen.setCollection(
                        jComboBoxCollection.getSelectedItem().toString());
            }
            if (jComboBoxFeatures.getSelectedIndex() == -1 &&
                    jComboBoxFeatures.getSelectedItem() == null) {
                specimen.setFeatures("");
            } else {
                specimen.setFeatures(jComboBoxFeatures.getSelectedItem().toString());
            }
            if (jComboBoxLifeStage.getSelectedIndex() == -1 &&
                    jComboBoxLifeStage.getSelectedItem() == null) {
                specimen.setLifeStage("");
            } else {
                specimen.setLifeStage(jComboBoxLifeStage.getSelectedItem().toString());
            }
            if (jComboBoxSex.getSelectedIndex() == -1 &&
                    jComboBoxSex.getSelectedItem() == null) {
                specimen.setSex("");
            } else {
                specimen.setSex(jComboBoxSex.getSelectedItem().toString());
                log.debug("jComboBoxSex selectedIndex=" +
                        jComboBoxSex.getSelectedIndex());
            }

            log.debug("sex=" + specimen.getSex());

            specimen.setCitedInPublication(jTextFieldCitedInPub.getText());
            // specimen.setPreparationType(jTextFieldPreparationType.getText());
            specimen.setAssociatedTaxon(jTextFieldAssociatedTaxon.getText());
            specimen.setHabitat(jTextFieldHabitat.getText());
            specimen.setMicrohabitat(textFieldMicrohabitat.getText());
            specimen.setSpecimenNotes(jTextAreaSpecimenNotes.getText());
            specimen.setInferences(jTextFieldInferences.getText());
            specimen.setLastUpdatedBy(
                    Singleton.getSingletonInstance().getUserFullName());
            specimen.setDateLastUpdated(new Date());
            specimen.setWorkFlowStatus(
                    jComboBoxWorkflowStatus.getSelectedItem().toString());

            specimen.setQuestions(jTextFieldQuestions.getText());
            try {
                // make sure specimen controller does not throw null pointer exception –
                // whyever
                if (specimenController.getSpecimen() == null) {
                    specimenController.setSpecimen(specimen);
                }
                specimenController.save(); // save the record
                setStateToClean();         // enable the navigation buttons
                this.setStatus("Saved");   // inform the user
                jTextFieldStatus.setForeground(Color.BLACK);
                setWarnings();
                jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
                jTextFieldDateLastUpdated.setText(
                        specimen.getDateLastUpdated().toString());
            } catch (SaveFailedException e) {
                setStateToDirty(); // disable the navigation buttons
                this.setWarning("Error: " + e.getMessage());
                return false;
            }
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            Singleton.getSingletonInstance().getMainFrame().setCount(
                    sls.findSpecimenCount());
        } catch (OptimisticLockException e) {
            // Oh, well. Issues with foreign keys already deleting items, which are
            // not found afterwards. We catch these here and silence them. TODO:
            // resolve by changing database structure We might also catch unwanted
            // ones; böh, too bad – alert the user just in case.
            log.error("Error", e);
            this.setWarning("Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // trap any exception and notify the user
            setStateToDirty(); // disable the navigation buttons
            this.setWarning("Error: " + e.getMessage());
            log.error("Error", e);
            throw e;
//      return false;
        }
        updateContentDependentLabels();

        try {
            thisPane.getParent().setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (Exception ex) {
            log.error("Error", ex);
        }
        return true;
    }

    /**
     * Set the fields values to the ones of the previous specimen
     */
    private void pastePreviousRecord() {
        log.debug("calling pastePreviousRecord()");
        // thisPane.setStateToDirty();
        jTextFieldDateDetermined.setText(previousSpecimen.getDateIdentified());
        jCBDeterminer.setSelectedItem(previousSpecimen.getIdentifiedBy());
        jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality());
        jComboBoxCountry.setSelectedItem(previousSpecimen.getCountry());
        jComboBoxPrimaryDivision.setSelectedItem(
                previousSpecimen.getPrimaryDivison());
        // Elevations
        try {
            jTextFieldMinElevation.setText(
                    Long.toString(previousSpecimen.getMinimum_elevation()));
        } catch (Exception e) {
            jTextFieldMinElevation.setText("");
        }
        try {
            textFieldMaxElev.setText(
                    Long.toString(previousSpecimen.getMaximum_elevation()));
        } catch (Exception e) {
            textFieldMaxElev.setText("");
        }
        if (previousSpecimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(previousSpecimen.getElev_units());
        } else {
            comboBoxElevUnits.setSelectedItem("");
        }
        jTextFieldLocality.setText(previousSpecimen.getSpecificLocality());
        jComboBoxCollection.setSelectedItem(previousSpecimen.getCollection());
        jTextFieldDateNos.setText(previousSpecimen.getDateNos());
        jTextFieldISODate.setText(previousSpecimen.getIsoDate());
        jTextFieldDateEmerged.setText(previousSpecimen.getDateEmerged());
        jTextFieldDateCollectedIndicator.setText(
                previousSpecimen.getDateCollectedIndicator());
        jTextFieldDateEmergedIndicator.setText(
                previousSpecimen.getDateEmergedIndicator());
        jTextFieldDateCollected.setText(previousSpecimen.getDateCollected());
        jComboBoxLifeStage.setSelectedItem(previousSpecimen.getLifeStage());
        jComboBoxSex.setSelectedItem(previousSpecimen.getSex());
        jTextFieldAssociatedTaxon.setText(previousSpecimen.getAssociatedTaxon());
        jTextFieldHabitat.setText(previousSpecimen.getHabitat());
        textFieldMicrohabitat.setText(previousSpecimen.getMicrohabitat());
        jTextAreaSpecimenNotes.setText(previousSpecimen.getSpecimenNotes());
        jTextFieldInferences.setText(previousSpecimen.getInferences());

        //+numbers
        specimen.getNumbers().clear();
        for (Number number : previousSpecimen.getNumbers()) {
            // specimen.getNumbers().add((Number.class)iter.next());
            Number n = (Number) number.clone();
            n.setSpecimen(specimen);
            specimen.getNumbers().add(n);
        }
        jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));
        this.setupNumberJTableRenderer();

        //+ verify the georeference data (we do want it all copied)

        //+ preparation type (the whole table!) = specimen parts
        // trying not to remove & add one of same name to counter sql constraints
        Set<SpecimenPart> followingParts = previousSpecimen.getSpecimenParts();
        Set<SpecimenPart> newParts =
                followingParts.stream()
                        .filter(part -> !specimen.getSpecimenParts().contains(part))
                        .collect(Collectors.toSet());
        log.debug("Collectors: {}, {}, {}", followingParts, newParts,
                specimen.getSpecimenParts());
        specimen.getSpecimenParts().removeIf(
                part -> !followingParts.contains(part));
        for (SpecimenPart specimenPart : newParts) {
            SpecimenPart part = (SpecimenPart) specimenPart.clone();
            part.setSpecimen(specimen);
            specimen.getSpecimenParts().add(part);
        }
        jTableSpecimenParts.setModel(
                new SpecimenPartsTableModel(specimen.getSpecimenParts()));
        this.setupSpecimenPartsJTableRenderer();

        //+collectors
        // trying not to remove & add one of same name to counter sql constraints
        Set<Collector> followingCollectors = previousSpecimen.getCollectors();
        // the collectors to be added:
        Set<Collector> newCollectors =
                followingCollectors.stream()
                        .filter(collector -> !specimen.getCollectors().contains(collector))
                        .collect(Collectors.toSet());
        // the collectors to be removed:
        specimen.getCollectors().removeIf(
                collector -> !followingCollectors.contains(collector));
        for (Collector collector : newCollectors) {
            Collector c = (Collector) collector.clone();
            c.setSpecimen(specimen);
            specimen.getCollectors().add(c);
        }
        jTableCollectors.setModel(
                new CollectorTableModel(specimen.getCollectors()));
        this.setupCollectorJTableRenderer();

        //+determinations
        specimen.getDeterminations().clear();
        for (Determination prevdet : previousSpecimen.getDeterminations()) {
            Determination newdet = prevdet.clone();
            newdet.setSpecimen(specimen);
            specimen.getDeterminations().add(newdet);
        }

        //+georeference
        specimen.getLatLong().clear();
        // prepare hash set as otherwise, in getLatLong(), an empty LatLong is
        // returned
        HashSet<LatLong> latLongs = new HashSet<>();
        for (LatLong prevgeo : previousSpecimen.getLatLong()) {
            LatLong newgeo = prevgeo.clone();
            log.debug("Got newgeo with lat " + newgeo.getDecLat());
            newgeo.setSpecimen(specimen);
            latLongs.add(newgeo);
        }
        specimen.setLatLong(latLongs);

        // new - verbatim locality
        jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality());
        // new - publications
        jTextFieldCitedInPub.setText(previousSpecimen.getCitedInPublication());
        // new - features
        jComboBoxFeatures.setSelectedItem(previousSpecimen.getFeatures());
        // new - collecting method
        jTextFieldCollectingMethod.setText(previousSpecimen.getCollectingMethod());

        updateContentDependentLabels();
        thisPane.setStatus("Pasted specimen with id " +
                thisPane.previousSpecimen.getSpecimenId());
    }

    /**
     * Set the values of the fields to the ones of the specimen
     * TODO: refactor to unused: move to instantiation of fields, e.g.
     */
    private void setValues() {
        log.debug("okok setting values, specimenid is " + specimen.getSpecimenId());
        this.setStatus("Setting values");
        jTextFieldBarcode.setText(specimen.getBarcode());

        // alliefix - set to value from properties
        // jComboBoxLocationInCollection.setSelectedItem(specimen.getLocationInCollection());
        String locationInCollectionPropertiesVal =
                Singleton.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.KEY_DISPLAY_COLLECTION);
        jComboBoxLocationInCollection.setSelectedItem(
                locationInCollectionPropertiesVal);

        // allie try
    /*Set<LatLong> georeferences = specimen.getLatLong();
    log.debug("setvalues: georeferences size is : + " + georeferences.size());
    LatLong georeference_pre = georeferences.iterator().next();
    log.debug("lat is : + " + georeference_pre.getLatDegString());
    log.debug("long is : + " + georeference_pre.getLongDegString());*/

        cbTypeStatus.setSelectedItem(specimen.getTypeStatus());
        jTextFieldDrawerNumber.setText(specimen.getDrawerNumber());
        jComboBoxFamily.setSelectedItem(specimen.getFamily());
        jComboBoxSubfamily.setSelectedItem(specimen.getSubfamily());
        jTextFieldTribe.setText(specimen.getTribe());
        jTextFieldGenus.setText(specimen.getGenus());
        jTextFieldSpecies.setText(specimen.getSpecificEpithet());
        jTextFieldSubspecies.setText(specimen.getSubspecificEpithet());
        jTextFieldInfraspecificEpithet.setText(specimen.getInfraspecificEpithet());
        jTextFieldInfraspecificRank.setText(specimen.getInfraspecificRank());
        jTextFieldAuthorship.setText(specimen.getAuthorship());

        // allie new - bugfix
        textFieldMicrohabitat.setText(specimen.getMicrohabitat());

        jTextFieldIdRemarks.setText(specimen.getIdentificationRemarks());
        jTextFieldDateDetermined.setText(specimen.getDateIdentified());

        // allie change
        // log.debug("jComboBoxLifeStage here!!! specimen life stage is " +
        // specimen.getLifeStage());
        if (specimen.getLifeStage() == null || specimen.getLifeStage().equals("")) {
            specimen.setLifeStage("adult");
            jComboBoxLifeStage.setSelectedIndex(0);
        }

        // allie change - removed this
        // MCZbaseAuthAgentName selection = new MCZbaseAuthAgentName();
        // selection.setAgent_name(specimen.getIdentifiedBy());
        //((AgentNameComboBoxModel)jCBDeterminer.getModel()).setSelectedItem(selection);
        // jCBDeterminer.getEditor().setItem(jCBDeterminer.getModel().getSelectedItem());

        // allie change - added this
        // jCBDeterminer.setText(specimen.getIdentifiedBy());
        jCBDeterminer.setSelectedItem(specimen.getIdentifiedBy());

        jComboBoxNatureOfId.setSelectedItem(specimen.getNatureOfId());

        jTextFieldUnnamedForm.setText(specimen.getUnNamedForm());
        jTextFieldVerbatimLocality.setText(specimen.getVerbatimLocality());
        // Specimen record contains a string, delegate handling of lookup of object
        // to the combo box model.
        // allieremove
        // 		log.debug("Debug {}", specimen.getHigherGeography());
        // 		((HigherGeographyComboBoxModel)comboBoxHigherGeog.getModel()).setSelectedItem(specimen.getHigherGeography());
        // //TODO ? set model not notifying listeners?
        // 		higherGeogNotFoundWarning = new StringBuffer();
        // 		comboBoxHigherGeog.getEditor().setItem(comboBoxHigherGeog.getModel().getSelectedItem());
        // 		if (specimen.getHigherGeography()==null) {
        // 			comboBoxHigherGeog.setBackground(Color.YELLOW);
        // 		} else {
        // 			if
        // (comboBoxHigherGeog.getModel().getSelectedItem()==null) {
        // comboBoxHigherGeog.setBackground(Color.RED);
        // 				higherGeogNotFoundWarning.append("Higher Geog:
        // [").append(specimen.getHigherGeography()).append("] not found. Fix before
        // saving.");
        // 			}
        // 		}
        // 		jTextFieldCountry.setText(specimen.getCountry());
        jComboBoxCountry.setSelectedItem(specimen.getCountry());
        if (specimen.getValidDistributionFlag() != null) {
            jCheckBoxValidDistributionFlag.setSelected(
                    specimen.getValidDistributionFlag());
        } else {
            jCheckBoxValidDistributionFlag.setSelected(false);
        }
        jComboBoxPrimaryDivision.setSelectedItem(specimen.getPrimaryDivison());
        jTextFieldLocality.setText(specimen.getSpecificLocality());

        // Elevations
        // **********************************************************************
        try {
            jTextFieldMinElevation.setText(
                    Long.toString(specimen.getMinimum_elevation()));
        } catch (Exception e) {
            jTextFieldMinElevation.setText("");
        }
        try {
            textFieldMaxElev.setText(Long.toString(specimen.getMaximum_elevation()));
        } catch (Exception e) {
            textFieldMaxElev.setText("");
        }
        if (specimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(specimen.getElev_units());
        } else {
            comboBoxElevUnits.setSelectedItem("");
        }

        jTextFieldCollectingMethod.setText(specimen.getCollectingMethod());
        jTextFieldISODate.setText(specimen.getIsoDate());
        jTextFieldDateNos.setText(specimen.getDateNos());
        jTextFieldDateCollected.setText(specimen.getDateCollected());
        jTextFieldDateEmerged.setText(specimen.getDateEmerged());
        jTextFieldDateCollectedIndicator.setText(
                specimen.getDateCollectedIndicator());
        jTextFieldDateEmergedIndicator.setText(specimen.getDateEmergedIndicator());
        jComboBoxCollection.setSelectedItem(specimen.getCollection());
        // jTextFieldPreparationType.setText(specimen.getPreparationType());
        jTextFieldAssociatedTaxon.setText(specimen.getAssociatedTaxon());
        jTextFieldHabitat.setText(specimen.getHabitat());
        textFieldMicrohabitat.setText(specimen.getMicrohabitat());
        jTextAreaSpecimenNotes.setText(specimen.getSpecimenNotes());
        jComboBoxFeatures.setSelectedItem(specimen.getFeatures());
        jComboBoxLifeStage.setSelectedItem(specimen.getLifeStage());
        jComboBoxSex.setSelectedItem(specimen.getSex());
        jTextFieldCitedInPub.setText(specimen.getCitedInPublication());
        jTextFieldQuestions.setText(specimen.getQuestions());
        jComboBoxWorkflowStatus.setSelectedItem(specimen.getWorkFlowStatus());
        if (specimen.isStateDone()) {
            jTextFieldMigrationStatus.setText(
                    "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" +
                            specimen.getCatNum());
        } else {
            jTextFieldMigrationStatus.setText("");
        }
        jTextFieldInferences.setText(specimen.getInferences());
        jTextFieldCreator.setText(specimen.getCreatedBy());
        if (specimen.getDateCreated() != null) {
            jTextFieldDateCreated.setText(specimen.getDateCreated().toString());
        }
        jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
        if (specimen.getDateLastUpdated() != null) {
            jTextFieldDateLastUpdated.setText(
                    specimen.getDateLastUpdated().toString());
        }

        // allie change
        if (specimen.getNatureOfId() == null ||
                specimen.getNatureOfId().equals("")) {
            specimen.setLifeStage("expert ID");
            jComboBoxNatureOfId.setSelectedIndex(0);
        }

        // without this, it does save the 1st record, and it does not copy the next
        // record!
        log.debug(
                "setValues calling jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));");
        jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));
        this.setupNumberJTableRenderer();

        jTableCollectors.setModel(
                new CollectorTableModel(specimen.getCollectors()));
        this.setupCollectorJTableRenderer();

        jTableSpecimenParts.setModel(
                new SpecimenPartsTableModel(specimen.getSpecimenParts()));
        setupSpecimenPartsJTableRenderer();

        updateContentDependentLabels();

        setWarnings();
        this.setStateToClean();
        this.setStatus("Loaded");
    }

    private void updateDeterminationCount() {
        if (specimen.getDeterminations() == null) {
            this.setDeterminationCount(0);
        } else {
            this.setDeterminationCount(specimen.getDeterminations().size());
        }
    }

    private void setDeterminationCount(int count) {
        String detSuffix = count == 1 ? "s" : "";
        jButtonDeterminations.setText(count + " Det" + detSuffix + ".");
        jButtonDeterminations.updateUI();
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldStatus() {
        if (jTextFieldStatus == null) {
            jTextFieldStatus = new JTextField("Status");
            jTextFieldStatus.setEditable(false);
            jTextFieldStatus.setEnabled(true);
        }
        return jTextFieldStatus;
    }

    /**
     * This method initializes jPanel, laying out the UI components.
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel(new MigLayout("wrap 4, fillx")); // 4 col layout
            // section: top information
            this.addBasicJLabel(jPanel, "Barcode");
            jPanel.add(this.getBarcodeJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Collection");
            jPanel.add(this.getLocationInCollectionJComboBox());
            // row
            this.addBasicJLabel(jPanel, "Number of Images");
            jPanel.add(this.getJTextFieldImgCount(), "grow");
            this.addBasicJLabel(jPanel, "Migration Status");
            jPanel.add(this.getJTextFieldMigrationStatus(), "grow");
            // section: family, classification
            // row
            this.addBasicJLabel(jPanel, "Family");
            jPanel.add(this.getFamilyJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Subfamily");
            jPanel.add(this.getJTextFieldSubfamily(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Genus");
            jPanel.add(this.getGenusJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Species");
            jPanel.add(this.getSpecificEpithetJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Subspecies");
            jPanel.add(this.getSubspecifcEpithetJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Tribe");
            jPanel.add(this.getJTextFieldTribe(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Infrasubspecific Name");
            jPanel.add(this.getJTextFieldInfraspecificName(), "grow");
            this.addBasicJLabel(jPanel, "Infrasubspecific Rank");
            jPanel.add(this.getJTextFieldInfraspecificRank(), "grow");
            // section: identification/determination
            // row
            this.addBasicJLabel(jPanel, "ID by");
            jPanel.add(this.getJCBDeterminer());
            this.addBasicJLabel(jPanel, "Nature of ID");
            jPanel.add(this.getJComboBoxNatureOfId());
            // row
            this.addBasicJLabel(jPanel, "ID Date");
            jPanel.add(this.getJTextFieldDateDetermined(), "grow");
            jPanel.add(this.getDetsJButton());
            jPanel.add(this.getDBIdLabel());
            // row
            this.addBasicJLabel(jPanel, "ID Remark");
            jPanel.add(this.getJTextFieldIdRemarks(), "grow, span 3");
            // row
            this.addBasicJLabel(jPanel, "Author");
            jPanel.add(this.getJTextFieldAuthorship(), "grow");
            this.addBasicJLabel(jPanel, "TypeStatus");
            jPanel.add(this.getCbTypeStatus());
            // section: locale
            // row
            this.addBasicJLabel(jPanel, "Verbatim Locality");
            jPanel.add(this.getVerbatimLocalityJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Country");
            jPanel.add(this.getCountryJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "State/Province");
            jPanel.add(this.getPrimaryDivisionJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Valid Dist.");
            jPanel.add(this.getValidDistributionJCheckBox());
            // row
            jPanel.add(this.getJButtonSpecificLocality(), "align label");
            jPanel.add(this.getSpecificLocalityJTextField(), "grow, span 2");
            jPanel.add(this.getJButtonGeoreference());
            // row
            jPanel.add(new JLabel("Elevation"), "span, split 6, sizegroup elevation");
            jPanel.add(new JLabel("from:"),
                    "align label, sizegroup elevation, right");
            jPanel.add(this.getVerbatimElevationJTextField(),
                    "grow, sizegroup elevation");
            jPanel.add(new JLabel("to:"), "align label, sizegroup elevation, right");
            jPanel.add(this.getTextFieldMaxElev(), "grow, sizegroup elevation");
            jPanel.add(this.getComboBoxElevUnits(), "wrap, sizegroup elevation");
            // section: collection
            // row
            this.addBasicJLabel(jPanel, "Collection");
            jPanel.add(this.getJTextFieldCollection(), "grow, span 3");
            // double row:
            this.addBasicJLabel(jPanel, "Collectors");
            jPanel.add(this.getJScrollPaneCollectors(), "span 2 2, grow");
            this.addBasicJLabel(jPanel, "Collecting Method");
            jPanel.add(this.getJButtonCollectorAdd());
            jPanel.add(this.getJTextFieldCollectingMethod(), "growx, top");
            // row
            this.addBasicJLabel(jPanel, "Verbatim date");
            jPanel.add(this.getJTextFieldVerbatimDate(), "grow");
            this.addBasicJLabel(jPanel, "yyyy/mm/dd");
            jPanel.add(this.getJTextFieldISODate(), "grow");
            // row
            jPanel.add(this.getDateEmergedJButton(), "align label");
            jPanel.add(this.getJTextFieldDateEmerged(), "grow");
            this.addBasicJLabel(jPanel, "Date emerged note");
            jPanel.add(this.getJTextFieldDateEmergedIndicator(), "grow");
            // row
            jPanel.add(this.getDateCollectedJButton(), "align label");
            jPanel.add(this.getJTextFieldDateCollected(), "grow");
            this.addBasicJLabel(jPanel, "Date collected note");
            jPanel.add(this.getJTextFieldDateCollectedIndicator(), "grow");
            // section: pictured specifics
            // row
            this.addBasicJLabel(jPanel, "Habitat");
            jPanel.add(this.getJTextFieldHabitat(), "grow");
            this.addBasicJLabel(jPanel, "Microhabitat");
            jPanel.add(this.getTextFieldMicrohabitat(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Life Stage");
            jPanel.add(this.getJComboBoxLifeStage(), "grow");
            this.addBasicJLabel(jPanel, "Sex");
            jPanel.add(this.getJComboBoxSex(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Features");
            jPanel.add(this.getJComboBoxFeatures(), "wrap");
            // double row:
            this.addBasicJLabel(jPanel, "Specimen Parts");
            jPanel.add(this.getJScrollPaneSpecimenParts(), "span 3 2, grow");
            jPanel.add(this.getJButtonAddPrep());
            // row
            this.addBasicJLabel(jPanel, "Publications");
            jPanel.add(this.getCitedInPublicationJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Associated Taxon");
            jPanel.add(this.getAssociatedTaxonJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Specimen Notes");
            jPanel.add(this.getJScrollPaneNotes(), "span 3, grow");
            // double row
            this.addBasicJLabel(jPanel, "Numbers & more");
            jPanel.add(this.getNumbersJScrollPane(), "span 3 2, grow");
            jPanel.add(this.getJButtonNumbersAdd());
            // section: other fields
            // row
            this.addBasicJLabel(jPanel, "Drawer Number");
            jPanel.add(this.getDrawerNumberJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Inferences");
            jPanel.add(this.getJTextFieldInferences(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Created by");
            jPanel.add(this.getCreatorJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Creation date");
            jPanel.add(this.getDateCreatedJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Last updated by");
            jPanel.add(this.getLastUpdatedByJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Last edit date");
            jPanel.add(this.getJTextFieldDateUpdated(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Workflow Status");
            jPanel.add(this.getJComboBoxWorkflowStatus());
            this.addBasicJLabel(jPanel, "Unnamed Form");
            jPanel.add(this.getJTextFieldUnnamedForm(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Questions");
            jPanel.add(this.getQuestionsJTextField(), "grow, span 3");
            // section: controls
            jPanel.add(this.getJButtonPaste(),
                    "span, split 6");          //, sizegroup controls");
            jPanel.add(this.getJButtonHistory()); //, "span, split 4");//, "sizegroup
            // controls");
            jPanel.add(this.getJButtonPrevious(), "tag back");
            jPanel.add(this.getJButtonNext(), "tag next");
            jPanel.add(this.getJButtonCopySave(),
                    "tag apply");                        //, "sizegroup controls");
            jPanel.add(this.getSaveJButton(), "tag apply"); //, "sizegroup controls");
        }
        return jPanel;
    }

    /**
     * Get the label field containing the id of the specimen
     *
     * @return JLabel the label with the database id
     */
    private JLabel getDBIdLabel() {
        if (jLabelDBId == null) {
            jLabelDBId = new JLabel();
        }
        updateDBIdLabel();
        return jLabelDBId;
    }

    /**
     * Update the field: data base ID to match the assigned specimen
     */
    private void updateDBIdLabel() {
        this.jLabelDBId.setText("DataBase ID: " + specimen.getSpecimenId());
    }

    private void addBasicJLabel(JPanel target, String labelText) {
        JLabel label = new JLabel();
        label.setText(labelText.concat(":"));
        target.add(label,
                "tag label, right"); //"align label" was removed as requested
    }

    /**
     * This method initializes jTextFieldBarcode
     *
     * @return javax.swing.JTextField
     */
    private JTextField getBarcodeJTextField() {
        if (jTextFieldBarcode == null) {
            jTextFieldBarcode = new JTextField();
            jTextFieldBarcode.setEditable(false);
            jTextFieldBarcode.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Barcode"));
        }
        return jTextFieldBarcode;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getGenusJTextField() {
        if (jTextFieldGenus == null) {
            jTextFieldGenus = new JTextField();
            jTextFieldGenus.setEditable(specimen.isEditable());
            jTextFieldGenus.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Genus", jTextFieldGenus));
            jTextFieldGenus.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Genus"));
            jTextFieldGenus.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldGenus;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSpecificEpithetJTextField() {
        if (jTextFieldSpecies == null) {
            jTextFieldSpecies = new JTextField();
            jTextFieldSpecies.setEditable(specimen.isEditable());
            jTextFieldSpecies.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "SpecificEpithet", jTextFieldSpecies));
            jTextFieldSpecies.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "SpecificEpithet"));
            jTextFieldSpecies.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldSpecies;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSubspecifcEpithetJTextField() {
        if (jTextFieldSubspecies == null) {
            jTextFieldSubspecies = new JTextField();
            jTextFieldSubspecies.setEditable(specimen.isEditable());
            jTextFieldSubspecies.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "SubspecificEpithet", jTextFieldSubspecies));
            jTextFieldSubspecies.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "SubspecificEpithet"));
            jTextFieldSubspecies.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldSubspecies;
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSpecificLocalityJTextField() {
        if (jTextFieldLocality == null) {
            jTextFieldLocality = new JTextField();
            jTextFieldLocality.setEditable(specimen.isEditable());
            jTextFieldLocality.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "SpecificLocality", jTextFieldLocality));
            jTextFieldLocality.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "SpecificLocality"));
            jTextFieldLocality.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldLocality;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getSaveJButton() {
        if (jButtonSave == null) {
            jButtonSave = new JButton("Save");
            jButtonSave.setEnabled(specimen.isEditable());
            if (specimen.isStateDone()) {
                jButtonSave.setEnabled(false);
                jButtonSave.setText("Migrated " + specimen.getLoadFlags());
            }
            jButtonSave.setMnemonic(KeyEvent.VK_S);
            jButtonSave.setToolTipText(
                    "Save changes to this record to the database. No fields should have red backgrounds before you save.");
            jButtonSave.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisPane.save();
                }
            });
        }

        return jButtonSave;
    }

    /**
     * This method initializes the collection field
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJTextFieldCollection() {
        if (jComboBoxCollection == null) {
            log.debug("init jComboBoxCollection");
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxCollection = new JComboBox<String>();
            jComboBoxCollection.setModel(
                    new DefaultComboBoxModel<String>(sls.getDistinctCollections()));
            jComboBoxCollection.setEditable(specimen.isEditable());
            // jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Collection", jComboBoxCollection));
            jComboBoxCollection.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
            jComboBoxCollection.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxCollection);
        }
        return jComboBoxCollection;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getLastUpdatedByJTextField() {
        if (jTextFieldLastUpdatedBy == null) {
            jTextFieldLastUpdatedBy = new JTextField();
            jTextFieldLastUpdatedBy.setEditable(specimen.isEditable());
            jTextFieldLastUpdatedBy.setEditable(false);
            jTextFieldLastUpdatedBy.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "LastUpdatedBy"));
            // jTextFieldLastUpdatedBy.setEnabled(false);
            jTextFieldLastUpdatedBy.setForeground(Color.BLACK);
        }
        return jTextFieldLastUpdatedBy;
    }

    /**
     * This method initializes jScrollPaneCollectors
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneCollectors() {
        if (jScrollPaneCollectors == null) {
            jScrollPaneCollectors = this.getBasicWrapperJScrollPane();
            jScrollPaneCollectors.setViewportView(getJTableCollectors());
            jScrollPaneCollectors.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jScrollPaneCollectors;
    }

    /**
     * This method initializes jTableCollectors
     *
     * @return javax.swing.JTable
     */
    private JTable getJTableCollectors() {
        if (jTableCollectors == null) {
            try {
                jTableCollectors = new JTableWithRowBorder(
                        new CollectorTableModel(this.specimen.getCollectors()));
            } catch (NullPointerException e) {
                jTableCollectors = new JTableWithRowBorder(new CollectorTableModel());
            }

            setupCollectorJTableRenderer();
            jTableCollectors.setRowHeight(jTableCollectors.getRowHeight() + 5);

            jTableCollectors.setObjectName("Collector");
            jTableCollectors.setParentPane(thisPane);
            jTableCollectors.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    thisPane.setStateToDirty();
                }
            });
            jTableCollectors.enableDeleteability();
        }
        return jTableCollectors;
    }

    /**
     * Setup from time to time the editor
     */
    private void setupCollectorJTableRenderer() {
        CollectorLifeCycle cls = new CollectorLifeCycle();
        JComboBox<String> jComboBoxCollector =
                new JComboBox<>(cls.getDistinctCollectors());
        jComboBoxCollector.setEditable(specimen.isEditable());
        // field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class,
        // "CollectorName", field));
        jTableCollectors.getColumnModel().getColumn(0).setCellEditor(
                new ComboBoxCellEditor(jComboBoxCollector));
        AutoCompleteDecorator.decorate(jComboBoxCollector);
    }

    private JScrollPane getJScrollPaneSpecimenParts() {
        if (jScrollPaneSpecimenParts == null) {
            jScrollPaneSpecimenParts = this.getBasicWrapperJScrollPane();
            jScrollPaneSpecimenParts.setViewportView(getJTableSpecimenParts());
            jScrollPaneSpecimenParts.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jScrollPaneSpecimenParts;
    }

    public void fireSpecimenPartsTableUpdate() {
        ((AbstractTableModel) this.getJTableSpecimenParts().getModel())
                .fireTableDataChanged();
    }

    private JTable getJTableSpecimenParts() {
        if (jTableSpecimenParts == null) {
            try {
                jTableSpecimenParts = new JTableWithRowBorder(
                        new SpecimenPartsTableModel(specimen.getSpecimenParts()));
            } catch (NullPointerException e) {
                jTableSpecimenParts =
                        new JTableWithRowBorder(new SpecimenPartsTableModel());
            }
            jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTableSpecimenParts.setRowHeight(jTableSpecimenParts.getRowHeight() + 5);
            setupSpecimenPartsJTableRenderer();

            log.debug("Debug: Specimen parts size: {}",
                    specimen.getSpecimenParts().size());

            jTableSpecimenParts.setObjectName("Specimen Part");
            jTableSpecimenParts.setParentPane(thisPane);
            jTableSpecimenParts.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    thisPane.setStateToDirty();
                }
            });
            jTableSpecimenParts.enableDeleteability();
        }
        return jTableSpecimenParts;
    }

    private void setupSpecimenPartsJTableRenderer() {
        log.debug("Setting specimen part cell editors");
        JComboBox<String> comboBoxPart = new JComboBox<>(SpecimenPart.PART_NAMES);
        comboBoxPart.setEditable(specimen.isEditable());
        getJTableSpecimenParts().getColumnModel().getColumn(0).setCellEditor(
                new DefaultCellEditor(comboBoxPart));
        JComboBox<String> comboBoxPrep =
                new JComboBox<>(SpecimenPart.PRESERVATION_NAMES);
        comboBoxPrep.setEditable(specimen.isEditable());
        getJTableSpecimenParts().getColumnModel().getColumn(1).setCellEditor(
                new DefaultCellEditor(comboBoxPrep));

        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellRenderer(
                new ButtonRenderer());
        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellEditor(
                new ButtonEditor(ButtonEditor.OPEN_SPECIMENPARTATTRIBUTES, this));
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateUpdated() {
        if (jTextFieldDateLastUpdated == null) {
            jTextFieldDateLastUpdated = new JTextField();
            jTextFieldDateLastUpdated.setEditable(false);
            // jTextFieldDateLastUpdated.setEnabled(false);
            jTextFieldDateLastUpdated.setForeground(Color.BLACK);
        }
        return jTextFieldDateLastUpdated;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getCreatorJTextField() {
        if (jTextFieldCreator == null) {
            jTextFieldCreator = new JTextField();
            jTextFieldCreator.setEditable(false);
            // jTextFieldCreator.setEnabled(false);
            jTextFieldCreator.setForeground(Color.BLACK);
            jTextFieldCreator.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Creator"));
        }
        return jTextFieldCreator;
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private JTextField getDateCreatedJTextField() {
        if (jTextFieldDateCreated == null) {
            jTextFieldDateCreated = new JTextField();
            jTextFieldDateCreated.setEditable(false);
            // jTextFieldDateCreated.setEnabled(false);
            jTextFieldDateCreated.setForeground(Color.BLACK);
        }
        return jTextFieldDateCreated;
    }

    /**
     * This method initializes jScrollPaneNumbers
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getNumbersJScrollPane() {
        if (jScrollPaneNumbers == null) {
            jScrollPaneNumbers = this.getBasicWrapperJScrollPane();
            jScrollPaneNumbers.setViewportView(getNumberJTable());
            jScrollPaneNumbers.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jScrollPaneNumbers;
    }

    /**
     * This method initializes jTable for numbers fields
     *
     * @return javax.swing.JTable
     */
    private JTable getNumberJTable() {
        if (jTableNumbers == null) {
            try {
                jTableNumbers = new JTableWithRowBorder(
                        new NumberTableModel(specimen.getNumbers()));
            } catch (NullPointerException e) {
                jTableNumbers = new JTableWithRowBorder(new NumberTableModel());
            }
            jTableNumbers.setRowHeight(jTableNumbers.getRowHeight() + 5);
            setupNumberJTableRenderer();

            jTableNumbers.setObjectName("Number");
            jTableNumbers.setParentPane(thisPane);
            jTableNumbers.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    thisPane.setStateToDirty();
                }
            });
            jTableNumbers.enableDeleteability();
        }
        return jTableNumbers;
    }

    /**
     * Setting the model will overwrite the existing cell editor bound
     * to the column model, so we might need to add it again som times.
     * That's what this function does
     */
    private void setupNumberJTableRenderer() {
        // First, setup the number field
        JTextField field1 = new JTextField();
        field1.setEditable(specimen.isEditable());
        field1.setInputVerifier(
                MetadataRetriever.getInputVerifier(Number.class, "Number", field1));
        field1.setVerifyInputWhenFocusTarget(true);
        jTableNumbers.getColumnModel()
                .getColumn(NumberTableModel.COLUMN_NUMBER)
                .setCellEditor(new ValidatingTableCellEditor(field1));
        // Then, setup the type field
        JComboBox<String> jComboNumberTypes =
                new JComboBox<String>(NumberLifeCycle.getDistinctTypes());
        jComboNumberTypes.setEditable(specimen.isEditable());
        TableColumn typeColumn =
                jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
        ComboBoxCellEditor comboBoxEditor =
                new ComboBoxCellEditor(jComboNumberTypes);
        AutoCompleteDecorator.decorate(jComboNumberTypes);
        typeColumn.setCellEditor(new ComboBoxCellEditor(jComboNumberTypes));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for pick list of number types.");
        typeColumn.setCellRenderer(renderer);
    }

    /**
     * This method initializes jButtonNumbersAdd
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonNumbersAdd() {
        if (jButtonNumbersAdd == null) {
            jButtonNumbersAdd = new JButton();
            jButtonNumbersAdd.setText("+");
            URL iconFile = this.getClass().getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
            try {
                jButtonNumbersAdd.setText("");
                jButtonNumbersAdd.setIcon(new ImageIcon(iconFile));
                jButtonNumbersAdd.addActionListener(
                        new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent e) {
                                ((NumberTableModel) jTableNumbers.getModel())
                                        .addNumber(new Number(specimen, "", ""));
                                thisPane.setStateToDirty();
                            }
                        });
            } catch (Exception e) {
                jButtonNumbersAdd.setText("+");
            }
        }
        return jButtonNumbersAdd;
    }

    private JButton getJButtonGeoreference() {
        if (jButtonGeoReference == null) {
            jButtonGeoReference = new JButton();
            // allie add
      /*try {
              Set<LatLong> georeferences = specimen.getLatLong();
              log.debug("getJButtonGeoreference georeferences size is : + " +
georeferences.size()); LatLong georeference_pre =
georeferences.iterator().next(); log.debug("lat is : + " +
georeference_pre.getLatDegString()); log.debug("long is : + " +
georeference_pre.getLongDegString()); if
((!("").equals(georeference_pre.getLatDegString())) ||
                      (!("").equals(georeference_pre.getLongDegString()))){
                      jButtonGeoreference.setText("1.0 Georeference");
              }else{
                      jButtonGeoreference.setText("0.0 Georeference");
              }
} catch (Exception e) {
      log.error(e.getMessage(), e);
}	*/

            try {
                updateJButtonGeoreference();
                jButtonGeoReference.addActionListener(
                        new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent e) {
                                thisPane.setStateToDirty();
                                Set<LatLong> georeferences = specimen.getLatLong();
                                // log.debug("the lat long is : " + specimen.getLatLong().);
                                LatLong georeference = georeferences.iterator().next();
                                georeference.setSpecimen(specimen);
                                GeoreferenceDialog georefDialog =
                                        new GeoreferenceDialog(georeference, thisPane);
                                georefDialog.setVisible(true);
                                georefDialog.addComponentListener(new ComponentAdapter() {
                                    @Override
                                    public void componentHidden(ComponentEvent e) {
                                        updateJButtonGeoreference();
                                        super.componentHidden(e);
                                        autocompleteGeoDataFromGeoreference();
                                    }
                                });
                            }
                        });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return jButtonGeoReference;
    }

    public void setLocationData(String verbatimLoc, String specificLoc, String country, String stateProvince) {
        Map<Component, String> defaultsMapImmutable = Map.ofEntries(
                Map.entry(this.getVerbatimLocalityJTextField(), verbatimLoc),
                Map.entry(this.getSpecificLocalityJTextField(), specificLoc),
                Map.entry(this.getCountryJTextField(), country),
                Map.entry(this.getPrimaryDivisionJTextField(), stateProvince)
        );

        Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
        defaultsMapImmutable.forEach((field, value) -> {
            try {
                if (field instanceof JTextField) {
                    if (((JTextField) field).getText().trim().equals("") || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
                        ((JTextField) field).setText(value);
                    }
                } else if (field instanceof JComboBox) {
                    if (((JComboBox<?>) field).getSelectedItem().toString().trim().equals("") || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
                        ((JComboBox<?>) field).setSelectedItem(value);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to set value for location field", e);
            }
        });

        this.updateJButtonGeoreference();
    }

    private void updateJButtonGeoreference() {
        if (specimen.getLatLong() != null && !specimen.getLatLong().isEmpty() &&
                !specimen.getLatLong().iterator().next().isEmpty()) {
            jButtonGeoReference.setText("✅ Georeference (1)");
        } else {
            jButtonGeoReference.setText("❔ Georeference (0)");
        }
        jButtonGeoReference.updateUI();
    }

    /**
     * This method initializes jButtonCollsAdd
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonCollectorAdd() {
        if (jButtonCollectorAdd == null) {
            jButtonCollectorAdd = new JButton();
            jButtonCollectorAdd.setText("+");
            URL iconFile = this.getClass().getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
            try {
                jButtonCollectorAdd.setText("");
                jButtonCollectorAdd.setIcon(new ImageIcon(iconFile));
                jButtonCollectorAdd.addActionListener(
                        new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent e) {
                                log.debug("adding a new collector........");
                                ((CollectorTableModel) jTableCollectors.getModel())
                                        .addCollector(new Collector(specimen, ""));
                                thisPane.setStateToDirty();
                            }
                        });
            } catch (Exception e) {
                jButtonCollectorAdd.setText("+");
            }
        }
        return jButtonCollectorAdd;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getDrawerNumberJTextField() {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = new JTextField();
            jTextFieldDrawerNumber.setEditable(specimen.isEditable());
            jTextFieldDrawerNumber.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class, "DrawerNumber",
                            jTextFieldDrawerNumber));
            jTextFieldDrawerNumber.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "DrawerNumber"));
            jTextFieldDrawerNumber.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldDrawerNumber;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getVerbatimLocalityJTextField() {
        if (jTextFieldVerbatimLocality == null) {
            jTextFieldVerbatimLocality = new JTextField();
            jTextFieldVerbatimLocality.setEditable(specimen.isEditable());
            jTextFieldVerbatimLocality.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class, "VerbatimLocality",
                            jTextFieldVerbatimLocality));
            jTextFieldVerbatimLocality.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimLocality"));
            jTextFieldVerbatimLocality.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldVerbatimLocality;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getCountryJTextField() {
        // if (jTextFieldCountry == null) {
    /*jTextFieldCountry = new JTextField();
jTextFieldCountry.setEditable(specimen.isEditable());
    jTextFieldCountry.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class,
"Country", jTextFieldCountry));
    jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class,
"Country")); jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
            }
    });*/
        // allie fix
        if (jComboBoxCountry == null) {
            log.debug("init jComboBoxCountry");
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxCountry = new JComboBox<String>();
            // jComboBoxCountry.setModel(new
            // DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
            jComboBoxCountry.setModel(
                    new DefaultComboBoxModel<String>(sls.getDistinctCountries()));
            jComboBoxCountry.setEditable(specimen.isEditable());
            jComboBoxCountry.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
            jComboBoxCountry.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxCountry);
        }
        return jComboBoxCountry;
        // return jTextFieldCountry;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
  /*private JTextField getJTextField23() {
          if (jTextFieldPrimaryDivision == null) {
                  jTextFieldPrimaryDivision = new JTextField();
jTextFieldPrimaryDivision.setEditable(specimen.isEditable());
                  jTextFieldPrimaryDivision.setInputVerifier(
                                  MetadataRetriever.getInputVerifier(Specimen.class,
"primaryDivison", jTextFieldPrimaryDivision));
                  jTextFieldPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class,
"primaryDivison")); jTextFieldPrimaryDivision.addKeyListener(new
java.awt.event.KeyAdapter() { public void keyTyped(java.awt.event.KeyEvent e) {
                                  thisPane.setStateToDirty();
                          }
                  });
          }
          return jTextFieldPrimaryDivision;
  }*/
    // allie change
    private JComboBox<String> getPrimaryDivisionJTextField() {
        if (jComboBoxPrimaryDivision == null) {
            jComboBoxPrimaryDivision = new JComboBox<>();
            jComboBoxPrimaryDivision.setEditable(specimen.isEditable());
            // jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "primaryDivison", jTextFieldPrimaryDivision));
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxPrimaryDivision.setModel(
                    new DefaultComboBoxModel<String>(sls.getDistinctPrimaryDivisions()));
            jComboBoxPrimaryDivision.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "primaryDivison"));
            jComboBoxPrimaryDivision.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision);
        }
        return jComboBoxPrimaryDivision;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getFamilyJTextField() {
        if (jComboBoxFamily == null) {
            jComboBoxFamily = new JComboBox<String>();
            jComboBoxFamily.setModel(new DefaultComboBoxModel<String>(
                    HigherTaxonLifeCycle.selectDistinctFamily()));
            jComboBoxFamily.setEditable(specimen.isEditable());
            // jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Family", jTextFieldFamily));
            jComboBoxFamily.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Family"));
            jComboBoxFamily.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxFamily);
        }
        return jComboBoxFamily;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getJTextFieldSubfamily() {
        if (jComboBoxSubfamily == null) {
            jComboBoxSubfamily = new JComboBox<String>();
            jComboBoxSubfamily.setModel(new DefaultComboBoxModel<String>(
                    HigherTaxonLifeCycle.selectDistinctSubfamily("")));
            jComboBoxSubfamily.setEditable(specimen.isEditable());
            // jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Subfamily", jTextFieldSubfamily));
            jComboBoxSubfamily.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Subfamily"));
            jComboBoxSubfamily.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxSubfamily);
        }
        return jComboBoxSubfamily;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldTribe() {
        if (jTextFieldTribe == null) {
            jTextFieldTribe = new JTextField();
            jTextFieldTribe.setEditable(specimen.isEditable());
            jTextFieldTribe.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Tribe", jTextFieldTribe));
            jTextFieldTribe.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Tribe"));
            jTextFieldTribe.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldTribe;
    }

    /**
     * This method initializes jComboBoxSex
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJComboBoxSex() {
        if (jComboBoxSex == null) {
            jComboBoxSex = new JComboBox<String>();
            jComboBoxSex.setModel(
                    new DefaultComboBoxModel<String>(Sex.getSexValues()));
            jComboBoxSex.setEditable(specimen.isEditable());
            jComboBoxSex.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Sex"));
            jComboBoxSex.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxSex);
        }
        return jComboBoxSex;
    }

    /**
     * This method initializes jComboBoxFeatures
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJComboBoxFeatures() {
        if (jComboBoxFeatures == null) {
            jComboBoxFeatures = new JComboBox<String>();
            jComboBoxFeatures.setModel(
                    new DefaultComboBoxModel<String>(Features.getFeaturesValues()));
            jComboBoxFeatures.setEditable(specimen.isEditable());
            jComboBoxFeatures.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Features"));
            jComboBoxFeatures.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxFeatures);
            // TODO: validate input length
        }
        return jComboBoxFeatures;
    }

    private JComboBox<String> getJComboBoxNatureOfId() {
        if (jComboBoxNatureOfId == null) {
            jComboBoxNatureOfId = new JComboBox<String>();
            jComboBoxNatureOfId.setModel(
                    new DefaultComboBoxModel<String>(NatureOfId.getNatureOfIdValues()));
            jComboBoxNatureOfId.setEditable(specimen.isEditable());
            jComboBoxNatureOfId.setToolTipText(
                    MetadataRetriever.getFieldHelp(Determination.class, "NatureOfId"));
            jComboBoxNatureOfId.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxNatureOfId);
            jComboBoxNatureOfId.setSelectedItem(NatureOfId.EXPERT_ID);
            jComboBoxNatureOfId.setSelectedIndex(0);
        }
        return jComboBoxNatureOfId;
    }

    /**
     * This method initializes jComboBoxLifeStage
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJComboBoxLifeStage() {
        if (jComboBoxLifeStage == null) {
            jComboBoxLifeStage = new JComboBox<String>();
            jComboBoxLifeStage.setModel(
                    new DefaultComboBoxModel<String>(LifeStage.getLifeStageValues()));
            jComboBoxLifeStage.setEditable(specimen.isEditable());
            jComboBoxLifeStage.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Lifestage"));
            jComboBoxLifeStage.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxLifeStage);
            jComboBoxLifeStage.setSelectedItem("adult");
            jComboBoxLifeStage.setSelectedIndex(0);
        }
        return jComboBoxLifeStage;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldVerbatimDate() {
        if (jTextFieldDateNos == null) {
            jTextFieldDateNos = new JTextField();
            jTextFieldDateNos.setEditable(specimen.isEditable());
            // jTextFieldDateNos.setToolTipText("Date found on labels where date might
            // be either date collected or date emerged, or some other date");
            jTextFieldDateNos.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "DateNOS", jTextFieldDateNos));
            jTextFieldDateNos.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "DateNOS"));
            jTextFieldDateNos.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            //            InputUtility.addChangeListener(jTextFieldDateNos, e -> {
            //                // co-update the ISO-date field
            //                // TODO: note that this could be unwanted. possibly. On
            //                the other hand,
            //                //  more possibilities for formats could be
            //                automatically set.
            //                //  To be sure that we do not update unwanted, we only
            //                apply it to certain forms only
            //                //  Possible pitfalls of other forms: not knowing
            //                if 19.., 18.., ... String verbatimDate =
            //                jTextFieldDateNos.getText().trim(); if
            //                (verbatimDate.matches("\\d{1,2}\\.\\s*\\d{1,2}\\.\\s*\\d{4}"))
            //                { // assume dd.mm.yyyy resp. dd. mm. yyyy
            //                    String[] parts = verbatimDate.split("\\.");
            //                    jTextFieldISODate.setText(parts[2].trim() + "/" +
            //                    parts[1].trim() + "/" + parts[0].trim()); //
            //                    yyyy/mm/dd
            //                } else if
            //                (verbatimDate.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
            //                    jTextFieldISODate.setText(verbatimDate);
            //                }
            //            });
        }
        return jTextFieldDateNos;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateEmerged() {
        if (jTextFieldDateEmerged == null) {
            jTextFieldDateEmerged = new JTextField();
            jTextFieldDateEmerged.setEditable(specimen.isEditable());
            jTextFieldDateEmerged.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "DateEmerged", jTextFieldDateEmerged));
            jTextFieldDateEmerged.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "DateEmerged"));
            jTextFieldDateEmerged.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldDateEmerged;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateEmergedIndicator() {
        if (jTextFieldDateEmergedIndicator == null) {
            jTextFieldDateEmergedIndicator = new JTextField();
            jTextFieldDateEmergedIndicator.setEditable(specimen.isEditable());
            jTextFieldDateEmergedIndicator.setToolTipText(
                    "Verbatim text indicating that this is a date emerged.");
            jTextFieldDateEmergedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class,
                            "DateEmergedIndicator",
                            jTextFieldDateEmergedIndicator));
            jTextFieldDateEmergedIndicator.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class,
                            "DateEmergedIndicator"));
            jTextFieldDateEmergedIndicator.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldDateEmergedIndicator;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateCollected() {
        if (jTextFieldDateCollected == null) {
            jTextFieldDateCollected = new JTextField();
            jTextFieldDateCollected.setEditable(specimen.isEditable());
            jTextFieldDateCollected.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "DateCollected"));
            jTextFieldDateCollected.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldDateCollected;
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateCollectedIndicator() {
        if (jTextFieldDateCollectedIndicator == null) {
            jTextFieldDateCollectedIndicator = new JTextField();
            jTextFieldDateCollectedIndicator.setEditable(specimen.isEditable());
            jTextFieldDateCollectedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class,
                            "DateCollectedIndicator",
                            jTextFieldDateCollectedIndicator));
            jTextFieldDateCollectedIndicator.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class,
                            "DateCollectedIndicator"));
            jTextFieldDateCollectedIndicator.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldDateCollectedIndicator;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldInfraspecificName() {
        if (jTextFieldInfraspecificEpithet == null) {
            jTextFieldInfraspecificEpithet = new JTextField();
            jTextFieldInfraspecificEpithet.setEditable(specimen.isEditable());
            jTextFieldInfraspecificEpithet.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class,
                            "InfraspecificEpithet",
                            jTextFieldInfraspecificEpithet));
            jTextFieldInfraspecificEpithet.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class,
                            "InfraspecificEpithet"));
            jTextFieldInfraspecificEpithet.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldInfraspecificEpithet;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldInfraspecificRank() {
        if (jTextFieldInfraspecificRank == null) {
            jTextFieldInfraspecificRank = new JTextField();
            jTextFieldInfraspecificRank.setEditable(specimen.isEditable());
            jTextFieldInfraspecificRank.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class,
                            "InfraspecificRank",
                            jTextFieldInfraspecificRank));
            jTextFieldInfraspecificRank.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "InfraspecificRank"));
            jTextFieldInfraspecificRank.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldInfraspecificRank;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldAuthorship() {
        if (jTextFieldAuthorship == null) {
            jTextFieldAuthorship = new JTextField();
            jTextFieldAuthorship.setEditable(specimen.isEditable());
            jTextFieldAuthorship.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Authorship", jTextFieldAuthorship));
            jTextFieldAuthorship.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Authorship"));
            jTextFieldAuthorship.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldAuthorship;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldUnnamedForm() {
        if (jTextFieldUnnamedForm == null) {
            jTextFieldUnnamedForm = new JTextField();
            jTextFieldUnnamedForm.setEditable(specimen.isEditable());
            jTextFieldUnnamedForm.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "UnnamedForm", jTextFieldUnnamedForm));
            jTextFieldUnnamedForm.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "UnnamedForm"));
            jTextFieldUnnamedForm.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldUnnamedForm;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getVerbatimElevationJTextField() {
        if (jTextFieldMinElevation == null) {
            jTextFieldMinElevation = new JTextField();
            jTextFieldMinElevation.setEditable(specimen.isEditable());
            jTextFieldMinElevation.setInputVerifier(
                    MetadataRetriever.getInputVerifier(
                            Specimen.class, "VerbatimElevation", jTextFieldMinElevation));
            jTextFieldMinElevation.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimElevation"));
            jTextFieldMinElevation.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldMinElevation;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldCollectingMethod() {
        if (jTextFieldCollectingMethod == null) {
            jTextFieldCollectingMethod = new JTextField();
            jTextFieldCollectingMethod.setEditable(specimen.isEditable());
            jTextFieldCollectingMethod.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class, "CollectingMethod",
                            jTextFieldCollectingMethod));
            jTextFieldCollectingMethod.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "CollectingMethod"));
            jTextFieldCollectingMethod.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jTextFieldCollectingMethod;
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextAreaNotes() {
        if (jTextAreaSpecimenNotes == null) {
            jTextAreaSpecimenNotes = new JTextArea();
            jTextAreaSpecimenNotes.setRows(3);
            jTextAreaSpecimenNotes.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "SpecimenNotes"));
            jTextAreaSpecimenNotes.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextAreaSpecimenNotes;
    }

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getValidDistributionJCheckBox() {
        if (jCheckBoxValidDistributionFlag == null) {
            jCheckBoxValidDistributionFlag = new JCheckBox();
            // jCheckBoxValidDistributionFlag.setToolTipText("Check if locality
            // represents natural biological range.  Uncheck for Specimens that came
            // from a captive breeding program");
            jCheckBoxValidDistributionFlag.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class,
                            "ValidDistributionFlag"));
            jCheckBoxValidDistributionFlag.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
        }
        return jCheckBoxValidDistributionFlag;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getQuestionsJTextField() {
        if (jTextFieldQuestions == null) {
            jTextFieldQuestions = new JTextField();
            jTextFieldQuestions.setEditable(specimen.isEditable());
            jTextFieldQuestions.setBackground(MainFrame.BG_COLOR_QC_FIELD);
            jTextFieldQuestions.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Questions", jTextFieldQuestions));
            jTextFieldQuestions.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Questions"));
            jTextFieldQuestions.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldQuestions;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JButton getJButtonAddPrep() {
        if (jButtonAddPreparationType == null) {
            jButtonAddPreparationType = new JButton("Add Prep");
            jButtonAddPreparationType.setMnemonic(KeyEvent.VK_A);

            jButtonAddPreparationType.addActionListener(
                    new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            log.debug("Adding new SpecimenPart");
                            SpecimenPart newPart = new SpecimenPart();
                            newPart.setPreserveMethod(
                                    Singleton.getSingletonInstance()
                                            .getProperties()
                                            .getProperties()
                                            .getProperty(
                                                    ImageCaptureProperties.KEY_DEFAULT_PREPARATION));
                            newPart.setSpecimen(specimen);
                            SpecimenPartLifeCycle spls = new SpecimenPartLifeCycle();
                            log.debug("Attaching new SpecimenPart");
                            try {
                                spls.persist(newPart);
                                specimen.getSpecimenParts().add(newPart);
                                ((AbstractTableModel) jTableSpecimenParts.getModel())
                                        .fireTableDataChanged();
                                log.debug("Added new SpecimenPart");
                            } catch (SaveFailedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    });
        }
        return jButtonAddPreparationType;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getAssociatedTaxonJTextField() {
        if (jTextFieldAssociatedTaxon == null) {
            jTextFieldAssociatedTaxon = new JTextField();
            jTextFieldAssociatedTaxon.setEditable(specimen.isEditable());
            jTextFieldAssociatedTaxon.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class, "AssociatedTaxon",
                            jTextFieldAssociatedTaxon));
            jTextFieldAssociatedTaxon.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "AssociatedTaxon"));
            jTextFieldAssociatedTaxon.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldAssociatedTaxon;
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldHabitat() {
        if (jTextFieldHabitat == null) {
            jTextFieldHabitat = new JTextField();
            jTextFieldHabitat.setEditable(specimen.isEditable());
            jTextFieldHabitat.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Habitat", jTextFieldHabitat));
            jTextFieldHabitat.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Habitat"));
            jTextFieldHabitat.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldHabitat;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJComboBoxWorkflowStatus() {
        if (jComboBoxWorkflowStatus == null) {
            jComboBoxWorkflowStatus = new JComboBox<String>();
            jComboBoxWorkflowStatus.setModel(new DefaultComboBoxModel<String>(
                    WorkFlowStatus.getWorkFlowStatusValues()));
            jComboBoxWorkflowStatus.setEditable(false);
            jComboBoxWorkflowStatus.setBackground(MainFrame.BG_COLOR_QC_FIELD);
            jComboBoxWorkflowStatus.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "WorkflowStatus"));
            AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus);
        }
        return jComboBoxWorkflowStatus;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getLocationInCollectionJComboBox() {
        if (jComboBoxLocationInCollection == null) {
            jComboBoxLocationInCollection = new JComboBox<String>();
            jComboBoxLocationInCollection.setModel(new DefaultComboBoxModel<String>(
                    LocationInCollection.getLocationInCollectionValues()));
            jComboBoxLocationInCollection.setEditable(false);
            jComboBoxLocationInCollection.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class,
                            "LocationInCollection"));

            // alliefix - set default from properties file
            // jComboBoxLocationInCollection.setSelectedIndex(1);

            jComboBoxLocationInCollection.addKeyListener(
                    new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent e) {
                            thisPane.setStateToDirty();
                        }
                    });
            AutoCompleteDecorator.decorate(jComboBoxLocationInCollection);
        }
        return jComboBoxLocationInCollection;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldInferences() {
        if (jTextFieldInferences == null) {
            jTextFieldInferences = new JTextField();
            jTextFieldInferences.setEditable(specimen.isEditable());
            jTextFieldInferences.setBackground(MainFrame.BG_COLOR_ENT_FIELD);
            jTextFieldInferences.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "Inferences", jTextFieldInferences));
            jTextFieldInferences.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "Inferences"));
            jTextFieldInferences.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldInferences;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonHistory() {
        if (jButtonGetHistory == null) {
            jButtonGetHistory = new JButton();
            jButtonGetHistory.setText("History");
            jButtonGetHistory.setToolTipText(
                    "Show the history of who edited this record");
            jButtonGetHistory.setMnemonic(KeyEvent.VK_H);
            jButtonGetHistory.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // retrieve and display the tracking events for this specimen
                    // Tracking t = new Tracking();
                    // t.setSpecimen(specimen);
                    TrackingLifeCycle tls = new TrackingLifeCycle();
                    // Request by specimen doesn't work with Oracle.  Why?
                    // EventLogFrame logViewer = new EventLogFrame(new
                    // ArrayList<Tracking>(tls.findBySpecimen(specimen)));
                    EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(
                            tls.findBySpecimenId(specimen.getSpecimenId())));
                    logViewer.pack();
                    logViewer.setVisible(true);
                }
            });
        }
        return jButtonGetHistory;
    }

    /**
     * Instantiate - if necessary - the button to paste the copied specimen on the
     * current one
     *
     * @return
     */
    private JButton getJButtonPaste() {
        log.debug("prev spec:::");
        if (jButtonPaste == null) {
            jButtonPaste = new JButton();
            jButtonPaste.setText("Paste");
            jButtonPaste.setToolTipText(
                    "Paste previous record values into this screen");
            // TODO: decide on keyboard shortcut
            jButtonPaste.setMnemonic(KeyEvent.VK_V);
            jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // populate the fields with the data.
                    previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache;
                    pastePreviousRecord();
                }
            });
            this.updateJButtonPaste();
        }
        return jButtonPaste;
    }

    private void updateJButtonPaste() {
        if (jButtonPaste != null) {
            jButtonPaste.setEnabled(
                    !(this.previousSpecimen == null &&
                            ImageCaptureApp.lastEditedSpecimenCache == null));
        }
    }

    /**
     * Instantiate - if necessary - the button to save and then copy the current
     * specimen
     *
     * @return
     */
    private JButton getJButtonCopySave() {
        if (jButtonCopy == null) {
            jButtonCopy = new JButton();
            jButtonCopy.setText("Save & Copy");
            jButtonCopy.setToolTipText(
                    "Copy the values of this record after saving it");
            // TODO: decide on keyboard shortcut
            jButtonCopy.setMnemonic(KeyEvent.VK_K);
            jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (thisPane.save()) {
                        // TODO: rather clone the specimen to prevent external/later changes
                        ImageCaptureApp.lastEditedSpecimenCache = thisPane.specimen;
                        thisPane.setStatus("Saved & copied specimen with id " +
                                thisPane.specimen.getSpecimenId());
                    }
                }
            });
        }
        return jButtonCopy;
    }

    /**
     * This method initializes jButtonNext
     *
     * @return javax.swing.JButton
     */
    // this is not the right arrow button!
    private JButton getJButtonNext() {
        if (jButtonNext == null) {
            jButtonNext = new JButton();
            URL iconFile = this.getClass().getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/next.png");
            if (iconFile != null) {
                jButtonNext.setIcon(new ImageIcon(iconFile));
            } else {
                jButtonNext.setText("Next");
            }
            jButtonNext.setMnemonic(KeyEvent.VK_N);
            jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable());
            log.debug("next button enabled: " +
                    specimenController.hasNextSpecimenInTable());
            jButtonNext.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisPane.gotoNextSpecimen();
                }
            });
        }
        return jButtonNext;
    }

    private void gotoNextSpecimen() {
        try {
            // try to move to the next specimen in the table.
            thisPane.setStatus("Switching to next specimen...");
            if (thisPane.specimenController.openNextSpecimenInTable()) {
                thisPane.setVisible(false);
                thisPane.invalidate();
            } else {
                thisPane.setWarning("No next specimen available.");
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                thisPane.getParent().setCursor(
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } catch (Exception ex) {
                log.error("Error", ex);
            }
        }
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonPrevious() {
        if (jButtonPrevious == null) {
            jButtonPrevious = new JButton();
            URL iconFile = this.getClass().getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/back.png");
            if (iconFile != null) {
                jButtonPrevious.setIcon(new ImageIcon(iconFile));
            } else {
                jButtonPrevious.setText("Previous");
            }
            jButtonPrevious.setMnemonic(KeyEvent.VK_P);
            jButtonPrevious.setToolTipText("Move to Previous Specimen");
            jButtonPrevious.setEnabled(
                    specimenController.hasPreviousSpecimenInTable());
            jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisPane.gotoPreviousSpecimen();
                }
            });
        }
        return jButtonPrevious;
    }

    private void gotoPreviousSpecimen() {
        try {
            // try to move to the previous specimen in the table.
            thisPane.setStatus("Switching to previous specimen...");
            if (thisPane.specimenController.openPreviousSpecimenInTable()) {
                thisPane.setVisible(false);
                thisPane.invalidate();
            } else {
                thisPane.setWarning("No previous specimen available.");
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void setStateToClean() {
        state = STATE_CLEAN;
        // if this is a record that is part of a navigateable set, enable the
        // navigation buttons
        if (specimenController != null) {
            log.debug("Has controller");
            if (specimenController.isInTable()) {
                log.debug("Controller is in table");
                // test to make sure the buttons have been created before trying to
                // enable them.
                if (jButtonNext != null) {
                    jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable());
                }
                if (jButtonPrevious != null) {
                    jButtonPrevious.setEnabled(
                            specimenController.hasPreviousSpecimenInTable());
                }
            }
        }
    }

    private void setStateToDirty() {
        state = STATE_DIRTY;
        if (jButtonNext != null) {
            this.jButtonNext.setEnabled(false);
        }
        if (jButtonPrevious != null) {
            this.jButtonPrevious.setEnabled(false);
        }
    }

    /**
     * State of the data in the forms as compared to the specimen from which the
     * data was loaded.
     *
     * @return true if the data as displayed in the forms hasn't changed since the
     * data was last loaded from
     * or saved to the specimen, otherwise false indicating a dirty record.
     */
    private boolean isClean() {
        boolean result = false;
        if (state == STATE_CLEAN) {
            result = true;
        }
        return result;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldISODate() {
        if (jTextFieldISODate == null) {
            jTextFieldISODate = new JTextField();
            jTextFieldISODate.setEditable(specimen.isEditable());
            jTextFieldISODate.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "ISODate", jTextFieldISODate));
            jTextFieldISODate.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "ISODate"));
            jTextFieldISODate.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldISODate;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getDetsJButton() {
        if (jButtonDeterminations == null) {
            jButtonDeterminations = new JButton();
            jButtonDeterminations.setText("Dets.");
            jButtonDeterminations.setMnemonic(KeyEvent.VK_D);

            jButtonDeterminations.addActionListener(
                    new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            DeterminationFrame dets = new DeterminationFrame(specimen);
                            // update the text of the dets as soon as the component is closed
                            dets.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentHidden(ComponentEvent e) {
                                    updateDeterminationCount();
                                    super.componentHidden(e);
                                }
                            });
                            dets.setVisible(true);
                        }
                    });
        }
        return jButtonDeterminations;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getCitedInPublicationJTextField() {
        if (jTextFieldCitedInPub == null) {
            jTextFieldCitedInPub = new JTextField();
            jTextFieldCitedInPub.setEditable(specimen.isEditable());
            jTextFieldCitedInPub.setInputVerifier(MetadataRetriever.getInputVerifier(
                    Specimen.class, "CitedInPublication", jTextFieldCitedInPub));
            jTextFieldCitedInPub.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "CitedInPublication"));
            jTextFieldCitedInPub.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldCitedInPub;
    }

    private JScrollPane getBasicWrapperJScrollPane() {
        JScrollPane pane = new JScrollPane();
        pane.addMouseWheelListener(new MouseWheelScrollListener(pane));
        int maxHeight = Integer.parseInt(
                Singleton.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.KEY_MAX_FIELD_HEIGHT));
        pane.setMaximumSize(new Dimension(1000, maxHeight));
        return pane;
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneNotes() {
        if (jScrollPaneNotes == null) {
            jScrollPaneNotes = this.getBasicWrapperJScrollPane();
            jScrollPaneNotes.setViewportView(getJTextAreaNotes());
            // jScrollPaneNotes.add(getJTextAreaNotes()); //allie!!!
        }
        return jScrollPaneNotes;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getDateEmergedJButton() {
        if (dateEmergedButton == null) {
            dateEmergedButton = new JButton();
            dateEmergedButton.setText("Date Emerged");
            dateEmergedButton.setToolTipText(
                    "Fill date emerged with data from verbatim date");
            dateEmergedButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jTextFieldDateNos.getText().equals("")) {
                        jTextFieldDateNos.setText(jTextFieldDateEmerged.getText());
                    } else {
                        jTextFieldDateEmerged.setText(jTextFieldDateNos.getText());
                    }
                }
            });
        }
        return dateEmergedButton;
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private JButton getDateCollectedJButton() {
        if (dateCollectedButton == null) {
            dateCollectedButton = new JButton();
            dateCollectedButton.setText("Date Collected");
            dateCollectedButton.setToolTipText(
                    "Fill date collected with data from verbatim date");
            dateCollectedButton.addActionListener(
                    new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            if (jTextFieldDateNos.getText().equals("")) {
                                jTextFieldDateNos.setText(jTextFieldDateCollected.getText());
                            } else {
                                jTextFieldDateCollected.setText(jTextFieldDateNos.getText());
                            }
                        }
                    });
        }
        return dateCollectedButton;
    }

    /**
     * This method initializes jButtonSpecificLocality
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSpecificLocality() {
        if (jButtonSpecificLocality == null) {
            jButtonSpecificLocality = new JButton();
            jButtonSpecificLocality.setText("Specific Locality");
            jButtonSpecificLocality.setToolTipText(
                    "Fill specific locality with data from verbatim locality");
            jButtonSpecificLocality.addActionListener(
                    new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            if (jTextFieldVerbatimLocality.getText().equals("")) {
                                if (jTextFieldLocality.getText().equals("")) {
                                    // If both are blank, set the blank value string.
                                    jTextFieldLocality.setText("[no specific locality data]");
                                }
                                jTextFieldVerbatimLocality.setText(
                                        jTextFieldLocality.getText());
                            } else {
                                jTextFieldLocality.setText(
                                        jTextFieldVerbatimLocality.getText());
                            }
                        }
                    });
        }
        return jButtonSpecificLocality;
    }

    private JTextField getJTextFieldMigrationStatus() {
        if (jTextFieldMigrationStatus == null) {
            jTextFieldMigrationStatus = new JTextField();
            jTextFieldMigrationStatus.setEditable(specimen.isEditable());
            // jLabelMigrationStatus.setBackground(null);
            // jLabelMigrationStatus.setBorder(null);
            jTextFieldMigrationStatus.setEditable(false);
            jTextFieldMigrationStatus.setText("");
            if (specimen.isStateDone()) {
                String uri = "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" +
                        specimen.getCatNum();
                jTextFieldMigrationStatus.setText(uri);
            }
        }
        return jTextFieldMigrationStatus;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldImgCount() {
        if (jTextFieldImageCount == null) {
            jTextFieldImageCount = new JTextField();
            if (specimen != null) {
                jTextFieldImageCount.setEditable(specimen.isEditable());
            }
            jTextFieldImageCount.setForeground(Color.BLACK);
            jTextFieldImageCount.setEnabled(false);
            updateImageCount();
        }
        return jTextFieldImageCount;
    }

    /**
     * Set the image count value into the corresponding field.
     * Set the color to red if there is more than 1 image
     */
    private void updateImageCount() {
        int imageCount = 0;
        if (specimen.getICImages() != null) {
            imageCount = specimen.getICImages().size();
        }
        jTextFieldImageCount.setText(Integer.toString(imageCount));
        if (imageCount > 1) {
            jTextFieldImageCount.setForeground(Color.RED);
        } else {
            jTextFieldImageCount.setForeground(Color.BLACK);
        }
    }

    private JTextField getTextFieldMaxElev() {
        if (textFieldMaxElev == null) {
            textFieldMaxElev = new JTextField();
            textFieldMaxElev.setEditable(specimen.isEditable());
        }
        return textFieldMaxElev;
    }

    private JComboBox<String> getComboBoxElevUnits() {
        if (comboBoxElevUnits == null) {
            comboBoxElevUnits = new JComboBox<String>();
            comboBoxElevUnits.setModel(
                    new DefaultComboBoxModel<>(new String[]{"", "?", "m", "ft"}));
        }
        return comboBoxElevUnits;
    }

    private JTextField getTextFieldMicrohabitat() {
        if (textFieldMicrohabitat == null) {
            textFieldMicrohabitat = new JTextField();
            textFieldMicrohabitat.setEditable(specimen.isEditable());
        }
        return textFieldMicrohabitat;
    }

    private void autocompleteGeoDataFromGeoreference() {
        LatLong georeff = this.specimen.getLatLong().iterator().next();
        if (georeff.getDecLat() != null && georeff.getDecLong() != null) {
            // do it async as the request could take longer than desired
            new Thread(() -> {
                log.debug("Fetching address from openstreetmap");
                Map<String, Object> data =
                        OpenStreetMapUtility.getInstance().reverseSearchValues(
                                georeff.getDecLat(), georeff.getDecLong(),
                                new ArrayList<>(
                                        Arrays.asList("address.state", "address.country")));
                if (data != null) {
                    log.debug("Got address from openstreetmap: " + data.toString());
                    if (this.getCountryJTextField().getSelectedItem() == null ||
                            this.getCountryJTextField().getSelectedItem().equals("")) {
                        this.getCountryJTextField().setSelectedItem(
                                data.get("address.country"));
                    } else {
                        log.debug("Won't automatically set country as is '" +
                                this.getCountryJTextField().getSelectedItem() + "'.");
                    }
                    if (this.getPrimaryDivisionJTextField().getSelectedItem() == null ||
                            this.getPrimaryDivisionJTextField().getSelectedItem().equals(
                                    "")) {
                        this.getPrimaryDivisionJTextField().setSelectedItem(
                                data.get("address.state"));
                    } else {
                        log.debug("Won't automatically set primary division as is '" +
                                this.getCountryJTextField().getSelectedItem() + "'.");
                    }
                }
            }).start();
        }
    }

    /**
     * This method initializes jTextFieldDateDetermined
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDateDetermined() {
        if (jTextFieldDateDetermined == null) {
            jTextFieldDateDetermined = new JTextField();
            jTextFieldDateDetermined.setEditable(specimen.isEditable());
            jTextFieldDateDetermined.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen.class, "ISODate",
                            jTextFieldDateDetermined));
            jTextFieldDateDetermined.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "DateIdentified"));
            jTextFieldDateDetermined.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldDateDetermined;
    }

    /**
     * This method initializes the Determier pick list.
     *
     * @return FilteringAgentJComboBox
     */
    private JComboBox<String> getJCBDeterminer() {
        log.debug("calling getJCBDeterminer() ... it is " + jCBDeterminer);
        if (jCBDeterminer == null) {
            log.debug("init jCBDeterminer determiner null, making a new one");
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jCBDeterminer = new JComboBox<String>();
            jCBDeterminer.setModel(
                    new DefaultComboBoxModel<String>(sls.getDistinctDeterminers()));
            jCBDeterminer.setEditable(specimen.isEditable());
            // jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Collection", jComboBoxCollection));
            // jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class,
            // "Collection"));
            jCBDeterminer.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jCBDeterminer);
        }
        return jCBDeterminer;
    }

    /**
     * This method initializes type status pick list
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getCbTypeStatus() {
        if (cbTypeStatus == null) {
            cbTypeStatus = new JComboBox<String>(TypeStatus.getTypeStatusValues());
            // cbTypeStatus = new JComboBox(TypeStatus.getTypeStatusValues());  // for
            // visual editor
            cbTypeStatus.setEditable(specimen.isEditable());
            cbTypeStatus.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "TypeStatus"));
            cbTypeStatus.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return cbTypeStatus;
    }

    /**
     * This method initializes jTextFieldIdRemarks
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldIdRemarks() {
        if (jTextFieldIdRemarks == null) {
            jTextFieldIdRemarks = new JTextField();
            jTextFieldIdRemarks.setEditable(specimen.isEditable());
            jTextFieldIdRemarks.setToolTipText(MetadataRetriever.getFieldHelp(
                    Specimen.class, "IdentificationRemarks"));
            jTextFieldIdRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
        }
        return jTextFieldIdRemarks;
    }

    private void updateContentDependentLabels() {
        updateJButtonGeoreference();
        updateDeterminationCount();
        updateJButtonPaste();
        updateDBIdLabel();
    }
}
