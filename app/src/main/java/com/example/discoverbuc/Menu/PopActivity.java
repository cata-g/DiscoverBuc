package com.example.discoverbuc.Menu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverbuc.Common.Fragments.HomeRetailerFragment;
import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.Menu.HelperClasses.CarouselAdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CarouselCardHelperClass;
import com.example.discoverbuc.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class PopActivity extends Activity {

    TextView title_text, desc_text, details_headline, email_text, phone_text, program_text, website_text;
    RatingBar ratingBar;
    Button voteRating;
    ProgressBar loading;

    ImageButton addWishlist;

    String title, desc, tag, categoryTag;
    int imgRes, wishedSrc;
    float rating, firstRating;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<CarouselCardHelperClass> imagesArray;

    SessionManager sm;
    HashMap<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.95), (int)(height*.8));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        title_text = findViewById(R.id.title_text);
        desc_text = findViewById(R.id.desc_text);
        details_headline = findViewById(R.id.details_headline);
        email_text = findViewById(R.id.email_detail);
        phone_text = findViewById(R.id.phone_detail);
        program_text = findViewById(R.id.program_detail);
        website_text = findViewById(R.id.website_detail);
        ratingBar = findViewById(R.id.rating_detail);
        loading = findViewById(R.id.progress_bar_popactivity);
        recyclerView = findViewById(R.id.recyclerView_carousel);
        addWishlist = findViewById(R.id.add_to_wishlist);
        voteRating = findViewById(R.id.vote_rating_button);

        voteRating.setVisibility(View.GONE);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        imgRes = intent.getIntExtra("imageRes", 0);
        rating = intent.getFloatExtra("rating", 0);
        tag = intent.getStringExtra("tag");
        categoryTag = intent.getStringExtra("category");
        wishedSrc = intent.getIntExtra("wished", 0);
        imagesArray = new ArrayList<>();

        sm = new SessionManager(this);
        data = sm.getDataFromSession();

        voteRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    checkEligibilityForVote();
            }
        });

        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishlist(tag, categoryTag);
            }
        });
        clearActivity();
        setDetails();
    }

    private void checkEligibilityForVote() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations").child(categoryTag).child(tag).child("votedBy").child(data.get(sm.KEY_USERNAME));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ratingBar.setEnabled(false);
                }
                else{
                    voteRating.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.toString());
            }
        });

    }


    private void vote() {

        float changedRating = ratingBar.getRating();

        firstRating = rating;
        float newRating = firstRating + changedRating;
        newRating /= 2;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations").child(categoryTag).child(tag);
        ref.child("rating").setValue(newRating);
        ref.child("votedBy").child(data.get(sm.KEY_USERNAME)).setValue(data.get(sm.KEY_USERNAME));

        Toast.makeText(this, "Thank you for your vote!", Toast.LENGTH_SHORT).show();

        ratingBar.setRating(newRating);
        ratingBar.setEnabled(false);
        voteRating.setVisibility(View.GONE);

    }

    private void clearActivity(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        title_text.setText("");
        desc_text.setText("");
        details_headline.setText("");
        email_text.setText("");
        phone_text.setText("");
        program_text.setText("");
        website_text.setText("");

        ratingBar.setRating(0);
    }

    private void setDetails(){

        title_text.setText(title);
        ratingBar.setRating(rating);
        desc_text.setText(desc);

        if(wishedSrc == 0)
            addWishlist.setImageResource(wishedSrc);
        else
            addWishlist.setImageResource(R.drawable.full_heart);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("locations").child(categoryTag).child(tag);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("images").exists()){
                    Iterable<DataSnapshot> images = snapshot.child("images").getChildren();
                    Iterator<DataSnapshot> image = images.iterator();

                    while(image.hasNext()){
                        DataSnapshot imageDetail = (DataSnapshot) image.next();
                        String imageName = imageDetail.getValue().toString();
                        int imageLoc = getApplicationContext().getResources().getIdentifier(imageName, "drawable", getApplicationContext().getPackageName());
                        imagesArray.add(new CarouselCardHelperClass(imageLoc));
                    }

                }

                if(snapshot.child("contact").exists()){
                    details_headline.setText("Details");
                    String emailToSet = "Email " + snapshot.child("contact").child("email").getValue();
                    String phoneToSet = "Phone " + snapshot.child("contact").child("phone").getValue();
                    String programToSet = "Program " + snapshot.child("contact").child("program").getValue();
                    String websiteToSet = "Website " + snapshot.child("contact").child("website").getValue();

                    email_text.setText(emailToSet);
                    phone_text.setText(phoneToSet);
                    program_text.setText(programToSet);
                    website_text.setText(websiteToSet);

                }

                adapter = new CarouselAdapterHelperClass(imagesArray);
                recyclerView.setAdapter(adapter);
                loading.setVisibility(View.GONE);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PopActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        loading.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(valueEventListener);

    }

    private void addToWishlist(String tagToAdd, String catToAdd) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(new SessionManager(getApplicationContext()).getDataFromSession().get("username")));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("wishlist").exists()) {
                    ref.child("wishlist").child(tagToAdd).setValue(catToAdd);
                    Toast.makeText(getApplicationContext(), "ITEM ADDED TO WISHLIST", Toast.LENGTH_SHORT).show();
                    addWishlist.setImageResource(R.drawable.full_heart);
                } else {
                    if (snapshot.child("wishlist").child(tagToAdd).exists()) {
                        ref.child("wishlist").child(tagToAdd).removeValue();
                        Toast.makeText(getApplicationContext(), "ITEM REMOVED FROM WISHLIST", Toast.LENGTH_SHORT).show();
                        addWishlist.setImageResource(R.drawable.empty_heart);
                    } else {
                        ref.child("wishlist").child(tagToAdd).setValue(catToAdd);
                        Toast.makeText(getApplicationContext(), "ITEM ADDED TO WISHLIST", Toast.LENGTH_SHORT).show();
                        addWishlist.setImageResource(R.drawable.full_heart);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.toString());
            }
        });
    }

}