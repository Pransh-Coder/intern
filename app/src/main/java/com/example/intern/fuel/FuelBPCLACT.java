package com.example.intern.fuel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.intern.databinding.ActivityBpclFuelQrScannerBinding;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;

public class FuelBPCLACT extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQ_CODE = 23;
    private ActivityBpclFuelQrScannerBinding binding;
    private BarcodeCallback barcodeCallback = result -> {
        //TODO : Set callback for the scanned bar code
        Toast.makeText(this, "Found An Invoice!", Toast.LENGTH_SHORT).show();
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBpclFuelQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPerms();
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
        binding.qrView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        binding.qrView.initializeFromIntent(getIntent());
        binding.qrView.setStatusText("Scan the Invoice");
        binding.qrView.decodeContinuous(barcodeCallback);
    }
    
    private void checkPerms(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQ_CODE);
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.bpclFuelQrScannerSubmit.setOnClickListener(v -> {
            Editable amount = binding.bpclFuelQrScannerEtEnterAmount.getText();
            Editable invoiceNo = binding.bpclFuelQrScannerEtInvoiceNumber.getText();
            if(amount != null && invoiceNo != null){
                String amt= amount.toString();
                String invoice = invoiceNo.toString();
                if(amt.isEmpty()){
                    binding.bpclFuelQrScannerEtEnterAmount.setError("Cannot be empty");
                    return;
                }else if(invoice.isEmpty()){
                    binding.bpclFuelQrScannerEtInvoiceNumber.setError("Cannot be Empty");
                    return;
                }else{
                    //TODO : Submit the data
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION_REQ_CODE){
            if(permissions[0].equals(Manifest.permission.CAMERA)){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //TODO : Camera permission granted
                }else{
                    new AlertDialog.Builder(this).setTitle("Needs Camera Permission")
                            .setMessage("To scan the invoice")
                            .setPositiveButton("OK", (button, which)->{
                        if(which == AlertDialog.BUTTON_POSITIVE){
                            checkPerms();
                        }
                    }).setNegativeButton("EXIT", (button, which)->{
                        if(which==AlertDialog.BUTTON_NEGATIVE){
                            finish();
                        }
                    }).show();
                }
            }
        }
    }
}
