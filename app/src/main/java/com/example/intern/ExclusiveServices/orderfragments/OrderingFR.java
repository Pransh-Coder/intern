package com.example.intern.ExclusiveServices.orderfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.ExclusiveServices.AllOrders;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.FragmentOrderingFRBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderingFR extends Fragment {
	
	private FragmentOrderingFRBinding binding;
	private SharedPrefUtil prefUtil;
	private OrderingVM viewModel;
	private List<String> vendorIDS;
	List<VendorPOJO> vendorPOJOS;
	public OrderingFR() {}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		vendorPOJOS = new ArrayList<>();
		vendorIDS = new ArrayList<>();
		binding = FragmentOrderingFRBinding.inflate(inflater);
		viewModel = new ViewModelProvider(requireActivity()).get(OrderingVM.class);
		viewModel.setImageReceivedListener(b -> {
			if(b){
				binding.ivOrderList.setImageBitmap(viewModel.getOrderImageBitmap());
			}
		});
		prefUtil = new SharedPrefUtil(requireContext());
		View view = binding.getRoot();
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ProgressDialog loadingDialog = new ProgressDialog(requireContext());
		loadingDialog.setMessage("Looking for vendors in your area");
		loadingDialog.setCancelable(false);
		//Dismiss this dialog as soon as the vendor list is loaded
		loadingDialog.show();
		//Fetch the list of vendors in the pin code
		FirebaseFirestore.getInstance().collection("vendorcluster").document(prefUtil.getPreferences().getString(SharedPrefUtil.USER_PIN_CODE_KEY, null))
				.get().addOnSuccessListener(snapshot -> {
			Map<String, Object> data = snapshot.getData();
			if(data != null){
				//Found some vendors
				Set<String> vendorIdSet = data.keySet();
				Object[] vendorIDs = vendorIdSet.toArray();
				for(Object vendorId : vendorIDs){
					//Add the vendor to list
					vendorIDS.add(vendorId.toString());
					FirebaseFirestore.getInstance().collection("vendors").document(vendorId.toString())
							.get().addOnSuccessListener(vendorDataSnap -> {
						String name = vendorDataSnap.getString("stName");
						String phoneNumber = vendorDataSnap.getString("phNo");
						VendorPOJO vendorPOJO = new VendorPOJO(vendorId.toString(), name, phoneNumber);
						vendorPOJOS.add(vendorPOJO);
						if(vendorId.equals(vendorIDs[vendorIDs.length-1])){
							//Last one
							loadingDialog.dismiss();
							proceedNow();
						}
					});
				}
			}else{
				Toast.makeText(requireContext(), "No Vendors Found!", Toast.LENGTH_SHORT).show();
				requireActivity().finish();
			}
		});
		binding.btnYourOrders.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), AllOrders.class);
			startActivity(intent);
		});
	}
	
	
	public void proceedNow() {
		//Set an item selected listener after all vendorIDS are added
		List<String> shopNames = new ArrayList<>();
		for(VendorPOJO pojo : vendorPOJOS){
			shopNames.add(pojo.vendorShopName);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, shopNames);
		binding.vendorSpinner.setAdapter(adapter);
		binding.vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				viewModel.setChosenVendorID(vendorPOJOS.get(position).vendorID);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		//Do all things here
		binding.ivOrderList.setOnClickListener(v -> ImagePicker.Companion.with(requireActivity()).crop().compress(1024).start());
		binding.btnSubmit.setOnClickListener(v -> {
			//Forward with collected data
			if(viewModel.getOrderImageBitmap()==null){
				Editable orderDetailEditable = binding.etOrderDetail.getText();
				if(orderDetailEditable==null||orderDetailEditable.length()<5){
					//Need one of the things
					binding.etOrderDetail.setError("Please place order");
				}else{
					viewModel.setOrderDetailString(orderDetailEditable.toString());
					viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
				}
			}else{
				String orderDetail = binding.etOrderDetail.getText().toString();
				if(!TextUtils.isEmpty(orderDetail))viewModel.setOrderDetailString(orderDetail);
				viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
			}
		});
	}
	
	class VendorPOJO{
		public String vendorID;
		public String vendorShopName;
		public String vendorPhoneNumber;
		public VendorPOJO(String id, String name, String phone){
			this.vendorID = id;
			this.vendorShopName = name;
			this.vendorPhoneNumber = phone;
		}
	}
}
