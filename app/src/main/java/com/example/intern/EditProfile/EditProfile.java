package com.example.intern.EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityEditProfileBinding;
import com.example.intern.mainapp.MainApp;

public class EditProfile extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        SharedPrefUtil prefUtil = new SharedPrefUtil(this);
        SharedPreferences preferences = prefUtil.getPreferences();
        binding.name.setText(preferences.getString(SharedPrefUtil.USER_NAME_KEY, null));
        binding.email.setText(preferences.getString(SharedPrefUtil.USER_EMAIL_KEY, null));
        binding.occupation.setText(preferences.getString(SharedPrefUtil.USER_OCCUPATION_KEY, null ));
        binding.address.setText(preferences.getString(SharedPrefUtil.USER_ADDRESS_KEY, null));
        update();
    }
    
    private void update(){
        binding.addCustomer.setOnClickListener(v->{
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
        });
    }
}
