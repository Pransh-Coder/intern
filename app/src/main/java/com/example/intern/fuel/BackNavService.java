package com.example.intern.fuel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.intern.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BackNavService extends Service {
	public static final String CHANNEL_ID = "ForegroundLocationUpdates";
	public static final String KEY_LATITUDE_INTENT_EXTRA = "LAT";
	public static final String KEY_LONGITUDE_INTENT_EXTRA = "LON";
	public static final int NOTIFICATION_ID = 20;
	private static final String TAG = BackNavService.class.getSimpleName();
	private static double latitude, longitude;
	private FusedLocationProviderClient locationProviderClient;
	private LocationRequest locationRequest;
	private LocationCallback locationCallback;
	public BackNavService() {}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotificationChannel();
		//Get Location Requests from the Intent
		latitude = intent.getDoubleExtra(KEY_LATITUDE_INTENT_EXTRA, -1);
		longitude = intent.getDoubleExtra(KEY_LONGITUDE_INTENT_EXTRA, -1);
		Log.d(TAG, "onStartCommand: Got target" + latitude + " , " + longitude);
		if(latitude == -1 || longitude == -1){
			stopSelf();
		}
		Location target = new Location("");
		target.setLatitude(latitude);target.setLongitude(longitude);
		//Notification for when user reaches the pump
		Intent fuelIntent = new Intent(this, FuelWithUsAct.class);
		fuelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, fuelIntent, 0);
		NotificationCompat.Builder pumpReachedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.pslogotrimmed).setContentTitle("You are here !")
				.setContentText("Click here to receive discounts ! ").setPriority(NotificationCompat.PRIORITY_HIGH)
				.setContentIntent(pendingIntent);
		//Location Request Builder
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		locationRequest.setInterval(30 * 1000);
		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
		//Location Callback
		locationCallback = new LocationCallback(){
			@Override
			public void onLocationResult(LocationResult locationResult) {
				Log.d(TAG, "onLocationResult: Got Location Update");
				if(locationResult == null){
					Log.d(TAG, "onLocationResult: Received null location");
				}else{
					double curr_lat = locationResult.getLastLocation().getLatitude();
					double curr_long = locationResult.getLastLocation().getLongitude();
					Log.d(TAG, "onLocationResult: Received proper location" + curr_lat + " , " + curr_long);
					int proximityInMeters = (int)locationResult.getLastLocation().distanceTo(target);
					if(proximityInMeters <= 100){
						//TODO : Got to the location
						notificationManagerCompat.notify(NOTIFICATION_ID, pumpReachedBuilder.build());
						locationProviderClient.removeLocationUpdates(locationCallback);
						stopSelf();
					}else {
						Log.d(TAG, "onLocationResult: Not yet there");
					}
				}
			}
		};
		locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
		locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
		Log.d(TAG, "onStartCommand: Made Service");
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return null;
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
	public void onDestroy() {
		Log.d(TAG, "onDestroy: Stopped BackNavService");
		super.onDestroy();
	}
}
