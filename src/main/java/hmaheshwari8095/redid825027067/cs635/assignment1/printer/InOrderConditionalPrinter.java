package hmaheshwari8095.redid825027067.cs635.assignment1.printer;

import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An In Order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that is applied if the respective predicate holds true.
 */
public class InOrderConditionalPrinter<T extends Comparable<T>> implements Printable<Node<T>> {
  private final Predicate<T> testCondition;
  private final Consumer<T> dynamicAcceptor;

  public InOrderConditionalPrinter(Predicate<T> testCondition,
                                   Consumer<T> dynamicAcceptor) {
    this.testCondition = testCondition;
    this.dynamicAcceptor = dynamicAcceptor;
  }

  @Override
  public void print(Node<T> treeNodeToBePrinted) {
    inOrder(treeNodeToBePrinted);
  }

  /**
   * Recursively calls inOrder until end of tree
   *
   * @param currentNode the node under recursion to act inOrder logic on
   */
  private void inOrder(Node<T> currentNode) {
    for (int i = 0; i < currentNode.getNoOfElementsInNode(); i++) {
      //start from the left most child until you reach the leaf node.
      Node<T> child = currentNode.getChildAtIndex(i);
      if (child != null) {
        inOrder(child);
      }
      /*once you reach the leaf check predicate and accept the print
      function on currentValue*/
      T currentValue = currentNode.valueAtIndex(i);
      if (testCondition.test(currentValue)) {
        dynamicAcceptor.accept(currentValue);
      }

    }
    //for every non leaf node, call inOrder for the last Child Node of the
    //current Node
    if (currentNode.getNoOfChildNodes() != 0) {
      Node<T> lastNode = currentNode
         .getChildAtIndex(currentNode.getNoOfChildNodes() - 1);
      if (lastNode != null) {
        inOrder(lastNode);
      }
    }
  }
}
