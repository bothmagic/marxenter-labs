package org.jdesktop.swingx.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.NativeSearchFieldSupport;
import org.jdesktop.swingx.JXSearchField.LayoutStyle;
import org.jdesktop.swingx.JXSearchField.SearchMode;

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
public class CustomizeSearchPanel extends CustomizePanel {
	public CustomizeSearchPanel() {
		initComponents();
	}

	@Override
	public void setField(JTextComponent textComponent) {
		super.setField(textComponent);
		spnDelay.setValue(getSearchField().getInstantSearchDelay());
		cbUseNative.setSelected(getSearchField().isUseNativeSearchFieldIfPossible());
		cbUseNative.setEnabled(NativeSearchFieldSupport.isNativeSearchFieldSupported());
		cbUseSeperate.setSelected(getSearchField().isUseSeperatePopupButton());
		
		if(getSearchField().isVistaLayoutStyle()){
			rbVista.setSelected(true);
		}else{
			rbMac.setSelected(true);
		}
	}
	

	private JXSearchField getSearchField() {
		return (JXSearchField) getField();
	}

	private void searchModeChanged(ChangeEvent e) {
		if (rbRegular.isSelected()) {
			getSearchField().setSearchMode(SearchMode.REGULAR);
		}else{
			getSearchField().setSearchMode(SearchMode.INSTANT);
		}
		changed();
	}

	private void spnDelayStateChanged(ChangeEvent e) {
		getSearchField().setInstantSearchDelay(((Integer)spnDelay.getValue()).intValue());
	}

	private void cbUseNativeStateChanged(ChangeEvent e) {
		getSearchField().setUseNativeSearchFieldIfPossible(cbUseNative.isSelected());
		
		boolean nat = cbUseNative.isSelected() && cbUseNative.isEnabled();
		rbMac.setEnabled(!nat);
		rbVista.setEnabled(!nat);
		cbUseSeperate.setEnabled(!nat);
		
		changed();
	}

	private void styleChanged(ChangeEvent e) {
		if(rbMac.isSelected()){
			getSearchField().setLayoutStyle(LayoutStyle.MAC);
		}else{
			getSearchField().setLayoutStyle(LayoutStyle.VISTA);
		}
		changed();
	}

	private void cbWithPopupStateChanged(ChangeEvent e) {
		if(cbWithPopup.isSelected()){
			getSearchField().setRecentSearchesSaveKey("demoSearch");
		}else{
			getSearchField().setRecentSearchesSaveKey(null);
		}
	}

	private void cbUseSeperateStateChanged(ChangeEvent e) {
		getSearchField().setUseSeperatePopupButton(cbUseSeperate.isSelected());
	}

