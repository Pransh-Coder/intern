package com.example.intern.ExclusiveServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityVendorChosingBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VendorChosingAct extends AppCompatActivity {
	ActivityVendorChosingBinding binding;
	SharedPrefUtil prefUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefUtil = new SharedPrefUtil(this);
		binding = ActivityVendorChosingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		//Populate the List
		ArrayList<String> vendors = new ArrayList<>();
		String pinCode = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PIN_CODE_KEY, null);
		if(pinCode==null){
			Toast.makeText(getApplicationContext(), "Update Pin Code!", Toast.LENGTH_SHORT).show();
			return;
		}
		final Context context = this;
		FirebaseFirestore.getInstance().collection("vendorcluster").document(pinCode).get().addOnSuccessListener(snapshot -> {
			Map<String, Object> data = snapshot.getData();
			if(data != null && !data.isEmpty()){
				Set<String> vendorKeys = data.keySet();
				vendors.addAll(vendorKeys);
				//Have all vendor keys , populate the adapter
				VendorListRecyclerAdapter adapter = new VendorListRecyclerAdapter(context, vendors);
				binding.recyclerVendorList.setLayoutManager(new LinearLayoutManager(context));
				binding.recyclerVendorList.setAdapter(adapter);
			}
		});
	}
	
	class VendorListRecyclerAdapter extends RecyclerView.Adapter<VendorListRecyclerAdapter.VendorVH>{
		Context context;
		List<String> vendorIDs;
		VendorListRecyclerAdapter(Context context, List<String> vendorIDs){
			this.context = context;
			this.vendorIDs = vendorIDs;
		}
		
		@NonNull
		@Override
		public VendorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new VendorVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vendor_list_item, parent, false));
		}
		
		@Override
		public void onBindViewHolder(@NonNull VendorVH holder, int position) {
			holder.mVendorID.setText(vendorIDs.get(position));
			//TODO : Add click listener to send to order activity
			holder.mVendorID.setOnClickListener(v -> {
				Intent intent = new Intent(context, OrderActivity.class);
				intent.putExtra(OrderActivity.EXTRA_VENDOR_ID, vendorIDs.get(position));
				startActivity(intent);
				finish();
			});
		}
		
		@Override
		public int getItemCount() {
			return vendorIDs.size();
		}
		
		class VendorVH extends RecyclerView.ViewHolder{
			TextView mVendorID;
			VendorVH(@NonNull View itemView) {
				super(itemView);
				mVendorID = itemView.findViewById(R.id.tv_vendor_id);
			}
		}
	}
}
