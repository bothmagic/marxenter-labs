/*
 * Created on 20.01.2008
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.time.calendar.Clock;
import javax.time.calendar.ISOChronology;
import javax.time.calendar.OffsetDateTime;
import javax.time.calendar.format.DateTimeFormatter;
import javax.time.calendar.format.DateTimeFormatterBuilder;
import javax.time.calendar.format.DateTimeFormatterBuilder.TextStyle;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * First draft of a header like "top" of the datepicker in windows vista.
 * Prev button | selected with link to zoom out | Next button. 
 */
public class JXZoomableSpinner extends JXPanel {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(JXZoomableSpinner.class
            .getName());
    AbstractActionExt zoomOutAction;

    ZoomableSpinnerModel zoomable;
    SpinnerModel current;
    // storing the current date ...
//    private JXMonthView monthView;
    
    
    TableFactory tableFactory;
    private ChangeListener currentListener;
    private ChangeListener zoomListener;
    private CardLayout detailsLayout;
    private JXPanel detailsPanel;
    private OffsetDateTime selectedDateTime;
    
   
    public JXZoomableSpinner() {
        
        // data
        
        selectedDateTime = Clock.systemDefaultZone().offsetDateTime();
        zoomable = new ZoomableSpinnerModel(selectedDateTime);
        zoomable.addChangeListener(getZoomListener());
        
        tableFactory = new TableFactory(selectedDateTime.toLocalDate());
        // actions
        
        zoomOutAction = createZoomAction();
        List<AbstractHyperlinkAction<OffsetDateTime>> zooms = createZoomInActions();
        
        buildContent(zooms);
        
        updateFromZoomSpinnerChanged();
    }



    private void addCard(int i, List<AbstractHyperlinkAction<OffsetDateTime>> zooms, boolean b) {
        JComponent table = tableFactory.createConfigureDetailsTable(zoomable.getSpinner(i), zooms.get(i));
        if (b) {
            table = new JScrollPane(table);
        }
        detailsPanel.add(table, zoomable.getSpinnerName(i));
    }

    /**
     * Callback for all linkActions in details tables.
     * 
     * @param target
     */
    protected void zoomIn(OffsetDateTime target) {
        selectedDateTime = target;
        tableFactory.selectedDate = selectedDateTime.toLocalDate();
        zoomable.zoomIn(target);
    }

    /**
     * Updates zoomout bar.
     */
    protected void updateZoomOut() {
        Format currentFormat = ((OffsetDateTimeSpinnerModel) current).getFormat();
        zoomOutAction.setName(currentFormat.format(current.getValue()));
    }
    
    /**
     * callback for change in zoom spinner.
     */
    protected void updateFromZoomSpinnerChanged() {
        if (current != null) {
            current.removeChangeListener(getCurrentListener());
        }
        current = zoomable.getValue();
        current.addChangeListener(getCurrentListener());
        detailsLayout.show(detailsPanel, ((OffsetDateTimeSpinnerModel) current).getName());
        updateZoomOut();
    }
    

    /**
     * Lazily create and return listener for current spinner.
     * 
     * @return
     */
    private ChangeListener getCurrentListener() {
        if (currentListener == null) {
            currentListener = new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    updateZoomOut();
                }
            };
        }
        return currentListener;
    }

    /**
     * Lazily create and return listener for ZoomSpinner.
     * 
     * @return
     */
    private ChangeListener getZoomListener() {
        if (zoomListener == null) {
            zoomListener = new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    updateFromZoomSpinnerChanged();
                }
            };
        }
        return zoomListener;
    }

    /**
     * Create and return action for next button.
     * 
     * @return
     */
    private Action createNextAction() {
        Action action = new AbstractActionExt(">") {

            public void actionPerformed(ActionEvent e) {
                zoomable.next();
            }
            
        };
        return action;
    }

    /**
     * Create and return action for next button.
     * 
     * @return
     */
    private Action createPreviousAction() {
        Action action = new AbstractActionExt("<") {

            public void actionPerformed(ActionEvent e) {
                zoomable.previous();
            }
            
        };
        return action;
    }

    /**
     * Create and return action for hyperlink in bar.
     * 
     * @return
     */
   protected AbstractActionExt createZoomAction() {
        AbstractActionExt action = new AbstractActionExt("zoomOut") {

            public void actionPerformed(ActionEvent e) {
                zoomable.zoomOut();
            }
            
        };
        return action;
    }

   
   /**
    * Create linkAction for hyperlink renderers in details tables.
    * 
    * @param format
    * @return
    */
   private AbstractHyperlinkAction<OffsetDateTime> createZoomInAction(Format format) {
       final StringValue sv = new FormatStringValue(format);
       
       AbstractHyperlinkAction<OffsetDateTime> linkAction = new AbstractHyperlinkAction<OffsetDateTime>() {

           
           @Override
           public boolean isVisited() {
               return false;
           }

           
           @Override
           protected void installTarget() {
//               LOG.info(sv.getString(getTarget()));
               setName(sv.getString(getTarget()));
           }


           public void actionPerformed(ActionEvent e) {
               zoomIn(getTarget());
           }
           
       };
       return linkAction;
   }

   // actions for hyperlink renderers in the details table
   private List<AbstractHyperlinkAction<OffsetDateTime>> createZoomInActions() {
       List<AbstractHyperlinkAction<OffsetDateTime>> zooms = new ArrayList<AbstractHyperlinkAction<OffsetDateTime>>();
       DateTimeFormatter dayFormatter = new DateTimeFormatterBuilder()
           .appendValue(ISOChronology.dayOfMonthRule())
           .toFormatter();
       zooms.add(createZoomInAction(dayFormatter.toFormat()));
       DateTimeFormatter monthFormatter = new DateTimeFormatterBuilder()
           .appendText(ISOChronology.monthOfYearRule(), TextStyle.SHORT)
           .toFormatter();
       zooms.add(createZoomInAction(monthFormatter.toFormat()));
       DateTimeFormatter yearFormatter = new DateTimeFormatterBuilder()
           .appendValue(ISOChronology.yearRule()).toFormatter();
       Format yearNameFormat = yearFormatter.toFormat();

       zooms.add(createZoomInAction(yearNameFormat));
       return zooms;
   }




    private void buildContent(List<AbstractHyperlinkAction<OffsetDateTime>> zooms) {
        // components
        
        JButton previous = createArrowButton(createPreviousAction());
        JButton next = createArrowButton(createNextAction());
        JXHyperlink zoomOutLink = new JXHyperlink(zoomOutAction);
        Color textColor = new Color(16, 66, 104);
        zoomOutLink.setUnclickedColor(textColor);
        zoomOutLink.setClickedColor(textColor);
        
       
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JXPanel bar = new JXPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.LINE_AXIS));
        bar.add(previous);
        bar.add(Box.createHorizontalGlue());
        bar.add(zoomOutLink);
        bar.add(Box.createHorizontalGlue());
        bar.add(next);
        bar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        add(bar);

        detailsLayout = new CardLayout();
        detailsPanel = new JXPanel(detailsLayout);
        fillDetailsCards(zooms);
        add(detailsPanel);
    }

    private void fillDetailsCards(List<AbstractHyperlinkAction<OffsetDateTime>> zooms) {
        for (int i = 0; i < zoomable.getDepth(); i++) {
            addCard(i, zooms, i == 0);
        }
    }



    
    private JButton createArrowButton(Action prev) {
        JButton b = new JButton(prev);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setRolloverEnabled(true);
        return b;
    }


}
