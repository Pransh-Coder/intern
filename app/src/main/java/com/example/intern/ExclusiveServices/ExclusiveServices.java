package com.example.intern.ExclusiveServices;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class ExclusiveServices extends AppCompatActivity {
    ImageView back,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_service);

        back = findViewById(R.id.back);
        home = findViewById(R.id.homeIMG);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        home.setOnClickListener( view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
    }
}
