/*
* $Id: Editor.java 135 2004-10-19 01:10:49Z gonzo $
*
* Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
* Santa Clara, California 95054, U.S.A. All rights reserved.
*/

package org.jdesktop.jdnc.incubator.gonzo.editor;

import org.jdesktop.swing.JXEditorPane;

import org.jdesktop.swing.Application;
import org.jdesktop.swing.JXFrame;
import org.jdesktop.swing.JXStatusBar;

import org.jdesktop.swing.actions.AbstractActionExt;
import org.jdesktop.swing.actions.ActionFactory;
import org.jdesktop.swing.actions.ActionManager;
import org.jdesktop.swing.actions.BoundAction;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Editor
    extends JPanel {
    
    private static List imageTypes;
    
    private ActionManager manager = null;
    private List editorActions = null;
    private List colorActions = null;
    private JXEditorPane renderer = null;
    private JScrollPane rendererScroller = null;
    private JXEditorPane editor = null;
    private JScrollPane editorScroller = null;
    private JXStatusBar status = null;
    private DropTargetListener dropper = null;
    private JPopupMenu editorPopup = null;
    
    static {
        imageTypes = new ArrayList();
        
        imageTypes.add(Constants.IMAGE_TIFF);
        imageTypes.add(Constants.IMAGE_TIF);
        imageTypes.add(Constants.IMAGE_GIF);
        imageTypes.add(Constants.IMAGE_JPEG);
        imageTypes.add(Constants.IMAGE_JPG);
        imageTypes.add(Constants.IMAGE_PNG);
    }

    public Editor() {
        init();
        
        setEditor(false, true);
    }

    public void setFocus() {
        this.editor.requestFocus();
    }
    
    public void setEditor(boolean selected) {
        setEditor(selected, false);
    }
    
    public void setEditor(boolean selected, boolean reset) {
        String contentType = ! selected ?
            Constants.MIME_HTML : Constants.MIME_PLAIN;
        String t = this.editor != null ? removeText(this.editor) : "";

        this.editor = new JXEditorPane();
        
        this.editor.setContentType(contentType);   
        this.editor.setPreferredSize(new Dimension(Constants.EDITOR_WIDTH,
            Constants.EDITOR_HEIGHT));
        this.editor.setMinimumSize(this.editor.getPreferredSize());
        this.editor.setMargin(new Insets(2, 2, 2, 2));
        this.editor.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                showPopupMenu(me);
            }

            public void mouseReleased(MouseEvent me) {
                showPopupMenu(me);
            }

            private void showPopupMenu(MouseEvent me) {
                if (me.isPopupTrigger()) {
                    editorPopup.show((Component)me.getSource(), me.getX(), me.getY());
                }
            }
        });
        this.editor.setCaretPosition(this.editor.getDocument().getLength());
        
        InputMap im = this.editor.getInputMap();
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK),
            DefaultEditorKit.backwardAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK),
            DefaultEditorKit.forwardAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK),
            DefaultEditorKit.upAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK),
            DefaultEditorKit.downAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK),
            Constants.BREAK);
        
        new DropTarget(this.editor, this.dropper);
        
        insertText(this.editor, t);
        
        this.editorScroller.setViewportView(this.editor);
        
        ActionManager am = Application.getInstance().getActionManager();
        
        am.setSelected(Constants.EDITOR, selected);
        am.setEnabled(Constants.BOLD, ! selected);
        am.setEnabled(Constants.ITALIC, ! selected);
        am.setEnabled(Constants.UNDERLINE, ! selected);
        am.setEnabled(Constants.LEFT, ! selected);
        am.setEnabled(Constants.CENTER, ! selected);
        am.setEnabled(Constants.RIGHT, ! selected);
        
        if (reset) {
            // xxx: don't reset but honor currently selected
            //am.setSelected(Constants.LEFT, true);
        }
        
        setFocus();
    }
    
    // xxx: don't dispatch empty body ... but we'll loose headers (eg location)
    public void dispatch() {
        setEditor(false);
        
        if (hasText(this.editor)) {
            //System.out.println("ep: " + readText(this.editor));
            //System.out.println("rd: " + readText(this.renderer));
            insertText(this.renderer, removeText(this.editor));
            //System.out.println("ep: " + readText(this.editor));
            //System.out.println("rd: " + readText(this.renderer));
            
            setEditor(false, true);
        }
    }
    
    private void init() {
        this.manager = createManager();
        this.dropper = createDropper();

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(gb);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        Component c = createRenderer();

        gb.setConstraints(c, gbc);

        add(c);

        c = createEditor();

        gbc.gridy++;

        gb.setConstraints(c, gbc);

        add(c);

        c = createStatus();

        gbc.gridy++;

        gb.setConstraints(c, gbc);

        add(c);
    }

    private ActionManager createManager() {
        ActionManager am = Application.getInstance().getActionManager();
        String action = null;
        
        this.editorActions = new ArrayList();
        
        this.editorActions.add(action = Constants.UNDO);
        am.addAction(createAction(action, Constants.UNDO_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.UNDO_IMAGE),
            Constants.UNDO_DESCRIPTION, null, Constants.UNDO_MNEMONIC, true,
            false));
        this.editorActions.add(action = Constants.REDO);
        am.addAction(createAction(action, Constants.REDO_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.REDO_IMAGE),
            Constants.REDO_DESCRIPTION, null, Constants.REDO_MNEMONIC, true,
            false));

        this.editorActions.add(null);
        this.editorActions.add(action = Constants.BOLD);
        am.addAction(createAction(action, Constants.BOLD_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.BOLD_IMAGE),
            Constants.BOLD_DESCRIPTION, null, Constants.BOLD_MNEMONIC, true,
            true));
        this.editorActions.add(action = Constants.ITALIC);
        am.addAction(createAction(action, Constants.ITALIC_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.ITALIC_IMAGE),
            Constants.ITALIC_DESCRIPTION, null, Constants.ITALIC_MNEMONIC, true,
            true));
        this.editorActions.add(action = Constants.UNDERLINE);
        am.addAction(createAction(action, Constants.UNDERLINE_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.UNDERLINE_IMAGE),
            Constants.UNDERLINE_DESCRIPTION, null, Constants.UNDERLINE_MNEMONIC,
            true, true));

        this.editorActions.add(null);
        this.editorActions.add(action = Constants.CUT);
        am.addAction(createAction(action, Constants.CUT_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.CUT_IMAGE),
            Constants.CUT_DESCRIPTION, null, Constants.CUT_MNEMONIC, false,
            true));
        this.editorActions.add(action = Constants.COPY);
        am.addAction(createAction(action, Constants.COPY_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.COPY_IMAGE),
            Constants.COPY_DESCRIPTION, null, Constants.COPY_MNEMONIC, false,
            true));
        this.editorActions.add(action = Constants.PASTE);
        am.addAction(createAction(action, Constants.PASTE_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.PASTE_IMAGE),
            Constants.PASTE_DESCRIPTION, null, Constants.PASTE_MNEMONIC, false,
            true));

        this.editorActions.add(null);
        this.editorActions.add(action = Constants.LEFT);
        am.addAction(createAction(action, Constants.LEFT_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.LEFT_IMAGE),
            Constants.LEFT_DESCRIPTION, Constants.PARAGRAPH,
            Constants.LEFT_MNEMONIC, true, true));
        this.editorActions.add(action = Constants.CENTER);
        am.addAction(createAction(action, Constants.CENTER_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.CENTER_IMAGE),
            Constants.CENTER_DESCRIPTION, Constants.PARAGRAPH,
            Constants.CENTER_MNEMONIC,true, true));
        this.editorActions.add(action = Constants.RIGHT);
        am.addAction(createAction(action, Constants.RIGHT_DESCRIPTION,
            getImagePath(Constants.IMAGE_TEXT, Constants.RIGHT_IMAGE),
            Constants.RIGHT_DESCRIPTION, Constants.PARAGRAPH,
            Constants.RIGHT_MNEMONIC, true, true));
        
        action = Constants.SELECT_ALL;
        am.addAction(createAction(action, Constants.SELECT_ALL_DESCRIPTION,
            null, Constants.SELECT_ALL_DESCRIPTION, null,
            Constants.SELECT_ALL_MNEMONIC, false, true));
        
        action = Constants.EDITOR;
        am.addAction(createAction(action, Constants.EDITOR_DESCRIPTION,
            getImagePath(Constants.IMAGE_GENERAL, Constants.EDITOR_IMAGE),
            Constants.EDITOR_DESCRIPTION, null, Constants.EDITOR_MNEMONIC,
            true, true, this, "setEditor"));
        
        this.colorActions = new ArrayList();
        
        this.colorActions.add(new StyledEditorKit.ForegroundAction("Red", Color.red));
        this.colorActions.add(new StyledEditorKit.ForegroundAction("Green", Color.green));
        this.colorActions.add(new StyledEditorKit.ForegroundAction("Blue", Color.blue));
        this.colorActions.add(new StyledEditorKit.ForegroundAction("Black", Color.black));
        
        return am;
    }

    private String getImagePath(String type, String name) {
        return Constants.IMAGE_PREFIX + type + Constants.FILE_SEPARATOR +
            name + Constants.IMAGE_SIZE + Constants.MIME_GIF;
    }

    private Component createRenderer() {
        JPanel p = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        p.setLayout(gb);
        p.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        this.renderer = new JXEditorPane();
        
        this.renderer.setContentType(Constants.TEXT_HTML);
        this.renderer.setPreferredSize(new Dimension(Constants.RENDERER_WIDTH,
            Constants.RENDERER_HEIGHT));
        this.renderer.setMinimumSize(this.renderer.getPreferredSize());
        this.renderer.setMargin(new Insets(2, 2, 2, 2));
        this.renderer.setEditable(false);

        this.rendererScroller = new JScrollPane(this.renderer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.rendererScroller.setMinimumSize(this.renderer.getPreferredSize());
        this.rendererScroller.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gb.setConstraints(this.rendererScroller, gbc);

        p.add(this.rendererScroller);
        
        

        return p;
    }

    private Component createEditor() {
        JPanel p = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        p.setLayout(gb);
        p.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        Component c = this.manager.getFactory().createToolBar(this.editorActions);

        ((JToolBar)c).setFloatable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0;
        gbc.weighty = 0;

        gb.setConstraints(c, gbc);

        p.add(c);

        this.editorScroller =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.editorScroller.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gb.setConstraints(this.editorScroller, gbc);

        p.add(this.editorScroller);

        JButton b = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                dispatch();
            }
        });

        b.setText(Constants.SEND_LABEL);

        gbc.gridx++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;

        gb.setConstraints(b, gbc);

        p.add(b);

        List l = new ArrayList(this.editorActions);

        l.add(null);
        l.add(Constants.SELECT_ALL);
        l.add(null);
        l.add(Constants.EDITOR);

        this.editorPopup = this.manager.getFactory().createPopup(l);
        JMenu cm = new JMenu("Color");

        for (Iterator ca = colorActions.iterator(); ca.hasNext(); ) {
            cm.add((Action)ca.next());
        }

        this.editorPopup.add(cm);

        return p;
    }

    private Component createStatus() {
        return this.status = new JXStatusBar();
    }
    
    private Action createAction(String id, String name, String icon,
        String desc, String group, String mnemonic, boolean toggle,
        boolean isEnabled) {
         return createAction(id, name, icon, desc, group, mnemonic, toggle,
            isEnabled, null, null);
    }

    private Action createAction(String id, String name, String icon,
        String desc, String group, String mnemonic, boolean toggle,
        boolean isEnabled, Object callBack, String handler) {
        AbstractAction a = createAction(id, name, mnemonic, toggle, group,
            callBack, handler);
         ImageIcon ii = icon != null ?
            new ImageIcon(getClass().getResource(icon)) : null;

        ActionFactory.decorateAction(a, desc, desc, ii, ii, null);

        a.setEnabled(isEnabled);

        return a;
    }

    private AbstractAction createAction(String id, String name,
        String mnemonic, boolean toggle, String group, Object callBack,
        String handler) {
        AbstractAction a = null;

        if (callBack == null ||
            handler == null) {
            a = ActionFactory.createTargetableAction(id, name, mnemonic,
                toggle, group);
        } else {
            a = ActionFactory.createBoundAction(id, name, mnemonic, toggle,
                group);

            ((BoundAction)a).registerCallback(callBack, handler);
        }

        return a;
    }
    
    private void setStatus(String s) {
        //this.statusetText(s);
    }
    
    private DropTargetListener createDropper() {
        DropTargetListener dtl = new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
            
            public void dragExit(DropTargetEvent dte) {
            }
            
            public void dragOver(DropTargetDragEvent dtde) {
            }
            
            public void drop(DropTargetDropEvent dtde) {
                Transferable t = dtde.getTransferable();
                Object d = null;
                
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    
                    try {
                        d =  t.getTransferData(DataFlavor.javaFileListFlavor);
                    } catch (UnsupportedFlavorException use) {
                    } catch (IOException ioe) {
                    }
                } else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    
                    try {
                        d = t.getTransferData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException use) {
                    } catch (IOException ioe) {
                    }
                }
                
                boolean isValid = false;
                
                if (d != null) {
                    isValid = handle(d);
                }

                dtde.dropComplete(isValid);
                
                if (! isValid) {
                    dtde.rejectDrop();
                }
            }
            
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }
            
            private boolean handle(Object d) {
                boolean isValid = true;
                String s = null;
                
                if (d != null) {
                    if (d instanceof Collection) {
                        for (Iterator i = ((Collection)d).iterator();
                            i.hasNext(); ) {
                            isValid = handle((String)i.next());
                        }
                    } else if (d instanceof String) {
                        s = process((String)d);

                        if (s != null) {
                            insertText(editor, s);
                        } else {
                            isValid = false;
                        }
                    }
                }
                
                return isValid;
            }
            
            private String process(String d) {
                String s = null;
                
                if (d != null &&
                    d.trim().length() > 0) {
                    URL u = null;

                    try {
                        u = new URL(d);
                    } catch (MalformedURLException mue) {
                    }

                    if (u != null) {
                        if (u.getProtocol().equalsIgnoreCase(Constants.PROTOCOL_FILE)) {
                            s = readFile(u);
                        } else if (u.getProtocol().equalsIgnoreCase(Constants.PROTOCOL_HTTP)) {
                            s = decorateHTML(u);
                        }
                    } else {
                        s = d;
                    }
                }
                
                return s;
            }
            
            private String readFile(URL u) {
                StringBuffer sb = null;
                
                try {
                    Object o = u.getContent();
                    Reader r = new BufferedReader(new InputStreamReader((InputStream)o));

                    for (int c = r.read(); c >= 0; c = r.read()) {
                        sb.append((char)c);
                    }
                } catch (IOException ioe) {
                }
                
                return sb != null ? sb.toString() : null;
            }
            
            private String decorateHTML(URL u) {
                String s = null;
                String t = u != null ? u.toString() : null;
                
                if (t != null) {
                    if (isImage(u)) {
                        s = Constants.IMAGE_PREAMBLE + t +
                            Constants.IMAGE_POSTAMBLE;
                    } else {
                        int i = t.indexOf(Constants.URI_DELIMITER);

                        s = Constants.ANCHOR_PREAMBLE_PREFIX + t +
                            Constants.ANCHOR_PREAMBLE_POSTFIX +
                            ((i >= 0) ?
                                t.substring(i + Constants.URI_DELIMITER.length()) : t) +
                            Constants.ANCHOR_POSTAMBLE;
                    }
                }
                
                return s;
            }
            
            private boolean isImage(URL u) {
                String s = u != null ? u.getFile().toLowerCase() : null;
                int i = s.lastIndexOf(Constants.DOT);
                String t = i > -1 ? s.substring(i) : null;

                return imageTypes.contains(t);
            }
        };
        
        return dtl;
    }

    private Point calculateScrollerPosition() {
        BoundedRangeModel m = this.rendererScroller.getVerticalScrollBar().getModel();

        return (m.getValue() + m.getExtent() < m.getMaximum()) ?
            this.rendererScroller.getViewport().getViewPosition() : null;
    }

    private void adjustScroller(Point p) {
        if (p != null) {
            this.rendererScroller.getViewport().setViewPosition(p);
        }
    }

    private boolean hasText(JXEditorPane ep) {
        boolean hasText = false;
        Document d = ep.getDocument();
        
        try {
            hasText = d.getText(0, d.getLength()).trim().length() > 0;
        } catch (BadLocationException ble) {
        }

        return hasText;
    }

    private String readText(JXEditorPane ep) {
        return readText(ep, 0, ep.getDocument().getLength());
    }

    private String readText(JXEditorPane ep, int start, int end) {
        Writer w = new StringWriter();

        try {
            ep.getEditorKit().write(w, ep.getDocument(), start, end);
        } catch (IOException ioe) {}
        catch (BadLocationException ble) {}

        return w.toString();
    }

    private void insertText(JXEditorPane ep, String s) {
        //insertText(ep, s, ep.getDocument().getLength());
        insertText(ep, s, ep.getCaretPosition());
    }

    private void insertText(JXEditorPane ep, String s, int index) {
        EditorKit ek = ep.getEditorKit();
        Document d = ep.getDocument();
        final Point p = calculateScrollerPosition();
        boolean isValid = false;

        try {
            //if (ek instanceof HTMLEditorKit) {
            //    ((HTMLEditorKit)ek).insertHTML((HTMLDocument)d, index, s, 0,
            //        0, null);
            //} else if (ek instanceof DefaultEditorKit) {
            if (ek instanceof DefaultEditorKit) {
                ((DefaultEditorKit)ek).read(new StringReader(s), d, index);
            }
            
            isValid = true;
        } catch (IOException ioe) {
        } catch (BadLocationException ble) {
        }
        
        if (isValid) {
            ep.setCaretPosition(Math.min(index + s.length(), d.getLength()));
            
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    adjustScroller(p);
                }
            });
        }
    }

    private String removeText(JXEditorPane ep) {
        return removeText(ep, 0, ep.getDocument().getLength());
    }

    private String removeText(JXEditorPane ep, int start, int end) {
        String s = readText(ep);

        try {
            ep.getDocument().remove(start, end);
        } catch (BadLocationException ble) {}

        return s;
    }
}