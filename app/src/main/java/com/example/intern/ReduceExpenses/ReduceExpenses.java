package com.example.intern.ReduceExpenses;

import android.content.Intent;
import android.os.Bundle;

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
    }
}
