package com.example.discoverbuc.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Register.RegisterPreferences;
import com.example.discoverbuc.Register2.StartupScreen;

public class LoadingActivity extends AppCompatActivity {

    ImageView logo, appName, bg;
    LottieAnimationView loadingAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        //init of the variables
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        bg = findViewById(R.id.bg);
        loadingAnimation = findViewById(R.id.animation);

        // animation
        bg.animate().translationX(2000).setDuration(1000).setStartDelay(3500);

        appName.animate().translationX(-1000).setDuration(1000).setStartDelay(3500);
        logo.animate().translationX(-1000).setDuration(1000).setStartDelay(3500);
        loadingAnimation.animate().translationX(1000).setDuration(1000).setStartDelay(3500);

        SessionManager sm = new SessionManager(this);
        Boolean checkbox = sm.checkLoginStatus();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if(checkbox)
                {
                    intent = new Intent(LoadingActivity.this, Dashboard.class);
                }
                else
                {
                    intent = new Intent(LoadingActivity.this, StartupScreen.class);
                }
                startActivity(intent);
                LoadingActivity.this.finish();
            }
        }, 5000);


    }
}