package peralex.binding.widgethalf;

import javax.swing.JSlider;

/**
 * 
 * @author Noel Grandin
 */
public class JSliderBinding extends WidgetHalfBinding {

	private final JSlider slider;

	public JSliderBinding(JSlider slider) {
		this.slider = slider;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.slider.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return modelFieldClass.isAssignableFrom(Number.class);
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = slider.getValue();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		slider.setValue(((Number) modelValue).intValue());
	}
	
}
