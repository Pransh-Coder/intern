package com.example.intern.MedicalRecords;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityMedicalRecordBinding;

public class MedicalRecord extends AppCompatActivity {
    private ActivityMedicalRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent = new Intent(MedicalRecord.this, Vitals.class);
                startActivity(intent);
            }
        });
    }
    
    private void populateText(){
        String source = "Create your health passport with " + "<b>vCura</b>" + " and have all your Medical Records at one place" +
                ". We help elderly with -";
        binding.fstPara.setText(Html.fromHtml(source));
    }
}
