package peralex.binding.widgethalf;


/**
 * Used to mark objects capable of binding themselves to a model.
 * 
 * @author Noel Grandin
 */
public interface IBindableWidget {


	/**
	 * return a binding object binding myself to the specified
	 * field.
	 */
	WidgetHalfBinding createBinding();

}
