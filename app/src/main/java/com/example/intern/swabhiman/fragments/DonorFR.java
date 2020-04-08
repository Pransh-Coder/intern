package com.example.intern.swabhiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.intern.databinding.ActivitySwabhimanDonorBinding;

public class DonorFR extends Fragment {
	private ActivitySwabhimanDonorBinding binding;
	
	public DonorFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = ActivitySwabhimanDonorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return  view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.swabhimanDonorButtonSubmit.setOnClickListener(v->{
			//TODO: Donate Amount using payment window
		});
	}
}
