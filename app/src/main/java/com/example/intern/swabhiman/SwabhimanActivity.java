package com.example.intern.swabhiman;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivitySwabhimanBinding;
import com.razorpay.PaymentResultListener;

public class SwabhimanActivity extends AppCompatActivity implements PaymentResultListener {
	private ActivitySwabhimanBinding binding;
	private SwabhimanVM viewModel;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySwabhimanBinding.inflate(getLayoutInflater());
		viewModel = new ViewModelProvider(this).get(SwabhimanVM.class);
		setContentView(binding.getRoot());
		NavController navController = Navigation.findNavController(this, R.id.swabhiman_nav_host);
		viewModel.setNavController(navController);
		SharedPrefUtil prefUtil = new SharedPrefUtil(this);
		viewModel.setUserMail(prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY, null));
		if(viewModel.getUserMail() == null|| viewModel.getUserMail().equals("null") || viewModel.getUserMail().isEmpty() || TextUtils.isEmpty(viewModel.getUserMail())){
			new AlertDialog.Builder(this).setTitle("Needs Your E-Mail")
					.setMessage("Please Update your e-mail address and try again")
					.setPositiveButton("OK", (dialog, which) -> {
						if(which==DialogInterface.BUTTON_POSITIVE){
							Intent intent = new Intent(SwabhimanActivity.this, EditProfile.class);
							startActivity(intent);
						}
					}).setNegativeButton("Dismiss", (dialog, which) -> finish())
					.setIcon(getResources().getDrawable(R.drawable.pslogotrimmed)).show();
		}
	}
	
	@Override
	public void onPaymentSuccess(String s) {
		new android.app.AlertDialog.Builder(this).setTitle("THANK YOU!")
				.setPositiveButton("OK", (button, which)->{
					viewModel.getNavController().navigateUp();
				}).show();
	}
	
	@Override
	public void onPaymentError(int i, String s) {
		Toast.makeText(this, "There was an error processing payment!", Toast.LENGTH_SHORT).show();
	}
}