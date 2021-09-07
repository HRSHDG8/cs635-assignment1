package edu.sdsu.cs635.assignment1.printer;

import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A Reverse Order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that would be applied if the respective predicate holds true.
 */
public class ReverseOrderConditionalPrinter implements Printable<Node> {

    private final Predicate<Student> testCondition;
    private final Consumer<Student> studentPrintValue;

    public ReverseOrderConditionalPrinter(Predicate<Student> testCondition, Consumer<Student> studentPrintValue) {
        this.testCondition = testCondition;
        this.studentPrintValue = studentPrintValue;
    }

    @Override
    public void print(Node treeNodeToBePrinted) {
        reverseOrder(treeNodeToBePrinted);
    }

    /**
     * Recursively calls reverseOrder until end of tree
     *
     * @param currentNode the node under recursion to call the reverseOrder logic on
     */
    private void reverseOrder(Node currentNode) {
        for (int i = currentNode.getNoOfElementsInNode() - 1; i >= 0; i--) {
            //start from the right most child until you reach the leaf node.
            Node child = currentNode.getChildAtIndex(i + 1);
            if (child != null) {
                reverseOrder(child);
            }
            //once you reach the leaf check predicate and accept the print function on currentStudent
            Student currentStudent = currentNode.valueAtIndex(i);
            if (testCondition.test(currentStudent)) {
                studentPrintValue.accept(currentStudent);
            }

        }
        //for every non leaf node, call reverseOrder for the first Child Node of the current Node
        if (currentNode.getNoOfChildNodes() != 0) {
            Node firstNode = currentNode.getChildAtIndex(0);
            if (firstNode != null) {
                reverseOrder(firstNode);
            }
        }
    }
}
