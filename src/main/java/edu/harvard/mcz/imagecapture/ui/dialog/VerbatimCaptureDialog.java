/**
 * VerbatimCaptureDialog.java
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

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.PositionTemplate;
import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.ImageLoadException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.loader.Verbatim;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dialog for transcribing verbatim data from specimen images, using declarative
 * form bindings.
 */
public class VerbatimCaptureDialog extends JDialog implements DataChangeListener {

	private static final long serialVersionUID = 4462958599102371519L;
	private static final Logger log = LoggerFactory.getLogger(VerbatimCaptureDialog.class);

	private Specimen specimen = null;
	private SpecimenController specimenController = null;
	private final FormBindingContext<Specimen> bindingContext;

	private JLabel lblBarcode;
	private JLabel lblCurrentid;
	private ImageZoomPanel imagePanelPinLabels;

	private JTextArea textFieldVerbLocality;
	private JTextField textFieldVerbDate;
	private JTextField textFieldVerbCollector;
	private JTextField textFieldVerbCollection;
	private JTextField textFieldVerbNumbers;
	private JTextArea textFieldVerbUnclassifiedText;
	private JTextField textFieldQuestions;
	private JComboBox<String> comboBoxWorkflowStatus;

	private JButton btnPrevious;
	private JButton btnNext;

	/**
	 * Create the dialog.
	 */
	public VerbatimCaptureDialog() {
		this.bindingContext = new FormBindingContext<>(Specimen.class, true);
		init();
	}

	/**
	 * Create the dialog for a specimen.
	 *
	 * @param targetSpecimen
	 *            the specimen to transcribe
	 * @param targetSpecimenController
	 *            the controller managing specimen data and navigation
	 */
	public VerbatimCaptureDialog(Specimen targetSpecimen, SpecimenController targetSpecimenController) {
		this.specimen = targetSpecimen;
		this.specimenController = targetSpecimenController;
		this.bindingContext = new FormBindingContext<>(Specimen.class, true);
		if (this.specimenController != null) {
			this.specimenController.addListener(this);
		}
		init();
		if (specimen != null) {
			setValues();
		}
	}

