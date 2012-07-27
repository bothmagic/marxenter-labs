/*
 * $Id: AbstractGraphicsEffectDemo.java 2750 2008-10-08 15:13:18Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXEffectPanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.TestComponent;

/**
 * Provides an abstract framework for creating demos for the GraphicsEffect implementaions.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractGraphicsEffectDemo<T extends GraphicsEffect<Object>> extends JXPanel {
    private T source;

    private JComponent controlsContainer;
    private JComponent previewContainer;
    protected JComponent controls;
    protected JComponent preview;

    private boolean previewVisible;
    private boolean controlsVisible = true;

    public AbstractGraphicsEffectDemo() {
        this(null);
    }





    public AbstractGraphicsEffectDemo(T source) {
        this(source, true);
    }





    public AbstractGraphicsEffectDemo(T source, boolean previewVisible) {

        init();
        setPreviewVisible(previewVisible);
        if (source != null) {
            setSource(source);
        }
    }





    protected void init() {
        setLayout(new BorderLayout());
        setOpaque(false);
        controlsContainer = new JPanel(new BorderLayout());
        previewContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        previewContainer.setOpaque(false);

        controls = createControls();
        if (controls != null) {
            controlsContainer.add(controls);
        }

        add(controlsContainer, BorderLayout.SOUTH);
        add(previewContainer, BorderLayout.CENTER);
    }





    protected void initPreview() {
        preview = createPreview();
        if (preview == null) {
            throw new NullPointerException("preview returned from createPreview cannot be null");
        }

        assert previewContainer != null:"preview container was null";
        JXEffectPanel effectContainer = new JXEffectPanel();
        effectContainer.setOpaque(false);
        previewContainer.add(effectContainer);
        effectContainer.add(preview);

        updateEffectContainer();
    }





    private void updateEffectContainer() {
        if (previewContainer != null) {
            if (previewContainer.getComponentCount() == 1) {
                assert previewContainer.getComponent(0) instanceof JXEffectPanel;

                JXEffectPanel fxPanel = (JXEffectPanel) previewContainer.getComponent(0);
                fxPanel.setEffect(getSource());
            }
        }
    }





    protected abstract T createDefaultSource();





    protected abstract JComponent createControls();





    protected JComponent createPreview() {
        return new TestComponent();
    }





    public T getSource() {
        if (source == null) {
            source = createDefaultSource();
        }
        return source;
    }





    public void setSource(T source) {
        T old = getSource();
        this.source = source;
        updateEffectContainer();
        firePropertyChange("source", old, getSource());
    }





    public boolean isPreviewVisible() {
        return previewVisible;
    }





    public void setPreviewVisible(boolean previewVisible) {
        boolean old = isPreviewVisible();
        if (old != previewVisible) {
            this.previewVisible = previewVisible;
            if (previewVisible && preview == null) {
                initPreview();
            }

            if (previewContainer != null) {
                previewContainer.setVisible(isPreviewVisible());
            }
            firePropertyChange("previewVisible", old, isPreviewVisible());
        }
    }





    public boolean isControlsVisible() {
        return controlsVisible;
    }





    public void setControlsVisible(boolean controlsVisible) {
        boolean old = isControlsVisible();
        if (old != controlsVisible) {
            this.controlsVisible = controlsVisible;
            if (controlsContainer != null) {
                // can be null if the initialisation code has been overriden
                controlsContainer.setVisible(isControlsVisible());
            }
            firePropertyChange("controlsVisible", old, isControlsVisible());
        }
    }
}
