package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import edu.harvard.mcz.imagecapture.ui.theme.ThemeManager;
import edu.harvard.mcz.imagecapture.ui.theme.ThemeManager.ThemeMode;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JTextField;
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
		assertFalse(ThemeManager.isDarkTheme());
		ThemeManager.applyTheme(ThemeMode.DARK, false);
		assertTrue(ThemeManager.isDarkTheme());
		ThemeManager.applyTheme(ThemeMode.AUTO, false);
	}

	@Test
	public void testThemeAwareColorsAndReadOnlyField() {
		ThemeManager.applyTheme(ThemeMode.LIGHT, false);
		Color lightFg = ThemeManager.getTextFieldForeground();
		Color lightBg = ThemeManager.getTextFieldBackground();
		Color lightEnt = ThemeManager.getEntFieldBackground();
		Color lightQc = ThemeManager.getQcFieldBackground();
		Color lightErr = ThemeManager.getErrorFieldBackground();
		assertTrue("Light mode foreground should be darker than background", luminance(lightFg) < luminance(lightBg));

		ThemeManager.applyTheme(ThemeMode.DARK, false);
		Color darkFg = ThemeManager.getTextFieldForeground();
		Color darkBg = ThemeManager.getTextFieldBackground();
		Color darkEnt = ThemeManager.getEntFieldBackground();
		Color darkQc = ThemeManager.getQcFieldBackground();
		Color darkErr = ThemeManager.getErrorFieldBackground();
		assertTrue("Dark mode foreground should be lighter than background", luminance(darkFg) > luminance(darkBg));
		assertNotEquals(lightEnt, darkEnt);
		assertNotEquals(lightQc, darkQc);
		assertNotEquals(lightErr, darkErr);
		assertTrue("Dark mode ent field should be darker than dark foreground", luminance(darkEnt) < luminance(darkFg));
		assertTrue("Dark mode qc field should be darker than dark foreground", luminance(darkQc) < luminance(darkFg));

		FormBindingContext<Specimen> ctx = new FormBindingContext<>(Specimen.class, false);
		JTextField readOnly = ctx.bindReadOnlyTextField("Barcode", Specimen::getBarcode);
		assertNotEquals("Read-only field must not hardcode black text in dark mode", Color.BLACK,
				readOnly.getForeground());

		JTextField dynamicField = new JTextField();
		ThemeManager.applyDynamicBackground(dynamicField, ThemeManager::getEntFieldBackground);
		assertEquals(darkEnt, dynamicField.getBackground());
		ThemeManager.applyTheme(ThemeMode.LIGHT, false);
		dynamicField.updateUI();
		assertEquals(lightEnt, dynamicField.getBackground());
	}

	private static double luminance(Color c) {
		return 0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue();
	}
}
