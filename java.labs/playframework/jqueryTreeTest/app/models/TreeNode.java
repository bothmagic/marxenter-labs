/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marxma
 */
public class TreeNode {
    
    public String data;
    
    public Map<String, String> attr = new HashMap<String, String>();
    
    public List<TreeNode> children = new ArrayList<TreeNode>();
    
    public String state = "closed";
    
}
