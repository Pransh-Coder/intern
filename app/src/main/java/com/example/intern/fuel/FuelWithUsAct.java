package com.example.intern.fuel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.mainapp.MainApp;

public class FuelWithUsAct extends AppCompatActivity {
	EditText amount;
	TextView tv_qty;
	Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuel_with_us);
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
		submit.setOnClickListener(v -> {
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
		});
		findViewById(R.id.iv_back_button).setOnClickListener(v->{
			onBackPressed();
			finish();
		});
		findViewById(R.id.iv_home_button).setOnClickListener(v->{
			Intent intent = new Intent(this, MainApp.class);
			startActivity(intent);
			finish();
		});
	}
}
