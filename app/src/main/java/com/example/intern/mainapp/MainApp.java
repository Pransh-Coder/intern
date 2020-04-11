package com.example.intern.mainapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.ActivityShopping;
import com.example.intern.BPCL_Fuel_QR_ScannerActivity;
import com.example.intern.EditProfile.EditProfile;
import com.example.intern.FeedBackOrComplaintACT;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TermsAndConditions;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityHomeBinding;
import com.example.intern.databinding.HomeMenuHeaderBinding;
import com.example.intern.payment.BecomeAMember;
import com.example.intern.payment.auth.RazorPayAuthAPI;
import com.example.intern.swabhiman.SwabhimanActivity;
import com.google.firebase.auth.FirebaseAuth;

import save_money.SaveMoney;

public class MainApp extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	public static int BECOME_MEMBER_REQ_CODE = 20;
	private MainAppViewModel viewModel;
	private ActivityHomeBinding binding;
	private HomeMenuHeaderBinding headerBinding;

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
		View headerView = binding.navigationId.getHeaderView(0);
		headerBinding =  HomeMenuHeaderBinding.bind(headerView);
		setClickListeners();
		setDrawerClickListeners();
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
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		int percentage = prefUtil.getProfileCompletionPercent();
		headerBinding.tvProfileUsername.setText(prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, "PS User"));
		headerBinding.progressMenuProfileCompletion.setProgress(percentage);
		headerBinding.eighty.setText(percentage + "% Profile Completed");
	}
	
	private void setClickListeners(){
		binding.SaveMoneyLinear.setOnClickListener(v->{
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
		binding.swabhimanMainChoice.setOnClickListener(v->{
			Intent intent = new Intent(this, SwabhimanActivity.class);
			startActivity(intent);
		});
		binding.shopping.setOnClickListener(v->{
			Intent intent = new Intent(this, ActivityShopping.class);
			startActivity(intent);
		});
		binding.bpclfuel.setOnClickListener(v->{
			Intent intent = new Intent(this, BPCL_Fuel_QR_ScannerActivity.class);
			startActivity(intent);
		});
		binding.requestService.setOnClickListener(v->{
		
		});
		binding.exclusiveServices.setOnClickListener(v->{
		
		});
	}
	
	private void setDrawerClickListeners(){
		headerBinding.ivProfilePic.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, EditProfile.class);
			startActivity(intent);
		});
		headerBinding.ivLogOut.setOnClickListener(v->{
			FirebaseAuth.getInstance().signOut();
			//TODO : Purge the shared Prefs
			SharedPrefUtil prefUtil = new SharedPrefUtil(this);
			prefUtil.getPreferences().edit().clear().apply();
		});
		binding.navigationId.setNavigationItemSelectedListener(item -> {
			Intent intent = null;
			switch (item.getItemId()){
				case R.id.menu_edit_profile:
					intent = new Intent(this, EditProfile.class);
					break;
				case R.id.menu_medical_records:
					intent = new Intent(this, MedicalRecord.class);
					break;
				case R.id.menu_tnc:
					intent = new Intent(this, TermsAndConditions.class);
					break;
				case R.id.menu_reduce_expense:
					intent = new Intent(this, ReduceExpenses.class);
					break;
				case R.id.menu_total_disc:
					intent = new Intent(this, TotalDiscountReceived.class);
					break;
				case R.id.menu_local_net:
					//TODO : Add intent to local network
					break;
				case R.id.menu_newsnupdates:
					intent = new Intent(this, NewsAndUpdatesACT.class);
					break;
				case R.id.menu_feedback:
					intent = new Intent(this, FeedBackOrComplaintACT.class);
					break;
				case R.id.menu_rate:
					//TODO : Send to the app link in play store
					break;
			}
			if(intent!=null){
				startActivity(intent);
			}
			return true;
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