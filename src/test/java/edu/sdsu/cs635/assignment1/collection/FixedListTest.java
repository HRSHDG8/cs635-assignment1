package edu.sdsu.cs635.assignment1.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FixedListTest {

    @Test
    public void checkListBounds() {
        List<Integer> primeNumberList = new FixedList<>(2);
        assertTrue(primeNumberList.add(1));
        assertTrue(primeNumberList.add(2));
        assertFalse(primeNumberList.add(3));
    }

    @Test
    public void testIfAddReplacesElement() {
        List<Integer> primeNumberList = new FixedList<>(2);
        primeNumberList.add(1, 3);
        assertEquals(3, primeNumberList.get(1));
    }

    @Test
    public void testOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> new FixedList<>(2).add(2, 3));
    }
}
