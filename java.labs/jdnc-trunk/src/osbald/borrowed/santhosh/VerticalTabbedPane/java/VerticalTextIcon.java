import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class VerticalTextIcon implements Icon {
    private JLabel renderer;
    private FontMetrics fontMetrics;
    private int width, height;
    private boolean clockwise;
    private static final double ROTATION = Math.PI / 2;

    public VerticalTextIcon(String text) {
        this(text, false);
    }

    public VerticalTextIcon(String text, boolean clockwise) {
        renderer = new JLabel(text);
        this.clockwise = clockwise;
        configure();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        //TODO really use our label for rendering?
        //TODO icon support? sub-pixel aliasing? (freebies with the above?)
        Graphics2D g2 = (Graphics2D) g;
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        AffineTransform oldTransform = g2.getTransform();

        g.setFont(renderer.getFont());
        g.setColor(renderer.getForeground());
        if (clockwise) {
            g2.translate(x + getIconWidth(), y);
            g2.rotate(ROTATION);
        } else {
            g2.translate(x, y + getIconHeight());
            g2.rotate(-ROTATION);
        }
        g.drawString(renderer.getText(), 0, fontMetrics.getLeading() + fontMetrics.getAscent());
        //renderer.paintComponents(g2);

        g.setFont(oldFont);
        g.setColor(oldColor);
        g2.setTransform(oldTransform);


    }

    public int getIconWidth() {
        return height;
    }

    public int getIconHeight() {
        return width;
    }

    public Color getForeground() {
        return renderer.getForeground();
    }

    public void setForeground(Color color) {
        renderer.setForeground(color);
    }

    public Font getFont() {
        return renderer.getFont();
    }

    public void setFont(Font font) {
        renderer.setFont(font);
        configure();
    }

    void configure() {
        fontMetrics = renderer.getFontMetrics(renderer.getFont());
        width = SwingUtilities.computeStringWidth(fontMetrics, renderer.getText());
        height = fontMetrics.getHeight();
    }
}