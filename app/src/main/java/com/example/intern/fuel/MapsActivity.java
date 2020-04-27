package com.example.intern.fuel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivityMapsBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
	private static final String TAG = MapsActivity.class.getSimpleName();
	private static int chosenPosition = -1;
	private static List<Double> lats = Arrays.asList(23.076927, 22.995767 ,23.037741, 23.057581);
	private static List<Double> longs = Arrays.asList(72.524779 , 72.536386, 72.558908, 72.556343);
	private static List<String> addresses = Arrays.asList("Near Sola Road, SG Highway, Ahmedabad <b>(BPCL)</b>", "Maktampura, Vasna Road, Ahmedabad <b>(BPCL)</b>", "Swastik Cross Road, CG road, Ahmedabad <b>(IOCL)</b>", "Ankur Road, Naranpura, Ahmedabad <b>(IOCL)</b>");
	SharedPreferences preferences;
	SupportMapFragment mapFragment;
	private Double current_lat, current_long, chosenLat, chosenLong;
	private boolean isMapReady;
	private ActivityMapsBinding binding;
	private GoogleMap mMap;
	private FusedLocationProviderClient locationProviderClient;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMapsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		checkPerms();
		/*locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
			current_lat = location.getLatitude();
			current_long = location.getLongitude();
			if(chosenLat != null && chosenLong != null){
				float[] distanceRes = new float[1];
				Location.distanceBetween(current_lat, current_long, chosenLat, chosenLong, distanceRes);
				binding.tvDistance.setText("about " + Math.round(distanceRes[0]/1000) + " KMs");
			}
		});*/
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		 try{
		 	mapFragment = (SupportMapFragment) getSupportFragmentManager()
				 .findFragmentById(R.id.map);
			 if(mapFragment != null)mapFragment.getMapAsync(this);
		 }catch (Exception ignored){}
		 
		binding.back.setOnClickListener(v -> onBackPressed());
		binding.home.setOnClickListener(v -> {
			Intent intent = new Intent(MapsActivity.this, MainApp.class);
			startActivity(intent);finish();
		});
		binding.notifi.setOnClickListener(v -> {
			Intent intent = new Intent(MapsActivity.this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
		preferences = getSharedPreferences("fuelPrefs", Context.MODE_PRIVATE);
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		isMapReady = true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		getLocationCallBack();
		//Logic for running activity
/*		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice ,addresses);
		binding.addressSpinner.setAdapter(adapter);*/
		/*binding.addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				binding.textAddressCustom.setText(addresses.get(position));
				((TextView)parent.getChildAt(0)).setTextSize(13);
				chosenPosition = position;
				chosenLat = lats.get(position); chosenLong = longs.get(position);
				if(current_lat != null && current_long != null){
					float[] distanceRes = new float[1];
					Location.distanceBetween(current_lat, current_long, lats.get(position), longs.get(position), distanceRes);
					binding.tvDistance.setText("about " + Math.round(distanceRes[0]/1000) + " KMs");
				}
				if(isMapReady){
					LatLng markerLocation = new LatLng(lats.get(position), longs.get(position));
					mMap.addMarker(new MarkerOptions().position(markerLocation).title("Petrol Pump"));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 15.0f));
				}else{
					if(mapFragment!=null) mapFragment.getMapAsync(mapReadyCallback);
				}
				//Set Icon
				if(addresses.get(position).contains("IOCL")){
					//TODO : Set Logo
					binding.fuelCompanyLogo.setImageDrawable(getResources().getDrawable(R.drawable.iocl_logo));
				}else{
					binding.fuelCompanyLogo.setImageDrawable(getResources().getDrawable(R.drawable.bpcl_logo));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});*/
	}
	
	private void checkPerms(){
		final Context context = this;
		Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
			@Override
			public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
				Log.d(TAG, "onPermissionGranted: Has Permissions and called further methods");
				locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
				getLocationCallBack();
			}
			
			@Override
			public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
				new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed)
						.setTitle("Need your permission").setMessage("To function properly and to show map on screen!")
						.setPositiveButton("OK", ((dialog, which) -> {
							if(which== AlertDialog.BUTTON_POSITIVE)checkPerms();
						})).setOnDismissListener(dialog -> {
							Toast.makeText(context, "Denied Permissions", Toast.LENGTH_SHORT).show();
						}).show();
			}
			
			@Override
			public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {}
		}).check();
	}
	
	@SuppressLint("SetTextI18n")
	private void getLocationCallBack(){
		if(locationProviderClient != null){
			locationProviderClient.getLastLocation().addOnSuccessListener(this, this::locationReadyCall);
		}else{
			Log.d(TAG, "getLocationCallBack: Failed to load location provider client");
		}
	}
	
	private void locationReadyCall(Location location){
		//Get current location
		current_lat = location.getLatitude();
		current_long = location.getLongitude();
		//Make the custom adapter when got the location
		CustomAdapter adapter = new CustomAdapter(this, addresses, lats, longs, current_lat, current_long);
		binding.spinnerCustom.setAdapter(adapter);
		//Move to last visited pump
		int lastPosition = preferences.getInt("lastPos", -1);
		if(lastPosition != -1) {
			//Found a last visited pump, move to that location
			binding.spinnerCustom.setSelection(lastPosition);
		}
		//Added check for when activity pauses
		if(chosenPosition != -1)binding.spinnerCustom.setSelection(chosenPosition);
		//Set listener to listen to spinner selection events
		binding.spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				chosenLat = lats.get(position);
				chosenLong = longs.get(position);
				chosenPosition = position;
				if(isMapReady){
					LatLng markerLocation = new LatLng(chosenLat, chosenLong);
					mMap.addMarker(new MarkerOptions().position(markerLocation).title("Petrol Pump"));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 15.0f));
				}else{
					Log.d(TAG, "onItemSelected: Cannnot ready map");
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		//Set button listeners after getting location
		binding.navigateLast.setOnClickListener(v -> {
			Intent stopStaleService = new Intent(MapsActivity.this, BackNavService.class);
			Intent serviceIntent = new Intent(MapsActivity.this, BackNavService.class);
			serviceIntent.putExtra(BackNavService.KEY_LATITUDE_INTENT_EXTRA, chosenLat);
			serviceIntent.putExtra(BackNavService.KEY_LONGITUDE_INTENT_EXTRA, chosenLong);
			//Stop Already running service with stale target
			try{stopService(stopStaleService);}catch (Exception ignored){}
			startService(serviceIntent);
			String base = "https://www.google.com/maps/dir/?api=1&destination="+ chosenLat + "," + chosenLong + "&travelmode=driving";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(base));
			startActivity(intent);
		});
		binding.continueLast.setOnClickListener(v -> {
			//TODO : Stop the service when user manually clicks I'm here
			Intent stopStaleService = new Intent(MapsActivity.this, BackNavService.class);
			stopService(stopStaleService);
			//Update shared preferences to store the last visited pump
			preferences.edit().putInt("lastPos", chosenPosition).apply();
			Intent intent = new Intent(this, FuelWithUsAct.class);
			startActivity(intent);finish();
		});
	}
	
	class CustomAdapter extends BaseAdapter{
		private Context context;
		private List<String> addresses;
		private List<Double> latitudes , longitudes;
		private double currentLat, currentLong;
		CustomAdapter(Context context, List<String> addresses, List<Double> latitudes, List<Double> longitudes, double currentLat, double currentLong){
			this.context = context;
			this.addresses = addresses;
			this.latitudes = latitudes;
			this.longitudes = longitudes;
			this.currentLong = currentLong;
			this.currentLat = currentLat;
		}
		
		@Override
		public int getCount() {
			return addresses.size();
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@SuppressLint("SetTextI18n")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.map_custom_spinner_item,null);
				holder = new ViewHolder();
				holder.mAddress = convertView.findViewById(R.id.map_spinner_address);
				holder.mDistance = convertView.findViewById(R.id.map_spinner_distance);
				holder.mPumpLogo = convertView.findViewById(R.id.map_spinner_logo);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			try{
				//TODO : populate the spinner
				String address = addresses.get(position);
				holder.mAddress.setText(Html.fromHtml(address));
				if(addresses.get(position).contains("IOCL")){
					holder.mPumpLogo.setImageDrawable(getResources().getDrawable(R.drawable.iocl_logo));
				}else{
					holder.mPumpLogo.setImageDrawable(getResources().getDrawable(R.drawable.bpcl_logo));
				}
				float[] distRes = new float[1];
				Location.distanceBetween(currentLat, currentLong, latitudes.get(position), longitudes.get(position), distRes);
				holder.mDistance.setText("about " + (int)distRes[0]/1000 + " KMs");
			}catch (Exception ignored){}
			return convertView;
		}
		
		public class ViewHolder{
			TextView mAddress, mDistance;
			ImageView mPumpLogo;
		}
	}
}
