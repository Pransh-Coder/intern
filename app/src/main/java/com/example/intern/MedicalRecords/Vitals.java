package com.example.intern.MedicalRecords;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityVitalsBinding;
import com.example.intern.mailers.VCuraAutoMailer;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;

public class Vitals extends AppCompatActivity {
    ActivityVitalsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVitalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.back.setOnClickListener(v -> onBackPressed());
        SharedPrefUtil prefUtil = new SharedPrefUtil(this);
        //TODO : Send the info to VCura
        binding.submit.setOnClickListener(v -> {
            String email = prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY,null);
            if(email == null){
                Toast.makeText(this, "Please update email to continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EditProfile.class);
                startActivity(intent);
                finish();
            }else if(binding.etPersonName.getText().toString().isEmpty()){
                binding.etPersonName.setError("Please Enter Name at least");
            }else{
                String name = binding.etPersonName.getText().toString();
                String bloodGrp = binding.etBloodGroup.getText().toString();
                String bloodGlucose = binding.etBloodGlucose.getText().toString();
                String bloodPr = binding.etBloodPressure.getText().toString();
                String temperature = binding.etTemperature.getText().toString();
                String height = binding.etHeight.getText().toString();
                String weight = binding.etWeight.getText().toString();
                String phoneAdded = getIntent().getStringExtra("phoneAdded");
                String phone = "";
                if(phoneAdded.equals("true")){
                    phone = getIntent().getStringExtra("phone");
                }
                else{
                    phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                }
                String email_body = "Patient Name : " + name + " Patient Phone number : " + phone + "\n Shared Health Details : \n1. Blood Group - " + bloodGrp +
                        "\n2.Blood Glucose - " + bloodGlucose + "\n3.Blood Pressure - " + bloodPr + "\n4.Temperature - " +
                        temperature + "\n5.Weight - "+ height + "\n6.Allergy - " + weight +
                        "\nThis email is auto generated and the truth of above vitals depend upon the data submitted by the patient.";
                VCuraAutoMailer.sendMail("Patient Request | PSByPrarambh",email_body,email);
                new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed)
                        .setTitle("Request sent").setMessage("Your Request to connect to VCura has been sent. They might contact you shortly\n" +
                        "PS holds no responsibility for any delay in service. Check your internet connection if problem persists")
                        .setPositiveButton("OK", null).show();
            }
        });
    }
}
