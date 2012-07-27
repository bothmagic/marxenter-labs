/*
 * Created on 27.12.2006
 *
 */
package org.jdesktop.swingx.renderer.scott.extreme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Extracted the extreme component from the original renderer to be 
 * usable in a RenderingComponentController. Changed to use FormLayout.
 * 
 * @author sky
 */
class RendererPanel extends JPanel {
    // Color for the from label
    static final Color FROM_COLOR = new Color(0,  81, 212);
    static final Color TEXT_COLOR = Color.DARK_GRAY;
    
        private final JLabel dateLabel;
        // NOTE: If you look at the original MailMan source you'll see I wrote
        // this code to deal with an arbitrary number of rows. I stuck with
        // 2 as that worked the best. Look to the original code for how this
        // is dealt with.
        private final JLabel labels[];
        private final JLabel fromLabel;
        private final JImagePanel imagePanel;
        private final JLabel subjectLabel;
        private final List<Component> toAdjust;
        private final char[] tmpChars;
        private String text;
        private int layoutWidth;
        private boolean showImage;

        RendererPanel() {
            this.showImage = true;
            tmpChars = new char[512];
            setBorder(new EmptyBorder(4, 4, 4, 4));
            setOpaque(true);
            fromLabel = new JLabel(" ");
            fromLabel.setForeground(FROM_COLOR);
            fromLabel.setFont(fromLabel.getFont().deriveFont(Font.ITALIC));
            subjectLabel = new JLabel("a");
            dateLabel = new JLabel("a");
            dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
            Font f = subjectLabel.getFont();
            subjectLabel.setFont(f.deriveFont(Font.BOLD, f.getSize() + 4f));
            labels = new JLabel[getRowCount()];
            for (int i = 0; i < getRowCount(); i++) {
                labels[i] = new JLabel(" ");
            }
            imagePanel = new JImagePanel();
            imagePanel.setBorder(new LineBorder(Color.BLACK, 1));
            imagePanel.setEditable(false);
//            COLUMN SPECS:
//                f:d:n, l:4dluX:n, f:d:n, l:4dluX:n, f:max(p;50dluX):g, l:4dluX:n, f:max(p;5dluX):g, l:4dluX:n, f:d:n
//                ROW SPECS:   
//                c:d:n, t:4dluY:n, c:d:n, t:4dluY:n, c:d:n
//
//                COLUMN GROUPS:  {}
//                ROW GROUPS:     {}
//
//                COMPONENT CONSTRAINTS
//                ( 1,  1,  1,  5, "d=f, d=f"); de.kleopatra.view.JTitledPlaceHolder; name=image
//                ( 3,  1,  5,  1, "d=f, d=c"); javax.swing.JLabel      "subject"; name=subject
//                ( 9,  1,  1,  1, "r, d=c"); javax.swing.JLabel      "date"; name=date
//                ( 3,  3,  7,  1, "d=f, d=c"); javax.swing.JLabel      "textstart"; name=textstart
//                ( 3,  5,  3,  1, "d=f, d=c"); javax.swing.JLabel      "textend"; name=textend
//                ( 7,  5,  3,  1, "r, d=c"); javax.swing.JLabel      "email"; name=email

            FormLayout formLayout = new FormLayout(
                  "f:30dlu:n, l:4dlu:n, f:d:n, l:4dlu:n, f:max(p;50dlu):g, l:4dlu:n, f:max(p;5dlu):g, l:4dlu:n, f:d:n",
                "c:p:n, t:2dlu:n, c:p:n, t:0dlu:n, c:p:n"
                    );
            PanelBuilder builder = new PanelBuilder(formLayout, this);
            CellConstraints cl = new CellConstraints();
            builder.add(imagePanel, cl.xywh(1,  1,  1,  5));
            builder.add(subjectLabel, cl.xywh(3,  1,  5,  1));
            builder.add(dateLabel, cl.xywh(9,  1,  1,  1));
            builder.add(labels[0], cl.xywh(3,  3,  7,  1));
            builder.add(labels[1], cl.xywh(3,  5,  3,  1));
            builder.add(fromLabel, cl.xywh(7,  5,  3,  1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
            toAdjust = new LinkedList<Component>();
            toAdjust.add(dateLabel);
            toAdjust.addAll(Arrays.asList(labels));
            toAdjust.add(fromLabel);
        }

        /**
         * quick shot to remove the image panel.
         *
         */
        public void hideImage() {
            if (!showImage) return;
            this.showImage = false;
            FormLayout layout = (FormLayout) getLayout();
            remove(imagePanel);
            layout.removeColumn(1);
            layout.removeColumn(1);
        }

        private int getRowCount() {
            return 2;
        }
        
        public void setMessage(Message m) {
            if (m == null) {
                text = null;
                subjectLabel.setText(" ");
                fromLabel.setText(" ");
                imagePanel.setImage(null);
                resetMessageLabels();
            } else {
                subjectLabel.setText(m.getSubject());
                if (m.getImageLocation() != null) {
                    imagePanel.setImagePath(m.getImageLocation());
                } else {
                    imagePanel.setImage(null);
                }
                text = m.getBody();
                reflowText();
                dateLabel.setText(DateHelper.convert(m.getDateTime()));
                String from = m.getFrom();
                if (from != null) {
                    fromLabel.setText(from);
                } else {
                    fromLabel.setText(" ");
                }
             }
        }
        
        public void doLayout() {
            super.doLayout();
            if (layoutWidth != getWidth()) {
                layoutWidth = getWidth();
                reflowText();
            }
        }
        
        public void setDefaultBorder() {
            setBorder(Borders.DLU4_BORDER);
        }
        
        private void adjustColors(Color bg, Color fg) {
            if (toAdjust == null) return;
            for (Component c : toAdjust) {
                c.setForeground(fg);
//                c.setBackground(bg);
            }
        }

        @Override
        public void setForeground(Color foreground) {
            super.setForeground(foreground);
            adjustColors(null, foreground);
        }
        
        public void setDefaultForeground() {
            fromLabel.setForeground(FROM_COLOR);
            for (Component c : labels) {
                c.setForeground(TEXT_COLOR);
            }
            
        }

        private int nextNonWhitespace(int index, int length) {
            while (index < length && Character.isWhitespace(tmpChars[index])) {
                tmpChars[index] = ' ';
                index++;
            }
            return index;
        }
        
        private int nextWhitespace(int index, int length) {
            while (index < length && !Character.isWhitespace(tmpChars[index])) {
                index++;
            }
            return index;
        }

        private void resetMessageLabels() {
            for (JLabel label : labels) {
                label.setText(" ");
            }
        }
        
        private void reflowText() {
            // This obviously isn't i18n aware; it was written for a demo.
            resetMessageLabels();
            int availableWidth = labels[0].getWidth();
            if (availableWidth > 0 && text != null) {
                int charCount = Math.min(tmpChars.length, text.length());
                text.getChars(0, charCount, tmpChars, 0);
                FontMetrics fm = labels[0].getFontMetrics(labels[0].getFont());
                int lineStart = nextNonWhitespace(0, charCount);
                int lastChunkThatFits = lineStart;
                int lineIndex = 0;
                int index = lineStart;
                boolean tooLong = false;
                while (!tooLong && index < charCount && lineIndex < labels.length) {
                    index = nextWhitespace(index, charCount);
                    int charWidth = fm.charsWidth(tmpChars, lineStart, index - lineStart);
                    if (charWidth < availableWidth) {
                        lastChunkThatFits = index;
                        index = nextNonWhitespace(index, charCount);
                    } else if (lastChunkThatFits == lineStart) {
                        tooLong = true;
                    } else {
                        // charWidth > availableWidth, lastChunkThatFits != lineStart
                        labels[lineIndex++].setText(new String(
                                tmpChars, lineStart, lastChunkThatFits - lineStart));
                        lineStart = nextNonWhitespace(lastChunkThatFits, charCount);
                        lastChunkThatFits = lineStart;
                    }
                }
                if (!tooLong && lineIndex < labels.length && lineStart != charCount) {
                    labels[lineIndex].setText(new String(
                            tmpChars, lineStart, charCount - lineStart));
                }
            }
        }


    }