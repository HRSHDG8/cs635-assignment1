package edu.sdsu.cs635.tree;


import java.util.*;
import java.util.function.Consumer;


public class BTree<E> implements Collection<E> {
  private static final int DEFAULT_ORDER = 3;
  private final int maximumValuesInNode;
  private final int order;
  private final Comparator<E> valueComparator;
  protected transient int modCount = 0;
  private BTreeNode root;
  private int size;

  @SuppressWarnings("unchecked")
  public BTree(int order) {
    this(order, (Comparator<E>) Comparator.nullsLast(Comparator.naturalOrder()));
  }

  public BTree() {
    this(DEFAULT_ORDER);
  }

  public BTree(int order, Comparator<E> valueComparator) {
    this.maximumValuesInNode = order - 1;
    this.order = order;
    this.valueComparator = valueComparator;
  }

  private void increaseSize() {
    this.size++;
    this.modCount++;
  }

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
    return new Object[0];
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return null;
  }

  @Override
  public boolean add(E value) {
    increaseSize();
    //base condition to insert the very first value
    if (isNull(root)) {
      root = new BTreeNode(null);
      root.add(value);
    } else {
      BTreeNode currentBTreeNode = root;
      while (!isNull(currentBTreeNode)) {
        if (checkAndInsertInCurrentNode(currentBTreeNode, value)) {
          break;
        }
        BTreeNode leftBTreeNode = navigateLeft(currentBTreeNode, value);
        if (!isNull(leftBTreeNode)) {
          currentBTreeNode = leftBTreeNode;
          continue;
        }
        BTreeNode rightBTreeNode = navigateRight(currentBTreeNode, value);
        if (!isNull(rightBTreeNode)) {
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

  private BTreeNode navigateRight(BTreeNode BTreeNode, E value) {
        /*
        For BTree the highest possible value in a node is the last value
        And the highest possible child node is the right most node
        */
    int indexOfLastElement = BTreeNode.size() - 1;
    BTreeNode rightBTreeNode = null;
    E highestComparableValue = BTreeNode.get(indexOfLastElement);
    if (valueComparator.compare(value, highestComparableValue) > 0) {
      rightBTreeNode = BTreeNode.getChild(BTreeNode.size());
    }
    return rightBTreeNode;
  }

  private BTreeNode navigateLeft(BTreeNode BTreeNode, E value) {
        /*
        For BTree the lowest possible value in a node is the first value
        And the lowest possible child node is the left most node
        */
    E lowestComparableValue = BTreeNode.get(0);
    BTreeNode leftBTreeNode = null;
    if (valueComparator.compare(value, lowestComparableValue) <= 0) {
      leftBTreeNode = BTreeNode.getChild(0);
    }
    return leftBTreeNode;
  }

  /**
   * @param currentBTreeNode current node under traversal.
   * @param valueToBeAdded   the value that needs to be added to the tree.
   * @return true if a value was added in current node, else returns false.
   */
  private boolean checkAndInsertInCurrentNode(BTreeNode currentBTreeNode, E valueToBeAdded) {
    if (currentBTreeNode.childrenSize() == 0) {
      currentBTreeNode.add(valueToBeAdded);
      if (currentBTreeNode.size() <= maximumValuesInNode) {
        return true;
      }
      splitAndBalance(currentBTreeNode);
      return true;
    }
    return false;
  }

  private BTreeNode medianNode(E value, BTreeNode parentBTreeNode) {
    BTreeNode medianBTreeNode = null;
    for (int i = 1; i < parentBTreeNode.size(); i++) {
      E previousValue = parentBTreeNode.get(i - 1);
      E nextValue = parentBTreeNode.get(i);
      if (valueComparator.compare(value, previousValue) > 0 && valueComparator.compare(value, nextValue) <= 0) {
        medianBTreeNode = parentBTreeNode.getChild(i);
        break;
      }
    }
    return medianBTreeNode;
  }

  /**
   * @param bTreeNode split the current node and balance the tree
   */
  private void splitAndBalance(BTreeNode bTreeNode) {
    BTreeNode nodeUnderSplit = bTreeNode;
    int noOfElementsInNode = nodeUnderSplit.size();
    int medianIndex = noOfElementsInNode / 2;
    E medianValue = nodeUnderSplit.get(medianIndex);
    //start the split for the node from 0th index to the one before median for the values and child nodes
    int leftStartIndex = 0;
    int leftValueEndIndex = medianIndex - 1;
    int leftChildEndIndex = medianIndex;
    // Create a child node for the left section of the current node being spilt.
    BTreeNode left = getNodeValuesAndChildrenInRange(nodeUnderSplit, leftStartIndex, leftValueEndIndex, leftChildEndIndex);
    //start the split for the node from (median + 1) index to the last value for the values and the last child nodes
    int rightStartIndex = medianIndex + 1;
    int rightValueEndIndex = noOfElementsInNode - 1;
    int rightChildEndIndex = nodeUnderSplit.childrenSize() - 1;
    // Create a child node for the right section of the current node being spilt.
    BTreeNode right = getNodeValuesAndChildrenInRange(nodeUnderSplit, rightStartIndex, rightValueEndIndex, rightChildEndIndex);
    // a new parent or root node must be created if a parent does not exist
    boolean shouldCreateNewRoot = isNull(nodeUnderSplit.getParent());
    if (shouldCreateNewRoot) {
      BTreeNode newRoot = new BTreeNode(null);
      newRoot.add(medianValue);
      nodeUnderSplit.setParent(newRoot);
      //repoint BTrees' root to the newly created root node
      root = newRoot;
      nodeUnderSplit = root;
      nodeUnderSplit.addChild(left);
      nodeUnderSplit.addChild(right);
    } else {
      BTreeNode parent = nodeUnderSplit.getParent();
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
  private BTreeNode getNodeValuesAndChildrenInRange(BTreeNode BTreeNodeToBeSplit,
                                                    int startIndex,
                                                    int valueEndIndex,
                                                    int childEndIndex) {
    BTreeNode splitBTreeNode = new BTreeNode(null);
    for (int i = startIndex; i <= valueEndIndex; i++) {
      splitBTreeNode.add(BTreeNodeToBeSplit.get(i));
    }
    if (BTreeNodeToBeSplit.childrenSize() > 0) {
      for (int j = startIndex; j <= childEndIndex; j++) {
        BTreeNode childBTreeNode = BTreeNodeToBeSplit.getChild(j);
        splitBTreeNode.addChild(childBTreeNode);
      }
    }
    return splitBTreeNode;
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
    return false;
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

  private boolean isNull(BTreeNode BTreeNode) {
    return BTreeNode == null;
  }

  abstract class TreeIterator implements Iterator<E> {
    int cursor;       // index of next element to return
    int expectedModCount;
    Stack<NodeIndexEntry> recursionStack;

    final void checkForComodification() {
      if (size != expectedModCount)
        throw new ConcurrentModificationException();
    }

    class NodeIndexEntry {
      private final BTreeNode node;
      private final int startIndex;

      protected NodeIndexEntry(BTreeNode node, int startIndex) {
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
      checkForComodification();
      while (!recursionStack.isEmpty()) {
        NodeIndexEntry entry = recursionStack.pop();
        BTreeNode currentNode = entry.node;
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
          BTreeNode child = currentNode.getChild(currentIndex);
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

  // TODO Test this out and revisit entire logic coz you suck bro;
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
      checkForComodification();
      while (!recursionStack.isEmpty()) {
        NodeIndexEntry entry = recursionStack.pop();
        BTreeNode currentNode = entry.node;
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
          if (currentIndex >= currentNode.size()) {
            recursionStack.push(new NodeIndexEntry(currentNode, currentIndex - 1));
          }
          BTreeNode child = currentNode.getChild(currentIndex);
          recursionStack.push(new NodeIndexEntry(child, currentNode.childrenSize() - 1));
          if (value != null) {
            cursor--;
            return value;
          }
        }
      }
      throw new IndexOutOfBoundsException("No more elements in tree, check hasNext before calling next");
    }
  }

  /**
   * A node for the {@link BTree} class
   * Each node holds a reference to its parent
   * A list of values it currently holds, and its count
   * A list of pointers to the child nodes, and its count
   */
  private class BTreeNode {
    private final List<E> values;
    private final List<BTreeNode> children;
    private final Comparator<BTreeNode> childNodeComparator;
    private BTreeNode parent;
    private int size;
    private int childrenSize;

    // access is protected, so that it can only be accessed in the same sub package
    private BTreeNode(BTreeNode parent) {
      this.parent = parent;
      // the list is initialized with order and not order - 1, to accommodate the extra value that would be needed to process splitAndBalance
      // only order - 1 element will be present in the node
      this.values = new ArrayList<>(order);
      this.children = new ArrayList<>(order + 1);
      for (int i = 0; i < order; i++) {
        children.add(null);
        values.add(null);
      }
      children.add(null);
      this.size = 0;
      this.childrenSize = 0;
      //sort child nodes by the comparable value of first element of each child node
      this.childNodeComparator = Comparator.nullsLast((o1, o2) -> valueComparator.compare(o1.get(0), o2.get(0)));
    }

    private BTreeNode getParent() {
      return parent;
    }

    /**
     * @param parent sets a parent node to the current node
     */
    private void setParent(BTreeNode parent) {
      this.parent = parent;
    }

    private boolean isLeaf() {
      return childrenSize == 0;
    }

    /**
     * Api to get a value from an index without having to expose the entire "values" data structure.
     *
     * @param index integer value to retrieve value from
     * @return a {@link E} at index in the current node.
     */
    private E get(int index) {
      return values.get(index);
    }

    /**
     * adds {@link E} objects to "values" List, increments the noOfElementsInNode
     * and sorts them in lexicographical order keeping nulls at last
     *
     * @param value a {@link E} object to be added to the current node
     */
    private void add(E value) {
      values.set(size++, value);
      values.sort(valueComparator);
    }

    /**
     * @param index int value to retrieve node from
     * @return a child node at index
     */
    private BTreeNode getChild(int index) {
      return children.get(index);
    }

    /**
     * adds a {@link BTreeNode} to children List, increments the noOfChildNodes
     * and sorts the T object in the comparable order keeping nulls at last
     *
     * @param child a child {@link BTreeNode} to be added to the current node
     */
    private void addChild(BTreeNode child) {
      child.parent = this;
      children.set(childrenSize++, child);
      children.sort(childNodeComparator);
    }

    /**
     * @param child removes child {@link BTreeNode}, within the current node only, after spilt and balance happens
     */
    private void removeChild(BTreeNode child) {
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
        children.set(--childrenSize, null);
      }
    }

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
  }
}
