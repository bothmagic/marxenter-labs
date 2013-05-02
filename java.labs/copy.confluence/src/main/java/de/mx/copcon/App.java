package de.mx.copcon;

import de.mx.copcon.xmlrpc.ContentProcessor;
import de.mx.copcon.xmlrpc.commands.CopyEnum;
import de.mx.copcon.xmlrpc.commands.XmlRpcCopyPage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException, IOException
    {
        try {
            Pattern p = Pattern.compile("(?i)(^.*?)(?:/display/)(.*?)/(.*$)");
            Matcher m = p.matcher("http://ecgconfluence.hv.vng/display/team1512/Home");
            String server = "";
            String space = "";
            String page = "";
            if (m.matches()) {
                server = m.group(1);
                space = m.group(2);
                page = m.group(3).replaceAll("\\+", " ");    
            }
            
            String destServer = "http://10.96.18.170:8090";
            
            String srcSpace = "team";
            String destSpace = "team1501";
            
            Object[] copyPages = new Object[] {
                //"03 Projekte 1501", "3 Projekte",
                "01 Organisation 1501", "1.1 Organisation", false,
                "01 Organigramm und Verantwortlichkeiten 1501", "1.1.1 Organigramm", false,
                "02 Rollen 1501", "1.1.1 Organigramm", false,
                "03 Richtlinien 1501", "1.1.3 Richtlinien", true,
                "04 Arbeitsprozesse 1501", "1.1.4 Arbeitsprozesse", true,
                "01 organisatorisch 1501", "1.2.1 Organisatorisch", false,
                "02 fachlich 1501", "1.2.2 Fachlich", false,
                "02 Einarbeitung 1501", "1.2 Einarbeitung", false,
                "01 Team 1501", "1 Team", false,
                "1501 - Quality Management", "1501 Quality Management", false,
                "03 Mitarbeiter 1501", "1.3 Mitarbeiter", true,
                "02 Produkte 1501", "2 Leistungen", true,
                "04 Fachwissen 1501", "4 Fachwissen", true,
                "05 Archiv 1501", "5 Archiv", true
            };
            
            
            CommandFactory.INSTANCE.login(server, "marxma", "Edgar0001");
            CommandFactory.INSTANCE.login(destServer, "chefe", "erdgasi");
            
            for (int i = 0; i < copyPages.length-1; i=i+3) {
                XmlRpcCopyPage copyPage = new XmlRpcCopyPage(server, srcSpace, 
                        (String)copyPages[i], destServer, destSpace, 
                        (String)copyPages[i+1], (Boolean)copyPages[i+2], EnumSet.allOf(CopyEnum.class), new ArrayList<ContentProcessor>());
                copyPage.run(null);
            }
        } catch (XmlRpcException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
}
