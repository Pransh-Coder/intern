package com.example.intern.swabhiman.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.databinding.ActivitySwabhimanInvestorBinding;
import com.example.intern.mailers.SwabhimanAutoMailer;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvestorFR extends Fragment {
	private static int INVEST_LIMIT = 1000;
	private ActivitySwabhimanInvestorBinding binding;
	private SwabhimanVM viewModel;
	
	public InvestorFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanInvestorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.submitInvest.setOnClickListener(v->{
			Editable investAmt = binding.etInvest.getText();
			if(investAmt != null){
				String invest = investAmt.toString();
				if(Integer.parseInt(invest) >= INVEST_LIMIT){
					SwabhimanAutoMailer.sendSwabhimanMail(SwabhimanAutoMailer.SWABHIMAN_INVESTOR_SUB_BASE + " Rs." +
							invest, "User wants to Invest INR "+ invest,viewModel.getUserMail());
					new AlertDialog.Builder(requireContext()).setTitle("THANK YOU !")
							.setPositiveButton("OK", (button, which)-> viewModel.getNavController().navigateUp());
				}else{
					Toast.makeText(requireContext(), "Don't you think it is too low?", Toast.LENGTH_LONG).show();
				}
			}
		});
		binding.ivBackButton.setOnClickListener(v-> viewModel.getNavController().navigateUp());
		binding.ivHomeButton.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
	}
}
