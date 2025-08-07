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
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.*;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.fixed.*;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.interfaces.CloseListener;
import edu.harvard.mcz.imagecapture.interfaces.CloseType;
import edu.harvard.mcz.imagecapture.lifecycle.*;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
import edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListener;
import edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditor;
import edu.harvard.mcz.imagecapture.ui.component.JAccordionPanel;
import edu.harvard.mcz.imagecapture.ui.component.JTableCellTabbing;
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder;
import edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog;
import edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBox;
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel;
import edu.harvard.mcz.imagecapture.utility.CastUtility;
import edu.harvard.mcz.imagecapture.utility.GeoNamesUtility;
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility;
import jakarta.persistence.OptimisticLockException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import org.hibernate.SessionException;
import org.hibernate.TransactionException;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPanel for editing a record of a Specimen in a details view for that
 * specimen. <p>
 * TODO: BugID: 10 add length limits (remaining to do for Number/Collector
 * tables, and for JComboBox fields).
 */
public class SpecimenDetailsViewPane extends JPanel {

    public static final boolean copyPasteActivated = Singleton.getSingletonInstance().getUser() != null && Singleton.getSingletonInstance().getUser().canCopyPaste();
    private static final Logger log = LoggerFactory.getLogger(SpecimenDetailsViewPane.class);
    private static final long serialVersionUID = 3716072190995030749L;
    private static final int STATE_CLEAN = 0;
    private static final int STATE_DIRTY = 1;
    private final Specimen specimen; //  @jve:decl-index=0:
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
    private JAccordionPanel accordionDetailsPanel = null;
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
    // private JButton jButtonSaveNext = null;
    private JButton jButtonSpecificLocality = null;
    private JCheckBox jCheckBoxValidDistributionFlag = null;
    private JComboBox<String> cbTypeStatus;
    private JComboBox<String> comboBoxElevUnits = null;
    private JComboBox<String> jCBDeterminer = null;
    private JComboBox<String> jComboBoxCollection = null;
    private JComboBox<String> jComboBoxCountry = null;
    private JComboBox<String> jComboBoxFamily = null;
    private JComboBox<String> jComboBoxHigherOrder = null;
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
    private JTextField jTextFieldMaxElevation = null;
    private JTextField jTextFieldQuestions = null;
    private JTextField jTextFieldSpecies = null;
    private JTextField jTextFieldStatus = null;
    private JTextField jTextFieldSubspecies = null;
    private JTextField jTextFieldTribe = null;
    private JTextField jTextFieldUnnamedForm = null;
    private JTextField jTextFieldVerbatimLocality = null;
    private JTextField textFieldMicrohabitat = null;
    private JTextField jTextFieldGBIFTaxonId = null;
    private JButton jButtonGBIFView = null;
    private JButton citedInPublicationButton = null;
    private SpecimenDetailsViewPane thisPane = null;

    private JTextField textFieldDecimalLat;
    private JTextField textFieldDecimalLong;
    private JComboBox<String> cbMethod;
    private JComboBox<String> cbDatum;
    private JTextField txtErrorRadius;
    private JComboBox<String> comboBoxErrorUnits;
    private JButton pasteExcelButton;
    private GeoreferenceDialog georeferenceDialog;
    private JButton jButtonNahimaLink;

