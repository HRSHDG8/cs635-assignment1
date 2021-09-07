package edu.sdsu.cs635.assignment1.simpletree;

import edu.sdsu.cs635.assignment1.model.Student;
import edu.sdsu.cs635.assignment1.printer.DebuggingPrinter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeVisualizePrinter {
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
  public void printTree() {
    BTree tree = new BTree(3);
    ThreadLocalRandom gpaGenerator = ThreadLocalRandom.current();
    IntStream.range(0, 10).forEach(value -> {
      double gpa = gpaGenerator.nextDouble(2.5, 4.0);
      tree.add(new Student(823000000L + value, "" + value, gpa));
    });
    tree.print(new DebuggingPrinter());
    assertTrue(outContent.toString().contains("823000003"));
  }

  @Test
  public void printEmptyTree() {
    BTree tree = new BTree(3);
    tree.print(new DebuggingPrinter());
    assertTrue(outContent.toString().contains("Tree is empty"));
  }

  @AfterAll
  //only to debug to  after the console output stream is restored.
  public static void destroy() {
    BTree tree = new BTree(3);
    ThreadLocalRandom gpaGenerator = ThreadLocalRandom.current();
    IntStream.range(0, 10).forEach(value -> {
      double gpa = gpaGenerator.nextDouble(2.5, 4.0);
      tree.add(new Student(823000000L + value, "" + value, gpa));
    });
    tree.print(new DebuggingPrinter());
  }
}
