package demo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.DateFormat;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Element;
 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  
import org.w3c.dom.Document;
import com.exalto.ExaltoXmlNode;

/**
 * Assembles the UI. The UI consists of a JTreeTable and a menu.
 * The JTreeTable uses a BookmarksModel to visually represent a bookmarks
 * file stored in the Netscape file format.
 *
 * @author Scott Violet
 */
public class TreeTableExample3 {
    /** Number of instances of TreeTableExample3. */
    private static int         ttCount;

    /** Used to represent the model. */
    private JXmlTreeTable         treeTable;
    /** Frame containing everything. */
    private JFrame             frame;
    /** Path created for. */
    private String             path;

    /** expr created for. */
    private String  expr = "1 + 2 * 3";

    Hashtable  nodeMapTbl = new Hashtable();
    int currRow = 0;
    int maxCol = 0;

    /**
     * Creates a TreeTableExample3, loading the bookmarks from the file
     * at <code>path</code>.
     */
    public TreeTableExample3(String fname) {
    	ttCount++;

    	frame = createFrame();

    	Container       cPane = frame.getContentPane();
    	JMenuBar        mb = createMenuBar();

        System.out.println( " file = " + fname); 
        File f = new File(fname);
        System.out.println( " file path = " + f.getAbsolutePath()); 
        
    	boolean rootVisible = false;
		Document document = null;
		Element root;
		ExaltoXmlNode top;
 	    Hashtable  nodeMapTbl = new Hashtable();
	    Vector nodeMapVec = new Vector();
	    ArrayList parentList = new ArrayList();
	    HashMap rowMapper = new HashMap();

    try {
    	DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
         factory.setIgnoringElementContentWhitespace(true);
           DocumentBuilder builder = factory.newDocumentBuilder();
           document = builder.parse( new File(fname));
    }
    catch( SAXParseException pe )  {
        System.out.println( "not a valid expression" ); 
    }
    catch( SAXException e ) {
        System.out.println( "a sax excep" );
    }
    catch( Exception e ) {
        System.out.println( "a generic excep!" );
    }
    
    ExaltoXmlNode enode = new ExaltoXmlNode(document.getDocumentElement());

    rootVisible = true;
    
     XmlTreeModel  model =  new SimpleTreeModel(enode, document, rootVisible);

	treeTable = new JXmlTreeTable(model);
	treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	treeTable.setPreferredScrollableViewportSize(new Dimension(1500,500)); 
 
//	treeTable = createTreeTable(model);
        JScrollPane sp = new JScrollPane(treeTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.getViewport().setBackground(Color.white);
	cPane.add(sp);

        frame.setSize(new Dimension(350,400));
	frame.setJMenuBar(mb);
//	frame.pack();
//	frame.show();
        
        frame.setVisible(true);
    }

    /**
     * Creates the JFrame that will contain everything.
     */
    protected JFrame createFrame() {
	JFrame       retFrame = new JFrame("TreeTable III -- " + path);

	retFrame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		frame.dispose();
		if (--ttCount == 0) {
		    System.exit(0);
		}
	    }
	});
	return retFrame;
    }

    /**
     * Creates a menu bar.
     */
    protected JMenuBar createMenuBar() {
        JMenu            fileMenu = new JMenu("File"); 
	JMenuItem        menuItem;

	menuItem = new JMenuItem("Open");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
	    }	    
	});
	fileMenu.add(menuItem);
	fileMenu.addSeparator();
	
	menuItem = new JMenuItem("Exit"); 
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		System.exit(0);
	    }
	});
	fileMenu.add(menuItem); 


	// Create a menu bar
	JMenuBar        menuBar = new JMenuBar();

	menuBar.add(fileMenu);

	// Menu for the look and feels (lafs).
	UIManager.LookAndFeelInfo[]        lafs = UIManager.
	                                    getInstalledLookAndFeels();
	ButtonGroup                        lafGroup = new ButtonGroup();

	JMenu          optionsMenu = new JMenu("Options");

	menuBar.add(optionsMenu);

	for(int i = 0; i < lafs.length; i++) {
	    JRadioButtonMenuItem rb = new JRadioButtonMenuItem(lafs[i].
							       getName()); 
	    optionsMenu.add(rb);
	    rb.setSelected(UIManager.getLookAndFeel().getName().equals
			   (lafs[i].getName()));
	    rb.putClientProperty("UIKey", lafs[i]);
	    rb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent ae) {
	            JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.
			                       getSource();
	            if(rb2.isSelected()) {
		        UIManager.LookAndFeelInfo       info =
                                      (UIManager.LookAndFeelInfo)
			               rb2.getClientProperty("UIKey");
		        try {
		            UIManager.setLookAndFeel(info.getClassName());
		            SwingUtilities.updateComponentTreeUI(frame);
			}
			catch (Exception e) {
		             System.err.println("unable to set UI " +
						e.getMessage());
			}
	            }
	        }
	    });
	    lafGroup.add(rb);
	}
	return menuBar;
    }

    
    public static void main(String[] args) {
    	new TreeTableExample3(args[0]);
    }
}

