package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.student;

import hmaheshwari8095.redid825027067.cs635.assignment1.model.Student;
import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.BTree;
import hmaheshwari8095.redid825027067.cs635.assignment1.simpletree.Tree;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeCreationTest {

  @Test
  public void createAndCheckSizeOfTree() {
    Tree<Student> studentTree = new BTree<>(3);
    ThreadLocalRandom gpaGenerator = ThreadLocalRandom.current();
    IntStream.range(0, 20).forEach(value -> {
      double gpa = gpaGenerator.nextDouble(2.5, 4.0);
      studentTree.add(new Student(823000000L + value, "" + value, gpa));
    });
    assertEquals(20, studentTree.size());
  }
}
