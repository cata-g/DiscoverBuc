package com.example.discoverbuc.Register2.Register;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.discoverbuc.Dashboard;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.HelperClasses.PrefsHelperClass;
import com.example.discoverbuc.Register2.HelperClasses.UserHelperClass;
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


    PinView userPin;
    String systemCode;
    int natureSelected, museumSelected, restaurantSelected, coffee_shopSelected;
    String username,password, name, birthday, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_verification);

        userPin = findViewById(R.id.pinView);

        //TRANSFER
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        birthday = intent.getStringExtra("birthday");
        phone = intent.getStringExtra("phone");
        natureSelected = intent.getIntExtra("isNatureSelected", 0);
        museumSelected = intent.getIntExtra("isMuseumSelected", 0);
        restaurantSelected =  intent.getIntExtra("isRestaurantSelected", 0);
        coffee_shopSelected = intent.getIntExtra("isCoffeeShopSelected", 0);

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
                userPin.setText(smsCode);
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
        createUser();
        signInWithPhoneAuthCredential(credential);
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            createUser();

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                            //Animations
                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair(findViewById(R.id.login_to_dashboard), "transition_login");

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Verification.this, pairs);
                                startActivity(intent, options.toBundle());
                            } else {
                                startActivity(intent);
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Verification.this, "Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void Finish(View view){

        String codeEnteredByUser = userPin.getText().toString();
        if(!codeEnteredByUser.isEmpty()){

            userPin.setError(null);
            userPin.setFocusable(false);
            validateCode(codeEnteredByUser);
        }else{
            userPin.setError("Pin cannot be empty!");
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