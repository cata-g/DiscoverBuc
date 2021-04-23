package com.example.discoverbuc.Register2.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Login.Login;

public class RegisterPreferences extends AppCompatActivity {

    TextView headline;
    Button back, next, login;

    String username,password, name, birthday, phone;

    CheckBox nature, museum, restaurant, coffee_shop;
    int natureSelected, museumSelected, restaurantSelected, coffee_shopSelected;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register_preferences);

        //Animation
        back = findViewById(R.id.register_back_button);
        headline = findViewById(R.id.register_headline);
        next = findViewById(R.id.register_next_button);
        login = findViewById(R.id.register_login_button);

        //Transfer
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        birthday = intent.getStringExtra("birthday");
        phone = intent.getStringExtra("phone");

        //For validation
        nature = findViewById(R.id.register_hobby_nature);
        museum = findViewById(R.id.register_hobby_museum);
        restaurant = findViewById(R.id.register_hobby_restaurant);
        coffee_shop = findViewById(R.id.register_hobby_coffeeShop);
    }

    public void callVerifyScreen(View view){

        if(!validation()){
            return;
        }


        Intent intent = new Intent(getApplicationContext(), Verification.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        intent.putExtra("birthday", birthday);
        intent.putExtra("phone", phone);
        intent.putExtra("isNatureSelected", natureSelected);
        intent.putExtra("isMuseumSelected", museumSelected);
        intent.putExtra("isRestaurantSelected", restaurantSelected);
        intent.putExtra("isCoffeeShopSelected", coffee_shopSelected);
        intent.putExtra("activity", "register");

        //Animations
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.next_to_verification), "transition_verification_page");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPreferences.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();

    }

    public void callBackToRegisterName(View view) {


        Intent intent = new Intent(getApplicationContext(), RegisterName.class);

        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPreferences.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

    }

    public void callLoginScreenFromRegPrefs(View view) {


        Intent intent = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.register_login_button), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterPreferences.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();

    }

    private boolean validation(){
        if(nature.isChecked()) {
            counter++;
            natureSelected = 1;
        }
        if(museum.isChecked()){
            counter++;
            museumSelected = 1;
        }
        if(coffee_shop.isChecked()){
            counter++;
            coffee_shopSelected = 1;
        }
        if(restaurant.isChecked()){
            counter++;
            restaurantSelected = 1;
        }

        if(counter == 0){
            Toast.makeText(this, "You must select at least one", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



}