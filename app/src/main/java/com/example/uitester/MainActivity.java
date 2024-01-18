/*
[NAME] MISHA BUSHKOV
[TASK] ICS3U SUMMATIVE
[DATE] 01 / 22 / 2024
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

    private final String STAT_FILE_ADDRESS = "stats.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearSystemUI();

        if (statFileEmpty()) {
            saveToStatFile("0:0 0 0 0 0 0:0");
        }

    }

    public void goToInfoMenu(View view) {

        ImageButton infoButton = findViewById(R.id.infoButton);
        infoButton.setImageResource(R.drawable.info_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                infoButton.setImageResource(R.drawable.info);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        }, 80); // Delay to see the icon change
    }

    public void goToStatsMenu(View view) {

        ImageButton statButton = findViewById(R.id.statButton);
        statButton.setImageResource(R.drawable.stat_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statButton.setImageResource(R.drawable.stat);
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        }, 80); // Delay to see the icon change
    }


    public void goToGameSettings(View view) {

        ImageButton gameSettingsButton = findViewById(R.id.playButton);
        gameSettingsButton.setImageResource(R.drawable.play_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gameSettingsButton.setImageResource(R.drawable.play);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }, 80); // Delay to see the icon change
    }

    public Boolean statFileEmpty() {

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

            return stringBuilder.toString().isEmpty();

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

        return false;

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
}