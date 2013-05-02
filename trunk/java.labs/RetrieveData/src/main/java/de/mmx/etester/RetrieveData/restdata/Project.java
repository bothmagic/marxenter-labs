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
public class Project {
    
    @JsonProperty("Id")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("RequirementPackages")
    private List<ETPackage> reqPackage;

    public Project() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ETPackage> getReqPackage() {
        return reqPackage;
    }

    public void setReqPackage(List<ETPackage> reqPackage) {
        this.reqPackage = reqPackage;
    }
    
}
