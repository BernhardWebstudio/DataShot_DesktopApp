/**
 * SearchDialog.java
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

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.lifecycle.*;
import edu.harvard.mcz.imagecapture.query.Specification;
import edu.harvard.mcz.imagecapture.query.StringToDateQueryParser;
import edu.harvard.mcz.imagecapture.ui.field.JIntegerField;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * SearchDialog
 */
public class SearchDialog extends JDialog {

    private static final Logger log =
            LoggerFactory.getLogger(SearchDialog.class); //  @jve:decl-index=0:
    private static final long serialVersionUID = 1L;
    private final Dimension maxComboBoxDims = new Dimension(350, 250);
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JButton jButton = null;
    private JPanel jPanel1 = null;
    private JScrollPane scrollPane = null;
    private JTextField jTextFieldDrawerNumber = null;
    private JTextField jTextFieldBarcode = null;
    private JComboBox<String> jTextFieldOrder = null;
    private JTextField jTextFieldFamily = null;
    private JTextField jTextFieldGenus = null;
    private JTextField jTextFieldSpecies = null;
    private JTextField jTextFieldCollectionNr = null;
    private JComboBox<String> jComboBoxCollection = null;
    private JComboBox<Object> jComboBoxWorkflowStatus = null;
    private JTextField jTextFieldImageFilename = null;
    private JComboBox<String> jComboBoxPath = null;
    private JComboBox<String> jComboBoxEntryBy = null;
    private JComboBox<String> jComboBoxIdentifiedBy = null;
    private JTextField jTextFieldSubfamily = null;
    private JTextField jTextFieldSubspecies = null;
    private JButton jButton1 = null;
    private JComboBox<String> jComboBoxCollector = null;
    private JTextField jTextFieldVerbatimLocality = null;
    private JComboBox<String> jComboBoxCountry = null;
    private JComboBox<String> jComboBoxSpecificLocality = null;
    private JComboBox<String> jComboBoxQuestions = null;
    private JIntegerField jOffsetNumberField = null;
    private JIntegerField jLimitNumberField = null;
    private JTextField jTextFieldTribe = null;
    private JTextField jTextFieldDateModified = null;
    //    private DatePicker jTextFieldDateModified = null;
    //private JTextField jTextFieldPrimaryDivision = null;
    private JComboBox<String> jComboBoxPrimaryDivision = null;
    private JTextField textFieldHigherGeog;
    private JLabel lblHigherGeography;
    private JTextField jTextFieldInterpretedDate;
//    private Frame parentFrame;

    /**
     * @param owner
     */
    public SearchDialog(Frame owner) {
        super(owner);
//        parentFrame = owner;
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
//        this.setSize(500, 750);
//        this.setLocationRelativeTo(parentFrame);
        this.setTitle("Search For Specimens");
        this.setContentPane(getJContentPane());
//        this.setPreferredSize(new Dimension(500, 750));
        this.pack();
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanelWithButtons(), BorderLayout.SOUTH);
            jContentPane.add(getJPanelWithFields(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelWithButtons() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getJButtonSearch(), new GridBagConstraints());
            jPanel.add(getCloseJButton(), gridBagConstraints17);
        }
        return jPanel;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearch() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Search");
            jButton.setMnemonic(KeyEvent.VK_S);
            this.getRootPane().setDefaultButton(jButton);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Searching...");
//                    if (jTextFieldDrawerNumber.getText() != null && jTextFieldDrawerNumber.getText().length() > 0) {
//                        searchCriteria.setDrawerNumber(jTextFieldDrawerNumber.getText());
//                    }
//                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    Map<String, Object> query = new HashMap<>();

