package com.example.intern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Services_list extends AppCompatActivity {
    private static String[] mname = {"<b>Phonewala</b><br>Area : Ashram Road<br><b>Offer : Handsfree Free + 10% On Second Onwards",
            "<b>R.O. Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Washing Machine Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Refrigerator Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>Microwave Services</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF",
            "<b>A.C. Service</b><br>Area : All Ahmedabad<br><b>Offer : 100Rs. OFF"};
    RecyclerView myrecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        MyAdapter_services recycleadapter = new MyAdapter_services(this, mname);
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
