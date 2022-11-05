package edu.harvard.mcz.imagecapture.ui.dialog;

import de.sciss.syntaxpane.syntaxkits.JavaScriptSyntaxKit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VerifyJSONDialog extends JDialog {

    public static final int RETURN_ACCEPT = 1;
    public static final int RETURN_SKIP = 2;
    public static final int RETURN_CHANGE_SEARCH = 3;

    String json = "{}";
    String search = "";
    private int returnDecision;

    // the UI elements
    private JPanel jContentPane;
    private JTextPane descriptionPane;
    private JButton acceptChoiceButton;
    private JButton skipSpecimenButton;
    private JEditorPane editor;

    public VerifyJSONDialog(Frame owner, String json, String search) {
        super(owner, "Create a matching ...", ModalityType.APPLICATION_MODAL);
        this.json = json;
        this.search = search;
        initialize();
    }

    private void initialize() {
        this.setTitle("Check Creation of New Nahima Object");
        this.setContentPane(getJContentPane());
        this.pack();
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel(new BorderLayout());
            JPanel jScrollPaneContent = new JPanel();
            jScrollPaneContent.setLayout(new MigLayout("wrap 1, fillx, hmin 250lp"));
            // add contents
            jScrollPaneContent.add(this.getDescription(), "grow");
            jScrollPaneContent.add(this.getEditor(), "grow, hmin 250lp");
            jScrollPaneContent.add(this.getSkipSpecimenButton(), "grow, split 2");
            jScrollPaneContent.add(this.getAcceptChoiceButton(), "grow");

            // add the scroll pane content to the panel
            JScrollPane scrollPane = new JScrollPane(jScrollPaneContent);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            jContentPane.add(scrollPane, BorderLayout.CENTER);
        }
        return jContentPane;
    }

    public JEditorPane getEditor() {
        if (editor != null) {
            return editor;
        }
        editor = new JEditorPane();
        editor.setEditorKit(new JavaScriptSyntaxKit());
        editor.setText(this.json);
        return editor;
    }

    public JTextPane getDescription() {
        if (descriptionPane != null) {
            return descriptionPane;
        }
        descriptionPane = new JTextPane();
        descriptionPane.setText("Search for \"" + this.search + "\" resulted in no results. Can I create a new one like this?");
        return descriptionPane;
    }

    public JButton getAcceptChoiceButton() {
        if (acceptChoiceButton != null) {
            return acceptChoiceButton;
        }
        acceptChoiceButton = new JButton("Continue with Creation");
        acceptChoiceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                returnDecision = RETURN_ACCEPT;
                setVisible(false);
                dispose();
            }
        });
        return acceptChoiceButton;
    }

    public JButton getSkipSpecimenButton() {
        if (skipSpecimenButton != null) {
            return skipSpecimenButton;
        }
        skipSpecimenButton = new JButton("Skip Specimen");

        skipSpecimenButton.addActionListener((e) -> {
            returnDecision = RETURN_SKIP;
            setVisible(false);
            dispose();
        });
        return skipSpecimenButton;
    }

    public String getResultingJSON() {
        return this.getEditor().getText();
    }

    public int getReturnDecision() {
        return returnDecision;
    }
}
