/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  My custom node class for use in the priority queue.
 * This implements Comparable so that I can compare nodes according to the
 * lab guidelines.
 **************************************************************************** */


package Lab3;

public class Node implements Comparable<Node> {
    private final int data;
    private final String symbol;
    private Node leftTree = null;
    private Node rightTree = null;

    // Constructor for when no left and right subtree is submitted, and as such each is assigned null
    Node(int data, String symbol) {
        this.data = data;
        this.symbol = symbol;
    }

    // Constructor for when a left and right subtree is submitted
    Node(int data, String symbol, Node leftTree, Node rightTree) {
        this.data = data;
        this.symbol = symbol;
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    // Return the data, which for this lab will be the frequency
    int getData() {
        return data;
    }

    // Return the symbol, which for this lab will be the character
    String getSymbol() {
        return symbol;
    }

    // Return the left subtree root node
    Node getLeftTree() {
        return leftTree;
    }

    // Return the right subtree root node
    Node getRightTree() {
        return rightTree;
    }

    // This class implements Comparable, which enables two objects of the same type
    // to be compared according to their natural order, as defined by the compareTo
    // method which must be implemented.
    @Override
    public int compareTo(Node o) {
        if (this.data > o.data) {
            return 1;
        } else if (this.data < o.data) {
            return -1;
        }
        if (this.symbol.length() > o.symbol.length()) {
            return 1;
        } else if (this.symbol.length() < o.symbol.length()) {
            return -1;
        }
        return this.symbol.compareTo(o.symbol);
    }
}

