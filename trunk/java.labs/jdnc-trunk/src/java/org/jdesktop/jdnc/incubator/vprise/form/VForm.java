package org.jdesktop.jdnc.incubator.vprise.form;

import org.jdesktop.swing.form.*;
import org.jdesktop.swing.data.*;
import org.jdesktop.swing.binding.*;
        
/**
 * A bunch of small changes to the form class to allow users to manipulate
 * the default layout.
 *
 * @author Shai Almog
 */
public class VForm extends JForm {
    /**
     * Allows us to manipulate the layout of the form while maintaining
     * the magic of automatic form construction.
     */
    private FormTemplate template;

    public VForm(FormTemplate template) {
        this.template = template;
    }
    
    /**
     * This should probably be modified. Constructors should NEVER
     * throw an exception.
     */
    public VForm(DataModel model, FormTemplate template) throws BindException {
        this.template = template;
        initActions();
        bind(model);
        pull();
    }

    public VForm() {
        template = new FormTemplate();
    }
    
    /**
     * This should probably be modified. Constructors should NEVER
     * throw an exception.
     */
    public VForm(DataModel model) throws BindException {
        template = new FormTemplate();
        initActions();
        bind(model);
        pull();
    }

    /**
     * Allows us to manipulate the layout of the form while maintaining
     * the magic of automatic form construction.
     */
    public FormTemplate getTemplate() {
        return template;
    }

    /**
     * Allows us to manipulate the layout of the form while maintaining
     * the magic of automatic form construction.
     */
    public void setTemplate(FormTemplate template) {
        this.template = template;
    }
}
