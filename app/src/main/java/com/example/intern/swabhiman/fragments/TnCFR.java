package com.example.intern.swabhiman.fragments;

import android.content.Intent;
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

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivitySwabhimanTcBinding;
import com.example.intern.swabhiman.SwabhimanVM;

public class TnCFR extends Fragment {
	boolean f = false;
	private SwabhimanVM viewModel;
	private ActivitySwabhimanTcBinding binding;
	TextView secPara,thrPara,forPara,sixpara;
	
	public TnCFR() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get((SwabhimanVM.class));
		binding = ActivitySwabhimanTcBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		secPara = view.findViewById(R.id.secPara);
		thrPara = view.findViewById(R.id.thrPara);
		forPara = view.findViewById(R.id.forPara);
		sixpara = view.findViewById(R.id.sixpara);

		//arrow1.setText("<b>"+"&#8594"+"</b>"+" ");
		String sourceString =/*"<b>"+"&#8594"+"</b>"+*/ "<b>Opportunity for good cash flow</b> ";
		secPara.setText(Html.fromHtml(sourceString));

		String sourceString2 =/*"<b>"+"&#8594 "+"</b>"+*/"<b>Opportunities for starting a small business</b>";
		thrPara.setText(Html.fromHtml(sourceString2));

		String sourceString3 = /*"<b>"+"&#8594 "+"</b>"+*/"<b>Option to become a consultant based on experience</b>";
		forPara.setText(Html.fromHtml(sourceString3));
		String sourceString4 = /*"<b>"+"&#8594 "+"</b>"+*/"<b>Part-time job option as appropriate</b>";
		sixpara.setText(Html.fromHtml(sourceString4));
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
		binding.ivNotifButton.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), NewsAndUpdatesACT.class);
			startActivity(intent);
		});
	}
}