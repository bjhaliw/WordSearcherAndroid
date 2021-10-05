package com.example.wordsearcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

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
        InputStream stream = getResources().openRawResource(R.raw.word_list);

        // Word Search object, will perform the word searching
        WordSearcher searcher = new WordSearcher(stream);

        // TextFields for user input
        TextInputEditText letters = findViewById(R.id.lettersFieldText);
        TextInputEditText starts = findViewById(R.id.startsWithFieldText);
        TextInputEditText contains = findViewById(R.id.containsExactFieldText);
        TextInputEditText ends = findViewById(R.id.endsWithFieldText);
        TextInputEditText length = findViewById(R.id.worldLengthFieldText);

        // Search Button to start the process
        Button searchButton = findViewById(R.id.searchButton);

        // Scrollview to display the found words
        ScrollView view = findViewById(R.id.scrollView);

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

                view.removeAllViews();

                String[] split = searcher.getSb().toString().split("\n");
                TreeMap<Integer, ArrayList<String>> map = searcher.getTreeMap();

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                TextView textView = new TextView(this);

                if (searcher.getNumWords() == 0) {
                    textView.setText("No words found.");
                    layout.addView(textView);

                } else {
                    if (map.size() == 0) {
                        for (String s : split) {
                            textView.setText(s);
                            layout.addView(textView);
                        }
                    } else {
                        for (Map.Entry<Integer, ArrayList<String>> value : map.entrySet()) {
                            TextView num = new TextView(this);
                            num.setText("Words of Length: " + value.getKey());
                            layout.addView(num);
                            for (String s : value.getValue()) {
                                TextView word = new TextView(this);
                                word.setText(s);
                                layout.addView(word);
                            }

                            TextView space = new TextView(this);
                            space.setText("\n");
                            layout.addView(space);
                        }
                    }
                }
                view.addView(layout);
                view.scrollTo(0, 0);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Use the fields above to search", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_icon:
                startActivity(new Intent(MainActivity.this, CreditsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}