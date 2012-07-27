/*
 * ImportTest.java
 *
 * Created on March 3, 2005, 12:05 PM
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
import org.jdesktop.dataset.DataSet;

import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.io.transfer.*;

/**
 * <p>Command-line example of exporting a data table to an XML format (String) and copying to 
 * the clipboard. No arguments--just run, and then paste in another application to see the results.
 *
 * @author Patrick Wright
 */
public class ClipboardCopyExample {
    InputStream stream;
    
    /** Creates a new instance of ImportTest */
    public ClipboardCopyExample(String resourceName) {
        try {
            stream = getClass().getResourceAsStream(resourceName);
        } catch (Exception e) {
            System.out.println("can't open resource " + resourceName);
            return;
        }
        if ( stream == null ) {
            System.err.println("resource does not exist: " + resourceName);
            return;
        }
    }
    
    private void run() {
        try {
            TextTransfer textTransfer = new TextTransfer();
            DataTable table = Importer.fromCSVWithHeaders(new DataSet(), "activity", new InputStreamReader(stream));
            StringWriter sw = new StringWriter();
            Exporter.toSimpleXML("ActivityDS", table, sw);
            
            textTransfer.setClipboardContents(sw.toString());
            System.out.println("Copied Adventure activities to clipboard. Try pasting in a text document somewhere.");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String args[]) {
        new ClipboardCopyExample("resources/activity.csv").run();
    }
    
    
    /** taken from javapractices.com, http://www.javapractices.com/Topic82.cjp */
    public final class TextTransfer implements ClipboardOwner {
        /**
         * Empty implementation of the ClipboardOwner interface.
         */
        public void lostOwnership( Clipboard aClipboard, Transferable aContents) {
            //do nothing
        }
        
        /**
         * Place a String on the clipboard, and make this class the
         * owner of the Clipboard's contents.
         */
        public void setClipboardContents( String aString ){
            StringSelection stringSelection = new StringSelection( aString );
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents( stringSelection, this );
        }
        
        /**
         * Get the String residing on the clipboard.
         *
         * @return any text found on the Clipboard; if none found, return an
         * empty String.
         */
        public String getClipboardContents() {
            String result = "";
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            //odd: the Object param of getContents is not currently used
            Transferable contents = clipboard.getContents(null);
            boolean hasTransferableText = (contents != null) &&
                    contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            if ( hasTransferableText ) {
                try {
                    result = (String)contents.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException ex){
                    //highly unlikely since we are using a standard DataFlavor
                    System.out.println(ex);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            return result;
        }
    }
    
}