    /**
     * Construct an instance of a SpecimenDetailsViewPane showing the data present
     * in aSpecimenInstance.
     *
     * @param aSpecimenInstance the Specimen instance to display for editing.
     */
    public SpecimenDetailsViewPane(Specimen aSpecimenInstance, SpecimenController aController) {
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
        // spals.attachDirty(spa); log.debug("Debug",
        // spa.getSpecimenPartAttributeId()); 				} catch
        // (SaveFailedException e)
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
            String status = "Undefined error initializing SpecimenDetails. " + "Restarting Database connection...";
            if (e instanceof SessionException || e instanceof TransactionException) {
                status = "Database Connection Error. Resetting connection... Try again.";
            } else if (e instanceof IllegalStateException) {
                status = "Illegal state exception. Last edit possibly lost. Try again.";
            } else if (e instanceof OptimisticLockException) {
                status = "Error: last edited entry has been modified externally. Try again.";
            }
            log.debug(status);
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

        JScrollPane scrollPane = new JScrollPane(getJPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
        if (copyPasteActivated) {
            registerShortcut("specimen.copyThis", "ctrl alt C", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ImageCaptureApp.lastEditedSpecimenCache = thisPane.specimen;
                    thisPane.setStatus("Copied specimen with id " + thisPane.specimen.getSpecimenId());
                }
            });
            registerShortcut("specimen.paste", "ctrl alt V", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache;
                    pastePreviousRecord();
                }
            });
        }
        if (!specimen.isEditable(Singleton.getSingletonInstance().getUser())) {
            JOptionPane.showMessageDialog(thisPane, "This Specimen is already exported. Edit " + "will not be saved to Nahima.", "Warning: not editable", JOptionPane.WARNING_MESSAGE);
        }
    }

    void registerShortcut(String name, String defaultStroke, Action action) {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();
        inputMap.put(manager.getShortcut(name, defaultStroke), name);
        actionMap.put(name, action);
        log.debug("Registered shortcut: " + name + " as " + manager.getShortcut(name, defaultStroke));
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
                    if (Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE).equals("true")) {
                        this.setWarning("Warning: An image has mismatch between Comment and Barcode.");
                        log.debug("Setting: Warning: Image has mismatch between Comment " + "and Barcode.");
                    }
                }
            }
        }
        if (higherGeogNotFoundWarning.length() > 0) {
            this.setWarning(higherGeogNotFoundWarning.toString());
        }
    }

    public void setStatus(String status) {
        log.info("Setting status to: ".concat(status));
        jTextFieldStatus.setText(status);
        jTextFieldStatus.setForeground(Color.BLACK);
    }

    private boolean save() {
        if (!specimen.isEditable(Singleton.getSingletonInstance().getUser())) {
            JOptionPane.showMessageDialog(thisPane, "This Specimen cannot be edited. No edit will be saved.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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

            if (cbTypeStatus.getSelectedIndex() == -1 && cbTypeStatus.getSelectedItem() == null) {
                specimen.setTypeStatus(Specimen.STATUS_NOT_A_TYPE);
            } else {
                specimen.setTypeStatus((String) cbTypeStatus.getSelectedItem());
            }
            specimen.setMicrohabitat(textFieldMicrohabitat.getText());

            if (jComboBoxLocationInCollection.getSelectedItem() != null) {
                specimen.setLocationInCollection(jComboBoxLocationInCollection.getSelectedItem().toString());
            }

            specimen.setDrawerNumber(jTextFieldDrawerNumber.getText());
            if (jComboBoxHigherOrder.getSelectedIndex() == -1 && jComboBoxHigherOrder.getSelectedItem() == null) {
                specimen.setOrder("");
            } else {
                specimen.setOrder(jComboBoxHigherOrder.getSelectedItem().toString());
                log.debug("Set order to " + jComboBoxHigherOrder.getSelectedItem().toString());
            }
            if (jComboBoxFamily.getSelectedIndex() == -1 && jComboBoxFamily.getSelectedItem() == null) {
                specimen.setFamily("");
            } else {
                specimen.setFamily(jComboBoxFamily.getSelectedItem().toString());
            }
            if (jComboBoxSubfamily.getSelectedIndex() == -1 && jComboBoxSubfamily.getSelectedItem() == null) {
                specimen.setSubfamily("");
            } else {
                specimen.setSubfamily(jComboBoxSubfamily.getSelectedItem().toString());
            }
            specimen.setTribe(jTextFieldTribe.getText());
            specimen.setGenus(jTextFieldGenus.getText());
            specimen.setSpecificEpithet(jTextFieldSpecies.getText());
            specimen.setSubspecificEpithet(jTextFieldSubspecies.getText());
            specimen.setInfraspecificEpithet(jTextFieldInfraspecificEpithet.getText());
            specimen.setInfraspecificRank(jTextFieldInfraspecificRank.getText());
            specimen.setAuthorship(jTextFieldAuthorship.getText());
            // TODO: handle the collectors set!

            // this returns TRUE for the copied item!!
            log.debug("in save(). specimen numbers size: " + specimen.getNumbers().size());
            log.debug("okok in save(), specimenid is " + specimen.getSpecimenId());

            if (previousSpecimen != null && previousSpecimen.getNumbers() != null) {
                log.debug("in save(). prev specimen numbers size: " + previousSpecimen.getNumbers().size());
                // specimen.setNumbers(previousSpecimen.getNumbers()); - this gives
                // hibernate exceptions here too!
                log.debug("okok in save(), prev specimenid is " + previousSpecimen.getSpecimenId());
            }

            specimen.setIdentifiedBy((String) jCBDeterminer.getSelectedItem());

            specimen.setDateIdentified(jTextFieldDateDetermined.getText());
            specimen.setIdentificationRemarks(jTextFieldIdRemarks.getText());
            if (jComboBoxNatureOfId.getSelectedIndex() == -1 && jComboBoxNatureOfId.getSelectedItem() == null) {
                // specimen.setNatureOfId(NatureOfId.LEGACY);
                specimen.setNatureOfId(NatureOfId.EXPERT_ID);
            } else {
                specimen.setNatureOfId((String) jComboBoxNatureOfId.getSelectedItem());
            }

            specimen.setUnNamedForm(jTextFieldUnnamedForm.getText());
            specimen.setVerbatimLocality(jTextFieldVerbatimLocality.getText());
            specimen.setCountry((String) jComboBoxCountry.getSelectedItem());
            specimen.setValidDistributionFlag(jCheckBoxValidDistributionFlag.isSelected());
            specimen.setPrimaryDivison((String) jComboBoxPrimaryDivision.getSelectedItem());
            specimen.setSpecificLocality(jTextFieldLocality.getText());

            // Elevations
            Long min_elev;
            if (jTextFieldMinElevation.getText().trim().isEmpty()) {
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
            if (jTextFieldMaxElevation.getText().trim().isEmpty()) {
                max_elev = null;
            } else {
                try {
                    max_elev = Long.parseLong(jTextFieldMaxElevation.getText());
                } catch (NumberFormatException e) {
                    max_elev = null;
                }
            }
            specimen.setMaximum_elevation(max_elev);
            if (this.comboBoxElevUnits.getSelectedIndex() == -1 && comboBoxElevUnits.getSelectedItem() == null) {
                specimen.setElev_units("");
            } else {
                specimen.setElev_units(comboBoxElevUnits.getSelectedItem().toString());
            }

            specimen.setCollectingMethod(jTextFieldCollectingMethod.getText());
            specimen.setIsoDate(jTextFieldISODate.getText());
            specimen.setDateNos(jTextFieldDateNos.getText());
            specimen.setDateCollected(jTextFieldDateCollected.getText());
            specimen.setDateEmerged(jTextFieldDateEmerged.getText());
            specimen.setDateCollectedIndicator(jTextFieldDateCollectedIndicator.getText());
            specimen.setDateEmergedIndicator(jTextFieldDateEmergedIndicator.getText());
            if (jComboBoxCollection.getSelectedIndex() == -1 && jComboBoxCollection.getSelectedItem() == null) {
                specimen.setCollection("");
            } else {
                specimen.setCollection(jComboBoxCollection.getSelectedItem().toString());
            }
            if (jComboBoxFeatures.getSelectedIndex() == -1 && jComboBoxFeatures.getSelectedItem() == null) {
                specimen.setFeatures("");
            } else {
                specimen.setFeatures(jComboBoxFeatures.getSelectedItem().toString());
            }
            if (jComboBoxLifeStage.getSelectedIndex() == -1 && jComboBoxLifeStage.getSelectedItem() == null) {
                specimen.setLifeStage("");
            } else {
                specimen.setLifeStage(jComboBoxLifeStage.getSelectedItem().toString());
            }
            if (jComboBoxSex.getSelectedIndex() == -1 && jComboBoxSex.getSelectedItem() == null) {
                specimen.setSex("");
            } else {
                specimen.setSex(jComboBoxSex.getSelectedItem().toString());
                log.debug("jComboBoxSex selectedIndex=" + jComboBoxSex.getSelectedIndex());
            }

            log.debug("sex=" + specimen.getSex());

            // specimen.setPreparationType(jTextFieldPreparationType.getText());
            specimen.setAssociatedTaxon(jTextFieldAssociatedTaxon.getText());
            specimen.setHabitat(jTextFieldHabitat.getText());
            specimen.setMicrohabitat(textFieldMicrohabitat.getText());
            specimen.setSpecimenNotes(jTextAreaSpecimenNotes.getText());
            specimen.setInferences(jTextFieldInferences.getText());
            specimen.setHigherOrder(jComboBoxHigherOrder.getSelectedItem().toString());
            specimen.setGBIFTaxonId(jTextFieldGBIFTaxonId.getText());
            specimen.setLastUpdatedBy(Singleton.getSingletonInstance().getUserFullName());
            specimen.setDateLastUpdated(new Date());
            specimen.setWorkFlowStatus(Objects.requireNonNull(jComboBoxWorkflowStatus.getSelectedItem()).toString());

            specimen.setQuestions(jTextFieldQuestions.getText());
            try {
                // make sure specimen controller knows about the latest changes
                specimenController.setSpecimen(specimen);
                specimenController.save(); // save the record
                setStateToClean();         // enable the navigation buttons
                this.setStatus("Saved");   // inform the user
                jTextFieldStatus.setForeground(Color.BLACK);
                setWarnings();
                jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
                jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString());
            } catch (SaveFailedException e) {
                setStateToDirty(); // disable the navigation buttons
                this.setWarning("Error: " + e.getMessage());
                return false;
            }
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            Singleton.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount(", "));
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
            thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
        jComboBoxPrimaryDivision.setSelectedItem(previousSpecimen.getPrimaryDivison());
        // Elevations
        try {
            jTextFieldMinElevation.setText(Long.toString(previousSpecimen.getMinimum_elevation()));
        } catch (Exception e) {
            jTextFieldMinElevation.setText("");
        }
        try {
            jTextFieldMaxElevation.setText(Long.toString(previousSpecimen.getMaximum_elevation()));
        } catch (Exception e) {
            jTextFieldMaxElevation.setText("");
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
        jTextFieldDateCollectedIndicator.setText(previousSpecimen.getDateCollectedIndicator());
        jTextFieldDateEmergedIndicator.setText(previousSpecimen.getDateEmergedIndicator());
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
        Set<SpecimenPart> newParts = followingParts.stream().filter(part -> !specimen.getSpecimenParts().contains(part)).collect(Collectors.toSet());
        log.debug("Collectors: {}, {}, {}", followingParts, newParts, specimen.getSpecimenParts());
        specimen.getSpecimenParts().removeIf(part -> !followingParts.contains(part));
        for (SpecimenPart specimenPart : newParts) {
            SpecimenPart part = (SpecimenPart) specimenPart.clone();
            part.setSpecimen(specimen);
            specimen.getSpecimenParts().add(part);
        }
        jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
        this.setupSpecimenPartsJTableRenderer();

        //+collectors
        // trying not to remove & add one of same name to counter sql constraints
        Set<Collector> followingCollectors = previousSpecimen.getCollectors();
        // the collectors to be added:
        Set<Collector> newCollectors = followingCollectors.stream().filter(collector -> !specimen.getCollectors().contains(collector)).collect(Collectors.toSet());
        // the collectors to be removed:
        specimen.getCollectors().removeIf(collector -> !followingCollectors.contains(collector));
        for (Collector collector : newCollectors) {
            Collector c = (Collector) collector.clone();
            c.setSpecimen(specimen);
            specimen.getCollectors().add(c);
        }
        jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));
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
        reloadGeoRefFieldValues();

        // new - verbatim locality
        jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality());
        // new - publications
        //        jTextFieldCitedInPub.setText(previousSpecimen.getCitedInPublication());
        specimen.setCitedInPublicationComment(previousSpecimen.getCitedInPublicationComment());
        specimen.setCitedInPublicationLink(previousSpecimen.getCitedInPublicationLink());
        specimen.setCitedInPublication(previousSpecimen.getCitedInPublication());
        // new - features
        jComboBoxFeatures.setSelectedItem(previousSpecimen.getFeatures());
        // new - collecting method
        jTextFieldCollectingMethod.setText(previousSpecimen.getCollectingMethod());

        updateContentDependentLabels();
        thisPane.setStatus("Pasted specimen with id " + thisPane.previousSpecimen.getSpecimenId());
    }

    /**
     * Set the values of the fields to the ones of the specimen
     * TODO: refactor to unused: move to instantiation of fields, e.g.
     */
    private void setValues() {
        log.debug("okok setting values, specimenid is " + specimen.getSpecimenId());
        this.setStatus("Setting values");
        getBarcodeJTextField().setText(specimen.getBarcode());

        // set to value from properties
        // jComboBoxLocationInCollection.setSelectedItem(specimen.getLocationInCollection());
        String locationInCollectionPropertiesVal = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_DISPLAY_COLLECTION);
        getLocationInCollectionJComboBox().setSelectedItem(locationInCollectionPropertiesVal);

        reloadGeoRefFieldValues();

        getCbTypeStatus().setSelectedItem(specimen.getTypeStatus());
        getDrawerNumberJTextField().setText(specimen.getDrawerNumber());
        getOrderJTextField().setSelectedItem(specimen.getHigherOrder());
        log.debug("Selected order to '" + specimen.getOrder() + "'");
        getFamilyJTextField().setSelectedItem(specimen.getFamily());
        getJTextFieldSubfamily().setSelectedItem(specimen.getSubfamily());
        getJTextFieldTribe().setText(specimen.getTribe());
        getGenusJTextField().setText(specimen.getGenus());
        getSpecificEpithetJTextField().setText(specimen.getSpecificEpithet());
        getSubspecifcEpithetJTextField().setText(specimen.getSubspecificEpithet());
        getJTextFieldInfraspecificName().setText(specimen.getInfraspecificEpithet());
        getJTextFieldInfraspecificRank().setText(specimen.getInfraspecificRank());
        getJTextFieldAuthorship().setText(specimen.getAuthorship());
        getTextFieldMicrohabitat().setText(specimen.getMicrohabitat());

        getJTextFieldIdRemarks().setText(specimen.getIdentificationRemarks());
        getJTextFieldDateDetermined().setText(specimen.getDateIdentified());

        // log.debug("jComboBoxLifeStage here!!! specimen life stage is " +
        // specimen.getLifeStage());
        if (specimen.getLifeStage() == null || specimen.getLifeStage().equals("")) {
            specimen.setLifeStage("adult");
            getJComboBoxLifeStage().setSelectedIndex(0);
        }

        // jCBDeterminer.setText(specimen.getIdentifiedBy());
        getJCBDeterminer().setSelectedItem(specimen.getIdentifiedBy());

        getJComboBoxNatureOfId().setSelectedItem(specimen.getNatureOfId());

        getJTextFieldUnnamedForm().setText(specimen.getUnNamedForm());
        getVerbatimLocalityJTextField().setText(specimen.getVerbatimLocality());
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
        getCountryJTextField().setSelectedItem(specimen.getCountry());
        if (specimen.getValidDistributionFlag() != null) {
            getValidDistributionJCheckBox().setSelected(specimen.getValidDistributionFlag());
        } else {
            getValidDistributionJCheckBox().setSelected(false);
        }
        getPrimaryDivisionJTextField().setSelectedItem(specimen.getPrimaryDivison());
        getSpecificLocalityJTextField().setText(specimen.getSpecificLocality());

        // Elevations
        // **********************************************************************
        try {
            jTextFieldMinElevation.setText(Long.toString(specimen.getMinimum_elevation()));
        } catch (Exception e) {
            jTextFieldMinElevation.setText("");
        }
        try {
            jTextFieldMaxElevation.setText(Long.toString(specimen.getMaximum_elevation()));
        } catch (Exception e) {
            jTextFieldMaxElevation.setText("");
        }
        if (specimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(specimen.getElev_units());
        } else {
            comboBoxElevUnits.setSelectedItem("");
        }

        getJTextFieldCollectingMethod().setText(specimen.getCollectingMethod());
        getJTextFieldISODate().setText(specimen.getIsoDate());
        getJTextFieldVerbatimDate().setText(specimen.getDateNos());
        getJTextFieldDateCollected().setText(specimen.getDateCollected());
        getJTextFieldDateCollectedIndicator().setText(specimen.getDateCollectedIndicator());
        getJTextFieldDateEmerged().setText(specimen.getDateEmerged());
        getJTextFieldDateEmergedIndicator().setText(specimen.getDateEmergedIndicator());
        getJTextFieldCollection().setSelectedItem(specimen.getCollection());
        // jTextFieldPreparationType.setText(specimen.getPreparationType());
        getAssociatedTaxonJTextField().setText(specimen.getAssociatedTaxon());
        getJTextFieldHabitat().setText(specimen.getHabitat());
        getTextFieldMicrohabitat().setText(specimen.getMicrohabitat());
        getJTextAreaNotes().setText(specimen.getSpecimenNotes());
        getJComboBoxFeatures().setSelectedItem(specimen.getFeatures());
        getJComboBoxLifeStage().setSelectedItem(specimen.getLifeStage());
        getJComboBoxSex().setSelectedItem(specimen.getSex());
        //        getCitedInPublicationJTextField().setText(specimen.getCitedInPublication());
        getQuestionsJTextField().setText(specimen.getQuestions());
        getJComboBoxWorkflowStatus().setSelectedItem(specimen.getWorkFlowStatus());
        if (specimen.isExported()) {
            getJTextFieldMigrationStatus().setText(WorkFlowStatus.STAGE_DONE);
            //                    "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" +
            //                            specimen.getCatNum());
        } else {
            getJTextFieldMigrationStatus().setText("");
        }
        getJTextFieldInferences().setText(specimen.getInferences());
        getCreatorJTextField().setText(specimen.getCreatedBy());
        if (specimen.getDateCreated() != null) {
            getDateCreatedJTextField().setText(specimen.getDateCreated().toString());
        }
        getLastUpdatedByJTextField().setText(specimen.getLastUpdatedBy());
        if (specimen.getDateLastUpdated() != null) {
            getJTextFieldDateUpdated().setText(specimen.getDateLastUpdated().toString());
        }

        // allie change
        if (specimen.getNatureOfId() == null || specimen.getNatureOfId().equals("")) {
            specimen.setLifeStage("expert ID");
            getJComboBoxNatureOfId().setSelectedIndex(0);
        }

        // without this, it does save the 1st record, and it does not copy the next
        // record!
        log.debug("setValues calling jTableNumbers.setModel(new " + "NumberTableModel(specimen.getNumbers()));");
        getNumberJTable().setModel(new NumberTableModel(specimen.getNumbers()));
        this.setupNumberJTableRenderer();

        getJTableCollectors().setModel(new CollectorTableModel(specimen.getCollectors()));
        this.setupCollectorJTableRenderer();

        getJTableSpecimenParts().setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
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
        getDetsJButton().setText(count + " Det" + detSuffix + ".");
        getDetsJButton().updateUI();
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
            // row
            this.addBasicJLabel(jPanel, "Barcode");
            jPanel.add(this.getBarcodeJTextField(), "grow");
            this.addBasicJLabel(jPanel, "ID by");
            jPanel.add(this.getJCBDeterminer());
            // section: identification/determination
            // row
            this.addBasicJLabel(jPanel, "Nature of ID");
            jPanel.add(this.getJComboBoxNatureOfId());
            this.addBasicJLabel(jPanel, "ID Date");
            jPanel.add(this.getJTextFieldDateDetermined(), "grow, span 1, split 2, sizegroup datedet");
            jPanel.add(this.getDetsJButton(), "sizegroup datedet");
            // section: family, classification
            // row
            this.addBasicJLabel(jPanel, "Order");
            jPanel.add(this.getOrderJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Family");
            jPanel.add(this.getFamilyJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Subfamily");
            jPanel.add(this.getJTextFieldSubfamily(), "grow");
            this.addBasicJLabel(jPanel, "Tribe");
            jPanel.add(this.getJTextFieldTribe(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Genus");
            jPanel.add(this.getGenusJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Species");
            jPanel.add(this.getSpecificEpithetJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Subspecies");
            jPanel.add(this.getSubspecifcEpithetJTextField(), "grow");
            this.addBasicJLabel(jPanel, "Infrasubspecific Name");
            jPanel.add(this.getJTextFieldInfraspecificName(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Infrasubspecific Rank");
            jPanel.add(this.getJTextFieldInfraspecificRank(), "grow");
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
            // row
            jPanel.add(this.getJButtonSpecificLocality(), "align label");
            jPanel.add(this.getSpecificLocalityJTextField(), "grow, span 2");
            // row
            this.addBasicJLabel(jPanel, "Latitude");
            jPanel.add(this.getTextFieldDecimalLat(), "grow");
            this.addBasicJLabel(jPanel, "Longitude");
            jPanel.add(this.getTextFieldDecimalLong(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Method");
            jPanel.add(this.getMethodComboBox());
            this.addBasicJLabel(jPanel, "Datum");
            jPanel.add(this.getDatumComboBox());
            // row
            this.addBasicJLabel(jPanel, "Error Radius");
            jPanel.add(this.getTxtErrorRadius(), "span 1, split 2, sizegroup errorradius, grow");
            jPanel.add(this.getErrorUnitComboBox(), "sizegroup errorradius");
            jPanel.add(this.getJButtonGeoreference());
            jPanel.add(this.getJButtonPasteExcel());

            // row
            this.addBasicJLabel(jPanel, "Elevation from");
            jPanel.add(this.getVerbatimElevationJTextField(), "grow");
            this.addBasicJLabel(jPanel, "to");
            jPanel.add(this.getjTextFieldMaxElevation(), "grow, span 1, split 2, sizegroup elevation");
            jPanel.add(this.getComboBoxElevUnits(), "sizegroup elevation");
            // section: collection
            // row
            this.addBasicJLabel(jPanel, "Author");
            jPanel.add(this.getJTextFieldAuthorship(), "grow");
            this.addBasicJLabel(jPanel, "Collection");
            jPanel.add(this.getJTextFieldCollection(), "grow"); // "span 3"
            // double row:
            this.addBasicJLabel(jPanel, "Collectors");
            jPanel.add(this.getJScrollPaneCollectors(), "span 2 2, grow");
            this.addBasicJLabel(jPanel, "Collecting Method");
            jPanel.add(this.getJButtonCollectorAdd(), "right");
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
            //            this.addBasicJLabel(jPanel, "Features");
            //            jPanel.add(this.getJComboBoxFeatures(), "wrap");
            // double row:
            this.addBasicJLabel(jPanel, "Specimen Parts");
            jPanel.add(this.getJScrollPaneSpecimenParts(), "span 3 2, grow");
            jPanel.add(this.getJButtonAddPrep(), "right");
            // row
            this.addBasicJLabel(jPanel, "Publications");
            jPanel.add(this.getCitedInPublicationButton(), "grow");
            this.addBasicJLabel(jPanel, "Associated Taxon");
            jPanel.add(this.getAssociatedTaxonJTextField(), "grow");
            // row
            this.addBasicJLabel(jPanel, "Specimen Notes");
            jPanel.add(this.getJScrollPaneNotes(), "span 3, grow");
            // double row
            this.addBasicJLabel(jPanel, "Numbers & more");
            jPanel.add(this.getNumbersJScrollPane(), "span 3 2, grow");
            jPanel.add(this.getJButtonNumbersAdd(), "right");
            // section: other fields
            // row
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
            // row
            jPanel.add(this.getAccordionDetailsPanel(), "grow, span 4");
            // section: controls
            int splitSize = copyPasteActivated ? 6 : 4;
            if (this.supportsLinkToNahima()) {
                splitSize += 1;
            }
            jPanel.add(this.getJButtonHistory(), "span, split " + splitSize); //, "span, split 4");//, "sizegroup
            if (this.supportsLinkToNahima()) {
                jPanel.add(this.getLinkToNahima());
            }
            if (copyPasteActivated) {
                jPanel.add(this.getJButtonPaste()); //, sizegroup controls");
            }
            // controls");
            jPanel.add(this.getJButtonPrevious(), "tag back");
            jPanel.add(this.getJButtonNext(), "tag next");
            if (copyPasteActivated) {
                jPanel.add(this.getJButtonCopySave(), "tag apply"); //, "sizegroup controls");
            }
            jPanel.add(this.getSaveJButton(), "tag apply"); //, "sizegroup controls");
            //            jPanel.add(this.getSaveNextJButton(), "tag next, tag
            //            apply");
        }
        return jPanel;
    }

    private JButton getCitedInPublicationButton() {
        if (citedInPublicationButton == null) {
            citedInPublicationButton = new JButton("Manage Publication");
            SpecimenDetailsViewPane self = this;
            citedInPublicationButton.addActionListener(actionEvent -> {
                CitedInDialog dialog = new CitedInDialog(self.specimen.getCitedInPublication(), self.specimen.getCitedInPublicationLink(), self.specimen.getCitedInPublicationComment(), self.specimen.getCitedInPublicationYear());
                dialog.addCloseListener((type, source) -> {
                    if (type == CloseType.OK) {
                        self.specimen.setCitedInPublication(dialog.getCitedInPublication());
                        self.specimen.setCitedInPublicationLink(dialog.getCitedInLink());
                        self.specimen.setCitedInPublicationComment(dialog.getCitedInComment());
                        self.specimen.setCitedInPublicationYear(dialog.getCitedInPublicationYear());
                    }
                });
                dialog.setVisible(true);
            });
        }
        return citedInPublicationButton;
    }

    //    private JButton getSaveNextJButton() {
    //        if (jButtonSaveNext == null) {
    //            jButtonSaveNext = new JButton(WorkFlowStatus.STAGE_1 + ", Save,
    //            Next"); SpecimenDetailsViewPane self = this;
    //            jButtonSaveNext.setEnabled(specimen.isEditable());
    //            jButtonSaveNext.addActionListener(new ActionListener() {
    //                @Override
    //                public void actionPerformed(ActionEvent actionEvent) {
    //                    self.getJComboBoxWorkflowStatus().setSelectedItem(WorkFlowStatus.STAGE_1);
    //                    self.jButtonSave.doClick();
    //                    if (self.isClean()) {
    //                        self.jButtonNext.doClick();
    //                    }
    //                }
    //            });
    //        }
    //        return jButtonSaveNext;
    //    }

    private JButton getJButtonPasteExcel() {
        if (pasteExcelButton == null) {
            pasteExcelButton = new JButton("Paste Excel");
            SpecimenDetailsViewPane self = this;
            pasteExcelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    try {
                        self.getGeoreferenceDialog().pasteFromExcel((String) clipboard.getData(DataFlavor.stringFlavor));
                    } catch (Exception e) {
                        log.error("Failed to paste clipboard data from excel", e);
                    }
                    self.reloadGeoRefFieldValues();
                }
            });
        }
        return pasteExcelButton;
    }

    private JPanel getAccordionDetailsPanel() {
        if (accordionDetailsPanel == null) {
            JPanel accordionContent = new JPanel(new MigLayout("wrap 4, fillx"));

            // row
            this.addBasicJLabel(accordionContent, "Number of Images");
            accordionContent.add(this.getJTextFieldImgCount(), "grow");
            this.addBasicJLabel(accordionContent, "Migration Status");
            accordionContent.add(this.getJTextFieldMigrationStatus(), "grow");
            // row
            this.addBasicJLabel(accordionContent, "Collection", "tag label, right, span 4, split 5, sizegroup idrow");
            accordionContent.add(this.getLocationInCollectionJComboBox(), "sizegroup idrow");
            accordionContent.add(this.getjButtonGBIFView(), "tag label, right, sizegroup idrow");
            accordionContent.add(this.getjTextFieldGBIFTaxonId(), "sizegroup idrow, grow");
            accordionContent.add(this.getDBIdLabel(), "sizegroup idrow");
            // row
            this.addBasicJLabel(accordionContent, "ID Remark");
            accordionContent.add(this.getJTextFieldIdRemarks(), "grow, span 3");

            // row
            this.addBasicJLabel(accordionContent, "Valid Dist.");
            accordionContent.add(this.getValidDistributionJCheckBox());
            this.addBasicJLabel(accordionContent, "Drawer Number");
            accordionContent.add(this.getDrawerNumberJTextField(), "grow");

            accordionDetailsPanel = new JAccordionPanel("Less Details", "More Details", accordionContent);
        }
        return accordionDetailsPanel;
    }

    /**
     * Reset the field values of the geoRef fields (lat, long etc.)
     */
    private void reloadGeoRefFieldValues() {
        Set<LatLong> geoReferences = specimen.getLatLong();
        boolean resetFields = true;
        if (!geoReferences.isEmpty()) {
            LatLong geoReferencePre = geoReferences.iterator().next();
            if (!geoReferencePre.isEmpty()) {
                resetFields = false;
                getTextFieldDecimalLat().setText(geoReferencePre.getDecLatString());
                getTextFieldDecimalLong().setText(geoReferencePre.getDecLongString());
                getMethodComboBox().setSelectedItem(geoReferencePre.getGeorefmethod());
                getDatumComboBox().setSelectedItem(geoReferencePre.getDatum());
                getTxtErrorRadius().setText(geoReferencePre.getMaxErrorDistanceString());
                getErrorUnitComboBox().setSelectedItem(geoReferencePre.getMaxErrorUnits());
            }
        }

        if (resetFields) {
            getTextFieldDecimalLat().setText("");
            getTextFieldDecimalLong().setText("");
            getMethodComboBox().setSelectedItem("");
            getDatumComboBox().setSelectedItem("");
            getTxtErrorRadius().setText("");
            getErrorUnitComboBox().setSelectedItem("");
        }
        this.updateJButtonGeoreference();
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

    /**
     * Add a label to a JPanel
     *
     * @param target
     * @param labelText
     */
    private void addBasicJLabel(JPanel target, String labelText) {
        addBasicJLabel(target, labelText, "tag label, right"); //"align label" was removed as requested
    }

    /**
     * Add a label to a JPanel
     *
     * @param target
     * @param labelText
     * @param constraints
     */
    private void addBasicJLabel(JPanel target, String labelText, String constraints) {
        JLabel label = new JLabel();
        label.setText(labelText.concat(":"));
        target.add(label, constraints);
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
            jTextFieldBarcode.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Barcode"));
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
            jTextFieldGenus = this.getBasicJTextField();
            jTextFieldGenus.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Genus", jTextFieldGenus));
            jTextFieldGenus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Genus"));
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
            jTextFieldSpecies = this.getBasicJTextField();
            jTextFieldSpecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SpecificEpithet", jTextFieldSpecies));
            jTextFieldSpecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SpecificEpithet"));
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
            jTextFieldSubspecies = this.getBasicJTextField();
            jTextFieldSubspecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SubspecificEpithet", jTextFieldSubspecies));
            jTextFieldSubspecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SubspecificEpithet"));
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
            jTextFieldLocality = this.getBasicJTextField();
            jTextFieldLocality.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SpecificLocality", jTextFieldLocality));
            jTextFieldLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SpecificLocality"));
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
            if (!specimen.isEditable()) {
                jButtonSave.setEnabled(false);
                jButtonSave.setText(specimen.getWorkFlowStatus());
            }
            jButtonSave.setMnemonic(KeyEvent.VK_S);
            jButtonSave.setToolTipText("Save changes to this record to the database. No fields should " + "have red backgrounds before you save.");
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
            jComboBoxCollection = new JComboBox<>();
            jComboBoxCollection.setModel(new DefaultComboBoxModel<>());
            // lazily load the collections
            (new Thread(() -> {
                String[] collections = sls.getDistinctCollections();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxCollection.setModel(new DefaultComboBoxModel<>(collections));
                    if (!Arrays.stream(collections).anyMatch(""::equals)) {
                        jComboBoxCollection.addItem("");
                    }
                    jComboBoxCollection.setSelectedItem(specimen.getCollection());
                });
            })).start();
            jComboBoxCollection.setEditable(specimen.isEditable());
            // jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Collection", jComboBoxCollection));
            jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
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
            jTextFieldLastUpdatedBy.setEditable(false);
            jTextFieldLastUpdatedBy.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "LastUpdatedBy"));
            // jTextFieldLastUpdatedBy.setEnabled(false);
            jTextFieldLastUpdatedBy.setForeground(Color.BLACK);
        }
        return jTextFieldLastUpdatedBy;
    }

    private JTextField getTextFieldDecimalLat() {
        if (textFieldDecimalLat == null) {
            textFieldDecimalLat = this.getBasicJTextField();
            textFieldDecimalLat.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    System.out.println("User entered " + textFieldDecimalLat.getText());
                    if (!textFieldDecimalLat.getText().trim().equals("")) {
                        getGeoreferenceDialog().getGeoReference().setDecLat(BigDecimal.valueOf(Double.parseDouble(textFieldDecimalLat.getText())));
                    } else {
                        getGeoreferenceDialog().getGeoReference().setDecLat(null);
                    }
                    getGeoreferenceDialog().loadData();
                }
            });
        }
        return textFieldDecimalLat;
    }

    private JTextField getTextFieldDecimalLong() {
        if (textFieldDecimalLong == null) {
            textFieldDecimalLong = this.getBasicJTextField();
            textFieldDecimalLong.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    System.out.println("User entered " + textFieldDecimalLong.getText());
                    if (!textFieldDecimalLong.getText().trim().equals("")) {
                        getGeoreferenceDialog().getGeoReference().setDecLong(BigDecimal.valueOf(Double.parseDouble(textFieldDecimalLong.getText())));
                    } else {
                        getGeoreferenceDialog().getGeoReference().setDecLong(null);
                    }
                    getGeoreferenceDialog().loadData();
                }
            });
        }
        return textFieldDecimalLong;
    }

    private JComboBox getMethodComboBox() {
        if (cbMethod == null) {
            cbMethod = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"not recorded", "unknown", "GEOLocate", "Geoportal", "Google Earth", "Google Maps", "Gazeteer", "GPS", "Label Data", "Wikipedia", "MaNIS/HertNet/ORNIS Georeferencing Guidelines"}));
            cbMethod.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getGeoreferenceDialog().getGeoReference().setGeorefmethod((String) cbMethod.getSelectedItem());
                    getGeoreferenceDialog().loadData();
                    getGeoreferenceDialog().setState();
                }
            });
        }
        return cbMethod;
    }

    private JComboBox getDatumComboBox() {
        if (cbDatum == null) {
            ComboBoxModel<String> datumModel = new ListComboBoxModel<String>(LatLong.getDatumValues());
            cbDatum = new JComboBox<>(datumModel);
            // set default
            cbDatum.setSelectedItem("WGS84");
            cbDatum.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getGeoreferenceDialog().getGeoReference().setDatum((String) cbDatum.getSelectedItem());
                    getGeoreferenceDialog().loadData();
                }
            });
        }
        return cbDatum;
    }

    private JTextField getTxtErrorRadius() {
        if (txtErrorRadius == null) {
            txtErrorRadius = this.getBasicJTextField();

            txtErrorRadius.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    System.out.println("User entered " + txtErrorRadius.getText());
                    String result = txtErrorRadius.getText();
                    if (!result.trim().equals("")) {
                        getGeoreferenceDialog().getGeoReference().setMaxErrorDistance(Integer.parseInt(result));
                    } else {
                        getGeoreferenceDialog().getGeoReference().setMaxErrorDistance(null);
                    }
                    getGeoreferenceDialog().loadData();
                }
            });
        }
        return txtErrorRadius;
    }

    private JComboBox getErrorUnitComboBox() {
        if (comboBoxErrorUnits == null) {
            comboBoxErrorUnits = new JComboBox<>();
            comboBoxErrorUnits.setModel(new DefaultComboBoxModel<>(new String[]{"m", "ft", "km", "mi", "yd"}));
            // set default
            comboBoxErrorUnits.setSelectedItem("m");

            comboBoxErrorUnits.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getGeoreferenceDialog().getGeoReference().setMaxErrorUnits((String) comboBoxErrorUnits.getSelectedItem());
                    getGeoreferenceDialog().loadData();
                }
            });
        }
        return comboBoxErrorUnits;
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
                jTableCollectors = new JTableWithRowBorder(new CollectorTableModel(this.specimen.getCollectors()));
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
        JComboBox<String> jComboBoxCollector = new JComboBox<>(cls.getDistinctCollectors());
        jComboBoxCollector.setEditable(specimen.isEditable());
        // field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class,
        // "CollectorName", field));
        AutoCompleteDecorator.decorate(jComboBoxCollector);
        jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(jComboBoxCollector));
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
        ((AbstractTableModel) this.getJTableSpecimenParts().getModel()).fireTableDataChanged();
    }

    private JTable getJTableSpecimenParts() {
        if (jTableSpecimenParts == null) {
            try {
                jTableSpecimenParts = new JTableWithRowBorder(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
            } catch (NullPointerException e) {
                jTableSpecimenParts = new JTableWithRowBorder(new SpecimenPartsTableModel());
            }
            jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTableSpecimenParts.setRowHeight(jTableSpecimenParts.getRowHeight() + 5);
            setupSpecimenPartsJTableRenderer();

            log.debug("Debug: Specimen parts size: {}", specimen.getSpecimenParts().size());

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
        getJTableSpecimenParts().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBoxPart));
        JComboBox<String> comboBoxPrep = new JComboBox<>(SpecimenPart.PRESERVATION_NAMES);
        comboBoxPrep.setEditable(specimen.isEditable());
        getJTableSpecimenParts().getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBoxPrep));

        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(ButtonEditor.OPEN_SPECIMENPARTATTRIBUTES, this));
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
            jTextFieldCreator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Creator"));
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
                jTableNumbers = new JTableWithRowBorder(new NumberTableModel(specimen.getNumbers()));
                //                {
                //                    public void changeSelection(final int row, final
                //                    int column, boolean toggle, boolean extend) {
                //                        super.changeSelection(row, column, toggle,
                //                        extend); jTableNumbers.editCellAt(row,
                //                        column); jTableNumbers.transferFocus();
                //                    }
                //                };
                if (!specimen.getNumbers().isEmpty()) {
                    JTableCellTabbing.setTabMapping(jTableNumbers, 0, specimen.getNumbers().size(), 0, 2);
                }
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

            // Enable single click editing
            jTableNumbers.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);

            // Better key handling for editing
            jTableNumbers.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_F2) {
                        int row = jTableNumbers.getSelectedRow();
                        int col = jTableNumbers.getSelectedColumn();
                        if (row >= 0 && col >= 0) {
                            jTableNumbers.editCellAt(row, col);
                            Component editor = jTableNumbers.getEditorComponent();
                            if (editor != null) {
                                editor.requestFocusInWindow();
                            }
                        }
                    }
                }
            });

            // Better mouse handling
            jTableNumbers.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        int row = jTableNumbers.rowAtPoint(e.getPoint());
                        int col = jTableNumbers.columnAtPoint(e.getPoint());
                        if (row >= 0 && col >= 0) {
                            jTableNumbers.changeSelection(row, col, false, false);
                            // Small delay to ensure selection is processed
                            SwingUtilities.invokeLater(() -> {
                                jTableNumbers.editCellAt(row, col);
                                Component editor = jTableNumbers.getEditorComponent();
                                if (editor instanceof JTextField) {
                                    ((JTextField) editor).selectAll();
                                }
                            });
                        }
                    }
                }
            });
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
        JTextField field1 = this.getBasicJTextField();
        field1.setInputVerifier(MetadataRetriever.getInputVerifier(Number.class, "Number", field1));
        field1.setVerifyInputWhenFocusTarget(true);
        jTableNumbers.setColumnSelectionAllowed(true);
        jTableNumbers.setRowSelectionAllowed(true);
        jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_NUMBER).setCellEditor(new ValidatingTableCellEditor(field1));
        // Then, setup the type field
        JComboBox<String> jComboNumberTypes = new JComboBox<>();

        //        (new Thread(() -> {
        String[] types = NumberLifeCycle.getDistinctTypes();
        //            SwingUtilities.invokeLater(() ->
        jComboNumberTypes.setModel(new DefaultComboBoxModel<>(types));
        //            );
        //        })).start();
        jComboNumberTypes.setEditable(specimen.isEditable());
        TableColumn typeColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
        ComboBoxCellEditor comboBoxEditor = new ComboBoxCellEditor(jComboNumberTypes);
        AutoCompleteDecorator.decorate(jComboNumberTypes);
        typeColumn.setCellEditor(new ComboBoxCellEditor(jComboNumberTypes));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for pick list of number types.");
        typeColumn.setCellRenderer(renderer);

        // enable tabbing (does not work yet)
        //        field1.addKeyListener(new KeyAdapter() {
        //            @Override
        //            public void keyPressed(KeyEvent e) {
        //                if (e.getKeyCode() == KeyEvent.VK_TAB) {
        //                    int row = jTableNumbers.getSelectedRow();
        //                    int col = jTableNumbers.getSelectedColumn();
        //                    assert( col == 0);
        //                    jTableNumbers.changeSelection(row, 1, false, false);
        //                    jTableNumbers.editCellAt(row, 1);
        //                    jTableNumbers.transferFocus();
        //                } else {
        //                    super.keyPressed(e);
        //                }
        //            }
        //        });
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
            URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
            try {
                jButtonNumbersAdd.setText("");
                jButtonNumbersAdd.setIcon(new ImageIcon(iconFile));
                jButtonNumbersAdd.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        ((NumberTableModel) jTableNumbers.getModel()).addNumber(new Number(specimen, "", ""));
                        thisPane.setStateToDirty();

                        JTableCellTabbing.setTabMapping(jTableNumbers, 0, jTableNumbers.getRowCount(), 0, 2);
                    }
                });
            } catch (Exception e) {
                jButtonNumbersAdd.setText("+");
            }
        }
        return jButtonNumbersAdd;
    }

    private boolean supportsLinkToNahima() {
        return Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_NAHIMA_URL) != null && specimen.isExported() && specimen.getNahimaId() != null && !Objects.equals(specimen.getNahimaId(), "");
    }

    private JButton getLinkToNahima() {
        if (jButtonNahimaLink == null) {
            jButtonNahimaLink = new JButton();
            jButtonNahimaLink.setText("Open in Nahima");
            jButtonNahimaLink.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String urlString = "";
                    try {
                        Properties properties = Singleton.getSingletonInstance().getProperties().getProperties();

                        urlString = properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL, "https://nahima.ethz.ch");

                        // need to first convert he global object id to the uuid for the link
                        NahimaManager nahimaManager = new NahimaManager(properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL), properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_USER), properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_PASSWORD), false, false);

                        JSONObject nahimaEntry = nahimaManager.findObjectByGlobalObjectId(specimen.getNahimaId());

                        urlString = urlString + "/#/detail/" + nahimaEntry.getString("_uuid");
                    } catch (Exception ex) {
                        log.error("Failed to assemble URL to Nahima", ex);
                    }
                    try {
                        Desktop.getDesktop().browse(new URL(urlString).toURI());
                    } catch (Exception ex) {
                        log.error("Failed opening entry in Nahima", ex);
                    }
                }
            });
        }
        return jButtonNahimaLink;
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
                SpecimenDetailsViewPane self = this;
                jButtonGeoReference.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        thisPane.setStateToDirty();
                        GeoreferenceDialog georefDialog = self.getGeoreferenceDialog();
                        georefDialog.setVisible(true);
                        georefDialog.addCloseListener(new CloseListener() {
                            @Override
                            public void onClose(CloseType type, Component source) {
                                if (type == CloseType.OK) {
                                    autocompleteGeoDataFromGeoreference();
                                }
                            }
                        });
                        // could be included in close listener once it supports close by
                        // "x"
                        georefDialog.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentHidden(ComponentEvent e) {
                                updateJButtonGeoreference();
                                super.componentHidden(e);
                                reloadGeoRefFieldValues();
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

    private GeoreferenceDialog getGeoreferenceDialog() {
        if (this.georeferenceDialog == null) {
            Set<LatLong> georeferences = specimen.getLatLong();
            LatLong georeference = georeferences.iterator().next();
            if (georeference.isEmpty()) {
                // to fix the two different default values
                georeference.setDatum((String) this.cbDatum.getSelectedItem());
            }
            georeference.setSpecimen(specimen);

            this.georeferenceDialog = new GeoreferenceDialog(georeference, thisPane);
        }
        return this.georeferenceDialog;
    }

    public void setLocationData(String verbatimLoc, String specificLoc, String country, String stateProvince, String lat, String lng) {
        log.debug(String.join(" ", verbatimLoc, specificLoc, country, stateProvince, lat, lng));
        Map<Component, String> defaultsMapImmutable = Map.ofEntries(Map.entry(this.getVerbatimLocalityJTextField(), verbatimLoc), Map.entry(this.getSpecificLocalityJTextField(), specificLoc), Map.entry(this.getCountryJTextField(), country), Map.entry(this.getPrimaryDivisionJTextField(), stateProvince), Map.entry(this.getTextFieldDecimalLat(), lat), Map.entry(this.getTextFieldDecimalLong(), lng));

        Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
        defaultsMapImmutable.forEach((field, value) -> {
            try {
                if (field instanceof JTextField) {
                    if (((JTextField) field).getText().trim().equals("") || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
                        ((JTextField) field).setText(value);
                    }
                } else if (field instanceof JComboBox) {
                    String content = "";
                    if (((JComboBox<?>) field).getSelectedItem() != null) {
                        content = ((JComboBox<?>) field).getSelectedItem().toString();
                    }
                    if (content.trim().equals("") || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
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
        if (specimen.getLatLong() != null && !specimen.getLatLong().isEmpty() && !specimen.getLatLong().iterator().next().isEmpty()) {
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
            URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
            try {
                jButtonCollectorAdd.setText("");
                jButtonCollectorAdd.setIcon(new ImageIcon(iconFile));
                jButtonCollectorAdd.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        log.debug("adding a new collector........");
                        ((CollectorTableModel) jTableCollectors.getModel()).addCollector(new Collector(specimen, ""));
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
            jTextFieldDrawerNumber = this.getBasicJTextField();
            jTextFieldDrawerNumber.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DrawerNumber", jTextFieldDrawerNumber));
            jTextFieldDrawerNumber.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DrawerNumber"));
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
            jTextFieldVerbatimLocality = this.getBasicJTextField();
            jTextFieldVerbatimLocality.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "VerbatimLocality", jTextFieldVerbatimLocality));
            jTextFieldVerbatimLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimLocality"));
        }
        return jTextFieldVerbatimLocality;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getCountryJTextField() {
        if (jComboBoxCountry == null) {
            log.debug("init jComboBoxCountry");
            jComboBoxCountry = new JComboBox<>();
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] countries = sls.getDistinctCountries();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxCountry.setModel(new DefaultComboBoxModel<>(countries));
                    jComboBoxCountry.setSelectedItem(specimen.getCountry());
                });
            })).start();
            //            jComboBoxCountry.setInputVerifier(
            //                    MetadataRetriever.getInputVerifier(Specimen.class,
            //                            "Country", jComboBoxCountry));
            jComboBoxCountry.setEditable(specimen.isEditable());
            jComboBoxCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
            jComboBoxCountry.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxCountry);
        }
        return jComboBoxCountry;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getPrimaryDivisionJTextField() {
        if (jComboBoxPrimaryDivision == null) {
            jComboBoxPrimaryDivision = new JComboBox<>();
            jComboBoxPrimaryDivision.setEditable(specimen.isEditable());
            // jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "primaryDivison", jTextFieldPrimaryDivision));
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] primaryDivisions = sls.getDistinctPrimaryDivisions();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxPrimaryDivision.setModel(new DefaultComboBoxModel<>(primaryDivisions));
                    jComboBoxPrimaryDivision.setSelectedItem(specimen.getPrimaryDivison());
                });
            })).start();
            jComboBoxPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "primaryDivison"));
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
    private JComboBox<String> getOrderJTextField() {
        if (jComboBoxHigherOrder == null) {
            jComboBoxHigherOrder = new JComboBox<>();

            (new Thread(() -> {
                String[] orders = HigherTaxonLifeCycle.selectDistinctOrder();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxHigherOrder.setModel(new DefaultComboBoxModel<>(orders));
                    if (!Arrays.stream(orders).anyMatch(""::equals)) {
                        jComboBoxHigherOrder.addItem("");
                    }
                    jComboBoxHigherOrder.setSelectedItem(specimen.getHigherOrder());
                });
            })).start();
            jComboBoxHigherOrder.setEditable(specimen.isEditable());
            // jComboBoxHigherOrder.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Order", jComboBoxHigherOrder));
            jComboBoxHigherOrder.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "HigherOrder"));
            jComboBoxHigherOrder.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxHigherOrder);
        }
        return jComboBoxHigherOrder;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JComboBox<String> getFamilyJTextField() {
        if (jComboBoxFamily == null) {
            jComboBoxFamily = new JComboBox<>();
            jComboBoxFamily.setModel(new DefaultComboBoxModel<>(HigherTaxonLifeCycle.selectDistinctFamily()));
            jComboBoxFamily.setEditable(specimen.isEditable());
            // jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Family", jTextFieldFamily));
            jComboBoxFamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Family"));
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
            jComboBoxSubfamily = new JComboBox<>();
            jComboBoxSubfamily.setModel(new DefaultComboBoxModel<>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
            jComboBoxSubfamily.setEditable(specimen.isEditable());
            // jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Subfamily", jTextFieldSubfamily));
            jComboBoxSubfamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Subfamily"));
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
            jTextFieldTribe = this.getBasicJTextField();
            jTextFieldTribe.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Tribe", jTextFieldTribe));
            jTextFieldTribe.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Tribe"));
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
            jComboBoxSex = new JComboBox<>();
            jComboBoxSex.setModel(new DefaultComboBoxModel<>(Sex.getSexValues()));
            jComboBoxSex.setEditable(specimen.isEditable());
            jComboBoxSex.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Sex"));
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
            jComboBoxFeatures = new JComboBox<>();
            jComboBoxFeatures.setModel(new DefaultComboBoxModel<>(Features.getFeaturesValues()));
            jComboBoxFeatures.setEditable(specimen.isEditable());
            jComboBoxFeatures.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Features"));
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
            jComboBoxNatureOfId = new JComboBox<>();
            jComboBoxNatureOfId.setModel(new DefaultComboBoxModel<>(NatureOfId.getNatureOfIdValues()));
            jComboBoxNatureOfId.setEditable(specimen.isEditable());
            jComboBoxNatureOfId.setToolTipText(MetadataRetriever.getFieldHelp(Determination.class, "NatureOfId"));
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
            jComboBoxLifeStage = new JComboBox<>();
            jComboBoxLifeStage.setModel(new DefaultComboBoxModel<>(LifeStage.getLifeStageValues()));
            jComboBoxLifeStage.setEditable(specimen.isEditable());
            jComboBoxLifeStage.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Lifestage"));
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
            jTextFieldDateNos = this.getBasicJTextField();
            // jTextFieldDateNos.setToolTipText("Date found on labels where date might
            // be either date collected or date emerged, or some other date");
            jTextFieldDateNos.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateNOS", jTextFieldDateNos));
            jTextFieldDateNos.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateNOS"));
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
            jTextFieldDateEmerged = this.getBasicJTextField();
            jTextFieldDateEmerged.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateEmerged", jTextFieldDateEmerged));
            jTextFieldDateEmerged.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateEmerged"));
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
            jTextFieldDateEmergedIndicator = this.getBasicJTextField();
            jTextFieldDateEmergedIndicator.setToolTipText("Verbatim text indicating that this is a date emerged.");
            jTextFieldDateEmergedIndicator.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateEmergedIndicator", jTextFieldDateEmergedIndicator));
            jTextFieldDateEmergedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateEmergedIndicator"));
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
            jTextFieldDateCollected = this.getBasicJTextField();
            jTextFieldDateCollected.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateCollected"));
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
            jTextFieldDateCollectedIndicator = this.getBasicJTextField();
            jTextFieldDateCollectedIndicator.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateCollectedIndicator", jTextFieldDateCollectedIndicator));
            jTextFieldDateCollectedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateCollectedIndicator"));
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
            jTextFieldInfraspecificEpithet = this.getBasicJTextField();
            jTextFieldInfraspecificEpithet.setEditable(specimen.isEditable());
            jTextFieldInfraspecificEpithet.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "InfraspecificEpithet", jTextFieldInfraspecificEpithet));
            jTextFieldInfraspecificEpithet.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "InfraspecificEpithet"));
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
            jTextFieldInfraspecificRank = this.getBasicJTextField();
            jTextFieldInfraspecificRank.setEditable(specimen.isEditable());
            jTextFieldInfraspecificRank.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "InfraspecificRank", jTextFieldInfraspecificRank));
            jTextFieldInfraspecificRank.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "InfraspecificRank"));
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
            jTextFieldAuthorship = this.getBasicJTextField();
            jTextFieldAuthorship.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Authorship", jTextFieldAuthorship));
            jTextFieldAuthorship.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Authorship"));
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
            jTextFieldUnnamedForm = this.getBasicJTextField();
            jTextFieldUnnamedForm.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "UnnamedForm", jTextFieldUnnamedForm));
            jTextFieldUnnamedForm.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "UnnamedForm"));
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
            jTextFieldMinElevation = this.getBasicJTextField();
            jTextFieldMinElevation.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    if (jTextFieldMinElevation.getText().equals("")) {
                        return true;
                    }
                    try {
                        double val1 = Double.parseDouble(jTextFieldMinElevation.getText());
                        if (jTextFieldMaxElevation.getText().equals("")) {
                            return true;
                        }
                        double val2 = Double.parseDouble(jTextFieldMaxElevation.getText());
                        return val1 <= val2;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            });
            jTextFieldMinElevation.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimElevation"));
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
            jTextFieldCollectingMethod = this.getBasicJTextField();
            jTextFieldCollectingMethod.setEditable(specimen.isEditable());
            jTextFieldCollectingMethod.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "CollectingMethod", jTextFieldCollectingMethod));
            jTextFieldCollectingMethod.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "CollectingMethod"));
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
            jTextAreaSpecimenNotes.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SpecimenNotes"));
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
            jCheckBoxValidDistributionFlag.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "ValidDistributionFlag"));
            jCheckBoxValidDistributionFlag.addKeyListener(new java.awt.event.KeyAdapter() {
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
            jTextFieldQuestions = this.getBasicJTextField();
            jTextFieldQuestions.setBackground(MainFrame.BG_COLOR_QC_FIELD);
            jTextFieldQuestions.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Questions", jTextFieldQuestions));
            jTextFieldQuestions.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Questions"));
        }
        return jTextFieldQuestions;
    }


    /**
     * This method initializes the button to add a new preparation type.
     *
     * @return javax.swing.JTextField
     */
    private JButton getJButtonAddPrep() {
        if (jButtonAddPreparationType == null) {
            jButtonAddPreparationType = new JButton("Add Prep");
            jButtonAddPreparationType.setMnemonic(KeyEvent.VK_A);

            jButtonAddPreparationType.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    log.debug("Adding new SpecimenPart");
                    SpecimenPart newPart = new SpecimenPart();
                    newPart.setPreserveMethod(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_DEFAULT_PREPARATION));
                    newPart.setSpecimen(specimen);
                    SpecimenPartLifeCycle spls = new SpecimenPartLifeCycle();
                    log.debug("Attaching new SpecimenPart");
                    try {
                        spls.persist(newPart);
                        specimen.getSpecimenParts().add(newPart);
                        ((AbstractTableModel) jTableSpecimenParts.getModel()).fireTableDataChanged();
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
     * This method initializes the associated taxon text field.
     *
     * @return javax.swing.JTextField
     */
    private JTextField getAssociatedTaxonJTextField() {
        if (jTextFieldAssociatedTaxon == null) {
            jTextFieldAssociatedTaxon = this.getBasicJTextField();
            jTextFieldAssociatedTaxon.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "AssociatedTaxon", jTextFieldAssociatedTaxon));
            jTextFieldAssociatedTaxon.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "AssociatedTaxon"));
        }
        return jTextFieldAssociatedTaxon;
    }

    /**
     * This method initializes the habitat text field.
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldHabitat() {
        if (jTextFieldHabitat == null) {
            jTextFieldHabitat = this.getBasicJTextField();
            jTextFieldHabitat.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Habitat", jTextFieldHabitat));
            jTextFieldHabitat.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Habitat"));
        }
        return jTextFieldHabitat;
    }

    /**
     * This method initializes the workflow status combo box.
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getJComboBoxWorkflowStatus() {
        if (jComboBoxWorkflowStatus == null) {
            jComboBoxWorkflowStatus = new JComboBox<>();
            jComboBoxWorkflowStatus.setModel(new DefaultComboBoxModel<>(WorkFlowStatus.getWorkFlowStatusValues()));
            jComboBoxWorkflowStatus.setEditable(false);
            jComboBoxWorkflowStatus.setBackground(MainFrame.BG_COLOR_QC_FIELD);
            jComboBoxWorkflowStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "WorkflowStatus"));
            AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus);
        }
        return jComboBoxWorkflowStatus;
    }

    /**
     * This method initializes the location in collection combo box.
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getLocationInCollectionJComboBox() {
        if (jComboBoxLocationInCollection == null) {
            jComboBoxLocationInCollection = new JComboBox<>();
            jComboBoxLocationInCollection.setModel(new DefaultComboBoxModel<>(LocationInCollection.getLocationInCollectionValues()));
            jComboBoxLocationInCollection.setEditable(false);
            jComboBoxLocationInCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "LocationInCollection"));

            // alliefix - set default from properties file
            // jComboBoxLocationInCollection.setSelectedIndex(1);

            jComboBoxLocationInCollection.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            AutoCompleteDecorator.decorate(jComboBoxLocationInCollection);
        }
        return jComboBoxLocationInCollection;
    }

    /**
     * This method initializes the inferences text field.
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldInferences() {
        if (jTextFieldInferences == null) {
            jTextFieldInferences = this.getBasicJTextField();
            jTextFieldInferences.setBackground(MainFrame.BG_COLOR_ENT_FIELD);
            jTextFieldInferences.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Inferences", jTextFieldInferences));
            jTextFieldInferences.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Inferences"));
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
            jButtonGetHistory.setToolTipText("Show the history of who edited this record");
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
                    EventLogFrame logViewer = new EventLogFrame(tls.findBySpecimen(specimen));
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
            jButtonPaste.setToolTipText("Paste previous record values into this screen");
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
            jButtonPaste.setEnabled(!(this.previousSpecimen == null && ImageCaptureApp.lastEditedSpecimenCache == null));
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
            jButtonCopy.setToolTipText("Copy the values of this record after saving it");
            // TODO: decide on keyboard shortcut
            jButtonCopy.setMnemonic(KeyEvent.VK_K);
            jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (thisPane.save()) {
                        // TODO: rather clone the specimen to prevent external/later changes
                        ImageCaptureApp.lastEditedSpecimenCache = thisPane.specimen;
                        thisPane.setStatus("Saved & copied specimen with id " + thisPane.specimen.getSpecimenId());
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
            URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/next.png");
            if (iconFile != null) {
                jButtonNext.setIcon(new ImageIcon(iconFile));
            } else {
                jButtonNext.setText("Next");
            }
            jButtonNext.setMnemonic(KeyEvent.VK_N);
            jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable());
            log.debug("next button enabled: " + specimenController.hasNextSpecimenInTable());
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
                thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
            URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/back.png");
            if (iconFile != null) {
                jButtonPrevious.setIcon(new ImageIcon(iconFile));
            } else {
                jButtonPrevious.setText("Previous");
            }
            jButtonPrevious.setMnemonic(KeyEvent.VK_P);
            jButtonPrevious.setToolTipText("Move to Previous Specimen");
            jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable());
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
                    jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable());
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
        boolean result = state == STATE_CLEAN;
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
            jTextFieldISODate.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "ISODate", jTextFieldISODate));
            jTextFieldISODate.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "ISODate"));
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

            jButtonDeterminations.addActionListener(new java.awt.event.ActionListener() {
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

    private JScrollPane getBasicWrapperJScrollPane() {
        JScrollPane pane = new JScrollPane();
        pane.addMouseWheelListener(new MouseWheelScrollListener(pane));
        int maxHeight = Integer.parseInt(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_MAX_FIELD_HEIGHT));
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
            dateEmergedButton.setToolTipText("Fill date emerged with data from verbatim date");
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
            dateCollectedButton.setToolTipText("Fill date collected with data from verbatim date");
            dateCollectedButton.addActionListener(new java.awt.event.ActionListener() {
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
            jButtonSpecificLocality.setToolTipText("Fill specific locality with data from verbatim locality");
            jButtonSpecificLocality.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jTextFieldVerbatimLocality.getText().equals("")) {
                        if (jTextFieldLocality.getText().equals("")) {
                            // If both are blank, set the blank value string.
                            jTextFieldLocality.setText("[no specific locality data]");
                        }
                        jTextFieldVerbatimLocality.setText(jTextFieldLocality.getText());
                    } else {
                        jTextFieldLocality.setText(jTextFieldVerbatimLocality.getText());
                    }
                }
            });
        }
        return jButtonSpecificLocality;
    }

    private JTextField getJTextFieldMigrationStatus() {
        if (jTextFieldMigrationStatus == null) {
            jTextFieldMigrationStatus = this.getBasicJTextField();
            // jLabelMigrationStatus.setBackground(null);
            // jLabelMigrationStatus.setBorder(null);
            jTextFieldMigrationStatus.setEditable(false);
            jTextFieldMigrationStatus.setText("");
            if (specimen.isExported()) {
                //                String uri =
                //                "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" +
                //                        specimen.getCatNum();
                //                jTextFieldMigrationStatus.setText(uri);
                jTextFieldMigrationStatus.setText(WorkFlowStatus.STAGE_DONE);
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
            jTextFieldImageCount = this.getBasicJTextField();
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

    private JTextField getjTextFieldMaxElevation() {
        if (jTextFieldMaxElevation == null) {
            jTextFieldMaxElevation = this.getBasicJTextField();

            jTextFieldMaxElevation.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    if (jTextFieldMaxElevation.getText().equals("")) {
                        return true;
                    }
                    try {
                        double val2 = Double.parseDouble(jTextFieldMaxElevation.getText());
                        if (jTextFieldMinElevation.getText().equals("")) {
                            return true;
                        }
                        double val1 = Double.parseDouble(jTextFieldMinElevation.getText());
                        return val1 <= val2;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            });
        }
        return jTextFieldMaxElevation;
    }

    private JTextField getBasicJTextField() {
        JTextField jTextField = new JTextField();
        if (specimen != null) {
            jTextField.setEditable(specimen.isEditable());
        }
        jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                thisPane.setStateToDirty();
            }
        });
        return jTextField;
    }

    private JComboBox<String> getComboBoxElevUnits() {
        if (comboBoxElevUnits == null) {
            comboBoxElevUnits = new JComboBox<>();
            comboBoxElevUnits.setModel(new DefaultComboBoxModel<>(new String[]{"", "?", "m", "ft"}));
            // set default
            //            comboBoxElevUnits.setSelectedItem("m");
        }
        return comboBoxElevUnits;
    }

    private JTextField getTextFieldMicrohabitat() {
        if (textFieldMicrohabitat == null) {
            textFieldMicrohabitat = this.getBasicJTextField();
        }
        return textFieldMicrohabitat;
    }

    private void autocompleteGeoDataFromGeoreference() {
        // initialize hash map; todo: memoize, load from file
        HashMap<String, String> primaryDivisionMapping = new HashMap<>();
        primaryDivisionMapping.put("Grisons", "Graubünden");
        primaryDivisionMapping.put("St. Gallen", "Sankt Gallen");
        primaryDivisionMapping.put("Tessin", "Ticino");
        primaryDivisionMapping.put("Wallis", "Valais");
        primaryDivisionMapping.put("Zurich", "Zürich");
        primaryDivisionMapping.put("Occitania", "Occitanie");

        // query data
        LatLong georeff = this.specimen.getLatLong().iterator().next();
        if (georeff.getDecLat() != null && georeff.getDecLong() != null) {
            // do it async as the request could take longer than desired
            new Thread(() -> {
                // first, try GeoNames.
                log.debug("Fetching address from GeoNames");
                GeoNamesUtility geoNamesUtility = new GeoNamesUtility();
                try {
                    Map<String, Object> data = geoNamesUtility.reverseSearchValues(georeff.getDecLat(), georeff.getDecLong(), new ArrayList<>(Arrays.asList("countryCode", "countryName", "adminName1")));
                    if (data != null) {
                        log.debug("Got address from GeoNames: " + data);
                        if (this.getCountryJTextField().getSelectedItem() == null || this.getCountryJTextField().getSelectedItem().equals("")) {
                            String countryName = (new ISO3166LifeCycle()).findByCountryCode((String) data.get("countryCode")).getCountryName();
                            // data.get("countryName")
                            this.getCountryJTextField().setSelectedItem(countryName);
                        } else {
                            log.debug("Won't automatically set country as is '" + this.getCountryJTextField().getSelectedItem() + "'.");
                        }
                        if (this.getPrimaryDivisionJTextField().getSelectedItem() == null || this.getPrimaryDivisionJTextField().getSelectedItem().equals("")) {
                            this.getPrimaryDivisionJTextField().setSelectedItem(data.get("adminName1"));
                        } else {
                            log.debug("Won't automatically set primary division as is '" + this.getCountryJTextField().getSelectedItem() + "'.");
                        }
                        return;
                    }
                } catch (Exception e) {
                    log.error("Failed to fetch geodata using GeoNames", e);
                }

                // if it fails, continue with openstreetmap
                log.debug("Fetching address from openstreetmap");
                Map<String, Object> data = OpenStreetMapUtility.getInstance().reverseSearchValues(georeff.getDecLat(), georeff.getDecLong(), new ArrayList<>(Arrays.asList("address.county", "address.state", "address.country")));
                if (data != null) {
                    log.debug("Got address from openstreetmap: " + data);
                    if (this.getCountryJTextField().getSelectedItem() == null || this.getCountryJTextField().getSelectedItem().equals("")) {
                        this.getCountryJTextField().setSelectedItem(data.get("address.country"));
                    } else {
                        log.debug("Won't automatically set country as is '" + this.getCountryJTextField().getSelectedItem() + "'.");
                    }
                    if (this.getPrimaryDivisionJTextField().getSelectedItem() == null || this.getPrimaryDivisionJTextField().getSelectedItem().equals("")) {
                        String primaryDivision = (String) data.get("address.state");
                        if (primaryDivision == null || primaryDivision == "") {
                            primaryDivision = (String) data.get("address.county");
                        }
                        this.getPrimaryDivisionJTextField().setSelectedItem(primaryDivisionMapping.getOrDefault(primaryDivision, primaryDivision));
                    } else {
                        log.debug("Won't automatically set primary division as is '" + this.getCountryJTextField().getSelectedItem() + "'.");
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
            jTextFieldDateDetermined.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "ISODate", jTextFieldDateDetermined));
            jTextFieldDateDetermined.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateIdentified"));
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
        if (jCBDeterminer == null) {
            jCBDeterminer = new JComboBox<>();
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] determiners = sls.getDistinctDeterminers();
                SwingUtilities.invokeLater(() -> {
                    jCBDeterminer.setModel(new DefaultComboBoxModel<>(determiners));
                    jCBDeterminer.setSelectedItem(specimen.getIdentifiedBy());
                });
            })).start();
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
            cbTypeStatus = new JComboBox<>(TypeStatus.getTypeStatusValues());
            // cbTypeStatus = new JComboBox(TypeStatus.getTypeStatusValues());  // for
            // visual editor
            cbTypeStatus.setEditable(specimen.isEditable());
            cbTypeStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "TypeStatus"));
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
            jTextFieldIdRemarks.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "IdentificationRemarks"));
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

    public JTextField getjTextFieldGBIFTaxonId() {
        if (jTextFieldGBIFTaxonId == null) {
            jTextFieldGBIFTaxonId = this.getBasicJTextField();
            jTextFieldGBIFTaxonId.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "GBIFTaxonId", jTextFieldGBIFTaxonId));
            jTextFieldGBIFTaxonId.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "GBIFTaxonId"));
            jTextFieldGBIFTaxonId.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    thisPane.setStateToDirty();
                }
            });
            // validate: check that https://www.gbif.org/species/" + taxonId does not result in a 404
            jTextFieldGBIFTaxonId.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String taxonId = jTextFieldGBIFTaxonId.getText().trim();
                    if (taxonId.isEmpty()) {
                        return true; // empty is valid
                    }
                    try {
                        URI uri = new URI("https://www.gbif.org/species/" + taxonId);
                        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
                        connection.setRequestMethod("HEAD");
                        int responseCode = connection.getResponseCode();
                        return responseCode != 404; // valid if not 404
                    } catch (Exception e) {
                        log.error("Error verifying GBIF Taxon ID", e);
                        return false; // invalid if any error occurs
                    }
                }
            });
        }
        return jTextFieldGBIFTaxonId;
    }

    public JButton getjButtonGBIFView() {
        if (jButtonGBIFView == null) {
            jButtonGBIFView = new JButton();
            jButtonGBIFView.setText("GBIF Taxon");
            jButtonGBIFView.setToolTipText("Open the GBIF view for this specimen");
            jButtonGBIFView.setMnemonic(KeyEvent.VK_G);
            jButtonGBIFView.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String taxonId = getjTextFieldGBIFTaxonId().getText().trim();
                    if (!taxonId.isEmpty()) {
                        String url = "https://www.gbif.org/species/" + taxonId;
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception ex) {
                            log.error("Error opening GBIF view", ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(thisPane, "No GBIF Taxon ID provided.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return jButtonGBIFView;
    }
}
