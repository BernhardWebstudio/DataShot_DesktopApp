package edu.harvard.mcz.imagecapture.interfaces;

import java.awt.*;

public static enum CloseType {
    CANCEL,
    OK,

}

public interface CloseListener {

    public void onClose(CloseType type, Component source);
}
