package com.example.intern.payment;

/*import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityBecomeMemberBinding;
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
			//TODO:Check whether user is still a member or not
			isMember = data.getBooleanExtra(MEMBER_STATUS , false);
			String payID = data.getStringExtra(PAY_ID_TAG);
			if(payID != null){
				startWebAuth(payID);
			}else {
				Log.d(TAG, "onActivityResult: something went wrong");
			}
		}
	}
	
	private void updateUI(){
		if(isMember){
			binding.frameNotAMember.setVisibility(View.GONE);
			binding.frameMember.setVisibility(View.VISIBLE);
			binding.btnGreat.setOnClickListener( v -> {
				//TODO:Send back with member privileges
				finish();
			});
		}else{
			Toast.makeText(this, "Please complete Payment" , Toast.LENGTH_SHORT).show();
		}
	}
	
	private void startWebAuth(String paymentID){
		RazorPayAPI razorPayAPI = RazorPayAuthAPI.getRetrofit().create(RazorPayAPI.class);
		Call<PaymentEntity> call = razorPayAPI.paymentInfo(paymentID);
		Log.d(TAG, "startWebAuth: URL called" + call.request().url());
		Callback<PaymentEntity> callback = new Callback<PaymentEntity>() {
			@Override
			public void onResponse(Call<PaymentEntity> call, Response<PaymentEntity> response) {
				if(response.body().getStatus().equals("authorized")){
					Log.d(TAG, "onResponse: Payment authorized");
					Intent intent = new Intent();
					intent.putExtra(MEMBER_STATUS, true);
					setResult(BecomeAMember.PAYMENT_ACTIVITY_RESULT_CODE, intent);
					updateUI();
				}
			}
			@Override
			public void onFailure(Call<PaymentEntity> call, Throwable t) {
				Log.d(TAG, "onFailure: call failure");
			}
		};
		call.enqueue(callback);
	}
}

 */

