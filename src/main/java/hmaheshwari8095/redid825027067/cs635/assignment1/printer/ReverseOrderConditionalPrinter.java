package hmaheshwari8095.redid825027067.cs635.assignment1.printer;

import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A Reverse Order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that is applied if the respective predicate holds true.
 */
public class ReverseOrderConditionalPrinter<T extends Comparable<T>> implements Printable<Node<T>> {

  private final Predicate<T> testCondition;
  private final Consumer<T> dynamicAcceptor;

  public ReverseOrderConditionalPrinter(Predicate<T> testCondition,
                                        Consumer<T> dynamicAcceptor) {
    this.testCondition = testCondition;
    this.dynamicAcceptor = dynamicAcceptor;
  }

  @Override
  public void print(Node<T> treeNodeToBePrinted) {
    reverseOrder(treeNodeToBePrinted);
  }

  /**
   * Recursively calls reverseOrder until end of tree
   *
   * @param currentNode the node under recursion to call the
   *                    reverseOrder logic on
   */
  private void reverseOrder(Node<T> currentNode) {
    for (int i = currentNode.getNoOfElementsInNode() - 1; i >= 0; i--) {
      //start from the right most child until you reach the leaf node.
      Node<T> child = currentNode.getChildAtIndex(i + 1);
      if (child != null) {
        reverseOrder(child);
      }
      //once you reach the leaf check predicate and accept the print
      //function on currentValue
      T currentValue = currentNode.valueAtIndex(i);
      if (testCondition.test(currentValue)) {
        dynamicAcceptor.accept(currentValue);
      }

    }
    //for every non leaf node, call reverseOrder for the first Child Node
    //of the current Node
    if (currentNode.getNoOfChildNodes() != 0) {
      Node<T> firstNode = currentNode.getChildAtIndex(0);
      if (firstNode != null) {
        reverseOrder(firstNode);
      }
    }
  }
}
