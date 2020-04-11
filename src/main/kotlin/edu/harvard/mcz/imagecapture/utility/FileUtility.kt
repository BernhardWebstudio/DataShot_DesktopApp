package edu.harvard.mcz.imagecapture.utility


import edu.harvard.mcz.imagecapture.Singleton
import java.awt.Dimension
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object FileUtility {
    fun askForDirectory(startpoint: File?): File? {
        val fileChooser = JFileChooser()
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        fileChooser.setPreferredSize(Dimension(800, 600))
        if (startpoint != null && startpoint.canRead()) {
            fileChooser.setCurrentDirectory(startpoint)
        }
        val returnValue: Int = fileChooser.showOpenDialog(Singleton.MainFrame)
        return if (returnValue == JFileChooser.APPROVE_OPTION) {
            fileChooser.SelectedFile
        } else {
            null
        }
    }

    fun askForImageFile(startpoint: File?): File? {
        val fileChooser = JFileChooser()
        fileChooser.setCurrentDirectory(startpoint)
        fileChooser.setPreferredSize(Dimension(800, 600))
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TIFF Images", "tif", "tiff");
        val filter = FileNameExtensionFilter("Image files", "tif", "tiff", "jpg", "jpeg", "png")
        fileChooser.setFileFilter(filter)
        val returnValue: Int = fileChooser.showOpenDialog(Singleton.MainFrame)
        return if (returnValue == JFileChooser.APPROVE_OPTION) {
            fileChooser.SelectedFile
        } else null
    }
}
