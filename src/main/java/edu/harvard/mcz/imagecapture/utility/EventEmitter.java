package edu.harvard.mcz.imagecapture.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class EventEmitter {

    protected ArrayList<ActionListener> listeners;

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }

    protected void emitEvent(ActionEvent event) {
        for (ActionListener listener :
                listeners) {
            listener.actionPerformed(event);
        }
    }
}
