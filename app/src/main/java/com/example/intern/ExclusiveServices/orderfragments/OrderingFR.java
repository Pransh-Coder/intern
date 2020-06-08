package com.example.intern.ExclusiveServices.orderfragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.ExclusiveServices.AllOrders;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.custom.PriceChartDialog;
import com.example.intern.database.PriceChartItemPOJO;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.FragmentOrderingFRBinding;
import com.example.intern.mainapp.MainApp;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class OrderingFR extends Fragment {
	
	private FragmentOrderingFRBinding binding;
	private OrderingVM viewModel;
	private Set<VendorPOJO> vendorPOJOS;
	public OrderingFR() {}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		vendorPOJOS = new ArraySet<>();
		binding = FragmentOrderingFRBinding.inflate(inflater);
		viewModel = new ViewModelProvider(requireActivity()).get(OrderingVM.class);
		viewModel.setImageReceivedListener(b -> {
			if(b){
				binding.ivOrderList.setImageBitmap(viewModel.getOrderImageBitmap());
			}
		});
		return binding.getRoot();
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ProgressDialog loadingDialog = new ProgressDialog(requireContext());
		loadingDialog.setMessage("Looking for vendors in your area");
		loadingDialog.setCancelable(false);
		SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
		String orderList = sharedPreferences.getString("list", "");
		//Toast.makeText(getContext(),orderList,Toast.LENGTH_LONG).show();
		if(!orderList.isEmpty()) {
			//Toast.makeText(getContext(),"in here"+orderList,Toast.LENGTH_LONG).show();
			binding.etOrderDetail.setText(orderList);
		}
		//Dismiss this dialog as soon as the vendor list is loaded
		loadingDialog.show();
		//Fetch the list of vendors in the pin code
		String pinCode = viewModel.getPrefUtil().getPreferences().getString(SharedPrefUtil.USER_PIN_CODE_KEY, null);
		if(pinCode==null){
			Toast.makeText(requireContext(), "Illegal state", Toast.LENGTH_SHORT).show();
			requireActivity().finish();
			return;
		}
		FirebaseFirestore.getInstance().collection("vendorcluster").document(pinCode)
				.get().addOnSuccessListener(snapshot -> {
			Map<String, Object> data = snapshot.getData();
			if(data != null){
				//Found some vendors
				Set<String> vendorIDs = data.keySet();
				Iterator<String> iterator = vendorIDs.iterator();
				while(iterator.hasNext()){
					String thisVendID = iterator.next();
					FirebaseFirestore.getInstance().collection("vendors").document(thisVendID)
							.get().addOnSuccessListener(vendorDataSnap -> {
						String name = vendorDataSnap.getString("stName");
						String phoneNumber = vendorDataSnap.getString("phNo");
						String uid=vendorDataSnap.getString("unique_id");
						VendorPOJO vendorPOJO = new VendorPOJO(thisVendID, name, phoneNumber, uid);
						vendorPOJOS.add(vendorPOJO);
						//Last one
						if(!iterator.hasNext()){
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
		binding.back.setOnClickListener(v -> requireActivity().finish());
		binding.home.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), MainApp.class);
			startActivity(intent);
			requireActivity().finish();
		});
		binding.notofi.setOnClickListener(v -> {
			Intent intent = new Intent(requireContext(), NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		binding.searchVendor.setOnClickListener(v -> {
			String uniqueID = binding.searchVendor.getText().toString();
			CollectionReference vendor = FirebaseFirestore.getInstance().collection("vendors");
			Query query = vendor.whereEqualTo("unique_id", uniqueID);
			query.get().addOnCompleteListener(task -> {
				if (task.isSuccessful()) {
					if(task.getResult() == null){
						Toast.makeText(requireContext(), "Could not Find Vendor", Toast.LENGTH_SHORT).show();
						return;
					}
					for (QueryDocumentSnapshot document : task.getResult()) {
						Toast.makeText(requireContext(), "Found Vendor", Toast.LENGTH_SHORT).show();
						binding.vendorSpinner.setVisibility(View.GONE);
						binding.tvSearchedVendor.setVisibility(View.VISIBLE);
						viewModel.setChosenVendorID(document.getId());
						viewModel.setVendorName(document.getString("stName"));
						binding.tvSearchedVendor.setText("Vendor : " + viewModel.getVendorName() );
					}
				}
			});
		});
		final TextWatcher mTextEditorWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@SuppressLint("SetTextI18n")
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//This sets a textview to the current length
				binding.charCount2.setText(s.length() + "/200");
			}
			public void afterTextChanged(Editable s) {}
		};
		binding.etOrderDetail.addTextChangedListener(mTextEditorWatcher);
	}
	
	//AutoSet is used to determine whether user searched and spinner needs to autoset for the searched vendor
	//Keep in mind that first entry is bogus, so setting the position +1 is needed
	@SuppressLint("SetTextI18n")
	private void proceedNow() {
		//Convert the vendorPOJO set into a workable list now
		List<VendorPOJO> vendorPOJOList = new ArrayList<>(vendorPOJOS);
		//Set an item selected listener after all vendorIDS are added
		List<String> shopNames = new ArrayList<>();
		shopNames.add(0,"Select Vendor Id");
		for(VendorPOJO pojo : vendorPOJOList){
			shopNames.add(pojo.vendorShopName);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, shopNames);
		binding.vendorSpinner.setAdapter(adapter);
		binding.vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position>0) {
					viewModel.setChosenVendorID(vendorPOJOList.get(position-1).vendorID);
					viewModel.setuID(vendorPOJOList.get(position-1).uid);
					viewModel.setVendorName(vendorPOJOList.get(position-1).vendorShopName);
					binding.tvViewPriceList.setVisibility(View.VISIBLE);
				}else{
					//Hide the view price list option
					binding.tvViewPriceList.setVisibility(View.GONE);
					viewModel.setChosenVendorID(null);
					viewModel.setVendorName(null);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		//Get stale order info
		if(viewModel.getOrderImageBitmap() != null){
			binding.ivOrderList.setImageBitmap(viewModel.getOrderImageBitmap());
		}
		if(viewModel.getOrderDetailString() != null){
			binding.etOrderDetail.setText(viewModel.getOrderDetailString());
		}
		if(viewModel.getChosenVendorID() != null){
			binding.vendorSpinner.setVisibility(View.GONE);
			binding.tvSearchedVendor.setVisibility(View.VISIBLE);
			binding.tvSearchedVendor.setText("Vendor : " + viewModel.getVendorName());
			for(int i = 0; i < vendorPOJOList.size(); i++){
				if(vendorPOJOList.get(i).vendorID.equals(viewModel.getChosenVendorID())){
					try {
						//Account for extra string in the beginning and for the fact that the vendor might have been searched
						binding.vendorSpinner.setSelection(i+1);
						binding.vendorSpinner.setVisibility(View.VISIBLE);
						binding.tvSearchedVendor.setVisibility(View.GONE);
					}catch (Exception ignored){}
				}
			}
		}
		//Set a click listener to show the price list to user
		binding.tvViewPriceList.setOnClickListener(v -> {
			if (binding.tvViewPriceList.getVisibility() != View.VISIBLE) throw new AssertionError();
			FirebaseFirestore.getInstance().collection("vendors").document(viewModel.getChosenVendorID()).collection("pricelist")
					.document("list").get().addOnSuccessListener(documentSnapshot -> {
						String priceListJSON = documentSnapshot.getString("jsonlist");
						if(priceListJSON == null){
							Toast.makeText(requireContext(), "Vendor has no price list", Toast.LENGTH_SHORT).show();
						}else{
							Log.d("json", "proceedNow: " + priceListJSON);
							//Convert and show dialog
							Type typeToken = new TypeToken<List<PriceChartItemPOJO>>(){}.getType();
							List<PriceChartItemPOJO> priceChartItemPOJOList = new Gson().fromJson(priceListJSON, typeToken);
							for(PriceChartItemPOJO pojo : priceChartItemPOJOList){
								Log.d("json", "proceedNow: " + pojo.item + " " + pojo.price);
							}
							PriceChartDialog priceChartDialog = new PriceChartDialog(requireContext(), priceChartItemPOJOList, item_name -> {
								//Add this item to the list
								Toast.makeText(requireContext(), "Added Item", Toast.LENGTH_SHORT).show();
								binding.etOrderDetail.append("\n" + item_name);
							});
							priceChartDialog.show(getChildFragmentManager(), "PRICE_CHART");
						}
			});
		});
		//Do all things here
		binding.ivOrderList.setOnClickListener(v -> ImagePicker.Companion.with(requireActivity()).crop().compress(1024).start());
		binding.btnSubmit.setOnClickListener(v -> {
			if(viewModel.getChosenVendorID() == null && viewModel.getVendorName() == null){
				Toast.makeText(requireContext(), "Please Choose a vendor", Toast.LENGTH_SHORT).show();
				return;
			}
			//Forward with collected data
			if(viewModel.getOrderImageBitmap()==null){
				Editable orderDetailEditable = binding.etOrderDetail.getText();
				if(orderDetailEditable==null||orderDetailEditable.length()<5){
					//Need one of the things
					binding.etOrderDetail.setError("Please place order");
				}else{
					//String orderDetail = binding.etOrderDetail.getText().toString();
					SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("list",orderDetailEditable.toString());
					editor.apply();
					viewModel.setOrderDetailString(orderDetailEditable.toString());
					viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
				}
			}else{
				String orderDetail = binding.etOrderDetail.getText().toString();
				if(!TextUtils.isEmpty(orderDetail))viewModel.setOrderDetailString(orderDetail);
				{
					SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("list",orderDetail);editor.apply();
					viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
				}
			}
		});
	}
	
	static class VendorPOJO{
		String vendorID;
		String vendorShopName;
		String vendorPhoneNumber;
		String uid;
		VendorPOJO(String id, String name, String phone,String thisVendID){
			this.vendorID = id;
			this.vendorShopName = name;
			this.vendorPhoneNumber = phone;
			this.uid=thisVendID;
		}
	}
}
