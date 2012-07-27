package peralex.binding.widgethalf;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jdesktop.swingx.JXDatePicker;

/**
 * 
 * @author Noel Grandin
 */
public class JXDatePickerBinding extends WidgetHalfBinding {

	private final JXDatePicker picker;

	public JXDatePickerBinding(JXDatePicker combo) {
		this.picker = combo;
	}

	@Override
	public void setComponentEnabled(boolean enabled) {
		this.picker.setEnabled(enabled);
	}
	
	@Override
	public boolean isValidModelFieldClass(Class<?> modelFieldClass)
	{
		return (modelFieldClass == Date.class)
			|| (modelFieldClass == Calendar.class)
			|| (modelFieldClass == Long.class);
	}
	
	@Override
	public Object getUIValue(Class<?> modelFieldClass)
	{
		final Object value;
		if (modelFieldClass == Date.class) {
			value = picker.getDate();
		} else if (modelFieldClass == Calendar.class) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(picker.getDateInMillis());
			value = cal;
		} else if (modelFieldClass == Long.class) {
			value = Long.valueOf(picker.getDateInMillis());
		} else {
			// should never happen
			throw new IllegalStateException("cannot convert this model field class");
		}
		
		return value;
	}
	
	@Override
	public void updateUIFromModel(Class<?> modelFieldClass, Object modelValue)
	{
		if (modelFieldClass == Date.class) {
			picker.setDate((Date) modelValue);
		} else if (modelFieldClass == Calendar.class) {
			picker.setDateInMillis(((Calendar) modelValue).getTimeInMillis());
		} else if (modelFieldClass == Long.class) {
			picker.setDateInMillis(((Long) modelValue).longValue());
		}
	}
	
}
