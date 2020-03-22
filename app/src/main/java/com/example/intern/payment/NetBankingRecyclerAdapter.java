package com.example.intern.payment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;

import java.util.List;

public class NetBankingRecyclerAdapter extends RecyclerView.Adapter<NetBankingRecyclerAdapter.ViewHolder> {
	private static String TAG = "NetbankingRecycler";
	public int lastSelectedItem = -1;
	public boolean hasSelected = false;
	private Context context;
	private List<String> bankIDs;
	private List<String> bankNames;
	
	public NetBankingRecyclerAdapter(Context context, List<String> bankIDs, List<String> bankNames){
		this.context = context;
		this.bankIDs = bankIDs;
		this.bankNames = bankNames;
	}
	
	public String getBankID(){
		if(hasSelected){
			return bankIDs.get(lastSelectedItem);
		}else{
			//TODO: This means user hasn't selected any bank but still proceeded
			return null;
		}
	}
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.netbanking_recycler_item, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.mSelection.setChecked( position == lastSelectedItem);
		holder.mBankName.setText(bankNames.get(position));
	}
	
	@Override
	public int getItemCount() {
		return bankNames.size();
	}
	
	class ViewHolder extends RecyclerView.ViewHolder{
		RadioButton mSelection;
		TextView mBankName;
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			mSelection = itemView.findViewById(R.id.bank_selection);
			mBankName = itemView.findViewById(R.id.bank_name);
			View.OnClickListener l = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					lastSelectedItem = getAdapterPosition();
					notifyDataSetChanged();
					hasSelected = true;
					Log.d(TAG, "onClick: view clicked");
				}
			};
			itemView.setOnClickListener(l);
			mSelection.setOnClickListener(l);
		}
	}
}