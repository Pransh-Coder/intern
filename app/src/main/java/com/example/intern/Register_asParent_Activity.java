package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import save_money.SaveMoney;

public class Register_asParent_Activity extends AppCompatActivity {

    Button parent_SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_parent);
        getSupportActionBar().hide();

        parent_SignIn = findViewById(R.id.btnRegisterasChild_SignIn);

        parent_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_asParent_Activity.this, SaveMoney.class);
                startActivity(intent);
            }
        });
    }
}
