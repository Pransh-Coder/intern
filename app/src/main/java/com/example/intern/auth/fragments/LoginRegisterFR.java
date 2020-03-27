package com.example.intern.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentLoginRegisterFRBinding;

public class LoginRegisterFR extends Fragment {
	private FragmentLoginRegisterFRBinding binding;
	private AuthViewModel viewModel;
	
	public LoginRegisterFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		binding = FragmentLoginRegisterFRBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//TODO: make a login FR too
		binding.registrationButton.setOnClickListener(v->{
			Navigation.findNavController(v).navigate(R.id.action_loginRegisterFR_to_registrationChoiceFR);
		});
	}
}
