package com.example.discoverbuc.Menu.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;

import java.util.ArrayList;

public class AdapterHelperClass extends RecyclerView.Adapter<AdapterHelperClass.ViewHolder> {

    ArrayList<CardHelperClass> locations;
    Context context;

    public AdapterHelperClass(ArrayList<CardHelperClass> locations, Context context) {
        this.locations = locations;
        this.context = context;
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
        holder.rating.setRating(cardHelperClass.getRating());
        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopActivity.class);
                intent.putExtra("tag", cardHelperClass.getTag());
                intent.putExtra("imageRes", cardHelperClass.getImageSrc());
                intent.putExtra("title", cardHelperClass.getTitle());
                intent.putExtra("rating", cardHelperClass.getRating());
                intent.putExtra("category", cardHelperClass.getCategoryTag());
                intent.putExtra("wishedSrc", cardHelperClass.getWishedSrc());
                context.startActivity(intent);
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
            rating = itemView.findViewById(R.id.locationCard_rating);
            moreDetails = itemView.findViewById(R.id.btn_more);

        }
    }

}
