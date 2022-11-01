package edu.harvard.mcz.imagecapture.interfaces;

import java.lang.reflect.InvocationTargetException;

public interface GuiProviderForRunnable {
    void invokeOnUIThread(Runnable runnable) throws InterruptedException, InvocationTargetException;
}
