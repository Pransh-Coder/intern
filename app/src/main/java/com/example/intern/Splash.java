package com.example.intern;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.auth.AuthActivity;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class Splash extends AppCompatActivity {
	private static String TAG = Splash.class.getSimpleName();
	private FirebaseUser user;
	private CircleImageView psIcon;
	Animation topAnim;
	private static int SPLASH_SCREEN = 3000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		user = FireStoreUtil.getFirebaseUser(this);
		psIcon=findViewById(R.id.logo);
		topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
		psIcon.setAnimation(topAnim);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "onStart: Splash screen shown");
				SharedPrefUtil prefUtil = new SharedPrefUtil(getApplicationContext());
				if(prefUtil.getLoginStatus()){
					segueIntoApp();
				}else{
					Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
					startActivity(intent);
					finish();
				}
			}

			private void segueIntoApp(){
				Log.d(TAG, "segueIntoApp: authentication verified");
				try {
					Toast.makeText(getApplicationContext(), "Welcome back to PS", Toast.LENGTH_LONG).show();
					//TODO:Redirect to main app
					Intent intent1 = new Intent(getApplicationContext(), MainApp.class);
					startActivity(intent1);
					finishAffinity();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		},SPLASH_SCREEN);

	}
}
