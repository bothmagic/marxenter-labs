/*
 * $Id: SwingXDemo.java 685 2005-09-16 03:31:50Z david_hall $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx;

import com.jgoodies.forms.factories.Borders;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.jdesktop.demo.CmdLineParser;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.MainWindow;
import org.jdesktop.demo.swingx.common.FramedEditorPaneLinkVisitor;
import org.jdesktop.demo.swingx.common.MarginHighlighter;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.LinkModel;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;


/**
 * A JPanel that demonstrates the use of various SwingX components. Each
 * SwingX Component is shown in its own independent panel, and the user
 * can navigate between the different demos from a list.
 *
 * @author  Richard Bair
 * @author Patrick Wright
 * @author Jeanette Winzenburg
 * 
 */
public class SwingXDemo extends DemoPanel {
    
    //  components
    private JXTitledPanel descriptionContainer;
    // convenience alias to descriptionContainer's content
    private JXEditorPane descriptionPane;
    private JXTitledPanel demoContainer;
    private DropShadowBorder dsb = new DropShadowBorder(UIManager.getColor("Control"), 0, 8, .5f, 12, false, true, true, true);
    
    // controlling views and models
    private JXList demoList;
    /** A List of each component we're demonstrating--all of which are DemoPanel
     * instances. */
    private List<DemoPanel> demoPanels = new ArrayList<DemoPanel>();
    private DemoPanel currentDemo = null;
    private LinkAction readMoreLinkAction;
 
    /**
     * Creates new form SwingXDemo
     */
    public SwingXDemo() {
        this(Arrays.asList(new JXTableDemoPanel(),
                           new DecoratorDemoPanel(),
                           new JXTaskPaneDemoPanel(),
                           new JXTipOfTheDayDemoPanel(),
                           new JXHyperlinkDemoPanel(),
                           new JXDatePickerDemoPanel(),
                           new JXMonthViewDemoPanel(),
                           new AutoCompleteDemoPanel(),
                           new ActionDemoPanel(),
                           new AuthenticationDemoPanel(),
                           new JXErrorDialogDemoPanel(),
                           new DropShadowBorderPanel(),
                           new JXGlassBoxDemoPanel(),
                           new JXPanelTranslucencyDemoPanel()
                           ));

    }

    public SwingXDemo(List<DemoPanel> demoPanels) {
        this.demoPanels = demoPanels;
        
        initComponents();
        configureComponents();
        build();
        bind();
        
        
    }
    
