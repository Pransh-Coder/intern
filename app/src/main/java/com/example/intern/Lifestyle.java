package com.example.intern;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityScreen11Binding;

public class Lifestyle extends AppCompatActivity {
    ActivityScreen11Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreen11Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_screen_11);
        //getSupportActionBar().hide();
        binding.salonLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.fitnessLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.spaLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.parlorLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
    }
    
    private void showWaitDialog(){
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}
