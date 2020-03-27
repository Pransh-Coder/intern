package com.example.intern.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
	private static String TAG = AuthActivity.class.getSimpleName();
	private ActivityAuthBinding binding;
	private AuthViewModel viewModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAuthBinding.inflate(getLayoutInflater());
		viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
		setContentView(binding.getRoot());
		//TODO:Create splash screen
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}
}
