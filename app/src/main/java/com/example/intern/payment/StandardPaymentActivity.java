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
import com.example.intern.database.SharedPrefUtil;
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
		//TODO: Set key ID for razor pay
		checkout.setKeyID("rzp_test_9SFxBSOfMFPxyk");
		checkout.setImage(R.drawable.pslogotrimmed);
		try{
			JSONObject options = new JSONObject();
			options.put("name", "Prarambh PVT LTD");
			options.put("description" ,"PS Membership Fee");
			options.put("currency" , "INR");
			options.put("amount" , "51000");
			SharedPrefUtil prefUtil = new SharedPrefUtil(this);
			String email = prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY,null);
			String contact = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PHONE_NO, null);
			JSONObject prefill = new JSONObject();
			prefill.put("email" ,email);
			prefill.put("contact" , contact);
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
			String payment_id = paymentData.getPaymentId();
			String order_id = paymentData.getOrderId();
			String signature = paymentData.getSignature();
			String data = order_id + "|" + payment_id ;
			String generatedSignature = SignatureVerifier.calculateRFC2104HMAC(data, SignatureVerifier.key_secret);
			if(generatedSignature.equals(signature)){
				Toast.makeText(this, "Payment Successful" , Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra(MEMBER_STATUS, true);
				setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
				Log.d(TAG, "onPaymentSuccess : Signature verified");
				finish();
			}else {
				Log.d(TAG, "onPaymentSuccess: client side signature verification failed");
				Intent intent = new Intent();
				intent.putExtra(MEMBER_STATUS, true);
				intent.putExtra(BecomeAMember.PAY_ID_TAG, paymentData.getPaymentId());
				setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
				finish();
			}
		}catch (Exception e){
			Log.d(TAG, "onPaymentSuccess: Unknown error");
			Toast.makeText(this, "Something Went Wrong\nPlease Try Again", Toast.LENGTH_LONG).show();
		}
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
	
	static class SignatureVerifier{
		private static String TAG = SignatureVerifier.class.getSimpleName();
		private static String key_secret = "Qi9f1wEd1SSTeEprS64810cZ";
		
		public static String calculateRFC2104HMAC(String data, String secret)
				throws java.security.SignatureException
		{
			String result;
			try {
				// get an hmac_sha256 key from the raw secret bytes
				String HMAC_SHA256_ALGORITHM = "HmacSHA256";
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
