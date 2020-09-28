/**
 * FieldLoaderWizard.java
 * <p>
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.loader;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FieldLoaderWizard extends JDialog {

    private static final long serialVersionUID = 6643976186857446662L;

    private final JPanel contentPanel = new JPanel();
    private final JTextField filenameField;
    private final JTable table;
    private final JTextField textFieldOKToLoad;

    private File selectedFile;

    /**
     * Create a dialog to manage loading transcribed verbatim data or verbatim classified data from an external file..
     */
    public FieldLoaderWizard() {
        setTitle("Load Data");
        setBounds(100, 100, 665, 345);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
        gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPanel);
        {
            JButton btnNewButton = new JButton("Pick File");
            GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
            gbc_btnNewButton.anchor = GridBagConstraints.EAST;
            gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
            gbc_btnNewButton.gridx = 0;
            gbc_btnNewButton.gridy = 0;
            contentPanel.add(btnNewButton, gbc_btnNewButton);
        }
        {
            filenameField = new JTextField();
            filenameField.setEditable(false);
            GridBagConstraints gbc_filename = new GridBagConstraints();
            gbc_filename.insets = new Insets(0, 0, 5, 0);
            gbc_filename.fill = GridBagConstraints.HORIZONTAL;
            gbc_filename.gridx = 1;
            gbc_filename.gridy = 0;
            contentPanel.add(filenameField, gbc_filename);
            filenameField.setColumns(10);
        }

        JLabel lblOverwriteExistingVerbatim = new JLabel("Existing Verbatim Values:");
        GridBagConstraints gbc_lblOverwriteExistingVerbatim = new GridBagConstraints();
        gbc_lblOverwriteExistingVerbatim.anchor = GridBagConstraints.EAST;
        gbc_lblOverwriteExistingVerbatim.insets = new Insets(0, 0, 5, 5);
        gbc_lblOverwriteExistingVerbatim.gridx = 0;
        gbc_lblOverwriteExistingVerbatim.gridy = 1;
        contentPanel.add(lblOverwriteExistingVerbatim, gbc_lblOverwriteExistingVerbatim);

        JCheckBox chckbxOvewrite = new JCheckBox("Ovewrite");
        GridBagConstraints gbc_chckbxOvewrite = new GridBagConstraints();
        gbc_chckbxOvewrite.anchor = GridBagConstraints.WEST;
        gbc_chckbxOvewrite.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxOvewrite.gridx = 1;
        gbc_chckbxOvewrite.gridy = 1;
        contentPanel.add(chckbxOvewrite, gbc_chckbxOvewrite);
        {
            table = new JTable();
            GridBagConstraints gbc_table = new GridBagConstraints();
            gbc_table.insets = new Insets(0, 0, 5, 0);
            gbc_table.gridwidth = 2;
            gbc_table.fill = GridBagConstraints.BOTH;
            gbc_table.gridx = 0;
            gbc_table.gridy = 2;
            contentPanel.add(table, gbc_table);
        }
        {
            textFieldOKToLoad = new JTextField();
            textFieldOKToLoad.setEditable(false);
            textFieldOKToLoad.setBackground(Color.RED);
            GridBagConstraints gbc_textFieldOKToLoad = new GridBagConstraints();
            gbc_textFieldOKToLoad.insets = new Insets(0, 0, 0, 5);
            gbc_textFieldOKToLoad.fill = GridBagConstraints.HORIZONTAL;
            gbc_textFieldOKToLoad.gridx = 0;
            gbc_textFieldOKToLoad.gridy = 3;
            contentPanel.add(textFieldOKToLoad, gbc_textFieldOKToLoad);
            textFieldOKToLoad.setColumns(10);
        }
        {
            JButton okButton = new JButton("Load Data");
            okButton.setEnabled(false);
            GridBagConstraints gbc_okButton = new GridBagConstraints();
            gbc_okButton.anchor = GridBagConstraints.WEST;
            gbc_okButton.gridx = 1;
            gbc_okButton.gridy = 3;
            contentPanel.add(okButton, gbc_okButton);
            okButton.setActionCommand("OK");
            getRootPane().setDefaultButton(okButton);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JobVerbatimFieldLoad scan = new JobVerbatimFieldLoad(selectedFile);
                    (new Thread(scan)).start();
                }
            });
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    protected void pickFile() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_LASTLOADPATH) != null) {
            fileChooser.setCurrentDirectory(new File(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_LASTLOADPATH)));
        }

        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
    }
}
