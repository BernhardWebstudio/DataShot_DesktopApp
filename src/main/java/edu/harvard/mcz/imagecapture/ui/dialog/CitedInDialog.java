package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.interfaces.CloseListener;
import edu.harvard.mcz.imagecapture.interfaces.CloseType;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CitedInDialog extends JDialog {

	private static final Logger log = LoggerFactory.getLogger(CitedInDialog.class);

	public static class CitationData {
		private String citedInPublication;
		private String citedInLink;
		private String citedInComment;
		private String citedInYear;

		public CitationData(String citedIn, String link, String comment, String year) {
			this.citedInPublication = citedIn;
			this.citedInLink = link;
			this.citedInComment = comment;
			this.citedInYear = year;
		}

		public String getCitedInPublication() {
			return citedInPublication;
		}
		public void setCitedInPublication(String citedInPublication) {
			this.citedInPublication = citedInPublication;
		}
		public String getCitedInLink() {
			return citedInLink;
		}
		public void setCitedInLink(String citedInLink) {
			this.citedInLink = citedInLink;
		}
		public String getCitedInComment() {
			return citedInComment;
		}
		public void setCitedInComment(String citedInComment) {
			this.citedInComment = citedInComment;
		}
		public String getCitedInYear() {
			return citedInYear;
		}
		public void setCitedInYear(String citedInYear) {
			this.citedInYear = citedInYear;
		}
	}

	private final CitationData data;
	private final FormBindingContext<CitationData> bindingContext;
	private final List<CloseListener> closeListener = new ArrayList<>();

	public CitedInDialog(String citedIn, String link, String comment, String year) {
		this.data = new CitationData(citedIn, link, comment, year);
		this.bindingContext = new FormBindingContext<>(CitationData.class, true);
		log.debug("CitedInDialog with {}, {}, {} and {}", citedIn, link, comment, year);
		init();
	}

	public void addCloseListener(CloseListener closeListener) {
		this.closeListener.add(closeListener);
	}

	public String getCitedInPublication() {
		return data.getCitedInPublication();
	}

	public String getCitedInLink() {
		return data.getCitedInLink();
	}

	public String getCitedInComment() {
		return data.getCitedInComment();
	}

	public String getCitedInPublicationYear() {
		return data.getCitedInYear();
	}

	private void init() {
		JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 10"));

		contentPanel.add(new JLabel("Cited In:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField(null, "citedInPublication", CitationData::getCitedInPublication,
				CitationData::setCitedInPublication, null), "grow");

		contentPanel.add(new JLabel("Link:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField(null, "citedInLink", CitationData::getCitedInLink,
				CitationData::setCitedInLink, null), "grow");

		contentPanel.add(new JLabel("Year:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField(null, "citedInYear", CitationData::getCitedInYear,
				CitationData::setCitedInYear, null), "grow");

		contentPanel.add(new JLabel("Comment:"), "tag label, right");
		contentPanel.add(bindingContext.bindTextField(null, "citedInComment", CitationData::getCitedInComment,
				CitationData::setCitedInComment, null), "grow");

		bindingContext.readFrom(data);

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> {
			closeListener.forEach(listener -> listener.onClose(CloseType.CANCEL, this));
			setVisible(false);
		});
		buttonPane.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(e -> {
			bindingContext.writeTo(data);
			closeListener.forEach(listener -> listener.onClose(CloseType.OK, this));
			setVisible(false);
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		this.setLayout(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.SOUTH);
		this.setMinimumSize(new Dimension(275, 100));
		this.pack();
	}
}
