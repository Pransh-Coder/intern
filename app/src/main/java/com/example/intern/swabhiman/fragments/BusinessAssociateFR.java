package com.example.intern.swabhiman.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.databinding.ActivitySwabhimanBussinessAssociateBinding;
import com.example.intern.mailers.SwabhimanAutoMailer;
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
	}
}
