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
import edu.harvard.mcz.imagecapture.interfaces.CloseListener;
import edu.harvard.mcz.imagecapture.interfaces.CloseType;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.utility.InputUtility;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeoreferenceDialog for editing latitude, longitude, and related georeference
 * metadata on a {@link LatLong} entity using declarative form bindings.
 */
public class GeoreferenceDialog extends JDialog {

	private static final Logger log = LoggerFactory.getLogger(GeoreferenceDialog.class);

	private final JPanel contentPanel;
	private final FormBindingContext<LatLong> bindingContext;
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
	private JTextField textDetDate;
	private JTextField textRefSource;
	private JXMapViewer mapViewer;
	private final ArrayList<CloseListener> closeListener;

	public GeoreferenceDialog(LatLong geoReference, SpecimenDetailsViewPane parent) {
		this(geoReference);
		this.parent = parent;
	}

	public GeoreferenceDialog(LatLong geoReference) {
		this.closeListener = new ArrayList<>();
		this.geoReference = geoReference != null ? geoReference : new LatLong();
		this.bindingContext = new FormBindingContext<>(LatLong.class, true);
		this.contentPanel = new JPanel(new MigLayout("wrap 1, fill"));
		init();
		loadData();
		setState();
		updateMap();
	}

	public LatLong getGeoReference() {
		return geoReference;
	}

	public void addCloseListener(CloseListener closeListener) {
		this.closeListener.add(closeListener);
	}

	public void setGeoReference(LatLong geoReference) {
		this.geoReference = geoReference != null ? geoReference : new LatLong();
		this.loadData();
	}

	public void loadData() {
		lblErrorLabel.setText("");
		bindingContext.readFrom(geoReference);
		if (geoReference.getDatum() != null && !geoReference.getDatum().isEmpty()) {
			cbDatum.setSelectedItem(geoReference.getDatum());
		}
		log.debug("Loaded geo data: lat: {}, long: {}", geoReference.getDecLatString(),
				geoReference.getDecLongString());
	}

	public void setState() {
		if (cbMethod.getSelectedItem() != null) {
			String acc = cbMethod.getSelectedItem().toString();
			txtGPSAccuracy.setEnabled("GPS".equals(acc));
		}

		if (comboBoxOrigUnits.getSelectedItem() != null) {
			String state = comboBoxOrigUnits.getSelectedItem().toString();
			switch (state) {
				case "degrees dec. minutes" :
					textFieldDecimalLat.setEnabled(false);
					textFieldDecimalLong.setEnabled(false);
					txtLatDegrees.setEnabled(true);
					txtLatDecMin.setEnabled(true);
					txtLatMin.setEnabled(false);
					txtLatSec.setEnabled(false);
					cbLatDir.setEnabled(true);
					txtLongDegrees.setEnabled(true);
					txtLongDecMin.setEnabled(true);
					txtLongMin.setEnabled(false);
					txtLongSec.setEnabled(false);
					cbLongDir.setEnabled(true);
					break;
				case "deg. min. sec." :
					textFieldDecimalLat.setEnabled(false);
					textFieldDecimalLong.setEnabled(false);
					txtLatDegrees.setEnabled(true);
					txtLatDecMin.setEnabled(false);
					txtLatMin.setEnabled(true);
					txtLatSec.setEnabled(true);
					cbLatDir.setEnabled(true);
					txtLongDegrees.setEnabled(true);
					txtLongDecMin.setEnabled(false);
					txtLongMin.setEnabled(true);
					txtLongSec.setEnabled(true);
					cbLongDir.setEnabled(true);
					break;
				case "decimal degrees" :
				case "unknown" :
				default :
					textFieldDecimalLat.setEnabled(true);
					textFieldDecimalLong.setEnabled(true);
					txtLatDegrees.setEnabled(false);
					txtLatDecMin.setEnabled(false);
					txtLatMin.setEnabled(false);
					txtLatSec.setEnabled(false);
					cbLatDir.setEnabled(false);
					txtLongDegrees.setEnabled(false);
					txtLongDecMin.setEnabled(false);
					txtLongMin.setEnabled(false);
					txtLongSec.setEnabled(false);
					cbLongDir.setEnabled(false);
					break;
			}
		}
	}

