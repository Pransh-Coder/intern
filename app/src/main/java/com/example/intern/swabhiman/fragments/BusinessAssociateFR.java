package com.example.intern.swabhiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.intern.databinding.ActivitySwabhimanBussinessAssociateBinding;

public class BusinessAssociateFR extends Fragment {
	private ActivitySwabhimanBussinessAssociateBinding binding;
	
	public BusinessAssociateFR() {}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = ActivitySwabhimanBussinessAssociateBinding.inflate(inflater,container,false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.swabhimanBussinessAssociateButtonSubmit.setOnClickListener(v->{
			//TODO : Send Data
		});
	}
}
