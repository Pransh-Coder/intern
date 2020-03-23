package com.example.intern.qr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityQrBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

public class QRScanner extends AppCompatActivity {
	private static String TAG = QRScanner.class.getSimpleName();
	private ActivityQrBinding binding;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityQrBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		binding.button.setOnClickListener(v->{
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
			integrator.setPrompt("Scan the vendor QR code");
			integrator.setBeepEnabled(true);
			integrator.setBarcodeImageEnabled(true);
			integrator.setTorchEnabled(true);
			integrator.initiateScan();
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if(result != null) {
			if(result.getContents() == null) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			} else {
				//TODO : store in vendor - user relation that user has taken the offer
				getQRData(result);
				Toast.makeText(this, "Scanned" + result.getContents(), Toast.LENGTH_LONG).show();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void getQRData(IntentResult result){
		try{
			JSONObject data = new JSONObject(result.getContents());
			String vendorData = data.get("vendor") + "\n" + data.get("offer");
			binding.textView.setText(vendorData);
		}catch (Exception e){
			Log.d(TAG, "getQRData: JSON parse error");
		}
	}
}