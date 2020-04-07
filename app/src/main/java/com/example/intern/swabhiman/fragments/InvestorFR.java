package com.example.intern.swabhiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.intern.databinding.ActivitySwabhimanInvestorBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvestorFR extends Fragment {
	private ActivitySwabhimanInvestorBinding binding;
	
	public InvestorFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = ActivitySwabhimanInvestorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.submitInvest.setOnClickListener(v->{
			//TODO : Submit Data
		});
	}
}
