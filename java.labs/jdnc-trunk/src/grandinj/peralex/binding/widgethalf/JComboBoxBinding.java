package peralex.binding.widgethalf;

import javax.swing.JComboBox;

/**
 * 
 * @author Noel Grandin
 */
public class JComboBoxBinding extends WidgetHalfBinding {

	private final JComboBox combo;

	public JComboBoxBinding(JComboBox combo) {
		this.combo = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.combo.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		// Don't know because I'm not sure what is in the combobox, or even if the combobox is homogeneous (which it may not be).
		return true;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = combo.getSelectedItem();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		combo.setSelectedItem(modelValue);
	}
	
}
