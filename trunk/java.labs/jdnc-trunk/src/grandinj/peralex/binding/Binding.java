package peralex.binding;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import peralex.binding.widgethalf.BindingWidgetException;
import peralex.binding.widgethalf.WidgetHalfBinding;

/**
 * It holds the higher level semantic stuff related to bindings.
 * 
 * @author Noel Grandin
 */
public class Binding {

	private final WidgetHalfBinding widgetHalfBinding;

	private final String fieldName;
	private final Object modelObject;
	private final PropertyDescriptor propertyDescriptor;
	
	private boolean readOnly = false;
	private final List<BindingValidator> validators = new ArrayList<BindingValidator>();

	/**
	 * 
	 * @throws IllegalArgumentException if the binding does not support the field data type
	 */
	public Binding(Object modelObject, String fieldName, WidgetHalfBinding widgetBinding) {
		this.modelObject = modelObject;
		this.fieldName = fieldName;
		try {
			this.propertyDescriptor = PropertyUtils.getPropertyDescriptor(modelObject, fieldName);
		} catch (IllegalAccessException ex) {
			// this is one time I don't want to clutter client code with exception handling, but
			// I want to fail early.
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
		this.widgetHalfBinding = widgetBinding;
		
		if (!widgetHalfBinding.isValidModelFieldClass(getModelFieldClass()))
		{
			throw new IllegalArgumentException("binding " + widgetBinding.getClass().getName() + " for " + fieldName + " does not support type " + getModelFieldClass().getName());
		}
	}

	/**
	 * if the binding is read-only, then attempts to update the model from the UI do nothing.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * if the binding is read-only, then attempts to update the model from the UI do nothing.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * enable/disable the UI component
	 */
	public void setComponentEnabled(boolean enabled) {
		widgetHalfBinding.setComponentEnabled(enabled);
	}

	/**
	 * do all of the validation, but don't perform the update
	 * @throws BindingModelUpdateException
	 */
	public void testUpdateModelFromUI() throws BindingModelUpdateException {
		if (readOnly)
		{
			return;
		}
		final Object modelValueFromUI;
		try {
			modelValueFromUI = widgetHalfBinding.getUIValue(getModelFieldClass());
		} catch (BindingWidgetException ex) {
			throw new BindingModelUpdateException(new BindingErrorField(getModelFieldName(), ex.getMessage()));
		}
		doValidation(modelValueFromUI);
	}

	private void doValidation(final Object modelValue) throws BindingModelUpdateException
	{
		final List<BindingErrorField> errors = new ArrayList<BindingErrorField>();
		for (BindingValidator validator : validators)
		{
			final String msg = validator.validate(getModelFieldClass(), modelValue);
			if (msg!=null) errors.add(new BindingErrorField(getModelFieldName(), msg));
		}
		if (errors.size()>0) {
			throw new BindingModelUpdateException(new BindingErrorComposite(errors.toArray(new BindingErrorField[errors.size()])));
		}
	}
	
	/**
	 * perform validation, and then do the update
	 */
	public void updateModelFromUI() throws BindingModelUpdateException {
		if (readOnly)
		{
			return;
		}
		
		final Object modelValueFromUI;
		try {
			modelValueFromUI = widgetHalfBinding.getUIValue(getModelFieldClass());
		} catch (BindingWidgetException ex) {
			throw new BindingModelUpdateException(new BindingErrorField(getModelFieldName(), ex.getMessage()));
		}
		
		// do this just in case
		doValidation(modelValueFromUI);
		
		setModelFieldValue(modelValueFromUI);
	}

	public void updateUIFromModel() {
		widgetHalfBinding.updateUIFromModel(getModelFieldClass(), getModelFieldValue());
	}
	
	public void addBindingValidator(BindingValidator validator)
	{
		validators.add(validator);
	}
	
	public void removeBindingValidator(BindingValidator validator)
	{
		validators.remove(validator);
	}
	
	private String getModelFieldName()
	{
		return this.fieldName;
	}
	
	private Class<?> getModelFieldClass()
	{
		return this.propertyDescriptor.getPropertyType();
	}
	
	private final Object getModelFieldValue()
	{
		try {
			return PropertyUtils.getProperty(modelObject, getModelFieldName());
		} catch (IllegalAccessException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			// should never happen
			throw new RuntimeException(ex);
		}
	}
	
	private final void setModelFieldValue(Object value)
	{
		try {
			PropertyUtils.setProperty(modelObject, getModelFieldName(), value);
		} catch (IllegalAccessException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			// should never happen
			throw new RuntimeException(ex);
		}
	}
}
