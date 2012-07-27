package org.jdesktop.swingx.inspection;

import java.util.*;

public abstract class AbstractDetails implements Details {

    private Map<String, Object> values;

    public AbstractDetails() {
        super();
    }





    protected void setValue(String key, Object value) {
        if (value == null) {
            if (values != null) {
                values.remove(key);
            }
        } else {
            if (values == null) {
                values = new HashMap<String, Object>();
            }
            values.put(key, value);
        }
    }





    public Object getValue(String key) {
        return values == null ? null : values.get(key);
    }





    protected abstract List<Entry> getEntries();





    public int getDetailCount() {
        List<Entry> entries = getEntries();
        return entries == null ? 0 : entries.size();
    }





    public String getLabelAt(int index) {
        if (index < 0 || index >= getDetailCount()) {
            throw new IndexOutOfBoundsException(getDetailCount() + ":" + index);
        }
        List<Entry> entries = getEntries();
        return entries.get(index).getLabel();
    }





    public Object getValueAt(int index) {
        if (index < 0 || index >= getDetailCount()) {
            throw new IndexOutOfBoundsException(getDetailCount() + ":" + index);
        }
        List<Entry> entries = getEntries();
        return entries.get(index).getValue();
    }





    protected class Entry {
        protected String label;
        protected Object value;

        public Entry(String label, Object value) {
            if (label == null) {
                throw new NullPointerException("label cannot be null");
            }
            if (value == null) {
                throw new NullPointerException("value cannot be null");
            }
            this.label = label;
            this.value = value;
        }





        protected Entry() {
        }





        public String getLabel() {
            return label;
        }





        public Object getValue() {
            return value;
        }





        public void setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
