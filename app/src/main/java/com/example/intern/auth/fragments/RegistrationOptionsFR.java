package com.example.intern.auth.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentRegistrationOptionsFRBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegistrationOptionsFR extends Fragment {
	private static String TAG = RegistrationOptionsFR.class.getSimpleName();
	private int G_SIGN_IN_REQ_CODE = 12;
	private FragmentRegistrationOptionsFRBinding binding;
	private AuthViewModel viewModel;
	
	public RegistrationOptionsFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		binding = FragmentRegistrationOptionsFRBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//TODO:
		binding.btnGoogleSignup.setOnClickListener(v->{
			Intent intent = viewModel.getGoogleSignInClient().getSignInIntent();
			startActivityForResult(intent, G_SIGN_IN_REQ_CODE);
		});
		binding.btnSignupPhone.setOnClickListener(v->{
			Navigation.findNavController(v).navigate(R.id.action_registrationOptionsFR_to_phoneRegistrationFR);
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == G_SIGN_IN_REQ_CODE) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				Log.d(TAG, "onActivityResult: g sign in");
				GoogleSignInAccount account = task.getResult(ApiException.class);
				if(account != null){
					firebaseAuthWithGoogle(account);
				}
			} catch (ApiException e) {
				Log.d(TAG, "onActivityResult: failed g sign in");
			}
		}
	}
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount account){
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		viewModel.getFirebaseAuth().signInWithCredential(credential).addOnSuccessListener(authResult -> {
			if(authResult.getUser() != null){
				Log.d(TAG, "firebaseAuthWithGoogle: success");
				viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
				if(!viewModel.isRegChoiceisParent()){
					viewModel.getNavController().navigate(R.id.action_registrationOptionsFR_to_registerAsChildFR);
				}else{
					viewModel.getNavController().navigate(R.id.action_registrationOptionsFR_to_registerAsParentFR);
				}
			}else{
				Log.d(TAG, "firebaseAuthWithGoogle: dismissed");
			}
		});
	}
}
