package com.example.intern.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.intern.databinding.FragmentRegisterAsChildBinding;

public class RegisterAsChildFragment extends Fragment {
	private String TAG = "RegisterAsChildFR";
	private FragmentRegisterAsChildBinding binding;
	
	public RegisterAsChildFragment() {	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentRegisterAsChildBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		Log.d(TAG, "onCreateView: inflated view");
		return view;
	}
}
