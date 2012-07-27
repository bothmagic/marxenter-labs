package uk.co.osbald.sample;

import org.jdesktop.beans.AbstractBean;

import java.util.Collection;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 12:13:30
 */

public class OpenDialogModel extends AbstractBean {
    Module selectedModule;
    Collection<Module> modules;

    public OpenDialogModel(Collection<Module> models) {
        this.modules = models;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        firePropertyChange("selectedModule", this.selectedModule, this.selectedModule = selectedModule);
    }

    public Collection<Module> getModules() {
        return modules;
    }

    public void setModules(Collection<Module> modules) {
        firePropertyChange("modules", this.modules, this.modules = modules);
    }
}
