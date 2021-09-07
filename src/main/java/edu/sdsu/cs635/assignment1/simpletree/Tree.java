package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.printer.Printable;

/**
 * @param <T> Create a tree of type T
 */
public interface Tree<T> {
    /**
     * @param value adds a value of type T to the Tree.
     */
    void add(T value);

    /**
     * @param printable a custom implementation of the Printable interface.
     */
    void print(Printable<Node> printable);

    /**
     * @param index the index (kth) element in the tree.
     * @return element T if its found.
     * @throws IndexOutOfBoundsException if k is out of bounds.
     */
    T findElementByIndex(int index) throws IndexOutOfBoundsException;

    /**
     * @return size of the tree i.e. the total no of elements in every node of the tree.
     */
    int size();
}
