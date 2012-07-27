/*
 * Created by JFormDesigner on Tue Nov 20 14:20:13 CET 2007
 */

package org.jdesktop.swingx.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.PromptSupport;

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
public class CustomizePromptPanel extends CustomizePanel {
	public CustomizePromptPanel() {
		initComponents();
	}

	@Override
	public void setField(JTextComponent textComponent) {
		super.setField(textComponent);
		promptChanged(null);
		focusBehaviorChanged(null);
		backgroundChanged(null);
		foregroundChanged(null);
		fontChanged(null);
	}

	private void promptChanged(KeyEvent e) {
		PromptSupport.setPrompt(txtPrompt.getText(), getField());
		changed();
	}

	private void focusBehaviorChanged(ChangeEvent e) {
		PromptSupport.FocusBehavior fb = PromptSupport.FocusBehavior.HIDE_PROMPT;
		if (rbShow.isSelected()) {
			fb = PromptSupport.FocusBehavior.SHOW_PROMPT;
		} else if (rbHighlight.isSelected()) {
			fb = PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT;
		}
		PromptSupport.setFocusBehavior(fb, getField());
		changed();
	}

	private void foregroundChanged(ChangeEvent e) {
		Color fg = getField().getDisabledTextColor();
		if(rbRedForeground.isSelected()){
			fg = Color.red;
		}
		PromptSupport.setForeground(fg, getField());
		changed();
	}

	private void backgroundChanged(ChangeEvent e) {
		Color bg = getField().getBackground();
		if(rbYelowBackground.isSelected()){
			bg = Color.yellow;
		}
		PromptSupport.setBackground(bg, getField());
		changed();
	}

	private void fontChanged(ChangeEvent e) {
		if(rbBoldFont.isSelected()){
			PromptSupport.setFontStyle(Font.BOLD, getField());
		}else if(rbItalicFont.isSelected()){
			PromptSupport.setFontStyle(Font.ITALIC, getField());
		}else if(rbBoldItalicFont.isSelected()){
			PromptSupport.setFontStyle(Font.BOLD | Font.ITALIC, getField());
		}else{
			PromptSupport.setFontStyle(null, getField());
		}
		changed();
	}
	
	public void setPrompt(String prompt){
		txtPrompt.setText(prompt);
		promptChanged(null);
	}
	
