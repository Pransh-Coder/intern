package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Services_list extends AppCompatActivity {
    RecyclerView myrecycleview;
    private static String[] mname= {"<b>Phonewala</b><br>Area : Ashram Road<br><b>Offer : Handsfree Free + 10% On Second Onwards",
            "<b>R.O. Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Washing Machine Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Refrigerator Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Microwave Services</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>A.C. Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        MyAdapter_services recycleadapter = new MyAdapter_services(this,mname);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
    }
}
