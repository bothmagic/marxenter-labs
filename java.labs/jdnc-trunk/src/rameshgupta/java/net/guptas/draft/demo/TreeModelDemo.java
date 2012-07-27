/**
 * Copyright (C) 2005 by Ramesh Gupta. All rights reserved.
 */

package net.guptas.draft.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import net.guptas.draft.MutableTreeModel;
import net.guptas.draft.MutableTreeNode;
import net.guptas.draft.impl.DefaultMutableTreeModel;	// use in factory method
import net.guptas.draft.impl.DefaultMutableTreeNode;	// use in factory method


/**
 * This is a simple demo showing how to construct a tree model using an extended
 * tree model interface that adds support for mutability and Generics. As the
 * package name indicates, this is a very rough draft, and none of this should
 * be used in any production code in its present state.
 *
 * To insulate application code from implementation details, all access to model
 * and nodes is through interfaces. Only the factory methods used to instantiate
 * models and nodes make direct references to the implementation classes.
 *
 * To minimize need for subclassing, a node may optionally be accessorized with a
 * user-specified object to hold domain-specific information, and an additional
 * user-data object to hold other arbitrary information for the node. In a tree
 * that models a file system, for example, each node could be accessorized with
 * the File object that the node represents. Additional information, if any, may
 * be conveniently attached to the node using a user-data object without having
 * to wrap the domain-specific object just to carry the additional information.
 *
 * The example itself uses a perennially favorite tree data source, namely, Java
 * class information, discovered through reflection.
 *
 * In this example, the root node represents an arbitrary Java class. The root's
 * descendants include nodes that represent all publicly declared constructors,
 * methods, and inner classes for the class that the root node represents. These
 * descendants are grouped by their type. Thus, constructors, methods, and inner
 * classes are grouped under separate nodes. A group node is present if and only
 * if there is at least one node that can be put under that node. In other words,
 * group nodes are never empty.
 *
 * Nodes representing constructors have a child node for each argument to the
 * constructor, where the child node represents the class of that argument.
 * Similarly, nodes representing methods have a child for each argument to the
 * method, where the child node represents the class of that argument. Nodes
 * representing inner classes, if any, have the same structure as the node
 * representing the outer class, thus giving the tree its recursive structure.
 *
 * Since the tree contains nodes that model different types of domain objects,
 * we accessorize each node with a specialized domain object as the node is
 * created. A node representing a Java class is accessorized with the Class object
 * for that class. Similarly, a node representing a constructor is accessorized
 * with a Constructor object, and a node representing a method is accessorized
 * with a Method object. Grouping nodes that aggregate constructors, methods,
 * and inner classes don’t have any domain-specific accessories. Instead, we
 * accessorize them with simple String labels identifying their role.
 *
 * The example, although contrived, can easily be adapted for more realistic
 * scenarios, as it touches upon all the key points. In particular, it shows how
 * little code is required to create a tree model without subclassing either the
 * default model class or the default node class. The code is, in fact, shorter
 * than the description of the example!
 *
 * @author Ramesh Gupta
 */
public class TreeModelDemo {
	/**
	 * Main entry point constructs and displays a sample tree in which the root
	 * node contains information for the JTree class (for no particular reason).
	 *
	 * @param args String[]
	 */
	public static void main(String[] args) {
		MutableTreeModel<Class,?>	model = createTreeModel((Class) JTree.class, null);
		populate(model.getRoot(), model);	// populate the model recursively

		JFrame  frame = new JFrame("Generic Tree Model");
		frame.add(new JScrollPane(new JTree(model)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Factory method constructs a MutableTreeModel whose root node is
	 * accessorized with the specified accessory and user data objects.
	 * This the only method that refers directly to the model implementation class.
	 *
	 * @param accessory N arbitrary accessory object for the root node
	 * @param userData X arbitrary user data object for the root node
	 * @return MutableTreeModel the newly created model
	 */
	private static <N,X> MutableTreeModel<N,X> createTreeModel(N accessory, X userData) {
		return new DefaultMutableTreeModel<N,X>(createTreeNode(accessory, userData));
	}

	/**
	 * Factory method constructs a MutableTreeNode that is accessorized with the
	 * specified accessory and user data objects.
	 * This the only method that refers directly to the node implementation class.
	 *
	 * @param accessory N arbitrary accessory object for the new node
	 * @param userData X arbitrary user data object for the new node
	 * @return MutableTreeNode the newly created node
	 */
	private static <N,X> MutableTreeNode<N,X> createTreeNode(N accessory, X userData) {
		return new DefaultMutableTreeNode<N,X>(accessory, userData);
	}

	/**
	 * Recursively populates the specified node in the specified tree.
	 *
	 * @param node MutableTreeNode the node to populate
	 * @param tree MutableTreeModel the tree in which the node resides
	 */
	private static void populate(MutableTreeNode<Class,?> node, MutableTreeModel tree) {
		Class			klass = node.getAccessory();

		Constructor[]	constructors = klass.getConstructors();
		if (constructors.length > 0) {	// skip if there aren't any constructors
			MutableTreeNode<String,?> group = add("Constructors", null, node, tree);
			for (Constructor constructor : constructors) {
				MutableTreeNode<Constructor,?> newNode = add(constructor, null, group, tree);
				for (Class paramType : constructor.getParameterTypes()) {
					add(paramType, null, newNode, tree);
				}
			}
		}

		Method[]		methods = klass.getMethods();
		if (methods.length > 0) {	// skip if there aren't any methods
			MutableTreeNode<String,?> group = add("Methods", null, node, tree);
			for (Method method : methods) {
				MutableTreeNode<Method,?> newNode = add(method, null, group, tree);
				for (Class paramType : method.getParameterTypes()) {
					add(paramType, null, newNode, tree);
				}
			}
		}

		Class[]			classes = klass.getClasses();
		if (classes.length > 0) {	// skip if there aren't any inner classes
			MutableTreeNode<String,?> group = add("Inner Classes", null, node, tree);
			for (Class innerClass : classes) {
				MutableTreeNode<Class,?> newNode = add(innerClass, null, group, tree);
				populate(newNode, tree);	// drill down recursively
			}
		}
	}

	/**
	 * Creates a new MutableTreeNode with the specified accessory and user data
	 * objects, adds it to the specified parent node in the specified tree, and
	 * returns the newly created node.
	 *
	 * @param accessory N arbitrary accessory object for the new node
	 * @param userData X arbitrary user data object for the new node
	 * @param parent MutableTreeNode parent to which the new node is added
	 * @param tree MutableTreeModel model in which the parent resides
	 * @return MutableTreeNode the newly created node
	 */
	private static <N,X> MutableTreeNode<N,X> add(N accessory, X userData,
		MutableTreeNode parent, MutableTreeModel tree) {
		MutableTreeNode<N,X>	newNode = createTreeNode(accessory, userData);
		tree.add(newNode, parent, parent.getChildCount());
		return newNode;
	}
    
    /*
     *
     * 
     file.listFiles(fileFilter)
     *
     *
     */
}
