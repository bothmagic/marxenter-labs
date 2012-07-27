package peralex.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.beanutils.MethodUtils;

import peralex.binding.widgethalf.IBindableWidget;
import peralex.binding.widgethalf.WidgetHalfBinding;
import peralex.binding.widgethalf.WidgetHalfFactory;

/**
 * Implements 2 kinds of binding : model<->UI and UI->action.
 * 
 * The UI->action binding uses reflection to find methods and invoke them. This
 * reduces the amount of the code necessary in UI classes in exchange for an
 * increased possibility of making mistakes. We mitigate this by searching for
 * the invokee at bind time, rather than at invoke time. This layer might seem
 * redundant, since we already have UI<->binding, but some UI code does not
 * really have a "model", it simply executes some kind of command.
 * 
 * @author Noel Grandin
 */
public class UIBinder {

	/**
	 * Defining this class just helps with code clarity.
	 */
	public static class BindingList {
		private final Set<Binding> bindings = new HashSet<Binding>();
		
		public void clear() {
			bindings.clear();
		}
	}

	/**
	 * UIBinder is not meant to be instantiated.
	 */
	private UIBinder() {}
	
	/**
	 * enable/disable the UI component
	 */
	public static void setEnabled(BindingList bindingList, boolean enabled) {
		for (Binding binding : bindingList.bindings) {
			binding.setComponentEnabled(enabled);
		}
	}
	
	/**
	 * Bind a field on the data object to a component on the UI using reflection.
	 */
	public static void bind(BindingList bindingList, JComponent uiComponent, Object modelObject, String fieldName) {
		bind(bindingList, uiComponent, modelObject, fieldName, false, null);
	}
	
	/**
	 * Bind a field on the data object to a component on the UI using reflection.
	 */
	public static void bind(BindingList bindingList, JComponent uiComponent, Object modelObject, String fieldName, boolean readOnly) {
		bind(bindingList, uiComponent, modelObject, fieldName, readOnly, null);
	}
	
	/**
	 * Bind a field on the data object to a component on the UI using reflection.
	 * 
	 * @param readOnly if true, the field is disabled
	 */
	public static void bind(BindingList bindingList, JComponent uiComponent, Object modelObject, String fieldName, boolean readOnly, final BindingValidator validator) {
		/* There are a whole bunch of fields BindingFactory does not know how to bind */
		final WidgetHalfBinding rawBinding;
		
		if (uiComponent instanceof IBindableWidget) {
			final IBindableWidget iBindableWidget = (IBindableWidget) uiComponent;
			rawBinding = iBindableWidget.createBinding();
			
		} else {
			rawBinding = WidgetHalfFactory.newBinding(uiComponent);
		}
		
		if (rawBinding==null) {
			throw new IllegalStateException("cannot construct a UI half-binding for fieldname: " + fieldName);
		}
		
		final Binding binding = new Binding(modelObject, fieldName, rawBinding);
		/* setup readonly */
		if (readOnly) {
			binding.setReadOnly(true);
		}
		
		/* setup validator */
		if (validator!=null) {
			binding.addBindingValidator(validator);
		}
		
		bindingList.bindings.add(binding);
	}

	public static void updateModelFromUI(BindingList bindingList) 
		throws BindingModelUpdateException
	{
		// test first
		for (Binding binding : bindingList.bindings) {
			binding.testUpdateModelFromUI();
		}
		// and then update, so that we don't update the model object if there is a problem
		for (Binding binding : bindingList.bindings) {
			binding.updateModelFromUI();
		}
	}

	public static void updateUIFromModel(BindingList bindingList) {
		for (Binding binding : bindingList.bindings) {
			binding.updateUIFromModel();
		}
	}

	private static Method findMethod(Object callTarget, String methodName, Class<?> param) {
		Method method = null;
		if (param != null) {
			method = MethodUtils.getAccessibleMethod(callTarget.getClass(), methodName, new Class[] { param });
		}
		if (method == null) {
			// sometimes we bind to a method with no parameters
			method = MethodUtils.getAccessibleMethod(callTarget.getClass(), methodName, new Class [] {});
		}
		if (method == null) {
			throw new IllegalArgumentException("could not find method " + methodName + " on class "
				+ callTarget.getClass().getName());
		}
		return method;
	}

	private static void invokeListenerCallback(Object callTarget, Method method, boolean b) {
		invokeListenerCallback(callTarget, method, b ? Boolean.TRUE : Boolean.FALSE);
	}

	private static void invokeListenerCallback(Object callTarget, Method method, Object value) {
		try {
			if (method.getParameterTypes().length == 0) {
				method.invoke(callTarget, (Object[]) null);
			} else if (method.getParameterTypes().length == 1) {
				method.invoke(callTarget, new Object[] { value });
			} else {
				throw new IllegalStateException("too many parameters - can't call this method " + method.getName());
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * binds an update listener to a method on the target object. The method
	 * must either take no parameters or a single parameter compatible with the
	 * values stored in the combobox.
	 */
	public static void addUpdateListener(final Object callTarget, JComboBox comboBox, final String methodName) {
		final Method method = findMethod(callTarget, methodName, Object.class);
		comboBox.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent event) {
				if (event.getStateChange()!=ItemEvent.SELECTED) {
					return;
				}
				invokeListenerCallback(callTarget, method, event.getItem());
			}
		});
	}

