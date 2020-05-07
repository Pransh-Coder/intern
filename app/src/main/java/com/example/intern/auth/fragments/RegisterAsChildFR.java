package com.example.intern.auth.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.intern.databinding.FragmentRegisterAsChildBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RegisterAsChildFR extends Fragment {
	private static String TAG = RegisterAsChildFR.class.getSimpleName();
	private final Calendar calendar = Calendar.getInstance();
	private AuthViewModel viewModel;
	private FirebaseUser user;
	//private String pinCode;
	private FragmentRegisterAsChildBinding binding;
	public static  final String Shared_pref="sharedPrefs";
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
		 binding.etDOB.setText(calendar.get(Calendar.DAY_OF_MONTH) + " / " + (calendar.get(Calendar.MONTH) + 1) + " / "  + calendar.get(Calendar.YEAR));
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
		viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
		user = viewModel.getFirebaseUser();
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(user == null){
			Toast.makeText(requireContext(), "Cannot find account", Toast.LENGTH_LONG).show();
			viewModel.getNavController().navigate(R.id.action_registerAsChildFR_to_registrationChoiceFR);
			return;
		}
		//Check for location permissions
		String user_chosen_phone = user.getPhoneNumber();
		if(user_chosen_phone != null && !user_chosen_phone.isEmpty()){
			if(user_chosen_phone.contains("+"))binding.etChildNumber.setText(user_chosen_phone.substring(3));
			else binding.etChildNumber.setText(user_chosen_phone);
		}
		binding.calenderIcon.setOnClickListener(v->{
			DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, 2000, 1,1);
			datePickerDialog.show();
		});
		//todo:
		//checkPerms();
	}
	
	private void checkPerms(){
		try{
			if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
			}else{
				try{
					getPinCode();
				}catch (Exception ignored){}
			}
		}catch(SecurityException e){
			try{
				Dexter.withContext(requireContext()).withPermission(Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
						getPinCode();
					}
					
					@Override
					public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
					
					}
					
					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
					
					}
				}).check();
			}catch (Exception ignored){}
		}catch (Exception ignored){}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == 122){
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				try{
					getPinCode();
				}catch (Exception ignored){}
			}else{
				new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed)
						.setTitle(getResources().getString(R.string.need_location_perm_title))
						.setPositiveButton(getResources().getString(R.string.ok), null)
						.setMessage(getResources().getString(R.string.pincode_prompt_message))
						.setOnDismissListener(dialog -> checkPerms()).show();
			}
		}
	}
	
	private void getPinCode(){
		try{
			FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
			locationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
				Geocoder geocoder = new Geocoder(requireContext());
				try {
					List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
					if(addresses != null && addresses.size() > 0 ){
						/*pinCode = addresses.get(0).getPostalCode();
						binding.etPinCode.setText(pinCode);
						Log.d(TAG, "proceedWithLocationPermissions: Found PinCode" + pinCode);*/
					}else{
						try{
							getPinCode();
						}catch (Exception ignored){}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}catch (Exception ignored){}
	}
	
	private int getAge(){
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
		/*binding.etPinCode.setText(pinCode);
		//Check if pincode is set or not
		if(binding.etPinCode.getText().toString().isEmpty()){
			try{
				getPinCode();
			}catch (Exception ignored){}
		}*/
		setClickListeners();
	}
	
	private void setClickListeners(){
		binding.btnRegisterasChildSignIn.setOnClickListener(v->{
			String name = binding.etName.getText().toString();
			if(name.isEmpty()){
				binding.etName.setError("Name Cannot Be Empty");return;
			}
			//Date check
			if(dateTimeStamp==null || !hasSelectedDate){
				binding.etDOB.setError("DOB Cannot Be Empty");return;
			}
			//Age check
			if(getAge() > 58){
				new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed).setTitle("OOPS!")
						.setMessage("It seems that you are too young. Register as a senior citizen instead")
						.setCancelable(false)
						.setPositiveButton("Go back", (dialog, which) -> {
							if(which==AlertDialog.BUTTON_POSITIVE){
								viewModel.getNavController().navigate(R.id.action_registerAsChildFR_to_registrationChoiceFR);
							}
						})
						.setNegativeButton("Dismiss", null)
						.show();
				return;
			}
			//PIN CODE check
			String pinCode = binding.etPinCode.getText().toString();
			if(pinCode.length() != 6){
				binding.etPinCode.setError("Enter a valid pin code");
				return;
			}
			String nick_name = binding.etNickName.getText().toString();
			String ps_nick_name = binding.etPsNickName.getText().toString();
			String parent_number =binding.etParNumber.getText().toString();
			String child_number = binding.etChildNumber.getText().toString();
			//Phone number check
			if(!child_number.isEmpty() && child_number.length() != 10){
				//Wrong child number
				binding.etChildNumber.setError("Enter a valid number");
				return;
			}else if(TextUtils.isEmpty(child_number)){
				binding.etChildNumber.setError("Enter Phone Number");
				return;
			}
			if(!parent_number.isEmpty() && !parent_number.equals("null")  && parent_number.length() != 10){
				binding.etParNumber.setError("Should be a valid Phone Number");
				return;
			}
			ProgressDialog dialog = new ProgressDialog(requireContext());
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.setMessage("Please Wait");
			dialog.show();
			FirebaseFirestore.getInstance().collection(FireStoreUtil.STATIC_DATA_COLLECTION_NAME).document("static").get().addOnSuccessListener(snapshot -> {
				if(snapshot != null && snapshot.exists()){
					try{
						Double membershipFee = snapshot.getDouble("memfee");
						FireStoreUtil.makeUserWithUID(requireContext(), user.getUid()
								,name, user.getEmail(), nick_name,ps_nick_name, child_number,	dateTimeStamp, pinCode,parent_number,"1", membershipFee)
								.addOnSuccessListener(success->{
									FireStoreUtil.addToCluster(requireContext(), pinCode, user.getUid()).addOnSuccessListener(anotherVoid->{
										//CHeck if user exists
										FirebaseFirestore.getInstance().collection(FireStoreUtil.USER_COLLECTION_NAME).document(user.getUid()).addSnapshotListener((userSnap, error)->{
											if(userSnap != null && userSnap.exists()){
												Log.d(TAG, "successfully made user");
												if(user.getPhoneNumber() != null){
													FireStoreUtil.addToPhoneNumberList(requireContext() , userSnap.getString(FireStoreUtil.USER_PHONE_NUMBER), user.getUid());
												}
												viewModel.getPrefUtil().updateSharedPreferencesPostRegister(user.getUid(), name, user.getEmail(), nick_name, ps_nick_name,
														dateTimeStamp, pinCode, child_number, parent_number);
												dialog.dismiss();
												SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Shared_pref, MODE_PRIVATE);
												SharedPreferences.Editor editor = sharedPreferences.edit();
												editor.putString("role", "2");
												editor.apply();
												viewModel.getLoggedInListener().isLoggedIn(true);
											}
										});
									});
								});
					}catch (Exception ignored){}
				}
			});
		});
	}
	
	/*private void verifyPhoneNumber(String phoneNumber){
		//Phone verification callback
		PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				try{
					FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
						Log.d(TAG, "onVerificationCompleted: Verified");
						Toast.makeText(requireContext(),  "Phone Number Verified!", Toast.LENGTH_SHORT).show();
					});
				}catch (Exception ignored){
					Log.d(TAG, "onVerificationCompleted: Cannot update phone number");
				}
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
					if(which==DialogInterface.BUTTON_POSITIVE){*//*Do stuff*//*}else return;
					//TODO : Get the OTP and Verify
					String otp = otpEditText.getText().toString();
					if(otp.length() != 6)otpEditText.setError("Invalid OTP");
					else{
						PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
						try{
							FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnSuccessListener(authResult -> {
								Log.d(TAG, "onCodeSent: linked the account");
								Toast.makeText(requireContext(),  "Phone Number Verified!", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							});
						}catch (Exception ignored){
							Log.d(TAG, "onCodeSent: Cannot verify number");
						}
					}
				});
				otpGetterDialog.setNegativeButton("DISMISS", (dialog, which) -> dialog.dismiss());
				otpGetterDialog.setNeutralButton("RESEND OTP", (dialog, which) -> {
					PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 5 , TimeUnit.SECONDS, requireActivity(), this,token);
				});
				otpGetterDialog.show();
				Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_LONG).show();
			}
		};
		//Make an instance of Phone verifier
		PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 5, TimeUnit.SECONDS,requireActivity(), callbacks);
	}*/
}
