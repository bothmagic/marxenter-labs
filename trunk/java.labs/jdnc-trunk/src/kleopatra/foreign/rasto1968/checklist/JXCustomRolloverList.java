/*
 * Created on 02.07.2009
 *
 */
package rasto1968.checklist;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.rollover.ListRolloverController;
import org.jdesktop.swingx.rollover.ListRolloverProducer;
import org.jdesktop.swingx.rollover.RolloverProducer;

/**
 * Experimental XList with hotspot support integrated in Rollover-support.<p>
 * 
 * For now, extending - mainly because that's the only way to hook custom 
 * RolloverProducer/-Controller. Maybe need additional api on component itself.<p>
 * 
 * Possible problems: 
 * <ul>
 * <li> producer must consume a pressed if used for toggling the checkbox.
 * That's more invasive then it was meant for.
 * <li> whether or not to actually enable toggle must be controllable (and visually indicated) 
 * per-index, that is the checkBox must appear disabled if not
 * <li> slight feel glitch: checkbox action is triggered on released (or maybe LF-dependent?)
 * but the controller must consume a pressed (and even actually trigger the hotspot action) 
 * to not trigger a selection
 * <ul>
 * 
 */
public class JXCustomRolloverList extends JXList {

    
    @Override
    public void setRolloverEnabled(boolean rolloverEnabled) {
        boolean old = isRolloverEnabled();
        if (rolloverEnabled == old)
            return;
        super.setRolloverEnabled(rolloverEnabled);
        if (isRolloverEnabled()) {
            // this is diiirty - but the rollover listener needs to be notified before
            // the uidelegate
            // calling updateUI forces the ui listeners to be added after
            updateUI();
        }
    }

    @Override
    protected ListRolloverController<JXList> createLinkController() {
        return new HotSpotRolloverController();
    }

    @Override
    protected RolloverProducer createRolloverProducer() {
        return new HotSpotRolloverProducer();
    }
    
    public static class HotSpotRolloverController extends ListRolloverController<JXList> {

        
        @Override
        public void install(JXList table) {
            super.install(table);
            table.addPropertyChangeListener(HotSpotRolloverProducer.ROLLOVER_HOTSPOT, this);
        }

        @Override
        public void release() {
            if (component != null)
            component.addPropertyChangeListener(HotSpotRolloverProducer.ROLLOVER_HOTSPOT, this);
            super.release();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (HotSpotRolloverProducer.ROLLOVER_HOTSPOT.equals(evt.getPropertyName())) {
                hotSpot((Point) evt.getOldValue(), (Point) evt.getNewValue());
            } else {
                super.propertyChange(evt);
            }
        }

        private void hotSpot(Point oldValue, Point newValue) {
            if (newValue == null) return;
            if (!(component.getModel() instanceof CheckListModel)) return;
            CheckListModel checkListModel = (CheckListModel) component.getModel();
            checkListModel.toggleChecked(component.convertIndexToModel(newValue.y));
        }
        
    }

    public static class HotSpotRolloverProducer extends ListRolloverProducer {

        public static final String ROLLOVER_HOTSPOT = "swingx.hotspot";

         protected Point pendingHotSpot = null;

        /**
         * Delaying fire of hotSpot to released doesn't work - results in always
         * select.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (pendingHotSpot == null) {
                super.mouseReleased(e);
            } else {
                updateClientProperty((JComponent) e.getSource(),
                        ROLLOVER_HOTSPOT, true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!(e.getSource() instanceof JXList))
                return;
            // we assume to get a pressed only after some enter/exit/moved
            // don't bother if somewhere else on the list
            if (!hasCellRollover()) {
                return;
            }
            JXList list = (JXList) e.getSource();
            if (isHotSpotPressed(list, e.getPoint())) {
                // hmm ... if we don't do the actual toggle here the event is
                // used for selection
                // how weird is that?
                // Mouselister is a AWTListener - they are notified
                // first-added, first-notified
//                updateClientProperty((JComponent) e.getSource(),
//                        ROLLOVER_HOTSPOT, true);
               pendingHotSpot = new Point(rollover);
                e.consume();
            } else {
                pendingHotSpot = null;
                list.putClientProperty(ROLLOVER_HOTSPOT, null);
            }

        }

        private boolean isHotSpotPressed(JXList list, Point pt) {
            int index = rollover.y;
            Component c = list.getCellRenderer().getListCellRendererComponent(
                    list,
                    list.getElementAt(index), index,
                    list.isSelectedIndex(index),
                    list.hasFocus() && (list.getLeadSelectionIndex() == index));

            // producer must reset hotspot if not active
            boolean toggle = false;
            if (c instanceof HotSpotAware) {
                pt.y -= list.getCellBounds(index, index).y;
                toggle = ((HotSpotAware) c).isHotSpot(pt);
            }
            return toggle;
        }
        
        private boolean hasCellRollover() {
            return rollover != null && rollover.x >= 0 && rollover.y >= 0;
        }
        
        
    }
}
