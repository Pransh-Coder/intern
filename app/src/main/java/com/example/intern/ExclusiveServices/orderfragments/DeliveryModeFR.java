package com.example.intern.ExclusiveServices.orderfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.ExclusiveServices.OrderTrackService;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.database.local.EssentialOrderEntity;
import com.example.intern.database.local.OrderDB;
import com.example.intern.databinding.FragmentDeliveryModeFRBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DeliveryModeFR extends Fragment {
	
	private FragmentDeliveryModeFRBinding binding;
	private OrderingVM viewModel;
	public DeliveryModeFR() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentDeliveryModeFRBinding.inflate(inflater);
		viewModel = new ViewModelProvider(requireActivity()).get(OrderingVM.class);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SharedPreferences preferences = viewModel.getPrefUtil().getPreferences();
		String userAddress = preferences.getString(SharedPrefUtil.USER_NAME_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_HOUSE_NUMBER, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_STREET_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_AREA_KEY, null) + ", "
				+ preferences.getString(SharedPrefUtil.USER_PIN_CODE_KEY, null) + ", Ph. No. : "
				+ preferences.getString(SharedPrefUtil.USER_PHONE_NO, null);
		binding.etAddress.setText(userAddress);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Set click listeners
		binding.tvTakeaway.setOnClickListener(v -> {
			binding.spinnerTimeSlot.setVisibility(View.GONE);
			binding.etAddress.setVisibility(View.GONE);
			binding.tvTakeaway.setBackgroundColor(Color.GREEN);
			viewModel.setHasChosenHomeDelivery(false);
		});
		binding.tvHomeDel.setOnClickListener(v -> {
			binding.spinnerTimeSlot.setVisibility(View.VISIBLE);
			binding.etAddress.setVisibility(View.VISIBLE);
			binding.tvHomeDel.setBackgroundColor(Color.GREEN);
			viewModel.setHasChosenHomeDelivery(true);
		});
		binding.btnSubmit.setOnClickListener(v -> {
			Map<String, Object> order = new HashMap<>();
			//add timestamp of order
			order.put("timestamp", System.currentTimeMillis());
			//add user phone number
			order.put("userphone", viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_PHONE_NO, null));
			//add order details if there
			if(viewModel.getOrderDetailString()!= null){
				//Upload order string if there
				order.put("orderDet", viewModel.getOrderDetailString());
			}
			//add address if needed with timeslot
			if(viewModel.isHasChosenHomeDelivery()){
				String address = binding.etAddress.getText().toString();
				String timeSlot;
				//Check for time slot
				if(binding.spinnerTimeSlot.getSelectedItemPosition()==0){
					Toast.makeText(requireContext(), "Choose a time slot", Toast.LENGTH_SHORT).show();
					return;
				}else{
					timeSlot = getResources().getStringArray(R.array.timeslots)[binding.spinnerTimeSlot.getSelectedItemPosition()];
				}
				//Check for Address
				if(TextUtils.isEmpty(address) || address.equals("null") || address.length() < 10){
					Toast.makeText(requireContext(), "Enter address", Toast.LENGTH_SHORT).show();
					return;
				}
				//fulfilled conditions for home delivery
				order.put("address", address);
				order.put("time", timeSlot);
			}
			if(viewModel.getOrderImageBitmap()!= null){
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				viewModel.getOrderImageBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
				byte[] imageData = outputStream.toByteArray();
				FirebaseStorage.getInstance().getReference().child("users").child(viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null))
						.child("orders/" + System.currentTimeMillis() + ".png").putBytes(imageData).addOnSuccessListener(taskSnapshot -> {
					taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
						String imageDownloadURI = uri.toString();
						order.put("orderimg", imageDownloadURI);
						//TODO : upload the order on vendor side toggling first
						FirebaseFirestore.getInstance().collection("vendors").document(viewModel.getChosenVendorID()).get().addOnSuccessListener(snapshot -> {
							try{
								boolean toggle = snapshot.getBoolean("toggle");
								toggle = !toggle;
								Map<String, Object> updateToggle = new HashMap<>();
								updateToggle.put("toggle", toggle);
								snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
									uploadOrder(order, snapshot.getReference());
								});
							}catch (Exception e){
								//Did not find toggle, make one
								boolean toggle = false;
								Map<String, Object> updateToggle = new HashMap<>();
								updateToggle.put("toggle", toggle);
								snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
									uploadOrder(order, snapshot.getReference());
								});
							}
						});
					});
				});
			}else{
				//No Image provided, no need for uri
				FirebaseFirestore.getInstance().collection("vendors").document(viewModel.getChosenVendorID()).get().addOnSuccessListener(snapshot -> {
					try{
						boolean toggle = snapshot.getBoolean("toggle");
						toggle = !toggle;
						Map<String, Object> updateToggle = new HashMap<>();
						updateToggle.put("toggle", toggle);
						snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
							uploadOrder(order, snapshot.getReference());
						});
					}catch (Exception e){
						//Did not find toggle, make one
						boolean toggle = false;
						Map<String, Object> updateToggle = new HashMap<>();
						updateToggle.put("toggle", toggle);
						snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
							uploadOrder(order, snapshot.getReference());
						});
					}
				});
			}
		});
	}
	
	private void uploadOrder(Map<String, Object> order, DocumentReference reference) {
		reference.collection("orders").add(order).addOnSuccessListener(documentReference -> {
			EssentialOrderEntity orderEntity = new EssentialOrderEntity(viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null), viewModel.getChosenVendorID(), documentReference.getId(), System.currentTimeMillis());
			OrderDB.getInstance(requireContext()).insertOrder(orderEntity);
			Intent intent = new Intent(requireContext(), OrderTrackService.class);
			intent.putExtra(OrderTrackService.EXTRA_VENDOR_ID, viewModel.getChosenVendorID());
			intent.putExtra(OrderTrackService.EXTRA_ORDER_DOCUMENT_ID, documentReference.getId());
			OrderTrackService.enqueueWork(requireContext(), intent);
			Toast.makeText(requireContext(), "Order made", Toast.LENGTH_SHORT).show();
			requireActivity().finish();
		});
	}
}