                    if (jTextFieldBarcode.getText() != null && jTextFieldBarcode.getText().length() > 0) {
                        query.put("barcode", jTextFieldBarcode.getText());
                    }
                    if (jTextFieldOrder.getSelectedItem() != null && jTextFieldOrder.getSelectedItem().toString().trim().length() > 0) {
                        query.put("higherOrder", jTextFieldOrder.getSelectedItem().toString());
                    }
                    if (jTextFieldFamily.getText() != null && jTextFieldFamily.getText().length() > 0) {
                        query.put("family", jTextFieldFamily.getText());
                    }
                    if (jTextFieldSubfamily.getText() != null && jTextFieldSubfamily.getText().length() > 0) {
                        query.put("subfamily", jTextFieldSubfamily.getText());
                    }
                    if (jTextFieldTribe.getText() != null && jTextFieldTribe.getText().length() > 0) {
                        query.put("tribe", jTextFieldTribe.getText());
                    }
                    if (jTextFieldGenus.getText() != null && jTextFieldGenus.getText().length() > 0) {
                        query.put("genus", jTextFieldGenus.getText());
                    }
                    if (jTextFieldSpecies.getText() != null && jTextFieldSpecies.getText().length() > 0) {
                        query.put("specificEpithet", jTextFieldSpecies.getText());
                    }
                    if (jTextFieldSubspecies.getText() != null && jTextFieldSubspecies.getText().length() > 0) {
                        query.put("subspecificEpithet", jTextFieldSubspecies.getText());
                    }
                    if (jTextFieldVerbatimLocality.getText() != null && jTextFieldVerbatimLocality.getText().length() > 0) {
                        query.put("verbatimLocality", jTextFieldVerbatimLocality.getText());
                    }
					/*if (jTextFieldPrimaryDivision.getText()!=null && jTextFieldPrimaryDivision.getText().length() > 0) { 
						searchCriteria.setPrimaryDivison(jTextFieldPrimaryDivision.getText());
					}*/
                    if (jComboBoxWorkflowStatus.getSelectedItem() != null) {
                        if (!jComboBoxWorkflowStatus.getSelectedItem().toString().equals("")) {
                            query.put("workFlowStatus", jComboBoxWorkflowStatus.getSelectedItem().toString());
                        }
                    }
                    // TODO: Add higher geography
                    if (jComboBoxCountry.getSelectedItem() != null) {
                        if (!jComboBoxCountry.getSelectedItem().toString().equals("")) {
                            query.put("country", jComboBoxCountry.getSelectedItem().toString());
                        }
                    }

                    if (jComboBoxSpecificLocality.getSelectedItem() != null) {
                        if (!jComboBoxSpecificLocality.getSelectedItem().toString().equals("")) {
                            query.put("specificLocality", jComboBoxSpecificLocality.getSelectedItem().toString());
                        }
                    }

                    if (jComboBoxPrimaryDivision.getSelectedItem() != null) {
                        if (!jComboBoxPrimaryDivision.getSelectedItem().toString().equals("")) {
                            query.put("primaryDivison", jComboBoxPrimaryDivision.getSelectedItem().toString());
                        }
                    }

