package edu.sdsu.cs635.assignment1.collection;

import java.util.ArrayList;

/**
 * This is a custom sub-class of {@link ArrayList} to mimic the behavior of arrays with fixed size.
 * This Class with initialize a list of size n with null values.
 * Any value added to the list at an index will replace the null value without increasing the size of the list.
 * Any value added to the list without an index will replace the first available null element.
 *
 * @param <T>
 */
public class FixedList<T> extends ArrayList<T> {
    private final int fixedSize;

    /**
     * The constructor invoke the constructor of the ArrayList class and sets a local copy of fixed limit list that needs to be craeted
     * It then initializes the list with null values.
     *
     * @param initialCapacity takes an integer to create a local copy of the maximum size that the list can take in
     */
    public FixedList(int initialCapacity) {
        super(initialCapacity);
        this.fixedSize = initialCapacity;
        for (int i = 0; i < initialCapacity; i++) {
            super.add(null);
        }
    }

    /**
     * A custom implementation of the add method from array list to take in a value and set it on the first available null value
     *
     * @param element the value "element" that needs to be added to the list
     * @return a boolean if a null value was found and value was replaced, else returns false.
     */
    @Override
    public boolean add(T element) {
        int i = this.indexOf(null);
        checkIfOutOfBounds(i);
        if (i != -1) {
            super.set(i, element);
            return true;
        } else {
            return false;
        }
    }

    private void checkIfOutOfBounds(int index) {
        if (index >= fixedSize) {
            throw new ArrayIndexOutOfBoundsException("You cannot add more than ::" + fixedSize + " elements in the fixed list");
        }
    }

    /**
     * overriding the add method with custom implementation that replaces the previous value and does not increase list size.
     *
     * @param index   an integer index ranging from 0-> fixedSize at which the below element must be replaced.
     * @param element the value "element" that needs to be added to the list
     */
    @Override
    public void add(int index, T element) {
        checkIfOutOfBounds(index);
        super.set(index, element);
    }
}
