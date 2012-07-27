package peralex.binding;

import java.util.Collection;

public class BindingErrorComposite extends BindingError {

	private final BindingError [] childErrors;
	
	public BindingErrorComposite(BindingError [] childErrors) {
		this.childErrors = childErrors;
	}
	
	public BindingErrorComposite(Collection<BindingError> childErrors) {
		this.childErrors = childErrors.toArray(new BindingError [childErrors.size()]);
	}
	
	/** FIXME = this should be an immutable list view */
	public BindingError [] getChildErrors()
	{
		return this.childErrors;
	}
}
