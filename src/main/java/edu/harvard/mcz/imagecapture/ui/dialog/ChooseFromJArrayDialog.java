package edu.harvard.mcz.imagecapture.ui.dialog;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ChooseFromJArrayDialog extends JDialog {

    public static final int RETURN_ACCEPT = 1;
    public static final int RETURN_SKIP = 2;
    public static final int RETURN_CHANGE_SEARCH = 3;
    public static final int RETURN_CREATE_NEW = 4;
    private static final Logger log = LoggerFactory.getLogger(ChooseFromJArrayDialog.class);
    private final JSONArray selection;
    private final String entityType;
    private final String search;
    private int returnDecision;
    // the UI elements
    private JPanel jContentPane;
    private JTextPane descriptionPane;
    private ButtonGroup selectionButtonGroup;
    private JPanel buttonGroupPanel;
    private JButton acceptChoiceButton;
    private JButton skipSpecimenButton;
    private JButton createNewButton;


    public ChooseFromJArrayDialog(Frame owner, JSONArray selection, String entityType, String search) {
        super(owner, "Choose a matching ...", ModalityType.APPLICATION_MODAL);
        this.entityType = entityType;
        this.selection = selection;
        this.search = search;
        initialize();
    }

    private void initialize() {
        this.setTitle("Choose the matching " + entityType);
        this.setContentPane(getJContentPane());
        this.pack();
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel(new BorderLayout());
            JPanel jScrollPaneContent = new JPanel();
            jScrollPaneContent.setLayout(new MigLayout("wrap 1, fillx"));
            // add contents
            jScrollPaneContent.add(this.getDescription(), "grow");
            jScrollPaneContent.add(this.getSelectionPane(), "grow");
            jScrollPaneContent.add(this.getSkipSpecimenButton(), "grow, split 3");
            jScrollPaneContent.add(this.getCreateNewButton(), "grow");
            jScrollPaneContent.add(this.getAcceptChoiceButton(), "grow");

            // add the scroll pane content to the panel
            JScrollPane scrollPane = new JScrollPane(jScrollPaneContent);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            jContentPane.add(scrollPane, BorderLayout.CENTER);
        }
        return jContentPane;
    }

    public JTextPane getDescription() {
        if (descriptionPane == null) {
            descriptionPane = new JTextPane();
            descriptionPane.setText("Search for \"" + this.search + "\" resulted in multiple results. Which one is the correct one?");
        }
        return descriptionPane;
    }

    public String simplifyJSONObjectText(Object obj) {
        try {
            JSONObject jsonObject = (JSONObject) obj;
            try {
                while (jsonObject.has(entityType) && !jsonObject.has("_standard")) {
                    jsonObject = jsonObject.getJSONObject(entityType);
                }

                if (jsonObject.has("_standard")) {
                    JSONObject standardObj = jsonObject.getJSONObject("_standard");
                    if (standardObj.keySet().size() == 1) {
                        standardObj = standardObj.getJSONObject((String) standardObj.keySet().toArray()[0]);

                        JSONObject textObj = standardObj.getJSONObject("text");
                        return textObj.toString();
                    } else {
                        log.debug("More than 1 _standard available -> cannot simplify object: " + standardObj.toString());
                    }
                } else {
                    log.debug("Key _standard not available -> cannot simplify object: " + jsonObject.toString());
                }
            } catch (Exception e) {
                log.error("Failed to simplify JSON object to text: " + obj.toString(), e);
            }

            return obj.toString();
        } catch (ClassCastException e) {
            return obj.toString();
        }
    }

    public JPanel getSelectionPane() {
        if (buttonGroupPanel == null) {
            selectionButtonGroup = new ButtonGroup();
            buttonGroupPanel = new JPanel();
            buttonGroupPanel.setLayout(new GridLayout(selection.length(), 1));

            for (Object obj : this.selection) {
                JRadioButton radioButton = new JRadioButton();
                radioButton.setText(simplifyJSONObjectText(obj));
                selectionButtonGroup.add(radioButton);
                buttonGroupPanel.add(radioButton);
            }

            buttonGroupPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Possibilities"));
        }
        return buttonGroupPanel;
    }

    public JButton getAcceptChoiceButton() {
        if (acceptChoiceButton == null) {
            acceptChoiceButton = new JButton("Continue with Choice");
            acceptChoiceButton.addActionListener(actionEvent -> {
                returnDecision = RETURN_ACCEPT;
                setVisible(false);
                dispose();
            });
        }
        return acceptChoiceButton;
    }

    public JButton getSkipSpecimenButton() {
        if (skipSpecimenButton == null) {
            skipSpecimenButton = new JButton("Skip Specimen");

            skipSpecimenButton.addActionListener((e) -> {
                returnDecision = RETURN_SKIP;
                setVisible(false);
                dispose();
            });
        }
        return skipSpecimenButton;
    }

    public JButton getCreateNewButton() {
        if (createNewButton == null) {
            createNewButton = new JButton("Create New");

            createNewButton.addActionListener((e) -> {
                returnDecision = RETURN_CREATE_NEW;
                setVisible(false);
                dispose();
            });
        }
        return createNewButton;
    }

    public int getSelectedIndex() {
        Enumeration<AbstractButton> buttons = selectionButtonGroup.getElements();
        int i = 0;
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public JSONObject getSelectedItem() {
        return getSelectedIndex() >= 0 ? selection.getJSONObject(getSelectedIndex()) : null;
    }

    public int getReturnDecision() {
        return returnDecision;
    }
}
