package edu.harvard.mcz.imagecapture.ui.field;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Java swing bean to represent an integer field in a GUI. Features admissible values.
 */
public class JIntegerField extends JTextField implements ActionListener {

    private static final long serialVersionUID = -138039675088007707L;

    Number value = new Integer(0);

    int[] admissibleValues = null;
    int lowerBound = -Integer.MAX_VALUE;
    int upperBound = Integer.MAX_VALUE;

    public JIntegerField() {
        super();
        this.addActionListener(this);
    }

    public JIntegerField(int value, String format, ActionListener actionListener) {
        super();
        this.addActionListener(actionListener);
        this.addActionListener(this);
        setValue(value);
    }

    public JIntegerField(int value, ActionListener actionListener) {
        super();
        this.addActionListener(actionListener);
        this.addActionListener(this);
        setValue(value);
    }

    public JIntegerField(String format) {
        super(format);
        this.addActionListener(this);
        setValue(0);
    }

    public Number getValue() {
        parseField();
        updateData();
        return value;
    }

    public void setValue(int value) {
        this.setValue(new Integer(value));
    }

    public void setValue(Integer value) {
        this.value = value;
        updateData();
    }

    public void setRange(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        parseField();
        updateData();
    }

    public void setAdmissibleValues(int[] admissibleValues) {
        this.admissibleValues = admissibleValues;
    }

    public void add(int increment) {
        setValue(getValue().intValue() + increment);
    }

    public void addToAdmissibleValueIndex(int increment) {
        int index = getAdmissibleValueIndex();
        if (index < 0) return;    // Admissible values not set

        index = Math.max(0, Math.min(index + increment, admissibleValues.length - 1));
        value = admissibleValues[index];

        updateData();
    }

    public int getIntValue() {
        return getValue().intValue();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        parseField();
        updateData();
    }

    private void parseField() {
        // Parse field
        try {
            setValue(new Integer(this.getText()));
        } catch (NumberFormatException e) {
            // Reset field to previous value
            if (value != null) setValue(value.intValue());
            else setValue(0);
        }
    }

    private void updateData() {
        if (value == null) parseField();

        // Constrain to admissibleValues
        int index = getAdmissibleValueIndex();
        if (index >= 0) value = admissibleValues[index];

        // Apply bounds
        this.value = new Integer(Math.min(Math.max(lowerBound, value.intValue()), upperBound));

        // Write and resize field
        this.setText(value.toString());

        if (lowerBound != -Integer.MAX_VALUE && upperBound != Integer.MAX_VALUE) {
            this.setColumns(1 + Math.max(Integer.toString(lowerBound).length(), Integer.toString(upperBound).length()));
        } else {
            this.setColumns(1 + this.getText().length());
        }
    }

    private int getAdmissibleValueIndex() {
        // Constrain to admissibleValues
        if (this.admissibleValues != null && admissibleValues.length > 0) {
            int index = java.util.Arrays.binarySearch(admissibleValues, value.intValue());
            if (index < 0) index = -index - 1;
            if (index > admissibleValues.length) index--;
            return index;
        } else return -1;
    }
}
