package com.app.cloudy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import static com.app.cloudy.MyLocationListener.imHere;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    double E_temp;
    double latidue;
    double longtidue;
    public static final String LOG_TAG = "Main";
    View Update;
    TextView Degrees;
    TextView Humidity;
    TextView Wind;
    TextView ETemp;
    TextView City;
    Handler handler;
    LocationManager locationManager;

    ImageView Headgear;
    ImageView Body;
    ImageView Bottom;
    ImageView Shoes;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        else {
            getLoc();
        }

        Headgear = (ImageView)findViewById(R.id.main_activity_headgear_image);
        Body = (ImageView)findViewById(R.id.main_activity_body_image);
        Bottom = (ImageView)findViewById(R.id.main_activity_bottom_image);
        Shoes = (ImageView)findViewById(R.id.main_activity_shoes_image);

        Update = findViewById(R.id.main_activity_update);
        City = (TextView)findViewById(R.id.main_activity_yellow_geo);
        Degrees = (TextView)findViewById(R.id.main_activity_yellow_degrees);
        Humidity = (TextView)findViewById(R.id.main_activity_humidity_value);
        Wind = (TextView)findViewById(R.id.main_activity_wind_value);
        ETemp = (TextView)findViewById(R.id.main_activity_e_temp_value);
        handler = new Handler();

        GetWeather();


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                }
                else {
                    getLoc();
                }
                GetWeather();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getLoc();
        }
        else {
            Toast.makeText(this, "Permisson denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private  void GetWeather(){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(latidue, longtidue);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){

                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            RenderWeather(json);
                            SetClothes();
                        }
                    });
                }
            }
        }.start();
    }

    private void RenderWeather(JSONObject json){

        double temp = 0;
        double humidity = 0;
        double wind = 0;
        String city = "";
        try{
            JSONObject main = json.getJSONObject("main");
            humidity =  main.getDouble("humidity");
            temp = main.getDouble("temp") - 273;
            wind = json.getJSONObject("wind").getDouble("speed");
            city = json.getString("name");
        }catch(Exception e){

        }

        Humidity.setText(String.valueOf(humidity));
        Degrees.setText(String.format("%.2f",temp));
        Wind.setText(String.valueOf(wind));
        City.setText(city);
        E_temp =Clothing_Selection.EffectiveTemperature(temp, humidity, wind);

        ETemp.setText(String.format("%.2f", E_temp));

    }

    @SuppressLint("MissingPermission")
    private void getLoc(){

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latidue =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longtidue =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void SetClothes(){

        if (E_temp < 0){
            Headgear.setImageResource(R.drawable.hat);
        } else {
            Headgear.setImageResource(R.drawable.cap);
        }

        if (E_temp < -5){
            Body.setImageResource(R.drawable.down_coat);
        } else {
            if (E_temp < 8){
                Body.setImageResource(R.drawable.jacket);
            }
            else {
                if (E_temp < 18){
                    Body.setImageResource(R.drawable.hoodie);
                }
                else {
                    Body.setImageResource(R.drawable.tshirt);
                }
            }
        }

        if (E_temp < 16){
            Bottom.setImageResource(R.drawable.pants);
        }
        else {
            Bottom.setImageResource(R.drawable.shorts);
        }

        if (E_temp <0){
            Shoes.setImageResource(R.drawable.boots);
        }
        else {
            Shoes.setImageResource(R.drawable.sneakers);
        }


    }
}