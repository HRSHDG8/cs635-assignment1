package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

import hmaheshwari8095.redid825027067.cs635.assignment1.model.Student;
import hmaheshwari8095.redid825027067.cs635.assignment1.printer.ReverseOrderConditionalPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerfectGpaTest {
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
  public void studentsWithPerfectGpa() {
    BTree studentTree = new BTree(3);
    studentTree.add(new Student(825027001L, "a", 4.00)); // prefect score
    studentTree.add(new Student(825027002L, "b", 3.30));
    studentTree.add(new Student(825027003L, "c", 2.80));
    studentTree.add(new Student(825027004L, "d", 3.10));
    studentTree.add(new Student(825027005L, "e", 4.00)); //perfect score
    studentTree.add(new Student(825027006L, "f", 2.84));
    studentTree.print(new ReverseOrderConditionalPrinter(Student::isPerfectScore, Student::printName));
    assertTrue(outContent.toString().contains("e") && outContent.toString().contains("a"));
  }
}
