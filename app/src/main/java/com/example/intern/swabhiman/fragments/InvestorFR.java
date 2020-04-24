package com.example.intern.swabhiman.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.ActivitySwabhimanInvestorBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
		TextView tv = view.findViewById(R.id.tv_desc1);
		tv.setText(Html.fromHtml("We Welcome you to become a part of <b>PS</b>. Anyone can start investing from &#8377; 10,000 and can go up to any limit. They will get 10-12% of monthly interest on the investment.<br>"));
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.submitInvestor.setOnClickListener(v -> {
			//TODO :
			ProgressDialog dialog = new ProgressDialog(getContext());
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.SWABHIMAN_SERVICE)
					.document(FireStoreUtil.INVESTOR)
					.collection(FireStoreUtil.INVESTOR).add(data)
					.addOnSuccessListener(documentReference -> {
						dialog.dismiss();
						new AlertDialog.Builder(getContext()).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
								.setMessage("We will get back to you shortly").setPositiveButton("OK", null).show();

					}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
				}
			});
		});
		binding.ivBackButton.setOnClickListener(v-> viewModel.getNavController().navigateUp());
		binding.ivHomeButton.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		binding.ivNotifButton.setOnClickListener(v -> {
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
