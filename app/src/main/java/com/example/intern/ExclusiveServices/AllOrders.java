package com.example.intern.ExclusiveServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllOrders extends AppCompatActivity {
	
	ActivityAllOrdersBinding binding;
	List<EssentialOrderEntity> orderEntityList;
	ArrayAdapter<String> adapterspinner;
	ArrayList<String> spinnerDataList;
	
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
		spinnerDataList=new ArrayList<>();
		spinnerDataList.add(0,"Select Vendor Id");
		spinnerDataList.addAll(OrderDB.getInstance(this).getVid());
		adapterspinner=new ArrayAdapter<String>(AllOrders.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
		binding.orderspinner.setAdapter(adapterspinner);

		binding.orderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position==0)
					orderEntityList = OrderDB.getInstance(getApplicationContext()).getOrders();
				else {
					String vid = null;
					if (binding.orderspinner != null && binding.orderspinner.getSelectedItem() != null) {
						vid = (String) binding.orderspinner.getSelectedItem();
						Toast.makeText(getApplicationContext(), vid, Toast.LENGTH_LONG).show();
						orderEntityList = OrderDB.getInstance(getApplicationContext()).getVidOrders(vid);
					}
				}
						if (orderEntityList == null || orderEntityList.isEmpty()) {
							Toast.makeText(getApplicationContext(), "Never placed any order !", Toast.LENGTH_SHORT).show();
							finish();
							return;
						}
						else {

						}

						OrderRecyclerAdapter adapter;
						adapter = new OrderRecyclerAdapter(getApplicationContext(), orderEntityList);
						binding.recyclerUserOrders.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
						binding.recyclerUserOrders.setAdapter(adapter);
					}


			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

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
