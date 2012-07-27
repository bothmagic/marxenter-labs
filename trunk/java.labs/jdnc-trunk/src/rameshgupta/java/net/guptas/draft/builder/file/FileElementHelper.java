package net.guptas.draft.builder.file;

import java.io.File;
import net.guptas.draft.builder.TreeStructureElement;
import net.guptas.draft.builder.TreeNodeVisitor;

/**
 * A FileElementHelper is used by a FileTreeStructure to pull a TreeStructureElement for Files
 * as they are visited; the helper returns a single instance of a TreeStructureElement for
 * directories and for files, and delegates to the FileTreeNodeVisitor visitFile or visitDirectory
 * methods when calling accept(). The advantage of this is that new TreeStructureElements
 * are not called on each File as it is visited. 
 * 
 * <p>The usage is to create a single instance of the helper, then pull out the element
 * that will receive the call to accept()
 * <CODE>
 * // early in the code
 * fileElementHelper fileElementHelper = new fileElementHelper();
 * .
 * .
 * .
 * TreeStructureElement tse = fileElementHelper.getTreeStructureElement(file);
 * tse.accept(visitor, depth);
 * </CODE>
 * @author Patrick Wright
 */
public class FileElementHelper {
    FileFileElement singleFFElement;
    DirectoryFileElement singleDFElement;
    
    /**
     * Default constructor.
     */
    public FileElementHelper() {
        singleFFElement = new FileFileElement();
        singleDFElement = new DirectoryFileElement();    
    }
    
    /**
     * Returns a TreeStructureElement that will delegate to a FileTreeNodeVisitor to visit
     * files or directories; the file argument can be either.
     * @param f The file to visit.
     * @return A TreeStructureElement to visit the file; call accept() on this element.
     */
    public TreeStructureElement getTreeStructureElement(File f) {
        if ( f.isFile()) {
            singleFFElement.setFile(f);
            return singleFFElement;
        } else {
            singleDFElement.setFile(f);
            return singleDFElement;
        }
    }
    
    abstract class AbstractFileElement implements TreeStructureElement {
        File file;
        FileTreeNode fileNode;
        
        AbstractFileElement() {
            fileNode = new FileTreeNode();
        }

        public void setFile(File f) {
            file = f;
            fileNode.setFile(f);
        }
    }

    class DirectoryFileElement extends AbstractFileElement {
        DirectoryFileElement() {
            super();
            assert file.isDirectory();
        }

        public void accept(TreeNodeVisitor v, int depth) {
            assert v instanceof FileTreeNodeVisitor;
            FileTreeNodeVisitor ftnv = (FileTreeNodeVisitor)v;
            ftnv.visitDirectory(fileNode, depth);
        }
    }
    
    class FileFileElement extends AbstractFileElement {
        FileFileElement() {
            super();
            assert file.isFile();
        }

        public void accept(TreeNodeVisitor v, int depth) {
            assert v instanceof FileTreeNodeVisitor;
            FileTreeNodeVisitor ftnv = (FileTreeNodeVisitor)v;
            ftnv.visitFile(fileNode, depth);
        }
    }
}