                    if (jTextFieldInterpretedDate.getText() != null && jTextFieldInterpretedDate.getText().length() > 0) {
                        query.put("isoDate", jTextFieldInterpretedDate.getText());
                    }
                    if (jTextFieldDateModified.getText() != null && jTextFieldDateModified.getText().length() > 0) {
                        try {
                            StringToDateQueryParser parser = new StringToDateQueryParser(jTextFieldDateModified.getText());
                            query.put("dateLastUpdated", new Specification<Specimen, Long>() {
                                @Override
                                public Predicate toPredicate(Root<Specimen> root, CriteriaQuery<Long> query, CriteriaBuilder cb) {
                                    return cb.between(
                                            root.get("dateLastUpdated"), parser.getDateLowerBound(), parser.getDateUpperBound()
                                    );
                                }
                            });
                        } catch (IllegalArgumentException exception) {
                            exception.printStackTrace();
                            Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Error: Search could not be composed.");
                            JOptionPane.showMessageDialog(null, "Date for dateLastUpdated search could not be parsed.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
//                        query.put("dateLastUpdated", jTextFieldDateModified.getText());
                    }
//                    Date fieldModifiedValue = (Date) jTextFieldDateModified.convert().getDateWithDefaultZone();
//                    if (fieldModifiedValue != null) {
//                        searchCriteria.setDateLastUpdated(fieldModifiedValue);
//                    }

                    if (jComboBoxCollector.getSelectedItem() != null) {
                        if (!jComboBoxCollector.getSelectedItem().toString().equals("")) {
                            query.put(
                                    "collectors.collectorName",
                                    jComboBoxCollector.getSelectedItem().toString()
                            );
                        }
                    }
                    if ((jTextFieldImageFilename.getText() != null && jTextFieldImageFilename.getText().length() > 0) ||
                            (jComboBoxEntryBy.getSelectedItem() != null)) {
                        // Either image filename or date imaged or both have content
                        // so we need to add an image to the search criteria.
                        if (jTextFieldImageFilename.getText() != null && jTextFieldImageFilename.getText().length() > 0) {
                            // if filename has content, add it
                            query.put(
                                    "ICImages.filename",
                                    jTextFieldImageFilename.getText()
                            );
                        }
                        if (jComboBoxPath.getSelectedItem() != null) {
                            if (!jComboBoxPath.getSelectedItem().toString().equals("")) {
                                // it the path = date imaged has content, add it
                                query.put(
                                        "ICImages.path",
                                        jComboBoxPath.getSelectedItem().toString()
                                );
                            }
                        }
                    }
                    if (jComboBoxCollection.getSelectedItem() != null) {
                        if (!jComboBoxCollection.getSelectedItem().toString().equals("")) {
                            query.put(
                                    "collection",
                                    jComboBoxCollection.getSelectedItem().toString()
                            );
                        }
                    }
                    if (jTextFieldCollectionNr.getText() != null && jTextFieldCollectionNr.getText().length() > 0) {
                        query.put("numbers.numberType", "Collection Number");
                        query.put("numbers.number", jTextFieldCollectionNr.getText());
                    }
                    if (jComboBoxEntryBy.getSelectedItem() != null) {
                        if (!jComboBoxEntryBy.getSelectedItem().toString().equals("")) {
                            query.put(
                                    "trackings.user", jComboBoxEntryBy.getSelectedItem().toString()
                            );
                        }
                    }
                    if (jComboBoxIdentifiedBy.getSelectedItem() != null) {
                        if (!jComboBoxIdentifiedBy.getSelectedItem().toString().equals("")) {
                            query.put("identifiedBy", jComboBoxIdentifiedBy.getSelectedItem().toString());
                        }
                    }
                    if (jComboBoxQuestions.getSelectedItem() != null) {
                        if (!jComboBoxQuestions.getSelectedItem().toString().equals("")) {
                            query.put("questions", jComboBoxQuestions.getSelectedItem().toString());
                        }
                    }
                    log.debug("Starting search with criteria...");
                    Singleton.getSingletonInstance().getMainFrame().setSpecimenBrowseList(query, jLimitNumberField.getIntValue(), jOffsetNumberField.getIntValue());
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes the main JPanel
     *
     * @return
     */
    private JScrollPane getJPanelWithFields() {
        if (scrollPane == null) {
            // set titles etc.

            jPanel1 = new JPanel(new MigLayout("wrap 2, fillx"));
            JLabel jLabelInstructions = new JLabel("Search for specimens. Use % as a wildcard.");
            Font f = jLabelInstructions.getFont();
            // bold
            jLabelInstructions.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            jPanel1.add(jLabelInstructions, "span 2");

            // set fields
            String[] labels = {
                    "Image Filename",
                    "Imaged Date/Path",
                    //"Drawer Number",
                    "Barcode",
                    "Order",
                    "Family",
                    "Tribe",
                    "Subfamily",
                    "Genus",
                    "Species",
                    "Subspecies",
                    "Verbatim Locality",
                    "Specific Locality",
                    "State/Province",
                    "Country",
                    "Collection",
                    "Collection Nr.",
                    "Collector",
                    "Interpreted Date",
                    "Workflow Status",
                    "Questions",
                    "Entry By",
                    "Identified By",
                    "Date Last Updated",
                    "Limit",
                    "Offset"
            };

            Component[] fields = {
                    this.getImageFileJTextField(),
                    this.getImagePathJComboBox(),
                    //this.getDrawerNumberJTextField(),
                    this.getBarcodeJTextField(),
                    this.getOrderJTextField(),
                    this.getFamilyJTextField(),
                    this.getTribeJTextField(),
                    this.getSubfamilyJTextField(),
                    this.getGenusJTextField(),
                    this.getSpeciesJTextField(),
                    this.getSubspeciesJTextField(),
                    this.getVerbatimLocalityJTextField(),
                    this.getSpecificLocalityJComboBox(),
                    this.getPrimaryDivisionJComboBox(),
                    this.getCountryJComboBox(),
                    this.getCollectionJComboBox(),
                    this.getCollectionNrJTextField(),
                    this.getCollectorsJComboBox(),
                    this.getInterpretedDateTextField(),
                    this.getWorkflowsJComboBox(),
                    this.getQuestionJComboBox(),
                    this.getUsersJComboBox(),
                    this.getIdentifiedByComboBox(),
                    this.getLastUpdatedJTextField(),
                    this.getLimitJIntegerField(),
                    this.getOffsetJIntegerField()
            };

            assert (fields.length == labels.length);
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel();
                label.setText(labels[i].concat(":"));
                jPanel1.add(label, "right"); //"align label");
                jPanel1.add(fields[i], "grow");
            }
						/*
						GridBagConstraints gbc_lblHigherGeography = new GridBagConstraints();
						gbc_lblHigherGeography.insets = new Insets(0, 0, 5, 5);
						gbc_lblHigherGeography.anchor = GridBagConstraints.EAST;
						gbc_lblHigherGeography.gridx = 0;
						gbc_lblHigherGeography.gridy = 12;
						jPanel1.add(getLblHigherGeography(), gbc_lblHigherGeography);
						GridBagConstraints gbc_textFieldHigherGeog = new GridBagConstraints();
						gbc_textFieldHigherGeog.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldHigherGeog.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldHigherGeog.gridx = 1;
						gbc_textFieldHigherGeog.gridy = 12;
						*/
            //jPanel1.add(getTextFieldHigherGeog(), gbc_textFieldHigherGeog);
            scrollPane = new JScrollPane(jPanel1);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
        return scrollPane;
    }

    private JTextField getCollectionNrJTextField() {
        if (jTextFieldCollectionNr == null) {
            jTextFieldCollectionNr = new JTextField();
            jTextFieldCollectionNr.setColumns(10);
            // jTextFieldCollectionNr.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "CollectionNr", jTextFieldCollectionNr));
//            jTextFieldCollectionNr.setToolTipText(
//                    MetadataRetriever.getFieldHelp(Specimen.class, "CollectionNr"));
        }
        return jTextFieldCollectionNr;
    }

    private JComboBox<String> getOrderJTextField() {
        if (jTextFieldOrder == null) {
            jTextFieldOrder = new JComboBox<>();
            jTextFieldOrder.setModel(new DefaultComboBoxModel<>());
            // lazily load the orders
            (new Thread(() -> {
                String[] orders = HigherTaxonLifeCycle.selectDistinctOrder();
                SwingUtilities.invokeLater(() -> {
                    jTextFieldOrder.setModel(new DefaultComboBoxModel<>(
                            orders
                    ));

                    if (!Arrays.stream(orders).anyMatch(""::equals)) {
                        jTextFieldOrder.addItem("");
                    }
                    jTextFieldOrder.setSelectedItem("");
                });
            })).start();

            jTextFieldOrder.setEditable(true);
            // jComboBoxHigherOrder.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Order", jComboBoxHigherOrder));
            jTextFieldOrder.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "HigherOrder"));
            AutoCompleteDecorator.decorate(jTextFieldOrder);
        }
        return jTextFieldOrder;
    }

