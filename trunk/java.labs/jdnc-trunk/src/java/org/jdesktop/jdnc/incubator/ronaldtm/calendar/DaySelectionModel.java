package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

/**
 * @author Ronald Tetsuo Miura
 */
public interface DaySelectionModel {

    /** */
    public static final int SINGLE_SELECTION = 0;

    /** */
    public static final int MULTIPLE_SELECTION = 1;

    /** */
    public static final int WEEK_SELECTION = 2;

    /** */
    public static final int NONE = -1;

    /** */
    public static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    /**
     * @param listener
     */
    public abstract void addDaySelectionListener(DaySelectionListener listener);

    /**
     * @param listener
     */
    public abstract void removeDaySelectionListener(DaySelectionListener listener);

    /**
     * @param time -
     * @return -
     */
    public abstract boolean isSelectable(long time);

    /**
     * @param time -
     * @return -
     */
    public abstract boolean isSelected(long time);

    /**
     * @return -
     */
    public abstract long getSelectionAnchor();

    /**
     * @return -
     */
    public abstract long getSelectionLead();

    /**
     * @param time -
     */
    public abstract void setSelectionAnchor(long time);

    /**
     * @param time
     */
    public abstract void setSelectionLead(long time);

    /**
     * @param time
     */
    public void setSelection(long time);

    /**
     * @param anchor
     * @param lead
     */
    public void setSelection(long anchor, long lead);

    /**
     * @return -
     */
    public abstract int getSelectionMode();

    /**
     * @param mode -
     */
    public abstract void setSelectionMode(int mode);

    /**
     * @return -
     */
    public abstract int getFirstDayOfWeek();

    /**
     * @param firstDayOfWeek
     */
    public abstract void setFirstDayOfWeek(int firstDayOfWeek);

}