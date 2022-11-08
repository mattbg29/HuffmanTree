/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  This class builds the priority queue, using either a given
 * frequency table, or a given array of phrases from which it builds
 * an array of custom frequency tables.
 **************************************************************************** */


package Lab3;

import java.io.BufferedReader;
import java.io.IOException;

public class BuildPriQueue {
    private final MGArrayList<MGPriorityQueue<Node>> arrPriQueue = new MGArrayList<>();
    private final BufferedReader inputFile;
    private final MGArrayList<String> phrases = new MGArrayList<>();
    private boolean hasError = false;

    BuildPriQueue(BufferedReader inputFile, boolean hasTable) throws IOException {
        this.inputFile = inputFile;
        buildPriorityQueue(hasTable);
    }

    // This builds the priority queue either using a user input file containing
    // symbols and frequencies, or creating a custom frequency table using
    // phrases provided by the user.  If a frequency table was provided,
    // it must follow specific guidelines or will result in an error
    // message to the user (see README for details).
    private void buildPriorityQueue(boolean hasTable) throws IOException {
        MGPriorityQueue<Node> priQueue = new MGPriorityQueue<>();
        String element = inputFile.readLine();
        if (hasTable) {
            MGArrayList<String> existingSymbols = new MGArrayList<>();
            while (element != null) {
                if (!element.equals("")) {
                    String[] symbolAndFreq = element.split(" - ");
                    if (symbolAndFreq.length == 2) {
                        String symbol = symbolAndFreq[0];
                        if (existingSymbols.contains(symbol) > -1) {
                            System.out.println("The following symbol is repeated, and will be skipped: " + element);
                        } else {
                            existingSymbols.add(symbol);
                            try {
                                int freq = Integer.parseInt(symbolAndFreq[1]);
                                Node newNode = new Node(freq, symbol);
                                priQueue.add(newNode);
                            } catch (NumberFormatException e) {
                                System.out.println("The following frequency is invalid: " + element + ". Please check file and try again.");
                                hasError = true;
                                break;
                            }
                        }
                    } else {
                        System.out.println("The following entry is invalid: " + element + ". Please check and try again.");
                        hasError = true;
                        break;
                    }
                }
                element = inputFile.readLine();
            }
            arrPriQueue.add(priQueue);
        } else {
            // Here I create a custom frequency table leveraging a nested class, MGMap,
            // which stores each unique character in a given phrase and its corresponding frequency
            while (element != null) {
                if (!element.equals("")) {
                    phrases.add(element);
                    MGMap map = new MGMap();
                    for (int i = 0; i < element.length(); i++) {
                        map.add(element.substring(i, i + 1));
                    }
                    for (int i = 0; i < map.symbol.size(); i++) {
                        priQueue.add(new Node(map.frequency.get(i), map.symbol.get(i)));
                    }
                    arrPriQueue.add(priQueue);
                    priQueue = new MGPriorityQueue<>();
                }
                element = inputFile.readLine();
            }
        }
    }

    MGArrayList<MGPriorityQueue<Node>> getArrPriQueue() {
        return arrPriQueue;
    }

    MGArrayList<String> getPhrases() {
        return phrases;
    }

    boolean getError() {
        return hasError;
    }

    // Custom Map class for storing unique symbols and their corresponding frequency
    private static class MGMap {
        MGArrayList<String> symbol = new MGArrayList<>();
        MGArrayList<Integer> frequency = new MGArrayList<>();

        void add(String symbol) {
            int index = this.symbol.contains(symbol);
            if (index >= 0) {
                this.frequency.update(index, this.frequency.get(index) + 1);
            } else {
                this.symbol.add(symbol);
                this.frequency.add(1);
            }
        }
    }
}
