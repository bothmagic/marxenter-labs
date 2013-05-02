/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData;

import de.mmx.etester.RetrieveData.wizardpage.WizardPage.RequiementSettings;
import de.mmx.etester.RetrieveData.etrest.CommandFactory;
import de.mmx.etester.RetrieveData.restdata.ItemList;
import de.mmx.etester.RetrieveData.restdata.Requirement;
import java.awt.CardLayout;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;
import org.ciscavate.cjwizard.WizardPage;
import org.ciscavate.cjwizard.WizardSettings;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;


/**
 *
 * @author marxma
 */
public class MainDialog extends JFrame 
implements FirstSettingsPage.FirstSettingsPanelListener,
    de.mmx.etester.RetrieveData.wizardpage.WizardPage.FinishListener {
    public static final String FIRSTPAGE = "firstpage";
    public static final String WIZARDPAGE = "wizardpage";
    private final de.mmx.etester.RetrieveData.wizardpage.WizardPage wizardPage;
    private File template;

    public MainDialog() {
        setTitle("Enterpriese Tester Documentgeneration");
        FirstSettingsPage firstPage = new FirstSettingsPage();
        getContentPane().setLayout(new CardLayout());
        firstPage.setListener(this);
        getContentPane().add(firstPage, FIRSTPAGE);
        wizardPage = new de.mmx.etester.RetrieveData.wizardpage.WizardPage(this);
        getContentPane().add(wizardPage, WIZARDPAGE);
       
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
      pack();
        
    }
    
    /**
     * init wizard pages. click on next button on first page.
     * 
     * @param url
     * @param user
     * @param password
     * @param template 
     */
    public void next(String url, String user, String password, File template) {
        List<String> templateList = new ArrayList<String>();
        this.template = template;
        try {
            WordprocessingMLPackage wordMLPackage = 
                WordprocessingMLPackage.load(template);
            
            MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
            
            for (Object content: mdp.getContent()) {
                
                if (content instanceof P) {
                    String text = String.valueOf(content).trim();
                    if (text.indexOf("{ET:") >= 0) {
                        if (!templateList.contains(text))
                            templateList.add(text);
                    }
                }
            }
        } catch (Docx4JException ex) {
            Logger.getLogger(MainDialog.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        CommandFactory.INSTANCE.setPassword(password);
        CommandFactory.INSTANCE.setUser(user);
        CommandFactory.INSTANCE.setUrl(url);
        
        ((CardLayout)getContentPane().getLayout()).show(getContentPane(), WIZARDPAGE);
        wizardPage.setTemplateList(templateList);
        
    }

    public void close() {
        System.exit(0);
    }

    
    /**
     * get values from requirements and render the template.
     * @param output
     * @param settings 
     */
    public void finish(File output, Map<String, RequiementSettings> settings) {
        
        DocXTemplate template = new DocXTemplate(this.template);
        CommandFactory cmd = CommandFactory.INSTANCE;
        
        Pattern p = Pattern.compile("\\{(.*):(.*)\\}");
        
        for (Entry<String, RequiementSettings> entry: settings.entrySet()) {
            // load Requirements
            
            List<Map<String, Object>> valueMapList = new ArrayList<Map<String, Object>>();
            // get all templatepart vars 
            List<String> templateVars = template.getTemplateVars(entry.getKey());
            Map<String, String> picklists = new HashMap<String, String>();
            Map<String, String> picklistValues = new HashMap<String, String>();
            // init picklist values
            for (String var: templateVars) {
                Matcher m = p.matcher(var);
                if (m.matches()) {
                    picklists.put(m.group(1), m.group(2));
                }
            }
            
            for (Requirement req: entry.getValue().getRequirements()) {
                
                Map<String, Object> myreq = cmd.getRequirement(req.getId());
                ItemList<Map<String, Object>> attachments = cmd.getAttachments(req.getId());
                Map<String, Object> valueMap = new HashMap<String, Object>();
                // get systemvalues
                for (Map.Entry<String, Object> r : myreq.entrySet()) {
                    if (templateVars.contains("{"+r.getKey()+"}")) {   
                        valueMap.put("{"+r.getKey()+"}", (r.getValue()==null)?"":r.getValue());
                    }
                    
                    // get picklistvalues
                    if (picklists.containsKey(r.getKey())) {
                        if (picklistValues.containsKey(r.getKey())) {
                            valueMap.put("{"+r.getKey()+":"+picklists.get(r.getKey()) +"}", 
                                    picklistValues.get(String.valueOf(r.getValue())));
                        } else { // load picklist
                            Map<String, Object> project = cmd.getProject((String)myreq.get("ProjectId"));
                            
                            for ( Object obj:(List<Object>)project.get(picklists.get(r.getKey()))) {
                                
                                Map<String, Object> pI = (Map<String, Object>)obj;
                                if (r.getValue().equals(pI.get("Id"))) {
                                    valueMap.put("{"+r.getKey()+":"+picklists.get(r.getKey()) +"}", 
                                        String.valueOf(pI.get("Text")));
                                }
                            }
                            
                        }
                    }
                }
                // get uservalues
                for (Map.Entry<String, Object> r : ((Map<String, Object>)myreq.get("FieldValues")).entrySet()) {
                    if (templateVars.contains("{"+r.getKey()+"}")) {   
                        valueMap.put("{"+r.getKey()+"}", (r.getValue()==null)?"":r.getValue());
                    }
                    
                    // get picklistvalues
                }
                
                
               
                
                valueMapList.add(valueMap);
                // get attachments
                List<String> attList = new ArrayList<String>();
                valueMap.put("attachments", attList);
                for (Map<String, Object> att: attachments.getItemList()) {
                    BufferedOutputStream bfout = null;
                    try {
                        
                        
                        try {
                            bfout = new BufferedOutputStream(new FileOutputStream((String)att.get("Name")));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MainDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        IOUtils.copy(cmd.getRawData((String)att.get("Content")), bfout);
                        attList.add((String)att.get("Name"));
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MainDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            bfout.flush();
                            bfout.close();
                        } catch (IOException ex) {
                            Logger.getLogger(MainDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
                
            }
            
            // render templatepart
            template.replace(entry.getKey(), valueMapList);
        }
        try {
            template.save(output);
        } catch (Docx4JException ex) {
            Logger.getLogger(MainDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

        
}


