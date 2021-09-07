package hmaheshwari8095.redid825027067.cs635.assignment1.printer;

import hmaheshwari8095.redid825027067.cs635.assignment1.model.Student;
import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An In Order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that is applied if the respective predicate holds true.
 */
public class InOrderConditionalPrinter implements Printable<Node> {
  private final Predicate<Student> testCondition;
  private final Consumer<Student> studentPrintValue;

  public InOrderConditionalPrinter(Predicate<Student> testCondition,
                                   Consumer<Student> studentPrintValue) {
    this.testCondition = testCondition;
    this.studentPrintValue = studentPrintValue;
  }

  @Override
  public void print(Node treeNodeToBePrinted) {
    inOrder(treeNodeToBePrinted);
  }

  /**
   * Recursively calls inOrder until end of tree
   *
   * @param currentNode the node under recursion to act inOrder logic on
   */
  private void inOrder(Node currentNode) {
    for (int i = 0; i < currentNode.getNoOfElementsInNode(); i++) {
      //start from the left most child until you reach the leaf node.
      Node child = currentNode.getChildAtIndex(i);
      if (child != null) {
        inOrder(child);
      }
      /*once you reach the leaf check predicate and accept the print
      function on currentStudent*/
      Student currentStudent = currentNode.valueAtIndex(i);
      if (testCondition.test(currentStudent)) {
        studentPrintValue.accept(currentStudent);
      }

    }
    //for every non leaf node, call inOrder for the last Child Node of the
    //current Node
    if (currentNode.getNoOfChildNodes() != 0) {
      Node lastNode = currentNode
         .getChildAtIndex(currentNode.getNoOfChildNodes() - 1);
      if (lastNode != null) {
        inOrder(lastNode);
      }
    }
  }
}
