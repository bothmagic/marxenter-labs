/*
 * Created on 25.07.2008
 *
 */
package org.jdesktop.swingx.table;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.CellRendererPane;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.JRendererLabel;

public class VistaColumnMoves extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(VistaColumnMoves.class
            .getName());

    public static void main(String[] args) {
        setSystemLF(true);
        VistaColumnMoves test = new VistaColumnMoves();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void interactiveNormal() {
        JXTable table = new JXTable(new AncientSwingTeam());
        table.setColumnControlVisible(true);
        table.setHorizontalScrollEnabled(true);
        table.getTableHeader().setAutoscrolls(true);
        showWithScrollingInFrame(table, "normal behaviour (for comparison)");

    }

    public void interactiveTransparent() {
        JXTable table = new JXTable(new AncientSwingTeam());
        table.setColumnControlVisible(true);
        table.setHorizontalScrollEnabled(true);
        table.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
        XTableHeader tableHeader = new XTableHeader(table.getColumnModel());
        tableHeader.setAutoscrolls(true);
        table.setTableHeader(tableHeader);
        JXFrame frame = wrapWithScrollingInFrame(table,
                "transparent header cell on move");
        addComponentOrientationToggle(frame);
        show(frame);
    }

    /**
     * Table header which behaves similarly to Vista on column moves via mouse
     * drags. Beware: dirty tricks! This should be supported by the ui-delegate,
     * but was rejected as unnecessary in core bug report #6434444
     * <p>
     * 
     * PENDING: implement bidi compliance
     * <p>
     * 
     */
    public static class XTableHeader extends JXTableHeader {

        private ColumnMoveSupport columnMoveSupport;


        public XTableHeader(TableColumnModel columnModel) {
            super(columnModel);
            columnMoveSupport = new ColumnMoveSupport(this);
        }

        protected void setDragOver(Point point) {
            if (!getAutoscrolls() || (getXTable() == null))
                return;
            int column = columnAtPoint(point);
            if (column >= 0) {
                getXTable().scrollColumnToVisible(column);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            columnMoveSupport.paint(g);
        }


        @Override
        protected void processMouseEvent(MouseEvent e) {
            if (columnMoveSupport.blocked(e)) return;
            super.processMouseEvent(e);
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e) {
            if (columnMoveSupport.blocked(e)) return;
            super.processMouseMotionEvent(e);
        }

    }

    /**
     * Support for Vista-style column move. Handles the visual effects
     * (transparent header renderer and insertion marker) and controls
     * the move itself. <p>
     * 
     *  NOTE: this should be handled by the ui-delegate! The trick to
     *  make it work is to block the other listeners if a column move
     *  by mouse is under way.
     */
    public static class ColumnMoveSupport  {

        private int mouseXOffset;

        private int source;

        private int target;

        private int marker;

        private XTableHeader header;

        private boolean inMove;

        private int draggingDistance;

        private MouseInputListener dragListener;
        private JLabel draggingImage;

        public ColumnMoveSupport(XTableHeader header) {
            install(header);
            draggingImage = new JRendererLabel();
            draggingImage.setBackground(ColorUtil
                    .setAlpha(Color.LIGHT_GRAY, 60));
            draggingImage.setForeground(Color.WHITE);
        }

        
        public void paint(Graphics g) {
            if (inMove) {
                paintDragImage(g);
                paintDropMarker(g);
            }
        }


        private void paintDragImage(Graphics g) {
            draggingImage.setText(((TableColumnExt) header.getDraggedColumn())
                    .getTitle());
            CellRendererPane pane = (CellRendererPane) header.getComponent(0);
            int column = getViewIndexForColumn(header.getDraggedColumn());
            Rectangle cellRect = header.getHeaderRect(column);
            cellRect.translate(header.columnMoveSupport.draggingDistance, 0);
            pane.paintComponent(g, draggingImage, header, cellRect.x, cellRect.y,
                    cellRect.width, cellRect.height, true);
        }

        private void paintDropMarker(Graphics g) {
            // Paint insertion marker
            if (marker == JLabel.CENTER)
                return;
            Rectangle cellRect = header.getHeaderRect(target);
            int dropLine = cellRect.x;
            if (header.getComponentOrientation().isLeftToRight()) {
                if (marker == JLabel.LEADING) {
                    dropLine += cellRect.width - 2;
                }
            } else {
                if (marker == JLabel.TRAILING) {
                    dropLine += cellRect.width - 2;
                }
            }
            g.setColor(Color.RED);
            g.drawLine(dropLine, cellRect.y, dropLine, cellRect.y
                    + cellRect.height);
        }


        public boolean blocked(MouseEvent e) {
            if (inMove) {
                processMouseEvent(e);
                if (e.isConsumed()) {
                    return true;
                }
            }
            return false;
        }

        private MouseInputListener getDragListener() {
            if (dragListener == null) {
                dragListener = new DragListener();
            }
            return dragListener;
        }
        
        private void install(XTableHeader header) {
            uninstall();
            if (header == null)
                return;
            this.header = header;
            header.addMouseListener(getDragListener());
            header.addMouseMotionListener(getDragListener());
        }

        private void uninstall() {
            if (header == null)
                return;
            header.removeMouseListener(getDragListener());
            header.removeMouseMotionListener(getDragListener());
            header = null;
        }


        private void processMouseEvent(MouseEvent e) {
            MouseInputListener listener = getDragListener();
            if (listener != null) {
                int id = e.getID();
                switch (id) {
                case MouseEvent.MOUSE_PRESSED:
                    listener.mousePressed(e);
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    listener.mouseReleased(e);
                    break;
                case MouseEvent.MOUSE_CLICKED:
                    listener.mouseClicked(e);
                    break;
                case MouseEvent.MOUSE_EXITED:
                    listener.mouseExited(e);
                    break;
                case MouseEvent.MOUSE_ENTERED:
                    listener.mouseEntered(e);
                    break;
                case MouseEvent.MOUSE_MOVED:
                    listener.mouseMoved(e);
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    listener.mouseDragged(e);
                    break;
                }
            }

        }

        public class DragListener implements MouseInputListener {
            public void mouseDragged(MouseEvent e) {
                if (inMove) {
                    e.consume();
                    draggingDistance = e.getX() - mouseXOffset;
                    int column = header.columnAtPoint(e.getPoint());
                    // we dragged either left/right off
                    if (column < 0) {
                        // PENDING: this looks terrible - any way to
                        // cleanup the logic?
                        if (header.getComponentOrientation().isLeftToRight()) {
                            if (e.getX() <= 0) {
                                column = 0;
                            } else {
                                column = header.getColumnModel()
                                        .getColumnCount() - 1;
                            }
                        } else {
                            if (e.getX() >= header.getWidth()) {
                                column = 0;
                            } else {
                                column = header.getColumnModel()
                                        .getColumnCount() - 1;
                            }
                        }
                    }
                    if (column == source) {
                        target = source;
                    } else {
                        Rectangle targetRect = header.getHeaderRect(column);
                        // always pos - absolute coordinates to left corner of
                        // comp
                        // no - not for left-off. But: the negative seems to
                        // do the right thing when dragging off
                        int relativeDrag = e.getX() - targetRect.x;
                        boolean left = relativeDrag < targetRect.width / 2;
                        int align = left ? JLabel.TRAILING : JLabel.LEADING;
                        if (!header.getComponentOrientation().isLeftToRight()) {
                            align = left ? JLabel.LEADING : JLabel.TRAILING;
                        }
                        if (column < source) {
                            if (JLabel.TRAILING == align) {
                                target = column;
                            } else {
                                target = column + 1;
                            }
                            marker = JLabel.TRAILING;
                        } else {
                            if (JLabel.TRAILING == align) {
                                target = column - 1;
                            } else {
                                target = column;
                            }
                            marker = JLabel.LEADING;
                        }
                    }
                    if (target == source) {
                        marker = JLabel.CENTER;
                    }
                    header.setDragOver(e.getPoint());
                    header.repaint();
                }
            }

            public void mousePressed(MouseEvent e) {
                inMove = header.getDraggedColumn() != null;
                if (inMove) {
                    source = getViewIndexForColumn(header.getDraggedColumn());
                    target = source;
                    mouseXOffset = e.getX();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (inMove) {
                    if ((target >= 0) && (target != source)) {
                        header.getColumnModel().moveColumn(source, target);
                    }
                }
                inMove = false;
            }

            public void mouseClicked(MouseEvent e) {
                inMove = false;
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {

            }

            public void mouseMoved(MouseEvent e) {

            }
        }
        /**
         * Returns the (visible) view index for the given column or -1 if not
         * visible or not contained in this header's columnModel.
         * 
         * 
         * @param aColumn
         * @return
         */
        private int getViewIndexForColumn(TableColumn aColumn) {
            if (aColumn == null)
                return -1;
            TableColumnModel cm = header.getColumnModel();
            for (int column = 0; column < cm.getColumnCount(); column++) {
                if (cm.getColumn(column) == aColumn) {
                    return column;
                }
            }
            return -1;
        }

    }

}
