package com.app.cloudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


public class Step1Activity extends AppCompatActivity {

    boolean gender = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);


        View buttonOkay = findViewById(R.id.step1_activity_okay);

        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                clickButtonOkay();
            }
        });



        View buttonMan = findViewById(R.id.step1_activity_male);
        View buttonWoman = findViewById(R.id.step1_activity_female);

        buttonMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonMan();
            }
        });

        buttonWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonWoman();
            }
        });

    }

    private void clickButtonWoman() {
        if (gender == true){
            findViewById(R.id.step1_activity_male).setBackgroundColor(Color.parseColor("#FFFFFF"));
            findViewById(R.id.step1_activity_female).setBackgroundColor(Color.parseColor("#4168FF"));
            TextView man = (TextView)findViewById(R.id.step1_activity_man);
            man.setTextColor(Color.parseColor("#999999"));
            TextView woman = (TextView)findViewById(R.id.step1_activity_woman);
            man.setTextColor(Color.parseColor("#FFFFFF"));
            gender = false;
        }
    }

    private void clickButtonMan() {
        if (gender == false){
            findViewById(R.id.step1_activity_female).setBackgroundColor(Color.parseColor("#FFFFFF"));
            findViewById(R.id.step1_activity_male).setBackgroundColor(Color.parseColor("#4168FF"));
            TextView man = (TextView)findViewById(R.id.step1_activity_man);
            man.setTextColor(Color.parseColor("#FFFFFF"));
            TextView woman = (TextView)findViewById(R.id.step1_activity_woman);
            man.setTextColor(Color.parseColor("#999999"));
            gender = true;
        }
    }

    private void clickButtonOkay() {
        Intent intent = new Intent(this, Step2Activity.class);
        startActivity(intent);
    }


}