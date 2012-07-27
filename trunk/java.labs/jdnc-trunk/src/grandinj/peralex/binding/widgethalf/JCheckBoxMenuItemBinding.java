package peralex.binding.widgethalf;

import javax.swing.JCheckBoxMenuItem;

/**
 * 
 * @author Noel Grandin
 */
public class JCheckBoxMenuItemBinding extends WidgetHalfBinding {

	private final JCheckBoxMenuItem checkbox;

	public JCheckBoxMenuItemBinding(JCheckBoxMenuItem checkbox) {
		this.checkbox = checkbox;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.checkbox.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return modelFieldClass==Boolean.class || modelFieldClass==Boolean.TYPE;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = Boolean.valueOf(checkbox.isSelected());
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		checkbox.setSelected((Boolean) modelValue);
	}
	
}
