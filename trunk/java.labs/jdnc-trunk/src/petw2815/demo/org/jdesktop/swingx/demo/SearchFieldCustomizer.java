package org.jdesktop.swingx.demo;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXSearchField;

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
public class SearchFieldCustomizer extends JPanel {
	public SearchFieldCustomizer() {
		initComponents();
	}

	private void search(ActionEvent e) {
		lblSearchString.setText(field.getText());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		panel1 = new JPanel();
		field = new JXSearchField();
		panel2 = new JPanel();
		label1 = new JLabel();
		lblSearchString = new JLabel();
		scrollPane1 = new JScrollPane();
		panel3 = new JPanel();
		customizeSearchPanel1 = new CustomizeSearchPanel();
		customizeGeneralPanel1 = new CustomizeGeneralPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========

		setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormFactory.PARAGRAPH_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.PARAGRAPH_GAP_ROWSPEC
				}));

			//---- field ----
			field.setUseSeperatePopupButton(true);
			field.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					search(e);
				}
			});
			panel1.add(field, cc.xy(3, 2));

			//======== panel2 ========
			{
				panel2.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.UNRELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- label1 ----
				label1.setText("Searching For:");
				panel2.add(label1, cc.xy(2, 1));

				//---- lblSearchString ----
				lblSearchString.setText("nothing yet");
				lblSearchString.setFont(lblSearchString.getFont().deriveFont(lblSearchString.getFont().getStyle() | Font.BOLD));
				panel2.add(lblSearchString, cc.xy(4, 1));
			}
			panel1.add(panel2, cc.xywh(1, 4, 5, 1));
		}
		add(panel1, cc.xy(1, 1));

		//======== scrollPane1 ========
		{
			scrollPane1.setBorder(Borders.DLU2_BORDER);
			scrollPane1.setOpaque(false);

			//======== panel3 ========
			{
				panel3.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- customizeSearchPanel1 ----
				customizeSearchPanel1.setField(field);
				panel3.add(customizeSearchPanel1, cc.xy(1, 1));

				//---- customizeGeneralPanel1 ----
				customizeGeneralPanel1.setField(field);
				panel3.add(customizeGeneralPanel1, cc.xy(1, 3));
			}
			scrollPane1.setViewportView(panel3);
		}
		add(scrollPane1, cc.xy(1, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private JPanel panel1;
	private JXSearchField field;
	private JPanel panel2;
	private JLabel label1;
	private JLabel lblSearchString;
	private JScrollPane scrollPane1;
	private JPanel panel3;
	private CustomizeSearchPanel customizeSearchPanel1;
	private CustomizeGeneralPanel customizeGeneralPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
