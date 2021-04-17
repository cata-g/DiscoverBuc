package com.example.discoverbuc.Register2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.discoverbuc.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);
    }

    public void callBackToStartup(View view) {

        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.login_back_button), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callRegisterFromLogin(View view) {

        Intent intent = new Intent(getApplicationContext(), Register.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.login_to_register), "transition_register_page");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }



}