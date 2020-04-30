package com.example.intern.askservices;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityDemandBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;

public class DemandActivity extends AppCompatActivity {
    ActivityDemandBinding binding;
    List<String> services;
    String service = null;
    String filePath=null;
	SharedPrefUtil prefUtil;
    private boolean isProductPageVisible;
    private boolean isServicesPageVisible = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPerms();
        prefUtil = new SharedPrefUtil(this);
        services = Arrays.asList(getResources().getStringArray(R.array.AskServicesOptions));
	    binding.etServiceDescription.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    binding.charCount.setText(s.length() +"/200");
		    }
		
		    @Override
		    public void afterTextChanged(Editable s) {
			    binding.charCount.setText(s.length() +"/200");
			
		    }
	    });
	    binding.etProductName.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    binding.charCount2.setText(s.length() +"/200");
		    }
		
		    @Override
		    public void afterTextChanged(Editable s) {
			    binding.charCount2.setText(s.length() +"/200");
		    }
	    });
        binding.demandButtonBack.setOnClickListener(v-> onBackPressed());
        binding.demandButtonHome.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.demandButtonProduct.setOnClickListener(v -> {
            toggleToProductPage();
        });
        binding.demandButtonService.setOnClickListener(v -> {
            toggleToServicesPage();
        });
        binding.servicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service = services.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                service = "other";
            }
        });
        binding.demandNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    private void toggleToProductPage(){
        isProductPageVisible = true;
        isServicesPageVisible = false;
        binding.demandButtonService.setBackground(getResources().getDrawable(R.drawable.button_oulined_not_selected));
        binding.demandButtonProduct.setBackground(getResources().getDrawable(R.drawable.button_outlined_selected));
        binding.contraintLayoutServices.setVisibility(GONE);
        binding.scrollViewProduct.setVisibility(View.VISIBLE);
    }
    
    private void toggleToServicesPage(){
        isProductPageVisible = false;
        isServicesPageVisible = true;
        binding.demandButtonProduct.setBackground(getResources().getDrawable(R.drawable.button_oulined_not_selected));
        binding.demandButtonService.setBackground(getResources().getDrawable(R.drawable.button_outlined_selected));
        binding.contraintLayoutServices.setVisibility(View.VISIBLE);
        binding.scrollViewProduct.setVisibility(GONE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.d("Demand", "onActivityResult OK");
            filePath = ImagePicker.Companion.getFilePath(data);
            Glide.with(this).load(filePath).fallback(R.drawable.ic_image_idol).into(binding.ivProductImage);
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, "Could not load image",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.ivProductImage.setOnClickListener(v ->
		        ImagePicker.Companion.with(this)
                .maxResultSize(1080, 1080)
                .start());
        binding.demandSubmit.setOnClickListener(v -> {
            final Context context = this;
	        String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null);
	        if(UID == null){
		        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		        if(user==null){
                    Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
                    finish();
		        }else{
			        UID = user.getUid();
		        }
	        }
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.please_wait));
            dialog.setMessage(getString(R.string.uploading_request_message));
            if(isServicesPageVisible){
                String description = binding.etServiceDescription.getText().toString();
                if(TextUtils.isEmpty(description)){
                    binding.etServiceDescription.setError(getString(R.string.describe_service_error));
                }else{
	                dialog.show();
                    FireStoreUtil.uploadServiceRequest(UID,service,description).addOnSuccessListener(documentReference -> {         // &#128522 \uD83D\uDE4F\uD83C\uDFFB"
                        dialog.dismiss();
	                    new AlertDialog.Builder(context).setMessage(R.string.request_submit_success_alert).setPositiveButton(R.string.ok, (dialog1, which) -> onBackPressed())
			                    .setTitle(R.string.thank_you).setIcon(R.drawable.pslogotrimmed).show();
                    });
                }
            }else if(isProductPageVisible){
            	//TODO : Fallback to previous settings
                String productRequest = binding.etProductName.getText().toString();
                if (filePath != null && !filePath.isEmpty()){
                	//Image is provided
	                dialog.show();
                    FireStoreUtil.uploadImage(this, UID,
                            BitmapFactory.decodeFile(filePath)).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                FireStoreUtil.uploadProductRequest(prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null), productRequest, uri.toString()).addOnSuccessListener(documentReference -> {
                                    dialog.dismiss();
                                    new AlertDialog.Builder(context).setMessage(R.string.request_submit_success_alert).setPositiveButton(R.string.ok, (dialog1, which) -> onBackPressed())
                                            .setTitle(R.string.thank_you).setIcon(R.drawable.pslogotrimmed).show();
                                });
                            }));
                }else{
                	//No Image provided by user
                    if(productRequest.isEmpty()){
                    	binding.etProductName.setError(getString(R.string.ask_product_error_message));
                    }else{
	                    dialog.show();
	                    FireStoreUtil.uploadProductRequest(UID, productRequest, null).addOnSuccessListener(documentReference -> {
		                    dialog.dismiss();
		                    new AlertDialog.Builder(context).setMessage(R.string.request_submit_success_alert).setPositiveButton(R.string.ok, (dialog1, which) -> onBackPressed())
				                    .setTitle(R.string.thank_you).setIcon(R.drawable.pslogotrimmed).show();
	                    });
                    }
                }
            }
        });
    }
    
    private void checkPerms(){
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(!multiplePermissionsReport.areAllPermissionsGranted())checkPerms();
            }
    
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
        
            }
        }).check();
    }
}
