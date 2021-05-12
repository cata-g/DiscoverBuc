package com.example.discoverbuc.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverbuc.Common.Fragments.HomeRetailerFragment;
import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.Menu.HelperClasses.CarouselAdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CarouselCardHelperClass;
import com.example.discoverbuc.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class PopActivity extends FragmentActivity implements OnMapReadyCallback{

    TextView title_text, desc_text,program_text;
    RatingBar ratingBar;
    AppCompatButton voteRating;
    ProgressBar loading;
    LinearLayout contact;

    ImageButton addWishlist;

    String title, desc, tag, categoryTag;
    int imgRes, wishedSrc;
    float rating, firstRating;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<CarouselCardHelperClass> imagesArray;

    SessionManager sm;
    HashMap<String, String> data;

    GoogleMap mGoogleMap;
    double lat, lng;

    AppCompatImageButton website, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.9), (int)(height*.8));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        title_text = findViewById(R.id.title_text);
        desc_text = findViewById(R.id.desc_text);
        program_text = findViewById(R.id.program_detail);
        ratingBar = findViewById(R.id.rating_detail);
        loading = findViewById(R.id.progress_bar_popactivity);
        recyclerView = findViewById(R.id.recyclerView_carousel);
        addWishlist = findViewById(R.id.add_to_wishlist);
        voteRating = findViewById(R.id.vote_rating_button);
        contact = findViewById(R.id.contact);

        website = findViewById(R.id.websiteButton);
        email = findViewById(R.id.emailButton);
        phone = findViewById(R.id.phoneButton);

        voteRating.setVisibility(View.GONE);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        imgRes = intent.getIntExtra("imageRes", 0);
        rating = intent.getFloatExtra("rating", 0);
        tag = intent.getStringExtra("tag");
        categoryTag = intent.getStringExtra("category");
        wishedSrc = intent.getIntExtra("wishedSrc", 0);


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

        if(firstRating != 0)
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
        program_text.setText("");
        desc_text.setText("");
        contact.setVisibility(View.GONE);

        ratingBar.setRating(0);
        addWishlist.setImageResource(R.drawable.empty_heart);
    }

    private void setDetails(){

        contact.setVisibility(View.VISIBLE);
        title_text.setText(title);
        ratingBar.setRating(rating);
        desc_text.setText(desc);

        if(wishedSrc != 0){
            addWishlist.setImageResource(wishedSrc);
        }

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
                    String emailToSet = snapshot.child("contact").child("email").getValue().toString();
                    String phoneToSet = snapshot.child("contact").child("phone").getValue().toString();
                    String programToSet = snapshot.child("contact").child("program").getValue().toString();
                    String websiteToSet = snapshot.child("contact").child("website").getValue().toString();
                    website.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWebsite(websiteToSet);
                        }
                    });

                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openEmail(emailToSet);
                        }
                    });

                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialer(phoneToSet);
                        }
                    });

                    program_text.setText(programToSet);

                    DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference("users").child(data.get(sm.KEY_USERNAME)).child("wishlist").child(tag);
                    wishRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                addWishlist.setImageResource(R.drawable.full_heart);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    contact.setVisibility(View.GONE);
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


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);


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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("locations").child(categoryTag).child(tag).child("loc");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    lat = snapshot.child("lat").getValue(double.class);
                    lng = snapshot.child("lng").getValue(double.class);
                    mGoogleMap = googleMap;
                    LatLng latLng = new LatLng(lat, lng);
                    Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(mapIcons(categoryTag))));
                    marker.showInfoWindow();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                    mGoogleMap.moveCamera(cameraUpdate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.toString());
            }
        });

    }

    public Bitmap mapIcons(String cat){

        int src = 0;

        if(cat.equals("museums")){
            src =  R.drawable.icon_museum;
        }else if(cat.equals("coffeeshops")){
            src = R.drawable.icon_coffee;
        }else if(cat.equals("nature")){
            src = R.drawable.icon_nature;
        }else if(cat.equals("restaurants")){
            src = R.drawable.icon_restaurant;
        }else if(cat.equals("malls")){
            src = R.drawable.icon_mall;
        }else if(cat.equals("pubs")){
            src = R.drawable.icon_pub;
        }

        int height = 75;
        int width = 75;
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(src);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);
        return icon;

    }

    public void openWebsite(String website){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(intent);

    }

    public void openEmail(String email){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);

    }

    public void openDialer(String phone){

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);

    }

}