	public String getPrompt(){
		return txtPrompt.getText();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Peter Weishapl
		titledSeparator1 = new TitledSeparator();
		label5 = new JLabel();
		txtPrompt = new JTextField();
		panel1 = new JPanel();
		label1 = new JLabel();
		rbHide = new JRadioButton();
		rbShow = new JRadioButton();
		rbHighlight = new JRadioButton();
		panel2 = new JPanel();
		label2 = new JLabel();
		rbDefaultForeground = new JRadioButton();
		rbRedForeground = new JRadioButton();
		label3 = new JLabel();
		rbDefaultBackground = new JRadioButton();
		rbYelowBackground = new JRadioButton();
		label4 = new JLabel();
		rbDefaultFont = new JRadioButton();
		rbItalicFont = new JRadioButton();
		rbBoldFont = new JRadioButton();
		rbBoldItalicFont = new JRadioButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========

		setLayout(new FormLayout(
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- titledSeparator1 ----
		titledSeparator1.setTitle("Prompt");
		add(titledSeparator1, cc.xywh(1, 1, 4, 1));

		//---- label5 ----
		label5.setText("Prompt:");
		add(label5, cc.xy(2, 3));

		//---- txtPrompt ----
		txtPrompt.setText("Hello, I'm a plain JTextField. Come play with me...");
		txtPrompt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				promptChanged(e);
			}
			@Override
			public void keyPressed(KeyEvent e) {
				promptChanged(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				promptChanged(e);
			}
		});
		add(txtPrompt, cc.xy(4, 3));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.UNRELATED_GAP_COLSPEC,
					FormFactory.PREF_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- label1 ----
			label1.setText("When this component has the focus, the prompt should be:");
			panel1.add(label1, cc.xywh(1, 1, 10, 1));

			//---- rbHide ----
			rbHide.setText("Hidden");
			rbHide.setSelected(true);
			rbHide.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					focusBehaviorChanged(e);
				}
			});
			panel1.add(rbHide, cc.xy(2, 3));

			//---- rbShow ----
			rbShow.setText("Shown");
			rbShow.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					focusBehaviorChanged(e);
				}
			});
			panel1.add(rbShow, cc.xy(4, 3));

			//---- rbHighlight ----
			rbHighlight.setText("Highlighted (Selected)");
			rbHighlight.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					focusBehaviorChanged(e);
				}
			});
			panel1.add(rbHighlight, cc.xy(6, 3));
		}
		add(panel1, cc.xywh(2, 5, 3, 1));

		//======== panel2 ========
		{
			panel2.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.RIGHT, Sizes.PREFERRED, FormSpec.NO_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.UNRELATED_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- label2 ----
			label2.setText("Foreground:");
			label2.setHorizontalAlignment(SwingConstants.RIGHT);
			panel2.add(label2, cc.xy(1, 1));

			//---- rbDefaultForeground ----
			rbDefaultForeground.setText("Default");
			rbDefaultForeground.setSelected(true);
			rbDefaultForeground.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					foregroundChanged(e);
				}
			});
			panel2.add(rbDefaultForeground, cc.xy(3, 1));

			//---- rbRedForeground ----
			rbRedForeground.setText("Red");
			rbRedForeground.setForeground(Color.red);
			rbRedForeground.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					foregroundChanged(e);
				}
			});
			panel2.add(rbRedForeground, cc.xy(5, 1));

			//---- label3 ----
			label3.setText("Background:");
			label3.setHorizontalAlignment(SwingConstants.RIGHT);
			panel2.add(label3, cc.xy(1, 3));

			//---- rbDefaultBackground ----
			rbDefaultBackground.setText("Default");
			rbDefaultBackground.setSelected(true);
			rbDefaultBackground.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					backgroundChanged(e);
				}
			});
			panel2.add(rbDefaultBackground, cc.xy(3, 3));

			//---- rbYelowBackground ----
			rbYelowBackground.setText("Yellow");
			rbYelowBackground.setBackground(Color.yellow);
			rbYelowBackground.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					backgroundChanged(e);
				}
			});
			panel2.add(rbYelowBackground, cc.xy(5, 3));

			//---- label4 ----
			label4.setText("Font:");
			label4.setHorizontalAlignment(SwingConstants.RIGHT);
			panel2.add(label4, cc.xy(1, 5));

			//---- rbDefaultFont ----
			rbDefaultFont.setText("Default");
			rbDefaultFont.setSelected(true);
			rbDefaultFont.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fontChanged(e);
				}
			});
			panel2.add(rbDefaultFont, cc.xy(3, 5));

			//---- rbItalicFont ----
			rbItalicFont.setText("Italic");
			rbItalicFont.setFont(rbItalicFont.getFont().deriveFont(rbItalicFont.getFont().getStyle() | Font.ITALIC));
			rbItalicFont.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fontChanged(e);
				}
			});
			panel2.add(rbItalicFont, cc.xy(5, 5));

			//---- rbBoldFont ----
			rbBoldFont.setText("Bold");
			rbBoldFont.setFont(rbBoldFont.getFont().deriveFont(rbBoldFont.getFont().getStyle() | Font.BOLD));
			rbBoldFont.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fontChanged(e);
				}
			});
			panel2.add(rbBoldFont, cc.xy(7, 5));

			//---- rbBoldItalicFont ----
			rbBoldItalicFont.setText("Bold & Italic");
			rbBoldItalicFont.setFont(rbBoldItalicFont.getFont().deriveFont(Font.BOLD|Font.ITALIC));
			rbBoldItalicFont.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fontChanged(e);
				}
			});
			panel2.add(rbBoldItalicFont, cc.xy(9, 5));
		}
		add(panel2, cc.xywh(2, 7, 3, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rbHide);
		buttonGroup1.add(rbShow);
		buttonGroup1.add(rbHighlight);

		//---- buttonGroup2 ----
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(rbDefaultForeground);
		buttonGroup2.add(rbRedForeground);

		//---- buttonGroup3 ----
		ButtonGroup buttonGroup3 = new ButtonGroup();
		buttonGroup3.add(rbDefaultBackground);
		buttonGroup3.add(rbYelowBackground);

		//---- buttonGroup4 ----
		ButtonGroup buttonGroup4 = new ButtonGroup();
		buttonGroup4.add(rbDefaultFont);
		buttonGroup4.add(rbItalicFont);
		buttonGroup4.add(rbBoldFont);
		buttonGroup4.add(rbBoldItalicFont);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Peter Weishapl
	private TitledSeparator titledSeparator1;
	private JLabel label5;
	private JTextField txtPrompt;
	private JPanel panel1;
	private JLabel label1;
	private JRadioButton rbHide;
	private JRadioButton rbShow;
	private JRadioButton rbHighlight;
	private JPanel panel2;
	private JLabel label2;
	private JRadioButton rbDefaultForeground;
	private JRadioButton rbRedForeground;
	private JLabel label3;
	private JRadioButton rbDefaultBackground;
	private JRadioButton rbYelowBackground;
	private JLabel label4;
	private JRadioButton rbDefaultFont;
	private JRadioButton rbItalicFont;
	private JRadioButton rbBoldFont;
	private JRadioButton rbBoldItalicFont;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
