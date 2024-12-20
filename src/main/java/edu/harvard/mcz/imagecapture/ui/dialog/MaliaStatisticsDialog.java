package edu.harvard.mcz.imagecapture.ui.dialog;

import com.github.lgooddatepicker.components.DatePicker;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle;
import edu.harvard.mcz.imagecapture.utility.CastUtility;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
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

public class MaliaStatisticsDialog extends JDialog {
    private static final Logger log =
            LoggerFactory.getLogger(MaliaStatisticsDialog.class);
    private JPanel jContentPanel;
    private DatePicker endDateField;
    private DatePicker startDateField;
    private JTextArea familiesTextField;
    private JTextArea resultsTextField;
    private JButton doAnalysisButton;
    private JComboBox orderField;


    public MaliaStatisticsDialog(Frame owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setTitle("MALIA Statistics");
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
                    "Date to",
                    "Order"
            };
            Component[] fields = {
                    this.getDateStartField(),
                    this.getDateEndField(),
                    this.getOrderField()
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

    private Component getOrderField() {
        if (orderField == null) {
            orderField = new JComboBox<String>();
            orderField.setModel(new DefaultComboBoxModel<String>(
                    HigherTaxonLifeCycle.selectDistinctOrder()));
            orderField.setEditable(true);
            // jComboBoxHigherOrder.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class,
            // "Order", jComboBoxHigherOrder));
            orderField.setToolTipText(
                    MetadataRetriever.getFieldHelp(Specimen.class, "HigherOrder"));
            AutoCompleteDecorator.decorate(orderField);
        }
        return orderField;
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
    private DatePicker getDateEndField() {
        if (endDateField == null) {
            endDateField = new DatePicker();
        }
        return endDateField;
    }

    /**
     * @return
     */
    private DatePicker getDateStartField() {
        if (startDateField == null) {
            startDateField = new DatePicker();
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
            doAnalysisButton = new JButton("Acquire MALIA Statistics");
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
            startDate = (Date) this.getDateStartField().convert().getDateWithDefaultZone();
            endDate = (Date) this.getDateEndField().convert().getDateWithDefaultZone();
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
            Query query1 = session.createQuery("SELECT count(DISTINCT i.imageId) FROM ICImage i LEFT JOIN i.specimen AS s WHERE i.path < :end AND i.path > :start AND (s.family in (:families) OR s.higherOrder = :higherOrder)");
            query1.setParameter("end", endDateFormatted);
            query1.setParameter("start", startDateFormatted);
            query1.setParameterList("families", familiesList);
            query1.setParameter("higherOrder", orderField.getSelectedItem().toString());
            List resultsList = query1.getResultList();
            String results1 = CastUtility.castToString(query1.getSingleResult());
            results += results1 + " images with matching family or higher order and path between " + startDateFormatted + " and " + endDateFormatted + "\n";

            //
            Query query2 = session.createQuery("SELECT count(DISTINCT s.specimenId) FROM ICImage i LEFT JOIN i.specimen AS s WHERE i.path < :end AND i.path > :start AND (s.family in (:families) OR s.higherOrder = :higherOrder)");
            query2.setParameter("end", endDateFormatted);
            query2.setParameter("start", startDateFormatted);
            query2.setParameterList("families", familiesList);
            query2.setParameter("higherOrder", orderField.getSelectedItem().toString());
            String results2 = CastUtility.castToString(query2.getSingleResult());
            results += "corresponding to " + results2 + "specimen.\n";

            //
            Query query3 = session.createQuery("SELECT count(DISTINCT s.specimenId) FROM Tracking t LEFT JOIN t.specimen as s WHERE t.eventDateTime < :end AND t.eventDateTime > :start AND t.eventType LIKE :eventType AND (s.family in (:families) OR s.higherOrder = :higherOrder)");
            query3.setParameter("end", endDate);
            query3.setParameter("start", startDate);
            query3.setParameterList("families", familiesList);
            query3.setParameter("eventType", "Text Entered");
            query3.setParameter("higherOrder", orderField.getSelectedItem().toString());
            String results3 = CastUtility.castToString(query3.getSingleResult());
            results += "In this date-range for these families/order, for " + results3 + " specimen was a text entered event tracked.\n";

            session.getTransaction().commit();
        } catch (Exception e) {
            results += "Error running query: " + e.getMessage() + "\n";
            log.error("Failed to do Melia statistics: " + e.getMessage(), e);
            session.getTransaction().rollback();
        }


        getResultsTextField().setText(results);
    }
}
