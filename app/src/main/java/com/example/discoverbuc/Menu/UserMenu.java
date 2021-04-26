package com.example.discoverbuc.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.R;
import com.example.discoverbuc.SessionManager;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class UserMenu extends AppCompatActivity {

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

    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        recyclerView = findViewById(R.id.recyclerView);
        loading = findViewById(R.id.progress_bar_menu);
        loading.setVisibility(View.GONE);
        todaysCover = findViewById(R.id.todaysCover);
        todaysDesc = findViewById(R.id.todaysDesc);
        todaysRating = findViewById(R.id.todaysRating);
        todaysTitle = findViewById(R.id.todaysTitle);
        weatherText = findViewById(R.id.weatherText);
        casesText = findViewById(R.id.casesText);

        sm = new SessionManager(this);
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

    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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
                            String tag = detail.getKey();
                            String title = detail.child("name").getValue(String.class);
                            String desc = detail.child("desc").getValue(String.class);
                            float rating = detail.child("rating").getValue(float.class);
                            String imageName = detail.child("image").getValue(String.class);
                            int imageLoc = getApplicationContext().getResources().getIdentifier(imageName, "drawable", getApplicationContext().getPackageName());
                            locationsArray.add(new CardHelperClass(imageLoc, rating, title, desc, tag));

                        }
                    }
                    i++;
                }

                int size = locationsArray.size();
                    todaysRec = Math.max(size,date) % Math.min(size,date);

                todaysCover.setImageResource(locationsArray.get(todaysRec).getImageSrc());
                todaysTitle.setText(locationsArray.get(todaysRec).getTitle());
                todaysRating.setRating(locationsArray.get(todaysRec).getRating());
                todaysDesc.setText(locationsArray.get(todaysRec).getDesc());

                locationsArray.remove(todaysRec);

                loading.setVisibility(View.GONE);
                adapter = new AdapterHelperClass(locationsArray);
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
                    String desc = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp");

                    String textShown = decimalFormat.format(temp) + "°C";
                    weatherText.setText(textShown);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

//    public void showMoreDetails(View view){
//
//        Button btn = findViewById(R.id.btn_more);
//        String tag = btn.getTag().toString();
//        Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getApplicationContext(), PopActivity.class);
//        startActivity(intent);
//
//    }

}