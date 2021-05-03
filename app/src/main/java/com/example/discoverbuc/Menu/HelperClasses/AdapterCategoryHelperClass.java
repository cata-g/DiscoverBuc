package com.example.discoverbuc.Menu.HelperClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverbuc.R;

import java.util.ArrayList;

public class AdapterCategoryHelperClass extends RecyclerView.Adapter<AdapterCategoryHelperClass.ViewHolder> {

    ArrayList<CategoryCardsHelperClass> categories;
    Context context;

    public AdapterCategoryHelperClass(ArrayList<CategoryCardsHelperClass> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryCardsHelperClass categoryCardsHelperClass = categories.get(position);

        holder.title.setText(categoryCardsHelperClass.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("moglinz", categoryCardsHelperClass.getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.category_title_detail);
        }
    }
}
