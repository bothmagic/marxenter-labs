/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.combobox.ListComboBoxModel;

/**
 * 
 * @author Karl George Schaefer
 */
public class AutoSuggestDecorator {
    public static void decorate(JList list, JTextComponent textComponent) {
        decorate(textComponent, list.getModel());
        //TODO watch for model changes and update
        AutoCompleteDecorator.decorate(list, textComponent);
    }

    public static void decorate(JList list, JTextComponent textComponent,
            ObjectToStringConverter stringConverter) {
        decorate(textComponent, list.getModel());
        //TODO watch for model changes and update
        AutoCompleteDecorator.decorate(list, textComponent, stringConverter);
    }

    public static void decorate(JTextComponent textComponent, AutoCompleteDocument document,
            AbstractAutoCompleteAdaptor adaptor) {
        AutoCompleteDecorator.decorate(textComponent, document, adaptor);
    }

    public static void decorate(JTextComponent textComponent, List<?> items, boolean strictMatching) {
        decorate(textComponent, new ListComboBoxModel(items));
        
        AutoCompleteDecorator.decorate(textComponent, items, strictMatching);
    }

    public static void decorate(JTextComponent textComponent, List<?> items,
            boolean strictMatching, ObjectToStringConverter stringConverter) {
        decorate(textComponent, new ListComboBoxModel(items));
        
        AutoCompleteDecorator.decorate(textComponent, items, strictMatching, stringConverter);
    }
    
    private static void decorate(JTextComponent focusable, ListModel model) {
        focusable.addFocusListener(new AutoSuggestController(focusable, new AutoSuggestPopup(model)));
    }
}
