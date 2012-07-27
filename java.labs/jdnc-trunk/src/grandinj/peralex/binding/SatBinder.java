package peralex.binding;

import java.lang.reflect.Field;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

/**
 * Some methods specific to the Satellite project.
 * 
 * @author Noel Grandin
 */
public class SatBinder {

	/**
	 * Find fields on a UI object that match fields in a data object, and updates the UI
	 */
	public static void updateUI(Object uiObject, Object dataObject) {
		try {
			final Field [] fields = dataObject.getClass().getDeclaredFields();
			for (int i=0; i<fields.length; i++) {
				final Field dataField = fields[i];
				final Field uiField;
				try {
					uiField = uiObject.getClass().getField(dataField.getName());
				} catch (NoSuchFieldException ex) {
					continue;
				}
				
				if (JComboBox.class.isAssignableFrom(uiField.getType())) {
					JComboBox comboBox = (JComboBox) uiField.get(uiObject);
					// don't change it if the user is trying to edit the value
					if (!comboBox.isFocusOwner() 
						&& !comboBox.getEditor().getEditorComponent().isFocusOwner())
					{
						comboBox.setSelectedItem(dataField.get(dataObject));
					}
				} else if (JSpinner.class.isAssignableFrom(uiField.getType())) {
					JSpinner spinner = (JSpinner) uiField.get(uiObject);
					spinner.setValue(dataField.get(dataObject));
				} else if (JCheckBox.class.isAssignableFrom(uiField.getType())) {
					JCheckBox checkbox = (JCheckBox) uiField.get(uiObject);
					checkbox.setSelected(((Boolean)dataField.get(dataObject)).booleanValue());
				} else if (JTextComponent.class.isAssignableFrom(uiField.getType())) {
					JTextComponent textField = (JTextComponent) uiField.get(uiObject);
					// don't change it if the user is trying to edit the value
					if (!textField.isFocusOwner()) {
						Object data = dataField.get(dataObject);
						textField.setText(data==null ? null : data.toString());
					}
				} else {
					throw new IllegalStateException("cannot update field type " + uiField.getType() + ". field=" + uiField.getName());
				}
			}
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

}
