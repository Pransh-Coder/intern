/*
package com.example.intern.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.razorpay.Razorpay;

public class PaymentConfirmationActivity extends AppCompatActivity{
	//Request codes
	public static String AMOUNT_TO_BE_DEDUCTED;
	public static int REQUEST_PAYMENT = 1;
	public static int CANCEL_PAYMENT = -1;
	private static String TAG = PaymentConfirmationActivity.class.getSimpleName();
	Button mConfirm;
	Button mCancel;
	//Fee data
	TextView mBaseFee;
	TextView mProcessingFee;
	TextView mTotalFee;
	Razorpay razorpay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		razorpay = new Razorpay(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_confirmation);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mBaseFee = findViewById(R.id.tv_membership_fee);
		mProcessingFee = findViewById(R.id.tv_processing_fee);
		mTotalFee = findViewById(R.id.tv_total_fee);
		showPaymentData();
		setOnClickListeners();
	}
	
	private void showPaymentData(){
		//TODO: Fetch payment charges and fee
		double baseFee = 500;
		double chargeFee = (0.02*baseFee);
		double totalFee = (baseFee + chargeFee);
		double deduction_amount = totalFee * 100; //in paise
		mBaseFee.setText(Long.toString((long)baseFee));
		mProcessingFee.setText(Long.toString((long)chargeFee));
		mTotalFee.setText(Long.toString((long)totalFee));
		AMOUNT_TO_BE_DEDUCTED = Long.toString((long)deduction_amount);
		Log.d(TAG, "showPaymentData: " + AMOUNT_TO_BE_DEDUCTED);
	}
	
	private void setOnClickListeners(){
		mCancel = findViewById(R.id.btn_cancel_payment);
		mConfirm = findViewById(R.id.btn_confirm_payment);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(CANCEL_PAYMENT);
				finish();
			}
		});
		mConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: Forward to payment activity
				Log.d(TAG, "onClick: Confirmed");
				Intent intent = new Intent(PaymentConfirmationActivity.this, PaymentActivity.class);
				intent.putExtra("AMOUNT" , AMOUNT_TO_BE_DEDUCTED);
				startActivityForResult(intent, REQUEST_PAYMENT);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == PaymentActivity.PAYMENT_CONFIRMED){
			//TODO : Payment is confirmed .. return as a member
			Log.d(TAG, "onActivityResult success");
			Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
			setResult(resultCode);
			finish();
		}else if(resultCode == PaymentActivity.PAYMENT_FAILED){
			//TODO : Payment failed due to some reason ... return as not a member
			Log.d(TAG, "onActivityResult fail");
			Toast.makeText(this, "FAILURE", Toast.LENGTH_LONG).show();
			setResult(resultCode);
			finish();
		}else if(resultCode == PaymentActivity.PAYMENT_CANCELLED){
			Log.d(TAG, "onActivityResult: cancelled");
			Toast.makeText(this, "CANCELLED", Toast.LENGTH_LONG).show();
			setResult(resultCode);
		}
	}
}
*/
