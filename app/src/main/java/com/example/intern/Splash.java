package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.auth.AuthActivity;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
	private static String TAG = Splash.class.getSimpleName();
	private FirebaseUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		user = FirebaseAuth.getInstance().getCurrentUser();
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onStart: Splash screen shown");
		super.onResume();
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		if(user != null){
			segueIntoApp();
		}
		if(prefUtil.getLoginStatus()){
			segueIntoApp();
		}else{
			Intent intent = new Intent(this, AuthActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private void segueIntoApp(){
		Log.d(TAG, "segueIntoApp: authentication verified");
		try {
			Thread.sleep(500);
			Toast.makeText(this, "Welcome back to PS", Toast.LENGTH_LONG).show();
			String UID = user.getUid();
			Intent intent = new Intent(this, AuthVerifyService.class);
			intent.putExtra(AuthVerifyService.USER_UID_INTENT_KEY, UID);
			this.startService(intent);
			//TODO:Redirect to main app
			Intent intent1 = new Intent(this, MainApp.class);
			startActivity(intent1);
			finishAffinity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
