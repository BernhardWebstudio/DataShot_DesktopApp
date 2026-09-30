package edu.harvard.mcz.imagecapture.ui.component;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

/**
 * Pure Java Swing auto-complete utility that provides search-as-you-type
 * functionality for {@link JComboBox} and {@link JTextComponent} without any
 * dependency on SwingX.
 *
 * <p>
 * Unlike SwingX's legacy {@code AutoCompleteDecorator} (which intercepts
 * Backspace with {@code NonStrictBackspaceAction} and prevents deleting
 * matching text), this implementation uses a standard {@link DocumentFilter} so
 * that Backspace and Delete work as expected.
 */
public class AutoCompleteHelper {

	private AutoCompleteHelper() {
		// Utility class
	}

	/**
	 * Decorates an editable JComboBox with auto-complete functionality.
	 *
	 * @param comboBox
	 *            the combo box to decorate
	 */
	public static void decorate(JComboBox<?> comboBox) {
		decorate(comboBox, Objects::toString);
	}

	/**
	 * Decorates an editable JComboBox with auto-complete functionality using a
	 * custom string converter.
	 *
	 * @param comboBox
	 *            the combo box to decorate
	 * @param converter
	 *            function to convert model items to display strings
	 */
	public static void decorate(JComboBox<?> comboBox, Function<Object, String> converter) {
		if (comboBox == null) {
			return;
		}
		comboBox.setEditable(true);
		installOnComboBox(comboBox, converter);
		comboBox.addPropertyChangeListener("editor", evt -> {
			installOnComboBox(comboBox, converter);
		});
	}

	/**
	 * Decorates a JTextComponent with auto-complete from a static list of items.
	 *
	 * @param textComponent
	 *            the text component
	 * @param items
	 *            the completion items
	 * @param strictMatching
	 *            whether to enforce strict matching
	 */
	public static void decorate(JTextComponent textComponent, List<?> items, boolean strictMatching) {
		decorate(textComponent, items, strictMatching, Objects::toString);
	}

	/**
	 * Decorates a JTextComponent with auto-complete from a static list of items and
	 * custom converter.
	 *
	 * @param textComponent
	 *            the text component
	 * @param items
	 *            the completion items
	 * @param strictMatching
	 *            whether to enforce strict matching
	 * @param converter
	 *            function to convert items to string
	 */
	public static void decorate(JTextComponent textComponent, List<?> items, boolean strictMatching,
			Function<Object, String> converter) {
		if (textComponent == null || items == null) {
			return;
		}
		installFilter(textComponent, () -> items, converter, null);
	}

	public static void fixBackspace(JComboBox<?> comboBox) {
		if (comboBox != null && comboBox.getEditor() != null
				&& comboBox.getEditor().getEditorComponent() instanceof JTextComponent tc) {
			fixBackspace(tc);
		}
	}

