package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import javax.swing.JMenu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for MainFrame menu bar states (not logged in vs logged in) and menu UI
 * synchronization.
 */
public class TestMainFrameMenuState {

	private MainFrame mainFrame;

	@Before
	public void setUp() {
		try {
			org.junit.Assume.assumeFalse("Skipping MainFrame UI test in headless environment",
					java.awt.GraphicsEnvironment.isHeadless());
			Singleton.getSingletonInstance().unsetCurrentUser();
			mainFrame = new MainFrame();
			Singleton.getSingletonInstance().setMainFrame(mainFrame);
		} catch (java.awt.HeadlessException e) {
			org.junit.Assume.assumeNoException("Skipping MainFrame UI test due to HeadlessException", e);
		}
	}

	@After
	public void tearDown() {
		Singleton.getSingletonInstance().unsetCurrentUser();
		if (mainFrame != null) {
			mainFrame.dispose();
		}
	}

	@Test
	public void testMenusDisabledWhenNotLoggedIn() {
		// When in STATE_INIT or STATE_RESET without user logged in
		mainFrame.setState(MainFrame.STATE_RESET);

		JMenu editMenu = mainFrame.getMenuByName("Edit");
		JMenu actionMenu = mainFrame.getMenuByName("Action");
		JMenu dataMenu = mainFrame.getMenuByName("Data");
		JMenu qcMenu = mainFrame.getMenuByName("QualityControl");
		JMenu configMenu = mainFrame.getMenuByName("Configuration");

		assertNotNull(editMenu);
		assertNotNull(actionMenu);
		assertNotNull(dataMenu);
		assertNotNull(qcMenu);
		assertNotNull(configMenu);

		assertFalse("Edit menu should be disabled when not logged in", editMenu.isEnabled());
		assertFalse("Action menu should be disabled when not logged in", actionMenu.isEnabled());
		assertFalse("Data menu should be disabled when not logged in", dataMenu.isEnabled());
		assertFalse("Quality Control menu should be disabled when not logged in", qcMenu.isEnabled());
		assertFalse("Config menu should be disabled when not logged in", configMenu.isEnabled());
	}

	@Test
	public void testMenusEnabledAfterLoginWithDataEntryRole() {
		// Start in logged-out state
		mainFrame.setState(MainFrame.STATE_RESET);

		// Log in as Data entry user
		Users dataEntryUser = new Users("testde", "Data Entry Test", Users.ROLE_DATAENTRY);
		Singleton.getSingletonInstance().setCurrentUser(dataEntryUser);
		mainFrame.setState(MainFrame.STATE_RUNNING);

		JMenu editMenu = mainFrame.getMenuByName("Edit");
		JMenu dataMenu = mainFrame.getMenuByName("Data");
		JMenu qcMenu = mainFrame.getMenuByName("QualityControl");
		JMenu configMenu = mainFrame.getMenuByName("Configuration");
		JMenu actionMenu = mainFrame.getMenuByName("Action");

		assertTrue("Edit menu should be enabled after login", editMenu.isEnabled());
		assertTrue("Data menu should be enabled for Data Entry user", dataMenu.isEnabled());
		assertTrue("Quality Control menu should be enabled for Data Entry user", qcMenu.isEnabled());
		assertTrue("Config menu should be enabled for authenticated user", configMenu.isEnabled());
		assertFalse("Action menu should be disabled for basic Data Entry user", actionMenu.isEnabled());
	}

	@Test
	public void testMenusEnabledAfterLoginWithFullAccessRole() {
		// Start in logged-out state
		mainFrame.setState(MainFrame.STATE_RESET);

		// Log in as Full access user
		Users fullUser = new Users("testadmin", "Admin Test", Users.ROLE_ADMINISTRATOR);
		Singleton.getSingletonInstance().setCurrentUser(fullUser);
		mainFrame.setState(MainFrame.STATE_RUNNING);

		JMenu editMenu = mainFrame.getMenuByName("Edit");
		JMenu dataMenu = mainFrame.getMenuByName("Data");
		JMenu qcMenu = mainFrame.getMenuByName("QualityControl");
		JMenu configMenu = mainFrame.getMenuByName("Configuration");
		JMenu actionMenu = mainFrame.getMenuByName("Action");

		assertTrue("Edit menu should be enabled after login", editMenu.isEnabled());
		assertTrue("Data menu should be enabled for Admin", dataMenu.isEnabled());
		assertTrue("Quality Control menu should be enabled for Admin", qcMenu.isEnabled());
		assertTrue("Config menu should be enabled for Admin", configMenu.isEnabled());
		assertTrue("Action menu should be enabled for Admin", actionMenu.isEnabled());
	}

	@Test
	public void testUpdateMenuBarUIExecutesCleanly() {
		// Should execute cleanly on EDT or background thread without errors
		mainFrame.updateMenuBarUI();
		assertNotNull(mainFrame.getJMenuBar());
	}
}
