package peralex.binding.widgethalf;

import org.jdesktop.swingx.JXRadioGroup;

/**
 * 
 * @author Noel Grandin
 */
public class JXRadioGroupBinding extends WidgetHalfBinding {

	private final JXRadioGroup<Object> radioGroup;

	public JXRadioGroupBinding(JXRadioGroup<Object> combo) {
		this.radioGroup = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.radioGroup.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return true;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		return radioGroup.getSelectedValue();
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		radioGroup.setSelectedValue(modelValue);
	}
	
}
