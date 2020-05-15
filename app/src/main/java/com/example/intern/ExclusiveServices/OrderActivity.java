package com.example.intern.ExclusiveServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.database.local.EssentialOrderEntity;
import com.example.intern.database.local.OrderDB;
import com.example.intern.databinding.ActivityOrderBinding;
import com.example.intern.databinding.RecyclerItemVendorSingleItemBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
	private static final String TAG = OrderActivity.class.getSimpleName();
	public static final String EXTRA_VENDOR_ID = "vend_id";
	ActivityOrderBinding binding;
	ItemRecyclerAdapter recyclerAdapter;
	SharedPrefUtil prefUtil;
	private DocumentReference thisRequestDocRef;
	private boolean hasSubmitted;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityOrderBinding.inflate(getLayoutInflater());
		prefUtil = new SharedPrefUtil(this);
		String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
		if(UID == null){
			Toast.makeText(this, "Unknown User!", Toast.LENGTH_SHORT).show();
			return;
		}
		setContentView(binding.getRoot());
		final Context context = this;
		String vendorID = getIntent().getStringExtra(EXTRA_VENDOR_ID);
		if(vendorID==null){
			Toast.makeText(this, "Unaccepted Entry", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		//Create the recycler adapter with an  empty state
		recyclerAdapter = new ItemRecyclerAdapter();
		binding.recyclerItems.setLayoutManager(new LinearLayoutManager(this));
		binding.recyclerItems.setAdapter(recyclerAdapter);
		binding.btnAddItem.setOnClickListener(v -> {
			recyclerAdapter.items.add("");
			recyclerAdapter.quants.add("");
			if(!recyclerAdapter.items.isEmpty()){
				recyclerAdapter.notifyItemInserted(recyclerAdapter.items.size()-1);
			}
		});
		
		//Getting a formatted address for delivery
		SharedPreferences preferences = prefUtil.getPreferences();
		String userAddress = preferences.getString(SharedPrefUtil.USER_HOUSE_NUMBER, null)
				+ preferences.getString(SharedPrefUtil.USER_STREET_KEY, null)
				+ preferences.getString(SharedPrefUtil.USER_AREA_KEY, null)
				+ preferences.getString(SharedPrefUtil.USER_PIN_CODE_KEY, null);
		binding.btnSubmit.setOnClickListener(v -> {
			List<String> items = recyclerAdapter.items;
			List<String> quantities = recyclerAdapter.quants;
			Map<String, Object> order = new HashMap<>();
			//Used for vendor accepting or rejecting
			order.put("vendorstat", true);
			//Used for user accepting or rejecting
			order.put("userstat", true);
			//Used when the bill is uploaded
			order.put("finalstat", false);
			//Delivery address
			order.put("deliveradd", userAddress);
			order.put("items", items);
			order.put("quants", quantities);
			FirebaseFirestore.getInstance().collection("vendors").document(vendorID)
					.collection("orders").add(order).addOnSuccessListener(documentReference -> {
						thisRequestDocRef = documentReference;
						hasSubmitted = true;
						if(documentReference != null){
							EssentialOrderEntity orderEntity = new EssentialOrderEntity(UID, vendorID, documentReference.getId()
							,System.currentTimeMillis(), new Gson().toJson(items), new Gson().toJson(quantities));
							OrderDB.getInstance(context).insertOrder(orderEntity);
							Log.d(TAG, "onCreate: Created an entity in the local DB");
						}
					});
		});
		/*binding.btnSubmit.setOnClickListener(v -> {
			*//*String item1 = binding.etItem1.getText().toString();
			String item2 = binding.etItem2.getText().toString();*//*
			*//*String quantity1 = binding.etQuant1.getText().toString();
			String quantity2 = binding.etQuant2.getText().toString();*//*
			Map<String, Object> request = new HashMap<>();
			request.put("items", items);
//			request.put("quants", quantities);
			//Start with ideal situation where both accept the transaction
			request.put("userStat", true);
			request.put("vendorStat", true);
			//Final flag to tell vendor that user accepted the order
			request.put("deliver", false);
			FirebaseFirestore.getInstance().collection("vendors").document(vendorID)
					.collection("orders").add(request).addOnSuccessListener(documentReference -> {
						thisRequestDocRef = documentReference;
						//Add a listener to listen for events
						documentReference.addSnapshotListener((snapshot, e) -> {
							if(snapshot!= null && snapshot.getBoolean("vendorStat")){
								//Vendor accepted the request, show prices
								List<String> prices = (List<String>) snapshot.get("prices");
								if(prices != null && !prices.isEmpty()){
									*//*binding.etPrice1.setText(prices.get(0));
									binding.etPrice2.setText(prices.get(1));*//*
									binding.btnSubmit.setVisibility(View.GONE);
								}
							}
						});
					});
		});*/
	}
	
	@Override
	protected void onDestroy() {
		if(hasSubmitted){
			Intent intent = new Intent(this, OrderTrackService.class);
			intent.putExtra(OrderTrackService.EXTRA_ORDER_DOCUMENT_ID, thisRequestDocRef.getId());
			intent.putExtra(OrderTrackService.EXTRA_VENDOR_ID, getIntent().getStringExtra(EXTRA_VENDOR_ID));
			OrderTrackService.enqueueWork(this, intent);
			Log.d(TAG, "onDestroy: Attached a listener");
		}
		super.onDestroy();
	}
	
	static class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder>{
		private final String TAG = ItemRecyclerAdapter.class.getSimpleName();
		List<String> items, quants;
		
		ItemRecyclerAdapter(){
			items = new ArrayList<>();
			quants = new ArrayList<>();
		}
		
		@NonNull
		@Override
		public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_vendor_single_item, parent, false));
		}
		
		@Override
		public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
			holder.binding.etItemName.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void afterTextChanged(Editable s) {
					items.remove(position);
					items.add(position, s.toString());
					Log.d(TAG, "afterTextChanged: Added" + s.toString() + " at " + position);
				}
			});
			holder.binding.etQuantity.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void afterTextChanged(Editable s) {
					quants.remove(position);
					quants.add(position, s.toString());
					Log.d(TAG, "afterTextChanged: Added quant at same pos");
				}
			});
		}
		
		@Override
		public int getItemCount() {
			return items.size();
		}
		
		static class ItemViewHolder extends RecyclerView.ViewHolder{
			RecyclerItemVendorSingleItemBinding binding;
			
			ItemViewHolder(@NonNull View itemView) {
				super(itemView);
				binding = RecyclerItemVendorSingleItemBinding.bind(itemView);
			}
		}
	}
}
