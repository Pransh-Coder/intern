package com.example.intern.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.intern.R;
import com.example.intern.adapters.PriceChartAdapter;
import com.example.intern.database.PriceChartItemPOJO;
import com.example.intern.databinding.DialogPriceChartBinding;

import java.util.List;

public class PriceChartDialog extends DialogFragment implements PriceChartAdapter.AddItemListener {
	private DialogPriceChartBinding binding;
	private List<PriceChartItemPOJO> priceChartItemPOJOList;
	private PriceChartAdapter.AddItemListener listener;
	private Context context;
	public PriceChartDialog(Context context, List<PriceChartItemPOJO> priceChartItemPOJOList, PriceChartAdapter.AddItemListener listener){
		this.context = context;
		this.priceChartItemPOJOList = priceChartItemPOJOList;
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		binding = DialogPriceChartBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_price_chart, null));
		builder.setView(binding.getRoot());
		PriceChartAdapter adapter = new PriceChartAdapter(priceChartItemPOJOList, this);
		binding.recyclerPriceChart.setLayoutManager(new LinearLayoutManager(context));
		binding.recyclerPriceChart.setAdapter(adapter);
		return builder.create();
	}
	
	@Override
	public void addItemToList(String item_name) {
		listener.addItemToList(item_name);
	}
}
