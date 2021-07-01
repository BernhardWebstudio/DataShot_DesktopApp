package edu.harvard.mcz.imagecapture.ui.component;

import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JAccordionPanel extends JPanel {

    private String title = "";
    private JScrollPane mainContent;
    private static final Logger log =
            LoggerFactory.getLogger(JAccordionPanel.class);
    private JButton toggleButton = null;
    private boolean contentVisible = false;

    public JAccordionPanel(String title, Component content) {
        super(new MigLayout("wrap 1, fillx"));
        this.title = title;
        this.mainContent = new JScrollPane(content);
        this.mainContent.setVisible(this.contentVisible);
        this.add(this.getToggleButton(), "grow");
        this.add(this.mainContent, "grow, hidemode 2");
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
                    self.log.debug("Changing visibility of accordion content to " + String.valueOf(self.contentVisible));
                    self.contentVisible = !self.contentVisible;
                    self.mainContent.setVisible(self.contentVisible);
                    self.revalidate();
                }
            });
        }
        return toggleButton;
    }
}
