/*
 * Created on 11.12.2009
 *
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;

/**
 * 
 */
public class RendererConfig {

    private static RendererConfig instance = new RendererConfig();
    
    public static RendererConfig getInstance() {
        return instance;
    }
    
    public void prepareRenderingComponent(JList list, int index,
            JComponent component) {
        component.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR,
                1.0);
        if (!(list.getUI() instanceof SubstanceListUIX))
            return;
        SubstanceListUIX ui = (SubstanceListUIX) list.getUI();
        ComponentState state = ui.getCellState(index, component);
        ComponentState prevState = ui.getPrevCellState(index, component);

        SubstanceColorScheme scheme = (state == ComponentState.DEFAULT) ? ui
                .getDefaultColorScheme() : ui.getHighlightColorScheme(state);
        if (scheme == null) {
            scheme = (state == ComponentState.DEFAULT) ? SubstanceColorSchemeUtilities
                    .getColorScheme(list, state)
                    : SubstanceColorSchemeUtilities.getColorScheme(list,
                            ColorSchemeAssociationKind.HIGHLIGHT, state);
        }
        SubstanceColorScheme prevScheme = scheme;
        if (prevState != state) {
            prevScheme = (prevState == ComponentState.DEFAULT) ? ui
                    .getDefaultColorScheme() : ui
                    .getHighlightColorScheme(prevState);
            if (prevScheme == null) {
                prevScheme = (prevState == ComponentState.DEFAULT) ? SubstanceColorSchemeUtilities
                        .getColorScheme(list, prevState)
                        : SubstanceColorSchemeUtilities
                                .getColorScheme(list,
                                        ColorSchemeAssociationKind.HIGHLIGHT,
                                        prevState);
            }
        }

        // special case for drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {
            scheme = SubstanceColorSchemeUtilities.getColorScheme(list,
                    ColorSchemeAssociationKind.TEXT_HIGHLIGHT, state);
            prevScheme = scheme;
        }

        // Timeline runningTimeline = ui.getRunningTimeline(index);
        StateTransitionTracker rowTracker = ui.getStateTransitionTracker(index);
        Color color = SubstanceColorUtilities.getInterpolatedForegroundColor(
                list, scheme, state, prevScheme, prevState, rowTracker);

        // System.out.println("[row " + index + "] - " + prevState.name()
        // + "[" + prevScheme.getDisplayName() + "] -> "
        // + state.name() + "[" + scheme.getDisplayName() + "]\n\t"
        // + color);

        component.setForeground(new ColorUIResource(color));

        if (SubstanceLookAndFeel.isCurrentLookAndFeel()
                && (list.getLayoutOrientation() == JList.VERTICAL))
            SubstanceStripingUtils.applyStripedBackground(list, index,
                    component);

        Insets ins = SubstanceSizeUtils
                .getListCellRendererInsets(SubstanceSizeUtils
                        .getComponentFontSize(list));
        component.setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom,
                ins.right));

        component.setOpaque(false);

    }
}
