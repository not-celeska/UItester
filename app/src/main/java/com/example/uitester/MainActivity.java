/*
[NAME] MISHA BUSHKOV
[TASK] ICS3U SUMMATIVE
[DATE] 01 / 22 / 2024
[DESCRIPTION] Basic Bulls & Cows game adapted for android devices.
 */

// == FILE LOCATION =========
package com.example.uitester;

// == IMPORTS ==================================
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// == MAIN MENU =====================================
public class MainActivity extends AppCompatActivity {

    // [FIELD] File address for the statistics file.
    private final String STAT_FILE_ADDRESS = "stats.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // [LAYOUT] Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearSystemUI();

        // [CLARITY] If the app is first open (the stat file hasn't been initialized yet),
        //           or the file was corrupted, statistics will be reset.
        if (statFileEmpty()) {
            initializeStatFile();
        }
    }


    // ==================================
    // == MENU BUTTON FUNCTIONS =========
    // ==================================


    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the info button image and opens the info menu.
    public void goToInfoMenu(View view) {

        ImageButton infoButton = findViewById(R.id.infoButton);
        infoButton.setImageResource(R.drawable.info_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                infoButton.setImageResource(R.drawable.info);

                // [CLARITY] Opens the info menu.
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);

            }
        }, 80);
    }

    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the stat button image and opens the stat menu.
    public void goToStatsMenu(View view) {

        ImageButton statButton = findViewById(R.id.statButton);
        statButton.setImageResource(R.drawable.stat_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statButton.setImageResource(R.drawable.stat);

                // [CLARITY] Opens the statistics menu.
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);

            }
        }, 80);
    }

    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the play button image and opens the settings menu.
    public void goToGameSettings(View view) {

        ImageButton gameSettingsButton = findViewById(R.id.playButton);
        gameSettingsButton.setImageResource(R.drawable.play_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gameSettingsButton.setImageResource(R.drawable.play);

                // [CLARITY] Opens the game settings menu.
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        }, 80);
    }


    // ==================================
    // == BACK-END OPERATION FUNCTIONS ==
    // ==================================


    // Parameters: None | Uses static variable access.
    // Description: Checks if the statistics file is empty (hasn't been initialized or corrupted).
    private Boolean statFileEmpty() {

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

            // [CLARITY] Will return empty status of the file.
            return stringBuilder.toString().isEmpty();

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

        return false;

    }

    // Parameters: None | Uses static variable access.
    // Description: Sets the text inside the statistics file to be all 0; a form of initializing.
    private void initializeStatFile() {

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

}