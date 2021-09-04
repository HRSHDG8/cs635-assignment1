package edu.sdsu.cs635.assignment1.printer;

import edu.sdsu.cs635.assignment1.simpletree.Node;

/**
 * Used to print the tree by DFS Left to Right Approach
 */
public class DebuggingPrinter implements Printable<Node> {
    /**
     * A debug printer that utilizes each nodes' print method to print the tree node and its children for debugging purpose.
     *
     * @param treeNodeToBePrinted {@link Node} object from where print needs to begin, typically the root of the tree
     */
    @Override
    public void print(Node treeNodeToBePrinted) {
        treeNodeToBePrinted.print("");
    }
}
