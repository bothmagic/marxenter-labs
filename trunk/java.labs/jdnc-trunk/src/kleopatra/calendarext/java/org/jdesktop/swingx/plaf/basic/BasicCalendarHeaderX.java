/*
 * $Id: BasicCalendarHeaderHandler.java 3571 2010-01-05 15:07:17Z kleopatra $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.Localizable;
import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.swingx.calendar.CalendarStringValues.CalendarStringValue;
import org.jdesktop.swingx.calendar.CalendarStringValues.DecadeTitleStringValue;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;


/**
 * Custom implementation of a CalendarHeaderHandler in preparation of a vista-style 
 * calendar. Does nothing yet.
 * 
 * @author Jeanette Winzenburg
 */
public class BasicCalendarHeaderX extends CalendarHeader {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(BasicCalendarHeaderX.class.getName());
    
    @Override
    public void install(JXCalendar calendarView) {
        super.install(calendarView);
        getHeaderComponent().setActions(
                calendarView.getActionMap().get(PREVIOUS_PAGE_KEY),
                calendarView.getActionMap().get(NEXT_PAGE_KEY),
                calendarView.getActionMap().get(ZOOM_OUT_KEY));

    }
    
    @Override
    protected void installNavigationActions() {
        super.installNavigationActions();
        ZoomOutAction zoomOutAction = new ZoomOutAction(
                calendarView.getActionMap().get(Navigator.ZOOM_OUT_KEY));
        zoomOutAction.setTarget(calendarView);
        calendarView.getActionMap().put(ZOOM_OUT_KEY, zoomOutAction);
    }



    @Override
    public void uninstall(JXCalendar calendarView) {
        getHeaderComponent().setActions(null, null, null);
        super.uninstall(calendarView);
    }


    @Override
    public BasicCalendarHeaderPanel getHeaderComponent() {
        // TODO Auto-generated method stub
        return (BasicCalendarHeaderPanel) super.getHeaderComponent();
    }

    @Override
    protected BasicCalendarHeaderPanel createCalendarHeader() {
        return new BasicCalendarHeaderPanel();
    }

    /**
     * Quick fix for Issue #1046-swingx: header text not updated if zoomable.
     * 
     */
    protected static class ZoomOutAction extends AbstractHyperlinkAction<JXCalendar> {

        private PropertyChangeListener calendarListener;
        private Action delegatee;
        private Map<FieldType, StringValue> stringValues;
        private Locale locale;
        private PropertyChangeListener delegateeListener;

        public ZoomOutAction(Action delegatee) {
            super();
            this.delegatee = delegatee;
            delegatee.addPropertyChangeListener(getDelegateActionListener());
            
            stringValues = new HashMap<FieldType, StringValue>();
            stringValues.put(FieldType.MONTH,
                    new CalendarStringValue(locale, "MMMM yyyy"));//);
            stringValues.put(FieldType.YEAR, new CalendarStringValue(locale, "yyyy"));
            stringValues.put(FieldType.DECADE, new DecadeTitleStringValue(locale, "yyyy"));
        }
        
        private PropertyChangeListener getDelegateActionListener() {
            if (delegateeListener == null) {
                delegateeListener = new PropertyChangeListener() {
                    
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("enabled".equals(evt.getPropertyName())) {
                            setEnabled(delegatee.isEnabled());
                        }
                    }
                };
            }
            return delegateeListener;
        }

        public void actionPerformed(ActionEvent e) {
            if (delegatee != null) {
                delegatee.actionPerformed(e);
            }
        }

        
        /**
         * installs a propertyChangeListener on the target and
         * updates the visual properties from the target.
         */
        @Override
        protected void installTarget() {
            if (getTarget() != null) {
                getTarget().addPropertyChangeListener(getTargetListener());
            }
            updateLocale();
            updateFromTarget();
        }

        /**
         * 
         */
        private void updateLocale() {
            Locale current = getTarget() != null ? getTarget().getLocale() : Locale.getDefault();
            this.locale = current;
            if (stringValues == null) return;
            for (StringValue sv : stringValues.values()) {
                if (sv instanceof Localizable) {
                    ((Localizable) sv).setLocale(locale);
                } else {
                    LOG.info("sv not localizable? " + sv);
                }
            }
        }

        /**
         * removes the propertyChangeListener. <p>
         * 
         * Implementation NOTE: this does not clean-up internal state! There is
         * no need to because updateFromTarget handles both null and not-null
         * targets. Hmm...
         * 
         */
        @Override
        protected void uninstallTarget() {
            if (getTarget() == null) return;
            getTarget().removePropertyChangeListener(getTargetListener());
        }

        protected void updateFromTarget() {
            // this happens on construction with null target
            if (stringValues == null) return;
            if (getTarget() == null) {
                setName("");
                return;
            }
            Calendar calendar = getTarget().getUI().getPage();
            StringValue tsv = stringValues.get(getTarget().getUI().getPageType());
//            LOG.info("update in header: " + getTarget().getUI().getPageType());
            setName(tsv.getString(calendar));
        }

        private PropertyChangeListener getTargetListener() {
            if (calendarListener == null) {
             calendarListener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("firstDisplayedDay".equals(evt.getPropertyName())) {
                        updateFromTarget();
                    } else if ("pageType".equals(evt.getPropertyName())) {
                        updateFromTarget();
                    } else if ("locale".equals(evt.getPropertyName())) {
                        updateLocale();
                        updateFromTarget();
                    }
                }
                
            };
            }
            return calendarListener;
        }

        
    }

    
    /**
     * Active header for a JXMonthView in zoomable mode.<p>
     * 
     *  PENDING JW: very much work-in-progress.
     */
    static class BasicCalendarHeaderPanel extends JXPanel {

        protected AbstractButton prevButton;
        protected AbstractButton nextButton;
        protected JXHyperlink zoomOutLink;

        public BasicCalendarHeaderPanel() {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            prevButton = createNavigationButton();
            nextButton = createNavigationButton();
            zoomOutLink = createZoomLink();
            add(prevButton);
            add(Box.createHorizontalGlue());
            add(zoomOutLink);
            add(Box.createHorizontalGlue());
            add(nextButton);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        }

        /**
         * Sets the actions for backward, forward and zoom out navigation.
         * 
         * @param prev
         * @param next
         * @param zoomOut
         */
        public void setActions(Action prev, Action next, Action zoomOut) {
            prevButton.setAction(prev);
            nextButton.setAction(next);
            zoomOutLink.setAction(zoomOut);
        }
        
        
        /**
         * {@inheritDoc} <p>
         * 
         * Overridden to set the font of the zoom hyperlink.
         */
        @Override
        public void setFont(Font font) {
            super.setFont(font);
            if (zoomOutLink != null)
                zoomOutLink.setFont(font);
        }

        private JXHyperlink createZoomLink() {
            JXHyperlink zoomOutLink = new JXHyperlink();
            Color textColor = new Color(16, 66, 104);
            zoomOutLink.setUnclickedColor(textColor);
            zoomOutLink.setClickedColor(textColor);
            zoomOutLink.setFocusable(false);
            return zoomOutLink;
        }

        private AbstractButton createNavigationButton() {
            JXHyperlink b = new JXHyperlink();
            b.setContentAreaFilled(false);
            b.setBorder(BorderFactory.createEmptyBorder());
            b.setRolloverEnabled(true);
            b.setFocusable(false);
            return b;
        }
    }

}
    
