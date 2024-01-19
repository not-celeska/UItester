// == FILE LOCATION =========
package com.example.uitester;

// == IMPORTS ==================================
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

// == GAMEPLAY SCREEN ===================================
public class GameplayActivity extends AppCompatActivity {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    // [FIELD] Game stats.
    private int guessesTaken;
    private long startTime;
    private long endTime;

    // [FIELD] Correct number variables.
    private int numDigitsInCorrectNumber;
    private String correctNumber; // String is required for "0123" possibility.

    // [FIELD] Guess number variables.
    private TextView guessDisplay;
    private String guess;

    // [FIELD] Feedback variables.
    private int[] guessFeedback = new int[2]; // [CLARITY] Where the feedback will be stored.
    private final int BULLS = 0; // [CLARITY] This is just for the code to be more readable.
    private final int COWS = 1; // [CLARITY] This is just for the code to be more readable.

    // [FIELD] Miscellaneous.
    private final String STAT_FILE_ADDRESS = "stats.txt";
    private TextView warningMessageDisplay;
    private TextView feedbackText;


    // ==================================
    // == CLASS METHODS [FUNCTIONS] =====
    // ==================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // [LAYOUT] Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        clearSystemUI();

        // [SETUP] Setup game.
        setupGameVariables();
        setupViews();

    }


    // ==================================
    // == SETUP METHODS =================
    // ==================================

    // Parameters: None | Uses access to class variables.
    // Description: Wires the text fields to variables in this class.
    private void setupViews() {

        guessDisplay = findViewById(R.id.guessDisplay);
        guessDisplay.setText("#" + " #".repeat(numDigitsInCorrectNumber - 1));
        warningMessageDisplay = findViewById(R.id.warningDisplay);
        warningMessageDisplay.setTextColor(Color.RED);
        feedbackText = findViewById(R.id.feedbackText);
        feedbackText.setMovementMethod(new ScrollingMovementMethod()); // [CLARITY] Sets the text field to be scrollable.

    }

    // Parameters: None | Uses access to class variables.
    // Description: Generates random number, notes start time, sets total guesses to 0.
    private void setupGameVariables() {

        // [CORRECT NUMBER] Takes the passed 'numberOfDigits' from the GameSettings class.
        numDigitsInCorrectNumber = getIntent().getIntExtra("numDigits", 4);
        correctNumber = generateRandomNumber(numDigitsInCorrectNumber);

        // [SHOW CORRECT NUMBER] Will show the correct number if the user checked the box in the settings.
        if (getIntent().getBooleanExtra("showCorrectNumber", false)) {

            // [TEXT FIELD] Finds and saves the display.
            TextView correctNumberDisplay = findViewById(R.id.correctNumberDisplay);

            // [CORRECT NUMBER SHOW] Sets the text in the text field to a formatted correct number.
            for (int symbolIndex = 0; symbolIndex < correctNumber.length(); symbolIndex++) {
                if (symbolIndex != 0) {
                    correctNumberDisplay.append("  " + correctNumber.charAt(symbolIndex));
                }
                else {
                    correctNumberDisplay.setText(String.valueOf(correctNumber.charAt(0)));
                }

                // [RESULT] "1234" --> "1 2 3 4"

            }

        }

        // [STATS] Initializes user stats.
        guessesTaken = 0;
        guess = "";

        // [TIME] Takes note of start time; will be used later.
        startTime = System.currentTimeMillis();

        // [THOUGHT] In a TUI, this is where the while loop would start.
    }


    // ==================================
    // == GAMEPLAY METHODS ==============
    // ==================================

    // Parameters: Char | Symbol to add to guess.
    // Description: Adds a given symbol to the guess string.
    private void addSymbolToGuess(char symbol) {

        // [CONDITION] Is there already enough digits in the guess?
        if (guess.length() < numDigitsInCorrectNumber) {

            guess += symbol; // [CONCATENATION] Concatenates the chosen number to the guess.

            String textToDisplay = "";

            for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
                if (symbolIndex != 0) {
                    textToDisplay += " " + guess.charAt(symbolIndex);
                } else {
                    textToDisplay += guess.charAt(symbolIndex);
                }
            }

            guessDisplay.setText(textToDisplay + " #".repeat(numDigitsInCorrectNumber - guess.length()));

        }

        // [CLARITY] Clears the warning.
        warningMessageDisplay.setText("");

    }

    // Parameters: None | Uses access to class variables.
    // Description: Formats feedback: (Turn 1) 1B2C --> " 1 |    1B  2C"
    private String formatFeedback(int[] feedback) {

        String formattedFeedback;

        if ((guessesTaken + 1) < 10) {
            formattedFeedback = " " + (guessesTaken + 1) + "   ";

        }
        else {
            formattedFeedback = (guessesTaken + 1) + "   ";
        }

        for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
            formattedFeedback += " " + guess.charAt(symbolIndex);
        }

        formattedFeedback += " | " + feedback[BULLS] + "B " + feedback[COWS] + "C";

        return formattedFeedback;

    }

    // Parameters: int | How many numbers in the correct
    // Description: Generates a string with [param] random digits.
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
                randomNumber += randomDigit; // Appends this random digit to the random number String.
            }
        }

        return randomNumber;

    }

    // Parameters: None | Uses access to class variables.
    // Description: Returns int[] with Bulls and Cows: [BULLS, COWS].
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

    // Parameters: None | Uses access to class variables.
    // Description: Calculates, displays, and saves game stats to file.
    private void userWinsGame() {

        // [TIME] Takes note of end time; then processes it.
        endTime = System.currentTimeMillis();
        int secondsElapsed = Math.round((endTime - startTime) / 1000);
        String formattedTime = formatTime(secondsElapsed);

        // [SCORE] Scuffly calculates the score based off: Guesses needed, time needed (in seconds), and number of digits.
        int score = (int) (((15.0 / (1.0 * guessesTaken)) + (10.0 / (1.0 * secondsElapsed))) * 200.0 * (numDigitsInCorrectNumber - 1));

        // [POPUP] Initializes the win popup display.
        Dialog winScreen = new Dialog(GameplayActivity.this);
        winScreen.setContentView(R.layout.win_screen);

        // [CLARITY] Sets popup size.
        winScreen.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        // [CLARITY] Clicking outside the popup will not close the popup.
        winScreen.setCancelable(false);

        // [SCORE MESSAGE] If correct number was shown (cheats) or "dont save score" setting was enabled, message will show up and stats will not be saved.
        if (getIntent().getBooleanExtra("showCorrectNumber", false) || getIntent().getBooleanExtra("dontSaveScore", false)) {
            TextView dataNotSavedDisplay = winScreen.findViewById(R.id.dataNotSavedDisplay);
            dataNotSavedDisplay.setText("THIS DATA IS NOT SAVED!");
        }
        else {
            saveData(secondsElapsed, score);
        }

        // [TEXTVIEW] Initialize and show time taken.
        TextView timeDisplay = winScreen.findViewById(R.id.timeDisplay);
        timeDisplay.setText(formattedTime);

        // [TEXTVIEW] Initialize and show guesses taken.
        TextView guessesTakenDisplay = winScreen.findViewById(R.id.guessesTakenDisplay);
        guessesTakenDisplay.setText(String.valueOf(guessesTaken));

        // [TEXTVIEW] Initialize and show score.
        TextView displayScore = winScreen.findViewById(R.id.scoreDisplay);
        displayScore.setText(String.valueOf(score));

        // [IMAGE-BUTTON] Initialize and set listener for close (gotcha) button.
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
                }, 80); 
            }
        });

        winScreen.show(); // [CLARITY] Show popup.

    }


    // ==================================
    // == BACK-END METHODS ==============
    // ==================================

    // Parameters: int | Total time taken in seconds.
    // Description: Returns String: 65 seconds --> "1m 5s"
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

    // Parameters: None | Actions *inside* affect the UI construction.
    // Description: Gets rid of system UI: Navigation & Status bars.
    private void clearSystemUI() {

        // [CLARITY] Gets rid of the top status bar.
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // [CLARITY] Gets rid of the bottom navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        
    }

    // Parameters: int(s) | Stats from the game which are not class variables.
    // Description: Updates the stats in the file with game's stats.
    private void saveData(int gameTotalSecondsPlayed, int gameScore) {

        String[] oldRawData = readStatFile().split(" ");
        String newData = "";

        // [TOTAL PLAYTIME] Extract old, add new, format MM:SS.
        int gameMinutes = gameTotalSecondsPlayed / 60;
        int gameSeconds = gameTotalSecondsPlayed % 60;
        int oldMinutes = Integer.parseInt(oldRawData[0].split(":")[0]);
        int oldSeconds = Integer.parseInt(oldRawData[0].split(":")[1]);
        int newMinutes = oldMinutes + gameMinutes + (gameSeconds + oldSeconds) / 60;
        int newSeconds = (gameSeconds + oldSeconds) % 60;
        newData += newMinutes + ":" + newSeconds + " ";

        // [TOTAL GUESSES MADE] Extract old, sum old and new, put back into file.
        int oldTotalGuesses = Integer.parseInt(oldRawData[1]);
        newData += (oldTotalGuesses + guessesTaken) + " ";

        // [TOTAL GAMES PLAYED] Extract old, put back (old + 1).
        int oldTotalGamesPlayed = Integer.parseInt(oldRawData[2]);
        newData += (oldTotalGamesPlayed + 1) + " ";

        // [HIGH SCORE] Extract old, check if score higher than saved high score, put back in.
        int oldHighscore = Integer.parseInt(oldRawData[3]);
        if (gameScore > oldHighscore) {
            newData += gameScore + " ";
        }
        else {
            // [CLARITY] If its not greater than, then just put the old one back.
            newData += oldHighscore + " ";
        }

        // [AVERAGE GUESSES] Take new total guesses and divide by new total games played.
        newData += (oldTotalGuesses + guessesTaken) / (oldTotalGamesPlayed + 1) + " ";

        // [AVERAGE TIME] Convert new time to seconds, average seconds, convert back to MM:SS.
        int totalSeconds = newSeconds + newMinutes * 60;
        int averageTotalSeconds = totalSeconds / (oldTotalGamesPlayed + 1);
        int averageMinutes = averageTotalSeconds / 60;
        int averageSeconds = averageTotalSeconds % 60;
        newData += averageMinutes + ":" + averageSeconds;

        // [SAVING] Save the new data to the file to replace the old one.
        saveToStatFile(newData);
        
    }

    // Parameters: None | Uses access to static variable.
    // Description: Returns the text inside the stat file.
    public String readStatFile() {

        // Disclaimer: same thing as the read from other classes.
        
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(STAT_FILE_ADDRESS);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String textToLoad;

            while ((textToLoad = bufferedReader.readLine()) != null) {
                stringBuilder.append(textToLoad).append("\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "FAIL";

    }

    // Parameters: String | Text to SAVE to the file.
    // Description: Saves given text to the "stats.txt" file.
    public void saveToStatFile(String textToSave) {
        
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(STAT_FILE_ADDRESS, MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // ==================================
    // == KEYPAD BUTTON METHODS =========
    // ==================================


    // Parameters: View | Requirement for button-activation.
    // Description: Closes the game screen.
    public void closeMenu(View view) {

        ImageButton quitButton = findViewById(R.id.quitButton);
        quitButton.setImageResource(R.drawable.quit_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quitButton.setImageResource(R.drawable.quit);

                finish(); // [CLARITY] Closes the gameplay menu (screen).

            }
        }, 80);

    }

    // Parameters: View | Requirement for button-activation.
    // Description: Checks if submission is valid, initiates feedback.
    public void submit(View view) {

        ImageButton submitButton = findViewById(R.id.submitButton);
        submitButton.setImageResource(R.drawable.submit_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitButton.setImageResource(R.drawable.submit);

                // [CONDITION] Checks if there's enough digits in the guess to process it.
                if (guess.length() == numDigitsInCorrectNumber) {

                    // [FEEDBACK] Gets the feedback & writes it in the feedbackText.
                    guessFeedback = getGuessFeedback(); // [THOUGHT] "Feedback is the breakfast of champions!"
                    feedbackText.append("\n\n" + formatFeedback(guessFeedback));

                    guessesTaken++; // [GUESSES TAKEN] Updates the total guesses took.

                    // [CONDITION] Calls on the win condition.
                    if (guessFeedback[BULLS] == numDigitsInCorrectNumber) {
                        userWinsGame();
                    }

                    // [RESET] Reset the guess and guess display for next turn.
                    guessDisplay.setText("#" + " #".repeat(numDigitsInCorrectNumber - 1));
                    guess = "";
                    
                }
                else {
                    warningMessageDisplay.setText("Submission Error: \nNOT ENOUGH NUMBERS");
                }

            }
        }, 70);

    }

    // Parameters: View | Requirement for button-activation.
    // Description: Removes last digit from guess (unless empty).
    public void backspace(View view) {

        ImageButton backspaceButton = findViewById(R.id.backspaceButton);
        backspaceButton.setImageResource(R.drawable.backspace_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backspaceButton.setImageResource(R.drawable.backspace);

                // [CONDITION] Checks that the guess has more than 0 numbers in the guess.
                if (guess.length() > 0) {

                    // [RECOMPOSE] Updates the current guess without its last digit.
                    guess = guess.substring(0, (guess.length() - 1));

                    try {
                        
                        String textToDisplay = "";

                        // [UPDATE GUESS DISPLAY] Formatted to be: 123 --> "1 2 3 # #".
                        for (int symbolIndex = 0; symbolIndex < guess.length(); symbolIndex++) {
                            if (symbolIndex != 0) {
                                textToDisplay += " " + guess.charAt(symbolIndex);
                            }
                            else {
                                textToDisplay += guess.charAt(symbolIndex);
                            }
                        }

                        // [IF GUESS IS NOW EMPTY] Format empty guess.
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

                    // [CLARITY] Will update the guess length warning.
                    warningMessageDisplay.setText("");
                }
                else {
                    warningMessageDisplay.setText("BACKSPACE ERROR: NO DIGITS");
                }
                
            }
        }, 70);
    }

    // == THE FOLLOWING 10 METHODS ===============================
    // Are the same; they only change a character passed into the
    // addSymbolToGuess method, they also change their respective icon.

    public void addOne(View view) {

        ImageButton buttonOne = findViewById(R.id.buttonOne);
        buttonOne.setImageResource(R.drawable.num_one_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonOne.setImageResource(R.drawable.num_one);
                addSymbolToGuess('1');
            }
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
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
        }, 70); 
    }

}