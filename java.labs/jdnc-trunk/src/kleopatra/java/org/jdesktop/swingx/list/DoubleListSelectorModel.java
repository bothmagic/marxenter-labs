/*
 * Created on 14.04.2009
 *
 */
package org.jdesktop.swingx.list;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.RowFilter;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.sort.RowFilters.GeneralFilter;
import org.jdesktop.swingx.util.Contract;

public class DoubleListSelectorModel {

    private BeanListModel<CheckableNode> listModel;
    private boolean loaded;
    private StringValue sv;
    private String resourceName;
    private String resourcePrefix;
    
    
    public void setDataSource(Class<?> base, String resource) {
        getListModel().removeAll();
        URL url = base.getResource(resource);
        int pos = resource.lastIndexOf("/") + 1;
        
        if (pos > 0) {
            this.resourceName = resource.substring(pos);
            this.resourcePrefix = resource.substring(0, pos);
        } else {
            this.resourceName = resource;
            this.resourcePrefix = "";
        }
        loadDataFromCSV(url);
        checkForIncluded(base.getResource(getIncludedResource()));
    }
    

    private String getIncludedResource() {
        return resourcePrefix + "included" + resourceName;
    }

    public StringValue getStringValue(boolean included) {
        if (sv == null) {
            sv = new StringValue() {

                public String getString(Object value) {
                    if (value instanceof CheckableNode) {
                        value = ((CheckableNode) value).getUserObject();
                    }
                    if (value instanceof WikiDesktopTopic) {
                        return ((WikiDesktopTopic) value).getTopic();
                    }
                    return StringValues.TO_STRING.getString(value);
                }
                
            };
        }
        return sv;
        
    }
    
    public BeanListModel<CheckableNode> getListModel() {
        if (!loaded) {
           listModel = loadData();
           loaded = true;
        }
        return listModel;
    }

    public RowFilter getFilter(boolean included) {
        return new CheckedFilter(included);
    }
    
    public void toggleInclusion(Object... items ) {
        for (Object object : items) {
            CheckableNode checkable = (CheckableNode) object;
            checkable.setChecked(!checkable.isChecked());
        }
    }
    

    public void exportIncluded() {
        if (listModel == null) return;
        List<WikiDesktopTopic> topics = new ArrayList<WikiDesktopTopic>();
        for (int i = 0; i < listModel.getSize(); i++) {
            CheckableNode checkable = listModel.getElementAt(i);
            if (checkable.isChecked()) {
                topics.add((WikiDesktopTopic) checkable.getUserObject());
            }
        }
        saveData(topics);
    }
    
