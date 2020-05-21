package com.example.intern.ExclusiveServices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.ExclusiveServices.orderfragments.OrderingVM;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityOrderBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class OrderActivity extends AppCompatActivity {
	public static final String EXTRA_VENDOR_ID = "vend_id";
	ActivityOrderBinding binding;
	OrderingVM viewModel;
	SharedPrefUtil prefUtil;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = new ViewModelProvider(this).get(OrderingVM.class);
		binding = ActivityOrderBinding.inflate(getLayoutInflater());
		prefUtil = new SharedPrefUtil(this);
		viewModel.setPrefUtil(prefUtil);
		setContentView(binding.getRoot());
		NavController navController = Navigation.findNavController(this, R.id.ordering_nav_host);
		viewModel.setNavController(navController);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			//Got the image
			Bitmap orderBitmap = BitmapFactory.decodeFile(ImagePicker.Companion.getFilePath(data));
			viewModel.setOrderImageBitmap(orderBitmap);
			viewModel.getImageReceivedListener().hasReceivedImage(true);
		}
	}
	
	@Override
	public void onBackPressed() {
		if(viewModel.getNavController().navigateUp()){
			Log.i("OrderActivity", "onBackPressed: popped fragment");
		}else{
			super.onBackPressed();
		}
	}
	
	/*@Override
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
		//Get the time slot
		binding.spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0){
					timeSlot = getResources().getStringArray(R.array.timeslots)[position];
				}else{
					//Ignore the first entry
					timeSlot = null;
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				timeSlot = null;
			}
		});
		//Show the spinner only in case of home delivery
		binding.radBtnCod.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if(isChecked){
				hasOptedHomeDelivery = true;
				binding.tvUselessTime.setVisibility(View.VISIBLE);
				binding.spinnerTimeSlot.setVisibility(View.VISIBLE);
			}else{
				hasOptedHomeDelivery = false;
				binding.tvUselessTime.setVisibility(View.GONE);
				binding.spinnerTimeSlot.setVisibility(View.GONE);
			}
		});
		
		//Getting a formatted address for delivery
		SharedPreferences preferences = prefUtil.getPreferences();
		String userAddress = preferences.getString(SharedPrefUtil.USER_NAME_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_HOUSE_NUMBER, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_STREET_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_AREA_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_PIN_CODE_KEY, null) + ", Ph. No. : "
				+ preferences.getString(SharedPrefUtil.USER_PHONE_NO, null);
		binding.btnSubmit.setOnClickListener(v -> {
			List<String> items = recyclerAdapter.items;
			List<String> quantities = recyclerAdapter.quants;
			Map<String, Object> order = new HashMap<>();
			if(binding.radioDeliveryTypes.getCheckedRadioButtonId() == -1){
				//Hasn't checked anything
				Toast.makeText(context, "Choose A Delivery Type !", Toast.LENGTH_SHORT).show();
				return;
			}
			if(binding.radioDeliveryTypes.getCheckedRadioButtonId() == R.id.rad_btn_cod){
				//Cash on delivery opted
				order.put("homedel", true);
			}else{
				//Takeaway opted
				order.put("homedel", false);
			}
			if(timeSlot == null && hasOptedHomeDelivery){
				//Hasn't choosen a timeslot
				Toast.makeText(context, "Pick a preferred delivery time", Toast.LENGTH_SHORT).show();
				return;
			}
			order.put("time", timeSlot);
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
							//Change the value of toggle first
							DocumentReference vendorDocRef = FirebaseFirestore.getInstance().collection("vendors").document(vendorID);
							vendorDocRef.get().addOnSuccessListener(snapshot -> {
								try{
									boolean toggleVal = snapshot.getBoolean("toggle");
									toggleVal = !toggleVal;
									Map<String, Object> updateToggle = new HashMap<>();
									updateToggle.put("toggle", toggleVal);
									vendorDocRef.update(updateToggle).addOnSuccessListener(voiD->{
										Toast.makeText(context, "Order made successfully", Toast.LENGTH_SHORT).show();
										finish();
									});
								}catch (Exception e){
									//toggle field not there, make one
									Map<String, Object> updateToggle = new HashMap<>();
									updateToggle.put("toggle", true);
									vendorDocRef.update(updateToggle).addOnSuccessListener(VoiD->{
										Toast.makeText(context, "Order made successfully", Toast.LENGTH_SHORT).show();
										finish();
									});
								}
							});
						}
					});
		});
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
		List<String> items, quants;
		ItemRecyclerAdapter(){
			items = new ArrayList<>();
			quants = new ArrayList<>();
		}
		
		@NonNull
		@Override
		public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_vendor_single_item, parent, false);
			return new ItemViewHolder(view);
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
					if(items.isEmpty()){
						items.add(s.toString());
					}else{
						items.set(position, s.toString());
					}
				}
			});
			holder.binding.etQuantity.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(quants.isEmpty()){
						quants.add(s.toString());
					}else {
						quants.set(position, s.toString());
					}
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
	}*/
}
