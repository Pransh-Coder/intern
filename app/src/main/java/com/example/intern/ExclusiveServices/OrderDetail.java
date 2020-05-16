package com.example.intern.ExclusiveServices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.intern.R;
import com.example.intern.databinding.ActivityOrderDetailBinding;
import com.example.intern.databinding.RecyclerItemOrderDetailBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail extends AppCompatActivity {
	
	public static final String EXTRA_DOCUMENT_ID_KEY = "doc_id";
	public static final String EXTRA_VENDOR_ID_KEY = "vend_id";
	ActivityOrderDetailBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		binding.recyclerItems.setLayoutManager(new LinearLayoutManager(this));
		String vendorID = getIntent().getStringExtra(EXTRA_VENDOR_ID_KEY);
		String documentID = getIntent().getStringExtra(EXTRA_DOCUMENT_ID_KEY);
		if (vendorID == null || documentID == null){
			Toast.makeText(this, "Illegal Entry!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		FirebaseFirestore.getInstance().collection("vendors").document(vendorID).collection("orders")
				.document(documentID).get().addOnSuccessListener(snapshot -> {
					try{
						boolean vendorStat = snapshot.getBoolean("vendorstat");
						if(vendorStat){
							binding.tvStatus.setText("Status : Order Accepted");
							//TODO : Show bill if there
							try {
								String billUrl = snapshot.getString("billpic");
								if(billUrl==null){
									binding.ivBill.setVisibility(View.GONE);
								}else{
									Glide.with(this).load(billUrl).placeholder(android.R.drawable.progress_indeterminate_horizontal)
											.into(binding.ivBill);
									binding.ivBill.setVisibility(View.VISIBLE);
								}
							}catch (Exception e){
								//No bill found !
								binding.ivBill.setVisibility(View.GONE);
							}
						}else{
							binding.tvStatus.setText("Status : Order Rejected");
							binding.ivBill.setVisibility(View.GONE);
						}
					}catch (Exception ignored){
						//Vendor status cannot be found
						binding.tvStatus.setText("Status : Awaiting Vendor Response");
						binding.ivBill.setVisibility(View.GONE);
					}
					//Pass snapshot to the adapter to do the processing of data
					OrderDetailAdapter adapter = new OrderDetailAdapter(snapshot);
					binding.recyclerItems.setAdapter(adapter);
				});
	}
	
	class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailsViewHolder>{
		DocumentSnapshot snapshot;
		List<String> items;
		List<String> quants;
		List<String> prices;
		OrderDetailAdapter(DocumentSnapshot snapshot){
			this.snapshot = snapshot;
			prices = new ArrayList<>();
			quants = new ArrayList<>();
			items = new ArrayList<>();
		}
		@NonNull
		@Override
		public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new DetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_order_detail, parent, false));
		}
		
		@Override
		public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
			try{
				//Get all the things needed
				items = (List<String>)snapshot.get("items");
				quants = (List<String>)snapshot.get("quants");
				prices = (List<String>)snapshot.get("prices");
			}catch (Exception ignored){
				//Prices cannot be found
				holder.binding.tvPrice.setText("-");
			}
			if(prices == null || prices.isEmpty()){
				Toast.makeText(OrderDetail.this, "Not yet accepted!", Toast.LENGTH_SHORT).show();
			}else {
				holder.binding.tvPrice.setText(prices.get(position));
				int total = 0;
				for(String price: prices){
					total += Integer.parseInt(price);
				}
				binding.tvTotal.setText("Total : " + total + " INR");
			}
			holder.binding.tvItem.setText(items.get(position));
			holder.binding.tvQuant.setText(quants.get(position));
		}
		
		@Override
		public int getItemCount() {
			return ((List<String>)snapshot.get("items")).size();
		}
		
		class DetailsViewHolder extends RecyclerView.ViewHolder{
			RecyclerItemOrderDetailBinding binding;
			DetailsViewHolder(@NonNull View itemView) {
				super(itemView);
				binding = RecyclerItemOrderDetailBinding.bind(itemView);
			}
		}
	}
}
