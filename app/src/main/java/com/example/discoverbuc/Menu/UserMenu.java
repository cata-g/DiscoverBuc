package com.example.discoverbuc.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.R;

import java.util.ArrayList;

public class UserMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView();

    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardHelperClass> locations = new ArrayList<>();

        locations.add(new CardHelperClass(R.drawable.palatul_parlamentului, 5, "Palatu Parlamentului", "asdjahj dajh dad kja dkjasd asjd asd a"));
        locations.add(new CardHelperClass(R.drawable.palatul_parlamentului, 3.5f, "Titlu 2", "asasddsfa dkjasd asjd asd a"));
        locations.add(new CardHelperClass(R.drawable.palatul_parlamentului, 2.5f, "Titlu 3", "asdjahasdasd dkjasd asjd asd a"));
        locations.add(new CardHelperClass(R.drawable.palatul_parlamentului, 1f, "Titlu 4", "asdjahj dajh dad kasd asdasdja dkjasd aasd asd asd as ddassjd asd a"));

        adapter = new AdapterHelperClass(locations);
        recyclerView.setAdapter(adapter);

    }
}