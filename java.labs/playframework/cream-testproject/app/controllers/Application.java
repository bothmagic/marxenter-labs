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
public class Application extends Controller {
    
    public static void index() {
        
        render();
        
    }
    
   public static void appMenu() {
       render();
   }
    
    public static void loadTree(String id) {
        
        TreeNode node = new TreeNode();
        node.data = "Node 1";
        node.attr.put("id", "1");
        
        TreeNode node1 = new TreeNode();
        node1.data = "Node2";
        node1.attr.put("id", "111");
        node.children.add(node1);
        System.out.println("id is " + params.get("id"));
        if ("111".equals(id)) {
            renderJSON(node1.children);
        } else { renderJSON(node);
        
        }
        
    }
    
}