	public static void fixBackspace(JTextComponent textComponent) {
		if (textComponent == null) {
			return;
		}
		KeyStroke backspace = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
		KeyStroke shiftBackspace = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, InputEvent.SHIFT_DOWN_MASK);
		textComponent.getInputMap().put(backspace, DefaultEditorKit.deletePrevCharAction);
		textComponent.getInputMap().put(shiftBackspace, DefaultEditorKit.deletePrevCharAction);
	}

	private static void installOnComboBox(JComboBox<?> comboBox, Function<Object, String> converter) {
		if (comboBox.getEditor() == null || !(comboBox.getEditor().getEditorComponent() instanceof JTextComponent)) {
			return;
		}
		JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		fixBackspace(editor);
		editor.putClientProperty(FlatClientProperties.SELECT_ALL_ON_FOCUS_POLICY,
				FlatClientProperties.SELECT_ALL_ON_FOCUS_POLICY_ALWAYS);

		if (editor.getClientProperty("AutoCompleteHelper.focusListener") == null) {
			FocusAdapter focusAdapter = new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					String text = editor.getText();
					if (comboBox.getSelectedItem() == null || !comboBox.getSelectedItem().toString().equals(text)) {
						comboBox.setSelectedItem(text);
					}
				}
			};
			editor.addFocusListener(focusAdapter);
			editor.putClientProperty("AutoCompleteHelper.focusListener", focusAdapter);
		}

		installFilter(editor, () -> {
			ComboBoxModel<?> model = comboBox.getModel();
			if (model == null) {
				return Collections.emptyList();
			}
			int size = model.getSize();
			Object[] items = new Object[size];
			for (int i = 0; i < size; i++) {
				items[i] = model.getElementAt(i);
			}
			return List.of(items);
		}, converter, comboBox);
	}

	private static void installFilter(JTextComponent editor, java.util.function.Supplier<List<?>> itemsSupplier,
			Function<Object, String> converter, JComboBox<?> comboBox) {
		if (editor.getDocument() instanceof AbstractDocument doc) {
			AutoCompleteDocumentFilter filter = new AutoCompleteDocumentFilter(editor, itemsSupplier, converter,
					comboBox);
			doc.setDocumentFilter(filter);
		}
	}

	private static class AutoCompleteDocumentFilter extends DocumentFilter {
		private final JTextComponent editor;
		private final java.util.function.Supplier<List<?>> itemsSupplier;
		private final Function<Object, String> converter;
		private final JComboBox<?> comboBox;
		private boolean adjusting = false;

		public AutoCompleteDocumentFilter(JTextComponent editor, java.util.function.Supplier<List<?>> itemsSupplier,
				Function<Object, String> converter, JComboBox<?> comboBox) {
			this.editor = editor;
			this.itemsSupplier = itemsSupplier;
			this.converter = converter != null ? converter : Objects::toString;
			this.comboBox = comboBox;
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			if (adjusting) {
				super.remove(fb, offset, length);
				return;
			}
			super.remove(fb, offset, length);
			if (comboBox != null) {
				String remaining = fb.getDocument().getText(0, fb.getDocument().getLength());
				adjusting = true;
				try {
					comboBox.setSelectedItem(remaining);
				} finally {
					adjusting = false;
				}
			}
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			if (adjusting || string == null || string.isEmpty()) {
				super.insertString(fb, offset, string, attr);
				return;
			}
			replace(fb, offset, 0, string, attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			if (adjusting) {
				super.replace(fb, offset, length, text, attrs);
				return;
			}
			if (text == null || text.isEmpty()) {
				// Deleting text / selection (e.g. Backspace or Delete)
				super.replace(fb, offset, length, text, attrs);
				if (comboBox != null) {
					String remaining = fb.getDocument().getText(0, fb.getDocument().getLength());
					adjusting = true;
					try {
						comboBox.setSelectedItem(remaining);
					} finally {
						adjusting = false;
					}
				}
				return;
			}

			// User is typing / inserting text
			String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());

			// If text being set matches existing text entirely (e.g. setText/setItem),
			// avoid autocomplete expansion
			if (offset == 0 && length == currentText.length() && text.length() > 1 && !editor.isFocusOwner()) {
				super.replace(fb, offset, length, text, attrs);
				return;
			}

			// If editing inside existing text (not at the end of the text), perform
			// standard replace without autocomplete expansion
			boolean isAtEnd = (offset + length == currentText.length());
			if (!isAtEnd) {
				super.replace(fb, offset, length, text, attrs);
				if (comboBox != null) {
					String updated = fb.getDocument().getText(0, fb.getDocument().getLength());
					adjusting = true;
					try {
						comboBox.setSelectedItem(updated);
					} finally {
						adjusting = false;
					}
				}
				return;
			}

			String prefix = currentText.substring(0, offset) + text;

			String matchedString = null;
			Object matchedItem = null;
			List<?> items = itemsSupplier.get();
			if (items != null && !prefix.isEmpty()) {
				// Check for exact case match first
				for (Object item : items) {
					if (item != null) {
						String itemStr = converter.apply(item);
						if (itemStr != null && itemStr.startsWith(prefix)) {
							matchedString = itemStr;
							matchedItem = item;
							break;
						}
					}
				}
				// If no exact case match, fallback to case-insensitive prefix match
				if (matchedString == null) {
					for (Object item : items) {
						if (item != null) {
							String itemStr = converter.apply(item);
							if (itemStr != null && itemStr.toLowerCase().startsWith(prefix.toLowerCase())) {
								matchedString = itemStr;
								matchedItem = item;
								break;
							}
						}
					}
				}
			}

			if (matchedString != null) {
				// Construct completion: preserve user's explicit casing (e.g. uppercase where
				// DB has lowercase)
				StringBuilder sb = new StringBuilder();
				int minLen = Math.min(prefix.length(), matchedString.length());
				for (int i = 0; i < minLen; i++) {
					char pChar = prefix.charAt(i);
					char mChar = matchedString.charAt(i);
					if (Character.isUpperCase(pChar) && Character.isLowerCase(mChar)) {
						sb.append(pChar);
					} else {
						sb.append(mChar);
					}
				}
				if (matchedString.length() > minLen) {
					sb.append(matchedString.substring(minLen));
				}
				String completion = sb.toString();

				adjusting = true;
				try {
					fb.replace(0, currentText.length(), completion, attrs);
					if (comboBox != null) {
						if (completion.equals(matchedString)) {
							comboBox.setSelectedItem(matchedItem);
						} else {
							comboBox.setSelectedItem(completion);
						}
					}
				} finally {
					adjusting = false;
				}
				final int highlightStart = prefix.length();
				final int highlightEnd = completion.length();
				Runnable setSelection = () -> {
					if (editor.getDocument().getLength() >= highlightEnd) {
						editor.setCaretPosition(highlightEnd);
						editor.moveCaretPosition(highlightStart);
					}
				};
				if (SwingUtilities.isEventDispatchThread()) {
					setSelection.run();
				}
				SwingUtilities.invokeLater(setSelection);
			} else {
				super.replace(fb, offset, length, text, attrs);
				if (comboBox != null) {
					String updated = fb.getDocument().getText(0, fb.getDocument().getLength());
					adjusting = true;
					try {
						comboBox.setSelectedItem(updated);
					} finally {
						adjusting = false;
					}
				}
			}
		}
	}
}
