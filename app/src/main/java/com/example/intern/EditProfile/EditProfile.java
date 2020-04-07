package com.example.intern.EditProfile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityEditProfileBinding;

public class EditProfile extends AppCompatActivity {
    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
    }
}
