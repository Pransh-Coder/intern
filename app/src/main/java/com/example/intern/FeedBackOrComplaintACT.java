package com.example.intern;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.mainapp.MainApp;

public class FeedBackOrComplaintACT extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_or_complaint);
        findViewById(R.id.iv_back_button).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.iv_home_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
    }
}
