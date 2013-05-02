/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon;

import java.util.List;

/**
 *
 * @author marxma
 */
public interface InformationCallback {
    
    void setInformation(String info);
    
    void addError(String message, Exception ex);
    
    List<Object[]> getErrors();
    
}
