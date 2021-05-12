package com.example.discoverbuc.Register2.PasswordRecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Login.Login;

public class PasswordChanged extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);
    }

    public void BackToLogin(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.back_to_login_from_passChanged), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(PasswordChanged.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

    }

}