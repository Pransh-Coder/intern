package com.example.intern.mainapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.databinding.ActivityHomeBinding;
import com.example.intern.databinding.ActivityMainAppBinding;

import save_money.SaveMoney;

public class MainApp extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	private ActivityMainAppBinding binding;
	LinearLayout mSaveMoney;
	private MainAppViewModel viewModel;
	private ActivityHomeBinding childBinding;
	
	private void initializeViews(){
		mSaveMoney = findViewById(R.id.savemoney);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(MainAppViewModel.class);
		binding = ActivityMainAppBinding.inflate(getLayoutInflater());
		childBinding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initializeViews();
//		Toolbar toolbar = findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		viewModel.drawerLayout = findViewById(R.id.drawer_layout);
		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.home_activity_background));
//		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main_app);
//		viewModel.setNavController(navController);
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
		mSaveMoney.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, SaveMoney.class);
			startActivity(intent);
		});
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
