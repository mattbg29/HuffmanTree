/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  This class builds the Huffman Tree, and generates all of the
 * output associated with the tree and the user inputs.
 **************************************************************************** */


package Lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HuffmanTree {
    private final MGPriorityQueue<Node> priQueue;
    private final StringBuilder sbTree = new StringBuilder("\nThe tree in preorder: ");
    private final StringBuilder sbCode = new StringBuilder("\nThe character codes: ");
    private final StringBuilder sbFrequencyTable = new StringBuilder("\nThe frequency table: ");
    private final MGArrayList<String> symbols = new MGArrayList<>();
    private final MGArrayList<String> codes = new MGArrayList<>();
    private final MGArrayList<StringBuilder> arrTree = new MGArrayList<>();
    private int totalCount = 0;
    private final PrintWriter writer;

    HuffmanTree(MGPriorityQueue<Node> priQueue, PrintWriter writer) throws IOException {
        this.writer = writer;
        this.priQueue = priQueue;
    }

    void buildAndPrint() {
        buildTree();
        preorderTraversal(priQueue.peek(), new StringBuilder());
        drawTree(priQueue.peek(), -2, 'L');
        printTreeAndCode();
    }

    // To build the Huffman tree, I remove the smallest node from the priority queue,
    // then remove the next smallest, and create a new node that contains the sum
    // of the two smallest nodes' frequencies with left and right tree pointing to
    // the smallest and second smallest, respectively, and then I add this new node
    // back into the priority queue, repeating this process until only one node remains
    private void buildTree() {
        while (priQueue.size() > 1) {
            Node smallest = priQueue.remove();
            Node secondSmallest = priQueue.remove();
            int freqSum = smallest.getData() + secondSmallest.getData();
            String symbolSum = smallest.getSymbol() + secondSmallest.getSymbol();
            Node newNode = new Node(freqSum, symbolSum, smallest, secondSmallest);
            priQueue.add(newNode);
        }
    }

    // This method carries out a preorder traversal of the Huffman Tree,
    // visiting first the root node, then the left subtree, then the right subtree.
    // I build three StringBuilder sequences as I traverse the tree:
    // 1) sbTree, which for each node, appends the symbol and frequency on a new line
    // 2) sbFrequencyTable, which for each leaf, appends the symbol and frequency on a new line
    // 3) sbCode, which for each leaf, appends the symbol and code for each leaf symbol,
    // where code is defined as a 0, 1 for each left, right subtree that needed to be
    // traversed, respectively
    private void preorderTraversal(Node root, StringBuilder code) {
        if (root != null) {
            sbTree.append("\n").append(root.getSymbol()).append(": ").append(root.getData());
            if (root.getLeftTree() == null && root.getRightTree() == null) {
                symbols.add(root.getSymbol());
                codes.add(code.toString());
                sbFrequencyTable.append("\n").append(root.getSymbol()).append(" - ").append(root.getData());
                sbCode.append("\n").append(root.getSymbol()).append(" = ").append(code);
            } else {
                preorderTraversal(root.getLeftTree(), code.append("0"));
                code.deleteCharAt(code.length() - 1);
                preorderTraversal(root.getRightTree(), code.append("1"));
                code.deleteCharAt(code.length() - 1);
            }
        }
    }

    // This method draws the Huffman Tree, where nodes are represented by periods,
    // leaves are represented by their corresponding symbols, and branches are
    // represented by /0---1\
    // This work as follows:  I do a preorder traversal, visiting each root node,
    // then traversing left, then right.  Each time I draw a leaf or node that
    // came from a left traversal, I increment the member variable totalCount by 4,
    // Doing so means that no new node will ever be to the left of any left subtree node
    // that came before it.  The right node has minimum space in front of it's corresponding
    // left node, so increases to totalCount are unnecessary for traversals down the right subtree.
    // See drawTreeLevel for further details.
    private void drawTree(Node root, int count, char subTree) {
        if (root != null) {
            count += 2;
            drawTree(root.getLeftTree(), count, 'L');
            if (subTree == 'L') {
                drawSubTree(count, 'L', root);
                totalCount += 4;
            }
            drawTree(root.getRightTree(), count, 'R');
            if (subTree == 'R') {
                drawSubTree(count, 'R', root);
            }
        }
    }

    // Here I draw the the given subtree and corresponding branch.
    // The tree needs visual space between nodes and branches, and I implement this
    // through logic that I explain in comments below
    private void drawSubTree(int count, char subTree, Node root) {
        // First, add empty StringBuilders for the new levels.  Each tree level requires
        // two sequences of Strings: one for the symbols, and one for their corresponding branches
        growArrTree(count);

        // Next, set a minimum amount of space to append to the current level of
        // the max of 3 and totalCount (which is either 4 greater than the previous node,
        // if the previous node was a left node, the most recent level length if the last
        // node was a leaf, or 1 less the most recent totalCount, to allow for
        // right nodes to gradually condense as we go up the tree)
        int toAppend = Math.max(totalCount - arrTree.get(count).length(), 3);

        // If the node is a leaf, assign to symbolNow its symbol and reset totalCount to be the length
        // of the given level less 1; else, assign to symbolNow "." and subtract one from totalCount
        String symbolNow = ".";
        totalCount -= 1;
        if (root.getLeftTree() == null && root.getRightTree() == null) {
            symbolNow = root.getSymbol();
            totalCount = arrTree.get(count).length() + toAppend - 1;
        }

        // Update the symbol level, count + 1, to include the predetermined spaces and the symbol
        // Then, update the branch level, count, to have either a left branch,
        // or a 0 followed by a the predetermined number of dashes followed by a 1 and a right branch
        arrTree.update(count + 1, arrTree.get(count + 1).append(" ".repeat(toAppend)).append(symbolNow));
        if (subTree == 'L') {
            arrTree.update(count, arrTree.get(count).append(" ".repeat(toAppend)).append("/"));
        } else {
            arrTree.update(count, arrTree.get(count).append("0").append("-".repeat(toAppend - 2)).append("1").append("\\"));
        }
    }

    // to grow the drawing arrTree to account for new levels,
    // see drawSubTree for details
    private void growArrTree(int count) {
        while (arrTree.size() <= (count + 1)) {
            arrTree.add(new StringBuilder());
        }
    }

    // To decode a file with 1 or more coded phrases.
    // This is carried out by traversing the tree according to the given bit in the
    // code - for 0, go left, for 1, go right.  Once a leaf is found, append the leaf's
    // symbol to a result string and continue to the next character in the phrase
    void decode(BufferedReader decodeFile) throws IOException {
        String code = decodeFile.readLine();
        while (code != null) {
            if (!code.equals("")) {
                writer.println("\nThe phrase to decode: \n" + code);
                Node rootCurrent = priQueue.peek();
                StringBuilder result = new StringBuilder();
                boolean hasError = false;
                for (int i = 0; i < (code.length()); i++) {
                    char bit = code.charAt(i);
                    if (bit == '1') {
                        rootCurrent = rootCurrent.getRightTree();
                    } else if (bit == '0') {
                        rootCurrent = rootCurrent.getLeftTree();
                    } else {
                        writer.println("Invalid code entry: " + bit + ".  Code must be only 1s and 0s");
                        hasError = true;
                        break;
                    }
                    if (rootCurrent.getLeftTree() == null && rootCurrent.getRightTree() == null) {
                        result.append(rootCurrent.getSymbol());
                        rootCurrent = priQueue.peek();
                    }
                }
                if (!hasError) {
                    writer.println("The phrase decoded: \n" + result);
                }
            }
            code = decodeFile.readLine();
        }
        writer.close();
        decodeFile.close();
    }

    // To encode one given phrase; this comes into play when the user has
    // input a file of phrases and elected for a custom frequency table.
    // This is carried out by iterating through the phrase character by character,
    // finding the code that corresponds to the given symbol in the codes and symbols arrays
    // (generated via preorder traversal) and appending said code into a result string which
    // is then printed out. Since the frequency table is derived from each element in the
    // given phrase, this does account for any possible character and case sensitivity
    void encode(String phrase) {
        writer.println("\nThe phrase to encode: \n" + phrase);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < phrase.length(); i++) {
            String symbolNow = phrase.substring(i, i + 1);
            result.append(codes.get(symbols.contains(symbolNow)));
        }
        writer.println("The phrase encoded: \n" + result);
    }

    // To encode a given set of phrases all via the same independently generated
    // frequency table.  Here, only symbols that are included in the frequency table
    // will be included in the encoding, and case sensitivity is only preserved
    // if the given frequency table contains both upper and lower case letters.
    // If not, this will encode in either upper or lower case, whichever is present.
    void encode(BufferedReader phrases) throws IOException {
        String phrase = phrases.readLine();
        while (phrase != null) {
            writer.println("\nThe phrase to encode: \n" + phrase);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < phrase.length(); i++) {
                String symbolNow = phrase.substring(i, i + 1);
                int indexNow = symbols.contains(symbolNow);
                if (indexNow == -1) {
                    indexNow = symbols.contains(symbolNow.toUpperCase(), symbolNow.toLowerCase());
                }
                if (indexNow > -1) {
                    result.append(codes.get(indexNow));
                }
            }
            writer.println("The phrase encoded: \n" + result);
            phrase = phrases.readLine();
        }
    }

    // Print to the output file the Huffman Tree, preorder output, symbol codes,
    // and frequency table
    private void printTreeAndCode() {
        writer.println("Huffman Tree:");
        for (int i = 1; i < arrTree.size(); i++) {
            writer.println(arrTree.get(i));
        }
        writer.println(sbTree);
        writer.println(sbCode);
        writer.println(sbFrequencyTable);
    }

}
