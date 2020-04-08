package com.example.intern.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
				.putString(USER_PHONE_NO, null).putBoolean(USER_PAY_VER_STATUS , false);
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
		Map map = preferences.getAll();
		Set keySet = map.keySet();
		Iterator iterator = keySet.iterator();
		while(iterator.hasNext()){
			if(map.get(iterator.next()) != null){
				percent += 10;
			}
		}
		return percent;
	}
	
	public SharedPreferences getPreferences(){
		return preferences;
	}
}
