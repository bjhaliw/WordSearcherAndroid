package com.example.wordsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream stream = getResources().openRawResource(R.raw.words_alpha);

        // Word Search object, will perform the word searching
        WordSearcher searcher = new WordSearcher(stream);

        // The EditText fields containing user input
        EditText startsWith = (EditText) findViewById(R.id.startsWithTextField);
        EditText containsExact = (EditText) findViewById(R.id.containsExactTextField);
        EditText containsLetters = (EditText) findViewById(R.id.containsLettersTextField);
        EditText containsOnly = (EditText) findViewById(R.id.onlyContainsTextField);
        EditText endsWith = (EditText) findViewById(R.id.endsWithTextField);
        EditText wordLength = (EditText) findViewById(R.id.wordLengthTextField);

        // Search Button to start the process
        Button searchButton = (Button) findViewById(R.id.searchButton);

        // Scrollview to display the found words
        ScrollView view = (ScrollView) findViewById(R.id.scrollView);

        // When Search Button is pressed, initiate search
        searchButton.setOnClickListener(e -> {

            // Hide the keyboard if it's still up
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e1) {
                // Don't need to do anything since the keyboard is already hidden
            }


            // Convert wordLength EditText field from String to int
            String lengthString = wordLength.getText().toString();
            int length;

            try {
                length = Integer.parseInt(wordLength.getText().toString());
            } catch (NumberFormatException e2) {
                length = 0;
            }

            if (startsWith.getText().toString().equals("")
                    && containsExact.getText().toString().equals("")
                    && containsLetters.getText().toString().equals("")
                    && containsOnly.getText().toString().equals("")
                    && endsWith.getText().toString().equals("")
                    && wordLength.getText().toString().equals("")) {

                System.out.println("No input");

            } else {
                String result = searcher.findWord(
                        startsWith.getText().toString().trim(),
                        containsExact.getText().toString().trim(),
                        containsLetters.getText().toString().trim(),
                        containsOnly.getText().toString().trim(),
                        endsWith.getText().toString().trim(),
                        length);

                String[] resultSplit = result.split("\n");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                for (String s : resultSplit) {
                    TextView textView = new TextView(this);
                    textView.setText(s);
                    layout.addView(textView);
                }

                view.removeAllViews();

                view.addView(layout);

                view.scrollTo(0, 0);
            }


        });

    }
}