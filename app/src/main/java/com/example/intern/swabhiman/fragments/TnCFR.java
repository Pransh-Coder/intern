package com.example.intern.swabhiman.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.databinding.ActivitySwabhimanTcBinding;
import com.example.intern.swabhiman.SwabhimanVM;

public class TnCFR extends Fragment {
	boolean f = false;
	private SwabhimanVM viewModel;
	private ActivitySwabhimanTcBinding binding;
	
	public TnCFR() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get((SwabhimanVM.class));
		binding = ActivitySwabhimanTcBinding.inflate(inflater, container, false);
		View view = binding.getRoot();

		TextView tv_list = view.findViewById(R.id.tv_list_concept);
		tv_list.setText(Html.fromHtml("&#8594; Opportunity for good cash flow<br>&#8594; Opportunities for starting a small business<br>" +
				"&#8594; Option to become a consultant based on experience<br>&#8594; Part-time job option as appropriate"));
		TextView tv = view.findViewById(R.id.tv_desc1);
		tv.setText(Html.fromHtml("A platform created specially for <b>senior citizens</b>, it will help them to explore new ways for growth in income or to contribute to the society as per their interest and experience, such as<br>"));
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//TODO: Accept TnC and proceed
		binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			f = binding.checkbox.isChecked();
		});
		binding.submit.setOnClickListener(v -> {
			if (f) {
				viewModel.setHasAcceptedTnC(true);
				Navigation.findNavController(v).navigate(R.id.action_tnCFR_to_choicesFR);
			}else{
				Toast.makeText(requireContext(), "Please Accept T&C", Toast.LENGTH_SHORT).show();
			}
		});
		binding.ivBackButton.setOnClickListener(v->{
			requireActivity().onBackPressed();
		});
		binding.ivHomeButton.setOnClickListener(v->{
			requireActivity().onBackPressed();
		});
	}
}