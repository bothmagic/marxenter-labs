/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.etrest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import de.mmx.etester.RetrieveData.restdata.ETPackage;
import de.mmx.etester.RetrieveData.restdata.ItemList;
import de.mmx.etester.RetrieveData.restdata.Project;
import de.mmx.etester.RetrieveData.restdata.Requirement;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 * Wrapper to retrieve restdata from et services.
 * @author marxma
 */
public class CommandFactory {
    
    private final static String CMD_PROJECTS = "/api/projects?$expand=RequirementPackages";
    private final static String CMD_ET_REQ_PACKAGE = "/api/requirementpackage/%s?$expand=Children,Requirements";
    private final static String CMD_ET_REQUIREMENT = "/api/requirement/%s?$expand=Children,FieldValues";
    private final static String CMD_ET_ATTACHMENTS = "/api/requirement/%s/attachments";
    private final static String CMD_ET_PROJECT = "/api/project/%s?$expand=All";
    
    
    public final static CommandFactory INSTANCE
            = new CommandFactory();
    
    private String user;
    private String url;
    private String password;
    
    private CommandFactory() {
        
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Project> getProjectsCommand() {
        WebResource wr = createWebresource(CMD_PROJECTS);
        ItemList<Project> projectList;
        projectList = wr.get(new GenericType<ItemList<Project>>(){});
        return projectList.getItemList();
    }
    
    public void loadPackage(ETPackage eTPackage) {
        WebResource wr = createWebresource(String.format(CMD_ET_REQ_PACKAGE, eTPackage.getId()));
        ETPackage loadedPackage = wr.get(ETPackage.class);
        eTPackage.setChildren(loadedPackage.getChildren());
        eTPackage.setRequirements(loadedPackage.getRequirements());
    }
    
    public void loadRequirement(Requirement req) {
        WebResource wr = createWebresource(String.format(CMD_ET_REQUIREMENT, req.getId()));
        Requirement loadedReq = wr.get(Requirement.class);
        req.setChildren(loadedReq.getChildren());
        
    }
    
    public Map<String, Object> getRequirement(String id) {        
        Map<String, Object> req;
        
        WebResource wr = createWebresource(String.format(CMD_ET_REQUIREMENT, id));
        req = wr.get(new GenericType<Map<String, Object>>(){});
        
        return req;
    }
    
    public ItemList<Map<String, Object>> getAttachments(String id) {
        ItemList<Map<String, Object>> req;
        
        WebResource wr = createWebresource(String.format(CMD_ET_ATTACHMENTS, id));
        req = wr.get(new GenericType<ItemList<Map<String, Object>>>(){});
        
        return req;
    }
    
    public InputStream getRawData(String url) {
        ClientConfig cfg = new DefaultClientConfig();
        Client c = Client.create(cfg);
        c.addFilter(new HTTPBasicAuthFilter("Administrator", "erdgas"));
        WebResource wr = c.resource(url);
        wr.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        ClientResponse clientr = wr.get(ClientResponse.class);
        return clientr.getEntityInputStream();
    }
    
    private WebResource createWebresource(String cmd) {
        ClientConfig cfg = new DefaultClientConfig();
        cfg.getClasses().add(JacksonJsonProvider.class);
        Client c = Client.create(cfg);
        c.addFilter(new HTTPBasicAuthFilter("Administrator", "erdgas"));
        WebResource wr = c.resource(url+cmd);
        wr.accept(MediaType.APPLICATION_JSON_TYPE);
        return wr;
    }

    public Map<String, Object> getProject(String id) {
        Map<String, Object> project;
        
        WebResource wr = createWebresource(String.format(CMD_ET_PROJECT, id));
        project = wr.get(new GenericType<Map<String, Object>>(){});
        return project;
    }

    
    
}
