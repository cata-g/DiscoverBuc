package com.example.discoverbuc.Common.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){

        MapMarkerInfoHelperClass markerInfo = (MapMarkerInfoHelperClass) marker.getTag();

        RatingBar ratingBar = view.findViewById(R.id.rating_infoW);
        TextView titleView = view.findViewById(R.id.title_infoW);
        ImageView imageView = view.findViewById(R.id.image_infoW);
        TextView clickText = view.findViewById(R.id.click_infoW);

        String cat = markerInfo.getCatTag();
        if(cat.equals("bicycle")){
            titleView.setText(markerInfo.getTitle());
            ratingBar.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            clickText.setVisibility(View.GONE);
        }
        else{

            ratingBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            clickText.setVisibility(View.VISIBLE);

            String title = markerInfo.getTitle();
            float rating = markerInfo.getRating();
            int image = markerInfo.getImageLoc();

            titleView.setText(title);
            ratingBar.setRating(rating);
            imageView.setImageResource(image);
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
