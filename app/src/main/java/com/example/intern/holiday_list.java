package com.example.intern;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class holiday_list extends AppCompatActivity {
    private static String[] mname = {"<b>The Spa</b><br>Area : Navrangpura<br><b>Offer : Rs.350 OFF + 15% On Second Onwards",
            "<b>Sletter Beauty Parlour</b><br>Area : Navrangpura<br><b>Offer : Rs.200 OFF + 20% On Six Sitting",
            "<b>Sharma Beauty Parlour</b><br>Area : Panchavati<br><b>Offer : Rs.299 OFF + 10% On Second Onwards"};
    RecyclerView myrecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        MyAdapter_lifestyle recycleadapter = new MyAdapter_lifestyle(this, mname);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
    }
}
