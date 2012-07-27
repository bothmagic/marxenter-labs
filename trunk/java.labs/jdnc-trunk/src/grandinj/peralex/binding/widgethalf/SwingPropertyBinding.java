package peralex.binding.widgethalf;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This binds to any swing property.
 * 
 * @author Noel Grandin
 */
public class SwingPropertyBinding extends WidgetHalfBinding {

	private final Component comp;
	private Object newComponentPropertyValue;
	private final Method setterMethod;
	
	public SwingPropertyBinding(Component comp, String propertyName, Class<?> modelFieldClass) {
		this.comp = comp;
		
		// add listener
		this.comp.addPropertyChangeListener(propertyName, new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt) {
				newComponentPropertyValue = evt.getNewValue();
			}
		});
		
		// get the method for updating the UI object
		try {
			final String setMethodName = "set" + propertyName.substring(0,1).toUpperCase() + 
				propertyName.substring(1);
			this.setterMethod = this.comp.getClass().getMethod(setMethodName, new Class [] { modelFieldClass });
		} catch (SecurityException ex) {
			// I don't like cluttering client code with reflection exception handling, but we need to fail early rather than continue.
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.comp.setEnabled(enabled);
	}
	
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		// since we found a setter method in the constructor, we know we're good.
		return true;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		return newComponentPropertyValue;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		try {
			setterMethod.invoke(comp, new Object [] { modelValue });
		} catch (IllegalArgumentException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			// should never happen
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			// should never happen
			throw new RuntimeException(ex);
		}
	}
	
}
