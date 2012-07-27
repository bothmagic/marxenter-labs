/*
 * $Id: Constants.java 135 2004-10-19 01:10:49Z gonzo $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.gonzo.editor;

import javax.swing.text.DefaultEditorKit;

class Constants {

    public static final String FILE_MENU = "file-menu";

    public static final String POPUP = "main-toolbar";

    public static final String UNDO = "undo";
    public static final String UNDO_DESCRIPTION = "Undo";
    public static final String UNDO_MNEMONIC = "U";
    public static final String UNDO_IMAGE = "Undo";
    public static final String REDO = "redo";
    public static final String REDO_DESCRIPTION = "Redo";
    public static final String REDO_MNEMONIC = "R";
    public static final String REDO_IMAGE = "Redo";
    public static final String CUT = DefaultEditorKit.cutAction;
    public static final String CUT_DESCRIPTION = "Cut to Clipboard";
    public static final String CUT_MNEMONIC = "C";
    public static final String CUT_IMAGE = "Cut";
    public static final String COPY = DefaultEditorKit.copyAction;
    public static final String COPY_DESCRIPTION = "Copy to Clipboard";
    public static final String COPY_MNEMONIC = "P";
    public static final String COPY_IMAGE = "Copy";
    public static final String PASTE = DefaultEditorKit.pasteAction;
    public static final String PASTE_DESCRIPTION = "Paste from Clipboard";
    public static final String PASTE_MNEMONIC = "T";
    public static final String PASTE_IMAGE = "Paste";
    public static final String BOLD = "font-bold";
    public static final String BOLD_DESCRIPTION = "Bold";
    public static final String BOLD_MNEMONIC = "B";
    public static final String BOLD_IMAGE = "Bold";
    public static final String ITALIC = "font-italic";
    public static final String ITALIC_DESCRIPTION = "Italic";
    public static final String ITALIC_MNEMONIC = "I";
    public static final String ITALIC_IMAGE = "Italic";
    public static final String UNDERLINE = "font-underline";
    public static final String UNDERLINE_DESCRIPTION = "Underline";
    public static final String UNDERLINE_MNEMONIC = "U";
    public static final String UNDERLINE_IMAGE = "Underline";
    public static final String LEFT = "left-justify";
    public static final String LEFT_DESCRIPTION = "Left Justify";
    public static final String LEFT_MNEMONIC = "L";
    public static final String LEFT_IMAGE = "AlignLeft";
    public static final String CENTER = "center-justify";
    public static final String CENTER_DESCRIPTION = "Center Justify";
    public static final String CENTER_MNEMONIC = "N";
    public static final String CENTER_IMAGE = "AlignCenter";
    public static final String RIGHT = "right-justify";
    public static final String RIGHT_DESCRIPTION = "Right Justify";
    public static final String RIGHT_MNEMONIC = "R";
    public static final String RIGHT_IMAGE = "AlignRight";
    public static final String PARAGRAPH = "paragraph-align";
    public static final String SELECT_ALL = "select-all";
    public static final String SELECT_ALL_DESCRIPTION = "Select All";
    public static final String SELECT_ALL_MNEMONIC = "A";
    public static final String EDITOR = "editor";
    public static final String EDITOR_DESCRIPTION = "Editor";
    public static final String EDITOR_MNEMONIC = "E";
    public static final String EDITOR_IMAGE = "Edit";
    public static final String BREAK = "insert-break";
    
    public static final String FILE_SEPARATOR = "/";
    public static final String IMAGE_PREFIX = FILE_SEPARATOR +
        "toolbarButtonGraphics" + FILE_SEPARATOR;
    public static final String IMAGE_GENERAL = "general";
    public static final String IMAGE_TEXT = "text";
    public static final String IMAGE_MEDIA = "media";
    //public static final String IMAGE_SIZE = 24;
    public static final int IMAGE_SIZE = 16;
    public static final String MIME_GIF = ".gif";
    
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    
    public static final int RENDERER_WIDTH = 250;
    public static final int RENDERER_HEIGHT = 250;

    public static final int EDITOR_WIDTH = RENDERER_WIDTH;
    public static final int EDITOR_HEIGHT = 40;
    
    public static final String EDITOR_HTML_LABEL = "HTML";
    public static final String EDITOR_TEXT_LABEL = "Text";
    
    public static final String SEND_LABEL = "Send";
    
    public static final String MIME_HTML = "text/html";
    public static final String MIME_PLAIN = "text/plain";
    
    public static final String PROTOCOL_FILE = "file";
    public static final String PROTOCOL_HTTP = "http";
    
    public static final String URI_DELIMITER = "://";
    public static final String ANCHOR_PREAMBLE_PREFIX = "<a href=\"";
    public static final String ANCHOR_PREAMBLE_POSTFIX = "\">";
    public static final String ANCHOR_POSTAMBLE = "</a>";
    public static final String IMAGE_PREAMBLE = "<img src=\"";
    public static final String IMAGE_POSTAMBLE = "\">";
    
    public static final String DOT = ".";
    
    public static final String IMAGE_TIFF = DOT + "tiff";
    public static final String IMAGE_TIF = DOT + "tif";
    public static final String IMAGE_JPEG = DOT + "jpeg";
    public static final String IMAGE_JPG = DOT + "jpg";
    public static final String IMAGE_GIF = DOT + "gif";
    public static final String IMAGE_PNG = DOT + "png";
}      
