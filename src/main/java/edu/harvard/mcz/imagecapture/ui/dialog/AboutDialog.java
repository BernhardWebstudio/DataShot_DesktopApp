/**
 * AboutDialog.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * AboutDialog, a dialog describing the application, its version, contributors, and libraries.
 * <p>
 * Note: Revision, as shown here is one revision behind the commit to CVS of the jar file.
 */
public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 5585851631769499899L;
    private JPanel jContentPane = null;
    private JPanel jPanel1 = null;
    private JButton jButton = null;
    private AboutDialog thisDialog = null;
    private JPanel jPanel = null;
    private JTextField jTextFieldApp = null;
    private JTextField jTextFieldVersion = null;
    private JTextField jTextField = null;
    private JTextArea jTextArea = null;
    private JTextField jTextFieldCopyright = null;
    private JTextField jTextField1 = null;
    private JTextField jTextField2 = null;
    private JTextArea textArea;
    private JLabel lblLicence;

    /**
     * This method initializes
     */
    public AboutDialog() {
        super();
        thisDialog = this;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(new Dimension(694, 530));
        this.setTitle("About" + ImageCaptureApp.APP_NAME);
        this.setContentPane(getJPanel());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
    }

    /**
     * This method assembles the close button with the content jpanels
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanelWrapper(), BorderLayout.SOUTH);
            jContentPane.add(getJPanelContent(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelWrapper() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(getJButtonClose(), new GridBagConstraints());
        }
        return jPanel1;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonClose() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Close");
            jButton.setMnemonic(KeyEvent.VK_C);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisDialog.setVisible(false);
                    thisDialog.dispose();
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes the content jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelContent() {
        if (jPanel == null) {
            jPanel = new JPanel(new MigLayout("wrap 2"));
            // set fields
            String[] labels = {
                    "Application",
                    "Version",
                    "Description",
                    "License",
                    "Copyright",
                    "Contributors",
                    "Libraries"
            };

            Component[] fields = {
                    this.getJTextFieldApp(),
                    this.getJTextFieldVersion(),
                    this.getJTextAreaDescription(),
                    this.getTextAreaLicense(),
                    this.getJTextFieldCopyright(),
                    this.getJTextFieldContributors(),
                    this.getJTextFieldLibraries()
            };

            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel();
                label.setText(labels[i]);
                jPanel.add(label, "align label");
                jPanel.add(fields[i], "grow");
            }
        }
        return jPanel;
    }

    /**
     * This method initializes jTextFieldApp
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldApp() {
        if (jTextFieldApp == null) {
            jTextFieldApp = new JTextField(ImageCaptureApp.APP_NAME);
            jTextFieldApp.setEditable(false);
        }
        return jTextFieldApp;
    }

    /**
     * This method initializes jTextFieldVersion
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldVersion() {
        if (jTextFieldVersion == null) {
            jTextFieldVersion = new JTextField(ImageCaptureApp.getAppVersion());
            jTextFieldVersion.setEditable(false);
        }
        return jTextFieldVersion;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField(ImageCaptureApp.APP_REV);
            jTextField.setEditable(false);
        }
        return jTextField;
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextAreaDescription() {
        if (jTextArea == null) {
            jTextArea = new JTextArea(3, 50);
            jTextArea.setText(ImageCaptureApp.APP_DESCRIPTION);
            jTextArea.setEditable(false);
        }
        return jTextArea;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldCopyright() {
        if (jTextFieldCopyright == null) {
            jTextFieldCopyright = new JTextField(ImageCaptureApp.APP_COPYRIGHT);
            jTextFieldCopyright.setEditable(false);
        }
        return jTextFieldCopyright;
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldContributors() {
        if (jTextField1 == null) {
            jTextField1 = new JTextField(ImageCaptureApp.APP_CONTRIBUTORS);
            jTextField1.setEditable(false);
        }
        return jTextField1;
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldLibraries() {
        if (jTextField2 == null) {
            jTextField2 = new JTextField(ImageCaptureApp.APP_LIBRARIES);
            jTextField2.setEditable(false);
        }
        return jTextField2;
    }


    /**
     * @return JTextArea
     */
    private JTextArea getTextAreaLicense() {
        if (textArea == null) {
            textArea = new JTextArea();
            textArea.setText(ImageCaptureApp.APP_LICENSE);
            textArea.setEditable(false);
        }
        return textArea;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
