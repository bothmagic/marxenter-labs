/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.action;

import de.mx.copcon.Action;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.InformationCallback;
import de.mx.copcon.xmlrpc.commands.XmlRpcSetPermittion;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 * @author marxma
 */
public class SetPermissionAction implements Action, InformationCallback {
    
    private int progress;
    private String progressDesc = "";
    private List<Object[]> errorMsg = new ArrayList<Object[]>();
    public SetPermissionAction(Options option) {
        Option permit = OptionBuilder
                .withArgName("permit")
                .hasArg()
                .withDescription("A Pipe seperated list (without space) of Permittions. \n"
                + "Allowed Values are: \n"
                + "VIEWSPACE \t View all content in the space\n"
                + "EDITSPACE \t Create new pages and edit existing ones\n"
                + "EXPORTPAGE \t Export pages to PDF, Word\n"
                + "SETPAGEPERMISSIONS \t Set page-level permissions\n"
                + "REMOVEPAGE \t Remove pages\n"
                + "EDITBLOG \t Create news items and edit existing ones\n"
                + "REMOVEBLOG \t Remove news\n"
                + "COMMENT \t Add comments to pages or news in the space\n"
                + "REMOVECOMMENT \t Remove the user's own comments\n"
                + "CREATEATTACHMENT \t Add attachments to pages and news\n"
                + "REMOVEATTACHMENT \t Remove attachments\n"
                + "REMOVEMAIL \t Remove mail\n"
                + "EXPORTSPACE \t Export space to HTML or XML\n"
                + "SETSPACEPERMISSIONS \t Administer the space")
                .create("permit");
        Option to = OptionBuilder.withArgName("to").hasArg()
                .withDescription("User or Groupname").create("to");
        Option spacekey = OptionBuilder.withArgName("spacekey").hasArg()
                .withDescription("key of the space.").create("spacekey");
        option.addOption(permit);
        option.addOption(to);
        option.addOption(spacekey);
        
    }
    

    @Override
    public void start(CommandLine cmd, String[] args) {
        
        
        String spaceKey = cmd.getOptionValue("spacekey");
        String to = cmd.getOptionValue("to");
        String permit = cmd.getOptionValue("permit");
        String server = cmd.getOptionValue("serverurl");
        List<String> permittionList = new ArrayList<String>();
        setInformation(String.format("set permittion for space %s to user %s", spaceKey, to));
        StringTokenizer tok = new StringTokenizer(permit, "|");
        while (tok.hasMoreTokens()) {
            permittionList.add(tok.nextToken());
        }
        
        XmlRpcSetPermittion setpermittion = new XmlRpcSetPermittion(server, spaceKey, to, permittionList);
        try {
            setpermittion.run(this);
        } catch (RpcCommandException ex) {
            this.addError(String.format("could not set permittion on %s ", spaceKey), ex);
        }
        
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public String getProgressDesc() {
        return progressDesc;
    }

    @Override
    public void setInformation(String info) {
        progressDesc = info;
    }

    @Override
    public void addError(String message, Exception ex) {
        errorMsg.add(new Object[] {message, ex});
    }
    
    @Override
    public List<Object[]> getErrors() {
        return errorMsg;
    }

    
}
