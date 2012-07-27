/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.TreeNode;
import play.mvc.Controller;

/**
 *
 * @author marxma
 */
public class TestProjectTree extends Controller {
    
    
    
    public static void loadTree() {
        
    }
    
    public static void createTestProject() {
        System.out.println();
        System.out.println(params.get("email"));
        TreeNode node = new TreeNode();
        node.data = params.get("name");
        node.attr.put("id", "123");
        renderJSON(node);
        
    }
    
}
