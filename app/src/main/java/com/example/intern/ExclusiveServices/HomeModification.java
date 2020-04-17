package com.example.intern.ExclusiveServices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.HomeActivity;
import com.example.intern.R;

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeModification.this,ExclusiveServices.class);
                startActivity(intent);
                finish();
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeModification.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeModification.this,"Will get back to you Shortly!!",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(HomeModification.this, ExclusiveServices.class));
        finish();

    }
}
