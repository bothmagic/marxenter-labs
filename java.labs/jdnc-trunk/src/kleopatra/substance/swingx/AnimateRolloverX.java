/*
 * Created on 11.11.2009
 *
 */
package swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.jdesktop.swingx.util.WindowUtils;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;
import org.pushingpixels.trident.ease.TimelineEase;

import swingx.plaf.animated.ViewRowHighlightPredicate;
import swingx.trident.Installer;
import swingx.trident.TimelineTracker;
import swingx.trident.TimelineX;

public class AnimateRolloverX {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AnimateRolloverX.class
            .getName());
//    private TimelineTracker<Integer> tracker;
    private TimelineTracker<Integer> tracker;
    
    private JComponent createContent() {
        JTabbedPane pane = new JTabbedPane();
            TableModel model = new DefaultTableModel(10, 5) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 1 ? Boolean.class : Object.class;
                }
                
            };
            JXTable table = new JXTable(model);
            // to better see the fading transitions
            table.setRowHeight(30);
            // add a fadin/out rollover highlighter
            table.addHighlighter(createRolloverHighlighter(table));
            pane.addTab("Table", new JScrollPane(table));

            Action actino = new AbstractAction("clear tracker") {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    tracker.dispose();
                }
            };
            
            JXPanel panel = new JXPanel(new BorderLayout());
            panel.add(pane);
            panel.add(new JButton(actino), BorderLayout.SOUTH);
//            ListModel listModel = new DefaultComboBoxModel(new Object[]{false, true, false, false});

//            JXList list = new JXList(listModel);
//            list.setRolloverEnabled(true);
//            list.addHighlighter(createFadeRolloverHighlighter(list));
//            pane.addTab("List", new JScrollPane(list));
            
            
            return panel;
        }

    private Highlighter createRolloverHighlighter(JComponent table) {
        final CompoundHighlighter result = new CompoundHighlighter();
        
        // quick code for having mutable local var
        final CompoundHighlighter dummy = new CompoundHighlighter(new ViewRowHighlightPredicate(-1));
        tracker = new TimelineTracker<Integer>();
        
        PropertyChangeListener l = new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (RolloverProducer.ROLLOVER_KEY.equals(evt.getPropertyName())) {
                    Point currentPoint = (Point) evt.getNewValue();
                    Point lastPoint = (Point) evt.getOldValue();
                    if (isRowChanged(lastPoint, currentPoint)) {
                        updateFade(lastPoint, currentPoint);
                    }
                }
            }
            private void updateFade(Point lastPoint, Point currentPoint) {
                int lastRow = lastPoint != null ? lastPoint.y : -1;
                if (lastRow >= 0) {
                    Timeline t = tracker.getTimeline(lastRow);
                    if (t != null) {
                        t.playReverse();
                    }
                }
                int currentRow = currentPoint != null ? currentPoint.y : -1;
                if (currentRow >= 0) {
                    ViewRowHighlightPredicate predicate = new ViewRowHighlightPredicate(currentRow);
                    TimelineX t = createTimeline(result, predicate);
                    tracker.add(t, currentRow);
                     t.play();
                }
            }
            private boolean isRowChanged(Point lastPoint, Point currentPoint) {
                if ((lastPoint == null) && (currentPoint == null)) {
                    return false;
                }
                if ((lastPoint != null) && (currentPoint != null)) {
                    return lastPoint.y != currentPoint.y;
                }
                return true;
            }
        };
        table.addPropertyChangeListener(l);
        
        return result;
    }
    
    private TimelineX createTimeline(final CompoundHighlighter result, HighlightPredicate predicate) {
            // color highlighter with predicate from dummy
            ColorHighlighter currentRowHL = new ColorHighlighter(predicate); 
            Installer installer = new Installer() {
            
            @Override
            public void release(Object target) {
                result.removeHighlighter((Highlighter) target);
            }
            
            @Override
            public void install(Object target) {
                result.addHighlighter((Highlighter) target);
                
            }
            
         };
            TimelineX fadeIn = new TimelineX(currentRowHL, installer);
            TimelineEase ease = new Spline(0.5f);
            fadeIn.setEase(ease);
            fadeIn.addPropertyToInterpolate("background", 
                    ColorUtil.setAlpha(Color.MAGENTA, 0), Color.MAGENTA);
            fadeIn.setDuration(2000);
        return fadeIn;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String nimbus = null;
//                try {
//                    LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
//                    for (LookAndFeelInfo info : infos) {
//                        if (info.getName().contains("Nimbus")) {
//                            nimbus = info.getClassName();
//                        }
//                    }
//                    LookAndFeel substance = new SubstanceCremeLookAndFeel();
//                    UIManager
//                            .setLookAndFeel(substance);
//                } catch (Exception e) {
//                    System.out
//                            .println("Substance Raven Graphite failed to initialize");
//                }
                JXFrame frame = new JXFrame("RolloverAnimation", true);
                frame.add(new AnimateRolloverX().createContent());
                frame.pack();
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }
        });
    }

}
