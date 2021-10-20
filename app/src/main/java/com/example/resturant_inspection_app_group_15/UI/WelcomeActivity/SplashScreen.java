package com.example.resturant_inspection_app_group_15.UI.WelcomeActivity;

/*
    SplashScreen.java displays a welcome screen when the app starts (at the beginning of the app) before
    the activity 1.
*/


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.example.resturant_inspection_app_group_15.UI.MapActivity.mapPageActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}