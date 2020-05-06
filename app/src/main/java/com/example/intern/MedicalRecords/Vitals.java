package com.example.intern.MedicalRecords;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityVitalsBinding;
import com.example.intern.mailers.VCuraAutoMailer;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;

public class Vitals extends AppCompatActivity {
    ActivityVitalsBinding binding;
    private int weight;
    private String allergy;
    
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
        //Show dialog boxes for last two
        binding.etWeight.setOnClickListener(v -> {
            CustomVitalDialog customDialog = new CustomVitalDialog("Enter Weight", "Ex : 70", Integer.toString(weight), InputType.TYPE_CLASS_NUMBER, 3, text -> {
                if (!text.isEmpty()) binding.etWeight.setText(text);
                try{
                    weight = Integer.parseInt(text);
                }catch(Exception ignored){}
            });
            customDialog.show(getSupportFragmentManager(), "WeightIN");
        });
        binding.etAllergy.setOnClickListener(v -> {
            CustomVitalDialog customDialog = new CustomVitalDialog("Enter allergies", "if any", allergy, InputType.TYPE_TEXT_FLAG_AUTO_CORRECT, 30, text -> {
                allergy = text;
                binding.etAllergy.setText(text);
            });
            customDialog.show(getSupportFragmentManager(), "Allergy");
        });
        //TODO : Send the info to VCura
        binding.submit.setOnClickListener(v -> {
            String email = prefUtil.getPreferences().getString(SharedPrefUtil.USER_EMAIL_KEY, null);
            if (email == null) {
                Toast.makeText(this, "Please update email to continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EditProfile.class);
                startActivity(intent);
                finish();
            } else if (binding.etPersonName.getText().toString().isEmpty()) {
                binding.etPersonName.setError("Please Enter Name");
            } else {
                String name = binding.etPersonName.getText().toString();
                String bloodGrp = binding.etBloodGroup.getText().toString();
                String bloodGlucose = binding.etBloodGlucose.getText().toString();
                String bloodPr = binding.etBloodPressure.getText().toString();
                String temperature = binding.etTemperature.getText().toString();
                String weight = binding.etWeight.getText().toString();
                String phoneAdded = getIntent().getStringExtra("phoneAdded");
                String phone = "";
                if (phoneAdded.equals("true")) {
                    phone = getIntent().getStringExtra("phone");
                } else {
                    phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                }
                String email_body = "Patient Name : " + name + " Patient Phone number : " + phone + "\n Shared Health Details : \n1. Blood Group - " + bloodGrp +
                        "\n2.Blood Glucose - " + bloodGlucose + "\n3.Blood Pressure - " + bloodPr + "\n4.Temperature - " +
                        temperature + "\n5.Weight - " + weight + "\n6.Allergy - " + allergy +
                        "\nThis email is auto generated and the truth of above vitals depend upon the data submitted by the patient.";
                VCuraAutoMailer.sendMail("Patient Request | PSByPrarambh", email_body, email);
                new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed)
                        .setTitle("Request sent").setMessage("Your Request to connect to VCura has been sent. They might contact you shortly\n" +
                        "Note : PS holds no responsibility for any delay in service. You must be connected to the internet")
                        .setPositiveButton("OK", null).show();
            }
        });
    }
    
    
    interface textListener{
        void setText(String text);
    }

    public static class CustomVitalDialog extends DialogFragment {
        TextView mTitle;
        EditText mDetails;
        TextView mOK;
        String title;
        String hint;
        String previousText;
        int inputType, maxLength;
        private textListener textListener;
        
        public CustomVitalDialog(String title, String hint , String previousText, int inputType, int maxLength,textListener textListener){
            this.textListener = textListener;
            this.title = title;
            this.hint = hint;
            this.maxLength = maxLength;
            this.inputType = inputType;
            if(previousText != null)this.previousText = previousText;
        }
        
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.enter_text_dialog, container, false);
            return v;
        }
        
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mTitle = view.findViewById(R.id.tv_title);
            mDetails = view.findViewById(R.id.et_detail);
            mOK = view.findViewById(R.id.ok_button);
            mOK.setOnClickListener(v -> dismiss());
            mTitle.setText(title);
            mDetails.setHint(hint);
            if(previousText != null)mDetails.setText(previousText);
            if(inputType!=0)mDetails.setInputType(inputType);
            if(maxLength!=0)mDetails.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        
        @Override
        public void onStart() {
            super.onStart();
            mDetails.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    textListener.setText(s.toString());
                }
            });
        }
    }
}