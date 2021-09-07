package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

import hmaheshwari8095.redid825027067.cs635.assignment1.collection.FixedList;
import hmaheshwari8095.redid825027067.cs635.assignment1.model.Student;

import java.util.Comparator;
import java.util.List;

/**
 * A node for the {@link BTree} class
 * Each node holds a reference to its parent
 * A list of values it currently holds, and its count
 * A list of pointers to the child nodes, and its count
 */
public class Node {
  private Node parent;
  private final List<Student> values;
  private final List<Node> children;
  private int noOfElementsInNode;
  private int noOfChildNodes;
  private Comparator<Node> childNodeComparator;

  // access is protected, so that it can only be accessed in the same sub package
  // i.e. "edu.sdsu.cs635.assignment1.simpletree"
  protected Node(Node parent, int order) {
    this.parent = parent;
    // the list is initialized with order and not order - 1, to accommodate the extra value that would be needed to process splitAndBalance
    // only order - 1 element will be present in the node
    this.values = new FixedList<>(order);
    this.children = new FixedList<>(order + 1);
    this.noOfElementsInNode = 0;
    this.noOfChildNodes = 0;
    //sort child nodes by the comparable value of first element of each child node
    this.childNodeComparator = Comparator.nullsLast(Comparator.comparing(arg0 -> arg0.valueAtIndex(0)));
  }

  /**
   * @return parent of the current node
   */
  public Node getParent() {
    return parent;
  }

  /**
   * @param parent sets a parent node to the current node
   */
  public void setParent(Node parent) {
    this.parent = parent;
  }

  /**
   * Recursively call print on each child node in a DFS pattern
   *
   * @param prefix a string to prepend before printing the node
   */
  public void print(String prefix) {
    System.out.print(prefix);
    for (int i = 0; i < this.noOfElementsInNode; i++) {
      if (i != this.noOfElementsInNode - 1) {
        System.out.print(" | " + values.get(i));
      } else {
        System.out.print(" | " + values.get(i) + " | ");
      }
    }
    System.out.println();
    for (int i = 0; i < this.noOfChildNodes; i++) {
      //add tab for each level going down
      children.get(i).print(prefix + "\t");
    }
  }

  /**
   * @param index integer value to retrieve value from
   * @return a Student at index in the current node.
   */
  public Student valueAtIndex(int index) {
    return values.get(index);
  }

  /**
   * adds {@link Student} objects to "values" List, increments the noOfElementsInNode
   * and sorts them in lexicographical order keeping nulls at last
   *
   * @param value a {@link Student} object to be added to the current node
   */
  public void addValue(Student value) {
    values.add(noOfElementsInNode++, value);
    values.sort(Comparator.nullsLast(Comparator.naturalOrder()));
  }

  /**
   * @param index int value to retrieve node from
   * @return a child node at index
   */
  public Node getChildAtIndex(int index) {
    return children.get(index);
  }

  /**
   * adds a {@link Node} to children List, increments the noOfChildNodes
   * and sorts the students in lexicographical order keeping nulls at last
   *
   * @param child a child {@link Node} to be added to the current node
   */
  public void addChild(Node child) {
    child.parent = this;
    children.add(noOfChildNodes++, child);
    children.sort(childNodeComparator);
  }

  /**
   * @param child removes child {@link Node}, within the current node only, after spilt and balance happens
   */
  public void removeChild(Node child) {
    boolean childNodeFound = false;
    if (noOfChildNodes == 0) {
      return;
    }
    for (int i = 0; i < noOfChildNodes; i++) {
      if (children.get(i).equals(child)) {
        childNodeFound = true;
      } else if (childNodeFound) {
        children.add(i - 1, children.get(i));
      }
    }
    if (childNodeFound) {
      children.add(--noOfChildNodes, null);
    }
  }

  /**
   * @return no of non-null elements in current node
   */
  public int getNoOfElementsInNode() {
    return noOfElementsInNode;
  }

  /**
   * @return the no of non-null child nodes in current node
   */
  public int getNoOfChildNodes() {
    return noOfChildNodes;
  }
}
