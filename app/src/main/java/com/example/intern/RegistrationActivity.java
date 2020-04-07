package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    Spinner registration_SpinnerLanguages;
    TextView registration_tv_AsChild, registration_tv_AsParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // getSupportActionBar().hide();
        registration_SpinnerLanguages = findViewById(R.id.registration_spin_Language);

        registration_tv_AsChild = findViewById(R.id.registration_tv_asChild);
        registration_tv_AsParent = findViewById(R.id.registration_tv_asParent);

        registration_SpinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    String selected = adapterView.getItemAtPosition(position).toString();
                    Toast.makeText(RegistrationActivity.this, "Selected " + selected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                registration_SpinnerLanguages.setSelection(0);
            }
        });

        registration_tv_AsChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerasChild_Intent = new Intent(getApplicationContext(), Register_asChild_Activity.class);
                startActivity(registerasChild_Intent);
            }
        });

        registration_tv_AsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerasParent_Intent = new Intent(getApplicationContext(), Register_asParent_Activity.class);
                startActivity(registerasParent_Intent);
            }
        });
    }
}
