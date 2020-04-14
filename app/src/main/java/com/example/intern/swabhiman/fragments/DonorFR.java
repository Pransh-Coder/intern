package com.example.intern.swabhiman.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivitySwabhimanDonorBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class DonorFR extends Fragment {
	private ActivitySwabhimanDonorBinding binding;
	private SwabhimanVM viewModel;
	
	public DonorFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanDonorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return  view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.swabhimanDonorButtonSubmit.setOnClickListener(v->{
			Editable amount = binding.editTextDonationAmountSwabhimanDonor.getText();
			if(amount != null){
				final Context context = requireContext();
				final Checkout checkout = new Checkout();
				checkout.setKeyID("rzp_test_9SFxBSOfMFPxyk");
				checkout.setImage(R.drawable.pslogotrimmed);
				int donationAmt=  Integer.parseInt(amount.toString())*100;
				String setAmt = Integer.toString(donationAmt);
				try{
					JSONObject options = new JSONObject();
					options.put("name", "Prarambh PVT LTD");
					options.put("description" ,"Donation to PS");
					options.put("currency" , "INR");
					options.put("amount" , setAmt);
					SharedPrefUtil prefUtil = new SharedPrefUtil(requireContext());
					String email = prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY,null);
					String contact = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PHONE_NO, null);
					JSONObject prefill = new JSONObject();
					prefill.put("email" ,email);
					prefill.put("contact" , contact);
					//Theme options
					JSONObject theme = new JSONObject();
					theme.put("color" , "#FFEB00");
					options.put("prefill" , prefill);
					options.put("theme", theme);
					checkout.open(requireActivity(), options);
				}catch (Exception e){
					Toast.makeText(context, "Something Went Wrong\nPlease Try Again", Toast.LENGTH_LONG).show();
				}
			}
		});
		binding.swabhimanDonorButtonBack.setOnClickListener(v->{
			viewModel.getNavController().navigateUp();
		});
		binding.swabhimanDonorButtonHome.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
	}
}
