package com.example.intern.TotalDiscountReceived;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityTotalDiscountReceivedBinding;
import com.example.intern.mainapp.MainApp;

public class TotalDiscountReceived extends AppCompatActivity {
    ActivityTotalDiscountReceivedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalDiscountReceivedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
        binding.home.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
    }
}
