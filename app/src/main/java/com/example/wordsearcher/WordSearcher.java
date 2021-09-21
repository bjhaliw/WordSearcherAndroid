package com.example.wordsearcher;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class WordSearcher {

    // Key: A-Z, Value: Words starting with Key letter
    private HashMap<String, ArrayList<String>> wordMap;
    private TreeMap<Integer, ArrayList<String>> treeMap;
    private ArrayList<String> wordList;

    public WordSearcher(InputStream stream) {
        //wordMap = new HashMap<>();
        this.wordList = new ArrayList<>();
        loadWordFile(stream);
    }

    public String findWord(String startsWith, String containsExact, String containsLetters,
                           String containsOnly, String endsWith, int wordLength) {

        StringBuilder sb = new StringBuilder();
        String currWord;

        for (int i = 0; i < this.wordList.size(); i++) {
            currWord = this.wordList.get(i);

            if (currWord.startsWith(startsWith) && currWord.contains(containsExact) &&
                    doesWordContainLetters(currWord, containsLetters) && currWord.endsWith(endsWith)) {

                sb.append(currWord + "\n");
            }
        }

        return sb.toString();
    }

    private boolean doesWordContainLetters(String currWord, String containsLetters) {
        if (containsLetters.length() == 0) {
            return true;
        }

        String[] arr = currWord.split("");
        String tempLetters = containsLetters;
        int counter = 0;


        for (int i = 0; i < arr.length; i++) {
            if (tempLetters.contains(arr[i])) {
                tempLetters = tempLetters.replaceFirst(arr[i], "");
                counter++;
            }

            if (counter == containsLetters.length()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Loads the HashMaps containing the word list in the res/raw/alpha_words.txt file
     *
     * @param stream - InputStream to the text file location
     */
    private void loadWordFile(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        while (scanner.hasNextLine()) {
            this.wordList.add(scanner.nextLine());
        }

        scanner.close();
    }

}
