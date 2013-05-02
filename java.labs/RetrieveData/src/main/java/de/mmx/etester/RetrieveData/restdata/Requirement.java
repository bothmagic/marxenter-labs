/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.restdata;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author marxma
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Requirement {
    
    @JsonProperty("Id")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    
    @JsonProperty("Children")
    private List<Requirement> children;
    
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

    public List<Requirement> getChildren() {
        return children;
    }

    public void setChildren(List<Requirement> children) {
        this.children = children;
    }
}
