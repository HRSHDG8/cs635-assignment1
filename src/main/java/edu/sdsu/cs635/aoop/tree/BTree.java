package edu.sdsu.cs635.aoop.tree;

import java.util.*;
import java.util.function.Consumer;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

/**
 * @author 825027067
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

  // - Start Constructor Declarations

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

  // - End Constructor Declarations

  // - Start java.util.Set Api Methods

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
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
    return index >= this.size || index < 0;
  }

  @Override
  public Object[] toArray() {
    Object[] array = new Object[this.size];
    int i = 0;
    for (E e : this) {
      array[i++] = e;
    }
    // makes sure a deep clone is returned so a change to array does not change the tree.
    return Arrays.copyOf(array, this.size);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) {
    if (a.length < this.size)
      // Make a new array of a's runtime type, but my contents:
      return (T[]) Arrays.copyOf(toArray(), this.size, a.getClass());
    System.arraycopy(toArray(), 0, a, 0, this.size);
    if (a.length > this.size)
      a[this.size] = null;
    return a;
  }

  @Override
  public boolean add(E value) {
    if (value == null) {
      throw new IllegalArgumentException("you cannot add null values to BTree");
    }
    increaseSize();
    //base condition to insert the very first value
    if (this.root.isNull()) {
      // as per null object pattern pass a NullNode as parent of root, rather than null.
      this.root = new DataNode(new NullNode(), value);
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
    this.root.forEach(acceptor);
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
    return oldSize + c.size() == this.size;
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
    this.size = 0;
    this.root = new NullNode();
  }

  // - End java.util.Set API Methods

  // - Start java.util.SortedSet Api Methods

  @Override
  public Comparator<? super E> comparator() {
    return this.comparisonStrategy;
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

  // - End java.util.SortedSet Api Methods

  // - Start edu.sdsu.cs635.SortedSetTree Api Methods

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

  // - End edu.sdsu.cs635.SortedSetTree Api Methods

  // - Object class methods

  @Override
  public String toString() {
    // use the nodes toString method to bank on the toString Logic for the entire Tree
    return this.root.toString();
  }

  // - End Object class methods

  // - Start Private methods

  private void increaseSize() {
    this.size++;
  }

  private Node navigateRight(Node node, E value) {
    //For BTree the highest possible value in a node is the last value and the highest possible child node is the right most node
    int indexOfLastElement = node.size() - 1;
    Node rightBTreeNode = new NullNode();
    E highestComparableValue = node.get(indexOfLastElement);
    if (this.comparisonStrategy.compare(value, highestComparableValue) > 0) {
      rightBTreeNode = node.getChild(node.size());
    }
    return rightBTreeNode;
  }

  private Node navigateLeft(Node node, E value) {
    //For BTree the lowest possible value in a node is the first value and the lowest possible child node is the left most node
    E lowestComparableValue = node.get(0);
    Node leftBTreeNode = new NullNode();
    if (this.comparisonStrategy.compare(value, lowestComparableValue) <= 0) {
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
    if (currentNode.isLeaf()) {
      currentNode.add(valueToBeAdded);
      if (currentNode.size() <= this.maximumValuesInNode) {
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
      if (this.comparisonStrategy.compare(value, previousValue) > 0 &&
         this.comparisonStrategy.compare(value, nextValue) <= 0) {
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
   * Then insert the median node at appropriate position, the old overflown node is Garbage collected.
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
      this.root = newRoot;
      nodeUnderSplit = this.root;
      nodeUnderSplit.addChild(left);
      nodeUnderSplit.addChild(right);
    } else {
      Node parent = nodeUnderSplit.getParent();
      parent.add(medianValue);
      parent.removeChild(nodeUnderSplit);
      parent.addChild(left);
      parent.addChild(right);
      // if the parent overflows or is unbalanced, apply split and balance on it.
      if (parent.size() > this.maximumValuesInNode) {
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

  // - End private methods

  // -- Start External Iterator Class

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
    // Use Map.Entry to maintain the current index of element being returned,
    // its use it to just remember a node with the current element being accessed.
    Stack<Entry<Node, Integer>> traversalStack;

    public InOrderTreeIterator() {
      // Create a stack to maintain the current node under traversal and the index to start traversing the value from.
      this.cursor = 0;
      this.traversalStack = new Stack<>();
      this.expectedModCount = size;
      //start from the root node and 0th index.
      this.traversalStack.push(new SimpleEntry<>(root, 0));
    }

    final void checkForCoModification() {
      if (size != this.expectedModCount)
        throw new ConcurrentModificationException();
    }

    @Override
    public boolean hasNext() {
      return this.cursor < size;
    }

    /**
     * The function implements inorder traversal of the BTree using a stack
     * The logic flows as follows
     * The stack stores data as pairs of < Node, Index >, where node is the current node under traversal and Index is index if the next value to be returned.
     * The stack is initialized with root node.
     * A loop runs until the stack is empty.
     * Each entry is stack is checked as follows
     * check if a Node has children
     * - Yes (Non Leaf Node)
     * check if Index > 0
     * assign the value at Index - 1 to a variable
     * check if Node has more children
     * Push the Node back in stack with < Node, Index+1 >
     * check if value to be returned is not null
     * increment cursor and return value
     * - No (Leaf Nodes)
     * assign the value at Index to a variable
     * check if Node has more children
     * Push the Node back in stack with < Node, Index+1 >
     * increment cursor and return value
     * If the stack is empty or the tree is empty and a next is called without hasNext throw {@link IndexOutOfBoundsException}
     */
    @Override
    public E next() {
      checkForCoModification();
      while (!this.traversalStack.isEmpty() && !isEmpty()) {
        Entry<Node, Integer> entry = this.traversalStack.pop();
        Node currentNode = entry.getKey();
        int currentIndex = entry.getValue();
        if (currentNode.isLeaf()) {
          E value = currentNode.get(currentIndex);
          this.cursor++;
          // if the current node has more elements, push the same node in stack with the next index to access.
          if (currentIndex < currentNode.size() - 1) {
            this.traversalStack.push(new SimpleEntry<>(currentNode, currentIndex + 1));
          }
          return value;
        } else {
          E value = null;
          // if the left child has been visited return the current element in node.
          if (currentIndex > 0) {
            value = currentNode.get(currentIndex - 1);
          }
          // if the current node has more elements, push the same node in stack with the next index to access.
          if (currentIndex < currentNode.size()) {
            this.traversalStack.push(new SimpleEntry<>(currentNode, currentIndex + 1));
          }
          //push the children at index to the stack, so it's available in the next iteration to be traversed.
          Node child = currentNode.getChild(currentIndex);
          this.traversalStack.push(new SimpleEntry<>(child, 0));
          if (value != null) {
            this.cursor++;
            return value;
          }
        }
      }
      throw new IndexOutOfBoundsException("No more elements in tree, check hasNext before calling next");
    }
  }

  // -- End External Iterator Class

  // ---- Start Node Related Classes

  // -- Start Abstract Node

  /**
   * Abstraction for a tree node, provides common methods and fields for underlying implementation to use
   */
  abstract class Node {
    List<E> values;
    List<Node> children;
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

    abstract void forEach(Consumer<? super E> acceptor);

    /**
     * @return no of non-null elements in current node
     */
    int size() {
      return this.size;
    }

    /**
     * @return the no of non-null child nodes in current node
     */
    int childrenSize() {
      return this.childrenSize;
    }

    boolean isLeaf() {
      return this.childrenSize == 0;
    }
  }
  // -- End Abstract Node
  // -- Start Data Node

  /**
   * A Data node implementation for {@link Node} Abstraction
   * Each node holds a reference to its parent
   * A list of values it currently holds, and its count
   * A list of pointers to the child nodes, and its count
   */
  private class DataNode extends Node {

    private final Comparator<Node> childNodeComparator;

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
        this.children.add(new NullNode());
        this.values.add(null);
      }
      this.children.add(new NullNode());
      this.size = 0;
      this.childrenSize = 0;
      //sort child nodes by the comparable value of first element of each child node
      this.childNodeComparator = Comparator.nullsLast((o1, o2) -> comparisonStrategy.compare(o1.get(0), o2.get(0)));
    }

    @Override
    Node getParent() {
      return this.parent;
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
      return this.values.get(index);
    }

    /**
     * adds {@link E} objects to "values" List, increments the noOfElementsInNode
     * and sorts them in lexicographical order keeping nulls at last
     *
     * @param value a {@link E} object to be added to the current node
     */
    @Override
    boolean add(E value) {
      E isSet = this.values.set(size++, value);
      this.values.sort(comparisonStrategy);
      return isSet != null;
    }

    /**
     * @param index int value to retrieve node from
     * @return a child node at index
     */
    @Override
    Node getChild(int index) {
      if (index >= this.children.size() || index < 0) {
        return new NullNode();
      }
      return this.children.get(index);
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
      Node addedValue = this.children.set(childrenSize++, child);
      this.children.sort(childNodeComparator);
      return !addedValue.isNull();
    }

    /**
     * @param child removes old child {@link Node}, within the current node only, after spilt and balance happens
     */
    @Override
    boolean removeChild(Node child) {
      boolean childNodeFound = false;
      for (int i = 0; i < this.childrenSize; i++) {
        if (this.children.get(i).equals(child)) {
          childNodeFound = true;
        } else if (childNodeFound) {
          this.children.set(i - 1, this.children.get(i));
        }
      }
      if (childNodeFound) {
        this.children.set(--this.childrenSize, new NullNode());
      }
      return childNodeFound;
    }

    @Override
    boolean isNull() {
      return false;
    }

    @Override
    void forEach(Consumer<? super E> acceptor) {
      for (int i = this.size() - 1; i >= 0; i--) {
        //start from the right most child until you reach the leaf node.
        Node child = this.getChild(i + 1);
        child.forEach(acceptor);
        E value = this.get(i);
        acceptor.accept(value);
      }
      Node firstNode = this.getChild(0);
      firstNode.forEach(acceptor);
    }

    @Override
    public String toString() {
      StringBuilder nodeAsString = new StringBuilder();
      for (int i = 0; i < this.size(); i++) {
        //start from the left most child until you reach the leaf node.
        Node child = this.getChild(i);
        // No need to check if child is null, null object pattern will return empty string for NullObjects
        nodeAsString.append(child.toString()).append("\n");
        E value = this.get(i);
        nodeAsString.append(value.toString());
      }
      //for every non leaf node, call inOrder for the last Child Node of the current Node
      Node lastNode = this.getChild(this.childrenSize() - 1);
      nodeAsString.append(lastNode.toString());
      return nodeAsString.toString();
    }
  }
  // -- End DataNode
  // -- Start Null Node

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
    void forEach(Consumer<? super E> acceptor) {
      // do nothing for a null node
    }

    @Override
    public String toString() {
      // Null Node must return empty string to not change the actual nodes
      return "";
    }
  }
  // -- End Null Node
  // ---- End Node Related Classes
}
