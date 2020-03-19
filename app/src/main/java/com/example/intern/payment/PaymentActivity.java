/*package com.example.intern;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.razorpay.Razorpay;

import org.json.JSONObject;


public class PaymentActivity extends Activity {
	CardView mCardPaymentOption;
	CardView mNetBankingOption;
	CardView mBankTransferOption;
	ConstraintLayout mExpandedCardInfo;
	EditText mCardName;
	EditText mCardNumber;
	EditText mExpiryMonth;
	EditText mExpiryYear;
	Button mPayNow;
	private String TAG = PaymentActivity.class.getSimpleName();
	private boolean isCardOptionsVisible = false;
	private boolean isNetBankingOptionsVisible = false;
	EditText mCVV;
	private boolean isBankTransferOptionVisible = false;
	Razorpay razorpay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mCardPaymentOption = findViewById(R.id.option_card_payment);
		mNetBankingOption = findViewById(R.id.option_net_banking);
		mBankTransferOption = findViewById(R.id.option_bank_transfer);
		mExpandedCardInfo = findViewById(R.id.card_payment_required_expanded);
		setOnClickListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//initialize RazorPay and user payload
		razorpay = new Razorpay(this);
	}
	
	private void setOnClickListeners(){
		mCardPaymentOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleCardInfo();
			}
		});
	}
	
	private void doCardPayment(String cardName, String cardNumber, String expMonth, String expYear, String cvv){
		//TODO: make payment using card
		try{
			JSONObject payload = new JSONObject();
			//Amount need to be calculated
			payload.put("amount" , "100");
			payload.put("currency", "INR");
		}catch (Exception e){
			Log.d(TAG, "doCardPayment: JSON cannot be made");
		}
	}
	
	private void toggleCardInfo(){
		if(isCardOptionsVisible){
			mExpandedCardInfo.setVisibility(View.GONE);
			mNetBankingOption.setVisibility(View.VISIBLE);
			mBankTransferOption.setVisibility(View.VISIBLE);
			isCardOptionsVisible = false;
		}else {
			mExpandedCardInfo.setVisibility(View.VISIBLE);
			mCardName = findViewById(R.id.et_name_on_card);
			mCardNumber = findViewById(R.id.et_card_number);
			mExpiryMonth = findViewById(R.id.et_expiry_month);
			mExpiryYear = findViewById(R.id.et_expiry_year);
			mCVV = findViewById(R.id.et_cvv);
			mPayNow = findViewById(R.id.btn_pay_now);
			setOnCardPayListener();
			applyTextFilters();
			mNetBankingOption.setVisibility(View.GONE);
			mBankTransferOption.setVisibility(View.GONE);
			isCardOptionsVisible = true;
		}
	}
	
	private void setOnCardPayListener(){
		mPayNow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = mCardName.getText().toString();
				String cardNumber = mCardNumber.getText().toString().trim();
				String expiryMonth = mExpiryMonth.getText().toString();
				String expiryYear = mExpiryYear.getText().toString();
				String cvv = mCVV.getText().toString();
				if(name.isEmpty()){
					mCardName.setError("Name cannot be empty");
				}else if(name.matches(".*\\d.*")){
					mCardName.setError("Name cannot have digits");
				}else if(cardNumber.length() < 16){
					mCardNumber.setError("Invalid Card number");
				}else if(expiryMonth.length() == 0 || expiryMonth.length() == 1  ){
					mExpiryMonth.setError("Enter expiry month");
				} else if(expiryYear.length() == 0  || expiryYear.length() == 1){
					mExpiryYear.setError("Enter expiry year");
				} else if(Integer.parseInt(expiryMonth) > 12 ){
					mExpiryMonth.setError("Invalid expiry month");
				}else if(Integer.parseInt(expiryYear) < 20){
					mExpiryYear.setError("Invalid expiry year");
				} else if(cvv.length() < 3){
					mCVV.setError("Invalid CVV");
				}else {
					doCardPayment(name, cardNumber, expiryMonth, expiryYear, cvv);
				}
			}
		});
	}
	
	private void applyTextFilters(){
		//For date
		mExpiryMonth.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() == 2){
					mExpiryMonth.clearFocus();
					mExpiryYear.requestFocus();
					mExpiryYear.setCursorVisible(true);
				}
			}
		});
		
		mExpiryYear.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() == 0){
					mExpiryYear.clearFocus();
					mExpiryMonth.requestFocus();
					mExpiryMonth.setCursorVisible(true);
				}
			}
		});
		
		//For the Credit Card Number Formatting
		mCardNumber.addTextChangedListener(new TextWatcher() {
			private boolean lock;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (lock || s.length() > 16) {
					return;
				}
				lock = true;
				for (int i = 4; i < s.length(); i += 5) {
					if (s.toString().charAt(i) != ' ') {
						s.insert(i, " ");
					}
				}
				lock = false;
				Log.d(TAG, "afterTextChanged: " + s.toString());
			}
		});
	}
	
}*/
