/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  To run the program.  In Main, I get user input via the UserInput class,
 * then generate the priority queue via the BuildPriQueue class, and finally
 * build and print the Huffman Tree and other output via the HuffmanTree class.
 * See README for details about the overall program.
 **************************************************************************** */

package Lab3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner inputReader = new Scanner(System.in);
        try {
            // Get the user input
            UserInput userInput = new UserInput(inputReader);
            BuildPriQueue priQueue;

            // Build the priority queue(s) using a frequency table or, if the user
            // input phrases and elected a custom frequency table, using the phrases
            try {
                if (userInput.phrasesTable) {
                    priQueue = new BuildPriQueue(userInput.frequencyTable, true);
                } else {
                    priQueue = new BuildPriQueue(userInput.phrases, false);
                }

                if (!priQueue.getError()) {
                    // Get the array of priority queues; this will be a single
                    // priority queue unless the user inputted a file of phrases and
                    // elected for custom frequency tables, in which case there will be
                    // a priority queue for each phrase
                    MGArrayList<MGPriorityQueue<Node>> arrPriQueue = priQueue.getArrPriQueue();
                    MGArrayList<String> phrases = priQueue.getPhrases();
                    String outputFile = userInput.outputFile;
                    PrintWriter writer = new PrintWriter(outputFile);

                    // Build the Huffman Tree and generate all of the output, with the
                    // particular output defined by the user's input choice
                    HuffmanTree huffTree = new HuffmanTree(arrPriQueue.get(0), writer);
                    huffTree.buildAndPrint();
                    switch (userInput.inputChoice) {
                        case ("Frequency_table"):
                            writer.close();
                            break;
                        case ("Decode"):
                            huffTree.decode(userInput.codes);
                            writer.close();
                            break;
                        case ("Phrases"):
                            // When the user inputs phrases and elects for custom frequency tables,
                            // the arrPriQueue will contain more than 1 item, and this will generate
                            // a separate HuffmanTree output for each, with each written to a unique file
                            for (int i = 0; i < arrPriQueue.size(); i++) {
                                if (userInput.phrasesTable) {
                                    huffTree.encode(userInput.phrases);
                                } else if (i == 0) {
                                    huffTree.encode(phrases.get(i));
                                } else {
                                    writer = new PrintWriter(outputFile);
                                    huffTree = new HuffmanTree(arrPriQueue.get(i), writer);
                                    huffTree.buildAndPrint();
                                    huffTree.encode(phrases.get(i));
                                }
                                String[] outputFileSplit = userInput.outputFile.split("\\.");
                                outputFile = outputFileSplit[0] + (i + 1) + "." + outputFileSplit[1];
                                writer.close();
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + userInput.inputChoice);
                    }
                }
            } catch (IOException e) {
                System.out.println("Problem reading one of the input files.  Please review and try again.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening one of the files.  Please check files and try again");
        }

        inputReader.close();
    }


}
