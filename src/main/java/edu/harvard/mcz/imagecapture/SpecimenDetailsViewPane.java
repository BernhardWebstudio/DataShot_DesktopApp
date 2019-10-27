/**
 * SpecimenDetailsViewPane.java
 * edu.harvard.mcz.imagecapture
 * Copyright © 2009 President and Fellows of Harvard College
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Author: Paul J. Morris
 */
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.data.Collector;
import edu.harvard.mcz.imagecapture.data.CollectorLifeCycle;
import edu.harvard.mcz.imagecapture.data.CollectorTableModel;
import edu.harvard.mcz.imagecapture.data.Determination;
import edu.harvard.mcz.imagecapture.data.Features;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.data.HigherTaxonLifeCycle;
import edu.harvard.mcz.imagecapture.data.ICImage;
import edu.harvard.mcz.imagecapture.data.LatLong;
import edu.harvard.mcz.imagecapture.data.LifeStage;
import edu.harvard.mcz.imagecapture.data.LocationInCollection;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.data.NatureOfId;
import edu.harvard.mcz.imagecapture.data.Number;
import edu.harvard.mcz.imagecapture.data.NumberLifeCycle;
import edu.harvard.mcz.imagecapture.data.NumberTableModel;
import edu.harvard.mcz.imagecapture.data.Sex;
import edu.harvard.mcz.imagecapture.data.Specimen;
import edu.harvard.mcz.imagecapture.data.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.data.SpecimenPart;
import edu.harvard.mcz.imagecapture.data.SpecimenPartAttribute;
import edu.harvard.mcz.imagecapture.data.SpecimenPartLifeCycle;
import edu.harvard.mcz.imagecapture.data.SpecimenPartsTableModel;
import edu.harvard.mcz.imagecapture.data.Tracking;
import edu.harvard.mcz.imagecapture.data.TrackingLifeCycle;
import edu.harvard.mcz.imagecapture.data.TypeStatus;
import edu.harvard.mcz.imagecapture.data.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.ui.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionException;
import org.hibernate.TransactionException;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

/**
 * JPanel for editing a record of a Specimen in a details view for that specimen. 
 * 
 * @author Paul J. Morris
 * 
 *  TODO: BugID: 10 add length limits (remaining to do for Number/Collector tables, 
 *  and for JComboBox fields).
 */
public class SpecimenDetailsViewPane extends JPanel {
	
	private static final Log log = LogFactory.getLog(SpecimenDetailsViewPane.class);
	
	private static final long serialVersionUID = 3716072190995030749L;
	
	private static final int STATE_CLEAN = 0;
	private static final int STATE_DIRTY = 1;
	
	
	private Specimen specimen;  //  @jve:decl-index=0:
	private Specimen lastEditedSpecimen = null;
	//allie add
	Specimen previousSpecimen = null;
	
	private SpecimenController specimenController = null;
	private int state;   // dirty if data in controls has been changed and not saved to specimen.
	
	private JTextField jTextFieldStatus = null;
	private JPanel jPanel = null;
	private JTextField jTextFieldBarcode = null;
	private JTextField jTextFieldGenus = null;
	private JTextField jTextFieldSpecies = null;
	private JTextField jTextFieldSubspecies = null;
	private JTextField jTextFieldLocality = null;
	private JButton jButtonSave = null;
	private JComboBox<String> jComboBoxCollection = null;
	private JTextField jTextFieldLastUpdatedBy = null;
	private JScrollPane jScrollPaneCollectors = null;
	private JTable jTableCollectors = null;
	private JScrollPane jScrollPaneSpecimenParts = null;
	private JTable jTableSpecimenParts = null;
	private JScrollPane jScrollPaneNumbers = null;
	private JTable jTableNumbers = null;
	
	private int clickedOnPartsRow;
	private JPopupMenu jPopupSpecimenParts;
	private int clickedOnCollsRow;
	private JPopupMenu jPopupCollectors;
	private int clickedOnNumsRow;
	private JPopupMenu jPopupNumbers;

	private JTextField jTextFieldDateLastUpdated = null;
	private JTextField jTextFieldCreator = null;
	private JTextField jTextFieldDateCreated = null;
	private JButton jButtonNumbersAdd = null;
	private JButton jButtonCollectorAdd = null;
	private JTextField jTextFieldDrawerNumber = null;
	private JTextField jTextFieldVerbatimLocality = null;
	private FilteringGeogJComboBox comboBoxHigherGeog;
	private StringBuffer higherGeogNotFoundWarning = new StringBuffer();
	private JButton jButtonGeoReference = null;
	//private JTextField jTextFieldCountry = null;
	private JComboBox<String> jComboBoxCountry = null;
	//allie change
	//private JTextField jTextFieldPrimaryDivision = null;
	private JComboBox<String> jComboBoxPrimaryDivision = null;
	private JComboBox<String> jComboBoxFamily = null;
	private JComboBox<String> jComboBoxSubfamily = null;
	private JTextField jTextFieldTribe = null;
	private JComboBox<String> jComboBoxSex = null;
	private JComboBox<String> jComboBoxFeatures = null;
	private JComboBox<String> jComboBoxLifeStage = null;
	private JTextField jTextFieldDateNos = null;
	private JTextField jTextFieldDateEmerged = null;
	private JTextField jTextFieldDateEmergedIndicator = null;
	private JTextField jTextFieldDateCollected = null;
	private JTextField jTextFieldDateCollectedIndicator = null;
	private JTextField jTextFieldInfraspecificEpithet = null;
	private JTextField jTextFieldInfraspecificRank = null;
	private JTextField jTextFieldAuthorship = null;
	private JTextField jTextFieldUnnamedForm = null;
	private JTextField jTextFieldMinElevation = null;
	private JTextField textFieldMaxElev = null;
	private JComboBox<String> comboBoxElevUnits = null;
	private JTextField jTextFieldCollectingMethod = null;
	private JTextArea jTextAreaSpecimenNotes = null;
	private JCheckBox jCheckBoxValidDistributionFlag = null;
	private JTextField jTextFieldMigrationStatus = null;
	private JTextField jTextFieldQuestions = null;
	// private JTextField jTextFieldPreparationType = null;
	private JButton jButtonAddPreparationType;
	private JTextField jTextFieldAssociatedTaxon = null;
	private JTextField jTextFieldHabitat = null;
	private JComboBox<String> jComboBoxWorkflowStatus = null;
	private JComboBox<String> jComboBoxLocationInCollection = null;
	private JTextField jTextFieldInferences = null;
	private JButton jButtonGetHistory = null;
	private JButton jButtonCopyPrev = null;
	private JButton jButtonNext = null;
	private SpecimenDetailsViewPane thisPane = null;
    private JButton jButtonPrevious = null;
    private JPanel jPanel1 = null;   // panel for navigation buttons
	private JTextField jTextFieldISODate = null;
	private JButton jButtonDeterminations = null;
	private JTextField jTextFieldCitedInPub = null;
	private JScrollPane jScrollPaneNotes = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButtonSpecificLocality = null;
	private JTextField jTextFieldImageCount = null;
	private JTextField textFieldMicrohabitat = null;
	private JComboBox<String> jComboBoxNatureOfId;
	private JTextField jTextFieldDateDetermined;
	
	//allie change
	//private JTextField jCBDeterminer;
	//private FilteringAgentJComboBox jCBDeterminer;
	private JComboBox<String> jCBDeterminer = null;
	
	private JTextField jTextFieldIdRemarks;
	private JComboBox<String> cbTypeStatus;
	
