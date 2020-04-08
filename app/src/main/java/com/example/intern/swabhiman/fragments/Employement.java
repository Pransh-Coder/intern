package com.example.intern.swabhiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.intern.databinding.ActivitySwabhimanRegisterBinding;

public class Employement extends Fragment {
	private ActivitySwabhimanRegisterBinding binding;
	
	public Employement() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = ActivitySwabhimanRegisterBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//TODO : Submit Details via Email
	}
}
