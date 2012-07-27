/*
 * $Id: JXInspectionPaneDemo.java 2813 2008-10-16 12:51:28Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.icon.BorderIcon;
import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.DefaultScalableIcon;
import org.jdesktop.swingx.image.ImageUtilities;
import org.jdesktop.swingx.inspection.AbstractDetails;
import org.jdesktop.swingx.inspection.DetailInspector;
import org.jdesktop.swingx.inspection.Details;
import org.jdesktop.swingx.painter.ItemSelectionPainter;
import org.jdesktop.swingx.rollover.DefaultRolloverListCellRenderer;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Simple demo showing off the use of a JXInspecitonPane.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXInspectionPaneDemo extends JComponent {

    private static Item[] items;

    public static void main(final String[] args) {
        items = createItems();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("JXInspectionPane Demo", new JXInspectionPaneDemo()).setVisible(true, 800, 600);
    }





    private static Item[] createItems() {
        Random r = new Random(0);
        return new Item[] {
              createImageItem("caption.png", r.nextFloat()),
              createImageItem("clock.png", r.nextFloat()),
              createImageItem("search-image.png", r.nextFloat()),
              createImageItem("color.png", r.nextFloat()),
              createImageItem("download.png", r.nextFloat()),
              createImageItem("crop.png", r.nextFloat()),
              createImageItem("mcrop.png", r.nextFloat()),
              createImageItem("person-group.png", r.nextFloat())
        };
    }





    private static Item createImageItem(String img, float filespace) {
        Item item = new Item();
        item.name = img;
        item.description = "Portable Network Graphics Image";
        item.diskSpaceUsed = filespace;

        URL url = JXInspectionPaneDemo.class.getResource("resources/" + img);
        try {
            DefaultScalableIcon icon = (DefaultScalableIcon) IconFactory.createScalableIcon(ImageIO.read(url));
            icon.setScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.SHRINK));
            icon.setInterpolation(ImageUtilities.Interpolation.BILINEAR_MULTISTEP);
            item.dimensions = new Dimension(icon.getIconWidth(), icon.getIconHeight());
            item.icon = icon;
        } catch (IOException ex) {
            // do nothing
        }

        try {
            File file = new File(url.toURI());
            if (file.exists()) {
                item.dateAccessed = new Date(file.lastModified());
                item.visible = !file.isHidden();
                item.canWrite = file.canWrite();
            }
        } catch (URISyntaxException ex) {
            throw new InternalError();
        } catch (IllegalArgumentException ex) {
            item.dateAccessed = new Date();
            item.canWrite = filespace > 0.5f;
            item.visible = true;
            item.dateAccessed = new Date(System.currentTimeMillis() - (long) (1000000000D * (double) filespace));
        }

        return item;
    }





    public JXInspectionPaneDemo() {
        super();
        updateUI();

    }





    @Override
    public void updateUI() {
        removeAll();
        super.updateUI();

        JList list = new JList(items);
        list.setVisibleRowCount(0);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        if (UIManager.getLookAndFeel().getName().indexOf("Windows") >= 0) {
            RolloverManager.install(list, true);
            list.setCellRenderer(new WindowsItemRenderer());
            list.putClientProperty(DefaultRolloverListCellRenderer.PAINT_FOCUS_PROPERTY, Boolean.FALSE);
        } else {
            list.setCellRenderer(new ItemRenderer());
        }
        ListModel selectionModel = new SelectionListModel(list);

        JXInspectionPane ip = new JXInspectionPane(selectionModel);
        ip.setInspector(new ItemInspector());
        ip.setMinimumSize(new Dimension());

        setLayout(new BorderLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(list), ip);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setDividerLocation(450);
        sp.setResizeWeight(1);
        add(sp, BorderLayout.CENTER);

    }





    private static class Item {
        private Icon icon;
        private String name;
        private String description;
        private String author = "Matt Nathan";
        private boolean canWrite;
        private boolean visible;
        private Date dateCreated;
        private Date dateAccessed;
        private Dimension dimensions;
        private float diskSpaceUsed;
        public Item() {}





        public Item(Icon icon, String name, String description, boolean readOnly, boolean visible, Date dateCreated, Date dateAccessed,
                    Dimension dimensions, float diskSpaceUsed) {
            this.icon = icon;
            this.name = name;
            this.description = description;
            this.canWrite = readOnly;
            this.visible = visible;
            this.dateCreated = dateCreated;
            this.dateAccessed = dateAccessed;
            this.dimensions = dimensions;
            this.diskSpaceUsed = diskSpaceUsed;
        }
    }







    private class ItemInspector implements DetailInspector {
        public Details inspect(Object ...items) {
            Item[] is = new Item[items.length];
            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(items, 0, is, 0, items.length);
            return new ItemDetails(is);
        }





        private class ItemDetails extends AbstractDetails {

            private java.util.List<Entry> entries;

            public ItemDetails(Item ...items) {
                if (items.length == 0) {
                    setValue(Details.TITLE, JXInspectionPaneDemo.items.length + " Items");
                    setValue(Details.SHORT_DESCRIPTION, "Select an item to see its details");
                } else if (items.length > 1) {
                    setValue(Details.TITLE, items.length + " Items selected");
                    setValue(Details.SHORT_DESCRIPTION, "Multiple items selected");

                    Icon[] icons = new Icon[items.length];
                    for (int i = 0, n = items.length; i < n; i++) {
                        icons[i] = items[i].icon;
                    }
                    CompoundIcon icon = IconFactory.createCompoundStackIcon(SwingConstants.NORTH_WEST, icons);
                    icon.setChildScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.SHRINK));
                    setValue(ICON, icon);

                } else {
                    assert items.length == 1;
                    Item item = items[0];
                    setValue(Details.TITLE, item.name);
                    setValue(Details.SHORT_DESCRIPTION, item.description);
                    setValue(Details.ICON, item.icon);

                    entries = new ArrayList<Entry>();
                    entries.add(new Entry("Dimensions", item.dimensions));
                    entries.add(new Entry("Author", item.author));
                    entries.add(new Entry("Visible", item.visible));
                    entries.add(new Entry("Can Write", item.canWrite));
                    entries.add(new Entry("Disk used", new DefaultBoundedRangeModel((int) (item.diskSpaceUsed * 100), 0, 0, 100)));
                    entries.add(new Entry("Last modified", item.dateAccessed));
                    entries.add(new Entry("Keywords", "<html>" + item.name.substring(0, item.name.length() - 4) + ", White, Image, Stock"));

                }
            }





            @Override
            protected java.util.List<Entry> getEntries() {
                return entries;
            }

        }
    }







    private class SelectionListModel extends AbstractListModel implements ListSelectionListener {

        private Object[] items = null;
        private final JList list;

        public SelectionListModel(JList list) {
            this.list = list;
            list.getSelectionModel().addListSelectionListener(this);
            validate();
        }





        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                if (getSize() > 0) {
                    items = null;
                    fireIntervalRemoved(this, 0, getSize() - 1);
                }
                validate();
            }
        }





        private void validate() {
            items = list.getSelectedValues();
            if (getSize() > 0) {
                fireIntervalAdded(this, 0, getSize() - 1);
            }
        }





        public int getSize() {
            return items == null ? 0 : items.length;
        }





        public Object getElementAt(int index) {
            if (index < 0 || index >= getSize()) {
                throw new IndexOutOfBoundsException(index + ":" + getSize());
            }
            return items[index];
        }

    }







    private class WindowsItemRenderer extends DefaultRolloverListCellRenderer {
        private BorderIcon icon = new BorderIcon() {
            @Override
            public int getIconWidth() {
                return 75;
            }





            @Override
            public int getIconHeight() {
                return 75;
            }
        };
        public WindowsItemRenderer() {
            super(null,
                  ItemSelectionPainter.VISTA_SELECTION,
                  ItemSelectionPainter.VISTA_SELECTION_NOFOCUS,
                  ItemSelectionPainter.VISTA_PRESSED,
                  ItemSelectionPainter.VISTA_ROLLOVER,
                  ItemSelectionPainter.VISTA_SELECTED_ROLLOVER);
            setIcon(icon);
            setVerticalTextPosition(BOTTOM);
            setHorizontalTextPosition(CENTER);
            setHorizontalAlignment(CENTER);
            icon.setBorder(BorderFactory.createCompoundBorder(new DropShadowBorder(false),
                  BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                  BorderFactory.createLineBorder(Color.WHITE, 3))));
        }





        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel r = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Item i = (Item) value;
            setIcon(icon);
            icon.setIcon(i.icon);
            setText(i.name);
            setForeground(list.getForeground());
            return r;
        }

    }







    private class ItemRenderer extends DefaultListCellRenderer {
        private BorderIcon icon = new BorderIcon() {
            @Override
            public int getIconWidth() {
                return 75;
            }





            @Override
            public int getIconHeight() {
                return 75;
            }
        };
        public ItemRenderer() {
            super();
            setIcon(icon);
            setVerticalTextPosition(BOTTOM);
            setHorizontalTextPosition(CENTER);
            setHorizontalAlignment(CENTER);
            icon.setBorder(BorderFactory.createCompoundBorder(new DropShadowBorder(false),
                  BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                  BorderFactory.createLineBorder(Color.WHITE, 3))));
        }





        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel r = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Item i = (Item) value;
            setIcon(icon);
            icon.setIcon(i.icon);
            setText(i.name);
            return r;
        }

    }

}
