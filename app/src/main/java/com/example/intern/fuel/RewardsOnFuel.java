package com.example.intern.fuel;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.databinding.ActivityRewardsOnFuelBinding;
import com.example.intern.mainapp.MainApp;

public class RewardsOnFuel extends AppCompatActivity {
	
	ActivityRewardsOnFuelBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityRewardsOnFuelBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		//Set click listeners
		binding.ivBackButton.setOnClickListener(v -> onBackPressed());
		binding.ivHomeButton.setOnClickListener(v -> {
			Intent intent = new Intent(this, MainApp.class);
			startActivity(intent);
		});
		binding.ivNotifButton.setOnClickListener(v -> {
			Intent intent = new Intent(this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
	}
}
