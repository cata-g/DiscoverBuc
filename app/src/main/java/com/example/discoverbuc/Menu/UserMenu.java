package com.example.discoverbuc.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.R;
import com.example.discoverbuc.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class UserMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressBar loading;

    SessionManager sm;
    HashMap<String, String> data;
    ArrayList<CardHelperClass> locationsArray;
    boolean usersPrefs[];

    int todaysRec,date;
    TextView todaysTitle, todaysDesc;
    RatingBar todaysRating;
    ImageView todaysCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        recyclerView = findViewById(R.id.recyclerView);
        loading = findViewById(R.id.progress_bar_menu);
        loading.setVisibility(View.GONE);
        todaysCover = findViewById(R.id.todaysCover);
        todaysDesc = findViewById(R.id.todaysDesc);
        todaysRating = findViewById(R.id.todaysRating);
        todaysTitle = findViewById(R.id.todaysTitle);

        sm = new SessionManager(this);
        data = sm.getDataFromSession();

        usersPrefs = new boolean[10];
        usersPrefs[0] = data.get(sm.KEY_INTERESTS_COFFEESHOP).equals("true");
        usersPrefs[1] = data.get(sm.KEY_INTERESTS_MUSEUM).equals("true");
        usersPrefs[2] = data.get(sm.KEY_INTERESTS_NATURE).equals("true");
        usersPrefs[3] = data.get(sm.KEY_INTERESTS_RESTAURANT).equals("true");

        date = Calendar.DAY_OF_MONTH;
        date = date/10 + date%10;

        recyclerView();

    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        locationsArray = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> categories = snapshot.getChildren();
                Iterator<DataSnapshot> category = categories.iterator();

                int i = 0;

                while(category.hasNext()){
                    DataSnapshot next = (DataSnapshot) category.next();
                    if(usersPrefs[i])
                    {
                        Iterable<DataSnapshot> locations = next.getChildren();
                        Iterator<DataSnapshot> locationDetails = locations.iterator();
                        while(locationDetails.hasNext()){
                            DataSnapshot detail = (DataSnapshot) locationDetails.next();
                            String title = detail.child("name").getValue(String.class);
                            String desc = detail.child("desc").getValue(String.class);
                            float rating = detail.child("rating").getValue(float.class);
                            String imageName = detail.child("image").getValue(String.class);
                            int imageLoc = getApplicationContext().getResources().getIdentifier(imageName, "drawable", getApplicationContext().getPackageName());
                            locationsArray.add(new CardHelperClass(imageLoc, rating, title, desc));

                        }
                    }
                    i++;
                }

                int size = locationsArray.size();
                todaysRec = size % date-1;

                todaysCover.setImageResource(locationsArray.get(todaysRec).getImageSrc());
                todaysTitle.setText(locationsArray.get(todaysRec).getTitle());
                todaysRating.setRating(locationsArray.get(todaysRec).getRating());
                todaysDesc.setText(locationsArray.get(todaysRec).getDesc());

                locationsArray.remove(todaysRec);

                loading.setVisibility(View.GONE);
                adapter = new AdapterHelperClass(locationsArray);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        loading.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(valueEventListener);



    }

}