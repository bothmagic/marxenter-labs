package org.jdesktop.swingx;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * A handler for performing drag-and-drop reordering.
 * 
 * @author kschaefer
 */
public class ReorderingTransferHandler extends TransferHandler {
    /**
     * An adapter to allow list/model implementations to specialize the update. Some models may need
     * to be rebuilt, while other models can perform the updates required. Depending on the JList
     * implementation, the view may be sortable, and that mechanism could be used for the
     * reordering.
     * 
     * @author kschaefer
     */
    public interface ModelUpdater {
        /**
         * Updates the specified {@code JList} by moving the object at the {@code originationIndex}
         * to the {@code destinationIndex}.
         * 
         * @param list the list to modify
         * @param originationIndex the origination index
         * @param destinationIndex the destination index
         */
        void update(JList list, int originationIndex, int destinationIndex);
    }
    
    private static class ListReorderingTransferable implements Transferable {
        private final int index;

        public ListReorderingTransferable(int index) {
            this.index = index;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                IOException {
            if (isDataFlavorSupported(flavor)) {
                return index;
            }

            return null;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { LIST_MODEL_FLAVOR };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            for (DataFlavor supported : getTransferDataFlavors()) {
                if (supported.equals(flavor)) {
                    return true;
                }
            }

            return false;
        }

    }
    private static final DataFlavor LIST_MODEL_FLAVOR = new DataFlavor(
            ListReorderingTransferable.class, "application-x/list-index");
    
    private ModelUpdater updater;
    
    public ReorderingTransferHandler() {
        this(ModelUpdaters.DEFAULT_LIST_MODEL_UPDATER);
    }
    
    public ReorderingTransferHandler(ModelUpdater updater) {
        this.updater = updater;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        return new ListReorderingTransferable(((JList) c).getSelectedIndex());
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        boolean result = false;

        for (DataFlavor flavor : transferFlavors) {
            if (flavor.equals(LIST_MODEL_FLAVOR)) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        JList list = (JList) comp;
        int originationIndex = -1;
        int destinationIndex = list.getSelectedIndex();

        try {
            originationIndex = (Integer) t.getTransferData(LIST_MODEL_FLAVOR);
        } catch (Exception e) {
            return false;
        }

        if (destinationIndex == -1) {
            destinationIndex = list.getModel().getSize() - 1;
        }
        
        updater.update(list, originationIndex, destinationIndex);

        list.clearSelection();
        list.setSelectedIndex(destinationIndex);
        System.out.println(Arrays.toString(list.getSelectedIndices()));
        
        return true;
    }
}
