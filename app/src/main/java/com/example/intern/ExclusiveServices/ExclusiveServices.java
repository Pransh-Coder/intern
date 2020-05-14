package com.example.intern.ExclusiveServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.AppStaticData;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.askservices.EssentialServices;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityExclusiveServiceBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ExclusiveServices extends AppCompatActivity {

    ActivityExclusiveServiceBinding binding;
    private SharedPrefUtil prefUtil;
    public static String FROM_EXCLUSIVE_SERVICES = "exclusiveredirect";
    public static String DEMAND_GROCERY = "grocery";
    public static String DEMAND_VEGETABLES = "veggies";
    public static String DEMAND_DAIRY = "dairy";
	public static String DEMAND_WATER = "water";
	private  boolean isSearchBarOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    binding = ActivityExclusiveServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefUtil = new SharedPrefUtil(this);
        
        binding.bannerexclusive.setOnClickListener(v -> {
	        String url="https://www.prarambhstore.com/PSbyPrarambh";
	        Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(i);
        });

        binding.back.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.homeIMG.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.homeModification.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, HomeModification.class);
            startActivity(intent);
        });
        binding.tiffin.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, TiffinService.class);
            startActivity(intent);
        });
        binding.notifi.setOnClickListener(v -> {
	        Intent intent = new Intent(this, NewsAndUpdatesACT.class);
	        startActivity(intent);
        });
		binding.doctor.setOnClickListener(v -> {
			Intent intent = new Intent(this, DoctorOnline.class);
			startActivity(intent);
		});
		binding.grocery.setOnClickListener(v -> {
			Intent intent = new Intent(this, VendorChosingAct.class);
			intent.putExtra(FROM_EXCLUSIVE_SERVICES, DEMAND_GROCERY);
			startActivity(intent);
		});
		binding.vegetables.setOnClickListener(v -> {
			Intent intent = new Intent(this, EssentialServices.class);
			intent.putExtra(FROM_EXCLUSIVE_SERVICES, DEMAND_VEGETABLES);
			startActivity(intent);
		});
		binding.diary.setOnClickListener(v -> {
			Intent intent = new Intent(this, EssentialServices.class);
			intent.putExtra(FROM_EXCLUSIVE_SERVICES, DEMAND_DAIRY);
			startActivity(intent);
		});
		binding.water.setOnClickListener(v -> {
			Intent intent = new Intent(this, EssentialServices.class);
			intent.putExtra(FROM_EXCLUSIVE_SERVICES, DEMAND_WATER);
			startActivity(intent);
		});
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		setUpSearchBar();
		binding.auto.setOnClickListener(v -> sendFirebaseRequest(FireStoreUtil.AUTO_SERVICES_SERVICES));
		binding.emergencyCare.setOnClickListener(v -> sendFirebaseRequest(FireStoreUtil.EMERGENCY_CARE_SERVICES));
		binding.legalFinancial.setOnClickListener(v -> sendFirebaseRequest(FireStoreUtil.LEGAL_FINANCIAL_SERVICES));
		binding.eduClasses.setOnClickListener(v -> sendFirebaseRequest(FireStoreUtil.EDUCATION_CLASSES_SERVICES));
	}
	
	private void setUpSearchBar() {
    	binding.searchIcon.setOnClickListener(v -> toggleSearchBar(true));
		binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
			String searchWord = v.getText().toString();
			if(!searchWord.isEmpty()){
				Intent foundIntent = AppStaticData.searchExclusiveServices(ExclusiveServices.this, searchWord);
				if(foundIntent!=null){
					startActivity(foundIntent);
				}
			}
			binding.searchBar.setText("");
			binding.searchBar.clearFocus();
			return false;
		});
	}
	
	private void toggleSearchBar(boolean b) {
		if(b){
			binding.searchIcon.setVisibility(View.GONE);
			binding.exclServIconTop.setVisibility(View.GONE);
			binding.txtExcusiveService.setVisibility(View.GONE);
			binding.searchBar.setVisibility(View.VISIBLE);
			binding.searchBar.requestFocus();
			InputMethodManager inputMethodManager =
					(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(inputMethodManager != null){
				View focus = getCurrentFocus();
				if(focus != null) inputMethodManager.toggleSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken()
						,InputMethodManager.SHOW_FORCED, 0);
			}
			isSearchBarOpen = true;
		}else{
			binding.searchBar.setText("");
			binding.searchBar.clearFocus();
			binding.searchIcon.setVisibility(View.VISIBLE);
			binding.exclServIconTop.setVisibility(View.VISIBLE);
			binding.txtExcusiveService.setVisibility(View.VISIBLE);
			binding.searchBar.setVisibility(View.GONE);
			isSearchBarOpen = false;
		}
	}
	
	private void sendFirebaseRequest(String type){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Please wait");
		dialog.setIcon(R.drawable.pslogotrimmed);
		dialog.show();
		Map<String, Object> data = new HashMap<>();
		data.put("uid", FirebaseAuth.getInstance().getUid());
		FirebaseFirestore.getInstance().collection(FireStoreUtil.EXCLUSIVE_SERVICES_COLLECTION_NAME)
				.document(type)
				.collection(type).add(data)
				.addOnSuccessListener(documentReference -> {
					dialog.dismiss();
					new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
							.setMessage("Thank you for showing interest. We will get back to you shortly").setPositiveButton("OK", null).show();
				});
	}
	
	@Override
	public void onBackPressed() {
		if(isSearchBarOpen)toggleSearchBar(false);
		else super.onBackPressed();
	}
}
