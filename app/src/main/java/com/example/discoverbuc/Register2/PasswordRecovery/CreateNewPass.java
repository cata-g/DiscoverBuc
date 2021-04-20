package com.example.discoverbuc.Register2.PasswordRecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Register.Verification;
import com.example.discoverbuc.Register2.StartupScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.ref.Reference;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CreateNewPass extends AppCompatActivity {

    TextInputLayout password, confirmPassword;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_pass);

        username = getIntent().getStringExtra("username");

        password = findViewById(R.id.create_new_pass);
        confirmPassword = findViewById(R.id.confirm_new_pass);

    }

    public void closePassCreate(View view) {


        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);

        //Animations
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.close_createPass), "transition_next_button");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateNewPass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

    }

    public void changePass(View view){

        if(!passwordValidation() | !confirmPasswordValidation()){
            return;
        }

        DatabaseReference changePass = FirebaseDatabase.getInstance().getReference("users");
        String hashedPass = BCrypt.withDefaults().hashToString(12, password.getEditText().getText().toString().toCharArray());
        changePass.child(username).child("password").setValue(hashedPass);

        Intent intent = new Intent(getApplicationContext(), PasswordChanged.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.next_to_changedPass), "transition_changedPass");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateNewPass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

        }

    private boolean passwordValidation() {

        String val = password.getEditText().getText().toString().trim();

        String checkPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        if (val.isEmpty()) {
            password.setError("Password field must not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean confirmPasswordValidation() {

        String val1 = password.getEditText().getText().toString().trim();
        String val2 = confirmPassword.getEditText().getText().toString().trim();

        if (val2.isEmpty()) {
            confirmPassword.setError("Retype your password");
            return false;
        } else if (!val2.equals(val1)) {
            confirmPassword.setError("Your passwords must match");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }

    }
}