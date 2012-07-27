package peralex.binding.widgethalf;

import javax.swing.JList;

/**
 * 
 * @author Noel Grandin
 */
public class JListBinding extends WidgetHalfBinding {

	private final JList list;

	public JListBinding(JList combo) {
		this.list = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.list.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		// Don't know because I'm not sure what is in the list, or even if the list is homogeneous (which it may not be).
		return true;
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value = list.getSelectedValue();
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		list.setSelectedValue(modelValue, true);
	}
	
}
