/*
 * Created on 17.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jdesktop.application.Action;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.beansbindingx.validator.NotEmptyValidator;

/**
 * Buffered presentation model of Album. 
 * 
 */
@SuppressWarnings("rawtypes")
public class AlbumModel extends Album {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AlbumModel.class
            .getName());
    // hack around disallowed binding to null?
//    private Album nullWrappee = new Album();
    private Album wrappee;
    
    private BindingGroupBean context;
    private boolean buffering;
    private Map<String, Validator> validators;
    
    public AlbumModel() {
        super();
        initBinding();
        initValidators();
    }
    
    @Action (enabledProperty = "buffering")
    public void apply() {
        if ((wrappee == null)) 
//                || (wrappee == nullWrappee)) 
            return;
        context.saveAndNotify();
    }

    @Action (enabledProperty = "buffering")
    public void discard() {
        if (wrappee == null) return;
        context.unbind();
        context.bind();
    }
    
    private void initBinding() {
        initPropertyBindings();
        initBufferingControl();
    }

    /**
     * 
     */
    private void initBufferingControl() {
        BindingGroup bufferingContext = new BindingGroup();
        // needs change-on-type in main binding to be effective
        bufferingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                context, BeanProperty.create("dirty"), 
                this, BeanProperty.create("buffering")));
        bufferingContext.bind();
    }

    /**
     * Buffer wrappee's properties to this.
     */
    private void initPropertyBindings() {
        context = new BindingGroupBean(true);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                wrappee, BeanProperty.create("artist"), 
                this, BeanProperty.create("artist")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                wrappee, BeanProperty.create("title"), 
                this, BeanProperty.create("title")));
        // binding ... hmm .. was some problem with context cleanup 
        // still a problem in revised binding? Yes - because
        // it has the side-effect of changing the composer property
        // need to bind th composer later
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                 wrappee, BeanProperty.create("classical"), 
                 this, BeanProperty.create("classical")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                wrappee, BeanProperty.create("composer"), 
                this, BeanProperty.create("composer")));
        context.bind();
    }

    private void initValidators() {
        validators = new HashMap<String, Validator>();
        final NotEmptyValidator notEmptyValidator = new NotEmptyValidator();
        validators.put("title", notEmptyValidator);
        validators.put("artist", notEmptyValidator);
        Validator<String> validator = new Validator<String>() {

            @Override
            public Result validate(String value) {
                if (isClassical()) {
                    return notEmptyValidator.validate(value);
                }
                return null;
            }
            
        };
        validators.put("composer", validator);
    }
    
    public void setAlbum(Album wrappee) {
        
        Object old = getAlbum();
        boolean oldEditEnabled = isEditEnabled();
        this.wrappee = wrappee;
        context.setSourceObject(wrappee);
//        if (old != null) {
//            context.unbind();
//        }
//        if (wrappee == null) {
//            wrappee = nullWrappee;
//        }
//        this.wrappee = wrappee;
//        for (Binding binding : context.getBindings()) {
//            // this does not reset the old value if wrappee ==  null
//            // need to actually bind to reset
//            binding.setSourceObject(wrappee);
//        }
////        if (wrappee != null)
//        context.bind();
        firePropertyChange("album", old, getAlbum());
        firePropertyChange("editEnabled", oldEditEnabled, isEditEnabled());
    }

    public boolean isEditEnabled() {
        return (wrappee != null); // && (wrappee != nullWrappee);
    }
    
    
    public boolean isComposerEnabled() {
        return isClassical();
    }
 
    /**
     * Overridden to fire a composerEnabled for the sake of the view.
     */
    @Override
    public void setClassical(boolean classical) {
        boolean old = isComposerEnabled();
        super.setClassical(classical);
        firePropertyChange("composerEnabled", old, isComposerEnabled());
    }

    
    public boolean isBuffering() {
        return buffering;
    }

    public void setBuffering(boolean buffering) {
        boolean old = isBuffering();
        this.buffering = buffering;
        firePropertyChange("buffering", old, isBuffering());
    } 

    public Validator getValidator(String property) {
        return validators.get(property);
    }
    /**
     * Public as an implementation artefact - binding cannot handle
     * write-only properrties? fixed in post-0.61
     * @return
     */
    public Album getAlbum() {
        return wrappee;
    }


}
