/**
 * GeoreferenceDialog.java
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

import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.utility.InputUtility;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class GeoreferenceDialog extends JDialog {

    private static final long serialVersionUID = -257199970146455008L;

    private static final Logger log =
            LoggerFactory.getLogger(GeoreferenceDialog.class);

    private final JPanel contentPanel;
    private final LatLong georeference;
    private JComboBox<String> comboBoxOrigUnits;
    private JComboBox<String> comboBoxErrorUnits;
    private JTextField txtGPSAccuracy;
    private JTextField textFieldDecimalLat;
    private JTextField textFieldDecimalLong;
    private JTextField txtLatDegrees;
    private JTextField txtLatDecMin;
    private JTextField txtLatMin;
    private JTextField txtLatSec;
    private JComboBox<String> cbLatDir;
    private JTextField txtLongDegrees;
    private JTextField txtLongDecMin;
    private JTextField txtLongMin;
    private JTextField txtLongSec;
    private JComboBox<String> cbLongDir;
    private JComboBox<String> cbDatum;
    private JComboBox<String> cbMethod;
    private JButton okButton;
    private JLabel lblErrorLabel;
    private JLabel lblNewLabel;
    private JTextField textFieldRemarks;
    private JLabel lblErrorRadius;
    private JTextField txtErrorRadius;
    private JTextField textFieldDetBy;
    private JFormattedTextField textDetDate;
    private JTextField textRefSource;
    private JXMapViewer mapViewer;

    public GeoreferenceDialog(LatLong georeference) {
        this.georeference = georeference;
        this.contentPanel = new JPanel(new MigLayout("wrap 1, fill"));
        init();
        loadData();
        setState();
        updateMap();
    }

    private void loadData() {
        lblErrorLabel.setText("");
        textFieldDecimalLat.setText(georeference.getDecLatString());
        textFieldDecimalLong.setText(georeference.getDecLongString());

        log.debug("load geo data: lat: " + georeference.getDecLatString() +
                ", long: " + georeference.getDecLongString());

        if (!georeference.getDatum().equals("")) {
            cbDatum.setSelectedItem(georeference.getDatum());
        }
        cbMethod.setSelectedItem(georeference.getGeorefmethod());

        txtGPSAccuracy.setText(georeference.getGpsaccuracyString());

        comboBoxOrigUnits.setSelectedItem(georeference.getOrigLatLongUnits());

        txtLatDegrees.setText(georeference.getLatDegString());
        txtLatDecMin.setText(georeference.getDecLatMinString());
        txtLatMin.setText(georeference.getLatMinString());
        txtLatSec.setText(georeference.getLatSecString());
        cbLatDir.setSelectedItem(georeference.getLatDir());

        txtLongDegrees.setText(georeference.getLongDegString());
        txtLongDecMin.setText(georeference.getDecLongMinString());
        txtLongMin.setText(georeference.getLongMinString());
        txtLongSec.setText(georeference.getLongSecString());
        cbLongDir.setSelectedItem(georeference.getLongDir());

        txtErrorRadius.setText(georeference.getMaxErrorDistanceString());
        comboBoxErrorUnits.setSelectedItem(georeference.getMaxErrorUnits());

        this.textFieldDetBy.setText(georeference.getDeterminedByAgent());
        this.textDetDate.setValue(new SimpleDateFormat("yyyy-MM-dd")
                .format(georeference.getDeterminedDate()));
        this.textRefSource.setText(georeference.getLatLongRefSource());

        textFieldRemarks.setText(georeference.getLatLongRemarks());
    }

    private void setState() {
        String acc = this.cbMethod.getSelectedItem().toString();
        this.txtGPSAccuracy.setEnabled(acc.equals("GPS"));

        String state = this.comboBoxOrigUnits.getSelectedItem().toString();
        switch (state) {
            case "degrees dec. minutes":
                this.textFieldDecimalLat.setEnabled(false);
                this.textFieldDecimalLong.setEnabled(false);
                this.txtLatDegrees.setEnabled(true);
                this.txtLatDecMin.setEnabled(true);
                this.txtLatMin.setEnabled(false);
                this.txtLatSec.setEnabled(false);
                this.cbLatDir.setEnabled(true);
                this.txtLongDegrees.setEnabled(true);
                this.txtLongDecMin.setEnabled(true);
                this.txtLongMin.setEnabled(false);
                this.txtLongSec.setEnabled(false);
                this.cbLongDir.setEnabled(true);
                break;
            case "deg. min. sec.":
                this.textFieldDecimalLat.setEnabled(false);
                this.textFieldDecimalLong.setEnabled(false);
                this.txtLatDegrees.setEnabled(true);
                this.txtLatDecMin.setEnabled(false);
                this.txtLatMin.setEnabled(true);
                this.txtLatSec.setEnabled(true);
                this.cbLatDir.setEnabled(true);
                this.txtLongDegrees.setEnabled(true);
                this.txtLongDecMin.setEnabled(false);
                this.txtLongMin.setEnabled(true);
                this.txtLongSec.setEnabled(true);
                this.cbLongDir.setEnabled(true);
                break;
            case "decimal degrees":
            case "unknown":
            default:
                this.textFieldDecimalLat.setEnabled(true);
                this.textFieldDecimalLong.setEnabled(true);
                this.txtLatDegrees.setEnabled(false);
                this.txtLatDecMin.setEnabled(false);
                this.txtLatMin.setEnabled(false);
                this.txtLatSec.setEnabled(false);
                this.cbLatDir.setEnabled(false);
                this.txtLongDegrees.setEnabled(false);
                this.txtLongDecMin.setEnabled(false);
                this.txtLongMin.setEnabled(false);
                this.txtLongSec.setEnabled(false);
                this.cbLongDir.setEnabled(false);
                break;
        }
    }

    private boolean saveData() {
        boolean result = true;
        this.okButton.grabFocus();
        if (textFieldDecimalLat.getText().length() > 0) {
            try {
                georeference.setDecLat(BigDecimal.valueOf(
                        Double.parseDouble(textFieldDecimalLat.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Latitude number format");
                result = false;
            }
        } else {
            georeference.setDecLat(null);
        }
        if (textFieldDecimalLong.getText().length() > 0) {
            try {
                georeference.setDecLong(BigDecimal.valueOf(
                        Double.parseDouble(textFieldDecimalLong.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Longitude number format");
                result = false;
            }
        } else {
            georeference.setDecLong(null);
        }
        if (cbDatum.getSelectedItem() != null) {
            georeference.setDatum(cbDatum.getSelectedItem().toString());
        }
        if (cbMethod.getSelectedItem() != null) {
            georeference.setGeorefmethod(cbMethod.getSelectedItem().toString());
        }

        if (txtGPSAccuracy.getText().length() > 0) {
            try {
                georeference.setGpsaccuracy(
                        BigDecimal.valueOf(Double.parseDouble(txtGPSAccuracy.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: GPS Accuracy number format");
                result = false;
            }
        }

        if (comboBoxOrigUnits.getSelectedItem() != null) {
            georeference.setOrigLatLongUnits(
                    comboBoxOrigUnits.getSelectedItem().toString());
        }

        if (txtLatDegrees.getText().length() > 0) {
            try {
                georeference.setLatDeg(Integer.parseInt(txtLatDegrees.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Degrees number format");
                result = false;
            }
        }
        if (txtLatDecMin.getText().length() > 0) {
            try {
                georeference.setDecLatMin(
                        BigDecimal.valueOf(Double.parseDouble(txtLatDecMin.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Dec Min number format");
                result = false;
            }
        }
        if (txtLatMin.getText().length() > 0) {
            try {
                georeference.setLatMin(Integer.parseInt(txtLatMin.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Min number format");
                result = false;
            }
        }
        if (txtLatSec.getText().length() > 0) {
            try {
                georeference.setLatSec(
                        BigDecimal.valueOf(Double.parseDouble(txtLatSec.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Degrees number format");
                result = false;
            }
        }
        if (cbLatDir.getSelectedItem() != null) {
            georeference.setLatDir(cbLatDir.getSelectedItem().toString());
        }

        if (txtLongDegrees.getText().length() > 0) {
            try {
                georeference.setLongDeg(Integer.parseInt(txtLongDegrees.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Degrees number format");
                result = false;
            }
        }
        if (txtLongDecMin.getText().length() > 0) {
            try {
                georeference.setDecLongMin(
                        BigDecimal.valueOf(Double.parseDouble(txtLongDecMin.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Dec Min number format");
                result = false;
            }
        }
        if (txtLongMin.getText().length() > 0) {
            try {
                georeference.setLongMin(Integer.parseInt(txtLongMin.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Min number format");
                result = false;
            }
        }
        if (txtLongSec.getText().length() > 0) {
            try {
                georeference.setLongSec(
                        BigDecimal.valueOf(Double.parseDouble(txtLongSec.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Degrees number format");
                result = false;
            }
        }
        if (cbLongDir.getSelectedItem() != null) {
            georeference.setLongDir(cbLongDir.getSelectedItem().toString());
        }

        if (txtErrorRadius.getText().length() > 0) {
            try {
                georeference.setMaxErrorDistance(
                        Integer.parseInt(txtErrorRadius.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Error radius number format");
                result = false;
            }
        }

        if (comboBoxErrorUnits.getSelectedItem() != null) {
            georeference.setMaxErrorUnits(
                    comboBoxErrorUnits.getSelectedItem().toString());
        }

        georeference.setDeterminedByAgent(this.textFieldDetBy.getText());

        try {
            georeference.setDeterminedDate(
                    new SimpleDateFormat("yyyy-MM-dd").parse(this.textDetDate.getText()));
        } catch (ParseException e) {
            lblErrorLabel.setText("Error: Error date determined format");
            result = false;
        }

        georeference.setLatLongRefSource(this.textRefSource.getText());

        georeference.setLatLongRemarks(this.textFieldRemarks.getText());

        return result;
    }

    /**
     * Update the marker on the map
     */
    private void updateMap() {
        try {
            GeoPosition address =
                    new GeoPosition(Double.parseDouble(textFieldDecimalLat.getText()),
                            Double.parseDouble(textFieldDecimalLong.getText()));
            mapViewer.setAddressLocation(address);
            // Create a waypoint painter that takes all the waypoints
            WaypointPainter<Waypoint> waypointPainter =
                    new WaypointPainter<Waypoint>();
            waypointPainter.setWaypoints(
                    new HashSet<Waypoint>(Arrays.asList(new DefaultWaypoint(address))));

            // Create a compound painter that uses both the route-painter and the
            // waypoint-painter
            List<Painter<JXMapViewer>> painters =
                    new ArrayList<Painter<JXMapViewer>>();
            painters.add(waypointPainter);

            CompoundPainter<JXMapViewer> painter =
                    new CompoundPainter<JXMapViewer>(painters);
            mapViewer.setOverlayPainter(painter);
            mapViewer.setZoom(5);
        } catch (Exception e) {
            log.error("Error", e);
        }
    }

    private void init() {
        // init the pane
        getContentPane().add(contentPanel);
        JPanel fieldsPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 0"));

        // instantiate input fields
        textFieldDecimalLat = new JTextField();
        InputUtility.addChangeListener(textFieldDecimalLat, e -> updateMap());
        textFieldDecimalLong = new JTextField();
        InputUtility.addChangeListener(textFieldDecimalLong, e -> updateMap());
        cbMethod =
                new JComboBox<String>(new DefaultComboBoxModel<String>(new String[]{
                        "not recorded", "unknown", "GEOLocate", "Geoportal", "Google Earth",
                        "Google Maps", "Gazeteer", "GPS", "Label Data", "Wikipedia",
                        "MaNIS/HertNet/ORNIS Georeferencing Guidelines"}));
        cbMethod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setState();
            }
        });
        ComboBoxModel<String> datumModel =
                new ListComboBoxModel<String>(LatLong.getDatumValues());
        cbDatum = new JComboBox<String>(datumModel);
        // set default
        cbDatum.setSelectedItem("WGS84");
        txtGPSAccuracy = new JTextField();
        comboBoxOrigUnits = new JComboBox<String>();
        comboBoxOrigUnits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setState();
            }
        });
        comboBoxOrigUnits.setModel(new DefaultComboBoxModel<String>(
                new String[]{"decimal degrees", "deg. min. sec.",
                        "degrees dec. minutes", "unknown"}));
        txtErrorRadius = new JTextField();
        comboBoxErrorUnits = new JComboBox<String>();
        comboBoxErrorUnits.setModel(new DefaultComboBoxModel<String>(
                new String[]{"m", "ft", "km", "mi", "yd"}));
        contentPanel.add(comboBoxErrorUnits);
        txtLatDegrees = new JTextField();
        txtLatDecMin = new JTextField();
        txtLatMin = new JTextField();
        txtLatSec = new JTextField();
        cbLatDir = new JComboBox<String>();
        cbLatDir.setModel(
                new DefaultComboBoxModel<String>(new String[]{"N", "S"}));
        txtLongDegrees = new JTextField();
        txtLongDecMin = new JTextField();
        txtLongMin = new JTextField();
        txtLongSec = new JTextField();
        cbLongDir = new JComboBox<String>();
        cbLongDir.setModel(
                new DefaultComboBoxModel<String>(new String[]{"E", "W"}));
        textFieldDetBy = new JTextField();
        try {
            textDetDate = new JFormattedTextField(new MaskFormatter("####-##-##"));
        } catch (ParseException e1) {
            textDetDate = new JFormattedTextField();
        }
        textDetDate.setToolTipText(
                "Date on which georeference was made, yyyy-mm-dd");
        textRefSource = new JTextField();
        textFieldRemarks = new JTextField();

        Component[] fields = {textFieldDecimalLat,
                textFieldDecimalLong,
                cbMethod,
                cbDatum,
                txtGPSAccuracy,
                comboBoxOrigUnits,
                txtErrorRadius,
                comboBoxErrorUnits,
                txtLatDegrees,
                txtLatDecMin,
                txtLatMin,
                txtLatSec,
                cbLatDir,
                txtLongDegrees,
                txtLongDecMin,
                txtLongMin,
                txtLongSec,
                cbLongDir,
                textFieldDetBy,
                textDetDate,
                textRefSource,
                textFieldRemarks};

        String[] labels = {"Latitude", "Longitude",
                "Method", "Datum",
                "GPS Accuracy", "Original Units",
                "Error Radius", "Error Radius Units",
                "Lat Degrees", "Lat Dec Min",
                "Lat Min", "Lat Sec",
                "Lat N/S", "Long Degrees",
                "Long Dec Min", "Long Min",
                "Long Sec", "Long E/W",
                "Determined By", "Date Determined",
                "Reference Source", "Remarks"};

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel();
            label.setText(labels[i].concat(":"));
            fieldsPanel.add(label, "right"); //"align label");
            fieldsPanel.add(fields[i], "grow");
        }

        contentPanel.add(fieldsPanel); // "grow");

        JPanel mapPanel = new JPanel(new MigLayout("wrap 1, fill, insets 0"));
        mapViewer = new JXMapViewer();
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);
        mapPanel.add(mapViewer, "grow");
        //        mapViewer.setPreferredSize(new Dimension(200, 100));
        contentPanel.add(mapPanel, "grow, span 2, hmin 150");

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        lblErrorLabel = new JLabel("Message");
        buttonPane.add(lblErrorLabel);

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblErrorLabel.setText("");

                if (saveData()) {
                    setVisible(false);
                }
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                loadData();
                setVisible(false);
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        // recalc
        this.pack();
    }
}
