package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.utility.CastUtility;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MeliaStatisticsDialog extends JDialog {
    private static final Logger log =
            LoggerFactory.getLogger(MeliaStatisticsDialog.class);
    private JPanel jContentPanel;
    private JDatePicker endDateField;
    private JDatePicker startDateField;
    private JTextArea familiesTextField;
    private JTextArea resultsTextField;
    private JButton doAnalysisButton;

    public MeliaStatisticsDialog(Frame owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setTitle("Melia Statistics");
        this.setContentPane(getJContentPane());
        this.pack();
    }


    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPanel == null) {
            jContentPanel = new JPanel(new BorderLayout());
            JPanel jScrollPaneContent = new JPanel();
            jScrollPaneContent.setLayout(new MigLayout("wrap 2, fillx"));
            // add contents
            String[] labels = {
                    "Date from",
                    "Date to"
            };
            Component[] fields = {
                    this.getDateStartField(),
                    this.getDateEndField()
            };

            assert (fields.length == labels.length);
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel();
                label.setText(labels[i].concat(":"));
                jScrollPaneContent.add(label, "right"); //"align label");
                jScrollPaneContent.add(fields[i], "grow");
            }
            // more contents
            JLabel familiesLabel = new JLabel();
            familiesLabel.setText("Families (comma and/or new-line separated)");
            jScrollPaneContent.add(familiesLabel, "wrap");
            jScrollPaneContent.add(getFamiliesTextField(), "grow, span 2 4");
            jScrollPaneContent.add(getDoAnalysisButton(), "wrap");
            jScrollPaneContent.add(getResultsTextField(), "grow, span 2 4");

            // add the scroll pane content to the panel
            JScrollPane scrollPane = new JScrollPane(jScrollPaneContent);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            jContentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        return jContentPanel;
    }

    /**
     * @return
     */
    private JTextArea getFamiliesTextField() {
        if (familiesTextField == null) {
            familiesTextField = new JTextArea();
        }
        return familiesTextField;
    }

    /**
     * @return
     */
    private JDatePicker getDateEndField() {
        if (endDateField == null) {
            endDateField = new JDatePicker(new UtilDateModel());
        }
        return endDateField;
    }

    /**
     * @return
     */
    private JDatePicker getDateStartField() {
        if (startDateField == null) {
            startDateField = new JDatePicker(new UtilDateModel());
        }
        return startDateField;
    }

    /**
     * @return
     */
    private JTextArea getResultsTextField() {
        if (resultsTextField == null) {
            resultsTextField = new JTextArea();
            resultsTextField.setEnabled(false);
        }
        return resultsTextField;
    }

    /**
     * @return
     */
    private JButton getDoAnalysisButton() {
        if (doAnalysisButton == null) {
            doAnalysisButton = new JButton("Acquire Melia Statistics");
            doAnalysisButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    doAnalysis();
                }
            });
        }
        return doAnalysisButton;
    }

    /**
     * Read the fields and combine them into the queries.
     */
    private void doAnalysis() {
        Date startDate;
        Date endDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        try {
            startDate = (Date) this.getDateStartField().getModel().getValue();
            endDate = (Date) this.getDateEndField().getModel().getValue();
        } catch (Exception e) {
            getResultsTextField().setText("Failed to read entered dates. ".concat(e.getMessage()));
            return;
        }
        if (startDate == null || endDate == null) {
            getResultsTextField().setText("Please enter a start and end date.");
            return;
        }
        if (!startDate.before(endDate)) {
            getResultsTextField().setText("Please enter a start before the end date.");
            return;
        }
        String startDateFormatted = format.format(startDate);
        String endDateFormatted = format.format(endDate);
        String[] families = getFamiliesTextField().getText().split("\n|,");
        List familiesList = Arrays.asList(Arrays.stream(families).map(String::trim).toArray(String[]::new));
        log.debug("Doing Melia statistics with " + startDateFormatted + ", " + endDateFormatted + ", and " + String.join(", ", families));
        // finally, all right, prepare & run queries
        String results = "";
        Session session;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
        } catch (Exception e) {
            getResultsTextField().setText("Failed to start Database transaction");
            return;
        }
        try {

            //
            Query query1 = session.createQuery("SELECT count(DISTINCT i.imageId) FROM ICImage i LEFT JOIN i.specimen AS s WHERE i.path < :end AND i.path > :start AND s.family in (:families)");
            query1.setParameter("end", endDateFormatted);
            query1.setParameter("start", startDateFormatted);
            query1.setParameterList("families", familiesList);
            List resultsList = query1.getResultList();
            String results1 = CastUtility.castToString(query1.getSingleResult());
            results += "Nr. of ICImages: " + results1 + "\n";

            //
            Query query2 = session.createQuery("SELECT count(DISTINCT s.specimenId) FROM ICImage i LEFT JOIN i.specimen AS s WHERE i.path < :end AND i.path > :start AND s.family IN (:families)");
            query2.setParameter("end", endDateFormatted);
            query2.setParameter("start", startDateFormatted);
            query2.setParameterList("families", familiesList);
            String results2 = CastUtility.castToString(query2.getSingleResult());
            results += "Corresponding to #specimen: " + results2 + "\n";

            //
            Query query3 = session.createQuery("SELECT count(DISTINCT s.specimenId) FROM Tracking t LEFT JOIN t.specimen as s WHERE t.eventDateTime < :end AND t.eventDateTime > :start AND t.eventType LIKE :eventType AND s.family IN (:families)");
            query3.setParameter("end", endDate);
            query3.setParameter("start", startDate);
            query3.setParameterList("families", familiesList);
            query3.setParameter("eventType", "Text Entered");
            String results3 = CastUtility.castToString(query3.getSingleResult());
            results += "Text Entered in this date-range: " + results3 + "\n";

            session.getTransaction().commit();
        } catch (Exception e) {
            results += "Error running query: " + e.getMessage() + "\n";
            log.error("Failed to do Melia statistics: " + e.getMessage(), e);
            session.getTransaction().rollback();
        }


        getResultsTextField().setText(results);
    }
}