	protected void setValues() {
		if (specimen == null) {
			return;
		}
		lblBarcode.setText(specimen.getBarcode());
		lblCurrentid.setText(specimen.assembleScientificName());

		bindingContext.readFrom(specimen);
		textFieldQuestions.setText(specimen.getQuestions() != null ? specimen.getQuestions() : "");

		try {
			if (specimen.getICImages() != null && !specimen.getICImages().isEmpty()) {
				Iterator<ICImage> i = specimen.getICImages().iterator();
				ICImage image = i.next();
				String path = image.getPath() != null ? image.getPath() : "";
				File anImageFile = new File(ImageCaptureProperties.assemblePathWithBase(path, image.getFilename()));

				PositionTemplate defaultTemplate = PositionTemplate.findTemplateForImage(image);
				BufferedImage imagefile = ImageIO.read(anImageFile);
				int x = defaultTemplate.getLabelPosition().width;
				int y = defaultTemplate.getLabelPosition().height;
				int w = defaultTemplate.getLabelSize().width;
				int h = defaultTemplate.getLabelSize().height;
				setPinLabelImage(imagefile.getSubimage(x, y, w, h));
				fitPinLabels();
			}
		} catch (ImageLoadException | IOException e) {
			log.error("Failed to load specimen image for verbatim capture", e);
		}

		if (specimenController != null) {
			btnNext.setEnabled(specimenController.hasNextSpecimenInTable());
			btnPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable());
		} else {
			btnNext.setEnabled(false);
			btnPrevious.setEnabled(false);
		}
	}

	protected void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Transcribe Verbatim Data");
		setMinimumSize(new Dimension(1020, 640));
		setBounds(100, 100, 1020, 640);
		getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Header panel
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		headerPanel.add(new JLabel("Verbatim Data for:"));
		lblBarcode = new JLabel("Barcode");
		headerPanel.add(lblBarcode);
		lblCurrentid = new JLabel("CurrentID");
		headerPanel.add(lblCurrentid);
		contentPanel.add(headerPanel, BorderLayout.NORTH);

		// Form panel (West)
		JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 5", "[right][grow,fill]"));

		formPanel.add(new JLabel("Locality:"), "top");
		textFieldVerbLocality = bindingContext.bindTextArea("VerbatimLocality", Specimen::getVerbatimLocality,
				Specimen::setVerbatimLocality, 3);
		formPanel.add(new JScrollPane(textFieldVerbLocality), "growx");

		formPanel.add(new JLabel("Date:"));
		textFieldVerbDate = bindingContext.bindTextField("DateNOS", Specimen::getDateNos, Specimen::setDateNos);
		formPanel.add(textFieldVerbDate, "growx");

		formPanel.add(new JLabel("Collector:"));
		textFieldVerbCollector = bindingContext.bindTextField("VerbatimCollector", Specimen::getVerbatimCollector,
				Specimen::setVerbatimCollector);
		formPanel.add(textFieldVerbCollector, "growx");

		formPanel.add(new JLabel("Collection:"));
		textFieldVerbCollection = bindingContext.bindTextField("VerbatimCollection", Specimen::getVerbatimCollection,
				Specimen::setVerbatimCollection);
		formPanel.add(textFieldVerbCollection, "growx");

		formPanel.add(new JLabel("Numbers:"));
		textFieldVerbNumbers = bindingContext.bindTextField("VerbatimNumbers", Specimen::getVerbatimNumbers,
				Specimen::setVerbatimNumbers);
		formPanel.add(textFieldVerbNumbers, "growx");

		formPanel.add(new JLabel("Other Text:"), "top");
		textFieldVerbUnclassifiedText = bindingContext.bindTextArea("VerbatimUnclassifiedText",
				Specimen::getVerbatimUnclassifiedText, Specimen::setVerbatimUnclassifiedText, 3);
		formPanel.add(new JScrollPane(textFieldVerbUnclassifiedText), "growx");

		formPanel.add(new JLabel("Questions:"));
		textFieldQuestions = new JTextField(30);
		formPanel.add(textFieldQuestions, "growx");

		formPanel.add(new JLabel("Workflow Status:"));
		comboBoxWorkflowStatus = bindingContext.bindComboBox("WorkflowStatus",
				WorkFlowStatus.getVerbatimWorkFlowStatusValues(), Specimen::getWorkFlowStatus,
				(s, val) -> s.setWorkFlowStatus(val != null ? val : ""));
		formPanel.add(comboBoxWorkflowStatus, "growx");

		// Quick transcription helper buttons
		JPanel helperButtonsPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 0", "[grow,fill][grow,fill]"));
		JButton btnPartiallyIllegible = new JButton("Partially Illegible");
		btnPartiallyIllegible.addActionListener(e -> appendToQuestions(Verbatim.PARTLY_ILLEGIBLE));
		helperButtonsPanel.add(btnPartiallyIllegible);

		JButton btnNoLocality = new JButton("No Locality Data");
		btnNoLocality.addActionListener(e -> textFieldVerbLocality.setText(Verbatim.NO_LOCALITY_DATA));
		helperButtonsPanel.add(btnNoLocality);

		JButton btnEntirelyIllegible = new JButton("Entirely Illegible");
		btnEntirelyIllegible.addActionListener(e -> appendToQuestions(Verbatim.ENTIRELY_ILLEGIBLE));
		helperButtonsPanel.add(btnEntirelyIllegible);

		JButton btnNoDateData = new JButton("No Date Data");
		btnNoDateData.addActionListener(e -> textFieldVerbDate.setText("[No date data]"));
		helperButtonsPanel.add(btnNoDateData);

		JButton btnLabelTruncatedIn = new JButton("Label Truncated in Image");
		btnLabelTruncatedIn.addActionListener(e -> appendToQuestions(Verbatim.TRUNCATED_BY_IMAGE));
		helperButtonsPanel.add(btnLabelTruncatedIn);

		JButton btnNoCollectorData = new JButton("No Collector Data");
		btnNoCollectorData.addActionListener(e -> textFieldVerbCollector.setText("[No collector data]"));
		helperButtonsPanel.add(btnNoCollectorData);

		JButton btnNoPinLabels = new JButton("No Pin Labels");
		btnNoPinLabels.addActionListener(e -> appendToQuestions(Verbatim.NO_PIN_LABELS));
		helperButtonsPanel.add(btnNoPinLabels, "span 2");

		formPanel.add(helperButtonsPanel, "span 2, growx, gaptop 10");
		contentPanel.add(formPanel, BorderLayout.WEST);

		// Center Image panel
		JPanel imageContainer = new JPanel(new BorderLayout(0, 0));
		imageContainer.add(getImagePanePinLabels(), BorderLayout.CENTER);
		contentPanel.add(imageContainer, BorderLayout.CENTER);

		// Bottom Button Panel
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnPrevious = new JButton("Previous");
		btnPrevious.setEnabled(specimenController != null && specimenController.isInTable());
		btnPrevious.addActionListener(e -> {
			save();
			if (specimenController.switchToPreviousSpecimenInTable()) {
				specimen = specimenController.getSpecimen();
				setValues();
			}
		});
		buttonPane.add(btnPrevious);

		btnNext = new JButton("Next");
		btnNext.setEnabled(specimenController != null && specimenController.isInTable());
		btnNext.addActionListener(e -> {
			save();
			if (specimenController.switchToNextSpecimenInTable()) {
				specimen = specimenController.getSpecimen();
				setValues();
			}
		});
		buttonPane.add(btnNext);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(e -> {
			if (save()) {
				setVisible(false);
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> setVisible(false));
		buttonPane.add(cancelButton);

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	public void fitPinLabels() {
		if (imagePanelPinLabels != null) {
			imagePanelPinLabels.zoomToFit();
		}
	}

	private ImageZoomPanel getImagePanePinLabels() {
		if (imagePanelPinLabels == null) {
			imagePanelPinLabels = new ImageZoomPanel();
		}
		return imagePanelPinLabels;
	}

	public void setPinLabelImage(Image anImage) {
		getImagePanePinLabels().setImage((BufferedImage) anImage);
		getImagePanePinLabels().zoomToFit();
		this.pack();
		if (getImagePanePinLabels().getPreferredSize().height > 500
				|| getImagePanePinLabels().getPreferredSize().width > 500) {
			getImagePanePinLabels().setPreferredSize(new Dimension(500, 500));
		}
		getImagePanePinLabels().setMaximumSize(new Dimension(500, 500));
	}

	protected boolean save() {
		if (specimen == null) {
			return false;
		}
		if (specimen.isExported()) {
			JOptionPane.showMessageDialog(this, "This Specimen is already exported. No edit will be saved.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		try {
			bindingContext.writeTo(specimen);

			StringBuilder questions = new StringBuilder();
			if (specimen.getQuestions() != null) {
				questions.append(specimen.getQuestions());
			}
			String enteredQuestions = textFieldQuestions.getText() != null ? textFieldQuestions.getText().trim() : "";
			if (!enteredQuestions.isEmpty()) {
				if (!questions.toString().contains(enteredQuestions)) {
					if (questions.length() > 0) {
						questions.append(Verbatim.SEPARATOR);
					}
					questions.append(enteredQuestions);
				}
			}
			specimen.setQuestions(questions.toString());
			if (specimenController != null) {
				specimenController.save();
			}
			return true;
		} catch (SaveFailedException e) {
			log.error("Failed to save verbatim transcription", e);
			return false;
		}
	}

	protected void appendToQuestions(String newQuestion) {
		String current = textFieldQuestions.getText() != null ? textFieldQuestions.getText().trim() : "";
		if (!current.contains(newQuestion)) {
			if (!current.isEmpty()) {
				current += Verbatim.SEPARATOR + newQuestion;
			} else {
				current = newQuestion;
			}
			textFieldQuestions.setText(current);
		}
	}

	@Override
	public void notifyDataHasChanged() {
		// Handled via controller listeners
	}
}
