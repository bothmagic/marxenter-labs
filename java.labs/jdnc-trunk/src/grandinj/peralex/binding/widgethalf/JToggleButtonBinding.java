package peralex.binding.widgethalf;

import javax.swing.JToggleButton;

/**
 * 
 * @author Noel Grandin
 */
public class JToggleButtonBinding extends WidgetHalfBinding {

	private final JToggleButton toggleButton;

	public JToggleButtonBinding(JToggleButton combo) {
		this.toggleButton = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.toggleButton.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return modelFieldClass==Boolean.class || modelFieldClass==Boolean.TYPE;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = Boolean.valueOf(toggleButton.isSelected());
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		toggleButton.setSelected((Boolean) modelValue);
	}
	
}
