package com.example.intern;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class swabhiman_tc extends AppCompatActivity {
    CheckBox cb;
    boolean f = false;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swabhiman_tc);
        getSupportActionBar().hide();
        cb = findViewById(R.id.checkbox);
        bt = findViewById(R.id.submit);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked()) {
                    f = true;
                } else {
                    f = false;
                }
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f) {
                    Toast.makeText(swabhiman_tc.this, "Hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
