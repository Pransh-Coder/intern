package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Login_Register extends AppCompatActivity {

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //getSupportActionBar().hide();
        btnLogin = findViewById(R.id.login_button);
        btnRegister = findViewById(R.id.registration_button);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegistrationChoice.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(registerIntent);
                finish();


            }
        });
    }
}
