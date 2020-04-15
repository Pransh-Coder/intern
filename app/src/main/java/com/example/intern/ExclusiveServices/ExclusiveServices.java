package com.example.intern.ExclusiveServices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.intern.HomeActivity;
import com.example.intern.R;

public class ExclusiveServices extends AppCompatActivity {
    ImageView back,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_services);

        back = findViewById(R.id.back);
        home = findViewById(R.id.homeIMG);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExclusiveServices.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExclusiveServices.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
