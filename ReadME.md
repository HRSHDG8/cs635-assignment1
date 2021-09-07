## CS635 Assignment 1

>#### The goal of the project is to implement a bTree of order 3

**The Project Functionalities can be checked from the test suites.**

The project is structured as follows
1. main - has the actual bTree and supporting code
   1. a collection package that holds FixedList.
   2. a model package that holds data objects like student class definition.
   3. printer package that has a base Printable interface and following implementation:
      1. DebuggingPrinter
      2. InOrderConditionalPrinter
      3. ReverseOrderConditionPrinter
   4. simpletree package that holds the Tree interface, BTree implementation and a constructor **protected** Node class.
2. test
   1. A test suite in collection package to test the FixedListClass and its behavior.
   2. Following Test suite in simpletree package:
      1. student:
         1. FindElementByIndexInTreeTest: tests the functionality to find kth element in the tree in lexicographic order
         2. PrefectGpaTest: test logic to print students with prefect GPA.
         3. ProbationaryGPATest: test the logic to print students with gpa less than 2.85.
         4. TreeCreationTest: test the logic of Btree creation and Student insertion.
         5. TreeVisualizePrinter: test the logic of DebugPrinter and visually see the tree contents.
      2. SimpleBtreePrinter: Creates and prints Btree with Integer class.
