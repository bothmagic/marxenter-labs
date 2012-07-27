package org.jdesktop.swing.decorator;

import javax.swing.event.AncestorListener;

import java.awt.event.KeyListener;

/**
 * @author Gilles Philippart
 */
public interface QuickSearcher extends KeyListener, AncestorListener {

    void search(String s);

    void clear();

    void setEnabled(boolean enabled);

    boolean isEnabled();

}
