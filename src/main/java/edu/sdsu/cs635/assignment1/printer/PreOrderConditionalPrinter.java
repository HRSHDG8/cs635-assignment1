package edu.sdsu.cs635.assignment1.printer;

import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.simpletree.Node;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A Pre order traversal of {@link Node} class,
 * A predicate to test a node value
 * A consumer that would be applied if the above predicate holds true.
 */
public class PreOrderConditionalPrinter implements Printable<Node> {
    private final Predicate<Student> testCondition;
    private final Consumer<Student> studentPrintValue;

    public PreOrderConditionalPrinter(Predicate<Student> testCondition, Consumer<Student> studentPrintValue) {
        this.testCondition = testCondition;
        this.studentPrintValue = studentPrintValue;
    }

    @Override
    public void print(Node treeNodeToBePrinted) {
        preOrder(treeNodeToBePrinted);
    }

    /**
     * Recursively calls preOrder until end of tree
     *
     * @param currentNode the node under recursion to act the preOrder logic on
     */
    private void preOrder(Node currentNode) {
        for (int i = 0; i < currentNode.getNoOfElementsInNode(); i++) {
            Node child = currentNode.getChildAtIndex(i);
            if (child != null) {
                preOrder(child);
            }
            Student s = currentNode.valueAtIndex(i);
            if (testCondition.test(s)) {
                studentPrintValue.accept(s);
            }

        }
        if (currentNode.getNoOfChildNodes() != 0) {
            Node lastNode = currentNode.getChildAtIndex(currentNode.getNoOfChildNodes() - 1);

            if (lastNode != null) {
                preOrder(lastNode);
            }
        }
    }
}
