package weebib.fastfilechooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultRowSorter;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.sort.RowFilters;
/**
 * FastFileChooser is basically a JTextField used to type the path to a File.
 * The JTextField has an auto-completion feature similar to that of most IDEs.
 * <p>
 * Upon Ctrl+SPACE, an auto-completion window appears that contains the Files
 * compatible with the path currently typed.
 * <p>
 * If Ctrl+SPACE is hit while the auto-completion window is displayed, the
 * common prefix of all possible Files is appended to the path.
 * <p>
 * 
 * Adaption: use a filtered list.
 * Quick change to use table (because a JXList is currently not filterable).
 * @author weebib
 */
public class FastFileChooserWithTableJW extends JPanel {
    // Used to retrieve icons for a given file
    // I find it quite ridiculous that there is no simplier mechanism than this
    // to retrieve a file icon
    // the problem is that this :
    // FileSystemView.getFileSystemView().getSystemIcon(theFile);
    // doesn't work on Mac OS.
    private JFileChooser theFileChooser;

    private JTextField theTextField;

    private File theCurrentFile;

    private JWindow thePopupWindow;

    private JXTable theFileItemTable;

    private FileItemTableModel theFileItemTableModel;

    private Point theRelativePosition;

    private DocumentListener theDocumentListener;

    private List<FileItem> theFileItems;

    private FileItemFilter fileItemFilter;


    // -- Inner classes ------
    private class FileItem {
        private File theFile;

        private String theName;

        public FileItem(File aFile, String aName) {
            theFile = aFile;
            theName = aName;
        }

        public File getFile() {
            return theFile;
        }

        public Icon getIcon() {
            return theFileChooser.getIcon(theFile);
        }

        public String getText() {
            return theName;
        }
    }

    private class FileItemListModel extends AbstractListModel {
        public int getSize() {
            return theFileItems.size();
        }

        public Object getElementAt(int index) {
            return theFileItems.get(index);
        }

        public void fireCleared(int aSize) {
            fireIntervalRemoved(this, 0, aSize - 1);
        }

        public void fireFilled(int aSize) {
            fireIntervalAdded(this, 0, aSize - 1);
        }
    }

    private class FileItemTableModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public int getRowCount() {
            return theFileItems.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return theFileItems.get(rowIndex);
        }
        
        public void fireCleared(int aSize) {
            if (aSize > getRowCount()) aSize = getRowCount();
            if (aSize == 0) return;
            fireTableRowsDeleted(0, aSize - 1);
        }

