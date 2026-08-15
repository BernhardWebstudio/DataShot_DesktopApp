package edu.harvard.mcz.imagecapture.ui.binding;

import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.JComboBox;

/**
 * Two-way binding between a JComboBox<V> and a property on entity T.
 *
 * @param <T>
 *            the model entity type
 * @param <V>
 *            the item type of the combo box
 */
public class ComboBoxBinding<T, V> implements Binding<T> {

	private final JComboBox<V> comboBox;
	private final String propertyName;
	private final Function<T, V> getter;
	private final BiConsumer<T, V> setter;
	private final FormBindingContext<T> context;

	public ComboBoxBinding(JComboBox<V> comboBox, String propertyName, Function<T, V> getter, BiConsumer<T, V> setter,
			FormBindingContext<T> context) {
		this.comboBox = comboBox;
		this.propertyName = propertyName;
		this.getter = getter;
		this.setter = setter;
		this.context = context;

		this.comboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED && context != null && !context.isUpdating()) {
				context.markDirty();
			}
		});

		this.comboBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (context != null && !context.isUpdating()) {
					context.markDirty();
				}
			}
		});
	}

	@Override
	public void readFrom(T source) {
		if (source == null) {
			comboBox.setSelectedItem(null);
			return;
		}
		V val = getter != null ? getter.apply(source) : null;
		comboBox.setSelectedItem(val);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void writeTo(T target) {
		if (target != null && setter != null) {
			Object selected = comboBox.getSelectedItem();
			setter.accept(target, (V) selected);
		}
	}

	@Override
	public JComboBox<V> getComponent() {
		return comboBox;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
}
