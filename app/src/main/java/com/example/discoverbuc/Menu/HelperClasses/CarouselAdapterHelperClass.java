package com.example.discoverbuc.Menu.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.R;

import java.util.ArrayList;

public class CarouselAdapterHelperClass extends RecyclerView.Adapter<CarouselAdapterHelperClass.ViewHolder> {

    ArrayList<CarouselCardHelperClass> images;

    public CarouselAdapterHelperClass(ArrayList<CarouselCardHelperClass> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_carousel_card_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CarouselCardHelperClass carouselCardHelperClass = images.get(position);
        holder.image.setImageResource(carouselCardHelperClass.getImgSrc());

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.image_pop_design);
        }

    }
}
