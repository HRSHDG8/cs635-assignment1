package edu.sdsu.cs635.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultTreeTest {
  private static final int DEFAULT_TEST_SIZE = 10;
  private SortedSetTree<Integer> numberTree = null;

  @BeforeEach
  public void init() {
    numberTree = new BTree<>();
    IntStream.range(0, DEFAULT_TEST_SIZE).forEach(numberTree::add);
  }

  @Test
  public void isSizeCorrect() {
    assertEquals(DEFAULT_TEST_SIZE, numberTree.size());
  }

  @Test
  public void addShouldIncreaseSize() {
    numberTree.add(34);
    assertEquals(DEFAULT_TEST_SIZE + 1, numberTree.size());
  }

  @Test
  public void nullAdditionShouldThrowError() {
    assertThrows(AssertionError.class, () -> numberTree.add(null));
  }

  @Test
  public void getWhenIndexInRange() {
    assertEquals(1, numberTree.get(1));
  }

  @Test
  public void getWhenIndexOutOfRange() {
    assertThrows(IndexOutOfBoundsException.class, () -> numberTree.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> numberTree.get(numberTree.size()));
  }

  @Test
  public void addAll() {
    List<Integer> elementsToBeAdded = Arrays.asList(-2, -10, 6, 34, 2, -9);
    numberTree.addAll(elementsToBeAdded);
    assertEquals(-10, numberTree.get(0));
  }

  @Test
  public void addElementException() {
    assertThrows(AssertionError.class, () -> numberTree.add(null));
  }

  @Test
  public void containsAllTest() {
    assertTrue(numberTree.containsAll(Arrays.asList(2, 3)));
  }

  @Test
  public void doesntContainAllTest() {
    assertFalse(numberTree.containsAll(Arrays.asList(2, 11)));
  }

  @Test
  public void clearTree() {
    assertFalse(numberTree.isEmpty());
    numberTree.clear();
    assertTrue(numberTree.isEmpty());
  }

  @Test
  public void checkFirstAndLastElement() {
    assertEquals(0, numberTree.first());
    assertEquals(DEFAULT_TEST_SIZE - 1, numberTree.last());
  }

  @Test
  public void checkFirstAndLastOnEmptyTree() {
    numberTree.clear();
    assertNull(numberTree.first());
    assertNull(numberTree.last());
  }

  @Test
  public void coModificationTest() {
    Iterator<Integer> numberIterator = numberTree.iterator();
    if (numberIterator.hasNext()) {
      numberTree.add(11);
      assertThrows(ConcurrentModificationException.class, numberIterator::next);
    }
  }

  @Test
  public void testNextOnAnEmptyTree() {
    numberTree.clear();
    assertThrows(IndexOutOfBoundsException.class, () -> numberTree.iterator().next());
  }


}
