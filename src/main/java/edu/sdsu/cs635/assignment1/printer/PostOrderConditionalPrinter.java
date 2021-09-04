package edu.sdsu.cs635.assignment1.printer;

import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A post order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that would be applied if the above predicate holds true.
 */
public class PostOrderConditionalPrinter implements Printable<Node> {

    private final Predicate<Student> testCondition;
    private final Consumer<Student> studentPrintValue;

    public PostOrderConditionalPrinter(Predicate<Student> testCondition, Consumer<Student> studentPrintValue) {
        this.testCondition = testCondition;
        this.studentPrintValue = studentPrintValue;
    }

    @Override
    public void print(Node treeNodeToBePrinted) {
        postOrder(treeNodeToBePrinted);
    }

    /**
     * Recursively calls postOrder until end of tree
     *
     * @param currentNode the node under recursion to call the postOrder logic on
     */
    private void postOrder(Node currentNode) {
        for (int i = currentNode.getNoOfElementsInNode() - 1; i >= 0; i--) {
            Node child = currentNode.getChildAtIndex(i + 1);
            if (child != null) {
                postOrder(child);
            }
            Student s = currentNode.valueAtIndex(i);
            if (testCondition.test(s)) {
                studentPrintValue.accept(s);
            }

        }
        if (currentNode.getNoOfChildNodes() != 0) {
            Node firstNode = currentNode.getChildAtIndex(0);

            if (firstNode != null) {
                postOrder(firstNode);
            }
        }
    }
}
