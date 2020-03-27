package com.example.intern.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentRegisterAsChildFRBinding;

public class RegisterAsChildFR extends Fragment {
	private FragmentRegisterAsChildFRBinding binding;
	private AuthViewModel viewModel;
	
	public RegisterAsChildFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		binding = FragmentRegisterAsChildFRBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
}
