package peralex.binding.widgethalf;

import javax.swing.JProgressBar;

/**
 * 
 * @author Noel Grandin
 */
public class JProgressBarBinding extends WidgetHalfBinding {

	private final JProgressBar progressBar;

	public JProgressBarBinding(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.progressBar.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return modelFieldClass.isAssignableFrom(Number.class);
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = progressBar.getValue();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		progressBar.setValue(((Number) modelValue).intValue());
	}
	
}
