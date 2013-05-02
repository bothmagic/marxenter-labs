/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.restdata;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author marxma
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemList<T extends Object> {
    
    @JsonProperty("Items")
    
    private List<T> projectList;

    public List<T> getItemList() {
        return projectList;
    }

    public void setProjectList(List<T> projectList) {
        this.projectList = projectList;
    }
    
}
