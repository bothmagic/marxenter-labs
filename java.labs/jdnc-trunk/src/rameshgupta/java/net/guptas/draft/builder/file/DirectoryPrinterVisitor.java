package net.guptas.draft.builder.file;



/**
 * A Visitor over FileTreeNodes that pretty-prints the file's name. This is taken
 * roughly from Allen Holub's Visitor pattern example in his book Holub on Patterns.
 *
 * @author Patrick Wright
 */
public class DirectoryPrinterVisitor extends AbstractFileTreeNodeVisitor {
    public void visitDirectory(FileTreeNode fileNode, int depth) {
        while ( --depth >= 0 ) {
            System.out.print("..");
        }
        System.out.println(fileNode);        
    }

    public void visitFile(FileTreeNode fileNode, int depth) {
        // do nothing
    }
}