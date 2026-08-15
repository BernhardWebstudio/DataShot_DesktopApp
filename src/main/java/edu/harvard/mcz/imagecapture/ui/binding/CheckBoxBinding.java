package edu.harvard.mcz.imagecapture.ui.binding;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.JCheckBox;

/**
 * Two-way binding between a JCheckBox and a Boolean property on entity T.
 *
 * @param <T>
 *            the model entity type
 */
public class CheckBoxBinding<T> implements Binding<T> {

	private final JCheckBox checkBox;
	private final String propertyName;
	private final Function<T, Boolean> getter;
	private final BiConsumer<T, Boolean> setter;
	private final FormBindingContext<T> context;

	public CheckBoxBinding(JCheckBox checkBox, String propertyName, Function<T, Boolean> getter,
			BiConsumer<T, Boolean> setter, FormBindingContext<T> context) {
		this.checkBox = checkBox;
		this.propertyName = propertyName;
		this.getter = getter;
		this.setter = setter;
		this.context = context;

		this.checkBox.addItemListener(e -> {
			if (context != null && !context.isUpdating()) {
				context.markDirty();
			}
		});

		this.checkBox.addKeyListener(new KeyAdapter() {
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
			checkBox.setSelected(false);
			return;
		}
		Boolean val = getter != null ? getter.apply(source) : null;
		checkBox.setSelected(Boolean.TRUE.equals(val));
	}

	@Override
	public void writeTo(T target) {
		if (target != null && setter != null) {
			setter.accept(target, checkBox.isSelected());
		}
	}

	@Override
	public JCheckBox getComponent() {
		return checkBox;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
}