	/**
	 * Binds an update listener to a method on the target object. The method
	 * must either take no parameters or a single parameter compatible with the
	 * values stored in the textfield.
	 */
	public static void addFocusLostListener(final Object callTarget, final JTextField textField, final String methodName) {
		final Method method = findMethod(callTarget, methodName, String.class);
		textField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				invokeListenerCallback(callTarget, method, textField.getText());
			}
		});
	}

	/**
	 * Binds an update listener to a method on the target object. The method
	 * must either take no parameters or a single boolean parameter.
	 */
	public static void addActionListener(final Object callTarget, final AbstractButton abstractButton, final String methodName) {
		final Method method = findMethod(callTarget, methodName, boolean.class);
		abstractButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invokeListenerCallback(callTarget, method, ((AbstractButton) e.getSource()).isSelected());
			}
		});
	}

	/**
	 * Binds an update listener to a method on the target object. The method
	 * must either take no parameters or a single boolean parameter.
	 */
	public static void addItemListener(final Object callTarget, final AbstractButton abstractButton, final String methodName) {
		final Method method = findMethod(callTarget, methodName, boolean.class);
		abstractButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange()!=ItemEvent.SELECTED) {
					return;
				}
				invokeListenerCallback(callTarget, method, ((AbstractButton) event.getSource()).isSelected());
			}
		});
	}
	
	/**
	 * Binds an update listener to a method on the target object. The method
	 * must either take no parameters or a single parameter compatible with the
	 * values stored in the spinner.
	 */
	public static void addChangeListener(final Object callTarget, final JSpinner spinner, final String methodName) {
		final Method method = findMethod(callTarget, methodName, Object.class);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				invokeListenerCallback(callTarget, method, ((JSpinner) event.getSource()).getValue());
			}
		});
	}

	/**
	 * Bind the data object to the UI using reflection
	 * This will bind field names in the dataObject and uiObject that match.
	 * Anything that does not match will cause an exception.
	 */
	public static final void bindAll(BindingList bindingList, Object dataObject, Object uiObject) {
		bindAll(bindingList, dataObject, uiObject, null);
	}
	
	/** 
	 * Parameter marker object. Used to indicate that we should 
	 * ignore fields on the data model object for which we can't find components
	 * on the UI object.
	 */
	public static final String [] DO_NOT_TEST = new String [] {};
	
	/**
	 * Bind the data object to the UI using reflection
	 * 
	 * @param modelObject the persistent data object to bind to the UI
	 * @param componentFieldExceptions array of UI JComponent fields that do not need binding
	 * 
	 * Note: I don't test for fields on the dataobject that are not bound because
	 * they are not as important as UI fields that don't bind.
	 * Also Hibernate does CGLIB magic that adds fields at runtime to the data objects.
	 */
	public static void bindAll(BindingList bindingList, Object modelObject, Object uiObject, String [] componentFieldExceptions) {
		// simplify the code by removing the need for null checks further down
		if (componentFieldExceptions==null) {
			componentFieldExceptions = new String [] {};
		} else {
			Arrays.sort(componentFieldExceptions);
		}
		
		try {
			final BeanInfo info = Introspector.getBeanInfo(modelObject.getClass());
			final PropertyDescriptor[] props = info.getPropertyDescriptors();
			final List<Field> boundComponentFields = new ArrayList<Field>();
			for (int i = 0; i < props.length; i++) {
				final PropertyDescriptor prop = props[i];
				if (prop.getPropertyType()==null) { 
					// Indexed properties return null if non-indexed access is not supported
				} else if (prop.getPropertyType().equals(Set.class)) {
					// ignore
				} else if (prop.getName().equals("versionNo")) {
					// ignore
				} else if (prop.getName().equals("class")) {
					// ignore
				} else {
					try {
						final Field componentField = uiObject.getClass().getField(prop.getName());
						final JComponent component = (JComponent) componentField.get(uiObject);
						boundComponentFields.add(componentField);
						UIBinder.bind(bindingList, component, modelObject, prop.getName());
					} catch (NoSuchFieldException ex) {
						// Ignore fields for which we can't find components.
					}
				}
			}
			
			/* Verify that we bound all of the fields correctly.
			 * This may seem excessive, but since we're using reflection, it is very easy
			 * to make a mistake and end up with a field not being updated in the database.
			 */
			if (componentFieldExceptions!=DO_NOT_TEST) {
				final List<String> noSuchFields = new ArrayList<String>();
				final Field [] componentFields = uiObject.getClass().getFields();
				for (int i=0; i<componentFields.length; i++) {
					final Field field = componentFields[i];
					if (JComponent.class.isAssignableFrom(field.getType())) {
						if (!boundComponentFields.contains(field)) {
							if (Arrays.binarySearch(componentFieldExceptions, field.getName())<0) {
								noSuchFields.add(field.getName());
							}
						}
					}
				}
				/* throw one exception rather than many to reduce the number of compile/debug
				 * cycles I do. */
				if (!noSuchFields.isEmpty()) {
					throw new IllegalStateException("unbound JComponent fields " + toString(noSuchFields));
				}
			}
		} catch (IntrospectionException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static String toString(final List<String> list) {
		String s = "(";
		for (Iterator<String> iter=list.iterator(); iter.hasNext(); ) {
			s += iter.next();
			if (iter.hasNext()) {
				s += ", ";
			}
		}
		s += ")";
		return s;
	}
}