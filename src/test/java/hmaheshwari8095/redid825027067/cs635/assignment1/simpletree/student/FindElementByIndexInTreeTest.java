package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.student;

import hmaheshwari8095.redid825027067.cs635.assignment1.model.Student;
import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.BTree;
import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Tree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindElementByIndexInTreeTest {
  @Test
  public void findStudentByIndexWhenIndexIsWithinBounds() {
    Tree<Student> tree = new BTree<>(3);
    tree.add(new Student(825027001L, "a", 3.6));
    tree.add(new Student(825027001L, "b", 3.3));
    tree.add(new Student(825027001L, "d", 3.8));
    tree.add(new Student(825027001L, "c", 3.1));
    Student third = tree.findElementByIndex(1);
    assertEquals("b", third.getName());
  }

  @Test
  public void findStudentByIndexBoundaryConditionLowerBound() {
    Tree<Student> studentTree = new BTree<>(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    studentTree.add(new Student(825027001L, "b", 3.3));
    studentTree.add(new Student(825027001L, "d", 3.8));
    studentTree.add(new Student(825027001L, "c", 3.1));
    Student third = studentTree.findElementByIndex(0);
    assertEquals("a", third.getName());
  }

  @Test
  public void findStudentByIndexBoundaryConditionHigherBound() {
    Tree<Student> studentTree = new BTree<>(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    studentTree.add(new Student(825027001L, "b", 3.3));
    studentTree.add(new Student(825027001L, "d", 3.8));
    studentTree.add(new Student(825027001L, "c", 3.1));
    Student third = studentTree.findElementByIndex(3);
    assertEquals("d", third.getName());
  }

  @Test
  public void findStudentByIndexWhenTreeIsEmpty() {
    Tree<Student> studentTree = new BTree<>(3);
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(1));
  }

  @Test
  public void findStudentByIndexNegativeIndex() {
    Tree<Student> studentTree = new BTree<>(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(-1));
  }

  @Test
  public void findStudentByIndexOutOfBounds() {
    Tree<Student> studentTree = new BTree<>(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.findElementByIndex(1));
  }
}
