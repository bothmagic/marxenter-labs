package peralex.binding;

public class BindingErrorGeneral extends BindingError {

	private final String msg;
	
	public BindingErrorGeneral(String msg)
	{
		this.msg = msg;
	}
	
	public String getMessage()
	{
		return msg;
	}
}
