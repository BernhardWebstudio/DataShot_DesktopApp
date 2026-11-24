package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.SpecimenBrowser;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.Tracking;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class TestSpecimenBrowser extends TestCase {

	private final List<Specimen> createdSpecimens = new ArrayList<>();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		for (int i = 1; i <= 5; i++) {
			Specimen s = new Specimen();
			s.setBarcode("TEST_PAGINATION_" + i);
			s.setFamily("Nymphalidae");
			s.setGenus("Vanessa");
			s.setSpecificEpithet("cardui_" + i);
			s.setDateCreated(new Date());
			session.persist(s);
			createdSpecimens.add(s);
		}
		tx.commit();
		session.close();
	}

	@Override
	protected void tearDown() throws Exception {
		SpecimenLifeCycle sls = new SpecimenLifeCycle();
		for (Specimen s : createdSpecimens) {
			try {
				Specimen found = sls.findById(s.getSpecimenId());
				if (found != null) {
					sls.delete(found);
				}
			} catch (Exception e) {
				// ignore cleanup errors
			}
		}
		super.tearDown();
	}

	@Test
	public void testSpecimenBrowserDefaultPagination() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Skipping UI test in headless environment");
			return;
		}
		SpecimenBrowser browser = new SpecimenBrowser();
		assertEquals(SpecimenBrowser.DEFAULT_PAGE_SIZE, browser.getMaxResults());
		assertEquals(0, browser.getOffset());
		assertNotNull(browser.getJPanelPagination());
		assertNotNull(browser.getJButtonPrev());
		assertNotNull(browser.getJButtonNext());
		assertNotNull(browser.getJLabelPage());
		assertFalse("Prev button should be disabled on first page", browser.getJButtonPrev().isEnabled());
	}

	@Test
	public void testSpecimenBrowserPageNavigationAndJump() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Skipping UI test in headless environment");
			return;
		}
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("genus", "Vanessa");

		// Limit 2 per page
		SpecimenBrowser browser = new SpecimenBrowser(criteria, false, 2, 0);
		assertEquals(2, browser.getMaxResults());
		assertEquals(0, browser.getOffset());
		assertEquals(5, browser.getTotalCount());

		JTable table = browser.getJTable();
		assertNotNull(table);
		assertEquals(2, table.getRowCount());

		JButton first = browser.getJButtonFirst();
		JButton prev = browser.getJButtonPrev();
		JButton next = browser.getJButtonNext();
		JButton last = browser.getJButtonLast();
		JLabel pageLabel = browser.getJLabelPage();

		assertFalse(first.isEnabled());
		assertFalse(prev.isEnabled());
		assertTrue(next.isEnabled());
		assertTrue(last.isEnabled());
		assertTrue(pageLabel.getText().contains("Specimens 1-2 of 5 (Page 1 of 3)"));

		// Navigate to Page 2
		next.doClick();
		assertEquals(2, browser.getOffset());
		assertEquals(2, table.getRowCount());
		assertTrue(first.isEnabled());
		assertTrue(prev.isEnabled());
		assertTrue(next.isEnabled());
		assertTrue(last.isEnabled());
		assertTrue(pageLabel.getText().contains("Specimens 3-4 of 5 (Page 2 of 3)"));

		// Jump directly to Page 3 via Last button
		last.doClick();
		assertEquals(4, browser.getOffset());
		assertEquals(1, table.getRowCount());
		assertTrue(first.isEnabled());
		assertTrue(prev.isEnabled());
		assertFalse(next.isEnabled());
		assertFalse(last.isEnabled());
		assertTrue(pageLabel.getText().contains("Specimens 5-5 of 5 (Page 3 of 3)"));

		// Jump back to Page 1 via First button
		first.doClick();
		assertEquals(0, browser.getOffset());
		assertEquals(2, table.getRowCount());
		assertFalse(first.isEnabled());
		assertFalse(prev.isEnabled());
		assertTrue(next.isEnabled());

		// Jump to Page 2 using the text field input
		browser.getJTextFieldPage().setText("2");
		browser.getJButtonGo().doClick();
		assertEquals(2, browser.getOffset());
		assertEquals(2, table.getRowCount());
		assertTrue(pageLabel.getText().contains("Page 2 of 3"));
	}

	@Test
	public void testDatabaseSortingAcrossPagination() {
		SpecimenLifeCycle sls = new SpecimenLifeCycle();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("genus", "Vanessa");

		// Count total
		long count = sls.countBy(criteria, false);
		assertEquals(5, count);

		// Test sorting by specificEpithet DESC - first page should have cardui_5 and
		// cardui_4
		List<Specimen> descPage1 = sls.findBy(criteria, 2, 0, false, "specificEpithet", false);
		assertNotNull(descPage1);
		assertEquals(2, descPage1.size());
		assertEquals("cardui_5", descPage1.get(0).getSpecificEpithet());
		assertEquals("cardui_4", descPage1.get(1).getSpecificEpithet());

		// Next page DESC - should have cardui_3 and cardui_2
		List<Specimen> descPage2 = sls.findBy(criteria, 2, 2, false, "specificEpithet", false);
		assertNotNull(descPage2);
		assertEquals(2, descPage2.size());
		assertEquals("cardui_3", descPage2.get(0).getSpecificEpithet());
		assertEquals("cardui_2", descPage2.get(1).getSpecificEpithet());

		// Test sorting by specificEpithet ASC - first page should have cardui_1 and
		// cardui_2
		List<Specimen> ascPage1 = sls.findBy(criteria, 2, 0, false, "specificEpithet", true);
		assertNotNull(ascPage1);
		assertEquals(2, ascPage1.size());
		assertEquals("cardui_1", ascPage1.get(0).getSpecificEpithet());
		assertEquals("cardui_2", ascPage1.get(1).getSpecificEpithet());
	}

	@Test
	public void testFindSpecimensForTable() {
		SpecimenLifeCycle sls = new SpecimenLifeCycle();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("genus", "Vanessa");

		List<Specimen> results = sls.findSpecimensForTable(criteria, 2, 0, false, "specificEpithet", true);
		assertNotNull(results);
		assertEquals(2, results.size());
		assertEquals("cardui_1", results.get(0).getSpecificEpithet());
		assertEquals("cardui_2", results.get(1).getSpecificEpithet());
		assertNotNull(results.get(0).getBarcode());
	}

	@Test
	public void testDirectQueryFindBySingleStep() {
		SpecimenLifeCycle sls = new SpecimenLifeCycle();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("genus", "Vanessa");

		// Test finding first 2
		List<Specimen> page1 = sls.findBy(criteria, 2, 0, false);
		assertNotNull(page1);
		assertEquals(2, page1.size());

		// Test finding next 2
		List<Specimen> page2 = sls.findBy(criteria, 2, 2, false);
		assertNotNull(page2);
		assertEquals(2, page2.size());
		assertFalse(page1.get(0).getSpecimenId().equals(page2.get(0).getSpecimenId()));

		// Test finding last 1
		List<Specimen> page3 = sls.findBy(criteria, 2, 4, false);
		assertNotNull(page3);
		assertEquals(1, page3.size());
	}

	@Test
	public void testTableHeaderSortArrowPosition() {
		SpecimenListTableModel model = new SpecimenListTableModel(new ArrayList<>());
		// Sort by Country (COL_COUNTRY = 10)
		model.setSortInfo(SpecimenListTableModel.COL_COUNTRY, true);

		// When copy-paste is inactive, COL_COUNTRY is at view/table column index 9
		// (since COL_COPY is omitted)
		int countryColIndex = SpecimenDetailsViewPane.isCopyPasteActivated()
				? SpecimenListTableModel.COL_COUNTRY
				: SpecimenListTableModel.COL_COUNTRY - 1;
		int subspeciesColIndex = SpecimenDetailsViewPane.isCopyPasteActivated()
				? SpecimenListTableModel.COL_SUBSPECIFIC
				: SpecimenListTableModel.COL_SUBSPECIFIC - 1;

		assertEquals("Country ▲", model.getColumnName(countryColIndex));
		assertEquals("Subspecies", model.getColumnName(subspeciesColIndex));

		// Sort DESC
		model.setSortInfo(SpecimenListTableModel.COL_COUNTRY, false);
		assertEquals("Country ▼", model.getColumnName(countryColIndex));
		assertEquals("Subspecies", model.getColumnName(subspeciesColIndex));
	}

	@Test
	public void testTopFilterBarGlobalSearchAndPagination() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Skipping UI test in headless environment");
			return;
		}
		// Open browser with page size 2 over the 5 test specimens
		SpecimenBrowser browser = new SpecimenBrowser(null, true, 2, 0);
		assertTrue("Initial total count should be at least 5", browser.getTotalCount() >= 5);
		assertEquals(2, browser.getRowCount());
		assertEquals(0, browser.getOffset());

		// Test finding a specimen that would have been on page 2 (index 3:
		// TEST_PAGINATION_4)
		browser.getJTextFieldBarcode().setText("TEST_PAGINATION_4");
		browser.newFilter();

		// Should find across entire DB, not just page 1
		assertEquals(1, browser.getTotalCount());
		assertEquals(1, browser.getRowCount());
		SpecimenListTableModel model = (SpecimenListTableModel) browser.getJTable().getModel();
		assertEquals("TEST_PAGINATION_4", model.getSpecimenAt(0).getBarcode());

		// Clear filter and verify pagination is restored
		browser.getJTextFieldBarcode().setText("");
		browser.newFilter();
		assertTrue(browser.getTotalCount() >= 5);
		assertEquals(2, browser.getRowCount());

		// Test family filter
		browser.getJTextFieldFamily().setText("Nymphalidae");
		browser.newFilter();
		assertTrue(browser.getTotalCount() >= 5);
		assertEquals(2, browser.getRowCount());

		// Non-matching filter returns 0 results
		browser.getJTextFieldBarcode().setText("NON_EXISTENT_BARCODE_XYZ");
		browser.newFilter();
		assertEquals(0, browser.getTotalCount());
		assertEquals(0, browser.getRowCount());
	}

	@Test
	public void testWildcardSearchMatching() {
		SpecimenLifeCycle sls = new SpecimenLifeCycle();

		// 1. Prefix wildcard: card% matches all 5 (cardui_1 .. cardui_5)
		Map<String, Object> prefixCriteria = new HashMap<>();
		prefixCriteria.put("specificEpithet", "card%");
		assertEquals(5, sls.countBy(prefixCriteria, true));
		List<Specimen> prefixResults = sls.findSpecimensForTable(prefixCriteria, 10, 0, true, "barcode", true);
		assertEquals(5, prefixResults.size());

		// 2. Exact match without wildcard: cardui_1 matches only 1
		Map<String, Object> exactCriteria = new HashMap<>();
		exactCriteria.put("specificEpithet", "cardui_1");
		assertEquals(1, sls.countBy(exactCriteria, true));
		List<Specimen> exactResults = sls.findSpecimensForTable(exactCriteria, 10, 0, true, "barcode", true);
		assertEquals(1, exactResults.size());

		// 3. Substring without wildcard: "ui_1" should NOT match because no % is added
		// automatically
		Map<String, Object> subCriteria = new HashMap<>();
		subCriteria.put("specificEpithet", "ui_1");
		assertEquals(0, sls.countBy(subCriteria, true));
		List<Specimen> subResults = sls.findSpecimensForTable(subCriteria, 10, 0, true, "barcode", true);
		assertEquals(0, subResults.size());

		// 4. Substring WITH explicit wildcard: "%ui_1" should match 1
		Map<String, Object> explicitSubCriteria = new HashMap<>();
		explicitSubCriteria.put("specificEpithet", "%ui_1");
		assertEquals(1, sls.countBy(explicitSubCriteria, true));
		List<Specimen> explicitSubResults = sls.findSpecimensForTable(explicitSubCriteria, 10, 0, true, "barcode",
				true);
		assertEquals(1, explicitSubResults.size());
	}

	@Test
	public void testCollectionNumberSorting() {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String[] nrValues = {"10", "2", "100", "1.5", "ABC-1", "", null};
		for (int i = 0; i < nrValues.length; i++) {
			Specimen s = new Specimen();
			s.setBarcode("TEST_COL_NR_" + i);
			s.setFamily("Papilionidae");
			s.setGenus("Papilio");
			s.setSpecificEpithet("machaon_" + i);
			s.setDateCreated(new Date());
			session.persist(s);
			if (nrValues[i] != null) {
				Number n = new Number(s, nrValues[i], "Collection Number");
				session.persist(n);
			}
			// Add a distractor number with a different type
			Number other = new Number(s, "99999", "Collector Number");
			session.persist(other);
			createdSpecimens.add(s);
		}
		tx.commit();
		session.close();

		SpecimenLifeCycle sls = new SpecimenLifeCycle();

		// 1. Simple criteria path (findSpecimensForTable HQL)
		Map<String, Object> simpleCriteria = new HashMap<>();
		simpleCriteria.put("genus", "Papilio");

		List<Specimen> asc = sls.findSpecimensForTable(simpleCriteria, 10, 0, false, "numbers.number", true);
		assertEquals(7, asc.size());
		assertEquals("1.5", asc.get(0).getFirstNumberWithType("Collection Number"));
		assertEquals("2", asc.get(1).getFirstNumberWithType("Collection Number"));
		assertEquals("10", asc.get(2).getFirstNumberWithType("Collection Number"));
		assertEquals("100", asc.get(3).getFirstNumberWithType("Collection Number"));
		assertEquals("ABC-1", asc.get(4).getFirstNumberWithType("Collection Number"));
		// Empty and null come last
		String fifth = asc.get(5).getFirstNumberWithType("Collection Number");
		String sixth = asc.get(6).getFirstNumberWithType("Collection Number");
		assertTrue(fifth == null || fifth.isEmpty());
		assertTrue(sixth == null || sixth.isEmpty());

		List<Specimen> desc = sls.findSpecimensForTable(simpleCriteria, 10, 0, false, "numbers.number", false);
		assertEquals(7, desc.size());
		assertEquals("100", desc.get(0).getFirstNumberWithType("Collection Number"));
		assertEquals("10", desc.get(1).getFirstNumberWithType("Collection Number"));
		assertEquals("2", desc.get(2).getFirstNumberWithType("Collection Number"));
		assertEquals("1.5", desc.get(3).getFirstNumberWithType("Collection Number"));
		assertEquals("ABC-1", desc.get(4).getFirstNumberWithType("Collection Number"));

		// 2. Complex criteria path (findIdsByCriteria + findSpecimensForTableByIds)
		Map<String, Object> complexCriteria = new HashMap<>();
		complexCriteria.put("genus", "Papilio");
		complexCriteria.put("numbers.numberType", "Collection Number");

		List<Specimen> complexAscPage1 = sls.findSpecimensForTable(complexCriteria, 3, 0, false, "numbers.number",
				true);
		assertEquals(3, complexAscPage1.size());
		assertEquals("1.5", complexAscPage1.get(0).getFirstNumberWithType("Collection Number"));
		assertEquals("2", complexAscPage1.get(1).getFirstNumberWithType("Collection Number"));
		assertEquals("10", complexAscPage1.get(2).getFirstNumberWithType("Collection Number"));

		List<Specimen> complexDescPage1 = sls.findSpecimensForTable(complexCriteria, 3, 0, false, "numbers.number",
				false);
		assertEquals(3, complexDescPage1.size());
		assertEquals("100", complexDescPage1.get(0).getFirstNumberWithType("Collection Number"));
		assertEquals("10", complexDescPage1.get(1).getFirstNumberWithType("Collection Number"));
		assertEquals("2", complexDescPage1.get(2).getFirstNumberWithType("Collection Number"));
	}

	@Test
	public void testFindSpecimensForTableComplexCriteriaWithSort() {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		Specimen s1 = new Specimen();
		s1.setBarcode("TEST_TRACK_B");
		s1.setFamily("Nymphalidae");
		s1.setGenus("Morpho");
		s1.setDateCreated(new Date());
		session.persist(s1);
		createdSpecimens.add(s1);

		Specimen s2 = new Specimen();
		s2.setBarcode("TEST_TRACK_A");
		s2.setFamily("Nymphalidae");
		s2.setGenus("Morpho");
		s2.setDateCreated(new Date());
		session.persist(s2);
		createdSpecimens.add(s2);

		Tracking t1 = new Tracking(s1, "Test Tracker", "Taxon entered");
		Tracking t2 = new Tracking(s2, "Test Tracker", "Taxon entered");
		session.persist(t1);
		session.persist(t2);

		tx.commit();
		session.close();

		SpecimenLifeCycle sls = new SpecimenLifeCycle();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("trackings.user", "Test Tracker");

		List<Specimen> asc = sls.findSpecimensForTable(criteria, 10, 0, false, "barcode", true);
		assertEquals(2, asc.size());
		assertEquals("TEST_TRACK_A", asc.get(0).getBarcode());
		assertEquals("TEST_TRACK_B", asc.get(1).getBarcode());

		List<Specimen> desc = sls.findSpecimensForTable(criteria, 10, 0, false, "barcode", false);
		assertEquals(2, desc.size());
		assertEquals("TEST_TRACK_B", desc.get(0).getBarcode());
		assertEquals("TEST_TRACK_A", desc.get(1).getBarcode());

		List<Specimen> byId = sls.findSpecimensForTable(criteria, 10, 0, false, "specimenId", true);
		assertEquals(2, byId.size());
	}
}
