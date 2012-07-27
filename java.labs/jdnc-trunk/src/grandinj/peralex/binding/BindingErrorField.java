package peralex.binding;

/**
 * A binding error on a specific field, with a message.
 * 
 * @author Noel Grandin
 */
public class BindingErrorField extends BindingError {

	private String fieldName;
	private final String msg;
	
	public BindingErrorField(String fieldName, String msg) {
		this.fieldName = fieldName;
		this.msg = msg;
	}
	
	public BindingErrorField(String msg) {
		this.msg = msg;
	}

	public String getMessage()
	{
		return this.msg;
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
}
