package hmaheshwari8095.redid825027067.cs635.assignment1.printer;

import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Node;

/**
 * Used to print the tree by DFS Left to Right Approach
 */
public class DebuggingPrinter<T extends Comparable<T>> implements Printable<Node<T>> {
  /**
   * A debug printer that utilizes each nodes' print method to print the
   * tree node and its children for debugging purpose.
   *
   * @param treeNodeToBePrinted {@link Node} object from where print
   *                     needs to begin, typically the root of the tree
   */
  @Override
  public void print(Node<T> treeNodeToBePrinted) {
    //Start printing the tree with an empty prefix
    treeNodeToBePrinted.print("");
  }
}
