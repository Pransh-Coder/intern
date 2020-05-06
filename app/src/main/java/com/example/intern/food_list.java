package com.example.intern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class food_list extends AppCompatActivity {
    private static String[] mname = {"<b>Rangoli Ice Cream</b><br>Area : Bopal<br><b>Offer : One Scoop Free",
            "<b>Flourish Pure Food Pvt. Ltd.</b><br>Area : Naranpura<br><b>Offer : Save Rs.200 on membership"};
    RecyclerView myrecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);

        MyAdapter_food recycleadapter = new MyAdapter_food(this, mname);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
        ImageView chatButton=findViewById(R.id.chat_button);
        chatButton.setOnClickListener(v -> {
            String url = "https://wa.me/919909906065?text=Hi%20PS,%20I%20have%20some%20queries";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

    }
}
