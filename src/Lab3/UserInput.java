/* *****************************************************************************
 * Name: Matthew Green
 * Date: 2Nov2022
 * Purpose:  To gather all input from the user.
 **************************************************************************** */

package Lab3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class UserInput {
    Scanner inputReader;
    String inputChoice, outputFile;
    BufferedReader frequencyTable, phrases, codes;
    String[] CHOICES = {"Frequency_table", "Phrases", "Decode"};
    boolean phrasesTable = true;

    UserInput(Scanner inputReader) throws FileNotFoundException {
        this.inputReader = inputReader;
        getUserInput();
    }

    // Ask the user what type of input file they want to submit, then
    // follow-up with more requests depending on the input
    private void getUserInput() throws FileNotFoundException {
        boolean repeatCheck = true;
        while (repeatCheck) {
            System.out.println("\nThere are three possible input file types: " +
                    "\n1. A file containing a frequency table (see README for allowable format) " +
                    "\n2. A file containing a list of words/phrases to encode" +
                    "\n3. A file containing a list of 0s and 1s to decode" +
                    "\n\nPlease enter the number corresponding the file type you will input:");
            String choice = inputReader.nextLine();
            if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                System.out.println("\nInvalid choice.  Please try again\n");
            } else {
                int choiceInt = Integer.parseInt(choice) - 1;
                inputChoice = CHOICES[choiceInt];
                repeatCheck = false;
            }
        }
        switch (inputChoice) {
            case ("Frequency_table"):
                getFrequencyTable();
                break;
            case ("Decode"):
                getInputFile("codes");
                getFrequencyTable();
                break;
            case ("Phrases"):
                getInputFile("phrases");
                phrasesTable();
                if (phrasesTable) {
                    getFrequencyTable();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + inputChoice);
        }
        System.out.println("\nPlease enter the output file name:");
        if (!phrasesTable) {
            System.out.println("(Note that since you elected to create custom frequency tables, " +
                    "\nThis will generate unique output files for each phrase, using " +
                    "\nyour input here followed by a number that increments with each phrase)");
        }
        outputFile = inputReader.nextLine();
        if (!outputFile.contains(".")) {
            outputFile += ".txt";
        }
    }

    // Retrieve the frequency table file
    private void getFrequencyTable() throws FileNotFoundException {
        boolean repeatCheck = false;
        File checkFile;
        String frequencyTable;
        do {
            if (repeatCheck) {
                System.out.println("\nUnable to find file.  Please try again.\n");
            }
            System.out.println("\nPlease enter the input file of the frequency table");
            frequencyTable = inputReader.nextLine();
            checkFile = new File(frequencyTable);
            repeatCheck = true;
        } while (!checkFile.exists());
        this.frequencyTable = new BufferedReader(new FileReader(frequencyTable));
    }

    // Retrieve the input file
    private void getInputFile(String type) throws FileNotFoundException {
        boolean repeatCheck = false;
        File checkFile;
        String inputFile;
        do {
            if (repeatCheck) {
                System.out.println("\nUnable to find file.  Please try again.\n");
            }
            System.out.println("\nPlease enter the source for the file with " + type + ": ");
            inputFile = inputReader.nextLine();
            checkFile = new File(inputFile);
            repeatCheck = true;
        } while (!checkFile.exists());
        switch (type) {
            case "codes":
                this.codes = new BufferedReader(new FileReader(inputFile));
                break;
            case "phrases":
                this.phrases = new BufferedReader(new FileReader(inputFile));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    // If the user is inputting a file of phrases, determine
    // whether the user would like to use one predefined frequency table for all,
    // or instead build a custom frequency table for each.
    private void phrasesTable() {
        boolean repeatCheck = true;
        while (repeatCheck) {
            System.out.println("\nWould you like to " +
                    "\n1. Use a frequency table to encode your phrases, or" +
                    "\n2. Encode each phrase according to its unique set of character frequencies ");
            String choice = inputReader.nextLine();
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("\nInvalid choice.  Please try again\n");
            } else {
                repeatCheck = false;
                phrasesTable = choice.equals("1");
            }
        }
    }
}
