package edu.sdsu.cs635.tree;

import java.util.SortedSet;

public interface Tree<E> extends SortedSet<E> {
  E get(int index);
}
