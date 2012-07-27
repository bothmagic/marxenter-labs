package org.jdesktop.incubator;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.border.IconBorder;
import org.jdesktop.incubator.plaf.JXInformationPaneAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/*
 * Needed a quick something that can display input hints & validation messages alike.
 * 
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 10-Jul-2007
 * Time: 12:06:36
 */

public class JXInformationPane extends JPanel {
    private String text;
    private Level level;
    private Icon icon;
    private JComponent textComponent;
    private IconBorder border;
    private JScrollPane scrollPane;

    //TODO replace background color with background painter?
    //TODO what about a list of message components (think hypertext links)?
    //NB what about a UIDelegate.. not much call?

    static {
        LookAndFeelAddons.contribute(new JXInformationPaneAddon());
    }

    public static enum Level {
        PLAIN,
        INFORMATION,
        WARNING,
        ERROR
    }

    public JXInformationPane() {
        putClientProperty("contentType", UIManager.get("JXInformationPane.contentType"));
        putClientProperty("contentType", "text/html");  // ..change our minds as jxlabel isn't a happy camper here!
        buildUI();
    }

    protected void buildUI() {
        setLayout(new BorderLayout());
        setBorder(UIManager.getBorder("JXInformationPane.border"));
        textComponent = createTextComponent();
        border = new IconBorderExt(null, SwingConstants.NORTH_WEST, 5);
        textComponent.setBorder(border);
        scrollPane = new JScrollPane(textComponent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if ("text".equals(event.getPropertyName())) {
                    String text = (String) event.getNewValue();
                    if (textComponent instanceof JLabel) {
                        ((JLabel) textComponent).setText(text);
                    } else if (textComponent instanceof JTextComponent) {
                        ((JTextComponent) textComponent).setText(text);
                        ((JTextComponent) textComponent).setCaretPosition(0);
                        textComponent.revalidate();
                    }
                } else if ("level".equals(event.getPropertyName())) {
                    setIcon(getLevelIcon((Level) event.getNewValue()));
                } else if ("icon".equals(event.getPropertyName())) {
                    Icon icon = (Icon) event.getNewValue();
                    if (border != null) {
                        border.setIcon(icon);
                        repaint();
                    }
                } else if ("foreground".equals(event.getPropertyName())) {
                    if (textComponent != null) {
                        textComponent.setForeground((Color) event.getNewValue());
                    }
                } else if ("background".equals(event.getPropertyName())) {
                    if (scrollPane != null) {
                        scrollPane.getViewport().setBackground((Color) event.getNewValue());
                    }
                }
            }
        });

        textComponent.setFont(UIManager.getFont("JXInformationPane.font"));
        setForeground(UIManager.getColor("JXInformationPane.foreground"));
        setBackground(UIManager.getColor("JXInformationPane.background"));
        setMinimumSize(new Dimension(0, 0));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null && text.length() > 0) {
            setText(Level.INFORMATION, text);
        } else {
            setText(null, "");
        }
    }

    public void setText(Level level, String text) {
        setLevel(level);
        String contentType = (String) getClientProperty("contentType");
        if ("text/html".equals(contentType) && !text.startsWith("<html>")) {
            text = normaliseHtml(text);
        }
        firePropertyChange("text", this.text, this.text = text);
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        firePropertyChange("icon", this.icon, this.icon = icon);
    }

    Level getLevel() {
        return level;
    }

    void setLevel(Level level) {
        firePropertyChange("level", this.level, this.level = level);
    }

    protected Icon getLevelIcon(Level level) {
        if (level != null) {
            switch (level) {
                case INFORMATION:
                    return UIManager.getIcon("JXInformationPane.informationIcon");
                case WARNING:
                    return UIManager.getIcon("JXInformationPane.warningIcon");
                case ERROR:
                    return UIManager.getIcon("JXInformationPane.errorIcon");
            }
        }
        return null;
    }

    protected JComponent createTextComponent() {
        String contentType = (String) getClientProperty("contentType");
        if ("text/html".equals(contentType)) {
            JEditorPane textPane = new JEditorPane();
            textPane.setContentType(contentType);
            textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            textPane.setOpaque(false);
            textPane.setEditable(false);
            configureHtml(textPane);
            return textPane;
        } else {
            //TODO really not working as I'd like.. (insets & html wrapping!)
            JXLabel label = new JXLabel();
            label.setLineWrap(true);
            label.setVerticalAlignment(SwingConstants.TOP);
            //? label.setPaintBorderInsets(true);
            configureHtml(label);
            return label;
        }
    }

    protected String normaliseHtml(String text) {
        return text.replace("\n", "<br>").trim();
    }

    protected void configureHtml(JComponent component) {
        try {
            HTMLDocument doc = null;
            if (component instanceof JEditorPane) {
                if (((JEditorPane) component).getDocument() instanceof HTMLDocument) {
                    doc = (HTMLDocument) ((JEditorPane) component).getDocument();
                }
            } else {
                View v = (View) component.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
                if (v != null && v.getDocument() instanceof HTMLDocument) {
                    doc = (HTMLDocument) v.getDocument();
                }
            }
            if (doc != null) {
                //TODO inject stylesheet via L&F
                String stylesheet = "body { margin: 0px; margin-bottom: 5px; padding: 0px; border: 0px; }\n"
                        + "h1, h2, h3, h4, ul, li, dl, dt, dd { margin: 0px; padding: 0px; border: 0px; }\n"
                        + "h1, h2, h3, h4 { font-size: 1em; margin-bottom: 5px;}";
                doc.getStyleSheet().loadRules(new java.io.StringReader(stylesheet), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //PENDING probally a pretty naive estimate - feedback..?

    public void setVisibleRowsColumns(int rows, int columns) {
        rows = Math.max(rows, 0);
        columns = Math.max(columns, 0);
        FontMetrics fontMetrics = textComponent.getFontMetrics(textComponent.getFont());
        int rowHeight = fontMetrics.getHeight();
        int columnWidth = fontMetrics.charWidth('m');
        setPreferredSize(new Dimension(30 + columnWidth * columns, 10 + rowHeight * rows));
        revalidate();
    }


    static class IconBorderExt extends IconBorder {
        private Icon icon;
        private int iconPosition;
        private Rectangle iconBounds = new Rectangle();

        public IconBorderExt(Icon icon, int position, int padding) {
            setIcon(icon);
            setPadding(padding);
            this.iconPosition = position;
        }

        @Override
        public void setIcon(Icon icon) {
            super.setIcon(icon);
            this.icon = icon != null ? icon : IconBorder.EMPTY_ICON;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int horizontalInset = icon.getIconWidth() + (2 * getPadding());
            if (iconPosition == SwingConstants.NORTH_EAST) {
                return new Insets(getPadding(), 0, 0, horizontalInset);
            } else if (iconPosition == SwingConstants.NORTH_WEST) {
                return new Insets(getPadding(), horizontalInset, 0, 0);
            } else if (iconPosition == SwingConstants.SOUTH_EAST) {
                return new Insets(0, 0, getPadding(), horizontalInset);
            } else if (iconPosition == SwingConstants.SOUTH_WEST) {
                return new Insets(0, horizontalInset, getPadding(), 0);
            } else {
                return super.getBorderInsets(c);
            }
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            int padding = getPadding();
            if (iconPosition == SwingConstants.NORTH_EAST) {
                iconBounds.y = y + padding;
                iconBounds.x = x + width - padding - icon.getIconWidth();
            } else if (iconPosition == SwingConstants.NORTH_WEST) {
                iconBounds.y = y + padding;
                iconBounds.x = x + padding;
            } else if (iconPosition == SwingConstants.SOUTH_EAST) {
                iconBounds.y = y + height - padding - icon.getIconWidth();
                iconBounds.x = x + width - padding - icon.getIconWidth();
            } else if (iconPosition == SwingConstants.SOUTH_WEST) {
                iconBounds.y = y + height - padding - icon.getIconWidth();
                iconBounds.x = x + padding;
            } else {
                super.paintBorder(c, g, x, y, width, height);
                return;
            }
            icon.paintIcon(c, g, iconBounds.x, iconBounds.y);
        }
    }
}
