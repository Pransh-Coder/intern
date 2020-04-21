package com.example.intern;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.mainapp.MainApp;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        findViewById(R.id.food_imageView1).setOnClickListener(v -> {
            showWaitDialog();
        });
        findViewById(R.id.food_button_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.food_home_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
        findViewById(R.id.food_button_notification).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    private void showWaitDialog(){
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}
