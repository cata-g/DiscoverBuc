package com.example.discoverbuc.Common.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.discoverbuc.Common.Dashboard;
import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.Menu.UserMenu;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.StartupScreen;

import java.util.HashMap;

public class HomeRetailerFragment extends Fragment {

    TextView tv;
    Button gotomenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = (TextView) getView().findViewById(R.id.textView);
        SessionManager sm = new SessionManager(getActivity());
        HashMap<String, String> data = sm.getDataFromSession();

        String name = data.get(sm.KEY_NAME);

        tv.setText(name);

        gotomenu = (Button) getView().findViewById(R.id.gotomenu);

        gotomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserMenu.class);
                startActivity(intent);
            }
        });


    }

}