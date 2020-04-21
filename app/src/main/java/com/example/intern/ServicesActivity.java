package com.example.intern;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityServicesBinding;
import com.example.intern.mainapp.MainApp;

import save_money.SaveMoney;

public class ServicesActivity extends AppCompatActivity {
    ActivityServicesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.linearLayoutMobileRepair.setOnClickListener(v -> showWaitDialog());
        binding.linearLayoutRoRepair.setOnClickListener(v -> showWaitDialog());
        binding.linearLayoutBikeRepair.setOnClickListener(v -> showMyDialog());
        binding.servicesButtonBack.setOnClickListener(v -> {
            Intent intent = new Intent(ServicesActivity.this, SaveMoney.class);
            startActivity(intent);

        });
        binding.servicesButtonHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
    }
    private void showWaitDialog(){
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
    private void showMyDialog(){
        startActivity(new Intent(ServicesActivity.this,two_wheeler_repair_list.class));
    }
}
