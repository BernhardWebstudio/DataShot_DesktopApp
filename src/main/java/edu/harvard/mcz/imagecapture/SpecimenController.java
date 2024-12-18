/**
 * SpecimenControler.java
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrame;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Control actions that can be taken on Specimen objects.
 */
public class SpecimenController {

    private static final Logger log =
            LoggerFactory.getLogger(SpecimenController.class);

    private Specimen specimen = null;
    private JTable table = null;
    private TableModel model =
            null; // model in which specimen can be found along with other specimens.
    private int currentRow = -1;     // row of this specimen in the table model.
    private int currentColumn = -1; // column of this specimen in the table model
    private boolean inTable = false; // true if model/row apply and specimen one
    // of list in a table model.
    private List<DataChangeListener> listeners;

    private ImageDisplayFrame resultFrame = null;

    public SpecimenController(Specimen aSpecimen) throws NoSuchRecordException {
        if (aSpecimen == null) {
            throw new NoSuchRecordException(
                    "Can't create a specimen controller with a null specimen");
        }
        specimen = aSpecimen;
        inTable = false;
    }

    /**
     * Create a specimen controller for a specimen in a table.  If this
     * constructor suceeds then isInTable() will return true.
     *
     * @param aSpecimen     the specimen
     * @param aModel        a SpecimenListTableModel containing the specimen
     * @param aTable        the table in which the SpecimenListTableModel is
     *                      shown.
     * @param theCurrentRow the row of the specimen in the view of the table.
     * @throws NoSuchRecordException
     */
    public SpecimenController(Specimen aSpecimen, SpecimenListTableModel aModel,
                              JTable aTable, int theCurrentRow, int theCurrentColumn)
            throws NoSuchRecordException {
        if (aSpecimen == null) {
            throw new NoSuchRecordException(
                    "Can't create a specimen controller with a null specimen");
        }
        log.debug(theCurrentRow + " " + aSpecimen);
        specimen = aSpecimen;
        if (aModel != null) {
            model = aModel;
            currentRow = theCurrentRow;
            currentColumn = theCurrentColumn;
            table = aTable;
            inTable = true;
        }
    }

    public SpecimenController(Long aSpecimenID) throws NoSuchRecordException {
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        specimen = sls.findById(aSpecimenID);
        if (specimen == null) {
            throw new NoSuchRecordException("No specimen found with SpecimenId = [" +
                    aSpecimenID.toString() + "]");
        }
        inTable = false;
    }

    public void setTargetFrame(ImageDisplayFrame targetFrame) {
        if (targetFrame != null) {
            resultFrame = targetFrame;
        }
    }

    public void displayInEditor(ImageDisplayFrame targetFrame) {
        if (targetFrame != null) {
            resultFrame = targetFrame;
        }
        displayInEditor();
    }

