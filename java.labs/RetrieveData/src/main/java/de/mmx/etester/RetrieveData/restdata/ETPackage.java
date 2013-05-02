/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.restdata;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marxma
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ETPackage {
    
    @JsonProperty("Id")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("Children")
    private List<ETPackage> children;
    
    @JsonProperty("Requirements")
    private List<Requirement> requirements;
   
    public ETPackage() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ETPackage> getChildren() {
        return children;
    }

    public void setChildren(List<ETPackage> children) {
        this.children = new ArrayList(children);
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = new ArrayList(requirements);
    }

}
