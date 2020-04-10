package com.example.intern.socialnetwork;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.databinding.FragmentSocialNetworkBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class SocialNetwork extends Fragment {
	private static String TAG = SocialNetwork.class.getSimpleName();
	FusedLocationProviderClient locationProviderClient;
	String pinCode = null;
	private FragmentSocialNetworkBinding binding;
	FirebaseFirestore fstore;
	String uid=null;


	public SocialNetwork() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
		binding = FragmentSocialNetworkBinding.inflate(inflater, container, false);
		final View view = binding.getRoot();
		initializefileds();
		retrivePincode();
		fstore= FirebaseFirestore.getInstance();
		uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
		return view;

	}

	private void initializefileds() {


	}

	private void retrivePincode() {
		DocumentReference docRef = fstore.collection("Users").document(uid);

		docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {

						pinCode=document.getString("pin");
						retriveClust();

					} else {
						Log.d(TAG, "No such document");
					}
				} else {
					Log.d(TAG, "get failed with ", task.getException());
				}
			}
		});
	}

	private void retriveClust() {
		DocumentReference docRefUclust = fstore.collection("Uclust").document(pinCode);
		docRefUclust.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {
						Map<String, Object> data = new HashMap<>();
						data.put(document.getId(),document.getData());
						Iterator iterator=data.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry mapElement = (Map.Entry)iterator.next();
							String userid=mapElement.getValue().toString();
							Toast.makeText(getContext(),userid,Toast.LENGTH_LONG).show();
						}



					} else {
						Log.d(TAG, "No such document");
					}
				} else {
					Log.d(TAG, "get failed with ", task.getException());
				}
			}
		});


	}



}
