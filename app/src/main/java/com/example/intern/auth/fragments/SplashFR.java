package com.example.intern.auth.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.AuthVerifyService;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.mainapp.MainAppActivity;

public class SplashFR extends Fragment {
	private static String TAG = SplashFR.class.getSimpleName();
	private AuthViewModel viewModel;
	
	public SplashFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		return inflater.inflate(R.layout.fragment_splash_f_r, container, false);
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onStart: Splash screen shown");
		super.onResume();
		SharedPrefUtil prefUtil = new SharedPrefUtil(requireActivity());
		if(viewModel.getFirebaseUser() != null){
			segueIntoApp();
		}
		if(prefUtil.getLoginStatus()){
			segueIntoApp();
		}else{
			viewModel.getNavController().navigate(R.id.action_splashFR_to_loginRegisterFR);
		}
	}
	
	private void segueIntoApp(){
		Log.d(TAG, "segueIntoApp: authentication verified");
		try {
			Thread.sleep(500);
			Toast.makeText(requireContext(), "Welcome back to PS", Toast.LENGTH_LONG).show();
			//TODO:Redirect to main app
			Intent intent = new Intent(requireContext(), MainAppActivity.class);
			startActivity(intent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String UID = viewModel.getFirebaseUser().getUid();
		Intent intent = new Intent(requireContext(), AuthVerifyService.class);
		intent.putExtra(AuthVerifyService.USER_UID_INTENT_KEY, UID);
		requireActivity().startService(intent);
	}
}
