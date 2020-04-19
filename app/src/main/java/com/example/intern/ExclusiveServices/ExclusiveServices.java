package com.example.intern.ExclusiveServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.ActivityExclusiveServiceBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ExclusiveServices extends AppCompatActivity {

    ActivityExclusiveServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    binding = ActivityExclusiveServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Context context = ExclusiveServices.this;

        binding.back.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.homeIMG.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.homeModification.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, HomeModification.class);
            startActivity(intent);
        });
        binding.tiffin.setOnClickListener(view -> {
            Intent intent = new Intent(ExclusiveServices.this, TiffinService.class);
            startActivity(intent);
        });
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		final Context context = ExclusiveServices.this;
		binding.auto.setOnClickListener(v -> {
			//TODO :
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.EXCLUSIVE_SERVICES_COLLECTION_NAME)
					.document(FireStoreUtil.AUTO_SERVICES_SERVICES)
					.collection(FireStoreUtil.AUTO_SERVICES_SERVICES).add(data)
					.addOnSuccessListener(documentReference -> {
						dialog.dismiss();
						new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
								.setMessage("Thank you for showing interest. We will get back to you shortly").setPositiveButton("OK", null).show();
					});
		});
		binding.emergencyCare.setOnClickListener(v -> {
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.EXCLUSIVE_SERVICES_COLLECTION_NAME)
					.document(FireStoreUtil.EMERGENCY_CARE_SERVICES)
					.collection(FireStoreUtil.EMERGENCY_CARE_SERVICES).add(data)
					.addOnSuccessListener(documentReference -> {
						dialog.dismiss();
						new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
								.setMessage("Thank you for showing interest. We will get back to you shortly").setPositiveButton("OK", null).show();
					});
		});
		binding.legalFinancial.setOnClickListener(v -> {
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.EXCLUSIVE_SERVICES_COLLECTION_NAME)
					.document(FireStoreUtil.LEGAL_FINANCIAL_SERVICES)
					.collection(FireStoreUtil.LEGAL_FINANCIAL_SERVICES).add(data)
					.addOnSuccessListener(documentReference -> {
						dialog.dismiss();
						new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
								.setMessage("Thank you for showing interest. We will get back to you shortly").setPositiveButton("OK", null).show();
					});
		});
		binding.eduClasses.setOnClickListener(v -> {
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle("Please wait");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.show();
			Map<String, Object> data = new HashMap<>();
			data.put("uid", FirebaseAuth.getInstance().getUid());
			FirebaseFirestore.getInstance().collection(FireStoreUtil.EXCLUSIVE_SERVICES_COLLECTION_NAME)
					.document(FireStoreUtil.EDUCATION_CLASSES_SERVICES)
					.collection(FireStoreUtil.EDUCATION_CLASSES_SERVICES).add(data)
					.addOnSuccessListener(documentReference -> {
						dialog.dismiss();
						new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Thank You")
								.setMessage("Thank you for showing interest. We will get back to you shortly").setPositiveButton("OK", null).show();
					});
		});
	}
}
