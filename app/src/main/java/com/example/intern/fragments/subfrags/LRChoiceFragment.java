package com.example.intern.fragments.subfrags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.databinding.FragmentLRChoiceBinding;
import com.example.intern.viewmodels.RegistrationModel;

public class LRChoiceFragment extends Fragment {
	private FragmentLRChoiceBinding binding;
	private RegistrationModel viewModel;
	
	public LRChoiceFragment() {	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(RegistrationModel.class);
		binding = FragmentLRChoiceBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		binding.registrationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewModel.getRegistrationFlowController().navigate(R.id.action_LRChoiceFragment_to_registrationFragment);
			}
		});
	}
}
