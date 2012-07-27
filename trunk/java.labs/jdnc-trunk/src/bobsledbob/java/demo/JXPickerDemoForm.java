/*
    Copyright (c) 2006  Adam Taft bobsledbob@dev.java.net
    Copyright (c) 2006  Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, California 95054, U.S.A.
    All rights reserved.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

/*
 * Created by JFormDesigner on Tue Apr 25 12:03:25 MDT 2006
 */

package demo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.calendar.*;

/**
 * @author Adam Taft
 */
public class JXPickerDemoForm extends JPanel {
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	JLabel label7;
	JPanel panel1;
	JRadioButton lookAndFeelMetal;
	JRadioButton lookAndFeelSystem;
	JRadioButton lookAndFeelMotif;
	JRadioButton lookAndFeelGoodies;
	JComponent goodiesFormsSeparator1;
	JLabel label1;
	JComboBox comboBox1;
	JLabel label2;
	JXPicker listPicker;
	JLabel label3;
	JXPicker treePicker;
	JLabel label5;
	JXDatePicker xDatePicker1;
	JLabel label4;
	JXPicker datePicker;
	JLabel label6;
	JScrollPane scrollPane1;
	JTable table;
	JScrollPane aListScrollPane;
	JList list;
	JXMonthView monthView;
	JScrollPane aTableEditorScrollPane;
	JTree tableEditorTree;
	JXPicker tableEditorPicker;
	JScrollPane aTreeScrollPane;
	JTree tree;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public JXPickerDemoForm() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		label7 = new JLabel();
		panel1 = new JPanel();
		lookAndFeelMetal = new JRadioButton();
		lookAndFeelSystem = new JRadioButton();
		lookAndFeelMotif = new JRadioButton();
		lookAndFeelGoodies = new JRadioButton();
		goodiesFormsSeparator1 = compFactory.createSeparator("");
		label1 = new JLabel();
		comboBox1 = new JComboBox();
		label2 = new JLabel();
		listPicker = new JXPicker();
		label3 = new JLabel();
		treePicker = new JXPicker();
		label5 = new JLabel();
		xDatePicker1 = new JXDatePicker();
		label4 = new JLabel();
		datePicker = new JXPicker();
		label6 = new JLabel();
		scrollPane1 = new JScrollPane();
		table = new JTable();
		aListScrollPane = new JScrollPane();
		list = new JList();
		monthView = new JXMonthView();
		aTableEditorScrollPane = new JScrollPane();
		tableEditorTree = new JTree();
		tableEditorPicker = new JXPicker();
		aTreeScrollPane = new JScrollPane();
		tree = new JTree();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU7_BORDER);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.UNRELATED_GAP_COLSPEC,
				new ColumnSpec("max(default;150dlu):grow")
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- label7 ----
		label7.setText("Look and Feel:");
		add(label7, cc.xy(1, 1));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- lookAndFeelMetal ----
			lookAndFeelMetal.setText("Metal");
			lookAndFeelMetal.setSelected(true);
			panel1.add(lookAndFeelMetal, cc.xy(1, 1));

			//---- lookAndFeelSystem ----
			lookAndFeelSystem.setText("System");
			panel1.add(lookAndFeelSystem, cc.xy(3, 1));

			//---- lookAndFeelMotif ----
			lookAndFeelMotif.setText("Motif");
			panel1.add(lookAndFeelMotif, cc.xy(5, 1));

			//---- lookAndFeelGoodies ----
			lookAndFeelGoodies.setText("JGoodies Plastic XP");
			panel1.add(lookAndFeelGoodies, cc.xy(7, 1));
		}
		add(panel1, cc.xy(3, 1));
		add(goodiesFormsSeparator1, cc.xywh(1, 3, 3, 1));

		//---- label1 ----
		label1.setText("JComboBox:");
		add(label1, cc.xy(1, 5));

		//---- comboBox1 ----
		comboBox1.setModel(new DefaultComboBoxModel(new String[] {
			"Item 1",
			"Item 2",
			"Item 3"
		}));
		add(comboBox1, cc.xy(3, 5));

		//---- label2 ----
		label2.setText("JXPicker w/ JList:");
		add(label2, cc.xy(1, 7));

		//---- listPicker ----
		listPicker.setPopupComponent(aListScrollPane);
		add(listPicker, cc.xy(3, 7));

		//---- label3 ----
		label3.setText("JXPicker w/ JTree:");
		add(label3, cc.xy(1, 9));

		//---- treePicker ----
		treePicker.setPopupComponent(aTreeScrollPane);
		add(treePicker, cc.xy(3, 9));

		//---- label5 ----
		label5.setText("JXDatePicker:");
		add(label5, cc.xy(1, 11));
		add(xDatePicker1, cc.xy(3, 11));

		//---- label4 ----
		label4.setText("JXPicker w/ JXMonthView:");
		add(label4, cc.xy(1, 13));

		//---- datePicker ----
		datePicker.setPopupComponent(monthView);
		add(datePicker, cc.xy(3, 13));

		//---- label6 ----
		label6.setText("JTable w/ JXPicker Cell Editor");
		add(label6, cc.xy(1, 15));

		//======== scrollPane1 ========
		{

			//---- table ----
			table.setModel(new DefaultTableModel(
				new Object[][] {
					{null},
					{null},
					{null},
				},
				new String[] {
					null
				}
			));
			table.setPreferredScrollableViewportSize(new Dimension(0, 50));
			scrollPane1.setViewportView(table);
		}
		add(scrollPane1, cc.xy(3, 15));

		//======== aListScrollPane ========
		{
			aListScrollPane.setBackground(null);
			aListScrollPane.setFocusable(false);

			//---- list ----
			list.setModel(new AbstractListModel() {
				String[] values = {
					"Item 1",
					"Item 2",
					"Item 3"
				};
				public int getSize() { return values.length; }
				public Object getElementAt(int i) { return values[i]; }
			});
			list.setFocusable(false);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			aListScrollPane.setViewportView(list);
		}

		//---- monthView ----
		monthView.setFocusable(false);
		monthView.setBorder(LineBorder.createBlackLineBorder());

		//======== aTableEditorScrollPane ========
		{
			aTableEditorScrollPane.setViewportView(tableEditorTree);
		}

		//---- tableEditorPicker ----
		tableEditorPicker.setPopupComponent(aTableEditorScrollPane);

		//======== aTreeScrollPane ========
		{
			aTreeScrollPane.setViewportView(tree);
		}

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(lookAndFeelMetal);
		buttonGroup1.add(lookAndFeelSystem);
		buttonGroup1.add(lookAndFeelMotif);
		buttonGroup1.add(lookAndFeelGoodies);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

}
