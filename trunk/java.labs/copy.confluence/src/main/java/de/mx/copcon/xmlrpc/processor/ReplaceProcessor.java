/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.xmlrpc.processor;

import de.mx.copcon.xmlrpc.ContentProcessor;

/**
 * replace a regex with another string.
 * @author marxma
 */
public class ReplaceProcessor implements ContentProcessor {

    private String replaceStr;
    private String searchStr;

    public ReplaceProcessor(String searchStr, String replaceStr) {
        this.replaceStr = replaceStr;
        this.searchStr = searchStr;
    }
    
    
    @Override
    public String process(String input) {
        return input.replaceAll(searchStr, replaceStr);
    }
    
    
    
    
}
