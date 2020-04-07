package com.example.intern.mainapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityHomeBinding;
import com.example.intern.payment.BecomeAMember;
import com.example.intern.payment.auth.RazorPayAuthAPI;

import save_money.SaveMoney;

public class MainApp extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	public static int BECOME_MEMBER_REQ_CODE = 20;
	private MainAppViewModel viewModel;
	private ActivityHomeBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(MainAppViewModel.class);
		binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
//		Toolbar toolbar = findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
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
		binding.savemoney.setOnClickListener(v->{
			SharedPrefUtil prefUtil = new SharedPrefUtil(this);
			String userPayID = prefUtil.getUserPayId();
			if(userPayID != null){
				razorPayVerification(userPayID);
			}else {
				Intent intent = new Intent(MainApp.this, BecomeAMember.class);
				startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
			}
		});
		//TODO : Setting click listeners for others
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == BECOME_MEMBER_REQ_CODE && resultCode == BecomeAMember.IS_A_MEMBER_RESULT_CODE) {
			if(data!= null){
				String payID = data.getStringExtra(BecomeAMember.PAY_ID_TAG);
				razorPayVerification(payID);
			}
		}else{
			Toast.makeText(this,"Payment Not Complete!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void razorPayVerification(String payID){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Verifying Payment");dialog.show();
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		boolean verified = prefUtil.getPreferences().getBoolean(SharedPrefUtil.USER_PAY_VER_STATUS, false);
		if(verified){
			Intent intent = new Intent(MainApp.this, SaveMoney.class);
			dialog.hide();
			startActivity(intent);
		}else {
			RazorPayAuthAPI.isPaymentVerified(payID, verificationStatus -> {
				if (verificationStatus) {
					SharedPreferences.Editor editor = prefUtil.getPreferences().edit();
					editor.putBoolean(SharedPrefUtil.USER_PAY_VER_STATUS, true); editor.apply();
					Intent intent = new Intent(MainApp.this, SaveMoney.class);
					dialog.hide();
					startActivity(intent);
				} else {
					Toast.makeText(this, "Payment Cannot Be Confirmed", Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}