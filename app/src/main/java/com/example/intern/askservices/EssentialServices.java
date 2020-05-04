package com.example.intern.askservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.EditProfile.EditProfile;
import com.example.intern.ExclusiveServices.ExclusiveServices;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.EssentialServiceBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class EssentialServices extends AppCompatActivity {
	private static List<String> productsOptions;
	private static String filePath;
	EssentialServiceBinding binding;
	private SharedPrefUtil prefUtil;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = EssentialServiceBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		productsOptions = Arrays.asList(getResources().getStringArray(R.array.ProductsOptions));
		prefUtil = new SharedPrefUtil(this);
		binding.demandButtonBack.setOnClickListener(v -> onBackPressed());
		binding.demandButtonHome.setOnClickListener(v -> {
			Intent intent = new Intent(EssentialServices.this, MainApp.class);
			startActivity(intent);finish();
		});
		binding.demandNotification.setOnClickListener(v -> {
			Intent intent = new Intent(EssentialServices.this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		//Check if the user has entered phone no or relative phone no
		if(!(prefUtil.getPreferences().getString(SharedPrefUtil.USER_PHONE_NO, null) != null || prefUtil.getPreferences().getString(SharedPrefUtil.USER_RELATIVE_PHONE_NUMBER_KEY, null) != null)){
			new androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Needs Your Contact Details")
					.setMessage("Please update your phone number try again")
					.setCancelable(false)
					.setPositiveButton("OK", (dialog, which) -> {
						if(which== DialogInterface.BUTTON_POSITIVE){
							Intent intent = new Intent(EssentialServices.this, EditProfile.class);
							startActivity(intent);
						}
					}).setNegativeButton("Dismiss", (dialog, which) -> finish())
					.setOnDismissListener(dialog -> finish())
					.setIcon(getResources().getDrawable(R.drawable.pslogotrimmed)).show();
		}
		String primitiveAddressCheck = prefUtil.getPreferences().getString(SharedPrefUtil.USER_AREA_KEY, null);
		if(primitiveAddressCheck == null || primitiveAddressCheck.isEmpty() || primitiveAddressCheck.length() < 4){
			new androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Needs Your Address")
					.setMessage("Please update your address and try again")
					.setCancelable(false)
					.setPositiveButton("OK", (dialog, which) -> {
						if(which== DialogInterface.BUTTON_POSITIVE){
							Intent intent = new Intent(EssentialServices.this, EditProfile.class);
							startActivity(intent);
						}
					}).setNegativeButton("Dismiss", (dialog, which) -> finish())
					.setOnDismissListener(dialog -> finish())
					.setIcon(getResources().getDrawable(R.drawable.pslogotrimmed)).show();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		handleIntent();
		//Set text watcher
		setTextWatcher();
		//Get the image
		binding.ivProductImage.setOnClickListener(v -> ImagePicker.Companion.with(this).crop().start());
		//Submit the product details
		binding.demandSubmit.setOnClickListener(v -> {
			String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null);
			if(UID == null){
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				if(user==null){
					Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					UID = user.getUid();
				}
			}
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setTitle(getString(R.string.please_wait));
			progressDialog.setMessage(getString(R.string.uploading_request_message));
			progressDialog.setIcon(R.drawable.pslogotrimmed);
			String productDescription = binding.etProductName.getText().toString();
			if(TextUtils.isEmpty(productDescription)){
				if(filePath == null || filePath.isEmpty()){
					binding.etProductName.setError(getString(R.string.essential_service_description_error));
				}else{
					progressDialog.show();
					String finalUID = UID;
					FireStoreUtil.uploadImage(this, UID, BitmapFactory.decodeFile(filePath)).addOnSuccessListener(taskSnapshot ->
							taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
								FireStoreUtil.uploadEssentialServiceRequest(finalUID, null, uri.toString()).addOnSuccessListener(documentReference -> {
									progressDialog.dismiss();
									showSuccessDialog();
						});
					}));
				}
			}else{
				//Form the product details
				String formDetails = productsOptions.get(binding.productspinner.getSelectedItemPosition()) + " : " + productDescription;
				if(filePath == null || filePath.isEmpty()){
					progressDialog.show();
					FireStoreUtil.uploadEssentialServiceRequest(UID, formDetails, null).addOnSuccessListener(documentReference -> {
						progressDialog.dismiss();
						showSuccessDialog();
							});
				}else{
					progressDialog.show();
					String finalUID = UID;
					FireStoreUtil.uploadImage(this, UID, BitmapFactory.decodeFile(filePath)).addOnSuccessListener(taskSnapshot ->
							taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri ->
								FireStoreUtil.uploadEssentialServiceRequest(finalUID, formDetails, uri.toString()).addOnSuccessListener(documentReference -> {
									progressDialog.dismiss();
									showSuccessDialog();
								})));
				}
			}
		});
	}
	
	private void showSuccessDialog() {
		new AlertDialog.Builder(this).setMessage(R.string.request_submit_success_alert).setPositiveButton(R.string.ok, (dialog1, which) -> onBackPressed())
				.setTitle(R.string.thank_you).setIcon(R.drawable.pslogotrimmed).show();
	}
	
	private void handleIntent(){
		Intent intent = getIntent();
		if(intent.hasExtra(ExclusiveServices.FROM_EXCLUSIVE_SERVICES)){
			String request = intent.getStringExtra(ExclusiveServices.FROM_EXCLUSIVE_SERVICES);
			if(request !=null){
				if(request.equals(ExclusiveServices.DEMAND_DAIRY)){
					binding.productspinner.setSelection(3);
				}else if(request.equals(ExclusiveServices.DEMAND_GROCERY)){
					binding.productspinner.setSelection(0);
				}else if(request.equals(ExclusiveServices.DEMAND_VEGETABLES)){
					binding.productspinner.setSelection(1);
				}else if(request.equals(ExclusiveServices.DEMAND_WATER)){
					binding.productspinner.setSelection(2);
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Log.d("Demand", "onActivityResult OK");
			filePath = ImagePicker.Companion.getFilePath(data);
			Glide.with(this).load(filePath).fallback(R.drawable.ic_image_idol).into(binding.ivProductImage);
		}else if(resultCode == ImagePicker.RESULT_ERROR){
			Toast.makeText(this, "Could not load image",Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setTextWatcher(){
		binding.tvUseless.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable s) {
				binding.tvUseless.setText(s.length() +"/200");
				if(s.length()>200){
					binding.etProductName.setText(s.subSequence(0,199));
				}
			}
		});
	}
}
