package com.example.intern.auth.fragments;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneRegistrationFR extends Fragment {
	private static String TAG = PhoneRegistrationFR.class.getSimpleName();
	private String mVerificationId;
	private PhoneAuthProvider.ForceResendingToken mResendToken;
	private ProgressDialog loadingbar;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
	private PhoneLoginUiBinding binding;
	private AuthViewModel viewModel;
	
	public PhoneRegistrationFR() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = PhoneLoginUiBinding.inflate(inflater, container, false);
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		loadingbar = new ProgressDialog(requireContext());
		callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
				signInWithPhoneAuthCredential(phoneAuthCredential);
				
			}
			
			@Override
			public void onVerificationFailed(FirebaseException e) {
				loadingbar.dismiss();
				Toast.makeText(requireContext(), "Invalid please enter correct phone number with your country code", Toast.LENGTH_LONG).show();
			}
			
			public void onCodeSent(@NonNull String verificationId,
			                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
				
				// Save verification ID and resending token so we can use them later
				mVerificationId = verificationId;
				mResendToken = token;
				loadingbar.dismiss();
				Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_LONG).show();
				binding.etPhoneNumber.setVisibility(View.INVISIBLE);
			}
		};
		binding.btnGetOtp.setOnClickListener(v -> {
			String phnno= binding.etPhoneNumber.getText().toString();
			if (TextUtils.isEmpty(phnno)) {
				binding.etPhoneNumber.setError("Phone Number is invalid");
			} else {
				loadingbar.setTitle("Phone Verification");
				loadingbar.setMessage("Please wait,while we authenticate your phone");
				loadingbar.setCanceledOnTouchOutside(false);
				loadingbar.show();
				
				PhoneAuthProvider.getInstance().verifyPhoneNumber(
						"+91" + phnno,        // Phone number to verify
						60,                 // Timeout duration
						TimeUnit.SECONDS,   // Unit of timeout
						requireActivity(),               // Activity (for callback binding)
						callbacks);        // OnVerificationStateChangedCallbacks
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
				signInWithPhoneAuthCredential(credential);
			}
		});
	}
	
	
	private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
		viewModel.getFirebaseAuth().signInWithCredential(credential)
				.addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							loadingbar.dismiss();
							Toast.makeText(requireContext(), "Logged in Successfully", Toast.LENGTH_LONG).show();
							viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_registerAsChildFR);
						} else {
							String msg = task.getException().toString();
							Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
						}
					}
				});
	}
}
