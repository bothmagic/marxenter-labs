/*
 * Created by JFormDesigner on Tue Nov 20 14:49:01 CET 2007
 */

package org.jdesktop.swingx.demo;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.NativeSearchFieldSupport;
import org.jdesktop.swingx.util.OS;

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
public class CustomizeGeneralPanel extends CustomizePanel {
	private final DefaultFormatterFactory ff = new DefaultFormatterFactory(new InsetsFormatter());

	public CustomizeGeneralPanel() {
		initComponents();
	}

	@Override
	public void setField(JTextComponent textComponent) {
		super.setField(textComponent);
		cbOpaque.setSelected(textComponent.isOpaque());
		txtMargin.setFormatterFactory(ff);
		txtMargin.setValue(textComponent.getMargin());

		// Mac Hack: Textfields look odd when opaque, otherwise opaque is
		// default.
		if (OS.isMacOSX() && UIManager.getLookAndFeel().isNativeLookAndFeel()) {
			cbOpaque.setSelected(false);
		} else {
			cbOpaque.setSelected(true);
		}

		columnsChanged(null);

		if (!(textComponent instanceof JTextField)) {
			rbLeading.setEnabled(false);
			rbCenter.setEnabled(false);
			rbTrailing.setEnabled(false);
			cbFixedSize.setEnabled(false);
		}
	}

	private void rbBorderStateChanged(ChangeEvent e) {
		if (rbDefault.isSelected()) {
			getField().setBorder(UIManager.getBorder("TextField.border"));
			if (getField() instanceof JTextField) {
				JTextField field = (JTextField) getField();
				NativeSearchFieldSupport.setSearchField(field, NativeSearchFieldSupport.isSearchField(field));
			}
		} else {
			getField().setBorder(new LineBorder(Color.BLACK, lineSlider.getValue()));
		}
		changed();
	}

	private void marginChange(PropertyChangeEvent e) {
		getField().setMargin((Insets) txtMargin.getValue());
		changed();
	}

	private void opaqueChanged(ChangeEvent e) {
		getField().setOpaque(cbOpaque.isSelected());
		changed();
	}

	private void columnsChanged(ChangeEvent e) {
		if (getField() instanceof JTextField) {
			JTextField field = (JTextField) getField();
			if (cbFixedSize.isSelected()) {
				field.setColumns(((Integer) spColumns.getValue()).intValue());
			} else {
				field.setColumns(0);
			}

			changed();
		}
	}

	private void alignmentChanged(ChangeEvent e) {
		if (getField() instanceof JTextField) {
			JTextField field = (JTextField) getField();
			if (rbCenter.isSelected()) {
				field.setHorizontalAlignment(JTextField.CENTER);
			} else if (rbTrailing.isSelected()) {
				field.setHorizontalAlignment(JTextField.TRAILING);
			} else {
				field.setHorizontalAlignment(JTextField.LEADING);
			}
			changed();
		}
	}

	private void cbEnabledStateChanged(ChangeEvent e) {
		getField().setEnabled(cbEnabled.isSelected());
	}

	private void cbEditableStateChanged(ChangeEvent e) {
		getField().setEditable(cbEditable.isSelected());
	}

