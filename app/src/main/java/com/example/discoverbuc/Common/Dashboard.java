package com.example.discoverbuc.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.discoverbuc.Common.Fragments.HomeRetailerFragment;
import com.example.discoverbuc.Common.Fragments.ProfileRetailerFragment;
import com.example.discoverbuc.Common.Fragments.WishlistRetailerFragment;
import com.example.discoverbuc.Menu.UserMenu;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.StartupScreen;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeRetailerFragment()).commit();
        menu();

    }

    private void menu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_home:
                        fragment = new HomeRetailerFragment();
                        break;

                    case R.id.bottom_nav_wishlist:
                        fragment = new WishlistRetailerFragment();
                        break;

                    case R.id.bottom_nav_profile:
                        fragment = new ProfileRetailerFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

    }


}