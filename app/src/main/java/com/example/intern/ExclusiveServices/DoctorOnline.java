package com.example.intern.ExclusiveServices;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;

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
    
    @SuppressLint("SetTextI18n")
    private void showDetail(boolean b){
    	//Fill the name and age if true
	    if(b){
	    	binding.etName.setText(prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY,null));
	    	int age = 0;
	    	try{
	    		String dobTimestamp = prefUtil.getPreferences().getString(SharedPrefUtil.USER_DOB_KEY, null);
	    		if(dobTimestamp != null){
	    			Calendar calendar = Calendar.getInstance();
	    			int currentYear = calendar.get(Calendar.YEAR);
	    			calendar.setTimeInMillis(Long.parseLong(dobTimestamp));
	    			int dobYear = calendar.get(Calendar.YEAR);
	    			age = currentYear - dobYear;
			    }
		    }catch(Exception ignored){}
	    	binding.etAge.setText(Integer.toString(age));
	    }else{
	    	binding.etAge.setText("");
	    	binding.etName.setText("");
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
                    showDetail(true);
                    askedForSelf = true;
                }else{
                    showDetail(false);
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
	        Editable descp = binding.etDescp.getText();
	        String description = "";
	        if(descp != null) {
	        	description = descp.toString();
	        }
	        if (description.isEmpty()) {
		        //did not enter description
		        binding.etDescp.setError("Describe Your Problem");
	        } else {
		        if (askedForSelf) {
			        String name = binding.etName.getText().toString();
			        String age = binding.etAge.getText().toString();
			        if (name.isEmpty()) {
				        binding.etName.setError("Enter name");
			        } else if (age.isEmpty()) {
				        binding.etAge.setError("Enter age");
			        } else {
				        //TODO : Store data in backend
				        FireStoreUtil.uploadDoctorRequest(prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null), "self", name, age, description).addOnSuccessListener(documentReference -> {
					        dialog.dismiss();
					        new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setTitle("Request Received").setMessage("We have received your request ! ")
							        .setPositiveButton("OK", null).show();
				        });
			        }
		        } else {
			        //Asked for others
			        String name = binding.etName.getText().toString();
			        String age = binding.etAge.getText().toString();
			        if (name.isEmpty()) {
				        binding.etName.setError("Enter name");
			        } else if (age.isEmpty()) {
				        binding.etAge.setError("Enter age");
			        } else {
				        //TODO : Store data in backend
				        FireStoreUtil.uploadDoctorRequest(prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null), "other_mem", name, age, description).addOnSuccessListener(documentReference -> {
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
        binding.tvDoctorDescription.setText(noTrailingwhiteLines(Html.fromHtml(detailHTML)));
    }
	
	private CharSequence noTrailingwhiteLines(CharSequence text) {
		
		while (text.charAt(text.length() - 1) == '\n') {
			text = text.subSequence(0, text.length() - 1);
		}
		return text;
	}
}