	/**
	 * Construct an instance of a SpecimenDetailsViewPane showing the data present
	 * in aSpecimenInstance. 
	 * 
	 * @param aSpecimenInstance the Specimen instance to display for editing.
	 */
	public SpecimenDetailsViewPane(Specimen aSpecimenInstance, SpecimenController aControler) {
		specimen = aSpecimenInstance;
		SpecimenLifeCycle s = new SpecimenLifeCycle();
		setStateToClean();
//		SpecimenPartAttributeLifeCycle spals = new SpecimenPartAttributeLifeCycle();
//		Iterator<SpecimenPart> i = specimen.getSpecimenParts().iterator();
//		while (i.hasNext()) { 
//			Iterator<SpecimenPartAttribute> ia = i.next().getAttributeCollection().iterator();
//			while (ia.hasNext()) { 
//				try {
//					SpecimenPartAttribute spa = ia.next();
//					log.debug(spa.getSpecimenPartAttributeId());
//					spals.attachDirty(spa);
//					log.debug(spa.getSpecimenPartAttributeId());
//				} catch (SaveFailedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		try {
			thisPane = this;
			s.attachClean(specimen);
			specimenController = aControler;
			initialize();
			setValues();
		} catch (SessionException e) { 
			log.debug(e.getMessage(),e);
			Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Database Connection Error.");
			HibernateUtil.terminateSessionFactory();
			this.setVisible(false);			
		} catch (TransactionException e) { 
			log.debug(e.getMessage(),e);
			Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Database Connection Error.");
			HibernateUtil.terminateSessionFactory();
			this.setVisible(false);
		}
	}

	
    /**
	 * Initializes the specimen details view pane.
	 *  Note, contains comments indicating how to enable visual designer with this class. 
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(0);
		borderLayout.setVgap(0);
		this.setLayout(borderLayout);
	    this.add(getJTextFieldStatus(), BorderLayout.SOUTH);
	    
	    // Un-comment this line to use design tool.
	    //    this.add(getJPanel(), BorderLayout.CENTER);
	        
	    // Comment this block out to use design tool.
	    //   see also getCbTypeStatus

	    JScrollPane scrollPane = new JScrollPane(getJPanel(),
	    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	    scrollPane.setBorder(BorderFactory.createEmptyBorder());
	    this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setWarning(String warning) { 
		jTextFieldStatus.setText(warning);
		jTextFieldStatus.setForeground(Color.RED);
	}
	
	private void setWarnings() { 
		log.debug("In set warnings");
		if (specimen.getICImages()!=null) { 
			log.debug("specimen.getICImages is not null");
			java.util.Iterator<ICImage> i = specimen.getICImages().iterator();
			log.debug(i.hasNext());
			while (i.hasNext()) { 
				log.debug("Checking image " + i );
				ICImage im = i.next();
				String rbc = "";
				if (im.getRawBarcode()!=null) { rbc = im.getRawBarcode(); }
				String ebc = "";
				if (im.getRawExifBarcode()!=null) { ebc = im.getRawExifBarcode(); } 
				if (!rbc.equals(ebc)) { 
					// warn of mismatch, but only if configured to expect both to be present.
					if (Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_REDUNDANT_COMMENT_BARCODE).equals("true")) {
						this.setWarning("Warning: An image has mismatch between Comment and Barcode.");
						log.debug("Setting: Warning: Image has mismatch between Comment and Barcode.");
					}
				}
			}
		}
		if (higherGeogNotFoundWarning!=null && higherGeogNotFoundWarning.length()>0) { 
			this.setWarning(higherGeogNotFoundWarning.toString());
		}
	}

	public void setStatus(String status) {
		jTextFieldStatus.setText(status);
		jTextFieldStatus.setForeground(Color.BLACK);
	}
		
	private void save() { 
		try { 
			this.setStatus("Saving");
			if (jTableCollectors.isEditing()) {
				jTableCollectors.getCellEditor().stopCellEditing();
			}
			if (jTableSpecimenParts.isEditing()) {
				jTableSpecimenParts.getCellEditor().stopCellEditing();
			}
			if (jTableNumbers.isEditing()) {
				log.debug("jTableNumbers IS editing!!!");
				jTableNumbers.getCellEditor().stopCellEditing();
			}

			if (cbTypeStatus.getSelectedIndex()==-1 && cbTypeStatus.getSelectedItem()==null) {
				specimen.setTypeStatus(Specimen.STATUS_NOT_A_TYPE);
			} else {
				specimen.setTypeStatus((String)cbTypeStatus.getSelectedItem());
			}
			specimen.setMicrohabitat(textFieldMicrohabitat.getText());

			if(jComboBoxLocationInCollection.getSelectedItem() != null){
				specimen.setLocationInCollection(jComboBoxLocationInCollection.getSelectedItem().toString());
			}


			specimen.setDrawerNumber(jTextFieldDrawerNumber.getText());
			if (jComboBoxFamily.getSelectedIndex()==-1 && jComboBoxFamily.getSelectedItem()==null) {
				specimen.setFamily("");
			} else {
				specimen.setFamily(jComboBoxFamily.getSelectedItem().toString());
			}
			if (jComboBoxSubfamily.getSelectedIndex()==-1 && jComboBoxSubfamily.getSelectedItem()==null) {
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
			//TODO: handle the collectors set!

			//this returns TRUE for the copied item!!
			log.debug("in save(). specimen numbers size: " + specimen.getNumbers().size());
			log.debug("okok in save(), specimenid is " + specimen.getSpecimenId());

			if(previousSpecimen != null && previousSpecimen.getNumbers() != null){
				log.debug("in save(). prev specimen numbers size: " + previousSpecimen.getNumbers().size());
				//specimen.setNumbers(previousSpecimen.getNumbers()); - this gives hibernate exceptions here too!
				log.debug("okok in save(), prev specimenid is " + previousSpecimen.getSpecimenId());
			}

			//allie change
			//log.debug("THE jCBDeterminer TEXT IS " + jCBDeterminer.getText());
			//specimen.setIdentifiedBy(jCBDeterminer.getText());
			specimen.setIdentifiedBy((String)jCBDeterminer.getSelectedItem());

			specimen.setDateIdentified(jTextFieldDateDetermined.getText());
			specimen.setIdentificationRemarks(jTextFieldIdRemarks.getText());
			if (jComboBoxNatureOfId.getSelectedIndex()==-1 && jComboBoxNatureOfId.getSelectedItem()==null) {
				//specimen.setNatureOfId(NatureOfId.LEGACY);
				specimen.setNatureOfId(NatureOfId.EXPERT_ID);
			} else {
				specimen.setNatureOfId((String)jComboBoxNatureOfId.getSelectedItem());
			}

			specimen.setUnNamedForm(jTextFieldUnnamedForm.getText());
			specimen.setVerbatimLocality(jTextFieldVerbatimLocality.getText());
			//allieremove
			/*if (comboBoxHigherGeog.getSelectedIndex()==-1 && comboBoxHigherGeog.getSelectedItem()==null) {
				specimen.setHigherGeography("");
			} else {
				// combo box contains a geography object, obtain the higher geography string.
				specimen.setHigherGeography(((HigherGeographyComboBoxModel)comboBoxHigherGeog.getModel()).getSelectedItemHigherGeography());
			}*/
			//specimen.setCountry(jTextFieldCountry.getText());
			specimen.setCountry((String)jComboBoxCountry.getSelectedItem());
			specimen.setValidDistributionFlag(jCheckBoxValidDistributionFlag.isSelected());
			specimen.setPrimaryDivison((String)jComboBoxPrimaryDivision.getSelectedItem());
			specimen.setSpecificLocality(jTextFieldLocality.getText());

			// Elevations
			Long min_elev;
			if (jTextFieldMinElevation.getText().trim().length()==0)  {
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
			if (textFieldMaxElev.getText().trim().length()==0)  {
				max_elev = null;
			} else {
				try {
					max_elev = Long.parseLong(textFieldMaxElev.getText());
				} catch (NumberFormatException e) {
					max_elev = null;
				}
			}
			specimen.setMaximum_elevation(max_elev);
			if (this.comboBoxElevUnits.getSelectedIndex()==-1 && comboBoxElevUnits.getSelectedItem()==null) {
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
			if (jComboBoxCollection.getSelectedIndex()==-1 && jComboBoxCollection.getSelectedItem()==null) {
				specimen.setCollection("");
			} else {
				specimen.setCollection(jComboBoxCollection.getSelectedItem().toString());
			}
			if (jComboBoxFeatures.getSelectedIndex()==-1 && jComboBoxFeatures.getSelectedItem()==null) {
					specimen.setFeatures("");
			} else {
				specimen.setFeatures(jComboBoxFeatures.getSelectedItem().toString());
			}
			if (jComboBoxLifeStage.getSelectedIndex()==-1 && jComboBoxLifeStage.getSelectedItem()==null) {
				specimen.setLifeStage("");
			} else {
				specimen.setLifeStage(jComboBoxLifeStage.getSelectedItem().toString());
			}
			if (jComboBoxSex.getSelectedIndex()==-1 && jComboBoxSex.getSelectedItem()==null) {
				specimen.setSex("");
			} else {
				specimen.setSex(jComboBoxSex.getSelectedItem().toString());
				log.debug("jComboBoxSex selectedIndex=" + jComboBoxSex.getSelectedIndex());
			}

			log.debug("sex=" + specimen.getSex());

			specimen.setCitedInPublication(jTextFieldCitedInPub.getText());
			//specimen.setPreparationType(jTextFieldPreparationType.getText());
			specimen.setAssociatedTaxon(jTextFieldAssociatedTaxon.getText());
			specimen.setHabitat(jTextFieldHabitat.getText());
			specimen.setMicrohabitat(textFieldMicrohabitat.getText());
			specimen.setSpecimenNotes(jTextAreaSpecimenNotes.getText());
			specimen.setInferences(jTextFieldInferences.getText());
			specimen.setLastUpdatedBy(Singleton.getSingletonInstance().getUserFullName());
			specimen.setDateLastUpdated(new Date());
			specimen.setWorkFlowStatus(jComboBoxWorkflowStatus.getSelectedItem().toString());


			specimen.setQuestions(jTextFieldQuestions.getText());
			try {
				specimenController.save();   // save the record
				setStateToClean();    // enable the navigation buttons
				this.setStatus("Saved");  // inform the user
				jTextFieldStatus.setForeground(Color.BLACK);
				setWarnings();
				jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
				jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString());

				//allie added this
				ImageCaptureApp.lastEditedSpecimenCache = specimen;

			} catch (SaveFailedException e) {
				setStateToDirty();    // disable the navigation buttons
				this.setWarning("Error: " + e.getMessage());
			}
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			Singleton.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount());
		} catch (Exception e) {
			// trap any exception and notify the user
	    	setStateToDirty();    // disable the navigation buttons
	    	this.setWarning("Error. " + e.getMessage()); 
	    	log.error(e);
		}
		updateDeterminationCount();
	}
	
	private void copyPreviousRecord(){
		log.debug("calling copyPreviousRecord()");
		//thisPane.setStateToDirty();
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
		    textFieldMaxElev.setText(Long.toString(previousSpecimen.getMaximum_elevation()));
		} catch (Exception e) { 
			textFieldMaxElev.setText("");
		}
		if (previousSpecimen.getElev_units()!=null) { 
		    comboBoxElevUnits.setSelectedItem(previousSpecimen.getElev_units());
		} else {
			comboBoxElevUnits.setSelectedItem("");
		}	
		jTextFieldLocality.setText(previousSpecimen.getSpecificLocality());
		jComboBoxCollection.setSelectedItem(previousSpecimen.getCollection());
		jTableCollectors.setModel(new CollectorTableModel(previousSpecimen.getCollectors()));
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
			//specimen.getNumbers().add((edu.harvard.mcz.imagecapture.data.Number)iter.next());
			Number n = new Number();
			Number n2 = number;
			n.setNumber(new String(n2.getNumber()));
			n.setSpecimen(specimen);
			n.setNumberId(new Long(n2.getNumberId()));
			n.setNumberType(new String(n2.getNumberType()));
			specimen.getNumbers().add(n);

		}
		jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));
		
		//+ verify the georeference data (we do want it all copied)
		
		
		//+ preparation type (the whole table!) = specimen parts
		/*jTableSpecimenParts.setModel(new SpecimenPartsTableModel(previousSpecimen.getSpecimenParts()));
		jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90);
		for (int i = 0; i < jTableSpecimenParts.getColumnCount(); i++) {
		    TableColumn column = jTableSpecimenParts.getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setPreferredWidth(120); 
		    } else {
		        column.setPreferredWidth(50);
		    }
		}
		
		specimen.getSpecimenParts().clear();
		Iterator<edu.harvard.mcz.imagecapture.data.SpecimenPart> iterp = previousSpecimen.getSpecimenParts().iterator();
		while(iterp.hasNext()){
			specimen.getSpecimenParts().add((edu.harvard.mcz.imagecapture.data.SpecimenPart)iterp.next());
		}
		jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));*/
		
		specimen.getSpecimenParts().clear();
		for (SpecimenPart specimenPart : previousSpecimen.getSpecimenParts()) {
			SpecimenPart part = new SpecimenPart();
			SpecimenPart part2 = specimenPart;
			Collection<SpecimenPartAttribute> coll = new ArrayList<SpecimenPartAttribute>();
			Collection<SpecimenPartAttribute> coll2 = part2.getAttributeCollection();
			for (SpecimenPartAttribute pa2 : coll2) {
				SpecimenPartAttribute pa = new SpecimenPartAttribute();
				pa.setAttributeDate(pa2.getAttributeDate());
				pa.setAttributeDeterminer(pa.getAttributeDeterminer());
				pa.setAttributeRemark(pa2.getAttributeRemark());
				pa.setAttributeType(pa2.getAttributeType());
				pa.setAttributeUnits(pa2.getAttributeUnits());
				pa.setAttributeValue(pa2.getAttributeValue());
				pa.setSpecimenPartAttributeId(pa2.getSpecimenPartAttributeId());
				pa.setSpecimenPartId(pa2.getSpecimenPartId());
				coll.add(pa);
			}
			part.setAttributeCollection(coll);
			part.setLotCount(part2.getLotCount());
			part.setLotCountModifier(part2.getLotCountModifier());
			part.setPartName(part2.getPartName());
			part.setPreserveMethod(part2.getPreserveMethod());
			part.setSpecimenId(part2.getSpecimenId());
			part.setSpecimenPartId(part2.getSpecimenPartId());
			specimen.getSpecimenParts().add(part);
		}
		jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
		
		//collectors - copy
		/*specimen.getCollectors().clear();
		Iterator<edu.harvard.mcz.imagecapture.data.Collector> iterc = previousSpecimen.getCollectors().iterator();
		while(iterc.hasNext()){
			specimen.getCollectors().add((edu.harvard.mcz.imagecapture.data.Collector)iterc.next());
		}		
		jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));*/
		
		specimen.getCollectors().clear();
		for (Collector collector : previousSpecimen.getCollectors()) {
			Collector c = new Collector();
			Collector c2 = collector;
			c.setCollectorId(c2.getCollectorId());
			c.setCollectorName(c2.getCollectorName());
			c.setSpecimen(specimen);
			specimen.getCollectors().add(c);
		}		
		jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));

		
	
		CollectorLifeCycle cls = new CollectorLifeCycle();
		JComboBox<String> jComboBoxCollector = new JComboBox<>(cls.getDistinctCollectors());
		jComboBoxCollector.setEditable(true);
		//field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
		jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(jComboBoxCollector));
		AutoCompleteDecorator.decorate(jComboBoxCollector);		
		
		//+determinations 
		specimen.getDeterminations().clear();
		for (Determination prevdet : previousSpecimen.getDeterminations()) {
			Determination newdet = new Determination();
			newdet.setGenus(prevdet.getGenus());
			newdet.setSpecificEpithet(prevdet.getSpecificEpithet());
			newdet.setSubspecificEpithet(prevdet.getSubspecificEpithet());
			newdet.setInfraspecificEpithet(prevdet.getInfraspecificEpithet());
			newdet.setInfraspecificRank(prevdet.getInfraspecificRank());
			newdet.setAuthorship(prevdet.getAuthorship());
			newdet.setUnNamedForm(prevdet.getUnNamedForm());
			newdet.setIdentifiedBy(prevdet.getIdentifiedBy());
			newdet.setTypeStatus(prevdet.getTypeStatus());
			newdet.setSpeciesNumber(prevdet.getSpeciesNumber());
			newdet.setVerbatimText(prevdet.getVerbatimText());
			newdet.setNatureOfId(prevdet.getNatureOfId());
			newdet.setDateIdentified(prevdet.getDateIdentified());
			newdet.setRemarks(prevdet.getRemarks());
			newdet.setSpecimen(specimen);
			specimen.getDeterminations().add(newdet);

		}		

		//+georeference
		Set<LatLong> georeferences = specimen.getLatLong();
		LatLong newgeo = georeferences.iterator().next();
		newgeo.setSpecimenId(specimen);

		for (LatLong prevgeo : previousSpecimen.getLatLong()) {
			newgeo.setAcceptedLatLongFg(prevgeo.getAcceptedLatLongFg());
			newgeo.setAcceptedLatLongFg(prevgeo.getAcceptedLatLongFg());
			newgeo.setDatum(prevgeo.getDatum());
			newgeo.setDecLat(prevgeo.getDecLat());
			newgeo.setDecLatMin(prevgeo.getDecLatMin());
			newgeo.setDecLong(prevgeo.getDecLong());
			newgeo.setDecLongMin(prevgeo.getDecLongMin());
			newgeo.setDeterminedByAgent(prevgeo.getDeterminedByAgent());
			newgeo.setDeterminedDate(prevgeo.getDeterminedDate());
			newgeo.setExtent(prevgeo.getExtent());
			newgeo.setFieldVerifiedFg(prevgeo.getFieldVerifiedFg());
			newgeo.setGeorefmethod(prevgeo.getGeorefmethod());
			newgeo.setGpsaccuracy(prevgeo.getGpsaccuracy());
			newgeo.setLatDeg(prevgeo.getLatDeg());
			newgeo.setLatDir(prevgeo.getLatDir());
			newgeo.setLatLongForNnpFg(prevgeo.getLatLongForNnpFg());
			newgeo.setLatLongRefSource(prevgeo.getLatLongRefSource());
			newgeo.setLatLongRemarks(prevgeo.getLatLongRemarks());
			newgeo.setLatMin(prevgeo.getLatMin());
			newgeo.setLatSec(prevgeo.getLatSec());
			newgeo.setLongDeg(prevgeo.getLongDeg());
			newgeo.setLongDir(prevgeo.getLongDir());
			newgeo.setLongMin(prevgeo.getLongMin());
			newgeo.setLongSec(prevgeo.getLongSec());
			newgeo.setMaxErrorDistance(prevgeo.getMaxErrorDistance());
			newgeo.setMaxErrorUnits(prevgeo.getMaxErrorUnits());
			newgeo.setNearestNamedPlace(prevgeo.getNearestNamedPlace());
			newgeo.setOrigLatLongUnits(prevgeo.getOrigLatLongUnits());
			newgeo.setUtmEw(prevgeo.getUtmEw());
			newgeo.setUtmNs(prevgeo.getUtmNs());
			newgeo.setUtmZone(prevgeo.getUtmZone());
			newgeo.setVerificationstatus(prevgeo.getVerificationstatus());
		}

		
		//new - verbatim locality
		jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality());
		//new - publications
		jTextFieldCitedInPub.setText(previousSpecimen.getCitedInPublication());
		//new - features
		jComboBoxFeatures.setSelectedItem(previousSpecimen.getFeatures());
		//new - collecting method
		jTextFieldCollectingMethod.setText(previousSpecimen.getCollectingMethod());
		
		
		setSpecimenPartsTableCellEditors();
		this.updateDeterminationCount();
	}
	
	private void setValues() { 
		log.debug("okok copy previous, specimenid is " + specimen.getSpecimenId());
		log.debug("invoking setValues()");
		this.setStatus("Setting values");
		jTextFieldBarcode.setText(specimen.getBarcode());
		
		//alliefix - set to value from properties
		//jComboBoxLocationInCollection.setSelectedItem(specimen.getLocationInCollection());	
		String locationInCollectionPropertiesVal = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(
	    		 ImageCaptureProperties.KEY_DISPLAY_COLLECTION);
		jComboBoxLocationInCollection.setSelectedItem(locationInCollectionPropertiesVal);
		
		//allie try
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
		
		//allie new - bugfix
		textFieldMicrohabitat.setText(specimen.getMicrohabitat());
		
		jTextFieldIdRemarks.setText(specimen.getIdentificationRemarks());
		jTextFieldDateDetermined.setText(specimen.getDateIdentified());
		
    	//allie change
    	//log.debug("jComboBoxLifeStage here!!! specimen life stage is " + specimen.getLifeStage());
    	if(specimen.getLifeStage() == null || specimen.getLifeStage().equals("")){
    		specimen.setLifeStage("adult");
    		jComboBoxLifeStage.setSelectedIndex(0);
    	}
    	
		//allie change - removed this 
        //MCZbaseAuthAgentName selection = new MCZbaseAuthAgentName();
        //selection.setAgent_name(specimen.getIdentifiedBy());
        //((AgentNameComboBoxModel)jCBDeterminer.getModel()).setSelectedItem(selection);
		//jCBDeterminer.getEditor().setItem(jCBDeterminer.getModel().getSelectedItem());
        
		//allie change - added this
        //jCBDeterminer.setText(specimen.getIdentifiedBy());
    	jCBDeterminer.setSelectedItem(specimen.getIdentifiedBy());
		
		jComboBoxNatureOfId.setSelectedItem(specimen.getNatureOfId());
		
		jTextFieldUnnamedForm.setText(specimen.getUnNamedForm());
		jTextFieldVerbatimLocality.setText(specimen.getVerbatimLocality());
		// Specimen record contains a string, delegate handling of lookup of object to the combo box model.
		//allieremove
// 		log.debug(specimen.getHigherGeography());
// 		((HigherGeographyComboBoxModel)comboBoxHigherGeog.getModel()).setSelectedItem(specimen.getHigherGeography());
// //TODO ? set model not notifying listeners? 	
// 		higherGeogNotFoundWarning = new StringBuffer();
// 		comboBoxHigherGeog.getEditor().setItem(comboBoxHigherGeog.getModel().getSelectedItem());
// 		if (specimen.getHigherGeography()==null) { 
// 			comboBoxHigherGeog.setBackground(Color.YELLOW);
// 		} else { 
// 			if (comboBoxHigherGeog.getModel().getSelectedItem()==null) {
// 				comboBoxHigherGeog.setBackground(Color.RED);
// 				higherGeogNotFoundWarning.append("Higher Geog: [").append(specimen.getHigherGeography()).append("] not found. Fix before saving.");
// 			}
// 		}
// 		jTextFieldCountry.setText(specimen.getCountry());
		jComboBoxCountry.setSelectedItem(specimen.getCountry());
		if (specimen.getValidDistributionFlag()!=null) { 
		    jCheckBoxValidDistributionFlag.setSelected(specimen.getValidDistributionFlag());
		} else { 
			jCheckBoxValidDistributionFlag.setSelected(false);
		}
		jComboBoxPrimaryDivision.setSelectedItem(specimen.getPrimaryDivison());
		jTextFieldLocality.setText(specimen.getSpecificLocality());
		
	    // Elevations  **********************************************************************
		try { 
		    jTextFieldMinElevation.setText(Long.toString(specimen.getMinimum_elevation()));
		} catch (Exception e) { 
		    jTextFieldMinElevation.setText("");
		}
		try { 
		    textFieldMaxElev.setText(Long.toString(specimen.getMaximum_elevation()));
		} catch (Exception e) { 
			textFieldMaxElev.setText("");
		}
		if (specimen.getElev_units()!=null) { 
		    comboBoxElevUnits.setSelectedItem(specimen.getElev_units());
		} else {
			comboBoxElevUnits.setSelectedItem("");
		}

		jTextFieldCollectingMethod.setText(specimen.getCollectingMethod());
		jTextFieldISODate.setText(specimen.getIsoDate());
		jTextFieldDateNos.setText(specimen.getDateNos());
		jTextFieldDateCollected.setText(specimen.getDateCollected());
		jTextFieldDateEmerged.setText(specimen.getDateEmerged());
		jTextFieldDateCollectedIndicator.setText(specimen.getDateCollectedIndicator());
		jTextFieldDateEmergedIndicator.setText(specimen.getDateEmergedIndicator());
		jComboBoxCollection.setSelectedItem(specimen.getCollection());
		//jTextFieldPreparationType.setText(specimen.getPreparationType());
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
	    	jTextFieldMigrationStatus.setText("http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.getCatNum());
	    } else { 
	    	jTextFieldMigrationStatus.setText("");
	    }
		jTextFieldInferences.setText(specimen.getInferences());
		jTextFieldCreator.setText(specimen.getCreatedBy());
		if (specimen.getDateCreated()!=null) { 
		   jTextFieldDateCreated.setText(specimen.getDateCreated().toString());
		} 
		jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy());
		if (specimen.getDateLastUpdated()!=null) { 
		   jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString());
		} 
		
		//allie change
    	if(specimen.getNatureOfId() == null || specimen.getNatureOfId().equals("")){
    		specimen.setLifeStage("expert ID");
    		jComboBoxNatureOfId.setSelectedIndex(0);
    	}
		
		//without this, it does save the 1st record, and it does not copy the next record!
    	log.debug("setValues calling jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));");
    	jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));

		// Setting the model will overwrite the existing cell editor bound 
		// to the column model, so we need to add it again.
		JTextField field1 = new JTextField();
		field1.setInputVerifier(MetadataRetriever.getInputVerifier(edu.harvard.mcz.imagecapture.data.Number.class, "Number", field1));
		field1.setVerifyInputWhenFocusTarget(true);
		jTableNumbers.getColumnModel().getColumn(0).setCellEditor(new ValidatingTableCellEditor(field1));
		JComboBox<String> jComboNumberTypes = new JComboBox<String>();
		jComboNumberTypes.setModel(new DefaultComboBoxModel<String>(NumberLifeCycle.getDistinctTypes()));
		jComboNumberTypes.setEditable(true);
		TableColumn typeColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
		typeColumn.setCellEditor(new DefaultCellEditor(jComboNumberTypes));
		
		jTableCollectors.setModel(new CollectorTableModel(specimen.getCollectors()));
		
		
		// Setting the model will overwrite the existing cell editor bound 
		// to the column model, so we need to add it again.
		
		//this line is the original!!!
		//FilteringAgentJComboBox field = new FilteringAgentJComboBox();
		
		//paul's comments
		//field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
		//jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new PicklistTableCellEditor(field, true));
		
		//this line is the original!!!
		//jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(field));
		
		//paul's comments
		//field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
		//field.setVerifyInputWhenFocusTarget(true);

		//allie - change: this is for simple text fields. below it has changed to combo boxes!
		/*JTextField field = new JTextField();
		field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
		jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ValidatingTableCellEditor(field));*/
		//end allie change
		
		//allie new
		CollectorLifeCycle cls = new CollectorLifeCycle();
		JComboBox<String> jComboBoxCollector = new JComboBox<>(cls.getDistinctCollectors());
		jComboBoxCollector.setEditable(true);
		//field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
		jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(jComboBoxCollector));
		AutoCompleteDecorator.decorate(jComboBoxCollector);
		//end allie new
		
		jTableSpecimenParts.setModel(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
		jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90);
		for (int i = 0; i < jTableSpecimenParts.getColumnCount(); i++) {
		    TableColumn column = jTableSpecimenParts.getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setPreferredWidth(120); 
		    } else {
		        column.setPreferredWidth(50);
		    }
		}
		setSpecimenPartsTableCellEditors();
		
		updateDeterminationCount();
		
		updateImageCount();
		
		setWarnings();
		this.setStateToClean();
		this.setStatus("Loaded");
	}

	private void updateDeterminationCount() {
		if (specimen.getDeterminations()==null) {
			this.setDeterminationCount(0);
		} else {
			this.setDeterminationCount(specimen.getDeterminations().size());
		}
	}

	private void setDeterminationCount(int count) {
		String detSuffix = count == 1 ? "s" : "";
		jButtonDeterminations.setText(Integer.toString(count) + " Det" + detSuffix + ".");
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
			jPanel = new JPanel(new MigLayout("wrap 4")); // 4 col layout
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
			//row
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
			jPanel.add(this.getDetsJButton(), "span 2");
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
			jPanel.add(new JLabel("from"), "align label, sizegroup elevation");
			jPanel.add(this.getVerbatimElevationJTextField(), "grow, sizegroup elevation");
			jPanel.add(new JLabel("to"), "align label, sizegroup elevation");
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
			jPanel.add(this.getJButtonCopyPrev());
			jPanel.add(this.getHistoryJButton());
			jPanel.add(this.getNextPrevButtonGroup());
			jPanel.add(this.getSaveJButton(), "tag apply");
		}
		return jPanel;
	}

	private void addBasicJLabel(JPanel target, String labelText) {
		JLabel label = new JLabel();
		label.setText(labelText);
		target.add(label, "align label");
	}

	private JPanel getNextPrevButtonGroup() {
		JPanel group = new JPanel(new MigLayout());
		group.add(this.getJButtonPrevious(), "tag back");
		group.add(this.getJButtonNext(), "tag next");
		return group;
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
			jTextFieldGenus = new JTextField();
			jTextFieldGenus.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Genus", jTextFieldGenus));
			jTextFieldGenus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Genus"));
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
			jTextFieldSpecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SpecificEpithet", jTextFieldSpecies));
			jTextFieldSpecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SpecificEpithet"));
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
			jTextFieldSubspecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SubspecificEpithet", jTextFieldSubspecies));
			jTextFieldSubspecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SubspecificEpithet"));
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
			jTextFieldLocality.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "SpecificLocality", jTextFieldLocality));
			jTextFieldLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "SpecificLocality"));
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
			if (specimen.isStateDone()) { 
				jButtonSave.setEnabled(false);
				jButtonSave.setText("Migrated " + specimen.getLoadFlags());
			}
			jButtonSave.setMnemonic(KeyEvent.VK_S);
			jButtonSave.setToolTipText("Save changes to this record to the database. No fields should have red backgrounds before you save.");
			jButtonSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try { 
					    thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					} catch (Exception ex) { 
						log.error(ex);
					}
					
					save();
					
					//this really does nothing!
					((CollectorTableModel)jTableCollectors.getModel()).fireTableDataChanged();
					((NumberTableModel)jTableNumbers.getModel()).fireTableDataChanged();
					
					
					try { 
					    thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					} catch (Exception ex) { 
						log.error(ex);
					}
				}
			});
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox<String> getJTextFieldCollection() {
		if (jComboBoxCollection == null) {
			log.debug("init jComboBoxCollection");
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			jComboBoxCollection = new JComboBox<String>();
			jComboBoxCollection.setModel(new DefaultComboBoxModel<String>(sls.getDistinctCollections()));
			jComboBoxCollection.setEditable(true);
			//jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
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
			//jTextFieldLastUpdatedBy.setEnabled(false);
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
			jScrollPaneCollectors = new JScrollPane();
			jScrollPaneCollectors.setViewportView(getJTableCollectors());
			jScrollPaneCollectors.setMaximumSize(new Dimension(1000, 100));
			jScrollPaneCollectors.addMouseWheelListener(new MouseWheelScrollListener(jScrollPaneCollectors));
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
			jTableCollectors = new JTable(new CollectorTableModel());
			
			// Note: When setting the values, the table column editor needs to be reset there, as the model is replaced.

			JComboBox field = new JComboBox();
			//field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
			jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(field));
			//
			jTableCollectors.setShowGrid(true);
			jTableCollectors.setRowHeight(jTableCollectors.getRowHeight() + 5);
		    jTableCollectors.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		    jTableCollectors.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnCollsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupCollectors.show(e.getComponent(),e.getX(),e.getY());
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnCollsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupCollectors.show(e.getComponent(),e.getX(),e.getY());
					}
				}
			});
		    // row deletion
		    jPopupCollectors = new JPopupMenu();
			JMenuItem menuItemDeleteRow = new JMenuItem("Delete Row");
			menuItemDeleteRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					try { 
						log.debug(clickedOnCollsRow);
						if (clickedOnCollsRow>=0) { 
							int ok = JOptionPane.showConfirmDialog(thisPane, "Delete the selected collector?", "Delete Collector", JOptionPane.OK_CANCEL_OPTION);
							if (ok==JOptionPane.OK_OPTION) { 
								log.debug("deleting collectors row " + clickedOnCollsRow);
					            ((CollectorTableModel)jTableCollectors.getModel()).deleteRow(clickedOnCollsRow);
					            setStateToDirty();
							} else { 
								log.debug("collector row delete canceled by user.");
							}
						} else { 
						    JOptionPane.showMessageDialog(thisPane, "Unable to select row to delete.  Try empting the value and pressing Save.");
						}
					} catch (Exception ex) { 
						log.error(ex.getMessage());
						JOptionPane.showMessageDialog(thisPane, "Failed to delete a collector row. " + ex.getMessage());
					}
				}
			});	
			jPopupCollectors.add(menuItemDeleteRow);
		}
		return jTableCollectors;
	}
	
	private JScrollPane getJScrollPaneSpecimenParts() {
		if (jScrollPaneSpecimenParts == null) {
			jScrollPaneSpecimenParts = new JScrollPane();
			jScrollPaneSpecimenParts.setViewportView(getJTableSpecimenParts());
			jScrollPaneSpecimenParts.setMaximumSize(new Dimension(1000, 100));
			jScrollPaneSpecimenParts.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
			jScrollPaneSpecimenParts.addMouseWheelListener(new MouseWheelScrollListener(jScrollPaneSpecimenParts));
		}
		return jScrollPaneSpecimenParts;
	}	
	
	private JTable getJTableSpecimenParts() { 
		if (jTableSpecimenParts == null) {
			try { 
		        jTableSpecimenParts = new JTable(new SpecimenPartsTableModel(specimen.getSpecimenParts()));
			} catch (NullPointerException e) { 
			    jTableSpecimenParts = new JTable(new SpecimenPartsTableModel());
			}
			jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90);
			jTableSpecimenParts.setRowHeight(jTableSpecimenParts.getRowHeight() + 5);
			setSpecimenPartsTableCellEditors();
			
		    log.debug(specimen.getSpecimenParts().size());
		    
		    jTableSpecimenParts.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnPartsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupSpecimenParts.show(e.getComponent(),e.getX(),e.getY());
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnPartsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupSpecimenParts.show(e.getComponent(),e.getX(),e.getY());
					}
				}
			});
		    
		    jPopupSpecimenParts = new JPopupMenu();
			JMenuItem mntmDeleteRow = new JMenuItem("Delete Row");
			mntmDeleteRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					try { 
						if (clickedOnPartsRow>=0) { 
							int ok = JOptionPane.showConfirmDialog(thisPane, "Delete the selected preparation?", "Delete Preparation", JOptionPane.OK_CANCEL_OPTION);
							if (ok==JOptionPane.OK_OPTION) { 
								log.debug("deleting parts row " + clickedOnPartsRow);
					            ((SpecimenPartsTableModel)jTableSpecimenParts.getModel()).deleteRow(clickedOnPartsRow);
					            setStateToDirty();
							} else { 
								log.debug("parts row delete canceled by user.");
							}
						} else { 
						    JOptionPane.showMessageDialog(thisPane, "Unable to select row to delete.");
						}
					} catch (Exception ex) { 
						log.error(ex.getMessage());
						JOptionPane.showMessageDialog(thisPane, "Failed to delete a part attribute row. " + ex.getMessage());
					}
				}
			});	
			jPopupSpecimenParts.add(mntmDeleteRow);	
		}
		return jTableSpecimenParts;
	}
	
    private void setSpecimenPartsTableCellEditors() { 
        log.debug("Setting cell editors");
		JComboBox<String> comboBoxPart = new JComboBox<>(SpecimenPart.PARTNAMES);
		//comboBoxPart.addItem("whole animal");
		//comboBoxPart.addItem("partial animal");
		getJTableSpecimenParts().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBoxPart));
		JComboBox<String> comboBoxPrep = new JComboBox<>(SpecimenPart.PRESERVENAMES);
		//comboBoxPrep.addItem("pinned");
		//comboBoxPrep.addItem("pointed");
		//comboBoxPrep.addItem("carded");
		//comboBoxPrep.addItem("capsule");
		//comboBoxPrep.addItem("envelope");
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
			//jTextFieldDateLastUpdated.setEnabled(false);
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
			//jTextFieldCreator.setEnabled(false);
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
			//jTextFieldDateCreated.setEnabled(false);
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
			jScrollPaneNumbers = new JScrollPane();
			jScrollPaneNumbers.setViewportView(getNumberJTable());
			jScrollPaneNumbers.setMaximumSize(new Dimension(1000, 100));
			jScrollPaneNumbers.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
			jScrollPaneNumbers.addMouseWheelListener(new MouseWheelScrollListener(jScrollPaneNumbers));
		}
		return jScrollPaneNumbers;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getNumberJTable() {
		if (jTableNumbers == null) {
			jTableNumbers = new JTable(new NumberTableModel());
			JComboBox<String> jComboNumberTypes = new JComboBox<String>();
			jComboNumberTypes.setModel(new DefaultComboBoxModel<String>(NumberLifeCycle.getDistinctTypes()));
			jComboNumberTypes.setEditable(true);
			TableColumn typeColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
			DefaultCellEditor comboBoxEditor = new DefaultCellEditor(jComboNumberTypes);
			//TODO: enable autocomplete for numbertypes picklist.
			//AutoCompleteDecorator.decorate((JComboBox) comboBoxEditor.getComponent());
			typeColumn.setCellEditor(comboBoxEditor);
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setToolTipText("Click for pick list of number types.");
            typeColumn.setCellRenderer(renderer);
            jTableNumbers.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
            jTableNumbers.setRowHeight(jTableNumbers.getRowHeight() + 5);
            
            jTableNumbers.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnNumsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupNumbers.show(e.getComponent(),e.getX(),e.getY());
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) { 
						 clickedOnNumsRow = ((JTable)e.getComponent()).getSelectedRow();
						 jPopupNumbers.show(e.getComponent(),e.getX(),e.getY());
					}
				}
			});
		    
		    jPopupNumbers = new JPopupMenu();
			JMenuItem mntmDeleteRow = new JMenuItem("Delete Row");
			mntmDeleteRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					try { 
						if (clickedOnNumsRow>=0) { 
							int ok = JOptionPane.showConfirmDialog(thisPane, "Delete the selected number?", "Delete Number", JOptionPane.OK_CANCEL_OPTION);
							if (ok==JOptionPane.OK_OPTION) { 
								log.debug("deleting numbers row " + clickedOnNumsRow);
					            ((NumberTableModel)jTableNumbers.getModel()).deleteRow(clickedOnNumsRow);
					            setStateToDirty();
							} else { 
								log.debug("number row delete canceled by user.");
							}
						} else { 
						    JOptionPane.showMessageDialog(thisPane, "Unable to select row to delete.  Try empting number and type and pressing Save.");
						}
					} catch (Exception ex) { 
						log.error(ex.getMessage());
						JOptionPane.showMessageDialog(thisPane, "Failed to delete a number row. " + ex.getMessage());
					}
				}
			});	
			jPopupNumbers.add(mntmDeleteRow);
		}
		return jTableNumbers;
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
	        			((NumberTableModel)jTableNumbers.getModel()).addNumber(new edu.harvard.mcz.imagecapture.data.Number(specimen, "", ""));
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
			//allie add
			/*try {
				Set<LatLong> georeferences = specimen.getLatLong();
				log.debug("getJButtonGeoreference georeferences size is : + " + georeferences.size());
				LatLong georeference_pre = georeferences.iterator().next();
				log.debug("lat is : + " + georeference_pre.getLatDegString());
				log.debug("long is : + " + georeference_pre.getLongDegString());
				if ((!("").equals(georeference_pre.getLatDegString())) ||
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
	        	jButtonGeoReference.addActionListener(new java.awt.event.ActionListener() {
	        		public void actionPerformed(java.awt.event.ActionEvent e) {
	        			thisPane.setStateToDirty();
	        			Set<LatLong> georeferences = specimen.getLatLong();
	        			//log.debug("the lat long is : " + specimen.getLatLong().);
	        			LatLong georeference = georeferences.iterator().next();
	        			georeference.setSpecimenId(specimen);
	        			GeoreferenceDialog georefDialog = new GeoreferenceDialog(georeference);
	        			georefDialog.setVisible(true);
						georefDialog.addComponentListener(new ComponentAdapter() {
							@Override
							public void componentHidden(ComponentEvent e) {
								updateJButtonGeoreference();
								super.componentHidden(e);
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

	private void updateJButtonGeoreference() {
		if (specimen.getLatLong() != null && !specimen.getLatLong().isEmpty()) {
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
	        			((CollectorTableModel)jTableCollectors.getModel()).addCollector(new Collector(specimen, ""));
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
			jTextFieldDrawerNumber.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "DrawerNumber", jTextFieldDrawerNumber));
			jTextFieldDrawerNumber.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DrawerNumber"));
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
			jTextFieldVerbatimLocality.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "VerbatimLocality", jTextFieldVerbatimLocality));
			jTextFieldVerbatimLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimLocality"));
			jTextFieldVerbatimLocality.addKeyListener(new java.awt.event.KeyAdapter() {
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
		//if (jTextFieldCountry == null) {
			/*jTextFieldCountry = new JTextField();
			jTextFieldCountry.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Country", jTextFieldCountry));
			jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
			jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});*/
			//allie fix
			if (jComboBoxCountry == null) {
				log.debug("init jComboBoxCountry");
				SpecimenLifeCycle sls = new SpecimenLifeCycle();
				jComboBoxCountry = new JComboBox<String>();
				//jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
				jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(sls.getDistinctCountries()));
				jComboBoxCountry.setEditable(true);
				jComboBoxCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
				jComboBoxCountry.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						thisPane.setStateToDirty();
					}
				});
				AutoCompleteDecorator.decorate(jComboBoxCountry);
			}
		return jComboBoxCountry;
		//return jTextFieldCountry;
	}

	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	/*private JTextField getJTextField23() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
			jTextFieldPrimaryDivision.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
			jTextFieldPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "primaryDivison"));
			jTextFieldPrimaryDivision.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jTextFieldPrimaryDivision;
	}*/
	//allie change
	private JComboBox<String> getPrimaryDivisionJTextField() {
		if (jComboBoxPrimaryDivision == null) {
			jComboBoxPrimaryDivision = new JComboBox<>();
			jComboBoxPrimaryDivision.setEditable(true);
			//jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			jComboBoxPrimaryDivision.setModel(new DefaultComboBoxModel<String>(sls.getDistinctPrimaryDivisions()));
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
	private JComboBox<String> getFamilyJTextField() {
		if (jComboBoxFamily == null) {
			jComboBoxFamily = new JComboBox<String>();
			jComboBoxFamily.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctFamily()));
			jComboBoxFamily.setEditable(true);
			//jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Family", jTextFieldFamily));
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
			jComboBoxSubfamily = new JComboBox<String>();
			jComboBoxSubfamily.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
			jComboBoxSubfamily.setEditable(true);
			//jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Subfamily", jTextFieldSubfamily));
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
			jTextFieldTribe = new JTextField();
			jTextFieldTribe.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Tribe", jTextFieldTribe));
			jTextFieldTribe.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Tribe"));
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
			jComboBoxSex.setModel(new DefaultComboBoxModel<String>(Sex.getSexValues()));
			jComboBoxSex.setEditable(true);
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
			jComboBoxFeatures = new JComboBox<String>();
			jComboBoxFeatures.setModel(new DefaultComboBoxModel<String>(Features.getFeaturesValues()));
			jComboBoxFeatures.setEditable(true);
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
			jComboBoxNatureOfId = new JComboBox<String>();
			jComboBoxNatureOfId.setModel(new DefaultComboBoxModel<String>(NatureOfId.getNatureOfIdValues()));
			jComboBoxNatureOfId.setEditable(false);
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
			jComboBoxLifeStage = new JComboBox<String>();
			jComboBoxLifeStage.setModel(new DefaultComboBoxModel<String>(LifeStage.getLifeStageValues()));
			jComboBoxLifeStage.setEditable(true);
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
			jTextFieldDateNos = new JTextField();
			//jTextFieldDateNos.setToolTipText("Date found on labels where date might be either date collected or date emerged, or some other date");
			jTextFieldDateNos.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateNOS", jTextFieldDateNos));
			jTextFieldDateNos.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateNOS"));
			jTextFieldDateNos.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
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
			jTextFieldDateEmerged.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "DateEmerged", jTextFieldDateEmerged));
			jTextFieldDateEmerged.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateEmerged"));
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
			jTextFieldDateEmergedIndicator.setToolTipText("Verbatim text indicating that this is a date emerged.");
			jTextFieldDateEmergedIndicator.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "DateEmergedIndicator", jTextFieldDateEmergedIndicator));
			jTextFieldDateEmergedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateEmergedIndicator"));
			jTextFieldDateEmergedIndicator.addKeyListener(new java.awt.event.KeyAdapter() {
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
			jTextFieldDateCollected.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateCollected"));
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
			jTextFieldDateCollectedIndicator.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "DateCollectedIndicator", jTextFieldDateCollectedIndicator));
			jTextFieldDateCollectedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "DateCollectedIndicator"));
			jTextFieldDateCollectedIndicator.addKeyListener(new java.awt.event.KeyAdapter() {
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
			jTextFieldInfraspecificEpithet.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "InfraspecificEpithet", jTextFieldInfraspecificEpithet));
			jTextFieldInfraspecificEpithet.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "InfraspecificEpithet"));
			jTextFieldInfraspecificEpithet.addKeyListener(new java.awt.event.KeyAdapter() {
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
			jTextFieldInfraspecificRank.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "InfraspecificRank", jTextFieldInfraspecificRank));
			jTextFieldInfraspecificRank.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "InfraspecificRank"));
			jTextFieldInfraspecificRank.addKeyListener(new java.awt.event.KeyAdapter() {
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
			jTextFieldAuthorship.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Authorship", jTextFieldAuthorship));
			jTextFieldAuthorship.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Authorship"));
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
			jTextFieldUnnamedForm.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "UnnamedForm", jTextFieldUnnamedForm));
			jTextFieldUnnamedForm.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "UnnamedForm"));
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
			jTextFieldMinElevation.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "VerbatimElevation", jTextFieldMinElevation));
			jTextFieldMinElevation.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "VerbatimElevation"));
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
			jTextFieldCollectingMethod.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "CollectingMethod", jTextFieldCollectingMethod));
			jTextFieldCollectingMethod.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "CollectingMethod"));
			jTextFieldCollectingMethod.addKeyListener(new java.awt.event.KeyAdapter() {
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
			//jCheckBoxValidDistributionFlag.setToolTipText("Check if locality represents natural biological range.  Uncheck for Specimens that came from a captive breeding program");
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
			jTextFieldQuestions = new JTextField();
			jTextFieldQuestions.setBackground(MainFrame.BG_COLOR_QC_FIELD);
			jTextFieldQuestions.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Questions", jTextFieldQuestions));
			jTextFieldQuestions.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Questions"));
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
			jButtonAddPreparationType.setMnemonic(KeyEvent.VK_P);
			
			jButtonAddPreparationType.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					log.debug("Adding new SpecimenPart");
					SpecimenPart newPart = new SpecimenPart();
					newPart.setPreserveMethod(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_DEFAULT_PREPARATION));
					newPart.setSpecimenId(specimen);
					SpecimenPartLifeCycle spls = new SpecimenPartLifeCycle();
                    log.debug("Attaching new SpecimenPart");
					try {
						spls.persist(newPart);
					    specimen.getSpecimenParts().add(newPart);
					    ((AbstractTableModel)jTableSpecimenParts.getModel()).fireTableDataChanged();
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
			jTextFieldAssociatedTaxon.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "AssociatedTaxon", jTextFieldAssociatedTaxon));
			jTextFieldAssociatedTaxon.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "AssociatedTaxon"));
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
			jTextFieldHabitat.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Habitat", jTextFieldHabitat));
			jTextFieldHabitat.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Habitat"));
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
			jComboBoxWorkflowStatus.setModel(new DefaultComboBoxModel<String>(WorkFlowStatus.getWorkFlowStatusValues()));
			jComboBoxWorkflowStatus.setEditable(false);
			jComboBoxWorkflowStatus.setBackground(MainFrame.BG_COLOR_QC_FIELD);
			jComboBoxWorkflowStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "WorkflowStatus"));
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
			jComboBoxLocationInCollection.setModel(new DefaultComboBoxModel<String>(LocationInCollection.getLocationInCollectionValues()));
			jComboBoxLocationInCollection.setEditable(false);
			jComboBoxLocationInCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "LocationInCollection"));
			
			//alliefix - set default from properties file
			//jComboBoxLocationInCollection.setSelectedIndex(1);
			
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
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldInferences() {
		if (jTextFieldInferences == null) {
			jTextFieldInferences = new JTextField();
			jTextFieldInferences.setBackground(MainFrame.BG_COLOR_ENT_FIELD);
			jTextFieldInferences.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Inferences", jTextFieldInferences));
			jTextFieldInferences.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Inferences"));
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
	private JButton getHistoryJButton() {
		if (jButtonGetHistory == null) {
			jButtonGetHistory = new JButton();
			jButtonGetHistory.setText("History");
			jButtonGetHistory.setToolTipText("Show the history of who edited this record");
			jButtonGetHistory.setMnemonic(KeyEvent.VK_H);
			jButtonGetHistory.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// retrieve and display the tracking events for this specimen 
					//Tracking t = new Tracking();
					//t.setSpecimen(specimen);
					TrackingLifeCycle tls = new TrackingLifeCycle();
					//Request by specimen doesn't work with Oracle.  Why?  
					//EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimen(specimen)));
					EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimenId(specimen.getSpecimenId())));
					logViewer.pack();
					logViewer.setVisible(true);
					
				}
			});
		}
		return jButtonGetHistory;
	}


	private JButton getJButtonCopyPrev() {
		log.debug("prev spec:::");
		if (jButtonCopyPrev == null) {
			jButtonCopyPrev = new JButton();
			jButtonCopyPrev.setText("Copy Prev");
			jButtonCopyPrev.setToolTipText("Copy previous record values into this screen");
			//TODO: decide on keyboard shortcut
			//jButtonCopyPrev.setMnemonic(KeyEvent.VK_H);
			jButtonCopyPrev.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//populate the fields with the data.
					previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache;
					copyPreviousRecord();
				}
			});
			this.updateJButtonCopyPrev();
		}
		return jButtonCopyPrev;
	}

	private void updateJButtonCopyPrev() {
		jButtonCopyPrev.setEnabled(!(this.previousSpecimen == null && ImageCaptureApp.lastEditedSpecimenCache == null));
	}
	
	
	/**
	 * This method initializes jButtonNext
	 * 	
	 * @return javax.swing.JButton	
	 */
	//this is not the right arrow button!
	private JButton getJButtonNext() {
		if (jButtonNext == null) {
			jButtonNext = new JButton();
			URL iconFile = this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/next.png");
			if (iconFile!=null) { 
			   jButtonNext.setIcon(new ImageIcon(iconFile));
			} else { 
				jButtonNext.setText("Next");
			}
			jButtonNext.setMnemonic(KeyEvent.VK_N);
			jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable());
			jButtonNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//TODO here save the data in memory for "copy prev"
					try {
						// try to move to the next specimen in the table.
						if (thisPane.specimenController.openNextSpecimenInTable()) {
						   //thisPane.myControler.setSpecimen(thisPane.specimen.getSpecimenId() + 1);
						   thisPane.setVisible(false);
						   thisPane.specimenController.displayInEditor();
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
							log.error(ex);
						}
					}
				}
			});
		}
		log.debug("SpecimenDetailsViewPane.getJButtonNext(): 9");
		return jButtonNext;
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
			if (iconFile!=null) {
			   jButtonPrevious.setIcon(new ImageIcon(iconFile));
			} else { 
				jButtonPrevious.setText("Previous");
			}
			jButtonPrevious.setMnemonic(KeyEvent.VK_P);
			jButtonPrevious.setToolTipText("Move to Previous Specimen");
			jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable());
			jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						// try to move to the previous specimen in the table.
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
			});
		}
		return jButtonPrevious;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
		}
		return jPanel1;
	}
	
	private void setStateToClean() { 
		state = STATE_CLEAN;
		// if this is a record that is part of a navigateable set, enable the navigation buttons
		if (specimenController !=null){
			log.debug("Has controller");
			if (specimenController.isInTable()) {
				log.debug("Controller is in table");
				// test to make sure the buttons have been created before trying to enable them.
				if (jButtonNext!=null) { 
					jButtonNext.setEnabled(true);
				}
				if (jButtonPrevious!=null) { 
					jButtonPrevious.setEnabled(true);
				}
			}
		}
	}
	
	private void setStateToDirty() { 
		state = STATE_DIRTY;
		if (jButtonNext!=null) { 
			this.jButtonNext.setEnabled(false);
		}
		if (jButtonPrevious!=null) { 
			this.jButtonPrevious.setEnabled(false);
		}
	}
	
	/** State of the data in the forms as compared to the specimen from which the data was loaded.
	 * 
	 * @return true if the data as displayed in the forms hasn't changed since the data was last loaded from
	 * or saved to the specimen, otherwise false indicating a dirty record.  
	 */
	private boolean isClean()  {
	    boolean result = false;
	    if (state==STATE_CLEAN) { 
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
			jTextFieldISODate.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "ISODate", jTextFieldISODate));
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

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCitedInPublicationJTextField() {
		if (jTextFieldCitedInPub == null) {
			jTextFieldCitedInPub = new JTextField();
			jTextFieldCitedInPub.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "CitedInPublication", jTextFieldCitedInPub));
			jTextFieldCitedInPub.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "CitedInPublication"));
			jTextFieldCitedInPub.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jTextFieldCitedInPub;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneNotes() {
		if (jScrollPaneNotes == null) {
			jScrollPaneNotes = new JScrollPane();
			jScrollPaneNotes.setViewportView(getJTextAreaNotes());
			jScrollPaneNotes.setPreferredSize(new Dimension(0, 50));
			jScrollPaneNotes.setMinimumSize(new Dimension(0, 50));
			jScrollPaneNotes.addMouseWheelListener(new MouseWheelScrollListener(jScrollPaneNotes));
			//jScrollPaneNotes.add(getJTextAreaNotes()); //allie!!!
		}
		return jScrollPaneNotes;
	}
	
	/*private JTextField getJScrollPaneNotes() {
		if (jScrollPaneNotes == null) {
			jScrollPaneNotes = new JTextField();
			//jScrollPaneNotes.setViewportView(getJTextAreaNotes());
			jScrollPaneNotes.add(getJTextAreaNotes()); //allie!!!
		}
		return jScrollPaneNotes;
	}*/	

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDateEmergedJButton() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Date Emerged");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jTextFieldDateNos.getText().equals("")) { 
						jTextFieldDateNos.setText(jTextFieldDateEmerged.getText());
					} else { 
						jTextFieldDateEmerged.setText(jTextFieldDateNos.getText());
					}
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDateCollectedJButton() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Date Collected");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jTextFieldDateNos.getText().equals("")) { 
						jTextFieldDateNos.setText(jTextFieldDateCollected.getText());
					} else { 
						jTextFieldDateCollected.setText(jTextFieldDateNos.getText());
					}
				}
			});
		}
		return jButton2;
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
		if (jTextFieldMigrationStatus ==null) {
			jTextFieldMigrationStatus = new JTextField();
			//jLabelMigrationStatus.setBackground(null);
			//jLabelMigrationStatus.setBorder(null);
			jTextFieldMigrationStatus.setEditable(false);
			jTextFieldMigrationStatus.setText("");
		    if (specimen.isStateDone()) { 
		    	String uri = "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.getCatNum();
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
		if (specimen.getICImages()!=null) {
			imageCount = specimen.getICImages().size();
		}
		jTextFieldImageCount.setText(Integer.toString(imageCount));
		if (imageCount > 1) {
			jTextFieldImageCount.setForeground(Color.RED);
		} else {
			jTextFieldImageCount.setForeground(Color.BLACK);
		}
	}

	/*

	private FilteringGeogJComboBox getComboBoxHighGeog() {
		if (comboBoxHigherGeog == null) {
			MCZbaseGeogAuthRecLifeCycle mls = new MCZbaseGeogAuthRecLifeCycle();
			comboBoxHigherGeog = new FilteringGeogJComboBox();
			comboBoxHigherGeog.setHGModel(new HigherGeographyComboBoxModel(mls.findAll()));
			comboBoxHigherGeog.setEditable(true);
			comboBoxHigherGeog.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.debug(e.getActionCommand());
					comboBoxHigherGeog.getSelectedIndex();
					log.debug(comboBoxHigherGeog.getSelectedItem());
					log.debug("Selected Index: " + comboBoxHigherGeog.getSelectedIndex());
					MCZbaseGeogAuthRec utl = (MCZbaseGeogAuthRec) ((HigherGeographyComboBoxModel)comboBoxHigherGeog.getModel()).getSelectedItem();
					if (utl==null) { 
						log.debug("null");
					} else { 
					    log.debug(utl.getHigher_geog());
					    specimen.setHigherGeography(utl.getHigher_geog());
					    comboBoxHigherGeog.setBackground(Color.WHITE);
					}
				}
			});
		}
		return comboBoxHigherGeog;
	}*/
	private JTextField getTextFieldMaxElev() {
		if (textFieldMaxElev == null) {
			textFieldMaxElev = new JTextField();
		}
		return textFieldMaxElev;
	}
	private JComboBox<String> getComboBoxElevUnits() {
		if (comboBoxElevUnits == null) {
			comboBoxElevUnits = new JComboBox<String>();
			comboBoxElevUnits.setModel(new DefaultComboBoxModel<>(new String[] {"", "?", "m", "ft"}));
		}
		return comboBoxElevUnits;
	}
	private JTextField getTextFieldMicrohabitat() {
		if (textFieldMicrohabitat == null) {
			textFieldMicrohabitat = new JTextField();
		}
		return textFieldMicrohabitat;
	}
	
	/**
	 * This method initializes jTextFieldDateDetermined
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDateDetermined() {
		if (jTextFieldDateDetermined == null) {
			jTextFieldDateDetermined = new JTextField();
			jTextFieldDateDetermined.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "ISODate", jTextFieldDateDetermined));
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
	//original code
	/*private FilteringAgentJComboBox getJCBDeterminer() {
		if (jCBDeterminer == null) {
			jCBDeterminer = new FilteringAgentJComboBox();
			jCBDeterminer.setEditable(true);
			jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "IdentifiedBy"));
			jCBDeterminer.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jCBDeterminer;
	}	*/

	//allie change 1
	/*private JTextField getJCBDeterminer() {
		if (jCBDeterminer == null) {
			jCBDeterminer = new JTextField();
			jCBDeterminer.setEditable(true);
			jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "IdentifiedBy"));
			jCBDeterminer.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jCBDeterminer;
	}*/
	//allie change 2	
	private JComboBox<String> getJCBDeterminer() {
		log.debug("calling getJCBDeterminer() ... it is " + jCBDeterminer);
		if (jCBDeterminer == null) {
			log.debug("initt jCBDeterminer determiner null, making a new one");
			SpecimenLifeCycle sls = new SpecimenLifeCycle();
			jCBDeterminer = new JComboBox<String>();
			jCBDeterminer.setModel(new DefaultComboBoxModel<String>(sls.getDistinctDeterminers()));
			jCBDeterminer.setEditable(true);
			//jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
			//jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
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
			// cbTypeStatus = new JComboBox(TypeStatus.getTypeStatusValues());  // for visual editor
			cbTypeStatus.setEditable(true);
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
			jTextFieldIdRemarks.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "IdentificationRemarks"));
			jTextFieldIdRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jTextFieldIdRemarks;
	}
}  //  @jve:decl-index=0:visual-constraint="10,15"
