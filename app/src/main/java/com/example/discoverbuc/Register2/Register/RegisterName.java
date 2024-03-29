package com.example.discoverbuc.Register2.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.Login.Login;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

public class RegisterName extends AppCompatActivity {

    TextView headline;
    Button next, login;
    ImageButton back;

    TextInputLayout name;
    DatePicker datePicker;
    CountryCodePicker ccp;

    String username, phone,password;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register_name);


        //Animation
        back = findViewById(R.id.register_back_button);
        headline = findViewById(R.id.register_headline);
        next = findViewById(R.id.register_next_button);
        login = findViewById(R.id.register_login_button);


        ccp = findViewById(R.id.country_code_picker);
        loading =  findViewById(R.id.progress_bar_regname);

        loading.setVisibility(View.GONE);

        //Activity Transfer
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("password");

        //Validation
        name = findViewById(R.id.register_name);
        datePicker = findViewById(R.id.register_birthday);
    }

    public void callPrefsRegisterScreen(View view) {

        loading.setVisibility(View.VISIBLE);
        if(!validateAge() | !validateName()){
            loading.setVisibility(View.GONE);
            return;
        }


        //Birthday
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String DateOfBirth = day + "/" + month + "/" + year;

        Intent intent = new Intent(getApplicationContext(), RegisterPreferences.class);
        intent.putExtra("username", username);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("name", name.getEditText().getText().toString());
        intent.putExtra("birthday", DateOfBirth);

        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();
    }

    public void callBackToRegister(View view) {

        Intent intent = new Intent(getApplicationContext(), Register.class);

        //Animations
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(back, "transition_back_button");
        pairs[1] = new Pair<View, String>(headline, "transition_register_headline");
        pairs[2] = new Pair<View, String>(next, "transition_next_button");
        pairs[3] = new Pair<View, String>(login, "transition_login_button");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();

    }

    public void callLoginScreenFromRegName(View view) {


        Intent intent = new Intent(getApplicationContext(), Login.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.register_login_button), "transition_login");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterName.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();

    }

    private boolean validateName(){

            String val = name.getEditText().getText().toString().trim();
            if(val.isEmpty()){
                name.setError("Name field must not be empty");
                return false;
            }
            else if(val.length()< 4){
                name.setError("Your name must have more than 4 characters");
                return false;
            }
            else{
                name.setError(null);
                name.setErrorEnabled(false);
                return true;
            }
    }

    private boolean validateAge(){

        //Current Date
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //User Details
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        if(currentYear - year < 12){
            Toast.makeText(this, "You must be at least 12", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(currentYear - year == 12){

            if(month < currentMonth){
                Toast.makeText(this, "You must be at least 12", Toast.LENGTH_SHORT).show();

                return false;
            }

            else if(month == currentMonth && day > currentDay){
                Toast.makeText(this, "You must be at least 12", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return true;
    }


}