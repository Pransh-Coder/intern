package com.example.intern.ExclusiveServices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.intern.HomeActivity;
import com.example.intern.R;

public class homeModificationServices extends AppCompatActivity {
    ImageView back,home_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_modification);

        back = findViewById(R.id.back);
        home_btn = findViewById(R.id.home_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeModificationServices.this,ExclusiveServices.class);
                startActivity(intent);
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeModificationServices.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}