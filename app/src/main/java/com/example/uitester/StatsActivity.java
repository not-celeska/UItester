package com.example.uitester;

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

public class StatsActivity extends AppCompatActivity {

    private final String STAT_FILE_ADDRESS = "stats.txt";

    // all the text areas
    TextView totalPlayTimeDisplay, totalGuessesMadeDisplay, totalGamesPlayedDisplay;
    TextView highestScoreDisplay, averageGuessesDisplay, averageTimeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearSystemUI();
        setContentView(R.layout.activity_stats);
        setupDisplays();
    }

    private void setupDisplays() {
        totalPlayTimeDisplay = findViewById(R.id.totalPlayTimeDisplay);
        totalGuessesMadeDisplay = findViewById(R.id.totalGuessesMadeDisplay);
        totalGamesPlayedDisplay = findViewById(R.id.totalGamesPlayedDisplay);
        highestScoreDisplay = findViewById(R.id.highestScoreDisplay);
        averageGuessesDisplay = findViewById(R.id.averageGuessesDisplay);
        averageTimeDisplay = findViewById(R.id.averageTimeDisplay);

        updateDisplays();
    }

    private void updateDisplays() {
        String[] rawData = readStatFile().split(" ");
        totalPlayTimeDisplay.setText(formatTime(rawData[0]));
        totalGuessesMadeDisplay.setText(formatBasicStat(rawData[1]));
        totalGamesPlayedDisplay.setText(formatBasicStat(rawData[2]));
        highestScoreDisplay.setText(formatScore(rawData[3]));
        averageGuessesDisplay.setText(formatBasicStat(rawData[4]));
        averageTimeDisplay.setText(formatTime(rawData[5].replace("\n", "")));
    }

    private String formatTime(String unformattedTime) {
        // expected in MM:SS
        String formattedTime = "";

        int minutes = Integer.parseInt((unformattedTime.split(":"))[0]);
        int seconds = Integer.parseInt((unformattedTime.split(":"))[1]);

        if (minutes < 10) {
            formattedTime += ("0" + minutes);
        }
        else {
            formattedTime += String.valueOf(minutes);
        }

        formattedTime += "m ";

        if (seconds < 10) {
            formattedTime += ("0" + seconds);
        }
        else {
            formattedTime += String.valueOf(seconds);
        }

        formattedTime += "s";

        return formattedTime;
    }

    private String formatBasicStat(String unformattedTwoDigitNumber) {
        if (Integer.parseInt(unformattedTwoDigitNumber) < 10) {
            return "0" + unformattedTwoDigitNumber;
        }
        else {
            return unformattedTwoDigitNumber;
        }
    }

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

    public void resetStats(View view) {
        ImageButton resetButton = findViewById(R.id.resetButton);
        resetButton.setImageResource(R.drawable.reset_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                resetButton.setImageResource(R.drawable.reset);


                Dialog confirmationScreen = new Dialog(StatsActivity.this);
                confirmationScreen.setContentView(R.layout.confirmation_screen);
                confirmationScreen.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                confirmationScreen.setCancelable(false);

                ImageButton noButton = confirmationScreen.findViewById(R.id.noButton);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noButton.setImageResource(R.drawable.no_pressed);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noButton.setImageResource(R.drawable.no);
                                confirmationScreen.cancel();
                            }
                        }, 80); // Delay to see the icon change
                    }
                });

                ImageButton yesButton = confirmationScreen.findViewById(R.id.yesButton);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        yesButton.setImageResource(R.drawable.yes_pressed);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noButton.setImageResource(R.drawable.yes);
                                saveToStatFile("0:0 0 0 0 0 0:0");
                                updateDisplays();
                                confirmationScreen.cancel();
                            }
                        }, 80); // Delay to see the icon change
                    }
                });

                confirmationScreen.show();
            }
        }, 80); // Delay to see the icon change
    }

    public void closeMenu(View view) {
        ImageButton gotchaButton = findViewById(R.id.gotchaButtonStat);
        gotchaButton.setImageResource(R.drawable.gotcha_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotchaButton.setImageResource(R.drawable.gotcha);
                finish();
            }
        }, 80); // Delay to see the icon change
    }

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

    public String readStatFile() {

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

}