    private void bind() {
        demoList.setModel(new AbstractListModel() {
            public int getSize() {
                return demoPanels.size();
            }
            
            public Object getElementAt(int index) {
                return demoPanels.get(index).getName();
            }
        });
        demoList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
             //   if (e.getValueIsAdjusting()) return;
                demoListValueChanged(e);
            }
            
        });
        descriptionPane.addHyperlinkListener(linkListener);    
        readMoreLinkAction = createReadMoreLink();
        descriptionContainer.addRightDecoration(new JXHyperlink(readMoreLinkAction));
    }
    
    private LinkAction createReadMoreLink() {
        LinkModel readMoreLink = new LinkModel("Read More...");
        readMoreLink.setURLString(getHowToURLString());
        LinkAction linkAction = new LinkAction(readMoreLink);
        linkAction.setVisitingDelegate(new FramedEditorPaneLinkVisitor());
//        URL url = getClass().getResource("/toolbarButtonGraphics/general/Information16.gif");
//        linkAction.putValue(Action.SMALL_ICON, new ImageIcon(url));
        return linkAction;
    }


    /**
     * create components we need access to.
     *
     */
    private void initComponents() {
        descriptionContainer = new JXTitledPanel();
        descriptionPane = new JXEditorPane();
        demoContainer = new JXTitledPanel();
        demoList = new JXList();
    }
    
    private void configureComponents() {
        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        demoList.setHighlighters(new HighlighterPipeline(
                new Highlighter[] {new MarginHighlighter(marginBorder)}));
    }

 
    private void build() {
        build(descriptionContainer, descriptionPane, false, "Information");
        JXTitledPanel listContainer = new JXTitledPanel();
        build(listContainer, demoList, true, "Table of Contents");
        build(demoContainer, null, true, "Demo");
        // this is a hack - components should have "nice" borders 
        // by default
        descriptionPane.setBorder(descriptionBorder);
        // top/bottom margins - same as descriptionPane
        demoList.setBorder(listBorder);
        
        JSplitPane detailsSP = createSplitPane(150, JSplitPane.VERTICAL_SPLIT);
        detailsSP.setLeftComponent(descriptionContainer);
        detailsSP.setRightComponent(demoContainer);
        
        JSplitPane mainSP = createSplitPane(200, JSplitPane.HORIZONTAL_SPLIT);
        mainSP.setLeftComponent(listContainer);
        mainSP.setRightComponent(detailsSP);
        setLayout(new BorderLayout());
        setBorder(Borders.TABBED_DIALOG_BORDER);
        add(mainSP);
    }
    
    private JSplitPane createSplitPane(int dividerLocation, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation);
        splitPane.setDividerLocation(dividerLocation);
        splitPane.setBorder(null);
      ((BasicSplitPaneUI)splitPane.getUI()).getDivider().setBorder(BorderFactory.createEmptyBorder());
        return splitPane;
    }

    private void build(JXTitledPanel container, JComponent component, boolean opaque, String title) {
        container.getContentContainer().setLayout(new BorderLayout());
        container.setBorder(dsb);
        container.setTitle(title);
        if (component != null) {
            component = buildScrollPane(component, opaque);
            container.getContentContainer().add(component);
        }
    }

    private JScrollPane buildScrollPane(JComponent component, boolean opaque) {
      JScrollPane scrollPane = new JScrollPane(component);
      scrollPane.setBorder(null);
      scrollPane.setOpaque(opaque);
      scrollPane.getViewport().setOpaque(opaque);
      component.setOpaque(opaque);
      return scrollPane;
    }

    
    private void demoListValueChanged(ListSelectionEvent evt) {
        DemoPanel dp = demoPanels.get(demoList.getSelectedIndex());
        currentDemo = dp;
        updateReadMoreLink();
        descriptionContainer.setTitle("Information :: " + dp.getInformationTitle());
        descriptionPane.setText(dp.getHtmlDescription());
        descriptionPane.setCaretPosition(0);
        demoContainer.setContentContainer(dp.getContent());
        demoContainer.revalidate();
        demoContainer.repaint();
    }

    private void updateReadMoreLink() {
        URL url = getHowToURL();
        if (url != null) {
            readMoreLinkAction.getLink().setURL(url);
            
        }
    }
    
    private URL getHowToURL() {
        return SwingXDemo.class.getResource(currentDemo.getHowToURLString());
    }

    public File getSourceFile() {
        if (currentDemo != null) {
            return currentDemo.getSourceFile();
        }
        return super.getSourceFile();
    }
    
    private HyperlinkListener linkListener=new HyperlinkListener(){
        public void hyperlinkUpdate(final HyperlinkEvent hle){
            
            if(hle.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
                try{
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            URL url = getHowToURL();
//                             readMoreLinkAction.getLink().setURL(url);
//                             readMoreLinkAction.actionPerformed(null);
                            new HowToWindow(currentDemo.getInformationTitle(), url).show();
                        }
                    });
                } catch(Exception e){
                    System.err.println("Couldn't load page");
                }
            }
        }
    };

    
    public static void main(String[] args) {
    	CmdLineParser parser = new CmdLineParser();
    	CmdLineParser.Option dOption = parser.addStringOption('d', "Demo");
    	try {
    		parser.parse(args);
    	} catch (CmdLineParser.UnknownOptionException ex) {
    		ex.printStackTrace();
    		printUsage();
    		System.exit(1);
    	} catch (CmdLineParser.IllegalOptionValueException ex) {
    		ex.printStackTrace();
    		printUsage();
    		System.exit(2);
    	}
    	//look for the -d argument
    	final String demoClasses = (String)parser.getOptionValue(dOption);
        if (demoClasses == null) {
            printUsage();
            System.exit(3);
        }
    	
    	MainWindow.setAppLookAndFeel();

        try {
            List<DemoPanel> panels = new ArrayList<DemoPanel>();
            for (String panel : demoClasses.split(",")) {
                panels.add((DemoPanel) Class.forName(panel).newInstance());
            }
            
            final SwingXDemo demo = new SwingXDemo(panels);
            MainWindow mainWindow = MainWindow.showMainWindow(demo);

            mainWindow.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e) {
                        synchronized(demo) {
                            demo.notify();
                        }
                    }
                });

            try {
                synchronized(demo) {
                    demo.wait();
                }
            }
            catch(InterruptedException x) {
                System.err.println("Interrupted while waiting for task to finish");
                x.printStackTrace();
                System.exit(4);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to find, create, and open the MainWindow. " +
                               "The Demo classes were '" + demoClasses + "'");
            
            e.printStackTrace();
            printUsage();
            System.exit(3);
        }
    }
    
    private static void printUsage() {
    	System.out.println("Pass in a comma separated list of demos to include, like so:\n" +
    			"-d org.jdesktop.demo.Demo1,org.jdesktop.demo.sample.Demo2");
    }
 }
