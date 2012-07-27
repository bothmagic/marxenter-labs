package org.jdesktop.swingx;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuBar;
import javax.swing.border.Border;

public class JXMenuBar extends JMenuBar {

    private JXBusyLabel busyLabel;
    private int y = 0;
    private int right = 0;
    private int left = 0;

    public JXMenuBar() {
        super();
        initBusyLabel();
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String propertyName=evt.getPropertyName();
                // According to the Javadocs, propertyName may be <code>null</code>
                // if multiple properties have changed - we need to check for this.
                if ("ancestor".equals(propertyName) ||
                        "border".equals(propertyName) ||
                        "componentOrientation".equals(propertyName)) {
                    initBusyLabel();
                }
            }
        });
    }

    private void initBusyLabel() {
        Dimension d = getPreferredSize();
        final Border border = getBorder();
        // border can also be null !
        if (border!=null) {
            Insets bi = border.getBorderInsets(this);
            y = bi.top;
            right = bi.right;
            left = bi.left;
            int h = d.height - y - bi.bottom;

            boolean wasRunning =  busyLabel != null && busyLabel.isBusy();
            busyLabel = new JXBusyLabel(new Dimension(h, h)) {
                @Override
                protected void frameChanged() {
                    //FYI: repaint after frame change
                    JXMenuBar.this.repaint(getBLX(), y, getWidth(), getHeight());
                }
            };

            busyLabel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    final String propertyName=evt.getPropertyName();
                    if (propertyName!=null) {
                        if ("busy".equals(propertyName) && !((Boolean)evt.getNewValue()).booleanValue()) {
                            // FYI: after reset to default repaint
                            JXMenuBar.this.repaint();
                        }
                    }
                }
            });
            busyLabel.setSize(busyLabel.getPreferredSize());
            busyLabel.setBusy(wasRunning);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics tmp = g.create();
        
        tmp.translate(getBLX(), y);
        busyLabel.paint(tmp);
    }
    
    public JXBusyLabel getBusyIcon() {
        return busyLabel;
    }
    
    private int getBLX() {
        if (getComponentOrientation().equals(ComponentOrientation.RIGHT_TO_LEFT)) {
            return left;
        } else { //L2R or UNK
            return getWidth() - busyLabel.getWidth() - right;
        }
    }
}
