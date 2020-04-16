package com.example.intern.ExclusiveServices;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExclusiveServices extends AppCompatActivity {
    ImageView back,home;
    CircleImageView home_mod,tifin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_services);

        back = findViewById(R.id.back);
        home = findViewById(R.id.homeIMG);
        home_mod=findViewById(R.id.home_modImg);
        tifin=findViewById(R.id.tiffinImg);

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
        home_mod.setOnClickListener( view -> {
            Intent intent = new Intent(ExclusiveServices.this, homeModificationServices.class);
            startActivity(intent);
            finish();
        });
        tifin.setOnClickListener( view -> {
            Intent intent = new Intent(ExclusiveServices.this, tiffinServices.class);
            startActivity(intent);
            finish();
        });
    }
}
