package com.example.intern.ExclusiveServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.database.local.EssentialOrderEntity;
import com.example.intern.database.local.OrderDB;
import com.example.intern.databinding.ActivityAllOrdersBinding;
import com.example.intern.databinding.RecyclerOrderDetailItemBinding;

import java.util.List;

public class AllOrders extends AppCompatActivity {
	
	ActivityAllOrdersBinding binding;
	List<EssentialOrderEntity> orderEntityList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAllOrdersBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		orderEntityList = OrderDB.getInstance(this).getOrders();
		if(orderEntityList == null || orderEntityList.isEmpty()){
			Toast.makeText(this, "Never placed any order !", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(this, orderEntityList);
		binding.recyclerUserOrders.setLayoutManager(new LinearLayoutManager(this));
		binding.recyclerUserOrders.setAdapter(adapter);
	}
	
	class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderDetailViewHolder>{
		List<EssentialOrderEntity> essentialOrderEntities;
		Context context;
		OrderRecyclerAdapter(Context context, List<EssentialOrderEntity> essentialOrderEntities){
			this.essentialOrderEntities = essentialOrderEntities;
			this.context = context;
		}
		@NonNull
		@Override
		public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new OrderDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order_detail_item, parent, false));
		}
		
		@SuppressLint("SetTextI18n")
		@Override
		public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
			holder.binding.tvTimestamp.setText(essentialOrderEntities.get(position).order_ID);
			holder.binding.getRoot().setOnClickListener(v -> {
				Intent intent = new Intent(context, OrderDetail.class);
				intent.putExtra(OrderDetail.EXTRA_DOCUMENT_ID_KEY, essentialOrderEntities.get(position).order_ID);
				intent.putExtra(OrderDetail.EXTRA_VENDOR_ID_KEY, essentialOrderEntities.get(position).vendor_ID);
				startActivity(intent);
			});
		}
		
		@Override
		public int getItemCount() {
			return essentialOrderEntities.size();
		}
		
		class OrderDetailViewHolder extends RecyclerView.ViewHolder{
			RecyclerOrderDetailItemBinding binding;
			OrderDetailViewHolder(@NonNull View itemView) {
				super(itemView);
				binding = RecyclerOrderDetailItemBinding.bind(itemView);
			}
		}
	}
}
