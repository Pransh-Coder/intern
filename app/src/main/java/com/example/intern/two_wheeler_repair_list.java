package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class two_wheeler_repair_list extends AppCompatActivity {

    RecyclerView myrecycleview;
    private MyAdapter_services.OnItemClickListener listener;
    String[] mname = {"<b>Go serve Auto India LLP</b><br>Area : Ghatlodiya, Naranpura<br><b>Offer : One service free worth Rs.225*"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_wheeler_repair_list);
        getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        listener = this;
        MyAdapter_services_bike recycleadapter = new MyAdapter_services_bike(this, mname,listener);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onItemClicked(View v){
        DialogFrag df = new DialogFrag();
        df.show(getFragmentManager(),"Dialog");
    }
}
