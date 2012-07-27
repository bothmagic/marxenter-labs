/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.firstorientdb.data;

import javax.persistence.Id;
import javax.persistence.Version;

/**
 *
 * @author markus
 */
public class Project {
    
   @Id
    private Object id;
    
   @Version
   private Object version;
   
   private String name;
   
   private String description;
   
   private Folder testcase;
   
   private Folder testrun;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getTestcase() {
        return testcase;
    }

    public void setTestcase(Folder testcase) {
        this.testcase = testcase;
    }

    public Folder getTestrun() {
        return testrun;
    }

    public void setTestrun(Folder testrun) {
        this.testrun = testrun;
    }
   
   
}
