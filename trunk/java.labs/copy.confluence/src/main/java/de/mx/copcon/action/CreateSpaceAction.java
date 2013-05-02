/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.action;

import de.mx.copcon.Action;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.InformationCallback;
import de.mx.copcon.xmlrpc.commands.XmlRpcSaveSpace;
import de.mx.copcon.xmlrpc.commands.XmlRpcSaveStructure;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.CharSet;

/**
 *
 * @author marxma
 */
public class CreateSpaceAction implements Action, InformationCallback {
    
    private int progress = 0;
    private String progressDesc = "";
    private List<Object[]> errorMsg = new ArrayList<Object[]>();
    
    public CreateSpaceAction(Options options) throws ParseException {
        
        Option spaceKey = OptionBuilder
                .withArgName("spacekey")
                .hasArg()
                .withDescription("key of the new space.")
                .isRequired()
                .create("spacekey");
        Option spaceName = OptionBuilder
                .withArgName("spacename")
                .hasArg()
                .withDescription("name of the new space.")
                .isRequired()
                .create("spacename");
        Option nostructure = OptionBuilder
                .withArgName("nostructure")
                .withDescription("prevent creation of base structure")
                .create("nostructure");
        options.addOption(spaceKey);
        options.addOption(spaceName);
        options.addOption(nostructure);

    }

    @Override
    public void start(CommandLine cmd, String args[]) {
        
        String server = cmd.getOptionValue("serverurl");
        String spaceKey = cmd.getOptionValue("spacekey");
        String spaceName = cmd.getOptionValue("spacename");
        
        try {    
            //System.out.println(String.format("create space with:\nspacename=%s, spacekey=%s", spaceKey, spaceName));
            
                    
            progress = 10;
            progressDesc = "Create Space";
            
            XmlRpcSaveSpace saveSpace = new XmlRpcSaveSpace(server, spaceKey, spaceName, "");
            saveSpace.run(this);
            Map<String, String> space = saveSpace.getSpace();
            if (!cmd.hasOption("nostructure")) {
                progress = 50;
                progressDesc = "Create Basestructure";
                XmlRpcSaveStructure saveStructure = new XmlRpcSaveStructure(Arrays.asList(baseStructure), 
                        server, spaceKey, "Home");
                saveStructure.run(this);
            }
            progress = 100;
            progressDesc = "Space \""+ spaceName + "\" created";
        } catch (RpcCommandException ex) {
            
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

    private String[] baseStructure = new String[] {
        "1 Team",
        "1 Team//1.1 Organisation",
        "1 Team//1.1 Organisation//1.1.1 Organigramm",
        "1 Team//1.1 Organisation//1.1.2 Rollen",
        "1 Team//1.1 Organisation//1.1.3 Richtlinien",
        "1 Team//1.1 Organisation//1.1.4 Arbeitsprozesse",
        "1 Team",
        "1 Team//1.2 Einarbeitung",
        "1 Team//1.2 Einarbeitung//1.2.1 Organisatorisch",
        "1 Team//1.2 Einarbeitung//1.2.2 Fachlich",
        "1 Team//1.3 Mitarbeiter",
        "1 Team//1.4 Arbeitsgruppen",
        "2 Leistungen",
        "3 Projekte",
        "4 Fachwissen",
        "5 Archiv"
    };

    @Override
    public List<Object[]> getErrors() {
        return errorMsg;
    }

}
