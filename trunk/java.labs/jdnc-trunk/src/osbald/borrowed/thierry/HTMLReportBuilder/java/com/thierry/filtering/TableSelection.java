package com.thierry.filtering;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.thierry.filtering.report.HTMLReportBuilder;
import com.thierry.filtering.report.TableReportGenerator;
import com.thierry.filtering.report.TextReportBuilder;

import sun.awt.datatransfer.DataTransferer;

public class TableSelection implements Transferable, ClipboardOwner {
	
	public static void installCopyAction(final JTable table) {
		KeyStroke copy = KeyStroke.getKeyStroke(
				KeyEvent.VK_C,
				ActionEvent.CTRL_MASK,
				false);

	      Action copyAction = new AbstractAction("Copy") {
		
			public void actionPerformed(ActionEvent e) {
				TableSelection transfer = new TableSelection(table);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transfer, transfer);
			}
		};
	      table.registerKeyboardAction(
	    		  copyAction,
	    		  "Copy",
	    		  copy,
	    		  JComponent.WHEN_FOCUSED);
	}
	
	 private static ArrayList<DataFlavor> htmlFlavors = 
		 new ArrayList<DataFlavor>();
	 
	 private static ArrayList<DataFlavor> textFlavors = 
		 new ArrayList<DataFlavor>();
	 private static ArrayList<DataFlavor> stringFlavors = 
		 new ArrayList<DataFlavor>();
	 
	 private static ArrayList<DataFlavor> supporterFlavors = 
		 new ArrayList<DataFlavor>(); 

     static {
		String encoding = null;
		DataTransferer transferer = DataTransferer.getInstance();
		if (transferer != null) {
		    encoding = transferer.getDefaultUnicodeEncoding();
		}
		htmlFlavors.add(
				new DataFlavor("text/html;charset="
						+encoding+";class=java.lang.String", "HTML Text"));
		htmlFlavors.add(
				new DataFlavor("text/html;charset="
						+encoding+";class=java.io.Reader", "HTML Text"));
		htmlFlavors.add(
				new DataFlavor("text/html;charset="
						+encoding+";class=java.io.InputStream", "HTML Text"));

		textFlavors.add(
				new DataFlavor("text/plain;charset="
						+encoding+";class=java.lang.String", "Plain Text"));
		textFlavors.add(
				new DataFlavor("text/plain;charset="
						+encoding+";class=java.io.Reader", "Plain Text"));
		textFlavors.add(
				new DataFlavor("text/plain;charset="
						+encoding+";class=java.io.InputStream", "Plain Text"));

         stringFlavors.add(new DataFlavor(
        		 DataFlavor.javaJVMLocalObjectMimeType+";class=java.lang.String",
        		 "String Flavor"));
         stringFlavors.add(DataFlavor.stringFlavor);
		
         supporterFlavors.addAll(htmlFlavors);
         supporterFlavors.addAll(textFlavors);
         supporterFlavors.addAll(stringFlavors);
     }
     
     private static boolean isHTMLDataFlavor(DataFlavor flavor) {
    	 return htmlFlavors.contains(flavor);
     }
     
     private static boolean isTextDataFlavor(DataFlavor flavor) {
    	 return textFlavors.contains(flavor);
     }
     private static boolean isStringDataFlavor(DataFlavor flavor) {
    	 return stringFlavors.contains(flavor);
     }

     private JTable table;

     public TableSelection(JTable table) {
    	 this.table = table;
     }



     public DataFlavor[] getTransferDataFlavors() {
         return (DataFlavor[]) supporterFlavors.toArray(
        		 new DataFlavor[supporterFlavors.size()]);
     }



     public boolean isDataFlavorSupported(DataFlavor flavor) {
         return supporterFlavors.contains(flavor);
     }



     public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    	 String transferedData = null;
    	 if (isHTMLDataFlavor(flavor)) {
    		 transferedData = getHTMLStringSelection();
    	 }
    	 if (isTextDataFlavor(flavor)) {
    		 transferedData = getTextStringSelection();
    	 }
    	 if (isStringDataFlavor(flavor)) {
    		 transferedData = getTextStringSelection();
    	 }
    	 if (transferedData != null) {
    		 return getTransferData(flavor, transferedData);
    	 }
         throw new UnsupportedFlavorException(flavor);

     }
     
     private String getHTMLStringSelection() {
    	return new TableReportGenerator(table).getSelection(new HTMLReportBuilder());
     }
     
     private Object getTransferData(DataFlavor flavor, String transfered) throws UnsupportedFlavorException {
    	 if (String.class.equals(flavor.getRepresentationClass())) {
             return transfered;
         } else if (Reader.class.equals(flavor.getRepresentationClass())) {
             return new StringReader(transfered);
         } else if (InputStream.class.equals(flavor.getRepresentationClass())) {
        	 return new ByteArrayInputStream(transfered.getBytes());
         }
    	 throw new UnsupportedFlavorException(flavor);
     }
     
     private String getTextStringSelection() {
    	 return new TableReportGenerator(table).getSelection(new TextReportBuilder());
     }

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
