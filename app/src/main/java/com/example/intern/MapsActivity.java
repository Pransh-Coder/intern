package com.example.intern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityMapsBinding;
import com.example.intern.fuel.FuelWithUsAct;
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
	private static int chosenPosition = -1;
	private static List<Double> lats = Arrays.asList(23.076927, 22.995767 ,23.037741, 23.057581);
	private static List<Double> longs = Arrays.asList(72.524779 , 72.536386, 72.558908, 72.556343);
	private static List<String> addresses = Arrays.asList("NEAR SOLA ROAD, SG Highway, Ahmedabad (BPCL)", "MAKTAMPURA, Vasna Road, Ahmedabad (BPCL)", "SWASTIK CROSS ROAD, CG road, Ahmedabad (IOCL)", "ANKUR ROAD, Naranpura, Ahmedabad (IOCL)");
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
		locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
		 mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if(mapFragment != null)mapFragment.getMapAsync(this);
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
		getLocationCallBack();
		//Initialise the last choice
		int lastVisistedPosition = preferences.getInt("lastPos",-1);
		if( lastVisistedPosition != -1){
			binding.addressLast.setText(addresses.get(lastVisistedPosition));
			binding.navigateLast.setOnClickListener(v -> {
				String base = "https://www.google.com/maps/dir/?api=1&destination="+ lats.get(lastVisistedPosition) + "," + longs.get(lastVisistedPosition) + "&travelmode=driving";
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(base));
				startActivity(intent);
			});
			binding.continueLast.setOnClickListener(v -> {
				Intent intent = new Intent(MapsActivity.this, FuelWithUsAct.class);
				startActivity(intent);finish();
			});
		}else{
			binding.continueLast.setVisibility(View.GONE);
			binding.navigateLast.setVisibility(View.GONE);
		}
		//Logic for running activity
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice ,addresses);
		binding.addressSpinner.setAdapter(adapter);
		final OnMapReadyCallback mapReadyCallback = this;
		if(chosenPosition != -1)binding.addressSpinner.setSelection(chosenPosition);
		binding.textAddressCustom.setOnClickListener(v -> {
			binding.addressSpinner.performClick();
		});
		binding.addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
					//TODO : Set IOCL Logo
					binding.fuelCompanyLogo.setImageDrawable(getResources().getDrawable(R.drawable.iocl_logo));
				}else{
					//TODO : Set BPCL LOGO
					binding.fuelCompanyLogo.setImageDrawable(getResources().getDrawable(R.drawable.bpcl_logo));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		binding.navigateButton.setOnClickListener(v -> {
			String base = "https://www.google.com/maps/dir/?api=1&destination="+ chosenLat + "," + chosenLong + "&travelmode=driving";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(base));
			startActivity(intent);
		});
		binding.continueCustom.setOnClickListener(v -> {
			preferences.edit().putInt("lastPos", chosenPosition).apply();
			Intent intent = new Intent(this, FuelWithUsAct.class);
			startActivity(intent);finish();
		});
	}
	
	private void checkPerms(){
		final Context context = this;
		Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
			@Override
			public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
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
			locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
				current_lat = location.getLatitude();
				current_long = location.getLongitude();
				float[] distanceRes = new float[1];
				if(chosenLat != null && chosenLong != null){
					Location.distanceBetween(current_lat, current_long, chosenLat, chosenLong, distanceRes);
				}else{
					Location.distanceBetween(current_lat, current_long, lats.get(0), longs.get(0), distanceRes);
				}
				binding.tvDistance.setText("about " + Math.round(distanceRes[0]/1000) + " KMs");
			});
		}
	}
}
