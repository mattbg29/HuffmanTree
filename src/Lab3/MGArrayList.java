/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  My version of an ArrayList, which includes the overloaded method
 * contains(), one with a single input to return the index at which the
 * input appears in the array (-1 if not present), and one with two inputs
 * to return the index at which either the upper or lower case version
 * of the input appears in the array.  The latter is useful for removing
 * case sensitivity from the encoding process.
 **************************************************************************** */

package Lab3;

public class MGArrayList<E> {
    private static final int STARTING_SIZE = 10;
    private int maxSize = STARTING_SIZE;
    private int size = 0;
    @SuppressWarnings("unchecked")
    private E[] array = (E[]) new Object[STARTING_SIZE];

    // Adds an item to the ArrayList
    void add(E item) {
        checkFull();
        array[size++] = item;
    }

    // Update the indicated array index with the new item
    void update(int index, E itemNew) {
        array[index] = itemNew;
    }

    // Remove the last item from the ArrayList
    E removeLast() {
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        checkMostlyEmpty();
        return array[--size];
    }

    // Return the size of the ArrayList
    int size() {
        return size;
    }

    // Returns the item in the ArrayList at the given index
    E get(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return array[index];
    }

    // Check if the ArrayList is full
    private void checkFull() {
        if (maxSize == size) {
            maxSize *= 2;
            reallocate("double");
        }
    }

    // Check if the ArrayList is less than 25% full
    private void checkMostlyEmpty() {
        if (size <= (maxSize / 4) && maxSize > STARTING_SIZE) {
            maxSize /= 2;
            reallocate("halve");
        }
    }

    // Check if the ArrayList is empty
    boolean isEmpty() {
        return size == 0;
    }

    // Double or halve the array size if it becomes full, or if after doubling at least once,
    // it becomes less than a quarter full
    private void reallocate(String input) {
        @SuppressWarnings("unchecked")
        E[] arrayNew = (E[]) new Object[maxSize];
        switch (input) {
            case "double":
                for (int i = 0; i < size; i++) {
                    arrayNew[i] = array[i];
                }
                break;
            case "halve":
                for (int i = 0; i < maxSize; i++) {
                    arrayNew[i] = array[i];
                }
                break;
            default:
                throw new IllegalStateException("Unexpected reallocation case: " + input);
        }
        array = arrayNew;
    }

    // Return the index at which the input item first appears in the array, or -1
    // if it does not appear in the array
    int contains(E item) {
        for (int i = 0; i < size; i++) {
            if (item.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    // Return the index at which the first of either the first or second item
    // appears in the array, or -1 if neither appears
    int contains(E item, E item2) {
        for (int i = 0; i < size; i++) {
            if (item.equals(array[i]) || item2.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

}

