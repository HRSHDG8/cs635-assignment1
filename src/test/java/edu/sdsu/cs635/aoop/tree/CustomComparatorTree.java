package edu.sdsu.cs635.aoop.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite to test custom comparator against the BTree Implementation
 */
public class CustomComparatorTree {
  private static final int DEFAULT_TEST_SIZE = 10;
  private SortedSetTree<Integer> numberTree = null;
  private static final Comparator<Integer> REVERSE_INTEGER_STRATEGY = Comparator.reverseOrder();

  @BeforeEach
  public void init() {
    //Strategy for sorting integers in reverse order
    numberTree = new BTree<>(REVERSE_INTEGER_STRATEGY);
    IntStream.range(0, DEFAULT_TEST_SIZE).forEach(numberTree::add);
  }

  @Test
  @DisplayName("First element in the tree should be the largest element for a reversed strategy")
  public void firstElementShouldBeLargest() {
    assertEquals(9, numberTree.get(0));
  }

  @Test
  @DisplayName("Last element in the tree should be the smallest element for a reversed strategy")
  public void lastElementShouldBeSmallest() {
    assertEquals(0, numberTree.get(numberTree.size() - 1));
  }

  @Test
  public void iteratorShouldBeInOrderOfTheCustomComparator() {
    int size = DEFAULT_TEST_SIZE;
    for (int i : numberTree) {
      assertEquals(--size, i);
    }
  }

  @Test
  public void forEachShouldBeReverseOrderOfTheCustomComparator() {
    AtomicInteger size = new AtomicInteger(0);
    numberTree.forEach(integer -> assertEquals(size.getAndIncrement(), integer));
  }

  @Test
  public void toArrayBlankParameterTest() {
    Object[] array = numberTree.toArray();
    int size = DEFAULT_TEST_SIZE;
    for (Object i : array) {
      assertEquals(--size, i);
    }
  }

  @Test
  public void toArrayTest() {
    Integer[] array = numberTree.toArray(new Integer[numberTree.size() - 1]);
    int size = DEFAULT_TEST_SIZE;
    assertEquals(array.length, numberTree.size());
    for (Object i : array) {
      assertEquals(--size, i);
    }
  }

  @Test
  public void toArrayTestLargeSize() {
    Integer[] array = numberTree.toArray(new Integer[numberTree.size() + 1]);
    int size = DEFAULT_TEST_SIZE;
    assertEquals(array.length, numberTree.size() + 1);
    for (Object i : array) {
      if (size > 0) {
        assertEquals(--size, i);
      }
    }
  }

}
