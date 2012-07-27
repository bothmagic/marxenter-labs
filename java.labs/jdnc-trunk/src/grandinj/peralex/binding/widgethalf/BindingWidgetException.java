package peralex.binding.widgethalf;

/**
 * Thrown when attempting to get a value from a widget if the widget is in an inconsistent state.
 * 
 * @author Noel Grandin
 */
public class BindingWidgetException extends Exception {

	public BindingWidgetException(String msg)
	{
		super(msg);
	}
}
