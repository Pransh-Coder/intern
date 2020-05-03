package com.example.intern.auth.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.PhoneLoginUiBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneRegistrationFR extends Fragment {
	private String mVerificationId;
	private ProgressDialog loadingbar;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
	private PhoneLoginUiBinding binding;
	private AuthViewModel viewModel;
	private String phnno;
	private ProgressDialog progressDialog;
	public PhoneRegistrationFR() {
		// Required empty public constructor
	}
	
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
		binding.btnCancel.setOnClickListener(v -> {
			viewModel.getNavController().navigate(R.id.action_phoneRegistrationFR_to_LoginOptionFR);
		});
		loadingbar = new ProgressDialog(requireContext());
		callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				signInWithPhoneAuthCredential(phoneAuthCredential);
				
			}
			
			@Override
			public void onVerificationFailed(@NonNull FirebaseException e) {
				loadingbar.dismiss();
				Toast.makeText(requireContext(), "Invalid please enter correct phone number with your country code", Toast.LENGTH_LONG).show();
			}
			
			public void onCodeSent(@NonNull String verificationId,
			                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
				
				// Save verification ID and resending token so we can use them later
				mVerificationId = verificationId;
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
			loadingbar.setTitle("Phone Verification");
			loadingbar.setMessage("Resending code...");
			loadingbar.setCanceledOnTouchOutside(false);
			loadingbar.show();
			PhoneAuthProvider.getInstance().verifyPhoneNumber(
					"+91" + phnno,        // Phone number to verify
					60,                 // Timeout duration
					TimeUnit.SECONDS,   // Unit of timeout
					requireActivity(),               // Activity (for callback binding)
					callbacks);
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
							checkExistence();
						} else {
							String msg = task.getException().toString();
							Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
						}
					}
				});
	}
	private void checkExistence() {
		String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		progressDialog.setTitle("Please Wait");
		progressDialog.setMessage("This will only take few Seconds...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		//Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_LONG).show();
		FirebaseFirestore.getInstance().collection("Users").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.getResult().exists()) {
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
			}
		});

	}

}
