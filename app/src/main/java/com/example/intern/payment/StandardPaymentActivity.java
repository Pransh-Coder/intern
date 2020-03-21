package com.example.intern.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.intern.R;
import com.example.intern.databinding.ActivityPaymentConfirmationBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class StandardPaymentActivity extends Activity implements PaymentResultWithDataListener {
	public static String MEMBER_STATUS = "isMember";
	private static String TAG = StandardPaymentActivity.class.getSimpleName();
	ActivityPaymentConfirmationBinding binding;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityPaymentConfirmationBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		setContentView(view);
		Checkout.preload(getApplicationContext());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Listener for cancel button
		binding.btnCancelPayment.setOnClickListener( v-> {onBackPressed();});
		//TODO:Fetch  membership fee from server
		binding.tvMembershipFee.setText("500");
		binding.tvProcessingFee.setText("10");
		binding.tvTotalFee.setText("510");
		binding.btnConfirmPayment.setOnClickListener( v -> {
			doPayment();
		});
	}
	
	private void doPayment(){
		final Activity activity = this;
		final Checkout checkout = new Checkout();
		checkout.setImage(R.drawable.pslogotrimmed);
		try{
			JSONObject options = new JSONObject();
			options.put("name", "Prarambh PVT LTD");
			options.put("description" ,"PS Membership Fee");
			options.put("currency" , "INR");
			options.put("amount" , "51000");
			//TODO:Fetch user data from database
			JSONObject prefill = new JSONObject();
			prefill.put("email" , "usermail@gmail.com");
			prefill.put("contact" , "9999999999");
			//Theme options
			JSONObject theme = new JSONObject();
			theme.put("color" , "#FFEB00");
			
			options.put("prefill" , prefill);
			options.put("theme", theme);
			
			checkout.open(activity, options);
		}catch (Exception e){
			Log.d(TAG, "doPayment: unexpected error");
			Toast.makeText(this, "Something Went Wrong\nPlease Try Again", Toast.LENGTH_LONG).show();
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onPaymentSuccess(String s, PaymentData paymentData) {
		//TODO:Verify signature
		try{
			/*String payment_id = paymentData.getPaymentId();
			String order_id = paymentData.getOrderId();
			String signature = paymentData.getSignature();
			String data = order_id + "|" + payment_id;
			String generatedSignature = SignatureVerifier.calculateRFC2104HMAC(data, SignatureVerifier.key_secret);*/
			/*if(generatedSignature == signature){
				Toast.makeText(this, "Payment Successful" , Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra(MEMBER_STATUS, true);
				setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
				Log.d(TAG, "onPaymentSuccess : Signature verified");
				finish();
			}*/
			Log.d(TAG, "onPaymentSuccess: Payment successful");
			Toast.makeText(this, "Payment Successful" , Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.putExtra(MEMBER_STATUS, true);
			setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
			finish();
		}catch (Exception e){
			Log.d(TAG, "onPaymentSuccess: Unknown error");
			Toast.makeText(this, "Something Went Wrong\nPlease Try Again", Toast.LENGTH_LONG).show();
		}
		Log.d(TAG, "onPaymentSuccess: Signature verification failure");
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onPaymentError(int i, String s, PaymentData paymentData) {
		try{
			Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.putExtra(MEMBER_STATUS, false);
			setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
			finish();
		}catch (Exception e){
			Log.d(TAG, "onPaymentError: Unexpected error");
			Toast.makeText(this, "Something Went Wrong\nPlease Try Again", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.putExtra(MEMBER_STATUS, false);
		setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
		finish();
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy: PaymentActivity is Destroyed");
		super.onDestroy();
	}
	
	static class SignatureVerifier{
		private static String TAG = SignatureVerifier.class.getSimpleName();
		private static String key_secret = "rzp_test_jBHSfE2SDuVJtO";
		private static String HMAC_SHA256_ALGORITHM = "HmacSHA256";
		public static String calculateRFC2104HMAC(String data, String secret)
				throws java.security.SignatureException
		{
			String result;
			try {
				// get an hmac_sha256 key from the raw secret bytes
				SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);
				
				// get an hmac_sha256 Mac instance and initialize with the signing key
				Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
				mac.init(signingKey);
				
				// compute the hmac on input data bytes
				byte[] rawHmac = mac.doFinal(data.getBytes());
				
				// base64-encode the hmac
				result = Base64.encodeToString(rawHmac, Base64.DEFAULT);
				
			} catch (Exception e) {
				throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
			}
			return result;
		}
	}
}
