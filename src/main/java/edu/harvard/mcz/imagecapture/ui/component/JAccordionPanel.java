package edu.harvard.mcz.imagecapture.ui.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JAccordionPanel extends JPanel {

    private String title = "";
    private JScrollPane mainContent;
    private JButton toggleButton = null;
    private boolean contentVisible = true;

    public JAccordionPanel(String title, Component content) {
        super(new MigLayout("wrap 1, fillx"));
        this.title = title;
        this.mainContent = new JScrollPane(content);
        this.add(this.getToggleButton(), "grow");
        this.add(this.mainContent, "grow");
    }

    public JAccordionPanel(Component content) {
        this("Show Details", content);
    }

    private JButton getToggleButton() {
        if (toggleButton == null) {
            toggleButton = new JButton(this.title);
            JAccordionPanel self = this;
            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    self.contentVisible = !self.contentVisible;
                    self.mainContent.setVisible(self.contentVisible);
                }
            });
        }
        return toggleButton;
    }
}
