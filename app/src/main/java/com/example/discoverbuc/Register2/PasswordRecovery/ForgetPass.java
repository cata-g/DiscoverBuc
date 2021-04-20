package com.example.discoverbuc.Register2.PasswordRecovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Register.Register;
import com.example.discoverbuc.Register2.Register.RegisterName;
import com.example.discoverbuc.Register2.Register.RegisterPreferences;
import com.example.discoverbuc.Register2.Register.Verification;
import com.example.discoverbuc.Register2.StartupScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ForgetPass extends AppCompatActivity {

    TextInputLayout username;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        username = findViewById(R.id.forgot_pass_username);
        loading = findViewById(R.id.progress_bar_forgotpassuname);

        loading.setVisibility(View.GONE);
    }

    public void callVerifyScreenFromPass(View view){

        loading.setVisibility(View.VISIBLE);
        if(!usernameValidation()){
            loading.setVisibility(View.GONE);
            return;
        }


        String val = username.getEditText().getText().toString().trim();
        Query checkData = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(val);
        checkData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setErrorEnabled(false);
                    username.setError(null);

                    String phone = snapshot.child(val).child("phone").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), Verification.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("activity", "changePass");
                    intent.putExtra("username", username.getEditText().getText().toString().trim());

                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair(findViewById(R.id.next_to_verif_forgetpass), "transition_verification_page");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPass.this, pairs);
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }

                    finish();


                }else {

                    loading.setVisibility(View.GONE);
                    username.setError("Username does not exist!");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgetPass.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callBackToLoginFromPass(View view){
        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.back_to_login_from_passChange), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();
    }

    private boolean usernameValidation() {

        String val = username.getEditText().getText().toString().trim();
        String spaces = "\\A\\w{1,20}\\z";


        if (val.isEmpty()) {
            username.setError("Username field must not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too long");
            return false;
        } else if (val.length() < 4) {
            username.setError("Username is too short");
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

}