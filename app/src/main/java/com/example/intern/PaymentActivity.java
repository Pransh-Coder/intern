package com.example.intern;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

public class PaymentActivity extends AppCompatActivity {
	//Animation
	private static final long ANIMATION_DURATION = 100;
	Transition cardInfoSlide = new Slide(Gravity.BOTTOM);
	//ViewModel
	PaymentViewModel paymentViewModel;
	//Views
	CardView mCardPaymentOption;
	NestedScrollView mParent;
	CardView mNetBankingOption;
	CardView mBankTransferOption;
	EditText mCVV;
	ConstraintLayout mExpandedCardInfo;
	EditText mCardName;
	EditText mCardNumber;
	EditText mExpiryMonth;
	EditText mExpiryYear;
	//Debugging
	private String TAG = PaymentActivity.class.getSimpleName();
	Button mPayNow;
	//Global booleans
	private boolean isCardOptionsVisible = false;
	private boolean isNetBankingOptionsVisible = false;
	private boolean isBankTransferOptionVisible = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mParent = findViewById(R.id.payment_layout_parent);
		mCardPaymentOption = findViewById(R.id.option_card_payment);
		mNetBankingOption = findViewById(R.id.option_net_banking);
		mBankTransferOption = findViewById(R.id.option_bank_transfer);
		mExpandedCardInfo = findViewById(R.id.card_payment_required_expanded);
		setOnClickListeners();
	}
	
	private void setOnClickListeners(){
		mCardPaymentOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleCardInfo();
			}
		});
	}
	
	private void toggleCardInfo(){
		//Animates transition
		cardInfoSlide.setDuration(ANIMATION_DURATION);
		cardInfoSlide.addTarget(mExpandedCardInfo).addTarget(mNetBankingOption).addTarget(mBankTransferOption);
		if(isCardOptionsVisible){
			TransitionManager.beginDelayedTransition(mParent, cardInfoSlide);
			mExpandedCardInfo.setVisibility(View.GONE);
			mNetBankingOption.setVisibility(View.VISIBLE);
			mBankTransferOption.setVisibility(View.VISIBLE);
			isCardOptionsVisible = false;
		}else {
			TransitionManager.beginDelayedTransition(mParent, cardInfoSlide);
			mExpandedCardInfo.setVisibility(View.VISIBLE);
			mNetBankingOption.setVisibility(View.GONE);
			mBankTransferOption.setVisibility(View.GONE);
			isCardOptionsVisible = true;
			//Find required views
			mCardName = findViewById(R.id.et_name_on_card);
			mCardNumber = findViewById(R.id.et_card_number);
			mExpiryMonth = findViewById(R.id.et_expiry_month);
			mExpiryYear = findViewById(R.id.et_expiry_year);
			mCVV = findViewById(R.id.et_cvv);
			mPayNow = findViewById(R.id.btn_pay_now);
			//Add polish to found views
			applyTextFilters();
			//Add Pay Button Listener
			setOnCardPayListener();
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
				//Naive Verify Fields
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
					//TODO: forward intent to confirmation activity
				}
			}
		});
	}
	
	private void applyTextFilters(){
		//For Month
		mExpiryMonth.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() == 2){
					mExpiryMonth.clearFocus();
					mExpiryYear.requestFocus();
					mExpiryYear.setCursorVisible(true);
				}
			}
		});
		//For Year
		mExpiryYear.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
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
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if (lock || s.length() > 16) return;
				lock = true;
				for (int i = 4; i < s.length(); i += 5) {
					if (s.toString().charAt(i) != ' ') s.insert(i, " ");
				}
				lock = false;
				Log.d(TAG, "afterTextChanged: " + s.toString());
			}
		});
	}
}