/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.cli;

import de.mx.copcon.Action;
import de.mx.copcon.action.CreateSpaceAction;
import de.mx.copcon.CommandFactory;
import de.mx.copcon.InformationCallback;
import de.mx.copcon.XmlRpcException;
import de.mx.copcon.action.CopyPagesAction;
import de.mx.copcon.action.CopyTemplateAction;
import de.mx.copcon.action.SetPermissionAction;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 * @author marxma
 */
public class App {

    private final static Map<String, ActionCommand> commandMap = new HashMap<String, ActionCommand>();
    private static String progressName;
    private static int progressId = 0;
    private static Properties props = new Properties();
    
    /*
     * Commands are mapped to ActionClasses.
     */
    static {
        commandMap.put("createSpace",
                new ActionCommand(CreateSpaceAction.class, "creates a new space"));
        commandMap.put("copyPages",
                new ActionCommand(CopyPagesAction.class, "copy confluence pages from one system to another system."));
        commandMap.put("copyTemplate",
                new ActionCommand(CopyTemplateAction.class, 
                "copy confluence template from one system to another system. usage: copyTempate [OPTIONS] srcSpace destSpace templateName"));
        commandMap.put("setPermission",
                new ActionCommand(SetPermissionAction.class, 
                "set permittion of a space: copyTempate [OPTIONS]"));
    }

    public static void main(String[] args) {

        boolean showHelp = false;

        if (args.length == 0) {
            System.out.println("For help enter [command] -help. Available commands:\n");
            for (Map.Entry<String, ActionCommand> entry : commandMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().getDescription() + "\n");
            }

            System.exit(0);
        }

        ActionCommand action = commandMap.get(args[0]);

        
        if (action != null) {
            Options option = initOptions();
            Thread th = null;
            try {
                Constructor constr = action.actionClass.getConstructor(Options.class);

                final Object cmdAction = constr.newInstance(option);

                args = loadProperties(args);

                CommandLineParser parser = new BasicParser();
                
                CommandLine cmd = parser.parse(option, args);

                if (cmd.hasOption("help")) {
                    showHelp = true;
                } else {

                    th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int progress;
                            try {
                                do {
                                    progress = ((Action) cmdAction).getProgress();
                                    printProgBar(((Action) cmdAction).getProgressDesc(), progress);

                                    Thread.sleep(500);

                                } while (progress < 100);
                            } catch (InterruptedException ex) {
                            }
                        }
                    });
                   
