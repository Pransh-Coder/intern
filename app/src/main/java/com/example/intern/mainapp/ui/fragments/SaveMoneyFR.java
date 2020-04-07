package com.example.intern.mainapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.MainAppViewModel;
import com.example.intern.databinding.ActivitySaveMoneyBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveMoneyFR extends Fragment {
	private ActivitySaveMoneyBinding binding;
	private MainAppViewModel viewModel;
	
	public SaveMoneyFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(MainAppViewModel.class);
		binding = ActivitySaveMoneyBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
}
