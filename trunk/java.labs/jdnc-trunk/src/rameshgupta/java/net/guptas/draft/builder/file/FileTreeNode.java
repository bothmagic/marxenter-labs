package net.guptas.draft.builder.file;

import java.io.File;
import net.guptas.draft.builder.TreeNode;

/**
 * A TreeNode representing a single file. The current File may be swapped out
 * by using setFile(), as long as thread safety is taken into consideration.
 *
 * @author Patrick Wright
 */
public class FileTreeNode implements TreeNode {
    private File file;
    
    public FileTreeNode() {}
    
    public FileTreeNode(File file) {
        this.file = file;
    }
    
    public void setFile(File f) {
        file = f;
    }    
    
    public File getFile() {
        return file;
    }    

    public String toString() {
        return file.getName();
    }
}