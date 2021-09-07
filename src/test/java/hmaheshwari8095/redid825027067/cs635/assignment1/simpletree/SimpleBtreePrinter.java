package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

import hmaheshwari8095.redid825027067.cs635.assignment1.printer.DebuggingPrinter;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
//Test class that will add integers to the btree and print it
public class SimpleBtreePrinter {

  @Test
  public void basicIntegerTest() {
    BTree<Integer> integerValueTree = new BTree<>(3);
    IntStream.range(0, 20).forEach(integerValueTree::add);
    integerValueTree.print(new DebuggingPrinter<>());
    assertEquals(20, integerValueTree.size());
  }
}
