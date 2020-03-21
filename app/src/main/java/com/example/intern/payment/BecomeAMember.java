package com.example.intern.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityBecomeMemberBinding;

public class BecomeAMember extends AppCompatActivity {
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
		if(binding.btnBecomeMember != null){
			binding.btnBecomeMember.setOnClickListener( v -> {
				Intent intent =  new Intent(BecomeAMember.this, StandardPaymentActivity.class);
				startActivityForResult(intent, BECOME_MEMBER_REQUEST_CODE);
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == BECOME_MEMBER_REQUEST_CODE){
			//TODO:Check whether user is still a member or not
			isMember = data.getBooleanExtra(StandardPaymentActivity.MEMBER_STATUS , false);
			updateUI();
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
}
