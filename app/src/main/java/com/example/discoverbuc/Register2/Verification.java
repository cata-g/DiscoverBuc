package com.example.discoverbuc.Register2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.HelperClasses.PrefsHelperClass;
import com.example.discoverbuc.Register2.HelperClasses.UserHelperClass;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Verification extends AppCompatActivity {

    int natureSelected, museumSelected, restaurantSelected, coffee_shopSelected;
    String username, email,password, name, birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_verification);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        birthday = intent.getStringExtra("birthday");
        natureSelected = intent.getIntExtra("isNatureSelected", 0);
        museumSelected = intent.getIntExtra("isMuseumSelected", 0);
        restaurantSelected =  intent.getIntExtra("isRestaurantSelected", 0);
        coffee_shopSelected = intent.getIntExtra("isCoffeeShopSelected", 0);

        sendVerificationCode(email);
    }

    private void sendVerificationCode(String email) {

        EmailAuthProvider.getCredential(email, password).getSignInMethod();

    }
    public void callBackToRegisterPrefs(View view) {


        Intent intent = new Intent(getApplicationContext(), RegisterPreferences.class);

        //Animations
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.next_to_verification), "transition_verification_page");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Verification.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }
    public void createUser(View view){
        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);
        //Transfer
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        intent.putExtra("birthday", birthday);
        intent.putExtra("isNatureSelected", natureSelected);
        intent.putExtra("isMuseumSelected", museumSelected);
        intent.putExtra("isRestaurantSelected", restaurantSelected);
        intent.putExtra("isCoffeeShopSelected", coffee_shopSelected);

        addUser();

        //Animations
        Pair[] pairs = new Pair[4];

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Verification.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
    private void addUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");


        UserHelperClass helperClass = new UserHelperClass(name, username, email, password, birthday);
        PrefsHelperClass prefsClass = new PrefsHelperClass(natureSelected, museumSelected, restaurantSelected, coffee_shopSelected);

        //UUID id = UUID.randomUUID();
        String id = username.toString();
        reference.child(id).setValue(helperClass);

        reference = reference.child(id);
        reference.child("Prefs").setValue(prefsClass);

        Toast.makeText(this, "User successfully created", Toast.LENGTH_SHORT).show();

    }
}