package edu.sdsu.cs635.aoop.tree;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author HMac 825027067
 * Implementation of {@link SortedSetTree} which extends {@link SortedSet} from {@link Collection} framework
 * The Default order of the tree is 3 and the default comparison strategy is of Natural Order (From the {@link Comparator} framewrok)
 * Any class that implements {@link Comparable} can create a {@link BTree} without passing a comparison Strategy.
 */
public class BTree<E> implements SortedSetTree<E> {
  private static final int DEFAULT_ORDER = 3;
  private final int maximumValuesInNode;
  private final int order;
  private final Comparator<E> comparisonStrategy;
  private Node root;
  private int size;

  //Constructor Declarations
  public BTree() {
    this(DEFAULT_ORDER);
  }

  @SuppressWarnings("unchecked")
  public BTree(int order) {
    this(order, (Comparator<E>) (Comparator.naturalOrder()));
  }

  public BTree(Comparator<E> comparator) {
    this(DEFAULT_ORDER, comparator);
  }

  public BTree(int order, Comparator<E> strategy) {
    this.root = new NullNode();
    this.maximumValuesInNode = order - 1;
    this.order = order;
    // comparing with the strategy passed by API consumer and make it null tolerant with Comparator.nullsLast
    this.comparisonStrategy = Comparator.nullsLast(strategy);
  }
  //End Constructor Declarations

  //java.util.Set Api Methods
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
    for (E e : this) {
      if (e.equals(o)) {
        return true;
      }
    }
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
  public String toString() {
    // use the nodes toString method to bank on the toString Logic for the entire Tree
    return root.toString();
  }

