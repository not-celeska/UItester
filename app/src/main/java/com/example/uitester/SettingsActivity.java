// == FILE LOCATION =========
package com.example.uitester;

// == IMPORTS ==================================

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

// == SETTINGS MENU =====================================
public class SettingsActivity extends AppCompatActivity {

    TextView numDigitsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // [LAYOUT] Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        clearSystemUI();

        // [VIEWS] This is the display which says how many digits there are.
        numDigitsDisplay = findViewById(R.id.numDigitsDisplay);

        // [VIEWS] Initialize and configure slider.
        setupSlider();

    }


    // ==================================
    // == MENU BUTTON FUNCTIONS =========
    // ==================================


    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the gotcha button image, closes the menu.
    public void closeMenu(View view) {

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setImageResource(R.drawable.back_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButton.setImageResource(R.drawable.back);

                finish(); // [CLARITY] This just closes this menu screen.

            }
        }, 80);
    }

    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the confirm button image, passes settings into next screen and starts gameplay.
    public void startGame(View view) {

        ImageButton confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setImageResource(R.drawable.confirm_pressed);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmButton.setImageResource(R.drawable.confirm);

                // [CLARITY] Sets up the next screen.
                Intent intent = new Intent(SettingsActivity.this, GameplayActivity.class);

                // [EXTRA] Packages the selected number of digits selected by the user.
                intent.putExtra("numDigits", Integer.valueOf(numDigitsDisplay.getText().toString()));

                // [EXTRA] Packages whether the user wants the correct number to be shown.
                CheckBox showCorrectNumberToggle = findViewById(R.id.showCorrectNumberToggle);
                intent.putExtra("showCorrectNumber", showCorrectNumberToggle.isChecked());

                // [EXTRA] Packages user's "don't save score" status.
                CheckBox dontSaveScoreToggle = findViewById(R.id.dontSaveScoreToggle);
                intent.putExtra("dontSaveScore", dontSaveScoreToggle.isChecked());

                // [CLARITY] Opens the gameplay menu screen.
                startActivity(intent);

            }
        }, 80);
    }


    // ==================================
    // == BACK-END OPERATION FUNCTIONS ==
    // ==================================


    // Parameters: None | Actions *inside* affect the UI construction.
    // Description: Creates the mechanics behind the slider.
    private void setupSlider() {

        // [CLARITY] Declare and initialize slider.
        SeekBar numDigitsSlider = findViewById(R.id.numDigitsSlider);

        // [CLARITY] Set progress increment; how many ticks can change at once.
        numDigitsSlider.setKeyProgressIncrement(1);

        // [CLARITY] Setup logic for when the slider is changed.
        numDigitsSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // [CLARITY] Determine the current value at which the slider is at.
                int alignedProgress = Math.round((float) progress);

                // [CLARITY] Update the digits display.
                numDigitsDisplay.setText(String.valueOf(alignedProgress - 1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // [CLARITY] This is just an implemented function; it isn't used.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // [CLARITY] This is just an implemented function; it isn't used.
            }
        });

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