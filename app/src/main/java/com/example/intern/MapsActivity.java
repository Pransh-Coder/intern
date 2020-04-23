package com.example.intern;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityMapsBinding;
import com.example.intern.fuel.FuelWithUsAct;
import com.example.intern.mainapp.MainApp;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
	private static String EXTRA_DATA_LAT_KEY = "latitude";
	private static String EXTRA_DATA_LON_KEY = "longitude";
	private static String EXTRA_STRING_ADDRESS = "address";
	private ActivityMapsBinding binding;
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMapsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		checkPerms();
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if(mapFragment != null)mapFragment.getMapAsync(this);
		binding.navigateButton.setOnClickListener(v -> {
			//TODO :
			String base = "https://www.google.com/maps/dir/?api=1&destination=23.005915,72.601313&travelmode=driving";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(base));
			startActivity(intent);
		});
		binding.buttonContinue.setOnClickListener(v -> {
			Intent intent = new Intent(MapsActivity.this, FuelWithUsAct.class);
			startActivity(intent);finish();
		});
		binding.back.setOnClickListener(v -> onBackPressed());
		binding.home.setOnClickListener(v -> {
			Intent intent = new Intent(MapsActivity.this, MainApp.class);
			startActivity(intent);finish();
		});
		binding.notifi.setOnClickListener(v -> {
			Intent intent = new Intent(MapsActivity.this, NewsAndUpdatesACT.class);
			startActivity(intent);
		});
	}
	
	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		// Add a marker in Sydney and move the camera
		//TODO :
		LatLng location = new LatLng(23.005915, 72.601313);
		mMap.addMarker(new MarkerOptions().position(location).title("Petrol Pump Address"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
	}
	
	private void checkPerms(){
		final Context context = this;
		Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
			@Override
			public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {}
			
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
}
