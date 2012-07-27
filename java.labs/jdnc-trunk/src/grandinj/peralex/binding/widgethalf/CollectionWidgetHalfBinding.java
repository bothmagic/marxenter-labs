package peralex.binding.widgethalf;

/**
 * the base class of all widget half bindings.
 * 
 * Subclasses are expected to conform to the pattern that the constructor
 * is declared as 
 *	public FooBinding(JFooComponent component, and then other stuff)
 *
 * This pattern is used by the DefaultBindingFactory to pick out the component's that the binding supports.
 *
 * FIXME maybe this should be an interface ?
 * 
 * @author Noel Grandin
 */
public abstract class CollectionWidgetHalfBinding {

	public CollectionWidgetHalfBinding()
	{
	}
	
	/**
	 * we check this explicitly so that we can fail earlier i.e. at bind time rather than than
	 * only when we start moving data around.
	 */
	public abstract boolean isValidModelEntryFieldClass(String widgetEntryFieldName, Class<?> modelEntryFieldClass);
	
	/**
	 * enable/disable the UI component
	 */
	public abstract void setComponentEnabled(boolean enabled);
	
	/**
	 * The Widget should return the UI value as the appropriate type for the model field.
	 * 
	 * @param modelEntryFieldClass the type of the model's field value
	 */
	public abstract Object getUIValue(String widgetEntryFieldName, Class<?> modelEntryFieldClass) throws BindingWidgetException;
	
	/**
	 * The Widget should update the UI, perhaps converting the model value to the appropriate type.
	 * 
	 * @param modelEntryFieldClass the type of the model entry's field value, we need this because the value
	 *   may be null.
	 */
	public abstract void updateUIFromModel(String widgetEntryFieldName, Class<?> modelEntryFieldClass, Object modelEntryFieldValue);
	
}

