package net.guptas.draft.builder.file;

import java.io.File;
import java.io.FileFilter;
import net.guptas.draft.builder.*;

/**
 * <p>A FileTreeStructure represents a directory/file structure which can be navigated
 * from top down recursively, implementing the Visitor pattern; this class represents
 * the Object Structure in the pattern. See Allen Holub's Holub on Patterns book for a good
 * discussion. For a file structure, it might be better to use a Hierarchical Visitor Pattern
 * (see http://c2.com/cgi/wiki?HierarchicalVisitorPattern) to better control which files in
 * the directory are visited. 
 * 
 * <p>The FileTreeStructure allows a FileFilter to be set to control
 * which files are visited (or skipped). Unfortunately, this filter really works for directories,
 * as skipping files themselves may result in empty nodes. For example, if you wanted to
 * build a tree for directories containing Java source files, you might want the traversal to
 * say, "include any Java source files and their parent directories, plus the ancestor directories
 * leading up to the root." This construct is not possible using this FileTreeStructure; you can
 * exclude non-Java source files using the FileFilter, but you can't prevent empty directories from
 * being added; directories will always be added, they just might be empty. However, as this is
 * a sample of the process, you can try filtering on directories instead (e.g. skip directories named
 * "CVS"), which does work. By default no filtering is performed.
 * 
 * <p>To use a FileTreeStructure, you need several components available, principally a TreeStructure.Visitor
 * implementation. As an example, to print out all directories, you could use the DirectoryPrinterVisitor
 * (taken and adapted from Holub's examples):
 * <code>
 * FileTreeStructure d = new FileTreeStructure(new File("/usr/patrick"));
 *   d.traverse(new DirectoryPrinterVisitor());
 * </code>
 * 
 * <p>To build a MutableTreeModel, you can use a DirectoryTNBuilder
 * <code>
 * File rootDirectory = new File("d:/java/javanet/xhtmlrenderer");
 * 
 * // create our MutableTreeModel adding the root automatically
 * MutableTreeModel<File,?> model = createTreeModel((File) rootDirectory, null);
 * 
 * // optional: skip directories named "CVS"
 * FileFilter filter = new FileFilter() {
 *    public boolean accept(File file) {
 *        return file.isFile() || ( file.isDirectory() && ! file.getName().endsWith("CVS"));
 *    }
 * };
 * 
 * // Use our visitor--the DirectoryTNBuilder--to build the tree
 * FileTreeStructure d = new FileTreeStructure(rootDirectory, filter);
 * DirectoryTNBuilder builder = new DirectoryTNBuilder(model);
 * d.traverse(builder);
 * 
 * // and display it
 * JFrame  frame = new JFrame("Generic Tree Model");
 * frame.add(new JScrollPane(new JTree(model)));
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * frame.pack();
 * frame.setSize(400, 600);
 * frame.setVisible(true);
 * </code>
 * 
 * <p><strong>Thread Safety:</strong> instances of this class are thread-safe locking on the
 * traverse method (but this is not tested :)). For efficiency, we reuse FileElementHelper and 
 * FileTreeNode elements instead of creating one for each File we visit.
 * @author Patrick Wright
 */
public class FileTreeStructure implements TreeStructure {
    /** Our local lock object for sync. */
    private Object lock = new Object();
    
    /**
     * Our root directory.
     */
    private File root;
    /**
     * A single instance of the utility class to get the right TreeStructureElement
     * to visit. We use this single instance and use getTreeStructureElement() to determine
     * the right TSE to execute against.
     */
    private FileElementHelper fileElementHelper;
    
    /**
     * The filter we apply to files we visit.
     */
    private FileFilter fileFilter;
    
    /**
     * A FileFilter that allows any file at all.
     */
    private static final FileFilter ALL_FILES = new FileFilter() {
        public boolean accept(File f) { return true; }
    };
    

    /**
     * Constructor for a given root directory, all files traversed.
     * @param root Root directory.
     */
    public FileTreeStructure(File root) {
        this(root, ALL_FILES);
    } 
    
    /**
     * Constructor for a given root directory, file list is filtered by the FileFilter.
     * @param root Root directory.
     * @param filter FileFilter to control which files are visited; see class comments.
     */
    public FileTreeStructure(File root, FileFilter filter) {
        this.root = root.getAbsoluteFile();
        this.fileElementHelper = new FileElementHelper();
        this.fileFilter = filter;
    }    
    
    // traverses all the elements in this directory and subdirectories
    /**
     * Kicks off the traversal of the tree, starting at root and working down to leaves; synchronized
     * internally.
     * @param visitor The TreeStructure.Visitor implementation that will visitNode() for each file--the 
     * TreeNode in this case will be a FileTreeNode.
     */
    public void traverse(TreeNodeVisitor visitor) {
        synchronized (lock) {
            recurse( root, visitor, 0 );
        }
    }
    
    // recursive method for traversing from file
    /**
     * The local recursive method to traverse the tree.
     * @param file The file to recurse on.
     * @param visitor The Visitor to visit the file.
     * @param depth Current depth in the tree.
     */
    private void recurse(File file, TreeNodeVisitor visitor, int depth) {
        TreeStructureElement tse = fileElementHelper.getTreeStructureElement(file);
        tse.accept(visitor, depth);
        if ( ! file.isFile()) {
            File[] children = file.listFiles(fileFilter);
            for ( File child : children ) {
                recurse(child, visitor, depth + 1);
            }
        }
    }
    
    public static void main(String[] args) {
        FileTreeStructure d = new FileTreeStructure(new File("d:/java/javanet/xhtmlrenderer/src"));
        d.traverse(new DirectoryPrinterVisitor());
    }
}
