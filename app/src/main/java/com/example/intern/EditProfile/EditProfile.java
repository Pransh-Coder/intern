package com.example.intern.EditProfile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.example.intern.R;
import com.example.intern.bgworkers.ProfileImageUploadWorker;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityEditProfileBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private boolean hasDataEdited = false;
    private SharedPrefUtil prefUtil;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefUtil = new SharedPrefUtil(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        checkPerms();
        SharedPrefUtil prefUtil = new SharedPrefUtil(this);
        SharedPreferences preferences = prefUtil.getPreferences();
        binding.name.setText(preferences.getString(SharedPrefUtil.USER_NAME_KEY, null));
        binding.email.setText(preferences.getString(SharedPrefUtil.USER_EMAIL_KEY, null));
        binding.occupation.setText(preferences.getString(SharedPrefUtil.USER_OCCUPATION_KEY, null ));
        binding.address.setText(preferences.getString(SharedPrefUtil.USER_ADDRESS_KEY, null));
        String filePath = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, null);
        if(filePath != null && !filePath.isEmpty()){
            binding.ivProfilePic.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }else{
            File imageFile = FireStoreUtil.getProfilePicInLocal(this, FirebaseAuth.getInstance().getUid());
            Glide.with(this).load(imageFile)
                    .fallback(R.drawable.edit_profile)
                    .into(binding.ivProfilePic);
        }
        binding.ivProfilePic.setOnClickListener(v->{
            //TODO : Get Image from user
            ImagePicker.Companion.with(this)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(720, 720)
                    .start();
        });
        update();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            String filePath = ImagePicker.Companion.getFilePath(data);
            prefUtil.getPreferences().edit().putString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY,filePath).apply();
            FireStoreUtil.uploadProfilePic(this,prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null),BitmapFactory.decodeFile(filePath)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Data workData = new Data.Builder().putString(ProfileImageUploadWorker.KEY_UID, prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null))
                            .putString(ProfileImageUploadWorker.KEY_IMG_PATH, filePath)
                            .build();
                    Constraints constraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED).build();
                    OneTimeWorkRequest uploadRequest  = new OneTimeWorkRequest.Builder(ProfileImageUploadWorker.class)
                            .setInputData(workData)
                            .setConstraints(constraints)
                            .build();
                    WorkManager.getInstance(getApplicationContext()).enqueue(uploadRequest);
                }
            });
            Glide.with(this).load(filePath)
                    .fallback(R.drawable.edit_profile).into(binding.ivProfilePic);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error Loading File", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void update(){
        setDataChangeListeners();
        binding.addCustomer.setOnClickListener(v->{
            if(hasDataEdited){
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Updating Profile");
                dialog.show();
                String u_name;
                String email;
                String occ = null;
                String add = null;
                Editable name = binding.name.getText();
                if(name != null){
                    u_name = name.toString();
                    if(TextUtils.isEmpty(u_name)){
                        dialog.hide();
                        binding.name.setError("Cannot be empty");
                        return;
                    }
                }else{
                    dialog.hide();
                    return;
                }
                Editable mail = binding.email.getText();
                if(mail != null){
                    email = mail.toString();
                    if(TextUtils.isEmpty(email)){
                        dialog.hide();
                        binding.email.setError("Cannot be empty");
                        return;
                    }else if(!email.contains("@") && !email.contains(".")){
                        dialog.hide();
                        binding.email.setError("Invalid e-mail");
                        return;
                    }
                }else {
                    dialog.hide();
                    return;
                }
                Editable occupation = binding.occupation.getText();
                if(occupation != null){
                    occ = occupation.toString();
                }
                Editable address = binding.address.getText();
                if(address != null){
                    add = address.toString();
                }
                SharedPrefUtil prefUtil = new SharedPrefUtil(this);
                FireStoreUtil.uploadMeta(this,u_name,email,occ,add).addOnSuccessListener(aVoid -> {
                    prefUtil.updateWithCloud(FireStoreUtil.getFirebaseUser(getApplicationContext()).getUid());
                    dialog.hide();
                    Intent intent = new Intent(EditProfile.this, MainApp.class);
                    finish();
                    startActivity(intent);
                });
            }else{
                Intent intent = new Intent(EditProfile.this, MainApp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    
    private  void checkPerms(){
        Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {}
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {}
        }).check();
    }
    
    private void setDataChangeListeners(){
        TextWatcher changeWatcher = new TextWatcher() {
            String afterText, beforeText;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
    
            @Override
            public void afterTextChanged(Editable s) {
                afterText = s.toString();
                if(!afterText.equals(beforeText)){
                    hasDataEdited = true;
                }
            }
        };
        binding.email.addTextChangedListener(changeWatcher);
        binding.address.addTextChangedListener(changeWatcher);
        binding.name.addTextChangedListener(changeWatcher);
        binding.occupation.addTextChangedListener(changeWatcher);
    }
}
