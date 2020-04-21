package com.example.intern.shopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.databinding.ActivityShoppingDescHBinding;
import com.example.intern.mainapp.MainApp;

public class ActivityShopping extends AppCompatActivity {
    private ActivityShoppingDescHBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingDescHBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.submit.setOnClickListener(v->{
            String url = "http://www.prarambhstore.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        binding.ivBackButton.setOnClickListener(v->{
            onBackPressed();
        });
        binding.ivHomeButton.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.medicineSite.setOnClickListener(v -> {
            String url = "https://www.prarambhstore.com/beta/order_medicines";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        binding.ivNotifButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
}
