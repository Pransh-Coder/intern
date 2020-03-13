package com.example.intern;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.databinding.ActivityMainBinding;
import com.example.intern.viewmodels.RegistrationModel;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private RegistrationModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegistrationModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Find the NavController and set it to ViewModel on creation of application
        
        NavController appFlowController = Navigation.findNavController(this, R.id.main_nav_host);
        viewModel.setAppFlowController(appFlowController);
        Log.d(TAG, "onCreate: completed");
    }
}
