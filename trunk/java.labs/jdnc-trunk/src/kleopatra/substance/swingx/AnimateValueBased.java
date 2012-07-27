/*
 * Created on 19.11.2009
 *
 */
package swingx;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.renderer.RelativePainterHighlighter;
import org.jdesktop.swingx.renderer.RelativePainterHighlighter.NumberRelativizer;
import org.jdesktop.swingx.renderer.RelativePainterHighlighter.Relativizer;
import org.jdesktop.swingx.sort.DefaultSortController;
import org.pushingpixels.trident.Timeline;

public class AnimateValueBased extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AnimateValueBased.class
            .getName());
    
    
    public void interactiveValueBasedTable() {
        JXTable table = new JXTable(createRandomNumbers(200, 1));
        RelativePainterHighlighter hl = new RelativePainterHighlighter(
                new MattePainter(ColorUtil.setAlpha(Color.MAGENTA, 125)));
        hl.setRelativizer(new NumberRelativizer(1, 200, 200));
        table.getColumnExt(1).addHighlighter(hl);
        JXFrame frame = wrapWithScrollingInFrame(table, "animate on table");
        final Timeline timeline = new Timeline(new Controller(hl, 1, 200));
        timeline.addPropertyToInterpolate("currentValue", 0, 200);
        timeline.setDuration(1000);
        Action play = new AbstractAction("play") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                timeline.replay();
            }
        };
//        addAction(frame, play);
        show(frame);
    }
    
    public void interactiveValueBasedList() {
        ListModel model = createRandomNumbers(500);
        DefaultComboBoxModel first = new DefaultComboBoxModel(new Object[] {model.getElementAt(0)});
        final JXList list = new JXList(model, true);
        list.setComparator(DefaultSortController.COMPARABLE_COMPARATOR);
        RelativePainterHighlighter hl = new RelativePainterHighlighter(
                new MattePainter(ColorUtil.setAlpha(Color.MAGENTA, 125)));
        list.addHighlighter(hl);
        JXFrame frame = wrapWithScrollingInFrame(list, "bounded Highlighter");
        
        final Timeline timeline = new Timeline(new Controller(hl, 0, 600));
        timeline.addPropertyToInterpolate("currentValue", 0, 500);
        timeline.setDuration(1000);
        Action play = new AbstractAction("play") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                timeline.replay();
            }
        };
        addAction(frame, play);
        
        Action sort = new AbstractAction("cycle sort") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                list.toggleSortOrder();
            }
        };
        addAction(frame, sort);
        show(frame, 400, 400);
    }


    // quick hack: don't yet know how to add non-properties?
    // PropertyInterpolator?
    public static class Controller {
        private RelativePainterHighlighter highlighter;
        protected int max;
        private int modelColumn;
        
        public Controller(RelativePainterHighlighter highlighter, int modelColumn, int max) {
            this.highlighter = highlighter;
            this.max = max;
        }
        
        public void setCurrentValue(int currentValue) {
            highlighter.setRelativizer(createRelativizer(currentValue));
        }

        /**
         * @param currentValue
         * @return
         */
        protected Relativizer createRelativizer(int currentValue) {
             return new NumberRelativizer(modelColumn, max, currentValue);
        }
    }
    
    private TableModel createRandomNumbers(int max, final int valueColumn) {
        final ListModel lm = createRandomNumbers(max);
        DefaultTableModel model = new DefaultTableModel(max, valueColumn + 3) {

            /* (non-Javadoc)
             * @see javax.swing.table.DefaultTableModel#getColumnCount()
             */
            @Override
            public Object getValueAt(int row, int column) {
                if (column == valueColumn) {
                    return lm.getElementAt(row);
                }
                return null;
            }

            /* (non-Javadoc)
             * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
             */
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == valueColumn) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }

            /* (non-Javadoc)
             * @see javax.swing.table.DefaultTableModel#getColumnName(int)
             */
            @Override
            public String getColumnName(int column) {
                return getColumnClass(column).getSimpleName();
            }
            
            
        };
        return model;
    }
    
    private ListModel createRandomNumbers(int max) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < max; i++) {
            model.addElement((int) (Math.random() * max));
        }
        return model;
    }
    
    public static void main(String[] args) {
        AnimateValueBased test = new AnimateValueBased();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
