package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.interfaces.CloseListener;
import edu.harvard.mcz.imagecapture.interfaces.CloseType;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CitedInDialog extends JDialog {

    private static final Logger log =
            LoggerFactory.getLogger(CitedInDialog.class);
    private final JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fillx"));
    private String citedInPublication;
    private String citedInLink;
    private String citedInComment;
    private String citedInYear;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField citedInTextField;
    private JTextField citedInLinkTextField;
    private JTextField citedInCommentTextField;
    private JTextField citedInYearTextField;

    private ArrayList<CloseListener> closeListener;

    public CitedInDialog(String citedIn, String link, String comment, String year) {
        this.closeListener = new ArrayList<CloseListener>();
        this.citedInComment = comment;
        this.citedInPublication = citedIn;
        this.citedInYear = year;
        this.citedInLink = link;
        log.debug("CitedInDialog with {}, {}, {} and {}", citedIn, link, comment, year);
        init();
    }

    public void addCloseListener(CloseListener closeListener) {
        this.closeListener.add(closeListener);
    }

    public String getCitedInPublication() {
        return citedInTextField.getText();
    }

    public String getCitedInLink() {
        return citedInLinkTextField.getText();
    }

    public String getCitedInComment() {
        return citedInCommentTextField.getText();
    }

    public String getCitedInPublicationYear() {
        return citedInYear;
    }

    private void init() {
        citedInTextField = new JTextField(this.citedInPublication);
        citedInCommentTextField = new JTextField(this.citedInComment);
        citedInLinkTextField = new JTextField(this.citedInLink);
        citedInYearTextField = new JTextField(this.citedInYear);

        Component[] fields = {
                citedInTextField,
                citedInLinkTextField,
                citedInYearTextField,
                citedInCommentTextField,
        };

        String[] labels = {
                "Cited In",
                "Link",
                "Year",
                "Comment",
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel();
            label.setText(labels[i].concat(":"));
            contentPanel.add(label, "tag label, right"); //"align label");
            contentPanel.add(fields[i], "grow");
        }

        // add buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        CitedInDialog self = this;
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeListener.forEach(listener -> listener.onClose(CloseType.OK, self));

                setVisible(false);
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeListener.forEach(listener -> listener.onClose(CloseType.CANCEL, self));
                setVisible(false);
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        this.add(contentPanel);
        this.setMinimumSize(new Dimension(275, 100));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        this.pack();
    }
}
