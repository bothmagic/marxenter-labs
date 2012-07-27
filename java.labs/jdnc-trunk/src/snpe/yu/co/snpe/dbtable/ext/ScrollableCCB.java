/*
 * Created on 21.06.2006
 *
 */
package yu.co.snpe.dbtable.ext;

import java.awt.ComponentOrientation;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.ColumnControlPopup;

import yu.co.snpe.dbtable.core.ScrollableMenu;

/**
 * A ColumnControl using the ScrollableMenu as popup control.
 * 
 * 
 * @author Jeanette Winzenburg, 
 */
public class ScrollableCCB extends ColumnControlButton {


    public ScrollableCCB(JXTable table, Icon icon) {
        super(table, icon);
    }

    @Override
    protected ColumnControlPopup createColumnControlPopup() {
        return new ScrollableControlPopup();
    }

    public class ScrollableControlPopup extends DefaultColumnControlPopup {
        private ScrollableMenu scrollableMenu = null;

        @Override
        public void updateUI() {
            getMenu().updateUI();
        }
        
        @Override
        public void applyComponentOrientation(ComponentOrientation o) {
            getMenu().applyComponentOrientation(o);
        }

        @Override
        public void removeAll() {
            getMenu().removeAll();
        }


        @Override
        protected void addItem(JMenuItem item) {
            getMenu().add(item);
        }



        @Override
        protected void addSeparator() {
            getMenu().addSeparator();
        }


        @Override
        protected JPopupMenu getPopupMenu() {
            return getMenu().getPopupMenu();
        }


        protected ScrollableMenu getMenu() {
            if (scrollableMenu == null) {
                scrollableMenu = new ScrollableMenu();
            }
            return scrollableMenu;
        }
        
    }

    
}
