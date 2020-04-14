package com.example.intern.askservices;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityDemandBinding;
import com.example.intern.mainapp.MainApp;

public class DemandActivity extends AppCompatActivity {
    ActivityDemandBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.demandButtonBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.demandButtonHome.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
    }
}
