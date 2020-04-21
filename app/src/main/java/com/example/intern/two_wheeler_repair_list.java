package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intern.mainapp.MainApp;
import com.example.intern.socialnetwork.Listactivity;

import save_money.SaveMoney;

public class two_wheeler_repair_list extends AppCompatActivity {

    RecyclerView myrecycleview;
    ImageView back,home;
    String[] mname = {"<b>Go serve Auto India LLP</b><br>Area : Ghatlodiya, Naranpura<br><b>Offer : One service free worth Rs.225*"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_wheeler_repair_list);
       // getSupportActionBar().hide();
        myrecycleview = findViewById(R.id.myrecycle);
        back=findViewById(R.id.iv_back_button);
        home=findViewById(R.id.iv_home_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(two_wheeler_repair_list.this, ServicesActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(two_wheeler_repair_list.this, MainApp.class);
                startActivity(intent);
            }
        });

        MyAdapter_services_bike recycleadapter = new MyAdapter_services_bike(this, mname);
        myrecycleview.setAdapter(recycleadapter);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onItemClicked(View v){
        DialogFrag df = new DialogFrag();
        df.show(getFragmentManager(),"Dialog");
    }
}
