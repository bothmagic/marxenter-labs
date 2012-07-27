/*
 * ContactPanel.java
 *
 * Created on 27. M�rz 2007, 18:43
 */

package org.jdesktop.swingx.demo;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;

import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import com.jgoodies.forms.factories.*;

import org.jdesktop.swingx.*;
import org.jdesktop.swingx.PromptSupport.FocusBehavior;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * 
 * @author peterw
 */
public class ContactPanel extends javax.swing.JPanel {
    /** Creates new form ContactPanel */
    public ContactPanel() {
        initComponents();
        DateFormatter dateFormatter = new DateFormatter(new SimpleDateFormat(xFormattedTextField1.getPrompt())){
            public Object stringToValue(String text) throws ParseException {
                if(text == null || text.length() == 0){
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        xFormattedTextField1.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
        for (Component c : getComponents()) {
            if (c instanceof JTextComponent) {
                final JTextComponent txt = (JTextComponent) c;

                PromptSupport.setFocusBehavior(FocusBehavior.HIGHLIGHT_PROMPT, txt);
                txt.setOpaque(false);
                txt.setBorder(new EmptyBorder(1,1,1,1));
                txt.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        txt.setBorder(new LineBorder(Color.GRAY));
                        txt.selectAll();
                    }

                    public void focusLost(FocusEvent e) {
                        txt.setBorder(new EmptyBorder(1,1,1,1));
                        
                        txt.invalidate();
                        txt.getParent().validate();
                    }
                });
                txt.getDocument().addDocumentListener(new DocumentListener(){
                    public void changedUpdate(DocumentEvent e) {
                        change();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        change();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        change();
                    }
                    
                    private void change() {
                        txt.invalidate();
                        txt.getParent().validate();
                    }
                });
            }
        }
    }

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Peter Weishapl
    private JXTextField xTextField2;
    private JXTextField xTextField1;
    private JXTextField xTextField3;
    private JLabel label1;
    private JXFormattedTextField xFormattedTextField1;
    private JXTextField xTextField4;
    private JSeparator separator1;
    private JXTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		xTextField2 = new JXTextField();
		xTextField1 = new JXTextField();
		xTextField3 = new JXTextField();
		label1 = new JLabel();
		xFormattedTextField1 = new JXFormattedTextField();
		xTextField4 = new JXTextField();
		separator1 = new JSeparator();
		xTextArea1 = new JXTextArea();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(Color.white);
		setBorder(Borders.DLU4_BORDER);

		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			new RowSpec[] {
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- xTextField2 ----
		xTextField2.setPrompt("Prefix");
		xTextField2.setFont(xTextField2.getFont().deriveFont(xTextField2.getFont().getStyle() | Font.BOLD, xTextField2.getFont().getSize() + 2f));
		add(xTextField2, cc.xy(1, 1));

		//---- xTextField1 ----
		xTextField1.setPrompt("First Name");
		xTextField1.setPromptForeground(Color.red);
		xTextField1.setToolTipText("You must enter a first name.");
		xTextField1.setFont(xTextField1.getFont().deriveFont(xTextField1.getFont().getStyle() | Font.BOLD, xTextField1.getFont().getSize() + 2f));
		add(xTextField1, cc.xy(3, 1));

		//---- xTextField3 ----
		xTextField3.setPrompt("Last Name");
		xTextField3.setPromptFontStyle(null);
		xTextField3.setFont(xTextField3.getFont().deriveFont(xTextField3.getFont().getStyle() | Font.BOLD, xTextField3.getFont().getSize() + 2f));
		add(xTextField3, cc.xy(5, 1));

		//---- label1 ----
		label1.setText("Birthdate:");
		add(label1, cc.xy(9, 1));

		//---- xFormattedTextField1 ----
		xFormattedTextField1.setPrompt("MM/dd/yy");
		add(xFormattedTextField1, cc.xy(11, 1));

		//---- xTextField4 ----
		xTextField4.setPrompt("Company");
		add(xTextField4, cc.xywh(1, 3, 11, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		add(separator1, cc.xywh(1, 7, 11, 1));

		//---- xTextArea1 ----
		xTextArea1.setPrompt("Notes");
		add(xTextArea1, cc.xywh(1, 9, 11, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
}