  @Override
  public boolean add(E value) {
    assert value != null;
    increaseSize();
    //base condition to insert the very first value
    //null object pattern helps here to avoid == null checks
    if (root.isNull()) {
      // as per null object pattern pass a NullNode as parent of root, rather than null.
      root = new DataNode(new NullNode(), value);
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
    reverseOrder(root, acceptor);
  }

  @Override
  public boolean remove(Object o) {
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    boolean containsValue = true;
    for (Object a : c) {
      containsValue &= contains(a);
    }
    return containsValue;
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
    root = new NullNode();
  }
  //End java.util.Set API Methods

  //java.util.SortedSet Api Methods
  @Override
  public Comparator<? super E> comparator() {
    return comparisonStrategy;
  }

  @Override
  public SortedSet<E> subSet(E fromElement, E toElement) {
    return Collections.emptySortedSet();
  }

  @Override
  public SortedSet<E> headSet(E toElement) {
    return Collections.emptySortedSet();
  }

  @Override
  public SortedSet<E> tailSet(E fromElement) {
    return Collections.emptySortedSet();
  }

  @Override
  public E first() {
    Iterator<E> inOrderIterator = new InOrderTreeIterator();
    if (inOrderIterator.hasNext()) {
      return inOrderIterator.next();
    } else {
      return null;
    }
  }

  @Override
  public E last() {
    E last = null;
    for (E element : this) {
      last = element;
    }
    return last;
  }

  //end java.util.SortedSet Api Methods

  //edu.sdsu.cs635.SortedSetTree Api Methods

  @Override
  public E get(int index) throws IndexOutOfBoundsException {
    int counter = 0;
    if (!isIndexOutOfBound(index)) {
      for (E e : this) {
        if (counter++ == index) {
          return e;
        }
      }
    }
    throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
  }

  //end edu.sdsu.cs635.SortedSetTree Api Methods

  //private methods

  private void increaseSize() {
    this.size++;
  }

  private Node navigateRight(Node node, E value) {
    //For BTree the highest possible value in a node is the last value and the highest possible child node is the right most node
    int indexOfLastElement = node.size() - 1;
    Node rightBTreeNode = new NullNode();
    E highestComparableValue = node.get(indexOfLastElement);
    if (comparisonStrategy.compare(value, highestComparableValue) > 0) {
      rightBTreeNode = node.getChild(node.size());
    }
    return rightBTreeNode;
  }

  private Node navigateLeft(Node node, E value) {
    //For BTree the lowest possible value in a node is the first value and the lowest possible child node is the left most node
    E lowestComparableValue = node.get(0);
    Node leftBTreeNode = new NullNode();
    if (comparisonStrategy.compare(value, lowestComparableValue) <= 0) {
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
      if (comparisonStrategy.compare(value, previousValue) > 0 && comparisonStrategy.compare(value, nextValue) <= 0) {
        medianBTreeNode = parentNode.getChild(i);
        break;
      }
    }
    return medianBTreeNode;
  }

  /**
   * This method creates a new node and attaches it to the tree rather than shifting values.
   * The strategy is to create a median node as parent,
   * a left node for all left values and child nodes,
   * a right node for all right values and child nodes,
   * Then insert the median node at appropriate position, the old overflow node is Garbage collected.
   *
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
      Node newRoot = new DataNode(new NullNode(), medianValue);
      nodeUnderSplit.setParent(newRoot);
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
   * @param nodeToBeSplit the node undergoing spilt and balance
   * @param startIndex    the index to spilt values and child node from
   * @param valueEndIndex the index upto which the value must be spilt
   * @param childEndIndex the index upto which the child nodes must be split
   * @return a new node which can be attached to the parent.
   */
  private Node getNodeValuesAndChildrenInRange(Node nodeToBeSplit, int startIndex, int valueEndIndex, int childEndIndex) {
    Node replacementNode = new DataNode(new NullNode());
    for (int i = startIndex; i <= valueEndIndex; i++) {
      replacementNode.add(nodeToBeSplit.get(i));
    }
    if (nodeToBeSplit.childrenSize() > 0) {
      for (int j = startIndex; j <= childEndIndex; j++) {
        Node childBTreeNode = nodeToBeSplit.getChild(j);
        replacementNode.addChild(childBTreeNode);
      }
    }
    return replacementNode;
  }


  /**
   * Internal iterator implementation to support forEach Method to iterate over the {@link BTree} in reverse order.
   *
   * @param currentNode is the node under recursion
   * @param acceptor    is the {@link Consumer} passed by the client
   */
  private void reverseOrder(Node currentNode, Consumer<? super E> acceptor) {
    for (int i = currentNode.size() - 1; i >= 0; i--) {
      //start from the right most child until you reach the leaf node.
      Node child = currentNode.getChild(i + 1);
      if (child != null) {
        reverseOrder(child, acceptor);
      }
      E value = currentNode.get(i);
      acceptor.accept(value);

    }
    //for every non leaf node, call reverseOrder for the first Child Node of the current Node
    if (!currentNode.isLeaf()) {
      Node firstNode = currentNode.getChild(0);
      if (firstNode != null) {
        reverseOrder(firstNode, acceptor);
      }
    }
  }

  //end private methods

  /**
   * Inorder implementation to support iterator interface on the {@link BTree} class.
   * The implementation banks on A stack that has the current Node under traversal and what element of the node is to be accessed next.
   */
  class InOrderTreeIterator implements Iterator<E> {
    // index of next element to return
    int cursor;
    // This field is to keep a copy of the expected modification count of the BTree in this case its the size.
    // the logic works with tree since we don't have a remove function and tree size can not decrease.
    // implement modCount logic on tree in next release and use that to check for modification
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
      while (!recursionStack.isEmpty() && !isEmpty()) {
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

  /**
   * Abstraction for a tree node, provides common methods and fields for underlying implementation to use
   */
  abstract class Node {
    List<E> values;
    List<Node> children;
    Comparator<Node> childNodeComparator;
    Node parent;
    int size;
    int childrenSize;

    abstract Node getParent();

    abstract void setParent(Node parent);

    abstract E get(int index);

    abstract boolean add(E value);

    abstract Node getChild(int index);

    abstract boolean addChild(Node child);

    abstract boolean removeChild(Node child);

    abstract boolean isNull();

    abstract String toString(String prefix);

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
  private class DataNode extends Node {

    // access is private, so that it can only be accessed in the BTree Class
    private DataNode(Node parent, E value) {
      this(parent);
      this.add(value);
    }

    private DataNode(Node parent) {
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
      this.childNodeComparator = Comparator.nullsLast((o1, o2) -> comparisonStrategy.compare(o1.get(0), o2.get(0)));
    }

    @Override
    Node getParent() {
      return parent;
    }

    /**
     * @param parent sets a parent node to the current node
     */
    @Override
    void setParent(Node parent) {
      this.parent = parent;
    }

    /**
     * Api to get a value from an index without having to expose the entire "values" data structure.
     *
     * @param index integer value to retrieve value from
     * @return a {@link E} at index in the current node.
     */
    @Override
    E get(int index) {
      return values.get(index);
    }

    /**
     * adds {@link E} objects to "values" List, increments the noOfElementsInNode
     * and sorts them in lexicographical order keeping nulls at last
     *
     * @param value a {@link E} object to be added to the current node
     */
    @Override
    boolean add(E value) {
      E isSet = values.set(size++, value);
      values.sort(comparisonStrategy);
      return isSet != null;
    }

    /**
     * @param index int value to retrieve node from
     * @return a child node at index
     */
    @Override
    Node getChild(int index) {
      return children.get(index);
    }

    /**
     * adds a {@link Node} to children List, increments the noOfChildNodes
     * and sorts the T object in the comparable order keeping nulls at last
     *
     * @param child a child {@link Node} to be added to the current node
     */
    @Override
    boolean addChild(Node child) {
      child.parent = this;
      Node addedValue = children.set(childrenSize++, child);
      children.sort(childNodeComparator);
      return !addedValue.isNull();
    }

    /**
     * @param child removes old child {@link Node}, within the current node only, after spilt and balance happens
     */
    @Override
    boolean removeChild(Node child) {
      if (isLeaf()) {
        return false;
      }
      boolean childNodeFound = false;
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
      return childNodeFound;
    }

    @Override
    boolean isNull() {
      return false;
    }

    @Override
    String toString(String prefix) {
      StringBuilder nodeAsString = new StringBuilder(prefix);
      for (int i = 0; i < this.size(); i++) {
        if (i != this.size - 1) {
          nodeAsString.append(" | ").append(values.get(i));
        } else {
          nodeAsString.append(" | ").append(values.get(i)).append(" | ");
        }
      }
      nodeAsString.append("\n");
      for (int i = 0; i < this.childrenSize(); i++) {
        //add tab for each level going down
        nodeAsString.append(children.get(i).toString(prefix + "\t"));
      }
      return nodeAsString.toString();
    }

    @Override
    public String toString() {
      return toString("");
    }
  }

  /**
   * Null Object Pattern Implementation for {@link Node} Abstraction, this class will not hold any information.
   * this replaces null objects insertion in tree and its subsequent null checks.
   */
  private class NullNode extends Node {
    @Override
    Node getParent() {
      return new NullNode();
    }

    @Override
    void setParent(Node parent) {

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
      return new NullNode();
    }

    @Override
    boolean addChild(Node child) {
      return false;
    }

    @Override
    boolean removeChild(Node child) {
      return false;
    }

    @Override
    boolean isNull() {
      return true;
    }

    @Override
    String toString(String prefix) {
      return "";
    }

  }
}
