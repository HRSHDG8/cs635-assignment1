package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindElementByIndexInTreeTest {
  @Test
  public void findStudentByIndexWhenIndexIsWithinBounds() {
    BTree tree = new BTree(3);
    tree.add(new Student(825027001L, "a", 3.6));
    tree.add(new Student(825027001L, "b", 3.3));
    tree.add(new Student(825027001L, "d", 3.8));
    tree.add(new Student(825027001L, "c", 3.1));
    Student third = tree.findElementByIndex(1);
    assertEquals("b", third.getName());
  }

  @Test
  public void findStudentByIndexBoundaryConditionLowerBound() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    studentTree.add(new Student(825027001L, "b", 3.3));
    studentTree.add(new Student(825027001L, "d", 3.8));
    studentTree.add(new Student(825027001L, "c", 3.1));
    Student third = studentTree.findElementByIndex(0);
    assertEquals("a", third.getName());
  }

  @Test
  public void findStudentByIndexBoundaryConditionHigherBound() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    studentTree.add(new Student(825027001L, "b", 3.3));
    studentTree.add(new Student(825027001L, "d", 3.8));
    studentTree.add(new Student(825027001L, "c", 3.1));
    Student third = studentTree.findElementByIndex(3);
    assertEquals("d", third.getName());
  }

  @Test
  public void findStudentByIndexWhenTreeIsEmpty() {
    BTree studentTree = new BTree(3);
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(1));
  }

  @Test
  public void findStudentByIndexNegativeIndex() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(-1));
  }

  @Test
  public void findStudentByIndexOutOfBounds() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(1));
  }
}
