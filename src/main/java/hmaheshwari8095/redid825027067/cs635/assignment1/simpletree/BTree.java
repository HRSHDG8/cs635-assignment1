package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

import hmaheshwari8095.redid825027067.cs635.assignment1.printer.Printable;

import java.util.Stack;

/**
 * @param <T> create a tree of any Class that implements comparable
 */
public class BTree<T extends Comparable<T>> implements Tree<T> {
  private Node<T> root;
  private final int maximumValuesInNode;
  private final int order;
  private int size;

  public BTree(int order) {
    this.maximumValuesInNode = order - 1;
    this.order = order;
  }

  /**
   * this function adds a value and balances the BTree
   *
   * @param value adds a value of type {@link T} to the BTree.
   */
  @Override
  public void add(T value) {
    size++;
    //base condition to insert the very first value
    if (isNull(root)) {
      root = new Node<>(null, order);
      root.addValue(value);
    } else {
      Node<T> currentNode = root;
      while (!isNull(currentNode)) {
        if (checkAndInsertInCurrentNode(currentNode, value)) {
          break;
        }
        Node<T> leftNode = navigateLeft(currentNode, value);
        if (!isNull(leftNode)) {
          currentNode = leftNode;
          continue;
        }
        Node<T> rightNode = navigateRight(currentNode, value);
        if (!isNull(rightNode)) {
          currentNode = rightNode;
          continue;
        }
        currentNode = medianNode(value, currentNode);
      }
    }
  }

  private Node<T> navigateRight(Node<T> node, T value) {
        /*
        For BTree the highest possible value in a node is the last value
        And the highest possible child node is the right most node
        */
    int indexOfLastElement = node.getNoOfElementsInNode() - 1;
    Node<T> rightNode = null;
    T highestComparableValue = node.valueAtIndex(indexOfLastElement);
    if (value.compareTo(highestComparableValue) > 0) {
      rightNode = node.getChildAtIndex(node.getNoOfElementsInNode());
    }
    return rightNode;
  }

  private Node<T> navigateLeft(Node<T> node, T value) {
        /*
        For BTree the lowest possible value in a node is the first value
        And the lowest possible child node is the left most node
        */
    T lowestComparableValue = node.valueAtIndex(0);
    Node<T> leftNode = null;
    if (value.compareTo(lowestComparableValue) <= 0) {
      leftNode = node.getChildAtIndex(0);
    }
    return leftNode;
  }

  /**
   * @param currentNode    current node under traversal.
   * @param valueToBeAdded the value that needs to be added to the tree.
   * @return true if a value was added in current node, else returns false.
   */
  private boolean checkAndInsertInCurrentNode(Node<T> currentNode, T valueToBeAdded) {
    if (currentNode.getNoOfChildNodes() == 0) {
      currentNode.addValue(valueToBeAdded);
      if (currentNode.getNoOfElementsInNode() <= maximumValuesInNode) {
        return true;
      }
      splitAndBalance(currentNode);
      return true;
    }
    return false;
  }

