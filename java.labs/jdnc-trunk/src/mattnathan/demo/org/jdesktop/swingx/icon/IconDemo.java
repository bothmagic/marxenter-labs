/*
 * $Id: IconDemo.java 1255 2007-04-19 09:45:59Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.icon.compound.GridPolicy;
import org.jdesktop.swingx.icon.compound.GridPolicyTest;
import org.jdesktop.swingx.icon.compound.OverlayPolicy;
import org.jdesktop.swingx.icon.compound.OverlayPolicyTest;
import org.jdesktop.swingx.icon.compound.PilePolicy;
import org.jdesktop.swingx.icon.compound.PilePolicyTest;
import org.jdesktop.swingx.icon.compound.StackPolicy;
import org.jdesktop.swingx.icon.compound.StackPolicyTest;
import org.jdesktop.swingx.icon.range.IconRange;
import org.jdesktop.swingx.icon.range.Range;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.TextPainter;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Simple demo showing the different icons.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IconDemo extends JComponent {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("Scalable Icon Showcase", new IconDemo()).setVisible(true);
    }





    private JTabbedPane contents;
    private List<Page> pages;

    public IconDemo() {
        super();
        pages = createDefaultPages();
        updateUI();
    }





    protected List<Page> createDefaultPages() {
        List<Page> result = new ArrayList<Page>();

        result.add(createPage("Temp Icon", new TempIconTest(new TempIcon())));
        result.add(createPage("Default Icon", new DefaultScalableIconTest(
              new DefaultScalableIcon(
                    new IconRange(applyScalePolicy(new PainterIcon(32, 32, new TextPainter("32", UIManager.getFont("Panel.font").deriveFont(16f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(64, 64, new TextPainter("64", UIManager.getFont("Panel.font").deriveFont(32f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(96, 96, new TextPainter("96", UIManager.getFont("Panel.font").deriveFont(64f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(128, 128, new TextPainter("128", UIManager.getFont("Panel.font").deriveFont(96f))),
              ScalePolicy.NONE))))));
        result.add(createPage("Border Icon", new BorderIconTest(new BorderIcon(
              new DefaultScalableIcon(
                    new IconRange(applyScalePolicy(new PainterIcon(16, 16, new TextPainter("16", UIManager.getFont("Panel.font").deriveFont(8f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(32, 32, new TextPainter("32", UIManager.getFont("Panel.font").deriveFont(16f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(64, 64, new TextPainter("64", UIManager.getFont("Panel.font").deriveFont(32f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(96, 96, new TextPainter("96", UIManager.getFont("Panel.font").deriveFont(64f))),
              ScalePolicy.NONE)),
                    new IconRange(applyScalePolicy(new PainterIcon(128, 128, new TextPainter("128", UIManager.getFont("Panel.font").deriveFont(96f))),
              ScalePolicy.NONE))),
              BorderFactory.createLineBorder(Color.YELLOW, 8)))));
        result.add(createPage("Card Icon", new CardIconTest(
              new CardIcon(
                    0.4f,
                    applyScalePolicy(new PainterIcon(32, 32, new TextPainter("0", UIManager.getFont("Panel.font").deriveFont(32f))), ScalePolicy.NONE),
                    applyScalePolicy(new PainterIcon(32, 32, new TextPainter("1", UIManager.getFont("Panel.font").deriveFont(32f))), ScalePolicy.NONE)))));
        result.add(createPage("Clip Icon", new ClipIconTest(new ClipIcon(new TempIcon(64, 64), new Rectangle(24, 24, 32, 32), true))));

        Icon[] icons = {

                       new TempIcon(32, 32, null, Color.RED),
                       new TempIcon(32, 32, null, Color.BLUE),
                       new TempIcon(32, 32, null, Color.GREEN),
                       new TempIcon(32, 32, null, Color.YELLOW),
                       new TempIcon(32, 32, null, Color.MAGENTA),
                       new TempIcon(32, 32, null, Color.CYAN)
        };
        result.add(createPage("Overlay Policy", new OverlayPolicyTest(new CompoundIcon(new OverlayPolicy(),
              new TempIcon(32, 32, Color.WHITE, Color.BLACK),
              applyScalePolicy(new PainterIcon(32, 32, new TextPainter("O", UIManager.getFont("Panel.font").deriveFont(32f), Color.RED)),
                               ScalePolicy.NONE),
              new PainterIcon(32, 32, new GlossPainter())))));
        result.add(createPage("Grid Policy", new GridPolicyTest(new CompoundIcon(new GridPolicy(2, 0, 4, 4), icons))));
        result.add(createPage("Stack Policy", new StackPolicyTest(new CompoundIcon(new StackPolicy(0.85f, StackPolicy.CONTAINER_SIZE), icons))));
        result.add(createPage("Pile Policy", new PilePolicyTest(new CompoundIcon(new PilePolicy(0.97f, StackPolicy.CONTAINER_SIZE), icons))));

        return result;
    }





    private AbstractScalableIcon applyScalePolicy(AbstractScalableIcon icon, ScalePolicy policy) {
        icon.setScalePolicy(policy);
        return icon;
    }





    private Page createPage(String title, AbstractScalableIconTest component) {
        Page p = new Page();
        p.title = title;
        p.icon = component.getIcon();
        p.contents = component;
        return p;
    }





    public void updateUI() {
        removeAll();
        super.updateUI();
        LookAndFeel.installColorsAndFont(this, "Panel.background", "Panel.foregound", "Panel.font");
        contents = new JTabbedPane(JTabbedPane.LEFT);

        for (Page page : getPages()) {
            page.contents.setFont(getFont());
            page.contents.setForeground(getForeground());
            page.contents.setBackground(getBackground());
            contents.addTab(page.title, page.icon, page.contents);
        }

        setLayout(new BorderLayout());
        add(contents);
        setPreferredSize(new Dimension(800, 600));
    }





    public List<Page> getPages() {
        if (pages == null) {
            pages = new ArrayList<Page>();
        }
        return pages;
    }





    private class Page {
        public ScalableIcon icon;
        public String title;
        public JComponent contents;
    }
}
