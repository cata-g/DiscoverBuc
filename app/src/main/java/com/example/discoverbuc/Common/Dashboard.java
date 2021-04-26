package com.example.discoverbuc.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.discoverbuc.Menu.UserMenu;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.StartupScreen;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView tv = findViewById(R.id.textView);
        SessionManager sm = new SessionManager(this);
        HashMap<String, String> data = sm.getDataFromSession();

        String name = data.get(sm.KEY_NAME);

        tv.setText(name);

    }

    public void Logout(View view){

        SessionManager sm = new SessionManager(this);
        sm.logoutUser();
        finish();

        Intent intent = new Intent(Dashboard.this, StartupScreen.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.logout_button), "transition_next_button");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();

    }

    public void goToMenu(View view){

        Intent intent = new Intent(this, UserMenu.class);
        startActivity(intent);

    }
}