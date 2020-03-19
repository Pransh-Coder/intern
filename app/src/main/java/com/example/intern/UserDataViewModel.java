package com.example.intern;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserDataViewModel  extends ViewModel {
	private final static String TAG = UserDataViewModel.class.getSimpleName();
	
	private  String userName;
	private  String userID;
	private  String userEmail;
	private  String userContact;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserContact() {
		return userContact;
	}
	
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	
	
	private static List<String> bankList = new ArrayList<>();
	
	public static List<String> getBankListFromJSON(String json){
		try{
			JSONObject response = new JSONObject(json);
			JSONObject netBankingList = response.getJSONObject("netbanking");
			Iterator<String> iterator = netBankingList.keys();
			while(iterator.hasNext()){
				bankList.add(netBankingList.getString(iterator.next()));
			}
		}catch (Exception e ){
			Log.d(TAG, "getBankListFromJSON: error parsing JSON");
		}finally {
			if(bankList != null){
				return bankList;
			}else{
				bankList.add("Cannot Load Banks");
				return bankList;
			}
		}
		
	}
}
