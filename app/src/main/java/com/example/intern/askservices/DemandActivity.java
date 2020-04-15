package com.example.intern.askservices;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.databinding.ActivityDemandBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DemandActivity extends AppCompatActivity {
    ActivityDemandBinding binding;
    List<String> services;
    String service = null;
    File file;
    private boolean isProductPageVisible;
    private boolean isServicesPageVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPerms();
        services = Arrays.asList(getResources().getStringArray(R.array.AskServicesOptions));
        binding.demandButtonBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.demandButtonHome.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.demandButtonProduct.setOnClickListener(v -> {
            isProductPageVisible = true;
            isServicesPageVisible = false;
            binding.contraintLayoutServices.setVisibility(View.GONE);
            binding.constraintProduct.setVisibility(View.VISIBLE);
        });
        binding.demandButtonService.setOnClickListener(v -> {
            isProductPageVisible = false;
            isServicesPageVisible = true;
            binding.contraintLayoutServices.setVisibility(View.VISIBLE);
            binding.constraintProduct.setVisibility(View.GONE);
        });
        binding.spinner2.setOnItemClickListener((parent, view, position, id) -> service = services.get(position));
        binding.ivProductImage.setOnClickListener(v -> {
            ImagePicker.Companion.with(this).start();
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        file = ImagePicker.Companion.getFile(data);
        //TODO : Do something with the file
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.demandSubmit.setOnClickListener(v -> {
            if(isServicesPageVisible){
                String description = binding.etServiceDescription.getText().toString();
                if(TextUtils.isEmpty(description)){
                    binding.etServiceDescription.setError("Describe the service");
                }else{
                    //TODO : Send the service to somewhere
                }
            }else if(isProductPageVisible){
                String product = binding.etProductName.getText().toString();
                //TODO : Do something with the image and the product name
            }
        });
    }
    
    private void checkPerms(){
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {}
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {checkPerms();}
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {}
        });
    }
}
