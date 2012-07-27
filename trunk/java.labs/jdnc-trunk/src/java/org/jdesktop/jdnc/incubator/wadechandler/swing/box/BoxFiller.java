/*
 * $Id: BoxFiller.java 281 2005-01-12 07:56:44Z wadechandler $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * BoxFiller.java
 *
 * Created on December 7, 2004, 11:28 AM
 */

package org.jdesktop.jdnc.incubator.wadechandler.swing.box;

import java.awt.Dimension;
import java.beans.PropertyChangeSupport;

/**
 * This class is used to make working with box layouts simpler from a gui designer.
 * It has been designed to be a nice bean, however it works best with the BoxLayout.
 * The best use of this component is to use it with a BoxLayout adding other containers
 * to each BoxFiller and even BoxFillers to BoxFillers setting the layout of each container
 * as needed.  This way a nice expandable gui/ui can be created using the standard
 * java layouts and classes.
 * @author Wade Chandler
 * @version 1.0
 */
public class BoxFiller
        extends javax.swing.Box.Filler
        
{
    
    /** 
     * Creates a new instance of BoxFiller.  This BoxFiller will be glue
     * at first.  This makes it a little hard to work with BoxFiller with layouts that
     * can't handle components not having a minimum, preferred, or maximum size as the
     * generic "glue" doesn't have these properties set.  This may be changed in the future
     * to default to a rigid area.  This works well with a BoxLayout.
     */
    public BoxFiller() {
        super(null, null, null);
        this.horizontalGlue = false;
        this.verticalGlue = false;
        this.glue = false;
        this.horizontalStrut = -1;
        this.verticalStrut = -1;
        this.rigidArea = null;
        //todo see if this should really default to glue.  if it defaulted to a rigid area it would work better in an IDE
        this.setupDefaultFiller();
        this.oldValues = new OldValues();
    }
    
    private BoxFiller(javax.swing.Box.Filler filler) {
        this();
        this.mimic(filler);
    }
    
    private void setupDefaultFiller() {
        this.mimic(javax.swing.Box.createGlue());
        this.glue = true;
    }
    
    /**
     *Utility method used to make it easier to shift this BoxFiller into the shape
     *of other Components.  This method helps to internally use the class
     *javax.swing.Box.Filler so there isn't as much source replication.  Sure this uses
     *a little more resources (until the used object is cleaned up), but it makes it
     *a little easier to maintain, or so that is the theory.  Maybe/maybe not.
     *@param filler the component to mimic the shape of.
     */
    private void mimic(java.awt.Component filler) {
        try {
            this.changeShape(filler.getMinimumSize(), filler.getPreferredSize(), filler.getMaximumSize());
        } catch(Throwable e) {
            //we'll swallow this one.
        }
    }
    
    /**
     *Used to make it easier to reset all of the values to their defaults without
     *calling any PropertyChangeEvents until they should really be called.
     */
    private void resetBeanSettingsForFiller() {
        this.horizontalGlue = false;
        this.verticalGlue = false;
        this.glue = false;
        this.horizontalStrut = -1;
        this.verticalStrut = -1;
        this.rigidArea = null;
    }

    /**
     *Used to tell if this BoxFiller is horizontal glue
     *@return whether this BoxFiller is horizontal glue or not
     */    
    public boolean isHorizontalGlue() {
        return horizontalGlue;
    }
    
    /**
     *Used to turn this BoxFiller into horizontal glue.
     *@param horizontalGlue whether or not this should be horizontal glue.  If horizontal
     *       glue is already true and false is passed then this BoxFiller becomes glue.
     */    
    public void setHorizontalGlue(boolean horizontalGlue) {
        boolean cv = this.isHorizontalGlue();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.horizontalGlue = horizontalGlue;
        if( horizontalGlue ) {
            this.mimic(javax.swing.Box.createHorizontalGlue());
        } else {
            if( cv ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }

    /**
     *Used to tell if this BoxFiller is vertical glue
     *@return whether this BoxFiller is vertical glue or not
     */    
    public boolean isVerticalGlue() {
        return verticalGlue;
    }

    /**
     *Used to turn this BoxFiller into vertical glue.
     *@param verticalGlue whether or not this should be vertical glue.  If vertical
     *       glue is already true and false is passed then this BoxFiller becomes glue.
     */
    public void setVerticalGlue(boolean verticalGlue) {
        boolean cv = this.isVerticalGlue();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.verticalGlue = verticalGlue;
        if( verticalGlue ) {
            this.mimic(javax.swing.Box.createVerticalGlue());
        } else {
            if( cv ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }
    
    /**
     *Used to tell if this BoxFiller is glue
     *@return whether this BoxFiller is glue or not
     */
    public boolean isGlue() {
        return glue;
    }
    
    /**
     *Used to turn this BoxFiller into glue.
     *@param glue whether or not this should be glue.  This is kind of funny because
     *       if glue is already true and false is passed this will become glue 
     *       as the default is glue.
     */
    public void setGlue(boolean glue) {
        boolean cv = this.isGlue();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.glue = glue;
        if( glue ) {
            this.mimic(javax.swing.Box.createGlue());
        } else {
            if( cv ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }

    /**
     *Method used to get the horizontal strut value of this BoxFiller.
     *This value will be a -1 if it isn't set.
     *@return the horizontal strut value.  less than 0 or -1 if not set
     */    
    public int getHorizontalStrut() {
        return horizontalStrut;
    }

    /**
     *Used to turn this BoxFiller into a horizontal strut.
     *@param horizontalStrut the size of the horizontal strut.  must be greater than
     *       -1.  If this value is not greater than -1 then this BoxFiller turns
     *       into Glue.
     */    
    public void setHorizontalStrut(int horizontalStrut) {
        int cv = this.getHorizontalStrut();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.horizontalStrut = horizontalStrut;
        if( horizontalStrut > -1 ) {
            this.mimic(javax.swing.Box.createHorizontalStrut(horizontalStrut));
        } else {
            if( cv < 0 ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }
    
    /**
     *Method used to get the vertical strut value of this BoxFiller.
     *This value will be a -1 if it isn't set.
     *@return the vertical strut value.  less than 0 or -1 if not set
     */
    public int getVerticalStrut() {
        return verticalStrut;
    }
    
    /**
     *Used to turn this BoxFiller into a vertical strut.
     *@param verticalStrut the size of the vertical strut.  must be greater than
     *       -1.  If this value is not greater than -1 then this BoxFiller turns
     *       into Glue.
     */
    public void setVerticalStrut(int verticalStrut) {
        int cv = this.getVerticalStrut();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.verticalStrut = verticalStrut;
        if( verticalStrut > -1 ) {
            this.mimic(javax.swing.Box.createVerticalStrut(verticalStrut));
        } else {
            if( cv < 0 ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }
    
    /**
     *Used to get the rigid area or Dimension currently used by this object.
     *This value may very well be null if it isn't a rigidArea.
     *@return the rigid area of this filler
     */
    public java.awt.Dimension getRigidArea() {
        return rigidArea;
    }
    
    /**
     *Used to turn this BoxFiller into a rigid area.  @see javax.swing.Box
     *@param rigidArea the Dimension to use for the rigid area.  If this value
     *       is null then this BoxFiller turns into glue.
     */
    public void setRigidArea(java.awt.Dimension rigidArea) {
        java.awt.Dimension cv = this.getRigidArea();
        this.setupOldValues();
        this.resetBeanSettingsForFiller();
        this.rigidArea = rigidArea;
        if( rigidArea != null ) {
            this.mimic(javax.swing.Box.createRigidArea(rigidArea));
        } else {
            if( cv != null ) {
                this.setupDefaultFiller();
            }
        }
        this.fireChangeEventsWithExclusion(-1);
    }
    
    /**
     *Utility method used internally to fire all of the PropertyChangeEvents when
     *one of the 'filler' propeties is set.  Obviously setting this as glue means
     *it can no longer be a strut, verticalGlue, horizontalGlue...etc.
     *@param exclude the property to exclude designated by the private statics like PROP_GLUE
     */
    private void fireChangeEventsWithExclusion(int exclude) {
        switch(exclude){
            case PROP_GLUE:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);
                break;
            case PROP_HORIZONTAL_GLUE:
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);                
                break;
            case PROP_HORIZONTAL_STRUT:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);                
                break;
            case PROP_VERTICAL_GLUE:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);                
                break;
            case PROP_VERTICAL_STRUT:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);                
                break;
            case PROP_RIGID_AREA:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);                
                break;                
            default:
                this.firePropertyChange("horizontalGlue", this.oldValues.horizontalGlue, this.horizontalGlue);
                this.firePropertyChange("verticalGlue", this.oldValues.verticalGlue, this.verticalGlue);
                this.firePropertyChange("glue", this.oldValues.glue, this.glue);
                this.firePropertyChange("horizontalStrut", this.oldValues.horizontalStrut, this.horizontalStrut);
                this.firePropertyChange("verticalStrut", this.oldValues.verticalStrut, this.verticalStrut);
                this.firePropertyChange("rigidArea", this.oldValues.rigidArea, this.rigidArea);
                break;
        }
    }
    
    /**
     *Used to simplify setting up the old values.  The old values are just used
     *to simplify firing the PropertyChangeEvents.
     */
    private void setupOldValues(){
        if( this.oldValues == null ){
            this.oldValues = new OldValues();
        }
        this.oldValues.horizontalGlue = this.horizontalGlue;
        this.oldValues.verticalGlue = this.verticalGlue;
        this.oldValues.glue = this.glue;
        this.oldValues.horizontalStrut = this.horizontalStrut;
        this.oldValues.verticalStrut = this.verticalStrut;
        this.oldValues.rigidArea = this.rigidArea;
        
    }
    
    /**
     *class used to simplify firing all the change events for all of the
     *properties that get changed by changing one.  This makes this control work
     *nicer inside of an IDE.  This only holds all of the old values that will be
     *passed to the PropertyChangeListeners
     */
    private class OldValues {
        public OldValues() {
            super();
        }
        public Dimension rigidArea = null;
        public int verticalStrut = -1;
        public int horizontalStrut = -1;
        public boolean verticalGlue = false;
        public boolean horizontalGlue = false;
        public boolean glue = false;
    }
    
    //set of private statics I thought I would use in conjunction with fireChangeEventsWithExclusion
    //might do that in the future...can't see why now.
    private static final int PROP_RIGID_AREA = 1;
    private static final int PROP_VERTICAL_STRUT = 2;
    private static final int PROP_HORIZONTAL_STRUT = 4;
    private static final int PROP_GLUE = 8;
    private static final int PROP_VERTICAL_GLUE = 16;
    private static final int PROP_HORIZONTAL_GLUE = 32;
    
    private OldValues oldValues = null;//used to hold the old values for PropertyChangeEvents
    
    private boolean horizontalGlue = false;
    private boolean verticalGlue = false;
    private boolean glue = false;
    private int horizontalStrut = -1;
    private int verticalStrut = -1;
    private java.awt.Dimension rigidArea = null;
    
}
