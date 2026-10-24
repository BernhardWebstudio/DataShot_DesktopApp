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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
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

					// Pre-fill DDM if empty but decimal fields have values
					if (txtLatDecMin.getText().trim().isEmpty() && !textFieldDecimalLat.getText().trim().isEmpty()) {
						Double lat = parseCoordinateString(textFieldDecimalLat.getText(), "N");
						if (lat != null) {
							DDM ddm = decimalToDDM(lat, true);
							txtLatDegrees.setText(String.valueOf(ddm.degrees()));
							txtLatDecMin.setText(String.format(Locale.US, "%.4f", ddm.decMinutes()));
							cbLatDir.setSelectedItem(ddm.direction());
						}
					}
					if (txtLongDecMin.getText().trim().isEmpty() && !textFieldDecimalLong.getText().trim().isEmpty()) {
						Double lon = parseCoordinateString(textFieldDecimalLong.getText(), "E");
						if (lon != null) {
							DDM ddm = decimalToDDM(lon, false);
							txtLongDegrees.setText(String.valueOf(ddm.degrees()));
							txtLongDecMin.setText(String.format(Locale.US, "%.4f", ddm.decMinutes()));
							cbLongDir.setSelectedItem(ddm.direction());
						}
					}
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

					// Pre-fill DMS if empty but decimal fields have values
					if ((txtLatMin.getText().trim().isEmpty() || txtLatSec.getText().trim().isEmpty())
							&& !textFieldDecimalLat.getText().trim().isEmpty()) {
						Double lat = parseCoordinateString(textFieldDecimalLat.getText(), "N");
						if (lat != null) {
							DMS dms = decimalToDMS(lat, true);
							txtLatDegrees.setText(String.valueOf(dms.degrees()));
							txtLatMin.setText(String.valueOf(dms.minutes()));
							txtLatSec.setText(String.format(Locale.US, "%.2f", dms.seconds()));
							cbLatDir.setSelectedItem(dms.direction());
						}
					}
					if ((txtLongMin.getText().trim().isEmpty() || txtLongSec.getText().trim().isEmpty())
							&& !textFieldDecimalLong.getText().trim().isEmpty()) {
						Double lon = parseCoordinateString(textFieldDecimalLong.getText(), "E");
						if (lon != null) {
							DMS dms = decimalToDMS(lon, false);
							txtLongDegrees.setText(String.valueOf(dms.degrees()));
							txtLongMin.setText(String.valueOf(dms.minutes()));
							txtLongSec.setText(String.format(Locale.US, "%.2f", dms.seconds()));
							cbLongDir.setSelectedItem(dms.direction());
						}
					}
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
		GeoPosition currentPos = getCurrentGeoPosition();
		if (currentPos != null) {
			geoReference.setDecLat(BigDecimal.valueOf(currentPos.getLatitude()));
			geoReference.setDecLong(BigDecimal.valueOf(currentPos.getLongitude()));
		}
		bindingContext.writeTo(geoReference);
		return true;
	}

	public record DDM(int degrees, double decMinutes, String direction) {
	}

	public record DMS(int degrees, int minutes, double seconds, String direction) {
	}

	public static DDM decimalToDDM(double decimal, boolean isLatitude) {
		String dir = isLatitude ? (decimal < 0 ? "S" : "N") : (decimal < 0 ? "W" : "E");
		double abs = Math.abs(decimal);
		int deg = (int) Math.floor(abs);
		double decMin = (abs - deg) * 60.0;
		return new DDM(deg, decMin, dir);
	}

	public static DMS decimalToDMS(double decimal, boolean isLatitude) {
		String dir = isLatitude ? (decimal < 0 ? "S" : "N") : (decimal < 0 ? "W" : "E");
		double abs = Math.abs(decimal);
		int deg = (int) Math.floor(abs);
		double totalMinutes = (abs - deg) * 60.0;
		int min = (int) Math.floor(totalMinutes);
		double sec = (totalMinutes - min) * 60.0;
		return new DMS(deg, min, sec, dir);
	}

	public static Double parseDMS(String degStr, String minStr, String secStr, String dir) {
		if (degStr == null || degStr.trim().isEmpty() || minStr == null || minStr.trim().isEmpty()) {
			return null;
		}
		try {
			double deg = Math.abs(Double.parseDouble(degStr.trim()));
			double min = Math.abs(Double.parseDouble(minStr.trim()));
			double sec = 0;
			if (secStr != null && !secStr.trim().isEmpty()) {
				sec = Math.abs(Double.parseDouble(secStr.trim()));
			}
			if (min >= 60 || sec >= 60) {
				return null;
			}
			double decimal = deg + (min / 60.0) + (sec / 3600.0);
			if (dir != null && ("S".equalsIgnoreCase(dir.trim()) || "W".equalsIgnoreCase(dir.trim()))) {
				decimal = -decimal;
			} else if (degStr.trim().startsWith("-")) {
				decimal = -decimal;
			}
			return decimal;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double parseDDM(String degStr, String decMinStr, String dir) {
		if (degStr == null || degStr.trim().isEmpty() || decMinStr == null || decMinStr.trim().isEmpty()) {
			return null;
		}
		try {
			double deg = Math.abs(Double.parseDouble(degStr.trim()));
			double decMin = Math.abs(Double.parseDouble(decMinStr.trim()));
			if (decMin >= 60) {
				return null;
			}
			double decimal = deg + (decMin / 60.0);
			if (dir != null && ("S".equalsIgnoreCase(dir.trim()) || "W".equalsIgnoreCase(dir.trim()))) {
				decimal = -decimal;
			} else if (degStr.trim().startsWith("-")) {
				decimal = -decimal;
			}
			return decimal;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double parseCoordinateString(String input, String defaultDir) {
		if (input == null || input.trim().isEmpty()) {
			return null;
		}
		String s = input.trim();

		boolean negative = false;
		if (s.toUpperCase().contains("S") || s.toUpperCase().contains("W")) {
			negative = true;
		} else if (s.startsWith("-")) {
			negative = true;
		} else if (defaultDir != null
				&& ("S".equalsIgnoreCase(defaultDir.trim()) || "W".equalsIgnoreCase(defaultDir.trim()))) {
			negative = true;
		}

		String clean = s.replaceAll("[°'\"]", " ").replaceAll("[NSEWnsew]", " ").replaceAll("[,;]", " ").trim();

		String[] parts = clean.split("\\s+");
		if (parts.length == 0 || parts[0].isEmpty()) {
			return null;
		}

		try {
			if (parts.length == 1) {
				double val = Math.abs(Double.parseDouble(parts[0]));
				return negative ? -val : val;
			} else if (parts.length == 2) {
				double deg = Math.abs(Double.parseDouble(parts[0]));
				double min = Math.abs(Double.parseDouble(parts[1]));
				double val = deg + (min / 60.0);
				return negative ? -val : val;
			} else if (parts.length >= 3) {
				double deg = Math.abs(Double.parseDouble(parts[0]));
				double min = Math.abs(Double.parseDouble(parts[1]));
				double sec = Math.abs(Double.parseDouble(parts[2]));
				double val = deg + (min / 60.0) + (sec / 3600.0);
				return negative ? -val : val;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return null;
	}

	public GeoPosition getCurrentGeoPosition() {
		String state = comboBoxOrigUnits != null && comboBoxOrigUnits.getSelectedItem() != null
				? comboBoxOrigUnits.getSelectedItem().toString()
				: "decimal degrees";

		String latDir = cbLatDir != null && cbLatDir.getSelectedItem() != null
				? cbLatDir.getSelectedItem().toString()
				: "N";
		String longDir = cbLongDir != null && cbLongDir.getSelectedItem() != null
				? cbLongDir.getSelectedItem().toString()
				: "E";

		if ("degrees dec. minutes".equals(state)) {
			Double lat = parseDDM(txtLatDegrees != null ? txtLatDegrees.getText() : null,
					txtLatDecMin != null ? txtLatDecMin.getText() : null, latDir);
			Double lon = parseDDM(txtLongDegrees != null ? txtLongDegrees.getText() : null,
					txtLongDecMin != null ? txtLongDecMin.getText() : null, longDir);
			if (lat != null && lon != null) {
				return new GeoPosition(lat, lon);
			}
		} else if ("deg. min. sec.".equals(state)) {
			Double lat = parseDMS(txtLatDegrees != null ? txtLatDegrees.getText() : null,
					txtLatMin != null ? txtLatMin.getText() : null, txtLatSec != null ? txtLatSec.getText() : null,
					latDir);
			Double lon = parseDMS(txtLongDegrees != null ? txtLongDegrees.getText() : null,
					txtLongMin != null ? txtLongMin.getText() : null, txtLongSec != null ? txtLongSec.getText() : null,
					longDir);
			if (lat != null && lon != null) {
				return new GeoPosition(lat, lon);
			}
		}

		// Decimal fields
		String decLatStr = textFieldDecimalLat != null ? textFieldDecimalLat.getText() : null;
		String decLongStr = textFieldDecimalLong != null ? textFieldDecimalLong.getText() : null;
		Double lat = parseCoordinateString(decLatStr, latDir);
		Double lon = parseCoordinateString(decLongStr, longDir);
		if (lat != null && lon != null) {
			return new GeoPosition(lat, lon);
		}

		// Fallbacks: check if DDM fields have values
		Double ddmLat = parseDDM(txtLatDegrees != null ? txtLatDegrees.getText() : null,
				txtLatDecMin != null ? txtLatDecMin.getText() : null, latDir);
		Double ddmLon = parseDDM(txtLongDegrees != null ? txtLongDegrees.getText() : null,
				txtLongDecMin != null ? txtLongDecMin.getText() : null, longDir);
		if (ddmLat != null && ddmLon != null) {
			return new GeoPosition(ddmLat, ddmLon);
		}

		// Fallback: check if DMS fields have values
		Double dmsLat = parseDMS(txtLatDegrees != null ? txtLatDegrees.getText() : null,
				txtLatMin != null ? txtLatMin.getText() : null, txtLatSec != null ? txtLatSec.getText() : null, latDir);
		Double dmsLon = parseDMS(txtLongDegrees != null ? txtLongDegrees.getText() : null,
				txtLongMin != null ? txtLongMin.getText() : null, txtLongSec != null ? txtLongSec.getText() : null,
				longDir);
		if (dmsLat != null && dmsLon != null) {
			return new GeoPosition(dmsLat, dmsLon);
		}

		return null;
	}

	/**
	 * Convert an error distance in specified units to meters.
	 */
	public static double convertErrorDistanceToMeters(double distance, String unit) {
		if (distance <= 0) {
			return 0;
		}
		if (unit == null) {
			return distance;
		}
		return switch (unit.trim().toLowerCase()) {
			case "km" -> distance * 1000.0;
			case "ft" -> distance * 0.3048;
			case "mi" -> distance * 1609.344;
			case "yd" -> distance * 0.9144;
			default -> distance;
		};
	}

	/**
	 * Custom Painter for JXMapViewer that renders a semi-transparent circle scaled
	 * to the georeference error distance in meters.
	 */
	public static class ErrorRadiusPainter implements Painter<JXMapViewer> {
		private final GeoPosition position;
		private final double radiusInMeters;
		private final Color fillColor;
		private final Color outlineColor;

		public ErrorRadiusPainter(GeoPosition position, double radiusInMeters) {
			this(position, radiusInMeters, new Color(0, 120, 255, 60), new Color(0, 120, 255, 180));
		}

		public ErrorRadiusPainter(GeoPosition position, double radiusInMeters, Color fillColor, Color outlineColor) {
			this.position = position;
			this.radiusInMeters = radiusInMeters;
			this.fillColor = fillColor;
			this.outlineColor = outlineColor;
		}

		public GeoPosition getPosition() {
			return position;
		}

		public double getRadiusInMeters() {
			return radiusInMeters;
		}

		public Color getFillColor() {
			return fillColor;
		}

		public Color getOutlineColor() {
			return outlineColor;
		}

		@Override
		public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
			if (position == null || radiusInMeters <= 0 || map == null || map.getTileFactory() == null) {
				return;
			}
			Point2D center = map.convertGeoPositionToPoint(position);
			if (center == null) {
				return;
			}

			// Earth radius in meters (WGS84 authalic / equatorial radius)
			final double EARTH_RADIUS = 6378137.0;
			double latRad = Math.toRadians(position.getLatitude());
			double cosLat = Math.cos(latRad);
			if (cosLat < 0.0001) {
				cosLat = 0.0001;
			}

			double deltaLon = (radiusInMeters / (EARTH_RADIUS * cosLat)) * (180.0 / Math.PI);
			double longitudeDegreeWidth = map.getTileFactory().getInfo().getLongitudeDegreeWidthInPixels(map.getZoom());
			double radiusPixels = deltaLon * longitudeDegreeWidth;
			if (radiusPixels <= 0) {
				return;
			}

			Graphics2D g2d = (Graphics2D) g.create();
			try {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Shape circle = new Ellipse2D.Double(center.getX() - radiusPixels, center.getY() - radiusPixels,
						radiusPixels * 2.0, radiusPixels * 2.0);

				g2d.setColor(fillColor);
				g2d.fill(circle);

				g2d.setColor(outlineColor);
				g2d.setStroke(new BasicStroke(1.5f));
				g2d.draw(circle);
			} finally {
				g2d.dispose();
			}
		}
	}

	public double getErrorRadiusInMeters() {
		if (txtErrorRadius == null || txtErrorRadius.getText() == null || txtErrorRadius.getText().trim().isEmpty()) {
			return 0;
		}
		try {
			double dist = Double.parseDouble(txtErrorRadius.getText().trim());
			if (dist <= 0) {
				return 0;
			}
			String unit = comboBoxErrorUnits != null && comboBoxErrorUnits.getSelectedItem() != null
					? comboBoxErrorUnits.getSelectedItem().toString()
					: "m";
			return convertErrorDistanceToMeters(dist, unit);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private boolean isUpdatingMap = false;

	/**
	 * Update the marker on the map
	 */
	public void updateMap() {
		if (isUpdatingMap) {
			return;
		}
		isUpdatingMap = true;
		try {
			GeoPosition address = getCurrentGeoPosition();
			if (address == null) {
				return;
			}

			// If in DDM or DMS mode, synchronize the calculated decimal values into decimal
			// text fields
			String origUnits = comboBoxOrigUnits != null && comboBoxOrigUnits.getSelectedItem() != null
					? comboBoxOrigUnits.getSelectedItem().toString()
					: "decimal degrees";
			if ("degrees dec. minutes".equals(origUnits) || "deg. min. sec.".equals(origUnits)) {
				String formattedLat = String.format(Locale.US, "%.6f", address.getLatitude());
				String formattedLong = String.format(Locale.US, "%.6f", address.getLongitude());
				if (textFieldDecimalLat != null && !formattedLat.equals(textFieldDecimalLat.getText())) {
					textFieldDecimalLat.setText(formattedLat);
				}
				if (textFieldDecimalLong != null && !formattedLong.equals(textFieldDecimalLong.getText())) {
					textFieldDecimalLong.setText(formattedLong);
				}
			}

			mapViewer.setAddressLocation(address);
			WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
			waypointPainter.setWaypoints(new HashSet<>(List.of(new DefaultWaypoint(address))));

			List<Painter<JXMapViewer>> painters = new ArrayList<>();
			double errorInMeters = getErrorRadiusInMeters();
			if (errorInMeters > 0) {
				painters.add(new ErrorRadiusPainter(address, errorInMeters));
			}
			painters.add(waypointPainter);

			CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
			mapViewer.setOverlayPainter(painter);
			if (mapViewer.getZoom() <= 0) {
				mapViewer.setZoom(5);
			}
		} catch (Exception e) {
			log.error("Failed to update map waypoint", e);
		} finally {
			isUpdatingMap = false;
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

		ComboBoxModel<String> datumModel = new DefaultComboBoxModel<>(LatLong.getDatumValues().toArray(new String[0]));
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
				LatLong::getOrigLatLongUnits, LatLong::setOrigLatLongUnits, cb -> cb.addActionListener(e -> {
					setState();
					updateMap();
				}));

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
		InputUtility.addChangeListener(txtErrorRadius, e -> updateMap());

		comboBoxErrorUnits = bindingContext.bindComboBox("MaxErrorUnits", new String[]{"m", "ft", "km", "mi", "yd"},
				LatLong::getMaxErrorUnits, LatLong::setMaxErrorUnits, cb -> cb.addActionListener(e -> updateMap()));

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
		InputUtility.addChangeListener(txtLatDegrees, e -> updateMap());
		InputUtility.addChangeListener(txtLatDecMin, e -> updateMap());
		InputUtility.addChangeListener(txtLatMin, e -> updateMap());
		InputUtility.addChangeListener(txtLatSec, e -> updateMap());
		cbLatDir.addActionListener(e -> updateMap());

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
		InputUtility.addChangeListener(txtLongDegrees, e -> updateMap());
		InputUtility.addChangeListener(txtLongDecMin, e -> updateMap());
		InputUtility.addChangeListener(txtLongMin, e -> updateMap());
		InputUtility.addChangeListener(txtLongSec, e -> updateMap());
		cbLongDir.addActionListener(e -> updateMap());

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

	public JXMapViewer getMapViewer() {
		return mapViewer;
	}

	public JTextField getTxtErrorRadius() {
		return txtErrorRadius;
	}

	public JComboBox<String> getComboBoxErrorUnits() {
		return comboBoxErrorUnits;
	}

	public JTextField getTextFieldDecimalLat() {
		return textFieldDecimalLat;
	}

	public JTextField getTextFieldDecimalLong() {
		return textFieldDecimalLong;
	}

	public JTextField getTxtLatDegrees() {
		return txtLatDegrees;
	}

	public JTextField getTxtLatDecMin() {
		return txtLatDecMin;
	}

	public JTextField getTxtLatMin() {
		return txtLatMin;
	}

	public JTextField getTxtLatSec() {
		return txtLatSec;
	}

	public JComboBox<String> getCbLatDir() {
		return cbLatDir;
	}

	public JTextField getTxtLongDegrees() {
		return txtLongDegrees;
	}

	public JTextField getTxtLongDecMin() {
		return txtLongDecMin;
	}

	public JTextField getTxtLongMin() {
		return txtLongMin;
	}

	public JTextField getTxtLongSec() {
		return txtLongSec;
	}

	public JComboBox<String> getCbLongDir() {
		return cbLongDir;
	}

	public JComboBox<String> getComboBoxOrigUnits() {
		return comboBoxOrigUnits;
	}
}
