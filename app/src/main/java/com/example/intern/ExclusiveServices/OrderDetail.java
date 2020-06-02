package com.example.intern.ExclusiveServices;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivityOrderDetailBinding;
import com.example.intern.mainapp.MainApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {

	public static final String EXTRA_DOCUMENT_ID_KEY = "doc_id";
	public static final String EXTRA_VENDOR_ID_KEY = "vend_id";
	ActivityOrderDetailBinding binding;
	String vendorID,documentID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		binding.etOrderDetail.setMovementMethod(new ScrollingMovementMethod());
		vendorID = getIntent().getStringExtra(EXTRA_VENDOR_ID_KEY);
		 documentID = getIntent().getStringExtra(EXTRA_DOCUMENT_ID_KEY);
		if (vendorID == null || documentID == null) {
			Toast.makeText(this, "Illegal Entry!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		binding.back.setOnClickListener(v -> onBackPressed());
		binding.homeBtn.setOnClickListener(v -> {
			Intent intent = new Intent(this, MainApp.class);
			startActivity(intent);finish();
		});
		binding.notifi.setOnClickListener(v -> {
			Intent intent = new Intent(this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		FirebaseFirestore.getInstance().collection("vendors").document(vendorID).collection("orders")
				.document(documentID).get().addOnSuccessListener(snapshot -> {
			//Show the basic details
			try {
				String orderDetails = snapshot.getString("orderDet");
				if (orderDetails != null && !TextUtils.isEmpty(orderDetails)) {
					binding.tvOrderDetail.setVisibility(View.VISIBLE);
					binding.tvOrderDetail.setText(orderDetails);
				}
				String imgURL = snapshot.getString("orderimg");
				if (imgURL != null && !imgURL.isEmpty()) {
					Glide.with(this).load(imgURL).placeholder(android.R.drawable.progress_indeterminate_horizontal)
							.into(binding.ivOrderList);
					binding.ivOrderList.setVisibility(View.VISIBLE);
				}
			} catch (Exception ignored) {
			}
			//Loaded basic details...check for vendorstat
			try {
				boolean vendorStatus = snapshot.getBoolean("vendorstat");
				if (vendorStatus) {
					//TODO : Flow goes where vendor accepted order
					binding.tvStatus.setText("Status : Accepted");
					//CHeck for billpic and total independently
					try {
						String billURL = snapshot.getString("billpic");
						String total = snapshot.getString("total");
						if (billURL != null && !TextUtils.isEmpty(billURL)) {
							Glide.with(this).load(billURL).placeholder(android.R.drawable.progress_indeterminate_horizontal)
									.into(binding.ivBill);
							binding.ivBill.setVisibility(View.VISIBLE);
						}
						//Must have given total
						assert total != null;
						binding.tvTotal.setText("Total : " + total + " INR");
						binding.tvTotal.setVisibility(View.VISIBLE);
					} catch (Exception ignored) {}
					//Check for delivery status
					try {
						boolean deliverStatus = snapshot.getBoolean("deliverstat");
						if (deliverStatus) {
							//Delivered, prompt user to pay or pay later
							binding.tvStatus.setText("Status : Delivered");
							//Check if paystat is there
							try {
								boolean payStat = snapshot.getBoolean("paystat");
								if (payStat) {
									binding.tvStatus.setText("Status : Paid");
								} else {binding.tvStatus.setText("Status : Pay Later");
									binding.edit.setVisibility(View.VISIBLE);
									binding.edit.setOnClickListener(v -> {
										//Performing UI updates beforehand
										binding.tvOrderDetail.setVisibility(View.GONE);
										binding.edit.setVisibility(View.GONE);
										binding.update.setVisibility(View.VISIBLE);
										editOrders(snapshot);
									});}
								//This marks the end of a typical flow
							} catch (Exception e) {
								//No pay status, last usable option for user is to choose paid or pay later
								binding.btnPaid.setVisibility(View.VISIBLE);
								binding.btnNotPaid.setVisibility(View.VISIBLE);
								binding.btnPaid.setOnClickListener(v -> {
									Map<String, Object> update = new HashMap<>();
									update.put("paystat", true);
									snapshot.getReference().update(update).addOnSuccessListener(aVoid -> {
										Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
										finish();
									});
								});
								binding.btnNotPaid.setOnClickListener(v -> {
									Map<String, Object> update = new HashMap<>();
									update.put("paystat", false);
									snapshot.getReference().update(update).addOnSuccessListener(aVoid -> {
										Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
										finish();
									});
								});
							}
						} else {
							//FOR future
						}
					} catch (Exception e) {
						//Not yet delivered, ignore
					}
				} else {
					//Vendor rejected the order
					binding.tvStatus.setText("Status : Rejected");
				}
			} catch (Exception e) {
				//Not yet accepted, user can still edit the order
				binding.tvStatus.setText("Status : Awaiting response");
				binding.edit.setVisibility(View.VISIBLE);
				binding.edit.setOnClickListener(v -> {
					//Performing UI updates beforehand
					binding.tvOrderDetail.setVisibility(View.GONE);
					binding.edit.setVisibility(View.GONE);
					binding.update.setVisibility(View.VISIBLE);
					editOrders(snapshot);
				});
			}
		});
		/*FirebaseFirestore.getInstance().collection("vendors").document(vendorID).collection("orders")
				.document(documentID).get().addOnSuccessListener(snapshot -> {
					try{
						boolean vendorStat = snapshot.getBoolean("vendorstat");
						if(vendorStat){
							binding.tvStatus.setText("Status : Order Accepted");
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
							//Check the delivery status
							try{
								boolean hasDelivered = snapshot.getBoolean("deliverstat");
								if(hasDelivered){
									binding.tvStatus.setText("Status : Order delivered");
									//Show and provide click listeners for paid and not paid buttons
									DocumentReference orderRef = snapshot.getReference();
									//Update paidstat if not already there
									try{
										boolean hasPaid = snapshot.getBoolean("paystat");
										if(hasPaid){
											binding.tvStatus.setText("Status : Paid for order");
										}else binding.tvStatus.setText("Status : Didn't pay for order");
									}catch (Exception e){
										binding.btnPaid.setVisibility(View.VISIBLE);
										binding.btnNotPaid.setVisibility(View.VISIBLE);
										binding.btnPaid.setOnClickListener(v -> {
											Map<String, Object> update = new HashMap<>();
											update.put("paystat", true);
											orderRef.update(update).addOnSuccessListener(aVoid -> {
												Toast.makeText(this, "Updated pay status", Toast.LENGTH_SHORT).show();
												finish();
											});
										});
										binding.btnNotPaid.setOnClickListener(v -> {
											Map<String, Object> update = new HashMap<>();
											update.put("paystat", false);
											orderRef.update(update).addOnSuccessListener(aVoid -> {
												Toast.makeText(this, "Updated pay status", Toast.LENGTH_SHORT).show();
												finish();
											});
										});
									}
								}
							}catch (Exception e){
								//No delivery status yet
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
				});*/
	}

	private void editOrders(DocumentSnapshot snapshot) {
		String orderDetails = snapshot.getString("orderDet");
		assert orderDetails != null;
		if (orderDetails != null && !TextUtils.isEmpty(orderDetails)) {
			binding.etOrderDetail.setVisibility(View.VISIBLE);
			binding.etOrderDetail.setText(orderDetails);
			binding.update.setOnClickListener(v -> {
				String editList=binding.etOrderDetail.getText().toString();
				//Avoid redundant update
				if(editList.equals(orderDetails)){
					binding.etOrderDetail.setVisibility(View.GONE);
					binding.tvOrderDetail.setVisibility(View.VISIBLE);
					binding.update.setVisibility(View.GONE);
					binding.edit.setVisibility(View.VISIBLE);
					return;
				}
				//Found change ... Update order list
				FirebaseFirestore.getInstance().collection("vendors").document(vendorID).collection("orders")
						.document(documentID).update("orderDet",editList).addOnSuccessListener(aVoid -> {
							new AlertDialog.Builder(OrderDetail.this).setTitle("Updated").setMessage(Html.fromHtml("Your Order has been updated Successfully. View all your orders in the order list."))
									.setIcon(R.drawable.pslogotrimmed).setPositiveButton("Ok", null)
									.setOnDismissListener(dialog -> {
										//UI Updates
										binding.tvOrderDetail.setText(editList);
										binding.tvOrderDetail.setVisibility(View.VISIBLE);
										binding.etOrderDetail.setVisibility(View.GONE);
										recreate();
									})
									.show();
							/*binding.redit.setVisibility(View.VISIBLE);
							binding.update.setVisibility(View.INVISIBLE);
							binding.redit.setOnClickListener(v1 -> {
								binding.tvOrderDetail.setVisibility(View.INVISIBLE);
								binding.etOrderDetail.setVisibility(View.VISIBLE);
								binding.etOrderDetail.setText(editList);
								binding.redit.setVisibility(View.INVISIBLE);
								binding.update.setVisibility(View.VISIBLE);
	
							});*/
	
						});
				//AddOnFailure is redundant as it cannot detect network issues
			});



		}
	}

	
	/*class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailsViewHolder>{
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
	}*/
}
