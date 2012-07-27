package org.jdesktop.jdnc.incubator.rlopes;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jdesktop.jdnc.incubator.bierhance.IncompatibleLookAndFeelException;
import org.jdesktop.jdnc.incubator.rlopes.colorcombo.ColorComboBox;
import org.jdesktop.jdnc.incubator.rlopes.smiliescombo.SmilieComboBox;


public class Demo {


	public static void main(String args[]) {
		try {
			JFrame frame = new JFrame();		
			ColorComboBox combo = new ColorComboBox();
            combo.addItemListener(new ItemListener() {
               public void itemStateChanged(ItemEvent evt) {
                   System.err.println(evt);
               } 
            });

			ColorComboBox combo2 = new ColorComboBox();
            
            frame.setLayout(new BorderLayout());
			frame.add(combo, BorderLayout.WEST);
            frame.add(combo2, BorderLayout.NORTH);
            
            frame.add(new JPanel(), BorderLayout.CENTER);
            final SmilieComboBox smilieCombo = new SmilieComboBox();
            smilieCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    System.err.println("Action Event Selected Index : " + smilieCombo.getSelectedIndex());
                    System.err.println("Smilie : " + smilieCombo.getSelectedItem());
                }
            });
            SmilieComboBox smilieCombo2 = new SmilieComboBox();
            frame.add(smilieCombo2, BorderLayout.SOUTH);

            frame.add(smilieCombo, BorderLayout.EAST);
			frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (IncompatibleLookAndFeelException ile) {
			ile.printStackTrace();
		}
	}
}
