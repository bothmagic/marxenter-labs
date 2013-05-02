/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.etrest;

import de.mmx.etester.RetrieveData.restdata.ETPackage;
import de.mmx.etester.RetrieveData.restdata.Requirement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marxma
 */
public class LoadManager {
    
    private final static LoadManager INSTANCE = new LoadManager();
    
    public static LoadManager getInstance() {
        return INSTANCE;
    }
    
    private List<String> loaded;
    
    
    private LoadManager() {
        loaded = new ArrayList<String>();
    }
    
    
    public void load(ETPackage etPackage, boolean all) {
        if (isLoaded(etPackage)) {
            return;
        }
        CommandFactory.INSTANCE.loadPackage(etPackage);
        loaded.add(etPackage.getId());
        if (all) {
            for (ETPackage pkg: etPackage.getChildren()) {
                load(pkg, all);
            }
            for (Requirement req: etPackage.getRequirements()) {
                load(req, all);
            }
        }
    }
    
    public void load(Requirement req, boolean all) {
        if (isLoaded(req)) {
            return;
        }
        CommandFactory.INSTANCE.loadRequirement(req);
        loaded.add(req.getId());
        if (all) {
            for (Requirement r: req.getChildren()) {
                load(r, all);
            }
        }
    }

    public boolean isLoaded(ETPackage eTPackage) {
        return false;//loaded.contains(eTPackage.getId());
    }
    
    public boolean isLoaded(Requirement req) {
        return false;//loaded.contains(req.getId());
    }
    
}