        public void fireFilled(int aSize) {
            fireTableRowsInserted(0, aSize - 1);
        }
        
        
    }
    // -- Constructor ------
    /**
     * @param aStartDirectory
     */
    public FastFileChooserWithTableJW(File aStartDirectory) {
        super(new BorderLayout(5, 5));
        if (aStartDirectory == null || !aStartDirectory.isDirectory()) {
            throw new IllegalArgumentException("Not a valid startDirectory");
        }

        theFileChooser = new JFileChooser();

        FileView fileView = theFileChooser.getUI().getFileView(theFileChooser);
        System.out.println("fileView = " + fileView);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        theTextField = new JTextField(50);
        add(theTextField, BorderLayout.NORTH);
        theTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (theCurrentFile != null && theCurrentFile.exists()) {
                    launchFile(theCurrentFile);
                }
            }
        });

        setCurrentFile(aStartDirectory);

        thePopupWindow = null;
        theRelativePosition = new Point(0, 0);

        theFileItems = new LinkedList<FileItem>();
        theFileItemTableModel = new FileItemTableModel();
        theFileItemTable = new JXTable(theFileItemTableModel) {

            @Override
            public int getVisibleRowCount() {
                return Math.min(super.getVisibleRowCount(), getRowCount());
            }
            
        };
        theFileItemTable.setVisibleRowCount(10);
        theFileItemTable.setTableHeader(null);
        theFileItemTable.setShowGrid(false, false);
        installFilters();
        theFileItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theFileItemTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable list,
                    Object value, boolean isSelected,
                    boolean cellHasFocus, int row, int column) {
                super.getTableCellRendererComponent(list, value, 
                        isSelected, cellHasFocus, row, column);
                FileItem fileItem = (FileItem) value;
                setIcon(fileItem.getIcon());
                setText(fileItem.getText());
                return this;
            }
        });
        theFileItemTable.setFocusable(false);
        theFileItemTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selected = theFileItemTable.getSelectedRow();
                    setNextFileItem((FileItem) theFileItemTable
                            .getValueAt(selected, 0));
                }
            }
        });
        theFileItemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if ((e.getButton() == MouseEvent.BUTTON1)
                        && (e.getClickCount() == 2)) {
                    int selected = theFileItemTable.getSelectedRow();
                    setNextFileItem((FileItem) theFileItemTable
                            .getValueAt(selected, 0));
                }
            }
        });

        InputMap inputMap = theTextField.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
                InputEvent.CTRL_MASK), "controlEspace");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
                InputEvent.CTRL_MASK), "home");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END,
                InputEvent.CTRL_MASK), "end");
        ActionMap actionMap = theTextField.getActionMap();
        actionMap.put("controlEspace", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onControlSpace();
            }
        });
        actionMap.put("home", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveStart();
            }
        });
        actionMap.put("end", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveEnd();
            }
        });
        theTextField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isAutoCompleting()) {
                    stopAutoCompletion();
                }
            }
        });
        theTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.isConsumed() || !isAutoCompleting())
                    return;
                switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    int selected = theFileItemTable.getSelectedRow();
                    setNextFileItem((FileItem) theFileItemTable
                            .getValueAt(selected, 0));
                    e.consume();
                    break;
                case KeyEvent.VK_DOWN:
                    moveDown();
                    e.consume();
                    break;
                case KeyEvent.VK_UP:
                    moveUp();
                    e.consume();
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    movePageDown();
                    e.consume();
                    break;
                case KeyEvent.VK_PAGE_UP:
                    movePageUp();
                    e.consume();
                    break;
                }
            }
        });
        theDocumentListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (!isAutoCompleting())
                    return;
                updateFileItems();
            }

            public void removeUpdate(DocumentEvent e) {
                if (!isAutoCompleting())
                    return;
                updateFileItems();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        };
        theTextField.getDocument().addDocumentListener(theDocumentListener);
    }

    // -- Private methods ------
    private void setCurrentFile(File aFile) {
        theCurrentFile = aFile;
        String absolutePath = theCurrentFile.getAbsolutePath();
        if (theCurrentFile.isDirectory()
                && !absolutePath.endsWith(File.separator)) {
            absolutePath += File.separator;
        }
        theTextField.setText(absolutePath);
        theTextField.setCaretPosition(theTextField.getText().length());
    }

    private void launchFile(File aFile) {
        // todo put a mechanism to launch the given file.
        System.out.println("aFile = " + aFile);
    }

    private void onControlSpace() {
        if (!isAutoCompleting()) {
            startAutoCompletion();
        } else {
            // append the common prefix for existing fileItems
            String[] names = new String[theFileItems.size()];
            for (int i = 0; i < names.length; i++) {
                names[i] = theFileItems.get(i).getText();
            }
            String prefix = findCommonPrefix(names);
            if (prefix.length() > 0) {
                theTextField.getDocument().removeDocumentListener(
                        theDocumentListener);
                File file = new File(theTextField.getText());
                File dir;
                if (file.isDirectory()) {
                    dir = file;
                } else {
                    dir = file.getParentFile();
                }
                setCurrentFile(new File(dir, prefix));
                theTextField.getDocument().addDocumentListener(
                        theDocumentListener);
            }
        }
    }

    private void startAutoCompletion() {
        prepareAutoCompletion();
        updateFileItems();
    }

    private void stopAutoCompletion() {
        setFileItems(new FileItem[0]);
        thePopupWindow.setVisible(false);
    }

    private boolean isAutoCompleting() {
        if (thePopupWindow == null)
            return false;
        return thePopupWindow.isVisible();
    }

    private void prepareAutoCompletion() {
        if (thePopupWindow == null) {
            Window owner = SwingUtilities.getWindowAncestor(this);
            thePopupWindow = new JWindow(owner);
            JScrollPane scrollPane = new JScrollPane(theFileItemTable,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory
                    .createBevelBorder(BevelBorder.RAISED));
            thePopupWindow.setContentPane(scrollPane);
            owner.addComponentListener(new ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    stopAutoCompletion();
                }

                public void componentMoved(ComponentEvent e) {
                    if (isAutoCompleting()) {
                        placePopupWindow();
                    }
                }
            });
            theFileItemTable.setFont(theTextField.getFont());
        }
    }

    private void installFilters() {
        fileItemFilter = new FileItemFilter();
        ((DefaultRowSorter) theFileItemTable.getRowSorter()).setRowFilter(fileItemFilter);
    }

    /**
     * Use view filtering instead of changing the model content. 
     * In Mustang sorting, RowFilters are immutable. This here is still ol'
     * swingx style, that is mutable - the filter was supposed to trigger
     * a contentsChanged. Not supported in Mustang, so client code must
     * explicitly trigger a sort, or create and set a new filter each time.
     * Hmm ...  
     */
    public static class FileItemFilter extends RowFilters.GeneralFilter {

        private String prefix;

        public FileItemFilter() {
            super(0);
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        protected boolean include(
                Entry<? extends Object, ? extends Object> entry,
                int index) {
            Object value = entry.getValue(0);
            if (value instanceof FileItem) {
                FileItem fileItem = (FileItem) value;
                if ((prefix == null) || (prefix.length() == 0))
                    return true;
                return fileItem.getText().toLowerCase().startsWith(prefix);
            }
            return false;
        }

    }

    private void updateFileItems() {
        String text = theTextField.getText();
        File file = new File(text);
        File dir;
        final String name;
        int index;
        if (file.isDirectory() && text.endsWith(File.separator)) {
            dir = file;
            name = "";
            index = text.length();
        } else {
            if (file.isFile()) {
                if (isAutoCompleting()) {
                    stopAutoCompletion();
                }
                return;
            }
            dir = file.getParentFile();
            if (dir == null) {
                if (isAutoCompleting()) {
                    stopAutoCompletion();
                }
                return;
            }
            name = file.getName();
            index = text.lastIndexOf(File.separator) + 1;
        }
        // new directory
        if (name.length() == 0) {
            FileItem[] fileItems = retrieveFileItems(dir);
            if (fileItems.length == 0) {
                if (isAutoCompleting()) {
                    stopAutoCompletion();
                }
                return;
            }
            // special case handling ... left out for now.
            // } else if (fileItems.length == 1 && !isAutoCompleting()) {
            // setNextFileItem(fileItems[0]);
            // return;
            setFileItems(fileItems);
        }
        fileItemFilter.setPrefix(name.toLowerCase());
        ((DefaultRowSorter) theFileItemTable.getRowSorter()).sort();
//        theFileItemTable.revalidate();
//        theFileItemTable.repaint();
//        ((JComponent) theFileItemTable.getParent()).revalidate();
        updatePopupLocation(index);
    }

    private void setFileItems(FileItem[] someFileItems) {
        int size = theFileItems.size();
        if (size > 0) {
            theFileItems.clear();
            theFileItemTableModel.fireCleared(size);
        }
        if (someFileItems != null) {
            for (int i = 0; i < someFileItems.length; i++) {
                theFileItems.add(someFileItems[i]);
            }
        }
        size = theFileItems.size();
        if (size > 0) {
            theFileItemTableModel.fireFilled(size);
            theFileItemTable.setRowSelectionInterval(0, 0);
        }
        thePopupWindow.pack();
    }

    private FileItem[] retrieveFileItems(File aDirectory) {
        if (aDirectory == null || !aDirectory.isDirectory())
            return new FileItem[0];
        File[] files = aDirectory.listFiles();
        List<FileItem> fileItems = new LinkedList<FileItem>();
        for (int i = 0; i < files.length; i++) {
            addFileItem(fileItems, files[i], "");
        }
        return fileItems.toArray(new FileItem[fileItems.size()]);
    }

    // private FileItem[] retrieveFileItems(File aDirectory, final String aName)
    // {
    // if (aDirectory == null || !aDirectory.isDirectory() || aName == null)
    // return new FileItem[0];
    // File[] files = aDirectory.listFiles(new FileFilter() {
    // public boolean accept(File pathname) {
    // return
    // (pathname.getName().toLowerCase().startsWith(aName.toLowerCase()));
    // }
    // });
    // List<FileItem> fileItems = new LinkedList<FileItem>();
    // for (int i = 0; i < files.length; i++) {
    // addFileItem(fileItems, files[i], "");
    // }
    // return fileItems.toArray(new FileItem[fileItems.size()]);
    // }
    //
    private void addFileItem(List<FileItem> someFileItems, File aFile,
            String aPrefix) {
        if (aFile == null || !aFile.exists())
            return;
        String name = aPrefix + aFile.getName();
        someFileItems.add(new FileItem(aFile, name));
        if (aFile.isDirectory()) {
            File[] children = aFile.listFiles();
            if (children != null && children.length == 1) {
                addFileItem(someFileItems, children[0], name + File.separator);
            }
        }
    }

    private void setNextFileItem(FileItem aFileItem) {
        theTextField.getDocument().removeDocumentListener(theDocumentListener);
        setCurrentFile(aFileItem.getFile());
        theTextField.getDocument().addDocumentListener(theDocumentListener);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                startAutoCompletion();
            }
        });
    }

    private void updatePopupLocation(final int index) {
        thePopupWindow.pack();
        Rectangle rect = null;
        try {
            rect = theTextField.getUI().modelToView(theTextField, index);
        } catch (BadLocationException e) {
            //
        }
        if (rect == null)
            return;
        theRelativePosition = new Point(rect.x, rect.y + rect.height);
        Point p = theTextField.getLocationOnScreen();
        Point windowLocation = new Point(p.x + theRelativePosition.x, p.y
                + theRelativePosition.y);
        windowLocation = relocatePopupWindow(windowLocation, thePopupWindow);
        thePopupWindow.setLocation(windowLocation);
        if (!isAutoCompleting()) {
            thePopupWindow.setVisible(true);
            theTextField.requestFocus();
        }
    }

    private void placePopupWindow() {
        if (theRelativePosition == null)
            return;
        Point p = theTextField.getLocationOnScreen();
        Point windowLocation = new Point(p.x + theRelativePosition.x, p.y
                + theRelativePosition.y);
        windowLocation = relocatePopupWindow(windowLocation, thePopupWindow);
        thePopupWindow.setLocation(windowLocation);
    }

    private void moveDown() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        int index = theFileItemTable.getSelectedRow();
        index++;
        if (index > size - 1)
            index = 0;
        theFileItemTable.setRowSelectionInterval(index, index);
        theFileItemTable.scrollRowToVisible(index);
    }

    private void moveUp() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        int index = theFileItemTable.getSelectedRow();
        index--;
        if (index < 0)
            index = size - 1;
        theFileItemTable.setRowSelectionInterval(index, index);
        theFileItemTable.scrollRowToVisible(index);
    }

    private void moveStart() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        theFileItemTable.setRowSelectionInterval(0, 0);
        theFileItemTable
                .scrollRowToVisible(0);
    }

    private void moveEnd() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        int endIndex = size - 1;
        theFileItemTable.setRowSelectionInterval(endIndex, endIndex);
        theFileItemTable.scrollRowToVisible(endIndex);
    }

    private void movePageUp() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        int current = theFileItemTable.getSelectedRow();
        int newIndex = Math.max(0, current
                - Math.max(0, theFileItemTable.getVisibleRowCount() - 1));
        theFileItemTable.setRowSelectionInterval(newIndex, newIndex);
        theFileItemTable.scrollRowToVisible(newIndex);
    }

    private void movePageDown() {
        int size = theFileItemTableModel.getRowCount();
        if (size < 1)
            return;
        int current = theFileItemTable.getSelectedRow();
        int newIndex = Math.min(size - 1, current
                + Math.max(0, theFileItemTable.getVisibleRowCount() - 1));
        theFileItemTable.setRowSelectionInterval(newIndex, newIndex);
        theFileItemTable.scrollRowToVisible(newIndex);
    }

    private static String findCommonPrefix(String[] someStrings) {
        char[] prefix = null;
        int length = -1;
        for (String s : someStrings) {
            if (prefix == null) {
                prefix = s.toCharArray();
                length = prefix.length;
            } else {
                boolean stop = false;
                int index = 0;
                while (index < length && index < s.length() && !stop) {
                    if (prefix[index] != s.charAt(index)) {
                        stop = true;
                    } else {
                        index++;
                    }
                }
                length = index;
            }
            if (length == 0)
                break;
        }
        return new String(prefix, 0, length);
    }

    private static Point relocatePopupWindow(Point aPoint, JWindow aWindow) {
        Dimension windowDim = aWindow.getSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
                aWindow.getGraphicsConfiguration());
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension availableDim = new Dimension(screenDim.width - insets.left
                - insets.right, screenDim.height - insets.top - insets.bottom);
        Point p = new Point(aPoint);
        if (windowDim.width < availableDim.width) {
            int rightDelta = (insets.left + availableDim.width)
                    - (p.x + windowDim.width);
            if (rightDelta < 0) {
                p.x = Math.max(insets.left, p.x + rightDelta);
            }
        }
        if (windowDim.height < availableDim.height) {
            int bottomDelta = (insets.top + availableDim.height)
                    - (p.y + windowDim.height);
            if (bottomDelta < 0) {
                p.y = Math.max(insets.top, p.y + bottomDelta);
            }
        }
        return p;
    }
}
