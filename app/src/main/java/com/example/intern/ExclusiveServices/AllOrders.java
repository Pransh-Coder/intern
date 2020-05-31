package com.example.intern.ExclusiveServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.example.intern.R;
import com.example.intern.database.local.EssentialOrderEntity;
import com.example.intern.database.local.OrderDB;
import com.example.intern.databinding.ActivityAllOrdersBinding;
import com.example.intern.databinding.RecyclerOrderDetailItemBinding;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllOrders extends AppCompatActivity {
	
	ActivityAllOrdersBinding binding;
	List<EssentialOrderEntity> orderEntityList;
	ArrayAdapter<String> adapterspinner;
	ArrayList<String> spinnerDataList;
	ArrayAdapter<String> adapterMonthspinner;
	ArrayList<String> spinnerMonthList;
	public static int state;

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
		state=0;
		OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(this, orderEntityList);
		binding.recyclerUserOrders.setLayoutManager(new LinearLayoutManager(this));
		binding.recyclerUserOrders.setAdapter(adapter);
		spinnerMonthList=new ArrayList<>();
		spinnerMonthList.addAll(OrderDB.getInstance(this).getMonth());
        Set<String> hSet = new HashSet<String>();
        hSet.addAll(spinnerMonthList);
        spinnerMonthList=new ArrayList<String>(hSet);
        spinnerMonthList.add(0,"Select Month");
        adapterMonthspinner= new ArrayAdapter<String>(AllOrders.this,android.R.layout.simple_spinner_dropdown_item,spinnerMonthList);
        binding.monthSpinner.setAdapter(adapterMonthspinner);
		spinnerDataList=new ArrayList<>();
		spinnerDataList.addAll(OrderDB.getInstance(this).getVid());
		int len=spinnerDataList.size();
		binding.total.setText("Total Orders:"+len);
		hSet = new HashSet<String>();
        hSet.addAll(spinnerDataList);
        spinnerDataList=new ArrayList<String>(hSet);
        spinnerDataList.add(0,"Select Vendor Id");
		adapterspinner=new ArrayAdapter<String>(AllOrders.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList );
		binding.orderspinner.setAdapter(adapterspinner);

		binding.orderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position==0)
				{
					orderEntityList = OrderDB.getInstance(getApplicationContext()).getOrders();
					state=0;

				}

				else {
					String vid = null;
					if (binding.orderspinner != null && binding.orderspinner.getSelectedItem() != null) {
						state=1;
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
		binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				state=0;
				if(position==0)
					orderEntityList = OrderDB.getInstance(getApplicationContext()).getOrders();
				else {
					String month = null;
					if (binding.monthSpinner != null && binding.monthSpinner.getSelectedItem() != null) {
						month = (String) binding.monthSpinner.getSelectedItem();
						Toast.makeText(getApplicationContext(), month, Toast.LENGTH_LONG).show();
						orderEntityList = OrderDB.getInstance(getApplicationContext()).getMonthOrders(month);
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
			FirebaseFirestore.getInstance().collection("vendors").document(essentialOrderEntities.get(position).vendor_ID).collection("orders")
					.document(essentialOrderEntities.get(position).order_ID).get().addOnSuccessListener(snapshot -> {
						//Show the basic details

								try {
									String total = snapshot.getString("total");
									if(total==null)
										holder.binding.tvAmount.setText("Total : " +"0 "+ "INR");
									else
									holder.binding.tvAmount.setText("Total : " + total + " INR");
								}
									catch(Exception ignored){
									}
					});
			if(state==0) {
				FirebaseFirestore.getInstance().collection("vendors").document(essentialOrderEntities.get(position).v_ID).get().addOnSuccessListener(snapshot -> {
					//Show the basic details

					try {
						String name = snapshot.getString("stName");
						holder.binding.tvVendorName.setText(name);
					} catch (Exception ignored) {
					}
				});
			}
			else if (state==1)
			{
				holder.binding.title2.setVisibility(View.INVISIBLE);
				holder.binding.tvVendorName.setVisibility(View.INVISIBLE);
			}
			Date date=new Date(essentialOrderEntities.get(position).l);
			SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
			String currentdate = currentdateformat.format(date.getTime());
			SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
			String currenttime = currenttimeformat.format(date.getTime());
			holder.binding.tvDate.setText(currentdate+" "+currenttime);

			holder.binding.tvTimestamp.setText(essentialOrderEntities.get(position).order_ID);
            binding.total.setText("Total Orders:"+essentialOrderEntities.size());
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
