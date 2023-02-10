package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingleNahimaExportDialog extends JDialog {
    private static final Logger log =
            LoggerFactory.getLogger(SingleNahimaExportDialog.class);
    private JTextField barcodeEnterField;
    private JButton doExportButton;
    private JPanel jContentPanel;
    private MainFrame parent;

    public SingleNahimaExportDialog(Frame owner, MainFrame parent) {
        super(owner);
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        this.setTitle("Export 1 Specimen to Nahima");
        this.setContentPane(getJContentPane());
        this.setPreferredSize(new Dimension(250, 100));
        this.pack();
    }

    private JPanel getJContentPane() {
        if (jContentPanel == null) {
            jContentPanel = new JPanel(new MigLayout("wrap 2, fillx"));

            JLabel label = new JLabel("Barcode of Specimen to export");
            jContentPanel.add(label, "right");
            jContentPanel.add(getBarcodeEntryField(), "grow");


            jContentPanel.add(getDoExportButton(), "wrap");
        }
        return jContentPanel;
    }

    private JButton getDoExportButton() {
        if (doExportButton == null) {
            doExportButton = new JButton("Export");
            doExportButton.addActionListener(actionEvent -> {
                NahimaExportDialog dialog = new NahimaExportDialog(parent, barcodeEnterField.getText());
                dialog.setVisible(true);
                setVisible(false);
            });
        }
        return doExportButton;
    }

    private JTextField getBarcodeEntryField() {
        if (barcodeEnterField == null) {
            barcodeEnterField = new JTextField();
        }
        return barcodeEnterField;
    }

}
