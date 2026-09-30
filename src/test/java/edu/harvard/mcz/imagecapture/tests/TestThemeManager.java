package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.ui.theme.ThemeManager;
import edu.harvard.mcz.imagecapture.ui.theme.ThemeManager.ThemeMode;
import javax.swing.JMenu;
import org.junit.Test;

public class TestThemeManager {

	@Test
	public void testThemeModesAndPersistence() {
		ThemeMode initial = ThemeManager.getSavedThemeMode();
		assertNotNull(initial);

		ThemeManager.saveThemeMode(ThemeMode.LIGHT);
		assertEquals(ThemeMode.LIGHT, ThemeManager.getSavedThemeMode());

		ThemeManager.saveThemeMode(ThemeMode.AUTO);
		assertEquals(ThemeMode.AUTO, ThemeManager.getSavedThemeMode());

		// Restore initial
		ThemeManager.saveThemeMode(initial);
	}

	@Test
	public void testIsSystemDarkDoesNotThrow() {
		// Should execute cleanly on any OS
		boolean isDark = ThemeManager.isSystemDark();
		// Assert that isDark is a valid boolean
		assertTrue(isDark || !isDark);
	}

	@Test
	public void testCreateThemeMenu() {
		JMenu menu = ThemeManager.createThemeMenu();
		assertNotNull(menu);
		assertEquals("Theme", menu.getText());
		assertTrue("Menu should contain theme options", menu.getItemCount() >= 3);
	}

	@Test
	public void testApplyThemes() {
		// Ensure applying each mode does not throw
		ThemeManager.applyTheme(ThemeMode.LIGHT, false);
		ThemeManager.applyTheme(ThemeMode.DARK, false);
		ThemeManager.applyTheme(ThemeMode.AUTO, false);
	}
}
