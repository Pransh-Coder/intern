package com.example.intern.qr;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class QRScanner extends AppCompatActivity {
    private static String TAG = QRScanner.class.getSimpleName();
    private ActivityQrBinding binding;
    private String lastText;
    private BeepManager beepManager;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            //Check for duplicate
            if (result.getText() == null) return;
            //update content
            binding.qrView.pauseAndWait();
            lastText = result.getText();
            beepManager.playBeepSoundAndVibrate();
            binding.ivScannedImage.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            showImageAndPopulate(result);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
        binding.qrView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        binding.qrView.initializeFromIntent(getIntent());
        binding.qrView.setStatusText("Scan a QR code from the vendor");
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.qrView.resume();
        binding.button.setOnClickListener(v -> {
            binding.qrView.resume();
            binding.ivScannedImage.setVisibility(View.INVISIBLE);
            binding.qrView.setVisibility(View.VISIBLE);
            binding.qrView.decodeSingle(callback);
            beepManager = new BeepManager(this);
            beepManager.setVibrateEnabled(true);
            beepManager.setBeepEnabled(true);
        });
        //TODO: confirm that user has taken offer using the redeem button
    }
	
/*	@Override
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
	}*/

    @Override
    protected void onPause() {
        super.onPause();
        binding.qrView.pause();
    }

    private void showImageAndPopulate(BarcodeResult result) {
        binding.ivScannedImage.setVisibility(View.VISIBLE);
        binding.qrView.setVisibility(View.INVISIBLE);
        binding.textView.setText(getQRData(lastText));
    }

    private String getQRData(String result) {
        try {
            JSONObject data = new JSONObject(result);
            if (data.has("offer")) {
                binding.qrView.setStatusText("Found an offer!");
            }
            String vendorData = data.get("vendor") + "\n" + data.get("offer");
            return vendorData;
        } catch (Exception e) {
            Log.d(TAG, "getQRData: JSON parse error");
        }
        return "No Such Vendor Found";
    }
}