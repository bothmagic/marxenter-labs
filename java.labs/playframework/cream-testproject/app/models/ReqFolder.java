/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrProperty;
import play.data.validation.Required;
import play.modules.cream.Model;

/**
 *
 * @author marxma
 */
@JcrNode(mixinTypes = { "mix:created", "mix:lastModified", "mix:versionable" })
public class ReqFolder extends Model {
    
    @JcrName
    public String name;

    @JcrProperty
    @Required
    public String description;
    
    @JcrChildNode
    public List<ReqFolder> folders;
    
    @JcrChildNode
    public List<Requirement> requirements;
    
}
