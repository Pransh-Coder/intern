package com.example.intern.swabhiman.fragments;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.ActivitySwabhimanDonorBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DonorFR extends Fragment {
	private ActivitySwabhimanDonorBinding binding;
	private SwabhimanVM viewModel;
	TextView secPara,thrPara,forPara,sixpara;
	
	public DonorFR() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanDonorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		secPara = view.findViewById(R.id.secPara);
		thrPara = view.findViewById(R.id.thrPara);
		forPara = view.findViewById(R.id.forPara);
		sixpara = view.findViewById(R.id.sixpara);

		TextView tv = view.findViewById(R.id.tv_desc1);
		tv.setText(Html.fromHtml("PS thanks you to support  this social enterprise cause,It wasn't possible with your kind support.You can donate:-<br>"));

		//arrow1.setText("<b>"+"&#8594"+"</b>"+" ");
		String sourceString =/*"<b>"+"&#8594"+"</b>"+*/ "<b>MONEY</b> ";
		secPara.setText(Html.fromHtml(sourceString));

		String sourceString2 =/*"<b>"+"&#8594 "+"</b>"+*/"<b>TIME</b>";
		thrPara.setText(Html.fromHtml(sourceString2));

		String sourceString3 = /*"<b>"+"&#8594 "+"</b>"+*/"<b>FREE SPACE</b>";
		forPara.setText(Html.fromHtml(sourceString3));
		String sourceString4 = /*"<b>"+"&#8594 "+"</b>"+*/"<b>OTHERS</b>";
		sixpara.setText(Html.fromHtml(sourceString4));
		return  view;
	}
	
	@Override
	public void onDetach() {
		viewModel.setFragmentOpen(false);
		super.onDetach();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.swabhimanDonorButtonSubmit.setOnClickListener(v -> {
			//TODO :
			ProgressDialog dialog = new ProgressDialog(getContext());
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.SWABHIMAN_SERVICE)
					.document(FireStoreUtil.DONOR)
					.collection(FireStoreUtil.DONOR).add(data)
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


		binding.swabhimanDonorButtonBack.setOnClickListener(v->{
			viewModel.getNavController().navigateUp();
		});
		binding.swabhimanDonorButtonHome.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		binding.swabhimanDonorButtonNotification.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), NewsAndUpdatesACT.class);
			startActivity(intent);
		});
	}
}
