package com.example.intern.mainapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.databinding.ActivityMainAppBinding;

public class MainApp extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	private ActivityMainAppBinding binding;
	private MainAppViewModel viewModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(MainAppViewModel.class);
		binding = ActivityMainAppBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
//		Toolbar toolbar = findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		viewModel.drawerLayout = findViewById(R.id.drawer_layout);
		setClickListeners();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean b = intent.getBooleanExtra(AuthVerifyService.KILL_APP_INTENT_KEY, false);
				if(b)finish();
			}
		};
		IntentFilter filter = new IntentFilter();
		registerReceiver(broadcastReceiver, filter);
	}
	
	private void setClickListeners(){
	
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
}
