package com.example.discoverbuc.Common.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.discoverbuc.Common.SessionManager;
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class HomeRetailerFragment extends Fragment {

    TextView tv;
    Button gotomenu;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressBar loading;

    SessionManager sm;
    HashMap<String, String> data;
    ArrayList<CardHelperClass> locationsArray;
    boolean usersPrefs[];

    int todaysRec,date;
    TextView todaysTitle, todaysDesc, weatherText, casesText;
    RatingBar todaysRating;
    ImageView todaysCover;
    Button moreDetails;

    double temp;
    int weatherCode;
    String weatherDesc;
    boolean shouldRecommendOutdoor;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    TextView dailyAdvice;
    String[] coronaMessages = {"Don't forget to use the mask!", "Take care of your friends and family!", "If you have symptoms of any kind remain at home!",
            "Use the hand sanitizer as often as you can!", "Don't go out in groups larger than six!", "Respect the circulation rules!", "Keep the distance from the people around!", "Respect the hygiene rules!"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        loading = getView().findViewById(R.id.progress_bar_menu);
        loading.setVisibility(View.GONE);
        todaysCover = getView().findViewById(R.id.todaysCover);
        todaysDesc = getView().findViewById(R.id.todaysDesc);
        todaysRating = getView().findViewById(R.id.todaysRating);
        todaysTitle = getView().findViewById(R.id.todaysTitle);
        weatherText = getView().findViewById(R.id.weatherText);
        casesText = getView().findViewById(R.id.casesText);
        dailyAdvice = getView().findViewById(R.id.DailyAdvice);
        moreDetails = getView().findViewById(R.id.todaysMore);

        sm = new SessionManager(getActivity());
        data = sm.getDataFromSession();

        usersPrefs = new boolean[10];
        usersPrefs[0] = data.get(sm.KEY_INTERESTS_COFFEESHOP).equals("true");
        usersPrefs[1] = data.get(sm.KEY_INTERESTS_MUSEUM).equals("true");
        usersPrefs[2] = data.get(sm.KEY_INTERESTS_NATURE).equals("true");
        usersPrefs[3] = data.get(sm.KEY_INTERESTS_RESTAURANT).equals("true");

        Calendar cal = Calendar.getInstance();
        date = cal.get(Calendar.DAY_OF_MONTH);
        date = date/10 + date%10;


        getWeatherDetails();
        getCoronaData();
        recyclerView();
        dailyAdvice.setText(coronaMessages[ThreadLocalRandom.current().nextInt(0, coronaMessages.length)]);


    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        locationsArray = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> categories = snapshot.getChildren();
                Iterator<DataSnapshot> category = categories.iterator();

                int i = 0;
                while(category.hasNext()){
                    DataSnapshot next = (DataSnapshot) category.next();
                    if(usersPrefs[i])
                    {
                        Iterable<DataSnapshot> locations = next.getChildren();
                        Iterator<DataSnapshot> locationDetails = locations.iterator();
                        while(locationDetails.hasNext()){
                            DataSnapshot detail = (DataSnapshot) locationDetails.next();
                            if(!shouldRecommendOutdoor){
                                boolean detailOutdoor = detail.child("outdoor").getValue(boolean.class);
                                if(!detailOutdoor){
                                    String tag = detail.getKey();
                                    String categoryTag = next.getKey();
                                    String title = detail.child("name").getValue(String.class);
                                    String desc = detail.child("desc").getValue(String.class);
                                    float rating = detail.child("rating").getValue(float.class);
                                    String imageName = detail.child("image").getValue(String.class);
                                    int imageLoc = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                                    locationsArray.add(new CardHelperClass(imageLoc, rating, title, desc, tag, categoryTag));
                                }
                            }else{
                                String tag = detail.getKey();
                                String categoryTag = next.getKey();
                                String title = detail.child("name").getValue(String.class);
                                String desc = detail.child("desc").getValue(String.class);
                                float rating = detail.child("rating").getValue(float.class);
                                String imageName = detail.child("image").getValue(String.class);
                                int imageLoc = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                                locationsArray.add(new CardHelperClass(imageLoc, rating, title, desc, tag, categoryTag));
                            }

                        }
                    }
                    i++;
                }

                int size = locationsArray.size();
                todaysRec = Math.max(size,date) % Math.min(size,date);

                String tag = locationsArray.get(todaysRec).getTag();
                String categoryTag = locationsArray.get(todaysRec).getCategoryTag();
                String title = locationsArray.get(todaysRec).getTitle();
                String desc = locationsArray.get(todaysRec).getDesc();
                float rating = locationsArray.get(todaysRec).getRating();
                int imageLoc =  locationsArray.get(todaysRec).getImageSrc();

                todaysCover.setImageResource(imageLoc);
                todaysTitle.setText(title);
                todaysRating.setRating(rating);
                todaysDesc.setText(desc);

                moreDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PopActivity.class);
                        intent.putExtra("tag",tag);
                        intent.putExtra("imageRes", imageLoc);
                        intent.putExtra("title", title);
                        intent.putExtra("desc", desc);
                        intent.putExtra("rating", rating);
                        intent.putExtra("category", categoryTag);
                        startActivity(intent);
                    }
                });

                locationsArray.remove(todaysRec);

                loading.setVisibility(View.GONE);
                adapter = new AdapterHelperClass(locationsArray, getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        loading.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(valueEventListener);



    }

    public void getWeatherDetails(){

        String url = "http://api.openweathermap.org/data/2.5/weather?q=Bucharest,RO&appid=a6b1360c11727203d8744d2d3dad55a6&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    weatherCode = jsonObjectWeather.getInt("id");
                    weatherDesc = jsonObjectWeather.getString("main");

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    temp = jsonObjectMain.getDouble("temp");

                    String textShown = decimalFormat.format(temp) + "°C" + "\n" + weatherDesc;
                    weatherText.setText(textShown);
                    weatherCode /= 100;
                    shouldRecommendOutdoor = weatherCode == 8;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void getCoronaData(){

        String url = "https://wrapapi.com/use/imN0oB/corona_virus_buc/cases_buc/0.0.2?wrapAPIKey=T4wPZdfHGDGzU6WIaHXPUtKKmNn41FMF";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray outputArray = dataObject.getJSONArray("output");

                    double cases = outputArray.getDouble(3);


                    String textShown = decimalFormat.format(cases) + "‰";
                    casesText.setText(textShown);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}