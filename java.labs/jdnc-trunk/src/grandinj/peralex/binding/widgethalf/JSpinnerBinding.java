package peralex.binding.widgethalf;

import javax.swing.JSpinner;

/**
 * 
 * @author Noel Grandin
 */
public class JSpinnerBinding extends WidgetHalfBinding {

	private final JSpinner spinner;

	public JSpinnerBinding(JSpinner combo) {
		this.spinner = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.spinner.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		if (spinner.getModel()==null) return true;
		if (spinner.getModel().getValue()==null) return true;
		return spinner.getModel().getValue().getClass()==modelFieldClass;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = spinner.getValue();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		spinner.setValue(modelValue);
	}
}
