package com.example.discoverbuc.Common.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.R;
import com.example.discoverbuc.Register2.HelperClasses.PrefsHelperClass;
import com.example.discoverbuc.Register2.StartupScreen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class ProfileRetailerFragment extends Fragment {

    CheckBox cf, museum, nature, restaurant;
    Button logout,save, edit;

    HashMap<String, String> data;
    boolean[] usersPrefs;
    SessionManager sm;

    int counter = 0, pref_Cf = 0, pref_Mus = 0, pref_Rest = 0, pref_Nat = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout = getView().findViewById(R.id.logout_button);
        save = getView().findViewById(R.id.save_edit_prefs);
        edit = getView().findViewById(R.id.edit_prefs_button);

        save.setVisibility(View.INVISIBLE);

        cf = getView().findViewById(R.id.profile_cf);
        museum = getView().findViewById(R.id.profile_museum);
        nature = getView().findViewById(R.id.profile_nature);
        restaurant = getView().findViewById(R.id.profile_restaurant);

        sm = new SessionManager(getActivity());
        data = sm.getDataFromSession();

        usersPrefs = new boolean[10];

        usersPrefs[0] = data.get(sm.KEY_INTERESTS_COFFEESHOP).equals("true");
        usersPrefs[1] = data.get(sm.KEY_INTERESTS_MUSEUM).equals("true");
        usersPrefs[2] = data.get(sm.KEY_INTERESTS_NATURE).equals("true");
        usersPrefs[3] = data.get(sm.KEY_INTERESTS_RESTAURANT).equals("true");

        cf.setChecked(usersPrefs[0]);
        museum.setChecked(usersPrefs[1]);
        nature.setChecked(usersPrefs[2]);
        restaurant.setChecked(usersPrefs[3]);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEdit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        sm.logoutUser();
        Intent intent = new Intent(getActivity(), StartupScreen.class);
        startActivity(intent);
        getActivity().finish();
    }

    private boolean validation(){

        counter = 0;

        if(nature.isChecked()) {
            counter++;
            pref_Nat = 1;
        }
        if(museum.isChecked()){
            counter++;
            pref_Mus = 1;
        }
        if(cf.isChecked()){
            counter++;
            pref_Cf = 1;
        }
        if(restaurant.isChecked()){
            counter++;
            pref_Rest  = 1;
        }

        if(counter == 0){
            Toast.makeText(getActivity(), "You must select at least one", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveEdit(){

        if(!validation()){
            return;
        }

        PrefsHelperClass prefsHelperClass = new PrefsHelperClass(pref_Nat, pref_Mus, pref_Rest, pref_Cf);
        sm.changePrefs(pref_Nat, pref_Mus, pref_Rest, pref_Cf);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(data.get(sm.KEY_USERNAME)).child("Prefs");
        reference.setValue(prefsHelperClass);

        enable_check(false);
        save.setVisibility(View.INVISIBLE);

        Toast.makeText(getActivity(), "Preferences Successfully Updated", Toast.LENGTH_SHORT).show();

    }

    private void startEdit(){
        enable_check(true);
        save.setVisibility(View.VISIBLE);
    }

    private void enable_check(boolean mode){

        cf.setEnabled(mode);
        museum.setEnabled(mode);
        nature.setEnabled(mode);
        restaurant.setEnabled(mode);

    }
}