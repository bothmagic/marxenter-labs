package peralex.binding.widgethalf;

import javax.swing.JTable;

public class JTableBinding extends CollectionWidgetHalfBinding {

	private final JTable table;

	public JTableBinding(JTable table) {
		this.table = table;
	}
	
	@Override
	public Object getUIValue(String widgetEntryFieldName, Class<?> modelEntryFieldClass) throws BindingWidgetException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isValidModelEntryFieldClass(String widgetEntryFieldName, Class<?> modelEntryFieldClass)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setComponentEnabled(boolean enabled)
	{
		table.setEnabled(enabled);
	}
	
	@Override
	public void updateUIFromModel(String widgetEntryFieldName, Class<?> modelEntryFieldClass, Object modelEntryFieldValue)
	{
		// TODO Auto-generated method stub
	}
}
