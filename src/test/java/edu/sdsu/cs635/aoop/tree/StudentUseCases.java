package edu.sdsu.cs635.aoop.tree;

import edu.sdsu.cs635.aoop.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the following use case
 * 1) Create a student {@link BTree} of order 3 and Ordered lexicographically.
 * 2) List all {@link Student} with perfect score in order of their names.
 * 3) List all {@link Student} redIds who are probationary in descending order of their names.
 * 4) Find {@link Student} by index of insertion (Lexicographically) and throw {@link ArrayIndexOutOfBoundsException} if index overflows the elements inserted.
 */
public class StudentUseCases {

  //Strategy that compares Students by there name ignoring case sensitivity
  private static final Comparator<Student> LEXICOGRAPHIC_ORDERING_STRATEGY = Comparator.comparing(o -> o.getName().toLowerCase());
  private SortedSetTree<Student> studentTree = null;

  @BeforeEach
  public void init() {
    studentTree = new BTree<>(3, LEXICOGRAPHIC_ORDERING_STRATEGY);
    studentTree.add(new Student(825000001L, "Alex", 3.9));
    studentTree.add(new Student(825000002L, "Jack", 3.8));
    studentTree.add(new Student(825000003L, "Marrissa", 3.4));
    studentTree.add(new Student(825000004L, "Olivia", 4.0));
    studentTree.add(new Student(825000005L, "Joshua", 2.84));
    studentTree.add(new Student(825000006L, "Max", 2.86));
    studentTree.add(new Student(825000006L, "Zack", 4.0));
  }

  @Test
  public void studentsWithPerfectGpa() {
    List<Student> perfectScoreStudents = new ArrayList<>();
    studentTree.forEach(student -> {
      if (student.isPerfectScore()) {
        perfectScoreStudents.add(student);
      }
    });
    assertEquals(2, perfectScoreStudents.size());
  }

  @Test
  public void probationaryStudents() {
    List<Long> probationaryStudentsId = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
             studentTree.iterator(),
             Spliterator.ORDERED)
          , false)
       .filter(Student::amIProbationary)
       .map(Student::getRedId)
       .collect(Collectors.toList());

    assertEquals(1, probationaryStudentsId.size());
    assertEquals(825000005L, probationaryStudentsId.get(0));
  }

  @Test
  public void getStudentAtIndex() {
    Student lastStudent = studentTree.get(studentTree.size() - 1);
    assertEquals("Zack", lastStudent.getName());
  }

  @Test
  public void getStudentAtIndexException() {
    assertThrows(IndexOutOfBoundsException.class, () -> studentTree.get(studentTree.size()));
  }
}
