package com.example.intern.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefUtil {
	public static String SHARED_PREF_NAME = "user_prefs";
	public static String USER_UID_KEY = "user_uid";
	public static String USER_NAME_KEY = "user_name";
	public static String USER_NICK_NAME_KEY = "user_nick_name";
	public static String USER_PS_NICK_NAME_KEY = "user_ps_nick_name";
	public static String USER_DOB_KEY = "user_DOB";
	public static String USER_EMAIL_KEY = "user_email";
	public static String USER_PIN_CODE_KEY  = "user_pin_code";
	public static String USER_PAY_ID = "user_pay_id";
	public static String USER_LOGGED_IN_STATUS_KEY = "user_log_in_status";
	public static String USER_RELATIVE_PHONE_NUMBER_KEY = "user_relative_ph_no";
	public static String USER_PHONE_NO = "user_ph_no";
	public static String USER_OCCUPATION_KEY = "user_occ";
	public static String USER_ADDRESS_KEY = "user_add";
	public static String USER_PAY_VER_STATUS = "pay_verified";
	private Context context;
	private SharedPreferences preferences;
	
	public SharedPrefUtil(Context context){
		this.context = context;
		preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public void updateSharedPreferencesPostRegister(String uid ,String name, String email, String nick_name, String ps_nick_name,
	                                     String DOB, String pin_code, String relatives_number){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(USER_UID_KEY, uid).putString(USER_NAME_KEY, name)
				.putString(USER_EMAIL_KEY, email).putString(USER_NICK_NAME_KEY, nick_name)
				.putString(USER_PS_NICK_NAME_KEY, ps_nick_name).putString(USER_PIN_CODE_KEY, pin_code)
				.putString(USER_DOB_KEY, DOB).putString(USER_RELATIVE_PHONE_NUMBER_KEY, relatives_number)
				.putBoolean(USER_LOGGED_IN_STATUS_KEY, true).putString(USER_PAY_ID, null)
				.putString(USER_PHONE_NO, null).putBoolean(USER_PAY_VER_STATUS , false)
				.putString(USER_OCCUPATION_KEY, null).putString(USER_ADDRESS_KEY, null);
		editor.apply();
	}
	
	public void storePayIdInSharedPrefs(String pay_id){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(USER_PAY_ID, pay_id);
		editor.apply();
	}
	
	public boolean getLoginStatus(){
		return preferences.getBoolean(USER_LOGGED_IN_STATUS_KEY, false);
	}
	
	public void setLoginStatus(boolean b){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(USER_LOGGED_IN_STATUS_KEY, b);
		editor.apply();
	}
	
	public String getUserPayId(){
		return preferences.getString(USER_PAY_ID, null);
	}
	
	public void setUserPayID(String payID){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(USER_PAY_ID, payID);
		editor.apply();
	}
	
	public int getProfileCompletionPercent(){
		int percent = 0;
		String name = preferences.getString(USER_NAME_KEY, null);
		String email = preferences.getString(USER_EMAIL_KEY, null);
		String nick_name = preferences.getString(USER_NICK_NAME_KEY, null);
		String ps_nick_name = preferences.getString(USER_PS_NICK_NAME_KEY, null);
		String pin_code = preferences.getString(USER_PIN_CODE_KEY, null);
		String dob = preferences.getString(USER_DOB_KEY, null);
		String ph_no = preferences.getString(USER_PHONE_NO, null);
		String rel_ph_no = preferences.getString(USER_RELATIVE_PHONE_NUMBER_KEY, null);
		String occ = preferences.getString(USER_OCCUPATION_KEY, null);
		String address = preferences.getString(USER_ADDRESS_KEY, null);
		List<String> data = new ArrayList<>();
		data.add(name);data.add(email);data.add(nick_name);data.add(ps_nick_name);data.add(pin_code);
		data.add(dob);data.add(ph_no);data.add(rel_ph_no);data.add(occ);data.add(address);
		for(String field : data){
			if(field != null && !TextUtils.isEmpty(field) && !field.isEmpty()){
				percent+=10;
			}
		}
		return percent;
	}
	
	public void updateWithCloud(String UID){
		SharedPreferences.Editor editor = preferences.edit();
		FireStoreUtil.getUserDocumentReference(context, UID).get().addOnSuccessListener(snapshot -> {
			editor.putString(USER_NAME_KEY, snapshot.getString(FireStoreUtil.USER_NAME));
			editor.putString(USER_PHONE_NO, snapshot.getString(FireStoreUtil.USER_PHONE_NUMBER));
			editor.putString(USER_EMAIL_KEY, snapshot.getString(FireStoreUtil.USER_EMAIL));
			editor.putString(USER_PIN_CODE_KEY, snapshot.getString(FireStoreUtil.USER_PIN_CODE));
			editor.putString(USER_DOB_KEY, snapshot.getString(FireStoreUtil.USER_DOB));
			editor.putString(USER_PAY_ID, snapshot.getString(FireStoreUtil.USER_PAY_ID));
			editor.putString(USER_NICK_NAME_KEY, snapshot.getString(FireStoreUtil.USER_NICK_NAME));
			editor.putString(USER_PS_NICK_NAME_KEY, snapshot.getString(FireStoreUtil.USER_PS_NICK_NAME));
			editor.putString(USER_ADDRESS_KEY, snapshot.getString(FireStoreUtil.USER_ADDRESS));
			editor.putString(USER_OCCUPATION_KEY, snapshot.getString(FireStoreUtil.USER_OCCUPATION));
			editor.apply();
		});
	}
	
	public SharedPreferences getPreferences(){
		return preferences;
	}
}