	private void btnResetActionPerformed(ActionEvent e) {
		getField().setMargin(UIManager.getInsets("TextField.margin"));
		setField(getField());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		titledSeparator1 = new TitledSeparator();
		panel3 = new JPanel();
		label5 = new JLabel();
		rbDefault = new JRadioButton();
		rbLine = new JRadioButton();
		lineSlider = new JSlider();
		label1 = new JLabel();
		txtMargin = new JFormattedTextField();
		btnReset = new JButton();
		label3 = new JLabel();
		rbLeading = new JRadioButton();
		rbCenter = new JRadioButton();
		rbTrailing = new JRadioButton();
		cbFixedSize = new JCheckBox();
		spColumns = new JSpinner();
		label2 = new JLabel();
		panel1 = new JPanel();
		cbEnabled = new JCheckBox();
		cbEditable = new JCheckBox();
		cbOpaque = new JCheckBox();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setBorder(null);

		setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC }));

		// ---- titledSeparator1 ----
		titledSeparator1.setTitle("General");
		add(titledSeparator1, cc.xywh(1, 1, 6, 1));

		// ======== panel3 ========
		{
			panel3.setLayout(new FormLayout(new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
					FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC }));

			// ---- label5 ----
			label5.setText("Border:");
			panel3.add(label5, cc.xy(1, 1));

			// ---- rbDefault ----
			rbDefault.setText("Default");
			rbDefault.setSelected(true);
			rbDefault.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					rbBorderStateChanged(e);
				}
			});
			panel3.add(rbDefault, cc.xywh(3, 1, 7, 1));

			// ---- rbLine ----
			rbLine.setText("Line:");
			rbLine.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					rbBorderStateChanged(e);
				}
			});
			panel3.add(rbLine, cc.xy(3, 3));

			// ---- lineSlider ----
			lineSlider.setMaximum(10);
			lineSlider.setValue(1);
			lineSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					rbBorderStateChanged(e);
				}
			});
			panel3.add(lineSlider, cc.xywh(5, 3, 7, 1));

			// ---- label1 ----
			label1.setText("Margin:");
			panel3.add(label1, cc.xy(1, 5));

			// ---- txtMargin ----
			txtMargin.addPropertyChangeListener("value", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					marginChange(e);
				}
			});
			panel3.add(txtMargin, cc.xywh(3, 5, 7, 1));

			// ---- btnReset ----
			btnReset.setText("Reset");
			btnReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnResetActionPerformed(e);
				}
			});
			panel3.add(btnReset, cc.xy(11, 5));

			// ---- label3 ----
			label3.setText("Alignment:");
			panel3.add(label3, cc.xy(1, 7));

			// ---- rbLeading ----
			rbLeading.setText("Leading");
			rbLeading.setSelected(true);
			rbLeading.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					alignmentChanged(e);
				}
			});
			panel3.add(rbLeading, cc.xy(3, 7));

			// ---- rbCenter ----
			rbCenter.setText("Center");
			rbCenter.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					alignmentChanged(e);
				}
			});
			panel3.add(rbCenter, cc.xy(5, 7));

			// ---- rbTrailing ----
			rbTrailing.setText("Trailing");
			rbTrailing.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					alignmentChanged(e);
				}
			});
			panel3.add(rbTrailing, cc.xy(7, 7));
		}
		add(panel3, cc.xywh(2, 3, 5, 1));

		// ---- cbFixedSize ----
		cbFixedSize.setText("Fixed Size:");
		cbFixedSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				columnsChanged(e);
			}
		});
		add(cbFixedSize, cc.xy(2, 5));

		// ---- spColumns ----
		spColumns.setModel(new SpinnerNumberModel(15, 0, 80, 1));
		spColumns.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				columnsChanged(e);
			}
		});
		add(spColumns, cc.xy(4, 5));

		// ---- label2 ----
		label2.setText("Columns");
		add(label2, cc.xy(6, 5));

		// ======== panel1 ========
		{
			panel1
					.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW) }, RowSpec
							.decodeSpecs("default")));

			// ---- cbEnabled ----
			cbEnabled.setText("Enabled");
			cbEnabled.setSelected(true);
			cbEnabled.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					cbEnabledStateChanged(e);
				}
			});
			panel1.add(cbEnabled, cc.xy(1, 1));

			// ---- cbEditable ----
			cbEditable.setText("Editable");
			cbEditable.setSelected(true);
			cbEditable.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					cbEditableStateChanged(e);
				}
			});
			panel1.add(cbEditable, cc.xy(3, 1));

			// ---- cbOpaque ----
			cbOpaque.setText("Opaque");
			cbOpaque.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					opaqueChanged(e);
				}
			});
			panel1.add(cbOpaque, cc.xy(5, 1));
		}
		add(panel1, cc.xywh(2, 7, 5, 1));

		// ---- grpBorder ----
		ButtonGroup grpBorder = new ButtonGroup();
		grpBorder.add(rbDefault);
		grpBorder.add(rbLine);

		// ---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rbLeading);
		buttonGroup1.add(rbCenter);
		buttonGroup1.add(rbTrailing);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private TitledSeparator titledSeparator1;
	private JPanel panel3;
	private JLabel label5;
	private JRadioButton rbDefault;
	private JRadioButton rbLine;
	private JSlider lineSlider;
	private JLabel label1;
	private JFormattedTextField txtMargin;
	private JButton btnReset;
	private JLabel label3;
	private JRadioButton rbLeading;
	private JRadioButton rbCenter;
	private JRadioButton rbTrailing;
	private JCheckBox cbFixedSize;
	private JSpinner spColumns;
	private JLabel label2;
	private JPanel panel1;
	private JCheckBox cbEnabled;
	private JCheckBox cbEditable;
	private JCheckBox cbOpaque;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
