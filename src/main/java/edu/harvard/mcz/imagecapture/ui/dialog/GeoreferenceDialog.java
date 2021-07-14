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

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
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
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 *
 */
public class GeoreferenceDialog extends JDialog {

    private static final long serialVersionUID = -257199970146455008L;

    private static final Logger log =
            LoggerFactory.getLogger(GeoreferenceDialog.class);
    private final JPanel contentPanel;
    private LatLong geoReference;
    private SpecimenDetailsViewPane parent;
    private JComboBox<String> comboBoxOrigUnits;
    private JComboBox<String> comboBoxErrorUnits;
    private JButton pasteExcelButton;
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
    private JTextField textFieldRemarks;
    private JTextField txtErrorRadius;
    private JTextField textFieldDetBy;
    private JFormattedTextField textDetDate;
    private JTextField textRefSource;
    private JXMapViewer mapViewer;

    public GeoreferenceDialog(LatLong geoReference, SpecimenDetailsViewPane parent) {
        this(geoReference);
        this.parent = parent;
    }

    public GeoreferenceDialog(LatLong geoReference) {
        this.geoReference = geoReference;
        this.contentPanel = new JPanel(new MigLayout("wrap 1, fill"));
        init();
        loadData();
        setState();
        updateMap();
    }

    public LatLong getGeoReference() {
        return geoReference;
    }

    public void setGeoReference(LatLong geoReference) {
        this.geoReference = geoReference;
        this.loadData();
    }

    public void loadData() {
        lblErrorLabel.setText("");
        textFieldDecimalLat.setText(geoReference.getDecLatString());
        textFieldDecimalLong.setText(geoReference.getDecLongString());

        log.debug("load geo data: lat: " + geoReference.getDecLatString() +
                ", long: " + geoReference.getDecLongString());

        if (!geoReference.getDatum().equals("")) {
            cbDatum.setSelectedItem(geoReference.getDatum());
        }
        cbMethod.setSelectedItem(geoReference.getGeorefmethod());

        txtGPSAccuracy.setText(geoReference.getGpsaccuracyString());

        comboBoxOrigUnits.setSelectedItem(geoReference.getOrigLatLongUnits());

        txtLatDegrees.setText(geoReference.getLatDegString());
        txtLatDecMin.setText(geoReference.getDecLatMinString());
        txtLatMin.setText(geoReference.getLatMinString());
        txtLatSec.setText(geoReference.getLatSecString());
        cbLatDir.setSelectedItem(geoReference.getLatDir());

        txtLongDegrees.setText(geoReference.getLongDegString());
        txtLongDecMin.setText(geoReference.getDecLongMinString());
        txtLongMin.setText(geoReference.getLongMinString());
        txtLongSec.setText(geoReference.getLongSecString());
        cbLongDir.setSelectedItem(geoReference.getLongDir());

        txtErrorRadius.setText(geoReference.getMaxErrorDistanceString());
        comboBoxErrorUnits.setSelectedItem(geoReference.getMaxErrorUnits());

        this.textFieldDetBy.setText(geoReference.getDeterminedByAgent());
        this.textDetDate.setValue(new SimpleDateFormat("yyyy-MM-dd")
                .format(geoReference.getDeterminedDate()));
        this.textRefSource.setText(geoReference.getLatLongRefSource());

        textFieldRemarks.setText(geoReference.getLatLongRemarks());
    }

