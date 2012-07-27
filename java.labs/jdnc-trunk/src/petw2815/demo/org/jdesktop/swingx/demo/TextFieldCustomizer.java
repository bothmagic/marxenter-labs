/*
 * Created by JFormDesigner on Tue Nov 20 16:53:37 CET 2007
 */

package org.jdesktop.swingx.demo;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import org.jdesktop.swingx.*;

/**
 * @author Peter Weishapl
 */
public class TextFieldCustomizer extends JPanel {
	public TextFieldCustomizer() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		panel1 = new JPanel();
		field = new JTextField();
		scrollPane1 = new JScrollPane();
		panel2 = new JPanel();
		customizePromptPanel1 = new CustomizePromptPanel();
		customizeBuddyField1 = new CustomizeBuddyField();
		customizeGeneralPanel1 = new CustomizeGeneralPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);

		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow, default, default:grow"),
				new RowSpec[] {
					FormFactory.UNRELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));
			panel1.add(field, cc.xy(2, 2));
		}
		add(panel1, cc.xy(1, 1));

		//======== scrollPane1 ========
		{
			scrollPane1.setBorder(Borders.DLU2_BORDER);
			scrollPane1.setOpaque(false);

			//======== panel2 ========
			{
				panel2.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					}));

				//---- customizePromptPanel1 ----
				customizePromptPanel1.setField(field);
				panel2.add(customizePromptPanel1, cc.xy(1, 1));

				//---- customizeBuddyField1 ----
				customizeBuddyField1.setField(field);
				panel2.add(customizeBuddyField1, cc.xy(1, 3));

				//---- customizeGeneralPanel1 ----
				customizeGeneralPanel1.setField(field);
				panel2.add(customizeGeneralPanel1, cc.xy(1, 5));
			}
			scrollPane1.setViewportView(panel2);
		}
		add(scrollPane1, cc.xy(1, 4));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private JPanel panel1;
	private JTextField field;
	private JScrollPane scrollPane1;
	private JPanel panel2;
	private CustomizePromptPanel customizePromptPanel1;
	private CustomizeBuddyField customizeBuddyField1;
	private CustomizeGeneralPanel customizeGeneralPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
