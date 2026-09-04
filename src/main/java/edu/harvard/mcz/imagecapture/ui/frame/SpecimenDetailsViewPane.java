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
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
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
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import edu.harvard.mcz.imagecapture.ui.component.JAccordionPanel;
import edu.harvard.mcz.imagecapture.ui.component.JTableCellTabbing;
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder;
import edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog;
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel;
import edu.harvard.mcz.imagecapture.utility.GeoNamesUtility;
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility;
import jakarta.persistence.OptimisticLockException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
 * specimen. Uses declarative {@link FormBindingContext} for 2-way data binding,
 * dirty tracking, tooltips, and validation.
 */
public class SpecimenDetailsViewPane extends JPanel {

	private static final Logger log = LoggerFactory.getLogger(SpecimenDetailsViewPane.class);
	private static final long serialVersionUID = 3716072190995030749L;
	private static final int STATE_CLEAN = 0;
	private static final int STATE_DIRTY = 1;

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3))
			.followRedirects(HttpClient.Redirect.NORMAL).build();

	public static boolean isCopyPasteActivated() {
		return Singleton.getSingletonInstance().getUser() != null
				&& Singleton.getSingletonInstance().getUser().canCopyPaste();
	}

	private Specimen specimen;
	private boolean dataLoadedSuccessfully = true;
	private final StringBuffer higherGeogNotFoundWarning = new StringBuffer();
	private final KeyboardShortcutManager manager = KeyboardShortcutManager.getInstance();
	private Specimen previousSpecimen = null;
	private SpecimenController specimenController = null;
	private int state;
	private SpecimenDetailsViewPane thisPane = null;

	// Declarative form binding context
	private final FormBindingContext<Specimen> bindingContext;

	// Form controls referenced in button actions, location autocompletion, or
	// dialog interactions
	private JTextField jTextFieldStatus;
	private JPanel jPanel;
	private JAccordionPanel accordionDetailsPanel;

	private JTextField jTextFieldDateNos;
	private JTextField jTextFieldDateCollected;
	private JTextField jTextFieldDateEmerged;
	private JTextField jTextFieldVerbatimLocality;
	private JTextField jTextFieldLocality;
	private JComboBox<String> jComboBoxCountry;
	private JComboBox<String> jComboBoxPrimaryDivision;
	private JTextField textFieldDecimalLat;
	private JTextField textFieldDecimalLong;
	private JComboBox<String> cbMethod;
	private JComboBox<String> cbDatum;
	private JTextField txtErrorRadius;
	private JComboBox<String> comboBoxErrorUnits;
	private JTextField jTextFieldMinElevation;
	private JTextField jTextFieldMaxElevation;
	private JComboBox<String> comboBoxElevUnits;
	private JTextField jTextFieldGBIFTaxonId;
	private JComboBox<String> jComboBoxLocationInCollection;
	private JComboBox<String> jComboBoxWorkflowStatus;
	private JTextField jTextFieldLastUpdatedBy;
	private JTextField jTextFieldDateLastUpdated;
	private boolean isVerified = true;
	private JTextField jTextFieldImageCount;
	private JTextField jTextFieldMigrationStatus;
	private JLabel jLabelDBId;

	// Tables & Table components
	private JTableWithRowBorder jTableCollectors;
	private JTableWithRowBorder jTableNumbers;
	private JTableWithRowBorder jTableSpecimenParts;
	private JScrollPane jScrollPaneCollectors;
	private JScrollPane jScrollPaneNumbers;
	private JScrollPane jScrollPaneSpecimenParts;
	private JScrollPane jScrollPaneNotes;

	// Buttons
	private JButton dateEmergedButton;
	private JButton dateCollectedButton;
	private JButton jButtonSpecificLocality;
	private JButton citedInPublicationButton;
	private JButton pasteExcelButton;
	private JButton jButtonGeoReference;
	private JButton jButtonGBIFView;
	private JButton jButtonAddPreparationType;
	private JButton jButtonCollectorAdd;
	private JButton jButtonNumbersAdd;
	private JButton jButtonHistory;
	private JButton jButtonNahimaLink;
	private JButton jButtonPaste;
	private JButton jButtonCopy;
	private JButton jButtonPrevious;
	private JButton jButtonNext;
	private JButton jButtonSave;
	private JButton jButtonDeterminations;

	private GeoreferenceDialog georeferenceDialog;

	/**
	 * Construct an instance of a SpecimenDetailsViewPane showing the data present
	 * in aSpecimenInstance.
	 *
	 * @param aSpecimenInstance
	 *            the Specimen instance to display for editing.
	 * @param aController
	 *            the SpecimenController coordinating navigation.
	 */
	public SpecimenDetailsViewPane(Specimen aSpecimenInstance, SpecimenController aController) {
		this.specimen = aSpecimenInstance;
		this.specimenController = aController;
		this.thisPane = this;
		this.bindingContext = new FormBindingContext<>(Specimen.class, specimen != null && specimen.isEditable(),
				this::setStateToDirty);

		SpecimenLifeCycle s = new SpecimenLifeCycle();
		setStateToClean();
		boolean wasFromCache = false;
		try {
			if (specimen != null && specimen.getSpecimenId() != null && !specimen.isFullyLoaded()) {
				Specimen full = SpecimenCache.get(specimen.getSpecimenId());
				if (full == null || !full.isFullyLoaded()) {
					full = s.findById(specimen.getSpecimenId());
					if (full != null && full.isFullyLoaded()) {
						SpecimenCache.put(full);
					}
				} else {
					wasFromCache = true;
				}
				if (full != null && full.isFullyLoaded()) {
					this.specimen = full;
					if (this.specimenController != null) {
						this.specimenController.setSpecimen(full);
					}
				}
			}

			if (specimen != null && specimen.isFullyLoaded()) {
				s.attachClean(specimen);
			}
			initialize();
			setValues();
			this.dataLoadedSuccessfully = (this.specimen != null && this.specimen.isFullyLoaded());
		} catch (Exception e) {
			this.dataLoadedSuccessfully = false;
			String status = "Undefined error initializing SpecimenDetails. Restarting Database connection...";
			if (e instanceof SessionException || e instanceof TransactionException) {
				status = "Database Connection Error. Resetting connection... Try again.";
			} else if (e instanceof IllegalStateException) {
				status = "Illegal state exception. Last edit possibly lost. Try again.";
			} else if (e instanceof OptimisticLockException) {
				status = "Error: last edited entry has been modified externally. Try again.";
			}
			log.debug(status);
			if (Singleton.getSingletonInstance().getMainFrame() != null) {
				Singleton.getSingletonInstance().getMainFrame().setStatusMessage(status);
			}
			log.debug(e.getMessage(), e);
			HibernateUtil.restartSessionFactory();
			this.setVisible(false);
		}
		boolean fromCache = wasFromCache || (specimenController != null && specimenController.isLoadedFromCache());
		if (fromCache && dataLoadedSuccessfully && specimen != null && specimen.getSpecimenId() != null) {
			this.isVerified = false;
			updateSaveButtonState();
			verifyFreshnessAsync();
		} else {
			this.isVerified = true;
			updateSaveButtonState();
		}
	}

	/**
	 * Initializes the specimen details view pane layout and keyboard shortcuts.
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(0);
		borderLayout.setVgap(0);
		this.setLayout(borderLayout);
		this.add(getJTextFieldStatus(), BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane(getJPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(scrollPane, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(100, 100));

		// Register keyboard shortcuts
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
		if (isCopyPasteActivated()) {
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
			JOptionPane.showMessageDialog(thisPane,
					"This Specimen is already exported. Edit will not be saved to Nahima.", "Warning: not editable",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	void registerShortcut(String name, String defaultStroke, Action action) {
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();
		inputMap.put(manager.getShortcut(name, defaultStroke), name);
		actionMap.put(name, action);
		log.debug("Registered shortcut: {} as {}", name, manager.getShortcut(name, defaultStroke));
	}

	public void setWarning(String warning) {
		jTextFieldStatus.setText(warning);
		jTextFieldStatus.setForeground(Color.RED);
	}

	private void setWarnings() {
		log.debug("In set warnings");
		if (specimen.getICImages() != null) {
			for (ICImage im : specimen.getICImages()) {
				String rbc = im.getRawBarcode() != null ? im.getRawBarcode() : "";
				String ebc = im.getRawExifBarcode() != null ? im.getRawExifBarcode() : "";
				if (!rbc.equals(ebc)) {
					if (Singleton.getSingletonInstance().getProperties().getProperties()
							.getProperty(ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE).equals("true")) {
						this.setWarning("Warning: An image has mismatch between Comment and Barcode.");
						log.debug("Setting: Warning: Image has mismatch between Comment and Barcode.");
					}
				}
			}
		}
		if (higherGeogNotFoundWarning.length() > 0) {
			this.setWarning(higherGeogNotFoundWarning.toString());
		}
	}

	public void setStatus(String status) {
		log.info("Setting status to: {}", status);
		jTextFieldStatus.setText(status);
		jTextFieldStatus.setForeground(Color.BLACK);
	}

	public boolean save() {
		if (!isVerified) {
			this.setWarning("Cannot save: verifying latest version from database. Please wait a moment.");
			return false;
		}
		if (!dataLoadedSuccessfully || specimen == null || !specimen.isFullyLoaded()) {
			JOptionPane.showMessageDialog(thisPane,
					"Cannot save: Specimen data was not fully loaded. Save is disabled to prevent data loss.",
					"Save Aborted", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!specimen.isEditable(Singleton.getSingletonInstance().getUser())) {
			JOptionPane.showMessageDialog(thisPane, "This Specimen cannot be edited. No edit will be saved.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		try {
			thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} catch (Exception ex) {
			log.error("Error setting cursor", ex);
		}
		try {
			this.setStatus("Saving");
			if (jTableCollectors != null && jTableCollectors.isEditing()) {
				jTableCollectors.getCellEditor().stopCellEditing();
			}
			if (jTableSpecimenParts != null && jTableSpecimenParts.isEditing()) {
				jTableSpecimenParts.getCellEditor().stopCellEditing();
			}
			if (jTableNumbers != null && jTableNumbers.isEditing()) {
				jTableNumbers.getCellEditor().stopCellEditing();
			}

			// Synchronize all declarative form fields from UI into the Specimen model
			bindingContext.writeTo(specimen);

			specimen.setLastUpdatedBy(Singleton.getSingletonInstance().getUserFullName());
			specimen.setDateLastUpdated(new Date());

			try {
				specimenController.setSpecimen(specimen);
				specimenController.save();
				setStateToClean();
				this.setStatus("Saved");
				jTextFieldStatus.setForeground(Color.BLACK);
				setWarnings();
				if (jTextFieldLastUpdatedBy != null) {
					jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
				}
				if (jTextFieldDateLastUpdated != null && specimen.getDateLastUpdated() != null) {
					jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString());
				}
			} catch (SaveFailedException e) {
				setStateToDirty();
				this.setWarning("Error: " + e.getMessage());
				return false;
			}
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			if (Singleton.getSingletonInstance().getMainFrame() != null) {
				Singleton.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount(", "));
			}
		} catch (OptimisticLockException e) {
			log.error("OptimisticLockException in save()", e);
			setStateToDirty();
			this.setWarning(
					"Error: This record has been modified externally by another user. Please reload before saving.");
			if (specimen != null && specimen.getSpecimenId() != null) {
				SpecimenCache.invalidate(specimen.getSpecimenId());
			}
			return false;
		} catch (Exception e) {
			setStateToDirty();
			this.setWarning("Error: " + e.getMessage());
			log.error("Exception in save()", e);
			throw e;
		} finally {
			updateContentDependentLabels();
			try {
				thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} catch (Exception ex) {
				log.error("Error restoring cursor", ex);
			}
		}
		return true;
	}

	/**
	 * Set the fields values to the ones of the previous specimen.
	 */
	private void pastePreviousRecord() {
		log.debug("calling pastePreviousRecord()");
		if (previousSpecimen == null) {
			return;
		}
		if (!previousSpecimen.isFullyLoaded()) {
			log.warn("Cannot paste: previousSpecimen is not fully loaded");
			this.setWarning("Cannot paste: source specimen is not fully loaded.");
			return;
		}

		// Synchronize all declarative form fields from previousSpecimen into UI
		bindingContext.readFrom(previousSpecimen);

		// Clone and copy numbers
		specimen.getNumbers().clear();
		for (Number number : previousSpecimen.getNumbers()) {
			Number n = (Number) number.clone();
			n.setSpecimen(specimen);
			specimen.getNumbers().add(n);
		}
		if (jTableNumbers != null) {
			jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));
			this.setupNumberJTableRenderer();
		}

		// Specimen parts
		Set<SpecimenPart> followingParts = previousSpecimen.getSpecimenParts();
		Set<SpecimenPart> newParts = followingParts.stream().filter(part -> !specimen.getSpecimenParts().contains(part))
				.collect(Collectors.toSet());
		specimen.getSpecimenParts().removeIf(part -> !followingParts.contains(part));
		for (SpecimenPart specimenPart : newParts) {
			SpecimenPart part = (SpecimenPart) specimenPart.clone();
			part.setSpecimen(specimen);
			specimen.getSpecimenParts().add(part);
		}
		if (jTableSpecimenParts != null) {
			jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
			this.setupSpecimenPartsJTableRenderer();
		}

		// Collectors
		Set<Collector> followingCollectors = previousSpecimen.getCollectors();
		Set<Collector> newCollectors = followingCollectors.stream()
				.filter(collector -> !specimen.getCollectors().contains(collector)).collect(Collectors.toSet());
		specimen.getCollectors().removeIf(collector -> !followingCollectors.contains(collector));
		for (Collector collector : newCollectors) {
			Collector c = (Collector) collector.clone();
			c.setSpecimen(specimen);
			specimen.getCollectors().add(c);
		}
		if (jTableCollectors != null) {
			jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));
			this.setupCollectorJTableRenderer();
		}

		// Determinations
		specimen.getDeterminations().clear();
		for (Determination prevdet : previousSpecimen.getDeterminations()) {
			Determination newdet = prevdet.clone();
			newdet.setSpecimen(specimen);
			specimen.getDeterminations().add(newdet);
		}

		// Georeference
		specimen.getLatLong().clear();
		HashSet<LatLong> latLongs = new HashSet<>();
		for (LatLong prevgeo : previousSpecimen.getLatLong()) {
			LatLong newgeo = prevgeo.clone();
			newgeo.setSpecimen(specimen);
			latLongs.add(newgeo);
		}
		specimen.setLatLong(latLongs);
		reloadGeoRefFieldValues();

		// Cited in publication
		specimen.setCitedInPublicationComment(previousSpecimen.getCitedInPublicationComment());
		specimen.setCitedInPublicationLink(previousSpecimen.getCitedInPublicationLink());
		specimen.setCitedInPublication(previousSpecimen.getCitedInPublication());
		specimen.setCitedInPublicationYear(previousSpecimen.getCitedInPublicationYear());

		updateContentDependentLabels();
		setStateToDirty();
		thisPane.setStatus("Pasted specimen with id " + thisPane.previousSpecimen.getSpecimenId());
	}

	/**
	 * Set the values of the fields to the ones of the specimen.
	 */
	private void setValues() {
		log.debug("Setting values, specimenid is {}", specimen != null ? specimen.getSpecimenId() : null);
		this.setStatus("Setting values");

		if (specimen == null || !specimen.isFullyLoaded()) {
			this.dataLoadedSuccessfully = false;
			this.setStatus("Warning: Record not fully loaded. Save disabled.");
		}

		try {
			// Synchronize all declarative form fields from Specimen model to UI components
			bindingContext.readFrom(specimen);

			// Handle property-based initial default for location in collection
			String locationInCollectionPropertiesVal = Singleton.getSingletonInstance().getProperties().getProperties()
					.getProperty(ImageCaptureProperties.KEY_DISPLAY_COLLECTION);
			if (jComboBoxLocationInCollection != null && locationInCollectionPropertiesVal != null
					&& !locationInCollectionPropertiesVal.isEmpty()) {
				jComboBoxLocationInCollection.setSelectedItem(locationInCollectionPropertiesVal);
			}

			reloadGeoRefFieldValues();

			if (jTableNumbers != null) {
				jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));
				this.setupNumberJTableRenderer();
			}

			if (jTableCollectors != null) {
				jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));
				this.setupCollectorJTableRenderer();
			}

			if (jTableSpecimenParts != null) {
				jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
				setupSpecimenPartsJTableRenderer();
			}

			updateImageCount();
			updateContentDependentLabels();
			setWarnings();
			this.setStateToClean();
			if (dataLoadedSuccessfully) {
				this.setStatus("Loaded");
			} else {
				this.setStatus("Warning: Record not fully loaded. Save disabled.");
			}
		} catch (Exception e) {
			this.dataLoadedSuccessfully = false;
			log.error("Error setting values in SpecimenDetailsViewPane", e);
			this.setWarning("Error loading record values: " + e.getMessage());
		} finally {
			updateSaveButtonState();
		}
	}

	private void updateDeterminationCount() {
		int count = specimen.getDeterminations() == null ? 0 : specimen.getDeterminations().size();
		setDeterminationCount(count);
	}

	private void setDeterminationCount(int count) {
		String detSuffix = count == 1 ? "s" : "";
		getDetsJButton().setText(count + " Det" + detSuffix + ".");
		getDetsJButton().updateUI();
	}

	private JTextField getJTextFieldStatus() {
		if (jTextFieldStatus == null) {
			jTextFieldStatus = new JTextField("Status");
			jTextFieldStatus.setEditable(false);
			jTextFieldStatus.setEnabled(true);
		}
		return jTextFieldStatus;
	}

	/**
	 * Initializes jPanel, laying out the UI components declaratively using
	 * {@link FormBindingContext}.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel(new MigLayout("wrap 4, fillx"));

			// Cross-validation verifier for min & max elevation
			InputVerifier elevationVerifier = new InputVerifier() {
				@Override
				public boolean verify(JComponent input) {
					String minText = jTextFieldMinElevation.getText().trim();
					String maxText = jTextFieldMaxElevation.getText().trim();
					if (!minText.isEmpty()) {
						try {
							double min = Double.parseDouble(minText);
							if (!maxText.isEmpty()) {
								double max = Double.parseDouble(maxText);
								return min <= max;
							}
						} catch (NumberFormatException e) {
							return false;
						}
					}
					if (!maxText.isEmpty()) {
						try {
							double max = Double.parseDouble(maxText);
							if (!minText.isEmpty()) {
								double min = Double.parseDouble(minText);
								return min <= max;
							}
						} catch (NumberFormatException e) {
							return false;
						}
					}
					return true;
				}
			};

			// section: top information
			addBasicJLabel(jPanel, "Barcode");
			jPanel.add(bindingContext.bindReadOnlyTextField("Barcode", Specimen::getBarcode), "grow");

			addBasicJLabel(jPanel, "ID by");
			jPanel.add(
					bindingContext.bindComboBox("IdentifiedBy", () -> new SpecimenLifeCycle().getDistinctDeterminers(),
							Specimen::getIdentifiedBy, Specimen::setIdentifiedBy),
					"grow");

			// section: identification/determination
			addBasicJLabel(jPanel, "Nature of ID");
			jPanel.add(bindingContext.bindComboBox(Determination.class, "NatureOfId", NatureOfId.getNatureOfIdValues(),
					Specimen::getNatureOfId,
					(s, val) -> s.setNatureOfId(val == null || val.isEmpty() ? NatureOfId.EXPERT_ID : val), cb -> {
						cb.setSelectedItem(NatureOfId.EXPERT_ID);
						cb.setSelectedIndex(0);
					}), "grow");

			addBasicJLabel(jPanel, "ID Date");
			JTextField dateDeterminedField = bindingContext.bindTextField("DateIdentified", Specimen::getDateIdentified,
					Specimen::setDateIdentified,
					f -> f.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "ISODate", f)));
			jPanel.add(dateDeterminedField, "grow, span 1, split 2, sizegroup datedet");
			jPanel.add(getDetsJButton(), "sizegroup datedet");

			// section: family, classification
			addBasicJLabel(jPanel, "Order");
			jPanel.add(bindingContext.bindComboBox("HigherOrder", () -> HigherTaxonLifeCycle.selectDistinctOrder(),
					Specimen::getHigherOrder, (s, val) -> s.setHigherOrder(val == null ? "" : val)), "grow");

			addBasicJLabel(jPanel, "Family");
			jPanel.add(bindingContext.bindComboBox("Family", () -> HigherTaxonLifeCycle.selectDistinctFamily(),
					Specimen::getFamily, (s, val) -> s.setFamily(val == null ? "" : val)), "grow");

			addBasicJLabel(jPanel, "Subfamily");
			jPanel.add(bindingContext.bindComboBox("Subfamily", () -> HigherTaxonLifeCycle.selectDistinctSubfamily(""),
					Specimen::getSubfamily, (s, val) -> s.setSubfamily(val == null ? "" : val)), "grow");

			addBasicJLabel(jPanel, "Tribe");
			jPanel.add(bindingContext.bindTextField("Tribe", Specimen::getTribe, Specimen::setTribe), "grow");

			addBasicJLabel(jPanel, "Genus");
			jPanel.add(bindingContext.bindTextField("Genus", Specimen::getGenus, Specimen::setGenus), "grow");

			addBasicJLabel(jPanel, "Species");
			jPanel.add(bindingContext.bindTextField("SpecificEpithet", Specimen::getSpecificEpithet,
					Specimen::setSpecificEpithet), "grow");

			addBasicJLabel(jPanel, "Subspecies");
			jPanel.add(bindingContext.bindTextField("SubspecificEpithet", Specimen::getSubspecificEpithet,
					Specimen::setSubspecificEpithet), "grow");

			addBasicJLabel(jPanel, "Infrasubspecific Name");
			jPanel.add(bindingContext.bindTextField("InfraspecificEpithet", Specimen::getInfraspecificEpithet,
					Specimen::setInfraspecificEpithet), "grow");

			addBasicJLabel(jPanel, "Infrasubspecific Rank");
			jPanel.add(bindingContext.bindTextField("InfraspecificRank", Specimen::getInfraspecificRank,
					Specimen::setInfraspecificRank), "grow");

			addBasicJLabel(jPanel, "TypeStatus");
			jPanel.add(
					bindingContext.bindComboBox("TypeStatus", TypeStatus.getTypeStatusValues(), Specimen::getTypeStatus,
							(s, val) -> s
									.setTypeStatus(val == null || val.isEmpty() ? Specimen.STATUS_NOT_A_TYPE : val)),
					"grow");

			// section: locale
			addBasicJLabel(jPanel, "Verbatim Locality");
			jTextFieldVerbatimLocality = bindingContext.bindTextField("VerbatimLocality", Specimen::getVerbatimLocality,
					Specimen::setVerbatimLocality);
			jPanel.add(jTextFieldVerbatimLocality, "grow");

			addBasicJLabel(jPanel, "Country");
			jComboBoxCountry = bindingContext.bindComboBox("Country",
					() -> new SpecimenLifeCycle().getDistinctCountries(), Specimen::getCountry, Specimen::setCountry,
					false, null);
			jPanel.add(jComboBoxCountry, "grow");

			addBasicJLabel(jPanel, "State/Province");
			jComboBoxPrimaryDivision = bindingContext.bindComboBox("primaryDivison",
					() -> new SpecimenLifeCycle().getDistinctPrimaryDivisions(), Specimen::getPrimaryDivison,
					Specimen::setPrimaryDivison, false, null);
			jPanel.add(jComboBoxPrimaryDivision, "grow");

			jPanel.add(getJButtonSpecificLocality(), "align right");
			jTextFieldLocality = bindingContext.bindTextField("SpecificLocality", Specimen::getSpecificLocality,
					Specimen::setSpecificLocality);
			jPanel.add(jTextFieldLocality, "grow, span 2");

			addBasicJLabel(jPanel, "Latitude");
			jPanel.add(getTextFieldDecimalLat(), "grow");
			addBasicJLabel(jPanel, "Longitude");
			jPanel.add(getTextFieldDecimalLong(), "grow");

			addBasicJLabel(jPanel, "Method");
			jPanel.add(getMethodComboBox());
			addBasicJLabel(jPanel, "Datum");
			jPanel.add(getDatumComboBox());

			addBasicJLabel(jPanel, "Error Radius");
			jPanel.add(getTxtErrorRadius(), "span 1, split 2, sizegroup errorradius, grow");
			jPanel.add(getErrorUnitComboBox(), "sizegroup errorradius");
			jPanel.add(getJButtonGeoreference());
			jPanel.add(getJButtonPasteExcel());

			addBasicJLabel(jPanel, "Elevation from");
			jTextFieldMinElevation = bindingContext.bindLongField("VerbatimElevation", Specimen::getMinimum_elevation,
					Specimen::setMinimum_elevation, f -> f.setInputVerifier(elevationVerifier));
			jPanel.add(jTextFieldMinElevation, "grow");

			addBasicJLabel(jPanel, "to");
			jTextFieldMaxElevation = bindingContext.bindLongField(null, Specimen::getMaximum_elevation,
					Specimen::setMaximum_elevation, f -> f.setInputVerifier(elevationVerifier));
			jPanel.add(jTextFieldMaxElevation, "grow, span 1, split 2, sizegroup elevation");

			comboBoxElevUnits = bindingContext.bindComboBox("Elev_units", new String[]{"", "?", "m", "ft"},
					Specimen::getElev_units, (s, val) -> s.setElev_units(val == null ? "" : val));
			jPanel.add(comboBoxElevUnits, "sizegroup elevation");

			// section: collection
			addBasicJLabel(jPanel, "Author");
			jPanel.add(bindingContext.bindTextField("Authorship", Specimen::getAuthorship, Specimen::setAuthorship),
					"grow");

			addBasicJLabel(jPanel, "Collection");
			jPanel.add(bindingContext.bindComboBox("Collection", () -> new SpecimenLifeCycle().getDistinctCollections(),
					Specimen::getCollection, (s, val) -> s.setCollection(val == null ? "" : val)), "grow");

			// double row:
			addBasicJLabel(jPanel, "Collectors");
			jPanel.add(getJScrollPaneCollectors(), "span 2 2, grow");
			addBasicJLabel(jPanel, "Collecting Method");
			jPanel.add(getJButtonCollectorAdd(), "right");
			jPanel.add(bindingContext.bindTextField("CollectingMethod", Specimen::getCollectingMethod,
					Specimen::setCollectingMethod), "growx, top");

			// row
			addBasicJLabel(jPanel, "Verbatim date");
			jTextFieldDateNos = bindingContext.bindTextField("DateNOS", Specimen::getDateNos, Specimen::setDateNos);
			jPanel.add(jTextFieldDateNos, "grow");

			addBasicJLabel(jPanel, "yyyy/mm/dd");
			jPanel.add(bindingContext.bindTextField("ISODate", Specimen::getIsoDate, Specimen::setIsoDate), "grow");

			// row
			jPanel.add(getDateEmergedJButton(), "align label");
			jTextFieldDateEmerged = bindingContext.bindTextField("DateEmerged", Specimen::getDateEmerged,
					Specimen::setDateEmerged);
			jPanel.add(jTextFieldDateEmerged, "grow");
			addBasicJLabel(jPanel, "Date emerged note");
			jPanel.add(bindingContext.bindTextField("DateEmergedIndicator", Specimen::getDateEmergedIndicator,
					Specimen::setDateEmergedIndicator), "grow");

			// row
			jPanel.add(getDateCollectedJButton(), "align label");
			jTextFieldDateCollected = bindingContext.bindTextField("DateCollected", Specimen::getDateCollected,
					Specimen::setDateCollected);
			jPanel.add(jTextFieldDateCollected, "grow");
			addBasicJLabel(jPanel, "Date collected note");
			jPanel.add(bindingContext.bindTextField("DateCollectedIndicator", Specimen::getDateCollectedIndicator,
					Specimen::setDateCollectedIndicator), "grow");

			// section: pictured specifics
			addBasicJLabel(jPanel, "Habitat");
			jPanel.add(bindingContext.bindTextField("Habitat", Specimen::getHabitat, Specimen::setHabitat), "grow");

			addBasicJLabel(jPanel, "Microhabitat");
			jPanel.add(
					bindingContext.bindTextField("Microhabitat", Specimen::getMicrohabitat, Specimen::setMicrohabitat),
					"grow");

			addBasicJLabel(jPanel, "Life Stage");
			jPanel.add(bindingContext.bindComboBox("Lifestage", LifeStage.getLifeStageValues(), Specimen::getLifeStage,
					(s, val) -> s.setLifeStage(val == null || val.isEmpty() ? "adult" : val), cb -> {
						cb.setSelectedItem("adult");
						cb.setSelectedIndex(0);
					}), "grow");

			addBasicJLabel(jPanel, "Sex");
			jPanel.add(bindingContext.bindComboBox("Sex", Sex.getSexValues(), Specimen::getSex,
					(s, val) -> s.setSex(val == null ? "" : val)), "grow");

			// double row: Specimen Parts
			addBasicJLabel(jPanel, "Specimen Parts");
			jPanel.add(getJScrollPaneSpecimenParts(), "span 3 2, grow");
			jPanel.add(getJButtonAddPrep(), "right");

			// row
			addBasicJLabel(jPanel, "Publications");
			jPanel.add(getCitedInPublicationButton(), "grow");
			addBasicJLabel(jPanel, "Associated Taxon");
			jPanel.add(bindingContext.bindTextField("AssociatedTaxon", Specimen::getAssociatedTaxon,
					Specimen::setAssociatedTaxon), "grow");

			// row
			addBasicJLabel(jPanel, "Specimen Notes");
			jPanel.add(getJScrollPaneNotes(), "span 3, grow");

			// double row: Numbers & more
			addBasicJLabel(jPanel, "Numbers & more");
			jPanel.add(getNumbersJScrollPane(), "span 3 2, grow");
			jPanel.add(getJButtonNumbersAdd(), "right");

			// section: other fields
			addBasicJLabel(jPanel, "Inferences");
			jPanel.add(bindingContext.bindTextField("Inferences", Specimen::getInferences, Specimen::setInferences,
					f -> f.setBackground(MainFrame.BG_COLOR_ENT_FIELD)), "grow");

			addBasicJLabel(jPanel, "Created by");
			jPanel.add(bindingContext.bindReadOnlyTextField("Creator", Specimen::getCreatedBy), "grow");

			addBasicJLabel(jPanel, "Creation date");
			jPanel.add(bindingContext.bindReadOnlyTextField("DateCreated",
					s -> s.getDateCreated() != null ? s.getDateCreated().toString() : ""), "grow");

			addBasicJLabel(jPanel, "Last updated by");
			jTextFieldLastUpdatedBy = bindingContext.bindReadOnlyTextField("LastUpdatedBy", Specimen::getLastUpdatedBy);
			jPanel.add(jTextFieldLastUpdatedBy, "grow");

			addBasicJLabel(jPanel, "Last edit date");
			jTextFieldDateLastUpdated = bindingContext.bindReadOnlyTextField("DateLastUpdated",
					s -> s.getDateLastUpdated() != null ? s.getDateLastUpdated().toString() : "");
			jPanel.add(jTextFieldDateLastUpdated, "grow");

			addBasicJLabel(jPanel, "Workflow Status");
			jComboBoxWorkflowStatus = bindingContext.bindComboBox("WorkflowStatus",
					WorkFlowStatus.getWorkFlowStatusValues(), Specimen::getWorkFlowStatus,
					(s, val) -> s.setWorkFlowStatus(val == null ? "" : val), cb -> {
						cb.setEditable(false);
						cb.setBackground(MainFrame.BG_COLOR_QC_FIELD);
					});
			jPanel.add(jComboBoxWorkflowStatus);

			addBasicJLabel(jPanel, "Unnamed Form");
			jPanel.add(bindingContext.bindTextField("UnnamedForm", Specimen::getUnNamedForm, Specimen::setUnNamedForm),
					"grow");

			addBasicJLabel(jPanel, "Questions");
			jPanel.add(bindingContext.bindTextField("Questions", Specimen::getQuestions, Specimen::setQuestions,
					f -> f.setBackground(MainFrame.BG_COLOR_QC_FIELD)), "grow, span 3");

			jPanel.add(getAccordionDetailsPanel(), "grow, span 4");

			// section: controls
			int splitSize = isCopyPasteActivated() ? 6 : 4;
			if (this.supportsLinkToNahima()) {
				splitSize += 1;
			}
			jPanel.add(getJButtonHistory(), "span, split " + splitSize);
			if (this.supportsLinkToNahima()) {
				jPanel.add(getLinkToNahima());
			}
			if (isCopyPasteActivated()) {
				jPanel.add(getJButtonPaste());
			}
			jPanel.add(getJButtonPrevious(), "tag back");
			jPanel.add(getJButtonNext(), "tag next");
			if (isCopyPasteActivated()) {
				jPanel.add(getJButtonCopySave(), "tag apply");
			}
			jPanel.add(getSaveJButton(), "tag apply");
		}
		return jPanel;
	}

	private JButton getCitedInPublicationButton() {
		if (citedInPublicationButton == null) {
			citedInPublicationButton = new JButton("Manage Publication");
			SpecimenDetailsViewPane self = this;
			citedInPublicationButton.addActionListener(actionEvent -> {
				CitedInDialog dialog = new CitedInDialog(self.specimen.getCitedInPublication(),
						self.specimen.getCitedInPublicationLink(), self.specimen.getCitedInPublicationComment(),
						self.specimen.getCitedInPublicationYear());
				dialog.addCloseListener((type, source) -> {
					if (type == CloseType.OK) {
						self.specimen.setCitedInPublication(dialog.getCitedInPublication());
						self.specimen.setCitedInPublicationLink(dialog.getCitedInLink());
						self.specimen.setCitedInPublicationComment(dialog.getCitedInComment());
						self.specimen.setCitedInPublicationYear(dialog.getCitedInPublicationYear());
						self.setStateToDirty();
					}
				});
				dialog.setVisible(true);
			});
		}
		return citedInPublicationButton;
	}

	private JButton getJButtonPasteExcel() {
		if (pasteExcelButton == null) {
			pasteExcelButton = new JButton("Paste Excel");
			SpecimenDetailsViewPane self = this;
			pasteExcelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					try {
						self.getGeoreferenceDialog()
								.pasteFromExcel((String) clipboard.getData(DataFlavor.stringFlavor));
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

			addBasicJLabel(accordionContent, "Number of Images");
			jTextFieldImageCount = new JTextField();
			jTextFieldImageCount.setForeground(Color.BLACK);
			jTextFieldImageCount.setEnabled(false);
			accordionContent.add(jTextFieldImageCount, "grow");

			addBasicJLabel(accordionContent, "Migration Status");
			jTextFieldMigrationStatus = bindingContext.bindReadOnlyTextField("MigrationStatus",
					s -> s.isExported() ? WorkFlowStatus.STAGE_DONE : "");
			accordionContent.add(jTextFieldMigrationStatus, "grow");

			addBasicJLabel(accordionContent, "Collection", "tag label, right, span 4, split 5, sizegroup idrow");
			jComboBoxLocationInCollection = bindingContext.bindComboBox("LocationInCollection",
					LocationInCollection.getLocationInCollectionValues(), Specimen::getLocationInCollection,
					(s, val) -> {
						if (val != null) {
							s.setLocationInCollection(val);
						}
					}, cb -> cb.setEditable(false));
			accordionContent.add(jComboBoxLocationInCollection, "sizegroup idrow");
			accordionContent.add(getjButtonGBIFView(), "tag label, right, sizegroup idrow");
			accordionContent.add(getjTextFieldGBIFTaxonId(), "sizegroup idrow, grow");
			accordionContent.add(getDBIdLabel(), "sizegroup idrow");

			addBasicJLabel(accordionContent, "ID Remark");
			accordionContent.add(bindingContext.bindTextField("IdentificationRemarks",
					Specimen::getIdentificationRemarks, Specimen::setIdentificationRemarks), "grow, span 3");

			addBasicJLabel(accordionContent, "Valid Dist.");
			accordionContent.add(bindingContext.bindCheckBox("ValidDistributionFlag",
					Specimen::getValidDistributionFlag, Specimen::setValidDistributionFlag));

			addBasicJLabel(accordionContent, "Drawer Number");
			accordionContent.add(
					bindingContext.bindTextField("DrawerNumber", Specimen::getDrawerNumber, Specimen::setDrawerNumber),
					"grow");

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

	private JLabel getDBIdLabel() {
		if (jLabelDBId == null) {
			jLabelDBId = new JLabel();
		}
		updateDBIdLabel();
		return jLabelDBId;
	}

	private void updateDBIdLabel() {
		if (this.jLabelDBId != null) {
			this.jLabelDBId.setText("DataBase ID: " + specimen.getSpecimenId());
		}
	}

	private void addBasicJLabel(JPanel target, String labelText) {
		addBasicJLabel(target, labelText, "tag label, right");
	}

	private void addBasicJLabel(JPanel target, String labelText, String constraints) {
		JLabel label = new JLabel();
		label.setText(labelText.concat(":"));
		target.add(label, constraints);
	}

	public JButton getSaveJButton() {
		if (jButtonSave == null) {
			jButtonSave = new JButton("Save");
			jButtonSave.setMnemonic(KeyEvent.VK_S);
			jButtonSave.addActionListener(e -> thisPane.save());
			updateSaveButtonState();
		}
		return jButtonSave;
	}

	public void updateSaveButtonState() {
		if (jButtonSave != null) {
			if (!isVerified) {
				jButtonSave.setEnabled(false);
				jButtonSave.setToolTipText("Save is disabled: Verifying latest version from database...");
			} else if (!dataLoadedSuccessfully || specimen == null || !specimen.isFullyLoaded()) {
				jButtonSave.setEnabled(false);
				jButtonSave.setToolTipText("Save is disabled: Record was not fully loaded from database.");
			} else if (!specimen.isEditable(Singleton.getSingletonInstance().getUser())) {
				jButtonSave.setEnabled(false);
				jButtonSave.setText(specimen.getWorkFlowStatus());
			} else {
				jButtonSave.setEnabled(true);
				jButtonSave.setText("Save");
				jButtonSave.setToolTipText("Save changes to this record to the database. No fields should "
						+ "have red backgrounds before you save.");
			}
		}
	}

	public Specimen getSpecimen() {
		return this.specimen;
	}

	public boolean isVerified() {
		return this.isVerified;
	}

	public void setVerified(boolean verified) {
		this.isVerified = verified;
		updateSaveButtonState();
	}

	public String getStatusText() {
		return jTextFieldStatus != null ? jTextFieldStatus.getText() : null;
	}

	public static boolean isSameTimestamp(Date d1, Date d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null || d2 == null) {
			return false;
		}
		return Math.abs(d1.getTime() - d2.getTime()) < 1000;
	}

	/**
	 * Asynchronously verifies that the cached specimen currently shown is up to
	 * date with the database. If stale, reloads the fresh entity and updates UI
	 * bindings.
	 *
	 * @return CompletableFuture completing with true when verified/reloaded, false
	 *         on error
	 */
	public CompletableFuture<Boolean> verifyFreshnessAsync() {
		if (specimen == null || specimen.getSpecimenId() == null) {
			this.isVerified = true;
			updateSaveButtonState();
			return CompletableFuture.completedFuture(true);
		}
		this.isVerified = false;
		updateSaveButtonState();
		this.setStatus("Verifying latest version from database...");

		Long id = specimen.getSpecimenId();
		Date cachedDate = specimen.getDateLastUpdated();
		Integer cachedVersion = specimen.getVersion();

		return CompletableFuture.supplyAsync(() -> {
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			Date dbDate = sls.findDateLastUpdated(id);
			Integer dbVersion = sls.findVersion(id);
			boolean isFresh = isSameTimestamp(cachedDate, dbDate) && Objects.equals(cachedVersion, dbVersion);
			if (!isFresh) {
				return sls.findById(id);
			}
			return null;
		}).thenApplyAsync(freshSpecimen -> {
			this.isVerified = true;
			if (freshSpecimen != null && freshSpecimen.isFullyLoaded()) {
				this.specimen = freshSpecimen;
				if (this.specimenController != null) {
					this.specimenController.setSpecimen(freshSpecimen);
				}
				SpecimenCache.put(freshSpecimen);
				setValues();
				this.setStatus("Reloaded latest version from database.");
			} else {
				this.setStatus("Loaded");
				updateSaveButtonState();
			}
			return true;
		}, SwingUtilities::invokeLater).exceptionally(ex -> {
			log.error("Freshness verification failed for specimen id " + id, ex);
			this.isVerified = true;
			updateSaveButtonState();
			return false;
		});
	}

	/**
	 * Synchronously waits for freshness verification to complete. Useful for tests.
	 *
	 * @return true if verification succeeded, false otherwise
	 */
	public boolean verifyFreshness() {
		try {
			CompletableFuture<Boolean> future = verifyFreshnessAsync();
			return future != null && future.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Freshness verification failed", e);
			return false;
		}
	}

	public boolean isSaveButtonEnabled() {
		return jButtonSave != null && jButtonSave.isEnabled();
	}

	public boolean isDataLoadedSuccessfully() {
		return dataLoadedSuccessfully;
	}

	private JTextField getTextFieldDecimalLat() {
		if (textFieldDecimalLat == null) {
			textFieldDecimalLat = new JTextField();
			textFieldDecimalLat.setEditable(specimen.isEditable());
			textFieldDecimalLat.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if (!textFieldDecimalLat.getText().trim().isEmpty()) {
						getGeoreferenceDialog().getGeoReference()
								.setDecLat(BigDecimal.valueOf(Double.parseDouble(textFieldDecimalLat.getText())));
					} else {
						getGeoreferenceDialog().getGeoReference().setDecLat(null);
					}
					getGeoreferenceDialog().loadData();
				}
			});
			textFieldDecimalLat.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
			});
		}
		return textFieldDecimalLat;
	}

	private JTextField getTextFieldDecimalLong() {
		if (textFieldDecimalLong == null) {
			textFieldDecimalLong = new JTextField();
			textFieldDecimalLong.setEditable(specimen.isEditable());
			textFieldDecimalLong.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if (!textFieldDecimalLong.getText().trim().isEmpty()) {
						getGeoreferenceDialog().getGeoReference()
								.setDecLong(BigDecimal.valueOf(Double.parseDouble(textFieldDecimalLong.getText())));
					} else {
						getGeoreferenceDialog().getGeoReference().setDecLong(null);
					}
					getGeoreferenceDialog().loadData();
				}
			});
			textFieldDecimalLong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
			});
		}
		return textFieldDecimalLong;
	}

	private JComboBox<String> getMethodComboBox() {
		if (cbMethod == null) {
			cbMethod = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"not recorded", "unknown", "GEOLocate",
					"Geoportal", "Google Earth", "Google Maps", "Gazeteer", "GPS", "Label Data", "Open Street Map",
					"Wikipedia", "MaNIS/HertNet/ORNIS Georeferencing Guidelines"}));
			cbMethod.setEditable(specimen.isEditable());
			cbMethod.addActionListener(e -> {
				getGeoreferenceDialog().getGeoReference().setGeorefmethod((String) cbMethod.getSelectedItem());
				getGeoreferenceDialog().loadData();
				getGeoreferenceDialog().setState();
				setStateToDirty();
			});
		}
		return cbMethod;
	}

	private JComboBox<String> getDatumComboBox() {
		if (cbDatum == null) {
			ComboBoxModel<String> datumModel = new ListComboBoxModel<>(LatLong.getDatumValues());
			cbDatum = new JComboBox<>(datumModel);
			cbDatum.setSelectedItem("WGS84");
			cbDatum.setEditable(specimen.isEditable());
			cbDatum.addActionListener(e -> {
				getGeoreferenceDialog().getGeoReference().setDatum((String) cbDatum.getSelectedItem());
				getGeoreferenceDialog().loadData();
				setStateToDirty();
			});
		}
		return cbDatum;
	}

	private JTextField getTxtErrorRadius() {
		if (txtErrorRadius == null) {
			txtErrorRadius = new JTextField();
			txtErrorRadius.setEditable(specimen.isEditable());
			txtErrorRadius.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					String result = txtErrorRadius.getText();
					if (!result.trim().isEmpty()) {
						try {
							getGeoreferenceDialog().getGeoReference().setMaxErrorDistance(Integer.parseInt(result));
						} catch (NumberFormatException ignored) {
						}
					} else {
						getGeoreferenceDialog().getGeoReference().setMaxErrorDistance(null);
					}
					getGeoreferenceDialog().loadData();
				}
			});
			txtErrorRadius.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					setStateToDirty();
				}
			});
		}
		return txtErrorRadius;
	}

	private JComboBox<String> getErrorUnitComboBox() {
		if (comboBoxErrorUnits == null) {
			comboBoxErrorUnits = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"m", "ft", "km", "mi", "yd"}));
			comboBoxErrorUnits.setSelectedItem("m");
			comboBoxErrorUnits.setEditable(specimen.isEditable());
			comboBoxErrorUnits.addActionListener(e -> {
				getGeoreferenceDialog().getGeoReference()
						.setMaxErrorUnits((String) comboBoxErrorUnits.getSelectedItem());
				getGeoreferenceDialog().loadData();
				setStateToDirty();
			});
		}
		return comboBoxErrorUnits;
	}

	private JScrollPane getJScrollPaneCollectors() {
		if (jScrollPaneCollectors == null) {
			jScrollPaneCollectors = this.getBasicWrapperJScrollPane();
			jScrollPaneCollectors.setViewportView(getJTableCollectors());
			jScrollPaneCollectors.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jScrollPaneCollectors;
	}

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
			jTableCollectors.addListener(actionEvent -> thisPane.setStateToDirty());
			jTableCollectors.enableDeleteability();
		}
		return jTableCollectors;
	}

	private void setupCollectorJTableRenderer() {
		CollectorLifeCycle cls = new CollectorLifeCycle();
		JComboBox<String> jComboBoxCollector = new JComboBox<>(cls.getDistinctCollectors());
		jComboBoxCollector.setEditable(specimen.isEditable());
		AutoCompleteDecorator.decorate(jComboBoxCollector);
		jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(jComboBoxCollector));
	}

	private JScrollPane getJScrollPaneSpecimenParts() {
		if (jScrollPaneSpecimenParts == null) {
			jScrollPaneSpecimenParts = this.getBasicWrapperJScrollPane();
			jScrollPaneSpecimenParts.setViewportView(getJTableSpecimenParts());
			jScrollPaneSpecimenParts.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
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

			log.debug("Specimen parts size: {}", specimen.getSpecimenParts().size());
			jTableSpecimenParts.setObjectName("Specimen Part");
			jTableSpecimenParts.setParentPane(thisPane);
			jTableSpecimenParts.addListener(actionEvent -> thisPane.setStateToDirty());
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
		getJTableSpecimenParts().getColumnModel().getColumn(4)
				.setCellEditor(new ButtonEditor(ButtonEditor.OPEN_SPECIMENPARTATTRIBUTES, this));
	}

	private JScrollPane getNumbersJScrollPane() {
		if (jScrollPaneNumbers == null) {
			jScrollPaneNumbers = this.getBasicWrapperJScrollPane();
			jScrollPaneNumbers.setViewportView(getNumberJTable());
			jScrollPaneNumbers.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jScrollPaneNumbers;
	}

	private JTable getNumberJTable() {
		if (jTableNumbers == null) {
			try {
				jTableNumbers = new JTableWithRowBorder(new NumberTableModel(specimen.getNumbers()));
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
			jTableNumbers.addListener(actionEvent -> thisPane.setStateToDirty());
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

			// Better mouse handling with pattern matching
			jTableNumbers.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {
						int row = jTableNumbers.rowAtPoint(e.getPoint());
						int col = jTableNumbers.columnAtPoint(e.getPoint());
						if (row >= 0 && col >= 0) {
							jTableNumbers.changeSelection(row, col, false, false);
							SwingUtilities.invokeLater(() -> {
								jTableNumbers.editCellAt(row, col);
								Component editor = jTableNumbers.getEditorComponent();
								if (editor instanceof JTextField tf) {
									tf.selectAll();
								}
							});
						}
					}
				}
			});
		}
		return jTableNumbers;
	}

	private void setupNumberJTableRenderer() {
		JTextField field1 = new JTextField();
		field1.setEditable(specimen.isEditable());
		field1.setInputVerifier(MetadataRetriever.getInputVerifier(Number.class, "Number", field1));
		field1.setVerifyInputWhenFocusTarget(true);
		jTableNumbers.setColumnSelectionAllowed(true);
		jTableNumbers.setRowSelectionAllowed(true);
		jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_NUMBER)
				.setCellEditor(new ValidatingTableCellEditor(field1));

		JComboBox<String> jComboNumberTypes = new JComboBox<>();
		String[] types = NumberLifeCycle.getDistinctTypes();
		jComboNumberTypes.setModel(new DefaultComboBoxModel<>(types));
		jComboNumberTypes.setEditable(specimen.isEditable());
		TableColumn typeColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
		AutoCompleteDecorator.decorate(jComboNumberTypes);
		typeColumn.setCellEditor(new ComboBoxCellEditor(jComboNumberTypes));
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for pick list of number types.");
		typeColumn.setCellRenderer(renderer);
	}

	private JButton getJButtonNumbersAdd() {
		if (jButtonNumbersAdd == null) {
			jButtonNumbersAdd = new JButton();
			jButtonNumbersAdd.setText("+");
			URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
			try {
				jButtonNumbersAdd.setText("");
				jButtonNumbersAdd.setIcon(new ImageIcon(iconFile));
				jButtonNumbersAdd.addActionListener(e -> {
					((NumberTableModel) jTableNumbers.getModel()).addNumber(new Number(specimen, "", ""));
					thisPane.setStateToDirty();
					JTableCellTabbing.setTabMapping(jTableNumbers, 0, jTableNumbers.getRowCount(), 0, 2);
				});
			} catch (Exception e) {
				jButtonNumbersAdd.setText("+");
			}
		}
		return jButtonNumbersAdd;
	}

	private boolean supportsLinkToNahima() {
		return Singleton.getSingletonInstance().getProperties().getProperties()
				.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL) != null && specimen.isExported()
				&& specimen.getNahimaId() != null && !Objects.equals(specimen.getNahimaId(), "");
	}

	private JButton getLinkToNahima() {
		if (jButtonNahimaLink == null) {
			jButtonNahimaLink = new JButton();
			jButtonNahimaLink.setText("Open in Nahima");
			jButtonNahimaLink.addActionListener(e -> {
				String urlString = "";
				try {
					Properties properties = Singleton.getSingletonInstance().getProperties().getProperties();
					urlString = properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL, "https://nahima.ethz.ch");

					NahimaManager nahimaManager = new NahimaManager(
							properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL),
							properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_USER),
							properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_PASSWORD),
							properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_CLIENT_ID),
							properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_CLIENT_SECRET), false, false);

					JSONObject nahimaEntry = nahimaManager.findObjectByGlobalObjectId(specimen.getNahimaId());
					urlString = urlString + "/#/detail/" + nahimaEntry.getString("_uuid");
				} catch (Exception ex) {
					log.error("Failed to assemble URL to Nahima", ex);
				}
				try {
					Desktop.getDesktop().browse(URI.create(urlString));
				} catch (Exception ex) {
					log.error("Failed opening entry in Nahima", ex);
				}
			});
		}
		return jButtonNahimaLink;
	}

	private JButton getJButtonGeoreference() {
		if (jButtonGeoReference == null) {
			jButtonGeoReference = new JButton();
			try {
				updateJButtonGeoreference();
				SpecimenDetailsViewPane self = this;
				jButtonGeoReference.addActionListener(e -> {
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
					georefDialog.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentHidden(ComponentEvent e1) {
							updateJButtonGeoreference();
							super.componentHidden(e1);
							reloadGeoRefFieldValues();
						}
					});
				});
			} catch (Exception e) {
				log.error("Error creating georeference button", e);
			}
		}
		return jButtonGeoReference;
	}

	private GeoreferenceDialog getGeoreferenceDialog() {
		if (this.georeferenceDialog == null) {
			Set<LatLong> georeferences = specimen.getLatLong();
			LatLong georeference = georeferences.iterator().next();
			if (georeference.isEmpty()) {
				georeference.setDatum((String) this.getDatumComboBox().getSelectedItem());
			}
			georeference.setSpecimen(specimen);
			this.georeferenceDialog = new GeoreferenceDialog(georeference, thisPane);
		}
		return this.georeferenceDialog;
	}

	/**
	 * Sets the location data on the corresponding controls. Modern Java 17 pattern
	 * matching for instanceof is applied.
	 */
	public void setLocationData(String verbatimLoc, String specificLoc, String country, String stateProvince,
			String lat, String lng) {
		log.debug(String.join(" ", verbatimLoc, specificLoc, country, stateProvince, lat, lng));
		Map<Component, String> defaultsMapImmutable = Map.ofEntries(
				Map.entry(this.jTextFieldVerbatimLocality, verbatimLoc),
				Map.entry(this.jTextFieldLocality, specificLoc), Map.entry(this.jComboBoxCountry, country),
				Map.entry(this.jComboBoxPrimaryDivision, stateProvince), Map.entry(this.getTextFieldDecimalLat(), lat),
				Map.entry(this.getTextFieldDecimalLong(), lng));

		Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
		defaultsMapImmutable.forEach((field, value) -> {
			try {
				if (field instanceof JTextField tf) {
					if (tf.getText().trim().isEmpty()
							|| settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
						tf.setText(value);
					}
				} else if (field instanceof JComboBox<?> cb) {
					String content = "";
					if (cb.getSelectedItem() != null) {
						content = cb.getSelectedItem().toString();
					}
					if (content.trim().isEmpty()
							|| settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
						cb.setSelectedItem(value);
					}
				}
			} catch (Exception e) {
				log.error("Failed to set value for location field", e);
			}
		});

		this.updateJButtonGeoreference();
	}

	private void updateJButtonGeoreference() {
		if (jButtonGeoReference != null) {
			if (specimen.getLatLong() != null && !specimen.getLatLong().isEmpty()
					&& !specimen.getLatLong().iterator().next().isEmpty()) {
				jButtonGeoReference.setText("✅ Georeference (1)");
			} else {
				jButtonGeoReference.setText("❔ Georeference (0)");
			}
			jButtonGeoReference.updateUI();
		}
	}

	private JButton getJButtonCollectorAdd() {
		if (jButtonCollectorAdd == null) {
			jButtonCollectorAdd = new JButton();
			jButtonCollectorAdd.setText("+");
			URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png");
			try {
				jButtonCollectorAdd.setText("");
				jButtonCollectorAdd.setIcon(new ImageIcon(iconFile));
				jButtonCollectorAdd.addActionListener(e -> {
					log.debug("adding a new collector........");
					((CollectorTableModel) jTableCollectors.getModel()).addCollector(new Collector(specimen, ""));
					thisPane.setStateToDirty();
				});
			} catch (Exception e) {
				jButtonCollectorAdd.setText("+");
			}
		}
		return jButtonCollectorAdd;
	}

	private JScrollPane getJScrollPaneNotes() {
		if (jScrollPaneNotes == null) {
			jScrollPaneNotes = this.getBasicWrapperJScrollPane();
			jScrollPaneNotes.setViewportView(bindingContext.bindTextArea("SpecimenNotes", Specimen::getSpecimenNotes,
					Specimen::setSpecimenNotes, 3));
		}
		return jScrollPaneNotes;
	}

	private JButton getJButtonAddPrep() {
		if (jButtonAddPreparationType == null) {
			jButtonAddPreparationType = new JButton("Add Prep");
			jButtonAddPreparationType.setMnemonic(KeyEvent.VK_A);
			jButtonAddPreparationType.addActionListener(e -> {
				log.debug("Adding new SpecimenPart");
				SpecimenPart newPart = new SpecimenPart();
				newPart.setPreserveMethod(Singleton.getSingletonInstance().getProperties().getProperties()
						.getProperty(ImageCaptureProperties.KEY_DEFAULT_PREPARATION));
				newPart.setSpecimen(specimen);
				SpecimenPartLifeCycle spls = new SpecimenPartLifeCycle();
				try {
					spls.persist(newPart);
					specimen.getSpecimenParts().add(newPart);
					((AbstractTableModel) jTableSpecimenParts.getModel()).fireTableDataChanged();
					log.debug("Added new SpecimenPart");
				} catch (SaveFailedException e1) {
					log.error("Failed to save new SpecimenPart", e1);
				}
			});
		}
		return jButtonAddPreparationType;
	}

	private JButton getJButtonHistory() {
		if (jButtonHistory == null) {
			jButtonHistory = new JButton();
			jButtonHistory.setText("History");
			jButtonHistory.setToolTipText("Show the history of who edited this record");
			jButtonHistory.setMnemonic(KeyEvent.VK_H);
			jButtonHistory.addActionListener(e -> {
				TrackingLifeCycle tls = new TrackingLifeCycle();
				EventLogFrame logViewer = new EventLogFrame(tls.findBySpecimen(specimen));
				logViewer.pack();
				logViewer.setVisible(true);
			});
		}
		return jButtonHistory;
	}

	private JButton getJButtonPaste() {
		if (jButtonPaste == null) {
			jButtonPaste = new JButton();
			jButtonPaste.setText("Paste");
			jButtonPaste.setToolTipText("Paste previous record values into this screen");
			jButtonPaste.setMnemonic(KeyEvent.VK_V);
			jButtonPaste.addActionListener(e -> {
				previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache;
				pastePreviousRecord();
			});
			this.updateJButtonPaste();
		}
		return jButtonPaste;
	}

	private void updateJButtonPaste() {
		if (jButtonPaste != null) {
			jButtonPaste
					.setEnabled(!(this.previousSpecimen == null && ImageCaptureApp.lastEditedSpecimenCache == null));
		}
	}

	private JButton getJButtonCopySave() {
		if (jButtonCopy == null) {
			jButtonCopy = new JButton();
			jButtonCopy.setText("Save & Copy");
			jButtonCopy.setToolTipText("Copy the values of this record after saving it");
			jButtonCopy.setMnemonic(KeyEvent.VK_K);
			jButtonCopy.addActionListener(e -> {
				if (thisPane.save()) {
					ImageCaptureApp.lastEditedSpecimenCache = thisPane.specimen;
					thisPane.setStatus("Saved & copied specimen with id " + thisPane.specimen.getSpecimenId());
				}
			});
		}
		return jButtonCopy;
	}

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
			jButtonNext.setEnabled(specimenController != null && specimenController.hasNextSpecimenInTable());
			jButtonNext.addActionListener(e -> thisPane.gotoNextSpecimen());
		}
		return jButtonNext;
	}

	private void gotoNextSpecimen() {
		try {
			thisPane.setStatus("Switching to next specimen...");
			if (thisPane.specimenController.openNextSpecimenInTable()) {
				thisPane.setVisible(false);
				thisPane.invalidate();
			} else {
				thisPane.setWarning("No next specimen available.");
			}
		} catch (Exception e1) {
			log.error("Failed to move to next specimen", e1);
		} finally {
			try {
				thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} catch (Exception ex) {
				log.error("Error restoring cursor", ex);
			}
		}
	}

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
			jButtonPrevious.setEnabled(specimenController != null && specimenController.hasPreviousSpecimenInTable());
			jButtonPrevious.addActionListener(e -> thisPane.gotoPreviousSpecimen());
		}
		return jButtonPrevious;
	}

	private void gotoPreviousSpecimen() {
		try {
			thisPane.setStatus("Switching to previous specimen...");
			if (thisPane.specimenController.openPreviousSpecimenInTable()) {
				thisPane.setVisible(false);
				thisPane.invalidate();
			} else {
				thisPane.setWarning("No previous specimen available.");
			}
		} catch (Exception e1) {
			log.error("Failed to move to previous specimen", e1);
		}
	}

	private void setStateToClean() {
		state = STATE_CLEAN;
		if (specimenController != null && specimenController.isInTable()) {
			if (jButtonNext != null) {
				jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable());
			}
			if (jButtonPrevious != null) {
				jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable());
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

	public boolean isClean() {
		return state == STATE_CLEAN;
	}

	private JButton getDetsJButton() {
		if (jButtonDeterminations == null) {
			jButtonDeterminations = new JButton();
			jButtonDeterminations.setText("Dets.");
			jButtonDeterminations.setMnemonic(KeyEvent.VK_D);
			jButtonDeterminations.addActionListener(e -> {
				DeterminationFrame dets = new DeterminationFrame(specimen);
				dets.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentHidden(ComponentEvent e1) {
						updateDeterminationCount();
						super.componentHidden(e1);
					}
				});
				dets.setVisible(true);
			});
		}
		return jButtonDeterminations;
	}

	private JScrollPane getBasicWrapperJScrollPane() {
		JScrollPane pane = new JScrollPane();
		pane.addMouseWheelListener(new MouseWheelScrollListener(pane));
		int maxHeight = Integer.parseInt(Singleton.getSingletonInstance().getProperties().getProperties()
				.getProperty(ImageCaptureProperties.KEY_MAX_FIELD_HEIGHT));
		pane.setMaximumSize(new Dimension(1000, maxHeight));
		return pane;
	}

	private JButton getDateEmergedJButton() {
		if (dateEmergedButton == null) {
			dateEmergedButton = new JButton();
			dateEmergedButton.setText("Date Emerged");
			dateEmergedButton.setToolTipText("Fill date emerged with data from verbatim date");
			dateEmergedButton.addActionListener(e -> {
				if (jTextFieldDateNos.getText().isEmpty()) {
					jTextFieldDateNos.setText(jTextFieldDateEmerged.getText());
				} else {
					jTextFieldDateEmerged.setText(jTextFieldDateNos.getText());
				}
			});
		}
		return dateEmergedButton;
	}

	private JButton getDateCollectedJButton() {
		if (dateCollectedButton == null) {
			dateCollectedButton = new JButton();
			dateCollectedButton.setText("Date Collected");
			dateCollectedButton.setToolTipText("Fill date collected with data from verbatim date");
			dateCollectedButton.addActionListener(e -> {
				if (jTextFieldDateNos.getText().isEmpty()) {
					jTextFieldDateNos.setText(jTextFieldDateCollected.getText());
				} else {
					jTextFieldDateCollected.setText(jTextFieldDateNos.getText());
				}
			});
		}
		return dateCollectedButton;
	}

	private JButton getJButtonSpecificLocality() {
		if (jButtonSpecificLocality == null) {
			jButtonSpecificLocality = new JButton();
			jButtonSpecificLocality.setText("Specific Locality");
			jButtonSpecificLocality.setToolTipText("Fill specific locality with data from verbatim locality");
			jButtonSpecificLocality.addActionListener(e -> {
				if (jTextFieldVerbatimLocality.getText().isEmpty()) {
					if (jTextFieldLocality.getText().isEmpty()) {
						jTextFieldLocality.setText("[no specific locality data]");
					}
					jTextFieldVerbatimLocality.setText(jTextFieldLocality.getText());
				} else {
					jTextFieldLocality.setText(jTextFieldVerbatimLocality.getText());
				}
			});
		}
		return jButtonSpecificLocality;
	}

	private void updateImageCount() {
		int imageCount = specimen.getICImages() != null ? specimen.getICImages().size() : 0;
		if (jTextFieldImageCount != null) {
			jTextFieldImageCount.setText(Integer.toString(imageCount));
			jTextFieldImageCount.setForeground(imageCount > 1 ? Color.RED : Color.BLACK);
		}
	}

	private void autocompleteGeoDataFromGeoreference() {
		HashMap<String, String> primaryDivisionMapping = new HashMap<>();
		primaryDivisionMapping.put("Grisons", "Graubünden");
		primaryDivisionMapping.put("St. Gallen", "Sankt Gallen");
		primaryDivisionMapping.put("Tessin", "Ticino");
		primaryDivisionMapping.put("Wallis", "Valais");
		primaryDivisionMapping.put("Zurich", "Zürich");
		primaryDivisionMapping.put("Occitania", "Occitanie");

		if (this.specimen.getLatLong() == null || this.specimen.getLatLong().isEmpty()) {
			return;
		}
		LatLong georeff = this.specimen.getLatLong().iterator().next();
		if (georeff.getDecLat() != null && georeff.getDecLong() != null) {
			new Thread(() -> {
				log.debug("Fetching address from GeoNames");
				GeoNamesUtility geoNamesUtility = new GeoNamesUtility();
				try {
					Map<String, Object> data = geoNamesUtility.reverseSearchValues(georeff.getDecLat(),
							georeff.getDecLong(),
							new ArrayList<>(Arrays.asList("countryCode", "countryName", "adminName1")));
					if (data != null) {
						log.debug("Got address from GeoNames: {}", data);
						if (jComboBoxCountry.getSelectedItem() == null
								|| jComboBoxCountry.getSelectedItem().equals("")) {
							String countryName = (new ISO3166LifeCycle())
									.findByCountryCode((String) data.get("countryCode")).getCountryName();
							SwingUtilities.invokeLater(() -> jComboBoxCountry.setSelectedItem(countryName));
						}
						if (jComboBoxPrimaryDivision.getSelectedItem() == null
								|| jComboBoxPrimaryDivision.getSelectedItem().equals("")) {
							SwingUtilities.invokeLater(
									() -> jComboBoxPrimaryDivision.setSelectedItem(data.get("adminName1")));
						}
						return;
					}
				} catch (Exception e) {
					log.error("Failed to fetch geodata using GeoNames", e);
				}

				log.debug("Fetching address from openstreetmap");
				Map<String, Object> data = OpenStreetMapUtility.getInstance().reverseSearchValues(georeff.getDecLat(),
						georeff.getDecLong(),
						new ArrayList<>(Arrays.asList("address.county", "address.state", "address.country")));
				if (data != null) {
					log.debug("Got address from openstreetmap: {}", data);
					if (jComboBoxCountry.getSelectedItem() == null || jComboBoxCountry.getSelectedItem().equals("")) {
						SwingUtilities.invokeLater(() -> jComboBoxCountry.setSelectedItem(data.get("address.country")));
					}
					if (jComboBoxPrimaryDivision.getSelectedItem() == null
							|| jComboBoxPrimaryDivision.getSelectedItem().equals("")) {
						String primaryDivision = (String) data.get("address.state");
						if (primaryDivision == null || primaryDivision.isEmpty()) {
							primaryDivision = (String) data.get("address.county");
						}
						String finalDivision = primaryDivisionMapping.getOrDefault(primaryDivision, primaryDivision);
						SwingUtilities.invokeLater(() -> jComboBoxPrimaryDivision.setSelectedItem(finalDivision));
					}
				}
			}).start();
		}
	}

	private void updateContentDependentLabels() {
		updateJButtonGeoreference();
		updateDeterminationCount();
		updateJButtonPaste();
		updateDBIdLabel();
		updateSaveButtonState();
	}

	/**
	 * Creates or returns the GBIF Taxon ID field with non-blocking
	 * java.net.http.HttpClient validation.
	 */
	public JTextField getjTextFieldGBIFTaxonId() {
		if (jTextFieldGBIFTaxonId == null) {
			jTextFieldGBIFTaxonId = bindingContext.bindTextField("GBIFTaxonId", Specimen::getGBIFTaxonId,
					Specimen::setGBIFTaxonId, field -> {
						field.setInputVerifier(new InputVerifier() {
							@Override
							public boolean verify(JComponent input) {
								String taxonId = jTextFieldGBIFTaxonId.getText().trim();
								if (taxonId.isEmpty()) {
									jTextFieldGBIFTaxonId.setBackground(Color.WHITE);
									return true;
								}
								try {
									URI uri = URI.create("https://www.gbif.org/species/" + taxonId);
									HttpRequest request = HttpRequest.newBuilder(uri)
											.method("HEAD", HttpRequest.BodyPublishers.noBody())
											.timeout(Duration.ofSeconds(3)).build();
									HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding())
											.thenAccept(response -> SwingUtilities.invokeLater(() -> {
												if (response.statusCode() == 404) {
													jTextFieldGBIFTaxonId.setBackground(MainFrame.BG_COLOR_ERROR);
												} else {
													jTextFieldGBIFTaxonId.setBackground(Color.WHITE);
												}
												jTextFieldGBIFTaxonId.revalidate();
											})).exceptionally(ex -> {
												log.error("Error verifying GBIF Taxon ID asynchronously", ex);
												return null;
											});
								} catch (Exception e) {
									log.error("Error building GBIF Taxon URI", e);
									return false;
								}
								return true;
							}

							@Override
							public boolean shouldYieldFocus(JComponent input) {
								return true;
							}
						});
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
			jButtonGBIFView.addActionListener(e -> {
				String taxonId = getjTextFieldGBIFTaxonId().getText().trim();
				if (!taxonId.isEmpty()) {
					String url = "https://www.gbif.org/species/" + taxonId;
					try {
						Desktop.getDesktop().browse(URI.create(url));
					} catch (Exception ex) {
						log.error("Error opening GBIF view", ex);
					}
				} else {
					JOptionPane.showMessageDialog(thisPane, "No GBIF Taxon ID provided.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		return jButtonGBIFView;
	}
}
