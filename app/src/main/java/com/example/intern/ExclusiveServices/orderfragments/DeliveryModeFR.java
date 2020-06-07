package com.example.intern.ExclusiveServices.orderfragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.ExclusiveServices.OrderTrackService;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.database.local.EssentialOrderEntity;
import com.example.intern.database.local.OrderDB;
import com.example.intern.databinding.FragmentDeliveryModeFRBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryModeFR extends Fragment {
	
	private FragmentDeliveryModeFRBinding binding;
	private OrderingVM viewModel;
	private static final int BUFFER_TIME_IN_HOURS = 4;
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
		binding.back.setOnClickListener(v -> viewModel.getNavController().navigateUp());
		binding.home.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
	}
	
	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onResume() {
		super.onResume();
		setUpTimeSlots();
		//Set click listeners
		binding.constraintTakeaway.setOnClickListener(v -> {
			binding.btnSubmit.setVisibility(View.VISIBLE);
			binding.linearTimeSlot.setVisibility(View.GONE);
			binding.etAddress.setVisibility(View.GONE);
			binding.constraintTakeaway.setBackground(requireActivity().getDrawable(R.drawable.rectangle_green_outline));
			binding.constraintHomeDelivery.setBackground(requireActivity().getDrawable(R.drawable.rectangle_outline));
			viewModel.setHasChosenHomeDelivery(false);
		});
		binding.constraintHomeDelivery.setOnClickListener(v -> {
			binding.btnSubmit.setVisibility(View.VISIBLE);
			binding.linearTimeSlot.setVisibility(View.VISIBLE);
			binding.etAddress.setVisibility(View.VISIBLE);
			binding.constraintHomeDelivery.setBackground(requireActivity().getDrawable(R.drawable.rectangle_green_outline));
			binding.constraintTakeaway.setBackground(requireActivity().getDrawable(R.drawable.rectangle_outline));
			viewModel.setHasChosenHomeDelivery(true);
		});
		binding.btnSubmit.setOnClickListener(v -> {
			Map<String, Object> order = new HashMap<>();
			//add timestamp of order
			order.put("timestamp", System.currentTimeMillis());
			//add user phone number
			order.put("userphone", viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_PHONE_NO, null));
			//Add user UID
			order.put("useruid", viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null));
			//Send user name explicitly
			order.put("username", viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, null));
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
					timeSlot = (String) binding.spinnerTimeSlot.getSelectedItem();
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
			//Wait dialog
			ProgressDialog dialog = new ProgressDialog(requireContext());
			dialog.setTitle(R.string.please_wait);
			dialog.setMessage("Uploading your request");
			dialog.setIcon(R.drawable.pslogotrimmed);
			dialog.setCancelable(false);
			dialog.show();
			if(viewModel.getOrderImageBitmap()!= null){
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				viewModel.getOrderImageBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
				byte[] imageData = outputStream.toByteArray();
				FirebaseStorage.getInstance().getReference().child("users").child(viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_UID_KEY,null))
						.child("orders/" + System.currentTimeMillis() + ".png").putBytes(imageData).addOnSuccessListener(taskSnapshot -> {
					taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
						String imageDownloadURI = uri.toString();
						order.put("orderimg", imageDownloadURI);
						FirebaseFirestore.getInstance().collection("vendors").document(viewModel.getChosenVendorID()).get().addOnSuccessListener(snapshot -> {
							try{
								long ordercount = snapshot.getLong("total_orders");
								ordercount++;
								Map<String, Object> updateToggle = new HashMap<>();
								updateToggle.put("total_orders", ordercount);
								snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
									uploadOrder(order, snapshot.getReference(), dialog);
								});
							}catch (Exception e){
								//Did not find toggle, make one
								long ordercount = 0L;
								Map<String, Object> updateToggle = new HashMap<>();
								updateToggle.put("total_orders", ordercount);
								snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
									uploadOrder(order, snapshot.getReference(), dialog);
								});
							}
						});
					});
				});
			}else{
				//No Image provided, no need for uri
				FirebaseFirestore.getInstance().collection("vendors").document(viewModel.getChosenVendorID()).get().addOnSuccessListener(snapshot -> {
					try{
						long ordercount = snapshot.getLong("total_orders");
						ordercount++;
						Map<String, Object> updateToggle = new HashMap<>();
						updateToggle.put("total_orders", ordercount);
						snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
							uploadOrder(order, snapshot.getReference(), dialog);
						});
					}catch (Exception e){
						//Did not find toggle, make one
						long ordercount = 0L;
						Map<String, Object> updateToggle = new HashMap<>();
						updateToggle.put("total_orders", ordercount);
						snapshot.getReference().update(updateToggle).addOnSuccessListener(aVoid -> {
							uploadOrder(order, snapshot.getReference(), dialog);
						});
					}
				});
			}
		});
	}
	
	private void setUpTimeSlots() {
		//TODO : Give a logic to hide previous time slots and hide really near time slots,
		// Give at least 4 hours between choose-able time slot and time of placing order
		List<String> availableTimeSlots = Arrays.asList(getResources().getStringArray(R.array.timeslots));
		List<String> timeSlots = new ArrayList<>(availableTimeSlots);
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
		int currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
		int currentMinute = calendar.get(Calendar.MINUTE);
		//if minutes are more than 15, increment hour for the logic
		if(currentMinute >= 15)currentHour++;
		//Now counting from this current hour, increment 4 hours for a buffer time for vendor
		currentHour+=BUFFER_TIME_IN_HOURS;
		//Check if it exceeded 24 hour limit
		if(currentHour >= 24){
			currentHour = currentHour%24;
		}
		//Now perform checks if this hour lies in any of the lastly available slots, if not , show all, means next day
		if(currentHour >= 12){
			//Missed first slot
			timeSlots.remove(0);
		}
		if(currentHour >= 17){
			//Missed second time slot too
			timeSlots.remove(0);
		}
		if(currentHour >= 21){
			//Missed the last one too, show all now, for next day
			timeSlots.clear();
			Log.d("TimeSlot", "onResume: removed all time slots, adding again for next day");
			timeSlots.addAll(availableTimeSlots);
			binding.tvWarningDelayDelivery.setVisibility(View.VISIBLE);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, timeSlots);
		binding.spinnerTimeSlot.setAdapter(adapter);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void uploadOrder(Map<String, Object> order, DocumentReference reference, ProgressDialog waitDialog) {
		reference.collection("orders").add(order).addOnSuccessListener(documentReference -> {
			EssentialOrderEntity orderEntity = new EssentialOrderEntity(viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null), viewModel.getuID(), documentReference.getId(), LocalDate.now().getMonth().toString(),System.currentTimeMillis(),viewModel.getChosenVendorID());
			OrderDB.getInstance(requireContext()).insertOrder(orderEntity);
			Intent intent = new Intent(requireContext(), OrderTrackService.class);
			intent.putExtra(OrderTrackService.EXTRA_VENDOR_ID, viewModel.getChosenVendorID());
			intent.putExtra(OrderTrackService.EXTRA_ORDER_DOCUMENT_ID, documentReference.getId());
			OrderTrackService.enqueueWork(requireContext(), intent);
			waitDialog.dismiss();
			new AlertDialog.Builder(requireContext()).setTitle(getString(R.string.thank_you))
					.setMessage("Your order has been placed").setPositiveButton("OK", null)
					.setOnDismissListener(dialog -> requireActivity().finish())
					.setCancelable(false).setIcon(R.drawable.pslogotrimmed).show();
		});
	}
}
