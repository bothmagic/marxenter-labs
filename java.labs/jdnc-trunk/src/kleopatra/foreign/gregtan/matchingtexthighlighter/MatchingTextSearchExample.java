/*
 * Created on 22.10.2008
 *
 */
package gregtan.matchingtexthighlighter;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.Timer;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.PainterAware;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.treetable.FileSystemModel;


/**
 * Experimenting with sub-cell text matching highlighter. 
 * 
 * TODO JW: add api for
 * pluggable highlighter to AbstractSearchable (or subclasses?)
 * 
 * TODO JW: support incremental search in list, tree searchable
 * 
 * TODO JW: make MatchingTextHiglighter cope with AbstractButton.
 * 
 * PENDING JW: multiple matches per-cell treated as a single find. Reason is
 * both in TableSearchable (doesn't support next inside cell but always
 * navigates to next cell) and in MatchingTextHighlighter. Probably can live
 * with it.
 */
public class MatchingTextSearchExample extends InteractiveTestCase {
    private static final Logger LOG = Logger
            .getLogger(MatchingTextSearchExample.class.getName());
    public static void main(String args[]) {
        setSystemLF(true);
        MatchingTextSearchExample test = new MatchingTextSearchExample();
        try {
//            test.runInteractiveTests();
//            test.runInteractiveTests("inter.*Animated.*");
            test.runInteractiveTests("inter.*Subcell.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }

    }
    
    public void interactiveAnimatedPinStripe() {
        final AlphaPainter alpha = createAnimatedPainer();
        HighlightPredicate predicate = new SearchPredicate("en", -1);
        Highlighter highlighter = new PainterHighlighter(predicate, alpha);
        JXTable table = new JXTable (new AncientSwingTeam());
        table.addHighlighter(highlighter);
        showWithScrollingInFrame(table, "animated ...");
    }
    public void interactiveSubcellSearchMarkerTable() {
        JXTable table = new JXTable(new AncientSwingTeam());
        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(createMatchingTextHighlighter());
        JXFrame frame = wrapWithScrollingInFrame(table, "SubcellTextMatch");
        addComponentOrientationToggle(frame);
        show(frame);
    }
    

    /**
     * @return
     */
    private AlphaPainter createAnimatedPainer() {
        final AlphaPainter alpha = new AlphaPainter();
        alpha.setAlpha(1f);
        final PinstripePainter pinstripePainter = new PinstripePainter(Color.MAGENTA,45,3,3);
        alpha.setPainters(new MattePainter(Color.YELLOW), pinstripePainter);
        ActionListener l = new ActionListener() {
            boolean add;
            @Override
            public void actionPerformed(ActionEvent e) {
                float a = add ? (alpha.getAlpha() + 0.1f) : (alpha.getAlpha() - 0.1f);
                if (a > 1.0) {
                    a = 1f;
                    add = false;
                } else if (a < 0) {
                    a = 0;
                    add = true;
                }
                alpha.setAlpha(a);
                pinstripePainter.setAngle(pinstripePainter.getAngle()+10);
            }
            
        };
        new Timer(100, l).start();
        return alpha;
    }
    

    /**
     * @return
     */
    private AbstractHighlighter createMatchingTextHighlighter() {
        Painter painter = new MattePainter(Color.YELLOW);
        AlphaPainter alpha = new AlphaPainter();
        alpha.setAlpha(1f);
        alpha.setPainters(new MattePainter(Color.RED), new PinstripePainter(new Color(255,255,255,125),45,2,2));

        AbstractHighlighter hl = new XMatchingTextHighlighter(createAnimatedPainer());
        return hl;
    }
    

    public void interactiveSubcellSearchMarkerList() {
        JXList table = new JXList(AncientSwingTeam.createNamedColorListModel());
        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(createMatchingTextHighlighter());
        JXFrame frame = wrapWithScrollingInFrame(table, "SubcellTextMatch");
        show(frame);
    }

    
    public void interactiveSubcellSearchMarkerTree() {
        JXTree table = new JXTree(new FileSystemModel());
        table.setCellRenderer(new DefaultTreeRenderer(StringValues.FILE_NAME));
        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(createMatchingTextHighlighter());
        JXFrame frame = wrapWithScrollingInFrame(table, "SubcellTextMatch");
        show(frame);
    }

    public void interactiveSubcellSearchMarkerTreeTable() {
        JXTreeTable table = new JXTreeTable(new FileSystemModel());
        table.setTreeCellRenderer(new DefaultTreeRenderer(StringValues.FILE_NAME));
//        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(createMatchingTextHighlighter());
        JXFrame frame = wrapWithScrollingInFrame(table, "SubcellTextMatch");
        show(frame);
    }

    /**
     * MatchingHighlighter which marks clipped match as well. This is a hack,
     * should be supported in super.
     */
    public static class XMatchingTextHighlighter extends MatchingTextHighlighter {
        // copy of private super field
        Rectangle myTextR;
        private PropertyChangeListener painterListener;
        
        public XMatchingTextHighlighter(Painter<JLabel> painter) {
            super(painter);
        }

        
        @Override
        protected List<Rectangle> createHighlightAreas(String text,
                FontMetrics fm, int offset, int height) {
            List<Rectangle> areas = super.createHighlightAreas(text, fm, offset, height);
            if (areas.isEmpty()) {
                // happens if the match is under the ellipses
                // highlight ellipses
                areas = new ArrayList<Rectangle>();
                // here we rely on the given text to not contain the ellipses
                // PENDING JW: should be supported in super
                int end = myTextR.x + fm.stringWidth(text) + offset;
                areas.add(new Rectangle(end, 0, myTextR.width - end, myTextR.height));
                LOG.info("myTextR" + myTextR);
            }
            return areas;
        }

        /**
         * Overridden to copy super's private field.
         */
        @Override
        protected int calculateXOffset(JLabel component, Rectangle viewR,
                Rectangle textR) {
            myTextR = textR;
            return super.calculateXOffset(component, viewR, textR);
        }


        @Override
        protected boolean canHighlight(Component component,
                ComponentAdapter adapter) {
            return (component instanceof JLabel || component instanceof WrappingIconPanel)
                    && component instanceof PainterAware
                    && getPainter() != null
                    && getHighlightPredicate() instanceof SearchPredicate;
//            return super.canHighlight(component, adapter);
        }


        @Override
        /**
         * Sets the Painter to use in this Highlighter, may be null.
         * Un/installs the listener to changes painter's properties.
         * 
         * @param painter the Painter to uses for decoration.
         */
        public void setPainter(Painter painter) {
            if (areEqual(painter, getPainter())) return;
            uninstallPainterListener();
            super.setPainter(painter);
            installPainterListener();
//            fireStateChanged();
        }

        /**
         * Installs a listener to the painter if appropriate.
         * This implementation registers its painterListener if
         * the Painter is of type AbstractPainter.
         */
        protected void installPainterListener() {
            if (getPainter() instanceof AbstractPainter) {
                ((AbstractPainter) getPainter()).addPropertyChangeListener(getPainterListener());
            }
        }

        /**
         * Uninstalls a listener from the painter if appropriate.
         * This implementation removes its painterListener if
         * the Painter is of type AbstractPainter.
         */
        protected void uninstallPainterListener() {
            if (getPainter() instanceof AbstractPainter) {
                ((AbstractPainter) getPainter()).removePropertyChangeListener(painterListener);
            }
        }

        /**
         * Lazyly creates and returns the property change listener used
         * to listen to changes of the painter.
         * 
         * @return the property change listener used to listen to changes
         *   of the painter.
         */
        protected final PropertyChangeListener getPainterListener() {
            if (painterListener == null) {
                painterListener = createPainterListener();
            }
            return painterListener;
        }

        /**
         * Creates and returns the property change listener used
         * to listen to changes of the painter. <p>
         * 
         * This implementation fires a stateChanged on receiving
         * any propertyChange, if the isAdjusting flag is false. 
         * Otherwise does nothing.
         * 
         * @return the property change listener used to listen to changes
         *   of the painter.
         */
        protected PropertyChangeListener createPainterListener() {
            PropertyChangeListener l = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
//                    if (isAdjusting) return;
                    fireStateChanged();
                }
                
            };
            return l;
        }

        
    }
    @Override
    protected void setUp() throws Exception {
        SearchFactory.getInstance().setUseFindBar(true);
    }

    
    
}
