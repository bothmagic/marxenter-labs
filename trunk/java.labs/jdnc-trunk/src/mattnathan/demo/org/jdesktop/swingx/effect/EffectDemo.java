/*
 * $Id: EffectDemo.java 2974 2009-01-15 17:43:35Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import javax.swing.*;
import java.util.*;

import org.jdesktop.swingx.icon.ScalableIcon;
import java.awt.BorderLayout;
import java.awt.Dimension;
import org.jdesktop.swingx.icon.*;
import org.jdesktop.swingx.*;

public class EffectDemo extends JComponent {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("Effect Demo", new EffectDemo()).setVisible(true);
    }





    private List<Page> pages;

    public EffectDemo() {
        super();
        pages = createDefaultPages();
        updateUI();
    }





    protected List<Page> createDefaultPages() {
        List<Page> result = new ArrayList<Page>();

        result.add(createPage("Identity", new IdentityGraphicsEffectDemo()));
        result.add(createPage("Translucent", new TranslucentGraphicsEffectDemo()));
        result.add(createPage("Translation", new RelativeTranslationEffectDemo()));
        result.add(createPage("Rotation", new RotationTransformEffectDemo()));
        result.add(createPage("Shear", new ShearTransformEffectDemo()));
        result.add(createPage("Blur", new BlurGraphicsEffectDemo()));
        result.add(createPage("Mask", new MaskGraphicsEffectDemo()));
        result.add(createPage("Color", new ColorFilterEffectDemo()));
        result.add(createPage("Parallel", new ParallelGraphicsEffectDemo()));
        result.add(createPage("Serial", new SerialGraphicsEffectDemo()));
        result.add(createPage("Reflection", new ReflectionEffectDemo()));
        result.add(createPage("Shadow", new ShadowEffectDemo()));

        return result;
    }





    public List<Page> getPages() {
        if (pages == null) {
            pages = new ArrayList<Page>();
        }
        return pages;
    }





    @Override
    public void updateUI() {
        removeAll();
        super.updateUI();

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);

        for (Page page : getPages()) {
            tabs.addTab(page.title, page.icon, page.contents);
        }

        setLayout(new BorderLayout());
        add(tabs);
        setPreferredSize(new Dimension(800, 600));
    }





    private Page createPage(String title, AbstractGraphicsEffectDemo demo) {
        Page page = new Page();
        page.title = title;
        page.icon = new EffectIcon(new TempIcon(24, 24), demo.getSource());
        page.contents = demo;
        return page;
    }





    private class Page {
        public ScalableIcon icon;
        public String title;
        public JComponent contents;
    }

}
