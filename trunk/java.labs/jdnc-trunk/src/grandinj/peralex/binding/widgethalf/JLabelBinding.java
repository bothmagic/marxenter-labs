package peralex.binding.widgethalf;

import javax.swing.JLabel;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * 
 * @author Noel Grandin
 */
public class JLabelBinding extends WidgetHalfBinding {

	private final JLabel label;

	public JLabelBinding(JLabel label) {
		this.label = label;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.label.setEnabled(enabled);
	}
	
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return ConvertUtils.lookup(modelFieldClass)!=null;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		// labels are not editable
		return null;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		String s = ConvertUtils.convert(modelValue);
		label.setText(s);
	}
	
}
