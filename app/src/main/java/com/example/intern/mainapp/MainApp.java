package com.example.intern.mainapp;

import android.app.AlertDialog;
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
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.ExecutorProvider;
import com.example.intern.FeedBackOrComplaintACT;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityHomeBinding;
import com.example.intern.databinding.HomeMenuHeaderBinding;
import com.example.intern.fuel.FuelBPCLACT;
import com.example.intern.payment.BecomeAMember;
import com.example.intern.payment.auth.RazorPayAuthAPI;
import com.example.intern.shopping.ActivityShopping;
import com.example.intern.swabhiman.SwabhimanActivity;
import com.example.intern.tnc.TermsAndConditions;
import com.google.firebase.auth.FirebaseAuth;

import save_money.SaveMoney;

public class MainApp extends AppCompatActivity {
	private BroadcastReceiver broadcastReceiver;
	public static int BECOME_MEMBER_REQ_CODE = 20;
	private ActivityHomeBinding binding;
	private HomeMenuHeaderBinding headerBinding;
	private MainAppViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(MainAppViewModel.class);
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		viewModel.setPrefUtil(prefUtil);
		binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.home_activity_background));
		View headerView = binding.navigationId.getHeaderView(0);
		headerBinding =  HomeMenuHeaderBinding.bind(headerView);
		viewModel.getPrefUtil().updateWithCloud(FireStoreUtil.getFirebaseUser(this).getUid());
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
	}
	
	private void setClickListeners(){
		binding.drawerPinHome.setOnClickListener(v->{
			binding.drawer.openDrawer(GravityCompat.START);
		});
		binding.SaveMoneyLinear.setOnClickListener(v->{
			String userPayID = viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_PAY_ID, null);
			if(userPayID != null){
				razorPayVerification(userPayID);
			}else{
				FireStoreUtil.getUserDocumentReference(this, FireStoreUtil.getFirebaseUser(this).getUid()).addSnapshotListener((snapshot, e) -> {
					String payID = snapshot.getString(FireStoreUtil.USER_PAY_ID);
					if(payID != null){
						razorPayVerification(payID);
					}else{
						Intent intent = new Intent(MainApp.this, BecomeAMember.class);
						startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
					}
				});
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
			Intent intent = new Intent(this, FuelBPCLACT.class);
			startActivity(intent);
		});
		binding.requestService.setOnClickListener(v->{
		
		});
		binding.exclusiveServices.setOnClickListener(v->{
		
		});
	}
	
	private void setDrawerClickListeners(){
		ExecutorProvider.getExecutorService().submit(() -> {
			int percentage = viewModel.getPrefUtil().getProfileCompletionPercent();
			headerBinding.tvProfileUsername.setText(viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, "PS User"));
			headerBinding.progressMenuProfileCompletion.setProgress(percentage);
			headerBinding.eighty.setText(percentage + "% Profile Completed");
			headerBinding.notify();
		});
		headerBinding.ivProfilePic.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, EditProfile.class);
			startActivity(intent);
		});
		headerBinding.ivLogOut.setOnClickListener(v->{
			new AlertDialog.Builder(this).setTitle("Log Out ?")
					.setPositiveButton("Yes", (button, which)->{
						if(which == AlertDialog.BUTTON_POSITIVE){
							FirebaseAuth.getInstance().signOut();
							SharedPrefUtil prefUtil = new SharedPrefUtil(this);
							prefUtil.getPreferences().edit().clear().apply();
							finish();
						}
					}).setNegativeButton("No", null).show();
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
			Intent intent = new Intent(MainApp.this, SaveMoney.class);
			startActivity(intent);
		}else{
			Toast.makeText(this,"Payment Not Complete!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void razorPayVerification(String payID){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Verifying Payment");dialog.show();
		boolean verified = viewModel.getPrefUtil().getPreferences().getBoolean(SharedPrefUtil.USER_PAY_VER_STATUS, false);
		if(verified){
			Intent intent = new Intent(MainApp.this, SaveMoney.class);
			dialog.hide();
			startActivity(intent);
		}else {
			RazorPayAuthAPI.isPaymentVerified(payID, verificationStatus -> {
				if (verificationStatus) {
					SharedPreferences.Editor editor = viewModel.getPrefUtil().getPreferences().edit();
					editor.putBoolean(SharedPrefUtil.USER_PAY_VER_STATUS, true); editor.apply();
					Intent intent = new Intent(MainApp.this, SaveMoney.class);
					dialog.hide();
					startActivity(intent);
				} else {
					dialog.hide();
					Toast.makeText(this, "Payment Cannot Be Confirmed", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainApp.this, BecomeAMember.class);
					startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
				}
			});
		}
	}
}