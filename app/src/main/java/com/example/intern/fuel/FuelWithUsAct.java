package com.example.intern.fuel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityFuelWithUsBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class FuelWithUsAct extends AppCompatActivity {
	ActivityFuelWithUsBinding binding;
	boolean hasChosenImage;
	private String filePath = null;
	private String UID;
	Double petrolPrice = null;
	Double discount = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityFuelWithUsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		checkPerms();
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null);
		if(UID == null){
			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
			if(user==null){
				Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				UID = user.getUid();
			}
		}
		final Context context = this;
		binding.etAmt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()!=0){
					double amt = Double.parseDouble(s.toString());
					//TODO : get petrol price from firebase
					if(petrolPrice != null){
						double qty = (double) Math.round(((amt / petrolPrice)*100.0)/100.0);
						discount = qty;
						binding.tvPetrolQty.setText("Petrol quantity is "+ qty +" L" +  " (approx)");
						binding.tvDiscountReceived.setText("Discount (approx) : " + qty + "INR");
					}else {
						binding.tvPetrolQty.setText("Petrol quantity is 0 L");
						binding.tvDiscountReceived.setText("Discount (approx) : 0 INR");
					}
				}
			}
		});
		binding.etPetrolPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					petrolPrice = Double.parseDouble(s.toString());
					double amt = Double.parseDouble(binding.etAmt.getText().toString());
					double qty = (double) Math.round(((amt / petrolPrice)*100.0)/100.0);
					discount = qty;
					binding.tvPetrolQty.setText("Petrol quantity is "+ qty +" L" +  " (approx)");
					binding.tvDiscountReceived.setText("Discount (approx) : " + qty + "INR");
				}else {
					binding.tvPetrolQty.setText("Petrol quantity is 0 L");
					binding.tvDiscountReceived.setText("Discount (approx) : 0 INR");
				}
			}
		});
		binding.ivBackButton.setOnClickListener(v->{
			onBackPressed();
			finish();
		});
		binding.ivHomeButton.setOnClickListener(v->{
			Intent intent = new Intent(this, MainApp.class);
			startActivity(intent);
			finish();
		});
		binding.ivNotifButton.setOnClickListener(v -> {
			Intent intent = new Intent(this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		binding.ivInvoice.setOnClickListener(v -> ImagePicker.Companion.with(this).maxResultSize(1080, 1080).crop().start());
		binding.submitPetrol.setOnClickListener(v -> {
			String amount = binding.etAmt.getText().toString();
			String invoiceNo = binding.etInvoice.getText().toString();
			if(amount.isEmpty() || amount.length()<=1){
				Toast.makeText(this,"Please enter the amount ",Toast.LENGTH_SHORT).show();
			}
			else if(invoiceNo.isEmpty() || invoiceNo.length()<=3){
				Toast.makeText(this,"Please enter invoice number",Toast.LENGTH_SHORT).show();
			}
			else{
				if(!hasChosenImage){
					Toast.makeText(this, "Please click a picture of the invoice", Toast.LENGTH_SHORT).show();
					ImagePicker.Companion.with(this).maxResultSize(1080, 1080).crop().start();
				}else{
					//TODO : Send the image to firebase and store for manual evaluation
					if(filePath == null || filePath.isEmpty()){
						Toast.makeText(this, "Couldn't load Image, please try again", Toast.LENGTH_SHORT).show();
					}else{
						ProgressDialog dialog = new ProgressDialog(this);
						dialog.setTitle("Please Wait");
						dialog.setIcon(R.drawable.pslogotrimmed);
						dialog.show();
						FireStoreUtil.uploadFuelInvoice(this,UID,invoiceNo, BitmapFactory.decodeFile(filePath)).addOnSuccessListener(taskSnapshot ->
								taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri ->
										FireStoreUtil.uploadFuelRefundRequest(UID, invoiceNo, discount, uri.toString()).addOnSuccessListener(documentReference -> {
											dialog.dismiss();
											new AlertDialog.Builder(context).setTitle("Request Received")
													.setMessage("We have received the invoice number and picture of the invoice \nDiscount : " + discount + " INR will be credited once the purchase is verified")
													.setIcon(R.drawable.pslogotrimmed)
													.setPositiveButton("OK", (dialog1, which) -> finish())
													.setOnDismissListener(dialog12 -> finish()).show();
						})));
					}
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			filePath = ImagePicker.Companion.getFilePath(data);
			Glide.with(this).load(filePath).fallback(R.drawable.ic_image_idol).into(binding.ivInvoice);
			hasChosenImage = true;
		}else if(resultCode == ImagePicker.RESULT_ERROR){
			Toast.makeText(this, "Invoice image is required for verification", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void checkPerms(){
		Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
			@Override
			public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
				if(!multiplePermissionsReport.areAllPermissionsGranted())checkPerms();
			}
			
			@Override
			public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
			
			}
		}).check();
	}
}
