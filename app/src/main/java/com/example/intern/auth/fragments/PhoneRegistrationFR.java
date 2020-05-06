package com.example.intern.auth.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.PhoneLoginUiBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PhoneRegistrationFR extends Fragment {
	private String mVerificationId;
	private ProgressDialog loadingbar;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
	private PhoneAuthProvider.ForceResendingToken forceResendingToken = null;
	private PhoneLoginUiBinding binding;
	private AuthViewModel viewModel;
	private String phnno;
	private ProgressDialog progressDialog;
	public PhoneRegistrationFR() {
		// Required empty public constructor
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = PhoneLoginUiBinding.inflate(inflater, container, false);
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		View view = binding.getRoot();
		binding.tvmascot.setText("Signup");
		progressDialog = new ProgressDialog(getContext());
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.btnCancel.setOnClickListener(v -> viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_LoginOptionFR));
		loadingbar = new ProgressDialog(requireContext());
		callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				try{
					signInWithPhoneAuthCredential(phoneAuthCredential);
				}catch (Exception e ){
					Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onVerificationFailed(@NonNull FirebaseException e) {
				loadingbar.dismiss();
				Toast.makeText(requireContext(), "Invalid please enter correct phone number", Toast.LENGTH_LONG).show();
			}
			
			public void onCodeSent(@NonNull String verificationId,
			                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
				
				// Save verification ID and resending token so we can use them later
				mVerificationId = verificationId;
				forceResendingToken = token;
				loadingbar.dismiss();
				Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_LONG).show();
				binding.etPhoneNumber.setVisibility(View.INVISIBLE);
			}
		};
		binding.btnOtp.setOnClickListener(v -> {
			phnno = binding.etPhoneNumber.getText().toString();
			if (TextUtils.isEmpty(phnno)) {
				binding.etPhoneNumber.setError("Phone Number is invalid");
			} else {
				loadingbar.setTitle("Phone Verification");
				loadingbar.setMessage("Please wait,while we authenticate your phone");
				loadingbar.setCanceledOnTouchOutside(false);
				loadingbar.show();
				binding.btnOtp.setVisibility(View.INVISIBLE);
				binding.etPhoneNumber.setVisibility(View.INVISIBLE);
				binding.tvOTP.setVisibility(View.INVISIBLE);
				binding.code91.setVisibility(View.INVISIBLE);
				binding.etOtp.setVisibility(View.VISIBLE);
				binding.btnCancel.setVisibility(View.VISIBLE);
				binding.btnLogin.setVisibility(View.VISIBLE);
				binding.btnResendotp.setVisibility(View.VISIBLE);
				PhoneAuthProvider.getInstance().verifyPhoneNumber(
						"+91" + phnno,        // Phone number to verify
						60,                 // Timeout duration
						TimeUnit.SECONDS,   // Unit of timeout
						requireActivity(),               // Activity (for callback binding)
						callbacks);        // OnVerificationStateChangedCallbacks
			}
		});
		binding.btnResendotp.setOnClickListener(v ->
		{
			if(forceResendingToken != null){
				loadingbar.setTitle("Phone Verification");
				loadingbar.setMessage("Resending code...");
				loadingbar.setCanceledOnTouchOutside(false);
				loadingbar.show();
				PhoneAuthProvider.getInstance().verifyPhoneNumber(
						"+91" + phnno,        // Phone number to verify
						60,                 // Timeout duration
						TimeUnit.SECONDS,   // Unit of timeout
						requireActivity(),               // Activity (for callback binding)
						callbacks, forceResendingToken);
			}
		});
		binding.btnLogin.setOnClickListener(v -> {
			String code = binding.etOtp.getText().toString();
			if (TextUtils.isEmpty(code)) {
				Toast.makeText(requireContext(), "Please enter the code", Toast.LENGTH_LONG).show();
			} else {
				loadingbar.setTitle("Code Verification");
				loadingbar.setMessage("Please wait,while we are verifying");
				loadingbar.setCanceledOnTouchOutside(false);
				loadingbar.show();
				PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
				try{
					signInWithPhoneAuthCredential(credential);
				}catch (Exception e){
					Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
		viewModel.getFirebaseAuth().signInWithCredential(credential)
				.addOnCompleteListener(requireActivity(), task -> {
					if (task.isSuccessful()) {
						loadingbar.dismiss();
						checkExistence();
					} else {
						try{
							Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
						}catch(Exception ignored){}
					}
				});
	}
	private void checkExistence() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if(user==null){
			//User was not authenticated
			Toast.makeText(requireContext(), "Cannot find account", Toast.LENGTH_SHORT).show();
			viewModel.getNavController().navigateUp();
			return;
		}
		String currentuserid = user.getUid();
		progressDialog.setTitle("Please Wait");
		progressDialog.setMessage("This will only take few Seconds...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		//Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_LONG).show();
		FirebaseFirestore.getInstance().collection("Users").document(currentuserid).get().addOnCompleteListener(task -> {
			DocumentSnapshot snapshot = task.getResult();
			if(snapshot != null && snapshot.exists()){
				progressDialog.dismiss();
				Toast.makeText(getContext(),"User  already exists..Login instead",Toast.LENGTH_LONG).show();
				viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_LoginRegister);
			}
			else {
				progressDialog.dismiss();
				viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
				if (!viewModel.isRegChoiceisParent()) {
					viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_registerAsChildFR);
				} else {
					viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_registerAsParentFR);
				}
			}
		});

	}

}
