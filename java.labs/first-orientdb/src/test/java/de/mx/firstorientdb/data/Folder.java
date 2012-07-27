/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.firstorientdb.data;

import java.util.List;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 *
 * @author markus
 */
public class Folder {
    
    @Id
    private Object id;
    
    @Version
    private Object version;
    
    
    private String name;
    
    private String description;
            
    private List<Object> children;

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

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
            
}
