package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

/**
 * Simple class to encapsulate the current node under check and its index
 * that needs a processing from.
 * This is pushed into stack to maintain a sequence of nodes the processing
 * should happen from.
 */
public class NodeIndexEntry<T extends Comparable<T>> {
  private final Node<T> node;
  private final int startIndex;

  protected NodeIndexEntry(Node<T> node, int startIndex) {
    this.node = node;
    this.startIndex = startIndex;
  }

  public Node<T> getNode() {
    return node;
  }

  public int getStartIndex() {
    return startIndex;
  }
}
