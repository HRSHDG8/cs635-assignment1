package edu.sdsu.cs635.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite to test custom comparator against the BTree Implementation
 */
public class CustomComparatorTree {
  private static final int DEFAULT_TEST_SIZE = 10;
  SortedSetTree<Integer> numberTree = null;

  @BeforeEach
  public void init() {
    //Strategy for sorting integers in reverse order
    numberTree = new BTree<>((i1, i2) -> i2 - i1);
    IntStream.range(0, DEFAULT_TEST_SIZE).forEach(numberTree::add);
  }

  @Test
  public void firstElementShouldBeLargest() {
    assertEquals(9, numberTree.get(0));
  }

  @Test
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
}
