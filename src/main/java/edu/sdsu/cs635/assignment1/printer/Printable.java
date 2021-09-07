package edu.sdsu.cs635.assignment1.printer;

/**
 * @param <T> any object that can be printed as a {@link String}
 */
public interface Printable<T> {
  void print(T treeNodeToBePrinted);
}
