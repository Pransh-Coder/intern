package com.example.intern.fuel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityRewardsOnFuelBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RewardsOnFuel extends AppCompatActivity {
	
	ActivityRewardsOnFuelBinding binding;
	SharedPrefUtil prefUtil;
	double wallet = 0d;
	double membershipFee = 0d;
	
	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityRewardsOnFuelBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		prefUtil = new SharedPrefUtil(this);
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage("Loading Info");
		dialog.setIcon(R.drawable.pslogotrimmed);
		dialog.show();
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
		String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
		if(UID == null || TextUtils.isEmpty(UID)){
			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
			if(user != null)UID = user.getUid();
		}
		FireStoreUtil.getUserDocumentReference(this, UID).get().addOnSuccessListener(snapshot -> {
			double pending = 0d;
			try{
				membershipFee = snapshot.getDouble("memfee");
				wallet = snapshot.getDouble("wallet");
				pending = snapshot.getDouble("fuelpend");
			}catch (Exception ignored){}
			binding.tvAmtApprovedInr.setText(" ₹ " + wallet);
			binding.tvAmountReqInr.setText(" ₹ " + pending);
			dialog.dismiss();
		});
		//TODO : Set click listener to reduce amount from memfee field
		String finalUID = UID;
		final Context context = this;
		binding.redeemMembershipFee.setOnClickListener(v -> {
			if(membershipFee>=0 && wallet >=0){
				if(membershipFee==0){
					Toast.makeText(context, "Membership fee is already 0 INR !", Toast.LENGTH_LONG).show();
					return;
				}
				if(wallet==0){
					Toast.makeText(context, "Wallet Amount is 0 INR !", Toast.LENGTH_LONG).show();
					return;
				}
				synchronized (RewardsOnFuel.class){
					Map<String,Object> update = new HashMap<>();
					double newWalletAmt = wallet;
					double newMemFee = membershipFee;
					if(wallet >= membershipFee){
						//Wallet has more money than being a member
						newWalletAmt = wallet - membershipFee;
						newMemFee = 0d;
					}else{
						newWalletAmt = 0d;
						newMemFee = membershipFee - wallet;
					}
					membershipFee = newMemFee;
					wallet = newWalletAmt;
					update.put("wallet", newWalletAmt);
					update.put("memfee", newMemFee);
					double finalNewMemFee = newMemFee;
					FireStoreUtil.getUserDocumentReference(this, finalUID).update(update).addOnSuccessListener(aVoid -> {
						new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setMessage("Congratulation")
								.setMessage("Membership Fee is Now Rs." + finalNewMemFee).setPositiveButton("OK", null)
								.show();
					});
					binding.tvAmtApprovedInr.setText(" ₹ " + newWalletAmt);
				}
			}else{
				Toast.makeText(this, "Cannot connect to the internet", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
