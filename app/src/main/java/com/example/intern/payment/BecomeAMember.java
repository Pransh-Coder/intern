package com.example.intern.payment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityBecomeMemberBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.payment.auth.PaymentEntity;
import com.example.intern.payment.auth.RazorPayAPI;
import com.example.intern.payment.auth.RazorPayAuthAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.intern.payment.StandardPaymentActivity.MEMBER_STATUS;

public class BecomeAMember extends AppCompatActivity {
	public static String PAY_ID_TAG = "pay_id";
	private static String TAG = BecomeAMember.class.getSimpleName();
	public static int PAYMENT_ACTIVITY_RESULT_CODE = 2;
	ActivityBecomeMemberBinding binding;
	private int BECOME_MEMBER_REQUEST_CODE = 1;
	public static int IS_A_MEMBER_RESULT_CODE = 11;
	private static String payID;
	private boolean isMember = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityBecomeMemberBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		setContentView(view);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		binding.btnBecomeMember.setOnClickListener( v -> {
			Intent intent =  new Intent(BecomeAMember.this, StandardPaymentActivity.class);
			startActivityForResult(intent, BECOME_MEMBER_REQUEST_CODE);
		});
	}
	
	@Override
	
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == BECOME_MEMBER_REQUEST_CODE){
			assert data != null;
			isMember = data.getBooleanExtra(MEMBER_STATUS , false);
			 payID = data.getStringExtra(PAY_ID_TAG);
			if(payID != null){
				startWebAuth(payID);
			}else {
				Log.d(TAG, "onActivityResult: something went wrong");
			}
		}
	}
	
	private void startWebAuth(String paymentID){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Verifying");
		dialog.setCancelable(false);
		RazorPayAPI razorPayAPI = RazorPayAuthAPI.getRetrofit().create(RazorPayAPI.class);
		Call<PaymentEntity> call = razorPayAPI.paymentInfo(paymentID);
		Log.d(TAG, "startWebAuth: URL called" + call.request().url());
		Callback<PaymentEntity> callback = new Callback<PaymentEntity>() {
			@Override
			public void onResponse(Call<PaymentEntity> call, Response<PaymentEntity> response) {
				if(response.body().getStatus().equals("authorized")){
					Log.d(TAG, "onResponse: Payment authorized");
					isMember = true;
					dialog.hide();
					updateUI();
				}
			}
			@Override
			public void onFailure(Call<PaymentEntity> call, Throwable t) {
				dialog.hide();
				Log.d(TAG, "onFailure: call failure");
			}
		};
		call.enqueue(callback);
	}
	
	private void updateUI(){
		if(isMember){
			binding.frameNotAMember.setVisibility(View.GONE);
			binding.frameMember.setVisibility(View.VISIBLE);
			binding.btnGreat.setOnClickListener( v -> {
				finishPaymentProcess();
			});
		}else{
			Toast.makeText(this, "Please complete Payment" , Toast.LENGTH_SHORT).show();
		}
	}
	
	private void finishPaymentProcess(){
		FireStoreUtil.uploadPayID(this, payID);
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		prefUtil.setUserPayID(payID);
		Intent intent = new Intent();
		intent.putExtra(PAY_ID_TAG, payID);
		setResult(IS_A_MEMBER_RESULT_CODE, intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		if(payID != null){
			finishPaymentProcess();
		}else{
			Toast.makeText(this, "Payment not complete", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), MainApp.class);
			startActivity(intent);
			finish();
		}
	}
}



