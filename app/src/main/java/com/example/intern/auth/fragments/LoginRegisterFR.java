package com.example.intern.auth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.intern.R;
import com.example.intern.auth.ViewPagerAdapter_for_Login_Register;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentLoginRegisterFRBinding;

import save_money.ViewPagerAdapter;

public class LoginRegisterFR extends Fragment {
	private FragmentLoginRegisterFRBinding binding;
	private AuthViewModel viewModel;

	public LoginRegisterFR() {
		// Required empty public constructor
	}

	//ViewPager
	ViewPager viewPager;
	TextView chngText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
		binding = FragmentLoginRegisterFRBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		//ViewPager
		viewPager = view.findViewById(R.id.viewpager_fr);
		chngText = view.findViewById(R.id.chngText);

		//Initialise ViewPager Adapter
		ViewPagerAdapter_for_Login_Register viewPagerAdapter = new ViewPagerAdapter_for_Login_Register(getContext());
		viewPager.setAdapter(viewPagerAdapter);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				switch (position){
					case 0:
						//Do stuff
						chngText.setText("Samay");
						break;
					case 1:
						//Do stuff
						chngText.setText("Prakruti");
						break;
					//Add other cases for the pages

				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}

		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		//TODO: make a login FR too
		binding.Signup.setOnClickListener(v->{
			Navigation.findNavController(v).navigate(R.id.action_loginRegisterFR_to_registrationChoiceFR);
		});
		binding.loginButton.setOnClickListener(v -> {
					Navigation.findNavController(v).navigate(R.id.action_loginRegisterFR_to_LoginOptionFR);
				}
		);
	}
}
