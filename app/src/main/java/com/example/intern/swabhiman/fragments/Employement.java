package com.example.intern.swabhiman.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.databinding.ActivitySwabhimanRegisterBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;

public class Employement extends Fragment {
	private ActivitySwabhimanRegisterBinding binding;
	private SwabhimanVM viewModel;
	
	public Employement() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanRegisterBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	@Override
	public void onDetach() {
		viewModel.setFragmentOpen(false);
		super.onDetach();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.ivBackButton.setOnClickListener(v->{
			viewModel.getNavController().navigateUp();
		});
		binding.ivHomeButton.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		//TODO : Submit Details via Email
	}
}
