package com.example.intern.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;

import save_money.SaveMoney;

public class Register_asParent_Activity extends AppCompatActivity {

    Button child_SignIn;
    private EditText name,petname,pspetnem,dob,pincode,phn;
    String userid,nm,pn,pspn,Dob,phone,pin;
    private FirebaseFirestore fstore;
    private FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_parent);
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
                    pincode.setError("Pincode is required");
                    return;
                }
                pn=petname.getText().toString();
                pspn=pspetnem.getText().toString();
                userid=fauth.getCurrentUser().getUid();
                DocumentReference documentReference=fstore.collection("Registered_As_Parent").document(userid);
                HashMap<String,String> profilemap=new HashMap<>();
                profilemap.put("uid",userid);
                profilemap.put("N",nm);
                profilemap.put("NN",pn);
                profilemap.put("PNN",pspn);
                profilemap.put("Phn",phone);
                profilemap.put("DOB",Dob);
                profilemap.put("Pin",pin);
                documentReference.set(profilemap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register_asParent_Activity.this,"Profile set up Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register_asParent_Activity.this, SaveMoney.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                });





            }
        });
    }
}

