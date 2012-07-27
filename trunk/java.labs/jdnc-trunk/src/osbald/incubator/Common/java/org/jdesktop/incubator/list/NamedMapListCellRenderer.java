package org.jdesktop.incubator.list;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Renderer that expects a map of name->value pairs. Internally creates a bidirectional map that maps values
 * to names so that the renderer uses the name string while the list/selection models retain the actual value objects.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 27-Mar-2006
 * Time: 17:54:08
 */

public class NamedMapListCellRenderer<T> extends DefaultListCellRenderer {
    private Map<T, String> lookup = new HashMap<T, String>();

    public NamedMapListCellRenderer(Map<String, T> mapping) {
        for (Map.Entry<String, T> entry : mapping.entrySet()) {
            lookup.put(entry.getValue(), entry.getKey());
        }
    }

    @SuppressWarnings("unchecked")
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, getString((T) value), index, isSelected, cellHasFocus);
    }

    String getString(T value) {
        String result = lookup.get(value);
        return result != null ? result : "";
    }
}