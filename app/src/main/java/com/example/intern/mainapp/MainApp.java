package com.example.intern.mainapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.intern.EditProfile.EditProfile;
import com.example.intern.ExclusiveServices.ExclusiveServices;
import com.example.intern.FeedBackOrComplaintACT;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.Rating;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.example.intern.askservices.DemandActivity;
import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;
import com.example.intern.databinding.ActivityMainAppBinding;
import com.example.intern.databinding.HomeMenuHeaderBinding;
import com.example.intern.fuel.FuelWithUsAct;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	private ArrayList<String> menuOptions = new ArrayList<>();
	private MenuAdapter menuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainAppBinding.inflate(getLayoutInflater());
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		signInClient = GoogleSignIn.getClient(this,gso);
		setContentView(binding.getRoot());
		prefUtil = new SharedPrefUtil(this);
		getWindow().setStatusBarColor(getResources().getColor(R.color.light_orange));
	}
	
	private void setUpMenu(){
		View headerView = binding.duoMenu.getHeaderView();
		headerBinding =  HomeMenuHeaderBinding.bind(headerView);
		menuOptions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_main)));
		menuAdapter = new MenuAdapter(menuOptions);
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
	}
	
	@SuppressLint("SetTextI18n")
	private void populateDrawer(){
		int percentage = prefUtil.getProfileCompletionPercent();
		String ppFilePath = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PROFILE_PIC_PATH_KEY, null);
		if (ppFilePath != null && !ppFilePath.isEmpty()) {
			Glide.with(this).load(ppFilePath)
					.fallback(R.drawable.edit_profile).into(headerBinding.ivProfilePic);
		} else {
			String UID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_UID_KEY, null);
			if (UID != null && !UID.isEmpty()) {
				File imageFile = FireStoreUtil.getProfilePicInLocal(this, UID);
				Glide.with(this).load(imageFile)
						.fallback(R.drawable.edit_profile).into(headerBinding.ivProfilePic);
			}
		}
		headerBinding.tvProfileUsername.setText(prefUtil.getPreferences().getString(SharedPrefUtil.USER_NAME_KEY, "PS User"));
		headerBinding.progressMenuProfileCompletion.setProgress(percentage);
		headerBinding.eighty.setText(percentage + "% Profile Completed");
		binding.duoMenu.getHeaderView().refreshDrawableState();
	}
	
	private void setClickListeners(){
		binding.drawerPinHome.setOnClickListener(v-> binding.drawerLayout.openDrawer());
		binding.SaveMoneyLinear.setOnClickListener(v->{
			//TODO : Change after pandemic stops
			/*String userPayID = prefUtil.getPreferences().getString(SharedPrefUtil.USER_PAY_ID, null);
			if(userPayID != null){
				razorPayVerification(userPayID);
			}else{
				FireStoreUtil.getUserDocumentReference(this, FireStoreUtil.getFirebaseUser(this).getUid()).addSnapshotListener((snapshot, e) -> {
					String payID = null;
					if(snapshot!= null){
						payID = snapshot.getString(FireStoreUtil.USER_PAY_ID);
					}
					if(payID != null){
						razorPayVerification(payID);
					}else{
						Intent intent = new Intent(MainApp.this, BecomeAMember.class);
						startActivityForResult(intent, BECOME_MEMBER_REQ_CODE);
					}
				});
			}*/
			new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
					.setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
		});
		//TODO : Setting click listeners for others
		binding.swabhimanMainChoice.setOnClickListener(v->{
			Intent intent = new Intent(this, SwabhimanActivity.class);
			startActivity(intent);
		});
		binding.shopping.setOnClickListener(v->{
			Intent intent = new Intent(this, ActivityShopping.class);
			startActivity(intent);
		});
		binding.bpclfuel.setOnClickListener(v->{
			Intent intent = new Intent(this, FuelWithUsAct.class);
			startActivity(intent);
		});
		binding.requestService.setOnClickListener(v->{
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
		binding.duoMenu.getFooterView().findViewById(R.id.logout_button).setOnClickListener(v->{
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
								FirebaseAuth.getInstance().signOut();
								progressDialog.dismiss();
								new AlertDialog.Builder(context).setTitle("You have been logged out!")
										.setPositiveButton("OK", (dialog, which1) -> {
											if(which1==AlertDialog.BUTTON_POSITIVE){
												signInClient.revokeAccess().addOnSuccessListener(aVoid1 -> {
													finish();
												});
											}
										}).setCancelable(false).show();
								finish();
								}).addOnFailureListener(e -> {
									progressDialog.dismiss();
									new AlertDialog.Builder(context).setTitle("Cannot Connect to the Internet")
											.setPositiveButton("OK", null).show();
								});
						}
					}).setNegativeButton("No", null).show();
		});
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
	public void onFooterClicked() {
	
	}
	
	@Override
	public void onHeaderClicked() {
	
	}
	
	@Override
	public void onOptionClicked(int position, Object objectClicked) {
		Intent intent = null;
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