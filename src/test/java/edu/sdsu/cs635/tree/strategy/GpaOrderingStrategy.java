package edu.sdsu.cs635.tree.strategy;

import edu.sdsu.cs635.model.Student;
import edu.sdsu.cs635.tree.BTree;
import edu.sdsu.cs635.tree.SortedSetTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GpaOrderingStrategy {
  //Strategy that compares Students by there GPA
  private static final Comparator<Student> GPA_ORDERING_STRATEGY = Comparator.comparing(Student::getGpa);
  private SortedSetTree<Student> studentTree = null;

  @BeforeEach
  public void init() {
    studentTree = new BTree<>(3, GPA_ORDERING_STRATEGY);
    studentTree.add(new Student(825000001L, "Alex", 3.9));
    studentTree.add(new Student(825000002L, "Jack", 3.8));
    studentTree.add(new Student(825000003L, "Marrissa", 3.4));
    studentTree.add(new Student(825000004L, "Olivia", 4.0));
    studentTree.add(new Student(825000005L, "Joshua", 2.84));
    studentTree.add(new Student(825000006L, "Max", 2.86));
  }

  @Test
  public void createsATreeOfSizeN() {
    assertEquals(6, studentTree.size());
  }


  @Test
  public void firstStudentShouldBeAlex() {
    Student lowestGpaStudent = studentTree.get(0);
    assertEquals(2.84, lowestGpaStudent.getGpa());
  }
}
