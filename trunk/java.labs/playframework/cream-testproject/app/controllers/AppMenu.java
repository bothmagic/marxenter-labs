/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Collections;
import models.Organisation;
import org.jcrom.JcrMappingException;
import play.modules.cream.ocm.JcrQueryResult;
import play.mvc.Controller;

/**
 *
 * @author markus
 */
public class AppMenu extends Controller {
    
    public static void listOrga() {
        JcrQueryResult<Organisation> orgaList = null;
        try {
            orgaList = Organisation.all();
        } catch(JcrMappingException e) {
            render(Collections.EMPTY_LIST);
        }
        System.out.println("find " + orgaList.count());
        renderArgs.put("orgas", orgaList.isEmpty()? Collections.EMPTY_LIST: orgaList.fetch());
        render();
    }
    
    public static void editOrga() {
        render();
    }
    
    public static void saveOrga(Organisation orga) {
        orga.name = orga.title;
        orga.save();
        System.out.println(orga.getPath());
        Controller.redirect("AppMenu.listOrga");
    }
    
    
}
