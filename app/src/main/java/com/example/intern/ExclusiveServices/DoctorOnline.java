package com.example.intern.ExclusiveServices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class DoctorOnline extends AppCompatActivity {

    ImageView doctor_button_back,doctor_home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_online);

        doctor_button_back= findViewById(R.id.doctor_button_back);
        doctor_home_button = findViewById(R.id.doctor_button_home);

        doctor_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorOnline.this,ExclusiveServices.class);
                startActivity(intent);
            }
        });

        doctor_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorOnline.this, MainApp.class);
                startActivity(intent);
            }
        });
    }
}
