package com.example.discoverbuc.Register2.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.discoverbuc.Dashboard;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Register.Register;
import com.example.discoverbuc.Register2.StartupScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Login extends AppCompatActivity {

    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

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

    private boolean usernameValidation() {

        String val = username.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            username.setError("Username field must not be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean passwordValidation() {

        String val = password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Password field must not be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {

        if (!usernameValidation() | !passwordValidation()) return;
        else checkUser();
    }



    private void checkUser() {

        String userValue = username.getEditText().getText().toString().trim();
        String passValue = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query validateUser = reference.orderByChild("username").equalTo(userValue);

        validateUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordDB = snapshot.child(userValue).child("password").getValue(String.class);

                    BCrypt.Result result = BCrypt.verifyer().verify(passValue.toCharArray(), passwordDB);
                    if (result.verified) {

                        password.setError(null);
                        password.setErrorEnabled(false);

                        //Basic Info
                        String name = snapshot.child(userValue).child("fullName").getValue(String.class);
                        String email = snapshot.child(userValue).child("email").getValue(String.class);
                        String birthday = snapshot.child(userValue).child("birthday").getValue(String.class);

                        //Interests info
                        boolean interestInCS = snapshot.child(userValue).child("Prefs").child("coffee_shopSelected").getValue(Boolean.class);
                        boolean interestInMuseums = snapshot.child(userValue).child("Prefs").child("museumSelected").getValue(Boolean.class);
                        boolean interestInNature = snapshot.child(userValue).child("Prefs").child("natureSelected").getValue(Boolean.class);
                        boolean interestInRestaurants = snapshot.child(userValue).child("Prefs").child("restaurantSelected").getValue(Boolean.class);

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        //Transfer Info
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("birthday", birthday);

                        //Transfer Interests
                        intent.putExtra("CS", interestInCS);
                        intent.putExtra("MUSEUMS", interestInMuseums);
                        intent.putExtra("NATURE", interestInNature);
                        intent.putExtra("RESTAURANTS", interestInRestaurants);

                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair(findViewById(R.id.login_to_dashboard), "transition_login_to_dashboard");


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }


                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("User not exists!");
                    username.requestFocus();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}