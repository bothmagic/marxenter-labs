/*
 * Created on 26.10.2007
 *
 */
package org.jdesktop.swingx.table.treetable.file;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

/**
 * Common String, Icon, ... representations. 
 */
public class DisplayValues {

    public static final StringValue FILE_NAME_VALUE = new StringValue() {

        public String getString(Object value) {
            if (value instanceof File) {
                return FileSystemView.getFileSystemView().getSystemDisplayName(
                        (File) value);
            }
            return StringValues.TO_STRING.getString(value);
        }
    };

    public static final IconValue FILE_ICON_VALUE = new IconValue() {

        public Icon getIcon(Object value) {
            if (value instanceof File) {
                return FileSystemView.getFileSystemView().getSystemIcon(
                        (File) value);
            }
            return null;
        }
    };

}
