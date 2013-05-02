/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon;

import org.apache.commons.cli.CommandLine;

/**
 *
 * @author marxma
 */
public interface Action {
    
    
    public void start(CommandLine cmd, String[] args);
    
    public int getProgress();
    
    public String getProgressDesc();
}
