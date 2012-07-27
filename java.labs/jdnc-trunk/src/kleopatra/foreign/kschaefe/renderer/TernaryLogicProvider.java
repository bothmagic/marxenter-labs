/*
 * Created on 21.02.2008
 *
 */
package kschaefe.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.JRendererCheckBox;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

/**
 * 
 */
public class TernaryLogicProvider extends ComponentProvider<AbstractButton> {
    /**
     * Generic three value set.
     */
    public static enum TernaryOperator {
        /**
         * Indicates the {@code true} state. Signified by a check mark.
         */
        TRUE,

        /**
         * Indicates the {@code false} state. Signified by a X.
         */
        FALSE,

        /**
         * Indicates an unknown state. Signified by an empty box.
         */
        UNKNOWN
    }

    /**
     * 
     */
    public static interface TernaryLogicValue {
        /**
         * An implementation of {@code TernaryLogicValue} that converts a {@code Boolean} into a
         * {@code TernaryOperator}. {@code Boolean} maps as follows:
         * <ul>
         * <li>{@code Boolean.TRUE  => TernaryOperator.TRUE}</li>
         * <li>{@code Boolean.FALSE => TernaryOperator.FALSE}</li>
         * <li>{@code null          => TernaryOperator.UNKNOWN}</li>
         * </ul>
         * However, since it would be possible to pass a non-{@code Boolean},
         * {@code !(value instanceof Boolean} => TernaryOperator.UNKNOWN}.
         */
        TernaryLogicValue FROM_BOOLEAN = new TernaryLogicValue() {
            /**
             * {@inheritDoc}
             */
            public TernaryOperator getValue(Object value) {
                if (value instanceof Boolean) {
                    Boolean b = (Boolean) value;

                    if (b.equals(Boolean.TRUE)) {
                        return TernaryOperator.TRUE;
                    }

                    return TernaryOperator.FALSE;
                }

                return TernaryOperator.UNKNOWN;
            }

        };

        /**
         * Obtains a {@code TernaryOperator} from the supplied value.
         * 
         * @param value
         *            the value to evaluate
         * @return a {@code TernaryOperator} for the supplied value. This will never return
         *         {@code null}.
         */
        TernaryOperator getValue(Object value);
    }

    public static class XMarkIcon implements Icon {
        private static final int SIZE = 13;

        private Icon delegate = UIManager.getIcon("CheckBox.icon"); //$NON-NLS-1$

        /**
         * {@inheritDoc}
         */
        public int getIconHeight() {
            if (delegate != null) {
                return delegate.getIconHeight();
            }

            return SIZE;
        }

        /**
         * {@inheritDoc}
         */
        public int getIconWidth() {
            if (delegate != null) {
                return delegate.getIconWidth();
            }

            return SIZE;
        }

