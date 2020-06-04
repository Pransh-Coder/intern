package com.example.intern.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.database.PriceChartItemPOJO;
import com.example.intern.databinding.RecyclerItemPriceChartBinding;

import java.util.List;

public class PriceChartAdapter extends RecyclerView.Adapter <PriceChartAdapter.ItemViewHolder>{
	private List<PriceChartItemPOJO> priceChartItemPOJOList;
	private AddItemListener listener;
	public PriceChartAdapter(List<PriceChartItemPOJO> priceChartItemPOJOList, AddItemListener listener){
		this.priceChartItemPOJOList = priceChartItemPOJOList;
		this.listener = listener;
	}
	@NonNull
	@Override
	public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_price_chart, parent, false));
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
		holder.binding.tvItemName.setText(priceChartItemPOJOList.get(position).item);
		holder.binding.tvPrice.setText(Integer.toString(priceChartItemPOJOList.get(position).price));
		holder.binding.ivAdd.setOnClickListener(v -> listener.addItemToList(priceChartItemPOJOList.get(position).item));
	}
	
	@Override
	public int getItemCount() {
		return priceChartItemPOJOList.size();
	}
	
	public interface AddItemListener{
		void addItemToList(String item_name);
	}
	
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		RecyclerItemPriceChartBinding binding;
		ItemViewHolder(@NonNull View itemView) {
			super(itemView);
			binding = RecyclerItemPriceChartBinding.bind(itemView);
		}
	}
}
