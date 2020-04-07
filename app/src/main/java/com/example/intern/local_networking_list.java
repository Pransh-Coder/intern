package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class local_networking_list extends AppCompatActivity {
    RecyclerView myrecycleview;
    private static String[] mname= {"<b>Name</b><br>Age<br>Occupation","<b>Name</b><br>Age<br>Occupation","<b>Name</b><br>Age<br>Occupation","<b>Name</b><br>Age<br>Occupation"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_networking_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        MyAdapter_lifestyle recycleadapter = new MyAdapter_lifestyle(this,mname);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
    }
}
