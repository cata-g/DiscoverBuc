package com.example.discoverbuc.Register2.Register;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import com.chaos.view.PinView;
import com.example.discoverbuc.Dashboard;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.HelperClasses.PrefsHelperClass;
import com.example.discoverbuc.Register2.HelperClasses.UserHelperClass;
import com.example.discoverbuc.Register2.Login.Login;
import com.example.discoverbuc.Register2.PasswordRecovery.CreateNewPass;
import com.example.discoverbuc.Register2.StartupScreen;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {


    //PinView userPin;
    Pinview userPin;
    String systemCode;
    int natureSelected, museumSelected, restaurantSelected, coffee_shopSelected;
    String username,password, name, birthday, phone, activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_verification);

        userPin = findViewById(R.id.pinView);

        //TRANSFER
        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");


        if(!activity.equals("changePass")){
            password = intent.getStringExtra("password");
            name = intent.getStringExtra("name");
            birthday = intent.getStringExtra("birthday");
            natureSelected = intent.getIntExtra("isNatureSelected", 0);
            museumSelected = intent.getIntExtra("isMuseumSelected", 0);
            restaurantSelected =  intent.getIntExtra("isRestaurantSelected", 0);
            coffee_shopSelected = intent.getIntExtra("isCoffeeShopSelected", 0);
        }



        sendVerificationCode(phone);
    }

    private void sendVerificationCode(String phone) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
             systemCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Automatic entered code
            String smsCode = phoneAuthCredential.getSmsCode();
            if(smsCode != null){
                userPin.setValue(smsCode);
                validateCode(smsCode);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void validateCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemCode, code);
        signInWithPhoneAuthCredential(credential);
    }
    public void closeVerif(View view) {


        Intent intent = new Intent(getApplicationContext(), StartupScreen.class);

        //Animations
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.close_validation), "transition_next_button");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Verification.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent;

                            if(activity.equals("changePass")){

                                intent = new Intent(getApplicationContext(), CreateNewPass.class);
                                intent.putExtra("username", username);

                            }
                            else{
                                createUser();
                                intent = new Intent(getApplicationContext(), Login.class);

                            }

                            //Animations
                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair(findViewById(R.id.next_verifyButton), "transition_login");

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Verification.this, pairs);
                                startActivity(intent, options.toBundle());
                            } else {
                                startActivity(intent);
                            }
                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Verification.this, "Try Again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void Finish(View view){

        String codeEnteredByUser = userPin.getValue().toString();
        if(!codeEnteredByUser.isEmpty()){

            //userPin.setError(null);
            userPin.setFocusable(false);
            validateCode(codeEnteredByUser);
        }else{
            //userPin.setError("Pin cannot be empty!");
            userPin.requestFocus();
        }

    }
    private void createUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");


        UserHelperClass helperClass = new UserHelperClass(name, username, password, birthday, phone);
        PrefsHelperClass prefsClass = new PrefsHelperClass(natureSelected, museumSelected, restaurantSelected, coffee_shopSelected);

        //UUID id = UUID.randomUUID();
        String id = username.toString();
        reference.child(id).setValue(helperClass);

        reference = reference.child(id);
        reference.child("Prefs").setValue(prefsClass);

        Toast.makeText(this, "User successfully created", Toast.LENGTH_SHORT).show();

    }
}