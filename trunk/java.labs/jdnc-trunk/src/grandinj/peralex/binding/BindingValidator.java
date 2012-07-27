package peralex.binding;

/**
 * Interface to the classes that validate bindings.
 * 
 * @author Noel Grandin
 */
public interface BindingValidator {

	/**
	 * @param modelFieldClass the type of the model's field value
	 * @param modelValueFromUI the object to be validated
	 * @return null if value valid, otherwise an error message
	 */
	String validate(Class<?> modelFieldClass, Object modelValueFromUI);
}
