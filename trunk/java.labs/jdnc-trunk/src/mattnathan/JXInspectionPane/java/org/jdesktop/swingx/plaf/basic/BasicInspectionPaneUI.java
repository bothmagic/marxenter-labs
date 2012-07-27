package org.jdesktop.swingx.plaf.basic;

import static org.jdesktop.swingx.plaf.XComponentUI.getUIProperty;
import org.jdesktop.swingx.plaf.InspectionPaneUI;
import javax.swing.plaf.*;
import javax.swing.*;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.event.DynamicObject;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.*;
import org.jdesktop.swingx.util.*;
import org.jdesktop.swingx.icon.*;
import org.jdesktop.swingx.inspection.*;
import java.awt.image.*;
import org.jdesktop.swingx.graphics.*;
import javax.swing.text.*;
import java.util.List;
import java.util.*;
import java.io.File;

import org.jdesktop.swingx.icon.range.*;
import javax.swing.event.*;
import org.jdesktop.swingx.image.ImageUtilities.*;
import org.jdesktop.swingx.plaf.*;

public class BasicInspectionPaneUI extends InspectionPaneUI {

    public static final String TITLE_FOREGROUND = "titleForeground";
    public static final String TITLE_FONT = "titleFont";
    public static final String DESCRIPTION_FOREGROUND = "descriptionForeground";
    public static final String DESCRIPTION_FONT = "descriptionFont";
    public static final String LABEL_FOREGROUND = "labelForeground";
    public static final String LABEL_FONT = "labelFont";
    public static final String LABEL_HORIZONTAL_ALIGNMENT = "labelHorizontalAlignment";
    public static final String LABEL_POSTSCRIPT = "labelPostscript";

    protected static final String ICON_COMPONENT = "icon";
    protected static final String TITLE_COMPONENT = "title";
    protected static final String SHORT_DESCRIPTION_COMPONENT = "shortDescription";
    protected static final String RENDERER_COMPONENT = "renderer";
    protected static final String LABEL_COMPONENT = "label";

    private static InspectionPaneUI SINGLETON;

