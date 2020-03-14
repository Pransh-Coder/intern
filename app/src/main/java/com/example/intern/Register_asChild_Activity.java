package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import save_money.SaveMoney;

public class Register_asChild_Activity extends AppCompatActivity {

    Button child_SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_child);
        getSupportActionBar().hide();

        child_SignIn= findViewById(R.id.btnRegisterasChild_SignIn);

        child_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_asChild_Activity.this, SaveMoney.class);
                startActivity(intent);
            }
        });
    }
}
