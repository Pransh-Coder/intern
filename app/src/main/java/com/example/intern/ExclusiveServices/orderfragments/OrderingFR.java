package com.example.intern.ExclusiveServices.orderfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.FragmentOrderingFRBinding;
import com.example.intern.mainapp.MainApp;
import com.facebook.share.Share;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class OrderingFR extends Fragment {
	
	private FragmentOrderingFRBinding binding;
	private OrderingVM viewModel;
	private List<VendorPOJO> vendorPOJOS;
	private VendorPOJO vendorPOJO;
	public OrderingFR() {}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		vendorPOJOS = new ArrayList<>();
		binding = FragmentOrderingFRBinding.inflate(inflater);
		viewModel = new ViewModelProvider(requireActivity()).get(OrderingVM.class);
		viewModel.setImageReceivedListener(b -> {
			if(b){
				binding.ivOrderList.setImageBitmap(viewModel.getOrderImageBitmap());
			}
		});
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ProgressDialog loadingDialog = new ProgressDialog(requireContext());
		loadingDialog.setMessage("Looking for vendors in your area");
		loadingDialog.setCancelable(false);
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
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
						VendorPOJO vendorPOJO = new VendorPOJO(thisVendID, name, phoneNumber);
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
			String vendorPhone=binding.searchVendor.getText().toString();
			// Create a reference to the cities collection
			CollectionReference vendor = FirebaseFirestore.getInstance().collection("vendors");
// Create a query against the collection.
			Query query = vendor.whereEqualTo("phNo", vendorPhone);
// retrieve  query results asynchronously using query.get()
			query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

				@Override
				public void onComplete(@NonNull Task<QuerySnapshot> task) {
					if (task.isSuccessful()) {
						for (QueryDocumentSnapshot document : task.getResult()) {
							viewModel.setChosenVendorID(document.getId());
						}
					}
				}
			});
		});
		final TextWatcher mTextEditorWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//This sets a textview to the current length
				binding.charCount2.setText(String.valueOf(s.length())+"/200");
			}

			public void afterTextChanged(Editable s) {
			}
		};
		binding.etOrderDetail.addTextChangedListener(mTextEditorWatcher);

	}
	
	
	private void proceedNow() {
		//Set an item selected listener after all vendorIDS are added
		List<String> shopNames = new ArrayList<>();
		shopNames.add(0,"Select Vendor Id");
		for(VendorPOJO pojo : vendorPOJOS){
			shopNames.add(pojo.vendorShopName);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, shopNames);
		binding.vendorSpinner.setAdapter(adapter);
		binding.vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position>0) {
					vendorPOJO = vendorPOJOS.get(position-1);
					viewModel.setChosenVendorID(vendorPOJOS.get(position-1).vendorID);
				}
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
					//String orderDetail = binding.etOrderDetail.getText().toString();
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("list",orderDetailEditable.toString());
					Boolean t=editor.commit();
					viewModel.setOrderDetailString(orderDetailEditable.toString());
					viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
				}
			}else{
				String orderDetail = binding.etOrderDetail.getText().toString();
				if(!TextUtils.isEmpty(orderDetail))viewModel.setOrderDetailString(orderDetail);
				{
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prevOrder", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("list",orderDetail);
					Boolean t=editor.commit();
					viewModel.getNavController().navigate(R.id.action_orderingFR_to_deliveryModeFR);
				}
			}
		});
	}
	
	class VendorPOJO{
		String vendorID;
		String vendorShopName;
		String vendorPhoneNumber;
		VendorPOJO(String id, String name, String phone){
			this.vendorID = id;
			this.vendorShopName = name;
			this.vendorPhoneNumber = phone;
		}
	}
}
