package com.example.uitester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameSettingsActivity extends AppCompatActivity {

    TextView numDigitsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearSystemUI();

        setContentView(R.layout.activity_game_settings);

        // [VIEWS] This is the display which says how many digits there are.
        numDigitsDisplay = findViewById(R.id.numDigitsDisplay);

        // [VIEWS] Initialize and configure slider.

        // Declare and initialize slider.
        SeekBar numDigitsSlider = findViewById(R.id.numDigitsSlider);

        // Set progress, maximum and minimum value.
        numDigitsSlider.setKeyProgressIncrement(1);

        // Setup logic for when the slider is changed.
        numDigitsSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Determine the current value at which the slider is at.
                int alignedProgress = Math.round((float) progress);

                // Update the digits display.
                numDigitsDisplay.setText(String.valueOf(alignedProgress - 1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // [CLARITY] This is just an implemented function; it isn't used.
                // TODO: Add colours?
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // [CLARITY] This is just an implemented function; it isn't used.
            }
        });

    }

    public void closeMenu(View view) {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.back_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButton.setImageResource(R.drawable.back);
                finish();
            }
        }, 80); // Delay to see the icon change
    }

    public void startGame(View view) {
        ImageButton confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setImageResource(R.drawable.confirm_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmButton.setImageResource(R.drawable.confirm);
                Intent intent = new Intent(GameSettingsActivity.this, GameplayActivity.class);
                intent.putExtra("numDigits", Integer.valueOf(numDigitsDisplay.getText().toString()));
                CheckBox showCorrectNumberToggle = findViewById(R.id.showCorrectNumberToggle);
                intent.putExtra("showCorrectNumber", showCorrectNumberToggle.isChecked());
                // TODO same for checkboxes
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