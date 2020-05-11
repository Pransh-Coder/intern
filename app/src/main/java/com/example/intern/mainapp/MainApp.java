package com.example.intern.mainapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.AppStaticData;
import com.example.intern.EditProfile.EditProfile;
import com.example.intern.ExclusiveServices.ExclusiveServices;
import com.example.intern.ExclusiveServices.HomeModification;
import com.example.intern.ExclusiveServices.TiffinService;
import com.example.intern.FeedBackOrComplaintACT;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.Rating;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.example.intern.askservices.DemandActivity;
import com.example.intern.auth.AuthActivity;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityMainAppBinding;
import com.example.intern.databinding.HomeMenuHeaderBinding;
import com.example.intern.fuel.FuelWithUsAct;
import com.example.intern.fuel.MapsActivity;
import com.example.intern.payment.BecomeAMember;
import com.example.intern.payment.auth.RazorPayAuthAPI;
import com.example.intern.shopping.ActivityShopping;
import com.example.intern.socialnetwork.Listactivity;
import com.example.intern.swabhiman.SwabhimanActivity;
import com.example.intern.tnc.TermsAndConditions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import save_money.SaveMoney;

public class MainApp extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
	public static int BECOME_MEMBER_REQ_CODE = 20;
	private ActivityMainAppBinding binding;
	private HomeMenuHeaderBinding headerBinding;
	private SharedPrefUtil prefUtil;
	private GoogleSignInClient signInClient;
	public static final String IS_NEW_USER = "new";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Get Locale
		String locale= getSharedPreferences("lang", MODE_PRIVATE).getString("lang", null);
		if(locale != null){
			Resources resources = getResources();
			DisplayMetrics dm = resources.getDisplayMetrics();
			Configuration configuration = resources.getConfiguration();
			configuration.setLocale(new Locale(locale.toLowerCase()));
			resources.updateConfiguration(configuration, dm);
		}
		binding = ActivityMainAppBinding.inflate(getLayoutInflater());
		setUpSearchBar();
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		signInClient = GoogleSignIn.getClient(this,gso);
		setContentView(binding.getRoot());
		prefUtil = new SharedPrefUtil(this);
		getWindow().setStatusBarColor(getResources().getColor(R.color.light_orange));
		newUserWelcome();
	}
	
	private void checkPerms() {
		final Context context = MainApp.this;
		Dexter.withContext(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
			@Override
			public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
				if(!multiplePermissionsReport.areAllPermissionsGranted() ||multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
					new AlertDialog.Builder(context).setIcon(R.drawable.pslogotrimmed).setTitle("Needs Your Permission")
							.setMessage("To Deliver its best for its consumers").setPositiveButton("OK", (dialog, which)->{
								if(which == AlertDialog.BUTTON_POSITIVE){
									checkPerms();
								}
					}).setNegativeButton("DISMISS", null)
							.setCancelable(false).show();
				}
			}
			@Override
			public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {}
		}).check();
	}
	
	private void setUpSearchBar() {
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SearchPredictions));
		binding.searchBar.setAdapter(arrayAdapter);
		binding.searchBar.setThreshold(0);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		binding.searchBar.setDropDownWidth(metrics.widthPixels/2);
		binding.searchBar.setText("");
		binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
			String keyWord = v.getText().toString();
			if(!keyWord.isEmpty()){
				Intent foundIntent = AppStaticData.getSearchResultIntentFromMain(MainApp.this, keyWord);
				if(foundIntent != null)startActivity(foundIntent);
			}
			binding.searchBar.setText("");
			binding.searchBar.clearFocus();
			return false;
		});
		binding.searchBar.setOnItemClickListener((parent, view, position, id) -> {
			//TODO : Implement the same search as normal one
			Intent intent;
			String keyWord = parent.getItemAtPosition(position).toString();
			if(keyWord.contains("Home")){
				intent= new Intent(MainApp.this, HomeModification.class);
				startActivity(intent);
			}else if(keyWord.contains("Tiffin")){
				intent = new Intent(MainApp.this, TiffinService.class);
				startActivity(intent);
			}else if(keyWord.contains("Swabhiman")){
				intent = new Intent(MainApp.this, SwabhimanActivity.class);
				startActivity(intent);
			}else if(keyWord.contains("Shopping")){
				intent = new Intent(MainApp.this, ActivityShopping.class);
				startActivity(intent);
			}else if(keyWord.contains("Fuel")){
				intent = new Intent(MainApp.this, FuelWithUsAct.class);
				startActivity(intent);
			}else if(keyWord.contains("Ask")){
				intent = new Intent(MainApp.this, DemandActivity.class);
				startActivity(intent);
			}else if(keyWord.contains("Save")){
				intent = new Intent(MainApp.this, SaveMoney.class);
				startActivity(intent);
			}else if(keyWord.contains("Edit")){
				intent = new Intent(MainApp.this, EditProfile.class);
				startActivity(intent);
			}else if(keyWord.contains("Reduce")){
				intent = new Intent(MainApp.this, ReduceExpenses.class);
				startActivity(intent);
			}else if(keyWord.contains("Discount")){
				intent = new Intent(MainApp.this, TotalDiscountReceived.class);
				startActivity(intent);
			}else if(keyWord.contains("Medical")){
				intent = new Intent(MainApp.this, MedicalRecord.class);
				startActivity(intent);
			}else if(keyWord.contains("Network")){
				intent = new Intent(MainApp.this, Listactivity.class);
				startActivity(intent);
			}else if(keyWord.contains("News")){
				intent = new Intent(MainApp.this, NewsAndUpdatesACT.class);
				startActivity(intent);
			}else if(keyWord.contains("Feedback")){
				intent = new Intent(MainApp.this, FeedBackOrComplaintACT.class);
				startActivity(intent);
			}else if(keyWord.contains("Rate")){
				intent = new Intent(MainApp.this, Rating.class);
				startActivity(intent);
			}else if(keyWord.contains("Terms")){
				intent = new Intent(MainApp.this, TermsAndConditions.class);
				startActivity(intent);
			}
			binding.searchBar.dismissDropDown();
			binding.searchBar.setText("");
			binding.searchBar.clearFocus();
		});
	}
	
	private void setUpMenu(){
		View headerView = binding.duoMenu.getHeaderView();
		headerBinding =  HomeMenuHeaderBinding.bind(headerView);
		ArrayList<String> menuOptions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_main)));
		MenuAdapter menuAdapter = new MenuAdapter(menuOptions);
		binding.duoMenu.setOnMenuClickListener(this);
		binding.duoMenu.setAdapter(menuAdapter);
		DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,binding.drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		binding.drawerLayout.setDrawerListener(duoDrawerToggle);
		duoDrawerToggle.syncState();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		setUpMenu();
		populateDrawer();
		setClickListeners();
		setDrawerClickListeners();
		checkPerms();
		binding.searchBar.setText("");
	}
	
	private void newUserWelcome(){
		if(getIntent().getBooleanExtra(IS_NEW_USER, false)){
			String message = "You are eligible for One month free benefit of discount on <b>Fuel</b> under <b>PS+</b> membership.";
			new AlertDialog.Builder(this).setIcon(R.drawable.pslogotrimmed).setTitle("Congratulations")
					.setMessage(Html.fromHtml(message))
					.setPositiveButton("Great", null)
					.setCancelable(false).show();
		}
	}
	
	@SuppressLint("SetTextI18n")
	private void populateDrawer(){
		int percentage = prefUtil.getProfileCompletionPercent();
		String ppFilePath = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, null);
		if (ppFilePath != null && !ppFilePath.isEmpty()) {
			Glide.with(this).load(ppFilePath)
					.fallback(R.drawable.edit_profile).into(headerBinding.ivProfilePic);
		} else {
			final Context context = MainApp.this;
			String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
			if (UID != null && !UID.isEmpty()) {
				try{
					FireStoreUtil.getProfilePicInLocal(this, UID).addOnSuccessListener(taskSnapshot ->
							taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri ->
							Glide.with(context).load(uri.toString()).fallback(R.drawable.edit_profile).error(R.drawable.edit_profile).into(headerBinding.ivProfilePic)));
				}catch (Exception ignored){}
			}
		}
		headerBinding.tvProfileUsername.setText(prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, "PS User"));
		headerBinding.progressMenuProfileCompletion.setProgress(percentage);
		headerBinding.eighty.setText(percentage + "% Profile Completed");
		binding.duoMenu.getHeaderView().refreshDrawableState();
	}
	
	private void setClickListeners(){
		binding.drawerPinHome.setOnClickListener(v-> {
			binding.drawerLayout.openDrawer();
			binding.searchBar.clearFocus();
		});
		binding.saveMoney.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, SaveMoney.class);
			startActivity(intent);
		});
		binding.swabhiman.setOnClickListener(v->{
			Intent intent = new Intent(this, SwabhimanActivity.class);
			startActivity(intent);
		});
		binding.shopping.setOnClickListener(v->{
			Intent intent = new Intent(this, ActivityShopping.class);
			startActivity(intent);
		});
		binding.discountFuel.setOnClickListener(v->{
			Intent intent = new Intent(this, MapsActivity.class);
			startActivity(intent);
		});
		binding.askServices.setOnClickListener(v->{
			Intent intent = new Intent(this, DemandActivity.class);
			startActivity(intent);
		});
		binding.exclusiveServices.setOnClickListener(v->{
			Intent intent = new Intent(this, ExclusiveServices.class);
			startActivity(intent);
		});
	}
	
	private void setDrawerClickListeners(){
		headerBinding.ivProfilePic.setOnClickListener(v->{
			Intent intent = new Intent(MainApp.this, EditProfile.class);
			startActivity(intent);
		});
		Context context = MainApp.this;
		binding.duoMenu.getFooterView().findViewById(R.id.logout_button).setOnClickListener(v->
				new AlertDialog.Builder(this).setTitle("Log Out ?")
				.setPositiveButton("Yes", (button, which)->{
					if(which == AlertDialog.BUTTON_POSITIVE){
						ProgressDialog progressDialog = new ProgressDialog(this);
						progressDialog.setTitle("Logging Out!");
						progressDialog.show();
						progressDialog.setCancelable(false);
						FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
						String currentuserid;
						if(user != null){
							currentuserid = user.getUid();
						}else{
							currentuserid = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
						}
						Map<String, Object> updata = new HashMap<>();
						updata.put("LS", "0");
						FireStoreUtil.getUserDocumentReference(context, currentuserid).update(updata).addOnSuccessListener(aVoid -> {
							prefUtil.getPreferences().edit().clear().apply();
							File  pp = new File(Environment.getExternalStorageDirectory() + "PSData/pp.jpg");
							if(pp.exists()){
								pp.delete();
							}
							FirebaseAuth.getInstance().signOut();
							signInClient.revokeAccess();
							progressDialog.dismiss();
							new AlertDialog.Builder(context).setTitle("You have been logged out!")
									.setPositiveButton("OK", null)
									.setOnDismissListener(dialog -> {
										Intent intent = new Intent(MainApp.this, AuthActivity.class);
										startActivity(intent);
										finish();
									}).show();
							}).addOnFailureListener(e -> {
								progressDialog.dismiss();
								new AlertDialog.Builder(context).setTitle("Cannot Connect to the Server")
										.setPositiveButton("OK", null).show();
							});
					}
				}).setNegativeButton("No", null).show());
		/*headerBinding.ivLogOut.setOnClickListener(v-> new AlertDialog.Builder(this).setTitle("Log Out ?")
				.setPositiveButton("Yes", (button, which)->{
					if(which == AlertDialog.BUTTON_POSITIVE){
						String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
						Map<String, Object> data = new HashMap<>();
						data.put("LS","0");
						Context context = MainApp.this;
						FirebaseFirestore.getInstance().collection("Users").document(currentuserid)
								.update(data).addOnSuccessListener(aVoid -> {
									prefUtil.getPreferences().edit().clear().apply();
									FirebaseAuth.getInstance().signOut();
									// Google revoke access
								signInClient.revokeAccess().addOnSuccessListener(aVoid1 -> {
										new AlertDialog.Builder(context).setTitle("You have been logged out!")
												.setPositiveButton("OK", (dialog, which1) -> {
													if(which1==AlertDialog.BUTTON_POSITIVE)finishAndRemoveTask();
													finish();

												}).setCancelable(false).show();
									});
								finish();
								});
					}
				}).setNegativeButton("No", null).show());*/
		/*binding.navigationId.setNavigationItemSelectedListener(item -> {
			Intent intent = null;
			switch (item.getItemId()){
				case R.id.menu_edit_profile:
					intent = new Intent(this, EditProfile.class);
					break;
				case R.id.menu_medical_records:
					intent = new Intent(this, MedicalRecord.class);
					break;
				case R.id.menu_tnc:
					intent = new Intent(this, TermsAndConditions.class);
					break;
				case R.id.menu_reduce_expense:
					intent = new Intent(this, ReduceExpenses.class);
					break;
				case R.id.menu_total_disc:
					intent = new Intent(this, TotalDiscountReceived.class);
					break;
				case R.id.menu_local_net:
					intent = new Intent(this, Listactivity.class);
					break;
				case R.id.menu_newsnupdates:
					intent = new Intent(this, NewsAndUpdatesACT.class);
					break;
				case R.id.menu_feedback:
					intent = new Intent(this, FeedBackOrComplaintACT.class);
					break;
				case R.id.menu_rate:
					//TODO : Send to the app link in play store
					break;
			}
			if(intent!=null){
				startActivity(intent);
			}
			return true;
		});*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == BECOME_MEMBER_REQ_CODE && resultCode == BecomeAMember.IS_A_MEMBER_RESULT_CODE) {
			if(data != null){
				String payID = data.getStringExtra(BecomeAMember.PAY_ID_TAG);
				prefUtil.setUserPayID(payID);
				razorPayVerification(payID);
			}
		}else{
			Toast.makeText(this,"Payment Not Complete!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void razorPayVerification(String payID){
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Verifying Payment");dialog.show();
		//TODO : Check for internet connectivity failure
		RazorPayAuthAPI.isPaymentVerified(payID, verificationStatus -> {
			if (verificationStatus) {
				Intent intent = new Intent(MainApp.this, SaveMoney.class);
				dialog.hide();
				startActivity(intent);
			} else {
				dialog.hide();
				Toast.makeText(this, "Payment Cannot Be Confirmed", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MainApp.this, BecomeAMember.class);
				startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finishAffinity();
		super.onBackPressed();
	}
	
	@Override
	public void onFooterClicked() {}
	
	@Override
	public void onHeaderClicked() {}
	
	@Override
	public void onOptionClicked(int position, Object objectClicked) {
		Intent intent;
		switch (position) {
			case 0:
				intent = new Intent(this, ReduceExpenses.class);
				break;
			case 1:
				intent = new Intent(this, TotalDiscountReceived.class);
				break;
			case 2:
				intent = new Intent(this, EditProfile.class);
				break;
			case 3:
				intent = new Intent(this,MedicalRecord.class );
				break;
			case 4:
				intent = new Intent(this, Listactivity.class);
				break;
			case 5:
				intent = new Intent(this, NewsAndUpdatesACT.class);
				break;
			case 6:
				intent = new Intent(this, FeedBackOrComplaintACT.class);
				break;
			case 7:
				intent = new Intent(this, Rating.class);
				break;
			case 8:
				intent = new Intent(this, TermsAndConditions.class);
				break;
			default:
				return;
		}
		startActivity(intent);
		binding.drawerLayout.closeDrawer();
	}
	
}