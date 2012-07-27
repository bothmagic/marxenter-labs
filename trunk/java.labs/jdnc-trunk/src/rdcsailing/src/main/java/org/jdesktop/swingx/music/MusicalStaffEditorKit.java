package org.jdesktop.swingx.music;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * MusicalStaffEditorKit
 * @author Ryan Cuprak
 */
public class MusicalStaffEditorKit extends StyledEditorKit {

    private static final ViewFactory styledEditorKitFactory = (new StyledEditorKit()).getViewFactory();

    private static final ViewFactory defaultFactory = new MusicalStaffViewFactory();

    /**
     * Return a copy
     * @return copy
     */
    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    @Override
    public Object clone() {
        return new MusicalStaffEditorKit();
    }

    /**
     * MusicalStaffViewFactory
     */
    public static class MusicalStaffViewFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String elementName = elem.getName();
            if(elementName != null) {
               // if(elementName.equals(AbstractDocument.ContentElementName)) {
                 //   return new Staff(elem);
                 if(elementName.equals(AbstractDocument.ParagraphElementName)) {
                    return new MusicalParagraph(elem);
                }
            }

            return styledEditorKitFactory.create(elem);
        }
    }

    /**
     * Returns the default factory
     * @return default factory
     */
    @Override
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }
}
