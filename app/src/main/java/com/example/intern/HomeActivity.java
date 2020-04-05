package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import save_money.SaveMoney;

public class HomeActivity extends AppCompatActivity {

    LinearLayout save_money;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        save_money = findViewById(R.id.save_money);

        save_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saveMoneyIntent = new Intent(getApplicationContext(), SaveMoney.class);
                startActivity(saveMoneyIntent);
            }
        });
    }
}
