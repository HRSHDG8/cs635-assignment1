package hmaheshwari8095.redid825027067.cs635.assignment1.model;

import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Node;

/**
 * Simple class to encapsulate the current node under check and its index
 * that needs a processing from.
 * This is pushed into stack to maintain a sequence of nodes the processing
 * should happen from.
 */
public class NodeIndexEntry {
    private final Node node;
    private final int startIndex;

    public NodeIndexEntry(Node node, int startIndex) {
        this.node = node;
        this.startIndex = startIndex;
    }

    public Node getNode() {
        return node;
    }

    public int getStartIndex() {
        return startIndex;
    }
}
