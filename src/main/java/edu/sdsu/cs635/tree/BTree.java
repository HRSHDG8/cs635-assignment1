package edu.sdsu.cs635.tree;


import java.util.*;
import java.util.function.Consumer;


public class BTree<E> implements Tree<E> {
  private static final int DEFAULT_ORDER = 3;
  private final int maximumValuesInNode;
  private final int order;
  private final Comparator<E> valueComparator;
  private Node root;
  private int size;

  //Constructor Declarations

  @SuppressWarnings("unchecked")
  public BTree(int order) {
    this(order, (Comparator<E>) (Comparator.naturalOrder()));
  }

  public BTree() {
    this(DEFAULT_ORDER);
  }

  public BTree(int order, Comparator<E> valueComparator) {
    this.root = new NullNode();
    this.maximumValuesInNode = order - 1;
    this.order = order;
    this.valueComparator = Comparator.nullsLast(valueComparator);
  }

  //Set Api Methods

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return new InOrderTreeIterator();
  }

  private boolean isIndexOutOfBound(int index) {
    return index >= size || index < 0;
  }

  @Override
  public Object[] toArray() {
    Object[] array = new Object[size];
    int i = 0;
    for (E e : this) {
      array[i++] = e;
    }
    // makes sure a deep clone is returned so a change to array does not change the tree.
    return Arrays.copyOf(array, size);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) {
    if (a.length < size)
      // Make a new array of a's runtime type, but my contents:
      return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
    System.arraycopy(toArray(), 0, a, 0, size);
    if (a.length > size)
      a[size] = null;
    return a;
  }

  @Override
  public boolean add(E value) {
    assert value != null;
    increaseSize();
    //base condition to insert the very first value
    if (root.isNull()) {
      root = new BTreeNode(new NullNode(), value);
    } else {
      Node currentBTreeNode = root;
      while (!currentBTreeNode.isNull()) {
        if (checkAndInsertInCurrentNode(currentBTreeNode, value)) {
          break;
        }
        Node leftBTreeNode = navigateLeft(currentBTreeNode, value);
        if (!leftBTreeNode.isNull()) {
          currentBTreeNode = leftBTreeNode;
          continue;
        }
        Node rightBTreeNode = navigateRight(currentBTreeNode, value);
        if (!rightBTreeNode.isNull()) {
          currentBTreeNode = rightBTreeNode;
          continue;
        }
        currentBTreeNode = medianNode(value, currentBTreeNode);
      }
    }
    return true;
  }

  @Override
  public void forEach(Consumer<? super E> acceptor) {
    Iterator<E> reverseIterator = new ReverseOrderTreeIterator();
    while (reverseIterator.hasNext()) {
      acceptor.accept(reverseIterator.next());
    }
  }

  @Override
  public boolean remove(Object o) {
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    int oldSize = size();
    c.forEach(this::add);
    return oldSize + c.size() == size;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {
    size = 0;
    root = null;
  }

  //Sorted Set Api Methods
  @Override
  public Comparator<? super E> comparator() {
    return valueComparator;
  }

  @Override
  public SortedSet<E> subSet(E fromElement, E toElement) {
    return null;
  }

  @Override
  public SortedSet<E> headSet(E toElement) {
    return null;
  }

  @Override
  public SortedSet<E> tailSet(E fromElement) {
    return null;
  }

  @Override
  public E first() {
    return null;
  }

  @Override
  public E last() {
    return null;
  }

  //Tree Api Methods
  @Override
  public E get(int index) throws IndexOutOfBoundsException {
    if (!isIndexOutOfBound(index)) {
      for (E e : this) {
        if (index-- == 0) {
          return e;
        }
      }
    }
    throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
  }

  //private methods

  private void increaseSize() {
    this.size++;
  }

  private Node navigateRight(Node node, E value) {
    //For BTree the highest possible value in a node is the last value and the highest possible child node is the right most node
    int indexOfLastElement = node.size() - 1;
    Node rightBTreeNode = new NullNode();
    E highestComparableValue = node.get(indexOfLastElement);
    if (valueComparator.compare(value, highestComparableValue) > 0) {
      rightBTreeNode = node.getChild(node.size());
    }
    return rightBTreeNode;
  }

  private Node navigateLeft(Node node, E value) {
    //For BTree the lowest possible value in a node is the first value and the lowest possible child node is the left most node
    E lowestComparableValue = node.get(0);
    Node leftBTreeNode = new NullNode();
    if (valueComparator.compare(value, lowestComparableValue) <= 0) {
      leftBTreeNode = node.getChild(0);
    }
    return leftBTreeNode;
  }

  /**
   * @param currentNode    current node under traversal.
   * @param valueToBeAdded the value that needs to be added to the tree.
   * @return true if a value was added in current node, else returns false.
   */
  private boolean checkAndInsertInCurrentNode(Node currentNode, E valueToBeAdded) {
    if (currentNode.childrenSize() == 0) {
      currentNode.add(valueToBeAdded);
      if (currentNode.size() <= maximumValuesInNode) {
        return true;
      }
      splitAndBalance(currentNode);
      return true;
    }
    return false;
  }

  private Node medianNode(E value, Node parentNode) {
    Node medianBTreeNode = new NullNode();
    for (int i = 1; i < parentNode.size(); i++) {
      E previousValue = parentNode.get(i - 1);
      E nextValue = parentNode.get(i);
      if (valueComparator.compare(value, previousValue) > 0 && valueComparator.compare(value, nextValue) <= 0) {
        medianBTreeNode = parentNode.getChild(i);
        break;
      }
    }
    return medianBTreeNode;
  }

  /**
   * @param node split the current node and balance the tree
   */
  private void splitAndBalance(Node node) {
    Node nodeUnderSplit = node;
    int noOfElementsInNode = nodeUnderSplit.size();
    int medianIndex = noOfElementsInNode / 2;
    E medianValue = nodeUnderSplit.get(medianIndex);
    //start the split for the node from 0th index to the one before median for the values and child nodes
    int leftStartIndex = 0;
    int leftValueEndIndex = medianIndex - 1;
    int leftChildEndIndex = medianIndex;
    // Create a child node for the left section of the current node being spilt.
    Node left = getNodeValuesAndChildrenInRange(nodeUnderSplit, leftStartIndex, leftValueEndIndex, leftChildEndIndex);
    //start the split for the node from (median + 1) index to the last value for the values and the last child nodes
    int rightStartIndex = medianIndex + 1;
    int rightValueEndIndex = noOfElementsInNode - 1;
    int rightChildEndIndex = nodeUnderSplit.childrenSize() - 1;
    // Create a child node for the right section of the current node being spilt.
    Node right = getNodeValuesAndChildrenInRange(nodeUnderSplit, rightStartIndex, rightValueEndIndex, rightChildEndIndex);
    // a new parent or root node must be created if a parent does not exist
    boolean shouldCreateNewRoot = nodeUnderSplit.getParent().isNull();
    if (shouldCreateNewRoot) {
      BTreeNode newRoot = new BTreeNode(new NullNode(), medianValue);
      nodeUnderSplit.setParent(newRoot);
      //repoint BTrees' root to the newly created root node
      root = newRoot;
      nodeUnderSplit = root;
      nodeUnderSplit.addChild(left);
      nodeUnderSplit.addChild(right);
    } else {
      Node parent = nodeUnderSplit.getParent();
      parent.add(medianValue);
      parent.removeChild(nodeUnderSplit);
      parent.addChild(left);
      parent.addChild(right);
      // if the parent overflows or is unbalanced, apply split and balance on it.
      if (parent.size() > maximumValuesInNode) {
        splitAndBalance(parent);
      }
    }
  }

  /**
   * @param BTreeNodeToBeSplit the node undergoing spilt and balance
   * @param startIndex         the index to spilt values and child node from
   * @param valueEndIndex      the index upto which the value must be spilt
   * @param childEndIndex      the index upto which the child nodes must be split
   * @return a new node which can be attached to the parent.
   */
  private Node getNodeValuesAndChildrenInRange(Node BTreeNodeToBeSplit,
                                               int startIndex,
                                               int valueEndIndex,
                                               int childEndIndex) {
    Node splitBTreeNode = new BTreeNode(new NullNode());
    for (int i = startIndex; i <= valueEndIndex; i++) {
      splitBTreeNode.add(BTreeNodeToBeSplit.get(i));
    }
    if (BTreeNodeToBeSplit.childrenSize() > 0) {
      for (int j = startIndex; j <= childEndIndex; j++) {
        Node childBTreeNode = BTreeNodeToBeSplit.getChild(j);
        splitBTreeNode.addChild(childBTreeNode);
      }
    }
    return splitBTreeNode;
  }

  //TreeIterator Abstraction, has common code for iterator and reverse iterator

  abstract class TreeIterator implements Iterator<E> {
    int cursor;       // index of next element to return
    int expectedModCount;
    Stack<NodeIndexEntry> recursionStack;

    final void checkForCoModification() {
      if (size != expectedModCount)
        throw new ConcurrentModificationException();
    }

    class NodeIndexEntry {
      private final Node node;
      private final int startIndex;

      protected NodeIndexEntry(Node node, int startIndex) {
        this.node = node;
        this.startIndex = startIndex;
      }
    }
  }

  class InOrderTreeIterator extends TreeIterator {

    public InOrderTreeIterator() {
      // Create a stack to maintain the current node under traversal and the index to start traversing the value from.
      cursor = 0;
      recursionStack = new Stack<>();
      expectedModCount = size;
      //start from the root node and 0th index.
      recursionStack.push(new NodeIndexEntry(root, 0));
    }

    @Override
    public boolean hasNext() {
      return cursor < size;
    }

    @Override
    public E next() {
      checkForCoModification();
      while (!recursionStack.isEmpty()) {
        NodeIndexEntry entry = recursionStack.pop();
        Node currentNode = entry.node;
        int currentIndex = entry.startIndex;
        if (currentNode.isLeaf()) {
          E value = currentNode.get(currentIndex);
          cursor++;
          if (currentIndex < currentNode.size() - 1) {
            recursionStack.push(new NodeIndexEntry(currentNode, currentIndex + 1));
          }
          return value;
        } else {
          E value = null;
          if (currentIndex > 0) {
            value = currentNode.get(currentIndex - 1);
          }
          if (currentIndex < currentNode.size()) {
            recursionStack.push(new NodeIndexEntry(currentNode, currentIndex + 1));
          }
          Node child = currentNode.getChild(currentIndex);
          recursionStack.push(new NodeIndexEntry(child, 0));
          if (value != null) {
            cursor++;
            return value;
          }
        }
      }
      throw new IndexOutOfBoundsException("No more elements in tree, check hasNext before calling next");
    }
  }

  class ReverseOrderTreeIterator extends TreeIterator {

    public ReverseOrderTreeIterator() {
      // Create a stack to maintain the current node under traversal and the index to start traversing the value from.
      recursionStack = new Stack<>();
      expectedModCount = size;
      cursor = size - 1;
      //start from the root node and 0th index.
      recursionStack.push(new NodeIndexEntry(root, root.size() - 1));
    }

    @Override
    public boolean hasNext() {
      return cursor >= 0;
    }

    @Override
    public E next() {
      checkForCoModification();
      while (!recursionStack.isEmpty()) {
        NodeIndexEntry entry = recursionStack.pop();
        Node currentNode = entry.node;
        int currentIndex = entry.startIndex;
        if (currentNode.isLeaf()) {
          E value = currentNode.get(currentIndex);
          cursor--;
          if (currentIndex > 0) {
            recursionStack.push(new NodeIndexEntry(currentNode, currentIndex - 1));
          }
          return value;
        } else {
          E value = null;
          if (currentIndex < currentNode.size() - 1) {
            value = currentNode.get(currentIndex + 1);
          }
          if (currentIndex >= 0) {
            recursionStack.push(new NodeIndexEntry(currentNode, currentIndex - 1));
          }
          Node child = currentNode.getChild(currentIndex + 1);
          recursionStack.push(new NodeIndexEntry(child, child.size() - 1));
          if (value != null) {
            cursor--;
            return value;
          }
        }
      }
      throw new IndexOutOfBoundsException("No more elements in tree, check hasNext before calling next");
    }
  }

  abstract class Node {

    List<E> values;
    List<Node> children;
    Comparator<Node> childNodeComparator;
    Node parent;
    int size;
    int childrenSize;

    abstract Node getParent();

    abstract void setParent(BTreeNode parent);

    abstract E get(int index);

    abstract boolean add(E value);

    abstract Node getChild(int index);

    abstract boolean addChild(Node child);

    abstract void removeChild(Node child);

    abstract boolean isNull();

    /**
     * @return no of non-null elements in current node
     */
    int size() {
      return size;
    }

    /**
     * @return the no of non-null child nodes in current node
     */
    int childrenSize() {
      return childrenSize;
    }

    boolean isLeaf() {
      return childrenSize == 0;
    }
  }

  /**
   * A node for the {@link BTree} class
   * Each node holds a reference to its parent
   * A list of values it currently holds, and its count
   * A list of pointers to the child nodes, and its count
   */
  private class BTreeNode extends Node {

    // access is private, so that it can only be accessed in the BTree Class
    private BTreeNode(Node parent, E value) {
      this(parent);
      this.add(value);
    }

    private BTreeNode(Node parent) {
      this.parent = parent;
      // the list is initialized with order and not order - 1, to accommodate the extra value that would be needed to process splitAndBalance
      // only order - 1 element will be present in the node
      this.values = new ArrayList<>(order);
      this.children = new ArrayList<>(order + 1);
      for (int i = 0; i < order; i++) {
        children.add(new NullNode());
        values.add(null);
      }
      children.add(new NullNode());
      this.size = 0;
      this.childrenSize = 0;
      //sort child nodes by the comparable value of first element of each child node
      this.childNodeComparator = Comparator.nullsLast((o1, o2) -> valueComparator.compare(o1.get(0), o2.get(0)));
    }

    Node getParent() {
      return parent;
    }

    /**
     * @param parent sets a parent node to the current node
     */
    void setParent(BTreeNode parent) {
      this.parent = parent;
    }

    /**
     * Api to get a value from an index without having to expose the entire "values" data structure.
     *
     * @param index integer value to retrieve value from
     * @return a {@link E} at index in the current node.
     */
    E get(int index) {
      return values.get(index);
    }

    /**
     * adds {@link E} objects to "values" List, increments the noOfElementsInNode
     * and sorts them in lexicographical order keeping nulls at last
     *
     * @param value a {@link E} object to be added to the current node
     */
    boolean add(E value) {
      E isSet = values.set(size++, value);
      values.sort(valueComparator);
      return isSet != null;
    }

    /**
     * @param index int value to retrieve node from
     * @return a child node at index
     */
    Node getChild(int index) {
      return children.get(index);
    }

    /**
     * adds a {@link BTreeNode} to children List, increments the noOfChildNodes
     * and sorts the T object in the comparable order keeping nulls at last
     *
     * @param child a child {@link BTreeNode} to be added to the current node
     */
    boolean addChild(Node child) {
      child.parent = this;
      Node setValue = children.set(childrenSize++, child);
      children.sort(childNodeComparator);
      return setValue != null;
    }

    /**
     * @param child removes child {@link BTreeNode}, within the current node only, after spilt and balance happens
     */
    void removeChild(Node child) {
      boolean childNodeFound = false;
      if (isLeaf()) {
        return;
      }
      for (int i = 0; i < childrenSize; i++) {
        if (children.get(i).equals(child)) {
          childNodeFound = true;
        } else if (childNodeFound) {
          children.set(i - 1, children.get(i));
        }
      }
      if (childNodeFound) {
        children.set(--childrenSize, new NullNode());
      }
    }

    @Override
    boolean isNull() {
      return false;
    }
  }

  private class NullNode extends Node {
    @Override
    Node getParent() {
      return new NullNode();
    }

    @Override
    void setParent(BTreeNode parent) {

    }

    @Override
    E get(int index) {
      return null;
    }

    @Override
    boolean add(E value) {
      return false;
    }

    @Override
    Node getChild(int index) {
      return null;
    }

    @Override
    boolean addChild(Node child) {
      return false;
    }

    @Override
    void removeChild(Node child) {

    }

    @Override
    boolean isNull() {
      return true;
    }
  }
}
