package com.example.discoverbuc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;


    private static final String LOGIN_STATUS = "IsLoggedIn";
    private static final String REMEMBER_STATUS = "isRemembered";

    public static final String KEY_NAME = "fullName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_BIRTHDAY = "birthday";
    //public static final String KEY_EMAIL = "email";

    public static final String KEY_INTERESTS_NATURE = "int_nature";
    public static final String KEY_INTERESTS_MUSEUM = "int_museum";
    public static final String KEY_INTERESTS_RESTAURANT = "int_restaurant";
    public static final String KEY_INTERESTS_COFFEESHOP = "int_coffeeshop";



    public SessionManager(Context context){

        userSession = context.getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void createLoginSession(String name, String username, String pass, String phoneNr, String birthday, String interestInMuseum, String interestInNature, String interestInRestaurant, String interestInCoffeeShop){

        editor.putBoolean(LOGIN_STATUS, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, pass);
        editor.putString(KEY_PHONE, phoneNr);
        editor.putString(KEY_BIRTHDAY, birthday);

        editor.putString(KEY_INTERESTS_NATURE, interestInNature);
        editor.putString(KEY_INTERESTS_MUSEUM, interestInMuseum);
        editor.putString(KEY_INTERESTS_RESTAURANT, interestInRestaurant);
        editor.putString(KEY_INTERESTS_COFFEESHOP, interestInCoffeeShop);

        editor.commit();

    }

    public HashMap<String, String> getDataFromSession(){

        HashMap<String, String> userData = new HashMap<String,String>();

        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_BIRTHDAY, userSession.getString(KEY_BIRTHDAY, null));

        userData.put(KEY_INTERESTS_NATURE, userSession.getString(KEY_INTERESTS_NATURE, null));
        userData.put(KEY_INTERESTS_MUSEUM, userSession.getString(KEY_INTERESTS_MUSEUM, null));
        userData.put(KEY_INTERESTS_RESTAURANT, userSession.getString(KEY_INTERESTS_RESTAURANT, null));
        userData.put(KEY_INTERESTS_COFFEESHOP, userSession.getString(KEY_INTERESTS_COFFEESHOP, null));

        return userData;

    }

    public boolean checkLoginStatus(){

        return userSession.getBoolean(REMEMBER_STATUS, false);

    }

    public void logoutUser(){

        editor.clear();
        editor.commit();

    }

    public void rememberUser(){
        editor.putBoolean(REMEMBER_STATUS, true);
        editor.commit();
    }
    public void dontRememberUser(){
        editor.putBoolean(REMEMBER_STATUS, false);
        editor.commit();
    }






}
