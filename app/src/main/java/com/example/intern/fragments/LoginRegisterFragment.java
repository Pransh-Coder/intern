package com.example.intern.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.databinding.FragmentLoginRegisterBinding;
import com.example.intern.viewmodels.RegistrationModel;

public class LoginRegisterFragment extends Fragment {
	private String TAG = "LoginRegisterFragment";
	private FragmentLoginRegisterBinding binding;
	private RegistrationModel viewModel;
	
	public LoginRegisterFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(RegistrationModel.class);
		binding = FragmentLoginRegisterBinding.inflate(inflater,container,false);
		View view = binding.getRoot();
		Log.d(TAG, "onCreateView: inflated!");
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		NavController registrationFlowController = Navigation.findNavController(requireActivity(), R.id.registration_nav_host);
		viewModel.setRegistrationFlowController(registrationFlowController);
		Log.d(TAG, "onViewCreated: ViewModel loaded");
	}
}
