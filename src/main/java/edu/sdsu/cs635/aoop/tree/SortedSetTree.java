package edu.sdsu.cs635.aoop.tree;

import java.util.SortedSet;

/**
 * Extension to the SortedSet Interface to add a provision for get method.
 */
public interface SortedSetTree<E> extends SortedSet<E> {
  E get(int index);
}