                    th.start();
                    try {
                        initConnection(cmd);
                        ((Action) cmdAction).start(cmd, args);
                        printFinalProgBar(((Action) cmdAction).getProgressDesc(), ((InformationCallback)cmdAction).getErrors());
                        CommandFactory.INSTANCE.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        th.interrupt();
                    }
                    th.interrupt();

                }

            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);

                System.err.println(ex.getMessage());

                showHelp = true;
            }

            if (showHelp) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(args[0] + " [OPTIONS]", action.getDescription(), option, "");
            }

        }



    }

    public static void printProgBar(String progressDesc, int percent) {
        if (progressDesc == null || progressDesc.isEmpty()) {
            progressDesc = "init";
        }
        StringBuilder bar = new StringBuilder(progressDesc);

        /*for (int i = 0; i < 50; i++) {
         if (i < (percent / 2)) {
         bar.append("=");
         } else if (i == (percent / 2)) {
         bar.append(">");
         } else {
         bar.append(" ");
         }
         }*/

        bar.append("  [");
        if (percent < 100) {
            switch (progressId) {
                case 0:
                    bar.append("\\");
                    progressId++;
                    break;
                case 1:
                    bar.append("/");
                    progressId++;
                    break;
                case 2:
                    bar.append("-");
                    progressId = 0;
                    break;
            }
        } 
        bar.append("]");
        System.out.print("\r" + bar.toString());
       
    }
    
    private static void printFinalProgBar(String progressDesc, List<Object[]> errors) {
        StringBuilder bar = new StringBuilder(progressDesc);
        if (errors.isEmpty()) {
            bar.append("  [OK]");
            System.out.print("\r" + bar.toString()+"\n");
        } else {
            bar.append("  [FAILED]");
            bar.append("\nError Count: "+errors.size()+"\n");
            System.out.print("\r" + bar.toString()+"\n");
            int i = 1;
            for (Object[] msg: errors) {
                bar.append("Error "+i+": "+msg[0]+"\n");
                if (msg[1] != null) {
                    ((Exception)msg[1]).printStackTrace();
                }
            }
        }
        
    }


    /**
     * load properties from copcon.properties set a option only if this option
     * is not set on commandline
     *
     *
     */
    private static String[] loadProperties(String[] args) {

        List<String> argList = new ArrayList<String>(Arrays.asList(args));
        String propfile = null;
        if (argList.contains("-propfile")) {
            propfile = argList.get(argList.indexOf("-propfile") + 1);
            try {
                props.load(new FileInputStream(propfile));
                
                for (Entry<Object, Object> entry: props.entrySet()) {
                    if (!argList.contains((String)entry.getKey())) {
                        argList.add(1, (String)entry.getValue());
                        argList.add(1, "-"+(String)entry.getKey());
                    }
                }
                
            } catch (IOException ex) {
                System.err.println("could not load propertyfile " + propfile);
            }
        }
        return argList.toArray(new String[argList.size()]);
    }

    private static String getOption(String key, CommandLine cmd) {

        return cmd.hasOption(key) ? cmd.getOptionValue(key) : props.getProperty(key);

    }

    private static Options initOptions() throws IllegalArgumentException {
        Option serverUrl = OptionBuilder.withArgName("serverUrl")
                .hasArg()
                .withDescription("confluence server url e.g. http://confluence.test[:8990]")
                .isRequired()
                .create("serverurl");
        Option userOpt = OptionBuilder.withArgName("user")
                .hasArg()
                .withDescription("confluence user")
                .isRequired()
                .create("user");
        Option passwordOpt = OptionBuilder.withArgName("password")
                .hasArg()
                .withDescription("user password")
                .isRequired()
                .create("password");
        Option srcServerUrl = OptionBuilder.withArgName("srcserverUrl")
                .hasArg()
                .withDescription("confluence server url e.g. http://confluence.test[:8990]")
                .create("srcserverurl");
        Option srcUserOpt = OptionBuilder.withArgName("srcuser")
                .hasArg()
                .withDescription("source confluence user")
                .create("srcuser");
        Option srcPasswordOpt = OptionBuilder.withArgName("srcpassword")
                .hasArg()
                .withDescription("src user password")
                .create("srcpassword");
        Option help = OptionBuilder.withArgName("help")
                .hasArg(false)
                .withDescription("show help")
                .create("help");
        Option propfile = OptionBuilder.withArgName("propfile")
                .hasArg(true)
                .withDescription("load options from propertyfile")
                .create("propfile");
        Option dburl = OptionBuilder.withArgName("dburl")
                .hasArg(true)
                .withDescription("jdbc to the db of confluence.")
                .create("dburl");
        Option dbuser = OptionBuilder.withArgName("dbuser")
                .hasArg(true)
                .withDescription("username for db connection.")
                .create("dbuser");
        Option dbpassword = OptionBuilder.withArgName("dbpassword")
                .hasArg(true)
                .withDescription("password for db connection.")
                .create("dbpassword");
        Option srcdburl = OptionBuilder.withArgName("srcdburl")
                .hasArg(true)
                .withDescription("jdbc to the db of source confluence.")
                .create("srcdburl");
        Option srcdbuser = OptionBuilder.withArgName("srcdbuser")
                .hasArg(true)
                .withDescription("username for src db connection.")
                .create("srcdbuser");
        Option srcdbpassword = OptionBuilder.withArgName("srcdbpassword")
                .hasArg(true)
                .withDescription("password for src db connection.")
                .create("srcdbpassword");
        Option verbose = OptionBuilder.withArgName("verbose")
                .hasArg(false)
                .withDescription("show verbose output.")
                .create("verbose");
        Options option = new Options();
        option.addOption(serverUrl);
        option.addOption(userOpt);
        option.addOption(passwordOpt);
        option.addOption(srcServerUrl);
        option.addOption(srcUserOpt);
        option.addOption(srcPasswordOpt);
        option.addOption(help);
        option.addOption(propfile);
        option.addOption(dburl);
        option.addOption(dbuser);
        option.addOption(dbpassword);
        option.addOption(srcdburl);
        option.addOption(srcdbuser);
        option.addOption(srcdbpassword);
        option.addOption(verbose);
        return option;
    }

    private static void initConnection(CommandLine cmd) throws XmlRpcException, MalformedURLException {
        String server = cmd.getOptionValue("serverurl");
        String user = cmd.getOptionValue("user");
        String password = cmd.getOptionValue("password");
        CommandFactory.INSTANCE.login(server, user, password);

        String srcserver = cmd.getOptionValue("srcserverurl");
        String srcuser = cmd.getOptionValue("srcuser");
        String srcpassword = cmd.getOptionValue("srcpassword");

        if (srcserver != null || srcuser != null || srcpassword != null) {

            CommandFactory.INSTANCE.login(srcserver, srcuser, srcpassword);
        }
        
        if (cmd.hasOption("dburl")|| cmd.hasOption("dbuser")||cmd.hasOption("dbpassword")) {
            String dburl = cmd.getOptionValue("dburl");
            String dbuser = cmd.getOptionValue("dbuser");
            String dbpassword = cmd.getOptionValue("dbpassword");
            try {
                CommandFactory.INSTANCE.dblogin(server, dburl, dbuser, dbpassword);
            } catch (SQLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (cmd.hasOption("srcdburl")|| cmd.hasOption("srcdbuser")||cmd.hasOption("srcdbpassword")) {
            String dburl = cmd.getOptionValue("srcdburl");
            String dbuser = cmd.getOptionValue("srcdbuser");
            String dbpassword = cmd.getOptionValue("srcdbpassword");
            try {
                CommandFactory.INSTANCE.dblogin(srcserver, dburl, dbuser, dbpassword);
            } catch (SQLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    
    public static class ActionCommand {

        private Class actionClass;
        private String description;

        public ActionCommand(Class actionClass, String description) {
            this.actionClass = actionClass;
            this.description = description;
        }

        public Class getActionClass() {
            return actionClass;
        }

        public String getDescription() {
            return description;
        }
    }
}
