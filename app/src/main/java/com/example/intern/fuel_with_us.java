package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fuel_with_us extends AppCompatActivity {
    EditText amount;
    TextView tv_qty;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_with_us);
        getSupportActionBar().hide();
        tv_qty = findViewById(R.id.tv_petrol_qty);
        amount = findViewById(R.id.et_amt);
        submit = findViewById(R.id.submit_petrol);
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()!=0){
                    int amt = Integer.parseInt(s.toString());
                    float qty = (float) amt / 70;
                    tv_qty.setText("Petrol quantity is "+qty);
                }
                else{
                    tv_qty.setText("Petrol quantity is 0");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = findViewById(R.id.et_amt);
                EditText e2 = findViewById(R.id.et_invoice);
                if(e1.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Please enter the amount ",Toast.LENGTH_SHORT).show();
                }
                else if(e2.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Please enter invoice number",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Intent object where you want to submit
                }
            }
        });
    }
}
