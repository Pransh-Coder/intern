package com.example.intern.auth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.intern.R;
import com.example.intern.auth.ViewPagerAdapter_for_Login_Register;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentLoginRegisterFRBinding;

import java.util.Timer;
import java.util.TimerTask;

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
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                } else if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("inside", "Timmer2 ");
                handler.post(Update);
            }
        }, 1000,  3000);
		String currentLocale = requireActivity().getSharedPreferences("lang", Context.MODE_PRIVATE).getString("lang", null);
		if(currentLocale != null){
			if(currentLocale.equals("en"))binding.registrationSpinLanguage.setSelection(0);
			if(currentLocale.contains("hi"))binding.registrationSpinLanguage.setSelection(1);
			if(currentLocale.contains("gu"))binding.registrationSpinLanguage.setSelection(2);
		}
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
	
	@Override
	public void onResume() {
		super.onResume();
		binding.registrationSpinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position==0){
					viewModel.getLanguageListener().newLang("en");
				}
				if(position==1){
					viewModel.getLanguageListener().newLang("hi");
				}
				if(position ==2){
					viewModel.getLanguageListener().newLang("gu");
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			
			}
		});
	}
}
