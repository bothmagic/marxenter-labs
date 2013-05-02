/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.wizardpage;

import de.mmx.etester.RetrieveData.etrest.CommandFactory;
import de.mmx.etester.RetrieveData.etrest.LoadManager;
import de.mmx.etester.RetrieveData.restdata.ETPackage;
import de.mmx.etester.RetrieveData.restdata.Project;
import de.mmx.etester.RetrieveData.restdata.Requirement;
import java.util.List;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 *
 * @author marxma
 */
public class ProjectTreeTableModel extends DefaultTreeTableModel {

    private List<Project> projectList;
    
    
    public ProjectTreeTableModel() {
        
        projectList = CommandFactory.INSTANCE.getProjectsCommand();
    }

    @Override
    public TreeTableNode getRoot() {
        return new DefaultMutableTreeTableNode("root", true);
    }
    

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return "Name";
    }

    @Override
    public boolean isLeaf(Object node) {
        boolean isLeaf = true;
        if (node instanceof DefaultMutableTreeTableNode) {
            isLeaf = false;
        } else if (node instanceof Project) {
            isLeaf = ((Project)node).getReqPackage() == null 
                    || ((Project)node).getReqPackage().isEmpty();
                    
        } else if (node instanceof ETPackage) {
            ETPackage eTPackage = ((ETPackage)node);
            if (!LoadManager.getInstance().isLoaded(eTPackage)) {
                LoadManager.getInstance().load(eTPackage, false);
                
            }
            final List<ETPackage> children = eTPackage.getChildren(); 
            final List<Requirement> requirements = eTPackage.getRequirements();
            isLeaf = (children == null || children.isEmpty()) &&
                         (requirements == null || requirements.isEmpty());
            
        } else if (node instanceof Requirement) {
            Requirement req = ((Requirement)node);
            if (!LoadManager.getInstance().isLoaded(req)) {
                LoadManager.getInstance().load(req, false);
                
            }
            final List<Requirement> children = req.getChildren(); 
            
            isLeaf = (children == null || children.isEmpty());
            
        }
        return isLeaf;
    }
    
    
    
    
    @Override
    public Object getValueAt(Object node, int column) {
        Object value = null;
        
        if (node instanceof Project) {
            value = ((Project)node).getName();
        } else if (node instanceof  ETPackage) {
            value = ((ETPackage)node).getName();
        } else if (node instanceof Requirement) {
            value = ((Requirement)node).getName();
        } else {
            value = "root";
        }
        return value;
    }


    @Override
    public Object getChild(Object parent, int index) {
        Object child = null;
        
        if (parent instanceof DefaultMutableTreeTableNode) {
            child = projectList.get(index);
        } else if (parent instanceof Project) {
            child = ((Project)parent).getReqPackage().get(index);
        } else if (parent instanceof ETPackage) {
            ETPackage eTPackage = ((ETPackage)parent);
            
            if (index > eTPackage.getChildren().size()-1) {
                child = eTPackage.getRequirements().get(index-eTPackage.getChildren().size());
            } else {
                child = eTPackage.getChildren().get(index);
            }
            
        } else if (parent instanceof Requirement) {
            child = ((Requirement)parent).getChildren().get(index);
        }
        
        return child;
    }

    @Override
    public int getChildCount(Object parent) {
        int count = 0;
        if (parent instanceof DefaultMutableTreeTableNode) {
            count = projectList.size();
        } else if (parent instanceof Project) {
            count = ((Project)parent).getReqPackage().size();
        } else if (parent instanceof ETPackage) {
            ETPackage eTPackage = ((ETPackage)parent);
            count = eTPackage.getChildren().size() 
                    + eTPackage.getRequirements().size();
        } else if (parent instanceof Requirement) {
            count = ((Requirement)parent).getChildren().size();
        }
        
        return count;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }
    
    

    @Override
    public int getHierarchicalColumn() {
        return 0;
    }
    
}
