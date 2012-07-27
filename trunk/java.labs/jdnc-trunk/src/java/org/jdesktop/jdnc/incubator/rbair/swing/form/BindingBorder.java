/*
 * $Id: BindingBorder.java 65 2004-09-22 21:25:51Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.form;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

import org.jdesktop.jdnc.incubator.rbair.swing.JXGlassBox;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.Binding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;

/**
 * @author Amy Fowler
 * @version 1.0
 */

public class BindingBorder
    implements Border {
    private static final int PAD = 0;
    private static Icon defaultValidIcon;
    private static Icon defaultInvalidIcon;
    private static Icon defaultRequiredIcon;

    private Binding binding;
    private Icon validIcon;
    private Icon invalidIcon;
    private Icon requiredIcon;
    private int iconPosition;

    private FocusListener focusListener;

    private Rectangle validIconBounds = new Rectangle();
    private Timer messageTriggerTimer;
    private int messageTriggerX;
    private int messageTriggerY;
    private JXGlassBox messageBox;

    static {
        URL url = BindingBorder.class.getResource("resources/blue-tipicon.png");
        defaultValidIcon = new ImageIcon(url);

        url = BindingBorder.class.getResource("resources/red-tipicon.png");
        defaultInvalidIcon = new ImageIcon(url);

        url = BindingBorder.class.getResource("resources/asterisk.8x8.png");
        defaultRequiredIcon = new ImageIcon(url);
    }

    public BindingBorder(Binding binding) {
        this(binding, SwingConstants.EAST);
    }

    public BindingBorder(Binding binding, int iconPosition) {
        this(binding, defaultValidIcon, defaultInvalidIcon, iconPosition);
    }

    public BindingBorder(Binding binding,
                         Icon validIcon, Icon invalidIcon, int iconPosition) {
        this(binding, validIcon, invalidIcon, null, iconPosition);
    }

    public BindingBorder(Binding binding,
                         Icon validIcon, Icon invalidIcon,
                         Icon requiredIcon,
                         int iconPosition) {
        this.binding = binding;
        this.validIcon = validIcon;
        this.invalidIcon = invalidIcon;
        this.iconPosition = iconPosition;
        Component component = binding.getComponent();
        MetaData metaData = binding.getDataModel().getMetaData(binding.getFieldName());
        if (metaData.isRequired()) {
            this.requiredIcon = requiredIcon;
        }
        binding.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                Binding binding = (Binding) e.getSource();
                if (e.getPropertyName().equals("validState")) {
                    binding.getComponent().repaint();
                }
            }
        });

        addFocusListener(component);

        messageTriggerTimer = new Timer(1000, new MessageTrigger());
        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (!isMessageBoxShowing() && validIconBounds.contains(x, y)) {
                    if (messageTriggerTimer.isRunning()) {
                        messageTriggerTimer.stop();
                    }
                    showMessageBox(x, y);
                }
            }
            public void mouseMoved(MouseEvent e) {
                if (!isMessageBoxShowing()) {
                    int x = e.getX();
                    int y = e.getY();
                    if (!messageTriggerTimer.isRunning()) {
                        if (validIconBounds.contains(x, y)) {
                            messageTriggerX = x;
                            messageTriggerY = y;
                            messageTriggerTimer.start();
                        }
                    }
                    else {
                        if (!validIconBounds.contains(x, y)) {
                            messageTriggerTimer.stop();
                        }
                    }
                }
            }
            public void mouseExited(MouseEvent e) {
                if (messageTriggerTimer.isRunning()) {
                    messageTriggerTimer.stop();
                }
            }
        };
        component.addMouseMotionListener(mouseAdapter);
        component.addMouseListener(mouseAdapter);
    }

    public Insets getBorderInsets(Component c) {
        if (requiredIcon != null) {
            return new Insets(0, 0, 0,
                              validIcon.getIconWidth() +
                              requiredIcon.getIconWidth() +
                              (3 * PAD));
        }
        return new Insets(0, 0, 0, validIcon.getIconWidth() + (2 * PAD));

    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComp = null;
        boolean paintValid = binding.getValidState() != Binding.INVALID;

        if (!hasFocus(c)) {
            // fade back icons if component or descendent does not have focus
            oldComp = g2d.getComposite();
            Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.
                SRC_OVER, 0.35f);
            g2d.setComposite(alphaComp);
        }
        Icon validIcon = (paintValid ? this.validIcon :
                          this.invalidIcon);

        int requiredIconSpace = (requiredIcon != null ?
                                 requiredIcon.getIconWidth() + PAD : 0);

        if (!isMessageBoxShowing()) {
            validIconBounds.x = x + width - PAD - requiredIconSpace -
                validIcon.getIconWidth();
            if (iconPosition == SwingConstants.NORTH_EAST) {
                validIconBounds.y = y + PAD;

            } else { // EAST
                validIconBounds.y = y +
                     ((height - validIcon.getIconHeight())/2);
            }
            validIconBounds.width = validIcon.getIconWidth();
            validIconBounds.height = validIcon.getIconHeight();
            validIcon.paintIcon(c, g, validIconBounds.x, validIconBounds.y);
        }

        if (requiredIcon != null) {
            int requiredX = x + width - PAD - requiredIcon.getIconWidth();
            int requiredY = y + (height - requiredIcon.getIconHeight()) / 2;
            requiredIcon.paintIcon(c, g, requiredX, requiredY);
        }

        if (!paintValid) {
            Color save = g.getColor();
            Color invalidColor = new Color(255, 0, 0, 225);
            g.setColor(invalidColor);
            g.drawRect(0, 0, c.getWidth(), c.getHeight());
            g.drawRect(1, 1, c.getWidth() - 2, c.getHeight() - 2);
            g.setColor(save);
        }

        if (oldComp != null) {
            g2d.setComposite(oldComp);
        }
    }

    private void addFocusListener(Component component) {
        if (focusListener == null) {
            focusListener = new FocusListener() {
                public void focusGained(FocusEvent e) {
                    binding.getComponent().repaint();
                }
                public void focusLost(FocusEvent e) {
                    binding.getComponent().repaint();
                }
            };
        }
        component.addFocusListener(focusListener);
        // Some components may be compound components where the focus
        // is taken on internal components
        if (component instanceof Container) {
            Component children[] = ((Container)component).getComponents();
            for(int i = 0; i < children.length; i++) {
                addFocusListener(children[i]);
            }
        }
    }

    private boolean hasFocus(Component component) {
        if (component.isFocusOwner()) {
            return true;
        }
        if (component instanceof Container) {
            Component children[] = ( (Container) component).getComponents();
            for (int i = 0; i < children.length; i++) {
                if (hasFocus(children[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMessageBoxShowing() {
        return (messageBox != null && messageBox.getParent() != null);
    }

    /** @todo: aim this will have localization problems - need to figure that out */
    private void showMessageBox(int x, int y) {
        JComponent component = binding.getComponent();
        boolean valid = binding.getValidState() != Binding.INVALID;
        Color borderColor = valid ? Color.blue : Color.red;
        Icon messageIcon = (valid ? validIcon : invalidIcon);
        MetaData metaData = binding.getDataModel().getMetaData(binding.
            getFieldName());

        messageBox = new JXGlassBox();
        messageBox.setAlpha(0.95f);
        messageBox.setBorder(new LineBorder(borderColor, 1));
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        messageBox.setLayout(gridBag);

        JLabel label = new JLabel(metaData.getLabel() + ":");
        Font boldFont = label.getFont().deriveFont(Font.BOLD);
        label.setFont(boldFont);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(2,4,0,2);
        gridBag.setConstraints(label, c);
        messageBox.add(label);

        Box textContent = new Box(BoxLayout.X_AXIS);
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(2, 2, 2, 2);
        gridBag.setConstraints(textContent, c);
        messageBox.add(textContent);

        JLabel iconLabel = new JLabel(messageIcon);
        c.gridx = 2;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(1,0,0,1);
        gridBag.setConstraints(iconLabel, c);
        messageBox.add(iconLabel);


        textContent.add(Box.createHorizontalStrut(4));
        Box content = new Box(BoxLayout.Y_AXIS);
        textContent.add(content);

        if (!valid) {
            String errors[] = binding.getValidationErrors();
            if (errors.length > 0) {
                for (int i = 0; i < errors.length; i++) {
                    content.add(new JLabel(errors[i]));
                }
            }
            else {
                content.add(new JLabel("contains invalid value"));
            }
        }
        else {
            content.add(new JLabel("value must be of type " +
                                   metaData.getElementClass().getName()));
            content.add(new JLabel("value is " +
                                   (metaData.isRequired() ? "required" :
                                    "optional")));
//            if (metaData instanceof NumberMetaData) {
//                NumberMetaData numberMetaData = (NumberMetaData) metaData;
//                Number minimum = numberMetaData.getMinimum();
//                if (minimum != null) {
//                    content.add(new JLabel("minimum value is " +
//                                           minimum.toString()));
//                }
//                Number maximum = numberMetaData.getMaximum();
//                if (maximum != null) {
//                    content.add(new JLabel("maximum value is " +
//                                           maximum.toString()));
//                }
//            }

        }
        /*
             Container glasspane = (Container)component.getRootPane().getGlassPane();
             Point p = SwingUtilities.convertPoint(component, x, y, glasspane);
                 messageBox.showOnGlassPane(glasspane, p.x, p.y);
         */
        Container glassPane = (Container) component.getRootPane().
            getGlassPane();
        // Only allow one message box to be visible at a time so
        // pop down any visible message boxes
        Component glassPaneChildren[] = glassPane.getComponents();
        for(int i = 0; i < glassPaneChildren.length; i++) {
            glassPaneChildren[i].setVisible(false);
            glassPane.remove(glassPaneChildren[i]);
        }
        messageBox.showOnGlassPane(glassPane, component, x, y,
                                   SwingConstants.TOP);

    }

    private class MessageTrigger implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            messageTriggerTimer.stop();
            showMessageBox(messageTriggerX, messageTriggerY);
        }
    }
}
