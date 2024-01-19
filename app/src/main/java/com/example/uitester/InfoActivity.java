// == FILE LOCATION =========
package com.example.uitester;

// == IMPORTS ==================================
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

// == INFO MENU =====================================
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // [LAYOUT] Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        clearSystemUI();

    }


    // ==================================
    // == MENU BUTTON FUNCTIONS =========
    // ==================================


    // Parameters: View | Requirement for button-activation.
    // Description: Quickly switches the gotcha button image closes the menu.
    public void closeMenu(View view) {

        ImageButton gotchaButton = findViewById(R.id.gotchaButton);
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