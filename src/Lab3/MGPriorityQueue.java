/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  My version of a Priority Queue, implemented via a heap data structure
 * where elements are stored in an array and each element's children (those in
 * the array at the parent's index * 2 + 1, + 2) are larger than their parent.
 * Element ordering is done according to their natural ordering, as defined by a class
 * that implements Comparable, as my custom Node class does.
 **************************************************************************** */

package Lab3;

public class MGPriorityQueue<E> {
    private final MGArrayList<E> arr;

    // My priority queue leverages a heap data structure using my MGArrayList
    public MGPriorityQueue() {
        arr = new MGArrayList<>();
    }

    // I add each item to the back of the heap, then swap it with its parent until
    // the parent is smaller than the element, or the element reaches the front
    public void add(E item) {
        arr.add(item);
        int child = arr.size() - 1;
        int parent = (child - 1) / 2;
        while (parent >= 0 && ((Comparable<E>) arr.get(parent)).compareTo(arr.get(child)) > 0) {
            swap(parent, child);
            child = parent;
            parent = (child - 1) / 2;
        }
    }

    // The method for swapping parent and child
    void swap(int indexParent, int indexChild) {
        E temp = arr.get(indexParent);
        arr.update(indexParent, arr.get(indexChild));
        arr.update(indexChild, temp);
    }

    // Removal from a priority queue is always from the front of the queue,
    // in this case as represented by the 0th index of of heap array, where
    // the smallest element resides.  This method replaces the 0th index with the
    // last element in the array and swaps this replacement element with its smaller child
    // until the element is smaller than its smaller child and thus the heap is restored
    E remove() {
        E smallest = arr.get(0);
        if (arr.size() == 1) {
            arr.removeLast();
        } else {
            arr.update(0, arr.removeLast());
            if (arr.size() > 1) {
                int parent = 0;
                int smallerChild = smallerChild(1, 2);
                while (((Comparable<E>) arr.get(parent)).compareTo(arr.get(smallerChild)) > 0) {
                    swap(parent, smallerChild);
                    parent = smallerChild;
                    int child = parent * 2 + 1;
                    if (child >= arr.size()) {
                        break;
                    }
                    smallerChild = smallerChild(child, child + 1);
                }
            }
        }
        return smallest;
    }

    // Returns the smaller child of two elements
    int smallerChild(int a, int b) {
        if (b >= arr.size() || ((Comparable<E>) arr.get(b)).compareTo(arr.get(a)) > 0) {
            return a;
        }
        return b;
    }

    // Returns the heap's size
    int size() {
        return arr.size();
    }

    // returns the element at the front of the heap, ie the element in the priority queue
    // with the highest priority, without altering the queue.
    E peek() {
        return arr.get(0);
    }
}