	private boolean saveData() {
		this.okButton.grabFocus();
		bindingContext.writeTo(geoReference);
		return true;
	}

	/**
	 * Update the marker on the map
	 */
	private void updateMap() {
		if (textFieldDecimalLat.getText().isEmpty() || textFieldDecimalLong.getText().isEmpty()) {
			return;
		}
		try {
			GeoPosition address = new GeoPosition(Double.parseDouble(textFieldDecimalLat.getText()),
					Double.parseDouble(textFieldDecimalLong.getText()));
			mapViewer.setAddressLocation(address);
			WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
			waypointPainter.setWaypoints(new HashSet<>(List.of(new DefaultWaypoint(address))));

			List<Painter<JXMapViewer>> painters = new ArrayList<>();
			painters.add(waypointPainter);

			CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
			mapViewer.setOverlayPainter(painter);
			mapViewer.setZoom(5);
		} catch (Exception e) {
			log.error("Failed to update map waypoint", e);
		}
	}

	private void init() {
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(scrollPane);
		JPanel fieldsPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 0"));

		textFieldDecimalLat = bindingContext.bindTextField("DecLat", LatLong::getDecLatString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setDecLat(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setDecLat(null);
			}
		});
		InputUtility.addChangeListener(textFieldDecimalLat, e -> updateMap());

