package org.jdesktop.swingx.demo;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;



/**
 * @author Peter Weishapl
 */
public class TextAreaCustomizer extends JPanel {
	public TextAreaCustomizer() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		panel1 = new JPanel();
		scrollPane2 = new JScrollPane();
		textArea1 = new JTextArea();
		scrollPane1 = new JScrollPane();
		panel2 = new JPanel();
		customizePromptPanel1 = new CustomizePromptPanel();
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
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					FormFactory.UNRELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//======== scrollPane2 ========
			{

				//---- textArea1 ----
				textArea1.setRows(3);
				scrollPane2.setViewportView(textArea1);
			}
			panel1.add(scrollPane2, cc.xy(1, 2));
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
				customizePromptPanel1.setField(textArea1);
				customizePromptPanel1.setPrompt("Hello, I'm a plain JTextArea.\nCome play with me...");
				panel2.add(customizePromptPanel1, cc.xy(1, 1));
			}
			scrollPane1.setViewportView(panel2);
		}
		add(scrollPane1, cc.xy(1, 4));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private JPanel panel1;
	private JScrollPane scrollPane2;
	private JTextArea textArea1;
	private JScrollPane scrollPane1;
	private JPanel panel2;
	private CustomizePromptPanel customizePromptPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
