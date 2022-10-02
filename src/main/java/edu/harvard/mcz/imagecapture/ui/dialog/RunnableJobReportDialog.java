/**
 * RunnableJobReportDialog.java
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

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError;
import edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RunnableJobReportDialog
 */
public class RunnableJobReportDialog extends JDialog {

    private static final long serialVersionUID = -8151583200152856827L;

    private static final Logger log =
            LoggerFactory.getLogger(RunnableJobReportDialog.class);

    private String title = "Job Results";
    private String textToCopy = null;
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JLabel titleJLabel = null;
    private JButton okJButton = null;
    private JScrollPane jScrollPane = null;
    private JTable jTable = null;
    private JTextArea jTextArea = null;
    private JDialog thisDialog = null;
    private RunnableJobErrorTableModel model = null;
    private JButton btnSave;
    private JButton copyReportButton;

    /**
     * @param owner
     * @wbp.parser.constructor
     */
    public RunnableJobReportDialog(Frame owner) {
        super(owner);
        thisDialog = this;
        initialize();
    }

    public RunnableJobReportDialog(Frame owner, String resultsMessage,
                                   List<RunnableJobError> errors, String title, String textToCopy) {
        super(owner);
        this.title = title;
        this.textToCopy = textToCopy;
        thisDialog = this;
        model = new RunnableJobErrorTableModel(errors);

        log.debug("Nr of rows in RunnableJobReportDialog: {}", model.getRowCount());

        initialize();

        jTextArea.setText(resultsMessage);
        pack();
    }

    public RunnableJobReportDialog(Frame owner, String resultsMessage,
                                   List<RunnableJobError> errors, String title) {
        this(owner, resultsMessage, errors, title, null);
    }

    public RunnableJobReportDialog(Frame owner, String resultsMessage,
                                   List<RunnableJobError> errors, int listType,
                                   String title, String textToCopy) {
        super(owner);
        this.title = title;
        this.textToCopy = textToCopy;
        thisDialog = this;
        model = new RunnableJobErrorTableModel(errors, listType);

        log.debug("Nr of rows in RunnableJobReportDialog: {}", model.getRowCount());
        log.debug("Nr of columns in RunnableJobReportDialog: {}", model.getColumnCount());
        log.debug(model.getColumnName(4));

        initialize();

        jTextArea.setText(resultsMessage);
        pack();
    }

    public RunnableJobReportDialog(Frame owner, String resultsMessage,
                                   List<RunnableJobError> errors, int listType,
                                   String title) {
        this(owner, resultsMessage, errors, listType, title, null);
    }

    public void setMessage(String resultsMessage) {
        if (resultsMessage == null) {
            jTextArea.setText("");
        } else {
            jTextArea.setText(resultsMessage);
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setPreferredSize(new Dimension(1000, 400));
        this.setTitle(title);
        this.setContentPane(getJContentPane());
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getTitleReportJPanel(), BorderLayout.NORTH);
            jContentPane.add(getSaveCloseButtonJPanel(), BorderLayout.SOUTH);
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getTitleReportJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel(new MigLayout("wrap 2, fillx"));
            titleJLabel = new JLabel();
            titleJLabel.setText(title);

            jPanel.add(titleJLabel, "span 2");

            jPanel.add(getJTextArea(), "grow");
            jPanel.add(getCopyReportButton(), "shrink");
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSaveCloseButtonJPanel() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            GridBagConstraints gbc_jButtonSave = new GridBagConstraints();
            gbc_jButtonSave.insets = new Insets(0, 0, 0, 5);
            gbc_jButtonSave.gridx = 0;
            gbc_jButtonSave.gridy = 0;
            jPanel1.add(getJButtonSave(), gbc_jButtonSave);
            GridBagConstraints gbc_jButton = new GridBagConstraints();
            gbc_jButton.gridx = 1;
            gbc_jButton.gridy = 0;
            jPanel1.add(getOkButton(), gbc_jButton);
        }
        return jPanel1;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okJButton == null) {
            okJButton = new JButton();
            okJButton.setText("OK");
            okJButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisDialog.setVisible(false);
                }
            });
        }
        return okJButton;
    }

    private JButton getCopyReportButton() {
        if (copyReportButton == null) {
            copyReportButton = new JButton();
            copyReportButton.setText("Copy numbers");
            copyReportButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    StringSelection selection = new StringSelection(textToCopy == null ? jTextArea.getText() : textToCopy);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection,selection);
                }
            });
        }
        return copyReportButton;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable(model);
            // jTable.setModel(model);
        }
        return jTable;
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            jTextArea.setEditable(false);
            jTextArea.setEnabled(true);
        }
        return jTextArea;
    }

    /**
     * Save the report into a csv file
     */
    protected void serializeTableModel() {
        PrintWriter out = null;
        CSVPrinter writer = null;
        try {
            int cols = jTable.getModel().getColumnCount();
            CSVFormat csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
                    .withHeaderComments(jTextArea.getText());
            TableModel model = jTable.getModel();
            csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
                    .withCommentMarker('*')
                    .withHeaderComments(jTextArea.getText());
            ArrayList<String> titles = new ArrayList<String>();
            for (int i = 0; i < cols; i++) {
                titles.add(i, model.getColumnName(i));
            }

            csvFormat = csvFormat.withHeader(titles.toArray(new String[0]));

            log.debug("Text in text area {}", jTextArea.getText());
            log.debug("CSV Header comments {}", csvFormat.getHeaderComments());

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
            String time = dateFormat.format(now);
            String filename = "jobreport_" + time + ".csv";
            out = new PrintWriter(filename);

            writer = new CSVPrinter(out, csvFormat);
            writer.flush();

            int rows = jTable.getModel().getRowCount();
            for (int i = 0; i < rows; i++) {
                ArrayList<String> values = new ArrayList<String>();
                for (int col = 0; col < cols; col++) {
                    values.add((String) jTable.getModel().getValueAt(i, col));
                }

                writer.printRecord(values);
            }
            writer.flush();
            writer.close();
            JOptionPane.showMessageDialog(
                    Singleton.getSingletonInstance().getMainFrame(),
                    "Saved report to file: " + filename, "Report to CSV file",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            log.error("Error", e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                log.error("Error", e);
            }
            try {
                writer.close();
            } catch (Exception e) {
                log.error("Error", e);
            }
        }
    }

    private JButton getJButtonSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save Report");
            btnSave.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    serializeTableModel();
                }
            });
        }
        return btnSave;
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
