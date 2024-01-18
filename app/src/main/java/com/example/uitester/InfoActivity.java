package com.example.uitester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        clearSystemUI();

    }

    public void closeMenu(View view) {
        ImageButton infoButton = findViewById(R.id.gotchaButton);
        infoButton.setImageResource(R.drawable.gotcha_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                infoButton.setImageResource(R.drawable.gotcha);
                finish();
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