    public void setState() {
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
                geoReference.setDecLat(BigDecimal.valueOf(
                        Double.parseDouble(textFieldDecimalLat.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Latitude number format");
                result = false;
            }
        } else {
            geoReference.setDecLat(null);
        }
        if (textFieldDecimalLong.getText().length() > 0) {
            try {
                geoReference.setDecLong(BigDecimal.valueOf(
                        Double.parseDouble(textFieldDecimalLong.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Longitude number format");
                result = false;
            }
        } else {
            geoReference.setDecLong(null);
        }
        if (cbDatum.getSelectedItem() != null) {
            geoReference.setDatum(cbDatum.getSelectedItem().toString());
        }
        if (cbMethod.getSelectedItem() != null) {
            geoReference.setGeorefmethod(cbMethod.getSelectedItem().toString());
        }

        if (txtGPSAccuracy.getText().length() > 0) {
            try {
                geoReference.setGpsaccuracy(
                        BigDecimal.valueOf(Double.parseDouble(txtGPSAccuracy.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: GPS Accuracy number format");
                result = false;
            }
        }

        if (comboBoxOrigUnits.getSelectedItem() != null) {
            geoReference.setOrigLatLongUnits(
                    comboBoxOrigUnits.getSelectedItem().toString());
        }

        if (txtLatDegrees.getText().length() > 0) {
            try {
                geoReference.setLatDeg(Integer.parseInt(txtLatDegrees.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Degrees number format");
                result = false;
            }
        }
        if (txtLatDecMin.getText().length() > 0) {
            try {
                geoReference.setDecLatMin(
                        BigDecimal.valueOf(Double.parseDouble(txtLatDecMin.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Dec Min number format");
                result = false;
            }
        }
        if (txtLatMin.getText().length() > 0) {
            try {
                geoReference.setLatMin(Integer.parseInt(txtLatMin.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Min number format");
                result = false;
            }
        }
        if (txtLatSec.getText().length() > 0) {
            try {
                geoReference.setLatSec(
                        BigDecimal.valueOf(Double.parseDouble(txtLatSec.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Lat Degrees number format");
                result = false;
            }
        }
        if (cbLatDir.getSelectedItem() != null) {
            geoReference.setLatDir(cbLatDir.getSelectedItem().toString());
        }

        if (txtLongDegrees.getText().length() > 0) {
            try {
                geoReference.setLongDeg(Integer.parseInt(txtLongDegrees.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Degrees number format");
                result = false;
            }
        }
        if (txtLongDecMin.getText().length() > 0) {
            try {
                geoReference.setDecLongMin(
                        BigDecimal.valueOf(Double.parseDouble(txtLongDecMin.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Dec Min number format");
                result = false;
            }
        }
        if (txtLongMin.getText().length() > 0) {
            try {
                geoReference.setLongMin(Integer.parseInt(txtLongMin.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Min number format");
                result = false;
            }
        }
        if (txtLongSec.getText().length() > 0) {
            try {
                geoReference.setLongSec(
                        BigDecimal.valueOf(Double.parseDouble(txtLongSec.getText())));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Long Degrees number format");
                result = false;
            }
        }
        if (cbLongDir.getSelectedItem() != null) {
            geoReference.setLongDir(cbLongDir.getSelectedItem().toString());
        }

        if (txtErrorRadius.getText().length() > 0) {
            try {
                geoReference.setMaxErrorDistance(
                        Integer.parseInt(txtErrorRadius.getText()));
            } catch (NumberFormatException e) {
                lblErrorLabel.setText("Error: Error radius number format");
                result = false;
            }
        }

        if (comboBoxErrorUnits.getSelectedItem() != null) {
            geoReference.setMaxErrorUnits(
                    comboBoxErrorUnits.getSelectedItem().toString());
        }

        geoReference.setDeterminedByAgent(this.textFieldDetBy.getText());

        try {
            geoReference.setDeterminedDate(
                    new SimpleDateFormat("yyyy-MM-dd").parse(this.textDetDate.getText()));
        } catch (ParseException e) {
            lblErrorLabel.setText("Error: Error date determined format");
            result = false;
        }

        geoReference.setLatLongRefSource(this.textRefSource.getText());

        geoReference.setLatLongRemarks(this.textFieldRemarks.getText());

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
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(scrollPane);
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

        Component[] fields = {
                this.getPasteExcelButton(),
                textFieldDecimalLat,
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

        String[] labels = {
                "Util",
                "Latitude", "Longitude",
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

    public void pasteFromExcel(String pasteValue) {
        Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();

        String[] pasteValuesStr = pasteValue.split("\t");
        ArrayList<String> pasteValues = new ArrayList<String>(Arrays.asList(pasteValuesStr));
        // handle values
        Map<String, Component> defaultsMapImmutable = Map.ofEntries(
                Map.entry(ImageCaptureProperties.KEY_EXCEL_COL_ERR_RAD, txtErrorRadius),
                Map.entry(ImageCaptureProperties.KEY_EXCEL_COL_METHOD, cbMethod)
        );
        // make it mutable
        Map<String, Component> defaultsMap = new HashMap<>(defaultsMapImmutable);

        // lat / long are exceptions, as they may need to be split
        defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_LAT, textFieldDecimalLat);
        if (settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT).equals(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LONG))) {
            String[] latLongValues = pasteValues.get(Integer.parseInt(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT))).trim().split("[,; ]+");
            log.debug("Split latLong into {} values: {}", latLongValues.length, latLongValues);
            // Set the long value to long only
            pasteValues.set(Integer.parseInt(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT)), latLongValues[0]);
            // Set the lat value at a new position
            pasteValues.add(latLongValues[latLongValues.length - 1]);
            // then, add this new position to the map we use
            // this is ugly and brings a bit of technical dept, see below
            defaultsMap.put(String.valueOf(pasteValues.size() - 1), textFieldDecimalLong);
        } else {
            // no problem if not need to split
            defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_LONG, textFieldDecimalLong);
        }

        // set all the values as applicable
        defaultsMap.forEach((key, field) -> {
            try {
                int intKey = 0;
                if (settings.getProperty(key, "unset").equals("unset")) {
                    // necessity: see above
                    intKey = Integer.parseInt(key);
                } else {
                    intKey = Integer.parseInt(settings.getProperty(key));
                }
                if (pasteValues.size() <= intKey) {
                    return;
                }
                if (field instanceof JTextField) {
                    if (((JTextField) field).getText().trim().equals("") || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
                        ((JTextField) field).setText(pasteValues.get(intKey));
                    }
                } else if (field instanceof JComboBox) {
                    JComboBox<String> comboBox = (JComboBox<String>) field;
                    if ((comboBox.getSelectedItem().equals("") || comboBox.getSelectedItem().equals("unknown") || comboBox.getSelectedItem().equals("not recorded")) || settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
                        ((JComboBox<?>) field).setSelectedItem(pasteValues.get(intKey));
                    }
                }
            } catch (Exception e) {
                log.error("Failed to set field when pasting, key: " + key, e);
            }
        });

        saveData();

        // also propagate changes to other screen
        if (parent != null) {
            parent.setLocationData(
                    getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_VERBATIM_LOC),
                    getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_SPECIFIC_LOC),
                    getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_COUNTRY),
                    getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_STATE_PROVINCE),
                    textFieldDecimalLat.getText(),
                    textFieldDecimalLong.getText()
            );
        }
    }

    private String getIndexIfAvailable(Properties settings, List<String> values, String key) {
        int intKey = Integer.parseInt(settings.getProperty(key));
        if (values.size() > intKey) {
            return values.get(intKey);
        }
        return "";
    }

    private JButton getPasteExcelButton() {
        if (pasteExcelButton == null) {
            pasteExcelButton = new JButton("Paste Excel");
            pasteExcelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    try {
                        pasteFromExcel((String) clipboard.getData(DataFlavor.stringFlavor));
                    } catch (Exception e) {
                        log.error("Failed to paste clipboard data from excel", e);
                    }
                }
            });
        }
        return pasteExcelButton;
    }
}
