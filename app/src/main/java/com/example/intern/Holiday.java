package com.example.intern;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.mainapp.MainApp;

public class Holiday extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_14);
        //getSupportActionBar().hide();
        findViewById(R.id.iv_stay_at_resort_image).setOnClickListener(v -> showWaitDialog());
        findViewById(R.id.iv_back_button).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.iv_home_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
    }
    private void showWaitDialog(){
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}