package com.example.intern.auth.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.FragmentRegisterAsChildBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class RegisterAsChildFR extends Fragment {
	private static String TAG = RegisterAsChildFR.class.getSimpleName();
	private final Calendar calendar = Calendar.getInstance();
	private AuthViewModel viewModel;
	private FusedLocationProviderClient locationProviderClient;
	private int LOCATION_REQUEST_CODE = 23;
	private String pinCode;
	private FirebaseUser user;
	private FragmentRegisterAsChildBinding binding;
	private boolean hasSelectedDate;
	private String dateTimeStamp = null;
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};
	
	@SuppressLint("SetTextI18n")
	private void updateLabel(){
		hasSelectedDate = true;
		 dateTimeStamp = Long.toString(calendar.getTimeInMillis());
		 binding.etDOB.setText(calendar.get(Calendar.DAY_OF_MONTH) + " / " + calendar.get(Calendar.MONTH) + " / "  + calendar.get(Calendar.YEAR));
	}
	
	public RegisterAsChildFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		binding = FragmentRegisterAsChildBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
		user = viewModel.getFirebaseUser();
		if(user == null){
			Toast.makeText(requireContext(), "Cannot find account", Toast.LENGTH_LONG).show();
			viewModel.getNavController().navigate(R.id.action_registerAsChildFR_to_registrationChoiceFR);
			onDetach();
		}
		setTextWatchers();
		setClickListeners();
	}
	private void setTextWatchers(){
		binding.etPinCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s != null && s.length() == 6) {
					pinCode = s.toString();
				}
			}
		});
		binding.calenderIcon.setOnClickListener(v->{
			DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, 2000, 1,1);
			datePickerDialog.show();
		});
	}
	
	private void setClickListeners(){
		binding.btnRegisterasChildSignIn.setOnClickListener(v->{
			String name = binding.etName.getText().toString();
			if(name.isEmpty()){
				binding.etName.setError("Name Cannot Be Empty");return;
			}
			if(dateTimeStamp==null && !hasSelectedDate){
				binding.etDOB.setError("DOB Cannot Be Empty");return;
			}
			if(pinCode != null && pinCode.length() == 6){
				String nick_name = binding.etNickName.getText().toString();
				String ps_nick_name = binding.etPsNickName.getText().toString();
				String parent_number =binding.etParNumber.getText().toString();
				String child_number = binding.etChildNumber.getText().toString();
				FirebaseFirestore.getInstance().collection(FireStoreUtil.STATIC_DATA_COLLECTION_NAME).document("static").get().addOnSuccessListener(snapshot -> {
					if(snapshot != null && snapshot.exists()){
						try{
							Double membershipFee = snapshot.getDouble("memfee");
							FireStoreUtil.makeUserWithUID(requireContext(), user.getUid()
									,name, user.getEmail(), nick_name,ps_nick_name, parent_number,	dateTimeStamp, pinCode,parent_number,"1", membershipFee)
									.addOnSuccessListener(success->{
										FireStoreUtil.addToCluster(requireContext(), pinCode, user.getUid());
										Log.d(TAG, "successfully made user");
										//TODO : Store user info in shared preferences
										if(user.getPhoneNumber() != null){
											FireStoreUtil.addToPhoneNumberList(requireContext() , user.getPhoneNumber(), user.getUid());
										}
										viewModel.getPrefUtil().updateSharedPreferencesPostRegister(user.getUid(), name, user.getEmail(), nick_name, ps_nick_name,
												dateTimeStamp, pinCode, child_number, parent_number);
										new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed).setTitle("Congratulations")
												.setMessage("Your 30 day free trial for Rs. 1/- discount on fuel is activated !")
												.setPositiveButton("Great", null).show();
										Intent intent = new Intent(requireContext(), MainApp.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(intent);
									});
						}catch (Exception ignored){}
					}
				});
			}else {
				requirePinCode();
			}
		});
	}
	private void getPostalCodeFromGPS(){
		locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
		locationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
			Geocoder geocoder = new Geocoder(requireContext());
			try {
				List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
				if(addresses != null && addresses.size() > 0 ){
					pinCode = addresses.get(0).getPostalCode();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void getPostalCodeFromUser(){
		binding.etPinCode.setError("Provide location permissions or enter manually");
	}
	
	private void requirePinCode(){
		String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
		if(ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(requireActivity(), new String[] {permission}, LOCATION_REQUEST_CODE);
		}else{
			getPostalCodeFromGPS();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == LOCATION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				requirePinCode();
			}else{
				getPostalCodeFromUser();
			}
		}
	}
}
