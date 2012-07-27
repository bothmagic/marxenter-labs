package org.jdesktop.jdnc.incubator.vprise.model;

import java.beans.*;
import org.jdesktop.jdnc.incubator.vprise.i18n.*;
import org.jdesktop.swing.data.*;

/**
 * An extension to the java bean data model that allows us to determine
 * the order of the fields in the bean and localize them.
 *
 * @author Shai Almog
 */
public class VJavaBeanDataModel extends JavaBeanDataModel {       
    private String[] fields;
    
    /** Creates a new instance of VJavaBeanDataModel */
    public VJavaBeanDataModel(Class beanClass) throws IntrospectionException {
        super(beanClass);
    }
    
    public VJavaBeanDataModel(Class beanClass, Object obj) throws IntrospectionException {
        super(beanClass, obj);
    }

    public String[] getFieldNames() {
        if(fields != null) {
            return fields;
        }
        return super.getFieldNames();
    }

    /**
     * This method allows us to reprder the fields
     */
    public void setFieldOrder(String[] fields) {
        this.fields = fields;
    }

    /**
     * This method sets the localizable keys that can be used to read the translated
     * names from the resource bundle
     */
    public void setFieldKeys(String[] fieldKeys) {
        String[] fields = getFieldNames();
        for(int iter = 0 ; iter < fieldKeys.length ; iter++) {
            getMetaData(fields[iter]).setLabel(Resources.getString(fieldKeys[iter]));
        }
    }
}
