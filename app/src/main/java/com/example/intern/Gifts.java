package com.example.intern;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.ExclusiveServices.DoctorOnline;
import com.example.intern.databinding.ActivityScreen12Binding;
import com.example.intern.mainapp.MainApp;

public class Gifts extends AppCompatActivity {

    ActivityScreen12Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreen12Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent services = new Intent(getApplicationContext(), GiftDetailsActivity.class);
                startActivity(services);*/
                showWaitDialog();
            }
        });
        binding.imageView8.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.imageView7.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.imageView9.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.notifi.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });

    }

    private void showWaitDialog() {
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}
