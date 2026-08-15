package edu.harvard.mcz.imagecapture.ui.binding;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Two-way or one-way binding between a JTextField and a property on entity T.
 *
 * @param <T>
 *            the model entity type
 * @param <V>
 *            the property value type
 */
public class TextBinding<T, V> implements Binding<T> {

	private final JTextField field;
	private final String propertyName;
	private final Function<T, V> getter;
	private final BiConsumer<T, V> setter;
	private final Function<V, String> toText;
	private final Function<String, V> fromText;
	private final FormBindingContext<T> context;

	public TextBinding(JTextField field, String propertyName, Function<T, V> getter, BiConsumer<T, V> setter,
			Function<V, String> toText, Function<String, V> fromText, FormBindingContext<T> context) {
		this.field = field;
		this.propertyName = propertyName;
		this.getter = getter;
		this.setter = setter;
		this.toText = toText != null ? toText : (v -> v != null ? v.toString() : "");
		this.fromText = fromText;
		this.context = context;

		this.field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyDirty();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				notifyDirty();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				notifyDirty();
			}
		});

		this.field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				notifyDirty();
			}
		});
	}

	private void notifyDirty() {
		if (context != null && !context.isUpdating()) {
			context.markDirty();
		}
	}

	@Override
	public void readFrom(T source) {
		if (source == null) {
			field.setText("");
			return;
		}
		V value = getter != null ? getter.apply(source) : null;
		String text = toText.apply(value);
		field.setText(text != null ? text : "");
	}

	@Override
	public void writeTo(T target) {
		if (target != null && setter != null && fromText != null) {
			String text = field.getText();
			V val = fromText.apply(text);
			setter.accept(target, val);
		}
	}

	@Override
	public JTextField getComponent() {
		return field;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
}
