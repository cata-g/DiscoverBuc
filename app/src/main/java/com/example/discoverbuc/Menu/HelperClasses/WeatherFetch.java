package com.example.discoverbuc.Menu.HelperClasses;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFetch {

    private static URL url;

    public JSONObject getJSON(Context context){

        try {

            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Bucharest,RO&appid=a6b1360c11727203d8744d2d3dad55a6&units=metric");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            while(reader.readLine() != null)
                json.append(reader.readLine()).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200){
                return null;
            }

            return data;

        } catch(Exception exception){
            return null;
        }

    }


}
