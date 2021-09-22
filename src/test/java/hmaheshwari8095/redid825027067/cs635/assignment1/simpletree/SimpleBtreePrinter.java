package hmaheshwari8095.redid825027067.cs635.assignment1.simpletree;

import edu.sdsu.cs635.tree.BTree;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
//Test class that will add integers to the btree and print it
public class SimpleBtreePrinter {

  @Test
  public void basicIntegerTest() {
    int size = 10;
    BTree<Integer> integerValueTree = new BTree<>(3);
    IntStream.range(0, size).forEach(integerValueTree::add);
//    Iterator<Integer> itr = integerValueTree.iterator();
//    while (itr.hasNext()) {
//      System.out.println(itr.next());
//    }
    integerValueTree.forEach(System.out::println);
    assertEquals(size, integerValueTree.size());
  }

}
