package com.example.uitester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameplayActivity extends AppCompatActivity {

    // [FEILD] User stats.
    private int totalGuesses;
    private long startTime;
    private long endTime;

    // [FEILD] Correct number variables.
    private int numDigitsInCorrectNumber;
    private String correctNumber; // String is required for "0123" possibility.
    private ArrayList<Character> correctNumberCharacters;

    // [FEILD] Guess number variables.
    private TextView guessDisplay;
    private String guess;

    // [FEILD] Feedback variables.
    private int[] guessFeedback = new int[2]; // [CLARITY] Where the feedback will be stored.
    private final int BULLS = 0; // [CLARITY] This is just for the code to be more readable.
    private final int COWS = 1; // [CLARITY] This is just for the code to be more readable.

    // [FEILD] Miscellanious.
    private TextView warningMessageDisplay;
    private TextView feedbackText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearSystemUI();
        setContentView(R.layout.activity_gameplay);
        setupGameVariables();
        setupViews();

    }

    private void setupViews() {

        guessDisplay = findViewById(R.id.guessDisplay);
        guessDisplay.setText("#" + " #".repeat(numDigitsInCorrectNumber - 1));

        warningMessageDisplay = findViewById(R.id.warningDisplay);
        warningMessageDisplay.setTextColor(Color.RED);
//        correctNumberDisplay = findViewById(R.id.correctNumberDisplay);
//        resultsDisplay = findViewById(R.id.resultsDisplay);
        feedbackText = findViewById(R.id.feedbackText);
        feedbackText.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setupGameVariables() {

        // [CLARITY] Takes the passed 'numberOfDigits' from the GameSettings class.
        numDigitsInCorrectNumber = getIntent().getIntExtra("numDigits", 4);
        correctNumber = generateRandomNumber(numDigitsInCorrectNumber);


         if (getIntent().getBooleanExtra("showCorrectNumber", false)) {
             // TODO: remove this =====
             TextView correctNumberDisplay = findViewById(R.id.correctNumberDisplay); // check if shown was true, etc

             for (int symbolIndex = 0; symbolIndex < correctNumber.length(); symbolIndex++) {
                 if (symbolIndex != 0) {
                     correctNumberDisplay.append("  " + correctNumber.charAt(symbolIndex));
                 } else {
                     correctNumberDisplay.setText(String.valueOf(correctNumber.charAt(0)));
                 }
             }
         }
        // TODO | == TO REMOVE =======================================

        // [STATS] Initializes user stats.
        totalGuesses = 0;
        guess = "";

        // [TIME] Takes note of start time; will be used later.
        startTime = System.currentTimeMillis();

        // [THOUGHT] In a TUI, this is where the while loop would start.
    }

    private void addSymbolToGuess(char symbol) {

        // [CLARITY] Is there already enough digits in the guess?
        if (guess.length() < numDigitsInCorrectNumber) {

            guess += symbol; // [CLARITY] Concatenates the chosen number to the guess.

            String textToDisplay = "";

            // do add 0th element here then do the rest in the loop

            for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
                if (symbolIndex != 0) {
                    textToDisplay += " " + guess.charAt(symbolIndex);
                } else {
                    textToDisplay += guess.charAt(symbolIndex);
                }
            }

            guessDisplay.setText(textToDisplay + " #".repeat(numDigitsInCorrectNumber - guess.length()));

        }

        warningMessageDisplay.setText("");
    }

    public void closeMenu(View view) {
        ImageButton quitButton = findViewById(R.id.quitButton);
        quitButton.setImageResource(R.drawable.quit_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quitButton.setImageResource(R.drawable.quit);
                finish();
            }
        }, 80); // Delay to see the icon change
    }
    public void submit(View view) {

        ImageButton submitButton = findViewById(R.id.submitButton);
        submitButton.setImageResource(R.drawable.submit_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitButton.setImageResource(R.drawable.submit);

                        // [CLARITY] Checks if there's enough digits in the guess to process it.
                        if (guess.length() == numDigitsInCorrectNumber) {

                            // [FEEDBACK] Gets the feedback & Writes it in the feedbackText.
                            guessFeedback = getGuessFeedback(); // [THOUGHT] "Feedback is the breakfast of champions!"
                            feedbackText.append("\n\n" + formatFeedback(guessFeedback));

                            totalGuesses++; // [CLARITY] Updates the total guesses took.

                            // [WIN CONDITION] Calls on the win condition.
                            if (guessFeedback[BULLS] == numDigitsInCorrectNumber) {
                                userWinsGame();
                            }

                            // [RESET] Reset the guess for next turn.
                            guessDisplay.setText("#" + " #".repeat(numDigitsInCorrectNumber - 1));
                            guess = "";


                        }
                        else {
                            warningMessageDisplay.setText("Submission Error: \nNOT ENOUGH NUMBERS");
                        }

            }
        }, 70); // Delay to see the icon change

    }

    public void backspace(View view) {

        ImageButton backspaceButton = findViewById(R.id.backspaceButton);
        backspaceButton.setImageResource(R.drawable.backspace_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backspaceButton.setImageResource(R.drawable.backspace);

                // [CLARITY] Checks that the guess has more than 0 numbers in the guess.
                        if (guess.length() > 0) {

                            // [CLARITY] Updates the current guess without its last digit.

                            guess = guess.substring(0, (guess.length() - 1));

                            try {
                                String textToDisplay = "";

                                // do add 0th element here then do the rest in the loop

                                for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
                                    if (symbolIndex != 0) {
                                        textToDisplay += " " + guess.charAt(symbolIndex);
                                    }
                                    else {
                                        textToDisplay += guess.charAt(symbolIndex);
                                    }
                                }

                                if (guess.length() == 0) {
                                    guessDisplay.setText(("# ".repeat(numDigitsInCorrectNumber)).substring(0, numDigitsInCorrectNumber * 2 - 1));
                                }
                                else {
                                    guessDisplay.setText(textToDisplay + " #".repeat(numDigitsInCorrectNumber - guess.length()));
                                }
                            }
                            catch(Exception e) {
                                warningMessageDisplay.setText("BACKSPACE ERROR: CATCH REACHED");
                            }

//                             [CLARITY] Will update the guess length warning.
                            warningMessageDisplay.setText("");
                        }


            }
        }, 70); // Delay to see the icon change
    }

    private String formatFeedback(int[] feedback) {

        String formattedFeedback;

        if ((totalGuesses + 1) < 10) {
            formattedFeedback = " " + (totalGuesses + 1) + "   ";

        }
        else {
            formattedFeedback = (totalGuesses + 1) + "   ";
        }

        for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
            formattedFeedback += " " + guess.charAt(symbolIndex);
        }

        formattedFeedback += " | " + feedback[BULLS] + "B " + feedback[COWS] + "C";

        return formattedFeedback;
    }

    private String generateRandomNumber(int numDigits) {
        Random numberGenerator = new Random();
        String randomNumber = "";

        // [THOUGHT] Doing each digit individually allows for:
        // 1. Numbers to start with 0. Example | "0123".
        // 2. More 'random' numbers than if generating a number from 1000 to 9999.

        // [THOUGHT] Having each digit be unique allows for:
        // 1. Better user experience.

        // [LOOP] Loops through each digit generation numDigit times.
        for (int i = 0; i < numDigits; i++)
        {
            int randomDigit;

            randomDigit = numberGenerator.nextInt(10);

            // [CLARITY] This checks whether the new randomDigit already appears in the generating number.
            if (randomNumber.contains(String.valueOf(randomDigit)))
            {
                numDigits++; // [CLARITY] This will have the for loop go on one more time.
            }
            else
            {
                randomNumber += randomDigit; // Appends this random digit to the randomnumber.
            }
        }

        return randomNumber;

    }

    public int[] getGuessFeedback() {

        int[] guessFeedback = {0, 0};

        // [LOOP] Go through the each digit and compare between the guess and correct numbers.
        for (int digitIndex = 0; digitIndex < String.valueOf(guess).length(); digitIndex++)
        {
            // [CONDITION] Is the guessed digit even in the correct number?
            if (correctNumber.contains(String.valueOf(guess.charAt(digitIndex))))
            {
                // [CONDITION] Is the digit in the same spot as the digit in the correct number?
                if (guess.charAt(digitIndex) == correctNumber.charAt(digitIndex))
                {
                    guessFeedback[BULLS]++; // [REMINDER] Bulls refers to the value at index 0.
                }
                else
                {
                    guessFeedback[COWS]++; // [REMINDER] Cows refers to the value at index 1.
                }
            }
        }

        return guessFeedback;

    }


    private void userWinsGame() {

        // [TIME] Takes note of end time; then processes it.
        endTime = System.currentTimeMillis();
        int secondsElapsed = Math.round((endTime - startTime) / 1000);
        String formattedTime = formatTime(secondsElapsed);

        // [SCORE] Scuffly calculates the score based off: Guesses needed, time needed (in seconds), and number of digits.
        int score = (int) (((15.0 / (1.0 * totalGuesses)) + (10.0 / (1.0 * secondsElapsed))) * 200.0 * (numDigitsInCorrectNumber - 1));

        Dialog winScreen = new Dialog(GameplayActivity.this);
        winScreen.setContentView(R.layout.win_screen);
        winScreen.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        winScreen.setCancelable(false);

        if (getIntent().getBooleanExtra("showCorrectNumber", false) || getIntent().getBooleanExtra("dontSaveScore", false)) {
            TextView dataNotSavedDisplay = winScreen.findViewById(R.id.dataNotSavedDisplay);
            dataNotSavedDisplay.setText("THIS DATA IS NOT SAVED!");
        }
        else {
            saveData();
        }



        TextView timeDisplay = winScreen.findViewById(R.id.timeDisplay);
        timeDisplay.setText(formattedTime);

        TextView displayTotalGuesses = winScreen.findViewById(R.id.totalGuessesDisplay);
        displayTotalGuesses.setText(String.valueOf(totalGuesses));

        TextView displayScore = winScreen.findViewById(R.id.scoreDisplay);
        displayScore.setText(String.valueOf(score));

        ImageButton closePopupButton = winScreen.findViewById(R.id.closePopup);
        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupButton.setImageResource(R.drawable.gotcha_pressed);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closePopupButton.setImageResource(R.drawable.gotcha);
                        finish();
                    }
                }, 80); // Delay to see the icon change
            }
        });



        winScreen.show();
    }


    private String formatTime(int seconds) {

        // [CLARITY] If there's only seconds, it will look like "#s".
        if (seconds < 60) {
            return (seconds + "s"); // [EXAMPLE] | "23s"
        }

        // [CLARITY] If there's both minutes and seconds, it will look like "#m #s".
        else
        {
            return (seconds / 60) + "m " + (seconds % 60) + "s"; // [EXAMPLE] | "1m 23s"
        }

    }

    private void clearSystemUI() {

        // Gets rid of the status bar / makes it transparent
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Gets rid of the nav bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void saveData() {

    }

    public void addOne(View view) {

        ImageButton buttonOne = findViewById(R.id.buttonOne);
        buttonOne.setImageResource(R.drawable.num_one_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonOne.setImageResource(R.drawable.num_one);
                addSymbolToGuess('1');
            }
        }, 70); // Delay to see the icon change
    }

    public void addTwo(View view) {

        ImageButton buttonTwo = findViewById(R.id.buttonTwo);
        buttonTwo.setImageResource(R.drawable.num_two_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonTwo.setImageResource(R.drawable.num_two);
                addSymbolToGuess('2');
            }
        }, 70); // Delay to see the icon change
    }

    public void addThree(View view) {

        ImageButton buttonThree = findViewById(R.id.buttonThree);
        buttonThree.setImageResource(R.drawable.num_three_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonThree.setImageResource(R.drawable.num_three);
                addSymbolToGuess('3');
            }
        }, 70); // Delay to see the icon change
    }

    public void addFour(View view) {

        ImageButton buttonFour = findViewById(R.id.buttonFour);
        buttonFour.setImageResource(R.drawable.num_four_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonFour.setImageResource(R.drawable.num_four);
                addSymbolToGuess('4');
            }
        }, 70); // Delay to see the icon change
    }

    public void addFive(View view) {

        ImageButton buttonFive = findViewById(R.id.buttonFive);
        buttonFive.setImageResource(R.drawable.num_five_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonFive.setImageResource(R.drawable.num_five);
                addSymbolToGuess('5');
            }
        }, 70); // Delay to see the icon change
    }

    public void addSix(View view) {

        ImageButton buttonSix = findViewById(R.id.buttonSix);
        buttonSix.setImageResource(R.drawable.num_six_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonSix.setImageResource(R.drawable.num_six);
                addSymbolToGuess('6');
            }
        }, 70); // Delay to see the icon change
    }

    public void addSeven(View view) {

        ImageButton buttonSeven = findViewById(R.id.buttonSeven);
        buttonSeven.setImageResource(R.drawable.num_seven_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonSeven.setImageResource(R.drawable.num_seven);
                addSymbolToGuess('7');
            }
        }, 70); // Delay to see the icon change
    }

    public void addEight(View view) {

        ImageButton buttonEight = findViewById(R.id.buttonEight);
        buttonEight.setImageResource(R.drawable.num_eight_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonEight.setImageResource(R.drawable.num_eight);
                addSymbolToGuess('8');
            }
        }, 70); // Delay to see the icon change
    }

    public void addNine(View view) {

        ImageButton buttonNine = findViewById(R.id.buttonNine);
        buttonNine.setImageResource(R.drawable.num_nine_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonNine.setImageResource(R.drawable.num_nine);
                addSymbolToGuess('9');
            }
        }, 70); // Delay to see the icon change
    }

    public void addZero(View view) {

        ImageButton buttonZero = findViewById(R.id.buttonZero);
        buttonZero.setImageResource(R.drawable.num_zero_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonZero.setImageResource(R.drawable.num_zero);
                addSymbolToGuess('0');
            }
        }, 70); // Delay to see the icon change
    }

}