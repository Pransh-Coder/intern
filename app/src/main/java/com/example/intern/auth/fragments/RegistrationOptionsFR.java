package com.example.intern.auth.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentRegistrationOptionsFRBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Delayed;

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
				checkuser();
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

	private void checkuser() {
		 int r;
		String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		//Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_LONG).show();
		FirebaseFirestore.getInstance().collection("Users").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.getResult().exists()) {
					Toast.makeText(getContext(),"Welcome Back",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(requireContext(), MainApp.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				} else {

					Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_LONG).show();
					//Intent intent = new Intent(requireContext(), LoginRegisterFR.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					//startActivity(intent);


				}
			}
		});
			}
		}