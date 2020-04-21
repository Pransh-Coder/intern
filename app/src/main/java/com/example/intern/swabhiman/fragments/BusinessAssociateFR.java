package com.example.intern.swabhiman.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivitySwabhimanBussinessAssociateBinding;
import com.example.intern.mailers.SwabhimanAutoMailer;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;

public class BusinessAssociateFR extends Fragment {
	private ActivitySwabhimanBussinessAssociateBinding binding;
	private SwabhimanVM viewModel;
	
	public BusinessAssociateFR() {}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanBussinessAssociateBinding.inflate(inflater,container,false);
		View view = binding.getRoot();
		TextView tv = view.findViewById(R.id.tv_desc1);
		tv.setText(Html.fromHtml("PS offerers Frenchisee ownersip of its <b>Prarambh Store</b> and <b>PS Desk</b> to the energetic and enthusiastic seniors,  It will give a chance to beocme an active part of this revolutionary social enterprise chain.<br>"));

		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.swabhimanBussinessAssociateButtonSubmit.setOnClickListener(v->{
			Editable info = binding.editTextSwabhimanBussinessAssociateFeedback.getText();
			if(info != null){
				//TODO : Mail this info to the company
				SwabhimanAutoMailer.sendSwabhimanMail(SwabhimanAutoMailer.SWABHIMAN_BUSS_ASSOCIATE_SUB, info.toString(), viewModel.getUserMail());
				new AlertDialog.Builder(requireContext()).setTitle("THANK YOU !").setPositiveButton("OK",
						(button, which)->{
					viewModel.getNavController().navigateUp();
						}).show();
			}
		});
		binding.swabhimanBussinessAssociateButtonBack.setOnClickListener(v->{
			viewModel.getNavController().navigateUp();
		});
		binding.swabhimanBussinessAssociateButtonHome.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		binding.swabhimanBussinessAssociateButtonNotification.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), NewsAndUpdatesACT.class);
			startActivity(intent);
		});
	}
	
	@Override
	public void onDetach() {
		viewModel.setFragmentOpen(false);
		super.onDetach();
	}
}