    public static ComponentUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicInspectionPaneUI();
        }
        return SINGLETON;
    }





    protected float titleFontScale = 1.8f;
    private Adapter adapter = new Adapter();

    public BasicInspectionPaneUI() {
        super();
        setInstallDefaultPropertySupport(true);
    }





    /**
     * Paints the specified component appropriate for the look and feel.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted; this argument is often ignored, but might be used if the UI object is
     *   stateless and shared by multiple components
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        JXInspectionPane ip = (JXInspectionPane) c;
        LayoutPolicy layout = getLayoutPolicy(ip);
        if (layout != null) {
            CellRendererPane renderer = (CellRendererPane) getUIComponent(ip, RENDERER_COMPONENT);

            final Rectangle bounds = new Rectangle();

            Component icon = getUIProperty(ip, ICON_COMPONENT);
            Component title = getUIProperty(ip, TITLE_COMPONENT);
            Component desc = getUIProperty(ip, SHORT_DESCRIPTION_COMPONENT);

            if (icon != null) {
                layout.getIconBounds(bounds);
                renderer.paintComponent(g, icon, c, bounds);
            }

            if (title != null) {
                layout.getTitleBounds(bounds);
                renderer.paintComponent(g, title, c, bounds);
            }

            if (desc != null) {
                layout.getDescriptionBounds(bounds);
                renderer.paintComponent(g, desc, c, bounds);
            }

            if (layout.getItemCount() > 0) {
                InspectionValueRenderer valueRenderer = ip.getValueRenderer();
                Component labelComp = getUIProperty(ip, LABEL_COMPONENT);
                Details details = getDetails(ip);
                boolean mutable = details instanceof MutableDetails;
                boolean enabled = ip.isEnabled();
                for (int i = 0, n = layout.getItemCount(); i < n; i++) {
                    String label = details.getLabelAt(i);
                    Object value = details.getValueAt(i);
                    boolean editable = mutable && ((MutableDetails) details).isEditableAt(i);
                    updateLabelComponentText(ip, labelComp, label);

                    layout.getLabelBounds(i, bounds);
                    renderer.paintComponent(g, labelComp, ip, bounds);

                    Component valueComp = valueRenderer.getInspectionValueRendererComponent(ip, value, label, i, editable, enabled);
                    layout.getValueBounds(i, bounds);
                    renderer.paintComponent(g, valueComp, ip, bounds);
                }
            }
        }
    }





    @Override
    protected LayoutManager createLayoutManager(JXInspectionPane c) {
        return new Layout();
    }





    @Override
    protected void installComponents(JXInspectionPane component) {
        addUIComponent(component, new CellRendererPane(), RENDERER_COMPONENT);
    }





    protected JComponent createLabelComponent(JXInspectionPane c) {
        JLabel label = new JLabel();
        updateLabelComponent(c, label, null);
        return label;
    }





    protected JComponent createIconComponent(final JXInspectionPane c) {
        JXIconPanel result = new JXIconPanel() {
            private final Rectangle r = new Rectangle();
            private final ChangeListener cl = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    LayoutPolicy l = getLayoutPolicy(c);
                    if (l == null) {
                        c.repaint();
                    } else {
                        c.repaint(l.getIconBounds(r));
                    }
                }
            };
            @Override
            public void setIcon(Icon icon) {
                Icon old = getIcon();
                if (old instanceof DynamicObject) {
                    ((DynamicObject) old).removeChangeListener(cl);
                }
                if (icon instanceof DynamicObject) {
                    ((DynamicObject) icon).addChangeListener(cl);
                }
                super.setIcon(icon);
            }
        };
        result.setOpaque(false);
        updateIconComponent(c, result, null);
        return result;
    }





    protected JComponent createTitleComponent(JXInspectionPane c) {
        JLabel title = new JLabel();
        updateTitleComponent(c, title, null);
        return title;
    }





    protected JComponent createShortDescriptionComponent(JXInspectionPane c) {
        JLabel label = new JLabel();
        updateDescriptionComponent(c, label, null);
        return label;
    }





    protected LayoutPolicy createLayoutPolicy(JXInspectionPane c) {
        LayoutPolicy result;
        switch (c.getOrientation()) {
            case VERTICAL:
            case HORIZONTAL:
            default:
                result = new HorizontalLayout();
                break;
        }
        // configure the result.

        return result;
    }





    @Override
    protected void installDefaults(JXInspectionPane component) {
        super.installDefaults(component);
        titleFontScale = getUIProperty(component, "titleScale", 1.8f);
        addUIProperty(component, ICON_COMPONENT, createIconComponent(component));
        addUIProperty(component, TITLE_COMPONENT, createTitleComponent(component));
        addUIProperty(component, SHORT_DESCRIPTION_COMPONENT, createShortDescriptionComponent(component));
        addUIProperty(component, "layoutPolicy", createLayoutPolicy(component));
        addUIProperty(component, LABEL_COMPONENT, createLabelComponent(component));
        LookAndFeelUtilities.installProperty(component, "valueRenderer", new DefaultInspectionValueRenderer());
    }





    @Override
    protected void installListeners(JXInspectionPane component) {
        super.installListeners(component);
        component.addListDataListener(createListDataListener(component));
    }





    @Override
    protected void uninstallDefaults(JXInspectionPane component) {
        super.uninstallDefaults(component);
    }





    protected void updateLabelComponentText(JXInspectionPane c, Component labelComponent, String text) {
        String postscript = getUIProperty(c, LABEL_POSTSCRIPT, "");
        ((JLabel) labelComponent).setText(text == null ? "" : text + postscript);
    }





    protected void updateLabelComponent(JXInspectionPane c, Component labelComponent, String property) {
        boolean override = property == null;
        JLabel label = (JLabel) labelComponent;
        if (override || property == "font") {
            Font f = c.getFont();
            label.setFont(LookAndFeelUtilities.createNonUIResource(f));
        }
        if (override || property == "foreground") {
            Color fg = c.getForeground();
            label.setForeground(LookAndFeelUtilities.createNonUIResource(fg));
        }
        if (override || property == LABEL_FONT) {
            Font f = getUIProperty(c, LABEL_FONT);
            if (f != null) {
                label.setFont(f);
            }
        }
        if (override || property == LABEL_FOREGROUND) {
            Color fg = getUIProperty(c, LABEL_FOREGROUND);
            if (fg != null) {
                label.setForeground(fg);
            }
        }
        if (override || property == LABEL_HORIZONTAL_ALIGNMENT) {
            Integer a = getUIProperty(c, LABEL_HORIZONTAL_ALIGNMENT);
            if (a != null) {
                label.setHorizontalAlignment(a);
            }
        }
        c.revalidate();
        c.repaint();
    }





    protected void updateIconComponent(JXInspectionPane c, Component iconComponent, String property) {
        boolean override = property == null;
        JXIconPanel ip = (JXIconPanel) iconComponent;
        if (override ||
            property == "dataModel" ||
            property == "inspector") {
            Details details = getDetails(c);
            if (details != null) {
                Object val = details.getValue(Details.ICON);
                ip.setIcon((Icon) val);
            }
        }
        ip.setVisible(ip.getIcon() != null);
        c.revalidate();
        c.repaint();
    }





    protected void updateTitleComponent(JXInspectionPane c, Component titleComponent, String property) {
        boolean override = property == null;
        JLabel label = (JLabel) titleComponent;
        if (override || property == "font") {
            Font f = c.getFont();
            f = f.deriveFont(f.getSize2D() * titleFontScale);
            label.setFont(f);
        }
        if (override || property == "foreground") {
            Color fg = c.getForeground();
            label.setForeground(LookAndFeelUtilities.createNonUIResource(fg));
        }
        if (override || property == TITLE_FONT) {
            Font f = getUIProperty(c, TITLE_FONT);
            if (f != null) {
                label.setFont(f);
            }
        }
        if (override || property == TITLE_FOREGROUND) {
            Color fg = getUIProperty(c, TITLE_FOREGROUND);
            if (fg != null) {
                label.setForeground(fg);
            }
        }
        if (override ||
            property == "dataModel" ||
            property == "inspector") {
            Details details = getDetails(c);
            if (details != null) {
                Object val = details.getValue(Details.TITLE);
                if (val == null) {
                    label.setText("");
                } else {
                    label.setText("<html>" + val);
                }
            }
        }
        c.revalidate();
        c.repaint();
    }





    protected void updateDescriptionComponent(JXInspectionPane c, Component descriptionComponent, String property) {
        boolean override = property == null;
        JLabel label = (JLabel) descriptionComponent;
        if (override || property == "font") {
            Font f = c.getFont();
            label.setFont(LookAndFeelUtilities.createNonUIResource(f));
        }
        if (override || property == "foreground") {
            Color fg = c.getForeground();
            label.setForeground(LookAndFeelUtilities.createNonUIResource(fg));
        }
        if (override || property == DESCRIPTION_FONT) {
            Font f = getUIProperty(c, DESCRIPTION_FONT);
            if (f != null) {
                label.setFont(f);
            }
        }
        if (override || property == DESCRIPTION_FOREGROUND) {
            Color fg = getUIProperty(c, DESCRIPTION_FOREGROUND);
            if (fg != null) {
                label.setForeground(fg);
            }
        }
        if (override ||
            property == "dataModel" ||
            property == "inspector") {
            Details details = getDetails(c);
            if (details != null) {
                Object val = details.getValue(Details.SHORT_DESCRIPTION);
                if (val == null) {
                    label.setText("");
                } else {
                    label.setText("<html>" + val);
                }
            }
        }
        c.revalidate();
        c.repaint();
    }





    @Override
    protected void propertyChange(JXInspectionPane c, String property, Object oldValue, Object newValue) {
        if (property == "dataModel" ||
            property == "enabled" ||
            property == "inspector") {
            updateIconComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, ICON_COMPONENT), property);
            updateTitleComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, TITLE_COMPONENT), property);
            updateDescriptionComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, SHORT_DESCRIPTION_COMPONENT), property);
        } else if (property == LABEL_FONT ||
                   property == LABEL_FOREGROUND ||
                   property == LABEL_HORIZONTAL_ALIGNMENT) {
            updateLabelComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, LABEL_COMPONENT), property);
        }
    }





    /**
     * Gets the details from the source component. This will first use the getDetails method of JXInspectionPane then if
     * that returns null will call createDefaultDetails to create a default stand-in for the details. Use
     * isDefaultDetails to determine if the details is the default instance if rendering is different in this case.
     *
     * @param c The source component.
     * @return The details to render.
     * @see #createDefaultDetails
     * @see #isDefaultDetails
     */
    protected Details getDetails(JXInspectionPane c) {
        Details d = c.getDetails();
        if (d == null) {
            d = createDefaultDetails(c);
        }
        return d;
    }





    protected boolean isDefaultDetails(JXInspectionPane c, Details details) {
        return details instanceof DefaultDetails;
    }





    protected Details createDefaultDetails(JXInspectionPane c) {
        return new DefaultDetails(c);
    }





    protected LayoutPolicy getLayoutPolicy(JXInspectionPane c) {
        return getUIProperty(c, "layoutPolicy");
    }





    protected ListDataListener createListDataListener(JXInspectionPane c) {
        return adapter;
    }





    @UIListener
    private class Adapter implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
            updateDetails(e);
        }





        private void updateDetails(ListDataEvent e) {
            JXInspectionPane c = (JXInspectionPane) e.getSource();
            updateIconComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, ICON_COMPONENT), null);
            updateTitleComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, TITLE_COMPONENT), null);
            updateDescriptionComponent(c, XComponentUI.<Component, JXInspectionPane>getUIProperty(c, SHORT_DESCRIPTION_COMPONENT), null);
        }





        public void intervalRemoved(ListDataEvent e) {
            updateDetails(e);
        }





        public void contentsChanged(ListDataEvent e) {
            updateDetails(e);
        }

    }







    private static class DefaultDetails extends AbstractDetails {
        public DefaultDetails(JXInspectionPane c) {
            setValue(TITLE, XComponentUI.getUIProperty(c, "defaultTitleText", "Globe Icon.png"));

            DefaultScalableIcon icon = (DefaultScalableIcon) IconFactory.createScalableIcon(
                  BasicInspectionPaneUI.class.getResource("resources" + File.pathSeparatorChar + "earth.png"), 300, 326);
            icon.setScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.SHRINK));
            icon.setInterpolation(Interpolation.BILINEAR_MULTISTEP);

            setValue(ICON, XComponentUI.getUIProperty(c, "defaultIcon", icon));
            setValue(SHORT_DESCRIPTION, XComponentUI.getUIProperty(c, "defaultDescriptionText", "Portable Network Graphics Image"));
        }





        @Override
        protected List<Entry> getEntries() {
            return Arrays.asList(
                  new Entry("Dimension", new Dimension(300, 326)),
                  new Entry("Size", 162319L),
                  new Entry("Disk used", new DefaultBoundedRangeModel(78, 0, 0, 100)),
                  new Entry("Date created", new Date(1175651516081L)),
                  new Entry("Date accessed", new Date(1175813651051L)),
                  new Entry("Primary color", new Color(0xFF9900)),
                  new Entry("Read only", true));
        }
    }







    protected static class HorizontalLayout extends AbstractLayout {

        public void validate(JXInspectionPane parent) {

            Component icon = getIconComponent(parent);
            Component title = getTitleComponent(parent);
            Component shortDescription = getShortDescriptionComponent(parent);

            Insets insets = parent.getInsets();
            int w = parent.getWidth() - insets.left - insets.right;
            int h = parent.getHeight() - insets.top - insets.bottom;
            int x = 0;
            int y = 0;
            int left = insets.left;
            int top = insets.top;

            // calc item bounds
            if (icon != null && icon.isVisible()) {
                Dimension iconMax = icon.getMaximumSize();
                Dimension iconMin = icon.getMinimumSize();

                Dimension iconSize;
                Dimension iconTarget;

                if (h > iconMax.height) {
                    iconSize = iconMax;
                    iconTarget = iconMin;
                } else if (h < iconMin.height) {
                    iconSize = iconMin;
                    iconTarget = iconMax;
                } else {
                    iconMax.setSize(w, h);
                    iconSize = ScalePolicy.FIXED_RATIO.fitInto(icon.getPreferredSize(), iconMax, iconMin);
                    iconTarget = iconMax;
                }
                iconTarget.setSize(iconSize.width, h);

                Point p = getIconLocationPolicy(parent).locate(iconSize, iconTarget, null);

                iconBounds.setBounds(left + x + p.x, top + y + p.y, iconSize.width, iconSize.height);

                x += iconSize.width + getIconTextGap(parent);
            }

            int headerWidth = 0;
            // inset top a bit
            top += 2;
            h -= 2;

            // calc title bounds
            if (title != null && title.isVisible()) {
                Dimension pref = LookAndFeelUtilities.getPreferredHTMLSize(title, Math.min(w - x, getMaxHeaderSize(parent)));

                titleBounds.setBounds(left + x, top + y, pref.width, pref.height);

                y += pref.height + getTitleDescriptionGap(parent);
                headerWidth = pref.width;
            }

            // calc description bounds
            if (shortDescription != null && shortDescription.isVisible()) {
                Dimension pref = LookAndFeelUtilities.getPreferredHTMLSize(shortDescription, Math.min(w - x, getMaxHeaderSize(parent)));
                descriptionBounds.setBounds(left + x, top + y, pref.width, pref.height);

                y += pref.height + getDescriptionDetailsGap(parent);
                if (headerWidth < pref.width) {
                    headerWidth = pref.width;
                }
            }

            if (headerWidth > 0) {
                headerWidth += getHeaderDetailsGap(parent);
            }

            // inset a bit bore for the items
            top += 2;
            h -= 2;

            // calc item bounds

            Details details = getDetails(parent);
            List<BoundItem> bounds = null;
            if (details != null && details.getDetailCount() > 0) {
                int maxLabelWidth = getMaxLabelSize(parent);
                int maxValueWidth = getMaxValueSize(parent);
                int labelValueGap = getLabelValueGap(parent);
                int columnGap = getDetailsColumnGap(parent);
                int rowGap = getDetailsRowGap(parent);

                Component labelComponent = getLabelComponent(parent);
                BasicInspectionPaneUI ui = getUI(parent);
                InspectionValueRenderer renderer = parent.getValueRenderer();

                bounds = new ArrayList<BoundItem>(details.getDetailCount());

                // collect preferred sizes for labels and values
                boolean mutable = details instanceof MutableDetails;
                boolean enabled = parent.isEnabled();
                for (int i = 0, n = details.getDetailCount(); i < n; i++) {
                    String label = details.getLabelAt(i);
                    Object value = details.getValueAt(i);
                    boolean editable = mutable && ((MutableDetails) details).isEditableAt(i);

                    // get label preferred size
                    ui.updateLabelComponentText(parent, labelComponent, label);
                    Dimension labelPref = LookAndFeelUtilities.getPreferredHTMLSize(labelComponent, maxLabelWidth);

                    // get value preferred size
                    Component valueRenderer = renderer.getInspectionValueRendererComponent(parent, value, label, i, editable, enabled);
                    Dimension valuePref = LookAndFeelUtilities.getPreferredHTMLSize(valueRenderer, maxValueWidth);
                    valueRenderer.setSize(Math.min(valuePref.width, maxValueWidth), valueRenderer.getHeight());
                    valuePref = valueRenderer.getPreferredSize();

                    BoundItem item = new BoundItem();
                    item.labelBounds.setSize(Math.min(labelPref.width, maxLabelWidth), labelPref.height);
                    item.valueBounds.setSize(Math.min(valuePref.width, maxValueWidth), valuePref.height);
                    bounds.add(item);
                }

                // position the items

                // cell coords
                int currentColumn = 0;
                int rowsInColumn = 0;

                // current coordinates of the detail item
                int currentx = x + left;
                int currenty = y + top;

                int labelWidthForColumn = 0; // the max label width for this column
                int valueWidthForColumn = 0; // the max value width for this column

                int lastItem = bounds.size(); // exclusive index of the last item to paint. i.e. the first item not to paint.

                int minFisrtColumnRows = getMinFirstColumnRows(parent);

                for (int i = 0; i < lastItem; i++) {
                    BoundItem item = bounds.get(i);
                    Rectangle label = item.labelBounds;
                    Rectangle value = item.valueBounds;

                    // do we have enough height to fit this item in?
                    int maxHeight = Math.max(label.height, value.height);
                    if (currenty + maxHeight > top + h) {
                        // new column
                        if (currentColumn != 0 && rowsInColumn == 0) {
                            // this is the only value in the column and we are not the first column
                            // this should not get included in the layout
                            /** @todo decide if we should move on to the next item to try and show the most */
                            lastItem = i;
                            break; // can't show any items
                        } else {
                            // move to next column
                            if (rowsInColumn > 0 &&
                                (rowsInColumn >= minFisrtColumnRows || currentColumn != 0)) { // minimum 2 rows in first column
                                // go through column aligning widths
                                for (int j = i - rowsInColumn, num = i; j < num; j++) {
                                    bounds.get(j).labelBounds.width = labelWidthForColumn;
                                    bounds.get(j).valueBounds.width = valueWidthForColumn;
                                    bounds.get(j).valueBounds.x += labelWidthForColumn + labelValueGap;
                                }
                                // move the current coord on to the next column.
                                if (currentColumn == 0) {
                                    // check for header width
                                    currentx += Math.max(labelWidthForColumn + labelValueGap + valueWidthForColumn + columnGap, headerWidth);
                                } else {
                                    currentx += labelWidthForColumn + labelValueGap + valueWidthForColumn + columnGap;
                                }
                            } else {
                                // first column shouldn't have any rows in it.
                                assert currentColumn == 0;
                                if (rowsInColumn > 0) { // if rows will fit then reset to 0
                                    i = 0;
                                    item = bounds.get(i);
                                    label = item.labelBounds;
                                    value = item.valueBounds;
                                    maxHeight = Math.max(label.height, value.height);
                                }
                                // move past the header labels (title and description)
                                currentx += headerWidth;
                            }
                            currenty = top;
                            currentColumn++;
                            rowsInColumn = 0;
                            labelWidthForColumn = valueWidthForColumn = 0;
                        }
                    }

                    // do we have enough width to fit this item in?
                    int totalWidth = label.width + labelValueGap + value.width;
                    if (currentx + totalWidth > left + w) {
                        lastItem = i;
                    } else {
                        // update maximum widths
                        if (label.width > labelWidthForColumn) {
                            labelWidthForColumn = label.width;
                        }
                        if (value.width > valueWidthForColumn) {
                            valueWidthForColumn = value.width;
                        }
                    }

                    // set both label and value to be at the start of their row, this will be corrected when the column
                    // is full
                    item.labelBounds.setLocation(currentx, currenty);
                    item.valueBounds.setLocation(currentx, currenty);

                    // move to next row
                    currenty += maxHeight + rowGap;
                    rowsInColumn++;

                    // if here then we are at the last item and will exit the loop next time around.
                    // clean up the column and align the labels and values
                    if (i >= lastItem - 1) {
                        // last item so clean up
                        for (int j = i + 1 - rowsInColumn, num = lastItem; j < num; j++) {
                            bounds.get(j).labelBounds.width = labelWidthForColumn;
                            bounds.get(j).valueBounds.width = valueWidthForColumn;
                            bounds.get(j).valueBounds.x += labelWidthForColumn + labelValueGap;
                        }
                    }
                }

                // trim the items to include only the items the fit
                bounds = bounds.subList(0, lastItem);

            }
            if (bounds == null || bounds.isEmpty()) {
                itemBounds = null;
            } else {
                itemBounds = bounds.toArray(new BoundItem[bounds.size()]);
            }

        }





        public Dimension preferredSize(JXInspectionPane parent) {
            return new Dimension(800, 80);
        }
    }







    protected static interface LayoutPolicy {
        public Rectangle getTitleBounds(Rectangle result);





        public Rectangle getDescriptionBounds(Rectangle result);





        public Rectangle getIconBounds(Rectangle result);





        public int getItemCount();





        public Rectangle getLabelBounds(int index, Rectangle result);





        public Rectangle getValueBounds(int index, Rectangle result);





        public Rectangle getEditorBounds(int index, Rectangle result);





        public void validate(JXInspectionPane parent);





        public Dimension preferredSize(JXInspectionPane parent);
    }







    protected abstract static class AbstractLayout implements LayoutPolicy {
        protected BoundItem[] itemBounds;
        protected final Rectangle iconBounds = new Rectangle();
        protected final Rectangle titleBounds = new Rectangle();
        protected final Rectangle descriptionBounds = new Rectangle();

        public Rectangle getTitleBounds(Rectangle result) {
            result.setBounds(titleBounds);
            return result;
        }





        public Rectangle getDescriptionBounds(Rectangle result) {
            result.setBounds(descriptionBounds);
            return result;
        }





        public Rectangle getIconBounds(Rectangle result) {
            result.setBounds(iconBounds);
            return result;
        }





        protected BasicInspectionPaneUI getUI(JXInspectionPane parent) {
            return (BasicInspectionPaneUI) parent.getUI();
        }





        protected Details getDetails(JXInspectionPane parent) {
            return ((BasicInspectionPaneUI) parent.getUI()).getDetails(parent);
        }





        protected Component getLabelComponent(JXInspectionPane parent) {
            return getUIProperty(parent, LABEL_COMPONENT);
        }





        protected Component getTitleComponent(JXInspectionPane parent) {
            return getUIComponent(parent, TITLE_COMPONENT);
        }





        protected Component getIconComponent(JXInspectionPane parent) {
            return getUIComponent(parent, ICON_COMPONENT);
        }





        protected Component getShortDescriptionComponent(JXInspectionPane parent) {
            return getUIComponent(parent, SHORT_DESCRIPTION_COMPONENT);
        }





        private Component getUIComponent(JXInspectionPane parent, String component) {
            return XComponentUI.getUIProperty(parent, component);
        }





        private <T> T getUIProperty(JXInspectionPane parent, String property) {
            return XComponentUI.<T, JXInspectionPane> getUIProperty(parent, property);
        }





        private <T> T getUIProperty(JXInspectionPane parent, String property, T backup) {
            return XComponentUI.<T, JXInspectionPane> getUIProperty(parent, property, backup);
        }





        protected LocationPolicy getIconLocationPolicy(JXInspectionPane ip) {
            return getUIProperty(ip, "iconLocationPolicy", LocationPolicy.valueOf(LocationPolicy.NORTH));
        }





        protected int getMinFirstColumnRows(JXInspectionPane parent) {
            return getUIProperty(parent, "minFirstColumnRows", 2);
        }





        protected int getIconTextGap(JXInspectionPane parent) {
            return getUIProperty(parent, "iconTextGap", 8);
        }





        protected int getTitleDescriptionGap(JXInspectionPane parent) {
            return getUIProperty(parent, "titleDescriptionGap", 4);
        }





        protected int getHeaderDetailsGap(JXInspectionPane parent) {
            return getUIProperty(parent, "headerDetailsGap", 12);
        }





        protected int getDetailsRowGap(JXInspectionPane parent) {
            return getUIProperty(parent, "detailsRowGap", 2);
        }





        protected int getDetailsColumnGap(JXInspectionPane parent) {
            return getUIProperty(parent, "detailsColumnGap", 6);
        }





        protected int getDescriptionDetailsGap(JXInspectionPane parent) {
            return getUIProperty(parent, "descriptionDetailsGap", 4);
        }





        protected int getLabelValueGap(JXInspectionPane parent) {
            return getUIProperty(parent, "labelValueGap", 2);
        }





        protected int getMaxHeaderSize(JXInspectionPane parent) {
            return getUIProperty(parent, "maxHeaderSize", 200);
        }





        protected int getMaxLabelSize(JXInspectionPane parent) {
            return getUIProperty(parent, "maxLabelSize", 200);
        }





        protected int getMaxValueSize(JXInspectionPane parent) {
            return getUIProperty(parent, "maxValueSize", 200);
        }





        public int getItemCount() {
            return itemBounds == null ? 0 : itemBounds.length;
        }





        public Rectangle getLabelBounds(int index, Rectangle result) {
            if (itemBounds == null) {
                throw new IndexOutOfBoundsException(index + ":0");
            }
            result.setBounds(itemBounds[index].labelBounds);
            return result;
        }





        public Rectangle getValueBounds(int index, Rectangle result) {
            if (itemBounds == null) {
                throw new IndexOutOfBoundsException(index + ":0");
            }
            result.setBounds(itemBounds[index].valueBounds);
            return result;
        }





        public Rectangle getEditorBounds(int index, Rectangle result) {
            if (itemBounds == null) {
                throw new IndexOutOfBoundsException(index + ":0");
            }
            result.setBounds(itemBounds[index].editorBounds);
            return result;
        }





        protected static class BoundItem {
            public final Rectangle labelBounds;
            public final Rectangle valueBounds;
            public final Rectangle editorBounds;

            public BoundItem() {
                this(new Rectangle(), new Rectangle(), new Rectangle());
            }





            public BoundItem(Rectangle labelBounds, Rectangle valueBounds, Rectangle editorBounds) {
                this.labelBounds = labelBounds;
                this.valueBounds = valueBounds;
                this.editorBounds = editorBounds;
            }
        }
    }







    private static class Layout implements LayoutManager, UIResource {

        private BasicInspectionPaneUI getUI(Component parent) {
            return (BasicInspectionPaneUI) ((JXInspectionPane) parent).getUI();
        }





        public void addLayoutComponent(String name, Component comp) {
        }





        public void removeLayoutComponent(Component comp) {
        }





        public Dimension preferredLayoutSize(Container parent) {
            return getUI(parent).getLayoutPolicy((JXInspectionPane) parent).preferredSize((JXInspectionPane) parent);
        }





        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }





        public void layoutContainer(Container parent) {
            getUI(parent).getLayoutPolicy((JXInspectionPane) parent).validate((JXInspectionPane) parent);
        }

    }
}