    /**
     * If the specimen for this controller is in a table model, switches out for
     * next specimen in model and displays result.
     *
     * @return true if specimen was changed, false if not, false if isInTable() is
     * false.
     */
    public boolean openNextSpecimenInTable() {
        // TODO: move cursor juggling to UI classes
        this.resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = this.switchToNextSpecimenInTable();
        this.displayInEditor();
        this.resultFrame.setCursor(
                Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return result;
    }

    /**
     * If the specimen for this controller is in a table model, switches out for
     * next specimen in model. If the specimen isn't in a table model, does
     * nothing.  If at last position in table model, does nothing.
     *
     * @return true if specimen was changed, false if not, false if isInTable() is
     * false.
     */
    public boolean switchToNextSpecimenInTable() {
        boolean result = false;
        if (inTable && model != null && currentRow > -1) {
            try {
                Specimen temp = (Specimen) model.getValueAt(
                        table.convertRowIndexToModel(currentRow + 1), 0);
                if (temp != null) {
                    specimen = (Specimen) model.getValueAt(
                            table.convertRowIndexToModel(currentRow + 1), 0);
                }
                currentRow = currentRow + 1;
                result = true;
            } catch (IndexOutOfBoundsException e) {
                log.debug("Debug {}", e);
            }
        }
        return result;
    }

    /**
     * If the specimen for this controller is in a table model, switches out for
     * previous specimen in model and displays the result.
     *
     * @return true if specimen was changed, false in not, false if isInTable() is
     * false.
     */
    public boolean openPreviousSpecimenInTable() {
        // TODO: move cursor juggling to UI classes
        this.resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = this.switchToPreviousSpecimenInTable();
        this.displayInEditor();
        this.resultFrame.setCursor(
                Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return result;
    }

    /**
     * If the specimen for this controller is in a table model, switches out for
     * previous specimen in model. If the specimen isn't in a table model, does
     * nothing.  If at first position in table model, does nothing.
     *
     * @return true if specimen was changed, false in not, false if isInTable() is
     * false.
     */
    public boolean switchToPreviousSpecimenInTable() {
        boolean result = false;
        if (inTable && model != null && currentRow > -1 && currentRow > 0) {
            try {
                Specimen temp = (Specimen) model.getValueAt(
                        table.convertRowIndexToModel(currentRow - 1), 0);
                if (temp != null) {
                    specimen = (Specimen) model.getValueAt(
                            table.convertRowIndexToModel(currentRow - 1), 0);
                }
                currentRow = currentRow - 1;
                result = true;
            } catch (IndexOutOfBoundsException e) {
                log.debug("Debug {}", e);
            }
        }
        return result;
    }

    /**
     * Is there a next specimen in the table model?
     *
     * @return true if there is a table model and the current position isn't the
     * last.
     */
    //    public boolean hasNextSpecimenInTable() {
    //        boolean result = false;
    //        if (inTable && model != null && currentRow > -1) {
    //            try {
    //                Specimen temp = (Specimen)
    //                model.getValueAt(table.convertRowIndexToModel(currentRow +
    //                1), 0); if (temp != null) {
    //                    result = true;
    //                }
    //            } catch (IndexOutOfBoundsException e) {
    //                log.debug("Debug {}", e);
    //            }
    //        }
    //        return result;
    //    }
    public boolean hasNextSpecimenInTable() {
        boolean result = false;
        if (inTable && model != null && currentRow > -1) {
            log.debug("Current row: " + currentRow +
                    " while row count: " + model.getRowCount());
            result = model.getRowCount() > (currentRow + 1);
        }
        return result;
    }

    /**
     * Is there a previous specimen in the table model?
     *
     * @return true if there is a table model and the current position isn't the
     * first.
     */
    //    public boolean hasPreviousSpecimenInTable() {
    //        boolean result = false;
    //        if (inTable && model != null && currentRow > 0) {
    //            try {
    //                Specimen temp = (Specimen)
    //                model.getValueAt(table.convertRowIndexToModel(currentRow -
    //                1), 0); if (temp != null) {
    //                    result = true;
    //                }
    //            } catch (IndexOutOfBoundsException e) {
    //                log.debug("Debug {}", e);
    //            }
    //        }
    //        return result;
    //    }
    public boolean hasPreviousSpecimenInTable() {
        boolean result = false;
        if (inTable && model != null && currentRow > 0) {
            result = currentRow > 0;
        }
        return result;
    }

    public boolean setSpecimen(Specimen aSpecimen) {
        if (aSpecimen != null) {
            specimen = aSpecimen;
            return true;
        }
        return false;
    }

    public boolean setSpecimen(Long aSpecimenID) throws NoSuchRecordException {
        boolean result = false;
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        specimen = sls.findById(aSpecimenID);
        if (specimen == null) {
            throw new NoSuchRecordException("No specimen found with SpecimenId = [" +
                    aSpecimenID.toString() + "]");
        } else {
            result = true;
        }
        return result;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    /**
     * Save the specimen.
     * Note that this method does not check permissions
     *
     * @return
     * @throws SaveFailedException
     */
    public void save() throws SaveFailedException {
        SpecimenLifeCycle s = new SpecimenLifeCycle();
        log.debug("in SpecimenControler.save: specimenId is " +
                specimen.getSpecimenId());
        log.debug("in SpecimenControler.save: specimen barcode is " +
                specimen.getBarcode());
        if (specimen.getSpecimenId() != null) {
            log.debug("before attachDirty");
            s.attachDirty(specimen);
            log.debug("after attachDirty");
        } else {
            try {
                s.persist(specimen);
            } catch (SpecimenExistsException e) {
                // convert special case used in preprocessing back to a save failed
                // exception.
                e.printStackTrace();
                throw new SaveFailedException(e.getMessage(), e);
            }
        }
        notifyListeners();
        // reload the specimen
        // Why??? Because we can. Also, to kinda show if more than one
        // user edited the same species
        Specimen updatedSpecimen = s.findById(specimen.getSpecimenId());
        if (updatedSpecimen != null) {
            specimen = updatedSpecimen;
        }

        updateModel();
    }

    public void updateModel() {
        if (inTable) {
            model.setValueAt(specimen, currentRow, currentColumn);
        }
    }

    public void save(Specimen copiedSpecimen) throws SaveFailedException {
        specimen = copiedSpecimen;
        SpecimenLifeCycle s = new SpecimenLifeCycle();
        if (specimen.getSpecimenId() != null) {
            s.attachDirty(specimen);
        } // else {
        try {
            s.persist(specimen);
        } catch (SpecimenExistsException e) {
            // convert special case used in preprocessing back to a save failed
            // exception.
            throw new SaveFailedException(e.getMessage(), e);
        }
        //}
        notifyListeners();
        // reload the specimen
        // Why???
        // specimen = s.findById(specimen.getSpecimenId());
        updateModel();
    }

    public void displayInEditor() {
        if (resultFrame == null) {
            resultFrame = new ImageDisplayFrame(specimen, this);
        } else {
            resultFrame.setTargetSpecimen(specimen);
        }
        SpecimenDetailsViewPane p = new SpecimenDetailsViewPane(specimen, this);

        resultFrame.setWest(p);

        // Add images of the specimen and labels to the details editor.
        // TODO: Add box and drawer level images
        String drawerNumber = null;
        if (specimen.getDrawerNumber() != null) {
            drawerNumber = specimen.getDrawerNumber();
        }
        List<ICImage> drawerImages = null;
        if (drawerNumber != null && !drawerNumber.trim().equals("")) {
            drawerImages = ICImageLifeCycle.findDrawerImages(drawerNumber);
            // TODO: check drawer number for trailing letter (e.g. 115.12a), then
            // check
            // for drawer (115.12) and unit tray (115.12a) level images.
        }
        // make visible before loading images as those take time
        resultFrame.pack();
        resultFrame.setVisible(true);

        if (drawerImages == null || drawerImages.isEmpty()) {
            // Specimen has multiple images, but no drawer images
            log.debug("Specimen with no drawer images: " + specimen.getBarcode());
            if (specimen.getICImages().size() == 0) {
                log.error("Specimen with no images: " + specimen.getBarcode());
            } else {
                resultFrame.loadImagesFromFiles(specimen.getICImages());
            }
        } else {
            Set<ICImage> images = specimen.getICImages();
            images.addAll(drawerImages);
            resultFrame.loadImagesFromFiles(images);
        }

        resultFrame.pack();
        resultFrame.centerSpecimen(); // Specimen is expected to be at the center of
        // the specimen part of the image.
    }

    /**
     * Determine if the specimen controled by this controller is part of a
     * list of specimens in a table model or not.
     *
     * @return true if specimen is part of a list in a table model.
     */
    public boolean isInTable() {
        return inTable;
    }

    public void addListener(DataChangeListener aListener) {
        if (listeners == null) {
            listeners = new ArrayList<DataChangeListener>();
        }
        log.debug("Added listener: " + aListener);
        listeners.add(aListener);
    }

    public void notifyListeners() {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).notifyDataHasChanged();
            }
        }
    }
}
