package com.example.intern;

import android.os.Bundle;

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

    }
}