        /**
         * {@inheritDoc}
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setColor(UIManager.getColor("CheckBox.shadow")); //$NON-NLS-1$
                g2d.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);

                g2d.setColor(UIManager.getColor("CheckBox.interiorBackground")); //$NON-NLS-1$
                g2d.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
                g2d.setColor(Color.decode("#CC3030")); //$NON-NLS-1$
                g2d.drawLine(x + 3, y + 3, x + 3, y + 3);
                g2d.drawLine(x + 9, y + 3, x + 9, y + 3);
                g2d.drawLine(x + 4, y + 4, x + 4, y + 4);
                g2d.drawLine(x + 8, y + 4, x + 8, y + 4);
                g2d.drawLine(x + 5, y + 5, x + 5, y + 5);
                g2d.drawLine(x + 7, y + 5, x + 7, y + 5);
                g2d.drawLine(x + 6, y + 6, x + 6, y + 6);
                g2d.drawLine(x + 5, y + 7, x + 5, y + 7);
                g2d.drawLine(x + 7, y + 7, x + 7, y + 7);
                g2d.drawLine(x + 4, y + 8, x + 4, y + 8);
                g2d.drawLine(x + 8, y + 8, x + 8, y + 8);
                g2d.drawLine(x + 3, y + 9, x + 3, y + 9);
                g2d.drawLine(x + 9, y + 9, x + 9, y + 9);

                // should we make the four center points 75% alpha?
                g2d.setColor(ColorUtil.setAlpha(g2d.getColor(), 127));
                g2d.drawLine(x + 4, y + 3, x + 4, y + 3);
                g2d.drawLine(x + 8, y + 3, x + 8, y + 3);
                g2d.drawLine(x + 3, y + 4, x + 3, y + 4);
                g2d.drawLine(x + 5, y + 4, x + 5, y + 4);
                g2d.drawLine(x + 7, y + 4, x + 7, y + 4);
                g2d.drawLine(x + 9, y + 4, x + 9, y + 4);
                g2d.drawLine(x + 4, y + 5, x + 4, y + 5);
                g2d.drawLine(x + 6, y + 5, x + 6, y + 5); // center point
                g2d.drawLine(x + 8, y + 5, x + 8, y + 5);
                g2d.drawLine(x + 5, y + 6, x + 5, y + 6); // center point
                g2d.drawLine(x + 7, y + 6, x + 7, y + 6); // center point
                g2d.drawLine(x + 4, y + 7, x + 4, y + 7);
                g2d.drawLine(x + 6, y + 7, x + 6, y + 7); // center point
                g2d.drawLine(x + 8, y + 7, x + 8, y + 7);
                g2d.drawLine(x + 3, y + 8, x + 3, y + 8);
                g2d.drawLine(x + 5, y + 8, x + 5, y + 8);
                g2d.drawLine(x + 7, y + 8, x + 7, y + 8);
                g2d.drawLine(x + 9, y + 8, x + 9, y + 8);
                g2d.drawLine(x + 4, y + 9, x + 4, y + 9);
                g2d.drawLine(x + 8, y + 9, x + 8, y + 9);
            } finally {
                g2d.dispose();
            }

        }
    }

    private TernaryLogicValue tlv;

    private Icon xIcon;

    /**
     * Instantiates a ButtonProvider with default properties.
     * <p>
     * 
     */
    public TernaryLogicProvider() {
        this((StringValue) null);
    }

    /**
     * @param stringValue
     *            the StringValue to use for formatting.
     */
    public TernaryLogicProvider(StringValue stringValue) {
        this(stringValue, null, JLabel.CENTER);
    }

    /**
     * @param logicValue
     *            the {@code LogicValue} to use for formatting.
     */
    public TernaryLogicProvider(TernaryLogicValue logicValue) {
        this(null, logicValue, JLabel.CENTER);
    }

    /**
     * Instantiates a {@code TernaryLogicProvider} with the given {@code StringValue},
     * {@code TernaryLogicValue}, and alignment.
     * 
     * @param stringValue
     *            the StringValue to use for formatting.
     * @param logicValue
     *            the {@code LogicValue} to use for formatting.
     * @param alignment
     *            the horizontalAlignment.
     */
    public TernaryLogicProvider(StringValue stringValue, TernaryLogicValue logicValue,
            int alignment) {
        super(stringValue == null ? StringValues.EMPTY : stringValue, alignment);
        this.tlv = logicValue == null ? TernaryLogicValue.FROM_BOOLEAN : logicValue;
        this.xIcon = new XMarkIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void configureState(CellContext context) {
        rendererComponent.setBorderPainted(true);
        rendererComponent.setHorizontalAlignment(getHorizontalAlignment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //CHECKSTYLE:OFF ignore abstract return type
    protected AbstractButton createRendererComponent() {
    //CHECKSTYLE:ON
        return new JRendererCheckBox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void format(CellContext context) {
        switch (tlv.getValue(context.getValue())) {
        case TRUE:
            rendererComponent.setIcon(null);
            rendererComponent.setSelected(true);
            break;
        case FALSE:
            rendererComponent.setIcon(null);
            rendererComponent.setSelected(false);
            break;
        case UNKNOWN:
            rendererComponent.setIcon(xIcon);
            rendererComponent.setSelected(false);
            break;
        default:
            // does nothing
        }

        rendererComponent.setText(getValueAsString(context));
    }

}