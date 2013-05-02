/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.jdbc.commands;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import de.mx.copcon.Command;
import de.mx.copcon.CommandFactory;
import de.mx.copcon.XmlRpcException;
import de.mx.copcon.prototyp.jdbc.TestHtmlUnit;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.InformationCallback;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class JdbcCopyTemplate implements Command {

    private final static String SQL_GETTEMPLATE = "select PAGETEMPLATES.* from PAGETEMPLATES join SPACES on "
            + "(SPACES.SPACEID = PAGETEMPLATES.SPACEID) where SPACES.SPACEKEY = ? "
            + "and PAGETEMPLATES.TEMPLATENAME = ?";
//    private final static String SQL_INSERTTEMPLATE = "insert into PAGETEMPLATES ("
//            + "templatename, templatedesc, content, spaceid, version, creator, creationdate, lastmodifier, "
//            + "lastmoddate) "
//            + "values (?, ?, ?, ?, 1, ?, ?, ?, ?)";
    private final static String SQL_UPDATETEMPLATE = "update PAGETEMPLATES set content = ?, templatedesc = ? where templateid = ? and spaceid= ?";
    //private final static String SQL_GETSPACEID = "select spaceid from spaces where spacekey = ?";
    private String srcServer;
    private String destServer;
    private String srcSpaceKey;
    private String destSpaceKey;
    private String templateName;

    public JdbcCopyTemplate(String srcServer, String destServer, String srcSpaceKey, String destSpaceKey, String templateName) {
        this.srcServer = srcServer;
        this.destServer = destServer;
        this.srcSpaceKey = srcSpaceKey;
        this.destSpaceKey = destSpaceKey;
        this.templateName = templateName;
    }

    @Override
    public void run(InformationCallback info) throws RpcCommandException {

        CommandFactory cmd = CommandFactory.INSTANCE;

        Connection srccon = cmd.getConnection(srcServer);
        Connection destcon = cmd.getConnection(destServer);
        PreparedStatement stat = null;
        try {

            stat = srccon.prepareStatement(SQL_GETTEMPLATE);
            stat.setString(1, srcSpaceKey);
            stat.setString(2, templateName);

            ResultSet resset = stat.executeQuery();

            if (resset.next()) {
                String templateDesc = resset.getString("templatedesc");
                String content = resset.getString("content");
                String creator = resset.getString("creator");
                Date creationDate = resset.getDate("creationdate");
                String lastmod = resset.getString("lastmodifier");
                Date lastmodDate = resset.getDate("lastmoddate");
                String labels = resset.getString("labels");
                
                try {
                    content = cmd.convertToStorageFormat(destServer, content);
                } catch (XmlRpcException ex) {
                    Logger.getLogger(JdbcCopyTemplate.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (!updateTemplate(content, templateDesc, destcon)) {
                    insertTemplate();
                    updateTemplate(content, templateDesc, destcon);
                }
                 
            }

        } catch (SQLException ex) {
            Logger.getLogger(JdbcCopyTemplate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JdbcCopyTemplate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * updates the template in the db. need a existing template.
     * @param content
     * @param templateDesc
     * @param destcon
     * @return
     * @throws SQLException 
     */
    private boolean updateTemplate(String content, String templateDesc, Connection destcon) throws SQLException {
        PreparedStatement getdesttemp = destcon.prepareStatement(SQL_GETTEMPLATE);
        getdesttemp.setString(1, destSpaceKey);
        getdesttemp.setString(2, templateName);
        ResultSet destResult = getdesttemp.executeQuery();
        
        if (destResult.next()) { // update template in db
            PreparedStatement updatetemp = destcon.prepareStatement(SQL_UPDATETEMPLATE);
            updatetemp.setString(1, content);
            updatetemp.setString(2, templateDesc);
            updatetemp.setLong(3, destResult.getLong("templateid"));
            updatetemp.setLong(4, destResult.getLong("spaceid"));
            if (updatetemp.executeUpdate() != 1) {
                System.err.println("error on executing update.");
            }
            updatetemp.close();
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * very very dirty. theres no possible to insert a new template without web.
     * so the method insertTemplate simulate a webbrowser session.
     */
    private void insertTemplate() {
        try {
            final WebClient webClient = new WebClient();
            webClient.setJavaScriptEnabled(false);
            final HtmlPage page = (HtmlPage)webClient.getPage(destServer+"/login.action");
            
            final HtmlTextInput user = (HtmlTextInput)page.getHtmlElementById("os_username");
            final HtmlPasswordInput password = (HtmlPasswordInput)page.getHtmlElementById("os_password");
            user.setValueAttribute(CommandFactory.INSTANCE.getUser(destServer));
            password.setValueAttribute(CommandFactory.INSTANCE.getPassword(destServer));
            final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("loginButton");
            button.click();
            
            final HtmlPage page1 = (HtmlPage)webClient.getPage(destServer+"/pages/templates2/createpagetemplate.action?key=team3");
            final HtmlTextInput title = (HtmlTextInput)page1.getHtmlElementById("content-title");
            title.setValueAttribute(templateName);
            final HtmlButton add = (HtmlButton)page1.getHtmlElementById("rte-button-publish");
            add.click();
        } catch (IOException ex) {
            Logger.getLogger(TestHtmlUnit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(TestHtmlUnit.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
