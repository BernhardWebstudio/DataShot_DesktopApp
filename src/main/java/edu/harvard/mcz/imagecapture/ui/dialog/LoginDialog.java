/**
 * LoginDialog.java
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
 */
package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.utility.HashUtility;
import net.miginfocom.swing.MigLayout;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * A database login dialog, including both username/password credentials and
 * specification of the database connection parameters.
 */
public class LoginDialog extends JDialog {

    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_LOGIN = 1;
    public static final String encryptPassword = "tmpPW-13.3.2021";
    private static final long serialVersionUID = -2016769537635603794L;
    private static final Logger log = LoggerFactory.getLogger(LoginDialog.class);
    private JDialog self = null;
    private int result = RESULT_LOGIN;
    private JPanel jPanel = null;
    private JTextField jTextFieldDBUsername = null;
    private JPasswordField jPasswordFieldDB = null;
    private JTextField jTextFieldDriver = null;
    private JTextField jTextFieldConnection = null;
    private JTextField jTextFieldDialect = null;
    private JButton jButtonLogin = null;
    private JButton jButtonCancel = null;

    private JTextField jTextFieldEmail = null;
    private JPasswordField jPasswordFieldUser = null;
    private JButton jButton2 = null;
    private JPanel jPanelAdvanced = null;
    private JLabel jLabelStatus = null;

    /**
     * Default constructor.  Produces a login dialog.
     */
    public LoginDialog() {
        super();
        self = this;
        initialize();
    }

