/*
 * Created on 20.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXMonthView;
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
    private JXMonthView monthView;
    
    
    TableFactory tableFactory;
    private ChangeListener currentListener;
    private ChangeListener zoomListener;
    private CardLayout detailsLayout;
    private JXPanel detailsPanel;
    
    
    public JXZoomableSpinner() {
        // Date
         monthView = new JXMonthView();
         tableFactory = new TableFactory(monthView);
        
        // data
        
        zoomable = new ZoomableSpinnerModel(monthView.getCalendar());
        zoomable.addChangeListener(getZoomListener());
        
        // actions
        
        zoomOutAction = createZoomAction();
        List<AbstractHyperlinkAction<Date>> zooms = createZoomInActions();
        
        buildContent(zooms);
        
        updateFromZoomSpinnerChanged();
    }



    private void addCard(int i, List<AbstractHyperlinkAction<Date>> zooms, boolean b) {
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
    protected void zoomIn(Date target) {
        // monthview is mis-used as a global date holder (highlighters)
        monthView.setSelectionDate(target);
        zoomable.zoomIn(target);
    }

    /**
     * Updates zoomout bar.
     */
    protected void updateZoomOut() {
        Format currentFormat = ((DateSpinner) current).getFormat();
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
        detailsLayout.show(detailsPanel, ((DateSpinner) current).getName());
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
   private AbstractHyperlinkAction<Date> createZoomInAction(String format) {
       final StringValue sv = new FormatStringValue(new SimpleDateFormat(format));
       
       AbstractHyperlinkAction<Date> linkAction = new AbstractHyperlinkAction<Date>() {

           
           @Override
           public boolean isVisited() {
               return false;
           }

           
           @Override
           protected void installTarget() {
               setName(sv.getString(getTarget()));
           }


           public void actionPerformed(ActionEvent e) {
               zoomIn(getTarget());
           }
           
       };
       return linkAction;
   }

   // actions for hyperlink renderers in the details table
   private List<AbstractHyperlinkAction<Date>> createZoomInActions() {
       List<AbstractHyperlinkAction<Date>> zooms = new ArrayList<AbstractHyperlinkAction<Date>>();
       zooms.add(createZoomInAction("d"));
       zooms.add(createZoomInAction("MMM"));
       zooms.add(createZoomInAction("yyyy"));
       return zooms;
   }




    private void buildContent(List<AbstractHyperlinkAction<Date>> zooms) {
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

    private void fillDetailsCards(List<AbstractHyperlinkAction<Date>> zooms) {
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
