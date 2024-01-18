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

// == MAIN MENU =====================================
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearSystemUI();

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
//                Intent intent = new Intent(MainActivity.this, StatActivity.class);
//                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, GameSettingsActivity.class);
                startActivity(intent);
            }
        }, 80); // Delay to see the icon change
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