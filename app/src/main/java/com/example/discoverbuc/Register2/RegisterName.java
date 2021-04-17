package com.example.discoverbuc.Register2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.discoverbuc.R;

public class RegisterName extends AppCompatActivity {

    TextView headline;
    Button back, next, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register_name);

        back = findViewById(R.id.register_back_button);
        headline = findViewById(R.id.register_headline);
        next = findViewById(R.id.register_next_button);
        login = findViewById(R.id.register_login_button);
    }

    public void callPrefsRegisterScreen(View view) {

        Intent intent = new Intent(getApplicationContext(), RegisterPreferences.class);

        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callBackToRegister(View view) {

        Intent intent = new Intent(getApplicationContext(), Register.class);

        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callLoginScreenFromRegName(View view) {


        Intent intent = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.register_login_button), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }


    }
}