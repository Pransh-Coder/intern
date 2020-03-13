package com.example.intern.fragments.subfrags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.databinding.FragmentRegistrationChoiceBinding;
import com.example.intern.viewmodels.RegistrationModel;

public class RegistrationChoiceFragment extends Fragment {
	private String TAG = "RegistrationFR";
	private FragmentRegistrationChoiceBinding binding;
	private RegistrationModel viewModel;
	
	public RegistrationChoiceFragment() {	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(RegistrationModel.class);
		binding = FragmentRegistrationChoiceBinding.inflate(inflater, container, false);
		Log.d(TAG, "onCreateView: inflated View");
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		binding.registrationTvAsChild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewModel.getAppFlowController().navigate(R.id.action_loginRegisterFragment_to_registerAsChildFragment);
			}
		});
		binding.registrationTvAsParent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewModel.getAppFlowController().navigate(R.id.action_loginRegisterFragment_to_registerAsParentFragment);
			}
		});
	}
}
