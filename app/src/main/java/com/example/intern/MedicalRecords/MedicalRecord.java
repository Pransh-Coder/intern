package com.example.intern.MedicalRecords;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityMedicalRecordBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;

public class MedicalRecord extends AppCompatActivity {
    private ActivityMedicalRecordBinding binding;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth = FirebaseAuth.getInstance();
        binding = ActivityMedicalRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        populateText();
        binding.agree.setOnClickListener(v->{
            SharedPrefUtil prefUtil = new SharedPrefUtil(this);
            String email = prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY, null);
            if(email == null){
                new AlertDialog.Builder(this).setTitle("Need Your E-Mail")
                        .setMessage("Please Update your e-mail address and try again")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==DialogInterface.BUTTON_POSITIVE){
                                    Intent intent = new Intent(MedicalRecord.this, EditProfile.class);
                                    startActivity(intent);
                                }
                            }
                        }).setNegativeButton("Dismiss", null)
                        .setIcon(getResources().getDrawable(R.drawable.pslogotrimmed)).show();
            }else{
                try{if(mauth.getCurrentUser().getPhoneNumber().isEmpty()){
                    LayoutInflater li = LayoutInflater.from(this);
                    View promptView = li.inflate(R.layout.dialog_phone,null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setView(promptView);
                    final EditText et = promptView.findViewById(R.id.et_phone);

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String phone = et.getText().toString();
                                    if(TextUtils.isEmpty(phone)){
                                        Toast.makeText(MedicalRecord.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Intent intent = new Intent(MedicalRecord.this,Vitals.class);
                                        intent.putExtra("phoneAdded","true");
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    Intent intent = new Intent(MedicalRecord.this, Vitals.class);
                    intent.putExtra("phoneAdded","false");
                    startActivity(intent);
                }}catch (NullPointerException e){
                    LayoutInflater li = LayoutInflater.from(this);
                    View promptView = li.inflate(R.layout.dialog_phone,null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setView(promptView);
                    final EditText et = promptView.findViewById(R.id.et_phone);

                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String phone = et.getText().toString();
                                    if(TextUtils.isEmpty(phone)){
                                        Toast.makeText(MedicalRecord.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Intent intent = new Intent(MedicalRecord.this,Vitals.class);
                                        intent.putExtra("phoneAdded","true");
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        binding.back.setOnClickListener(v->{
            onBackPressed();
            finish();
        });
        binding.homeBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.chatButton.setOnClickListener(v -> {
            String url = "https://wa.me/919909906065?text=Hi%20PS,%20I%20have%20some%20queries";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
    
    private void populateText(){
        String source = "Create your health passport with " + "<b>vCura</b>" + " and have all your Medical Records at one place" +
                ". We help elderly with -";
        binding.fstPara.setText(Html.fromHtml(source));
    }
}
