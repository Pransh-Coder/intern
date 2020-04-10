package com.example.intern.mainapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.example.intern.EditProfile.EditProfile;
import com.example.intern.FeedBackOrComplaintACT;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
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

import java.io.File;

import save_money.SaveMoney;

public class MainApp extends AppCompatActivity {
	public static int BECOME_MEMBER_REQ_CODE = 20;
	private ActivityHomeBinding binding;
	private HomeMenuHeaderBinding headerBinding;
	private SharedPrefUtil prefUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityHomeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		prefUtil = new SharedPrefUtil(this);
		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.home_activity_background));
		View headerView = binding.navigationId.getHeaderView(0);
		headerBinding =  HomeMenuHeaderBinding.bind(headerView);
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		populateDrawer();
		setClickListeners();
		setDrawerClickListeners();
	}
	
	@SuppressLint("SetTextI18n")
	private void populateDrawer(){
		int percentage = prefUtil.getProfileCompletionPercent();
		String ppFilePath = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, null);
		if (ppFilePath != null && !ppFilePath.isEmpty()) {
			Glide.with(this).load(ppFilePath)
					.fallback(R.drawable.edit_profile).into(headerBinding.ivProfilePic);
		} else {
			String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
			if (UID != null && !UID.isEmpty()) {
				File imageFile = FireStoreUtil.getProfilePicInLocal(this, UID);
				Glide.with(this).load(imageFile)
						.fallback(R.drawable.edit_profile).into(headerBinding.ivProfilePic);
			}
		}
		headerBinding.tvProfileUsername.setText(prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, "PS User"));
		headerBinding.progressMenuProfileCompletion.setProgress(percentage);
		headerBinding.eighty.setText(percentage + "% Profile Completed");
		binding.navigationId.getHeaderView(0).refreshDrawableState();
	}
	
	private void setClickListeners(){
		binding.drawerPinHome.setOnClickListener(v-> binding.drawer.openDrawer(GravityCompat.START));
		binding.SaveMoneyLinear.setOnClickListener(v->{
			String userPayID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PAY_ID, null);
			if(userPayID != null){
				razorPayVerification(userPayID);
			}else{
				FireStoreUtil.getUserDocumentReference(this, FireStoreUtil.getFirebaseUser(this).getUid()).addSnapshotListener((snapshot, e) -> {
					String payID = null;
					if(snapshot!= null){
						payID = snapshot.getString(FireStoreUtil.USER_PAY_ID);
					}
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
		headerBinding.ivProfilePic.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, EditProfile.class);
			startActivity(intent);
		});
		headerBinding.ivLogOut.setOnClickListener(v-> new AlertDialog.Builder(this).setTitle("Log Out ?")
				.setPositiveButton("Yes", (button, which)->{
					if(which == AlertDialog.BUTTON_POSITIVE){
						prefUtil.getPreferences().edit().clear().apply();
						FirebaseAuth.getInstance().signOut();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						finishAndRemoveTask();
					}
				}).setNegativeButton("No", null).show());
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
					dialog.hide();
					Toast.makeText(this, "Payment Cannot Be Confirmed", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainApp.this, BecomeAMember.class);
					startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
				}
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		finishAffinity();
		super.onBackPressed();
	}
}