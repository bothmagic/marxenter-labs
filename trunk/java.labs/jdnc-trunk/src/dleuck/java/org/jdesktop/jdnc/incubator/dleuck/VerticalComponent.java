package org.jdesktop.jdnc.incubator.dleuck;

public interface VerticalComponent {
	
    /** Identifies a change in the component's rotation. */
    public static final String ROTATION_CHANGED_PROPERTY =
    	"rotation";

    /**
     * Get the rotation (SwingConstants.LEFT for a -90 degree rotation or
     * SwingConstants.RIGHT for a 90 degree rotation)
     * 
     * @return The rotation
     */
    public int getRotation();
    
    /**
     * Set the rotation (SwingConstants.LEFT for a -90 degree rotation or
     * SwingConstants.RIGHT for a 90 degree rotation)
     * 
     * @param rotation The rotation
     */
    public void setRotation(int rotation);
}
