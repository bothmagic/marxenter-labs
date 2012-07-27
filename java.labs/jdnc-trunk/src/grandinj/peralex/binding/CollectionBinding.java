package peralex.binding;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import peralex.binding.widgethalf.BindingWidgetException;
import peralex.binding.widgethalf.CollectionWidgetHalfBinding;

/**
 * A Collection binding is effectively 2 bindings nested inside each other.
 * 
 * At the outer level, an entry in a collection is bound to a entry in a widget (e.g. a table).
 * 
 * At the inner level, the fields of the entry in the table are bound to various table columns.
 * 
 * @author Noel Grandin
 */
public class CollectionBinding
{

	private static final class InnerBinding
	{
		public String modelEntryFieldName;

		public String widgetEntryFieldName;
		
		public boolean readOnly;
	}

	private final CollectionWidgetHalfBinding widgetHalfBinding;

	private final List<InnerBinding> innerBindings = new ArrayList<InnerBinding>();

	private final Collection modelCollection;

	private final PropertyDescriptor[] propertyDescriptors;

	private final Class<?> modelClass;

	private boolean readOnly = false;

	private final List<BindingValidator> validators = new ArrayList<BindingValidator>();

	/**
	 * 
	 * @throws IllegalArgumentException if the binding does not support the field data type
	 */
	public CollectionBinding(Class<?> modelClass, Collection modelCollection, CollectionWidgetHalfBinding widgetBinding)
	{
		this.modelCollection = modelCollection;
		this.widgetHalfBinding = widgetBinding;
		this.modelClass = modelClass;
		this.propertyDescriptors = PropertyUtils.getPropertyDescriptors(modelClass);
	}

	/**
	 * @param readOnly if the binding is read-only, then attempts to update the model from the UI do nothing.
	 */
	public void addInnerBinding(String modelEntryFieldName, String widgetEntryFieldName, boolean readOnly)
	{
		final Class<?> clazz = getModelEntryFieldClass(modelEntryFieldName);
		if (clazz == null)
			throw new IllegalArgumentException("no property " + modelEntryFieldName + " exists on model class "
					+ modelClass);

		if (!widgetHalfBinding.isValidModelEntryFieldClass(widgetEntryFieldName, clazz))
		{
			throw new IllegalArgumentException("binding " + widgetHalfBinding.getClass().getName() + " for "
					+ modelEntryFieldName + " does not support type " + clazz.getName());
		}
		
		InnerBinding inner = new InnerBinding();
		inner.modelEntryFieldName = modelEntryFieldName;
		inner.readOnly = readOnly;
		inner.widgetEntryFieldName = widgetEntryFieldName;
		
		this.innerBindings.add(inner);
	}

	/**
	 * if the binding is read-only, then attempts to update the model from the UI do nothing.
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/**
	 * if the binding is read-only, then attempts to update the model from the UI do nothing.
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	 * enable/disable the UI component
	 */
	public void setComponentEnabled(boolean enabled)
	{
		widgetHalfBinding.setComponentEnabled(enabled);
	}

	/**
	 * do all of the validation, but don't perform the update
	 * 
	 * @throws BindingModelUpdateException
	 */
	public void testUpdateModelFromUI() throws BindingModelUpdateException
	{
		if (readOnly)
		{
			return;
		}
		for (InnerBinding inner : this.innerBindings) {
			final Class<?> modelEntryFieldClass = getModelEntryFieldClass(inner.modelEntryFieldName);
			final Object modelEntryValueFromUI;
			try
			{
				modelEntryValueFromUI = widgetHalfBinding.getUIValue(inner.widgetEntryFieldName, modelEntryFieldClass);
			}
			catch (BindingWidgetException ex)
			{
				throw new BindingModelUpdateException(new BindingErrorField(inner.modelEntryFieldName, ex.getMessage()));
			}
			doValidation(inner.modelEntryFieldName, modelEntryFieldClass, modelEntryValueFromUI);
		}
	}

	private void doValidation(String modelObjectFieldName, Class<?> modelFieldClass, final Object modelFieldValue) throws BindingModelUpdateException
	{
		final List<BindingErrorField> errors = new ArrayList<BindingErrorField>();
		for (BindingValidator validator : validators)
		{
			String msg = validator.validate(modelFieldClass, modelFieldValue);
			if (msg != null)
				errors.add(new BindingErrorField(modelObjectFieldName, msg));
		}
		if (errors.size() > 0)
		{
			throw new BindingModelUpdateException(new BindingErrorComposite(errors.toArray(new BindingErrorField[errors
					.size()])));
		}
	}

	/**
	 * perform validation, and then do the update
	 */
	public void updateModelFromUI() throws BindingModelUpdateException
	{
		if (readOnly)
		{
			return;
		}

		for (InnerBinding inner : this.innerBindings) {
			final Class<?> modelEntryFieldClass = getModelEntryFieldClass(inner.modelEntryFieldName);
			
			final Object modelEntryFieldValueFromUI;
			try
			{
				modelEntryFieldValueFromUI = widgetHalfBinding.getUIValue(inner.widgetEntryFieldName, modelEntryFieldClass);
			}
			catch (BindingWidgetException ex)
			{
				throw new BindingModelUpdateException(new BindingErrorField(inner.modelEntryFieldName, ex.getMessage()));
			}

			// do this just in case
			doValidation(inner.modelEntryFieldName, modelEntryFieldClass, modelEntryFieldValueFromUI);

			final Object modelEntry = null; // FIXME
			setModelFieldValue(modelEntry, inner.modelEntryFieldName, modelEntryFieldValueFromUI);
		}
	}

	public void updateUIFromModel()
	{
		for (InnerBinding inner : this.innerBindings) {
			final Class<?> modelEntryFieldClass = getModelEntryFieldClass(inner.modelEntryFieldName);
			final Object modelEntry = null; // FIXME
			final Object modelFieldValue = getModelFieldValue(modelEntry, inner.modelEntryFieldName);
			widgetHalfBinding.updateUIFromModel(inner.widgetEntryFieldName, modelEntryFieldClass, modelFieldValue);
		}
	}

	public void addBindingValidator(BindingValidator validator)
	{
		validators.add(validator);
	}

	public void removeBindingValidator(BindingValidator validator)
	{
		validators.remove(validator);
	}

	private Class<?> getModelEntryFieldClass(String modelEntryFieldName)
	{
		for (PropertyDescriptor pd : this.propertyDescriptors)
		{
			if (pd.getName().equals(modelEntryFieldName))
			{
				return pd.getPropertyType();
			}
		}
		return null;
	}

	private final Object getModelFieldValue(Object modelEntry, String modelEntryFieldName)
	{
		try
		{
			return PropertyUtils.getProperty(modelEntry, modelEntryFieldName);
		}
		catch (IllegalAccessException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
		catch (InvocationTargetException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
		catch (NoSuchMethodException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
	}

	private final void setModelFieldValue(Object modelEntry, String modelEntryFieldName, Object value)
	{
		try
		{
			PropertyUtils.setProperty(modelEntry, modelEntryFieldName, value);
		}
		catch (IllegalAccessException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
		catch (InvocationTargetException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
		catch (NoSuchMethodException ex)
		{
			// should never happen
			throw new RuntimeException(ex);
		}
	}
}
