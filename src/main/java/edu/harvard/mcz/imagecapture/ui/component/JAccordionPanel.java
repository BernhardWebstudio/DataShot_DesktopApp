package edu.harvard.mcz.imagecapture.ui.component;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Panel that has a button to trigger the visibility of another component
 */
public class JAccordionPanel extends JPanel {

    private static final Logger log =
            LoggerFactory.getLogger(JAccordionPanel.class);
    private String titleOpen = "";
    private String titleClosed = "";
    private final JScrollPane mainContent;
    private JButton toggleButton = null;
    private boolean contentVisible = false;


    /**
     * Instantiate the panel with a button to trigger the visibility of another component
     *
     * @param titleOpen   the button text to show when the content is visible
     * @param titleClosed the button text to show when the content is hidden
     * @param content     the other component to trigger the visibility of
     */
    public JAccordionPanel(String titleOpen, String titleClosed, Component content) {
        super(new MigLayout("wrap 1, fillx"));
        this.titleClosed = titleClosed;
        this.titleOpen = titleOpen;

        this.mainContent = new JScrollPane(content);
        this.mainContent.setVisible(this.contentVisible);
        this.add(this.getToggleButton(), "grow");
        this.add(this.mainContent, "grow, hidemode 2");
    }

    public JAccordionPanel(String titleOpen, Component content) {
        this(titleOpen, titleOpen, content);
    }

    public JAccordionPanel(Component content) {
        this("Hide Details", "Show Details", content);
    }

    /**
     * Instantiate the visibility toggling button
     *
     * @return
     */
    private JButton getToggleButton() {
        if (toggleButton == null) {
            toggleButton = new JButton(this.titleOpen);
            resetTitle();
            JAccordionPanel self = this;
            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    log.debug("Changing visibility of accordion content to " + self.contentVisible);
                    self.contentVisible = !self.contentVisible;
                    self.mainContent.setVisible(self.contentVisible);
                    resetTitle();
                    self.revalidate();
                }
            });
        }
        return toggleButton;
    }

    /**
     * Re-set the title
     */
    private void resetTitle() {
        this.toggleButton.setText(this.contentVisible ? this.titleOpen : this.titleClosed);
    }
}