    public static String hashPassword(JPasswordField ajPasswordField) {
        return HashUtility.getSHA1Hash(
                String.valueOf(ajPasswordField.getPassword()));
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setContentPane(getJPanel());
        this.setTitle("DataShot Login Dialog: Configured For: " +
                Singleton.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.KEY_COLLECTION));
        URL iconFile = this.getClass().getResource(
                "/edu/harvard/mcz/imagecapture/resources/images/icon.png");
        try {
            setIconImage(new ImageIcon(iconFile).getImage());
        } catch (Exception e) {
            log.error("Error", e);
        }
        if (Singleton.getSingletonInstance()
                .getProperties()
                .getProperties()
                .getProperty(ImageCaptureProperties.KEY_LOGIN_SHOW_ADVANCED)
                .equalsIgnoreCase("false")) {
            jPanelAdvanced.setVisible(false);
            this.setSize(new Dimension(650, 225));
        } else {
            jPanelAdvanced.setVisible(true);
            this.setSize(new Dimension(650, 355));
        }
        this.getRootPane().setDefaultButton(jButtonLogin);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
        //    this.pack();
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new MigLayout("wrap 2, fill"));

            JLabel keyImageLabel = new JLabel();
            URL iconFile = this.getClass().getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/key_small.png");
            try {
                // this.setIconImage(new ImageIcon(iconFile).getImage());
                keyImageLabel.setIcon(new ImageIcon(iconFile));
            } catch (Exception e) {
                System.out.println("Can't open icon file: " + iconFile);
            }
            // row
            jPanel.add(keyImageLabel);
            JLabel loginPrompt = new JLabel("Login & connect to database");
            Font f = loginPrompt.getFont();
            // bold
            loginPrompt.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            jPanel.add(loginPrompt);
            // row
            JLabel emailLabel = new JLabel("E-Mail/Username");
            jPanel.add(emailLabel);
            System.out.println(this.getJTextFieldEmail());
            jPanel.add(this.getJTextFieldEmail(), "growx");
            // row
            JLabel passwordLabel = new JLabel("Password");
            jPanel.add(passwordLabel);
            jPanel.add(this.getJPasswordFieldUser(), "growx");
            // row
            this.jLabelStatus = new JLabel();
            jPanel.add(this.jLabelStatus, "span 2, wrap");
            // row
            JLabel dbLabel = new JLabel("Database");
            jPanel.add(dbLabel);
            jPanel.add(this.getAdvancedSettingsJButton(), "wrap");
            // row
            jPanel.add(this.getJButtonCancel(), "tag cancel, align left");
            jPanel.add(this.getJButtonLogin(), "tag ok, align right");
            // row
            jPanel.add(this.getJPanelAdvanced(), "grow, span 2, wrap");
        }
        return jPanel;
    }

    /**
     * This method initializes jTextFieldSchemaName if necessary
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldSchemaName() {
        if (jTextFieldDBUsername == null) {
            jTextFieldDBUsername = new JTextField();
        }
        return jTextFieldDBUsername;
    }

    /**
     * This method initializes jPasswordFieldDB
     *
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getJPasswordFieldDB() {
        if (jPasswordFieldDB == null) {
            jPasswordFieldDB = new JPasswordField();
        }
        return jPasswordFieldDB;
    }

    /**
     * This method initializes jTextFieldDriver
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDriver() {
        if (jTextFieldDriver == null) {
            jTextFieldDriver = new JTextField("com.mysql.cj.jdbc.Driver");
        }
        return jTextFieldDriver;
    }

    /**
     * This method initializes jTextFieldConnection
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldConnection() {
        if (jTextFieldConnection == null) {
            jTextFieldConnection =
                    new JTextField("jdbc:mysql://localhost:3306/lepidoptera");
        }
        return jTextFieldConnection;
    }

    /**
     * This method initializes jTextFieldDialect
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDialect() {
        if (jTextFieldDialect == null) {
            jTextFieldDialect = new JTextField("org.hibernate.dialect.MySQLDialect");
        }
        return jTextFieldDialect;
    }

    /**
     * This method initializes jButtonLogin
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonLogin() {
        if (jButtonLogin == null) {
            jButtonLogin = new JButton("Login");
            jButtonLogin.setMnemonic(KeyEvent.VK_L);
            jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    result = LoginDialog.RESULT_LOGIN;
                    jButtonLogin.grabFocus();
                    getUserPasswordHash();
                    self.setVisible(false);
                }
            });
        }
        return jButtonLogin;
    }

    /**
     * This method initializes jButtonCancel
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton("Cancel");
            jButtonCancel.setMnemonic(KeyEvent.VK_C);
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    result = LoginDialog.RESULT_CANCEL;
                    self.setVisible(false);
                }
            });
        }
        return jButtonCancel;
    }

    /**
     * Obtain sha1 hash of the text in the user's Password field.  Assumes that
     * the text is in utf-8 encoding.  If SHA-1 isn't an available MessageDigest,
     * returns the plain text of the password.
     *
     * @return the SHA1 hash of the text in the (user)Password field.
     */
    public String getUserPasswordHash() {
        String result = hashPassword(jPasswordFieldUser);
        return result;
    }

    public String getUsername() {
        return jTextFieldEmail.getText();
    }

    public String getDBUserName() {
        return jTextFieldDBUsername.getText();
    }

    public void setDBUserName(String aDBSchemaName) {
        jTextFieldDBUsername.setText(aDBSchemaName);
    }

    public String getDBPassword() {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(encryptPassword);
        String passwordValue = String.valueOf(jPasswordFieldDB.getPassword());
        try {
            return textEncryptor.decrypt(passwordValue);
        } catch(EncryptionOperationNotPossibleException exception) {
            log.error("Failed to decrypt db password", exception);
            return passwordValue;
        }
    }

    public void setDBPassword(String aDBPassword) {
        jPasswordFieldDB.setText(aDBPassword);
        // Force advanced panel to open if no database password is stored.
        if (aDBPassword == null || aDBPassword.length() == 0) {
            jPanelAdvanced.setVisible(true);
        }
    }

    public String getDriver() {
        return jTextFieldDriver.getText();
    }

    /**
     * @param textFieldDriver the jTextFieldDriver to set
     */
    public void setDriver(String textFieldDriver) {
        jTextFieldDriver.setText(textFieldDriver);
    }

    public String getConnection() {
        return jTextFieldConnection.getText();
    }

    /**
     * @param textFieldConnection the jTextFieldConnection to set
     */
    public void setConnection(String textFieldConnection) {
        jTextFieldConnection.setText(textFieldConnection);
    }

    public String getDialect() {
        return jTextFieldDialect.getText();
    }

    /**
     * @param textFieldDialect the jTextFieldDialect to set
     */
    public void setDialect(String textFieldDialect) {
        jTextFieldDialect.setText(textFieldDialect);
    }

    public void setStatus(String aStatus) {
        this.jLabelStatus.setText(aStatus);
        this.jLabelStatus.updateUI();
    }

    public int getResult() {
        return result;
    }

    /**
     * This method initializes jTextFieldEmail
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldEmail() {
        if (jTextFieldEmail == null) {
            jTextFieldEmail = new JTextField();
        }
        return jTextFieldEmail;
    }

    /**
     * This method initializes jPasswordFieldUser
     *
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getJPasswordFieldUser() {
        if (jPasswordFieldUser == null) {
            jPasswordFieldUser = new JPasswordField();
        }
        return jPasswordFieldUser;
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private JButton getAdvancedSettingsJButton() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setText("Advanced");
            jButton2.setMnemonic(KeyEvent.VK_A);
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    toggleAdvanced();
                }
            });
        }
        return jButton2;
    }

    public void toggleAdvanced() {
        if (jPanelAdvanced.isVisible()) {
            this.setSize(new Dimension(650, 225));
            jPanelAdvanced.setVisible(false);
        } else {
            this.setSize(new Dimension(650, 355));
            jPanelAdvanced.setVisible(true);
        }
        //    this.pack();
    }

    /**
     * This method initializes jPanelAdvanced
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelAdvanced() {
        if (jPanelAdvanced == null) {
            JLabel jLabel4 = new JLabel();
            jLabel4.setText("Dialect");
            JLabel jLabel3 = new JLabel();
            jLabel3.setText("Connection");
            JLabel jLabel2 = new JLabel();
            jLabel2.setText("Driver");
            JLabel jLabel1 = new JLabel();
            jLabel1.setText("DBPassword");
            JLabel jLabel = new JLabel();
            jLabel.setText("Username");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.insets = new Insets(0, 15, 0, 0);
            gridBagConstraints4.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 3;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.anchor = GridBagConstraints.WEST;
            gridBagConstraints14.gridx = 1;
            gridBagConstraints14.gridy = 2;
            gridBagConstraints14.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.anchor = GridBagConstraints.EAST;
            gridBagConstraints9.gridy = 3;
            gridBagConstraints9.gridx = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.anchor = GridBagConstraints.EAST;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.anchor = GridBagConstraints.WEST;
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 5;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.fill = GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.anchor = GridBagConstraints.EAST;
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridy = 5;
            gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
            jPanelAdvanced = new JPanel();
            jPanelAdvanced.setLayout(new GridBagLayout());
            jPanelAdvanced.add(jLabel3, gridBagConstraints7);
            jPanelAdvanced.add(getJTextFieldConnection(), gridBagConstraints8);
            jPanelAdvanced.add(jLabel2, gridBagConstraints5);
            jPanelAdvanced.add(getJTextFieldDriver(), gridBagConstraints6);
            jPanelAdvanced.add(jLabel4, gridBagConstraints9);
            jPanelAdvanced.add(getJTextFieldDialect(), gridBagConstraints10);
            jPanelAdvanced.add(jLabel, gridBagConstraints);
            jPanelAdvanced.add(getJTextFieldSchemaName(), gridBagConstraints1);
            jPanelAdvanced.add(jLabel1, gridBagConstraints4);
            jPanelAdvanced.add(getJPasswordFieldDB(), gridBagConstraints3);
        }
        return jPanelAdvanced;
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
