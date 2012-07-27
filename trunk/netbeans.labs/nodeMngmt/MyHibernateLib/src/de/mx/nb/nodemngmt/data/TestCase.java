/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt.data;

import java.io.Serializable;

/**
 *
 * @author marxma
 */
public class TestCase implements Serializable {
    
    private Integer id;
   
    private String name;
    private String description;
    private String precondition;
    private String postcondition;
    private String result;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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

    public String getPostcondition() {
        return postcondition;
    }

    public void setPostcondition(String postcondition) {
        this.postcondition = postcondition;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
}
