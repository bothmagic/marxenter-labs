/** BasicTest.java                          ZTests
 *
 * Created 26/01/2007 12:04:33 PM
 * 
 * @author Ray Turnbull
 */
package demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXDatePicker;

import dateinput.DateInputField;
/**
 *
 */
public class DateInputFieldTest {

	public void show() {
		JFrame f = new JFrame("Basic Test");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setContentPane(getPanel());
		f.pack();
		f.setVisible(true);
	}
	
	private JPanel getPanel() {
		JPanel p = new JPanel();
		DateInputField dif = new DateInputField();
		dif.setName("DIF");
		dif.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("value")) {
					JFormattedTextField t = (JFormattedTextField) e
					.getSource();
					System.out.println(t.getName() + ":"
							+ t.getValue());
				}								
			}
		});
		p.add(dif);
		JXDatePicker dp = new JXDatePicker();
		dp.setName("JXDP");
		dp.setEditor(new DateInputField("MM/dd/yy"));
		dp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JXDatePicker p = (JXDatePicker)e.getSource();
				System.out.println(p.getName() + ":" + p.getDate());
				System.out.println(p.getName() + ":" + p.getMonthView().getSelection());
				
			}
		});
		p.add(dp);
		return p;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new DateInputFieldTest().show();
	}

}
