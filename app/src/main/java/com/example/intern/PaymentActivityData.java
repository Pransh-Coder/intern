package com.example.intern;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PaymentActivityData {
	private final static String TAG = PaymentActivityData.class.getSimpleName();
	
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
