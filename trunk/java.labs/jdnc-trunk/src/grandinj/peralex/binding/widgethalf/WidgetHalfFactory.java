package peralex.binding.widgethalf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Note: we will throw an exception if we register the same binding twice.
 * 
 * @author Noel Grandin
 */
public class WidgetHalfFactory {

	private static class Entry {
		public Class<? extends WidgetHalfBinding> bindingClazz;
		// some bindings might have multiple constructors so they can handle multiple widget types
		public Constructor<? extends WidgetHalfBinding> bindingClazzConstructor;
		// callstack where the binding was registered from
		public Throwable locationOfRegistrar;
	}
	
	private static final Map<Class<?>, Entry> bindings = new HashMap<Class<?>, Entry>();

	static {
		addBindingClass(JComboBoxBinding.class);
		addBindingClass(JCheckBoxBinding.class);
		addBindingClass(JCheckBoxMenuItemBinding.class);
		addBindingClass(JLabelBinding.class);
		addBindingClass(JListBinding.class);
		addBindingClass(JProgressBarBinding.class);
		addBindingClass(JScrollBarBinding.class);
		addBindingClass(JSliderBinding.class);
		addBindingClass(JSpinnerBinding.class);
		addBindingClass(JTextComponentBinding.class);
		addBindingClass(JToggleButtonBinding.class);
	
		// FIXME - this shouldn't be here	
		addBindingClass(JXRadioGroupBinding.class);
	}
	
	/**
	 * Subclasses of WidgetHalfBinding are expected to conform to the pattern that the constructor
	 * is declared as 
	 *	public FooHalfBinding(JFooComponent foo)
	 *
	 * This is pattern is used to pick out the component's that the binding supports.
	 * 
	 * @throws IllegalStateException if there are no constructors conforming to the correct pattern
	 */
	public static final void addBindingClass(Class<? extends WidgetHalfBinding> bindingClazz) throws IllegalStateException
	{
		Constructor [] constructors = bindingClazz.getConstructors();
		boolean foundOne = false;
		for (Constructor<? extends WidgetHalfBinding> c : constructors)
		{
			Class [] paramTypes = c.getParameterTypes();
			if (paramTypes!=null && paramTypes.length==1)
			{
				foundOne = true;
				Entry entry = new Entry();
				entry.locationOfRegistrar = new Throwable();
				entry.bindingClazz = bindingClazz;
				entry.bindingClazzConstructor = c;

				// rather fail early if people are going to do silly stuff
				Entry otherEntry = bindings.get(paramTypes[0]);
				if (otherEntry!=null) {
					throw new IllegalStateException("binding for class already exists", otherEntry.locationOfRegistrar);
				}
					
				bindings.put(paramTypes[0], entry);
			}
		}
		
		if (!foundOne)
		{
			throw new IllegalStateException("could not find a constructor that conforms to the correct pattern and indicates which component this binding supports");
		}
	}
	
	public static final WidgetHalfBinding newBinding(Object uiObject)
	{
		// find the entry that is the "closest" i.e. most specific binding
		Entry found = null;
		Class<?> testClass = uiObject.getClass();
		do {
			Entry entry = bindings.get(testClass);
			if (entry!=null)
			{
				found = entry;
				break;
			}
			testClass = testClass.getSuperclass();
		} while (testClass!=null);
		
		if (found==null) {
			throw new IllegalStateException("could not find binding for class " + uiObject.getClass());
		}
		
		try {
			WidgetHalfBinding binding = found.bindingClazzConstructor.newInstance(new Object [] { uiObject });
			return binding;
		} catch (IllegalArgumentException ex) {
			// this is one time I don't want to clutter client code with exception handling, but
			// I want to fail early.
			throw new RuntimeException(ex);
		} catch (InstantiationException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
}
