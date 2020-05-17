package com.example.intern.ExclusiveServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.intern.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class OrderTrackService extends JobIntentService {
	public static final String EXTRA_ORDER_DOCUMENT_ID = "doc_id";
	public static final String EXTRA_VENDOR_ID = "vendor_id";
	private static final String TAG = OrderTrackService.class.getSimpleName();
	private static final int JOB_ID = 1001;
	private static final String CHANNEL_ID = "Order Tracking";
	private ListenerRegistration listener;
	
	public static void enqueueWork(Context context, Intent intent){
		enqueueWork(context, OrderTrackService.class, JOB_ID, intent);
	}
	
	@Override
	protected void onHandleWork(@NonNull Intent intent) {
		String vendorID = intent.getStringExtra(EXTRA_VENDOR_ID);
		String orderID = intent.getStringExtra(EXTRA_ORDER_DOCUMENT_ID);
		if(vendorID == null || orderID == null){
			Log.d(TAG, "onHandleWork: Illegal service startup");
			stopSelf();
			return;
		}
		final Context context = this;
		listener = FirebaseFirestore.getInstance().collection("vendors")
				.document(vendorID).collection("orders")
				.document(orderID)
				.addSnapshotListener((snapshot, e) -> {
					if(snapshot != null && snapshot.exists()){
						//Access the newer states from the snapshot
						try{
							if(snapshot.getBoolean("vendorstat")){
								List<String> prices = (List<String>) snapshot.get("prices");
								if(prices != null && !prices.isEmpty()){
									//TODO : create a pending intent to go to show the order details
									Intent intent1 = new Intent(context, OrderDetail.class);
									intent1.putExtra(OrderDetail.EXTRA_DOCUMENT_ID_KEY, orderID);
									intent1.putExtra(OrderDetail.EXTRA_VENDOR_ID_KEY, vendorID);
									PendingIntent pendingIntent = PendingIntent.getActivity(context, 23,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
									showNotification("Order Accepted!", "Click here to see the details", pendingIntent);
								}
							}else{
								showNotification("Order denied!", "Sorry but the order " + orderID + " cannot be fulfilled by the vendor", null);
								onStopCurrentWork();
							}
						}catch (Exception ignored){}
					}
				});
	}
	
	private void showNotification(String title, String content, @Nullable PendingIntent pendingIntent){
		createNotificationChannel();
		NotificationCompat.Builder orderNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.pslogotrimmed).setContentTitle(title).setContentText(content)
				.setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent);
		NotificationManagerCompat.from(this).notify(1003, orderNotification.build());
	}
	
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = getString(R.string.PSNotificationChannel);
			String description = getString(R.string.notifi_channel_descp);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			if (notificationManager != null) {
				notificationManager.createNotificationChannel(channel);
			}
		}
		Log.d(TAG, "createNotificationChannel: Created Notification channel");
	}
	
	@Override
	public boolean onStopCurrentWork() {
		listener.remove();
		return super.onStopCurrentWork();
	}
}
