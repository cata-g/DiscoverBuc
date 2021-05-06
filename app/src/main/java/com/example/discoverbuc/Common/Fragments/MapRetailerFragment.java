package com.example.discoverbuc.Common.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.discoverbuc.Common.HelperClasses.BikeRentPlaceHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterCategoryHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CategoryCardsHelperClass;
import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MapRetailerFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{

    GoogleMap mGoogleMap;
    double lat, lng;

    ProgressBar loading;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

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

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        checkPermission();

        addBikes();
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

                while (category.hasNext()) {
                    DataSnapshot next = (DataSnapshot) category.next();
                    Iterable<DataSnapshot> locations = next.getChildren();
                    Iterator<DataSnapshot> locationDetails = locations.iterator();
                    while (locationDetails.hasNext()) {
                        DataSnapshot detail = (DataSnapshot) locationDetails.next();
                        String title;
                        double locLat, locLng;
                        String categoryTag = next.getKey();

                        if (detail.child("name").exists()) {
                            title = detail.child("name").getValue(String.class);
                        } else {
                            continue;
                        }

                        if (detail.child("loc").child("lat").exists()) {
                            lat = detail.child("loc").child("lat").getValue(double.class);
                        } else {
                            continue;
                        }

                        if (detail.child("loc").child("lng").exists()) {
                            lng = detail.child("loc").child("lng").getValue(double.class);
                        } else {
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
                    mGoogleMap.setPadding(0, 0, 0, 150);

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
        }else if(cat.equals("bicycle")){
            src = R.drawable.icon_bicycle;
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
            mGoogleMap.animateCamera(cameraUpdate);
        }

        return false;
    }

    private void addBikes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bikerentloc");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterable<DataSnapshot> locations = snapshot.getChildren();
                    Iterator<DataSnapshot> location = locations.iterator();

                    while(location.hasNext()){
                        DataSnapshot next = (DataSnapshot) location.next();

                        String name = next.getKey();
                        double lat = next.child("lat").getValue(double.class);
                        double lng = next.child("lng").getValue(double.class);

                        LatLng locLatLng = new LatLng(lat, lng);
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(locLatLng)
                                .title(name)
                                .icon(BitmapDescriptorFactory.fromBitmap(mapIcons("bicycle")
                                )));
                        marker.setTag(0);
                        marker.showInfoWindow();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.toString());
            }
        });

    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        } else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(getActivity(), "Locaiton Permission Denied!", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {


        LocationManager locationManager = (LocationManager) getActivity() .getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if(location != null){
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        LatLng latLng = new LatLng(lat,lng);
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("You are here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                );
                        marker.setTag(0);
                        marker.showInfoWindow();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();

                                double lat = location1.getLatitude();
                                double lng = location1.getLongitude();
                                LatLng latLng = new LatLng(lat,lng);
                                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("You are here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                );

                                marker.setTag(0);
                                marker.showInfoWindow();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                mGoogleMap.animateCamera(cameraUpdate);
                            }
                        };

                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

}