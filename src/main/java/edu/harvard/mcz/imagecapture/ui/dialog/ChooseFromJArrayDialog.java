package edu.harvard.mcz.imagecapture.ui.dialog;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ChooseFromJArrayDialog extends JDialog {

    public static final int RETURN_ACCEPT = 1;
    public static final int RETURN_SKIP = 2;
    public static final int RETURN_CHANGE_SEARCH = 3;
    public static final int RETURN_CREATE_NEW = 4;

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
        super(owner);
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

    public JPanel getSelectionPane() {
        if (selectionButtonGroup == null) {
            selectionButtonGroup = new ButtonGroup();
            buttonGroupPanel = new JPanel();
            buttonGroupPanel.setLayout(new GridLayout(selection.length(), 1));

            for (Object obj : this.selection) {
                JRadioButton btn = new JRadioButton();
                btn.setText(obj.toString());
                selectionButtonGroup.add(btn);
                buttonGroupPanel.add(btn);
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
            if (selectionButtonGroup.isSelected((ButtonModel) button)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public JSONObject getSelectedItem() {
        return selection.getJSONObject(getSelectedIndex());
    }

    public int getReturnDecision() {
        return returnDecision;
    }
}
