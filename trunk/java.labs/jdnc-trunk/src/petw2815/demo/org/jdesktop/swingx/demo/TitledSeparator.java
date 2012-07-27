/*
 * Created by JFormDesigner on Tue Nov 20 14:23:32 CET 2007
 */

package org.jdesktop.swingx.demo;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Peter Weishapl
 */
public class TitledSeparator extends JPanel {
	public TitledSeparator() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		lblTitle = new JLabel();
		separator1 = new JSeparator();
		CellConstraints cc = new CellConstraints();

		//======== this ========


		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			RowSpec.decodeSpecs("default")));

		//---- lblTitle ----
		lblTitle.setText("Title");
		add(lblTitle, cc.xy(1, 1));
		add(separator1, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	public String getTitle(){
		return lblTitle.getText();
	}
	
	public void setTitle(String title){
		lblTitle.setText(title);
	}
	

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private JLabel lblTitle;
	private JSeparator separator1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
