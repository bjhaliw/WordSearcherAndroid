package com.example.wordsearcher;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class WordSearcher {

    // Key: A-Z, Value: Words starting with Key letter

    private TreeMap<Integer, ArrayList<String>> treeMap;
    private StringBuilder sb;
    private HashMap<String, ArrayList<String>> startsWithMap;
    private HashMap<String, ArrayList<String>> endsWithMap;
    private ArrayList<String> wordList;

    public WordSearcher(InputStream stream) {
        this.wordList = new ArrayList<>();
        this.startsWithMap = new HashMap<>();
        this.endsWithMap = new HashMap<>();
        loadWordFile(stream);
    }

    /**
     * Main search function for the WordSearcher class. Loops through the entire word list if
     * the user did not supply any of the TextFields.
     * @param letters
     * @param startsWith
     * @param contains
     * @param endsWith
     * @param wordLength
     */
    public void findWord(String letters, String startsWith, String contains,
                         String endsWith, int wordLength) {

        this.treeMap = new TreeMap<>();
        this.sb = new StringBuilder();

        String currWord;

        // Iterate through the word list
        for (int i = 0; i < this.wordList.size(); i++) {
            currWord = this.wordList.get(i);

            if (doesWordContainLetters(currWord, letters) && currWord.startsWith(startsWith)
                    && currWord.contains(contains) && currWord.endsWith(endsWith)) {

                addWordToLists(currWord, wordLength);
            }
        }
    }

    /**
     * Helper method to determine if a word contains some letters in arbitrary positions. Can contain
     * duplicate letters.
     *
     * @param currWord        - The current word to check
     * @param containsLetters - The specified letters that may be in the word in any order
     * @return
     */
    private boolean doesWordContainLetters(String currWord, String containsLetters) {

        // User didn't include this field, automatically return true
        if (containsLetters.length() == 0) {
            return true;
        }

        // Can't have a word larger than the number of given letters
        if (currWord.length() > containsLetters.length()) {
            return false;
        }

        // Split the the current word into an array
        String[] arr = currWord.split("");

        String tempLetters = containsLetters;
        int counter = 0;


        for (int i = 0; i < arr.length; i++) {
            if (tempLetters.contains(arr[i])) {
                tempLetters = tempLetters.replaceFirst(arr[i], "");
                counter++;
            } else {
                return false;
            }

            if (counter == containsLetters.length()) {
                return true;
            }
        }

        if (counter > 0 && counter <= containsLetters.length()) {
            return true;
        }

        return false;
    }


    /**
     * Helper method to either add the current word to a TreeMap if the length is 0, or append
     * it to the StringBuilder if a length is specified
     *
     * @param currWord - The current word to add to the running track of words
     * @param length   - The specified target length of the word
     */
    private void addWordToLists(String currWord, int length) {

        // User didn't specify a length, sort by length
        if (length == 0) {
            if (treeMap.containsKey(currWord.length())) {
                treeMap.get(currWord.length()).add(currWord);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(currWord);
                treeMap.put(currWord.length(), list);
            }
        } else {
            if (currWord.length() == length) {
                this.sb.append(currWord + "\n");
            }
        }
    }

    /**
     * Loads the ArrayList<String> containing the word list in the res/raw/alpha_words.txt file
     *
     * @param stream - InputStream to the text file location
     */
    private void loadWordFile(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        String currWord;

        while (scanner.hasNextLine()) {
            currWord = scanner.nextLine();

            // Add current word to overall list
            this.wordList.add(currWord);
        }

        scanner.close();
    }

    public TreeMap<Integer, ArrayList<String>> getTreeMap() {
        return this.treeMap;
    }

    public StringBuilder getSb() {
        return sb;
    }
}
