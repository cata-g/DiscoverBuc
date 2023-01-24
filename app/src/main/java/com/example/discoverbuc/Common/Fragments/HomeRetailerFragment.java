package com.example.discoverbuc.Common.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.discoverbuc.Menu.HelperClasses.AdapterCategoryHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.AdapterVerticalHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CardHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.CategoryCardsHelperClass;
import com.example.discoverbuc.Menu.HelperClasses.SwipeToWishlistCallback;
import com.example.discoverbuc.Menu.PopActivity;
import com.example.discoverbuc.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HomeRetailerFragment extends Fragment {

    private static HomeRetailerFragment instance;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    RecyclerView categoriesView;
    RecyclerView.Adapter categoriesAdapter;

    boolean isVertical = false;

    ProgressBar loading;

    SessionManager sm;
    HashMap<String, String> data;

    ArrayList<CardHelperClass> locationsArray;
    ArrayList<CardHelperClass> showArray;
    ArrayList<CategoryCardsHelperClass> categoriesArray;

    HashMap<String, Integer> locInCat = new HashMap<String, Integer>();

    boolean usersPrefs[];

    int todaysRec,date;
    TextView todaysTitle, weatherText, casesText;
    RatingBar todaysRating;
    ImageView todaysCover;
    AppCompatButton moreDetails;

    double temp;
    int weatherCode;
    String weatherDesc;
    boolean shouldRecommendOutdoor;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    TextView dailyAdvice;
    static String[] coronaMessages = {"Don't forget to use the mask!", "Take care of your friends and family!", "If you have symptoms of any kind remain at home!",
            "Use the hand sanitizer as often as you can!", "Don't go out in groups larger than six!", "Respect the circulation rules!", "Keep the distance from the people around!", "Respect the hygiene rules!"};

    Button seeMoreDetails;
    boolean recyclerHorizontal = true;

    String tag, categoryTag, title, desc, imageName;
    float rating;
    int wishedSrc = R.drawable.empty_heart, imageLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        return inflater.inflate(R.layout.fragment_home_retailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerView);
        categoriesView = getView().findViewById(R.id.categories_recycler);
        loading = getView().findViewById(R.id.progress_bar_menu);
        loading.setVisibility(View.GONE);
        todaysCover = getView().findViewById(R.id.todaysCover);
        todaysRating = getView().findViewById(R.id.todaysRating);
        todaysTitle = getView().findViewById(R.id.todaysTitle);
        weatherText = getView().findViewById(R.id.weatherText);
        casesText = getView().findViewById(R.id.casesText);
        dailyAdvice = getView().findViewById(R.id.DailyAdvice);
        moreDetails = getView().findViewById(R.id.todaysMore);


        seeMoreDetails = getView().findViewById(R.id.details_otherrec);
        seeMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerHorizontal = !recyclerHorizontal;

                if(recyclerHorizontal == true){
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    seeMoreDetails.setText("SEE MORE");
                    adapter = new AdapterHelperClass(showArray, getActivity());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    isVertical = false;
                }else{
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    seeMoreDetails.setText("SEE LESS");
                    adapter = new AdapterVerticalHelperClass(showArray, getActivity());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    enableSwipeToWishlistAndUndo();
                    isVertical = true;
                }


            }
        });

        sm = new SessionManager(getActivity());
        data = sm.getDataFromSession();

        usersPrefs = new boolean[10];
        usersPrefs[0] = data.get(sm.KEY_INTERESTS_COFFEESHOP).equals("true");
        usersPrefs[1] = data.get(sm.KEY_INTERESTS_MALL).equals("true");
        usersPrefs[2] = data.get(sm.KEY_INTERESTS_MUSEUM).equals("true");
        usersPrefs[3] = data.get(sm.KEY_INTERESTS_NATURE).equals("true");
        usersPrefs[4] = data.get(sm.KEY_INTERESTS_PUB).equals("true");
        usersPrefs[5] = data.get(sm.KEY_INTERESTS_RESTAURANT).equals("true");

        Calendar cal = Calendar.getInstance();
        date = cal.get(Calendar.DAY_OF_MONTH);
        date = date/10 + date%10;

        getWeatherDetails();
        shouldRecommendOutdoor = false;
        getCoronaData();
        recyclerView();
        dailyAdvice.setText(coronaMessages[new Random().nextInt(coronaMessages.length)]);


    }

    private void recyclerView() {


        loading.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        categoriesView.setHasFixedSize(true);
        categoriesView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        locationsArray = new ArrayList<>();
        categoriesArray = new ArrayList<>();
        showArray = new ArrayList<>();
        categoriesArray.add(new CategoryCardsHelperClass("for you"));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> categories = snapshot.getChildren();
                Iterator<DataSnapshot> category = categories.iterator();

                int i = 0;
                while(category.hasNext()){
                    int j = 0;
                    DataSnapshot next = (DataSnapshot) category.next();
                    if(usersPrefs[i])
                    {
                        Iterable<DataSnapshot> locations = next.getChildren();
                        Iterator<DataSnapshot> locationDetails = locations.iterator();
                        while(locationDetails.hasNext()){
                            DataSnapshot detail = (DataSnapshot) locationDetails.next();
                            tag = detail.getKey();
                            categoryTag = next.getKey();

                            if(detail.child("name").exists()){
                                title = detail.child("name").getValue(String.class);
                            }else{
                                continue;
                            }

                            if(detail.child("desc").exists()){
                                desc = detail.child("desc").getValue(String.class);
                            }else{
                                continue;
                            }

                            if(detail.child("rating").exists()){
                                rating = detail.child("rating").getValue(float.class);
                            }else{
                                continue;
                            }

                            if(detail.child("image").exists())
                            {
                                imageName = detail.child("image").getValue(String.class);
                                imageLoc = getActivity().getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                            }else{
                                continue;
                            }

                            boolean outdoor;
                            if(detail.child("outdoor").exists()){
                                outdoor = detail.child("outdoor").getValue(boolean.class);
                            }else{
                                continue;
                            }

                            if(!shouldRecommendOutdoor)
                                if(outdoor)
                                    continue;
                            locationsArray.add(new CardHelperClass(imageLoc, rating, title, desc, tag, categoryTag, wishedSrc));
                            j++;
                        }

                    }

                    if(j != 0)
                    {
                        locInCat.put(next.getKey(), j);
                        categoriesArray.add(new CategoryCardsHelperClass(next.getKey()));
                    }
                    i++;
                }

                int size = locationsArray.size();
                todaysRec = Math.max(size, date) / Math.min(size, date);

                String tag = locationsArray.get(todaysRec).getTag();
                String categoryTag = locationsArray.get(todaysRec).getCategoryTag();
                String title = locationsArray.get(todaysRec).getTitle();
                float rating = locationsArray.get(todaysRec).getRating();
                int imageLoc =  locationsArray.get(todaysRec).getImageSrc();

                todaysCover.setImageResource(imageLoc);
                todaysTitle.setText(title);
                todaysRating.setRating(rating);

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

                for(int j = 0; j < locationsArray.size();j++)
                    checkWishlist(j);



                populateShowArray("for you");
                adapter = new AdapterHelperClass(showArray, getActivity());
                recyclerView.setAdapter(adapter);

                checkCat();
                categoriesAdapter = new AdapterCategoryHelperClass(categoriesArray, getActivity());
                categoriesView.setAdapter(categoriesAdapter);

                loading.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        loading.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(valueEventListener);



    }

    public void populateShowArray(String category){

        showArray = new ArrayList<>();

        if(category.equals("for you")){
            showArray.addAll(locationsArray);
        }else{
            for(CardHelperClass card : locationsArray)
                if(card.getCategoryTag().equals(category))
                    showArray.add(card);
        }


        adapter = isVertical ? new AdapterVerticalHelperClass(showArray, getActivity()) : new AdapterHelperClass(showArray, getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void checkCat(){
        int i = 1;
        for(Map.Entry<String, Integer> cat : locInCat.entrySet())
        {
            if(cat.getValue() == 0)
            {
                categoriesArray.remove(i);
            }
            i++;
        }
    }

    public void getWeatherDetails(){

        String url = "API";
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

                    String textShown = decimalFormat.format(temp) + "°C";
                    weatherText.setText(textShown);
                    weatherCode /= 100;
                    shouldRecommendOutdoor = weatherCode == 8;
                    if(temp < 5)
                        shouldRecommendOutdoor = false;

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

        String url = "API";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONObject outputArray = dataObject.getJSONObject("output");

                    String casesStr = outputArray.toString();
                    if(casesStr.charAt(21) < 9 && casesStr.charAt(21) > 0)
                        casesStr = casesStr.substring(18,22);
                    else
                        casesStr = casesStr.substring(18,21) + '0';


                    String textShown = casesStr + "‰";
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

    private void enableSwipeToWishlistAndUndo(){

        SwipeToWishlistCallback swipeToWishlistCallback = new SwipeToWishlistCallback(getActivity()){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                addToWishlist(locationsArray.get(pos).getTag(), locationsArray.get(pos).getCategoryTag(), pos);
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToWishlistCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void addToWishlist(String tagToAdd, String catToAdd, int pos) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(data.get("username"));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("wishlist").exists()) {
                    ref.child("wishlist").child(tagToAdd).setValue(catToAdd);
                    Toast.makeText(getActivity(), "ITEM ADDED TO WISHLIST", Toast.LENGTH_SHORT).show();

                    locationsArray.get(pos).setWishedSrc(R.drawable.full_heart);
                    adapter.notifyDataSetChanged();
                } else {
                    if (snapshot.child("wishlist").child(tagToAdd).exists()) {
                        ref.child("wishlist").child(tagToAdd).removeValue();
                        Toast.makeText(getActivity(), "ITEM REMOVED FROM WISHLIST", Toast.LENGTH_SHORT).show();

                        locationsArray.get(pos).setWishedSrc(R.drawable.empty_heart);
                        adapter.notifyDataSetChanged();
                    } else {
                        ref.child("wishlist").child(tagToAdd).setValue(catToAdd);
                        Toast.makeText(getActivity(), "ITEM ADDED TO WISHLIST", Toast.LENGTH_SHORT).show();

                        locationsArray.get(pos).setWishedSrc(R.drawable.full_heart);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.toString());
            }
        });
    }

    public  static  HomeRetailerFragment getInstance(){
        return instance;
    }

    private void checkWishlist(int pos){
        DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference("users").child(data.get("username")).child("wishlist").child(locationsArray.get(pos).getTag());
        wishRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    wishedSrc = R.drawable.full_heart;
                    locationsArray.get(pos).setWishedSrc(wishedSrc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
