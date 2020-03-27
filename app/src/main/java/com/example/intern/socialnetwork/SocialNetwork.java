package com.example.intern.socialnetwork;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.List;

public class SocialNetwork extends Fragment {
	private static String TAG = SocialNetwork.class.getSimpleName();
	FirestorePagingAdapter<FireStoreUtil.PSUser, ItemViewHolder> adapter;
	FusedLocationProviderClient locationProviderClient;
	String pinCode = null;
	private FragmentSocialNetworkBinding binding;
	
	public SocialNetwork() { }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
		binding = FragmentSocialNetworkBinding.inflate(inflater, container, false);
		final View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		/*UserRepository repository = new UserRepository(getContext(), FireStoreUtil.getFirebaseUser(getContext()).getUid());
		SocialRecycler adapter = new SocialRecycler(getContext(),
				repository.getUserEntity(FireStoreUtil.getFirebaseUser(getContext()).getUid()).user_pin_code);
		RecyclerView.LayoutManager gridManager = new GridLayoutManager(getContext(), 2);
		binding.socialRecycler.setLayoutManager(gridManager);
		binding.socialRecycler.setAdapter(adapter);*/
	}
	
	@Override
	public void onStart() {
		super.onStart();
		locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
		locationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
			Geocoder geocoder = new Geocoder(requireActivity());
			try {
				List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
				if(addresses != null && addresses.size() > 0 ){
					pinCode = addresses.get(0).getPostalCode();
					Log.d(TAG, "onStart: pin code" + pinCode);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Query baseQuery = FireStoreUtil.getDbReference(getContext())
				.collection(FireStoreUtil.USER_COLLECTION_NAME).whereEqualTo("pc", pinCode).limit(50);
		baseQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
			for(DocumentSnapshot snapshot : queryDocumentSnapshots){
				if(snapshot.get("pc").equals(pinCode)){
					Log.d(TAG, "onStart: Found a friend");
				}
				Log.d(TAG, "onStart: queried one snapshot");
			}
		});
		PagedList.Config config = new PagedList.Config.Builder()
				.setPrefetchDistance(10).setPageSize(20).setEnablePlaceholders(false).build();
		FirestorePagingOptions<FireStoreUtil.PSUser> options = new FirestorePagingOptions.Builder<FireStoreUtil.PSUser>()
				.setLifecycleOwner(requireActivity())
				.setQuery(baseQuery, config, FireStoreUtil.PSUser.class)
				.build();
		adapter = new FirestorePagingAdapter<FireStoreUtil.PSUser, ItemViewHolder>(options) {
			@Override
			protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull FireStoreUtil.PSUser model) {
				holder.textView.setText(model.un);
				Log.d(TAG, "onBindViewHolder: created user grid");
			}
			
			@NonNull
			@Override
			public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_social_network, parent, false);
				return new ItemViewHolder(view1);
			}
		};
		RecyclerView.LayoutManager gridManager = new GridLayoutManager(requireActivity(), 2);
		adapter.startListening();
		binding.socialRecycler.setAdapter(adapter);
		binding.socialRecycler.setLayoutManager(gridManager);
	}
	
	@Override
	public void onPause() {
		adapter.stopListening();
		super.onPause();
	}
	
	class ItemViewHolder extends RecyclerView.ViewHolder {
		TextView textView;
		
		public ItemViewHolder(@NonNull View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.tv_user_UID);
		}
	}
}
