package com.example.intern.EditProfile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
        binding.back.setOnClickListener(v->{
            onBackPressed();
        });
        binding.home.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String value = sharedPreferences.getString("role", "");
        if(value.equals("1"))
            binding.hintNumber.setHint("Child's Number");
        else if(value.equals("2"))
            binding.hintNumber.setHint("Parent's Number");
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
        binding.addressPin.setText(preferences.getString(SharedPrefUtil.USER_PIN_CODE_KEY, null ));
        //Prefetch Address
        binding.addressHouse.setText(preferences.getString(SharedPrefUtil.USER_HOUSE_NUMBER, null));
        binding.addressStreet.setText(preferences.getString(SharedPrefUtil.USER_STREET_KEY, null));
        binding.addressArea.setText(preferences.getString(SharedPrefUtil.USER_AREA_KEY, null));
        //REfine showing of phone numbers
        String user_phone = preferences.getString(SharedPrefUtil.USER_PHONE_NO, null);
        String alt_phone = preferences.getString(SharedPrefUtil.USER_RELATIVE_PHONE_NUMBER_KEY, null);
        if(user_phone != null && !user_phone.isEmpty() && user_phone.length() >= 10 && user_phone.contains("+")){
            binding.phone.setText(user_phone.substring(3));
        }else binding.phone.setText(user_phone);
        if(alt_phone != null && !alt_phone.isEmpty() && alt_phone.length() >=10 && alt_phone.contains("+")){
            binding.relativePhoneNumber.setText(alt_phone.substring(3));
        }else binding.relativePhoneNumber.setText(alt_phone);
        String filePath = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, null);
        if(filePath != null && !filePath.isEmpty()){
            Glide.with(this).load(filePath).fallback(R.drawable.edit_profile).error(R.drawable.edit_profile).into(binding.ivProfilePic);
        }else{
            final Context context = EditProfile.this;
            try {
                FireStoreUtil.getProfilePicInLocal(this, FirebaseAuth.getInstance().getUid()).addOnSuccessListener(taskSnapshot -> {
                    SharedPrefUtil prefUtil1 = new SharedPrefUtil(context);
                    File path = new File(Environment.getExternalStorageDirectory(), "PSData/pp.jpg");
                    prefUtil1.getPreferences().edit().putString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, path.getPath()).apply();
                    Glide.with(context).load(path)
                            .fallback(R.drawable.edit_profile)
                            .error(R.drawable.edit_profile)
                            .into(binding.ivProfilePic);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                dialog.setIcon(R.drawable.pslogotrimmed);
                dialog.setMessage("Please make sure you are connected to the internet");
                String u_name;
                String email;
                String occ = null;
                String phone = null;
                String areapin=null;
                Editable name = binding.name.getText();
                if(name != null){
                    u_name = name.toString();
                    if(TextUtils.isEmpty(u_name)){
                        binding.name.setError("Cannot be empty");
                        return;
                    }
                }else{
                    return;
                }
                Editable mail = binding.email.getText();
                if(mail != null){
                    email = mail.toString();
                    if(TextUtils.isEmpty(email)){
                        binding.email.setError("Cannot be empty");
                        return;
                    }else if(!email.contains("@") && !email.contains(".")){
                        binding.email.setError("Invalid e-mail");
                        return;
                    }
                }else {
                    return;
                }
                Editable occupation = binding.occupation.getText();
                if(occupation != null){
                    occ = occupation.toString();
                }
                Editable phoneNo = binding.phone.getText();
                if(phoneNo != null) {
                    phone = phoneNo.toString();
                    //Toast.makeText(getApplicationContext(),phone.length(),Toast.LENGTH_LONG).show();
                    if(phone.length() < 10){
                        binding.phone.setError("Invalid Phone Number");
                        return;
                    }
                }
                Editable pin = binding.addressPin.getText();
                if(occupation != null){
                    areapin = occupation.toString();
                    if(areapin.length()<6){
                        binding.phone.setError("Invalid Phone Number");
                    return;
                }
                }

                //Refine Address
                Editable houseEditable = binding.addressHouse.getText();
                Editable streetEditable = binding.addressStreet.getText();
                Editable areaEditable = binding.addressArea.getText();
                if(houseEditable == null || TextUtils.isEmpty(houseEditable) || houseEditable.length() < 4){
                    binding.addressHouse.setError("Enter House Number");return;
                }
                if(streetEditable == null || TextUtils.isEmpty(streetEditable) || streetEditable.length() < 4){
                    binding.addressStreet.setError("Enter Street");return;
                }
                if(areaEditable == null || TextUtils.isEmpty(areaEditable) || areaEditable.length() < 4){
                    binding.addressArea.setError("Enter Area");return;
                }

                Editable relativePh = binding.relativePhoneNumber.getText();
                if(relativePh != null && !TextUtils.isEmpty(relativePh) && relativePh.length() < 10) {
                    binding.relativePhoneNumber.setError("Invalid Phone Number");
                    return;
                }
                SharedPrefUtil prefUtil = new SharedPrefUtil(this);
                dialog.show();
                FireStoreUtil.uploadMeta(this,u_name,email,occ,houseEditable.toString(),streetEditable.toString(),areaEditable.toString(), phone, relativePh.toString()).addOnSuccessListener(aVoid -> {
                    prefUtil.updateWithCloud(FireStoreUtil.getFirebaseUser(getApplicationContext()).getUid());
                    dialog.dismiss();
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
        binding.name.addTextChangedListener(changeWatcher);
        binding.occupation.addTextChangedListener(changeWatcher);
        binding.phone.addTextChangedListener(changeWatcher);
        binding.relativePhoneNumber.addTextChangedListener(changeWatcher);
        binding.addressHouse.addTextChangedListener(changeWatcher);
        binding.addressStreet.addTextChangedListener(changeWatcher);
        binding.addressArea.addTextChangedListener(changeWatcher);
        binding.addressPin.addTextChangedListener(changeWatcher);
    }
}
