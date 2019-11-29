/**
 * VerbatimToTranscribeDialog.java
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

import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
import edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class VerbatimToTranscribeDialog extends JDialog {

    private static final long serialVersionUID = 1871411835203004797L;

    private static final Log log = LogFactory.getLog(VerbatimToTranscribeDialog.class);

    private final JPanel contentPanel = new JPanel();
    private JTable table;

    /**
     * Create the dialog.
     */
    public VerbatimToTranscribeDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 726, 557);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            table = new JTable();
            SpecimenLifeCycle sls = new SpecimenLifeCycle();
            GenusSpeciesCountTableModel model = new GenusSpeciesCountTableModel(sls.countSpecimensForVerbatim());
            table.setModel(model);
            table.setDefaultRenderer(GenusSpeciesCount.class, new ButtonRenderer("Transcribe"));
            table.setDefaultEditor(GenusSpeciesCount.class, new ButtonEditor(ButtonEditor.OPEN_SPECIMEN_VERBATIM, this));
            contentPanel.add(table, BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Close");
                cancelButton.setActionCommand("Close");
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }

                });
                buttonPane.add(cancelButton);
            }
        }
    }
}
