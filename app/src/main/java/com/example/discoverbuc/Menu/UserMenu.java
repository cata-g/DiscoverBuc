package com.example.discoverbuc.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class UserMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView();

    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardHelperClass> locationsArray = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> categories = snapshot.getChildren();
                Iterator<DataSnapshot> category = categories.iterator();

                while(category.hasNext()){
                    DataSnapshot next = (DataSnapshot) category.next();
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
                        Log.d("Message", "MERGE PANA AICI");
                        Log.d("name", title);
                        Log.d("imageloc", imageName);
                        Log.d("desc", desc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);

        adapter = new AdapterHelperClass(locationsArray);
        recyclerView.setAdapter(adapter);

    }
}