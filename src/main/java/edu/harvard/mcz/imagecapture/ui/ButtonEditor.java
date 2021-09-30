/**
 * ButtonEditor.java
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
package edu.harvard.mcz.imagecapture.ui;

import edu.harvard.mcz.imagecapture.*;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount;
import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordException;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialog;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * ButtonEditor A clickable button in a table cell that brings up a details view
 * for the row containing the button. Works with ButtonRenderer and a TableModel
 * that implements isCellEditable(). <p> Note: This implementation is specific
 * to a Specimen identified by its id. <p> Example Usage (assuming that only the
 * ID column is of type Long): <pre> SpecimenLifeCycle s = new
 * SpecimenLifeCycle(); jTable.setModel(new
 * SpecimenListTableModel(s.findAll())); jTable.setDefaultRenderer(Long.class,
 * new ButtonRenderer()); jTable.setDefaultEditor(Long.class, new
 * ButtonEditor());
 * </pre>
 *
 * @see SpecimenListTableModel#isCellEditable(int, int)
 * @see SpecimenListTableModel#getColumnClass(int)
 * @see edu.harvard.mcz.imagecapture.ui.ButtonRenderer
 */
public class ButtonEditor
        extends AbstractCellEditor implements TableCellEditor, ActionListener {

    // Note: OPEN_ and ACTION_ can't overlap.
    public static final int OPEN_SPECIMEN_DETAILS = 0;
    public static final int OPEN_TEMPLATE = 1;
    public static final int OPEN_USER = 2;
    public static final int ACTION_CANCEL_JOB = 3;
    public static final int OPEN_SPECIMENPARTATTRIBUTES = 4;
    public static final int OPEN_SPECIMEN_VERBATIM = 5;
    public static final int OPEN_VERBATIM_CLASSIFY = 6;
    protected static final String EVENT_PRESSED = "event_button_pressed";
    private static final long serialVersionUID = -2999614267588538934L;
    private static final Logger log = LoggerFactory.getLogger(ButtonEditor.class);
    private final JButton button;   // the button to display
    private final Component parentComponent;
    private Object targetId = null; // value for the cell (primary key value for
    // tuple displayed in row).
    private int formToOpen = OPEN_SPECIMEN_DETAILS;

    /**
     * Default constructor,  creates a button for the cell,  Expects to be in a
     * list of specimens bound to a column containing specimenIds as the values.
     */
    public ButtonEditor() {
        button = new JButton();
        button.setText("Edit");
        button.setMaximumSize(new Dimension(50, 30));
        button.setActionCommand(EVENT_PRESSED);
        parentComponent = null;
        button.addActionListener(this);
    }

    /**
     * Constructor to show a record of a type specified by aFormToOpen, use
     * the OPEN_ constants to specify what context this button is operating in,
     * or the action to take on pressing the button, using the ACTION_constants.
     *
     * @param aFormToOpen      the form within which to display the record
     *                         identified
     *                         by the value of the cell for which this button is
     *                         the CellEditor the available actions to be taken on a button press are
     *                         specified by the OPEN_ constants, or more generally the action to take on
     *                         pressing the button, specified by the ACTION_ constants.
     * @param aParentComponent the component within which the table is embedded.
     */
    public ButtonEditor(int aFormToOpen, Component aParentComponent) {
        button = new JButton();
        if (aFormToOpen == ACTION_CANCEL_JOB) {
            button.setText("Cancel");
        } else if (aFormToOpen == OPEN_SPECIMEN_VERBATIM) {
            button.setText("Transcribe");
        } else {
            button.setText("Edit");
        }
        button.setMaximumSize(new Dimension(50, 30));
        formToOpen = aFormToOpen;
        parentComponent = aParentComponent;
        button.setActionCommand(EVENT_PRESSED);
        button.addActionListener(this);
    }

    /* (non-Javadoc)
     * @see
     *     javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
     *     java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row,
                                                 int column) {
        button.setEnabled(true);
        targetId = value;
        return button;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return targetId;
    }

    /* (non-Javadoc)
     * @see
     *     java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Action might not be event_button_pressed on all systems.
        log.debug("Button event actionCommand: " + e.getActionCommand());
        if (e.getActionCommand().equals(EVENT_PRESSED)) {
            // Event is a click on the cell
            // Identify the row that was clicked on.
            JTable table = (JTable) ((JButton) e.getSource()).getParent();
            log.debug("Debug {}", e.getSource());
            log.debug("Debug {}", table);
            int row = table.getEditingRow();
            // Stop editing - note, we need to have gotten e.getSource.getParent and
            // getEditingRow first.
            fireEditingStopped(); // Make the renderer reappear.
            Singleton.getSingletonInstance().getMainFrame().setCursor(
                    Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (formToOpen) {
                case OPEN_SPECIMEN_DETAILS:
                    // Load the selected specimen record from its ID (the data value behind
                    // the button).
                    // SpecimenLifeCycle sls = new SpecimenLifeCycle();
                    // Specimen specimen = sls.findById((Long)targetId);
                    // if (specimen!=null) {
                    if (targetId != null) {
                        // a specimen with this ID exists, bring up the details editor.
                        try {
                            // SpecimenControler sc = new SpecimenControler(specimen);
                            if (((Specimen) targetId).getSpecimenId() != null) {
                                // Specimen is still editable
                                // Pass the specimen object for the row, the table model, and
                                // the row number on to the specimen controler.
                                try {
                                    SpecimenController sc = new SpecimenController(
                                            (Specimen) targetId,
                                            (SpecimenListTableModel) table.getModel(), table, row);
                                    if (table.getParent()
                                            .getParent()
                                            .getParent()
                                            .getParent()
                                            .getClass() == SpecimenBrowser.class) {
                                        sc.addListener((DataChangeListener) table.getParent());
                                    } else {
                                        Component x = table;
                                        boolean done = false;
                                        while (!done) {
                                            log.debug("Debug {}", x.getParent());
                                            x = x.getParent();
                                            if (x.getClass() == SpecimenBrowser.class) {
                                                sc.addListener((DataChangeListener) x);
                                                done = true;
                                            }
                                        }
                                    }
                                    sc.displayInEditor();
                                } catch (ClassCastException eNotSp) {
                                    // Request isn't coming from a SpecimenListTableModel
                                    // View just the specimen record.
                                    SpecimenController sc =
                                            new SpecimenController((Specimen) targetId);
                                    sc.displayInEditor();
                                }
                            } else {
                                log.debug(
                                        "User clicked on table row containing a new Specimen()");
                                JOptionPane.showMessageDialog(
                                        Singleton.getSingletonInstance().getMainFrame(),
                                        "No Specimen for this image", "Load Specimen Failed",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (NoSuchRecordException e1) {
                            log.error(
                                    "Tested for specimen!=null, but SpecimenControler threw null specimen exception");
                            log.error("Error", e1);
                        }
                    } else {
                        log.debug("No matches found to specimen id=" + targetId);
                        // TODO: Create new specimen record and bring up dialog
                        JOptionPane.showMessageDialog(
                                Singleton.getSingletonInstance().getMainFrame(),
                                "No specimen record.");
                    }
                    break;
                case OPEN_TEMPLATE:
                    // Load the selected specimen record from its ID (the data value behind
                    // the button).
                    try {
                        // a template with this targetID exists, display it.
                        ((PositionTemplateEditor) parentComponent)
                                .setTemplate((String) targetId);
                    } catch (NoSuchTemplateException e1) {
                        log.error("No such template on button press on a template in list.",
                                e1);
                    }

                    break;
                case OPEN_USER:
                    // TODO: tie to user
                    log.debug("Open user");
                    ((UserListBrowser) parentComponent)
                            .getEditUserPanel()
                            .setUser((Users) targetId);
                    break;
                case OPEN_SPECIMEN_VERBATIM:
                    log.debug("Open Verbatim Transcription");
                    SpecimenLifeCycle sls = new SpecimenLifeCycle();
                    List<Specimen> toTranscribe = sls.findForVerbatim(
                            ((GenusSpeciesCount) targetId).getGenus(),
                            ((GenusSpeciesCount) targetId).getSpecificEpithet(),
                            WorkFlowStatus.STAGE_1);
                    log.debug("Debug {}", toTranscribe.size());
                    SpecimenListTableModel stm = new SpecimenListTableModel(toTranscribe);
                    JTable stable = new JTable();
                    stable.setModel(stm);
                    SpecimenController verbCont;
                    try {
                        verbCont =
                                new SpecimenController(toTranscribe.get(0), stm, stable, 0);
                        VerbatimCaptureDialog dialog =
                                new VerbatimCaptureDialog(toTranscribe.get(0), verbCont);
                        dialog.setVisible(true);
                    } catch (NoSuchRecordException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                case OPEN_VERBATIM_CLASSIFY:
                    log.debug("Open Verbatim Classify dialog");
                    try {
                        VerbatimClassifyDialog dialog = new VerbatimClassifyDialog(
                                (VerbatimCount) table.getModel().getValueAt(row, 0));
                        dialog.setVisible(true);
                    } catch (ClassCastException e1) {
                        log.error(e1.getMessage(), e1);
                    }
                    break;
                case ACTION_CANCEL_JOB:
                    log.debug("Action Cancel requested on job " + targetId);
                    Singleton.getSingletonInstance()
                            .getJobList()
                            .getJobAt((Integer) targetId)
                            .cancel();
                    break;
                case OPEN_SPECIMENPARTATTRIBUTES:
                    SpecimenPartAttributeDialog attrDialog =
                            new SpecimenPartAttributeDialog((SpecimenPart) targetId,
                                    parentComponent);
                    attrDialog.setVisible(true);
                    break;
            }
            Singleton.getSingletonInstance().getMainFrame().setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            System.gc();
        }
    }
}
