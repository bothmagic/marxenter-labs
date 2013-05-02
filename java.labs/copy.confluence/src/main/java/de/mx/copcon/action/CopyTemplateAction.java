/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.action;

import de.mx.copcon.Action;
import de.mx.copcon.jdbc.commands.JdbcCopyTemplate;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.InformationCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author marxma
 */
public class CopyTemplateAction implements Action, InformationCallback {

    private int progress;
    private String progressDesc;
    private List<Object[]> errorMsg = new ArrayList<Object[]>();
    
    public CopyTemplateAction(Options options) {
    }
    
    @Override
    public void start(CommandLine cmd, String[] args) {
        
        progressDesc = "copy template";
        String[] copyTemp = Arrays.copyOfRange(args, args.length-3, args.length);
        
        String server = cmd.getOptionValue("serverurl");
        String srcServer = cmd.getOptionValue("srcserverurl");
        
        JdbcCopyTemplate jdbcCopy = new JdbcCopyTemplate(srcServer, server, copyTemp[0], copyTemp[1], copyTemp[2]);
        try {
            jdbcCopy.run(this);
        } catch (RpcCommandException ex) {
            Logger.getLogger(CopyTemplateAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        progress = 100;
        progressDesc = String.format("Template %s copied successfully", copyTemp[2]);
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