		textFieldDecimalLong = bindingContext.bindTextField("DecLong", LatLong::getDecLongString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setDecLong(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setDecLong(null);
			}
		});
		InputUtility.addChangeListener(textFieldDecimalLong, e -> updateMap());

		cbMethod = bindingContext.bindComboBox("Georefmethod",
				new String[]{"not recorded", "unknown", "GEOLocate", "Geoportal", "Google Earth", "Google Maps",
						"Gazeteer", "GPS", "Label Data", "Wikipedia", "MaNIS/HertNet/ORNIS Georeferencing Guidelines"},
				LatLong::getGeorefmethod, LatLong::setGeorefmethod, cb -> cb.addActionListener(e -> setState()));

		ComboBoxModel<String> datumModel = new ListComboBoxModel<>(LatLong.getDatumValues());
		cbDatum = bindingContext.bindComboBox("Datum", datumModel, LatLong::getDatum, LatLong::setDatum,
				cb -> cb.setSelectedItem("WGS84"));

		txtGPSAccuracy = bindingContext.bindTextField("GPSAccuracy", LatLong::getGpsaccuracyString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setGpsaccuracy(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setGpsaccuracy(null);
			}
		});

		comboBoxOrigUnits = bindingContext.bindComboBox("OrigLatLongUnits",
				new String[]{"decimal degrees", "deg. min. sec.", "degrees dec. minutes", "unknown"},
				LatLong::getOrigLatLongUnits, LatLong::setOrigLatLongUnits,
				cb -> cb.addActionListener(e -> setState()));

		txtErrorRadius = bindingContext.bindTextField("MaxErrorDistance", LatLong::getMaxErrorDistanceString,
				(g, str) -> {
					if (str != null && !str.trim().isEmpty()) {
						try {
							g.setMaxErrorDistance(Integer.parseInt(str.trim()));
						} catch (NumberFormatException ignored) {
						}
					} else {
						g.setMaxErrorDistance(null);
					}
				});

		comboBoxErrorUnits = bindingContext.bindComboBox("MaxErrorUnits", new String[]{"m", "ft", "km", "mi", "yd"},
				LatLong::getMaxErrorUnits, LatLong::setMaxErrorUnits);

		txtLatDegrees = bindingContext.bindTextField("LatDeg", LatLong::getLatDegString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLatDeg(Integer.parseInt(str.trim()));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLatDeg(null);
			}
		});

		txtLatDecMin = bindingContext.bindTextField("DecLatMin", LatLong::getDecLatMinString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setDecLatMin(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setDecLatMin(null);
			}
		});

		txtLatMin = bindingContext.bindTextField("LatMin", LatLong::getLatMinString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLatMin(Integer.parseInt(str.trim()));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLatMin(null);
			}
		});

		txtLatSec = bindingContext.bindTextField("LatSec", LatLong::getLatSecString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLatSec(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLatSec(null);
			}
		});

		cbLatDir = bindingContext.bindComboBox("LatDir", new String[]{"N", "S"}, LatLong::getLatDir,
				LatLong::setLatDir);

		txtLongDegrees = bindingContext.bindTextField("LongDeg", LatLong::getLongDegString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLongDeg(Integer.parseInt(str.trim()));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLongDeg(null);
			}
		});

		txtLongDecMin = bindingContext.bindTextField("DecLongMin", LatLong::getDecLongMinString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setDecLongMin(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setDecLongMin(null);
			}
		});

		txtLongMin = bindingContext.bindTextField("LongMin", LatLong::getLongMinString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLongMin(Integer.parseInt(str.trim()));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLongMin(null);
			}
		});

		txtLongSec = bindingContext.bindTextField("LongSec", LatLong::getLongSecString, (g, str) -> {
			if (str != null && !str.trim().isEmpty()) {
				try {
					g.setLongSec(BigDecimal.valueOf(Double.parseDouble(str.trim())));
				} catch (NumberFormatException ignored) {
				}
			} else {
				g.setLongSec(null);
			}
		});

		cbLongDir = bindingContext.bindComboBox("LongDir", new String[]{"E", "W"}, LatLong::getLongDir,
				LatLong::setLongDir);

		textFieldDetBy = bindingContext.bindTextField("DeterminedByAgent", LatLong::getDeterminedByAgent,
				LatLong::setDeterminedByAgent);

		textDetDate = bindingContext.bindTextField("DeterminedDate",
				g -> g.getDeterminedDate() != null
						? new SimpleDateFormat("yyyy-MM-dd").format(g.getDeterminedDate())
						: "",
				(g, str) -> {
					if (str != null && !str.trim().isEmpty()) {
						try {
							g.setDeterminedDate(new SimpleDateFormat("yyyy-MM-dd").parse(str.trim()));
						} catch (ParseException ignored) {
						}
					} else {
						g.setDeterminedDate(null);
					}
				});
		textDetDate.setToolTipText("Date on which georeference was made, yyyy-mm-dd");

		textRefSource = bindingContext.bindTextField("LatLongRefSource", LatLong::getLatLongRefSource,
				LatLong::setLatLongRefSource);

		textFieldRemarks = bindingContext.bindTextField("LatLongRemarks", LatLong::getLatLongRemarks,
				LatLong::setLatLongRemarks);

		Component[] fields = {getPasteExcelButton(), textFieldDecimalLat, textFieldDecimalLong, cbMethod, cbDatum,
				txtGPSAccuracy, comboBoxOrigUnits, txtErrorRadius, comboBoxErrorUnits, txtLatDegrees, txtLatDecMin,
				txtLatMin, txtLatSec, cbLatDir, txtLongDegrees, txtLongDecMin, txtLongMin, txtLongSec, cbLongDir,
				textFieldDetBy, textDetDate, textRefSource, textFieldRemarks};

		String[] labels = {"Util", "Latitude", "Longitude", "Method", "Datum", "GPS Accuracy", "Original Units",
				"Error Radius", "Error Radius Units", "Lat Degrees", "Lat Dec Min", "Lat Min", "Lat Sec", "Lat N/S",
				"Long Degrees", "Long Dec Min", "Long Min", "Long Sec", "Long E/W", "Determined By", "Date Determined",
				"Reference Source", "Remarks"};

		for (int i = 0; i < labels.length; i++) {
			JLabel label = new JLabel();
			label.setText(labels[i].concat(":"));
			fieldsPanel.add(label, "tag label, right");
			fieldsPanel.add(fields[i], "grow");
		}

		contentPanel.add(fieldsPanel);

		JPanel mapPanel = new JPanel(new MigLayout("wrap 1, fill, insets 0"));
		mapViewer = new JXMapViewer();
		TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		mapViewer.setTileFactory(tileFactory);
		tileFactory.setThreadPoolSize(8);
		mapPanel.add(mapViewer, "grow");
		contentPanel.add(mapPanel, "grow, span 2, hmin 150");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		lblErrorLabel = new JLabel("Message");
		buttonPane.add(lblErrorLabel);

		okButton = new JButton("OK");
		GeoreferenceDialog self = this;
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeListener.forEach(listener -> listener.onClose(CloseType.OK, self));
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
				closeListener.forEach(listener -> listener.onClose(CloseType.CANCEL, self));
				loadData();
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		this.pack();
	}

	public void pasteFromExcel(String pasteValue) {
		Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();

		String[] pasteValuesStr = pasteValue.split("\t");
		ArrayList<String> pasteValues = new ArrayList<>(Arrays.asList(pasteValuesStr));

		Map<String, Component> defaultsMap = new HashMap<>();
		defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_ERR_RAD, txtErrorRadius);
		defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_METHOD, cbMethod);

		defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_LAT, textFieldDecimalLat);
		if (settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT)
				.equals(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LONG))) {
			String[] latLongValues = pasteValues
					.get(Integer.parseInt(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT))).trim()
					.split("[,; ]+");
			log.debug("Split latLong into {} values: {}", latLongValues.length, latLongValues);
			pasteValues.set(Integer.parseInt(settings.getProperty(ImageCaptureProperties.KEY_EXCEL_COL_LAT)),
					latLongValues[0]);
			pasteValues.add(latLongValues[latLongValues.length - 1]);
			defaultsMap.put(String.valueOf(pasteValues.size() - 1), textFieldDecimalLong);
		} else {
			defaultsMap.put(ImageCaptureProperties.KEY_EXCEL_COL_LONG, textFieldDecimalLong);
		}

		defaultsMap.forEach((key, field) -> {
			try {
				int intKey;
				if ("unset".equals(settings.getProperty(key, "unset"))) {
					intKey = Integer.parseInt(key);
				} else {
					intKey = Integer.parseInt(settings.getProperty(key));
				}
				if (pasteValues.size() <= intKey) {
					return;
				}
				if (field instanceof JTextField tf) {
					if (tf.getText().trim().isEmpty()
							|| settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
						tf.setText(pasteValues.get(intKey));
					}
				} else if (field instanceof JComboBox<?> cb) {
					Object selected = cb.getSelectedItem();
					String strVal = selected != null ? selected.toString() : "";
					if (strVal.isEmpty() || "unknown".equals(strVal) || "not recorded".equals(strVal)
							|| settings.getProperty(ImageCaptureProperties.KEY_EXCEL_OVERWRITE).equals("true")) {
						((JComboBox<String>) cb).setSelectedItem(pasteValues.get(intKey));
					}
				}
			} catch (Exception e) {
				log.error("Failed to set field when pasting, key: {}", key, e);
			}
		});

		saveData();

		if (parent != null) {
			parent.setLocationData(
					getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_VERBATIM_LOC),
					getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_SPECIFIC_LOC),
					getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_COUNTRY),
					getIndexIfAvailable(settings, pasteValues, ImageCaptureProperties.KEY_EXCEL_COL_STATE_PROVINCE),
					textFieldDecimalLat.getText(), textFieldDecimalLong.getText());
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
			pasteExcelButton.addActionListener(e -> {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				try {
					pasteFromExcel((String) clipboard.getData(DataFlavor.stringFlavor));
				} catch (Exception ex) {
					log.error("Failed to paste clipboard data from excel", ex);
				}
			});
		}
		return pasteExcelButton;
	}
}
