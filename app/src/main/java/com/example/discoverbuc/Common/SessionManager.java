package com.example.discoverbuc.Common;

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
    public static final String KEY_INTERESTS_MALL = "int_mall";
    public static final String KEY_INTERESTS_PUB = "int_pub";



    public SessionManager(Context context){

        userSession = context.getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void createLoginSession(String name, String username, String pass, String phoneNr, String birthday, String interestInMuseum,
                                   String interestInNature, String interestInRestaurant, String interestInCoffeeShop, String interestInMall, String interestInPub){

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
        editor.putString(KEY_INTERESTS_MALL, interestInMall);
        editor.putString(KEY_INTERESTS_PUB, interestInPub);

        editor.commit();

    }

    public HashMap<String, String> getDataFromSession(){

        HashMap<String, String> userData = new HashMap<String,String>();

        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_BIRTHDAY, userSession.getString(KEY_BIRTHDAY, null));

        userData.put(KEY_INTERESTS_NATURE, userSession.getString(KEY_INTERESTS_NATURE, "false"));
        userData.put(KEY_INTERESTS_MUSEUM, userSession.getString(KEY_INTERESTS_MUSEUM, "false"));
        userData.put(KEY_INTERESTS_RESTAURANT, userSession.getString(KEY_INTERESTS_RESTAURANT, "false"));
        userData.put(KEY_INTERESTS_COFFEESHOP, userSession.getString(KEY_INTERESTS_COFFEESHOP, "false"));
        userData.put(KEY_INTERESTS_MALL, userSession.getString(KEY_INTERESTS_MALL, "false"));
        userData.put(KEY_INTERESTS_PUB, userSession.getString(KEY_INTERESTS_PUB, "false"));

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

    public void changePrefs(int nat, int mus, int res, int cf, int mall, int pub ){

        String natInterest = String.valueOf(nat == 1);
        String musInterest = String.valueOf(mus == 1);
        String resInterest = String.valueOf(res == 1);
        String cfInterest = String.valueOf(cf == 1);
        String mallInterest = String.valueOf(mall == 1);
        String pubInterest = String.valueOf(pub == 1);

        editor.putString(KEY_INTERESTS_NATURE, natInterest);
        editor.putString(KEY_INTERESTS_MUSEUM, musInterest);
        editor.putString(KEY_INTERESTS_RESTAURANT, resInterest);
        editor.putString(KEY_INTERESTS_COFFEESHOP, cfInterest);
        editor.putString(KEY_INTERESTS_MALL, mallInterest);
        editor.putString(KEY_INTERESTS_PUB, pubInterest);
        editor.commit();

    }






}
