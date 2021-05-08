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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.discoverbuc.Common.HelperClasses.BikeRentPlaceHelperClass;
import com.example.discoverbuc.Common.HelperClasses.CustomInfoWindowAdapter;
import com.example.discoverbuc.Common.HelperClasses.IconCategoryAdapterHelperClass;
import com.example.discoverbuc.Common.HelperClasses.IconCategoryHelperClass;
import com.example.discoverbuc.Common.HelperClasses.MapMarkerInfoHelperClass;
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

    private static MapRetailerFragment instance;

    GoogleMap mGoogleMap;
    double lat, lng;

    ProgressBar loading;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    RecyclerView iconRecycler;
    RecyclerView.Adapter adapter;
    ArrayList<IconCategoryHelperClass> iconArray;
    ArrayList<Marker> allMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        instance = this;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = getView().findViewById(R.id.progress_bar_map);
        iconRecycler = getView().findViewById(R.id.recycler_icon_map);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        checkPermission();
        allMarkers = new ArrayList<>();
        iconArray = new ArrayList<>();
        iconArray.add(new IconCategoryHelperClass(BarIcons("all"), "all"));
        iconArray.add(new IconCategoryHelperClass(BarIcons("bicycle"), "bicycle"));

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bigmap_fragment);
        supportMapFragment.getMapAsync(this::onMapReady);
        addBikes();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        iconRecycler.setHasFixedSize(true);
        iconRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mGoogleMap = googleMap;

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

                    iconArray.add(new IconCategoryHelperClass(BarIcons(next.getKey()), next.getKey()));

                    while (locationDetails.hasNext()) {
                        DataSnapshot detail = (DataSnapshot) locationDetails.next();
                        String title,desc, imageName;
                        float rating;
                        int imageLoc;
                        double locLat, locLng;
                        String categoryTag = next.getKey();
                        String tag = detail.getKey();

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

                        if(detail.child("desc").exists()){
                            desc = detail.child("desc").getValue(String.class);
                        }else{
                            continue;
                        }

                        if(detail.child("rating").exists()){
                            rating = detail.child("rating").getValue(float.class);
                        }else{
                            continue;
                        }

                        if(detail.child("rating").exists())
                        {
                            imageName = detail.child("image").getValue(String.class);
                            imageLoc = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                        }else{
                            continue;
                        }

                        int clickcount = 0;
                        MapMarkerInfoHelperClass mapMarkerInfo = new MapMarkerInfoHelperClass(clickcount, tag, categoryTag, title, desc, rating, imageLoc, getActivity());

                        LatLng locLatLng = new LatLng(lat, lng);
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(locLatLng)
                                .title(title)
                                .icon(BitmapDescriptorFactory.fromBitmap(mapIcons(categoryTag)
                                )));
                        marker.setTag(mapMarkerInfo);
                        marker.setVisible(false);
                        allMarkers.add(marker);
                    }
                    loading.setVisibility(View.GONE);
                    mGoogleMap.setOnMarkerClickListener(MapRetailerFragment.this::onMarkerClick);
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    mGoogleMap.setPadding(0, 0, 0, 150);
                    mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));

                    adapter = new IconCategoryAdapterHelperClass(iconArray, getActivity());
                    iconRecycler.setAdapter(adapter);
                    showMarkers("all");

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);

    }

    public Bitmap mapIcons(String cat){

        int src = BarIcons(cat);

        int height = 75;
        int width = 75;
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(src);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);
        return icon;

    }

    public int BarIcons(String cat){
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
        }else if(cat.equals("malls")){
            src = R.drawable.icon_mall;
        }else if(cat.equals("pubs")){
            src = R.drawable.icon_pub;
        }else if(cat.equals("all")){
            src = R.drawable.icon_user;
        }

        return src;
    }

    public void showMarkers(String cat){

        if(cat == "all"){
            for (Marker marker : allMarkers)
                marker.setVisible(true);
        }else{
            for(Marker marker : allMarkers)
            {
                MapMarkerInfoHelperClass tag = (MapMarkerInfoHelperClass) marker.getTag();
                if(tag.getCatTag().equals(cat))
                    marker.setVisible(true);
                else
                    marker.setVisible(false);
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        MapMarkerInfoHelperClass markerInfo = (MapMarkerInfoHelperClass) marker.getTag();
        int clickCount = markerInfo.getClickCount();

        if(clickCount == 0){
            markerInfo.setClickCount(clickCount++);
            marker.setTag(markerInfo);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20);
            mGoogleMap.animateCamera(cameraUpdate);
        }
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                markerInfo.openPop();
            }
        });

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
                                .icon(BitmapDescriptorFactory.fromBitmap(mapIcons("bicycle"))
                                ));
                        marker.setTag(new MapMarkerInfoHelperClass(0, "bicycle", marker.getTitle(), getActivity()));
                        //marker.showInfoWindow();
                        marker.setVisible(false);
                        allMarkers.add(marker);

                    }

                }
                showMarkers("all");
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
            Toast.makeText(getActivity(), "Location Permission Denied!", Toast.LENGTH_SHORT).show();
            lat = 44.439663;
            lng = 26.096306;
            LatLng latLng = new LatLng(lat, lng);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mGoogleMap.animateCamera(cameraUpdate);
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
                        marker.setTag(new MapMarkerInfoHelperClass(0, "bicycle", marker.getTitle(), getActivity()));
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

                                marker.setTag(new MapMarkerInfoHelperClass(0, "bicycle", marker.getTitle(), getActivity()));
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

    public static MapRetailerFragment getInstance(){
        return instance;
    }

}