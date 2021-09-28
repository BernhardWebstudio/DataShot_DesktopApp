package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.interfaces.ProgressListener;
import edu.harvard.mcz.imagecapture.jobs.NahimaExportJob;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class NahimaExportDialog extends JDialog implements ProgressListener {
    private static final Logger log =
            LoggerFactory.getLogger(NahimaExportDialog.class);
    private JPanel jContentPanel;
    private JProgressBar progressBar;
    private JTextArea resultsTextField;
    private JButton closeButton;
    private NahimaExportJob job;

    public NahimaExportDialog(Frame owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setTitle("Nahima Export");
        this.setContentPane(getJContentPane());
        this.pack();
        job = new NahimaExportJob();
        job.addProgressListener(this);
        Thread runningThread = new Thread(() -> {
            boolean didRunSuccessfully = true;
            try {
                job.run();
            } catch (Exception e) {
                didRunSuccessfully = false;
                log.error("Error when running Nahima export job", e);
                getResultsTextField().setText("Error: " + e.getMessage());
                getCloseButton().setEnabled(true);
            }
            if (job.getLastException() != null) {
                didRunSuccessfully = false;
                getResultsTextField().setText("Error: " + job.getLastException().getMessage());
            } else if (didRunSuccessfully) {
                getResultsTextField().setText("Finished processing " + job.getTotalCount() + " Specimen");
            }
            getCloseButton().setEnabled(true);
        });
        runningThread.start();
    }

    private JPanel getJContentPane() {
        if (jContentPanel == null) {
            jContentPanel = new JPanel(new BorderLayout());
            JPanel jScrollPaneContent = new JPanel();
            jScrollPaneContent.setLayout(new MigLayout("wrap 1, fillx"));
            // add contents
            jScrollPaneContent.add(this.getProgressBar(), "grow");
            jScrollPaneContent.add(this.getResultsTextField(), "grow");
            jScrollPaneContent.add(this.getCloseButton());

            // add the scroll pane content to the panel
            JScrollPane scrollPane = new JScrollPane(jScrollPaneContent);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            jContentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        return jContentPanel;
    }

    private JTextArea getResultsTextField() {
        if (resultsTextField == null) {
            resultsTextField = new JTextArea();
            resultsTextField.setEnabled(false);
            resultsTextField.setLineWrap(true);
            resultsTextField.setRows(5);
            resultsTextField.setText("Please do not close this window until the export is done.");
        }
        return resultsTextField;
    }

    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
        }
        return progressBar;
    }

    @Override
    public void progressChanged(int progress) {
        if (progress == 0) {
            progress = job.percentComplete();
        }
        this.getProgressBar().setValue(progress);

        if (job.getStatus() == NahimaExportJob.STATUS_FINISHED) {
            this.getCloseButton().setEnabled(true);
        }
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton("Close");
            closeButton.setEnabled(false);

            closeButton.addActionListener(actionEvent -> this.setVisible(false));
        }
        return closeButton;
    }
}
