package peralex.binding;


/**
 * thrown when attempting to update a model from the UI and there are validation errors.
 * 
 * @author Noel Grandin
 */
public class BindingModelUpdateException extends Exception {

	private final BindingError error;
	
	public BindingModelUpdateException(BindingError error)
	{
	    super(error.toString());
	    this.error = error;
	}

	public BindingError getBindingError()
	{
		return error;
	}
}
