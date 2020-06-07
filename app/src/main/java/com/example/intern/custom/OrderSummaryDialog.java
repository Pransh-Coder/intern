package com.example.intern.custom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.intern.R;
import com.example.intern.databinding.DialogOrderSummaryBinding;

import javax.annotation.Nullable;

public class OrderSummaryDialog extends DialogFragment {
	private Context context;
	private boolean optedHomeDelivery;
	private String vendorName, orderDetails, optedTimeSlot;
	private Bitmap orderListBitmap;
	private MakeOrderListener listener;
	public OrderSummaryDialog(@NonNull Context context, @NonNull String vendorName, @Nullable String orderDetails, @NonNull boolean optedHomeDelivery, @Nullable String optedTimeSlot, @Nullable Bitmap orderListBitmap, @NonNull MakeOrderListener listener){
		this.context = context;
		this.vendorName = vendorName;
		this.orderDetails = orderDetails;
		this.optedHomeDelivery = optedHomeDelivery;
		this.optedTimeSlot = optedTimeSlot;
		this.orderListBitmap = orderListBitmap;
		this.listener = listener;
	}
	
	@SuppressLint("SetTextI18n")
	@NonNull
	@Override
	public Dialog onCreateDialog(@androidx.annotation.Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		com.example.intern.databinding.DialogOrderSummaryBinding binding = DialogOrderSummaryBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_order_summary, null));
		builder.setView(binding.getRoot());
		//Populate UI of the dialog
		binding.tvVendorName.setText(vendorName);
		if(orderDetails != null){
			binding.linearTextOrderDetails.setVisibility(View.VISIBLE);
			binding.tvOrderDetail.setText(orderDetails);
		}
		if(orderListBitmap != null){
			binding.linearOrderImage.setVisibility(View.VISIBLE);
			binding.ivOrderList.setImageBitmap(orderListBitmap);
		}
		if(optedHomeDelivery){
			binding.tvDeliveryMode.setText("Home Delivery");
			if(optedTimeSlot != null){
				binding.linearTimeSlot.setVisibility(View.VISIBLE);
				binding.tvTimeSlot.setText(optedTimeSlot);
			}
		}else{
			binding.tvDeliveryMode.setText("Take Away");
		}
		builder.setPositiveButton("OK", ((dialog, which) -> listener.makeOrder(true)));
		builder.setNeutralButton("CANCEL", ((dialog, which) -> listener.makeOrder(false)));
		return builder.create();
	}
	
	public interface MakeOrderListener{
		void makeOrder(boolean canProceed);
	}
}
