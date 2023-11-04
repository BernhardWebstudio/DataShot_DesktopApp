package edu.harvard.mcz.imagecapture.ui.dialog;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.interfaces.GuiProviderForRunnable;
import edu.harvard.mcz.imagecapture.interfaces.ProgressListener;
import edu.harvard.mcz.imagecapture.jobs.NahimaExportJob;
import edu.harvard.mcz.imagecapture.utility.FileUtility;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NahimaDrawerExportDialog extends JDialog implements ProgressListener, GuiProviderForRunnable {
    private static final Logger log =
            LoggerFactory.getLogger(NahimaDrawerExportDialog.class);
    private JPanel jContentPanel;
    private JProgressBar progressBar;
    private JTextArea resultsTextField;
    private JButton closeButton;
    private NahimaExportJob job;
    private String staticStatusText;
    private ArrayList<Map<String, String>> csvContent;

    public NahimaDrawerExportDialog(Frame owner) throws CsvValidationException, IOException {
        super(owner);
        initialize();
    }

    private void initialize() throws CsvValidationException, IOException {
        this.setPreferredSize(new Dimension(450, 225));
        this.setTitle("Nahima Export");
        this.staticStatusText = "Please do not close this window until the export is done.";
        this.setContentPane(getJContentPane());
        this.setMinimumSize(new Dimension(350, 100));
        this.setSize(new Dimension(550, 225));
        this.pack();
        job = new NahimaExportJob();
        // TODO: loop images in folder, create Specimen for them based on the Excel file.
        File folder = this.askForFolder();
        if (folder == null) {
            return;
        }
        File csvFile = FileUtility.askForCSVFile(null);
        if (csvFile == null) {
            return;
        }
        this.readCSVFile(csvFile);

        ArrayList<Specimen> specimenToExport = this.translateFiles(folder);
        job.setSpecimenToExport(specimenToExport);
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
        // TODO: something like https://stackoverflow.com/questions/20269083/make-a-swing-thread-that-show-a-please-wait-jdialog
//        SwingUtilities.invokeLater(job);
    }

    private void readCSVFile(File csvFile) throws IOException, CsvValidationException {
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvFile));
        this.csvContent = new ArrayList<>();
        int numNull = 0;
        while (numNull < 3) {
            Map<String, String> values = reader.readMap();
            if (values != null) {
                this.csvContent.add(values);
            } else {
                numNull += 1;
            }
        }
    }

    private File askForFolder() {
        // launch a file chooser dialog to select the directory to scan
        File searchStartPoint = null;
        File startPoint = null;
        if (Singleton.getSingletonInstance()
                .getProperties()
                .getProperties()
                .getProperty(ImageCaptureProperties.KEY_LASTPATH) != null) {
            searchStartPoint =
                    new File(Singleton.getSingletonInstance()
                            .getProperties()
                            .getProperties()
                            .getProperty(ImageCaptureProperties.KEY_LASTPATH));
        }
        startPoint = FileUtility.askForDirectory(searchStartPoint);
        if (startPoint == null) {
            JOptionPane.showMessageDialog(
                    Singleton.getSingletonInstance().getMainFrame(),
                    "Can't scan without a directory.", "Error: No scanning possible.",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return startPoint;
    }

    private ArrayList<Specimen> translateFiles(File folder) {
        ArrayList<Specimen> specimens = new ArrayList<>();

        if (!folder.canRead()) {
            JOptionPane.showMessageDialog(
                    Singleton.getSingletonInstance().getMainFrame(),
                    "Can't start scan.  Unable to read selected directory: " +
                            folder.getPath(),
                    "Can't Scan.", JOptionPane.ERROR_MESSAGE);
            return specimens;
        }
        File[] containedFiles = folder.listFiles();
        if (containedFiles != null) {
            for (File fileToCheck : containedFiles) {
                if (!fileToCheck.isDirectory() && !fileToCheck.isHidden()) {
                    specimens.add(this.translateFile(fileToCheck));
                }
            }
        }
        return specimens;
    }

    private Specimen translateFile(File file) {
        Specimen specimen = new Specimen();
        specimen.setHigherOrder("Hymenoptera");
        specimen.setCollection("von Schulthess-Rechberg, Anton Dr. (1855-1941)");

        // figure out the drawer
        Pattern pattern = Pattern.compile("Schulthess_0*(\\d+)\\..*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(file.getName());
        if (matcher.find()) {
            int drawerNumber = Integer.parseInt(matcher.group(1));
            // if drawer found, can create the data based on the CSV file
            specimen.setBarcode("Schulthess Drawer " + matcher.group(1));
            List<Map<String, String>> relevantCsvRows = this.csvContent.stream().filter(row -> Integer.parseInt(row.get("Drawer")) == drawerNumber).collect(Collectors.toList());
            HashSet<String> relevantTags = new HashSet<>();
            //
            String[] firstOrderCols = new String[]{
                    "Suborder", "Family", "Subfamily", "Tribe",
            };
            for (String col : firstOrderCols) {
                relevantCsvRows.forEach(row -> {
                    String value = row.get(col);
                    if (value != null && value.length() > 0) {
                        relevantTags.add(value);
                    }
                });
            }

            String[] relevantCols = new String[]{
                    "Genus", "Species", "subspecies", "Infra. Name"
            };
            relevantCsvRows.forEach(row -> {
                for (String relevantCol : relevantCols) {
                    String value = row.get(relevantCol);
                    if (value != null && value.length() > 0) {
                        relevantTags.add(value);
                    }
                }
            });
            // add all this identifying data as a number
            Number number1 = new Number();
            number1.setTemporaryComment(String.join(", ", relevantTags));
            number1.setNumber(specimen.getBarcode());
            number1.setSpecimen(specimen);
            Number number2 = new Number();
            number2.setNumberType("Schulthess Drawer");
            number2.setNumber(matcher.group(1));
            number2.setSpecimen(specimen);
//            Number number3 = new Number();
//            number3.setNumberType("Test-Number-Type");
//            number3.setNumber(matcher.group(1));
//            number3.setSpecimen(specimen);
            specimen.setNumbers(new HashSet<>() {{
                add(number1);
                add(number2);
//                add(number3);
            }});
        }

        // create actual image file entry
        ICImage image = new ICImage(specimen);
        image.setPath(file.getAbsolutePath());
        image.setFilename(file.getName());
        image.setRawQRCode("");
        image.setRawBarcode("");
        specimen.setICImages(new HashSet<>() {{
            add(image);
        }});

        return specimen;
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
            resultsTextField.setText(this.staticStatusText);
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

    @Override
    public void currentWorkStatusChanged(String status) {
        resultsTextField.setText(this.staticStatusText + "\n" + status);
    }

    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton("Close");
            closeButton.setEnabled(false);

            closeButton.addActionListener(actionEvent -> this.setVisible(false));
        }
        return closeButton;
    }

    @Override
    public void invokeOnUIThread(Runnable runnable) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeLater(runnable);
    }
}
