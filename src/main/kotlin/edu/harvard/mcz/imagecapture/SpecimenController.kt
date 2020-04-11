/**
 * SpecimenControler.java
 * edu.harvard.mcz.imagecapture
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 */
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.SpecimenController
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrame
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel
import org.apache.commons.logging.LogFactory
import java.awt.Cursor
import java.util.*
import javax.swing.JTable
import javax.swing.table.TableModel

/**
 * Control actions that can be taken on Specimen objects.
 */
class SpecimenController {
    private var specimen: Specimen? = null
    private var table: JTable? = null
    private var model: TableModel? = null // model in which specimen can be found along with other specimens.
    private var currentRow = -1 // row of this specimen in the table model.
    /**
     * Determine if the specimen controled by this controller is part of a
     * list of specimens in a table model or not.
     *
     * @return true if specimen is part of a list in a table model.
     */
    var isInTable = false // true if model/row apply and specimen one of list in a table model.
        private set
    private var listeners: MutableList<DataChangeListener?>? = null
    private var resultFrame: ImageDisplayFrame? = null

    constructor(aSpecimen: Specimen?) {
        if (aSpecimen == null) {
            throw NoSuchRecordException("Can't create a specimen controller with a null specimen")
        }
        specimen = aSpecimen
        currentRow = -1
        isInTable = false
    }

    /**
     * Create a specimen controller for a specimen in a table.  If this constructor suceeds then isInTable()
     * will return true.
     *
     * @param aSpecimen     the specimen
     * @param aModel        a SpecimenListTableModel containing the specimen
     * @param aTable        the table in which the SpecimenListTableModel is shown.
     * @param theCurrentRow the row of the specimen in the view of the table.
     * @throws NoSuchRecordException
     */
    constructor(aSpecimen: Specimen?, aModel: SpecimenListTableModel?, aTable: JTable?, theCurrentRow: Int) {
        if (aSpecimen == null) {
            throw NoSuchRecordException("Can't create a specimen controller with a null specimen")
        }
        log!!.debug("$theCurrentRow $aSpecimen")
        specimen = aSpecimen
        if (aModel != null) {
            model = aModel
            currentRow = theCurrentRow
            table = aTable
            isInTable = true
        }
    }

    constructor(aSpecimenID: Long) {
        val sls = SpecimenLifeCycle()
        specimen = sls.findById(aSpecimenID)
        if (specimen == null) {
            throw NoSuchRecordException("No specimen found with SpecimenId = [$aSpecimenID]")
        }
        currentRow = -1
        isInTable = false
    }

    fun setTargetFrame(targetFrame: ImageDisplayFrame?) {
        if (targetFrame != null) {
            resultFrame = targetFrame
        }
    }

    fun displayInEditor(targetFrame: ImageDisplayFrame?) {
        if (targetFrame != null) {
            resultFrame = targetFrame
        }
        displayInEditor()
    }

    /**
     * If the specimen for this controller is in a table model, switches out for next specimen in model and displays result.
     *
     * @return true if specimen was changed, false if not, false if isInTable() is false.
     */
    fun openNextSpecimenInTable(): Boolean { // TODO: move cursor juggling to UI classes
        resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        val result = switchToNextSpecimenInTable()
        this.displayInEditor()
        resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        return result
    }

    /**
     * If the specimen for this controller is in a table model, switches out for next specimen in model.
     * If the specimen isn't in a table model, does nothing.  If at last position in table model, does nothing.
     *
     * @return true if specimen was changed, false if not, false if isInTable() is false.
     */
    fun switchToNextSpecimenInTable(): Boolean {
        var result = false
        if (isInTable && model != null && currentRow > -1) {
            try {
                val temp: Specimen = model.getValueAt(table.convertRowIndexToModel(currentRow + 1), 0) as Specimen
                if (temp != null) {
                    specimen = model.getValueAt(table.convertRowIndexToModel(currentRow + 1), 0) as Specimen
                }
                currentRow = currentRow + 1
                result = true
            } catch (e: IndexOutOfBoundsException) {
                log!!.debug(e)
            }
        }
        return result
    }

    /**
     * If the specimen for this controller is in a table model, switches out for previous specimen in model and displays the result.
     *
     * @return true if specimen was changed, false in not, false if isInTable() is false.
     */
    fun openPreviousSpecimenInTable(): Boolean { // TODO: move cursor juggling to UI classes
        resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        val result = switchToPreviousSpecimenInTable()
        this.displayInEditor()
        resultFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        return result
    }

    /**
     * If the specimen for this controller is in a table model, switches out for previous specimen in model.
     * If the specimen isn't in a table model, does nothing.  If at first position in table model, does nothing.
     *
     * @return true if specimen was changed, false in not, false if isInTable() is false.
     */
    fun switchToPreviousSpecimenInTable(): Boolean {
        var result = false
        if (isInTable && model != null && currentRow > -1 && currentRow > 0) {
            try {
                val temp: Specimen = model.getValueAt(table.convertRowIndexToModel(currentRow - 1), 0) as Specimen
                if (temp != null) {
                    specimen = model.getValueAt(table.convertRowIndexToModel(currentRow - 1), 0) as Specimen
                }
                currentRow = currentRow - 1
                result = true
            } catch (e: IndexOutOfBoundsException) {
                log!!.debug(e)
            }
        }
        return result
    }

