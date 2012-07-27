/*
 * Created on 31.10.2007
 *
 */
package org.jdesktop.beansbindingx;

import java.util.Arrays;
import java.util.List;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.observablecollections.ObservableCollections;

public class BeanTestUtils {
    
    public static List<TestBean> createBeans() {
        TestBean[] beans = new TestBean[] {
                new TestBean("first"), new TestBean("second"), new TestBean("third")
        };
        return ObservableCollections.observableList(Arrays.asList(beans));
    }

    /**
     * Bean to bind to.
     */
    public static class TestBean extends AbstractBean {
        private String value;
        private boolean active;
        public TestBean(String value) {
            this(value, false);
        }
        public TestBean(String value, boolean b) {
            this.value = value;
            this.active = b;
        }
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            Object old = getValue();
            this.value = value;
            firePropertyChange("value", old, getValue());
        }
        
        public void setActive(boolean active) {
            boolean old = isActive();
            this.active = active;
            firePropertyChange("active", old, isActive());
        }
        
        public boolean isActive() {
            return active;
        }
    }
    

}