    private Component getLastUpdatedJTextField() {
        if (jTextFieldDateModified == null) {
            //DatePickerSettings datePickerSettings = new DatePickerSettings(new Locale("en", "CH"));

            //jTextFieldDateModified = new DatePicker(datePickerSettings);
            jTextFieldDateModified = new JTextField();
        }
        return jTextFieldDateModified;
    }

    private GridBagConstraints initializeBasicGridBag() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 5, 0);
        return c;
    }

    private GridBagConstraints initializeBasicGridBag(int grid_x, int grid_y) {
        GridBagConstraints c = this.initializeBasicGridBag();
        c.gridx = grid_x;
        c.gridy = grid_y;
        return c;
    }

    private GridBagConstraints initializeBasicGridBag(int grid_x, int grid_y, int anchor) {
        GridBagConstraints c = this.initializeBasicGridBag(grid_x, grid_y);
        c.anchor = anchor;
        return c;
    }

    private GridBagConstraints initializeBasicResizableGridBag(int grid_x, int grid_y) {
        GridBagConstraints c = this.initializeBasicGridBag(grid_x, grid_y);
        c.fill = GridBagConstraints.BOTH;
        return c;
    }

    private GridBagConstraints initializeBasicResizableGridBag(int grid_x, int grid_y, int anchor) {
        GridBagConstraints c = this.initializeBasicGridBag(grid_x, grid_y, anchor);
        c.fill = GridBagConstraints.BOTH;
        return c;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getDrawerNumberJTextField() {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = new JTextField();
        }
        return jTextFieldDrawerNumber;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getBarcodeJTextField() {
        if (jTextFieldBarcode == null) {
            jTextFieldBarcode = new JTextField();
        }
        return jTextFieldBarcode;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getFamilyJTextField() {
        if (jTextFieldFamily == null) {
            jTextFieldFamily = new JTextField();
        }
        return jTextFieldFamily;
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private JTextField getGenusJTextField() {
        if (jTextFieldGenus == null) {
            jTextFieldGenus = new JTextField();
        }
        return jTextFieldGenus;
    }

    /**
     * This method initializes jTextField4
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSpeciesJTextField() {
        if (jTextFieldSpecies == null) {
            jTextFieldSpecies = new JTextField();
        }
        return jTextFieldSpecies;
    }

    /**
     * This method initializes jComboBox labelled collection
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getCollectionJComboBox() {
        if (jComboBoxCollection == null) {
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxCollection = new JComboBox<>();
            jComboBoxCollection.setModel(new DefaultComboBoxModel<>());
            // lazily load the collections
            (new Thread(() -> {
                String[] collections = sls.getDistinctCollections();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxCollection.setModel(new DefaultComboBoxModel<>(
                            collections
                    ));
                    if (!Arrays.stream(collections).anyMatch(""::equals)) {
                        jComboBoxCollection.addItem("");
                    }
                    jComboBoxCollection.setSelectedItem("");
                });
            })).start();
            jComboBoxCollection.setEditable(true);
            jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
            jComboBoxCollection.setMaximumSize(this.maxComboBoxDims);
            AutoCompleteDecorator.decorate(jComboBoxCollection);
        }
        return jComboBoxCollection;
    }

    /**
     * This method initializes jComboBox1 labelled workflow
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<Object> getWorkflowsJComboBox() {
        if (jComboBoxWorkflowStatus == null) {
            ArrayList<String> values = new ArrayList<>();
            values.add("");
            String[] wfsv = WorkFlowStatus.getWorkFlowStatusValues();
            values.addAll(Arrays.asList(wfsv));
            jComboBoxWorkflowStatus = new JComboBox<>(values.toArray());
            jComboBoxWorkflowStatus.getModel().setSelectedItem("");
            jComboBoxWorkflowStatus.setEditable(true);
            jComboBoxWorkflowStatus.setMaximumSize(this.maxComboBoxDims);
            AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus);
        }
        return jComboBoxWorkflowStatus;
    }

    /**
     * This method initializes jTextField5
     *
     * @return javax.swing.JTextField
     */
    private JTextField getImageFileJTextField() {
        if (jTextFieldImageFilename == null) {
            jTextFieldImageFilename = new JTextField();
        }
        return jTextFieldImageFilename;
    }

    /**
     * This method initializes jComboBox2
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getImagePathJComboBox() {
        if (jComboBoxPath == null) {
            jComboBoxPath = new JComboBox<>();
            (new Thread(() -> {
                ICImageLifeCycle ils = new ICImageLifeCycle();
                String[] paths = ils.getDistinctPaths();
                SwingUtilities.invokeLater(() -> jComboBoxPath.setModel(new DefaultComboBoxModel<>(paths)));
            })).start();
            jComboBoxPath.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxPath);
            jComboBoxPath.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxPath;
    }

    /**
     * This method initializes the combobox containing the users labelled "entry by"
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getUsersJComboBox() {
        if (jComboBoxEntryBy == null) {
            TrackingLifeCycle tls = new TrackingLifeCycle();
            jComboBoxEntryBy = new JComboBox<>();
            // lazily load the users
            (new Thread(() -> {
                String[] users = tls.getDistinctUsers();
                SwingUtilities.invokeLater(() -> jComboBoxEntryBy.setModel(new DefaultComboBoxModel<>(
                        users
                )));
            })).start();
            jComboBoxEntryBy.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxEntryBy);
            jComboBoxEntryBy.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxEntryBy;
    }

    /**
     * This method initializes the tracking user combo box
     *
     * @return java.swing.JComboBox
     */
    private JComboBox<String> getIdentifiedByComboBox() {
        if (jComboBoxIdentifiedBy == null) {
            jComboBoxIdentifiedBy = new JComboBox<>();
            // lazily load the determiners
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] determiners = sls.getDistinctDeterminers();
                SwingUtilities.invokeLater(() -> jComboBoxIdentifiedBy.setModel(new DefaultComboBoxModel<>(
                        determiners
                )));
            })).start();
            jComboBoxIdentifiedBy.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxIdentifiedBy);
            jComboBoxIdentifiedBy.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxIdentifiedBy;
    }

    /**
     * This method initializes jTextField providing subfamily info
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSubfamilyJTextField() {
        if (jTextFieldSubfamily == null) {
            jTextFieldSubfamily = new JTextField();
        }
        return jTextFieldSubfamily;
    }

    /**
     * This method initializes jTextField labelled subspecies
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSubspeciesJTextField() {
        if (jTextFieldSubspecies == null) {
            jTextFieldSubspecies = new JTextField();
        }
        return jTextFieldSubspecies;
    }

    private JTextField getInterpretedDateTextField() {
        if (jTextFieldInterpretedDate == null) {
            jTextFieldInterpretedDate = new JTextField();
        }
        return jTextFieldInterpretedDate;
    }

    /**
     * This method initializes the button to close the form
     * it might be slightly unnecessary as the behaviour is the same as the OS's window close button
     *
     * @return javax.swing.JButton
     */
    private JButton getCloseJButton() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Close");
            jButton1.setMnemonic(KeyEvent.VK_C);
            jButton1.addActionListener(e -> setVisible(false));
        }
        return jButton1;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getCollectorsJComboBox() {
        if (jComboBoxCollector == null) {
            CollectorLifeCycle cls = new CollectorLifeCycle();
            jComboBoxCollector = new JComboBox<>(cls.getDistinctCollectors());
            // lazily load the collectors
            (new Thread(() -> {
                String[] collectors = cls.getDistinctCollectors();
                SwingUtilities.invokeLater(() -> jComboBoxCollector.setModel(new DefaultComboBoxModel<>(
                        collectors
                )));
            })).start();
            jComboBoxCollector.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxCollector);
            jComboBoxCollector.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxCollector;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getVerbatimLocalityJTextField() {
        if (jTextFieldVerbatimLocality == null) {
            jTextFieldVerbatimLocality = new JTextField();
        }
        return jTextFieldVerbatimLocality;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getCountryJComboBox() {
        if (jComboBoxCountry == null) {
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxCountry = new JComboBox<>();
            // lazily load the countries
            (new Thread(() -> {
                String[] countries = sls.getDistinctCountries();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxCountry.setModel(new DefaultComboBoxModel<>(countries));

                    jComboBoxCountry.addItem("");
                    jComboBoxCountry.addItem("%_%");
                });
            })).start();
            jComboBoxCountry.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxCountry);
            jComboBoxCountry.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxCountry;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getSpecificLocalityJComboBox() {
        if (jComboBoxSpecificLocality == null) {
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            jComboBoxSpecificLocality = new JComboBox<>();
            // lazily load the countries
            (new Thread(() -> {
                String[] specificLocalities = sls.getDistinctSpecificLocality();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxSpecificLocality.setModel(new DefaultComboBoxModel<>(
                            specificLocalities
                    ));

                    jComboBoxSpecificLocality.addItem("");
                    jComboBoxSpecificLocality.addItem("%_%");
                });
            })).start();
            jComboBoxSpecificLocality.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxSpecificLocality);
            jComboBoxSpecificLocality.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxSpecificLocality;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getPrimaryDivisionJComboBox() {
        if (jComboBoxPrimaryDivision == null) {
            jComboBoxPrimaryDivision = new JComboBox<>();
            // lazily load the primary divisions
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] primaryDivisions = sls.getDistinctPrimaryDivisions();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxPrimaryDivision.setModel(new DefaultComboBoxModel<>(
                            primaryDivisions
                    ));

                    jComboBoxPrimaryDivision.addItem("");
                    jComboBoxPrimaryDivision.addItem("%_%");
                });
            })).start();
            jComboBoxPrimaryDivision.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision);
            jComboBoxPrimaryDivision.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxPrimaryDivision;
    }

    /**
     * This method initializes jComboBox1
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getQuestionJComboBox() {
        if (jComboBoxQuestions == null) {
            jComboBoxQuestions = new JComboBox<>();
            // lazily load the questions
            (new Thread(() -> {
                SpecimenLifeCycle sls = new SpecimenLifeCycle();
                String[] questions = sls.getDistinctQuestions();
                SwingUtilities.invokeLater(() -> {
                    jComboBoxQuestions.setModel(new DefaultComboBoxModel<>(
                            questions
                    ));

                    jComboBoxQuestions.addItem("");
                    jComboBoxQuestions.addItem("%_%");
                });
            })).start();
            jComboBoxQuestions.setEditable(true);
            AutoCompleteDecorator.decorate(jComboBoxQuestions);
            jComboBoxQuestions.setMaximumSize(this.maxComboBoxDims);
        }
        return jComboBoxQuestions;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTribeJTextField() {
        if (jTextFieldTribe == null) {
            jTextFieldTribe = new JTextField();
        }
        return jTextFieldTribe;
    }

    private JIntegerField getOffsetJIntegerField() {
        if (jOffsetNumberField == null) {
            jOffsetNumberField = new JIntegerField();
            jOffsetNumberField.setValue(0);
        }
        return jOffsetNumberField;
    }

    private JIntegerField getLimitJIntegerField() {
        if (jLimitNumberField == null) {
            jLimitNumberField = new JIntegerField();
            jLimitNumberField.setValue(50000);
        }
        return jLimitNumberField;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
	/*private JTextField getJTextFieldPrimaryDivision() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
		}
		return jTextFieldPrimaryDivision;
	}*/
    private JTextField getTextFieldHigherGeog() {
        if (textFieldHigherGeog == null) {
            textFieldHigherGeog = new JTextField();
            textFieldHigherGeog.setColumns(10);
        }
        return textFieldHigherGeog;
    }

    private JLabel getLblHigherGeography() {
        if (lblHigherGeography == null) {
            lblHigherGeography = new JLabel("Higher Geography");
        }
        return lblHigherGeography;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
