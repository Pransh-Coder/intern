package com.example.intern.payment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.intern.R;
import com.razorpay.BaseRazorpay;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Razorpay;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//TODO: Make package private after integration
public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener{
	//Recycler adapter for net banking
	NetBankingRecyclerAdapter netBankingRecyclerAdapter;
	//Confirms payment codes
	public static int PAYMENT_CONFIRMED = 0;
	public static int PAYMENT_FAILED = -1;
	public static int PAYMENT_CANCELLED = -2;
	Razorpay razorpay;
	WebView mWebView;
	PaymentViewModel paymentViewModel;
	private static final long ANIMATION_DURATION = 70;
	Transition cardInfoSlide = new Slide(Gravity.BOTTOM);
	//Views
	RecyclerView mBankRecyclerView;
	CardView mCardPaymentOption;
	ConstraintLayout mParent;
	CardView mNetBankingOption;
	CardView mBankTransferOption;
	EditText mCVV;
	ConstraintLayout mExpandedCardInfo;
	ConstraintLayout mExpandedNetBankingInfo;
	EditText mCardName;
	EditText mCardNumber;
	EditText mExpiryMonth;
	EditText mExpiryYear;
	//Debugging
	private String TAG = PaymentActivity.class.getSimpleName();
	Button mPayNow;
	Button mPayNowNB;
	//Global booleans
	private boolean isCardOptionsVisible = false;
	//TODO:
	private boolean isNetBankingOptionsVisible = false;
	private boolean isBankTransferOptionVisible = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
		razorpay = new Razorpay(this);
		mWebView = findViewById(R.id.webview_payment_activity);
		mParent = findViewById(R.id.payment_layout_parent);
		mCardPaymentOption = findViewById(R.id.option_card_payment);
		mNetBankingOption = findViewById(R.id.option_net_banking);
		mBankTransferOption = findViewById(R.id.option_bank_transfer);
		mExpandedCardInfo = findViewById(R.id.card_payment_required_expanded);
		mExpandedNetBankingInfo = findViewById(R.id.net_banking_required_expanded);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		paymentViewModel.makeBasePayload(getIntent().getStringExtra("AMOUNT"));
		razorpay.setWebView(mWebView);
		setOnClickListeners();
	}
	
	private void setOnClickListeners(){
		final Context context = this;
		mCardPaymentOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleCardInfo();
			}
		});
		mNetBankingOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleNetbankingInfo();
				razorpay.getPaymentMethods(new BaseRazorpay.PaymentMethodsCallback() {
					@Override
					public void onPaymentMethodsReceived(String s) {
						Log.d(TAG, "onPaymentMethodsReceived");
						populateNetBankRecycler(s);
					}
					@Override
					public void onError(String s) {
						Log.d(TAG, "onError: no internet");
					}
				});
			}
		});
	}
	
	private void populateNetBankRecycler(String s){
		List<String> bankIDs = new ArrayList<>();
		List<String> bankNames = new ArrayList<>();
		try{
			JSONObject entity = new JSONObject(s);
			JSONObject bankList = entity.getJSONObject("netbanking");
			Iterator<String> bankListIterator = bankList.keys();
			while(bankListIterator.hasNext()){
				String key = bankListIterator.next();
				bankIDs.add(key);
				bankNames.add(bankList.getString(key));
			}
		}
		catch (Exception ignored){}
		mBankRecyclerView.setHasFixedSize(true);
		mBankRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		netBankingRecyclerAdapter = new NetBankingRecyclerAdapter(this, bankIDs, bankNames);
		mBankRecyclerView.setAdapter(netBankingRecyclerAdapter);
		netBankingRecyclerAdapter.notifyDataSetChanged();
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
	
	private void toggleNetbankingInfo(){
		//Animates transition
		cardInfoSlide.setDuration(ANIMATION_DURATION);
		cardInfoSlide.addTarget(mExpandedCardInfo).addTarget(mNetBankingOption).addTarget(mBankTransferOption);
		if(isNetBankingOptionsVisible){
			TransitionManager.beginDelayedTransition(mParent, cardInfoSlide);
			mExpandedNetBankingInfo.setVisibility(View.GONE);
			mCardPaymentOption.setVisibility(View.VISIBLE);
			mNetBankingOption.setVisibility(View.VISIBLE);
			mBankTransferOption.setVisibility(View.VISIBLE);
			isNetBankingOptionsVisible = false;
		}else {
			TransitionManager.beginDelayedTransition(mParent, cardInfoSlide);
			mExpandedNetBankingInfo.setVisibility(View.VISIBLE);
			mCardPaymentOption.setVisibility(View.GONE);
			mBankTransferOption.setVisibility(View.GONE);
			isNetBankingOptionsVisible = true;
			//TODO : Find required views
			mPayNowNB = findViewById(R.id.btn_pay_now_net_banking);
			mBankRecyclerView = findViewById(R.id.netbanking_recycler);
			//TODO : Add listeners
			setOnNetBankingPayNowListener();
		}
	}
	
	private void setOnNetBankingPayNowListener(){
		final Context context = this;
		mPayNowNB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String bankID = netBankingRecyclerAdapter.getBankID();
				if(bankID == null){
					Toast.makeText(context, "Select a bank !" , Toast.LENGTH_LONG).show();
					return;
				}
				Log.d(TAG, "onClick: bank ID" + bankID);
				try {
					mWebView.setVisibility(View.VISIBLE);
					razorpay.submit(paymentViewModel.getNetBankingPayload(bankID), PaymentActivity.this);
				} catch (Exception ignored){}
			}
		});
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
					paymentViewModel.makeCardPaymentPayload(name,cardNumber,expiryMonth,expiryYear,cvv);
					razorpay.validateFields(paymentViewModel.getPayload(), new BaseRazorpay.ValidationListener() {
						@Override
						public void onValidationSuccess() {
							mWebView.setVisibility(View.VISIBLE);
							try {
								razorpay.submit(paymentViewModel.getPayload(), PaymentActivity.this);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Log.d(TAG, "onValidationSuccess");
						}
						
						@Override
						public void onValidationError(Map<String, String> map) {
							Log.d(TAG, "onValidationError");
						}
					});
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
/*		mCardNumber.addTextChangedListener(new TextWatcher() {
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
		});*/
	}
	
	@Override
	public void onPaymentSuccess(String s, PaymentData paymentData) {
		///TODO: Confirm data
		Log.d(TAG, "onPaymentSuccess");
		mWebView.setVisibility(View.GONE);
		setResult(PAYMENT_CONFIRMED);
		finish();
	}
	
	@Override
	public void onPaymentError(int i, String s, PaymentData paymentData) {
		Log.d(TAG, "onPaymentError");
		Toast.makeText(this, paymentData.getPaymentId(), Toast.LENGTH_LONG).show();
		setResult(PAYMENT_FAILED);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		if(isCardOptionsVisible){
			toggleCardInfo();
			return;
		} else if(isNetBankingOptionsVisible){
			toggleNetbankingInfo();
			return;
		}
		razorpay.onBackPressed();
		setResult(PAYMENT_CANCELLED);
		finish();
	}
}