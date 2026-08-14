package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.entity.LatLong;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Tests for LatLong coordinate model, cloning, equality, and conversions.
 */
public class TestLatLongEntity extends TestCase {

	@Test
	public void testLatLongPropertiesAndCloning() {
		LatLong coord1 = new LatLong();
		coord1.setLatLongId(100L);
		coord1.setLatDeg(46);
		coord1.setLatMin(30);
		coord1.setLatDir("N");
		coord1.setLongDeg(8);
		coord1.setLongMin(15);
		coord1.setLongDir("E");
		coord1.setDatum("WGS84");
		coord1.setGeorefmethod("OpenStreetMap Nominatim");
		coord1.setDecLat(BigDecimal.valueOf(46.508333));
		coord1.setDecLong(BigDecimal.valueOf(8.254167));
		coord1.setMaxErrorDistance(50);
		coord1.setMaxErrorUnits("m");

		// Clone
		LatLong cloned = coord1.clone();
		assertEquals(coord1.getDatum(), cloned.getDatum());
		assertEquals(coord1.getGeorefmethod(), cloned.getGeorefmethod());
		assertEquals(coord1.getLatDeg(), cloned.getLatDeg());
		assertEquals(coord1.getLongDeg(), cloned.getLongDeg());
		assertEquals(coord1.getDecLat(), cloned.getDecLat());
		assertEquals(coord1.getDecLong(), cloned.getDecLong());

		// Equals & HashCode (by ID)
		LatLong coord2 = new LatLong();
		coord2.setLatLongId(100L);
		assertEquals(coord1, coord2);
		assertEquals(coord1.hashCode(), coord2.hashCode());

		// Equals by values when IDs are null
		LatLong coordNoId1 = new LatLong();
		coordNoId1.setDecLat(BigDecimal.valueOf(46.5));
		coordNoId1.setDecLong(BigDecimal.valueOf(8.5));
		coordNoId1.setDatum("WGS84");

		LatLong coordNoId2 = new LatLong();
		coordNoId2.setDecLat(BigDecimal.valueOf(46.5));
		coordNoId2.setDecLong(BigDecimal.valueOf(8.5));
		coordNoId2.setDatum("WGS84");

		assertEquals(coordNoId1, coordNoId2);
		assertEquals(coordNoId1.hashCode(), coordNoId2.hashCode());
	}
}
