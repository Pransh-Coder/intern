package com.example.intern.ReduceExpenses;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class ReduceExpenses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduce_expenses);
        findViewById(R.id.back).setOnClickListener(v->{
            onBackPressed();
        });
        findViewById(R.id.home).setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        EditText et = findViewById(R.id.data3);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data6);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data9);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data12);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data15);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data18);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data20);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data23);
        et.setHint(Html.fromHtml("&#8377;"));
        et = findViewById(R.id.data26);
        et.setHint(Html.fromHtml("&#8377;"));
    }
}
