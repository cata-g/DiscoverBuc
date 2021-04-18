package com.example.discoverbuc.Register2;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.discoverbuc.R;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    //Animation variables
    TextView headline;
    Button back, next, login;

    //UserInput variables
    TextInputLayout username, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register);

        back = findViewById(R.id.register_back_button);
        headline = findViewById(R.id.register_headline);
        next = findViewById(R.id.register_next_button);
        login = findViewById(R.id.register_login_button);

        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirmPassword);

    }

    public void callNameRegisterScreen(View view) {

        if(!usernameValidation() | !emailValidation() | !passwordValidation() | !confirmPasswordValidation()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), RegisterName.class);

        intent.putExtra("username", username.getEditText().getText().toString());
        intent.putExtra("email", email.getEditText().getText().toString());
        intent.putExtra("password", password.getEditText().getText().toString());
        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callBackToStartup(View view) {

        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.register_back_button), "transition_register_page");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callLoginScreenFromReg(View view) {


        Intent intent = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.register_login_button), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }


    }

    private boolean usernameValidation() {

        String val = username.getEditText().getText().toString().trim();
        String spaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Username field must not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large");
            return false;
        } else if (val.length() < 4) {
            username.setError("Username is too small");
            return false;
        } else if (!val.matches(spaces)) {
            username.setError("Username must not contain any white spaces");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean emailValidation() {

        String val = email.getEditText().getText().toString().trim();
        String checkValid = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";

        if (val.isEmpty()) {
            email.setError("Email field must not be empty");
            return false;
        }
        else if(!val.matches(checkValid)){
            email.setError("Enter a valid email address");
            return false;
        }
         else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean passwordValidation() {

        String val = password.getEditText().getText().toString().trim();

        String checkPassword ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (val.isEmpty()) {
            password.setError("Password field must not be empty");
            return false;
        }
        else if(!val.matches(checkPassword)){
            password.setError("Password is too weak");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean confirmPasswordValidation(){

        String val1 = password.getEditText().getText().toString().trim();
        String val2 = confirmPassword.getEditText().getText().toString().trim();

        if(val2.isEmpty()){
            confirmPassword.setError("Retype your password");
            return false;
        }
        else if(!val2.equals(val1)){
            confirmPassword.setError("Your passwords must match");
            return false;
        }
        else{
            confirmPassword.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

}