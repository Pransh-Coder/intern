package com.example.intern.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.intern.auth.AuthActivity;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.FragmentSplashFRBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashFR extends Fragment {
	private static String TAG = SplashFR.class.getSimpleName();
	private FragmentSplashFRBinding binding;
	private FirebaseUser user;
	
	public SplashFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentSplashFRBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		user = FirebaseAuth.getInstance().getCurrentUser();
		return view;
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onStart: Splash screen shown");
		super.onResume();
		SharedPrefUtil prefUtil = new SharedPrefUtil(requireActivity());
		if(user != null){
			segueIntoApp();
		}
		if(prefUtil.getLoginStatus()){
			segueIntoApp();
		}else{
			Intent intent = new Intent(requireContext(), AuthActivity.class);
		}
	}
	
	private void segueIntoApp(){
		Log.d(TAG, "segueIntoApp: authentication verified");
		try {
			Thread.sleep(2000);
			Toast.makeText(requireContext(), "Welcome back to PS", Toast.LENGTH_LONG).show();
			//TODO:Redirect to main app
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String UID = user.getUid();
		Intent intent = new Intent(requireContext(), AuthVerifyService.class);
		intent.putExtra(AuthVerifyService.USER_UID_INTENT_KEY, UID);
		requireActivity().startService(intent);
	}
}
