package com.example.intern.mainapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.databinding.FragmentHomeBinding;
import com.example.intern.mainapp.MainAppViewModel;

public class HomeFragment extends Fragment {
	private FragmentHomeBinding binding;
	private MainAppViewModel viewModel;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		viewModel = new ViewModelProvider(requireActivity()).get(MainAppViewModel.class);
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		binding.drawerPinHome.setOnClickListener(v->{
			if(viewModel.drawerLayout.isDrawerOpen(GravityCompat.START)){
				viewModel.drawerLayout.closeDrawer(GravityCompat.START);
			}else{
				viewModel.drawerLayout.openDrawer(GravityCompat.START);
			}
		});
	}
}
