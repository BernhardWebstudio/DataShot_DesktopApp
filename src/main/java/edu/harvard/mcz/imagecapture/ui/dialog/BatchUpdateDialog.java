package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.utility.CastUtility;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BatchUpdateDialog extends JDialog {
    private static final Logger log =
            LoggerFactory.getLogger(MeliaStatisticsDialog.class);
    private JPanel jContentPanel;
    private JTextField valueToJTextField;
    private JTextField valueFromJTextField;
    private JComboBox fieldSelectionJComboBox;
    private JButton applyChangeJButton;

    public BatchUpdateDialog(Frame owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setTitle("Batch Update Field");
        this.setContentPane(getJContentPane());
        this.pack();
    }

    private JPanel getJContentPane() {
        if (jContentPanel == null) {

            jContentPanel = new JPanel(new BorderLayout());
            JPanel jScrollPaneContent = new JPanel();
            jScrollPaneContent.setLayout(new MigLayout("wrap 2, fillx"));
            // add contents
            String[] labels = {
                    "Change Field",
                    "Where value is",
                    "To value"
            };
            Component[] fields = {
                    this.getFieldSelectionJComboBox(),
                    this.getValueFromJTextField(),
                    this.getValueToJTextField()
            };

            assert (fields.length == labels.length);
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel();
                label.setText(labels[i].concat(":"));
                jScrollPaneContent.add(label, "right"); //"align label");
                jScrollPaneContent.add(fields[i], "grow");
            }

            jScrollPaneContent.add(getDoChangeButton(), "wrap");

            // add the scroll pane content to the panel
            JScrollPane scrollPane = new JScrollPane(jScrollPaneContent);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            jContentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        return jContentPanel;
    }

    private JButton getDoChangeButton() {
        if (applyChangeJButton == null) {
            applyChangeJButton = new JButton("Apply Change");

            applyChangeJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    doApplyChange();
                }
            });
        }
        return applyChangeJButton;
    }

    private JTextField getValueToJTextField() {
        if (valueToJTextField == null) {
            valueToJTextField = new JTextField();
        }
        return valueToJTextField;
    }

    private JTextField getValueFromJTextField() {
        if (valueFromJTextField == null) {
            valueFromJTextField = new JTextField();
        }
        return valueFromJTextField;
    }

    private JComboBox getFieldSelectionJComboBox() {
        if (fieldSelectionJComboBox == null) {
            fieldSelectionJComboBox = new JComboBox();
            for (UpdateableField field : UpdateableField.values()) {
                fieldSelectionJComboBox.addItem(field);
            }
        }
        return fieldSelectionJComboBox;
    }

    private void doApplyChange() {
        String fieldName = ((UpdateableField) this.getFieldSelectionJComboBox().getSelectedItem()).getDatabaseDescription();
        String conditionQuery = "WHERE " + fieldName + " LIKE :fromValue";

        Session session;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to start Database transaction: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // count how many specimen would be influenced
            Query selectQuery = session.createQuery("SELECT count(DISTINCT s.specimenId) FROM Specimen s LEFT JOIN s.collectors AS c " + conditionQuery);
            selectQuery.setParameter("fromValue", getValueFromJTextField().getText());
            String nrOfChanges = CastUtility.castToString(selectQuery.getSingleResult());
            // ask for confirmation
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, nrOfChanges + " Specimen would be affected. Continue?", "Warning", dialogButton);
            if (dialogResult == JOptionPane.NO_OPTION) {
                log.debug("Will not do batch update. Confirmation denied.");
                return;
            }
        } catch (Exception e) {
            log.error("Failed to do query update: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Failed to query: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            session.getTransaction().rollback();
            return;
        }

        try {
            Query updateQuery;
            // do the change
            if (fieldName.startsWith("s.")) {
                updateQuery = session.createQuery("UPDATE Specimen s SET " + fieldName + "= :toValue " + conditionQuery);
            } else if (fieldName.startsWith("c.")) {
                updateQuery = session.createQuery("UPDATE Collector c SET " + fieldName + "= :toValue " + conditionQuery);
            } else {
                log.error("Unhandled updateable field prefix");
                return;
            }
            updateQuery.setParameter("fromValue", getValueFromJTextField().getText());
            updateQuery.setParameter("toValue", getValueToJTextField().getText());

            int numAffected = updateQuery.executeUpdate();
            session.getTransaction().commit();
            JOptionPane.showMessageDialog(this, "Changed " + String.valueOf(numAffected) + " values", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            log.error("Failed to do query update: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Failed to do update: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            session.getTransaction().rollback();
            return;
        }
    }

    protected enum UpdateableField {
        COUNTRY("Country", "s.country"),
        PROVINCE("Province (Primary Division)", "s.primaryDivison"),
        SPECIFIC_LOCALITY("Specific locality", "s.specificLocality"),
        VERBATIM_LOCALITY("Verbatim locality", "s.verbatimLocality"),
        COLLECTOR("Collector's Name", "c.collectorName"),
        COLLECTION("Collection", "s.collection");

        private final String name;
        private final String dbDescription;

        UpdateableField(String name, String dbDescription) {
            this.name = name;
            this.dbDescription = dbDescription;
        }

        public String getName() {
            return this.name;
        }

        public String getDatabaseDescription() {
            return this.dbDescription;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
