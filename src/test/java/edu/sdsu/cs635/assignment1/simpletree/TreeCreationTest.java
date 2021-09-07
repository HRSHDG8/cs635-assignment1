package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.model.Student;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeCreationTest {

  @Test
  public void createAndCheckSizeOfTree() {
    BTree tree = new BTree(3);
    ThreadLocalRandom gpaGenerator = ThreadLocalRandom.current();
    IntStream.range(0, 20).forEach(value -> {
      double gpa = gpaGenerator.nextDouble(2.5, 4.0);
      tree.add(new Student(823000000L + value, "" + value, gpa));
    });
    assertEquals(20, tree.size());
  }
}
