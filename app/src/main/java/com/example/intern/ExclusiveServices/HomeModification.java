package com.example.intern.ExclusiveServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class HomeModification extends AppCompatActivity {

    ImageView back,home_btn;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_modification);

        back = findViewById(R.id.back);
        home_btn = findViewById(R.id.home_btn);
        submit=findViewById(R.id.submithm);

        back.setOnClickListener(view -> onBackPressed());

        home_btn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeModification.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        final Context context = this;
        submit.setOnClickListener(v -> {
            //TODO :
            new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
                    .setMessage("We will get back to you shortly").setPositiveButton("OK", null)
                    .setOnDismissListener(dialog -> onBackPressed()).show();
        });
    }
}
