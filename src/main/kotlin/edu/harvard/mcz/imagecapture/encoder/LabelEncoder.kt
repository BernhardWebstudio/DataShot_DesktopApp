/**
 * LabelEncoder.java
 * edu.harvard.mcz.imagecapture.encoder
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
package edu.harvard.mcz.imagecapture.encoder


import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.itextpdf.text.*
import com.itextpdf.text.BaseColor
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import edu.harvard.mcz.imagecapture.encoder.LabelEncoder
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.exceptions.PrintFailedException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycle
import org.apache.commons.logging.LogFactory
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * LabelEncoder
 */
class LabelEncoder(aLabel: UnitTrayLabel?) {
    private val label: UnitTrayLabel?// TODO Auto-generated catch block
    // set ErrorCorrectionLevel here
    private val qRCodeMatrix: BitMatrix?
        private get() {
            var result: BitMatrix? = null
            val writer = QRCodeWriter()
            try {
                val data: String = label.toJSONString()
                val hints: Hashtable<EncodeHintType?, ErrorCorrectionLevel?> = Hashtable<EncodeHintType?, ErrorCorrectionLevel?>() // set ErrorCorrectionLevel here
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                result = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints)
            } catch (e: WriterException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return result
        }// TODO Auto-generated catch block// TODO Auto-generated catch block

