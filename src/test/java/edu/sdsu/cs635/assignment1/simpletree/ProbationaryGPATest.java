package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.printer.InOrderConditionalPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A student is considered as probationary if his gpa is < 2.85
 */
public class ProbationaryGPATest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void gpaDefaultersTest() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 3.6));
    studentTree.add(new Student(825027002L, "b", 3.3));
    studentTree.add(new Student(825027003L, "c", 2.8));
    studentTree.add(new Student(825027004L, "d", 3.1));
    studentTree.add(new Student(825027005L, "e", 2.85));
    studentTree.add(new Student(825027006L, "f", 2.84));
    studentTree.print(new InOrderConditionalPrinter(Student::amIProbationary, Student::printRedId));
    assertTrue(outContent.toString().contains("825027003"));
  }
}
