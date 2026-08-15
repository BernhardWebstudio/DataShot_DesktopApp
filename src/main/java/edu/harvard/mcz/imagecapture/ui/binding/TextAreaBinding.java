package edu.harvard.mcz.imagecapture.ui.binding;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Two-way binding between a JTextArea and a String property on entity T.
 *
 * @param <T>
 *            the model entity type
 */
public class TextAreaBinding<T> implements Binding<T> {

	private final JTextArea textArea;
	private final String propertyName;
	private final Function<T, String> getter;
	private final BiConsumer<T, String> setter;
	private final FormBindingContext<T> context;

	public TextAreaBinding(JTextArea textArea, String propertyName, Function<T, String> getter,
			BiConsumer<T, String> setter, FormBindingContext<T> context) {
		this.textArea = textArea;
		this.propertyName = propertyName;
		this.getter = getter;
		this.setter = setter;
		this.context = context;

		this.textArea.getDocument().addDocumentListener(new DocumentListener() {
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

		this.textArea.addKeyListener(new KeyAdapter() {
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
			textArea.setText("");
			return;
		}
		String val = getter != null ? getter.apply(source) : null;
		textArea.setText(val != null ? val : "");
	}

	@Override
	public void writeTo(T target) {
		if (target != null && setter != null) {
			setter.accept(target, textArea.getText());
		}
	}

	@Override
	public JTextArea getComponent() {
		return textArea;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
}
