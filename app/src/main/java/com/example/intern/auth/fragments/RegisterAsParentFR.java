package com.example.intern.auth.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.intern.databinding.FragmentRegisterAsParentFRBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class RegisterAsParentFR extends Fragment {
    private static String TAG = RegisterAsParentFR.class.getSimpleName();
    private FragmentRegisterAsParentFRBinding binding;
    private AuthViewModel viewModel;
    private FusedLocationProviderClient locationProviderClient;
    private String pinCode;
    private FirebaseUser user;
    private final Calendar calendar = Calendar.getInstance();
    private boolean hasSelectedDate;
    private String dateTimeStamp = null;
    private DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };
    
    @SuppressLint("SetTextI18n")
    private void updateLabel(){
        hasSelectedDate = true;
        dateTimeStamp = Long.toString(calendar.getTimeInMillis());
        binding.etDOB.setText(calendar.get(Calendar.DAY_OF_MONTH) + " / " + calendar.get(Calendar.MONTH) + " / "  + calendar.get(Calendar.YEAR));
    }

    public RegisterAsParentFR() {
        // Required empty public constructor
    }

//some changes made
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding = FragmentRegisterAsParentFRBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
	    viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
	    user = viewModel.getFirebaseUser();
	    locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
        user = viewModel.getFirebaseUser();
        if(user == null){
            Toast.makeText(requireContext(), "Cannot find account", Toast.LENGTH_LONG).show();
            viewModel.getNavController().navigate(R.id.action_registerAsParentFR_to_registrationChoiceFR);
            return;
        }
        //Check for location permissions
        String user_chosen_phone = user.getPhoneNumber();
        if(user_chosen_phone != null && !user_chosen_phone.isEmpty()){
            if(user_chosen_phone.contains("+"))binding.etParentNumber.setText(user_chosen_phone.substring(3));
            else binding.etParentNumber.setText(user_chosen_phone);
        }
	    checkPerms();
    }
    
	private void checkPerms(){
		if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
		}else{
			getPinCode();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == 122){
			if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
				getPinCode();
			}else{
				new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed)
						.setTitle("Needs Location Permission")
						.setPositiveButton("OK", null)
						.setMessage("To serve you better and to get PinCode")
						.setOnDismissListener(dialog -> checkPerms()).show();
			}
		}
	}
	
	private void getPinCode(){
		locationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
			Geocoder geocoder = new Geocoder(requireContext());
			try {
				List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
				if(addresses != null && addresses.size() > 0 ){
					pinCode = addresses.get(0).getPostalCode();
					binding.etPinCode.setText(pinCode);
					Log.d(TAG, "proceedWithLocationPermissions: Found PinCode" + pinCode);
				}else{
					getPinCode();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
    
    private int getSeniorAge(){
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            age--;
        } else {
            if (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
        }
        return age;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		binding.etPinCode.setText(pinCode);
		//Check if pincode is set or not
		if(binding.etPinCode.getText().toString().isEmpty())getPinCode();
		binding.calenderIcon.setOnClickListener(v->{
			DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, 2000, 1,1);
			datePickerDialog.show();
		});
		setClickListeners();
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
            //Check age
            if(hasSelectedDate && getSeniorAge() < 59){
                new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed).setTitle("OOPS!")
                        .setMessage("It seems that you are not old enough to qualify as a senior citizen")
                        .setCancelable(false)
                        .setPositiveButton("Go back", (dialog, which) -> {
                            if(which==AlertDialog.BUTTON_POSITIVE){
	                            viewModel.getNavController().navigate(R.id.action_registerAsParentFR_to_registrationChoiceFR);
                            }
                        })
                        .setNegativeButton("Dismiss", null)
                        .show();
                return;
            }
	        String nick_name = binding.etNickName.getText().toString();
	        String ps_nick_name = binding.etPsNickName.getText().toString();
	        String parent_number =binding.etParentNumber.getText().toString();
	        String child_number = binding.etChildNumber.getText().toString();
	        if(pinCode.length() != 6){
		        binding.etPinCode.setError("Enter a valid pin code");
		        getPinCode();
		        return;
	        }
	        if(!parent_number.isEmpty() && parent_number.length() != 10){
	        	//Wrong parent number
		        binding.etChildNumber.setError("Enter a valid number");
		        return;
	        }
	        if(!child_number.isEmpty() && !child_number.equals("null") && child_number.length() != 10){
		        binding.etChildNumber.setError("Should be a valid Phone Number");
		        return;
	        }
	        ProgressDialog dialog = new ProgressDialog(requireContext());
	        dialog.setIcon(R.drawable.pslogotrimmed);
	        dialog.setMessage("Please Wait");
	        dialog.show();
	        FirebaseFirestore.getInstance().collection(FireStoreUtil.STATIC_DATA_COLLECTION_NAME).document("static").get().addOnSuccessListener(snapshot -> {
		        if(snapshot!=null && snapshot.exists()){
			        try {
				        Double membershipFee = snapshot.getDouble("memfee");
				        FireStoreUtil.makeUserWithUID(requireContext(), user.getUid()
						        ,name, user.getEmail(), nick_name,ps_nick_name, parent_number,	dateTimeStamp, pinCode,child_number,"1", membershipFee)
						        .addOnSuccessListener(success->{
							        FireStoreUtil.addToCluster(requireContext(), pinCode, user.getUid());
							        Log.d(TAG, "successfully made user");
							        if(user.getPhoneNumber() != null){
								        FireStoreUtil.addToPhoneNumberList(requireContext() , user.getPhoneNumber(), user.getUid());
							        }
							        viewModel.getPrefUtil().updateSharedPreferencesPostRegister(user.getUid(), name, user.getEmail(), nick_name, ps_nick_name,
									        dateTimeStamp, pinCode, parent_number,child_number);
							        Intent intent = new Intent(requireContext(), MainApp.class);
							        intent.putExtra(MainApp.IS_NEW_USER, true);
							        dialog.dismiss();
							        startActivity(intent);
						        });
			        }catch (Exception ignored){}
		        }
	        });
        });
    }
}

