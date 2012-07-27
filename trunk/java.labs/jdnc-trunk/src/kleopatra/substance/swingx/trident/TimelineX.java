/*
 * Created on 08.12.2009
 *
 */
package swingx.trident;

import org.pushingpixels.trident.TimelineAccessGlue;

/**
 * Timeline providing a hook for target related installs/release. This is 
 * unrelated to TimelineState as such, may be messaged in any state.
 */
public class TimelineX extends TimelineAccessGlue {
    
    private Installer installer;
    private boolean installed;
    
    public TimelineX() {
        this(null);
    }
    
    public TimelineX(Object target) {
        this(target, null);
    }
    
    public TimelineX(Object target, Installer installer) {
        super(target);
        setInstaller(installer);
    }
    
    public void release() {
        if (installer != null) {
            installer.release(getTarget());
            installed = false;
        }
    }

    public void install() {
        if (installer != null) {
            installer.install(getTarget());
            installed = true;
        }
    }
    
    public boolean isInstalled() {
        return installed;
    }
    
    /**
     * 
     * 
     * @param installer the installer to use, may be null
     */
    private void setInstaller(Installer installer) {
        this.installer = installer;
        installed = installer != null ? false : true;
    }

}
