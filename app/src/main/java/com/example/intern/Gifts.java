package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Gifts extends AppCompatActivity {

    ImageView imageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_12);
        imageView6 = findViewById(R.id.imageView6);
//        getSupportActionBar().hide();

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent services = new Intent(getApplicationContext(), GiftDetailsActivity.class);
                startActivity(services);
            }
        });

    }
}
