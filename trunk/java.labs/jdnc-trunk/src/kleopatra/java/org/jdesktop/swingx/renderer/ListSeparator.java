/*
 * Created on 23.02.2008
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;


/**
 * Allow separator in a List/Combo. Base idea (from Santhosh): do it 
 * on the renderer level by wrapping the rendering component into
 * a panel which contains the actual separator.
 * 
 *  Trying to do it the SwingX way by highlighting. Allows to 
 *  separate the logic as to when to show the separator from the
 *  rendering as such. Options:
 *  
 *  - use a border as separator. Visuals not quite okay.
 *  - use the wrapping trick. Doing so, the wrapper must delegate
 *  all allowed visual modifications down to the wrapped component, similar
 *  to what a WrappingIconPanel does. 
 *  
 *   
 */
public class ListSeparator extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(ListSeparator.class.getName());
    public static void main(String args[]) {
      setSystemLF(true);
        ListSeparator test = new ListSeparator();
      try {
//        test.runInteractiveTests();
         test.runInteractiveTests("interactive.*Separator.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    

    /**
     * The real issue to create a combo showing separators in its dropdown.
     * Do it with a list until we have a highlightable combo.
     * 
     * Here we use a borderHighlighter for the separator if needed. Same
     * problem as Santhosh (selection rect overlaps border). Highlighters
     * working, but same overlap.
     */
    public void interactiveListWithBorderAsSeparator() {
        JXList list = new JXList(AncientSwingTeam.createNamedColorListModel(), true);
        list.setRolloverEnabled(true);
        list.toggleSortOrder();
        HighlightPredicate predicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.row == 0) return false;
                String myText = adapter.getString();
                String first = myText.substring(0, 1);
                String previousText = adapter.getFilteredStringAt(adapter.row - 1, 0);
                return !previousText.startsWith(first);
            }};
        Border empty = BorderFactory.createEmptyBorder(10, 0, 0, 0);    
        Border border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY);    
        BorderHighlighter highlighter = new BorderHighlighter(predicate, 
                BorderFactory.createCompoundBorder(empty, border), true);
        list.addHighlighter(highlighter);
        list.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW,
                Color.RED, null));
        JXFrame frame = showWithScrollingInFrame(list, "XList - separators border highlighter");
        addStatusMessage(frame, "selection rect overlaps separator");
    }

    /**
     * The real issue to create a combo showing separators in its dropdown.
     * Do it with a list until we have a highlightable combo.
     * 
     * Here we use a highighter which decorates by replacing the rendering component with
     * a custom panel containing the separator. That's NOT recommeneded but gives
     * the correct selection rectangle (no overlap of border/separator). Highlighters
     * don't work.
     */
    public void interactiveListWithPanelContainingSeparator() {
        JXList list = new JXList(AncientSwingTeam.createNamedColorListModel(), true);
        list.setRolloverEnabled(true);
        list.toggleSortOrder();
        HighlightPredicate predicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.row == 0) return false;
                String myText = adapter.getString();
                String first = myText.substring(0, 1);
                String previousText = adapter.getFilteredStringAt(adapter.row - 1, 0);
                return !previousText.startsWith(first);
            }};
        Highlighter highlighter = new AbstractHighlighter(predicate){
            JXPanel panel = new JXPanel(new BorderLayout(0, 2));
            JSeparator separator = new JSeparator();
            Border border = BorderFactory.createEmptyBorder(3, 0, 0, 0);
            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                panel.removeAll();
                panel.add(component);
                panel.setBorder(border);
                separator.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                panel.add(separator, BorderLayout.NORTH);
                panel.setOpaque(false);
                panel.invalidate();
                separator.invalidate();
                return panel;
            }};    
        list.addHighlighter(highlighter);
        list.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW,
                Color.RED, null));
        JXFrame frame = showWithScrollingInFrame(list, "XList - separate by highlighter");
        addStatusMessage(frame, "Highlighter replaces comp - on-the-fly. Rollover highlighter not working");
    }

    /**
     * The real issue to create a combo showing separators in its dropdown.
     * Do it with a list until we have a highlightable combo.
     * 
     * Here we use a highighter which decorates by replacing the rendering component with
     * a custom panel containing the separator. That's NOT recommeneded but gives
     * the correct selection rectangle (no overlap of border/separator). 
     */
    public void interactiveListWithSeparatorPanel() {
        final JXList list = new JXList(AncientSwingTeam.createNamedColorListModel(), true);
        list.setRolloverEnabled(true);
        list.toggleSortOrder();
        HighlightPredicate predicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                if (adapter.row == 0) return false;
                String myText = adapter.getString();
                String first = myText.substring(0, 1);
                String previousText = adapter.getFilteredStringAt(adapter.row - 1, 0);
                return !previousText.startsWith(first);
            }};
            
        Highlighter highlighter = new AbstractHighlighter(predicate){
            SeparatorPanel panel = new SeparatorPanel();
            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                panel.setDelegate((JComponent) component);
                return panel;
            }};    
        list.addHighlighter(highlighter);
        list.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW,
                Color.RED, null));
        
        HighlightPredicate length =  new HighlightPredicate() {

            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                String text = adapter.getString();
                return (text != null) && text.length() > 5;
            }};
            
        Highlighter font = new AbstractHighlighter(length) {
            Font derived = list.getFont().deriveFont(Font.BOLD, 20f);
            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                component.setFont(derived);
                return component;
            }};    
        list.addHighlighter(font);    
        JXFrame frame = showWithScrollingInFrame(list, "XList - separators in panel");
        addStatusMessage(frame, "Highlighter replaces rendering comp with dedicated separator panel");
    }
    
    public static class SeparatorPanel extends JXPanel {
        Border border = BorderFactory.createEmptyBorder(3, 0, 0, 0);
        JSeparator separator = new JSeparator();
        JComponent delegate;
        
        public SeparatorPanel() {
            super(new BorderLayout(0, 2));
            setOpaque(false);
            super.setBorder(border);
            add(separator, BorderLayout.NORTH);
        }
        
        public void setDelegate(JComponent component) {
            if (delegate != null) {
                remove(delegate);
            }
            this.delegate = component;
            add(delegate, BorderLayout.CENTER);
            invalidate();
        }

        @Override
        public void setBackground(Color bg) {
            if (delegate !=  null) {
                delegate.setBackground(bg);
            }
            super.setBackground(bg);
        }

        @Override
        public void setBorder(Border border) {
            if (delegate !=  null) {
                delegate.setBorder(border);
            }
        }

        @Override
        public void setFont(Font font) {
            if (delegate != null) {
                delegate.setFont(font);
            }
            super.setFont(font);
        }

        @Override
        public void setForeground(Color fg) {
            if (delegate != null) {
                delegate.setForeground(fg);
            }
            super.setForeground(fg);
        }
        
        
    }
    /**
     * The real issue to create a combo showing separators in its dropdown.
     * Do it with a list until we have a highlightable combo.
     * 
     * Problem: selection overlaps separator (?? - really, anything changed in this example 
     * - because seems okay). Highlighters don't work.
     */
    public void interactiveListWithSanthoshSeparator() {
        JXList list = new JXList(AncientSwingTeam.createNamedColorListModel(), true);
        list.setRolloverEnabled(true);
        list.toggleSortOrder();
        ComboSeparatorsRenderer renderer = new ComboSeparatorsRenderer(new DefaultListRenderer()) {

            @Override
            protected boolean addSeparatorAfter(JList list, Object value,
                    int index) {
                return index % 4 == 0;
            }
            
        };
        list.setCellRenderer(renderer);
        list.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW,
                Color.RED, null));
        showWithScrollingInFrame(list, "XList - separators Santhosh (delegate renderer)");
    }

    /**
     * @author Santhosh Kumar T
     * @email santhosh.tekuri@gmail.com
     */
    public static abstract class ComboSeparatorsRenderer implements ListCellRenderer{
        private ListCellRenderer delegate;
        private JPanel separatorPanel = new JPanel(new BorderLayout(0, 2));
        private JSeparator separator = new JSeparator();

        public ComboSeparatorsRenderer(ListCellRenderer delegate){
            this.delegate = delegate;
            separatorPanel.setOpaque(false);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            Component comp = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(index!=-1 && addSeparatorAfter(list, value, index)){ // index==1 if renderer is used to paint current value in combo
                separatorPanel.removeAll();
                separatorPanel.add(comp, BorderLayout.CENTER);
                separatorPanel.add(separator, BorderLayout.SOUTH);
                return separatorPanel;
            }else
                return comp;
        }

        protected abstract boolean addSeparatorAfter(JList list, Object value, int index);
    }
}
