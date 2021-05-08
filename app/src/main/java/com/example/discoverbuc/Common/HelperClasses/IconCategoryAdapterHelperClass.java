package com.example.discoverbuc.Common.HelperClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.Common.Fragments.MapRetailerFragment;
import com.example.discoverbuc.R;

import java.util.ArrayList;

public class IconCategoryAdapterHelperClass extends RecyclerView.Adapter<IconCategoryAdapterHelperClass.ViewHolder> {

    ArrayList<IconCategoryHelperClass> icons;
    Context context;

    public IconCategoryAdapterHelperClass(ArrayList<IconCategoryHelperClass> icons, Context context) {
        this.icons = icons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_map_card_design, parent, false);
        IconCategoryAdapterHelperClass.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        IconCategoryHelperClass icon = icons.get(position);

        holder.category.setImageResource(icon.getIcon());
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapRetailerFragment.getInstance().showMarkers(icon.getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton category;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.icon_map_button);

        }
    }
}
