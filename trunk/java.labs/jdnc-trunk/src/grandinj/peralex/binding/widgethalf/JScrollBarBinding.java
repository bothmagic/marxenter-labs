package peralex.binding.widgethalf;

import javax.swing.JScrollBar;

/**
 * 
 * @author Noel Grandin
 */
public class JScrollBarBinding extends WidgetHalfBinding {

	private final JScrollBar scrollBar;

	public JScrollBarBinding(JScrollBar scrollBar) {
		this.scrollBar = scrollBar;
		
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.scrollBar.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return modelFieldClass.isAssignableFrom(Number.class);
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = scrollBar.getValue();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		scrollBar.setValue(((Number) modelValue).intValue());
	}
	
}