    /**
     * Is there a next specimen in the table model?
     *
     * @return true if there is a table model and the current position isn't the last.
     */
    fun hasNextSpecimenInTable(): Boolean {
        var result = false
        if (isInTable && model != null && currentRow > -1) {
            try {
                val temp: Specimen = model.getValueAt(table.convertRowIndexToModel(currentRow + 1), 0) as Specimen
                if (temp != null) {
                    result = true
                }
            } catch (e: IndexOutOfBoundsException) {
                log!!.debug(e)
            }
        }
        return result
    }
    //		public boolean hasNextSpecimenInTable() {
//			boolean result = false;
//			if (inTable && model!=null && currentRow > -1) {
//				result = model.RowCount > currentRow;
//			}
//			return result;
//		}
    /**
     * Is there a previous specimen in the table model?
     *
     * @return true if there is a table model and the current position isn't the first.
     */
    fun hasPreviousSpecimenInTable(): Boolean {
        var result = false
        if (isInTable && model != null && currentRow > -1 && currentRow > 0) {
            try {
                val temp: Specimen = model.getValueAt(table.convertRowIndexToModel(currentRow - 1), 0) as Specimen
                if (temp != null) {
                    result = true
                }
            } catch (e: IndexOutOfBoundsException) {
                log!!.debug(e)
            }
        }
        return result
    }

    fun setSpecimen(aSpecimen: Specimen?): Boolean {
        if (aSpecimen != null) {
            specimen = aSpecimen
            return true
        }
        return false
    }

    @Throws(NoSuchRecordException::class)
    fun setSpecimen(aSpecimenID: Long): Boolean {
        var result = false
        val sls = SpecimenLifeCycle()
        specimen = sls.findById(aSpecimenID)
        result = if (specimen == null) {
            throw NoSuchRecordException("No specimen found with SpecimenId = [$aSpecimenID]")
        } else {
            true
        }
        return result
    }

    fun getSpecimen(): Specimen? {
        return specimen
    }

    @Throws(SaveFailedException::class)
    fun save(): Boolean {
        val result = false
        val s = SpecimenLifeCycle()
        log!!.debug("in SpecimenControler.save: specimenId is " + specimen.SpecimenId)
        log.debug("in SpecimenControler.save: specimen barcode is " + specimen.Barcode)
        if (specimen.SpecimenId != null) {
            log.debug("before attachDirty")
            s.attachDirty(specimen)
            log.debug("after attachDirty")
        } else {
            try {
                s.persist(specimen)
            } catch (e: SpecimenExistsException) { // convert special case used in preprocessing back to a save failed exception.
                e.printStackTrace()
                throw SaveFailedException(e.message, e)
            }
        }
        notifyListeners()
        // reload the specimen
// Why??? Because we can. Also, to kinda show if more than one
// user edited the same species
        val updatedSpecimen: Specimen = s.findById(specimen.SpecimenId)
        if (updatedSpecimen != null) {
            specimen = updatedSpecimen
        }
        return result
    }

    @Throws(SaveFailedException::class)
    fun save(copiedSpecimen: Specimen?): Boolean {
        specimen = copiedSpecimen
        val result = false
        val s = SpecimenLifeCycle()
        if (specimen.SpecimenId != null) {
            s.attachDirty(specimen)
        } //else {
        try {
            s.persist(specimen)
        } catch (e: SpecimenExistsException) { // convert special case used in preprocessing back to a save failed exception.
            throw SaveFailedException(e.message, e)
        }
        //}
        notifyListeners()
        // reload the specimen
// Why???
//specimen = s.findById(specimen.SpecimenId);
        return result
    }

    fun displayInEditor() {
        if (resultFrame == null) {
            resultFrame = ImageDisplayFrame(specimen, this)
        } else {
            resultFrame.setTargetSpecimen(specimen)
        }
        val p = SpecimenDetailsViewPane(specimen, this)
        resultFrame.setWest(p)
        // Add images of the specimen and labels to the details editor.
// TODO: Add box and drawer level images
        var drawerNumber: String? = null
        if (specimen.DrawerNumber != null) {
            drawerNumber = specimen.DrawerNumber
        }
        var drawerImages: MutableList<ICImage?>? = null
        if (drawerNumber != null && drawerNumber.trim { it <= ' ' } != "") {
            drawerImages = ICImageLifeCycle.Companion.findDrawerImages(drawerNumber)
            //TODO: check drawer number for trailing letter (e.g. 115.12a), then check
// for drawer (115.12) and unit tray (115.12a) level images.
        }
        if (drawerImages == null || drawerImages.isEmpty()) { // Specimen has multiple images, but no drawer images
            log!!.debug("Specimen with no drawer images: " + specimen.Barcode)
            if (specimen.ICImages.size == 0) {
                log.error("Specimen with no images: " + specimen.Barcode)
            } else {
                resultFrame.loadImagesFromFiles(specimen.ICImages)
            }
        } else {
            val images: MutableSet<ICImage?> = specimen.ICImages
            images.addAll(drawerImages)
            resultFrame.loadImagesFromFiles(images)
        }
        resultFrame.pack()
        resultFrame.centerSpecimen() // Specimen is expected to be at the center of the specimen part of the image.
        resultFrame.setVisible(true)
    }

    fun addListener(aListener: DataChangeListener?) {
        if (listeners == null) {
            listeners = ArrayList<DataChangeListener?>()
        }
        log!!.debug("Added listener: $aListener")
        listeners!!.add(aListener)
    }

    fun notifyListeners() {
        if (listeners != null) {
            for (i in listeners!!.indices) {
                listeners!![i].notifyDataHasChanged()
            }
        }
    }

    companion object {
        private val log = LogFactory.getLog(SpecimenController::class.java)
    }
}
