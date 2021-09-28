package edu.sdsu.cs635.aoop.tree.iterator;

import edu.sdsu.cs635.aoop.model.Student;
import edu.sdsu.cs635.aoop.tree.BTree;
import edu.sdsu.cs635.aoop.tree.SortedSetTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalIteratorTest {
  SortedSetTree<Student> studentTree = null;

  @BeforeEach
  public void init() {
    studentTree = new BTree<>(3, Comparator.comparing(student -> student.getName().toLowerCase()));
    studentTree.add(new Student(825000001L, "Alex", 3.9));
    studentTree.add(new Student(825000002L, "Jack", 3.8));
    studentTree.add(new Student(825000003L, "Marrissa", 3.4));
    studentTree.add(new Student(825000004L, "Olivia", 4.0));
    studentTree.add(new Student(825000005L, "Joshua", 2.84));
    studentTree.add(new Student(825000006L, "Max", 2.86));
  }

  @Test
  public void internalIteratorMustBeReverseOrder() {
    String[] expectedTestOrder = new String[]{"Olivia", "Max", "Marrissa", "Joshua", "Jack", "Alex"};
    AtomicInteger index = new AtomicInteger(0);
    studentTree.forEach(student -> {
      assertEquals(expectedTestOrder[index.getAndIncrement()], student.getName());
    });
  }
}
