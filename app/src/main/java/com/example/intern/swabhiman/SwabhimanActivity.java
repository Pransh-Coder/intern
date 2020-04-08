package com.example.intern.swabhiman;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.databinding.ActivitySwabhimanBinding;

public class SwabhimanActivity extends AppCompatActivity {
	private ActivitySwabhimanBinding binding;
	private SwabhimanVM viewModel;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySwabhimanBinding.inflate(getLayoutInflater());
		viewModel = new ViewModelProvider(this).get(SwabhimanVM.class);
		setContentView(binding.getRoot());
		NavController navController = Navigation.findNavController(this, R.id.swabhiman_nav_host);
		viewModel.setNavController(navController);
	}
}