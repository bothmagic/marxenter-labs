/*
 * $Id: MetaData.java 48 2004-09-08 19:10:50Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Class for representing the meta-data for an field in a data model.
 * A &quot;field&quot; may map to a column on a RowSet, a property on a JavaBean,
 * or some other descrete data element on a data model.
 * The meta-data describes aspects of a data field such as it's name,
 * type, and edit constraints.  This class may be used when such information
 * isn't encapsulated in the data model object itself and thus must be represented
 * in an external object.  Meta-data is intended only for holding state about
 * a data model element and is not intended for implementing application semantics.</p>
 * <p/>
 * A meta-data object should be initialized at a minimum with a name, class,
 * and label.  Additional meta-data properties can be set as necessary.
 * For example:<br>
 * <pre><code>
 *     MetaData metaData = new MetaData(&quot;firstname&quot;, String.class,
 *                                      &quot;First Name&quot;);
 *     metaData.setRequired(true);
 * </code></pre>
 * If the associated data model field requires application-specific validation
 * logic to verify the correctness of a potential value, one or more Validator
 * instances can be added to the meta-data object.  Example:<br>
 * <pre><code>
 *     MetaData metaData = new MetaData(&quot;creditcard&quot;, String.class,
 *                                      &quot;Credit Card Number&quot;
 *     metaData.setRequired(true);
 *     metaData.addValidator(new MyCreditCardValidator());
 * </code></pre>
 * <p/>
 * MetaData can contain custom properties as well. These properties are based
 * on the traditional key/value paradygm. Keys in this map should be of the form
 * com.mydomain.packagename.PropertyName.
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class MetaData {
    /**
     * The name of the meta data field. This name maps to an underlying data element
     * on the data model, such as a column in a RowSet, or a property on a java bean.
     */
    protected String name;
    /**
     * The class of the meta data field. This is used by various components to determine
     * how to display the data in this field. For instance, a table component will use this
     * information to determine what kind of renderers/editors to use on the column
     */
    protected Class klass = String.class;
    /**
     * This is a human-readable name for the field. This can be used, for example. as a label
     * for a table column, or for a JLabel associated with a JTextField (where the text field
     * is a bound component for the field represented by this meta data).
     */
    protected String label;
    /**
     * This is a hint provided to components indicating how wide (in characters) the column
     * should be.
     */
    protected int displayWidth;
    /**
     * Converter used to convert the data associated with this field to/from a string
     * representation
     */
    protected Converter converter = null;
    /**
     * Optional object that describes how the converter should convert values from a string
     */
    protected Object decodeFormat = null;
    /**
     * Optional object that describes how the converter should convert values to a string
     */
    protected Object encodeFormat = null;
    /**
     * Flag indicating whether this field is read only. Read only fields should be
     * appropriately indicated in the user interface.
     */
    protected boolean readOnly = false;
    protected int minValueCount = 0; // null value okay
    protected int maxValueCount = 1; // only one value allowed
    /**
     * A list of validators. Before a new value for the field defined by this MetaData
     * is set for the field, it must pass through these validators.
     */
    protected ArrayList validators = null;
    /**
     * Defines what default value should be used for the field associated
     * with this meta data when the field is newly created.
     */
    protected Object defaultValue = null;
    /**
     * Map containing custom properties. Keys in this map should be of the form
     * com.mydomain.packagename.PropertyName.
     */
    protected Map customProps = new HashMap();
    /**
     * Convenience class for handling JavaBean property changes
     */
    protected PropertyChangeSupport pcs;

    /**
     * Instantiates a meta-data object with a default name &quot;value&quot; and
     * a default field class equal to <code>java.lang.String</code>.
     * This provides the no-argument constructor required for JavaBeans.
     * It is recommended that the program explicitly set a meaningful
     * &quot;name&quot; property.
     */
    public MetaData() {
        this("value");
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * a default field class equal to <code>java.lang.String</code>.
     *
     * @param name String containing the name of the data field
     */
    public MetaData(String name) {
        this.name = name;
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * field class.
     *
     * @param name  String containing the name of the data field
     * @param klass Class indicating type of data field
     */
    public MetaData(String name, Class klass) {
        this(name);
        this.klass = klass;
    }

    /**
     * Instantiates a meta-data object with the specified name,
     * field class, and label.
     *
     * @param name  String containing the name of the data field
     * @param klass Class indicating type of data field
     * @param label String containing the user-displayable label for the
     *              data field
     */
    public MetaData(String name, Class klass, String label) {
        this(name, klass);
        this.label = label;
    }

    /**
     * Places a custom property into the customProp map.
     *
     * @param key   A non-null string of the form
     *              com.mydomain.packagename.PropertyName
     * @param value The value to be placed in the map
     */
    public void putCustomProperty(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("The key for a custom property " +
                    "on MetaData cannot be null");
        }
        customProps.put(key, value);
    }

    /**
     * @param key A non-null string of the form
     *            com.mydomain.packagename.PropertyName
     * @return The value at the given key in the customProps map.
     */
    public Object getCustomProperty(String key) {
        if (key == null) {
            throw new IllegalArgumentException("The key for a custom property " +
                    "on MetaData cannot be null");
        }
        return customProps.get(key);
    }

    /**
     * @param key A non-null string of the form
     *            com.mydomain.packagename.PropertyName
     * @param def The default value to return if the customProps map
     *            does not contain they specified key
     * @return The value at the given key in the customProps map.
     */
    public Object getCustomProperty(String key, Object def) {
        if (key == null) {
            throw new IllegalArgumentException("The key for a custom property " +
                    "on MetaData cannot be null");
        }
        return customProps.containsKey(key) ? customProps.get(key) : def;
    }

    /**
     * Gets the meta-data &quot;name&quot; property which indicates
     * the logical name of the associated data field.
     *
     * @return String containing the name of the data field.
     * @see #setName
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the meta-data &quot;name&quot; property.
     *
     * @param name String containing the name of the data field
     * @see #getName
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        pcs.firePropertyChange("name", oldName, name);
    }

    /**
     * Returns what default value should be used for the field associated with this meta data when the field is newly created.
     *
     * @return
     */
	Object getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Sets what default value should be used for the field associated with this meta data when the field is newly created.
	 * @param defaultValue
	 */
	void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
     * Gets the meta-data's &quot;elementClass&quot; property which
     * indicates the type of the associated data field.
     * The default field class is <code>java.lang.String</code>.
     *
     * @return Class indicating type of data field
     * @see #setElementClass
     */
    public Class getElementClass() {
        return klass;
    }

    /**
     * Sets the meta-data's &quot;elementClass&quot; property.
     * This <code>set</code> method is provided for meta-data bean initialization
     * only; the field class is not intended to be modified after initialization,
     * since other aspects of a meta-data object may depend on this type setting.
     *
     * @param klass Class indicating type of data field
     * @see #getElementClass
     */
    public void setElementClass(Class klass) {
        Class oldClass = this.klass;
        this.klass = klass;
        pcs.firePropertyChange("elementClass", oldClass, klass);
    }

    /**
     * Gets the meta-data's &quot;label&quot; property, which provides
     * a label for the associated data field.  The label is intended
     * for display to the end-user and may be localized.
     * If no label has been explicitly set, then the meta-data's name is
     * returned.
     *
     * @return String containing the user-displayable label for the
     *         data field
     * @see #setLabel
     */
    public String getLabel() {
        return label == null ? name : label;
    }

    /**
     * Sets the meta-data's &quot;label&quot; property.
     *
     * @param label String containing the user-displable label for the
     *              data field
     * @todo Rename to setTitle
     * @see #getLabel
     */
    public void setLabel(String label) {
        String oldLabel = this.label;
        this.label = label;
        pcs.firePropertyChange("label", oldLabel, label);
    }

    /**
     * Returns an integer indicating the preferred display width for this
     * field, in characters. This is merely a hint to the UI
     *
     * @return
     */
    public int getDisplayWidth() {
        return displayWidth;
    }

    /**
     * Sets the preferred display width for this field, in characters. This is
     * a hint to the underlying UI. For instance, a Table component may attempt
     * to give (displayWidth * charwidth) pixels to the column represented
     * by this MetaData. When the displayWidth is changed, all listeners
     * are notified of this change so they may resize themselves if they so
     * desire.
     *
     * @param displayWidth
     */
    public void setDisplayWidth(int displayWidth) {
        int oldDisplayWidth = this.displayWidth;
        this.displayWidth = displayWidth;
        //notify listeners of this change
        pcs.firePropertyChange("displayWidth", oldDisplayWidth, displayWidth);
    }

    /**
     * Gets the meta-data's converter, which performs conversions between
     * string values and objects of the associated data field's type.
     * If no converter was explicitly set on this meta-data object,
     * this method will retrieve a default converter for this data
     * field's type from the converter registry.  If no default converter
     * is registered, this method will return <code>null</code>.
     *
     * @return Converter used to perform conversions between string values
     *         and objects of this field's type
     * @see Converters#get
     * @see #setConverter
     * @see #getElementClass
     */
    public Converter getConverter() {
        if (converter == null) {
            return Converters.get(klass);
        }
        return converter;
    }

    /**
     * Sets the meta-data's converter.
     *
     * @param converter Converter used to perform conversions between string values
     *                  and objects of this field's type
     * @see #getConverter
     */
    public void setConverter(Converter converter) {
        Converter oldConverter = this.converter;
        this.converter = converter;
        pcs.firePropertyChange("converter", oldConverter, converter);
    }

    /**
     * Gets the meta-data's decode format which is used when converting
     * values from a string representation.  This property must be used when
     * conversion requires format information on how the string representation
     * is structured.  For example, a decode format should be used when decoding
     * date values.  The default decode format is <code>null</code>.
     *
     * @return format object used to describe format for string-to-object conversion
     * @see #setDecodeFormat
     */
    public Object getDecodeFormat() {
        return decodeFormat;
    }

    /**
     * Sets the meta-data's decode format which is used when converting
     * values from a string representation.
     *
     * @param format object used to describe format for string-to-object conversion
     * @see #getDecodeFormat
     * @see java.text.DateFormat
     */
    public void setDecodeFormat(Object format) {
        Object oldDecodeFormat = this.decodeFormat;
        this.decodeFormat = format;
        pcs.firePropertyChange("decodeFormat", oldDecodeFormat, format);
    }

    /**
     * Gets the meta-data's encode format which is used when converting
     * values to a string representation.  This property must be used when
     * conversion requires format information on how the string representation
     * should be generated.  For example, an encode format should be used when
     * encoding date values.  The default encode format is <code>null</code>.
     *
     * @return format object used to describe format for object-to-string conversion
     * @see #setEncodeFormat
     */
    public Object getEncodeFormat() {
        return encodeFormat;
    }

    /**
     * Sets the meta-data's encode format which is used when converting
     * values to a string representation.
     *
     * @param format object used to describe format for object-to-string conversion
     * @see #getEncodeFormat
     * @see java.text.DateFormat
     */
    public void setEncodeFormat(Object format) {
        Object oldEncodeFormat = this.encodeFormat;
        this.encodeFormat = format;
        pcs.firePropertyChange("encodeFormat", oldEncodeFormat, format);
    }

    /**
     * Gets the meta-data's &quot;readOnly&quot; property which indicates
     * whether or not the associated data field's value cannot be modified.
     * The default is <code>false</code>.
     *
     * @return boolean indicating whether the data field is read-only
     * @see #setReadOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the meta-data's &quot;readOnly&quot; property.
     *
     * @param readOnly boolean indicating whether the data field is read-only
     * @see #isReadOnly()
     */
    public void setReadOnly(boolean readOnly) {
        boolean oldReadOnly = this.readOnly;
        this.readOnly = readOnly;
        pcs.firePropertyChange("readOnly", oldReadOnly, readOnly);
    }

    /**
     * Gets the meta-data's &quot;minValueCount&quot; property, which indicates
     * the minimum number of values required for the data field.  The default
     * is 0, which means a null value is permitted.  This property should be set
     * to 1 if the field requires a non-null value.
     *
     * @return integer indicating the minimum number of values required for
     *         the data field
     * @see #setMinValueCount
     */
    public int getMinValueCount() {
        return minValueCount;
    }

    /**
     * Sets the meta-data's &quot;minValueCount&quot; property.
     *
     * @param minValueCount integer indicating the minimum number of values required for
     *                      the data field
     */
    public void setMinValueCount(int minValueCount) {
        int oldMinValueCount = this.minValueCount;
        this.minValueCount = minValueCount;
        pcs.firePropertyChange("minValueCount", oldMinValueCount, minValueCount);
    }

    /**
     * Convenience method for calculating whether the &quot;minValueCount&quot;
     * property is greater than 0.
     *
     * @return boolean indicating whether at least one non-null value must
     *         be set for the data field
     */
    public boolean isRequired() {
        return getMinValueCount() > 0;
    }

    public void setRequired(boolean required) {
        setMinValueCount(1);
    }

    /**
     * Gets the meta-data's &quot;maxValueCount&quot; property, which indicates
     * the maximum number of values permitted for the data field.  The default
     * is 1, which means a single value is permitted.  If this property is set
     * to a value greater than 1, then the values will be contained in a
     * <code>List</code> collection.
     *
     * @return integer indicating the maximum number of values permitted for
     *         the data field
     * @see java.util.List
     * @see #setMaxValueCount
     */
    public int getMaxValueCount() {
        return maxValueCount;
    }

    /**
     * Sets the meta-data's &quot;maxValueCount&quot; property.
     *
     * @param maxValueCount integer indicating the maximum number of values permitted for
     *                      the data field
     */
    public void setMaxValueCount(int maxValueCount) {
        int oldMaxValueCount = this.maxValueCount;
        this.maxValueCount = maxValueCount;
        pcs.firePropertyChange("maxValueCount", oldMaxValueCount, maxValueCount);
    }

    /**
     * Adds the specified validator for this meta-data.  A validator object is
     * used to determine whether a particular object is a valid value for
     * the associated data field.  A data field may have 0 or more validators.
     *
     * @param validator Validator object which performs validation checks on
     *                  values being set on the associated data field
     * @see #removeValidator
     * @see #getValidators
     */
    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);
    }

    /**
     * Removes the specified validator for this meta-data.
     *
     * @param validator Validator object which performs validation checks on
     *                  values being set on the associated data field
     * @see #addValidator
     */
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
            if (validators.size() == 0) {
                validators = null;
            }
        }
    }

    /**
     * @return array containing 0 or more validators set on this meta-data
     * @see #addValidator
     */
    public Validator[] getValidators() {
        if (validators != null) {
            return (Validator[]) validators.toArray(new Validator[1]);
        }
        return new Validator[0];
    }

    /**
     * Adds the specified property change listener to this meta-data object.
     *
     * @param pcl PropertyChangeListener object to receive events when meta-data
     *            properties change
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes the specified property change listener from this meta-data object.
     *
     * @param pcl PropertyChangeListener object to receive events when meta-data
     *            properties change
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     * @return array containing the PropertyChangeListener objects registered
     *         on this meta-data object
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }
}
