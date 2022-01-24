package com.app.cloudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Step2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        View buttonOkay = findViewById(R.id.step2_activity_allow);

        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                clickButtonAllow();
            }
        });
    }

    private void clickButtonAllow() {
        Intent intent = new Intent(this, Step3Activity.class);
        startActivity(intent);
    }
}