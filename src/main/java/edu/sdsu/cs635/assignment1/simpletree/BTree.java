package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.model.NodeIndexEntry;
import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.printer.Printable;

import java.util.Stack;

/**
 * Btree implementing the Tree interface specifically for the Student type.
 */
public class BTree implements Tree<Student> {

    private Node root;
    private final int maximumValuesInNode;
    private final int order;
    private int size;

    public BTree(int order) {
        this.maximumValuesInNode = order - 1;
        this.order = order;
    }

    /**
     * this function adds a value and balances the b-tree
     *
     * @param value adds a value of type Student to the Tree.
     */
    @Override
    public void add(Student value) {
        size++;
        //base condition to insert the very first value
        if (isNull(root)) {
            root = new Node(null, order);
            root.addValue(value);
        } else {
            Node node = root;
            while (!isNull(node)) {
                if (checkAndInsertInCurrentNode(node, value)) {
                    break;
                }
                Node leftNode = navigateLeft(node, value);
                if (!isNull(leftNode)) {
                    node = leftNode;
                    continue;
                }
                Node rightNode = navigateRight(node, value);
                if (!isNull(rightNode)) {
                    node = rightNode;
                    continue;
                }
                node = medianNode(value, node);
            }
        }
    }

    private Node navigateRight(Node node, Student value) {
        int indexOfLastElementInNode = node.getNoOfElementsInNode() - 1;
        Node rightNode = null;
        Student highestComparableValue = node.valueAtIndex(indexOfLastElementInNode);
        if (value.compareTo(highestComparableValue) > 0) {
            rightNode = node.getChildAtIndex(node.getNoOfElementsInNode());
        }
        return rightNode;
    }

    private Node navigateLeft(Node node, Student value) {
        Student lowestComparableValue = node.valueAtIndex(0);
        Node leftNode = null;
        if (value.compareTo(lowestComparableValue) <= 0) {
            leftNode = node.getChildAtIndex(0);
        }
        return leftNode;
    }

    /**
     * @param parentNode     current node under traversal.
     * @param valueToBeAdded the value that needs to be added to the tree.
     * @return true if a value was added in current node, else returns false.
     */
    private boolean checkAndInsertInCurrentNode(Node parentNode, Student valueToBeAdded) {
        if (parentNode.getNoOfChildNodes() == 0) {
            parentNode.addValue(valueToBeAdded);
            if (parentNode.getNoOfElementsInNode() <= maximumValuesInNode) {
                return true;
            }
            splitAndBalance(parentNode);
            return true;
        }
        return false;
    }

    private Node medianNode(Student value, Node parentNode) {
        Node medianNode = null;
        for (int i = 1; i < parentNode.getNoOfElementsInNode(); i++) {
            Student previousValue = parentNode.valueAtIndex(i - 1);
            Student nextValue = parentNode.valueAtIndex(i);
            if (value.compareTo(previousValue) > 0 && value.compareTo(nextValue) <= 0) {
                medianNode = parentNode.getChildAtIndex(i);
                break;
            }
        }
        return medianNode;
    }

    /**
     * @param nodeToSplit split the current node and balance the tree
     */
    private void splitAndBalance(Node nodeToSplit) {
        Node underSplit = nodeToSplit;
        int noOfElementsInNode = underSplit.getNoOfElementsInNode();
        int medianIndex = noOfElementsInNode / 2;
        Student medianValue = underSplit.valueAtIndex(medianIndex);
        int leftStartIndex = 0;
        int leftValueEndIndex = medianIndex - 1;
        int leftChildEndIndex = medianIndex;
        Node left = getNodeValuesAndChildrenInRange(underSplit, leftStartIndex, leftValueEndIndex, leftChildEndIndex);
        int rightStartIndex = medianIndex + 1;
        int rightValueEndIndex = noOfElementsInNode - 1;
        int rightChildEndIndex = underSplit.getNoOfChildNodes() - 1;
        Node right = getNodeValuesAndChildrenInRange(underSplit, rightStartIndex, rightValueEndIndex, rightChildEndIndex);
        // a new parent or root node must be created if a parent does not exist
        boolean shouldCreateNewRoot = isNull(underSplit.getParent());
        if (shouldCreateNewRoot) {
            Node newRoot = new Node(null, order);
            newRoot.addValue(medianValue);
            underSplit.setParent(newRoot);
            root = newRoot;
            underSplit = root;
            underSplit.addChild(left);
            underSplit.addChild(right);
        } else {
            Node parent = underSplit.getParent();
            parent.addValue(medianValue);
            parent.removeChild(underSplit);
            parent.addChild(left);
            parent.addChild(right);
            if (parent.getNoOfElementsInNode() > maximumValuesInNode) {
                splitAndBalance(parent);
            }
        }
    }

    /**
     * @param nodeToBeSplit the node undergoing spilt and balance
     * @param startIndex    the index to spilt values and child node from
     * @param valueEndIndex the index upto which the value must be spilt
     * @param childEndIndex the index upto which the child nodes must be split
     * @return a new node which can be attached to the parent.
     */
    private Node getNodeValuesAndChildrenInRange(Node nodeToBeSplit, int startIndex, int valueEndIndex, int childEndIndex) {
        Node splitNode = new Node(null, order);
        for (int i = startIndex; i <= valueEndIndex; i++) {
            splitNode.addValue(nodeToBeSplit.valueAtIndex(i));
        }
        if (nodeToBeSplit.getNoOfChildNodes() > 0) {
            for (int j = startIndex; j <= childEndIndex; j++) {
                Node childNode = nodeToBeSplit.getChildAtIndex(j);
                splitNode.addChild(childNode);
            }
        }
        return splitNode;
    }

    /**
     * @param printable a custom implementation of the Printable interface.
     */
    @Override
    public void print(Printable<Node> printable) {
        if (isNull(root)) {
            System.out.println("Tree is empty");
        } else {
            printable.print(root);
        }
    }

    /**
     * @param index the index (kth) element in the tree.
     * @return the student found at index, if out of bounds throws {@link IndexOutOfBoundsException}
     */
    @Override
    public Student findElementByIndex(int index) {
        if (!isIndexOutOfBound(index)) {
            int current = -1;
            Stack<NodeIndexEntry> recursionStack = new Stack<>();
            recursionStack.push(new NodeIndexEntry(root, 0));
            while (!recursionStack.isEmpty()) {
                NodeIndexEntry currentNodeIndex = recursionStack.pop();
                Node currentNode = currentNodeIndex.getNode();
                int stackIndex = currentNodeIndex.getStartIndex();
                Node child = currentNode.getChildAtIndex(stackIndex);
                if (isNull(child)) {
                    for (int i = 0; i < currentNode.getNoOfElementsInNode(); i++) {
                        current++;
                        if (index == current) {
                            return currentNode.valueAtIndex(i);
                        }
                    }
                } else {
                    if (stackIndex > 0) {
                        current++;
                        if (index == current) {
                            return currentNode.valueAtIndex(stackIndex - 1);
                        }
                    }
                    if (stackIndex < currentNode.getNoOfElementsInNode()) {
                        recursionStack.push(new NodeIndexEntry(currentNode, stackIndex + 1));
                    }
                    recursionStack.push(new NodeIndexEntry(child, 0));
                }
            }
        }
        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");

    }

    private boolean isIndexOutOfBound(int index) {
        return index >= size || index < 0;
    }

    /**
     * @return size of the elements in b-tree.
     */
    @Override
    public int size() {
        return size;
    }

    private boolean isNull(Node node) {
        return node == null;
    }

}