  private Node<T> medianNode(T value, Node<T> parentNode) {
    Node<T> medianNode = null;
    for (int i = 1; i < parentNode.getNoOfElementsInNode(); i++) {
      T previousValue = parentNode.valueAtIndex(i - 1);
      T nextValue = parentNode.valueAtIndex(i);
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
  private void splitAndBalance(Node<T> nodeToSplit) {
    Node<T> nodeUndergoingSplit = nodeToSplit;
    int noOfElementsInNode = nodeUndergoingSplit.getNoOfElementsInNode();
    int medianIndex = noOfElementsInNode / 2;
    T medianValue = nodeUndergoingSplit.valueAtIndex(medianIndex);
    //start the split for the node from 0th index to the one before median for the values and child nodes
    int leftStartIndex = 0;
    int leftValueEndIndex = medianIndex - 1;
    int leftChildEndIndex = medianIndex;
    // Create a child node for the left section of the current node being spilt.
    Node<T> left = getNodeValuesAndChildrenInRange(nodeUndergoingSplit, leftStartIndex, leftValueEndIndex, leftChildEndIndex);
    //start the split for the node from (median + 1) index to the last value for the values and the last child nodes
    int rightStartIndex = medianIndex + 1;
    int rightValueEndIndex = noOfElementsInNode - 1;
    int rightChildEndIndex = nodeUndergoingSplit.getNoOfChildNodes() - 1;
    // Create a child node for the right section of the current node being spilt.
    Node<T> right = getNodeValuesAndChildrenInRange(nodeUndergoingSplit, rightStartIndex, rightValueEndIndex, rightChildEndIndex);
    // a new parent or root node must be created if a parent does not exist
    boolean shouldCreateNewRoot = isNull(nodeUndergoingSplit.getParent());
    if (shouldCreateNewRoot) {
      Node<T> newRoot = new Node<>(null, order);
      newRoot.addValue(medianValue);
      nodeUndergoingSplit.setParent(newRoot);
      //repoint BTrees' root to the newly created root node
      root = newRoot;
      nodeUndergoingSplit = root;
      nodeUndergoingSplit.addChild(left);
      nodeUndergoingSplit.addChild(right);
    } else {
      Node<T> parent = nodeUndergoingSplit.getParent();
      parent.addValue(medianValue);
      parent.removeChild(nodeUndergoingSplit);
      parent.addChild(left);
      parent.addChild(right);
      // if the parent overflows or is unbalanced, apply split and balance on it.
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
  private Node<T> getNodeValuesAndChildrenInRange(Node<T> nodeToBeSplit,
                                                  int startIndex,
                                                  int valueEndIndex,
                                                  int childEndIndex) {
    Node<T> splitNode = new Node<>(null, order);
    for (int i = startIndex; i <= valueEndIndex; i++) {
      splitNode.addValue(nodeToBeSplit.valueAtIndex(i));
    }
    if (nodeToBeSplit.getNoOfChildNodes() > 0) {
      for (int j = startIndex; j <= childEndIndex; j++) {
        Node<T> childNode = nodeToBeSplit.getChildAtIndex(j);
        splitNode.addChild(childNode);
      }
    }
    return splitNode;
  }

  /**
   * @param printable a custom implementation of the Printable interface.
   */
  @Override
  public void print(Printable<Node<T>> printable) {
    if (isNull(root)) {
      System.out.println("Tree is empty");
    } else {
      printable.print(root);
    }
  }

  /**
   * @param index the index (kth) element in the tree.
   * @return the T Object found at index, if out of bounds throws {@link IndexOutOfBoundsException}
   */
  @Override
  public T findElementByIndex(int index) throws IndexOutOfBoundsException {
    if (!isIndexOutOfBound(index)) {
      int currentTraversalIndex = -1;
      // Create a stack to maintain the current node under traversal and the index to start traversing the value from.
      Stack<NodeIndexEntry<T>> recursionStack = new Stack<>();
      //start from the root node and 0th index.
      recursionStack.push(new NodeIndexEntry<>(root, 0));
      while (!recursionStack.isEmpty()) {
        // start popping each stack element and traverse it until the stack is empty
        NodeIndexEntry<T> currentNodeIndex = recursionStack.pop();
        Node<T> currentNode = currentNodeIndex.getNode();
        int stackIndex = currentNodeIndex.getStartIndex();
        Node<T> child = currentNode.getChildAtIndex(stackIndex);
        if (isNull(child)) {
          //if the current node has no child node, check if of its element
          for (int i = 0; i < currentNode.getNoOfElementsInNode(); i++) {
            currentTraversalIndex++;
            if (index == currentTraversalIndex) {
              return currentNode.valueAtIndex(i);
            }
          }
        } else {
          if (stackIndex > 0) {
            currentTraversalIndex++;
            if (index == currentTraversalIndex) {
              return currentNode.valueAtIndex(stackIndex - 1);
            }
          }
          if (stackIndex < currentNode.getNoOfElementsInNode()) {
            recursionStack.push(new NodeIndexEntry<>(currentNode, stackIndex + 1));
          }
          recursionStack.push(new NodeIndexEntry<>(child, 0));
        }
      }
    }
    throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");

  }

  private boolean isIndexOutOfBound(int index) {
    return index >= size || index < 0;
  }

  /**
   * @return size of the all the {@link T} objects added in BTree.
   */
  @Override
  public int size() {
    return size;
  }

  private boolean isNull(Node<T> node) {
    return node == null;
  }

}
