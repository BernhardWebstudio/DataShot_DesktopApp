package edu.harvard.mcz.imagecapture.ui.binding;

import javax.swing.JComponent;

/**
 * Represents a binding between a UI component and a model property on an entity
 * of type T.
 *
 * @param <T>
 *            the model entity type
 */
public interface Binding<T> {

	/**
	 * Reads the value from the source entity and updates the UI component.
	 *
	 * @param source
	 *            the model entity to read from
	 */
	void readFrom(T source);

	/**
	 * Writes the value from the UI component into the target entity.
	 *
	 * @param target
	 *            the model entity to update
	 */
	void writeTo(T target);

	/**
	 * Gets the underlying UI component.
	 *
	 * @return the UI component
	 */
	JComponent getComponent();

	/**
	 * Gets the property name associated with this binding in MetadataRetriever.
	 *
	 * @return the property name, or null if custom/unnamed
	 */
	String getPropertyName();
}
