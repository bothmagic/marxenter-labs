/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.action;

import au.com.bytecode.opencsv.CSVReader;
import de.mx.copcon.Action;
import de.mx.copcon.xmlrpc.ContentProcessor;
import de.mx.copcon.InformationCallback;
import de.mx.copcon.xmlrpc.commands.CopyEnum;
import de.mx.copcon.xmlrpc.commands.XmlRpcCopyPage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 * @author marxma
 */
public class CopyPagesAction implements Action, InformationCallback {

    private String progressDesc;
    private int progress;
    private List<Object[]> errorMsg = new ArrayList<Object[]>();
    
    public CopyPagesAction(Options option) {
        
        Option copyPages = OptionBuilder
                .withArgName("copyfile")
                .hasArg()
                .withDescription("file that contains the copypages. A line contains commaserperated \"srcpagename, "
                + "destinationpagename, true|false\" if the last param true then it copies all subpages.")
                .create("copyfile");
        Option withmetadata = OptionBuilder
                .withArgName("withmetadata")
                .hasArg(false)
                .withDescription("Copies all pages with metadata. All db connection params are required")
                .create("withmetadata");
        Option withcomment = OptionBuilder
                .withArgName("withcomment")
                .hasArg(false)
                .withDescription("Copy page comments.")
                .create("withcomment");
        Option withlabel = OptionBuilder
                .withArgName("withlabel")
                .hasArg(false)
                .withDescription("Copy page labels")
                .create("withlabel");
        Option withcontent = OptionBuilder
                .withArgName("withcontent")
                .hasArg(false)
                .withDescription("Copy page content.")
                .create("withcontent");
        Option withattachment = OptionBuilder
                .withArgName("withattachment")
                .hasArg(false)
                .withDescription("Copy page attachments.")
                .create("withattachment");
        option.addOption(copyPages);
        option.addOption(withmetadata);
        option.addOption(withattachment);
        option.addOption(withcomment);
        option.addOption(withlabel);
        option.addOption(withcontent);
        
    }
    
    
    @Override
    public void start(CommandLine cmd, String[] args) {
        progress = 0;
        progressDesc = "init copy pages";
        CSVReader reader = null; 
        String server = cmd.getOptionValue("serverurl");
        String srcServer = cmd.getOptionValue("srcserverurl");
        EnumSet<CopyEnum> copyEnum = EnumSet.noneOf(CopyEnum.class);
        
        if (cmd.hasOption("withcomment")) {
            copyEnum.add(CopyEnum.WITHCOMMENT);
        }
        if (cmd.hasOption("withcontent")) {
            copyEnum.add(CopyEnum.WITHCONTENT);
        }
        if (cmd.hasOption("withattachment")) {
            copyEnum.add(CopyEnum.WITHATTACHMENT);
        }
        if (cmd.hasOption("withmetadata")) {
            copyEnum.add(CopyEnum.WITHMETADATA);
        }
        if (cmd.hasOption("withlabel")) {
            copyEnum.add(CopyEnum.WITHLABEL);
        }
        
        
        
        
        try {
            List<String[]> copyPages = null;
            if (cmd.hasOption("copyfile")) {
                reader = new CSVReader(new FileReader(cmd.getOptionValue("copyfile")), ',', '\"');
                copyPages = reader.readAll();
            } else {
                copyPages = new ArrayList<String[]>();
                copyPages.add(Arrays.copyOfRange(args, args.length-5, args.length));
            }
            
            
            List<Object[]> copyList = new ArrayList<Object[]>();
            
            for (String[] copyPage: copyPages) {
                Object[] copyListItem = new Object[5];
                copyListItem[0] = copyPage[0].trim();
                copyListItem[1] = copyPage[1].trim();
                copyListItem[2] = copyPage[2].trim();
                copyListItem[3] = copyPage[3].trim();
                copyListItem[4] = Boolean.valueOf(copyPage[4].trim());
                copyList.add(copyListItem);
            }
            int i = 1;
            
            List<ContentProcessor> processors = new ArrayList<ContentProcessor>();
            
            for (Object[] copyItem: copyList) {
                
                //progressDesc = String.format("Copy page %d / %d", i, copyList.size());
                
                XmlRpcCopyPage copyPage = new XmlRpcCopyPage(srcServer, (String)copyItem[0], 
                        (String)copyItem[1], server, (String)copyItem[2], 
                        (String)copyItem[3], (Boolean)copyItem[4], copyEnum, processors);
                
                copyPage.run(this);
                i++;
                
            }
            //progressDesc = String.format("%d of %d pages are successfull copied.", i-1, copyList.size());
        } catch (IOException ex) {
            Logger.getLogger(CopyPagesAction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(CopyPagesAction.class.getName()).log(Level.SEVERE, null, ex);
            }
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
