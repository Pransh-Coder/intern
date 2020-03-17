package com.example.intern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.razorpay.BaseRazorpay;
import com.razorpay.PaymentResultListener;
import com.razorpay.Razorpay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PaymentActivity extends Activity implements PaymentResultListener{
	
	private static String TAG = PaymentActivity.class.getSimpleName();
	
	EditText mAmount;
	EditText mName;
	EditText mNumber;
	EditText mExpMonth;
	EditText mExpYear;
	EditText mCVV;
	WebView mWebView;
	Button mButton;
	TextView mOrderID;
	String pay_id;
	String order_id;
	String signature;
	Razorpay razorpay;
	Spinner mBankListSpinner;
	Context context = this;
	ArrayAdapter adapter;
	JSONObject payload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mAmount = findViewById(R.id.et_amount);
		mCVV = findViewById(R.id.et_cvv);
		mExpMonth = findViewById(R.id.et_exp_month);
		mExpYear  =findViewById(R.id.et_exp_year);
		mName = findViewById(R.id.et_card_name);
		mNumber = findViewById(R.id.et_card_number);
		mWebView = findViewById(R.id.web_view);
		mButton = findViewById(R.id.btn_card_payment);
		mOrderID = findViewById(R.id.tv_test);
		mBankListSpinner = findViewById(R.id.bank_list);
		initRazorPay();
	}
	
	private void initRazorPay(){
		razorpay = new Razorpay(this);
		razorpay.getPaymentMethods(new BaseRazorpay.PaymentMethodsCallback() {
			@Override
			public void onPaymentMethodsReceived(String s) {
				Log.d(TAG, "onPaymentMethodsReceived: started json fetch");
				adapter = new ArrayAdapter(context, android.R.layout.simple_expandable_list_item_1, PaymentActivityData.getBankListFromJSON(s));
				adapter.notifyDataSetChanged();
				Log.d(TAG, "onPaymentMethodsReceived: adapter data chnaged");
			}
			
			@Override
			public void onError(String s) {
				Log.d(TAG, "onError: cannot connect to internet");
				adapter.clear();
			}
		});
		mBankListSpinner.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		razorpay.setWebView(mWebView);
		//Set these values from user credentials and purchase details
		try{
			payload = new JSONObject();
			payload.put("amount", "100");
			payload.put("currency", "INR");
			payload.put("contact" , "9958221386");
			payload.put("email", "jhkjh@gmail.com");
		}catch (JSONException ignored){}
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submitCardDetails();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "onBackPressed: payment cancelled");
	}
	
	private void submitCardDetails(){
		String name = mName.getText().toString();
		String month = mExpMonth.getText().toString();
		String year = mExpYear.getText().toString();
		String number = mNumber.getText().toString();
		String cvv = mCVV.getText().toString();
		try{
			payload.put("method", "card");
			payload.put("card[name]" , name);
			payload.put("card[number]" , number);
			payload.put("card[expiry_month]" , month);
			payload.put("card[expiry_year]" , year);
			payload.put("card[cvv]" , cvv);
		}catch (Exception ignored){}
		finally {
			initiatePayment();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		razorpay.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initiatePayment(){
		razorpay.validateFields(payload, new BaseRazorpay.ValidationListener() {
			@Override
			public void onValidationSuccess() {
				Log.d(TAG, "onValidationSuccess: ");
				try{
					mWebView.setVisibility(View.VISIBLE);
					razorpay.submit(payload, PaymentActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onValidationError(Map<String, String> map) {
				Log.d(TAG, "onValidationError: ");
			}
		});
	}
	
	@Override
	public void onPaymentSuccess(String s) {
		Log.d(TAG, "onPaymentSuccess: ");
	}
	
	@Override
	public void onPaymentError(int i, String s) {
		Log.d(TAG, "onPaymentError: ");
	}
}
