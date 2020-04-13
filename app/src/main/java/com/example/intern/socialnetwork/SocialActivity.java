package com.example.intern.socialnetwork;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;
import com.example.intern.databinding.FragmentSocialNetworkBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialActivity extends AppCompatActivity {
	FusedLocationProviderClient locationProviderClient;
	String pinCode = null;
	private FragmentSocialNetworkBinding binding;
	FirebaseFirestore fstore;
	String uid;
	String name,occ;
	private ListView list_view;
	private ArrayAdapter<String> arrayadapter;
	private ArrayList<String> listOfFriend = new ArrayList<>();
	Set<String> set = new HashSet<>();
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		list_view = (ListView) findViewById(R.id.list_view);
		fstore = FirebaseFirestore.getInstance();
		uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		progressDialog = new ProgressDialog(SocialActivity.this);
		initializefileds();
		retrivePincode();


	}

	private void initializefileds() {
		arrayadapter = new ArrayAdapter<String>(SocialActivity.this, android.R.layout.simple_list_item_1, listOfFriend);
		list_view.setAdapter(arrayadapter);


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
						Toast.makeText(SocialActivity.this,"We got some Friends for you!! ", Toast.LENGTH_LONG).show();
						progressDialog.dismiss();

						for(String user:list)
						{
							getUsername(user);
							//Toast.makeText(SocialActivity.this, user, Toast.LENGTH_LONG).show();

						}

						/*while (iterator.hasNext()) {
							String user= String.valueOf(iterator.hasNext());
							getUsername(user);
							Toast.makeText(SocialActivity.this, user, Toast.LENGTH_LONG).show();

						}
						//setValuetolist();

                       */

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
					name = documentSnapshot.getString("un");
					occ=documentSnapshot.getString("occ");

					//Toast.makeText(SocialActivity.this,name,Toast.LENGTH_LONG).show();
					set.add(name);
					//set.add(occ);
					listOfFriend.add(name);
					//listOfFriend.add(occ);
					arrayadapter.notifyDataSetChanged();


				} else {
					//Toast.makeText(SocialActivity.this, "No such document in ", Toast.LENGTH_LONG).show();
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
//}
