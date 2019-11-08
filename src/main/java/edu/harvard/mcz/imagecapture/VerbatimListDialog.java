/**
 * VerbatimListDialog.java
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

import edu.harvard.mcz.imagecapture.data.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.struct.VerbatimCount;
import edu.harvard.mcz.imagecapture.struct.VerbatimCountTableModel;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
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
public class VerbatimListDialog extends JDialog implements DataChangeListener {
    private static final long serialVersionUID = 6589370164897719328L;

    private static final Log log = LogFactory.getLog(VerbatimListDialog.class);

    private final JPanel contentPanel = new JPanel();
    private JTable table;

    /**
     * Create the dialog.
     */
    public VerbatimListDialog() {
        init();
    }

    protected void init() {
        setTitle("Verbatim Transcriptions to parse into fields");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        table = new JTable(new VerbatimCountTableModel(sls.countDistinctVerbatimValues()));
        table.setDefaultRenderer(VerbatimCount.class, new ButtonRenderer("Edit"));
        table.setDefaultEditor(VerbatimCount.class, new ButtonEditor(ButtonEditor.OPEN_VERBATIM_CLASSIFY, this));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
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

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#notifyDataHasChanged()
     */
    @Override
    public void notifyDataHasChanged() {
        ((VerbatimCountTableModel) table.getModel()).fireDataHasChanged();
    }
}