    private void saveData(List<?> topics) {
        if (topics.isEmpty()) return;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("included" + resourceName));
            for (Object item : topics) {
                writer.println(item);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private BeanListModel<CheckableNode> loadData() {
        return new BeanListModel<CheckableNode>();
    }
    
    private void checkForIncluded(URL url) {
        if (url == null) return;
        InputStream is = null;
        try {
            is = url.openStream();
//            Charset c = Charset.forName("UTF-16");
//            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is, c));
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
            String line;
            while (( line = lnr.readLine()) != null ) {
                System.out.println(line);
                checkRow(line);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

    private void loadDataFromCSV(URL url) {
        PrintWriter writer = null;
        InputStream is = null;
        try {
            is = url.openStream();
//            Charset c = Charset.forName("UTF-16");
//            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is, c));
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
            writer = new PrintWriter(new File("wikitopics.txt"));
            
            String line;
            while (( line = lnr.readLine()) != null ) {
                System.out.println(line);
                String mod = addRow(line);
                writer.println(mod);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            writer.flush();
            writer.close();
        }
    }

    private String addRow(String line) {
        WikiDesktopTopic topic = new WikiDesktopTopic(line);
        listModel.addBean(new CheckableNode(topic, false));
        return line;
    }

    private String checkRow(String line) {
        WikiDesktopTopic topic = new WikiDesktopTopic(line);
        List<CheckableNode> beans = listModel.getBeans();
        for (CheckableNode checkableNode : beans) {
            if (topic.equals(checkableNode.getUserObject())) {
                checkableNode.setChecked(true);
                break;
            }
        }
        return line;
    }

    public static class CheckedFilter extends GeneralFilter {
        
        private boolean matchSelected;

        public CheckedFilter(boolean matchSelected) {
            super(0);
            this.matchSelected = matchSelected;
        }

        @Override
        protected boolean include(
                Entry<? extends Object, ? extends Object> entry,
                int index) {
            Object value = entry.getValue(index);
            if (value instanceof CheckableNode) {
                boolean checked = ((CheckableNode) value).isChecked();
                return checked == matchSelected;
            }
            return false;
        }
        
    }

    public static class WikiDesktopTopic {
        private static final String HOST = "http://wiki.java.net";
        private static final String DESKTOP = "/bin/view/Javadesktop/";
        private static final String LOCATION = HOST + DESKTOP;
        private String topic;
        
        public WikiDesktopTopic(String topic) {
            setTopic(topic);
        }

        public String getTopic() {
            return topic;
        }
        
        private void setTopic(String line) {
            Contract.asNotNull(line, "topic must not be empty");
            int pos = line.indexOf(">");
            if (pos > 0) {
                line = line.substring(0, pos - 1);
            }
            pos = line.lastIndexOf("/") + 1;
            if (pos > 0) {
                line = line.substring(pos);
            }
            this.topic = line;
        }

        @Override
        public String toString() {
            return LOCATION + getTopic();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof WikiDesktopTopic)) return false;
            return getTopic().equals(((WikiDesktopTopic) other).getTopic());
        }
        
        
    }

    /**
     * Immutable wrapper around a user object.
     */
    public static class CheckableNode extends AbstractBean implements Comparable {

        private Object userObject;
        private boolean checked;

        public CheckableNode(Object userObject, boolean checked) {
            this.userObject = userObject;
            this.checked = checked;
        }

        public CheckableNode(Object userObject) {
            this(userObject, false);
        }

        public Object getUserObject() {
            return userObject;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            boolean old = isChecked();
            this.checked = checked;
            firePropertyChange("checked", old, isChecked());
        }

        /**
         * Implemented to compare the user objects only, so this is incompatible
         * with the equals. C&p'ed from ShuttleSorter.
         */
        public int compareTo(Object o) {
            if (!(o instanceof CheckableNode)) {
                return -1;
            }
            CheckableNode node = (CheckableNode) o;
            Object o1 = getUserObject();
            Object o2 = node.getUserObject();
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) { // Define null less than everything.
                return -1;
            } else if (o2 == null) {
                return 1;
            }
            // not copied: collator
            // patch from Jens Elkner (#189)
            if ((o1.getClass().isInstance(o2)) && (o1 instanceof Comparable)) {
                Comparable c1 = (Comparable) o1;
                return c1.compareTo(o2);
            } else if (o2.getClass().isInstance(o1)
                    && (o2 instanceof Comparable)) {
                Comparable c2 = (Comparable) o2;
                return -c2.compareTo(o1);
            }
            return 0;
        }
    }

    /**
     * A custom ListModel, taking a List of ColumnVisibilityActions and
     * registering PropertyChangeListener on each element.
     */
    public static class BeanListModel<T extends AbstractBean> extends AbstractListModel {

        List<T> actions;
        private PropertyChangeListener actionPropertyChangeListener;
        
        public void addBean(T bean) {
            getBeans().add(bean);
            bind(bean);
            fireIntervalAdded(this, getSize() - 1, getSize() - 1);
        }
        
        public void setBeans(List<T> elements) {
            this.removeAll();
            getBeans().addAll(elements);
            for (T action : actions) {
                bind(action);
            }
            if (getSize() > 0) {
                fireIntervalAdded(this, 0, getSize() - 1);
            }
        }
        

        public void removeAll() {
            int oldSize = getSize();
            if (oldSize == 0) return;
            for (T value : actions) {
                release(value);
            }
            getBeans().clear();
            fireIntervalRemoved(this, 0, oldSize - 1);
        }

        protected List<T> getBeans() {
            if (actions == null) {
                actions = new ArrayList<T>();
            }
            return actions;
        }
        
        private void release(T value) {
            value.removePropertyChangeListener(getActionPropertyChangeListener());
        }

        private void bind(T action) {
            action.addPropertyChangeListener(getActionPropertyChangeListener());
            
        }

        private PropertyChangeListener getActionPropertyChangeListener() {
            if (actionPropertyChangeListener == null) {
                actionPropertyChangeListener = createActionPropertyChangeListener();
            }
            return actionPropertyChangeListener;
        }

        private PropertyChangeListener createActionPropertyChangeListener() {
            PropertyChangeListener listener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    fireElementChanged(evt.getSource());
                    
                }
                
            };
            return listener;
        }

        protected void fireElementChanged(Object source) {
            int index = getBeans().indexOf(source);
            if (index >= 0) {
                fireContentsChanged(this, index, index);
            }
            
        }

        //------------------ implement ListModel
        
        public int getSize() {
            return getBeans().size();
        }

        public T getElementAt(int index) {
            return getBeans().get(index);
        }
        
    }

}
