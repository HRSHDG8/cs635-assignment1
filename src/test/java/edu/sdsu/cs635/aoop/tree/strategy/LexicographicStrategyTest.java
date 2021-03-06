package edu.sdsu.cs635.aoop.tree.strategy;

import edu.sdsu.cs635.aoop.model.Student;
import edu.sdsu.cs635.aoop.tree.BTree;
import edu.sdsu.cs635.aoop.tree.SortedSetTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexicographicStrategyTest {

  //Strategy that compares Students by there name ignoring case sensitivity
  private static final Comparator<Student> LEXICOGRAPHIC_ORDERING_STRATEGY = Comparator.comparing(o -> o.getName().toLowerCase());
  private SortedSetTree<Student> studentTree = null;

  @BeforeEach
  public void init() {
    studentTree = new BTree<>(LEXICOGRAPHIC_ORDERING_STRATEGY);
    studentTree.add(new Student(825000001L, "Alex", 3.9));
    studentTree.add(new Student(825000002L, "Jack", 3.8));
    studentTree.add(new Student(825000003L, "Marrissa", 3.4));
    studentTree.add(new Student(825000004L, "Olivia", 4.0));
    studentTree.add(new Student(825000005L, "Joshua", 2.84));
    studentTree.add(new Student(825000006L, "Max", 2.86));
  }

  @Test
  @DisplayName("Check if the size of the created tree is equal to the number of elements inserted")
  public void testSize() {
    assertEquals(6, studentTree.size());
  }


  @Test
  @DisplayName("The first element should be the lowest element as per the passed strategy")
  public void firstStudentShouldBeAlex() {
    Student firstStudent = studentTree.get(0);
    assertEquals("Alex", firstStudent.getName());
  }
}
