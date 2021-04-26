package com.example.discoverbuc.Menu.HelperClasses;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.Menu.UserMenu;
import com.example.discoverbuc.R;

import java.util.ArrayList;

public class AdapterHelperClass extends RecyclerView.Adapter<AdapterHelperClass.ViewHolder> {

    ArrayList<CardHelperClass> locations;

    public AdapterHelperClass(ArrayList<CardHelperClass> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_card_design, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardHelperClass cardHelperClass = locations.get(position);

        holder.cover.setImageResource(cardHelperClass.getImageSrc());
        holder.title.setText(cardHelperClass.getTitle());
        holder.desc.setText(cardHelperClass.getDesc());
        holder.rating.setRating(cardHelperClass.getRating());
        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", cardHelperClass.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cover;
        TextView title, desc;
        RatingBar rating;
        Button moreDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.locationCard_cover);
            title = itemView.findViewById(R.id.locationCard_title);
            desc = itemView.findViewById(R.id.locationCard_desc);
            rating = itemView.findViewById(R.id.locationCard_rating);
            moreDetails = itemView.findViewById(R.id.btn_more);

        }
    }

}
