package com.example.intern;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityNewsAndUpdatesBinding;
import com.example.intern.mainapp.MainApp;

public class NewsAndUpdatesACT extends AppCompatActivity {
    ActivityNewsAndUpdatesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsAndUpdatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBackButton.setOnClickListener(v->{
            onBackPressed();
        });
        binding.ivHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
    }
}
