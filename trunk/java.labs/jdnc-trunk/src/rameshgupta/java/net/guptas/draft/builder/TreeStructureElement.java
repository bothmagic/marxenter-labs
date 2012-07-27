package net.guptas.draft.builder;



/**
 * Reprensents the Element in the Visitor pattern. The element is called once for 
 * each TreeNode in the TreeStructure, and passes in the Visitor who then visits
 * the TreeNode. Some implementations may not create a new TreeStructureElement for
 * each TreeNode, as long as thread safety is preserved.
 *
 * @author Patrick Wright
 */
public interface TreeStructureElement {
    void accept(TreeNodeVisitor v, int depth);
}
