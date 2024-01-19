// == FILE LOCATION =========
package com.example.uitester;

// == IMPORTS ==================================
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// == STATS MENU =====================================
public class StatsActivity extends AppCompatActivity {

    // [FIELD] File address for the statistics file.
    private final String STAT_FILE_ADDRESS = "stats.txt";

    // [FIELD] All the text areas which display the stats.
    TextView totalPlayTimeDisplay, totalGuessesMadeDisplay, totalGamesPlayedDisplay;
    TextView highestScoreDisplay, averageGuessesDisplay, averageTimeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // [LAYOUT] Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        clearSystemUI();

        // [DISPLAYS] Wire every TextView to a variable.
        setupDisplays();

    }


    // ==================================
    // == MENU BUTTON FUNCTIONS =========
    // ==================================


    // Parameters: View | Requirement for button-activation.
    // Description: Opens popup, resets text in stat file, redraws TextViews.
    public void resetStats(View view) {

        ImageButton resetButton = findViewById(R.id.resetButton);
        resetButton.setImageResource(R.drawable.reset_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetButton.setImageResource(R.drawable.reset);

                // [POPUP] Begins construction of the confirmation popup.
                Dialog confirmationScreen = new Dialog(StatsActivity.this);
                confirmationScreen.setContentView(R.layout.confirmation_screen);
                confirmationScreen.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                confirmationScreen.setCancelable(false);

                // [POPUP-NO] Creates the no button on the popup screen.
                ImageButton noButton = confirmationScreen.findViewById(R.id.noButton);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noButton.setImageResource(R.drawable.no_pressed);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noButton.setImageResource(R.drawable.no);

                                confirmationScreen.cancel(); // [CLARITY] Closes the popup.

                            }
                        }, 80);
                    }
                });

                // [POPUP-YES] Creates the yes button on the popup screen.
                ImageButton yesButton = confirmationScreen.findViewById(R.id.yesButton);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        yesButton.setImageResource(R.drawable.yes_pressed);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noButton.setImageResource(R.drawable.yes);

                                clearStatFile(); // [CLARITY] Clears stat file.
                                updateDisplays(); // [CLARITY] "Redraws" the shown stats.
                                confirmationScreen.cancel(); // [CLARITY] Closes the popup.

                            }
                        }, 80); // Delay to see the icon change
                    }
                });

                confirmationScreen.show(); // [CLARITY] Actually shows the popup.

            }
        }, 80);

    }

    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the gotcha button image closes the menu.
    public void closeMenu(View view) {

        ImageButton gotchaButton = findViewById(R.id.gotchaButtonStat);
        gotchaButton.setImageResource(R.drawable.gotcha_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotchaButton.setImageResource(R.drawable.gotcha);

                finish(); // [CLARITY] This just closes this menu screen.

            }
        }, 80);
    }


    // ==================================
    // == BACK-END OPERATION FUNCTIONS ==
    // ==================================


    // Parameters: None | Uses static variable access.
    // Description: Sets up the TextView variables with their UI counterparts.
    private void setupDisplays() {

        // [CLARITY] Initializes every TextView variable.
        totalPlayTimeDisplay = findViewById(R.id.totalPlayTimeDisplay);
        totalGuessesMadeDisplay = findViewById(R.id.totalGuessesMadeDisplay);
        totalGamesPlayedDisplay = findViewById(R.id.totalGamesPlayedDisplay);
        highestScoreDisplay = findViewById(R.id.highestScoreDisplay);
        averageGuessesDisplay = findViewById(R.id.averageGuessesDisplay);
        averageTimeDisplay = findViewById(R.id.averageTimeDisplay);

        updateDisplays(); // [CLARITY] After initializing, show the stats.

    }

    // Parameters: None | Uses static variable access.
    // Description: Updates the TextViews to show the stats.
    private void updateDisplays() {

        // [CLARITY] Reads the file.
        String[] rawData = statFileEmpty().split(" ");

        // [CLARITY] Formats and sets the text of the TextViews.
        totalPlayTimeDisplay.setText(formatTime(rawData[0]));
        totalGuessesMadeDisplay.setText(formatBasicStat(rawData[1]));
        totalGamesPlayedDisplay.setText(formatBasicStat(rawData[2]));
        highestScoreDisplay.setText(formatScore(rawData[3]));
        averageGuessesDisplay.setText(formatBasicStat(rawData[4]));

        // [CLARITY] .replace() needed because of how file writing works.
        averageTimeDisplay.setText(formatTime(rawData[5].replace("\n", "")));

    }

    // Parameters: String | Time in MM:SS format.
    // Description: Converts MM:SS to MMm SSs.
    private String formatTime(String unformattedTime) {

        String formattedTime = "";

        // [CLARITY] Extract the given info.
        int minutes = Integer.parseInt((unformattedTime.split(":"))[0]);
        int seconds = Integer.parseInt((unformattedTime.split(":"))[1]);

        // [MINUTES] Format minutes.
        if (minutes < 10) {
            formattedTime += ("0" + minutes);
        }
        else {
            formattedTime += String.valueOf(minutes);
        }

        formattedTime += "m ";

        // [SECONDS] Format seconds.
        if (seconds < 10) {
            formattedTime += ("0" + seconds);
        }
        else {
            formattedTime += String.valueOf(seconds);
        }

        formattedTime += "s";

        // [RETURN] Return the final formatted time.
        return formattedTime;

    }

    // Parameters: String | One or two digit number.
    // Description: Converts to two digit sequence: ex. 1 --> 01.
    private String formatBasicStat(String unformattedTwoDigitNumber) {

        if (Integer.parseInt(unformattedTwoDigitNumber) < 10) {
            return "0" + unformattedTwoDigitNumber;
        }
        else {
            return unformattedTwoDigitNumber;
        }

    }

    // Parameters: String | Some digit number.
    // Description: Converts to four digit sequence: ex. 123 --> 0123.
    private String formatScore(String unformattedScore) {

        if (Integer.parseInt(unformattedScore) < 10) {
            return "000" + unformattedScore;
        }
        else if (Integer.parseInt(unformattedScore) < 100) {
            return "00" + unformattedScore;
        }
        else if (Integer.parseInt(unformattedScore) < 1000) {
            return "0" + unformattedScore;
        }
        else {
            return unformattedScore;
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

    // Parameters: None | Uses static variable access.
    // Description: Resets the stat file with all 0 values.
    private void clearStatFile() {

        // [NOTE] I used a video tutorial for the file IO; not 100% sure of the processes used.

        FileOutputStream fileOutputStream = null;

        try {
            // [CLARITY] Initialize fileOutputStream to write the stat file.
            fileOutputStream = openFileOutput(STAT_FILE_ADDRESS, MODE_PRIVATE);
            fileOutputStream.write("0:0 0 0 0 0 0:0".getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // [CLARITY] If fileOutputStream was opened: close it.
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // Parameters: None | Uses static variable access.
    // Description: Returns a string; what was read from the stat file.
    private String statFileEmpty() {

        // [NOTE] I used a video tutorial for the file IO; not 100% sure of the processes used.

        FileInputStream fileInputStream = null;

        try {

            // [CLARITY] Creates objects necessary to read the file.
            fileInputStream = openFileInput(STAT_FILE_ADDRESS);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String textToLoad;

            // [CLARITY] Reads the text in the file; stores in a String.
            while ((textToLoad = bufferedReader.readLine()) != null) {
                stringBuilder.append(textToLoad).append("\n");
            }

            // [CLARITY] Will return whatever was inside the file.
            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // [CLARITY] If fileInputStream was opened: close it.
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

}