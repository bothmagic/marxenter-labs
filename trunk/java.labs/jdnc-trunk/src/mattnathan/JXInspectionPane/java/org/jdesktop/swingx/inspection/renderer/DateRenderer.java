package org.jdesktop.swingx.inspection.renderer;

import java.util.*;
import java.text.*;
import org.jdesktop.swingx.JXInspectionPane;

public class DateRenderer extends AbstractLabelRenderer {

    private static final DateFormat DEFAULT = SimpleDateFormat.getInstance();

    /**
     * User set date format
     */
    private DateFormat format;

    /**
     * Create a default date format.
     */
    public DateRenderer() {
        this(null);
    }





    /**
     * Create a new date renderer with the given format. This can be null.
     *
     * @param format The format to use.
     */
    public DateRenderer(DateFormat format) {
        this.format = format;
    }





    /**
     * Get the date formatter for the date. This can be null.
     *
     * @return The formatter.
     * @see #getUsableDateFormat
     */
    public DateFormat getDateFormat() {
        return format;
    }





    /**
     * Set the date format for this renderer.
     *
     * @param dateFormat The formatter for the date.
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.format = dateFormat;
    }





    /**
     * Get a non-null DateFormat for converting the date. This first checks {@link #getDateFormat} then checks the panes
     * client properties then the UIManager for the format. This supports ui properties of types String and DateFormat.
     *
     * @param pane The source component.
     * @return Format for converting dates.
     */
    public DateFormat getUsableDateFormat(JXInspectionPane pane) {
        DateFormat result = getDateFormat();
        if (result == null) {
            Object o = getUIProperty(pane, "DateRenderer.format");

            if (o instanceof DateFormat) {
                result = (DateFormat) o;
            } else if (o != null) {
                result = new SimpleDateFormat(o.toString());
            }

            if (result == null) {
                result = DEFAULT;
            }
        }
        return result;
    }





    /**
     * Convert the given value into a String for representation.
     *
     * @param value The value to convert.
     * @return A string representing the value.
     */
    @Override
    protected String toString(JXInspectionPane pane, Object value) {
        return value instanceof Date ? getUsableDateFormat(pane).format((Date) value) : "";
    }
}
