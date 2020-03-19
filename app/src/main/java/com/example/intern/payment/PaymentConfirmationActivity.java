package com.example.intern.payment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;

class PaymentConfirmationActivity extends AppCompatActivity {
	PaymentViewModel paymentViewModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_confirmation);
	}
}
