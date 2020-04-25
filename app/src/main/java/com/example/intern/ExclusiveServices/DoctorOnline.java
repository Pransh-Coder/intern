package com.example.intern.ExclusiveServices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityDoctorOnlineBinding;
import com.example.intern.mainapp.MainApp;

import java.util.Calendar;

public class DoctorOnline extends AppCompatActivity {
    
    ActivityDoctorOnlineBinding binding;
    SharedPrefUtil prefUtil;
    boolean askedForSelf = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        populateDoctorDetails();
        prefUtil = new SharedPrefUtil(this);
        binding.doctorButtonBack.setOnClickListener(v-> onBackPressed());
        binding.doctorButtonHome.setOnClickListener(view -> {
            Intent intent = new Intent(DoctorOnline.this, MainApp.class);
            startActivity(intent);
        });
        binding.doctorNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    private void setDetailsVisibility(boolean b){
        if(b){
            binding.etOtherAge.setVisibility(View.VISIBLE);
            binding.etOtherName.setVisibility(View.VISIBLE);
        }else{
            binding.etOtherName.setVisibility(View.GONE);
            binding.etOtherAge.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.spinnerPersonType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //Self choice
                    setDetailsVisibility(false);
                    askedForSelf = true;
                }else{
                    setDetailsVisibility(true);
                    askedForSelf = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        binding.demandSubmit.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setIcon(R.drawable.pslogotrimmed);
            dialog.setTitle("Please Wait");
            dialog.show();
            final Context context = this;
            String description = binding.etServiceDescription.getText().toString();
            if(description.isEmpty()){
                //did not enter description
                binding.etServiceDescription.setError("Describe Your Problem");
            }else{
                if(askedForSelf){
                    int age = -1;
                    String ageTimeStamp = prefUtil.getPreferences().getString(SharedPrefUtil.USER_DOB_KEY, null);
                    String user_name = prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, null);
                    if(ageTimeStamp != null){
                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);
                        try{calendar.setTimeInMillis(Long.parseLong(ageTimeStamp));}catch (Exception ignored){}
                        int birthYear = calendar.get(Calendar.YEAR);
                        age = currentYear-birthYear;
                    }
                    //TODO : Store data in backend
                    FireStoreUtil.uploadDoctorRequest(prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null),user_name, Integer.toString(age),description).addOnSuccessListener(documentReference -> {
                        dialog.dismiss();
                        new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setTitle("Request Received").setMessage("We have received your request ! ")
                                .setPositiveButton("OK", null).show();
                    });
                }else{
                    //Asked for others
                    String name = binding.etOtherName.getText().toString();
                    String age = binding.etOtherAge.getText().toString();
                    if(name.isEmpty() || age.isEmpty()){
                        Toast.makeText(this, "Enter Details ! ", Toast.LENGTH_SHORT).show();
                    }else{
                        //Got the Details
                        //TODO : Store in backend
                        FireStoreUtil.uploadDoctorRequest(prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null),name,age, description).addOnSuccessListener(documentReference -> {
                            dialog.dismiss();
                            new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setTitle("Request Received").setMessage("We have received your request ! ")
                                    .setPositiveButton("OK", null).show();
                        });
                    }
                }
            }
        });
    }
    
    private void populateDoctorDetails(){
        String detailHTML =
                "<p><b>Age</b> - 75+ years<br>With experience of more than <b>45 years</b> in Ayurved.<br><br><b>Time</b> - Daily 4pm-5pm<br>" +
                "<b>Mode of consultation</b> - Audio/Video call<br>\n" +
                "<b>Charges</b> - Free for Senior Citizens<br>\n" +
                "₹300 for Others</p>";
        binding.tvDoctorDescription.setText(Html.fromHtml(detailHTML));
    }
}