	private void btnResetStyleActionPerformed(ActionEvent e) {
		if (UIManager.getBoolean("SearchField.useSeperatePopupButton")) {
			getSearchField().customSetUIProperty("useSeperatePopupButton", Boolean.TRUE, true);
		} else {
			getSearchField().customSetUIProperty("useSeperatePopupButton", Boolean.FALSE, true);
		}

		getSearchField().customSetUIProperty("layoutStyle", UIManager.get("SearchField.layoutStyle"), true);
		getSearchField().customSetUIProperty("promptFontStyle", UIManager.get("SearchField.promptFontStyle"), true);
		
		setField(getSearchField());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		titledSeparator1 = new TitledSeparator();
		label1 = new JLabel();
		rbRegular = new JRadioButton();
		panel1 = new JPanel();
		label3 = new JLabel();
		rbInstant = new JRadioButton();
		spnDelay = new JSpinner();
		label2 = new JLabel();
		panel2 = new JPanel();
		label4 = new JLabel();
		label5 = new JLabel();
		panel3 = new JPanel();
		rbMac = new JRadioButton();
		rbVista = new JRadioButton();
		cbWithPopup = new JCheckBox();
		panel5 = new JPanel();
		cbUseSeperate = new JCheckBox();
		cbUseNative = new JCheckBox();
		panel4 = new JPanel();
		btnResetStyle = new JButton();
		label6 = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========

		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- titledSeparator1 ----
		titledSeparator1.setTitle("Search");
		add(titledSeparator1, cc.xywh(1, 1, 8, 1));

		//---- label1 ----
		label1.setText("Search Mode:");
		add(label1, cc.xy(2, 3));

		//---- rbRegular ----
		rbRegular.setText("Regular");
		rbRegular.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				searchModeChanged(e);
			}
		});
		add(rbRegular, cc.xy(4, 3));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));

			//---- label3 ----
			label3.setText("<html>Search when the user clicks the search icon or presses Enter.");
			label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() - 1f));
			panel1.add(label3, cc.xywh(2, 1, 5, 1));
		}
		add(panel1, cc.xywh(4, 5, 5, 1));

		//---- rbInstant ----
		rbInstant.setText("Instant, with");
		rbInstant.setSelected(true);
		rbInstant.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				searchModeChanged(e);
			}
		});
		add(rbInstant, cc.xy(4, 7));

		//---- spnDelay ----
		spnDelay.setModel(new SpinnerNumberModel(200, 0, 999, 1));
		spnDelay.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spnDelayStateChanged(e);
			}
		});
		add(spnDelay, cc.xy(6, 7));

		//---- label2 ----
		label2.setText("Milliseconds delay.");
		add(label2, cc.xy(8, 7));

		//======== panel2 ========
		{
			panel2.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));

			//---- label4 ----
			label4.setText("<html>Also search when the text search string changes.");
			label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() - 1f));
			panel2.add(label4, cc.xywh(2, 1, 5, 1));
		}
		add(panel2, cc.xywh(4, 9, 5, 1));

		//---- label5 ----
		label5.setText("Style:");
		add(label5, cc.xy(2, 11));

		//======== panel3 ========
		{
			panel3.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- rbMac ----
			rbMac.setText("Mac");
			rbMac.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					styleChanged(e);
				}
			});
			panel3.add(rbMac, cc.xy(1, 1));

			//---- rbVista ----
			rbVista.setText("Vista");
			rbVista.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					styleChanged(e);
				}
			});
			panel3.add(rbVista, cc.xy(3, 1));

			//---- cbWithPopup ----
			cbWithPopup.setText("With Popup Menu (Recent Searches)");
			cbWithPopup.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					cbWithPopupStateChanged(e);
				}
			});
			panel3.add(cbWithPopup, cc.xywh(1, 3, 5, 1));

			//======== panel5 ========
			{
				panel5.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.UNRELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("default")));

				//---- cbUseSeperate ----
				cbUseSeperate.setText("Use Seperate Popup Button");
				cbUseSeperate.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						cbUseSeperateStateChanged(e);
					}
				});
				panel5.add(cbUseSeperate, cc.xywh(2, 1, 2, 1));
			}
			panel3.add(panel5, cc.xywh(1, 5, 5, 1));

			//---- cbUseNative ----
			cbUseNative.setText("Use Native Search Field (Mac OS X Leopard only)");
			cbUseNative.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					cbUseNativeStateChanged(e);
				}
			});
			panel3.add(cbUseNative, cc.xywh(1, 7, 5, 1));
		}
		add(panel3, cc.xywh(4, 11, 5, 1));

		//======== panel4 ========
		{
			panel4.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));

			//---- btnResetStyle ----
			btnResetStyle.setText("Set Default Style");
			btnResetStyle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnResetStyleActionPerformed(e);
				}
			});
			panel4.add(btnResetStyle, cc.xy(1, 1));

			//---- label6 ----
			label6.setText("Depending on the current Look and Feel.");
			label6.setEnabled(false);
			panel4.add(label6, cc.xywh(3, 1, 3, 1));
		}
		add(panel4, cc.xywh(4, 13, 5, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rbRegular);
		buttonGroup1.add(rbInstant);

		//---- buttonGroup2 ----
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(rbMac);
		buttonGroup2.add(rbVista);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private TitledSeparator titledSeparator1;
	private JLabel label1;
	private JRadioButton rbRegular;
	private JPanel panel1;
	private JLabel label3;
	private JRadioButton rbInstant;
	private JSpinner spnDelay;
	private JLabel label2;
	private JPanel panel2;
	private JLabel label4;
	private JLabel label5;
	private JPanel panel3;
	private JRadioButton rbMac;
	private JRadioButton rbVista;
	private JCheckBox cbWithPopup;
	private JPanel panel5;
	private JCheckBox cbUseSeperate;
	private JCheckBox cbUseNative;
	private JPanel panel4;
	private JButton btnResetStyle;
	private JLabel label6;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
