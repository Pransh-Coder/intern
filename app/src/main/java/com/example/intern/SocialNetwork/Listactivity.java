package com.example.intern.socialnetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Listactivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocalRecycler recyclerViewAdapter;
    private mLocalNetwork locallist=null;
    private ArrayList<mLocalNetwork> listItems;
    private mLocalNetwork mlistItems=null;
    String pinCode = null;
    FirebaseFirestore fstore;
    String uid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //LinearLayoutManager llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(llm);
       // recyclerView.setAdapter( recyclerViewAdapter );
        fstore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(Listactivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        retrivePincode();


        listItems = new ArrayList<>();
    }
    private void retrivePincode() {
        //Toast.makeText(SocialActivity.this,fstore.toString(),Toast.LENGTH_LONG).show();
        //Toast.makeText(SocialActivity.this,uid,Toast.LENGTH_LONG).show();
        DocumentReference docRef = fstore.collection("Users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        pinCode = document.getString("pc");
                        retriveClust();


                    } else {
                        //Toast.makeText(SocialActivity.this, "No such document", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Toast.makeText(SocialActivity.this, "No such document", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void retriveClust() {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("We are Fetching Friends near by you");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //Toast.makeText(SocialActivity.this, "In clust", Toast.LENGTH_LONG).show();
        DocumentReference docRefUclust = fstore.collection("uclust").document(pinCode);
        docRefUclust.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> list = new ArrayList<>();
                        Map<String, Object> map = task.getResult().getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            list.add(entry.getKey());
                        }
                        list.remove(uid);
                        Iterator<String> iterator = list.iterator();
                        Toast.makeText(Listactivity.this,"We got some Friends for you!! ", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        mlistItems = new mLocalNetwork();
                        for(String user:list)
                        {
                            getUsername(user);
                            //Toast.makeText(SocialActivity.this, user, Toast.LENGTH_LONG).show();

                        }
                        //Toast.makeText(Listactivity.this, "Goingtoputdata", Toast.LENGTH_LONG).show();
                        //recyclerViewAdapter = new LocalRecycler(Listactivity.this,listItems);
                        //recyclerView.setAdapter(recyclerViewAdapter);

                        //recyclerView.setAdapter(new LocalRecycler(Listactivity.this, listItems));
                        //recyclerViewAdapter.notifyDataSetChanged();

                    } else {
                        progressDialog.dismiss();
                        Log.d("IN Social Network", "No such document");
                    }
                } else {
                    progressDialog.dismiss();
                    Log.d("In Social Netowrk", "get failed with ", task.getException());
                }
            }
        });


    }



    private void getUsername(String user) {
        //Toast.makeText(SocialActivity.this, user, Toast.LENGTH_LONG).show();
        //Toast.makeText(SocialActivity.this, uid, Toast.LENGTH_LONG).show();
        //if (user!=uid) {

        DocumentReference docRef = fstore.collection("Users").document(user);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //Toast.makeText(SocialActivity.this, "got in", Toast.LENGTH_LONG).show();
                    locallist = documentSnapshot.toObject(mLocalNetwork.class);
                   //String nm= locallist.getOcc();

                    mlistItems.setun(locallist.getun());
                    mlistItems.setOcc(locallist.getOcc());
                    mlistItems.setAge(locallist.getAge());
                    listItems.add(mlistItems);
                    String nm= mlistItems.getOcc();
                    Toast.makeText(Listactivity.this,nm , Toast.LENGTH_LONG).show();

                    Toast.makeText(Listactivity.this, "Goingtoputdata", Toast.LENGTH_LONG).show();
                    recyclerViewAdapter = new LocalRecycler(Listactivity.this,listItems);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    //recyclerView.setAdapter(new LocalRecycler(Listactivity.this, listItems));
                    recyclerViewAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(Listactivity.this, "No such Document", Toast.LENGTH_LONG).show();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(SocialActivity.this, "No such document", Toast.LENGTH_LONG).show();
            }
        });
    }
}

