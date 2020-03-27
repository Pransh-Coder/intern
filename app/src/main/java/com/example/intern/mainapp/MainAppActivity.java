package com.example.intern.mainapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.auth.AuthVerifyService;

public class MainAppActivity extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_app);
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
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
}
