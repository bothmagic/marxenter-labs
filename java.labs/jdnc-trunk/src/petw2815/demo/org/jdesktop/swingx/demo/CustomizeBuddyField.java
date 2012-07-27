/*
 * Created by JFormDesigner on Tue Nov 20 15:12:15 CET 2007
 */

package org.jdesktop.swingx.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.BuddySupport;

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
public class CustomizeBuddyField extends CustomizePanel {
	public CustomizeBuddyField() {
		initComponents();
	}
	
	@Override
	public void setField(JTextComponent textComponent) {
		super.setField(textComponent);
		txtOuterMargin.setFormatterFactory(new DefaultFormatterFactory(new InsetsFormatter()));
		Insets outerMargin = BuddySupport.getOuterMargin((JTextField) textComponent);
		if(outerMargin == null) outerMargin = new Insets(0,0,0,0);
		txtOuterMargin.setValue(outerMargin);
	}

	private Component getSelectedComponent() {
		if (cbCheckbox.isSelected()) {
			JCheckBox cb = new JCheckBox();
			cb.setMargin(new Insets(0,0,0,0));
			cb.setOpaque(false);
			cb.setCursor(Cursor.getDefaultCursor());
			return cb;
		} else if (cbRss.isSelected()) {
			return new RssButton();
		} else if (cbSnapBack.isSelected()) {
			return new SnapBackButton();
		} else if(cbGap.isSelected()){
			return BuddySupport.createGap(3);
		}
		return new BookmarkButton();
	}

	private void btnAddLeftActionPerformed(ActionEvent e) {
		BuddySupport.addLeft(getSelectedComponent(), getTextField());
		changed();
	}

	private void btnAddRightActionPerformed(ActionEvent e) {
		BuddySupport.addRight(getSelectedComponent(), getTextField());
		changed();
	}

	private void btnRemoveallActionPerformed(ActionEvent e) {
		BuddySupport.removeAll(getTextField());
		changed();
	}

	private void txtOuterMarginPropertyChange(PropertyChangeEvent e) {
		BuddySupport.setOuterMargin(getTextField(), (Insets) txtOuterMargin.getValue());
		changed();
	}

	private JTextField getTextField() {
		return (JTextField) getField();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		panel2 = new JPanel();
		titledSeparator1 = new TitledSeparator();
		label2 = new JLabel();
		txtOuterMargin = new JFormattedTextField();
		panel3 = new JPanel();
		cbSnapBack = new JRadioButton();
		toggleButton1 = new JLabel();
		cbRss = new JRadioButton();
		label1 = new JLabel();
		cbCheckbox = new JRadioButton();
		checkBox2 = new JCheckBox();
		cbGap = new JRadioButton();
		panel1 = new JPanel();
		btnAddLeft = new JButton();
		btnAddRight = new JButton();
		btnRemoveall = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== panel2 ========
		{

			panel2.setLayout(new BorderLayout());

			//======== this ========
			{
				this.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.UNRELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.UNRELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- titledSeparator1 ----
				titledSeparator1.setTitle("Buddies");
				this.add(titledSeparator1, cc.xywh(1, 1, 4, 1));

				//---- label2 ----
				label2.setText("Outer Margin:");
				this.add(label2, cc.xy(2, 3));

				//---- txtOuterMargin ----
				txtOuterMargin.addPropertyChangeListener("value", new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						txtOuterMarginPropertyChange(e);
					}
				});
				this.add(txtOuterMargin, cc.xy(4, 3));

				//======== panel3 ========
				{
					panel3.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							new ColumnSpec(ColumnSpec.LEFT, Sizes.DLUX1, FormSpec.NO_GROW),
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							new ColumnSpec(ColumnSpec.LEFT, Sizes.DLUX1, FormSpec.NO_GROW),
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							new ColumnSpec(ColumnSpec.LEFT, Sizes.DLUX1, FormSpec.NO_GROW),
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- cbSnapBack ----
					cbSnapBack.setSelected(true);
					panel3.add(cbSnapBack, cc.xy(1, 1));

					//---- toggleButton1 ----
					toggleButton1.setIcon(new ImageIcon(getClass().getResource("/org/jdesktop/swingx/demo/Search_SnapBack.png")));
					panel3.add(toggleButton1, cc.xy(3, 1));
					panel3.add(cbRss, cc.xy(5, 1));

					//---- label1 ----
					label1.setIcon(new ImageIcon(getClass().getResource("/org/jdesktop/swingx/demo/ShowRSSButton.png")));
					panel3.add(label1, cc.xy(7, 1));
					panel3.add(cbCheckbox, cc.xy(9, 1));
					panel3.add(checkBox2, cc.xy(11, 1));

					//---- cbGap ----
					cbGap.setText("Gap");
					panel3.add(cbGap, cc.xy(13, 1));

					//======== panel1 ========
					{
						panel1.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.UNRELATED_GAP_COLSPEC,
								new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW),
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
								FormFactory.UNRELATED_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC
							},
							RowSpec.decodeSpecs("default")));

						//---- btnAddLeft ----
						btnAddLeft.setText("Add Left");
						btnAddLeft.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								btnAddLeftActionPerformed(e);
							}
						});
						panel1.add(btnAddLeft, cc.xy(2, 1));

						//---- btnAddRight ----
						btnAddRight.setText("Add Right");
						btnAddRight.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								btnAddRightActionPerformed(e);
							}
						});
						panel1.add(btnAddRight, cc.xy(4, 1));

						//---- btnRemoveall ----
						btnRemoveall.setText("Remove All");
						btnRemoveall.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								btnRemoveallActionPerformed(e);
							}
						});
						panel1.add(btnRemoveall, cc.xywh(6, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
					}
					panel3.add(panel1, cc.xywh(1, 3, 13, 1));
				}
				this.add(panel3, cc.xywh(2, 5, 3, 1));
			}
			panel2.add(this, BorderLayout.CENTER);
		}

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(cbSnapBack);
		buttonGroup1.add(cbRss);
		buttonGroup1.add(cbCheckbox);
		buttonGroup1.add(cbGap);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private JPanel panel2;
	private TitledSeparator titledSeparator1;
	private JLabel label2;
	private JFormattedTextField txtOuterMargin;
	private JPanel panel3;
	private JRadioButton cbSnapBack;
	private JLabel toggleButton1;
	private JRadioButton cbRss;
	private JLabel label1;
	private JRadioButton cbCheckbox;
	private JCheckBox checkBox2;
	private JRadioButton cbGap;
	private JPanel panel1;
	private JButton btnAddLeft;
	private JButton btnAddRight;
	private JButton btnRemoveall;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}