    // ZXing now has MatrixToImageWriter to avoid looping through byte arrays.
    val image: Image?
        get() {
            val barcode = qRCodeMatrix
            val bufferedImage: BufferedImage = MatrixToImageWriter.toBufferedImage(barcode)
            // ZXing now has MatrixToImageWriter to avoid looping through byte arrays.
            var image: Image? = null
            try {
                image = Image.getInstance(bufferedImage, null)
            } catch (e: BadElementException) { // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return image
        }

    companion object {
        private val log = LogFactory.getLog(LabelEncoder::class.java)
        @Throws(PrintFailedException::class)
        fun printList(taxa: MutableList<UnitTrayLabel?>?): Boolean {
            var taxa: MutableList<UnitTrayLabel?>? = taxa
            var result = false
            var label = UnitTrayLabel()
            var encoder = LabelEncoder(label)
            var image = encoder.image
            var counter = 0
            try {
                val document = Document()
                PdfWriter.getInstance(document, FileOutputStream("labels.pdf"))
                document.pageSize = PageSize.LETTER
                document.open()
                var table = PdfPTable(4)
                table.setWidthPercentage(100f)
                //table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                val cellWidths = floatArrayOf(30f, 20f, 30f, 20f)
                table.setWidths(cellWidths)
                val uls = UnitTrayLabelLifeCycle()
                if (taxa == null) {
                    taxa = uls.findAll()
                }
                var i: MutableIterator<UnitTrayLabel?> = taxa.iterator()
                var cell: PdfPCell? = null
                var cell_barcode: PdfPCell? = null
                // Create two lists of 12 cells, the first 6 of each representing
// the left hand column of 6 labels, the second 6 of each
// representing the right hand column.
// cells holds the text for each label, cells_barcode the barcode.
                val cells: ArrayList<PdfPCell?> = ArrayList<PdfPCell?>(12)
                val cells_barcode: ArrayList<PdfPCell?> = ArrayList<PdfPCell?>(12)
                for (x in 0..11) {
                    cells.add(null)
                    cells_barcode.add(null)
                }
                var cellCounter = 0
                while (i.hasNext()) { // Loop through all of the taxa (unit tray labels) found to print
                    label = i.next()
                    for (toPrint in 0 until label.NumberToPrint) { // For each taxon, loop through the number of requested copies
// Generate a text and a barcode cell for each, and add to array for page
                        log!!.debug("Label " + toPrint + " of " + label.NumberToPrint)
                        cell = PdfPCell()
                        cell.setBorderColor(BaseColor.LIGHT_GRAY)
                        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP)
                        cell.disableBorderSide(PdfPCell.RIGHT)
                        cell.setPaddingLeft(3f)
                        var higherNames = ""
                        higherNames = if (label.Tribe.trim({ it <= ' ' }).length > 0) {
                            label.Family + ": " + label.Subfamily + ": " + label.Tribe
                        } else {
                            label.Family + ": " + label.Subfamily
                        }
                        val higher = Paragraph()
                        higher.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        higher.add(Chunk(higherNames))
                        cell.addElement(higher)
                        val name = Paragraph()
                        val genus = Chunk(label.Genus.trim({ it <= ' ' }) + " ")
                        genus.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        var species = Chunk(label.SpecificEpithet.trim({ it <= ' ' }))
                        var normal: Chunk? = null // normal font prefix to preceed specific epithet (nr. <i>epithet</i>)
                        if (label.SpecificEpithet.contains(".") || label.SpecificEpithet.contains("[")) {
                            if (label.SpecificEpithet.startsWith("nr. ")) {
                                normal = Chunk("nr. ")
                                normal.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                                species = Chunk(label.SpecificEpithet.trim({ it <= ' ' }).substring(4))
                                species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                            } else {
                                species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                            }
                        } else {
                            species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        }
                        var s = ""
                        s = if (label.SubspecificEpithet.trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val subspecies = Chunk(s + label.SubspecificEpithet.trim({ it <= ' ' }))
                        if (label.SubspecificEpithet.contains(".") || label.SubspecificEpithet.contains("[")) {
                            subspecies.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        } else {
                            subspecies.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        }
                        s = if (label.InfraspecificRank.trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val infraRank = Chunk(s + label.InfraspecificRank.trim({ it <= ' ' }))
                        infraRank.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        s = if (label.InfraspecificEpithet.trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val infra = Chunk(s + label.InfraspecificEpithet.trim({ it <= ' ' }))
                        infra.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        s = if (label.UnNamedForm.trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val unNamed = Chunk(s + label.UnNamedForm.trim({ it <= ' ' }))
                        unNamed.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        name.add(genus)
                        if (normal != null) {
                            name.add(normal)
                        }
                        name.add(species)
                        name.add(subspecies)
                        name.add(infraRank)
                        name.add(infra)
                        name.add(unNamed)
                        cell.addElement(name)
                        val authorship = Paragraph()
                        authorship.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                        if (label.Authorship != null && label.Authorship.length > 0) {
                            val c_authorship = Chunk(label.Authorship)
                            authorship.add(c_authorship)
                        }
                        cell.addElement(authorship)
                        //cell.addElement(new Paragraph(" "));
                        if (label.DrawerNumber != null && label.DrawerNumber.length > 0) {
                            val drawerNumber = Paragraph()
                            drawerNumber.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                            val c_drawerNumber = Chunk(label.DrawerNumber)
                            drawerNumber.add(c_drawerNumber)
                            cell.addElement(drawerNumber)
                        } else {
                            if (label.Collection != null && label.Collection.length > 0) {
                                val collection = Paragraph()
                                collection.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                                val c_collection = Chunk(label.Collection)
                                collection.add(c_collection)
                                cell.addElement(collection)
                            }
                        }
                        cell_barcode = PdfPCell()
                        cell_barcode.setBorderColor(BaseColor.LIGHT_GRAY)
                        cell_barcode.disableBorderSide(PdfPCell.LEFT)
                        cell_barcode.setVerticalAlignment(PdfPCell.ALIGN_TOP)
                        encoder = LabelEncoder(label)
                        image = encoder.image
                        image!!.alignment = Image.ALIGN_TOP
                        cell_barcode.addElement(image)
                        cells.add(cellCounter, cell)
                        cells_barcode.add(cellCounter, cell_barcode)
                        cellCounter++
                        // If we have hit a full set of 12 labels, add them to the document
// in two columns, filling left column first, then right
                        if (cellCounter == 12) { // add a page of 12 cells in columns of two.
                            for (x in 0..5) {
                                if (cells[x] == null) {
                                    val c = PdfPCell()
                                    c.setBorder(0)
                                    table.addCell(c)
                                    table.addCell(c)
                                } else {
                                    table.addCell(cells[x])
                                    table.addCell(cells_barcode[x])
                                }
                                if (cells[x + 6] == null) {
                                    val c = PdfPCell()
                                    c.setBorder(0)
                                    table.addCell(c)
                                    table.addCell(c)
                                } else {
                                    table.addCell(cells[x + 6])
                                    table.addCell(cells_barcode[x + 6])
                                }
                            }
                            // Reset to begin next page
                            cellCounter = 0
                            document.add(table)
                            table = PdfPTable(4)
                            table.setWidthPercentage(100f)
                            table.setWidths(cellWidths)
                            for (x in 0..11) {
                                cells[x] = null
                                cells_barcode[x] = null
                            }
                        }
                    } // end loop through toPrint (for a taxon)
                    counter++
                } // end while results has next (for all taxa requested)
                // get any remaining cells in pairs
                for (x in 0..5) {
                    if (cells[x] == null) {
                        val c = PdfPCell()
                        c.setBorder(0)
                        table.addCell(c)
                        table.addCell(c)
                    } else {
                        table.addCell(cells[x])
                        table.addCell(cells_barcode[x])
                    }
                    if (cells[x + 6] == null) {
                        val c = PdfPCell()
                        c.setBorder(0)
                        table.addCell(c)
                        table.addCell(c)
                    } else {
                        table.addCell(cells[x + 6])
                        table.addCell(cells_barcode[x + 6])
                    }
                }
                // add any remaining cells
                document.add(table)
                try {
                    document.close()
                } catch (e: Exception) {
                    throw PrintFailedException("No labels to print." + e.message)
                }
                // Check to see if there was content in the document.
                if (counter == 0) {
                    result = false
                } else { // Printed to pdf ok.
                    result = true
                    // Increment number printed.
                    i = taxa.iterator()
                    while (i.hasNext()) {
                        label = i.next()
                        for (toPrint in 0 until label.NumberToPrint) {
                            label.setPrinted(label.Printed + 1)
                        }
                        label.setNumberToPrint(0)
                        try {
                            uls.attachDirty(label)
                        } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: FileNotFoundException) { // TODO Auto-generated catch block
                e.printStackTrace()
                throw PrintFailedException("File not found.")
            } catch (e: DocumentException) { // TODO Auto-generated catch block
                e.printStackTrace()
                throw PrintFailedException("Error buiding PDF document.")
            } catch (e: OutOfMemoryError) {
                println("Out of memory error. " + e.message)
                println("Failed.  Too many labels.")
                throw PrintFailedException("Ran out of memory, too many labels at once.")
            }
            return result
        }

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val uls = UnitTrayLabelLifeCycle()
                val taxa: MutableList<UnitTrayLabel?> = uls.findAll()
                val ok = printList(taxa)
            } catch (e: PrintFailedException) {
                println("Failed to print all.  " + e.message)
            }
            println("Done.")
        }
    }

    init {
        label = aLabel
    }
}
