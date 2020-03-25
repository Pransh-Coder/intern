package com.example.intern.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.ActivityRegisterAsChildBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

import save_money.SaveMoney;

public class Register_asChild_Activity extends AppCompatActivity {
    private static String TAG = Register_asChild_Activity.class.getSimpleName();
    private ActivityRegisterAsChildBinding binding;
    private String password;
    private String pinCode;
    private Context activityContext = this;
    
    private FusedLocationProviderClient locationProviderClient;
    private int LOCATION_REQUEST_CODE = 23;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterAsChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTextWatchers();
        setClickListeners();
    }
    
    private void setTextWatchers(){
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
        
            }
    
            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() >= 8){
                    password = s.toString();
                }else{
                    binding.etPassword.setError("Password must be 8 characters");
                }
            }
        });
        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
        
            }
    
            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() >= 8){
                    if(s.toString().equals(password)){
                        return;
                    }else{
                        binding.etConfirmPassword.setError("Passwords do not match");
                    }
                }
            }
        });
        binding.etPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
    
            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() == 6) {
                    pinCode = s.toString();
                }else return;
            }
        });
    }
    
    private void setClickListeners(){
        binding.btnRegisterasChildSignIn.setOnClickListener(v->{
            String name = binding.etName.getText().toString();
            String DOB = binding.etDOB.getText().toString();
            String password = binding.etPassword.getText().toString();
            if(name.isEmpty()){
                binding.etName.setError("Name Cannot Be Empty");return;
            }
            if(DOB.isEmpty()){
                binding.etDOB.setError("DOB Cannot Be Empty");return;
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Password Cannot Be Empty");return;
            }
            if(pinCode != null && pinCode.length() == 6){
                FirebaseUser user = FireStoreUtil.getFirebaseUser(activityContext);
                FireStoreUtil.makeUserWithUID(activityContext, user.getUid()
                        ,name, user.getEmail(), binding.etNickName.getText().toString(),
                        binding.etPsNickName.getText().toString(), binding.etParentNumber.getText().toString(),
                        DOB, pinCode, password)
                        .addOnSuccessListener(success->{
                            FireStoreUtil.addToCluster(activityContext, pinCode, user.getUid());
                            Intent intent = new Intent(Register_asChild_Activity.this, SaveMoney.class);
                            Log.d(TAG, "successfully made user");
                            startActivity(intent);
                        });
            }else {
                requirePinCode();
            }
        });
    }
    
    private String getPostalCodeFromGPS(){
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            Geocoder geocoder = new Geocoder(activityContext);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
                if(addresses != null && addresses.size() > 0 ){
                    pinCode = addresses.get(0).getPostalCode();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return pinCode;
    }

    private void getPostalCodeFromUser(){
        binding.etPinCode.setError("Provide location permissions or enter manually");
    }

    private String requirePinCode(){
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {permission}, LOCATION_REQUEST_CODE);
        }else{
            return getPostalCodeFromGPS();
        }
        return null;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                 requirePinCode();
            }else{
                getPostalCodeFromUser();
            }
        }
    }
}
