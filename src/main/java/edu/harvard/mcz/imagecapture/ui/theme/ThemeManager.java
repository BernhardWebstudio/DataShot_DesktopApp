package edu.harvard.mcz.imagecapture.ui.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages theme selection and Look and Feel switching (FlatLaf Light, Dark,
 * macOS themes, and Auto/System mode).
 */
public class ThemeManager {

	private static final Logger log = LoggerFactory.getLogger(ThemeManager.class);
	private static final String PREF_THEME = "ui.theme";
	private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);

	public enum ThemeMode {
		AUTO("Auto (System)"), LIGHT("FlatLaf Light"), DARK("FlatLaf Dark"), MAC_LIGHT("macOS Light"), MAC_DARK(
				"macOS Dark");

		private final String displayName;

		ThemeMode(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	private ThemeManager() {
		// Utility class
	}

	/**
	 * Initializes the Look and Feel based on user preferences and system settings.
	 * Must be called early in application startup before creating Swing components.
	 */
	public static void init() {
		if (SystemInfo.isMacOS) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("apple.awt.application.name", "DataShot");
			System.setProperty("apple.awt.application.appearance", "system");
		}

		ThemeMode savedMode = getSavedThemeMode();
		applyTheme(savedMode, false);

		// Global FlatLaf UI settings
		UIManager.put("TextComponent.selectAllOnFocusPolicy", "always");
		UIManager.put("JTextField.selectAllOnFocusPolicy", "always");
	}

	public static ThemeMode getSavedThemeMode() {
		String val = prefs.get(PREF_THEME, ThemeMode.AUTO.name());
		try {
			return ThemeMode.valueOf(val);
		} catch (IllegalArgumentException e) {
			return ThemeMode.AUTO;
		}
	}

	public static void saveThemeMode(ThemeMode mode) {
		prefs.put(PREF_THEME, mode.name());
	}

	/**
	 * Detects whether the host OS is currently in dark mode.
	 */
	public static boolean isSystemDark() {
		if (SystemInfo.isMacOS) {
			try {
				Process process = new ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle").start();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line = reader.readLine();
					return "Dark".equalsIgnoreCase(line != null ? line.trim() : "");
				}
			} catch (Exception ignored) {
				return false;
			}
		} else if (SystemInfo.isWindows) {
			try {
				Process process = new ProcessBuilder("reg", "query",
						"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "/v",
						"AppsUseLightTheme").start();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						if (line.contains("AppsUseLightTheme") && line.contains("0x0")) {
							return true;
						}
					}
				}
			} catch (Exception ignored) {
				return false;
			}
		} else if (SystemInfo.isLinux) {
			try {
				Process process = new ProcessBuilder("gsettings", "get", "org.gnome.desktop.interface", "color-scheme")
						.start();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line = reader.readLine();
					return line != null && line.contains("dark");
				}
			} catch (Exception ignored) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Applies the given theme mode.
	 *
	 * @param mode
	 *            the theme mode to apply
	 * @param save
	 *            whether to persist the user preference
	 */
	public static void applyTheme(ThemeMode mode, boolean save) {
		if (save) {
			saveThemeMode(mode);
		}
		try {
			switch (mode) {
				case AUTO :
					boolean dark = isSystemDark();
					if (SystemInfo.isMacOS) {
						if (dark) {
							FlatMacDarkLaf.setup();
						} else {
							FlatMacLightLaf.setup();
						}
					} else {
						if (dark) {
							FlatDarkLaf.setup();
						} else {
							FlatLightLaf.setup();
						}
					}
					break;
				case LIGHT :
					FlatLightLaf.setup();
					break;
				case DARK :
					FlatDarkLaf.setup();
					break;
				case MAC_LIGHT :
					FlatMacLightLaf.setup();
					break;
				case MAC_DARK :
					FlatMacDarkLaf.setup();
					break;
			}
			FlatLaf.updateUI();
		} catch (Exception e) {
			log.error("Failed to apply theme " + mode, e);
		}
	}

	/**
	 * Creates a JMenu with theme choices.
	 */
	public static JMenu createThemeMenu() {
		JMenu themeMenu = new JMenu("Theme");
		ButtonGroup group = new ButtonGroup();
		ThemeMode currentMode = getSavedThemeMode();

		for (ThemeMode mode : ThemeMode.values()) {
			if ((mode == ThemeMode.MAC_LIGHT || mode == ThemeMode.MAC_DARK) && !SystemInfo.isMacOS) {
				continue;
			}
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(mode.getDisplayName());
			item.setSelected(mode == currentMode);
			item.addActionListener(e -> applyTheme(mode, true));
			group.add(item);
			themeMenu.add(item);
		}

		return themeMenu;
	}
}
