package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.ui.dialog.SearchDialog;
import java.awt.GraphicsEnvironment;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import junit.framework.TestCase;

public class TestSearchDialog extends TestCase {

	public TestSearchDialog(String name) {
		super(name);
	}

	public void testSearchDialogInitialization() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Skipping UI test in headless environment");
			return;
		}
		SearchDialog dialog = new SearchDialog(null);
		assertNotNull(dialog);
		assertEquals("Search For Specimens", dialog.getTitle());
		assertTrue(dialog.getWidth() >= 450);
		assertTrue(dialog.getHeight() >= 400);

		// Find the scroll pane in content pane
		JScrollPane scrollPane = null;
		for (java.awt.Component c : dialog.getContentPane().getComponents()) {
			if (c instanceof JScrollPane) {
				scrollPane = (JScrollPane) c;
				break;
			}
		}
		assertNotNull("SearchDialog should have a JScrollPane", scrollPane);
		assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER, scrollPane.getHorizontalScrollBarPolicy());

		assertNotNull(dialog.getJButtonSearch());
		assertNotNull(dialog.getLimitJIntegerField());
		assertEquals(1000, dialog.getLimitJIntegerField().getIntValue());

		dialog.dispose();
	}
}
