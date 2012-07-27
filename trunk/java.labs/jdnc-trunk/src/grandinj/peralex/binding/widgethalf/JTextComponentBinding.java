package peralex.binding.widgethalf;

import javax.swing.text.JTextComponent;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

/**
 * 
 * @author Noel Grandin
 */
public class JTextComponentBinding extends WidgetHalfBinding {

	private final JTextComponent textComponent;

	public JTextComponentBinding(JTextComponent combo) {
		this.textComponent = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.textComponent.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return ConvertUtils.lookup(modelFieldClass) != null;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass) throws BindingWidgetException
	{
		String s = textComponent.getText();
		try {
			Object value = ConvertUtils.convert(s, modelFieldClass);
			return value;
		} catch (ConversionException ex) {
			// should never happen
			throw new BindingWidgetException(ex.getMessage());
		}
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		String s = ConvertUtils.convert(modelValue);
		textComponent.setText(s);
	}
	
}
