package edu.harvard.mcz.imagecapture.ui.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import edu.harvard.mcz.imagecapture.ui.DataShotColors;
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Supplier;
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

	/**
	 * Returns true if the active Look and Feel is a dark theme.
	 */
	public static boolean isDarkTheme() {
		return FlatLaf.isLafDark();
	}

	/**
	 * Returns the theme-appropriate default foreground color for text fields.
	 */
	public static Color getTextFieldForeground() {
		Color color = UIManager.getColor("TextField.foreground");
		if (color != null) {
			return color;
		}
		return isDarkTheme() ? Color.WHITE : Color.BLACK;
	}

	/**
	 * Returns the theme-appropriate default background color for text fields.
	 */
	public static Color getTextFieldBackground() {
		Color color = UIManager.getColor("TextField.background");
		if (color != null) {
			return color;
		}
		return isDarkTheme() ? new Color(70, 73, 75) : Color.WHITE;
	}

	/**
	 * Returns the theme-appropriate default foreground color for labels.
	 */
	public static Color getLabelForeground() {
		Color color = UIManager.getColor("Label.foreground");
		if (color != null) {
			return color;
		}
		return isDarkTheme() ? Color.WHITE : Color.BLACK;
	}

	/**
	 * Returns the theme-appropriate foreground color for warning/error text.
	 */
	public static Color getWarningForeground() {
		return isDarkTheme() ? new Color(255, 107, 107) : Color.RED;
	}

	/**
	 * Returns the theme-appropriate foreground color for informational status text.
	 */
	public static Color getInfoForeground() {
		return isDarkTheme() ? new Color(100, 181, 246) : new Color(0, 102, 204);
	}

	/**
	 * Returns the theme-appropriate background color for data-entry highlighted
	 * fields.
	 */
	public static Color getEntFieldBackground() {
		return isDarkTheme() ? new Color(72, 65, 42) : MainFrame.BG_COLOR_ENT_FIELD;
	}

	/**
	 * Returns the theme-appropriate background color for QC highlighted fields.
	 */
	public static Color getQcFieldBackground() {
		return isDarkTheme() ? new Color(55, 55, 88) : MainFrame.BG_COLOR_QC_FIELD;
	}

	/**
	 * Returns the theme-appropriate background color for fields with validation
	 * errors.
	 */
	public static Color getErrorFieldBackground() {
		return isDarkTheme() ? new Color(120, 40, 40) : MainFrame.BG_COLOR_ERROR;
	}

	/**
	 * Returns the theme-appropriate alternating row background color for tables.
	 */
	public static Color getAlternateTableRowBackground() {
		Color color = UIManager.getColor("Table.alternateRowColor");
		if (color != null) {
			return color;
		}
		return isDarkTheme() ? new Color(60, 63, 65) : DataShotColors.VERY_LIGHT_GRAY;
	}

	/**
	 * Returns the theme-appropriate row border color for tables.
	 */
	public static Color getTableRowBorderColor() {
		Color color = UIManager.getColor("Table.gridColor");
		if (color != null) {
			return color;
		}
		return isDarkTheme() ? new Color(80, 80, 80) : DataShotColors.VERY_DARK_GRAY;
	}

	/**
	 * Applies a theme-aware background color to a component and keeps it updated
	 * when the Look and Feel changes.
	 */
	public static void applyDynamicBackground(JComponent component, Supplier<Color> colorSupplier) {
		component.setBackground(colorSupplier.get());
		component.addPropertyChangeListener("UI", evt -> component.setBackground(colorSupplier.get()));
	}
}
