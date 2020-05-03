package com.example.intern.swabhiman.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivitySwabhimanRegisterBinding;
import com.example.intern.mailers.SwabhimanAutoMailer;
import com.example.intern.mainapp.MainApp;
import com.example.intern.swabhiman.SwabhimanVM;

public class Employement extends Fragment {
	private ActivitySwabhimanRegisterBinding binding;
	private SwabhimanVM viewModel;
	private String  condition, currently;
	
	public Employement() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(SwabhimanVM.class);
		binding = ActivitySwabhimanRegisterBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
	}
	@Override
	public void onDetach() {
		viewModel.setFragmentOpen(false);
		super.onDetach();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.ivBackButton.setOnClickListener(v->{
			viewModel.getNavController().navigateUp();
		});
		binding.ivHomeButton.setOnClickListener(v->{
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		binding.ivNotifButton.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		//TODO : Submit Details via Email
		binding.condition.setOnClickListener(v -> {
			CustomEmploymentDialog employmentDialog = new CustomEmploymentDialog("Any Conditions?", "If any", text -> {
				condition = text;
				binding.tvCondition.setText(text);
			}, condition);
			employmentDialog.show(getParentFragmentManager(), "EMP");
		});
		binding.currently.setOnClickListener(v -> {
			CustomEmploymentDialog employmentDialog = new CustomEmploymentDialog("Anything Doing currently?", "If any", text -> {
				currently = text;
				binding.tvCurrently.setText(text);
			}, currently);
			employmentDialog.show(getParentFragmentManager(), "EMP");
		});
		binding.btnSubmit.setOnClickListener(v -> {
			String messageBody = "Name : " + binding.name.getEditText().getText().toString() + "\nExperience : " + binding.exp.getEditText().getText().toString()
					+ "\nStrength : " + binding.good.getEditText().getText().toString() + "\nWilling hours : " + binding.hrs.getEditText().getText().toString()
					+ "\nCondition : " + condition + "\nDoing Currently : " + currently;
			SwabhimanAutoMailer.sendSwabhimanMail("Interested in Employment", messageBody, viewModel.getUserMail());
			new AlertDialog.Builder(requireContext()).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
					.setMessage("Your Request has been submitted").setPositiveButton("OK", null)
					.setOnDismissListener(dialog -> {
						viewModel.getNavController().navigateUp();
					}).show();
		});
	}
	
	interface textListener{
		void setText(String text);
	}
	
	public static class CustomEmploymentDialog extends DialogFragment {
		TextView mTitle;
		EditText mDetails;
		TextView mOK;
		String title;
		String hint;
		String previousText;
		private textListener textListener;
		
		public CustomEmploymentDialog(String title, String hint , textListener textListener, @Nullable String previousText){
			this.textListener = textListener;
			this.title = title;
			this.hint = hint;
			if(previousText != null)this.previousText = previousText;
		}
		
		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Nullable
		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.enter_text_dialog, container, false);
			return v;
		}
		
		@Override
		public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			mTitle = view.findViewById(R.id.tv_title);
			mDetails = view.findViewById(R.id.et_detail);
			mOK = view.findViewById(R.id.ok_button);
			mOK.setOnClickListener(v -> dismiss());
			mTitle.setText(title);
			mDetails.setHint(hint);
			if(previousText != null)mDetails.setText(previousText);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			mDetails.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void afterTextChanged(Editable s) {
					textListener.setText(s.toString());
				}
			});
		}
	}
}
