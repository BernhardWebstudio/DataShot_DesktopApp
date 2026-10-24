package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import junit.framework.TestCase;
import org.junit.Test;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Unit tests for: 1. Country Mismatch Warning in SpecimenDetailsViewPane 2.
 * Error Radius Circle custom Painter<JXMapViewer> in GeoreferenceDialog
 */
public class TestCountryMismatchAndErrorRadius extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		HibernateUtil.getTestSessionFactory();
	}

	@Test
	public void testConvertErrorDistanceToMeters() {
		// m -> meters
		assertEquals(100.0, GeoreferenceDialog.convertErrorDistanceToMeters(100.0, "m"), 0.001);
		assertEquals(100.0, GeoreferenceDialog.convertErrorDistanceToMeters(100.0, "M"), 0.001);
		assertEquals(100.0, GeoreferenceDialog.convertErrorDistanceToMeters(100.0, null), 0.001);

		// km -> meters
		assertEquals(5000.0, GeoreferenceDialog.convertErrorDistanceToMeters(5.0, "km"), 0.001);

		// ft -> meters (1 ft = 0.3048 m)
		assertEquals(304.8, GeoreferenceDialog.convertErrorDistanceToMeters(1000.0, "ft"), 0.001);

		// mi -> meters (1 mi = 1609.344 m)
		assertEquals(1609.344, GeoreferenceDialog.convertErrorDistanceToMeters(1.0, "mi"), 0.001);

		// yd -> meters (1 yd = 0.9144 m)
		assertEquals(9.144, GeoreferenceDialog.convertErrorDistanceToMeters(10.0, "yd"), 0.001);

		// Non-positive distances
		assertEquals(0.0, GeoreferenceDialog.convertErrorDistanceToMeters(0.0, "km"), 0.001);
		assertEquals(0.0, GeoreferenceDialog.convertErrorDistanceToMeters(-10.0, "m"), 0.001);
	}

	@Test
	public void testErrorRadiusPainterPropertiesAndPainting() {
		GeoPosition pos = new GeoPosition(47.3769, 8.5417);
		double radiusMeters = 500.0;
		GeoreferenceDialog.ErrorRadiusPainter painter = new GeoreferenceDialog.ErrorRadiusPainter(pos, radiusMeters);

		assertEquals(pos, painter.getPosition());
		assertEquals(radiusMeters, painter.getRadiusInMeters(), 0.001);
		assertNotNull(painter.getFillColor());
		assertNotNull(painter.getOutlineColor());
		assertTrue("Fill color should have semi-transparency", painter.getFillColor().getAlpha() < 255);

		// Custom colors
		Color customFill = new Color(255, 0, 0, 80);
		Color customOutline = new Color(255, 0, 0, 200);
		GeoreferenceDialog.ErrorRadiusPainter customPainter = new GeoreferenceDialog.ErrorRadiusPainter(pos,
				radiusMeters, customFill, customOutline);
		assertEquals(customFill, customPainter.getFillColor());
		assertEquals(customOutline, customPainter.getOutlineColor());

		// Test painting onto a mock map viewer canvas
		JXMapViewer mapViewer = new JXMapViewer();
		mapViewer.setSize(400, 400);
		mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
		mapViewer.setZoom(5);
		mapViewer.setAddressLocation(pos);

		BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		try {
			// Should paint without any exception
			painter.paint(g2d, mapViewer, 400, 400);
		} finally {
			g2d.dispose();
		}
	}

	@Test
	public void testGeoreferenceDialogRendersErrorRadiusCircle() throws Exception {
		javax.swing.SwingUtilities.invokeAndWait(() -> {
			LatLong latLong = new LatLong();
			latLong.setDecLat(new BigDecimal("47.3769"));
			latLong.setDecLong(new BigDecimal("8.5417"));
			latLong.setMaxErrorDistance(250);
			latLong.setMaxErrorUnits("m");

			GeoreferenceDialog dialog = new GeoreferenceDialog(latLong);

			try {
				// Verify error radius converted
				assertEquals(250.0, dialog.getErrorRadiusInMeters(), 0.001);

				// Verify overlay painter contains an ErrorRadiusPainter
				Painter<? super JXMapViewer> overlay = dialog.getMapViewer().getOverlayPainter();
				assertNotNull(overlay);
				assertTrue(overlay instanceof CompoundPainter);

				CompoundPainter<JXMapViewer> compound = (CompoundPainter<JXMapViewer>) overlay;
				boolean hasErrorRadiusPainter = false;
				for (Painter<JXMapViewer> p : compound.getPainters()) {
					if (p instanceof GeoreferenceDialog.ErrorRadiusPainter erp) {
						hasErrorRadiusPainter = true;
						assertEquals(250.0, erp.getRadiusInMeters(), 0.001);
						assertEquals(47.3769, erp.getPosition().getLatitude(), 0.0001);
					}
				}
				assertTrue("Overlay painter should contain ErrorRadiusPainter", hasErrorRadiusPainter);

				// Test changing error radius updates the painter
				dialog.getTxtErrorRadius().setText("2");
				dialog.getComboBoxErrorUnits().setSelectedItem("km");
				dialog.updateMap();
				assertEquals(2000.0, dialog.getErrorRadiusInMeters(), 0.001);

				compound = (CompoundPainter<JXMapViewer>) dialog.getMapViewer().getOverlayPainter();
				hasErrorRadiusPainter = false;
				for (Painter<JXMapViewer> p : compound.getPainters()) {
					if (p instanceof GeoreferenceDialog.ErrorRadiusPainter erp) {
						hasErrorRadiusPainter = true;
						assertEquals(2000.0, erp.getRadiusInMeters(), 0.001);
					}
				}
				assertTrue("Updated overlay painter should contain ErrorRadiusPainter with 2000m",
						hasErrorRadiusPainter);
			} finally {
				dialog.dispose();
			}
		});
	}

	@Test
	public void testCountryMismatchDetection() {
		// Empty/null specified country -> no mismatch
		assertFalse(SpecimenDetailsViewPane.isCountryMismatch(null, "CH", "Switzerland"));
		assertFalse(SpecimenDetailsViewPane.isCountryMismatch("", "CH", "Switzerland"));
		assertFalse(SpecimenDetailsViewPane.isCountryMismatch("   ", "CH", "Switzerland"));

		// Direct matches
		assertFalse("Matching ISO code directly", SpecimenDetailsViewPane.isCountryMismatch("CH", "CH", "Switzerland"));
		assertFalse("Matching ISO code case-insensitive",
				SpecimenDetailsViewPane.isCountryMismatch("ch", "CH", "Switzerland"));
		assertFalse("Matching English name via code/locale",
				SpecimenDetailsViewPane.isCountryMismatch("Switzerland", "CH"));
		assertFalse("Matching German name via code/locale", SpecimenDetailsViewPane.isCountryMismatch("Schweiz", "CH"));
		assertFalse("Matching French name via code/locale", SpecimenDetailsViewPane.isCountryMismatch("Suisse", "CH"));
		assertFalse("Matching ISO3 code via locale", SpecimenDetailsViewPane.isCountryMismatch("CHE", "CH"));
		assertFalse("Matching possibleCountryNames",
				SpecimenDetailsViewPane.isCountryMismatch("Confederatio Helvetica", "CH", "Confederatio Helvetica"));

		// Actual mismatches
		assertTrue("Germany does not match CH",
				SpecimenDetailsViewPane.isCountryMismatch("Germany", "CH", "Switzerland"));
		assertTrue("France does not match CH",
				SpecimenDetailsViewPane.isCountryMismatch("France", "CH", "Switzerland"));
		assertTrue("United States does not match CH",
				SpecimenDetailsViewPane.isCountryMismatch("United States", "CH", "Switzerland"));
		assertTrue("Austria does not match DE",
				SpecimenDetailsViewPane.isCountryMismatch("Austria", "DE", "Deutschland"));
	}

	@Test
	public void testFlagAndClearCountryMismatchOnViewPane() throws Exception {
		Specimen s = new Specimen();
		s.setBarcode("TEST12345");
		s.setCountry("France");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		JComboBox<String> countryBox = pane.getJComboBoxCountry();
		assertNotNull(countryBox);
		Border originalBorder = countryBox.getBorder();

		// Flag a mismatch with "Switzerland"
		pane.flagCountryMismatch("Switzerland");

		// Let event queue dispatch
		Thread.sleep(100);

		// Border should now be changed, tooltip set
		assertNotSame("Border should have been updated to warning highlight", originalBorder, countryBox.getBorder());
		assertNotNull("Tooltip should be set with warning", countryBox.getToolTipText());
		assertTrue("Tooltip contains reverse-geocoded country", countryBox.getToolTipText().contains("Switzerland"));
		assertTrue("Tooltip mentions mismatch", countryBox.getToolTipText().contains("does not match"));

		// Clear mismatch
		pane.clearCountryMismatch();
		Thread.sleep(100);

		assertNull("Tooltip should be cleared", countryBox.getToolTipText());
	}

	@Test
	public void testDMSandDDMCoordinateParsing() {
		// DMS: 47 deg, 22 min, 36.84 sec N -> 47.3769
		Double dmsLatN = GeoreferenceDialog.parseDMS("47", "22", "36.84", "N");
		assertNotNull(dmsLatN);
		assertEquals(47.3769, dmsLatN, 0.0001);

		// DMS: 47 deg, 22 min, 36.84 sec S -> -47.3769
		Double dmsLatS = GeoreferenceDialog.parseDMS("47", "22", "36.84", "S");
		assertNotNull(dmsLatS);
		assertEquals(-47.3769, dmsLatS, 0.0001);

		// DMS Longitude: 8 deg, 32 min, 30 sec W -> -8.541666...
		Double dmsLonW = GeoreferenceDialog.parseDMS("8", "32", "30", "W");
		assertNotNull(dmsLonW);
		assertEquals(-8.541666, dmsLonW, 0.0001);

		// DDM: 47 deg, 22.614 decMin N -> 47.3769
		Double ddmLatN = GeoreferenceDialog.parseDDM("47", "22.614", "N");
		assertNotNull(ddmLatN);
		assertEquals(47.3769, ddmLatN, 0.0001);

		// DDM: 8 deg, 32.502 decMin E -> 8.5417
		Double ddmLonE = GeoreferenceDialog.parseDDM("8", "32.502", "E");
		assertNotNull(ddmLonE);
		assertEquals(8.5417, ddmLonE, 0.0001);
	}

	@Test
	public void testDecimalToDDMAndDMSConversion() {
		// 47.3769 -> DDM 47 deg, 22.614 min N
		GeoreferenceDialog.DDM ddmLat = GeoreferenceDialog.decimalToDDM(47.3769, true);
		assertEquals(47, ddmLat.degrees());
		assertEquals(22.614, ddmLat.decMinutes(), 0.001);
		assertEquals("N", ddmLat.direction());

		// -47.3769 -> DDM S
		GeoreferenceDialog.DDM ddmSouth = GeoreferenceDialog.decimalToDDM(-47.3769, true);
		assertEquals(47, ddmSouth.degrees());
		assertEquals("S", ddmSouth.direction());

		// 47.3769 -> DMS 47 deg, 22 min, 36.84 sec N
		GeoreferenceDialog.DMS dmsLat = GeoreferenceDialog.decimalToDMS(47.3769, true);
		assertEquals(47, dmsLat.degrees());
		assertEquals(22, dmsLat.minutes());
		assertEquals(36.84, dmsLat.seconds(), 0.01);
		assertEquals("N", dmsLat.direction());
	}

	@Test
	public void testParseCoordinateStringVariations() {
		// Pure decimal
		assertEquals(47.3769, GeoreferenceDialog.parseCoordinateString("47.3769", "N"), 0.0001);
		assertEquals(-47.3769, GeoreferenceDialog.parseCoordinateString("-47.3769", "N"), 0.0001);

		// Cardinal suffixes
		assertEquals(-47.3769, GeoreferenceDialog.parseCoordinateString("47.3769 S", "N"), 0.0001);
		assertEquals(8.5417, GeoreferenceDialog.parseCoordinateString("8.5417 E", "E"), 0.0001);
		assertEquals(-8.5417, GeoreferenceDialog.parseCoordinateString("8.5417 W", "E"), 0.0001);

		// Degree symbols & DDM/DMS strings
		assertEquals(47.3769, GeoreferenceDialog.parseCoordinateString("47° 22.614' N", "N"), 0.0001);
		assertEquals(47.3769, GeoreferenceDialog.parseCoordinateString("47° 22' 36.84\" N", "N"), 0.0001);
		assertEquals(-47.3769, GeoreferenceDialog.parseCoordinateString("47° 22' 36.84\" S", "N"), 0.0001);
	}

	@Test
	public void testGeoreferenceDialogPlottingWithDDMandDMS() throws Exception {
		javax.swing.SwingUtilities.invokeAndWait(() -> {
			LatLong latLong = new LatLong();
			GeoreferenceDialog dialog = new GeoreferenceDialog(latLong);

			try {
				// 1. Switch to "degrees dec. minutes"
				dialog.getComboBoxOrigUnits().setSelectedItem("degrees dec. minutes");
				dialog.getTxtLatDegrees().setText("47");
				dialog.getTxtLatDecMin().setText("22.614");
				dialog.getCbLatDir().setSelectedItem("N");

				dialog.getTxtLongDegrees().setText("8");
				dialog.getTxtLongDecMin().setText("32.502");
				dialog.getCbLongDir().setSelectedItem("E");
				dialog.updateMap();

				GeoPosition currentPos = dialog.getCurrentGeoPosition();
				assertNotNull("Should resolve GeoPosition from DDM inputs", currentPos);
				assertEquals(47.3769, currentPos.getLatitude(), 0.0001);
				assertEquals(8.5417, currentPos.getLongitude(), 0.0001);

				GeoPosition mapAddress = dialog.getMapViewer().getAddressLocation();
				assertNotNull("Map address should be updated from DDM inputs", mapAddress);
				assertEquals(47.3769, mapAddress.getLatitude(), 0.0001);
				assertEquals(8.5417, mapAddress.getLongitude(), 0.0001);

				// 2. Switch to "deg. min. sec."
				dialog.getComboBoxOrigUnits().setSelectedItem("deg. min. sec.");
				dialog.getTxtLatDegrees().setText("46");
				dialog.getTxtLatMin().setText("30");
				dialog.getTxtLatSec().setText("0");
				dialog.getCbLatDir().setSelectedItem("N");

				dialog.getTxtLongDegrees().setText("7");
				dialog.getTxtLongMin().setText("45");
				dialog.getTxtLongSec().setText("0");
				dialog.getCbLongDir().setSelectedItem("E");
				dialog.updateMap();

				GeoPosition dmsPos = dialog.getCurrentGeoPosition();
				assertNotNull("Should resolve GeoPosition from DMS inputs", dmsPos);
				assertEquals(46.5, dmsPos.getLatitude(), 0.0001);
				assertEquals(7.75, dmsPos.getLongitude(), 0.0001);

				GeoPosition dmsMapAddress = dialog.getMapViewer().getAddressLocation();
				assertNotNull("Map address should be updated from DMS inputs", dmsMapAddress);
				assertEquals(46.5, dmsMapAddress.getLatitude(), 0.0001);
				assertEquals(7.75, dmsMapAddress.getLongitude(), 0.0001);
			} finally {
				dialog.dispose();
			}
		});
	}
}
