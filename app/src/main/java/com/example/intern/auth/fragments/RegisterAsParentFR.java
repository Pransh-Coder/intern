package com.example.intern.auth.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisterAsParentFR extends Fragment {
    private static String TAG = RegisterAsParentFR.class.getSimpleName();
    private FragmentRegisterAsParentFRBinding binding;
    private AuthViewModel viewModel;
    private FusedLocationProviderClient locationProviderClient;
    private String pinCode;
    private FirebaseUser user;
    private final Calendar calendar = Calendar.getInstance();
    private boolean hasSelectedDate;
	private boolean hasVerifiedPH;
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
        binding.etDOB.setText(calendar.get(Calendar.DAY_OF_MONTH) + " / " + (calendar.get(Calendar.MONTH) + 1) + " / "  + calendar.get(Calendar.YEAR));
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
	    binding.calenderIcon.setOnClickListener(v->{
		    DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, 2000, 1,1);
		    datePickerDialog.show();
	    });
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
	        //Show a dialog to verify the phone number
	        /*if(!viewModel.isHasOptedPhoneVerification() && !hasVerifiedPH){
		        verifyPhoneNumber(child_number);
		        return;
	        }*/
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
						        .addOnSuccessListener(aVoid->{
							        FireStoreUtil.addToCluster(requireContext(), pinCode, user.getUid()).addOnSuccessListener(anotherVoid->{
								        Log.d(TAG, "successfully made user");
								        if(user.getPhoneNumber() != null){
									        FireStoreUtil.addToPhoneNumberList(requireContext() , user.getPhoneNumber(), user.getUid());
								        }
								        //Check if user document exists
								        FirebaseFirestore.getInstance().collection(FireStoreUtil.USER_COLLECTION_NAME).document(user.getUid()).addSnapshotListener((userSnap, error)->{
									        if(userSnap != null && userSnap.exists()){
										        viewModel.getPrefUtil().updateSharedPreferencesPostRegister(user.getUid(), name, user.getEmail(), nick_name, ps_nick_name,
												        dateTimeStamp, pinCode, parent_number,child_number);
										        dialog.dismiss();
										        viewModel.getLoggedInListener().isLoggedIn(true);
									        }else if (error != null){
										        Log.d(TAG, "setClickListeners: " + error.getMessage());
									        }
								        });
							        });
						        });
			        }catch (Exception ignored){}
		        }
	        });
        });
    }
	
	private void verifyPhoneNumber(String phoneNumber){
		//Phone verification callback
		PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				try{
					FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
						hasVerifiedPH = true;
						Toast.makeText(requireContext(),  "Phone Number Verified!", Toast.LENGTH_SHORT).show();
					});
				}catch (Exception ignored){}
			}
			@Override
			public void onVerificationFailed(@NonNull FirebaseException e) {
				Toast.makeText(requireContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
			}
			
			public void onCodeSent(@NonNull String verificationId,
			                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
				// Save verification ID and resending token so we can use them later
				//Build a dialog box for verification of Phone Number
				final AlertDialog.Builder otpGetterDialog = new AlertDialog.Builder(requireContext());
				final EditText otpEditText = new EditText(requireContext());
				otpEditText.setHint("OTP");
				otpEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
				otpEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
				otpGetterDialog.setMessage("Enter OTP");
				otpGetterDialog.setIcon(R.drawable.pslogotrimmed);
				otpGetterDialog.setView(otpEditText);
				otpGetterDialog.setCancelable(false);
				otpGetterDialog.setPositiveButton("Verify", (dialog, which) -> {
					if(which== DialogInterface.BUTTON_POSITIVE){/*Do stuff*/}else return;
					//TODO : Get the OTP and Verify
					String otp = otpEditText.getText().toString();
					if(otp.length() != 6)otpEditText.setError("Invalid OTP");
					else{
						PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
						try{
							FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnSuccessListener(authResult -> {
								hasVerifiedPH = true;
								Log.d(TAG, "onCodeSent: linked the account");
								Toast.makeText(requireContext(),  "Phone Number Verified!", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							});
						}catch (Exception ignored){}
					}
				});
				otpGetterDialog.setNegativeButton("DISMISS", (dialog, which) -> dialog.dismiss());
				otpGetterDialog.setNeutralButton("RESEND OTP", (dialog, which) -> {
					PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 60 , TimeUnit.SECONDS, requireActivity(), this,token);
				});
				otpGetterDialog.show();
				Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_LONG).show();
			}
		};
		//Make an instance of Phone verifier
		PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 60, TimeUnit.SECONDS,requireActivity(), callbacks);
	}
}

