package edu.sdsu.cs635.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultTreeTest {
  private static final int DEFAULT_TEST_SIZE = 10;
  SortedSetTree<Integer> numberTree = null;

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

}
