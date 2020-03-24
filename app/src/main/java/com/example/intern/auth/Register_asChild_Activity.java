package com.example.intern.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import save_money.SaveMoney;

public class Register_asChild_Activity extends AppCompatActivity {

    Button child_SignIn;
    private EditText name,petname,pspetnem,dob,pincode,phn;
    String userid,nm,pn,pspn,Dob,phone,pin;
    private FirebaseFirestore fstore;
    private FirebaseAuth fauth;
    private final Context activityContext = this;
    private FusedLocationProviderClient locationProviderClient;
    private int LOCATION_REQUEST_CODE = 23;
    private AtomicReference<String> pinCode = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_child);
       // getSupportActionBar().hide();
        name=findViewById(R.id.et_name);
        petname=findViewById(R.id.et_nick_name);
        pspetnem=findViewById(R.id.et_ps_nick_name);
        dob=findViewById(R.id.et_DOB);
        phn=findViewById(R.id.et_parent_number);
        pincode=findViewById(R.id.et_pin_code);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        child_SignIn= findViewById(R.id.btnRegisterasChild_SignIn);
        final Context context = getApplicationContext();
        final Intent intent = getIntent();
        child_SignIn.setEnabled(false);
        child_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nm=name.getText().toString();
                if(nm.isEmpty())
                {
                    name.setError("Name is Required");
                    return ;
                }
                Dob=dob.getText().toString();
                if(Dob.isEmpty())
                {
                    dob.setError("DOB is required");
                    return;
                }
                phone=phn.getText().toString();
                if(phone.isEmpty())
                {
                    phn.setError("Phone number is required");
                    return;
                }
                pin=pincode.getText().toString();
                if(pin.isEmpty())
                {
                    getLocationPermsAndPinCode();
                    return;
                }
                pn=petname.getText().toString();
                pspn=pspetnem.getText().toString();
                userid=fauth.getCurrentUser().getUid();
                child_SignIn.setEnabled(true);
//                DocumentReference documentReference=fstore.collection("Users").document(userid);
//                TODO:Get pin code from location
                FireStoreUtil.makeUserWithUID(context, userid, nm,intent.getStringExtra(RegistrationChoice.E_MAIL_SELECTED)
                ,pn , pspn , pn, Dob, pin).addOnSuccessListener(aVoid -> {
                    Toast.makeText(Register_asChild_Activity.this,"Profile set up Successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register_asChild_Activity.this, SaveMoney.class);
                    startActivity(intent);
                    finish();
                });
                /*HashMap<String,String> profilemap=new HashMap<>();
                profilemap.put("uid",userid);
                profilemap.put("name",nm);
                profilemap.put("Nick Name",pn);
                profilemap.put("Ps Nick Name",pspn);
                profilemap.put("Phone number",phone);
                profilemap.put("DOB",Dob);
                profilemap.put("Pincode",pin);
                documentReference.set(profilemap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register_asChild_Activity.this,"Profile set up Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register_asChild_Activity.this, SaveMoney.class);
                        startActivity(intent);
                        finish();

                    }
                });*/
            }
        });
    }
    
    private String getPostalCodeFromGPS(){
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            Geocoder geocoder = new Geocoder(activityContext);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
                if(addresses != null && addresses.size() > 0 ){
                    pinCode.set(addresses.get(0).getPostalCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return pinCode.get();
    }

    private void getPostalCodeFromUser(){
        child_SignIn.setEnabled(false);
        pincode.setError("Provide location permissions or enter manually");
    }

    private void getLocationPermsAndPinCode(){
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {permission}, LOCATION_REQUEST_CODE);
        }else{
            getPostalCodeFromGPS();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPostalCodeFromGPS();
            }else{
                getPostalCodeFromUser();
            }
        }
    }
}
