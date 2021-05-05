package com.example.discoverbuc.Common.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterVerticalHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class WishlistRetailerFragment extends Fragment {

    TextView wishlist_not_found;
    ProgressBar loading;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    ArrayList<CardHelperClass> locationsWished;

    HashMap<String, String> data;
    SessionManager sm;

    LinearLayout.LayoutParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recycler_wish);
        wishlist_not_found = getView().findViewById(R.id.text_no_wishlist);
        loading = getView().findViewById(R.id.progress_bar_wishlist);

        loading.setVisibility(View.INVISIBLE);

        sm = new SessionManager(getActivity());
        data = sm.getDataFromSession();

        params = new LinearLayout.LayoutParams(0, 0);
        params.setMargins(0,0,0,0);

        populateRecycler();
    }

    private void populateRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        locationsWished = new ArrayList<>();
        String username = data.get(sm.KEY_USERNAME);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(username).child("wishlist");

        loading.setVisibility(View.VISIBLE);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()){
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }

                wishlist_not_found.setText(" ");
                wishlist_not_found.setLayoutParams(params);

                Iterable<DataSnapshot> locationsWish = snapshot.getChildren();

                for (DataSnapshot id : locationsWish) {

                    String locId = id.getKey();
                    String locCat = id.getValue().toString();

                    DatabaseReference locRef = FirebaseDatabase.getInstance().getReference("locations").child(locCat).child(locId);
                    locRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String title = snapshot.child("name").getValue(String.class);
                            String desc = snapshot.child("desc").getValue(String.class);
                            float rating = snapshot.child("rating").getValue(float.class);
                            String imageName = snapshot.child("image").getValue(String.class);
                            int imageLoc = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                            locationsWished.add(new CardHelperClass(imageLoc, rating, title, desc, locId, locCat, R.drawable.full_heart));

                            adapter = new AdapterVerticalHelperClass(locationsWished, getContext());
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("ERROR", error.toString());
                        }
                    });


                }


                loading.setVisibility(View.GONE);
                loading.setLayoutParams(params);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.toString());
            }
        };

        reference.addListenerForSingleValueEvent(valueEventListener);

    }
}