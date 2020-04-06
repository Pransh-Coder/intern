package com.example.intern.vendortest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;

import java.util.ArrayList;
import java.util.List;

public class VendorTrHistRecAdapter extends RecyclerView.Adapter<VendorTrHistRecAdapter.TransViewHolder> {
	List<String> customerCountList = new ArrayList<>();
	List<String> amountList = new ArrayList<>();
	List<String> datentimeList = new ArrayList<>();
	
	public VendorTrHistRecAdapter(List<String> customerCountList, List<String> amountList, List<String> datentimeList){
		this.customerCountList = customerCountList;
		this.amountList = amountList;
		this.datentimeList = datentimeList;
	}
	
	@NonNull
	@Override
	public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_transact_history_recycler_item, parent, false);
		return new TransViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
		holder.mCustomerCount.setText(customerCountList.get(position));
		holder.mAmount.setText(amountList.get(position));
		holder.mDatenTime.setText(datentimeList.get(position));
	}
	
	@Override
	public int getItemCount() {
		return customerCountList.size();
	}
	
	class TransViewHolder extends RecyclerView.ViewHolder{
		TextView mCustomerCount, mAmount, mDatenTime;
		public TransViewHolder(@NonNull View itemView) {
			super(itemView);
			this.mCustomerCount = itemView.findViewById(R.id.tv_no_of_customer);
			this.mAmount = itemView.findViewById(R.id.tv_amount);
			this.mDatenTime = itemView.findViewById(R.id.tv_datentime);
		}
	}
}
