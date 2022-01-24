package com.app.cloudy;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";

    public static JSONObject getJSON(double lat, double lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, String.valueOf(lat), String.valueOf(lon), "f27d5d2a24dd4bfe1e783fdf24aaca8b"));
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            try {
                connection.connect();
            }catch (Exception e){
                Log.e("connect", e.toString());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            Log.e("main", e.toString());
            return null;
        }
    }
}