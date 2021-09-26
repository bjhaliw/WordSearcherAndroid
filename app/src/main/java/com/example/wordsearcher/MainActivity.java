package com.example.wordsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Open up the text file containing the words to search
        InputStream stream = getResources().openRawResource(R.raw.words_alpha);

        // Word Search object, will perform the word searching
        WordSearcher searcher = new WordSearcher(stream);

        // TextFields for user input
        TextInputEditText letters = (TextInputEditText) findViewById(R.id.lettersFieldText);
        TextInputEditText starts = (TextInputEditText) findViewById(R.id.startsWithFieldText);
        TextInputEditText contains = (TextInputEditText) findViewById(R.id.containsExactFieldText);
        TextInputEditText ends = (TextInputEditText) findViewById(R.id.endsWithFieldText);
        TextInputEditText length = (TextInputEditText) findViewById(R.id.worldLengthFieldText);

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
            String lengthString = length.getText().toString();
            int numLength;

            try {
                numLength = Integer.parseInt(lengthString);
            } catch (NumberFormatException e2) {
                numLength = 0;
            }

            String lettersString = letters.getText().toString();
            String startsString = starts.getText().toString();
            String containsString = contains.getText().toString();
            String endsString = ends.getText().toString();

            // Make sure at least one field was entered
            if (!lettersString.matches("") || !startsString.matches("") || !containsString.matches("") || !endsString.matches("") || !lengthString.matches("")) {

                searcher.findWord(letters.getText().toString(), starts.getText().toString(), contains.getText().toString(), ends.getText().toString(), numLength);

            }

            String[] split = searcher.getSb().toString().split("\n");
            TreeMap<Integer, ArrayList<String>> map = searcher.getTreeMap();

            System.out.println(map);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            if (map.size() == 0) {
                for (String s : split) {
                    TextView textView = new TextView(this);
                    textView.setText(s);
                    layout.addView(textView);
                }
            } else {
                System.out.println("Looking at the tree map");
                for (Map.Entry<Integer, ArrayList<String>> value : map.entrySet()) {
                    TextView num = new TextView(this);
                    num.setText("Words of Length: " + value.getKey());
                    layout.addView(num);
                    for (String s : value.getValue()) {
                        System.out.println("Curr Word: " + s);
                        TextView word = new TextView(this);
                        word.setText(s);
                        layout.addView(word);
                    }

                    TextView space = new TextView(this);
                    space.setText("\n");
                    layout.addView(space);
                }
            }


            view.removeAllViews();

            view.addView(layout);

            view.scrollTo(0, 0);

        });

    }
}