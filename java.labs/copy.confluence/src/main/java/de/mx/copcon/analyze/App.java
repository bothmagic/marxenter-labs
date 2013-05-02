/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.analyze;

import de.mx.copcon.CommandFactory;
import de.mx.copcon.XmlRpcException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author marxma
 */
public class App {

    public static void main(String[] args) {
        String server = "http://ecgconfluence.hv.vng";
        String destServer = "http://10.96.18.170:8090";
        Pattern p = Pattern.compile(".*\\{(.*?)(\\}|\\:)");

        CommandFactory xmlrpc = CommandFactory.INSTANCE;
        try {
            xmlrpc.login(server, "marxma", "Edgar0001");
            xmlrpc.login(destServer, "chefe", "erdgasi");
        } catch (XmlRpcException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter writer = null;
        try {
            Object[] spaces = (Object[]) xmlrpc.execute(server, "getSpaces", new ArrayList(), CommandFactory.API_VERSION_2);
            writer = new FileWriter("out.csv");
            for (Object space : spaces) {
                Map<String, String> spaceMap = (Map<String, String>) space;
                System.out.println(spaceMap.get("name"));
                
                Object[] pages = (Object[]) xmlrpc.execute(server, "getPages", Arrays.asList(new Object[]{
                            spaceMap.get("key")
                        }), CommandFactory.API_VERSION_1);
                writer.write("Space\tSpaceId\tSeite\tSeiteId\tMakro\n");
                for (Object page : pages) {
                    Map<String, String> pageMap = (Map<String, String>) page;
                    
                    Map<String, String> pageContent = null;
                    try {
                        pageContent = (Map<String, String>) xmlrpc.getPage(server, pageMap.get("id"), CommandFactory.API_VERSION_1);
                    } catch (XmlRpcException ex) {
                        System.out.println("error get Page " + pageMap.get("id"));
                        continue;
                    }

                    String content = pageContent.get("content");
                    
                    int pos = 0;
                    while (pos < content.length()) {
                        
                        int end = content.indexOf("}", pos);
                        if (end < content.length()-1 && content.charAt(end+1) == '}') {
                            pos = end+2;
                            continue;
                        }
                            
                        if (end < 0) {
                            break;
                        }
                        //System.out.println(content.substring(pos, end+1)+"---");
                        Matcher m = p.matcher(content.substring(pos, end+1).trim());
                        
                        if (m.matches()) {
                            writer.write(spaceMap.get("name") + "\t"
                                    +spaceMap.get("id") + "\t"+ pageMap.get("title") + "\t"
                                    + pageMap.get("id") + "\t"
                                    + m.group(1).substring(0,
                                    m.group(1).indexOf(":") > 0 ? m.group(1).indexOf(":") : m.group(1).length()) + "\n");
                        }
                        pos = end+1;
                    }

                    writer.flush();
                }
            }

        } catch (XmlRpcException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
