package com.example.intern.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {
	private static String SHARED_PREF_NAME = "user_prefs";
	private static String USER_UID_KEY = "user_uid";
	private static String USER_NAME_KEY = "user_name";
	private static String USER_NICK_NAME_KEY = "user_nick_name";
	private static String USER_PS_NICK_NAME_KEY = "user_ps_nick_name";
	private static String USER_DOB_KEY = "user_DOB";
	private static String USER_EMAIL_KEY = "user_email";
	private static String USER_PIN_CODE_KEY  = "user_pin_code";
	private static String USER_PAY_ID = "user_pay_id";
	private static String USER_LOGGED_IN_STATUS_KEY = "user_log_in_status";
	private static String USER_RELATIVE_PHONE_NUMBER_KEY = "user_relative_ph_no";
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
				.putBoolean(USER_LOGGED_IN_STATUS_KEY, true).putString(USER_PAY_ID, null);
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
}
