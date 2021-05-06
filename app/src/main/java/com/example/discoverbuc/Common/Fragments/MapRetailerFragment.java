package com.example.discoverbuc.Common.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.discoverbuc.Menu.HelperClasses.AdapterCategoryHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CategoryCardsHelperClass;
import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import java.util.Iterator;

public class MapRetailerFragment extends Fragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    GoogleMap mGoogleMap;
    double lat, lng;

    ProgressBar loading;

    SupportMapFragment supportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_retailer, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = getView().findViewById(R.id.progress_bar_map);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bigmap_fragment);
        supportMapFragment.getMapAsync(this::onMapReady);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        lat = 44.439663;
        lng = 26.096306;
        mGoogleMap = googleMap;
        LatLng latLng = new LatLng(lat, lng);

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
                    while(locationDetails.hasNext()) {
                        DataSnapshot detail = (DataSnapshot) locationDetails.next();
                        String title;
                        double locLat, locLng;
                        String categoryTag = next.getKey();

                        if(detail.child("name").exists()){
                            title = detail.child("name").getValue(String.class);
                        }else{
                            continue;
                        }

                        if(detail.child("loc").child("lat").exists()){
                            lat = detail.child("loc").child("lat").getValue(double.class);
                        }else{
                            continue;
                        }

                        if(detail.child("loc").child("lng").exists()){
                            lng = detail.child("loc").child("lng").getValue(double.class);
                        }else{
                            continue;
                        }

                        LatLng locLatLng = new LatLng(lat, lng);
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(locLatLng)
                                .title(title)
                                .icon(BitmapDescriptorFactory.fromBitmap(mapIcons(categoryTag)
                                )));
                        marker.setTag(0);
                        marker.showInfoWindow();
                    }
                    loading.setVisibility(View.GONE);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12.5f);
                    mGoogleMap.moveCamera(cameraUpdate);
                    mGoogleMap.setOnMarkerClickListener(MapRetailerFragment.this::onMarkerClick);
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    mGoogleMap.setPadding(0,0,0,150);
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);




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
        }

        int height = 75;
        int width = 75;
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(src);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);
        return icon;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Integer clickCount = (Integer) marker.getTag();
        if(clickCount != null){
            clickCount += 1;
            marker.setTag(clickCount);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
            mGoogleMap.moveCamera(cameraUpdate);
        }

        return false;
    }
}