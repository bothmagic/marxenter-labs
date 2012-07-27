/*
 * FilterPanel.java
 *
 * Created on 28 de Junho de 2005, 14:18
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.jdnc.incubator.rlopes.jdncutils;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.jdnc.JNTable;

/**
 *
 * @author Ricardo Lopes
 */
public class FilterPanel extends JPanel {

    public static final Icon ICON_CLOSE_FILTER_PANEL = new ImageIcon(FilterPanel.class.getResource("/org/jdesktop/jdnc/incubator/rlopes/jdncutils/resources/close.png"));
    public static final String CLIENT_PROPERTY_FILTER_PANEL_KEY = "CLIENT_PROPERTY_FILTER_PANEL_KEY";
    public static final Boolean CLIENT_PROPERTY_FILTER_PANEL_VALUE = Boolean.TRUE;    
    
    private JNTable jnTable;
    private JButton closeButton;
    private JTextField filterField;
    private int filterColumnIndex;

    public FilterPanel(JNTable lJNTable, int lFilterColumnIndex) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        jnTable = lJNTable;
        filterColumnIndex = lFilterColumnIndex;
        initUI();
    }

    protected void initUI() {                
        closeButton = new JButton(ICON_CLOSE_FILTER_PANEL);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JDNCUtils.unhookFilterPanel(jnTable);
            }
        });

        add(closeButton);
        add(new JLabel("Filter by: "));

        filterField = new JTextField(25);
        filterField.getDocument().addDocumentListener(new FilterPanelQuickSearcher(jnTable, this, filterColumnIndex));
        filterField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JDNCUtils.unhookFilterPanel(jnTable);
                }
            }
        });        
        add(filterField);
        putClientProperty(CLIENT_PROPERTY_FILTER_PANEL_KEY, CLIENT_PROPERTY_FILTER_PANEL_VALUE);
    }
    
    public void setFilterString(String filterString) {
        filterField.setText(filterString);
    }
    
    public String getFilterString() {
        return filterField.getText();
    }
    
    public void setInputFocus() {
        filterField.requestFocus();
    }
}

