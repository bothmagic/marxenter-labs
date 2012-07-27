/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import java.util.Date;

/**
 *
 * @author marxma
 */
public class MyChildNode {

    public MyChildNode() {
    }

    MyChildNode(String name) {
        this.name = name;
    }

    public MyChildNode(String name, String descr, Date date, Integer state) {
        this.name = name;
        this.descr = descr;
        this.date = date;
        this.state = state;
    }
    
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
    private String name;
    private String descr;
    private Date date;
    private Integer state;
    
    
    
    
}
