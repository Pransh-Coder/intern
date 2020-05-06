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
            String url = "https://wa.me/919023728047?text=Hi,%20I%20want%20to%20order%20Some%20Medicine";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        binding.chatButton.setOnClickListener(v -> {
	        String url = "https://wa.me/919909906065?text=Hi%20PS,%20I%20have%20some%20queries";
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
