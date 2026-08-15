package edu.harvard.mcz.imagecapture.ui.binding;

import edu.harvard.mcz.imagecapture.data.MetadataRetriever;
import java.awt.Color;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * Context that manages declarative form bindings between Swing UI components
 * and an underlying entity model of type T.
 *
 * <p>
 * Handles:
 * <ul>
 * <li>Two-way data synchronization (Model &rarr; UI via
 * {@link #readFrom(Object)}, UI &rarr; Model via {@link #writeTo(Object)})</li>
 * <li>Automatic tooltip assignment via
 * {@link MetadataRetriever#getFieldHelp(Class, String)}</li>
 * <li>Automatic input verifier attachment via
 * {@link MetadataRetriever#getInputVerifier(Class, String, JTextField)}</li>
 * <li>Dirty state tracking on all bound components without duplicate
 * listeners</li>
 * </ul>
 *
 * @param <T>
 *            the model entity type
 */
public class FormBindingContext<T> {

	private final Class<T> entityClass;
	private final boolean defaultEditable;
	private final List<Binding<T>> bindings = new ArrayList<>();
	private final Map<String, JComponent> componentMap = new HashMap<>();
	private Runnable onDirty;
	private boolean updating = false;

	public FormBindingContext(Class<T> entityClass, boolean defaultEditable, Runnable onDirty) {
		this.entityClass = entityClass;
		this.defaultEditable = defaultEditable;
		this.onDirty = onDirty;
	}

	public FormBindingContext(Class<T> entityClass, boolean defaultEditable) {
		this(entityClass, defaultEditable, null);
	}

	public void setOnDirty(Runnable onDirty) {
		this.onDirty = onDirty;
	}

	public boolean isUpdating() {
		return updating;
	}

	public void markDirty() {
		if (!updating && onDirty != null) {
			onDirty.run();
		}
	}

	/**
	 * Reads values from the given entity instance into all registered UI
	 * components. Dirty notifications are suspended during this operation.
	 *
	 * @param source
	 *            the entity to read from
	 */
	public void readFrom(T source) {
		boolean prevUpdating = updating;
		updating = true;
		try {
			for (Binding<T> binding : bindings) {
				binding.readFrom(source);
			}
		} finally {
			updating = prevUpdating;
		}
	}

	/**
	 * Writes values from all registered UI components into the target entity
	 * instance.
	 *
	 * @param target
	 *            the entity to update
	 */
	public void writeTo(T target) {
		for (Binding<T> binding : bindings) {
			binding.writeTo(target);
		}
	}

	public List<Binding<T>> getBindings() {
		return Collections.unmodifiableList(bindings);
	}

	@SuppressWarnings("unchecked")
	public <C extends JComponent> C getComponent(String propertyName) {
		return (C) componentMap.get(propertyName);
	}

	public <C extends JComponent> C registerBinding(Binding<T> binding) {
		bindings.add(binding);
		if (binding.getPropertyName() != null) {
			componentMap.put(binding.getPropertyName(), binding.getComponent());
		}
		return (C) binding.getComponent();
	}

	// ------------------------------------------------------------------------
	// JTextField bindings
	// ------------------------------------------------------------------------

	/**
	 * Binds a standard editable JTextField to a String property.
	 */
	public JTextField bindTextField(String propertyName, Function<T, String> getter, BiConsumer<T, String> setter) {
		return bindTextField(entityClass, propertyName, getter, setter, null);
	}

	/**
	 * Binds an editable JTextField to a String property with a customizer.
	 */
	public JTextField bindTextField(String propertyName, Function<T, String> getter, BiConsumer<T, String> setter,
			Consumer<JTextField> customizer) {
		return bindTextField(entityClass, propertyName, getter, setter, customizer);
	}

	/**
	 * Binds an editable JTextField to a String property with a specific metadata
	 * class and customizer.
	 */
	public JTextField bindTextField(Class<?> metadataClass, String propertyName, Function<T, String> getter,
			BiConsumer<T, String> setter, Consumer<JTextField> customizer) {
		JTextField field = new JTextField();
		field.setEditable(defaultEditable);
		if (metadataClass != null && propertyName != null) {
			try {
				InputVerifier verifier = MetadataRetriever.getInputVerifier(metadataClass, propertyName, field);
				if (verifier != null) {
					field.setInputVerifier(verifier);
				}
			} catch (Exception ignored) {
			}
			try {
				String help = MetadataRetriever.getFieldHelp(metadataClass, propertyName);
				if (help != null && !help.isEmpty()) {
					field.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		if (customizer != null) {
			customizer.accept(field);
		}
		TextBinding<T, String> binding = new TextBinding<>(field, propertyName, getter, setter, Function.identity(),
				Function.identity(), this);
		return registerBinding(binding);
	}

	/**
	 * Binds a read-only JTextField to a String property.
	 */
	public JTextField bindReadOnlyTextField(String propertyName, Function<T, String> getter) {
		return bindReadOnlyTextField(entityClass, propertyName, getter, null);
	}

	/**
	 * Binds a read-only JTextField to a String property with customizer.
	 */
	public JTextField bindReadOnlyTextField(Class<?> metadataClass, String propertyName, Function<T, String> getter,
			Consumer<JTextField> customizer) {
		JTextField field = new JTextField();
		field.setEditable(false);
		field.setForeground(Color.BLACK);
		if (metadataClass != null && propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(metadataClass, propertyName);
				if (help != null && !help.isEmpty()) {
					field.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		if (customizer != null) {
			customizer.accept(field);
		}
		TextBinding<T, String> binding = new TextBinding<>(field, propertyName, getter, null, Function.identity(),
				Function.identity(), this);
		return registerBinding(binding);
	}

	/**
	 * Binds a JTextField to a Long number property with parse error tolerance.
	 */
	public JTextField bindLongField(String propertyName, Function<T, Long> getter, BiConsumer<T, Long> setter,
			Consumer<JTextField> customizer) {
		JTextField field = new JTextField();
		field.setEditable(defaultEditable);
		if (propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(entityClass, propertyName);
				if (help != null && !help.isEmpty()) {
					field.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		if (customizer != null) {
			customizer.accept(field);
		}
		TextBinding<T, Long> binding = new TextBinding<>(field, propertyName, getter, setter,
				val -> val != null ? Long.toString(val) : "", text -> {
					if (text == null || text.trim().isEmpty()) {
						return null;
					}
					try {
						return Long.parseLong(text.trim());
					} catch (NumberFormatException e) {
						return null;
					}
				}, this);
		return registerBinding(binding);
	}

	// ------------------------------------------------------------------------
	// JTextArea bindings
	// ------------------------------------------------------------------------

	/**
	 * Binds a JTextArea to a String property.
	 */
	public JTextArea bindTextArea(String propertyName, Function<T, String> getter, BiConsumer<T, String> setter,
			int rows) {
		JTextArea textArea = new JTextArea();
		textArea.setRows(rows);
		textArea.setEditable(defaultEditable);
		if (propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(entityClass, propertyName);
				if (help != null && !help.isEmpty()) {
					textArea.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		TextAreaBinding<T> binding = new TextAreaBinding<>(textArea, propertyName, getter, setter, this);
		return registerBinding(binding);
	}

	// ------------------------------------------------------------------------
	// JCheckBox bindings
	// ------------------------------------------------------------------------

	/**
	 * Binds a JCheckBox to a Boolean property.
	 */
	public JCheckBox bindCheckBox(String propertyName, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setEnabled(defaultEditable);
		if (propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(entityClass, propertyName);
				if (help != null && !help.isEmpty()) {
					checkBox.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		CheckBoxBinding<T> binding = new CheckBoxBinding<>(checkBox, propertyName, getter, setter, this);
		return registerBinding(binding);
	}

	// ------------------------------------------------------------------------
	// JComboBox bindings
	// ------------------------------------------------------------------------

	/**
	 * Binds a JComboBox with static string items.
	 */
	public JComboBox<String> bindComboBox(String propertyName, String[] items, Function<T, String> getter,
			BiConsumer<T, String> setter) {
		return bindComboBox(entityClass, propertyName, items, getter, setter, null);
	}

	/**
	 * Binds a JComboBox with static string items and customizer.
	 */
	public JComboBox<String> bindComboBox(String propertyName, String[] items, Function<T, String> getter,
			BiConsumer<T, String> setter, Consumer<JComboBox<String>> customizer) {
		return bindComboBox(entityClass, propertyName, items, getter, setter, customizer);
	}

	/**
	 * Binds a JComboBox with specific metadata class, static string items, and
	 * customizer.
	 */
	public JComboBox<String> bindComboBox(Class<?> metadataClass, String propertyName, String[] items,
			Function<T, String> getter, BiConsumer<T, String> setter, Consumer<JComboBox<String>> customizer) {
		JComboBox<String> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(items));
		comboBox.setEditable(defaultEditable);
		if (metadataClass != null && propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(metadataClass, propertyName);
				if (help != null && !help.isEmpty()) {
					comboBox.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		AutoCompleteDecorator.decorate(comboBox);
		if (customizer != null) {
			customizer.accept(comboBox);
		}
		ComboBoxBinding<T, String> binding = new ComboBoxBinding<>(comboBox, propertyName, getter, setter, this);
		return registerBinding(binding);
	}

	/**
	 * Binds a JComboBox with items loaded asynchronously in background thread.
	 */
	public JComboBox<String> bindComboBox(String propertyName, Supplier<String[]> asyncSupplier,
			Function<T, String> getter, BiConsumer<T, String> setter) {
		return bindComboBox(entityClass, propertyName, asyncSupplier, getter, setter, true, null);
	}

	/**
	 * Binds a JComboBox with items loaded asynchronously in background thread with
	 * customizer.
	 */
	public JComboBox<String> bindComboBox(String propertyName, Supplier<String[]> asyncSupplier,
			Function<T, String> getter, BiConsumer<T, String> setter, boolean ensureEmptyOption,
			Consumer<JComboBox<String>> customizer) {
		return bindComboBox(entityClass, propertyName, asyncSupplier, getter, setter, ensureEmptyOption, customizer);
	}

	/**
	 * Binds a JComboBox with items loaded asynchronously in background thread.
	 */
	public JComboBox<String> bindComboBox(Class<?> metadataClass, String propertyName, Supplier<String[]> asyncSupplier,
			Function<T, String> getter, BiConsumer<T, String> setter, boolean ensureEmptyOption,
			Consumer<JComboBox<String>> customizer) {
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<>());
		comboBox.setEditable(defaultEditable);
		if (metadataClass != null && propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(metadataClass, propertyName);
				if (help != null && !help.isEmpty()) {
					comboBox.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}

		(new Thread(() -> {
			String[] items = asyncSupplier.get();
			SwingUtilities.invokeLater(() -> {
				boolean prevUpdating = updating;
				updating = true;
				try {
					Object currentSelected = comboBox.getSelectedItem();
					comboBox.setModel(new DefaultComboBoxModel<>(items));
					if (ensureEmptyOption && !Arrays.stream(items).anyMatch(""::equals)) {
						comboBox.addItem("");
					}
					if (currentSelected != null) {
						comboBox.setSelectedItem(currentSelected);
					}
				} finally {
					updating = prevUpdating;
				}
			});
		})).start();

		AutoCompleteDecorator.decorate(comboBox);
		if (customizer != null) {
			customizer.accept(comboBox);
		}
		ComboBoxBinding<T, String> binding = new ComboBoxBinding<>(comboBox, propertyName, getter, setter, this);
		return registerBinding(binding);
	}

	/**
	 * Binds a JComboBox using a custom ComboBoxModel.
	 */
	public JComboBox<String> bindComboBox(String propertyName, ComboBoxModel<String> model, Function<T, String> getter,
			BiConsumer<T, String> setter, Consumer<JComboBox<String>> customizer) {
		JComboBox<String> comboBox = new JComboBox<>(model);
		comboBox.setEditable(defaultEditable);
		if (propertyName != null) {
			try {
				String help = MetadataRetriever.getFieldHelp(entityClass, propertyName);
				if (help != null && !help.isEmpty()) {
					comboBox.setToolTipText(help);
				}
			} catch (Exception ignored) {
			}
		}
		AutoCompleteDecorator.decorate(comboBox);
		if (customizer != null) {
			customizer.accept(comboBox);
		}
		ComboBoxBinding<T, String> binding = new ComboBoxBinding<>(comboBox, propertyName, getter, setter, this);
		return registerBinding(binding);
	}
}
